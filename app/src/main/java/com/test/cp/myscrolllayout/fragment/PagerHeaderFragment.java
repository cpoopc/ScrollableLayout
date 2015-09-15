package com.test.cp.myscrolllayout.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.test.cp.myscrolllayout.R;
import com.test.cp.myscrolllayout.adapter.MyHeadPicAdapter;
import com.test.cp.myscrolllayout.fragment.base.BasePagerFragment;


public class PagerHeaderFragment extends BasePagerFragment {

    private ScrollableLayout mScrollLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_pagerheader, container, false);
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

}
