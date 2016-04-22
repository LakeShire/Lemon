package com.github.lakeshire.lemon.fragment.examples;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.base.BaseFragment;
import com.github.lakeshire.lemon.util.BitmapUtil;
import com.github.lakeshire.lemon.util.ImageUtil;
import com.github.lakeshire.lemon.util.ScreenUtil;
import com.github.lakeshire.lemon.view.BlurableImageView;
import com.github.lakeshire.lemon.view.pulltozoom.PullToZoomListViewEx;

import java.util.ArrayList;
import java.util.List;

import kale.adapter.CommonAdapter;
import kale.adapter.item.AdapterItem;

/**
 *
 * 这个页面展示了通用容器CommonAdapter的使用
 * 方便的使用适配器，根据模型类型不同使用不同的布局
 *
 */
public class PullToZoomFragment extends BaseFragment {

    private String title;
    private CommonAdapter<DemoModel> mAdapter;
    private ArrayList<DemoModel> data = new ArrayList();
    private PullToZoomListViewEx listView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pulltozoom;
    }

    @Override
    public void initUi() {
        super.initUi();

//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAdapter = getAdapter(data);
        listView = (PullToZoomListViewEx) find(R.id.listview);
        listView.setAdapter(mAdapter);

        int mScreenWidth = ScreenUtil.getScreenWidth(getActivity());
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (mScreenWidth * 16 / 16f));
        listView.setHeaderLayoutParams(localObject);

        BlurableImageView zoomView = (BlurableImageView) listView.getZoomView();
        Bitmap bitmap = BitmapUtil.reduce(getContext(), R.drawable.image3, 256, 256);
        zoomView.blur(new BitmapDrawable(getActivity().getResources(), bitmap), "blur", true);

        View headerView = listView.getHeaderView();
        ((TextView) headerView.findViewById(R.id.tv_user_name)).setText("Lakeshire");
//        View header = View.inflate(getActivity(), R.layout.view_header, null);
//        ImageView ivImage = (ImageView) header.findViewById(R.id.image);
//        Bitmap bitmap = BitmapUtil.reduce(getContext(), R.drawable.image1, 256, 256);
//        ivImage.setImageBitmap(bitmap);
//        listView.setHeaderView(header);
//        listView.setZoomView(ivImage);
        ImageView ivAvatar = (ImageView) headerView.findViewById(R.id.iv_user_head);
        ImageUtil.getInstance(getActivity()).setImage(ivAvatar, R.drawable.image2, 256, 256, true);
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
}
