package com.test.cp.myscrolllayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends ActionBarActivity {

    private List<ScrollAbleFragment> mFragmentList;
    private ScrollableLayout mScrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // 头部图片集
        ViewPager vpImage = (ViewPager) findViewById(R.id.imagepager);
        vpImage.setAdapter(new MyHeadPicAdapter(this));

        // ScrollableLayout
        mScrollLayout = (ScrollableLayout) findViewById(R.id.scrollableLayout);
        // 扩展点击头部滑动范围
//        int headHeight = getResources().getDimensionPixelSize(R.dimen.head_height);
//        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
//        mScrollLayout.setClickHeadExpand(headHeight + tabHeight);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(ListFragment.newInstance(0));
        mFragmentList.add(ScrollViewFragment.newInstance(1));
        mFragmentList.add(RecyclerViewFragment.newInstance(2));

        List<String> titleList = new ArrayList<>();
        titleList.add("tab0");
        titleList.add("tab1");
        titleList.add("tab2");
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList, titleList));
        mScrollLayout.getHelper().setCurrentScrollableContainer(mFragmentList.get(0));
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerStrip);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.e("onPageSelected","page:"+i);
                mScrollLayout.getHelper().setCurrentScrollableContainer(mFragmentList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(0);
    }

}
