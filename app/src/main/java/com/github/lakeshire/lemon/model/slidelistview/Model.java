package com.github.lakeshire.lemon.model.slidelistview;

import com.github.lakeshire.lemon.model.Stock;

/**
 * Created by nali on 2016/4/12.
 */
public class Model {
    public String data7;
    public String title;
    public String data1;
    public String data2;
    public String data3;
    public String data4;
    public String data5;
    public String data6;

    public static final int NO_CHANGE = 0;
    public static final int INCREASE = 1;
    public static final int DECREASE = 2;
    public int change = NO_CHANGE;

    public Model(String title, String data1, String data2, String data3, String data4, String data5, String data6, String data7) {
        this.title = title;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
    }

    public void update(Stock stock) {
        this.title = stock.getName();
        this.data1 = stock.getTrade();
        this.data2 = stock.getChangepercent();
        this.data3 = stock.getPricechange();
        this.data4 = stock.getVolume();
        this.data5 = stock.getOpen();
        this.data6 = stock.getHigh();
        this.data7 = stock.getLow();
        float changeVal = Float.parseFloat(stock.getTrade()) - Float.parseFloat(this.data1);
        if (changeVal > 0) {
            change = INCREASE;
        } else if (changeVal < 0) {
            change = DECREASE;
        } else {
            change = NO_CHANGE;
        }
    }
}
