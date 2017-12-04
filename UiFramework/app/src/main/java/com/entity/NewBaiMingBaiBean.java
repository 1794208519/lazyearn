package com.entity;

import java.util.List;

/**
 * Created by qq944 on 2017/10/21.
 */

public class NewBaiMingBaiBean {

    private List<AutoDevicesBean> auto_devices;

    public List<AutoDevicesBean> getAuto_devices() {
        return auto_devices;
    }

    public void setAuto_devices(List<AutoDevicesBean> auto_devices) {
        this.auto_devices = auto_devices;
    }

    public static class AutoDevicesBean {
        /**
         * devicesId : 1
         * file : 10.xls
         * devices : 52b99baf7d34
         */

        private int devicesId;
        private String file;
        private String devices;

        public int getDevicesId() {
            return devicesId;
        }

        public void setDevicesId(int devicesId) {
            this.devicesId = devicesId;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getDevices() {
            return devices;
        }

        public void setDevices(String devices) {
            this.devices = devices;
        }
    }
}
