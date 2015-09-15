package com.test.cp.myscrolllayout.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.test.cp.myscrolllayout.fragment.base.ScrollAbleFragment;

import java.util.List;

/**
 * Created by cpoopc on 2015-02-10.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<ScrollAbleFragment> fragmentList;
    private List<String> titleList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<ScrollAbleFragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
