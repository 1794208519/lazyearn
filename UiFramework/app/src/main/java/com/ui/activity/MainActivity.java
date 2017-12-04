package com.ui.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.Interface.View.FragmentBackListener;
import com.base.BaseActivity;
import com.entity.ApkBean;
import com.entity.ItemBean;
import com.google.gson.Gson;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.uidemo.R;
import com.utils.Constant;
import com.utils.DownLoadAppUtils;
import com.utils.ItemDataUtils;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.DragLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

import okhttp3.Call;


public class MainActivity extends BaseActivity {
    private int index = 0;
    public String code;
    private DragLayout dl;
    private ListView lv;
    private ImageView ivIcon, ivBottom;
    private QuickAdapter<ItemBean> quickAdapter;
    // fragment的返回事件
    private FragmentBackListener backListener;
    private boolean isInterception = false;
    /**
     * 定义结束时间
     */
    private long exitTime = 0;
    //更新配置文件路径
    private String versionUrl = Urlutils.getVersionUrl();
    //apk更新路径
    private String apkPath = Urlutils.getApkUrl();
    //当前版本号
    private int mCurrentCode;

    private FragmentManager fragment;
    private FragmentTransaction ftransaction;

    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSwipeBackLayout().setEnableGesture(false);
        code = getSerialNumber();
        Log.e("123",""+getSerialNumber());
        //判断网络连接与否

        if (isNetworkAvailable(getApplicationContext())) {
            connToService();
        } else {
            ToastUtils.showShort(MainActivity.this, "无法连接网络...");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void findView() {
        dl = (DragLayout) findViewById(R.id.dl);

        ivBottom = (ImageView) findViewById(R.id.iv_bottom);
        lv = (ListView) findViewById(R.id.lv);



    }

    @Override
    public void initView() {
        //        setStatusBar();
    }


    @Override
    public void initListener() {
        lv.setAdapter(quickAdapter = new QuickAdapter<ItemBean>(this, R.layout.item_left_layout, ItemDataUtils.getItemBeans()) {
            @Override
            protected void convert(BaseAdapterHelper helper, ItemBean item) {
                helper.setImageResource(R.id.item_img, item.getImg())
                        .setText(R.id.item_tv, item.getTitle());
            }
        });
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                switch (position) {
//                    case 0:
//                        Intent intent = new Intent(MainActivity.this, AccManagerActivity.class);
//                        startActivity(intent);
//                        startThread();
//                        break;
                    case 0:
                        Intent intent1 = new Intent(MainActivity.this, AdrManagerActivity.class);
//                        intent1.putExtra("device", "");
                        startActivityForResult(intent1, 1);
                        startThread();
                        break;
                    case 1:
                        Intent intent2 = new Intent(MainActivity.this, CityActivity.class);
                        startActivity(intent2);
                        startThread();
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, DeleteDeadFriendsActivity.class));
                        break;
                    case 3:
                        index=21;
                        showNormalDialog();
                    default:
                        Snackbar.make(arg1, "暂未开放，敬请期待", Snackbar.LENGTH_SHORT).setAction("sss", null).show();
                        break;
                }
            }
        });
     /*   ivBottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
                startThread();
            }
        });*/
    }

    @Override
    public void initData() {

    }
    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("主人，懒懒要开始咯?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(networkTask).start();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
    /**
     * 2次退出效果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 如果进入fragment返回键的监听判断为true
            if (isInterception()) {
                if (backListener != null) {
                    backListener.onbackForward();
                }
            } else if (dl.getStatus() == DragLayout.Status.OPEN) {
                dl.close();
            } else {
                exit();
            } // 按返回键，true则退出
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() { // 按返回退出
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showShort(getApplicationContext(), "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else { // 退出
            finish();
            System.exit(0);
        }

    }

    // 得到fragment返回监听
    public FragmentBackListener getBackListener() {
        return backListener;
    }

    // 设置fragment返回监听
    public void setBackListener(FragmentBackListener backListener) {
        this.backListener = backListener;
    }

    public boolean isInterception() {
        return isInterception;
    }

    // 设置是否进入判断
    public void setInterception(boolean isInterception) {
        this.isInterception = isInterception;
    }

    /**
     * 延时关闭菜单,避免视觉上的交错体验
     */
    private void startThread() {
        dl.postDelayed(new Runnable() {
            @Override
            public void run() {
                dl.close();
            }
        }, 400);
    }

    // 检查网络连接状态
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static String getSerialNumber(){

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
    //连接服务器,检查新版本
    private void connToService() {
        OkHttpUtils.get()
                .url(versionUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DownLoadAppUtils downLoadAppUtils = new DownLoadAppUtils(apkPath, MainActivity.this);
                        mCurrentCode = downLoadAppUtils.setVersionCode();
                        try {
                            ApkBean apkBean = processData(response);
                            if (apkBean.getWxversion().getVersionCode() == mCurrentCode) {
//                                ToastUtils.showShort(MainActivity.this, "当前已是最新版本");
                            } else if (apkBean.getWxversion().getVersionCode() > mCurrentCode) {
                                downLoadAppUtils.updateApp(MainActivity.this, apkBean.getWxversion().getVersionName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //把Json格式的字符串转换成对应的模型对象
    public ApkBean processData(String json) {
        Gson gson = new Gson();
        ApkBean apkBean = gson.fromJson(json, ApkBean.class);
        return apkBean;
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

                os.close();
            }  catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
