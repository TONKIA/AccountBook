package com.tonkia.vo;

public class ViewType {
    //用于区分RecycleView里的布局
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_ITEM = 1;
    public int tpye;
    public int index;

    public ViewType(int tpye, int index) {
        this.tpye = tpye;
        this.index = index;
    }

}

