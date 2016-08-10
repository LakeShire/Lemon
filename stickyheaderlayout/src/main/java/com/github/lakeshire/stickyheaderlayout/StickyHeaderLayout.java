package com.github.lakeshire.stickyheaderlayout;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * 嵌套滑动页面组件
 * 
 * @author louis.liu
 * 
 */
public class StickyHeaderLayout extends RelativeLayout {

	private static final String TAG = StickyHeaderLayout.class.getName();

	private Context mContext;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private Scroller mScroller;

	private int mTotalHeight = 0;
	private int mLastX;
	private int mLastY;
	private int mScrollHeight;
	private int mFixedHeight;
	// 保留高度 计算Pager实际高度时用到 Pager实际高度=父容器高度-保留高度 保留高度=Tab高度+底部播放条高度
	private int mReserveHeight;

	private boolean firstMeasure = true;
	private boolean isHeaderAllShow = true;
	private boolean isHeaderHidden = false;
	private boolean isTouchable = true;
	private boolean isDebugMode = false;

	private VelocityTracker mVelocityTracker;
	private int mMinimumVelocity;
	private int mMaximumVelocity;

	private int mTabId = R.id.tabs;
	private int mViewPagerId = R.id.pager;
	private int mBannerId = R.id.banner;

	private int mDeltaY;
	private int mAbsDeltaX;
	private int mAbsDeltaY;
	private int mVelocityY;

	public int getTabId() {
		return mTabId;
	}

	public void setTabId(int tabId) {
		mTabId = tabId;
	}

	public int getViewPagerId() {
		return mViewPagerId;
	}

	public void setViewPagerId(int viewPagerId) {
		mViewPagerId = viewPagerId;
	}

	public int getBannerId() {
		return mBannerId;
	}

	public void setBannerId(int bannerId) {
		mBannerId = bannerId;
	}

	public void setTouchable(boolean touchable) {
		isTouchable = touchable;
	}

	public void setPager(ViewPager pager) {
		mPager = pager;
	}

	public void setPagerAdapter(PagerAdapter adapter) {
		mPagerAdapter = adapter;
	}

	public boolean isDebugMode() {
		return isDebugMode;
	}

	public void setDebugMode(boolean debugMode) {
		isDebugMode = debugMode;
	}

	public StickyHeaderLayout(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public StickyHeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public StickyHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		init();
	}

	private void init() {
		mScroller = new Scroller(mContext);
		mMaximumVelocity = ViewConfiguration.get(mContext).getScaledMaximumFlingVelocity();
		mMinimumVelocity = ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mTotalHeight = 0;
		layoutChildren(l);
	}

	private void layoutChildren(int left) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			int measureHeight = childView.getMeasuredHeight();
			int measuredWidth = childView.getMeasuredWidth();
			childView.layout(left, mTotalHeight, measuredWidth, mTotalHeight + measureHeight);
			mTotalHeight += measureHeight;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int measureWidth = measureWidth(widthMeasureSpec);
		int measureHeight = measureHeight(heightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth, measureHeight);

