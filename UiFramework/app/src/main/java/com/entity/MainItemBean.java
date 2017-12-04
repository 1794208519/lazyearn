package com.entity;

/**
 * Created by Eren on 2017/5/4.
 * 主页gridview布局实体
 */
public class MainItemBean {
    private String icon;
    private int imageId;

    public MainItemBean(int imageId, String icon) {
        this.imageId = imageId;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
