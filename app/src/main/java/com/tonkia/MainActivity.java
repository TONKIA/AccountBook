package com.tonkia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.tonkia.fragments.DealFragment;
import com.tonkia.fragments.DetailFragment;
import com.tonkia.fragments.SelfFragment;
import com.tonkia.fragments.TableFragment;
import com.tonkia.utils.AudioRecoderUtils;
import com.tonkia.utils.TimeUtils;
import com.tonkia.vo.AudioInfo;

import org.litepal.LitePal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;


public class MainActivity extends AppCompatActivity {

    private SpaceNavigationView spaceNavigationView;
    //Fragment
    private FragmentManager fragmentManager;
    private Fragment from;
    private DealFragment dealFragment;
    private DetailFragment detailFragment;
    private SelfFragment selfFragment;
    private TableFragment tableFragment;
    private AudioRecoderUtils mAudiorecoder;
    private float mPosY;
    private float mCurPosY;
    private float moveDis = 300;
    private FloatingActionButton centerBtn;

    private ImageView bindBadge;
    private BadgeView badgeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //处理Fragment的切换
        fragmentManager = getSupportFragmentManager();
        //初始打开的Fragment
        from = fragmentManager.findFragmentById(R.id.fragment_content);
        detailFragment = (DetailFragment) from;
        //init bottomNavigation
        spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.navigation_detail), R.drawable.ic_event_note_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.navigation_table), R.drawable.ic_call_missed_outgoing_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.navigation_deal), R.drawable.ic_access_time_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.navigation_self), R.drawable.ic_person_black_24dp));
        //设置点击/长按监听
        spaceNavigationView.setSpaceOnClickListener(new MySpaceOnClickListener());
        spaceNavigationView.setSpaceOnLongClickListener(new MySpaceOnLongClickListener());
        //设置中间button的颜色
        spaceNavigationView.setCentreButtonRippleColor(ContextCompat.getColor(this, R.color.centerBtnClick));
        //只显示ICON
        spaceNavigationView.showIconOnly();

        mAudiorecoder = new AudioRecoderUtils();
        mAudiorecoder.setOnAudioStatusUpdateListener(new MyAudioListener());
        //需要先初始化deal的badge


        bindBadge = findViewById(R.id.badge);

        badgeView = BadgeFactory.create(this)
                .setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setWidthAndHeight(18, 18)
                .setBadgeBackground(ContextCompat.getColor(this, R.color.colorPrimary))
                .setTextSize(8)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBadgeCount(0)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .bind(bindBadge);
        freshDeal();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);


    }

    private class MySpaceOnClickListener implements SpaceOnClickListener {
        @Override
        public void onCentreButtonClick() {
            Intent i = new Intent(MainActivity.this, AddActivity.class);
            startActivityForResult(i, 0);

        }

        @Override
        public void onItemClick(int itemIndex, String itemName) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (itemIndex) {
                case 0:
                    replaceFragment(detailFragment, transaction);
                    break;
                case 1:
                    if (tableFragment == null) {
                        tableFragment = new TableFragment();
                        transaction.add(R.id.mainContent, tableFragment);
                    }
                    replaceFragment(tableFragment, transaction);
                    break;
                case 2:
                    if (dealFragment == null) {
                        dealFragment = new DealFragment();
                        transaction.add(R.id.mainContent, dealFragment);
                    }
                    replaceFragment(dealFragment, transaction);
                    break;
                case 3:
                    if (selfFragment == null) {
                        selfFragment = new SelfFragment();
                        transaction.add(R.id.mainContent, selfFragment);
                    }
                    replaceFragment(selfFragment, transaction);
                    break;
            }
        }

        @Override
        public void onItemReselected(int itemIndex, String itemName) {

        }
    }

    //切换Fragment
    private void replaceFragment(Fragment to, FragmentTransaction transaction) {
        transaction.hide(from).show(to).commit();
        from = to;
    }

    private class MySpaceOnLongClickListener implements SpaceOnLongClickListener {
        @Override
        public void onCentreButtonLongClick() {
            //-----------------------长按开始录音---------------------------
            if (centerBtn == null)
                initMoveListener();
            if (Build.VERSION.SDK_INT > 22) {
                permissionForM();
            } else {
                //开始录音菜监听
                startAudioRecord();
            }
        }

        @Override
        public void onItemLongClick(int itemIndex, String itemName) {

        }
    }

    private void permissionForM() {
        //先检查权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        } else {
            //开始录音菜监听
            startAudioRecord();
        }

    }

    //反射找到中间Button
    private void initMoveListener() {
        try {
            Field field = SpaceNavigationView.class.getDeclaredField("centreButton");
            field.setAccessible(true);
            centerBtn = (FloatingActionButton) field.get(spaceNavigationView);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //录音MoveTouch监听
    class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPosY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mCurPosY = motionEvent.getY();

                    if (mCurPosY - mPosY < 0
                            && (Math.abs(mCurPosY - mPosY) > moveDis)) {
                        tvTip.setText("手指松开取消");
                    } else {
                        tvTip.setText("手指上滑取消");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mCurPosY = motionEvent.getY();
                    if (mCurPosY - mPosY < 0
                            && (Math.abs(mCurPosY - mPosY) > moveDis)) {
                        Toast.makeText(MainActivity.this, "语音备注取消", Toast.LENGTH_SHORT).show();
                        mAudiorecoder.cancelRecord();
                    } else {
                        mAudiorecoder.stopRecord();
                    }
                    centerBtn.setOnTouchListener(null);
                    bdAudio.dismiss();
                    break;
            }
            return true;
        }
    }

    private TextView tvTime;
    private TextView tvTip;
    private VoiceLineView voiceView;
    BottomDialog bdAudio;

    private void startAudioRecord() {
        bdAudio = BottomDialog.create(getSupportFragmentManager());
        bdAudio.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                tvTime = v.findViewById(R.id.tv_time);
                tvTip = v.findViewById(R.id.tv_tip);
                voiceView = v.findViewById(R.id.voicLine);
                //先初始化控件再监听
                centerBtn.setOnTouchListener(new MyTouchListener());
            }
        }).setLayoutRes(R.layout.dialog_record).setDimAmount(0.5f).setCancelOutside(false).setTag("AudioDialog").show();
        //everything is ok 后再录音
        mAudiorecoder.startRecord();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //添加record后返回
        if (resultCode == 1) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            replaceFragment(detailFragment, transaction);
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            detailFragment.setDate(year, month);
            //致命bug
            if (tableFragment != null)
                tableFragment.freshChart();
            //反射大法好  厉害厉害  哈哈哈哈哈哈哈哈
            try {
                Method method = SpaceNavigationView.class.getDeclaredMethod("updateSpaceItems", int.class);
                method.setAccessible(true);
                method.invoke(spaceNavigationView, 0);
                System.out.println(method);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if (requestCode == 0) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //授权成功
//            }
//            return;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


    class MyAudioListener implements AudioRecoderUtils.OnAudioStatusUpdateListener {
        @Override
        public void onUpdate(double db, long time) {
            tvTime.setText(TimeUtils.recordTime(time));
            voiceView.setVolume((int) db - 40);
        }

        @Override
        public void onStop(String filePath) {
            AudioInfo ai = new AudioInfo();
            ai.setPath(filePath);
            long time = System.currentTimeMillis();
            ai.setTime(time);
            ai.save();
            freshDeal();
        }
    }

    public void freshDeal() {
        List<AudioInfo> mList = LitePal.findAll(AudioInfo.class);
        Collections.reverse(mList);
        int count = mList.size();
        if (count > 0) {
            setBadge(count);
        } else {
            clearBadge();
        }
        if (dealFragment != null) {
            dealFragment.freshList(mList);
        }
    }

    private void clearBadge() {
        badgeView.setVisibility(View.INVISIBLE);
    }

    private void setBadge(int count) {
        //Toast.makeText(this, "Badge" + count, Toast.LENGTH_SHORT).show();
        badgeView.setVisibility(View.VISIBLE);
        badgeView.setBadgeCount(count);
    }

}
