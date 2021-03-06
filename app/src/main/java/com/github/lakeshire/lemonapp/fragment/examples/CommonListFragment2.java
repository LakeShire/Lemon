package com.github.lakeshire.lemonapp.fragment.examples;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lakeshire.lemon.fragment.base.BaseFragment;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.pulltozoomview.PullToZoomView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kale.adapter.CommonAdapter;
import kale.adapter.item.AdapterItem;

/**
 *
 * 这个页面展示了通用容器CommonAdapter的使用
 * 方便的使用适配器，根据模型类型不同使用不同的布局
 *
 */
public class CommonListFragment2 extends BaseFragment {

    private String title;
    private CommonAdapter<DemoModel> mAdapter;
    private ArrayList<DemoModel> data = new ArrayList();

    ListView listView;

    public CommonListFragment2(String title) {
        super();
        this.title = title;
    }

    public CommonListFragment2() {
        super();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_common_list;
    }

    @Override
    public void initUi() {
        super.initUi();
        mAdapter = getAdapter(data);
        listView = (ListView) find(R.id.list);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        data.clear();
        data.add(new DemoModel("吉安娜", "text"));
        data.add(new DemoModel("安度因", "text"));
        data.add(new DemoModel("乌瑟尔", "image"));
        data.add(new DemoModel("瓦利拉", "button"));
        data.add(new DemoModel("古尔丹", "text"));
        data.add(new DemoModel("玛法里奥", "image"));
        data.add(new DemoModel("闪光", "text"));
        data.add(new DemoModel("德哈卡", "text"));
        data.add(new DemoModel("阿尔萨斯", "button"));
        data.add(new DemoModel("阿塔尼斯", "image"));
        data.add(new DemoModel("凯瑞甘", "button"));
        data.add(new DemoModel("吉安娜", "text"));
        data.add(new DemoModel("安度因", "text"));
        data.add(new DemoModel("乌瑟尔", "image"));
        data.add(new DemoModel("瓦利拉", "button"));
        data.add(new DemoModel("卢西奥", "text"));
        data.add(new DemoModel("卢西奥", "image"));
        data.add(new DemoModel("卢西奥", "text"));
        data.add(new DemoModel("卢西奥", "text"));
        data.add(new DemoModel("卢西奥", "button"));
        data.add(new DemoModel("卢西奥", "image"));
        data.add(new DemoModel("卢西奥", "button"));
        mAdapter.notifyDataSetChanged();
    }

    /**
     * CommonAdapter的类型和item的类型是一致的
     * 这里的都是{@link DemoModel}
     *
     * 多种类型的type
     */
    private CommonAdapter<DemoModel> getAdapter(List<DemoModel> data) {
        return new CommonAdapter<DemoModel>(data, 3) {

            @Override
            public Object getItemType(DemoModel demoModel) {
                return demoModel.type;
            }

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                switch (((String) type)) {
                    case "text":
                        return new TextItem();
                    case "button":
                        return new ButtonItem();
                    case "image":
                        return new ImageItem();
                    default:
                        throw new IllegalArgumentException("不合法的type");
                }
            }
        };
    }

    class TextItem implements AdapterItem<DemoModel> {

        @Override
        public int getLayoutResId() {
            return R.layout.item_common_text;
        }

        TextView textView;

        @Override
        public void bindViews(View root) {
            textView = (TextView) root.findViewById(R.id.textview);
        }

        @Override
        public void setViews() { }

        @Override
        public void handleData(DemoModel model, int position) {
            textView.setText(model.content);
        }
    }

    class ImageItem implements AdapterItem<DemoModel> {

        @Override
        public int getLayoutResId() {
            return R.layout.item_common_image;
        }

        ImageView imageView;

        @Override
        public void bindViews(View root) {
            imageView = (ImageView) root.findViewById(R.id.imageview);
        }

        @Override
        public void setViews() { }

        @Override
        public void handleData(DemoModel model, int position) {
            imageView.setImageResource(R.drawable.path_music);
        }
    }

    class ButtonItem implements AdapterItem<DemoModel> {

        @Override
        public int getLayoutResId() {
            return R.layout.item_common_button;
        }

        Button button;

        @Override
        public void bindViews(View root) {
            button = (Button) root.findViewById(R.id.button);
        }

        @Override
        public void setViews() { }

        @Override
        public void handleData(DemoModel model, final int position) {
            button.setText(model.content);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "点击了按钮: " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class DemoModel {
        public String content;
        public String type;
        public DemoModel(String content, String type) {
            this.content = content;
            this.type = type;
        }
    }

    public boolean checkTop() {
        ListView absListView = listView;
        return !(absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void scroll(int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            listView.scrollListBy(y);
        } else {
            listView.scrollTo(0, y);
        }
    }

    public void onRefresh(final PullToZoomView ptrView) {
        Toast.makeText(getActivity(), "刷新页面", Toast.LENGTH_SHORT).show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ptrView.onRefreshCompleted();

                        }
                    });
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000);
    }
}
