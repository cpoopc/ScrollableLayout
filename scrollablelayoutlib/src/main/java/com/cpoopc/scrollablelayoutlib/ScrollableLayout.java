/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2015 cpoopc
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.cpoopc.scrollablelayoutlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by cpoopc(303727604@qq.com) on 2015-02-10.
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
    private int mHeadHeight;
    private int mScrollY;
    private View mHeadView;
    private int mExpandHeight = 0;
    private int sysVersion;
    private ViewPager childViewPager;
    private boolean flag1,flag2;
    private int mLastScrollerY;

    /**
     * 滑动方向 *
     */
    enum DIRECTION {
        UP,// 向上划
        DOWN// 向下划
    }

    private int minY = 0;
    private int maxY = 0;

    private int mCurY;
    private boolean isClickHead;
    private boolean isClickHeadExpand;

    private ScrollableHelper mHelper;

    public ScrollableHelper getHelper() {
        return mHelper;
    }

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
        mHelper = new ScrollableHelper();
        mScroller = new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        sysVersion = Build.VERSION.SDK_INT;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        float deltaY;
        int shiftX;
        int shiftY;
        shiftX = (int) Math.abs(currentX - mDownX);
        shiftY = (int) Math.abs(currentY - mDownY);
//        initVelocityTrackerIfNotExists();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag1 = true;
                flag2 = true;
                mDownX = currentX;
                mDownY = currentY;
                mLastX = currentX;
                mLastY = currentY;
                mScrollY = getScrollY();
                checkIsClickHead((int) currentY, mHeadHeight, getScrollY());
                checkIsClickHeadExpand((int) currentY, mHeadHeight, getScrollY());
//                Log.d(tag, "isClickHead:" + isClickHead);
//                Log.d(tag, "checkIsClickHeadExpand:" + isClickHeadExpand);
//                Log.d(tag, "ACTION_DOWN__mDownY:" + mDownY);
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(ev);
                deltaY = mLastY - currentY;
//                Log.d(tag, "mLastY:" + mLastY+"      currentY:" + currentY+"      deltaY:" + deltaY+"      shiftY:" + shiftY
//                        +"      mTouchSlop:" + mTouchSlop+"      shiftX:" + shiftX);
//                Log.d(tag, "deltaY:" + deltaY);
                if (flag1) {
                    if (shiftX > mTouchSlop && shiftX > shiftY) {
                        flag1 = false;
                        flag2 = false;
                    } else if (shiftY > mTouchSlop && shiftY > shiftX) {
                        flag1 = false;
                        flag2 = true;
                    }
                }
                
                if (flag2 && shiftY > mTouchSlop && shiftY > shiftX && (!isSticked() || mHelper.isTop() || isClickHeadExpand)) {

                    if (childViewPager != null) {
                        childViewPager.requestDisallowInterceptTouchEvent(true);
                    }
                    scrollBy(0, (int) (deltaY + 0.5));
                }
                mLastX = currentX;
                mLastY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                if (flag2 && shiftY > shiftX && shiftY > mTouchSlop) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    float yVelocity = -mVelocityTracker.getYVelocity();
