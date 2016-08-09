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
	private int mMinimumVelocity;
	private int mMaximumVelocity;

	/**
	 * 持有的ViewPager
	 */
	private ViewPager mPager;
	/**
	 * 所有View的总高度
	 */
	private int mTotalHeight = 0;
	/**
	 * 手指按下的X坐标
	 */
	private int mLastX;
	/**
	 * 手指按下的Y坐标
	 */
	private int mLastY;
	/**
	 * 主容器Y方向的滚动距离
	 */
	private int mScrollHeight;
	/**
	 * 本控件关联的滚动条
	 */
	private Scroller mScroller;
	/**
	 * 首次测量标志
	 */
	private boolean firstMeasure = true;
	/**
	 * 固定组件总高度 这个是需要算出来的
	 */
	private int mFixedHeight;
	/**
	 * 是否只有列表可见
	 */
	private boolean mOnlyList = false;
	/**
	 * ViewPager关联的Adapter
	 */
	private PagerAdapter mPagerAdapter;
	/**
	 * 保留高度 计算Pager实际高度时用到 Pager实际高度=父容器高度-保留高度 保留高度=Tab高度+底部播放条高度
	 */
	private int mReserveHeight;
	/**
	 * 头部是否全部显示
	 */
	private boolean mAllShow = true;

	private boolean isTouchable = true;
	private VelocityTracker mVelocityTracker;

	private int mTabId = R.id.tabs;
	private int mViewPagerId = R.id.pager;
	private int mBannerId = R.id.banner;

	public StickyHeaderLayout(Context context) {
		super(context);
	}

	public StickyHeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
		mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
		mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
	}

	public StickyHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * 覆写onLayout，其目的是为了指定视图的显示位置，方法执行的前后顺序是在onMeasure之后，因为视图肯定是只有知道大小的情况下，
	 * 才能确定怎么摆放
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mTotalHeight = 0;

		// 遍历所有子视图
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);

			// 获取在onMeasure中计算的视图尺寸
			int measureHeight = childView.getMeasuredHeight();
			int measuredWidth = childView.getMeasuredWidth();

			childView.layout(l, mTotalHeight, measuredWidth, mTotalHeight
					+ measureHeight);

			mTotalHeight += measureHeight;
		}
	}

	/**
	 * 计算控件的大小
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int measureWidth = measureWidth(widthMeasureSpec);
		int measureHeight = measureHeight(heightMeasureSpec);

		// 计算自定义的ViewGroup中所有子控件的大小
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		// 设置自定义的控件MyViewGroup的大小
		setMeasuredDimension(measureWidth, measureHeight);

		if (firstMeasure) {
			int childCount = this.getChildCount();
			for (int i = 0; i < childCount; i++) {

				final View child = this.getChildAt(i);

				if (child.getId() == getTabId()) {
					int tabHeight = child.getMeasuredHeight();
					mReserveHeight += tabHeight;
				}

				if (child.getId() == getViewPagerId()) {
					int validHeight = this.getMeasuredHeight();
					int height = validHeight - mReserveHeight;
					ViewGroup.LayoutParams lp = child.getLayoutParams();
					lp.height = height;
					child.setLayoutParams(lp);
				}

				if (child.getId() == getBannerId()) {
					mFixedHeight += child.getMeasuredHeight();
				}
			}
			firstMeasure = false;
		}
	}

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

	private int measureWidth(int pWidthMeasureSpec) {
		int result = 0;
		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);
		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);

		switch (widthMode) {
		/**
		 * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
		 * MeasureSpec.AT_MOST。
		 * 
		 * 
		 * MeasureSpec.EXACTLY是精确尺寸，
		 * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
		 * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
		 * 
		 * 
		 * MeasureSpec.AT_MOST是最大尺寸，
		 * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
		 * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
		 * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
		 * 
		 * 
		 * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
		 * 通过measure方法传入的模式。
		 */
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
			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastY;
			int absDeltaX = Math.abs(x - mLastX);
			int absDeltaY = Math.abs(y - mLastY);
			mLastX = x;
			mLastY = y;

			// 横向：子
			if (absDeltaX >= absDeltaY) {
				Log.i(TAG, "横向：子");
				return false;
			}
			// 纵向
			else {

				if (deltaY < 0) {
					// 上拉
					if (mOnlyList) {
						// 头部全部隐藏：子
						Log.i(TAG, "上拉，头部不可见：子");
						return false;
					} else {
						// 头部为全部隐藏：父
						Log.i(TAG, "上拉，头部可见：父");
						return true;
					}
				} else {
					// 下拉
					if (mOnlyList) {
						// 头部全部隐藏
						if (mPager != null && mPagerAdapter != null) {
							Fragment fragment = mPagerAdapter.getItem(mPager.getCurrentItem());
							if (fragment != null) {
								if (!(fragment instanceof IHandler)) {
									Log.d(TAG, "子页面没有实现IHandler接口，由父页面处理");
									return true;
								}
							}
							if (((IHandler) fragment).checkTop()) {
								// 列表到顶：父
								Log.i(TAG, "下拉，头部不可见，列表到顶：父");
								return true;
							} else {
								// 列表没到顶：子
								Log.i(TAG, "下拉，头部不可见，列表未到顶：子");
								return false;
							}
						}
					} else {
						// 头部未全部隐藏：父
						if (mAllShow) {
							// 头部全部显示:子
							Log.i(TAG, "下拉，头部全部可见：子");
							return false;
						} else {
							// 头部未全部显示:父
							Log.i(TAG, "下拉，头部全部可见：父");
							return true;
						}
					}
				}
			}
		}
		return intercepted;
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
			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
			int velocityY = (int) mVelocityTracker.getYVelocity();
			if (Math.abs(velocityY) > mMinimumVelocity) {
				if (velocityY < 0) {
					mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, mFixedHeight - mScroller.getFinalY());
				} else {
					mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, -mScroller.getFinalY());
				}
			}
			recycleVelocityTracker();
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastY;
			mLastY = y;
			mScrollHeight = -deltaY;
			// 处理触摸的情况
			// 上面不能拉的太过头
			// 下面不能拉出所有控件的高度

			if (mScroller.getCurrY() + mScrollHeight <= 0) {
				mAllShow = true;
				mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, -mScroller.getFinalY());
				invalidate();

				this.onInterceptTouchEvent(event);
				return false;
			}
			if (mScroller.getFinalY() + mScrollHeight >= 0 && mScroller.getFinalY() + mScrollHeight <= mTotalHeight) {
				mAllShow = false;
				if (!mOnlyList) {
					// 界面上不仅只有List的情况下 看滚动的长度
					if (mScroller.getFinalY() + mScrollHeight >= mFixedHeight) {
						// 如果滚动出固定长 则进入完全的List
						mOnlyList = true;
						mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, mFixedHeight - mScroller.getFinalY());
					} else {
						// 正常滚动
						mOnlyList = false;
						mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, mScrollHeight);
					}
				} else {
					// 界面上仅有List的情况下
					if (mScrollHeight < 0) {
						// 如果是向下拉 父控件处理滑动
						mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 0, mFixedHeight - mScroller.getCurrY());
						mOnlyList = false;
					} else {
						// 如果是向上推 就得让子控件推
						if (mPager != null && mPagerAdapter != null) {
							Fragment fragment = mPagerAdapter.getItem(mPager.getCurrentItem());
							if (fragment != null) {
								if (!(fragment instanceof IHandler)) {
									Log.d(TAG, "子页面没有实现IHandler接口，由父页面处理");
									break;
								} else {
									((IHandler) fragment).scroll(mScrollHeight);
								}
							}
						}
					}
				}

			}
			invalidate();
		default:
			break;
		}
		return true;
	}

	@Override
	public void computeScroll() {
		// 先判断mScroller滚动是否完成
		if (mScroller.computeScrollOffset()) {
			// 这里调用View的scrollTo()完成实际的滚动
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// 必须调用该方法，否则不一定能看到滚动效果
			postInvalidate();
		}
		super.computeScroll();
	}

	public void setPager(ViewPager pager) {
		mPager = pager;
	}

	public void setPagerAdapter(PagerAdapter adapter) {
		mPagerAdapter = adapter;
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

	public interface IHandler {
		//	如果实现这个接口 下拉时会先将子页面拉倒顶 再拉父页面
		boolean checkTop();
		void scroll(int y);
	}
}
