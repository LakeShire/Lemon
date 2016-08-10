package com.github.lakeshire.pulltozoomview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by nali on 2016/7/11.
 */
public class PullToZoomView extends ScrollView {

    private static final String TAG = PullToZoomView.class.getName();

    private Context mContext;
    private View mHeaderView;
    private View mContentView;

    private int mHeaderViewHeight;
    private int mHeight;

    private boolean isFirst = true;
    private boolean isAnimating = false;
    private boolean isZoomable = true;
    private boolean isRefreshing = false;
    private boolean isZoomed = false;
    private boolean isDebugMode = true;
    private boolean isRefreshable = false;

    private PtrHandler mPtrHandler;
    private PtrUiHandler mPtrUiHandler;

    private int mScrollY;
    private int mDownY;
    private int mLastY;
    private int mDeltaY;
    private float mAccuracy = 0.25f;
    private int mRefreshThreshold = 400;

    private int mPullState = UNKNOWN;
    public static final int UNKNOWN = 0;
    public static final int PULL_UP = 1;
    public static final int PULL_DOWN = 2;

    public void setDebugMode(boolean debugMode) {
        isDebugMode = debugMode;
    }

    public void setRefreshable(boolean refreshable) {
        isRefreshable = refreshable;
    }

    public void setZoomable(boolean zoom) {
        this.isZoomable = zoom;
    }

    public void setmRefreshThreshold(int value) {
        this.mRefreshThreshold = value;
    }

    public PullToZoomView(Context context) {
        super(context);
        mContext = context;
    }

    public PullToZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PullToZoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        adjustOnScrollOverTop();
    }

    private void adjustOnScrollOverTop() {
        if (getScrollY() < 0) {
            scrollTo(0, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isFirst) {
            getHeaderViewHeightOnFirstLayout();
        }
        adjustOnScrollOverTop();
    }

    private void getHeaderViewHeightOnFirstLayout() {
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        isFirst = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                recordLastMovePoint(y);
                recordLastDownPoint(y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                mDeltaY = y - mLastY;
                Log.d(TAG, "mDeltaY: " + mDeltaY);
                recordLastMovePoint(y);

                if (isPullingUp()) {
                    mPullState = PULL_UP;
                    return interceptOnPullingUp();
                } else if (isPullingDown()) {
                    mPullState = PULL_DOWN;
                    return interceptOnPullingDown(y);
                }
                else {
                    LOG("Parent: pull state not decided");
                    break;
                }
            }
        // 如果直接返回一个boolean值
        // 会出Invalid pointerId=-1 in onTouchEvent MOVE这句错误
        // 无法正常上拉
        return super.onInterceptTouchEvent(ev);
    }

    private boolean interceptOnPullingDown(int y) {
        if (checkTop()) {
            recordLastDownPoint(y);
            LOG("Parent: pull down, header hidden, top of page");
            return true;
        } else {
            LOG("Child: pull down, header hidden, not top of page");
            return false;
        }
    }

    private boolean interceptOnPullingUp() {
        if (isHeaderHidden()) {
            LOG("Child: pull up and header hidden");
            return false;
        } else {
            LOG("Parent: pull up and header not hidden");
            return true;
        }
    }

    private boolean isHeaderHidden() {
        return getScrollY() > mHeaderViewHeight;
    }

    private boolean isPullingUp() {
        return mDeltaY < 0;
    }

    private boolean isPullingDown() {
        return mDeltaY > 0;
    }

    private void recordLastDownPoint(int y) {
        mDownY = y;
    }

    private void recordLastMovePoint(int y) {
        mLastY = y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnimating) {
            return true;
        }
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                recordLastDownPoint(y);
                recordLastDownPoint(y);
                recordLastScrollY();
                isZoomed = false;
                break;
            case MotionEvent.ACTION_UP:
                canRelease();
                if (canRelease()) {
                    mPullState = UNKNOWN;
                    onReleased();
                } else {
                    mPullState = UNKNOWN;
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                calculateOffset(y);
                updateDownPointIfNeed(y);
                recordLastScrollY();
                decidePullStateIfNeed();

                /**
                 * TODO: 当头部没完全消失时 上推至消失 再下拉 本来应该由子View处理
                 * TODO: 但期间手指没有离开过 没有重新分配触摸事件
                 * TODO: 这里判断移动方向变化后重新分配触摸事件 然而还是有点生硬
                 */
                if (isPullStateChanged()) {
                    LOG("pull state changed, intercept again");
                    scroll(mDeltaY);
                    onInterceptTouchEvent(event);
                    return true;
                }

                // 必须在重新拦截触摸事件后更新最后的移动点 否则在拦截中的偏移一直为0 无法确定新方向
                recordLastMovePoint(y);

                if (mPullState == PULL_DOWN) {
                    if (getScrollY() > 0) {
                        return super.onTouchEvent(event);
                    } else {
                        if (isPullingEnoughToRefresh(y)) {
                            startRefreshIfPossible();
                        } else {
                            uiChangeOnRefreshPositionChange(y - mDownY);
                            if (canZoom(y)) {
                                zoom(y - mDownY);
                            } else {
                                return super.onTouchEvent(event);
                            }
                        }
                    }
                } else if (mPullState == PULL_UP) {
                    if (isHeaderHidden()) {
                        childScroll();
                    } else {
                        if (canZoom(y)) {
                            zoom(y - mDownY);
                        } else {
                            return super.onTouchEvent(event);
                        }
                    }
                }
                break;
        }
        return true;
    }

    private boolean canRelease() {
        return isZoomed && mPullState == PULL_DOWN && !isRefreshing;
    }

    private void childScroll() {
        scroll(-mDeltaY);
    }

    private boolean canZoom(int y) {
       return y - mDownY > 0 && isZoomable;
    }

    private void startRefreshIfPossible() {
        if (!isRefreshing) {
            isRefreshing = true;
            uiChangeOnRefreshStart();
            onRefresh(this);
        }
    }

    private boolean isPullingEnoughToRefresh(int y) {
        if (isRefreshable) {
            return y - mDownY >= mRefreshThreshold;
        } else {
            return false;
        }
    }

    private boolean isPullStateChanged() {
       return mPullState == PULL_UP && mDeltaY > 0 || mPullState == PULL_DOWN && mDeltaY < 0;
    }

    private void decidePullStateIfNeed() {
        if (mPullState == UNKNOWN) {
            if (isPullingDown()) {
                mPullState = PULL_DOWN;
            } else {
                mPullState = PULL_UP;
            }
        }
    }

    private void updateDownPointIfNeed(int y) {
        if (isScrollDirectionChanged()) {
            recordLastDownPoint(y);
        }
    }

    private boolean isScrollDirectionChanged() {
        return mScrollY > 0 && getScrollY() <= 0;
    }

    private void calculateOffset(int y) {
        mDeltaY = y - mLastY;
    }

    private void recordLastScrollY() {
        mScrollY = getScrollY();
    }

    private void zoom(int deltaY) {
        float rate = (deltaY * mAccuracy / (float) mHeaderViewHeight);
        int height = (int) ((1 + rate) * mHeaderViewHeight);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mHeaderView.getLayoutParams();
        lp.height = height;
        mHeaderView.setLayoutParams(lp);
        mHeaderView.setScaleY(1 + rate);
        mHeaderView.setScaleX(1 + rate);
        mHeight = height;
        isZoomed = true;
        adjustOnScrollOverTop();
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
            adjustOnScrollOverTop();
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
        boolean checkTop();
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

    private void LOG(String msg) {
        if (isDebugMode) {
            Log.d(TAG, msg);
        }
    }
}


