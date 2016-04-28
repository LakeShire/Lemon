package com.github.lakeshire.lemon.fragment.examples;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemon.view.pulltofresh.EnhancePtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class StickyHeaderListFragment extends BasePullFragment {

    private String title;
    private MyAdapter mAdapter;
    private ArrayList<DemoModel> data = new ArrayList();

    @Bind(R.id.list)
    ExpandableStickyListHeadersListView listView;

    public StickyHeaderListFragment() {
        super();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sticky_list;
    }

    @Override
    public void initUi() {
        super.initUi();
//        mAdapter = getAdapter(data);
        mAdapter = new MyAdapter(getActivity(), data);
        listView.setAdapter(mAdapter);
        listView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if (listView.isHeaderCollapsed(headerId)) {
                    listView.expand(headerId);
                } else {
                    listView.collapse(headerId);
                }
            }
        });
    }

    public class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        private List<DemoModel> countries;
        private LayoutInflater inflater;

        public MyAdapter(Context context, List<DemoModel> data) {
            inflater = LayoutInflater.from(context);
            countries = data;
        }

        @Override
        public int getCount() {
            return countries.size();
        }

        @Override
        public Object getItem(int position) {
            return countries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_common_text, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(countries.get(position).content);

            return convertView;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.list_item_header, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.textview);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            //set header text as first char in name
            String headerText = "" + countries.get(position).content.subSequence(0, 1).charAt(0);
            holder.text.setText(headerText);
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            //return the first character of the country as ID because this is what headers are based upon
            return countries.get(position).content.subSequence(0, 1).charAt(0);
        }

        class HeaderViewHolder {
            TextView text;
        }

        class ViewHolder {
            TextView text;
        }

    }

    @Override
    public void loadData() {
        super.loadData();
        data.clear();
        data.add(new DemoModel("吉安娜", "text"));
        data.add(new DemoModel("古尔丹", "text"));
        data.add(new DemoModel("玛法里奥", "image"));
        data.add(new DemoModel("闪光", "text"));
        data.add(new DemoModel("德哈卡", "text"));
        data.add(new DemoModel("阿尔萨斯", "button"));
        data.add(new DemoModel("安度因", "text"));
        data.add(new DemoModel("乌瑟尔", "image"));
        data.add(new DemoModel("瓦利拉", "button"));
        data.add(new DemoModel("阿塔尼斯", "image"));
        data.add(new DemoModel("凯瑞甘", "button"));
        mAdapter.notifyDataSetChanged();
    }

    class DemoModel {
        public String content;
        public String type;
        public DemoModel(String content, String type) {
            this.content = content;
            this.type = type;
        }
    }

    @Override
    protected boolean checkCanRefresh(EnhancePtrFrameLayout frame, View content, View header) {
        ListView absListView = listView.getWrappedList();
        return !(absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
    }
}
