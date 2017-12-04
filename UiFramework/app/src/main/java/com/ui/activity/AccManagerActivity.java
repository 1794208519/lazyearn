package com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.Interface.View.AccountCallbak;
import com.base.BaseActivity;
import com.entity.AccountBean;
import com.entity.AccountUpdateBean;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;
import com.ui.Adapter.MyAccmanagerAdapter;
import com.uidemo.R;
import com.utils.MyJsonUtil;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by vicmob_yf002 on 2017/5/19.
 */
public class AccManagerActivity extends BaseActivity implements View.OnClickListener {
    //账号适配器
    private MyAccmanagerAdapter myAccmanagerAdapter;
    //账号数据集合，用于适配器加载数据
    private AccountUpdateBean accountUpdateBean;
    //recyclerview
    private RecyclerView recyclerView;
    private RelativeLayout accmanager_load;
    private RelativeLayout accmanager_none;
    private ImageView mBack;
    private Button mManager;
    //显示回调数据通知
    private static final int SUCCESS = 1;
    private static final int NONE = 2;
    public static final int DESUCCESS = 3;
    public static final int DEFAILS = 4;
    //加载动画图片
    private ImageView imageView;
    //动画
    AnimationDrawable loadingDrawable;
    //账号地址
    private String url = Urlutils.getSelectAccountUrl();
    private String deleteUrl = Urlutils.getDeleteAccountUrl();

    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    //更新ui
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //加载数据
                            try {
                                Thread.sleep(1200);
                            } catch (InterruptedException e) {
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    accmanager_load.setVisibility(View.INVISIBLE);
                                    accmanager_none.setVisibility(View.INVISIBLE);
                                    myAccmanagerAdapter = new MyAccmanagerAdapter(accountUpdateBean.getAuto_account(), AccManagerActivity.this, new AccountCallbak() {
                                        @Override
                                        public void positiveCallbak(int position) {
                                            deleteThread(accountUpdateBean.getAuto_account().get(position).getAccountId()+"");
                                         }
                                    });
                                    recyclerView.setAdapter(myAccmanagerAdapter);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }).start();
                    break;
                case NONE:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //加载数据
                            try {
                                Thread.sleep(1200);
                            } catch (InterruptedException e) {
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setVisibility(View.INVISIBLE);
                                    accmanager_load.setVisibility(View.INVISIBLE);
                                    accmanager_none.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }).start();
                    break;
                case DESUCCESS:

                    if (msg.obj.equals("1")) {
                        ToastUtils.showShort(AccManagerActivity.this, "删除成功");
                        Alerter.create(AccManagerActivity.this)
                                                    .setTitle("删除成功")
                                                    .setDuration(500)
                                                    .setBackgroundColor(R.color.text_blue)
                                                    .show();
                    } else {
                        ToastUtils.showShort(AccManagerActivity.this, "删除失败");
                    }
                    break;
                case DEFAILS:
                    ToastUtils.showShort(AccManagerActivity.this, "删除失败");
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_accmanager;
    }

    @Override
    public void findView() {
        mBack = (ImageView) findViewById(R.id.accmanager_back);
        mManager = (Button) findViewById(R.id.accmanager_btn);
        recyclerView = (RecyclerView) findViewById(R.id.main_accmanagerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accmanager_load = (RelativeLayout) findViewById(R.id.accmanager_load);
        accmanager_none = (RelativeLayout) findViewById(R.id.accmanager_noneww);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mManager.setOnClickListener(this);
    }

    @Override
    public void initData() {
        recyclerView.setVisibility(View.INVISIBLE);
        accmanager_load.setVisibility(View.VISIBLE);
        accmanager_none.setVisibility(View.INVISIBLE);
        echoData();
    }

    /**
     * 数据回显
     */
    private void echoData() {

        loadThread();
        //显示加载的那个动画
        imageView = (ImageView) findViewById(R.id.accmanagerloading_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        loadingDrawable.start();
    }
    /**
     * 获取所有的添加的总的地点
     */
    private void loadThread() {
        Thread th = new Thread() {
            @Override
            public void run() {
                String url1 = url;
                OkHttpUtils
                        .post()
                        .url(url1)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(NONE);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("123",new Gson().toJson(response));
                                try {
                                    accountUpdateBean= MyJsonUtil.getBeanByJson(response,AccountUpdateBean.class);
                                    myHandler.sendEmptyMessage(SUCCESS);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    myHandler.sendEmptyMessage(NONE);
                                }
                            }
                        });
            }
        };
        th.start();
    }
    /**
     * 删除数据
     *
     * @param
     */
    public void deleteThread(final String Id) {

        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(deleteUrl)
                        .addParams("Id", Id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(DEFAILS);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("123",response);
                                Message msg = myHandler.obtainMessage();
                                msg.what = DESUCCESS;
                                msg.obj = response;
                                myHandler.sendMessage(msg);
                                if (accountUpdateBean.getAuto_account().size()==0){
                                    myHandler.sendEmptyMessage(NONE);
                                }
                            }
                        });
            }
        };
        th.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //重显数据
        initData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accmanager_back:
                finish();
                break;
            case R.id.accmanager_btn:
                Intent intent = new Intent(AccManagerActivity.this, AccountActivity.class);
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }
}
