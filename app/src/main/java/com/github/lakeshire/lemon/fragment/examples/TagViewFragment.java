package com.github.lakeshire.lemon.fragment.examples;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemon.view.tagview.Tag;
import com.github.lakeshire.lemon.view.tagview.TagListView;
import com.github.lakeshire.lemon.view.tagview.TagView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class TagViewFragment extends BasePullFragment {

    private final String[] titles = { "闪光", "秘法师李敏", "死灵法师祖尔", "第一只树妖露娜拉", "吉安娜", "凯尔萨斯","维拉", "泰凯斯", "泰兰德", "玛法里奥", "迪亚波罗", "高阶天堂的正义天使泰瑞尔" };
    private TagListView mTagListView;
    private List<Tag> mTags = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tag;
    }

    @Override
    public void initUi() {
        super.initUi();
        mTagListView = (TagListView) find(R.id.tagview);
        mTagListView.setDeleteMode(true);
        mTagListView.setCallback(new TagListView.Callback() {
            @Override
            public void onTagCheckedChanged(TagView tagView, Tag tag) {
                Logger.d(tag.getTitle() + (tag.isChecked() ? "被选中了" : "被取消选中了"));
            }

            @Override
            public void onDeleteClicked(Tag t) {
                mTags.remove(t);
            }
        });
        mTagListView.setMaxChecked(3);
    }

    @Override
    public void loadData() {
        for (int i = 0; i < titles.length; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(i % 2 == 0 ? true : false);
            tag.setTitle(titles[i]);
            mTags.add(tag);
        }
        mTagListView.setTags(mTags, true);
    }
}
