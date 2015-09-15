package com.test.cp.myscrolllayout.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.test.cp.myscrolllayout.constant.ColorsConstant;

import java.util.ArrayList;
import java.util.List;

public class MyHeadPicAdapter extends PagerAdapter {

    List<ImageView> imageViews = new ArrayList<ImageView>();

    public MyHeadPicAdapter(Context context) {
        for (int i = 0; i < ColorsConstant.colors.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
            imageView.setBackgroundColor(context.getResources().getColor(ColorsConstant.colors[i]));
            final int index = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "im android " + index, Toast.LENGTH_SHORT).show();
                }
            });
            imageViews.add(imageView);
        }
    }

    @Override
    public int getCount() {
        return ColorsConstant.colors.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position), 0);
        return imageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }
}