package com.test.cp.myscrolllayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.test.cp.myscrolllayout.widget.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ListFragment extends Fragment {

    private ListView mListview;

    private int page;

    public ListFragment(int page) {
        this.page = page;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mListview = (ListView) view.findViewById(R.id.listview);
        if(page == Helper.instance.getCurrentPage()){
            Helper.instance.setListView(mListview);
        }
        List<String> strlist = new ArrayList<String>();
//        for (int i = 0; i < 99; i++) {
        for (int i = 0; i < new Random().nextInt(100) + 1; i++) {
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

    public ListView getListView(){
        return mListview;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
