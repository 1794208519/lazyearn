package com.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.Interface.View.AccountCallbak;
import com.base.BaseDeviceFgmnt;
import com.entity.OperatorsCityBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.Adapter.DeviceCityAdapter;
import com.ui.activity.CityActivity;
import com.ui.view.swipedeletelayout.SwipeLayoutManager;
import com.uidemo.R;
import com.utils.DialogUtil;
import com.utils.MyJsonUtil;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.ui.activity.DeviceUpdateActivity.deviceSN;

/**
 *
 */
public class DeviceleftFragment extends BaseDeviceFgmnt implements View.OnClickListener {
    private Button mManager;
    private RecyclerView mRecyclerView;
    //加载动画图片
    private ImageView imageView;
    //动画
    AnimationDrawable loadingDrawable;
    private RelativeLayout add_load;
    private RelativeLayout add_error;
    private RelativeLayout add_none, main_list;
    public static final int SUCCESS = 1;
    public static final int FAILS = 2;
    public static final int DESUCCESS = 3;
    public static final int DEFAILS = 4;
    public static final int NONE = 5;
    public static final int UPSUCCESS = 6;
    public static final int DPFail = 7;
    private String url = Urlutils.getSelectCity();
    private String deleteUrl = Urlutils.getDeleteCity();
    private String insertUrl = Urlutils.getInsertCity();
    private String updateUrl = Urlutils.getUpdateCity();
    //存放运营商城市对象的集合
    private OperatorsCityBean operatorsCityBeanList = new OperatorsCityBean();

    private DeviceCityAdapter mAdapter;
    private HashMap<String, Boolean> accountMap = new HashMap<String, Boolean>();
    //删除，应用
    private Button btn_delete, btn_save, btn_cancelsave;
    //全选
    private Button ck_total;

    private boolean istotal;


    public Handler myHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_deviceleft;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init(inflater, container, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        mManager = (Button) findViewById(R.id.addManager_btn);
        add_load = (RelativeLayout) findViewById(R.id.add_load);
        add_error = (RelativeLayout) findViewById(R.id.add_error);
        add_none = (RelativeLayout) findViewById(R.id.add_noneww);
        main_list = (RelativeLayout) findViewById(R.id.main_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_addressList);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_save = (Button) findViewById(R.id.btn_save);
        ck_total = (Button) findViewById(R.id.ck_total);
        btn_cancelsave = (Button) findViewById(R.id.btn_cancelsave);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mManager.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        ck_total.setOnClickListener(this);
        btn_cancelsave.setOnClickListener(this);
    }

