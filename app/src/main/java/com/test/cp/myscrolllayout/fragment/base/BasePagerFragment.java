package com.test.cp.myscrolllayout.fragment.base;/**
 * Created by cpoopc on 2015/9/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.test.cp.myscrolllayout.adapter.MyFragmentPagerAdapter;
import com.test.cp.myscrolllayout.fragment.ListFragment;
import com.test.cp.myscrolllayout.fragment.RecyclerViewFragment;
import com.test.cp.myscrolllayout.fragment.ScrollViewFragment;
import com.test.cp.myscrolllayout.fragment.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: cpoopc
 * Date: 2015-09-15
 * Time: 10:31
 * Ver.: 0.1
 */
public abstract class BasePagerFragment extends Fragment{

    public void initFragmentPager( ViewPager viewPager,PagerSlidingTabStrip pagerSlidingTabStrip, final ScrollableLayout mScrollLayout) {
        final ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();
        fragmentList.add(ListFragment.newInstance());
        fragmentList.add(ScrollViewFragment.newInstance());
        fragmentList.add(RecyclerViewFragment.newInstance());
        fragmentList.add(WebViewFragment.newInstance());

        List<String> titleList = new ArrayList<>();
        titleList.add("ListView");
        titleList.add("ScrollView");
        titleList.add("RecyclerView");
        titleList.add("WebView");
        viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
        mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.e("onPageSelected", "page:" + i);
                /** 标注当前页面 **/
                mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(0);
    }
}
