package com.github.lakeshire.lemon.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.util.ScreenUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by nali on 2016/7/11.
 */
public class PullToZoomView extends ScrollView {

    private Context mContext;
    private View mHeaderView;
    private int mHeaderViewHeight;
    private boolean isFirst = true;
    private int mHeight;
    private boolean isAnimating = false;
    private View mContentView;
    private PtrHandler mPtrHandler;
    private boolean isZoomable = true;
    private boolean isRefreshing = false;
    private PtrUiHandler mPtrUiHandler;

    public PullToZoomView(Context context) {
        super(context);
    }

    public PullToZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PullToZoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //  快速滑动到顶时 有可能滑过头 这里调整一下
        if (getScrollY() < 0) {
            scrollTo(0, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isFirst) {
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
            isFirst = false;
        }

        //  快速滑动到顶时 有可能滑过头 这里调整一下
        if (getScrollY() < 0) {
            scrollTo(0, 0);
        }
    }

    //  分发时的触摸点
    private int mLastInterceptY;
    private int mScrollY;
    private boolean isZoomed = false;

    //  手指按下时的触摸点
    private int mFirstY;
    //  上一个触摸点
    private int mLastY;

    //  拖动状态：不定 上拉 下拉
    private int mPullState = UNKNOWN;
    public static final int UNKNOWN = 0;
    public static final int PULLUP = 1;
    public static final int PULLDOWN = 2;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastInterceptY = y;
                mLastY = y;
                mFirstY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastInterceptY;
                mLastInterceptY = y;
                mLastY = y;

