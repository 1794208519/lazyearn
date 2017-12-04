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
import android.widget.Toast;

import com.Interface.View.AccountCallbak;
import com.base.BaseDeviceFgmnt;
import com.entity.AccountUpdateBean;
import com.entity.Addresstimebean;
import com.entity.OperatorsCityBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.Adapter.DeviceAccountAdapter;
import com.ui.activity.AccountActivity;
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
public class DeviceAccountFragment extends BaseDeviceFgmnt implements View.OnClickListener {
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
    private String url = Urlutils.getSelectAccountUrl();
    private String deleteUrl = Urlutils.getDeleteAccountUrl();
    private String insertUrl = Urlutils.getInsertAccountUrl();
    private String updateUrl = Urlutils.getUpdateAccountUrl();
    //存放运营商城市对象的集合
    private AccountUpdateBean accountUpdateBean = new AccountUpdateBean();

    private DeviceAccountAdapter mAdapter;
    private HashMap<String, Boolean> accountMap = new HashMap<String, Boolean>();
    //删除，应用
    private Button btn_delete, btn_save, btn_cancelsave;
    //全选
    private Button ck_total;

    private boolean istotal;


    public Handler myHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device_account;
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

        mAdapter = new DeviceAccountAdapter(accountUpdateBean.getAuto_account(), getActivity(), accountMap, new AccountCallbak() {
            @Override
            public void positiveCallbak(int position) {
                accountMap.remove(accountUpdateBean.getAuto_account().get(position).getAccountId() + "");
                if (accountUpdateBean.getAuto_account().get(position).getDevices() != null && !accountUpdateBean.getAuto_account().get(position).getDevices().equals("null") && !accountUpdateBean.getAuto_account().get(position).getDevices().equals("")) {
                    mAdapter.setSelectnumber(mAdapter.getSelectnumber() - 1);
                }
                deleteThread(accountUpdateBean.getAuto_account().get(position).getAccountId() + "");
            }
        });
        mRecyclerView.setAdapter(mAdapter);
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
                                if (myHandler != null) {
                                    try {
                                        accountUpdateBean = MyJsonUtil.getBeanByJson(response, AccountUpdateBean.class);
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
                        .addParams("accountId", id)
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
                                if (myHandler != null) {

                                    Log.i("123", response);
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

    public void UpdateThread(final String id, final String devices, final int i) {
        Log.i("zw", id);
        final Map<String, String> params = new HashMap<>();
        params.put("accountId", id);
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
                                    mAdapter.setSelectnumber(0);
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
                for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {
                    if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "")) {
                        k++;
                    }
                }
                if (k > 0) {
                    DialogUtil.createConfirmDialog(getActivity(), "提示", "你确定要删除吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<AccountUpdateBean.AutoAccountBean> listdate = new ArrayList<AccountUpdateBean.AutoAccountBean>();
                            for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {
                                if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "") == true) {
                                    if (accountUpdateBean.getAuto_account().get(i).getDevices() != null && !accountUpdateBean.getAuto_account().get(i).getDevices().equals("null") && !accountUpdateBean.getAuto_account().get(i).getDevices().equals("")) {
                                        mAdapter.setSelectnumber(mAdapter.getSelectnumber() - 1);
                                    }
                                    deleteThread(accountUpdateBean.getAuto_account().get(i).getAccountId() + "");
                                    listdate.add(accountUpdateBean.getAuto_account().get(i));

                                    accountMap.remove(accountUpdateBean.getAuto_account().get(i).getAccountId() + "");
                                    Log.i("123", accountMap.size() + "B");
                                    //这俩句至关重要，实现了删除条目时的过度效果
                                    mAdapter.notifyItemRemoved(i);
                                    mAdapter.notifyItemRangeChanged(i, accountUpdateBean.getAuto_account().size());
                                    SwipeLayoutManager.create().closeLayout();
                                }
                            }
                            accountUpdateBean.getAuto_account().removeAll(listdate);
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
                DialogUtil.createConfirmDialog(getActivity(), "提示", "你确定要应用吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int a = 0;
                        for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {
                            if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "")) {
                                a++;
                            }

                        }
                        if (a == 0) {
                            DialogUtil.createMessageDialog(getActivity(), "提示", "没有选择应用的账号！", "我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            }, -1).show();
                        }
                        List<Addresstimebean> listdate = new ArrayList<Addresstimebean>();
                        int len = 0;
                        Log.i("123", mAdapter.getSelectnumber() + "" + accountMap.size());
                        //防止一个一个加到3个
                        if (mAdapter.getSelectnumber() > 3) {
                            ToastUtils.show(context, "为了自动换号正常运行一共只能应用3个账户", Toast.LENGTH_SHORT);
                            return;
                        }
                        for (int i = 0; i < accountMap.size(); i++) {
                            if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "") && accountUpdateBean.getAuto_account().get(i).getDevices() != null && !accountUpdateBean.getAuto_account().get(i).getDevices().equals("null") && !accountUpdateBean.getAuto_account().get(i).getDevices().equals("")) {
                                ToastUtils.show(context, "已经应用不要重新应用", Toast.LENGTH_SHORT);
                                return;
                            }
                        }
                        //防止刚开始就应用超过3个
                        for (int i = 0; i < accountMap.size(); i++) {
                            if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "") == true) {
                                len = len + 1;
                            }
                        }
                        Log.i("123", mAdapter.getSelectnumber() + ":" + len);
                        if (len > 3) {
                            ToastUtils.show(context, "为了自动换号正常运行一共只能应用3个账户", Toast.LENGTH_SHORT);
                            return;
                        }
                        if (mAdapter.getSelectnumber() + len > 3) {
                            ToastUtils.show(context, "为了自动换号正常运行一共只能应用3个账户", Toast.LENGTH_SHORT);
                            return;
                        }
                        Log.i("123", mAdapter.getSelectnumber() + ":" + len);
                        for (int i = 0; i < accountMap.size(); i++) {
                            if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "") == true) {
                                mAdapter.setSelectnumber(mAdapter.getSelectnumber() + 1);
                                UpdateThread(accountUpdateBean.getAuto_account().get(i).getAccountId() + "", deviceSN, i);

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
            case R.id.addManager_btn:
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.ck_total:
                if (istotal) {
                    for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {
                        Log.i("123", accountUpdateBean.getAuto_account().get(i) + "D");
                        accountMap.put(accountUpdateBean.getAuto_account().get(i).getAccountId() + "", false);

                    }
                    ck_total.setBackground(getResources().getDrawable(R.drawable.device_mid));
                    ck_total.setTextColor(getResources().getColor(R.color.text_blue));
                    istotal = false;
                } else {
                    for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {
                        Log.i("123", accountUpdateBean.getAuto_account().get(i) + "D");
                        accountMap.put(accountUpdateBean.getAuto_account().get(i).getAccountId() + "", true);

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
                        for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {
                            if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "")) {
                                k++;
                            }

                        }
                        if (k == 0) {
                            DialogUtil.createMessageDialog(getActivity(), "提示", "没有选择取消应用的账号！", "我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            }, -1).show();
                        }
                        for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {

                            if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "")
                                    && (accountUpdateBean.getAuto_account().get(i).getDevices() == null)) {
                                DialogUtil.createMessageDialog(getActivity(), "提示", "没有应用的账户不能取消！", "我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                }, -1).show();
                                return;
                            }
                        }
                        for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {
                            if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "") && accountUpdateBean.getAuto_account().get(i).getDevices() != null && (accountUpdateBean.getAuto_account().get(i).getDevices().equals("null") || accountUpdateBean.getAuto_account().get(i).getDevices().equals(""))) {
                                DialogUtil.createMessageDialog(getActivity(), "提示", "没有应用的账户不能取消！", "我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                }, -1).show();
                                return;
                            }
                        }
                        for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {

                            if (accountMap.get(accountUpdateBean.getAuto_account().get(i).getAccountId() + "") && accountUpdateBean.getAuto_account().get(i).getDevices() != null && !accountUpdateBean.getAuto_account().get(i).getDevices().equals("null") && !accountUpdateBean.getAuto_account().get(i).getDevices().equals("")) {
                                UpdateThread(accountUpdateBean.getAuto_account().get(i).getAccountId() + "", "", i);

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
    public void onResume() {
        super.onResume();
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SUCCESS:
                        if (myHandler != null) {
                            if (accountUpdateBean.getAuto_account().size() == 0) {
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
                                        for (int i = 0; i < accountUpdateBean.getAuto_account().size(); i++) {
                                            accountMap.put(accountUpdateBean.getAuto_account().get(i).getAccountId() + "", false);
                                        }
                                        Log.i("zw", new Gson().toJson(accountUpdateBean.getAuto_account()));
                                        mAdapter.refresh(accountUpdateBean.getAuto_account());
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
                                if (accountUpdateBean.getAuto_account().size() == 0) {
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
                                if (accountUpdateBean.getAuto_account().size() == 0) {
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
