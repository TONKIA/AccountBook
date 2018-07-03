package com.tonkia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.tonkia.fragments.DealFragment;
import com.tonkia.fragments.DetailFragment;
import com.tonkia.fragments.SelfFragment;
import com.tonkia.fragments.TableFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private SpaceNavigationView spaceNavigationView;
    //Fragment
    private FragmentManager fragmentManager;
    private Fragment from;
    private DealFragment dealFragment;
    private DetailFragment detailFragment;
    private SelfFragment selfFragment;
    private TableFragment tableFragment;

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

    private class MySpaceOnLongClickListener implements SpaceOnLongClickListener {
        @Override
        public void onCentreButtonLongClick() {
            Toast.makeText(MainActivity.this, "long click", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemLongClick(int itemIndex, String itemName) {

        }
    }

    //切换Fragment
    private void replaceFragment(Fragment to, FragmentTransaction transaction) {
        transaction.hide(from).show(to).commit();
        from = to;
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
}
