package com.github.lakeshire.lemonapp.view.morelistview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.github.lakeshire.lemonapp.R;

public class MoreItem extends RelativeLayout {

    private Scroller mScroller;
    private Context mContext;
    private int lastX;
    private int lastY;
    private int mHideWidth;
    private View mHideView;
    private int MARGIN = 0;
    private boolean isAnimating = false;
    private boolean extraHidden = true;
    private View mContainerView;
    private boolean isContainerAnimate = true;
    private OnClickListener mContainerListener;
    private FlingListener mListener;
    private GestureDetector mDetector;

    public MoreItem(Context context) {
        super(context);
        mContext = context;
        mScroller = new Scroller(context);
        setDetector();
    }

    public MoreItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScroller = new Scroller(context);
        setDetector();
    }

    public MoreItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mScroller = new Scroller(context);
        setDetector();
    }

    private void setDetector() {
        mListener = new FlingListener();
        mDetector = new GestureDetector(mListener);
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mHideView = this.findViewById(R.id.hidden);
        mContainerView = this.findViewById(R.id.container);
        mHideWidth = mHideView.getWidth() + MARGIN;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void showExtra() {
        if (extraHidden) {
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(mHideView, "translationX", 0, -mHideWidth);
            anim1.setDuration(500);
            anim1.setInterpolator(new AnticipateOvershootInterpolator());
            anim1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                    extraHidden = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            if (isContainerAnimate) {
                ObjectAnimator anim2 = ObjectAnimator.ofFloat(mContainerView, "translationX", 0, -mHideWidth);
                anim2.setDuration(500);
                AnimatorSet set = new AnimatorSet();
                set.play(anim1).with(anim2);
                set.start();
            } else {
                anim1.start();
            }
        }
    }

    public void hideExtra() {
        if (!extraHidden) {
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(mHideView, "translationX", -mHideWidth, 0);
            anim1.setDuration(100);
            anim1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                    extraHidden = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            if (isContainerAnimate) {
                ObjectAnimator anim2 = ObjectAnimator.ofFloat(mContainerView, "translationX", -mHideWidth, 0);
                anim2.setDuration(100);
                AnimatorSet set = new AnimatorSet();
                set.play(anim1).with(anim2);
                set.start();
            } else {
                anim1.start();
            }
        }
    }

    class FlingListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (!extraHidden) {
                (((MoreListView) getParent()).getAdapter()).notifyDataSetChanged();
            } else {
                performClick();
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (extraHidden) {
                performLongClick();
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (((MoreListView) getParent()).hasMore()) {
                (((MoreListView) getParent()).getAdapter()).notifyDataSetChanged();
                if (velocityX < 0) {
                    showExtra();
                } else {
                    hideExtra();
                }
            }
            return false;
        }
    }
}
