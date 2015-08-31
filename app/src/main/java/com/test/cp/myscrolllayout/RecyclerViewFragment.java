package com.test.cp.myscrolllayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerViewFragment extends ScrollAbleFragment implements ScrollableHelper.ScrollableContainer{

    private RecyclerView mRecyclerView;

    private int page;

    public void setPage(int page) {
        this.page = page;
    }

    public static RecyclerViewFragment newInstance(int page) {
        RecyclerViewFragment listFragment = new RecyclerViewFragment();
        listFragment.setPage(page);
        return listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        List<String> strlist = new ArrayList<String>();
//        for (int i = 0; i < 99; i++) {
        for (int i = 0; i < new Random().nextInt(100) + 31; i++) {
            strlist.add(String.valueOf(i));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new RecyclerAdapter(getActivity(), strlist));
        return view;
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }
}