    public void initData() {
        main_list.setVisibility(View.INVISIBLE);
        add_load.setVisibility(View.VISIBLE);
        add_error.setVisibility(View.INVISIBLE);
        add_none.setVisibility(View.INVISIBLE);
        loadThread();
        //显示加载的那个动画
        imageView = (ImageView) findViewById(R.id.loading_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        loadingDrawable.start();

        mAdapter = new DeviceCityAdapter(operatorsCityBeanList.getAuto_City(), getActivity(), accountMap, new AccountCallbak() {
            @Override
            public void positiveCallbak(int position) {
                accountMap.remove(operatorsCityBeanList.getAuto_City().get(position).getCityId() + "");
                //                                            new AccountDao(getActivity()).delete(mAccountBean.get(position));

                deleteThread(operatorsCityBeanList.getAuto_City().get(position).getCityId() + "");
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SUCCESS:
                        if (myHandler != null) {
                            if (operatorsCityBeanList.getAuto_City().size() == 0) {
                                myHandler.sendEmptyMessage(NONE);
                            }
                        }
                        //更新ui
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //加载数据
                                try {
                                    Thread.sleep(1200);
                                } catch (InterruptedException e) {
                                }
                                if (getActivity() == null)
                                    return;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        add_load.setVisibility(View.INVISIBLE);
                                        add_error.setVisibility(View.INVISIBLE);
                                        add_none.setVisibility(View.INVISIBLE);
                                        for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                                            accountMap.put(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "", false);
                                        }
                                        Log.i("zw", new Gson().toJson(operatorsCityBeanList.getAuto_City()));
                                        mAdapter.refresh(operatorsCityBeanList.getAuto_City());
                                        main_list.setVisibility(View.VISIBLE);
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
                                if (getActivity() == null)
                                    return;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        main_list.setVisibility(View.INVISIBLE);
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
                                if (getActivity() == null)
                                    return;
                                getActivity().runOnUiThread(new Runnable() {
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
                        if (myHandler != null) {
                            if ((Boolean) msg.obj) {
                                ToastUtils.showShort(getActivity(), "删除成功");
                                if (operatorsCityBeanList.getAuto_City().size() == 0) {
                                    myHandler.sendEmptyMessage(NONE);
                                }
                            } else {
                                ToastUtils.showShort(getActivity(), "删除失败");
                            }
                        }
                        break;
                    case DEFAILS:
                        ToastUtils.showShort(getActivity(), "删除失败");
                        break;
                    case UPSUCCESS:
                        if (myHandler != null) {
                            if ((Boolean) msg.obj) {
                                ToastUtils.showShort(getActivity(), "操作成功");
                                //                        mAdapter.refresh(operatorsCityBeanList);
                                if (operatorsCityBeanList.getAuto_City().size() == 0) {
                                    myHandler.sendEmptyMessage(NONE);
                                }
                            } else {
                                ToastUtils.showShort(getActivity(), "操作失败");
                            }
                        }
                        break;
                    case DPFail:
                        ToastUtils.showShort(getActivity(), "操作失败");
                        break;
                }
            }
        };
    }

    /**
     * 获取所有的添加的总的地点
     */
    private void loadThread() {
        //        accountBeanList = new AccountDao(getActivity()).queryForAll();
        //deviceSN.trim()
        Thread th = new Thread() {
            @Override
            public void run() {
                String url1 = url;
                OkHttpUtils
                        .post()
                        .url(url1)
                        .addParams("devices", deviceSN.trim())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                if (myHandler != null) {
                                    myHandler.sendEmptyMessage(NONE);
                                }
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("zw810", response + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                                if (myHandler != null) {
                                    try {
                                        //operatorsCityBeanList = processData(response);
                                        //                                    Type type=new TypeToken<List<OperatorsCityBean>>(){}.getType();
                                        Log.i("zw810", response + "1211111111111111111111111111111111");
                                        operatorsCityBeanList = MyJsonUtil.getBeanByJson(response, OperatorsCityBean.class);
                                        Log.i("zw810", operatorsCityBeanList.getAuto_City().get(0).toString() + "##########################");
                                        myHandler.sendEmptyMessage(SUCCESS);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        myHandler.sendEmptyMessage(NONE);
                                    }
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
    public void deleteThread(final String id) {
        Log.i("zw", id);
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(deleteUrl)
                        .addParams("cityId", id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                if (myHandler != null) {
                                    myHandler.sendEmptyMessage(DEFAILS);
                                }
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("123", response);
                                if (myHandler != null) {
                                    Message msg = myHandler.obtainMessage();
                                    msg.what = DESUCCESS;
                                    msg.obj = MyJsonUtil.getBeanByJson(response);
                                    myHandler.sendMessage(msg);
                                }
                            }
                        });
            }
        };
        th.start();
    }
/*    */

    /**
     * 添加数据
     *
     * @param
     *//*
    public void insertThread(final String city,final String operator) {
        final Map<String,String> params=new HashMap<>();
        params.put("city",city);
        params.put("operator",operator);
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(insertUrl)
                        .params(params)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(NONE);
                            }

                            @Override
                            public void onResponse(String response, int id) {

                                Message msg = myHandler.obtainMessage();
                                msg.what = SUCCESS;
                                msg.obj = response;
                                myHandler.sendMessage(msg);
                            }
                        });
            }
        };
        th.start();
    }*/
    //
    public void UpdateThread(final String id, final String devices, final int i) {
        Log.i("zw", id);
        final Map<String, String> params = new HashMap<>();
        params.put("cityId", id);
        params.put("devices", devices.trim());
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(updateUrl)
                        .params(params)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                if (myHandler != null) {
                                    myHandler.sendEmptyMessage(NONE);
                                }
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (myHandler != null) {
                                    Message msg = myHandler.obtainMessage();
                                    msg.what = UPSUCCESS;
                                    msg.obj = MyJsonUtil.getBeanByJson(response);
                                    myHandler.sendMessage(msg);
                                    //                                operatorsCityBeanList.get(i).setDevices(devices);
                                    loadThread();
                                }
                            }
                        });
            }
        };
        th.start();
    }

    //把Json格式的字符串转换成对应的模型对象集合
    public List<OperatorsCityBean> processData(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<OperatorsCityBean>>() {
        }.getType();

        //List<OperatorsCityBean> operatorsCityBean = gson.fromJson(json, OperatorsCityBean.class);

        List<OperatorsCityBean> operatorsCityBeanList = gson.fromJson(json, type);
        return operatorsCityBeanList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                int k = 0;
                for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                    if (accountMap.get(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "")) {
                        k++;
                    }
                }
                if (k > 0) {
                    DialogUtil.createConfirmDialog(getActivity(), "提示", "你确定要删除吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<OperatorsCityBean.AutoCityBean> listdate = new ArrayList<OperatorsCityBean.AutoCityBean>();
                            for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                                if (accountMap.get(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "") == true) {
                                    //                               new AccountDao(getActivity()).delete(mAccountBean.get(i));
                                    deleteThread(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "");
                                    listdate.add(operatorsCityBeanList.getAuto_City().get(i));

                                    accountMap.remove(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "");
                                    Log.i("123", accountMap.size() + "B");
                                    //这俩句至关重要，实现了删除条目时的过度效果
                                    mAdapter.notifyItemRemoved(i);
                                    mAdapter.notifyItemRangeChanged(i, operatorsCityBeanList.getAuto_City().size());
                                    SwipeLayoutManager.create().closeLayout();
                                }
                            }
                            operatorsCityBeanList.getAuto_City().removeAll(listdate);
                            mAdapter.notifyDataSetChanged();


                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }, -1).show();
                } else {
                    DialogUtil.createMessageDialog(getActivity(), "提示", "请选择删除的内容！", "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }, -1).show();
                }

                break;
            case R.id.btn_save:
                int m = 0;
                int n = 0;
                for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                    if (accountMap.get(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "")) {
                        m++;
                    }
                    if (operatorsCityBeanList.getAuto_City().get(i).getDevices() != null && !operatorsCityBeanList.getAuto_City().get(i).getDevices().equals("null") && !operatorsCityBeanList.getAuto_City().get(i).getDevices().equals("")) {
                        n++;
                    }
                }
                if (m > 1 || n > 0) {
                    DialogUtil.createMessageDialog(getActivity(), "提示", "一个设备只能设置一个城市运营商！", "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }, -1).show();
                } else {
                    DialogUtil.createConfirmDialog(getActivity(), "提示", "你确定要应用吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int a = 0;
                            for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                                if (accountMap.get(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "")) {
                                    a++;
                                }
                            }
                            if (a == 0) {
                                DialogUtil.createMessageDialog(getActivity(), "提示", "没有选择应用的城市！", "我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                }, -1).show();
                            }
                            for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                                if (accountMap.get(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "") == true) {
                                    //                               new AccountDao(getActivity()).delete(mAccountBean.get(i));
                                    UpdateThread(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "", deviceSN, i);

                                }
                            }
                            //                            mAdapter.notifyDataSetChanged();

                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }, -1).show();
                }

                break;
            case R.id.addManager_btn:
                Intent intent = new Intent(getActivity(), CityActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.ck_total:
                if (istotal) {
                    for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                        Log.i("123", operatorsCityBeanList.getAuto_City().get(i) + "D");
                        accountMap.put(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "", false);

                    }
                    ck_total.setBackground(getResources().getDrawable(R.drawable.device_mid));
                    ck_total.setTextColor(getResources().getColor(R.color.button_text1));
                    istotal = false;
                } else {
                    for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                        Log.i("123", operatorsCityBeanList.getAuto_City().get(i) + "D");
                        accountMap.put(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "", true);

                    }
                    ck_total.setBackground(getResources().getDrawable(R.drawable.device_mid1));
                    ck_total.setTextColor(getResources().getColor(R.color.white));
                    istotal = true;
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_cancelsave:

                DialogUtil.createConfirmDialog(getActivity(), "提示", "你确定要取消应用吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int k = 0;
                        for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                            if (accountMap.get(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "")) {
                                k++;
                            }

                        }
                        if (k == 0) {
                            DialogUtil.createMessageDialog(getActivity(), "提示", "没有选择取消应用的城市！", "我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            }, -1).show();
                        }
                        for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {

                            if (accountMap.get(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "")
                                    && (operatorsCityBeanList.getAuto_City().get(i).getDevices() == null)) {
                                DialogUtil.createMessageDialog(getActivity(), "提示", "没有应用的城市不能取消！", "我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                }, -1).show();
                                return;
                            }
                        }
                        for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {
                            if (accountMap.get(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "") && operatorsCityBeanList.getAuto_City().get(i).getDevices() != null && (operatorsCityBeanList.getAuto_City().get(i).getDevices().equals("null") || operatorsCityBeanList.getAuto_City().get(i).getDevices().equals(""))) {
                                DialogUtil.createMessageDialog(getActivity(), "提示", "没有应用的城市不能取消！", "我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        return;
                                    }
                                }, -1).show();
                                return;
                            }
                        }
                        for (int i = 0; i < operatorsCityBeanList.getAuto_City().size(); i++) {

                            if (accountMap.get(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "") && operatorsCityBeanList.getAuto_City().get(i).getDevices() != null && !operatorsCityBeanList.getAuto_City().get(i).getDevices().equals("null") && !operatorsCityBeanList.getAuto_City().get(i).getDevices().equals("")) {
                                UpdateThread(operatorsCityBeanList.getAuto_City().get(i).getCityId() + "", "", i);

                            }
                        }

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }, -1).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            main_list.setVisibility(View.INVISIBLE);
            add_load.setVisibility(View.VISIBLE);
            add_error.setVisibility(View.INVISIBLE);
            add_none.setVisibility(View.INVISIBLE);

            loadThread();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != myHandler) {
            myHandler.removeMessages(1);
            myHandler.removeMessages(2);
            myHandler.removeMessages(3);
            myHandler.removeMessages(4);
            myHandler.removeMessages(5);
            myHandler.removeMessages(6);
            myHandler.removeMessages(7);
            myHandler = null;

        }
    }

}
