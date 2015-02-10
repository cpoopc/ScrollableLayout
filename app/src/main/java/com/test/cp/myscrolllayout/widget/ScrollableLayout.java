package com.test.cp.myscrolllayout.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2015-02-10.
 */
public class ScrollableLayout extends LinearLayout {

    private Context context;
    private Scroller mScroller;
    private float mDownX;
    private float mDownY;
    private float mLastX;
    private float mLastY;
    private final String tag = "cp:scrollableLayout";

    public ScrollableLayout(Context context) {
        super(context);
        init(context);
    }

    public ScrollableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        mScroller = new Scroller(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        Log.d(tag, "事件分发:"+ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = currentX;
                mDownY = currentY;
                mLastX = currentX;
                mLastY = currentY;
//                Log.d(tag,"ACTION_DOWN__mDownY:"+mDownY);
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = mLastY - currentY;
                Log.d(tag,"ACTION_MOVE__deltaY:"+deltaY+",currentY:"+currentY);
                int scrollY = getScrollY();
                int toY = (int) (scrollY + deltaY);
                if(toY >= maxY){
                    toY = maxY;
                }else if(toY <= minY){
                    toY = minY;
                }
                Log.d(tag,"ACTION_MOVE__toY:"+toY + ",scrollY:"+scrollY);
                scrollTo(0,toY);
//                scrollBy(0, (int) deltaY);
                mLastX = currentX;
                mLastY = currentY;
//                scrollTo(0, (int) deltaY);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private int minY = 0,maxY = 140;

    @Override
    public void scrollBy(int x, int y) {
        int scrollY = getScrollY();
        int toY = scrollY + y;
        if(toY >= maxY){
            toY = maxY;
        }else if(scrollY <= minY){
            toY = minY;
        }
        y = toY - scrollY;
        super.scrollBy(x, y);
    }
}
