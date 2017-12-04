package com.ui.activity;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.MyApp.MyApplication;
import com.base.BaseActivity;
import com.ui.Adapter.MyFragmentPagerAdapter;
import com.ui.fragment.BaiMingDanFragment;
import com.ui.fragment.DeviceleftFragment;
import com.ui.fragment.DevicerightFragment;
import com.ui.fragment.DeviceAccountFragment;
import com.uidemo.R;
import com.utils.Urlutils;

import java.util.ArrayList;

public class DeviceUpdateActivity extends BaseActivity implements View.OnClickListener{
//    private Button mLeftBtn;
//    private Button mRightBtn;
    private ViewPager viewPager;// 页卡内容
    // viewpager页面fragment列表
    private ArrayList<Fragment> fragmentsList;
    private DeviceleftFragment DeviceleftFragment;
    private DevicerightFragment DevicerightFragment;
    private DeviceAccountFragment DeviceAccountFragment;
    private MyApplication app;
    //获取传过来的设备名称
    private String deviceName;
    private int deviceIndex;
    public static String deviceSN;
    public String position1;
    //数据库地址
    private String accounturl = Urlutils.getInsertAccountUrl();
    private String addressurl = Urlutils.getInsertAddressUrl();
    private ImageView device_back;
    private TextView tv_title_device;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.device_update_activity;
    }
    @Override
    public void initView() {
        InitViewPager();
    }
    @Override
    public void findView() {
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
//        mLeftBtn = (Button) findViewById(R.id.buttonLeft);
//        mRightBtn = (Button)findViewById(R.id.buttonRight);
        device_back= (ImageView) findViewById(R.id.device_back);
        tv_title_device = ((TextView) findViewById(R.id.tv_title_device));

        //初始化选中的状态
//        clearSelection();
//        mLeftBtn.setBackground(getResources().getDrawable(R.drawable.device_left1));
//        mLeftBtn.setTextColor(getResources().getColor(R.color.white));
        app = MyApplication.getInstance();
        Intent intent = getIntent();
        if (intent.getExtras()!=null) {
            deviceName = intent.getStringExtra("device");
            Log.d("zzzzzz",deviceName);
            if (deviceName == null || TextUtils.isEmpty(deviceName) || "".equals(deviceName)) {

            } else {
                Log.i("123", deviceName);
                deviceSN =deviceName.trim();
                Log.d("SN", deviceSN);
            }
        }
        position1 = intent.getStringExtra("position");
        tv_title_device.setText("设备"+position1);

    }

    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }
    @Override
    public void initListener() {
        device_back.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    /**
     * viewpager页面初始化
     *
     * @param
     */
    private void InitViewPager() {
        viewPager = (ViewPager) findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();
        DeviceleftFragment = new DeviceleftFragment();
        DevicerightFragment = new DevicerightFragment();
        DeviceAccountFragment = new DeviceAccountFragment();

        fragmentsList.add(DeviceleftFragment);
        fragmentsList.add(DevicerightFragment);
        fragmentsList.add(DeviceAccountFragment);
        fragmentsList.add(new BaiMingDanFragment());
        // viewpager使用适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragmentsList));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
        TabLayout.Tab tab1 = tabLayout.newTab().setText("城市管理");
        TabLayout.Tab tab2 = tabLayout.newTab().setText("地点管理");
        TabLayout.Tab tab3 = tabLayout.newTab().setText("账号管理");
        TabLayout.Tab tab4 = tabLayout.newTab().setText("白名单管理");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        tabLayout.addTab(tab4);
        tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.device_back:
                finish();
                break;
        }
    }
    /**
     * 页面改变监听
     *
     * @author Administrator
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageSelected(int arg0) {

        }

    }


}
