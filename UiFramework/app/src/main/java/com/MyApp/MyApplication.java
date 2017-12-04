package com.MyApp;

import android.app.Application;
import android.graphics.drawable.ColorDrawable;

import com.baidu.mapapi.SDKInitializer;
import com.crash.CrashApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.uidemo.R;
import com.utils.VolleyManager;

import org.xutils.x;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/5/4.
 */
public class MyApplication extends CrashApplication {
    // 成员实体对象
    private static MyApplication app;
    public List<String> deviceTile = new ArrayList<>();
    /**
     *图片options
     */
    @SuppressWarnings("deprecation")
    public static DisplayImageOptions goodOptions;
    private String devices;

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyManager.getInstance().init(this);
        initImageLoader();
        // 百度sdk初始化
        SDKInitializer.initialize(this);
        //对xUtils进行初始化
        x.Ext.init(this);
        setDevices(getSerialNumber());
    }
    @SuppressWarnings("deprecation")
    private void initImageLoader()
    {
        if (!ImageLoader.getInstance().isInited())
        {
            goodOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                    .showImageOnFail(getResources().getDrawable(R.drawable.a))
                    .showImageOnLoading(getResources().getDrawable(R.drawable.a))
                    .showImageForEmptyUri(getResources().getDrawable(R.drawable.a)).build();



            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true).build();
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                    .defaultDisplayImageOptions(defaultOptions).build();
            ImageLoader.getInstance().init(configuration);

        }
    }
    /**
     * 构造，初始化
     */
    public MyApplication() {
        super();
    }

    /**
     * 对象的实例化方法
     *
     * @return
     */
    public static synchronized MyApplication getInstance() {
        if (app == null) {
            app = new MyApplication();
        }
        return app;
    }


    /**
     * 获取设备号
     *
     * @return
     */
    public List<String> getDeviceTile() {
        return deviceTile;
    }

    public void setDeviceTile(List<String> deviceTile) {
        this.deviceTile = deviceTile;
    }

    public  String getSerialNumber(){

        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }
}
