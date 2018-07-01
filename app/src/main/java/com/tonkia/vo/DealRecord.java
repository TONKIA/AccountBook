package com.tonkia.vo;


import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class DealRecord extends LitePalSupport {
    private DealItem item;
    private float cost;
    private long time;
    private String itemName;
    private String desc;

    public DealItem getItem() {
        return item;
    }

    public void setItem(DealItem item) {
        this.item = item;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
