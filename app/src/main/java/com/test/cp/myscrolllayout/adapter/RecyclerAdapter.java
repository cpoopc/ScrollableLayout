package com.test.cp.myscrolllayout.adapter;/**
 * Created by cpoopc on 2015/8/31.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.test.cp.myscrolllayout.R;
import com.test.cp.myscrolllayout.constant.ColorsConstant;

import java.util.List;

/**
 * User: cpoopc
 * Date: 2015-08-31
 * Time: 12:53
 * Ver.: 0.1
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.SimpleViewHolder> {

    private Context mContext;
    private List<String> strList;

    public RecyclerAdapter(Context mContext, List<String> strList) {
        this.mContext = mContext;
        this.strList = strList;
    }

    @Override
    public RecyclerAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SimpleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        simpleViewHolder.bindData(strList.get(i),mContext.getResources().getColor(ColorsConstant.colors[i % ColorsConstant.colors.length]),i);
    }

    @Override
    public int getItemCount() {
        return strList.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView tv;
        int position;
        public SimpleViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv = (TextView) itemView.findViewById(R.id.tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "点击RecycleView item" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bindData(String txt,int bgcolor,int position) {
            this.position = position;
            tv.setText(txt);
            itemView.setBackgroundColor(bgcolor);
        }
    }
}
