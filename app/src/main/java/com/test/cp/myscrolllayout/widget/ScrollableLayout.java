package com.test.cp.myscrolllayout.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
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
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    // 方向
    private DIRECTION mDirection;
    private int mHeadHeight = 500;

    enum DIRECTION{
        UP,
        DOWN
    }

    private int minY = 0, maxY = 240;

    private int mCurY;
    private boolean isClickHead;

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
//        mScroller = new OverScroller(context);
        mScroller = new Scroller(context);
//        mScroller = new Scroller(context,new DecelerateInterpolator());
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        Log.d(tag,"mMaximumVelocity:"+mMaximumVelocity);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(tag,"onLayout child:"+getChildAt(0));
        if(getChildAt(0)!=null && !getChildAt(0).isClickable()){
            getChildAt(0).setClickable(true);

        }
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        float deltaY;
        float deltaX;
        int shiftX;
        int shiftY;
//        Log.d(tag, "事件分发:" + ev.getAction());
        initVelocityTrackerIfNotExists();
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = currentX;
                mDownY = currentY;
                mLastX = currentX;
                mLastY = currentY;
                checkIsClickHead((int) currentY, mHeadHeight,getScrollY());
                Log.d(tag,"ACTION_DOWN__mDownY:"+mDownY);
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                deltaY = mLastY - currentY;
                deltaX = mLastX - currentX;
                shiftX = (int) Math.abs(currentX - mDownX);
                shiftY = (int) Math.abs(currentY - mDownY);
                if(shiftY>mTouchSlop && shiftY > shiftX && (!isSticked() || Helper.instance.isTop()) ){
                    deltaY = deltaY * 8 / 10;
                    scrollBy(0, (int) deltaY);
                }
                mLastX = currentX;
                mLastY = currentY;
//                scrollTo(0, (int) deltaY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                deltaX = currentX - mDownX;
                deltaY = currentY - mDownY;
                shiftX = (int) Math.abs(currentX - mDownX);
                shiftY = (int) Math.abs(currentY - mDownY);
                if (shiftY > shiftX) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    float yVelocity = -mVelocityTracker.getYVelocity();
                    Log.d(tag, "ACTION:" + (ev.getAction() == MotionEvent.ACTION_UP ? "UP" : "CANCEL"));
                    if( Math.abs(yVelocity) > mMinimumVelocity){
                        mDirection = yVelocity > 0 ? DIRECTION.UP:DIRECTION.DOWN;
                        mScroller.fling(0, getScrollY(), 0, (int) yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                        mScroller.computeScrollOffset();
                        mLastScrollerY = getScrollY();
                        Log.d(tag, "StartFling1 yVelocity:" + yVelocity + " duration:" + mScroller.getDuration());
                        Log.d(tag, "StartFling2 ScrollY():" + getScrollY() + "->FinalY:" + mScroller.getFinalY() + ",mScroller.curY:" + mScroller.getCurrY());
                        invalidate();
                    }
                }
//                float deltaX2 = currentX - mDownX;
//                float deltaY2 = currentY - mDownY;
//                if(Math.abs(deltaX2)>2 || Math.abs(deltaY2)>2){
//                    ev.setAction(MotionEvent.ACTION_CANCEL);
//                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int mLastScrollerY;

    @Override
    public void computeScroll() {

        Log.d(tag, "computeScroll()");
        if (mScroller.computeScrollOffset()) {
            int currY = mScroller.getCurrY();
            if(mDirection == DIRECTION.UP){
//                Log.d(tag, "isClickHead-->" + isClickHead);
//                if(isClickHead){
                    if(isSticked() && Helper.instance.getListView()!=null){
                        Helper.instance.getListView().smoothScrollBy(mScroller.getFinalY() - currY, calcDuration(mScroller.getDuration(),mScroller.timePassed()));
                        mScroller.forceFinished(true);
                        Log.d(tag, "ListView:smoothScrollBy: " + (mScroller.getFinalY() - currY) + " duration:"+calcDuration(mScroller.getDuration(),mScroller.timePassed()));
                    }else{
                        scrollTo(0, currY);
                        Log.d(tag, "scrollTo: " + currY);
                    }
//                }
//                else{
//                    if(isSticked()){
//                        Helper.instance.getListView().smoothScrollBy(mScroller.getFinalY(), calcDuration(mScroller.getDuration(),mScroller.timePassed()));
//                        mScroller.forceFinished(true);
//                    }else{
//                        scrollTo(0,currY);
//                    }
//                }
            }else{
                if(Helper.instance.isTop()){
//                    Log.d(tag, "cY: "+getScrollY() + " mScroller.getFinalY()-mScroller.getCurrY()-->:" + (mScroller.getFinalY()-mScroller.getCurrY()));
//                    Log.d(tag, "mLastScrollerY: " + mLastScrollerY + " currY:" + currY);
//                    mScroller.startScroll();
//                      scrollTo(0,currY);
//                      Log.d(tag, "isTop scrollTo: " + currY);
                    scrollBy(0,(currY - mLastScrollerY));
                    Log.d(tag, "scrollBy: " + (currY - mLastScrollerY));
                    postInvalidate();
                }else{
                    postInvalidate();
                }

//                Log.d(tag, "mDirection-->:" + mDirection);
            }
            mLastScrollerY = currY;
//            Log.d(tag, "computeScroll-->scroller.Y:" + currY);
        } else {
            Log.d(tag, "computeScroll finish");
        }
        super.computeScroll();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return true;
//    }

    @Override
    public void scrollBy(int x, int y) {
        int scrollY = getScrollY();
        int toY = scrollY + y;
        if (toY >= maxY) {
            toY = maxY;
        } else if (toY <= minY) {
            toY = minY;
        }
        y = toY - scrollY;
        Log.v(tag,"scrollBy Y:"+y);
        super.scrollBy(x, y);
    }


    public boolean isSticked() {
        Log.d(tag,"isSticked = "+ (mCurY == maxY));
        return mCurY == maxY;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y >= maxY) {
            y = maxY;
        } else if (y <= minY) {
            y = minY;
        }
        mCurY = y;
        super.scrollTo(x, y);
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void checkIsClickHead(int downY,int headHeight,int scrollY){
        isClickHead = downY+scrollY<=headHeight;
    }

    private int calcDuration(int duration,int timepass){
        return duration - timepass;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec) + maxY,MeasureSpec.EXACTLY));
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}
