package com.github.lakeshire.lemonapp.fragment.examples;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.github.lakeshire.lemon.util.HttpUtil;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.lemonapp.adapter.slidelistview.MySlideAdapter;
import com.github.lakeshire.lemonapp.fragment.base.BasePullFragment;
import com.github.lakeshire.lemonapp.model.DataWrapper;
import com.github.lakeshire.lemonapp.model.Response;
import com.github.lakeshire.lemonapp.model.Stock;
import com.github.lakeshire.lemonapp.model.slidelistview.Model;
import com.github.lakeshire.lemonapp.model.slidelistview.MyModel;
import com.github.lakeshire.lemonapp.view.LoadMoreListView;
import com.github.lakeshire.lemonapp.view.pulltofresh.EnhancePtrFrameLayout;
import com.github.lakeshire.lemonapp.view.slidelistview.SlideListView;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SlideListViewFragment extends BasePullFragment {

    private SlideListView mListView;
    private ArrayList<MyModel> mDatas = new ArrayList();
    private MySlideAdapter mAdapter;
    private boolean loading = false;
    private boolean loadingMore = false;
    private int mPageId = 1;
    private int mPageSize = 20;
    private int mTotalPage = 1;
    private boolean isRefresh = false;
    private TimerTask mRefreshTask;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_slide_list;
    }

    @Override
    public void initUi() {
        super.initUi();
        initListView();
    }

    @Override
    public void loadData() {
        super.loadData();
        refresh();
        mRefreshTask = new TimerTask() {
            @Override
            public void run() {
                if (!loadingMore) {
                    loading = true;
                    updateData();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(mRefreshTask, 2000, 10000);
    }

    private void initListView() {
        mListView = (SlideListView) find(R.id.slide_list);

        mAdapter = new MySlideAdapter(getActivity(), mDatas, R.layout.item_slide, mListView);
        mListView.setAdapter(mAdapter);
        ((LoadMoreListView) mListView.getListView()).setLoadMoreCallback(new LoadMoreListView.Callback() {
            @Override
            public void loadMore() {
                ((LoadMoreListView) mListView.getListView()).onLoadMoreComplete(LoadMoreListView.STATUS_LOADING);
                mPageId++;
                getData();
            }

            @Override
            public void initFooter(View view) {
                ImageView ivAnim = (ImageView) view.findViewById(R.id.iv_anim);
                FadingCircle cg = new FadingCircle();
                int color = getResources().getColor(R.color.tiffany);
                cg.setColor(color);
                ivAnim.setBackgroundDrawable(cg);
                cg.start();
            }
        });
    }

    private void getData() {
        if (!loading) {
            loadingMore = true;
            HttpUtil.getInstance().get("http://web.juhe.cn:8080/finance/stock/shall?key=d87dcd62cf7671e3bdc99f37ceba16af&page=" + mPageId, new HttpUtil.Callback() {
                @Override
                public void onFail(String error) {
                    ((LoadMoreListView) mListView.getListView()).onLoadMoreComplete(LoadMoreListView.STATUS_NETWORK_ERROR);
                    loadingMore = false;
                }

                @Override
                public void onSuccess(String response) {
                    Response res = JSON.parseObject(response, Response.class);
                    Logger.d(response);
                    DataWrapper wrapper = JSON.parseObject(res.getResult(), DataWrapper.class);
                    mPageSize = Integer.parseInt(wrapper.getNum());
                    mTotalPage = (Integer.parseInt(wrapper.getTotalCount()) / mPageSize) + 1;
                    List<Stock> stocks = JSON.parseArray(wrapper.getData(), Stock.class);
                    if (stocks.isEmpty()) {
                        ((LoadMoreListView) mListView.getListView()).onLoadMoreComplete(LoadMoreListView.STATUS_NO_CONTENT);
                    }
                    for (Stock stock : stocks) {
                        mDatas.add(new MyModel(new Model(stock.getName(), stock.getTrade(), stock.getChangepercent(), stock.getPricechange(), ((float) Integer.parseInt(stock.getVolume()) / 10000) + "", stock.getOpen(), stock.getHigh(), stock.getLow()), false));
                    }
                    notifyAdapter();
                    loadingMore = false;
                }
            }, 0);
        }
    }

    private void updateData() {
        final int pageId = mListView.getListView().getFirstVisiblePosition() / mPageSize + 1;
        HttpUtil.getInstance().get("http://web.juhe.cn:8080/finance/stock/shall?key=d87dcd62cf7671e3bdc99f37ceba16af&page=" + pageId, new HttpUtil.Callback() {
            @Override
            public void onFail(String error) {
                loading = false;
            }

            @Override
            public void onSuccess(String response) {
                Response res = JSON.parseObject(response, Response.class);
                DataWrapper wrapper = JSON.parseObject(res.getResult(), DataWrapper.class);
                mTotalPage = (Integer.parseInt(wrapper.getTotalCount()) / Integer.parseInt(wrapper.getNum())) + 1;
                List<Stock> stocks = JSON.parseArray(wrapper.getData(), Stock.class);
                if (stocks.isEmpty()) {
                    ((LoadMoreListView) mListView.getListView()).onLoadMoreComplete(LoadMoreListView.STATUS_NO_CONTENT);
                }
                for (int i = 0; i < stocks.size(); i++) {
                    mDatas.get((pageId - 1) * mPageSize + i).getModel().update(stocks.get(i));
                }
                notifyAdapter();
                loading = false;
            }
        }, 0);
    }

    private void notifyAdapter() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    if (isRefresh) {
                        mPtrFrameLayout.refreshComplete();
                    }
                    if (mPageId == mTotalPage) {
                        ((LoadMoreListView) mListView.getListView()).onLoadMoreComplete(LoadMoreListView.STATUS_NO_MORE);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRefreshTask != null) {
            mRefreshTask.cancel();
        }
    }

    @Override
    protected void onRefresh(EnhancePtrFrameLayout frame) {
        super.onRefresh(frame);
        refresh();
    }

    private void refresh() {
        mDatas.clear();
        mPageId = 1;
        isRefresh = true;
        getData();
        ((LoadMoreListView) mListView.getListView()).resetStatus();
    }

    @Override
    protected boolean checkCanRefresh(EnhancePtrFrameLayout frame, View content, View header) {
        SlideListView view = (SlideListView) content;
        ListView absListView = view.getListView();
        return !(absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
    }
}
