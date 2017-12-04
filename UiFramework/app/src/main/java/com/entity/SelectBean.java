package com.entity;

import java.util.Date;

/**
 * Created by qq944 on 2017/11/2.
 */

public class SelectBean {
    private String keyword;
    private String date;
    private int start;
    private int numOfpages;

    public SelectBean() {

    }
    public SelectBean(String keyword, String date, int start, int numOfpages) {
        this.keyword = keyword;
        this.date = date;
        this.start = start;
        this.numOfpages = numOfpages;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getNumOfpages() {
        return numOfpages;
    }

    public void setNumOfpages(int numOfpages) {
        this.numOfpages = numOfpages;
    }
}
