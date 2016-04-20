package com.github.lakeshire.lemon.fragment.examples;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemon.view.tagview.Tag;
import com.github.lakeshire.lemon.view.tagview.TagListView;

import java.util.ArrayList;
import java.util.List;

public class TagViewFragment extends BasePullFragment {

    private final String[] titles = { "安全必备", "音乐", "父母学", "上班族必备",
            "360手机卫士", "QQ","输入法", "微信", "最美应用", "AndevUI", "蘑菇街" };
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
    }

    @Override
    public void loadData() {
        for (int i = 0; i < 10; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(true);
            tag.setTitle(titles[i]);
            mTags.add(tag);
        }
        mTagListView.setTags(mTags);
    }
}