		if (firstMeasure) {
			dealFirstMeasure();
		}
	}

	private void dealFirstMeasure() {
		int childCount = this.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = this.getChildAt(i);
			dealWhenViewIsTab(child);
			dealWhenViewIsViewPager(child);
			dealWhenViewIsBanner(child);
		}
		firstMeasure = false;
	}

	private void dealWhenViewIsBanner(View view) {
		if (view.getId() == getBannerId()) {
			mFixedHeight += view.getMeasuredHeight();
		}
	}

	private void dealWhenViewIsViewPager(View view) {
		if (view.getId() == getViewPagerId()) {
			adjustViewPagerHeight(view);

		}
	}

	private void adjustViewPagerHeight(View view) {
		int validHeight = getMeasuredHeight();
		int height = validHeight - mReserveHeight;
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		lp.height = height;
		view.setLayoutParams(lp);
	}

	private void dealWhenViewIsTab(View view) {
		if (view.getId() == getTabId()) {
			int tabHeight = view.getMeasuredHeight();
			mReserveHeight += tabHeight;
		}
	}

	private int measureWidth(int pWidthMeasureSpec) {
		int result = 0;
		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);
		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);

		switch (widthMode) {
			case MeasureSpec.AT_MOST:
			case MeasureSpec.EXACTLY:
				result = widthSize;
				break;
		}
		return result;
	}

	private int measureHeight(int pHeightMeasureSpec) {
		int result = 0;

		int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

		switch (heightMode) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = heightSize;
			break;
		}
		return result;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		initVelocityTrackerIfNotExists();
		boolean intercepted = false;
		int x = (int) ev.getX();
		int y = (int) ev.getY();

		if (!isTouchable) {
			return true;
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			recordLastTouchPoint(x, y);
			break;
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		case MotionEvent.ACTION_MOVE:
			calculateOffset(x, y);
			recordLastTouchPoint(x, y);

			if (isMoveHorizontal()) {
				return interceptOnHorizontalMoving();
			} else {
				return interceptOnVerticalMoving();
			}
		}
		return intercepted;
	}

	private void initVelocityTrackerIfNotExists() {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
	}

	private void recycleVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	private boolean interceptOnVerticalMoving() {
		if (isPullingUp()) {
			return interceptOnPullingUp();
		} else {
			return interceptOnPullingDown();
		}
	}

	private boolean interceptOnPullingDown() {
		if (isHeaderHidden()) {
			return interceptOnHeaderHidden();
		} else {
			return interceptOnHeaderNotHidden();

		}
	}

	private boolean interceptOnHeaderNotHidden() {
		if (isHeaderAllShow()) {
			LOG("Child: pull down and header is all shown");
			return false;
		} else {
			LOG("Parent: pull down and header is not all shown");
			return true;
		}
	}

	private boolean interceptOnHeaderHidden() {
		if (mPager != null && mPagerAdapter != null) {
			Fragment fragment = mPagerAdapter.getItem(mPager.getCurrentItem());
			if (fragment != null) {
				if (!(fragment instanceof IHandler)) {
					LOG("Parent: child page does not implement IHandle");
					return true;
				}
			}
			if (((IHandler) fragment).checkTop()) {
				LOG("Parent: pull down, header hidden and top of page");
				return true;
			} else {
				LOG("Child: pull down, header hidden and not top of page");
				return false;
			}
		} else {
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

	private boolean interceptOnHorizontalMoving() {
		LOG("Child: move horizontally");
		return false;
	}

	private boolean isHeaderAllShow() {
		return isHeaderAllShow;
	}

	private boolean isHeaderHidden() {
		return isHeaderHidden;
	}

	private boolean isPullingUp() {
		return mDeltaY < 0;
	}

	private void recordLastTouchPoint(int x, int y) {
		mLastX = x;
		mLastY = y;
	}

	private boolean isMoveHorizontal() {
		return mAbsDeltaX >= mAbsDeltaY;
	}

	private void calculateOffset(int x, int y) {
		mDeltaY = y - mLastY;
		mAbsDeltaX = Math.abs(x - mLastX);
		mAbsDeltaY = Math.abs(y - mLastY);
		mScrollHeight = -mDeltaY;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mVelocityTracker.addMovement(event);
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (!isTouchable) {
			return false;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			recordLastTouchPoint(x, y);
			break;
		case MotionEvent.ACTION_UP:
			flingOnActionUp();
			recycleVelocityTracker();
			break;
		case MotionEvent.ACTION_MOVE:
			calculateOffset(x, y);
			recordLastTouchPoint(x, y);

			if (isScrollingToHeaderAllShow()) {
				scrollToHeaderAllShowExactly();
				return false;
			}

			if (isScrollingInRange()) {
				isHeaderAllShow = false;
				if (!isHeaderHidden()) {
					scrollOnHeaderNotHidden();
				} else {
					if (isScrollingDown()) {
						scrollDownOnHeaderHidden();
					} else {
						childScrollUp();
					}
				}
			}
			invalidate();
		default:
			break;
		}
		return true;
	}

	private void childScrollUp() {
		if (mPager != null && mPagerAdapter != null) {
			Fragment fragment = mPagerAdapter.getItem(mPager.getCurrentItem());
			if (fragment != null) {
				if (!(fragment instanceof IHandler)) {
					LOG("Parent: child page does not implement IHandle");
				} else {
					LOG("child scroll up");
					((IHandler) fragment).scroll(mScrollHeight);
				}
			}
		}
	}

	private void scrollDownOnHeaderHidden() {
		LOG("header hidden, parent scroll down");
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, mFixedHeight - mScroller.getCurrY());
		isHeaderHidden = false;
	}

	private boolean isScrollingDown() {
		return mScrollHeight < 0;
	}

	private void scrollOnHeaderNotHidden() {
		if (isScrollingToHeaderHidden()) {
			scrollToHeaderHiddenExactly();
		} else {
			scrollNormally();
		}
	}

	private void scrollNormally() {
		isHeaderHidden = false;
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, mScrollHeight);
	}

	private void scrollToHeaderHiddenExactly() {
		LOG("scroll to header hidden exactly");
		isHeaderHidden = true;
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, mFixedHeight - mScroller.getFinalY());
	}

	private boolean isScrollingToHeaderHidden() {
		return mScroller.getFinalY() + mScrollHeight >= mFixedHeight;
	}

	private boolean isScrollingInRange() {
		return mScroller.getFinalY() + mScrollHeight >= 0 && mScroller.getFinalY() + mScrollHeight <= mTotalHeight;
	}

	private void scrollToHeaderAllShowExactly() {
		LOG("scroll to header all show exactly");
		isHeaderAllShow = true;
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, -mScroller.getFinalY());
	}

	private boolean isScrollingToHeaderAllShow() {
		return mScroller.getCurrY() + mScrollHeight <= 0;
	}

	private void flingOnActionUp() {
		mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
		mVelocityY = (int) mVelocityTracker.getYVelocity();
		if (isVelocityFastEnough()) {
			if (isFlingUp()) {
				flingToHideHeader();
			} else {
				flingToAllShowHeader();
			}
		}
	}

	private void flingToAllShowHeader() {
		LOG("fling down, all show header");
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, -mScroller.getFinalY());
		isHeaderAllShow = true;
	}

	private void flingToHideHeader() {
		LOG("fling up, hide header");
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, mFixedHeight - mScroller.getFinalY());
		isHeaderHidden = true;
	}

	private boolean isFlingUp() {
		return mVelocityY < 0;
	}

	private boolean isVelocityFastEnough() {
		return Math.abs(mVelocityY) > mMinimumVelocity;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// 必须调用该方法，否则不一定能看到滚动效果
			postInvalidate();
		}
		super.computeScroll();
	}

	public interface IHandler {
		//	如果实现这个接口 下拉时会先将子页面拉倒顶 再拉父页面
		boolean checkTop();
		void scroll(int y);
	}

	private void LOG(String msg) {
		if (isDebugMode) {
			Log.d(TAG, msg);
		}
	}
}
