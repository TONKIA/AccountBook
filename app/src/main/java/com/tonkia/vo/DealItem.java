package com.tonkia.vo;

import org.litepal.crud.LitePalSupport;

public class DealItem extends LitePalSupport {
    public static final int OUTPUT = 0;
    public static final int INPUT = 1;
    //0是支出 1是收入
    private int type;
    private String itemName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
