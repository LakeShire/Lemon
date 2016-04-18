package com.github.lakeshire.lemon.view.pulltofresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.view.DraggableCircle;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class EnhanceHeader extends FrameLayout implements PtrUIHandler {

    private final static String KEY_SharedPreferences = "cube_ptr_classic_last_update";
    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int mFlag = 0x0;
    private int mRotateAniTime = 150;
    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;
    private TextView mTitleTextView;
    private View mProgressBar;
    private long mLastUpdateTime = -1;
    private TextView mLastUpdateTextView;
    private String mLastUpdateTimeKey;
    private boolean mShouldShowLastUpdate;
    private LastUpdateTimeUpdater mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();
    private TextView mLoadingTextView;
    private ImageView mImageView;

    //  是否有自定义的标题栏
    //  如果有那么只显示进度条
    //  否则会有标题文字和加载文字
    private boolean isCustomTitleBar = false;
    private DraggableCircle mIvCircle;
    private boolean startRefresh = false;

    public boolean isCustomTitleBar() {
        return isCustomTitleBar;
    }
    public void setCustomTitleBar(boolean isCustomTitleBar) {
        this.isCustomTitleBar = isCustomTitleBar;
    }

    public final static byte FLAG_IMAGE = 0x1;
    public final static byte FLAG_TEXT = 0x1 << 1;
    public final static byte FLAG_PROGRESS = 0x1 << 2;

    public EnhanceHeader(Context context, int flag) {
        super(context);
        mFlag = flag;
        initViews(null);
    }

    public EnhanceHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public EnhanceHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, in.srain.cube.views.ptr.R.styleable.PtrClassicHeader, 0, 0);
        if (arr != null) {
            mRotateAniTime = arr.getInt(in.srain.cube.views.ptr.R.styleable.PtrClassicHeader_ptr_rotate_ani_time, mRotateAniTime);
        }
        buildAnimation();
        View header = LayoutInflater.from(getContext()).inflate(in.srain.cube.views.ptr.R.layout.cube_ptr_classic_default_header, this);

        mTitleTextView = (TextView) header.findViewById(R.id.tv_title);
        mLoadingTextView = (TextView) header.findViewById(R.id.tv_loading);
        mLastUpdateTextView = (TextView) header.findViewById(R.id.ptr_classic_header_rotate_view_header_last_update);
        mProgressBar = header.findViewById(in.srain.cube.views.ptr.R.id.ptr_classic_header_rotate_view_progressbar);
        mImageView = (ImageView) header.findViewById(R.id.ptr_classic_header_rotate_view);
        mIvCircle = (DraggableCircle) header.findViewById(R.id.circle);

        if ((mFlag & FLAG_TEXT) == 0) {
            mTitleTextView.setVisibility(GONE);
            mLoadingTextView.setVisibility(GONE);
        }
        if ((mFlag & FLAG_PROGRESS) == 0) {
            mProgressBar.setVisibility(GONE);
        }
        if ((mFlag & FLAG_IMAGE) == 0) {
            mImageView.setVisibility(GONE);
        }
        resetView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mLastUpdateTimeUpdater != null) {
            mLastUpdateTimeUpdater.stop();
        }
    }

    public void setRotateAniTime(int time) {
        if (time == mRotateAniTime || time == 0) {
            return;
        }
        mRotateAniTime = time;
        buildAnimation();
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mLastUpdateTimeKey = key;
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        setLastUpdateTimeKey(object.getClass().getName());
    }

    private void buildAnimation() {
        mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(mRotateAniTime);
        mFlipAnimation.setFillAfter(true);

        mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(mRotateAniTime);
        mReverseFlipAnimation.setFillAfter(true);
    }

    private void resetView() {
        hideRotateView();
        if ((mFlag & FLAG_PROGRESS) > 0) {
            mProgressBar.setVisibility(INVISIBLE);
        } else {
            mProgressBar.setVisibility(GONE);
        }
        if (!isCustomTitleBar && (mFlag & FLAG_TEXT) > 0) {
            mTitleTextView.setVisibility(VISIBLE);
            mLoadingTextView.setVisibility(INVISIBLE);
        }
    }

    private void hideRotateView() {

    }

    @Override
    public void onUIReset(EnhancePtrFrameLayout frame) {
        resetView();
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
    }

    @Override
    public void onUIRefreshPrepare(EnhancePtrFrameLayout frame) {
        startRefresh = true;
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.start();
    }

    @Override
    public void onUIRefreshBegin(EnhancePtrFrameLayout frame) {
        startRefresh = false;
        mIvCircle.refresh();
        mShouldShowLastUpdate = false;
        hideRotateView();
        if ((mFlag & FLAG_PROGRESS) > 0) {
            mProgressBar.setVisibility(VISIBLE);
        }
        if (!isCustomTitleBar && (mFlag & FLAG_TEXT) > 0) {
            mTitleTextView.setVisibility(INVISIBLE);
            mLoadingTextView.setVisibility(VISIBLE);
        }

        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.stop();
    }

    @Override
    public void onUIRefreshComplete(EnhancePtrFrameLayout frame) {
//        mIvCircle.cancelRefresh();
        hideRotateView();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KEY_SharedPreferences, 0);
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = new Date().getTime();
            sharedPreferences.edit().putLong(mLastUpdateTimeKey, mLastUpdateTime).commit();
        }
    }

    private void tryUpdateLastUpdateTime() {
        if (TextUtils.isEmpty(mLastUpdateTimeKey) || !mShouldShowLastUpdate) {
            mLastUpdateTextView.setVisibility(GONE);
        } else {
            String time = getLastUpdateTime();
            if (TextUtils.isEmpty(time)) {
                mLastUpdateTextView.setVisibility(GONE);
            } else {
                mLastUpdateTextView.setVisibility(VISIBLE);
                mLastUpdateTextView.setText(time);
            }
        }
    }

    private String getLastUpdateTime() {

        if (mLastUpdateTime == -1 && !TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = getContext().getSharedPreferences(KEY_SharedPreferences, 0).getLong(mLastUpdateTimeKey, -1);
        }
        if (mLastUpdateTime == -1) {
            return null;
        }
        long diffTime = new Date().getTime() - mLastUpdateTime;
        int seconds = (int) (diffTime / 1000);
        if (diffTime < 0) {
            return null;
        }
        if (seconds <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getContext().getString(in.srain.cube.views.ptr.R.string.cube_ptr_last_update));

        if (seconds < 60) {
            sb.append(seconds + getContext().getString(in.srain.cube.views.ptr.R.string.cube_ptr_seconds_ago));
        } else {
            int minutes = (seconds / 60);
            if (minutes > 60) {
                int hours = minutes / 60;
                if (hours > 24) {
                    Date date = new Date(mLastUpdateTime);
                    sb.append(sDataFormat.format(date));
                } else {
                    sb.append(hours + getContext().getString(in.srain.cube.views.ptr.R.string.cube_ptr_hours_ago));
                }

            } else {
                sb.append(minutes + getContext().getString(in.srain.cube.views.ptr.R.string.cube_ptr_minutes_ago));
            }
        }
        return sb.toString();
    }

    @Override
    public void onUIPositionChange(EnhancePtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (startRefresh) {
            mIvCircle.drag(currentPos);
        }
        if (lastPos == 0) {
            mIvCircle.cancelRefresh();
        }

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
            }
        }
    }

    private void crossRotateLineFromTopUnderTouch(EnhancePtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
        }
    }

    private void crossRotateLineFromBottomUnderTouch(EnhancePtrFrameLayout frame) {

    }

    private class LastUpdateTimeUpdater implements Runnable {

        private boolean mRunning = false;

        private void start() {
            if (TextUtils.isEmpty(mLastUpdateTimeKey)) {
                return;
            }
            mRunning = true;
            run();
        }

        private void stop() {
            mRunning = false;
            removeCallbacks(this);
        }

        @Override
        public void run() {
            tryUpdateLastUpdateTime();
            if (mRunning) {
                postDelayed(this, 1000);
            }
        }
    }
}
