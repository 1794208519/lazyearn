package com.entity;

import java.util.List;

/**
 * Created by fxw on 2017/7/6.
 */

public class DeviceAccountBean {
    private List<DeviceAccountBean.AutoDataBean> auto_data;

    public List<DeviceAccountBean.AutoDataBean> getAuto_data() {
        return auto_data;
    }

    public void setAuto_data(List<DeviceAccountBean.AutoDataBean> auto_data) {
        this.auto_data = auto_data;
    }
    public static class AutoDataBean {
        private String name;
        private String openid;
        private String sex;
        private String city;
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