                if (deltaY < 0) {
                    // 上拉
                    mPullState = PULLUP;
                    if (getScrollY() > mHeaderViewHeight) {
                        // 头部全部隐藏：子
                        Logger.d("上拉，头部不可见：子");
                        return false;
                    } else {
                        // 头部为全部隐藏：父
                        Logger.d("上拉，头部可见：父");
                        return true;
                    }
                } else if (deltaY > 0) {
                    // 下拉
                    mPullState = PULLDOWN;
                    if (checkTop()) {
                        mFirstY = y;
                        Logger.d("下拉，头部不可见，列表顶， 父");
                        return true;
                    } else {
                        Logger.d("下拉，头部不可见，子");
                        return false;
                    }
                } else {
                    break;
                }
            }
        // 如果直接返回一个boolean值
        // 会出Invalid pointerId=-1 in onTouchEvent MOVE这句错误
        // 无法正常上拉
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnimating) {
            return true;
        }
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isZoomed = false;
                mLastY = y;
                mFirstY = y;
                mScrollY = getScrollY();
                break;
            case MotionEvent.ACTION_UP:
                if (isZoomed && mPullState == PULLDOWN && !isRefreshing) {
                    mPullState = UNKNOWN;
                    onReleased();
                } else {
                    mPullState = UNKNOWN;
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastY;
                mLastY = y;
                if (mScrollY > 0 && getScrollY() <= 0) {
                    mFirstY = y;
                }
                mScrollY = getScrollY();

                if (mPullState == UNKNOWN) {
                    if (deltaY > 0) {
                        mPullState = PULLDOWN;
                    } else {
                        mPullState = PULLUP;
                    }
                }

                /**
                 * TODO: 当头部没完全消失时 上推至消失 再下拉 本来应该由子View处理
                 * TODO: 但期间手指没有离开过 没有重新分配触摸事件
                 * TODO: 这里判断移动方向变化后重新分配触摸事件 然而还是有点生硬
                 */

                if (mPullState == PULLUP && deltaY > 0 || mPullState == PULLDOWN && deltaY < 0) {
                    scroll(deltaY);
                    this.onInterceptTouchEvent(event);
                    return true;
                }

                if (mPullState == PULLDOWN) {
                    // 下拉
                    if (getScrollY() > 0) {
                        return super.onTouchEvent(event);
                    } else {
                        if (y - mFirstY >= mHeaderViewHeight / 2) {
                            if (!isRefreshing) {
                                isRefreshing = true;
                                uiChangeOnRefreshStart();
                                onRefresh(this);
                            }
//                            return super.onTouchEvent(event);
                        } else {
                            uiChangeOnRefreshPositionChange(y - mFirstY);
                            if (y - mFirstY > 0 && isZoomable) {
                                zoom(y - mFirstY);
                            } else {
                                return super.onTouchEvent(event);
                            }
                        }
//                        if (y - mFirstY > 0 && isZoomable) {
//                            zoom(y - mFirstY);
//                        } else {
//                            return super.onTouchEvent(event);
//                        }
                    }
                } else if (mPullState == PULLUP) {
                    // 上拉
                    if (getScrollY() >= mHeaderViewHeight) {
                        scroll(-deltaY);
                        invalidate();
                    } else if (getScrollY() <= 0) {
                        if (y - mFirstY > 0) {
                            zoom(y - mFirstY);
                        } else {
                            return super.onTouchEvent(event);
                        }
                    } else {
                        return super.onTouchEvent(event);
                    }
                }
                break;
        }
        return true;
    }

    private void zoom(int deltaY) {
        float rate = (float) (deltaY * 0.25 / (float) mHeaderViewHeight);
        int height = (int) ((1 + rate) * mHeaderViewHeight);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mHeaderView.getLayoutParams();
        lp.height = height;
        mHeaderView.setLayoutParams(lp);
        mHeaderView.setScaleY(1 + rate);
        mHeaderView.setScaleX(1 + rate);
        mHeight = height;
        isZoomed = true;
        scrollTo(0, 0);
    }

    private void onReleased() {
        if (isZoomed) {
            ValueAnimator animator = ValueAnimator.ofInt(mHeight, mHeaderViewHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int value = (int) animation.getAnimatedValue();
                    float rate = (float) value / mHeaderViewHeight;
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mHeaderView.getLayoutParams();
                    lp.height = value;
                    mHeaderView.setLayoutParams(lp);
                    mHeaderView.setScaleX(rate);
                    mHeaderView.setScaleY(rate);
                }
            });
            animator.setDuration(mHeight - mHeaderViewHeight);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isZoomed = false;
                    isAnimating = false;
                    isRefreshing = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
            scrollTo(0, 0);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = findViewById(R.id.header);
        mContentView = findViewById(R.id.content);
        int screenHeight =  ScreenUtil.getScreenHeight((Activity) mContext);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        lp.height = screenHeight;
        mContentView.setLayoutParams(lp);
    }

    public void setPtrHandler(PtrHandler ptrHandler) {
        this.mPtrHandler = ptrHandler;
    }
    public void setPtrUiHandler(PtrUiHandler ptrHandler) {
        this.mPtrUiHandler = ptrHandler;
    }
    public interface PtrHandler {
        // 判断内容View是否拉到顶部
        boolean checkTop();
        // 内容View滚动一定距离
        void scroll(int y);
        void onRefresh(PullToZoomView view);
    }

    public interface PtrUiHandler {
        void uiChangeOnRefreshPositionChange(float delta);
        void uiChangeOnRefreshCompleted();
        void uiChangeOnRefreshStart();
    }

    public void onRefreshCompleted() {
        mPullState = UNKNOWN;
        uiChangeOnRefreshCompleted();
        if (isZoomed) {
            onReleased();
        } else {
            isRefreshing = false;
        }
    }

    public void setZoomable(boolean zoom) {
        this.isZoomable = zoom;
    }
    
    public void uiChangeOnRefreshCompleted() {
        if (mPtrUiHandler != null) {
            mPtrUiHandler.uiChangeOnRefreshCompleted();
        }
    }

    public void uiChangeOnRefreshPositionChange(float rate) {
        if (mPtrUiHandler != null) {
            mPtrUiHandler.uiChangeOnRefreshPositionChange(rate);
        }
    }

    public void uiChangeOnRefreshStart() {
        if (mPtrUiHandler != null) {
            mPtrUiHandler.uiChangeOnRefreshStart();
        }
    }

    public boolean checkTop() {
        if (mPtrHandler != null) {
            return mPtrHandler.checkTop();
        } else {
            return true;
        }
    }

    public void scroll(int y) {
        if (mPtrHandler != null) {
            mPtrHandler.scroll(y);
        }
    }

    public void onRefresh(PullToZoomView view) {
        if (mPtrHandler != null) {
            mPtrHandler.onRefresh(view);
        }
    }
}


