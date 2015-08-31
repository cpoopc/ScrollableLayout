package com.test.cp.myscrolllayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ScrollViewFragment extends ScrollAbleFragment implements ScrollableHelper.ScrollableContainer{

    private ScrollView scrollView;

    public static ScrollViewFragment newInstance() {
        ScrollViewFragment scrollViewFragment = new ScrollViewFragment();
        return scrollViewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrollview, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.scrollview);
        return view;
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }
}
