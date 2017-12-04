package com.dao;

import android.content.Context;

import com.db.DBHelper;
import com.entity.AccountBean;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义数据访问对象，对指定的表进行增删改查操作
 * Created by vicmob_yf002 on 2017/6/23.
 */
public class AccountDao {
    private Dao<AccountBean, Integer> accountDaoOpe;
    private DBHelper dbHelper;

    /**
     * 构造方法
     * 获得数据库帮助类实例，通过传入Class对象得到相应的Dao
     *
     * @param context
     */
    public AccountDao(Context context) {
        try {
            dbHelper = DBHelper.getHelper(context);
            accountDaoOpe = dbHelper.getDao(AccountBean.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条记录
     *
     * @param accountBean
     * @throws SQLException
     */
    public void add(AccountBean accountBean) {
        try {
            accountDaoOpe.create(accountBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除一条记录
     *
     * @param accountBean
     */
    public void delete(AccountBean accountBean) {
        try {
            accountDaoOpe.delete(accountBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * 更新一条记录
     *
     * @param accountBean
     */
    public void update(AccountBean accountBean) {
        try {
            accountDaoOpe.update(accountBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询一条记录
     *
     * @param id
     * @return
     */
    public AccountBean queryForId(int id) {
        AccountBean accountBean = null;
        try {
            accountBean = accountDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountBean;
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<AccountBean> queryForAll() {
        List<AccountBean> accountBean = new ArrayList<AccountBean>();
        try {
            accountBean = accountDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountBean;
    }
}
