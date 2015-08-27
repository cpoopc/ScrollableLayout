package com.cpoopc.scrollablelayoutlib;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ScrollView;

/**
 * Created by cpoopc on 2015-02-10.
 */
public class ScrollableHelper {

    private ScrollableContainer mCurrentScrollableCainer;

    public interface ScrollableContainer{
        View getScrollableView();
    }

    public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer) {
        this.mCurrentScrollableCainer = scrollableContainer;
    }

    private View getScrollableView() {
        if (mCurrentScrollableCainer == null) {
            return null;
        }
        return mCurrentScrollableCainer.getScrollableView();
    }

    public boolean isTop() {
        View scrollableView = getScrollableView();
        if (scrollableView == null) {
            return false;
        }
        if (scrollableView instanceof AdapterView) {
            return isAdapterViewTop((AdapterView) scrollableView);
        }
        return false;
    }

    private static boolean isAdapterViewTop(AdapterView adapterView){
        if(adapterView != null){
            int firstVisiblePosition = adapterView.getFirstVisiblePosition();
            View childAt = adapterView.getChildAt(0);
            if(childAt == null || (firstVisiblePosition == 0 && childAt != null && childAt.getTop() == 0)){
                return true;
            }
        }
        return false;
    }

    public void smoothScrollBy(int distance, int duration) {
        View scrollableView = getScrollableView();
        if (scrollableView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) scrollableView;
            absListView.smoothScrollBy(distance, duration);
        }else if (scrollableView instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) scrollableView;
            scrollView.smoothScrollBy(0, distance);
        }

    }


}
