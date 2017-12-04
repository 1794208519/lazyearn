package com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.Interface.View.AccountCallbak;
import com.base.BaseActivity;
import com.entity.AddressBean;
import com.entity.Addresstimebean;
import com.google.gson.Gson;
import com.ui.Adapter.MyAddressAdapter;
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
public class AdrManagerActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBack;
    private Button mManager;
    private String url = Urlutils.getSelectAdd();
    private String deleteUrl = Urlutils.getDeleteAdd();
    public static final int SUCCESS = 1;
    public static final int FAILS = 2;
    public static final int DESUCCESS = 3;
    public static final int DEFAILS = 4;
    public static final int NONE = 5;

    private RecyclerView mRecyclerView;
    private MyAddressAdapter mAdapter;
    //存放地址对象的集合
    private List<Addresstimebean> mAddressBean;
    //加载动画图片
    private ImageView imageView;
    //动画
    AnimationDrawable loadingDrawable;
    private RelativeLayout add_load;
    private RelativeLayout add_error;
    private RelativeLayout add_none;

    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }

    public Handler myHandler = new Handler() {
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
                                    add_load.setVisibility(View.INVISIBLE);
                                    add_error.setVisibility(View.INVISIBLE);
                                    add_none.setVisibility(View.INVISIBLE);
                                    mAdapter = new MyAddressAdapter(mAddressBean, AdrManagerActivity.this, new AccountCallbak() {
                                        @Override
                                        public void positiveCallbak(int position) {
                                            deleteThread(mAddressBean.get(position).getDataId());
                                        }
                                    });
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }).start();
                    break;
                case NONE:
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
                                    mRecyclerView.setVisibility(View.INVISIBLE);
                                    add_load.setVisibility(View.INVISIBLE);
                                    add_error.setVisibility(View.INVISIBLE);
                                    add_none.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }).start();
                    break;
                case FAILS:
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
                                    mRecyclerView.setVisibility(View.INVISIBLE);
                                    add_load.setVisibility(View.INVISIBLE);
                                    add_error.setVisibility(View.VISIBLE);
                                    add_none.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }).start();
                    break;
                case DESUCCESS:
                    if ((Boolean) msg.obj) {
                        ToastUtils.showShort(AdrManagerActivity.this, "删除成功");

                    } else {
                        ToastUtils.showShort(AdrManagerActivity.this, "删除失败");
                    }
                    break;
                case DEFAILS:
                    ToastUtils.showShort(AdrManagerActivity.this, "删除失败");
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_addressmanager;
    }

    @Override
    public void findView() {
        mBack = (ImageView) findViewById(R.id.addManager_back);
        mManager = (Button) findViewById(R.id.addManager_btn);
        add_load = (RelativeLayout) findViewById(R.id.add_load);
        add_error = (RelativeLayout) findViewById(R.id.add_error);
        add_none = (RelativeLayout) findViewById(R.id.add_noneww);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_addressList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(AdrManagerActivity.this));
        mBack.setOnClickListener(this);
        mManager.setOnClickListener(this);

    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {


    }

    @Override
    public void initData() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        add_load.setVisibility(View.VISIBLE);
        add_error.setVisibility(View.INVISIBLE);
        add_none.setVisibility(View.INVISIBLE);
        loadThread();
        //显示加载的那个动画
        imageView = (ImageView) findViewById(R.id.loading_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        loadingDrawable.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addManager_back:
                finish();
                break;
            case R.id.addManager_btn:
                finish();
                break;
        }
    }

    //重新加载数据
    public void reLoadAddress(View view) {
        initData();
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
                                myHandler.sendEmptyMessage(FAILS);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    AddressBean addressBean = MyJsonUtil.getBeanByJson(response,AddressBean.class);
                                    mAddressBean = new ArrayList<Addresstimebean>();
                                    for (int i = 0; i < addressBean.getAuto_data().size(); i++) {
                                        Addresstimebean addresstimebean=new Addresstimebean();
                                        addresstimebean.setAddress(addressBean.getAuto_data().get(i).getAddress());
                                        addresstimebean.setDataId(addressBean.getAuto_data().get(i).getDataId()+"");
                                        addresstimebean.setDevices(addressBean.getAuto_data().get(i).getDevices());
                                        addresstimebean.setHello(addressBean.getAuto_data().get(i).getHello());
                                        addresstimebean.setLatitude(addressBean.getAuto_data().get(i).getLatitude());
                                        addresstimebean.setLongitude(addressBean.getAuto_data().get(i).getLongitude());
                                        mAddressBean.add(addresstimebean);
                                    }
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
    public void deleteThread(final String dataid) {
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(deleteUrl)
                        .addParams("dataId", dataid)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(DEFAILS);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Message msg = myHandler.obtainMessage();
                                msg.what = DESUCCESS;
                                msg.obj = MyJsonUtil.getBeanByJson(response);
                                myHandler.sendMessage(msg);
                                if (mAddressBean.size()==0){
                                    myHandler.sendEmptyMessage(NONE);
                                }
                            }
                        });
            }
        };
        th.start();
    }

    //把Json格式的字符串转换成对应的模型对象
    public AddressBean processData(String json) {
        Gson gson = new Gson();
        AddressBean addressBean1 = gson.fromJson(json, AddressBean.class);
        return addressBean1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }
}
