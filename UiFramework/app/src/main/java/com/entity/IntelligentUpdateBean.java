package com.entity;

import java.util.List;

/**
 * Created by fxw on 2017/10/27.
 */

public class IntelligentUpdateBean {

    private List<VicCodetextBean> vic_codetext;

    public List<VicCodetextBean> getVic_codetext() {
        return vic_codetext;
    }

    public void setVic_codetext(List<VicCodetextBean> vic_codetext) {
        this.vic_codetext = vic_codetext;
    }

    public static class VicCodetextBean {
        /**
         * id : null
         * isNewRecord : true
         * remarks : null
         * createDate : null
         * updateDate : null
         * codeTextId : 111
         * keyword : 哦了到家咯
         * text : 狗狗
         * creatime : 2017-10-26 21:04:03
         * type : text
         * isNewRecordCustomId : false
         */

        private Object id;
        private boolean isNewRecord;
        private Object remarks;
        private Object createDate;
        private Object updateDate;
        private int codeTextId;
        private String keyword;
        private String text;
        private String creatime;
        private String type;
        private boolean isNewRecordCustomId;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Object getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Object createDate) {
            this.createDate = createDate;
        }

        public Object getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Object updateDate) {
            this.updateDate = updateDate;
        }

        public int getCodeTextId() {
            return codeTextId;
        }

        public void setCodeTextId(int codeTextId) {
            this.codeTextId = codeTextId;
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

        public String getCreatime() {
            return creatime;
        }

        public void setCreatime(String creatime) {
            this.creatime = creatime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isIsNewRecordCustomId() {
            return isNewRecordCustomId;
        }

        public void setIsNewRecordCustomId(boolean isNewRecordCustomId) {
            this.isNewRecordCustomId = isNewRecordCustomId;
        }
    }
}
