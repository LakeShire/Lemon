package com.github.lakeshire.slidelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

/**
 * 可左右滑动的列表
 */
public class SlideListView extends LinearLayout {

    private static final int FADING_EDGE_LENGTH = 100;
    protected List<ScrollItemView> scrollViews = new ArrayList();
    private ScrollItemView headerScrollView;
    public ListView listView;
    private ScrollItemView touchView;
    private int totalHeight;
    private int mode;
    private ListAdapter adapter;
    private CheckBox blockView;

    public SlideListView(Context context) {
        super(context);
    }

    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        listView = (ListView) findViewById(R.id.scroll_list);
        headerScrollView = (ScrollItemView) findViewById(R.id.scroll_title);
        headerScrollView.setSlideListView(this);
        modifyScrollView(headerScrollView);
        scrollViews.add(headerScrollView);

        blockView = (CheckBox) findViewById(R.id.block);
    }

    public void setAdapter(ListAdapter adapter) {
        this.adapter = adapter;
        listView.setAdapter(adapter);
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (ScrollItemView scrollView : scrollViews) {
            if (touchView != scrollView) {
                scrollView.smoothScrollTo(l, t);
            }
        }
    }

    public void addHViews(final ScrollItemView newScrollView) {
        if (!scrollViews.isEmpty()) {
            int size = scrollViews.size();
            ScrollItemView scrollView = scrollViews.get(size - 1);
            final int scrollX = scrollView.getScrollX();
            //第一次满屏后，向下滑动，有一条数据在开始时未加入
            if (scrollX != 0) {
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        //当listView刷新完成之后，把该条移动到最终位置
                        newScrollView.scrollTo(scrollX, 0);
                    }
                });
            }
        }
        modifyScrollView(newScrollView);
        scrollViews.add(newScrollView);
    }

    public void modifyScrollView(ScrollItemView view) {
        view.setHorizontalFadingEdgeEnabled(true);
        view.setFadingEdgeLength(FADING_EDGE_LENGTH);
        view.setOverScrollMode(OVER_SCROLL_NEVER);
        if (hook != null) {
            hook.modifyScrollView(view);
        }
    }

    public interface Hook {
        void modifyScrollView(ScrollItemView view);
    }
    private Hook hook;
    public void setHook(Hook hook) {
        this.hook = hook;
    }

    public ScrollItemView getTouchView() {
        return touchView;
    }

    public void setTouchView(ScrollItemView mTouchView) {
        this.touchView = mTouchView;
    }

    /**
     * 覆写onLayout，其目的是为了指定视图的显示位置，方法执行的前后顺序是在onMeasure之后，因为视图肯定是只有知道大小的情况下，
     * 才能确定怎么摆放
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        totalHeight = 0;

        // 遍历所有子视图
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();

            childView.layout(l, totalHeight, measuredWidth, totalHeight + measureHeight);

            totalHeight += measureHeight;
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

        int reserveHeight = 0;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {

            final View child = this.getChildAt(i);
            // 注意容纳表头的View需要是这个id 否则列表会显示不全
            if (child.getId() == R.id.header) {
                reserveHeight += child.getHeight();
            }

            if (child instanceof ListView) {
                int validHeight = this.getMeasuredHeight();
                //  如果不做这个处理 最后一些项会无法显示
                int height = validHeight - reserveHeight;
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                lp.height = height;
                child.setLayoutParams(lp);
            }
        }
    }

    void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        measureChildWithMargins(child, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
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

    public final static int MODE_DISPLAY = 0;
    public final static int MODE_EDIT = 1;

    public void setMode(int mode) {
        this.mode = mode;
        ((SlideAdapter) adapter).notifyDataSetChanged();
        blockView.setVisibility(mode == MODE_DISPLAY ? View.GONE : View.INVISIBLE);
    }

    public int getMode() {
        return this.mode;
    }

    public ListView getListView() {
        return this.listView;
    }
}
