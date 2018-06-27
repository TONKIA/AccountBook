package com.tonkia;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tonkia.fragments.AddFragment;
import com.tonkia.fragments.DealFragment;
import com.tonkia.fragments.DetailFragment;
import com.tonkia.fragments.SelfFragment;
import com.tonkia.fragments.TableFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bnv;
    private FragmentManager fragmentManager;
    //Fragment
    private Fragment from;
    private AddFragment addFragment;
    private DealFragment dealFragment;
    private DetailFragment detailFragment;
    private SelfFragment selfFragment;
    private TableFragment tableFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.navigation_detail:
                    replaceFragment(dealFragment, transaction);
                    return true;
                case R.id.navigation_table:
                    if (tableFragment == null) {
                        tableFragment = new TableFragment();
                        transaction.add(R.id.mainContent, tableFragment);
                    }
                    replaceFragment(tableFragment, transaction);
                    return true;
                case R.id.navigation_add:
                    if (addFragment == null) {
                        addFragment = new AddFragment();
                        transaction.add(R.id.mainContent, addFragment);
                    }
                    replaceFragment(addFragment, transaction);
                    return true;
                case R.id.navigation_deal:
                    if (dealFragment == null) {
                        dealFragment = new DealFragment();
                        transaction.add(R.id.mainContent, dealFragment);
                    }
                    replaceFragment(dealFragment, transaction);
                    return true;
                case R.id.navigation_self:
                    if (selfFragment == null) {
                        selfFragment = new SelfFragment();
                        transaction.add(R.id.mainContent, selfFragment);
                    }
                    replaceFragment(selfFragment, transaction);
                    return true;
            }
            return false;
        }
    };

    //切换Fragment
    private void replaceFragment(Fragment to, FragmentTransaction transaction) {
        System.out.println(to + "**********");
        transaction.hide(from).show(to).commit();
        from = to;
    }

    private void init() {
        //处理底部菜单
        bnv = findViewById(R.id.navigation);
        bnv.setOnNavigationItemSelectedListener(mListener);

        //处理Fragment的切换
        fragmentManager = getSupportFragmentManager();
        //初始打开的Fragment
        from = fragmentManager.findFragmentById(R.id.fragment_content);
        detailFragment = (DetailFragment) from;
    }
}
