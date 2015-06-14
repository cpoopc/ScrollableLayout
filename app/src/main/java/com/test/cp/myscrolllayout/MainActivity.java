package com.test.cp.myscrolllayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.test.cp.myscrolllayout.widget.Helper;
import com.test.cp.myscrolllayout.widget.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends ActionBarActivity {

    private List<Fragment> mFragmentList;
    private ScrollableLayout mScrollLayout;

    class MyAdapter extends PagerAdapter{

        int[] images = new int[]{R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};

        List<ImageView> imageViews = new ArrayList<ImageView>();

        MyAdapter() {
            for (int i = 0;i<images.length;i++){
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(50,50));
                imageView.setImageResource(images[i]);
                final int index = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"im android "+index,Toast.LENGTH_SHORT).show();
                    }
                });
                imageViews.add(imageView);
            }
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position),0);
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPager vpImage = (ViewPager) findViewById(R.id.imagepager);
        vpImage.setAdapter(new MyAdapter());

        mScrollLayout = (ScrollableLayout) findViewById(R.id.scrollableLayout);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ListFragment(0));
        mFragmentList.add(new ListFragment(1));
        mFragmentList.add(new ListFragment(2));

        List<String> titleList = new ArrayList<>();
        titleList.add("tab0");
        titleList.add("tab1");
        titleList.add("tab2");
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList, titleList));
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerStrip);
        pagerSlidingTabStrip.setViewPager(viewPager);
//        Helper.instance.setListView(((ListFragment) mFragmentList.get(0)).getListView());
        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.e("onPageSelected","page:"+i);
                Helper.instance.setListView(((ListFragment) mFragmentList.get(i)).getListView());
                Helper.instance.setCurrentPage(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(0);
    }

}
