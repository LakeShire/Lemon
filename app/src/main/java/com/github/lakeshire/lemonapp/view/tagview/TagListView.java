package com.github.lakeshire.lemonapp.view.tagview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;

import com.github.lakeshire.lemonapp.R;

import java.util.ArrayList;
import java.util.List;

public class TagListView extends FlowLayout implements OnClickListener {

    private Context context;
    private boolean mIsDeleteMode;
    private Callback mListener;
    private OnTagClickListener mOnTagClickListener;
    private int mTagViewBackgroundResId;
    private int mTagViewTextColorResId;
    private final List<Tag> mTags = new ArrayList<Tag>();
    private int checkedCount = 0;

    public int getMaxChecked() {
        return maxChecked;
    }

    public void setMaxChecked(int maxChecked) {
        this.maxChecked = maxChecked;
    }

    private int maxChecked = 0;

    /**
     * @param context
     */
    public TagListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    /**
     * @param context
     * @param attributeSet
     */
    public TagListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    /**
     * @param context
     * @param attributeSet
     * @param defStyle
     */
    public TagListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    @Override
    public void onClick(View v) {
        if ((v instanceof TagView)) {
            Tag localTag = (Tag) v.getTag();
            if (this.mOnTagClickListener != null) {
                this.mOnTagClickListener.onTagClick((TagView) v, localTag);
            }
        }
    }

    private void init() {

    }

    private void inflateTagView(final Tag t, boolean b) {

        TagView localTagView = (TagView) View.inflate(getContext(), R.layout.view_tag, null);
        localTagView.setText(t.getTitle());
        localTagView.setTag(t);

        if (mTagViewTextColorResId <= 0) {
            int c = getResources().getColor(R.color.blue);
            localTagView.setTextColor(c);

        }

        if (mTagViewBackgroundResId <= 0) {
            mTagViewBackgroundResId = R.drawable.tag_bg;
            localTagView.setBackgroundResource(mTagViewBackgroundResId);
        }

        if (t.isChecked()) {
            checkedCount++;
            if (checkedCount > maxChecked) {
                t.setChecked(false);
                checkedCount--;
            }
        }
        localTagView.setCheckEnable(b);
        localTagView.setChecked(t.isChecked());

        if (mIsDeleteMode) {
            int k = (int) TypedValue.applyDimension(1, 5.0F, getContext().getResources().getDisplayMetrics());
            localTagView.setPadding(localTagView.getPaddingLeft(), localTagView.getPaddingTop(), k, localTagView.getPaddingBottom());
            localTagView.setDeleteMode(mIsDeleteMode);
        }
        if (t.getBackgroundResId() > 0) {
            localTagView.setBackgroundResource(t.getBackgroundResId());
        }
        if ((t.getLeftDrawableResId() > 0) || (t.getRightDrawableResId() > 0)) {
//            localTagView.setCompoundDrawablesWithIntrinsicBounds(t.getLeftDrawableResId(), 0, t.getRightDrawableResId(), 0);
        }
        localTagView.setOnClickListener(this);
        localTagView.setCallback(new TagView.Callback() {
            @Override
            public void onCheckedChanged(TagView view, boolean b) {
                t.setChecked(b);
                if (!b) {
                    checkedCount--;
                    t.setChecked(false);
                    if (TagListView.this.mListener != null) {
                        TagListView.this.mListener.onTagCheckedChanged(view, t);
                    }
                } else {
                    checkedCount++;
                    t.setChecked(true);
                    if (checkedCount > maxChecked) {
                        view.setCheckedWithNoNotify(false);
                        t.setChecked(false);
                        checkedCount--;
                    } else {
                        if (TagListView.this.mListener != null) {
                            TagListView.this.mListener.onTagCheckedChanged(view, t);
                        }
                    }
                }
                view.updateDrawable();
            }

            @Override
            public void onDeleteClicked(TagView view) {
                if (view.isChecked()) {
                    checkedCount--;
                }
                removeView(view);
                TagListView.this.mListener.onDeleteClicked(t);
            }
        });
        addView(localTagView);
    }

    public void addTag(int i, String s) {
        addTag(i, s, false);
    }

    public void addTag(int i, String s, boolean b) {
        addTag(new Tag(i, s), b);
    }

    public void addTag(Tag tag) {
        addTag(tag, false);
    }

    public void addTag(Tag tag, boolean b) {
        mTags.add(tag);
        inflateTagView(tag, b);
    }

    public void addTags(List<Tag> lists) {
        addTags(lists, false);
    }

    public void addTags(List<Tag> lists, boolean b) {
        for (int i = 0; i < lists.size(); i++) {
            addTag((Tag) lists.get(i), b);
        }
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public View getViewByTag(Tag tag) {
        return findViewWithTag(tag);
    }

    public void removeTag(Tag tag) {
        mTags.remove(tag);
        removeView(getViewByTag(tag));
    }

    public void setDeleteMode(boolean b) {
        mIsDeleteMode = b;
    }

    public void setCallback(Callback onTagCheckedChangedListener) {
        mListener = onTagCheckedChangedListener;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
    }

    public void setTagViewBackgroundRes(int res) {
        mTagViewBackgroundResId = res;
    }

    public void setTagViewTextColorRes(int res) {
        mTagViewTextColorResId = res;
    }

    public void setTags(List<? extends Tag> lists) {
        setTags(lists, false);
    }

    public void setTags(List<? extends Tag> lists, boolean b) {
        removeAllViews();
        mTags.clear();
        for (int i = 0; i < lists.size(); i++) {
            addTag((Tag) lists.get(i), b);
        }
    }

    public static abstract interface Callback {
        public abstract void onTagCheckedChanged(TagView tagView, Tag tag);
        void onDeleteClicked(Tag t);
    }

    public static abstract interface OnTagClickListener {
        public abstract void onTagClick(TagView tagView, Tag tag);
    }

}
