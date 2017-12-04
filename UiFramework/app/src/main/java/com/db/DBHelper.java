package com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.entity.AccountBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vicmob_yf002 on 2017/6/2.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    /**
     * 数据库名字
     */
    private static final String TABLE_NAME = "sqlite_test.db";
    /**
     * 用来存放Dao的双列集合
     */
    private Map<String, Dao> daos = new HashMap<String, Dao>();
    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 1;

    private static DBHelper instance;

    /**
     * 构造方法
     *
     * @param context
     */
    public DBHelper(Context context) {
        super(context, TABLE_NAME, null, DB_VERSION);
    }

    /**
     * 创建表
     *
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            // 创建数据库表（直接使用ORMLite的工具类创建）
            // 参数2是对应数据库表的JavaBean的class
            TableUtils.createTable(connectionSource, AccountBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行更新表操作
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // 删除表
            TableUtils.dropTable(connectionSource, AccountBean.class, true);
            // 重写创建表
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DBHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null)
                    instance = new DBHelper(context);
            }
        }
        return instance;
    }

    /**
     * @param clazz
     * @return
     * @throws SQLException
     */
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
