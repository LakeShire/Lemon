package com.github.lakeshire.lemon.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.github.lakeshire.lemon.R;

/**
 * 模拟水滴效果的可拖动的圆 可以做为下拉刷新的头部动画
 */
public class DraggableCircle extends View {

    private Paint mPaintPoint;
    private Paint mPaint;
    private float x;
    private float y;

    //TODO: 硬编码
    private static final int RADIUS = 50;
    private static final int RADIUS_FINGER = 20;
    private Point[] points = new Point[4];
    private float pressX;
    private float pressY;

    public static final int STATE_IDEL = 0;
    public static final int STATE_DOWN = 1;
    public static final int STATE_UP = 2;
    public static final int STATE_REFRESH = 3;
    public static final int STATE_MOVE = 4;
    private int status = 0;
    private Bitmap mSource;
    private int mDegree = 0;
    private boolean rotating = false;
    private ValueAnimator mRotateAnimation;

    public DraggableCircle(Context context) {
        super(context);
    }

    public DraggableCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(context.getResources().getColor(R.color.tiffany));

        mPaintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPoint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintPoint.setColor(context.getResources().getColor(R.color.black));
        mPaintPoint.setStrokeWidth(5);

        mSource = BitmapFactory.decodeResource(getResources(), R.drawable.refresh);
    }

    public DraggableCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (status) {
            case STATE_IDEL:
                canvas.drawCircle(x, y, RADIUS, mPaint);
                break;
            case STATE_DOWN:
            case STATE_MOVE:
            case STATE_UP:
                canvas.drawCircle(x, y, RADIUS, mPaint);
                canvas.drawCircle(pressX, pressY, RADIUS_FINGER, mPaint);
                Path path = new Path();
                path.moveTo(points[0].getX(), points[1].getY());
                for (int i = 1; i < 4; i++) {
                    path.lineTo(points[i].getX(), points[i].getY());
                }
                path.close();
                canvas.drawPath(path, mPaint);
                break;
            case STATE_REFRESH:
                canvas.drawCircle(x, y, RADIUS, mPaint);
                canvas.drawCircle(pressX, pressY, RADIUS_FINGER, mPaint);
                path = new Path();
                path.moveTo(points[0].getX(), points[1].getY());
                for (int i = 1; i < 4; i++) {
                    path.lineTo(points[i].getX(), points[i].getY());
                }
                path.close();
                canvas.drawPath(path, mPaint);
                canvas.translate(x, y);
                canvas.rotate(mDegree);
                canvas.drawBitmap(mSource, null, new Rect(-RADIUS / 2, -RADIUS / 2, RADIUS / 2, RADIUS / 2), mPaint);
                startRotateAnimation();
            default:
                break;

        }
    }

    private void startRotateAnimation() {
        if (!rotating) {
            rotating = true;
            mRotateAnimation = ValueAnimator.ofFloat(0, 360);
            mRotateAnimation.setDuration(2000);
            mRotateAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    mDegree = (int) value;
                    postInvalidate();
                }
            });
            mRotateAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    rotating = false;
                    startRotateAnimation();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mRotateAnimation.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        x = width / 2;
        y = height / 2;
    }

    private void bounce() {
        ValueAnimator animator = ValueAnimator.ofFloat(pressY, this.y);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                drag(DraggableCircle.this.x, value);
            }
        });
        animator.start();
    }

    /**
     * 开始刷新
     */
    public void refresh() {
        bounce();
        status = STATE_REFRESH;
    }

    /**
     * 取消刷新
     */
    public void cancelRefresh() {
        if (mRotateAnimation != null) {
            status = STATE_IDEL;
            mRotateAnimation.cancel();
            rotating = false;
        }
    }

    /**
     * 拖动
     * @param value
     */
    public void drag(float value) {
        if (status != STATE_REFRESH) {
            status = STATE_MOVE;
            drag(this.x, value / 2 + this.y);
        }
    }

    private void drag(float x, float y) {
        if (status == STATE_DOWN) {
            if (Math.abs(x - this.x) > RADIUS || Math.abs(y - this.y) > RADIUS) {
                return;
            }
        }

        if (y > getHeight() - 32) {
            return;
        }

        pressX = this.x;
        pressY = y;

        points[0] = new Point(this.x - RADIUS, this.y);
        points[1] = new Point(this.x + RADIUS, this.y);
        points[2] = new Point(pressX + RADIUS_FINGER, y);
        points[3] = new Point(pressX - RADIUS_FINGER, y);
        invalidate();
    }

    class Point {

        private float x;
        private float y;

        public float getX() {
            return x;
        }
        public float getY() {
            return y;
        }
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
