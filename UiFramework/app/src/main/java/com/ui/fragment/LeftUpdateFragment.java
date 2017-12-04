package com.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Interface.View.Callback;
import com.Interface.View.WhiteCallback;
import com.MyApp.MyApplication;
import com.service.MusiceService;
import com.tapadoo.alerter.Alerter;
import com.ui.Adapter.GridViewAdapter;
import com.ui.Adapter.MyPagerAdapter;
import com.ui.activity.IntelligentActivity;
import com.ui.activity.WeiXinHaoActivity;
import com.uidemo.R;
import com.utils.Constant;
import com.utils.MyJsonUtil;
import com.utils.PopupWindowUtil;
import com.utils.PrefUtils;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;


/**
 * author: Twisted
 * created on: 2017/8/9 11:22
 * description:
 */

public class LeftUpdateFragment extends Fragment {

    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2, t3;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private View view;

    //对话框
    Dialog dialog = null;
    private int index = 0;
    private GridView mDridView;
    //连接成功信息
    private static final int MSG = 1;
    //连接失败信息
    private static final int EXMSG = 2;
    private static final int UPDATESUCCESS = 3;
    private static final int UPDATEFAIL = 4;
    //设备数量
    public static int mDeviceNums = 0;
    public static String deviceList = "";
    public static String[] devices;
    private CustomerDialog customerDialog = null;
    private ConfirmDialog confirmDialog = null;
    private List<String> devicesList;
    private MyApplication app;
    private TabLayout tabLayout;
    //private boolean b = true;//开启取消定时
    private GridViewAdapter gridViewAdapter;
    private List<String> functions = new ArrayList<String>();
    private int hour;
    private int minute;
    private int interval;
    //更新ui
    private Handler myHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG:
                    try {
                        //解析字符串(xx;xx;xx)
                        String mm = (String) msg.obj;
                        Log.i("123", mm);
                        int j = mm.lastIndexOf(";");
                        String num = mm.substring(0, j);
                        int k = num.lastIndexOf(";");
                        String numbers = mm.substring(0, k);
                        int ii = Integer.parseInt(numbers);
                        if (ii == 0) {
                            String deviceNum = mm.substring(k + 1, j);
                            mDeviceNums = Integer.parseInt(deviceNum);
                            deviceList = mm.substring(j + 1);
                            Log.i("zw1029",deviceList);
                            devices = deviceList.replace("[", "").replace("]", "").split(",");
                            ToastUtils.showShort(getActivity(), "当前设备:" + mDeviceNums + "台");
                            devicesList = new ArrayList<>();
                            for (int i = 0; i < LeftFragment.mDeviceNums; i++) {
                                devicesList.add(devices[i]);
                            }
                            app.setDeviceTile(devicesList);
                        } else if (ii == 1) {
                            stop();
                            startMusic(Constant.musices[7]);  //连接成功语音
                            LoadDialog.dismiss(getActivity());
                            Alerter.create(getActivity())
                                    .setTitle("连接成功")
                                    .setDuration(500)
                                    .setBackgroundColor(R.color.text_blue)
                                    .show();
                        } else if(ii==27){
                            Log.i("zw1026","  FFFFFFFF");
                            PrefUtils.putBoolean(getActivity(),"isDingShi",false);
                            functions.set(16,"取消定时");
                            Log.i("zw1027",functions.get(16)+"  functions16");
                            gridViewAdapter.notifyChanged(functions);
                        }else if (ii==28){
                            Log.i("zw1026","  TTTTTTTT");
                            PrefUtils.putBoolean(getActivity(),"isDingShi",true);
                            functions.set(16,"开启定时");
                            Log.i("zw1027",functions.get(16)+"  functions16");
                            gridViewAdapter.notifyChanged(functions);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showShort(getActivity(), "获取设备异常");
                    }
                    break;
                case EXMSG:
                    LoadDialog.dismiss(getActivity());
                    Alerter.create(getActivity())
                            .setTitle("连接服务器异常，请检查")
                            .setDuration(600)
                            .setBackgroundColor(R.color.text_blue)
                            .show();
                    break;
                case UPDATESUCCESS:
                    break;
                case UPDATEFAIL:
                    break;
            }
        }
    };
    private GridView mQQDridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.left_update, container, false);
        InitViewPager();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化ViewPager
     */
    @SuppressLint("NewApi")
    private void InitViewPager() {
        hour = PrefUtils.getInt(getActivity(),"hour",8);
        minute = PrefUtils.getInt(getActivity(),"minute",30);
        interval = PrefUtils.getInt(getActivity(),"interval",24);
        Log.i("zw1101",hour+","+minute+","+interval);
        for(int i=0;i<Constant.weixinnames.length;i++){
            functions.add(Constant.weixinnames[i]);
        }

        if (PrefUtils.getBoolean(getActivity(),"isDingShi",true)){
            functions.set(16,"开启定时");
        }else{
            functions.set(16,"取消定时");
        }
        gridViewAdapter = new GridViewAdapter(functions, Constant.weixinimgs, getActivity());
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mPager = (ViewPager) view.findViewById(R.id.vPager);
        //tablayout关联viewpager
        tabLayout.setupWithViewPager(mPager);
        TabLayout.Tab tab1 = tabLayout.newTab().setText("微信");
        TabLayout.Tab tab2 = tabLayout.newTab().setText("QQ");
        TabLayout.Tab tab3 = tabLayout.newTab().setText("陌陌");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        listViews = new ArrayList<View>();
        //微信助手界面
        View v1 = getActivity().getLayoutInflater().inflate(R.layout.left_frag_layout, null);
        mDridView = (GridView) v1.findViewById(R.id.gv_main);
        mDridView.setAdapter(gridViewAdapter);
        mDridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //连接
                        index = 1;
                        LoadDialog.show(getActivity());
                        new Thread(networkTask).start();
                        stop();
                        startMusic(Constant.musices[0]);
                        break;
                    case 1: //亮屏
                        index = 2;
                        new Thread(networkTask).start();
                        stop();
                        startMusic(Constant.musices[1]);
                        break;
                    case 2: //息屏
                        index = 3;
                        stop();
                        startMusic(Constant.musices[2]);
                        setDialog1();
                        break;
                    case 3: //停止
                        index = 4;
                        stop();
                        startMusic(Constant.musices[6]);
                        setDialog2();
                        break;
                    case 4: //附近的人
                        PrefUtils.putString(getContext(), "time", new Date().getTime() + "");
                        index = 5;
                        stop();
                        startMusic(Constant.musices[4]);
                        setDialog();
                        break;
                    case 5://一键开始
                        index = 24;
                        setDialog4();
                        Log.e("123", "1111111111");
                        startMusic(Constant.musices[3]);
                        break;
                    case 6: //通讯录
                        PrefUtils.putString(getContext(), "time", new Date().getTime() + "");
                        index = 6;
                        stop();
                        startMusic(Constant.musices[8]);
                        setDialog4();
                        break;
                    case 7: //联系人
                        PrefUtils.putString(getContext(), "time", new Date().getTime() + "");
                        index = 7;
                        stop();
                        startMusic(Constant.musices[3]);
                        setDialog4();
                        break;
                    case 8: //群好友
                        PrefUtils.putString(getContext(), "time", new Date().getTime() + "");
                        index = 8;
                        stop();
                        startMusic(Constant.musices[3]);
                        setDialog4();
                        break;

                    case 9: //公众号
                        PrefUtils.putString(getContext(), "time", new Date().getTime() + "");
                        index = 9;
                        stop();
                        startMusic(Constant.musices[3]);
                        setDialog4();
                        break;
                    case 10: //智能回复
                        PrefUtils.putString(getContext(), "time", new Date().getTime() + "");
                        index = 10;
                        stop();
                        startMusic(Constant.musices[10]);
                        setDialog5();
                        break;
                    case 11: //朋友圈
                        PrefUtils.putString(getContext(), "time", new Date().getTime() + "");
                        index = 11;
                        setDialog4();
                        startMusic(Constant.musices[3]);
                        break;
                    case 12: //微信一键发消息
                        PrefUtils.putString(getContext(), "time", new Date().getTime() + "");
                        index = 12;
//                        startActivity(new Intent(getActivity(), DefaultDataActivity.class));
                        setDialog4();
                        startMusic(Constant.musices[3]);
                        break;
                    case 13://聊天记录

                        startActivity(new Intent(getActivity(), WeiXinHaoActivity.class));
                        break;
                    case 14://白名单
                        new PopupWindowUtil(getActivity(), view, new WhiteCallback() {
                            @Override
                            public void excelCallback() {
                                index = 22;
                                updateWhiteDate("0");
//                                Toast.makeText(getContext(), "点击了导入表", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void addressbookCallback() {
                                index = 25;
                                updateWhiteDate("1");
//                                Toast.makeText(getContext(), "点击了导入通讯录", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void addCallback() {
                                index = 26;
                                updateWhiteDate("2");
//                                Toast.makeText(getContext(), "点击了添加白名单", Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                    case 15://自动换号

                        setSwitchAccountDialog();
//                        if (new Date().getTime()-(Long.parseLong(time))>=2*3600000){
                        break;
                    case 16://开启定时
                        PrefUtils.putString(getContext(), "time", new Date().getTime() + "");
                        setDialogDingShi();
                        break;
                    default:
                        break;
                }
            }
        });
        //QQ助手界面
        View v2 = getActivity().getLayoutInflater().inflate(R.layout.qq_layout, null);
        mQQDridView = (GridView) v2.findViewById(R.id.qq_gv_main);
        List<String> qqfunctions = new ArrayList<String>();
        for (int i=0;i<Constant.qqnames.length;i++){
            qqfunctions.add(Constant.qqnames[i]);
        }
        mQQDridView.setAdapter(new GridViewAdapter(qqfunctions, Constant.qqimgs, getActivity()));
        mQQDridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //连接
                        index = 1;
                        LoadDialog.show(getActivity());
                        new Thread(networkTask).start();
                        stop();
                        startMusic(Constant.musices[0]);
                        break;
                    case 1: //亮屏
                        index = 2;
                        new Thread(networkTask).start();
                        stop();
                        startMusic(Constant.musices[1]);
                        break;
                    case 2: //息屏
                        index = 3;
                        stop();
                        startMusic(Constant.musices[2]);
                        setDialog1();
                        break;
                    case 3: //停止
                        index = 4;
                        stop();
                        startMusic(Constant.musices[6]);
                        setDialog2();
                        break;
                    case 4: //附近的人
                        index = 13;
                        stop();
                        startMusic(Constant.musices[4]);
                        setDialog();
                        break;
                    case 5: //通讯录
                        index = 14;
                        stop();
                        startMusic(Constant.musices[8]);
                        setDialog4();
                        break;
                    case 6: //联系人
                        index = 15;
                        stop();
                        startMusic(Constant.musices[3]);
                        setDialog4();
                        break;
                    case 7: //群好友
                        index = 16;
                        stop();
                        startMusic(Constant.musices[3]);
                        setDialog4();
                        break;
                    case 8: //公众号
