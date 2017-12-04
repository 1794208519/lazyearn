package com.entity;

import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/6/27.
 * 总的地点添加实体
 */
public class AddressBean {

    private List<AutoDataBean> auto_data;

    public List<AutoDataBean> getAuto_data() {
        return auto_data;
    }

    public void setAuto_data(List<AutoDataBean> auto_data) {
        this.auto_data = auto_data;
    }

    public static class AutoDataBean {
        /**
         * address
         */
        private int dataId;
        private String hello;
        private double longitude;
        private double latitude;
        private String devices;
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDataId() {
            return dataId;
        }

        public void setDataId(int dataId) {
            this.dataId = dataId;
        }

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getDevices() {
            return devices;
        }

        public void setDevices(String devices) {
            this.devices = devices;
        }
    }
}
