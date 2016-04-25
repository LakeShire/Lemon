package com.github.lakeshire.lemon.fragment.examples;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.adapter.MyCommonRcvAdapter;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemon.model.DemoModel;
import com.github.lakeshire.lemon.view.recyclerview.DividerGridItemDecoration;
import com.github.lakeshire.lemon.view.recyclerview.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import it.gmariotti.recyclerview.itemanimator.SlideInOutBottomItemAnimator;
import it.gmariotti.recyclerview.itemanimator.SlideInOutLeftItemAnimator;
import it.gmariotti.recyclerview.itemanimator.SlideInOutRightItemAnimator;
import it.gmariotti.recyclerview.itemanimator.SlideInOutTopItemAnimator;
import it.gmariotti.recyclerview.itemanimator.SlideScaleInOutRightItemAnimator;

/**
 * 
 * 这个页面展示了通用容器CommonAdapter的使用
 * 方便的使用适配器，根据模型类型不同使用不同的布局
 *
 */
public class RecycleListFragment extends BasePullFragment {

    private ArrayList<DemoModel> data = new ArrayList();

    @Bind(R.id.list)
    RecyclerView listView;
    private DividerItemDecoration mItemDecoration;
    private DividerGridItemDecoration mGridItemDecoration;
    private MyCommonRcvAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycle_list;
    }

    @Override
    public void initUi() {
        super.initUi();
        setHasOptionsMenu(true);
        initList();
        mAdapter = getAdapter(data);
        listView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new MyCommonRcvAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "click " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initGrid() {
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        listView.setLayoutManager(layoutManager);
        listView.removeItemDecoration(mItemDecoration);
        mGridItemDecoration = new DividerGridItemDecoration(getActivity());
        listView.addItemDecoration(mGridItemDecoration);
        listView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        listView.removeItemDecoration(mGridItemDecoration);
        mItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        listView.addItemDecoration(mItemDecoration);
        listView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void loadData() {
        super.loadData();
        data.clear();
        data.add(new DemoModel("吉安娜", "image"));
        data.add(new DemoModel("安度因", "image"));
        data.add(new DemoModel("乌瑟尔", "image"));
        data.add(new DemoModel("瓦利拉", "image"));
        data.add(new DemoModel("古尔丹", "image"));
        data.add(new DemoModel("玛法里奥", "image"));
        listView.getAdapter().notifyDataSetChanged();
    }

    /**
     * CommonAdapter的类型和item的类型是一致的
     * 这里的都是{@link DemoModel}
     *
     * 多种类型的type
     */
    private MyCommonRcvAdapter getAdapter(List<DemoModel> data) {
        return new MyCommonRcvAdapter(data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recycler_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_grid:
                if (item.getTitle().equals("网格")) {
                    item.setTitle("列表");
                    initGrid();
                } else {
                    item.setTitle("网格");
                    initList();
                }
                break;
            case R.id.action_add_default:
                data.add(new DemoModel("Unknown", "image"));
                mAdapter.notifyItemInserted(0);
                break;
            case R.id.action_add_slide_in_out:
                break;
            case R.id.action_add_slide_in_out_bottom:
                listView.setItemAnimator(new SlideInOutBottomItemAnimator(listView));
                data.add(new DemoModel("Unknown", "image"));
                mAdapter.notifyItemInserted(0);
                break;
            case R.id.action_add_slide_in_out_left:
                listView.setItemAnimator(new SlideInOutLeftItemAnimator(listView));
                data.add(new DemoModel("Unknown", "image"));
                mAdapter.notifyItemInserted(0);
                break;
            case R.id.action_add_slide_in_out_right:
                listView.setItemAnimator(new SlideInOutRightItemAnimator(listView));
                data.add(new DemoModel("Unknown", "image"));
                mAdapter.notifyItemInserted(0);
                break;
            case R.id.action_add_slide_in_out_top:
                listView.setItemAnimator(new SlideInOutTopItemAnimator(listView));
                data.add(new DemoModel("Unknown", "image"));
                mAdapter.notifyItemInserted(0);
                break;
            case R.id.action_add_slide_scale_in_out:
                listView.setItemAnimator(new SlideScaleInOutRightItemAnimator(listView));
                data.add(new DemoModel("Unknown", "image"));
                mAdapter.notifyItemInserted(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
