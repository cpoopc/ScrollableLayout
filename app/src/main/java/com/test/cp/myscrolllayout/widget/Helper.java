package com.test.cp.myscrolllayout.widget;

import android.view.View;
import android.widget.ListView;

/**
 * Created by Administrator on 2015-02-11.
 */
public enum  Helper {
    instance;

    private ListView mListView;

    public boolean isTop(){
        if(mListView != null){
            int firstVisiblePosition = mListView.getFirstVisiblePosition();
            View childAt = mListView.getChildAt(0);
            if(firstVisiblePosition == 0 && childAt != null && childAt.getTop() == 0){
                return true;
            }
        }
        return false;
    }

    public void setListView(ListView listView){
        this.mListView = listView;
    }

    public ListView getListView(){
        return mListView;
    }

}
