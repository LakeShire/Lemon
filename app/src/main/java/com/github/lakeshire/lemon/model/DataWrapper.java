package com.github.lakeshire.lemon.model;

/**
 * Created by nali on 2016/4/13.
 */
public class DataWrapper {
    String totalCount;
    String page;
    String num;
    String data;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getData() {
        return data;
    }

    public void setData(String stocks) {
        this.data = stocks;
    }
}