//                        index = 17;
//                        stop();
//                        start(Constant.musices[3]);
//                        setDialog4();
                        Snackbar.make(view, "暂未开放，敬请期待", Snackbar.LENGTH_SHORT).setAction("sss", null).show();
                        break;
                    case 9: //智能回复
//                        index = 18;
//                        stop();
//                        start(Constant.musices[10]);
//                        setDialog5();
                        Snackbar.make(view, "暂未开放，敬请期待", Snackbar.LENGTH_SHORT).setAction("sss", null).show();
                        break;
                    case 10: //朋友圈
//                        index = 19;
//                        setDialog4();
//                        start(Constant.musices[3]);
                        Snackbar.make(view, "暂未开放，敬请期待", Snackbar.LENGTH_SHORT).setAction("sss", null).show();
                        break;
                    case 11: //QQ一键发消息
                        index = 20;
                        setDialog4();
                        startMusic(Constant.musices[3]);
//                        startActivity(new Intent(getActivity(), QQDefaultDataActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

        //陌陌助手界面
        View v3 = getActivity().getLayoutInflater().inflate(R.layout.momo_layout, null);
        listViews.add(v1);
        listViews.add(v2);
        listViews.add(v3);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }


    /**
     * 微信网络操作相关的子线程
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
//                app.hour+":"+app.minute+":"+app.interval
                //定时

                Log.i("zw1027","hour:"+hour+",minute:"+minute+",interval:"+interval+",,,BBBBBBB");
                String strVal = "";
                if (index==27){
                    strVal = Constant.indexStr[index]+";"+hour+";"+minute+";"+interval;
                }else {
                    strVal = Constant.indexStr[index];
                }

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
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setDialog() {
        customerDialog = new CustomerDialog(getActivity(), new Callback() {
            @Override
            public void positiveCallback() {
                stop();
                startMusic(Constant.musices[4]);
                new Thread(networkTask).start();
                ToastUtils.showShort(getActivity(), "开始");
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(getActivity(), "取消");
            }
        });
        customerDialog.setContent("提示" + "\n主人，懒懒要开始咯?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //息屏对话框
    public void setDialog1() {
        customerDialog = new CustomerDialog(getActivity(), new Callback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void positiveCallback() {
                new Thread(networkTask).start();
                stop();
                ToastUtils.showShort(getActivity(), "好的");
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(getActivity(), "再说");
            }
        });
        customerDialog.setContent("提示" + "\n主人，让懒懒休息一会吧?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //停止对话框
    public void setDialog2() {
        customerDialog = new CustomerDialog(getActivity(), new Callback() {
            @Override
            public void positiveCallback() {
                new Thread(networkTask).start();
                stop();
                ToastUtils.showShort(getActivity(), "好的");
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(getActivity(), "不要");
            }
        });
        customerDialog.setContent("提示" + "\n主人，懒懒要走咯?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //朋友圈对话框
    public void setDialog3() {
        confirmDialog = new ConfirmDialog(getActivity(), new Callback() {
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
        customerDialog = new CustomerDialog(getActivity(), new Callback() {
            @Override
            public void positiveCallback() {
                stop();
                startMusic(Constant.musices[5]);
                new Thread(networkTask).start();
                ToastUtils.showShort(getActivity(), "开始");
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(getActivity(), "取消");
            }
        });
        customerDialog.setContent("提示" + "\n主人，懒懒要开始咯?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }
    //开启定时
    public void setDialogDingShi() {
        final boolean isDingShi = PrefUtils.getBoolean(getActivity(),"isDingShi",true);
        customerDialog = new CustomerDialog(getActivity(), new Callback() {
            @Override
            public void positiveCallback() {

                index = isDingShi?27:28;
                stop();
                startMusic(Constant.musices[5]);
                new Thread(networkTask).start();
                ToastUtils.showShort(getActivity(), "开始");
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(getActivity(), "取消");
            }
        });
        hour = PrefUtils.getInt(getActivity(),"hour",8);
        minute = PrefUtils.getInt(getActivity(),"minute",30);
        interval = PrefUtils.getInt(getActivity(),"interval",24);
        String minuteStr = String.format("%02d",minute);
        customerDialog.setContent(
                "提示" + (isDingShi?("\n主人，第一次运行时间为"
                        +hour+":"+minuteStr
                        +"，每间隔"+
                        interval+"小时重复运行，可在设置中进行修改哦！"):"\n确定取消?"));
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //懒懒助手对话框
    public void setDialog5() {
        customerDialog = new CustomerDialog(getActivity(), new Callback() {
            @Override
            public void positiveCallback() {
                stop();
                Intent intent = new Intent(getActivity(), IntelligentActivity.class);
                startActivity(intent);
            }

            @Override
            public void negativeCallback() {
                stop();
                ToastUtils.showShort(getActivity(), "取消");
            }
        });
        customerDialog.setContent("提示" + "\n主人，是否启用智能回复?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    public void setSwitchAccountDialog() {
        customerDialog = new CustomerDialog(getActivity(), new Callback() {
            @Override
            public void positiveCallback() {
                String time = PrefUtils.getString(getActivity(), "time", "");
                if (new Date().getTime() - (Long.parseLong(time)) >= 60000) {
                    index = 23;
                    PrefUtils.putString(getActivity(),"time",new Date().getTime()+"");
                    new Thread(networkTask).start();
                }else {
                    ToastUtils.showShort(getActivity(), "你的时间间隔太短");
                }
            }

            @Override
            public void negativeCallback() {

            }
        });
        customerDialog.setContent("提示" + "\n主人，自动换号功能与上一个运行微信号最后运行间隔最少2小时");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //开始播放音乐
    public void startMusic(int musArr) {
        Intent intent;
        intent = new Intent(getActivity(), MusiceService.class);
        intent.putExtra("raw", musArr);
        getActivity().startService(intent);
    }

    //停止音乐
    public void stop() {
        Intent intent = new Intent(getActivity(), MusiceService.class);
        getActivity().stopService(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout, 30, 30);
            }
        });
    }

    /**
     * 设置tablayout下划线长度的方法
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /****
     * 更新状态
     */
    public void updateWhiteDate(final String status) {
        Thread th = new Thread() {
            @Override
            public void run() {
                String url1 = Urlutils.updateBaiMingDanStatus;
                OkHttpUtils
                        .post()
                        .url(url1)
                        .addParams("status", status)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (MyJsonUtil.getBeanByJson(response)) {
                                    setDialog4();
                                    startMusic(Constant.musices[3]);
                                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        };
        th.start();
    }


}
