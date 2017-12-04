package com.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by vicmob_yf002 on 2017/6/23.
 */
@DatabaseTable(tableName = "t_account")
public class DeviceAddressBean {
    private int id;
    private String address;

    private double latitude ,longitude;

    /**
     * 此构造方法必须为无参
     */
    public DeviceAddressBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
