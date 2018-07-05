package com.tonkia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tonkia.fragments.add.InputFragment;
import com.tonkia.fragments.add.OutputFragment;
import com.tonkia.view.StateButton;
import com.tonkia.vo.DealItem;

import me.shaohui.bottomdialog.BottomDialog;

public class AddActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myViewPagerAdapter);
        //设置viewpager和tablayout联动
        tabLayout.setupWithViewPager(viewPager);
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        private final String[] title = new String[]{"支出", "收入"};
        private Fragment[] fragments = new Fragment[]{new OutputFragment(), new
                InputFragment()};

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments[i];
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    public void back(View view) {
        finish();
    }

    //点击添加item
    public void addItem(View view) {
        final BottomDialog bd = BottomDialog.create(getSupportFragmentManager());
        bd.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                final int type = tabLayout.getSelectedTabPosition();
                TextView tv = v.findViewById(R.id.title);
                final EditText et = v.findViewById(R.id.name);
                StateButton btnOk = v.findViewById(R.id.btn_ok);
                StateButton btnCancel = v.findViewById(R.id.btn_cancel);
                String title = type == 0 ? "添加支出项" : "添加收入项";
                tv.setText(title);
                //添加
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = et.getText().toString().trim();
                        if (!TextUtils.isEmpty(name)) {
                            DealItem di = new DealItem();
                            di.setType(type == 0 ? DealItem.OUTPUT : DealItem.INPUT);
                            di.setItemName(name);
                            di.save();
                            notifyChildFragment(type);
                            bd.dismiss();
                        }
                    }
                });
                //退出
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bd.dismiss();
                    }
                });
            }
        }).setLayoutRes(R.layout.dialog_add_item).setDimAmount(0.5f).setCancelOutside(true).setTag("AddItemDialog");
        bd.show();
    }

    private void notifyChildFragment(int type) {
        if (type == 0) {
            OutputFragment outputFragment = (OutputFragment) myViewPagerAdapter.getItem(0);
            outputFragment.initList();
        } else {
            InputFragment inputFragment = (InputFragment) myViewPagerAdapter.getItem(1);
            inputFragment.initList();
        }
    }

}
