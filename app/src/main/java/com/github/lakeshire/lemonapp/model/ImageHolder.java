package com.github.lakeshire.lemonapp.model;

/**
 * Created by nali on 2016/4/21.
 */
public class ImageHolder {
    public int local = 0;
    public String url = "";

    public ImageHolder(String url) {
        this.url = url;
    }

    public ImageHolder(int res) {
        this.local = res;
    }
}
