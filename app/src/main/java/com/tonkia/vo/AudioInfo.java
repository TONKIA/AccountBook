package com.tonkia.vo;

import org.litepal.crud.LitePalSupport;

public class AudioInfo extends LitePalSupport {

    private String path;
    private long time;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