//                    Log.d(tag, "ACTION:" + (ev.getAction() == MotionEvent.ACTION_UP ? "UP" : "CANCEL"));
                    if (Math.abs(yVelocity) > mMinimumVelocity) {
                        mDirection = yVelocity > 0 ? DIRECTION.UP : DIRECTION.DOWN;
                        if (mDirection == DIRECTION.UP && isSticked()) {
                        } else {
                            mScroller.fling(0, getScrollY(), 0, (int) yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                            mScroller.computeScrollOffset();
                            mLastScrollerY = getScrollY();
//                            Log.d(tag, "StartFling1 yVelocity:" + yVelocity + " duration:" + mScroller.getDuration());
//                            Log.d(tag, "StartFling2 ScrollY():" + getScrollY() + "->FinalY:" + mScroller.getFinalY() + ",mScroller.curY:" + mScroller.getCurrY());
                            invalidate();
                        }
                    }
//                    if ((shiftX > mTouchSlop || shiftY > mTouchSlop)) {
                    if (isClickHead || !isSticked()) {
                        int action = ev.getAction();
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        boolean dd = super.dispatchTouchEvent(ev);
                        ev.setAction(action);
                        return dd;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
//                Log.d(tag, "ACTION:" + (ev.getAction() == MotionEvent.ACTION_UP ? "UP" : "CANCEL"));
                if (flag2 && isClickHead && (shiftX > mTouchSlop || shiftY > mTouchSlop)) {
//                    Log.d(tag, "ACTION_CANCEL isClickHead");
                    int action = ev.getAction();
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    boolean dd = super.dispatchTouchEvent(ev);
//                    Log.d(tag, "super.dispatchTouchEvent(ACTION_CANCEL):" + dd);
                    ev.setAction(action);
                    return dd;
                }
                break;
            default:
                break;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private int getScrollerVelocity(int distance, int duration) {
        if (mScroller == null) {
            return 0;
        } else if (sysVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return (int) mScroller.getCurrVelocity();
        } else {
            return distance / duration;
        }
    }

    @Override
    public void computeScroll() {
//        Log.d(tag, "computeScroll()");
        if (mScroller.computeScrollOffset()) {
            final int currY = mScroller.getCurrY();
            if (mDirection == DIRECTION.UP) {
                // 手势向上划
                if (isSticked()) {
                    int distance = mScroller.getFinalY() - currY;
                    int duration = calcDuration(mScroller.getDuration(), mScroller.timePassed());
                    mHelper.smoothScrollBy(getScrollerVelocity(distance, duration), distance, duration);
                    mScroller.forceFinished(true);
//                    Log.d(tag, "computeScroll finish. post smoothScrollBy");
                    return;
                } else {
                    scrollTo(0, currY);
//                    Log.d(tag, "scrollTo: " + currY);
                }
            } else {
                // 手势向下划
                if (mHelper.isTop() || isClickHeadExpand) {
                    int deltaY = (currY - mLastScrollerY);
                    int toY = getScrollY() + deltaY;
//                    Log.e(tag, "toY " + toY);
                    scrollTo(0, toY);
                    if (mCurY <= minY) {
                        mScroller.forceFinished(true);
                        return;
                    }
//                    Log.d(tag, "scrollBy: " + (currY - mLastScrollerY));
                }
                invalidate();
            }
            mLastScrollerY = currY;
//        } else {
//            Log.d(tag, "computeScroll finish");
        }
    }

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
//        Log.v(tag, "scrollBy Y:" + y);
        super.scrollBy(x, y);
    }


    public boolean isSticked() {
//        Log.d(tag, "isSticked = " + (mCurY == maxY));
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

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void checkIsClickHead(int downY, int headHeight, int scrollY) {
        isClickHead = downY + scrollY <= headHeight;
    }

    private void checkIsClickHeadExpand(int downY, int headHeight, int scrollY) {
        if (mExpandHeight <= 0) {
            isClickHeadExpand = false;
        }
        isClickHeadExpand = downY + scrollY <= headHeight + mExpandHeight;
    }

    /**
     * 扩大头部点击滑动范围
     *
     * @param expandHeight
     */
    public void setClickHeadExpand(int expandHeight) {
        mExpandHeight = expandHeight;
    }

    private int calcDuration(int duration, int timepass) {
        return duration - timepass;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeadView = getChildAt(0);
        measureChildWithMargins(mHeadView, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);
        maxY = mHeadView.getMeasuredHeight();
        mHeadHeight = mHeadView.getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + maxY, MeasureSpec.EXACTLY));
    }

    public int getMaxY() {
        return maxY;
    }

    @Override
    protected void onFinishInflate() {
        if (mHeadView != null && !mHeadView.isClickable()) {
            mHeadView.setClickable(true);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != null && childAt instanceof ViewPager) {
                childViewPager = (ViewPager) childAt;
            }
        }
        super.onFinishInflate();
    }
}
