package com.test.cp.myscrolllayout.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Map;

/**
 * Created by cpoopc on 2015-06-14.
 */
public class ScrollableHelper {

//    int page;
//    Map<Integer, View> viewMap;

    private ScrollableContainer mCurrentScrollableCainer;

    public interface ScrollableContainer{
        public View getScrollableView();
    }

    public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer) {
        this.mCurrentScrollableCainer = scrollableContainer;
    }

//    public void setPageView(int page,View view) {
//        viewMap.put(page, view);
//    }

    public boolean isTop() {
//        View pageView = viewMap.get(page);
        if (mCurrentScrollableCainer == null) {
            return false;
        }
        View scrollableView = mCurrentScrollableCainer.getScrollableView();
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
            if(firstVisiblePosition == 0 && childAt != null && childAt.getTop() == 0){
                return true;
            }
        }
        return false;
    }


}
