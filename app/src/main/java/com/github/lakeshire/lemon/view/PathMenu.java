package com.github.lakeshire.lemon.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.lakeshire.lemon.R;

/**
 * Created by nali on 2016/4/19.
 */
public class PathMenu extends RelativeLayout {

    private Context context;
    private ImageView[] images;
//    private int[] icons = {R.drawable.path_music, R.drawable.path_location, R.drawable.path_photo, R.drawable.path_quote, R.drawable.path_sleep};
    private boolean expanded = false;
    private ImageView ivAdd;
    private boolean animating = false;
    private int[] dstX;
    private int[] dstY;
    private int radius = 256;
    private int startAngle;
    private int range = 0;
    private long animateDuration = 500;
    private Drawable background;

    public PathMenu(Context context) {
        super(context);
    }

    public PathMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PathMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(Context context, int[] icons, OnClickListener[] listeners) {
        initView(context, icons, listeners, 200, 0, 180, 500);
    }

    public void initView(Context context, int[] icons, OnClickListener[] listeners, int radius, int startAngle, int range, int animateDuration) {
        this.context = context;

        images = new ImageView[icons.length];
        dstX = new int[icons.length];
        dstY = new int[icons.length];

        for (int i = 0; i < icons.length; i++) {
            images[i] = new ImageView(context);
            images[i].setImageResource(R.drawable.path_add);
            images[i].setImageResource(icons[i]);
            images[i].setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(64, 64);
            lp.addRule(ALIGN_PARENT_BOTTOM);
            lp.addRule(CENTER_HORIZONTAL);
            images[i].setLayoutParams(lp);

            if (listeners != null && listeners.length == icons.length) {
                images[i].setOnClickListener(listeners[i]);
            }
            addView(images[i]);
        }

        ivAdd = new ImageView(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(64, 64);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        lp.addRule(CENTER_HORIZONTAL);
        ivAdd.setLayoutParams(lp);
        ivAdd.setImageResource(R.drawable.path_add);
        addView(ivAdd);
        ivAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!animating) {
                    if (expanded) {
                        foldMenu();
                    } else {
                        expandMenu();
                    }
                }
            }
        });

        if (radius != 0) {
            this.radius = radius;
        }
        this.startAngle = startAngle;
        this.range = range;
        this.animateDuration = animateDuration;
    }

    private void expandMenu() {
        background = ((View) getParent()).getBackground();
        ((View) getParent()).setBackgroundResource(R.color.transparent_black_light);
        for (int i = 0; i < images.length; i++) {
            dstX[i] = (int) (radius * Math.cos(startAngle - (range / (images.length - 1)) * Math.PI / 180 * i));
            dstY[i] = (int) (radius * Math.sin(startAngle - (range / (images.length - 1)) * Math.PI / 180 * i));
        }
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(animateDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ivAdd.setRotation(-45 * value);
                for (int i = 0; i < images.length; i++) {
                    images[i].setRotation(360 * value);
                    images[i].setTranslationX(dstX[i] * value);
                    images[i].setTranslationY(dstY[i] * value);
                    images[i].setScaleX(value);
                    images[i].setScaleY(value);
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animating = true;
                for (ImageView iv : images) {
                    iv.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animating = false;
                expanded = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void foldMenu() {
        ((View) getParent()).setBackgroundDrawable(background);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(animateDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ivAdd.setRotation(-45 * (1 - value));
                for (int i = 0; i < images.length; i++) {
                    if (value > 0.9) {
                        images[i].setVisibility(GONE);
                    }
                    images[i].setRotation(360 * value);
                    images[i].setTranslationX(dstX[i] * (1 - value));
                    images[i].setTranslationY(dstY[i] * (1 - value));
                    images[i].setScaleX(1 - value);
                    images[i].setScaleY(1 - value);
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animating = false;
                expanded = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
