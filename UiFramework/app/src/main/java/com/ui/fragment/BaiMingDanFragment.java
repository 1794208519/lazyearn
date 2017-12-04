package com.ui.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.entity.BaiMingDanBean;
import com.entity.NewBaiMingBaiBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.Adapter.BaiMingDanAdapter;
import com.ui.Adapter.MyWeixinAdapter;
import com.ui.Adapter.NewBaiMingDanAdapter;
import com.ui.view.swipedeletelayout.SwipeLayoutManager;
import com.uidemo.R;
import com.utils.DialogUtil;
import com.utils.MyJsonUtil;
import com.utils.Urlutils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ui.activity.DeviceUpdateActivity.deviceSN;

/**
 * Created by qq944 on 2017/10/19.
 */

public class BaiMingDanFragment extends Fragment {

    private RecyclerView mRecyclerView;
    //加载动画图片
    private ImageView imageView;
    //动画
    AnimationDrawable loadingDrawable;
    private RelativeLayout add_load;
    private RelativeLayout add_error;
    private RelativeLayout add_none;
    private RelativeLayout main_list;
    private View view;
    private List<String> baiMingDanBeen;
    private NewBaiMingDanAdapter adapter;
    private NewBaiMingBaiBean newbaiMingDanBean;
    private Map<String, Boolean> map = new HashMap<String, Boolean>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bai_ming_dan, container, false);
        initView();
        initData();
        add_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBaiMingDanLList();
            }
        });
        add_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBaiMingDanLList();
            }
        });
        //init();
        return view;
    }

    public void init() {


        adapter.setOnItemClickListener(new NewBaiMingDanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                // Toast.makeText(getActivity(),"点击item"+position,Toast.LENGTH_SHORT).show();
                if (!adapter.getMap().get(baiMingDanBeen.get(position))) {
                    Log.i("zw1024", map.size() + "2");
                    Log.i("zw1024", baiMingDanBeen.size() + "");
                    Log.i("zw1024", new Gson().toJson(map));
                    int size = 0;
                    for (int i = 0; i < map.size(); i++) {
                        if (adapter.getMap().get(baiMingDanBeen.get(i))) {
                            size++;
                        }
                    }
                    Log.i("zw1023", size + "");
                    if (size < 1) {
                        DialogUtil.createConfirmDialog(getActivity(), "提示", "确定要应用吗?", "确定", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadBaiMingDan(baiMingDanBeen.get(position), deviceSN.trim(), position);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }, -1).show();
                    } else {
                        DialogUtil.createMessageDialog(getActivity(), "提示", "一次只能应用一张表!!", "我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }, -1).show();
                        //Toast.makeText(getActivity(),"一次只能应用一张表",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    DialogUtil.createConfirmDialog(getActivity(), "提示", "确定要取消应用吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteBaiMingDan(newbaiMingDanBean.getAuto_devices().get(0).getDevicesId() + "", position);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }, -1).show();
                }
            }


        });

    }

    public void initData() {
        main_list.setVisibility(View.INVISIBLE);
        add_load.setVisibility(View.VISIBLE);
        add_error.setVisibility(View.INVISIBLE);
        add_none.setVisibility(View.INVISIBLE);

        getBaiMingDanLList();
    }

    public void initView() {
        add_load = (RelativeLayout) view.findViewById(R.id.add_load);
        add_error = (RelativeLayout) view.findViewById(R.id.add_error_bai_ming_dan);
        add_none = (RelativeLayout) view.findViewById(R.id.add_noneww);
        main_list = (RelativeLayout) view.findViewById(R.id.main_list);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_addressList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    }

    //从云服务器获得白名单
    public void getBaiMingDanInYun(String devices) {

        Log.i("zw1023", devices + "获得");
        //String url = Urlutils.getBaiMingDanInYunUrl+"/?devices="+devices;
        String url = Urlutils.getBaiMingDanInYunUrl;
        Log.i("zw1021", url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("devices", devices);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("zw1021", result);
                newbaiMingDanBean = MyJsonUtil.getBeanByJson(result, NewBaiMingBaiBean.class);
                if (newbaiMingDanBean.getAuto_devices().size() > 0) {
                    Log.i("zw1021", newbaiMingDanBean.getAuto_devices().get(0).getFile());
                    map.put(newbaiMingDanBean.getAuto_devices().get(0).getFile(), true);
                    adapter.setMap(map);
                    adapter.notifyDataSetChanged();
                    //init();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                add_load.setVisibility(View.GONE);
                add_error.setVisibility(View.VISIBLE);
                Log.e("zw1024", "ex:" + ex + "————isOnCallback:" + isOnCallback);
                Toast.makeText(getActivity(), "网络异常，无法获得数据", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //从PC端获得表名
    public void getBaiMingDanLList() {
        //显示加载的那个动画
        imageView = (ImageView) view.findViewById(R.id.loading_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        loadingDrawable.start();
        Log.i("zw1019", Urlutils.baiMingDanUrl);
        RequestParams params = new RequestParams(Urlutils.baiMingDanUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("zw1019", result);
                add_load.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>() {
                }.getType();
                baiMingDanBeen = gson.fromJson(result, type);
                for (int i = 0; i < baiMingDanBeen.size(); i++) {
                    map.put(baiMingDanBeen.get(i), false);
                }
                Log.i("zw1024", map.size() + "0");
                if (baiMingDanBeen.size() == 0) {
                    add_none.setVisibility(View.VISIBLE);
                } else {
                    main_list.setVisibility(View.VISIBLE);
                    adapter = new NewBaiMingDanAdapter(baiMingDanBeen, getActivity(), map);
                    mRecyclerView.setAdapter(adapter);
                    Log.i("zw1024", map.size() + "1");
                    init();
                    getBaiMingDanInYun(deviceSN.trim());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                add_load.setVisibility(View.GONE);
                add_error.setVisibility(View.VISIBLE);
                Log.e("zw1024", "ex:" + ex + "isOnCallback:" + isOnCallback);
                Toast.makeText(getActivity(), "网络异常，获得数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //根据表名删除云端数据库的内容
    public void deleteBaiMingDan(String devicesId, final Integer position) {
        add_load.setVisibility(View.VISIBLE);
        String url = Urlutils.deleteBaiMingDan + "/?devicesId=" + devicesId;
        Log.i("zw1024",url);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                add_load.setVisibility(View.GONE);
                Log.i("zw1020", result);
                if (MyJsonUtil.getBeanByJson(result)) {
                    //_____测试——————————
                    getBaiMingDanLList();
                 /*   map.put(baiMingDanBeen.get(position), false);
                    adapter.setMap(map);
                    adapter.notifyDataSetChanged();*/
                    DialogUtil.createMessageDialog(getActivity(), "提示", "取消应用成功!!", "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }, -1).show();
                } else {
                    DialogUtil.createMessageDialog(getActivity(), "提示", "取消应用失败!!", "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }, -1).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("zw1024","_____isOnCallback:" + isOnCallback,ex);
                Toast.makeText(getActivity(), "网络异常，取消失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //上传到云端数据库
    public void uploadBaiMingDan(final String whiteListName, final String devices, final Integer position) {
        add_load.setVisibility(View.VISIBLE);

        Log.i("zw1023", devices + "上传");
        //deleteBaiMingDan(devices);
        String url = Urlutils.setBaiMingDan + "/?file=" + whiteListName + "&devices=" + devices;
        Log.i("zw1020", url);
        RequestParams params = new RequestParams(url);
/*        params.addBodyParameter("file",whiteListName);
        params.addBodyParameter("devices",devices);*/
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                add_load.setVisibility(View.GONE);
                Log.i("zw1020", result);
                if (MyJsonUtil.getBeanByJson(result)) {

                    DialogUtil.createMessageDialog(getActivity(), "提示", "应用成功!!", "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }, -1).show();
                    getBaiMingDanInYun(devices);
                    //adapter.setBiaoming(whiteListName);
                    //_____测试——————————
                    getBaiMingDanLList();

                 /*   map.put(baiMingDanBeen.get(position), true);
                    adapter.setMap(map);
                    adapter.notifyDataSetChanged();*/
                } else {
                    DialogUtil.createMessageDialog(getActivity(), "提示", "应用失败!!", "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }, -1).show();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("zw1024", "ex:" + ex + "____isOnCallback:" + isOnCallback);
                Toast.makeText(getActivity(), "网络异常，应用失败", Toast.LENGTH_SHORT).show();
                add_load.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
