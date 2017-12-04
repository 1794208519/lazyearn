package com.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by vicmob_yf002 on 2017/6/23.
 */
@DatabaseTable(tableName = "t_account")
public class AccountBean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "account")
    private String account;
    @DatabaseField(columnName = "password")
    private String password;

    /**
     * 此构造方法必须为无参
     */
    public AccountBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "AccountBean{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
