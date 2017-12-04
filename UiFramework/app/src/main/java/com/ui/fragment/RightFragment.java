package com.ui.fragment;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Interface.View.MyItemClickListener;
import com.MyApp.MyApplication;
import com.base.BaseFragment;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;
import com.ui.Adapter.MyDevicesAdapter;
import com.ui.activity.DefaultDataActivity;
import com.ui.activity.DeviceActivity;
import com.ui.activity.DeviceUpdateActivity;
import com.uidemo.R;
import com.utils.Constant;
import com.utils.MyJsonUtil;
import com.utils.PrefUtils;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.LoadDialog;
import com.widget.MyItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;


/**
 * 当前类注释:第一个Fragment
 */
public class RightFragment extends BaseFragment implements View.OnClickListener, MyItemClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mGvDevice;
    private MyDevicesAdapter myDevicesAdapter;
    private MyApplication app;
    private String devicesName;
    private List<String> devicesList;
    private static final int SUCCESSMSG = 1;
    private static final int FAULTMSG = 2;
    private static final int MSG = 3;
    private static final int EXMSG = 4;
    //提交成功信息
    private static final int SUCCESS = 5;
    //提交失败信息
    private static final int FAIL = 6;
    private static final int REFRESH_COMPLETE = 0X110;
    private RelativeLayout devices_none;
    private RelativeLayout devices_load;
    private int index = 0;
    //加载动画图片
    private ImageView imageView;
    //动画
    AnimationDrawable loadingDrawable;
    List<String> devlist = new ArrayList<>();
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESSMSG:
                    mSwipeRefreshLayout.setRefreshing(false);
                    devices_none.setVisibility(View.INVISIBLE);
                    mGvDevice.setVisibility(View.VISIBLE);
                    devices_load.setVisibility(View.INVISIBLE);
                    myDevicesAdapter = new MyDevicesAdapter(app.getDeviceTile(), mActivity);
                    mGvDevice.setAdapter(myDevicesAdapter);
                    initlistener();
