package com.test.cp.myscrolllayout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.cp.myscrolllayout.R;
import com.test.cp.myscrolllayout.constant.ColorsConstant;

import java.util.List;

/**
 * Created by cpoopc on 2015-02-10.
 */
public class MyAdapter extends BaseAdapter {

    private List<String> strList;
    private Context mContext;

    public MyAdapter( Context mContext,List<String> strList) {
        this.strList = strList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null, false);
        convertView.setBackgroundColor(mContext.getResources().getColor(ColorsConstant.colors[position % ColorsConstant.colors.length]));
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(strList.get(position));
        return convertView;
    }
}
