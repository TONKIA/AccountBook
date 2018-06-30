package com.tonkia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tonkia.fragments.add.InputFragment;
import com.tonkia.fragments.add.OutputFragment;

public class AddActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        //设置viewpager和tablayout联动
        tabLayout.setupWithViewPager(viewPager);
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        private final String[] title = new String[]{"收入", "支出"};
        private Fragment[] fragments = new Fragment[]{new InputFragment(), new
                OutputFragment()};

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

    public void addItem(View view) {
        Toast.makeText(this, "增加Item", Toast.LENGTH_SHORT).show();
    }

}
