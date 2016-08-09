package com.github.lakeshire.slidelistview;

/**
 * 持有数据模型以及该项是否被选中
 */
public class CheckableModel<T> {
    private T model;
    private boolean checked;

    public CheckableModel(T model, boolean checked) {
        this.model = model;
        this.checked = checked;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
