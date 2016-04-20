package com.github.lakeshire.lemon.view.tagview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.github.lakeshire.lemon.R;

public class TagView extends LinearLayout implements Checkable {

    private Context context;
    private boolean mCheckEnable = true;
    private ToggleButton tbName;
    private ImageView ivDelete;
    private boolean checked;
    private boolean deleteMode = false;

    public void updateDrawable() {
        this.refreshDrawableState();
    }

    public interface Callback {
        void onCheckedChanged(TagView view, boolean b);
        void onDeleteClicked(TagView view);
    }
    private Callback listener;

    public TagView(Context paramContext) {
        super(paramContext);
        init();
    }

    public TagView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.context = paramContext;
        init();
    }

    public TagView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, 0);
        init();
    }

    private void init() {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tbName = new ToggleButton(context);
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, 64);
        tbName.setLayoutParams(lp);
        tbName.setTextSize(16);
        tbName.setTextOff(null);
        tbName.setTextOn(null);
        tbName.setBackgroundDrawable(null);
        tbName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setChecked(isChecked);
            }
        });
        addView(tbName);
    }

    private static final int[] CheckedStateSet = {
            android.R.attr.state_checked,
    };

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }

    public void setCheckEnable(boolean paramBoolean) {
        this.mCheckEnable = paramBoolean;
        if (!this.mCheckEnable) {
            setChecked(false);
        }
    }

    public void setChecked(boolean paramBoolean) {
        if (this.mCheckEnable) {
            this.checked = paramBoolean;
            tbName.setChecked(paramBoolean);
            if (listener != null) {
                listener.onCheckedChanged(this, paramBoolean);
            }
        }
    }

    public void setCheckedWithNoNotify(boolean paramBoolean) {
        if (this.mCheckEnable) {
            this.checked = paramBoolean;
            tbName.setChecked(paramBoolean);
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        this.checked = !this.checked;
    }

    public void setText(String text) {
        this.tbName.setText(text);
    }

    public void setTextColor(int color) {
        this.tbName.setTextColor(color);
    }

    public void setCallback(Callback listener) {
        this.listener = listener;
    }

    public void setDeleteMode(boolean mIsDeleteMode) {
        this.deleteMode = mIsDeleteMode;
        if (deleteMode) {
            LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            ivDelete = new ImageView(context);
            ivDelete.setImageResource(R.drawable.tag_delete);
            ivDelete.setLayoutParams(lp);
            ivDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeleteClicked(TagView.this);
                    }
                }
            });
            addView(ivDelete);
        }
    }
}
