package com.github.lakeshire.lemon.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.util.ScreenUtil;

public class ScrollableView extends ScrollView {

    private Context context;
    private int lastX;
    private int lastY;
    private int autoDismissScrollY = 600;
    private boolean isAdded = false;
    private boolean isAnimating = false;
    private View mContainerView;
    private int initTranslationY = 0;
    private ImageView mIvIcon;
    private int initTranslationX = 0;
    private int icon;
    private TextView mTvTitle;
    private TextView mTvContent;

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        this.isAdded = added;
    }

    public void setIconPosition(int x, int y) {
        initTranslationY = y - ScreenUtil.dp2px(context, 48 + 8) - 2;
        initTranslationX = x;
        mContainerView.findViewById(R.id.iv_icon).setTranslationX(x);

    }

    public interface Callback {
        void autoDismiss();
        void onSlideInComplete();
    }
    private Callback cb;

    private ViewDragHelper mDragger;

    public void setIcon(int res) {
        this.icon = res;
        ((ImageView) mContainerView.findViewById(R.id.iv_icon)).setImageResource(icon);
    }

    private void initDragger() {
        mDragger = ViewDragHelper.create(this, 5.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (!isAnimating) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return 0;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int minTop = getHeight() - getChildAt(0).getHeight();
                if (top <= minTop) {
                    return minTop;
                } else if (top + initTranslationY >= autoDismissScrollY) {
                    if (!isAnimating) {
                        fadingAnimate();
                    }
                    return top;
                } else {
                    return top;
                }
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return child.getHeight();
            }
        });
    }

    public void setCallback(Callback cb) {
        this.cb = cb;
    }

    public ScrollableView(Context context) {
        super(context);

        initDragger();
    }

    public ScrollableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initDragger();
    }

    public ScrollableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDragger();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContainerView = this.findViewById(R.id.container);

        mIvIcon = (ImageView) findViewById(R.id.iv_icon);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mContainerView.setTranslationY(initTranslationY + (100 - initTranslationY) * value);
                mIvIcon.setTranslationX(initTranslationX + calcTranslateX() * value);
            }
        }); animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                initTranslationY = 100;
                isAnimating = false;
                if (cb != null) {
                    cb.onSlideInComplete();
                }
                mTvTitle.setText("豌豆荚");
                mTvContent.setText("豌豆荚是中国 Android用户中人气、活跃度很高的“移动内容搜索”，也是中国移动互联网领域的创新企业。诞生于 2009 年 12 月的豌豆荚迄今安装量已超过 4.2 亿。豌豆荚专注于「移动内容搜索」领域的创新，并通过「应用内搜索」技术让用户搜索到千万量级的不重复应用、游戏、视频、电子书、主题、电影票、问答、旅游等内容，随时随地享受全面准确和直达行动的内容搜索消费体验");
                mTvTitle.animate().alpha(1).start();
                mTvContent.animate().alpha(1).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(1000);
        animator.start();
        isAnimating = true;
    }

    private int calcTranslateX() {
        int iconDis = getWidth() / 2 - mIvIcon.getWidth() / 2;
        return iconDis - initTranslationX;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }

    private void fadingAnimate() {
        isAnimating = true;
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mContainerView.setTranslationY(initTranslationY + autoDismissScrollY * value);
                setAlpha(1 - value);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                ((ViewGroup) ScrollableView.this.getParent()).removeView(ScrollableView.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500);
        animator.start();
    }
}