//                    insertDevicesData();
                    break;
                case FAULTMSG:
                    mSwipeRefreshLayout.setRefreshing(false);
                    mGvDevice.setVisibility(View.INVISIBLE);
                    devices_none.setVisibility(View.VISIBLE);
                    devices_load.setVisibility(View.INVISIBLE);
                    break;
                case MSG:
                    try {
                        //解析字符串(xx;xx;xx)
                        String mm = (String) msg.obj;
                        int j = mm.lastIndexOf(";");
                        String num = mm.substring(0, j);
                        int k = num.lastIndexOf(";");
                        String numbers = mm.substring(0, k);
                        int ii = Integer.parseInt(numbers);
                        if (ii == 0) {
                            String deviceNum = mm.substring(k + 1, j);
                            LeftFragment.mDeviceNums = Integer.parseInt(deviceNum);
                            LeftFragment.deviceList = mm.substring(j + 1);
                            LeftFragment.devices = LeftFragment.deviceList.replace("[", "").replace("]", "").split(",");
                            ToastUtils.showShort(mActivity, "当前设备:" + LeftFragment.mDeviceNums + "台");
                            devicesList = new ArrayList<>();
                            for (int i = 0; i < LeftFragment.mDeviceNums; i++) {
                                devicesList.add(LeftFragment.devices[i].trim());
                                boundDevice(LeftFragment.devices[i].trim(),app.getDevices());
                            }
                            app.setDeviceTile(devicesList);
                            myHandler.sendEmptyMessage(SUCCESSMSG);
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showShort(mActivity, "获取设备异常");
                        myHandler.sendEmptyMessage(FAULTMSG);
                    }
                    break;
                case EXMSG:
                    mSwipeRefreshLayout.setRefreshing(false);
                    myHandler.sendEmptyMessage(FAULTMSG);
                    ToastUtils.showShort(mActivity, "连接服务器异常，请检查");
//                    List<String> devlist =new ArrayList<>();
//                    devlist.add("设备1");
//                    devlist.add("设备2");
//                    devlist.add("设备3");
//                    devlist.add("设备4");
//                    devlist.add("设备5");
//                    app.setDeviceTile(devlist);
//                    myHandler.sendEmptyMessage(SUCCESSMSG);
                    break;
                case REFRESH_COMPLETE:
                    index = 0;
                    new Thread(networkTask).start();
                    break;
                case SUCCESS:
                    LoadDialog.dismiss(getActivity());
                    Alerter.create(getActivity())
                            .setTitle("提交成功")
                            .setDuration(500)
                            .setBackgroundColor(R.color.text_blue)
                            .show();
                    break;
                case FAIL:
                    LoadDialog.dismiss(getActivity());
                    Alerter.create(getActivity())
                            .setTitle("提交失败")
                            .setDuration(500)
                            .setBackgroundColor(R.color.text_blue)
                            .show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.right_frag_layout, container, false);
        v.setOnTouchListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化视图
        initView();
        initData();
        app = (MyApplication) getActivity().getApplication();
    }

    /**
     * 初始化视图
     */
    public void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mActivity.findViewById(R.id.mSwipeRefreshLayout);
        devices_none = (RelativeLayout) mActivity.findViewById(R.id.devices_noneww);
        devices_load = (RelativeLayout) mActivity.findViewById(R.id.devices_load);
        mGvDevice = (RecyclerView) mActivity.findViewById(R.id.gv_device);
        mGvDevice.setLayoutManager(new GridLayoutManager(this.mActivity, 4));
        mGvDevice.addItemDecoration(new MyItemDecoration());
        devices_none.setOnClickListener(this);
        mGvDevice.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LeftFragment.mDeviceNums == 0) {
                    myHandler.sendEmptyMessage(FAULTMSG);
                } else {
                    myHandler.sendEmptyMessage(SUCCESSMSG);
                }
            }
        }, 8000);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1500);
            }
        });
    }

    public void initData() {
        devices_none.setVisibility(View.INVISIBLE);
        devices_load.setVisibility(View.VISIBLE);
        mGvDevice.setVisibility(View.INVISIBLE);
        //显示加载的动画
        imageView = (ImageView) mActivity.findViewById(R.id.devices_load_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        loadingDrawable.start();
        //new Thread(networkTask).start();
    }

    /**
     * 初始化适配器点击监听
     */
    private void initlistener() {
        myDevicesAdapter.setOnItemClickListener(this);
    }


    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            Socket socket = null;
            String socketUrl = Urlutils.getWebUrl();
            try {
                // 建立与vicmob服务器连接的socket
                socket = new Socket(socketUrl, 18888);
                // 写数据到服务端:建立写数据流,往流写数据
                String strVal = Constant.indexStr[index];
                OutputStream os = socket.getOutputStream();
                os.write(strVal.getBytes());
                // 写关闭,不然会影响读:不然会一直阻塞着,服务器会认为客户端还一直在写数据
                // 由于从客户端发送的消息长度是任意的，客户端需要关闭连接以通知服务器消息发送完毕，如果客户端在发送完最后一个字节后
                // 关闭输出流，此时服务端将知道"我已经读到了客户端发送过来的数据的末尾了,即-1",就会读取出数据并关闭服务端的读数据流,在之后就可以
                // 自己(服务端)的输出流了,往客户端写数据了
                socket.shutdownOutput();

                InputStream is = socket.getInputStream();
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    String printName = new String(bytes, 0, len);
                    String pictureUrl = "http://www.vicmob.net";
                    System.out.println(pictureUrl + printName.toString());
                    Message message = new Message();
                    message.what = MSG;
                    message.obj = printName;
                    myHandler.sendMessage(message);
                }
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                myHandler.sendEmptyMessage(EXMSG);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    myHandler.sendEmptyMessage(EXMSG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.devices_noneww:
                initData();
                index = 0;
                new Thread(networkTask).start();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        devicesName = app.getDeviceTile().get(position);
        ToastUtils.showShort(mActivity, devicesName);
        Intent intent = new Intent(mActivity, DeviceUpdateActivity.class);
        intent.putExtra("position", (position + 1) + "");
        intent.putExtra("device", devicesName);
        startActivity(intent);
    }

    private void insertDevicesData() {
        Thread th = new Thread() {
            @Override
            public void run() {
                MyApplication mapp = (MyApplication) getActivity().getApplication();
                String url1 = Urlutils.getInsertDevicesData();
                final Map<String, Object> params = new HashMap<>();
                params.put("maindevices", mapp.getDevices());
                List<String> devicesList = new ArrayList<>();
                if (app.getDeviceTile().size() > 0) {
                    for (int i = 0; i < app.getDeviceTile().size(); i++) {
//                        Map<String, Object> obj = new HashMap<>();
//                        obj.put("partdevices", app.getDeviceTile().get(i));
                        devicesList.add(app.getDeviceTile().get(i));
                    }
                } else {
                }
                params.put("partdevices", devicesList);
                Log.i("zw810", new Gson().toJson(params));
//                OkHttpUtils
//                        .post()
//                        .url(url1)
//                        .params(params)
//                        .build()
                OkHttpUtils.postString().url(url1)
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .content(new Gson().toJson(params)).build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(EXMSG);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("123", response);
                                if (MyJsonUtil.getBeanByJson(response)) {
                                    myHandler.sendEmptyMessage(SUCCESS);
                                } else {
                                    myHandler.sendEmptyMessage(FAIL);
                                }
                            }
                        });
            }
        };
        th.start();
    }
    public void boundDevice(final String partdevices, final String maindevices){
        Thread thread = new Thread(){
            @Override
            public void run() {
                String url = Urlutils.boundDeviceUrl+"?partdevices="+partdevices+"&maindevices="+maindevices;
                Log.i("zw1029",url);
                OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "绑定设备号失败", Toast.LENGTH_SHORT).show();
                        Log.e("zw1029","Call:"+call,e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (MyJsonUtil.getBeanByJson(response)) {
                            Toast.makeText(getActivity(), "绑定设备号成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        thread.start();
    }

}

