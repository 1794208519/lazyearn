package com.entity;

/**
 * Created by qq944 on 2017/10/17.
 */

public class LiaoTianJiLuBean {
    private String liaotianzhe;
    private String liaotianhaoyou;
    private String liaotianshijian;
    private String liaotianflag;
    private String liaotianneirong;

    public LiaoTianJiLuBean() {
    }

    public LiaoTianJiLuBean(String liaotianzhe, String liaotianhaoyou, String liaotianshijian, String liaotianflag, String liaotianneirong) {
        this.liaotianzhe = liaotianzhe;
        this.liaotianhaoyou = liaotianhaoyou;
        this.liaotianshijian = liaotianshijian;
        this.liaotianflag = liaotianflag;
        this.liaotianneirong = liaotianneirong;
    }

    public String getLiaotianzhe() {
        return liaotianzhe;
    }

    public void setLiaotianzhe(String liaotianzhe) {
        this.liaotianzhe = liaotianzhe;
    }

    public String getLiaotianhaoyou() {
        return liaotianhaoyou;
    }

    public void setLiaotianhaoyou(String liaotianhaoyou) {
        this.liaotianhaoyou = liaotianhaoyou;
    }

    public String getLiaotianshijian() {
        return liaotianshijian;
    }

    public void setLiaotianshijian(String liaotianshijian) {
        this.liaotianshijian = liaotianshijian;
    }

    public String getLiaotianflag() {
        return liaotianflag;
    }

    public void setLiaotianflag(String liaotianflag) {
        this.liaotianflag = liaotianflag;
    }

    public String getLiaotianneirong() {
        return liaotianneirong;
    }

    public void setLiaotianneirong(String liaotianneirong) {
        this.liaotianneirong = liaotianneirong;
    }
}
