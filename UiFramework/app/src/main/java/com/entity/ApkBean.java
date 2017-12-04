package com.entity;

/**
 * Created by vicmob_yf002 on 2017/6/24.
 * APK更新实体
 */
public class ApkBean {


    private WxversionBean wxversion;

    public WxversionBean getWxversion() {
        return wxversion;
    }

    public void setWxversion(WxversionBean wxversion) {
        this.wxversion = wxversion;
    }

    public static class WxversionBean {
        /**
         * versionCode :版本code
         * versionName :版本名称
         * versionSpace :版本空间
         * versiondetail :版本细节
         */

        private int versionCode;
        private String versionName;
        private int versionSpace;
        private String versiondetail;

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionSpace() {
            return versionSpace;
        }

        public void setVersionSpace(int versionSpace) {
            this.versionSpace = versionSpace;
        }

        public String getVersiondetail() {
            return versiondetail;
        }

        public void setVersiondetail(String versiondetail) {
            this.versiondetail = versiondetail;
        }
    }
}
