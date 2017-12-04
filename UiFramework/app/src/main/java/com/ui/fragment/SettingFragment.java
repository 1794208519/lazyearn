package com.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseFragment;
import com.entity.ApkBean;
import com.google.gson.Gson;
import com.ui.activity.AboutVersionActivity;
import com.ui.activity.DefaultDataActivity;
import com.ui.activity.MainActivity;
import com.ui.activity.QQDefaultDataActivity;
import com.ui.activity.SetTimerActivity;
import com.uidemo.R;
import com.utils.Constant;
import com.utils.DownLoadAppUtils;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.CustomerListView;
import com.widget.HintDialog;
import com.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


/**
 * 当前类注释:第一个Fragment
 */
public class SettingFragment extends BaseFragment {
    private CustomerListView mListView;
    private CustomerListView mListView2;
    private MyListAdapter myListAdapter;
    private MyListAdapter2 myListAdapter2;
    private String[] names = Constant.setTitles;
    private String[] names2 = Constant.setSetTitles2;
    private int[] images = Constant.setImages;
    private int[] images2 = Constant.setImages2;

    private LoadingDialog loadingDialog;
    //更新配置文件路径
    private String versionUrl = Urlutils.getVersionUrl();
    //apk更新路径
    private String apkPath = Urlutils.getApkUrl();
    //当前版本号
    private int mCurrentCode;
    private static final int EXMSG = 1;
    private static final int UPDATESUCCESS = 2;
    private static final int UPDATEFAIL = 3;

    private HintDialog mHintDialog;
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case EXMSG:
                   loadingDialog = new LoadingDialog(mActivity);
                   loadingDialog.setContent("正在获取最新版本信息...");
                   loadingDialog.setIndicator(3);
                   loadingDialog.setCancelable(false);
                   loadingDialog.show();
                   break;
               case UPDATESUCCESS:
                   break;
               case UPDATEFAIL:
                   loadingDialog.dismiss();
                   mHintDialog = new HintDialog(mActivity);
                   mHintDialog.setContent("网络获取失败,请检查");
                   mHintDialog.setCancelable(true);
                   mHintDialog.show();
                   break;
           }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setting_frag_layout, container, false);
        v.setOnTouchListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // 初始化视图
        initView();
    }

    /**
     * 初始化视图
     */
    public void initView() {
        //初始化控件
        mListView = (CustomerListView) mActivity.findViewById(R.id.set_list);
        myListAdapter = new MyListAdapter(names, images, mActivity.getApplicationContext());
        myListAdapter.notifyDataSetChanged();
        mListView.setAdapter(myListAdapter);
        mListView2 = (CustomerListView) mActivity.findViewById(R.id.set_list1);
        myListAdapter2 = new MyListAdapter2(names2, images2, mActivity.getApplicationContext());
        myListAdapter2.notifyDataSetChanged();
        mListView2.setAdapter(myListAdapter2);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        myHandler.sendEmptyMessage(EXMSG);
                        getNewVision();

                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), AboutVersionActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), SetTimerActivity.class));
//                        ToastUtils.showShort(mActivity, names[position]);
                        break;
                }
            }
        });
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), DefaultDataActivity.class));
//                        ToastUtils.showShort(mActivity, names2[position]);
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), QQDefaultDataActivity.class));
//                        ToastUtils.showShort(mActivity, names2[position]);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    class MyListAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private String[] names = {};
        private int[] images = {};
        private Context context;

        public MyListAdapter(String[] names, int[] images, Context context) {
            layoutInflater = LayoutInflater.from(context);
            this.names = names;
            this.images = images;
            this.context = context;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return names[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.setlist_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image1);
                holder.textView = (TextView) convertView.findViewById(R.id.main_text);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.imageView.setImageResource(images[i]);
            holder.textView.setText(names[i]);
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }

    class MyListAdapter2 extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private String[] names = {};
        private int[] images = {};
        private Context context;

        public MyListAdapter2(String[] names, int[] images, Context context) {
            layoutInflater = LayoutInflater.from(context);
            this.names = names;
            this.images = images;
            this.context = context;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return names[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.setlist_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image1);
                holder.textView = (TextView) convertView.findViewById(R.id.main_text);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.imageView.setImageResource(images[i]);
            holder.textView.setText(names[i]);
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }

    //连接服务器,检查新版本
    private void getNewVision() {
        OkHttpUtils.get()
                .url(versionUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myHandler.sendEmptyMessage(UPDATEFAIL);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DownLoadAppUtils downLoadAppUtils = new DownLoadAppUtils(apkPath, getActivity());
                        mCurrentCode = downLoadAppUtils.setVersionCode();
                        try {
                            ApkBean apkBean = processData(response);
                            if (apkBean.getWxversion().getVersionCode() == mCurrentCode) {
                                myHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismiss();
                                        mHintDialog= new HintDialog(mActivity);
                                        mHintDialog.setContent("当前已是最新版本");
                                        mHintDialog.show();
                                    }
                                }, 1000);
                            } else if (apkBean.getWxversion().getVersionCode() > mCurrentCode) {
                                loadingDialog.dismiss();
                                downLoadAppUtils.updateApp(getActivity(), apkBean.getWxversion().getVersionName());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            myHandler.sendEmptyMessage(UPDATEFAIL);
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
}
