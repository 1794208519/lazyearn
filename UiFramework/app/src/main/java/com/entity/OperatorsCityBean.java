package com.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq944 on 2017/8/10.
 */

public class OperatorsCityBean {
    private List<OperatorsCityBean.AutoCityBean> auto_city=new ArrayList<OperatorsCityBean.AutoCityBean>();

    public List<OperatorsCityBean.AutoCityBean> getAuto_City() {
        return auto_city;
    }

    public void setAuto_city(List<OperatorsCityBean.AutoCityBean> auto_city) {
        this.auto_city = auto_city;
    }

    public static class AutoCityBean {
        //运行商城市

        private  int cityId;
        private String operator;
        private String city;
        private String devices;


        public AutoCityBean(int cityId, String operator, String city, String devices) {
            this.cityId = cityId;
            this.operator = operator;
            this.city = city;
            this.devices = devices;
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDevices() {
            return devices;
        }

        public void setDevices(String devices) {
            this.devices = devices;
        }


        @Override
        public String toString() {
            return "OperatorsCityBean{" +
                    "id='" + cityId + '\'' +
                    ", operator='" + operator + '\'' +
                    ", city='" + city + '\'' +
                    ", devices='" + devices + '\'' +
                    '}';
        }
    }
}
