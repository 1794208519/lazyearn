package com.ui.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.Interface.View.Callback;
import com.MyApp.MyApplication;
import com.base.BaseFragment;
import com.google.gson.Gson;
import com.service.MusiceService;
import com.tapadoo.alerter.Alerter;
import com.ui.Adapter.GridViewAdapter;
import com.ui.activity.IntelligentActivity;
import com.uidemo.R;
import com.utils.Constant;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.ConfirmDialog;
import com.widget.CustomerDialog;
import com.widget.LoadDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * 当前类注释:第一个Fragment
 */
public class LeftFragment extends BaseFragment {
    //对话框
    Dialog dialog = null;
    private int index = 0;
    private GridView mDridView;
    //连接成功信息
    private static final int MSG = 1;
    //连接失败信息
    private static final int EXMSG = 2;
    //设备数量
    public static int mDeviceNums = 0;
    public static String deviceList = "";
    public static String[] devices;
    private CustomerDialog customerDialog = null;
    private ConfirmDialog confirmDialog = null;
    private List<String> devicesList;
    private MyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.left_frag_layout, container, false);
        v.setOnTouchListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化视图
        initView();
        app = MyApplication.getInstance();
        index = 0;
        new Thread(networkTask).start();
    }

    /**
     * 初始化视图
     */
    public void initView() {
        mDridView = (GridView) mActivity.findViewById(R.id.gv_main);
      //  mDridView.setAdapter(new GridViewAdapter(Constant.weixinnames, Constant.weixinimgs, mActivity));

        mDridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //连接
                        index = 1;
                        LoadDialog.show(mActivity);
                        new Thread(networkTask).start();
                        stop();
                        start(Constant.musices[0]);
                        break;
                    case 1: //亮屏
                        index = 2;
                        new Thread(networkTask).start();
                        stop();
                        start(Constant.musices[1]);
                        break;
                    case 2: //息屏
                        index = 3;
                        stop();
                        start(Constant.musices[2]);
                        setDialog1();
                        break;

                    case 3: //停止
                        index = 4;
                        stop();
                        start(Constant.musices[6]);
                        setDialog2();
                        break;
                    case 4: //附近的人
                        index = 5;
                        stop();
                        start(Constant.musices[4]);
                        setDialog();
                        break;

                    case 5: //通讯录
                        index = 6;
                        stop();
                        start(Constant.musices[8]);
                        setDialog4();
                        break;

                    case 6: //联系人
                        index = 7;
                        stop();
                        start(Constant.musices[3]);
                        setDialog4();
                        break;

                    case 7: //群好友
                        index = 8;
                        stop();
                        start(Constant.musices[3]);
                        setDialog4();
                        break;

                    case 8: //公众号
                        index = 9;
                        stop();
                        start(Constant.musices[3]);
                        setDialog4();
                        break;

                    case 9: //智能回复
                        index = 10;
                        stop();
                        start(Constant.musices[10]);
                        setDialog5();
                        break;
                    case 10: //朋友圈
                        index = 11;
                        setDialog4();
                        start(Constant.musices[3]);
                        break;
                    case 11: //QQ助手
                        index = 12;
                        setDialog3();
                        start(Constant.musices[9]);
                        break;
                    case 12: //陌陌助手
                        index = 13;
                        setDialog3();
                        start(Constant.musices[9]);
                        break;
                    case 13: //FaceBook助手
                        index = 14;
                        setDialog3();
                        start(Constant.musices[9]);
                        break;
                    case 14: //探探助手
                        index = 15;
                        setDialog3();
                        start(Constant.musices[9]);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    //更新ui
    private Handler myHandler = new Handler() {
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG:

                    try {
                        //解析字符串(xx;xx;xx)
                        String mm = (String) msg.obj;
                        Log.i("123",mm);
                        int j = mm.lastIndexOf(";");
                        String num = mm.substring(0, j);
                        int k = num.lastIndexOf(";");
                        String numbers = mm.substring(0, k);
                        int ii = Integer.parseInt(numbers);
                        if (ii == 0) {
                            String deviceNum = mm.substring(k + 1, j);
                            mDeviceNums = Integer.parseInt(deviceNum);
                            deviceList = mm.substring(j + 1);
                            devices = deviceList.replace("[", "").replace("]", "").split(",");
                            ToastUtils.showShort(mActivity, "当前设备:" + mDeviceNums + "台");
                            devicesList = new ArrayList<>();
                            for (int i = 0; i < LeftFragment.mDeviceNums; i++) {
                                devicesList.add(devices[i]);
                            }
                            app.setDeviceTile(devicesList);
                        } else if (ii == 1) {
                            stop();
                            start(Constant.musices[7]);  //连接成功语音
                            LoadDialog.dismiss(mActivity);
                            Alerter.create(mActivity)
                                    .setTitle("连接成功")
                                    .setDuration(500)
                                    .setBackgroundColor(R.color.text_blue)
                                    .show();
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showShort(mActivity, "获取设备异常");
                    }

                    break;
                case EXMSG:
                    LoadDialog.dismiss(mActivity);
                    Alerter.create(mActivity)
                            .setTitle("连接服务器异常，请检查")
                            .setDuration(600)
                            .setBackgroundColor(R.color.text_blue)
                            .show();

                    break;
            }
        }
    };

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
                    Thread.sleep(3000);
                    myHandler.sendMessage(message);
                }
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
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

    //附近人对话框
    public void setDialog() {
        customerDialog = new CustomerDialog(mActivity, new Callback() {
            @Override
            public void positiveCallback() {
                stop();
                start(Constant.musices[4]);
                new Thread(networkTask).start();
                ToastUtils.showShort(mActivity, "开始");
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(mActivity, "取消");
            }
        });
        customerDialog.setContent("提示" + "\n主人，懒懒要开始咯?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //息屏对话框
    public void setDialog1() {
        customerDialog = new CustomerDialog(mActivity, new Callback() {
            @Override
            public void positiveCallback() {
                new Thread(networkTask).start();
                stop();
                ToastUtils.showShort(mActivity, "好的");
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(mActivity, "再说");
            }
        });
        customerDialog.setContent("提示" + "\n主人，让懒懒休息一会吧?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //停止对话框
    public void setDialog2() {
        customerDialog = new CustomerDialog(mActivity, new Callback() {
            @Override
            public void positiveCallback() {
                new Thread(networkTask).start();
                stop();
                ToastUtils.showShort(mActivity, "好的");
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(mActivity, "不要");
            }
        });
        customerDialog.setContent("提示" + "\n主人，懒懒要走咯?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //朋友圈对话框
    public void setDialog3() {
        confirmDialog = new ConfirmDialog(mActivity, new Callback() {
            @Override
            public void positiveCallback() {
                stop();
            }

            @Override
            public void negativeCallback() {

            }
        });
        confirmDialog.setContent("您好" + "\n暂未开启，敬请期待");
        confirmDialog.setCancelable(false);
        confirmDialog.show();
    }

    //其他对话框
    public void setDialog4() {
        customerDialog = new CustomerDialog(mActivity, new Callback() {
            @Override
            public void positiveCallback() {
                stop();
                start(Constant.musices[5]);
                new Thread(networkTask).start();
                ToastUtils.showShort(mActivity, "开始");
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(mActivity, "取消");
            }
        });
        customerDialog.setContent("提示" + "\n主人，懒懒要开始咯?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //懒懒助手对话框
    public void setDialog5() {
        customerDialog = new CustomerDialog(mActivity, new Callback() {
            @Override
            public void positiveCallback() {
                stop();
                Intent intent = new Intent(mActivity, IntelligentActivity.class);
                startActivity(intent);
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(mActivity, "取消");
            }
        });
        customerDialog.setContent("提示" + "\n主人，是否启用智能回复?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //开始播放音乐
    public void start(int musArr) {
        Intent intent;
        intent = new Intent(mActivity, MusiceService.class);
        intent.putExtra("raw", musArr);
        mActivity.startService(intent);
    }

    //停止音乐
    public void stop() {
        Intent intent = new Intent(mActivity, MusiceService.class);
        mActivity.stopService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }



}