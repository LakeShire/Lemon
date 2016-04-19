package com.github.lakeshire.lemon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lakeshire.lemon.R;

public class LoadMoreListView extends ListView {

    private Context context;
    private View footerView;
    private boolean loading = false;
    private boolean lastLine = false;
    private TextView hintTextView;
    private ImageView loadingImageView;
    private int status;

    public interface Callback {
        void loadMore();
        void initFooter(View view);
    }
    private Callback cb;
    public void setLoadMoreCallback(Callback cb) {
        this.cb = cb;
    }

    public LoadMoreListView(Context context) {
        super(context);
        this.context = context;
        initView();
        setListener();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        setListener();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        setListener();
    }

    public void initView() {
        footerView = View.inflate(context, R.layout.view_footer, null);
        footerView.setVisibility(INVISIBLE);
        hintTextView = (TextView) footerView.findViewById(R.id.tv_hint);
        loadingImageView = (ImageView) footerView.findViewById(R.id.iv_anim);
        addFooterView(footerView);
    }

    private void setListener() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && !loading && lastLine && status != STATUS_NO_MORE) {
                    footerView.setVisibility(VISIBLE);
                    loading = true;
                    if (cb != null) {
                        cb.initFooter(footerView);
                        cb.loadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastLine = (firstVisibleItem + visibleItemCount == totalItemCount);
            }
        });
    }

    public static final int STATUS_OK = 0;
    public static final int STATUS_NETWORK_ERROR = 1;
    public static final int STATUS_NO_CONTENT = 2;
    public static final int STATUS_LOADING = 3;
    public static final int STATUS_NO_MORE = 4;

    public void onLoadMoreComplete(int status) {
        this.loading = false;
        this.status = status;
        switch (status) {
            case STATUS_LOADING:
                loadingImageView.setVisibility(VISIBLE);
                hintTextView.setVisibility(INVISIBLE);
                break;
            case STATUS_NETWORK_ERROR:
                loadingImageView.setVisibility(INVISIBLE);
                hintTextView.setVisibility(VISIBLE);
                hintTextView.setText("网络出错");
                break;
            case STATUS_NO_CONTENT:
                loadingImageView.setVisibility(INVISIBLE);
                hintTextView.setVisibility(VISIBLE);
                hintTextView.setText("没有获得数据");
                break;
            case STATUS_NO_MORE:
                loadingImageView.setVisibility(INVISIBLE);
                hintTextView.setVisibility(VISIBLE);
                hintTextView.setText("没有更多数据");
                break;
            case STATUS_OK:
            default:
                footerView.setVisibility(INVISIBLE);
        }
    }

    public void resetStatus() {
        this.status = STATUS_OK;
        footerView.setVisibility(INVISIBLE);
    }
}
