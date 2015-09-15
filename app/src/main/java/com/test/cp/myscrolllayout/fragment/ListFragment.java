package com.test.cp.myscrolllayout.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.test.cp.myscrolllayout.adapter.MyAdapter;
import com.test.cp.myscrolllayout.R;
import com.test.cp.myscrolllayout.fragment.base.ScrollAbleFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListFragment extends ScrollAbleFragment implements ScrollableHelper.ScrollableContainer{

    private ListView mListview;

    public static ListFragment newInstance() {
        ListFragment listFragment = new ListFragment();
        return listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mListview = (ListView) view.findViewById(R.id.listview);
        List<String> strlist = new ArrayList<String>();
//        for (int i = 0; i < 99; i++) {
        for (int i = 0; i < new Random().nextInt(100) + 31; i++) {
            strlist.add(String.valueOf(i));
        }
        mListview.setAdapter(new MyAdapter(getActivity(), strlist));
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "点击item" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public View getScrollableView() {
        return mListview;
    }
}
