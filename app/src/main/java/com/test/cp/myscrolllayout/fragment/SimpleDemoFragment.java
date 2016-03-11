package com.test.cp.myscrolllayout.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.test.cp.myscrolllayout.R;
import com.test.cp.myscrolllayout.adapter.MyFragmentPagerAdapter;
import com.test.cp.myscrolllayout.adapter.MyHeadPicAdapter;
import com.test.cp.myscrolllayout.fragment.base.ScrollAbleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cpoopc
 * @date 2016/3/11
 * @time 11:31
 * @description
 */
public class SimpleDemoFragment extends Fragment {

    private ScrollableLayout mScrollLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_simple, container, false);
        ViewPager viewPager = (ViewPager) layout.findViewById(R.id.viewpager);
        // 头部图片集
        ViewPager vpImage = (ViewPager) layout.findViewById(R.id.imagepager);
        vpImage.setAdapter(new MyHeadPicAdapter(getActivity()));

        // ScrollableLayout
        mScrollLayout = (ScrollableLayout) layout.findViewById(R.id.scrollableLayout);
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) layout.findViewById(R.id.pagerStrip);
        initFragmentPager(viewPager, pagerSlidingTabStrip, mScrollLayout);

        return layout;
    }

    public void initFragmentPager( ViewPager viewPager,PagerSlidingTabStrip pagerSlidingTabStrip, final ScrollableLayout mScrollLayout) {
        final ArrayList<ScrollAbleFragment> fragmentList = new ArrayList<>();
        /** ListFragment实现ScrollableHelper.ScrollableContainer,返回嵌套的listview **/
        fragmentList.add(ListFragment.newInstance());
        fragmentList.add(ListFragment.newInstance());

        List<String> titleList = new ArrayList<>();
        titleList.add("ListView1");
        titleList.add("ListView2");
        viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
        /** 标注当前页面,从而ScrollableLayout可以从该ListFragment取到当前嵌套的listview,剩下的就交给ScrollableLayout去处理了 **/
        mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.e("onPageSelected", "page:" + i);
                /** 页面切换时,标注当前页面,从而ScrollableLayout可以从该ListFragment取到当前嵌套的listview,剩下的就交给ScrollableLayout去处理了 **/
                mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(0);
    }

}
