package com.github.lakeshire.lemonapp.fragment.examples;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.github.lakeshire.lemon.util.HttpUtil;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.lemonapp.adapter.morelistview.MoreItemAdapter;
import com.github.lakeshire.lemonapp.adapter.morelistview.MyMoreItemAdapter;
import com.github.lakeshire.lemonapp.fragment.base.BasePullFragment;
import com.github.lakeshire.lemonapp.model.DataWrapper;
import com.github.lakeshire.lemonapp.model.MoreModel;
import com.github.lakeshire.lemonapp.model.Response;
import com.github.lakeshire.lemonapp.model.Stock;
import com.github.lakeshire.lemonapp.model.slidelistview.Model;
import com.github.lakeshire.lemonapp.view.LoadMoreListView;
import com.github.lakeshire.lemonapp.view.morelistview.MoreListView;
import com.github.lakeshire.lemonapp.view.pulltofresh.EnhancePtrFrameLayout;
import com.github.ybq.android.spinkit.style.FadingCircle;

import java.util.ArrayList;
import java.util.List;

public class MoreListViewFragment extends BasePullFragment {

    private MoreListView mListView;
    private ArrayList<MoreModel> mDatas = new ArrayList();
    private MyMoreItemAdapter mAdapter;
    private boolean loading = false;
    private int mPageId = 1;
    private int mPageSize = 20;
    private int mTotalPage = 1;
    private boolean isRefresh = false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more_list;
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
    }

    private void initListView() {
        mListView = (MoreListView) find(R.id.list);

        mAdapter = new MyMoreItemAdapter(getActivity(), mDatas, R.layout.item_more);
        mListView.setAdapter(mAdapter);
        mListView.setLoadMoreCallback(new LoadMoreListView.Callback() {
            @Override
            public void loadMore() {
                mListView.onLoadMoreComplete(LoadMoreListView.STATUS_LOADING);
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
        mAdapter.setCb(new MoreItemAdapter.Callback() {
            @Override
            public void onExtraClicked(int res, int position) {
                if (res == R.id.delete) {
                    mDatas.remove(position);
                    mAdapter.notifyDataSetChanged();
                } else if (res == R.id.container) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemClicked(int position) {

            }

            @Override
            public void onItemLongClicked(int position) {

            }
        });
    }

    private void getData() {
        if (!loading) {
            loading = true;
            HttpUtil.getInstance().get("http://web.juhe.cn:8080/finance/stock/shall?key=d87dcd62cf7671e3bdc99f37ceba16af&page=" + mPageId, new HttpUtil.Callback() {
                @Override
                public void onFail(String error) {
                    mListView.onLoadMoreComplete(LoadMoreListView.STATUS_NETWORK_ERROR);
                    loading = false;
                }

                @Override
                public void onSuccess(String response) {
                    Response res = JSON.parseObject(response, Response.class);
                    DataWrapper wrapper = JSON.parseObject(res.getResult(), DataWrapper.class);
                    mPageSize = Integer.parseInt(wrapper.getNum());
                    mTotalPage = (Integer.parseInt(wrapper.getTotalCount()) / mPageSize) + 1;
                    List<Stock> stocks = JSON.parseArray(wrapper.getData(), Stock.class);
                    if (stocks.isEmpty()) {
                        mListView.onLoadMoreComplete(LoadMoreListView.STATUS_NO_CONTENT);
                    }
                    for (Stock stock : stocks) {
                        mDatas.add(new MoreModel(new Model(stock.getName(), stock.getTrade(), stock.getChangepercent(), stock.getPricechange(), ((float) Integer.parseInt(stock.getVolume()) / 10000) + "", stock.getOpen(), stock.getHigh(), stock.getLow()), false));
                    }
                    loading = false;
                    notifyAdapter();
                }
            }, 0);
        }
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
                    mListView.onLoadMoreComplete(LoadMoreListView.STATUS_NO_MORE);
                }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        mListView.resetStatus();
    }

    @Override
    protected boolean checkCanRefresh(EnhancePtrFrameLayout frame, View content, View header) {
        ListView absListView = mListView;
        return !(absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
    }
}
