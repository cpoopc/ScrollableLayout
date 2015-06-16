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
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by cpoopc on 2015-02-10.
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
    private int mScrollY;
    private View mHeadView;

    /**
     * 滑动方向 *
     */
    enum DIRECTION {
        UP,// 向上划
        DOWN// 向下划
    }

    private int minY = 0;
    private int maxY=0;

    private int mCurY;
    private boolean isClickHead;

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
//        mScroller = new OverScroller(context);
        mScroller = new Scroller(context);
//        mScroller = new Scroller(context,new DecelerateInterpolator());
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        Log.d(tag, "mMaximumVelocity:" + mMaximumVelocity);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(tag, "onLayout child:" + mHeadView);
        if (mHeadView != null && !mHeadView.isClickable()) {
            mHeadView.setClickable(true);
        }
        super.onLayout(changed, l, t, r, b);
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
                mScrollY = getScrollY();
                checkIsClickHead((int) currentY, mHeadHeight, getScrollY());
                Log.d(tag, "ACTION_DOWN__mDownY:" + mDownY);
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                deltaY = mLastY - currentY;
                if (shiftY > mTouchSlop && shiftY > shiftX && (!isSticked() || mHelper.isTop())) {
                    deltaY = deltaY * 9 / 10;
                    scrollBy(0, (int) deltaY);
                }
                mLastX = currentX;
                mLastY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                if (shiftY > shiftX) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    float yVelocity = -mVelocityTracker.getYVelocity();
                    Log.d(tag, "ACTION:" + (ev.getAction() == MotionEvent.ACTION_UP ? "UP" : "CANCEL"));
                    if (Math.abs(yVelocity) > mMinimumVelocity) {
                        mDirection = yVelocity > 0 ? DIRECTION.UP : DIRECTION.DOWN;
                        mScroller.fling(0, getScrollY(), 0, (int) yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                        mScroller.computeScrollOffset();
                        mLastScrollerY = getScrollY();
                        Log.d(tag, "StartFling1 yVelocity:" + yVelocity + " duration:" + mScroller.getDuration());
                        Log.d(tag, "StartFling2 ScrollY():" + getScrollY() + "->FinalY:" + mScroller.getFinalY() + ",mScroller.curY:" + mScroller.getCurrY());
                        invalidate();
                    }
                    if (isClickHead && (shiftX > mTouchSlop || shiftY > mTouchSlop)) {
                        int action = ev.getAction();
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        boolean dd = super.dispatchTouchEvent(ev);
                        ev.setAction(action);
                        return dd;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(tag, "ACTION:" + (ev.getAction() == MotionEvent.ACTION_UP ? "UP" : "CANCEL"));
                if (isClickHead && (shiftX > mTouchSlop || shiftY > mTouchSlop)) {
                    Log.d(tag, "ACTION_CANCEL isClickHead");
                    int action = ev.getAction();
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    boolean dd = super.dispatchTouchEvent(ev);
                    Log.d(tag, "super.dispatchTouchEvent(ACTION_CANCEL):" + dd);
                    ev.setAction(action);
                    return dd;
                }
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
            if (mDirection == DIRECTION.UP) {
                // 手势向上划
                if (isSticked()) {
                    mScroller.forceFinished(true);
                    Log.d(tag, "computeScroll finish. " +
                            "Sticked 交给listview自己滚");
                    return;
                } else {
                    scrollTo(0, currY);
                    Log.d(tag, "scrollTo: " + currY);
                }
            } else {
                // 手势向下划
                if (mHelper.isTop()) {
                    int deltaY = (currY - mLastScrollerY);
                    int toY = getScrollY() + deltaY;
                    Log.e(tag, "toY " + toY);
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
        } else {
            Log.d(tag, "computeScroll finish");
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
        Log.v(tag, "scrollBy Y:" + y);
        super.scrollBy(x, y);
    }


    public boolean isSticked() {
        Log.d(tag, "isSticked = " + (mCurY == maxY));
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

    private void checkIsClickHead(int downY, int headHeight, int scrollY) {
        isClickHead = downY + scrollY <= headHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeadView = getChildAt(0);
        measureChildWithMargins(mHeadView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        maxY = mHeadView.getMeasuredHeight();
        mHeadHeight = mHeadView.getMeasuredHeight();
        Log.e(tag, "onMeasure " + mHeadView.getMeasuredHeight());
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec) + maxY, MeasureSpec.EXACTLY));
    }

}
