package com.entity;

import java.util.List;

/**
 * Created by fxw on 2017/7/10.
 */

public class AccountUpdateBean {

    private List<AutoAccountBean> auto_account;

    public List<AutoAccountBean> getAuto_account() {
        return auto_account;
    }

    public void setAuto_account(List<AutoAccountBean> auto_account) {
        this.auto_account = auto_account;
    }

    public static class AutoAccountBean {
        /**
         * id : null
         * isNewRecord : true
         * remarks : null
         * createDate : null
         * updateDate : null
         * accountId : 3
         * account : 17365319236
         * password : wxk888888
         * devices : null
         * isNewRecordCustomId : false
         */

        private Object id;
        private boolean isNewRecord;
        private Object remarks;
        private Object createDate;
        private Object updateDate;
        private int accountId;
        private String account;
        private String password;
        private Object devices;
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

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Object getDevices() {
            return devices;
        }

        public void setDevices(Object devices) {
            this.devices = devices;
        }

        public boolean isIsNewRecordCustomId() {
            return isNewRecordCustomId;
        }

        public void setIsNewRecordCustomId(boolean isNewRecordCustomId) {
            this.isNewRecordCustomId = isNewRecordCustomId;
        }
    }
}
