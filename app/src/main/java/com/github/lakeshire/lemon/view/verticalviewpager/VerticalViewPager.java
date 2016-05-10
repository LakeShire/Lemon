package com.github.lakeshire.lemon.view.verticalviewpager;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nali on 2016/5/4.
 */
public class VerticalViewPager extends ViewGroup {

    private Context mContext;

    private Scroller mScroller;
    private int mLastY;
    private VelocityTracker mVelocityTracker;
    private int mPageHeight;
    private int mCurrentPage;
    private PagerAdapter mAdapter;
    private int mItemCount;
    private Map<Integer, RelativeLayout> mViews = new HashMap<>();
    private int mPressY;
    private int mOffScreenLimit = 1;
    private Map<Integer, Object> mObjects = new HashMap<>();

    private class PagerObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            dataSetChanged();
        }
        @Override
        public void onInvalidated() {
            dataSetChanged();
        }
    }
    private PagerObserver mObserver;

    public VerticalViewPager(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mContext = context;
    }

    public VerticalViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int top = 0;
//        for (int i = 0; i < getChildCount(); i++) {
//            View view = this.getChildAt(i);
//            view.layout(getLeft(), top, getRight(), top + mPageHeight);
//            top += mPageHeight;
//        }
        for (Map.Entry<Integer, RelativeLayout> entry : mViews.entrySet()) {
            entry.getValue().layout(getLeft(), getTop() + mPageHeight * entry.getKey(), getRight(), getTop() + (entry.getKey() + 1) * mPageHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);

        //  这个一定要啊 要不然子元素都不知道自己多大
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);
        return widthSize;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
        mPageHeight = heightSize;
        return heightSize * mItemCount;
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public void computeScroll() {
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                postInvalidate();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = true;
        int action = ev.getAction();
        int y = (int) ev.getY();
        int deltaY;

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPressY = y;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                deltaY = y - mLastY;
                mLastY = y;
                if (getScrollY() - deltaY > 0 && getScrollY() - deltaY < (mPageHeight * (mItemCount - 1))) {
                    scrollBy(0, -deltaY);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                deltaY = y - mLastY;
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityY = (int) velocityTracker.getYVelocity() ;
                if (Math.abs(velocityY) > 1000) {
                    int moveY = 0;
                    if (velocityY < 0) {
                        if (mCurrentPage < mItemCount - 1) {
                            moveY = (mCurrentPage + 1) * mPageHeight - getScrollY();
                            mCurrentPage++;
//                            Logger.d("向下快速切页，当前页" + mCurrentPage);
                            setCurrentPage(mCurrentPage, true);
                        }
                    } else if (velocityY > 0) {
                        if (mCurrentPage > 0) {
                            moveY = (mCurrentPage - 1) * mPageHeight - getScrollY();
                            mCurrentPage--;
                            setCurrentPage(mCurrentPage, true);
//                            Logger.d("向上快速切页，当前页" + mCurrentPage);
                        }
                    }

                    mScroller.startScroll(0, getScrollY(), 0, moveY);
                } else {
                    int moveY = 0;
                    if (y - mPressY < 0) {
                        if (y - mPressY > - mPageHeight * 0.5) {
//                            Logger.d("上拉不足半屏回弹");
                            moveY = mCurrentPage * mPageHeight - getScrollY();
                        } else {
                            if (mCurrentPage < mItemCount - 1) {
//                                Logger.d("上拉切下一页");
                                moveY = (mCurrentPage + 1) * mPageHeight - getScrollY();
                                mCurrentPage++;
                                setCurrentPage(mCurrentPage, true);
                            }
                        }
                    } else {
                        if (y - mPressY < mPageHeight * 0.5) {
//                            Logger.d("下拉不足半屏回弹");
                            moveY = mCurrentPage * mPageHeight - getScrollY();
                        } else {
                            if (mCurrentPage > 0) {
//                                Logger.d("下拉切上一页");
                                moveY = (mCurrentPage - 1) * mPageHeight - getScrollY();
                                mCurrentPage--;
                                setCurrentPage(mCurrentPage, true);
                            }
                        }
                    }
                    mScroller.startScroll(0, getScrollY(), 0, moveY, Math.abs(moveY));

                }
                invalidate();
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                break;
        }
        return result;
    }

    public void setAdapter(PagerAdapter adapter) {
        mAdapter = adapter;
        if (mAdapter != null) {
            setCount(mAdapter.getCount());
            setCurrentPage(0, false);
            requestLayout();
            mAdapter.finishUpdate(null);
        }
    }

    public void setOffScreenLimit(int limit) {
        mOffScreenLimit = limit;
    }

    private void loadPage() {
        int from = mCurrentPage - mOffScreenLimit < 0 ? 0 : mCurrentPage - mOffScreenLimit;
        int to = mCurrentPage + mOffScreenLimit > mItemCount - 1 ? mItemCount - 1 : mCurrentPage + mOffScreenLimit;

        int i;
        for (i = 0; i < from; i++) {
            if (mViews.containsKey(i)) {
                mAdapter.destroyItem(null, i, mObjects.get(i));
//                ((Fragment) mObjects.get(i)).onDestroy();
                removeView(mViews.get(i));
                mViews.remove(i);
                mObjects.remove(i);
                Logger.d("移除第" + i + "页");
            }
        }

        for (i = to + 1; i < mItemCount; i++) {
            if (mViews.containsKey(i)) {
                mAdapter.destroyItem(null, i, mObjects.get(i));
//                ((Fragment) mObjects.get(i)).onDestroy();
                removeView(mViews.get(i));
                mViews.remove(i);
                mObjects.remove(i);
                Logger.d("移除第" + i + "页");
            }
        }

        for (i = from; i <= to; i++) {
            if (!mViews.containsKey(i)) {
                RelativeLayout vp = new RelativeLayout(mContext);
                vp.setId(256 + i);
                //  设置一下容器的布局参数
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                vp.setLayoutParams(lp);
                Object object = mAdapter.instantiateItem(vp, i);
                mObjects.put(i, object);
                mViews.put(i, vp);
                addView(vp);
                Logger.d("载入第" + i + "页");
            }
        }
        requestLayout();
        mAdapter.finishUpdate(null);
    }

    private void setCurrentPage(int page, boolean smooth) {
        mCurrentPage = page;
        loadPage();
        if (smooth) {

        } else {
            scrollTo(0, page * mPageHeight);
        }
    }

    public void setCurrentPage(int page) {
        setCurrentPage(page, false);
    }

    void dataSetChanged() {
    }

    public void setCount(int count) {
        mItemCount = count;
    }
}
