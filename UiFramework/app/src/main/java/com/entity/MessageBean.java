package com.entity;

/**
 * Created by fxw on 2017/10/28.
 */

public class MessageBean {

    /**
     * codeTextId : 12
     * creatime : 2017-10-28 14:44:53
     * isNewRecord : true
     * isNewRecordCustomId : false
     * keyword : 地方
     * text : 骨灰盒
     * type : text
     */

    private int codeTextId;
    private String creatime;
    private boolean isNewRecord;
    private boolean isNewRecordCustomId;
    private String keyword;
    private String text;
    private String type;

    public int getCodeTextId() {
        return codeTextId;
    }

    public void setCodeTextId(int codeTextId) {
        this.codeTextId = codeTextId;
    }

    public String getCreatime() {
        return creatime;
    }

    public void setCreatime(String creatime) {
        this.creatime = creatime;
    }

    public boolean isIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public boolean isIsNewRecordCustomId() {
        return isNewRecordCustomId;
    }

    public void setIsNewRecordCustomId(boolean isNewRecordCustomId) {
        this.isNewRecordCustomId = isNewRecordCustomId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
