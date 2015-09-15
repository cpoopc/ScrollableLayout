package com.test.cp.myscrolllayout.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.nineoldandroids.view.ViewHelper;
import com.test.cp.myscrolllayout.R;
import com.test.cp.myscrolllayout.fragment.base.BasePagerFragment;


public class ParallaxImageHeaderFragment extends BasePagerFragment {

    private ScrollableLayout mScrollLayout;
    private ImageView imageHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_imageheader, container, false);
        ViewPager viewPager = (ViewPager) layout.findViewById(R.id.viewpager);
        imageHeader = (ImageView) layout.findViewById(R.id.imageHeader);
        // ScrollableLayout
        mScrollLayout = (ScrollableLayout) layout.findViewById(R.id.scrollableLayout);
        mScrollLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                ViewHelper.setTranslationY(imageHeader, (float) (currentY * 0.5));
            }
        });
        // 扩展点击头部滑动范围
//        int headHeight = getResources().getDimensionPixelSize(R.dimen.head_height);
//        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
//        mScrollLayout.setClickHeadExpand(headHeight + tabHeight);
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) layout.findViewById(R.id.pagerStrip);
        initFragmentPager(viewPager, pagerSlidingTabStrip, mScrollLayout);
        return layout;
    }

}
