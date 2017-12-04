package com.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.entity.AddressBean;
import com.entity.Addresstimebean;
import com.google.gson.Gson;
import com.ui.Adapter.DeviceAddressAdapter;
import com.ui.activity.MapupdateActivity;
import com.ui.view.swipedeletelayout.SwipeLayoutManager;
import com.uidemo.R;
import com.utils.DialogUtil;
import com.utils.MyJsonUtil;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.ui.activity.DeviceUpdateActivity.deviceSN;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * create an instance of this fragment.
 */
public class DevicerightFragment extends BaseDeviceFgmnt implements View.OnClickListener {
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
    private String url = Urlutils.getSelectAdd();
    private String deleteUrl = Urlutils.getDeleteAdd();
    private String updateUrl = Urlutils.getUpdateAddress();
    //存放账户对象的集合
    private List<Addresstimebean> mAddressBean;
    private DeviceAddressAdapter mAdapter;
    private HashMap<String, Boolean> addressMap = new HashMap<String, Boolean>();
    //删除，应用
    private Button btn_delete, btn_save, btn_cancelsave;
    //全选
    private Button ck_total;
    private boolean istotal;

    public Handler myHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_deviceright;
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
    }

    /**
     * 获取所有的添加的总的地点
     */
    //    deviceSN.trim()
    private void loadThread() {
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
                                    myHandler.sendEmptyMessage(FAILS);
                                }
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (myHandler != null) {
                                    try {
                                        Log.i("123", response);
                                        AddressBean addressBean = MyJsonUtil.getBeanByJson(response, AddressBean.class);
                                        mAddressBean = new ArrayList<Addresstimebean>();
                                        for (int i = 0; i < addressBean.getAuto_data().size(); i++) {
                                            Addresstimebean addresstimebean = new Addresstimebean();
                                            addresstimebean.setAddress(addressBean.getAuto_data().get(i).getAddress());
                                            addresstimebean.setDataId(addressBean.getAuto_data().get(i).getDataId() + "");
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
    public void deleteThread(final String dataId) {
        Log.i("zw", dataId);
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(deleteUrl)
                        .addParams("dataId", dataId)
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
        final Map<String, String> params = new HashMap<>();
        Log.i("zw", id + devices.trim());
        params.put("dataId", id);
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
                                    mAddressBean.get(i).setDevices(devices);
                                    mAdapter.notifyDataSetChanged();
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
        AddressBean addressBean = gson.fromJson(json, AddressBean.class);
        return addressBean;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                int k = 0;
                for (int i = 0; i < mAddressBean.size(); i++) {
                    if (addressMap.get(mAddressBean.get(i).getDataId() + "")) {
                        k++;
                    }
                }
                if (k > 0) {
                    DialogUtil.createConfirmDialog(getActivity(), "提示", "你确定要删除吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<Addresstimebean> listdate = new ArrayList<Addresstimebean>();

                            for (int i = 0; i < mAddressBean.size(); i++) {
                                if (addressMap.get(mAddressBean.get(i).getDataId())) {
                                    deleteThread(mAddressBean.get(i).getDataId());
                                    listdate.add(mAddressBean.get(i));
                                    addressMap.remove(mAddressBean.get(i).getDataId());
                                    //这俩句至关重要，实现了删除条目时的过度效果
                                    mAdapter.notifyItemRemoved(i);
                                    mAdapter.notifyItemRangeChanged(i, mAddressBean.size());
                                    SwipeLayoutManager.create().closeLayout();
                                }
                            }
                            mAddressBean.removeAll(listdate);
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
                        for (int i = 0; i < mAddressBean.size(); i++) {
                            if (addressMap.get(mAddressBean.get(i).getDataId() + "")) {
                                a++;
                            }
                        }
                        if (a == 0) {
                            DialogUtil.createMessageDialog(getActivity(), "提示", "没有选择应用的地点！", "我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            }, -1).show();
                        }
                        int k = 0;
                        for (int i = 0; i < mAddressBean.size(); i++) {
                            if (addressMap.get(mAddressBean.get(i).getDataId())) {
                                Log.i("123", i + "B");
                                if (mAddressBean.get(i).getDevices() != null && !mAddressBean.get(i).getDevices().equals("null") && !mAddressBean.get(i).getDevices().equals("")) {
                                    k++;
                                }
                            }
                        }

                        if (k != 0) {
                            DialogUtil.createMessageDialog(getActivity(), "提示", "要应用的地点存在已应用的！", "我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }, -1).show();
                        } else {
                            for (int i = 0; i < mAddressBean.size(); i++) {
                                if (addressMap.get(mAddressBean.get(i).getDataId())) {
                                    UpdateThread(mAddressBean.get(i).getDataId(), deviceSN, i);
                                }
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
                startActivityForResult(new Intent(getActivity(), MapupdateActivity.class), 2);
                break;
            case R.id.ck_total:
                if (istotal) {
                    for (int i = 0; i < mAddressBean.size(); i++) {

                        addressMap.put(mAddressBean.get(i).getDataId(), false);

                    }
                    ck_total.setBackground(getResources().getDrawable(R.drawable.device_mid));
                    ck_total.setTextColor(getResources().getColor(R.color.text_blue));
                    istotal = false;
                } else {
                    for (int i = 0; i < mAddressBean.size(); i++) {

                        addressMap.put(mAddressBean.get(i).getDataId(), true);

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
                        for (int i = 0; i < mAddressBean.size(); i++) {
                            if (addressMap.get(mAddressBean.get(i).getDataId() + "")) {
                                k++;
                            }

                        }
                        if (k == 0) {
                            DialogUtil.createMessageDialog(getActivity(), "提示", "没有选择取消应用的地点！", "我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            }, -1).show();
                        }
                        for (int i = 0; i < mAddressBean.size(); i++) {

                            if (addressMap.get(mAddressBean.get(i).getDataId() + "")
                                    && (mAddressBean.get(i).getDevices() == null)) {
                                DialogUtil.createMessageDialog(getActivity(), "提示", "没有应用的地点不能取消！", "我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                }, -1).show();
                                return;
                            }

                        }
                        for (int i = 0; i < mAddressBean.size(); i++) {
                            if (addressMap.get(mAddressBean.get(i).getDataId() + "") && mAddressBean.get(i).getDevices() != null && (mAddressBean.get(i).getDevices().equals("null") || mAddressBean.get(i).getDevices().equals(""))) {
                                DialogUtil.createMessageDialog(getActivity(), "提示", "没有应用的地点不能取消！", "我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                }, -1).show();
                                return;
                            }

                        }
                        for (int i = 0; i < mAddressBean.size(); i++) {

                            if (addressMap.get(mAddressBean.get(i).getDataId() + "") && mAddressBean.get(i).getDevices() != null && !mAddressBean.get(i).getDevices().equals("null") && !mAddressBean.get(i).getDevices().equals("")) {
                                UpdateThread(mAddressBean.get(i).getDataId() + "", "", i);

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
                            if (mAddressBean.size() == 0) {
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
                                        for (int i = 0; i < mAddressBean.size(); i++) {
                                            addressMap.put(mAddressBean.get(i).getDataId(), false);
                                        }
                                        mAdapter = new DeviceAddressAdapter(mAddressBean, getActivity(), addressMap, new AccountCallbak() {
                                            @Override
                                            public void positiveCallbak(int position) {
                                                addressMap.remove(mAddressBean.get(position).getDataId());
                                                Log.i("123", position + "A");
                                                deleteThread(mAddressBean.get(position).getDataId() + "");
                                            }
                                        });
                                        mRecyclerView.setAdapter(mAdapter);
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
                                        add_load.setVisibility(View.VISIBLE);
                                        add_error.setVisibility(View.INVISIBLE);
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
                                if (mAddressBean.size() == 0) {
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
                                if (mAddressBean.size() == 0) {
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
        if (resultCode == 2 || resultCode == -1) {
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
