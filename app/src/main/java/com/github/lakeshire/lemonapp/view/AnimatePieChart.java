package com.github.lakeshire.lemonapp.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.github.lakeshire.lemonapp.R;

/**
 * 载入时动态绘制的饼图
 */
public class AnimatePieChart extends View {

    private Paint mInnerPaint;
    private RectF mOval;
    private Paint mPaint;
    private float[] datas;
    private int[] colors;
    private int centerX;
    private int centerY;
    private int RADIUS = 200;
    private float sum = 0;
    private float mCurrentValue;
    private float[] angles;
    private boolean measured = false;
    private boolean drawText = false;

    public AnimatePieChart(Context context) {
        super(context);
    }

    public AnimatePieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(context.getResources().getColor(R.color.tiffany));

        mInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mInnerPaint.setColor(context.getResources().getColor(R.color.background_material_light));
    }

    public AnimatePieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!measured) {
            measured = true;
            centerX = getWidth() / 2;
            centerY = getHeight() / 2;
            mOval = new RectF(centerX - RADIUS, centerY - RADIUS, centerX + RADIUS, centerY + RADIUS);
        }

        int sections = 0;

        if (mCurrentValue <= angles[0]) {
            sections = 1;
        } else {
            for (int i = 0; i < angles.length - 1; i++) {
                if (mCurrentValue > angles[i] && mCurrentValue <= angles[i + 1]) {
                    sections = i + 2;
                    break;
                } else {
                    sections = angles.length;
                }
            }
        }

        if (sections >= 2) {
            for (int i = 0; i < sections - 1; i++) {
                mPaint.setColor(colors[i]);
                if (i == 0) {
                    canvas.drawArc(mOval, 0, angles[i], true, mPaint);
                } else {
                    canvas.drawArc(mOval, angles[i-1], angles[i] - angles[i-1], true, mPaint);
                }
            }
            mPaint.setColor(colors[sections - 1]);
            canvas.drawArc(mOval, angles[sections - 2], mCurrentValue - angles[sections - 2], true, mPaint);
        } else {
            mPaint.setColor(colors[0]);
            canvas.drawArc(mOval, 0, mCurrentValue, true, mPaint);
        }

        canvas.drawCircle(centerX, centerY, RADIUS / 2, mInnerPaint);

        if (drawText) {

        }
    }

    public void startDraw() {
        float start = 0;
        for (int i = 0; i < datas.length; i++) {
            angles[i] = start + datas[i] / sum * 360;
            start += datas[i] / sum * 360;
        }
        startAnimate();
    }

    public void setDataCount(int count) {
        datas = new float[count];
        colors = new int[count];
        angles = new float[count];
    }

    public void addData(int id, float data, int color) {
        datas[id] = data;
        colors[id] = color;
        sum += data;
    }

    private void startAnimate() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 360);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(500);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                drawText = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCurrentValue = value;
                postInvalidate();
            }
        });
        animator.start();
    }
}
