package com.test.cp.myscrolllayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.astuetz.PagerSlidingTabStrip;
import com.test.cp.myscrolllayout.widget.Helper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ListFragment());
        mFragmentList.add(new ListFragment());
        mFragmentList.add(new ListFragment());

        List<String> titleList = new ArrayList<>();
        titleList.add("tab1");
        titleList.add("tab2");
        titleList.add("tab3");
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList, titleList));
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerStrip);
        pagerSlidingTabStrip.setViewPager(viewPager);
        Helper.instance.setListView(((ListFragment) mFragmentList.get(0)).getListView());
        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                Helper.instance.setListView(((ListFragment) mFragmentList.get(i)).getListView());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
//        ScrollView
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        metrics.heightPixels;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
