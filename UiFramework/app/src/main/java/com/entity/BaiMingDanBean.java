package com.entity;

/**
 * Created by qq944 on 2017/10/19.
 */

public class BaiMingDanBean {
    private String devicesId;
    private String file;
    private String devices;

    public BaiMingDanBean(String devicesId, String file, String devices) {
        this.devicesId = devicesId;
        this.file = file;
        this.devices = devices;
    }

    public String getDevicesId() {
        return devicesId;
    }

    public void setDevicesId(String devicesId) {
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
