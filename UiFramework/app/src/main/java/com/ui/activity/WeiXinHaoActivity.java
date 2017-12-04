package com.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.base.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.Adapter.MyWeixinAdapter;
import com.uidemo.R;
import com.utils.Urlutils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeiXinHaoActivity extends BaseActivity {

    private List<String> weixinhaoList = new ArrayList<String>();
    private RecyclerView rl_weixinhao;
    private MyWeixinAdapter adapter;
    private ImageView iv_fanhui_weixinhao;
    private RelativeLayout devices_noneww;
    //加载动画图片
    private ImageView imageView;
    //动画
    AnimationDrawable loadingDrawable;
    private RelativeLayout devices_load;
    private static final int EXMSG = 1;
    private static final int LOADSUCCESS = 2;
    private static final int LOADFAIL = 3;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXMSG:
                    devices_load.setVisibility(View.VISIBLE);
                    devices_noneww.setVisibility(View.GONE);
                    loadingDrawable.start();
                    break;
                case LOADSUCCESS:
                    devices_load.setVisibility(View.GONE);
                    break;
                case LOADFAIL:
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            devices_load.setVisibility(View.GONE);
                            devices_noneww.setVisibility(View.VISIBLE);
                        }
                    }, 4000);
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.content_wei_xin_liao_tain;
    }

    @Override
    public void findView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        rl_weixinhao = ((RecyclerView) findViewById(R.id.rl_weixinhao));
        rl_weixinhao.setLayoutManager(new LinearLayoutManager(this));
        iv_fanhui_weixinhao = ((ImageView) findViewById(R.id.iv_fanhui_weixinhao));
        devices_noneww = ((RelativeLayout) findViewById(R.id.devices_noneww));
        //显示加载的动画
        imageView = (ImageView) findViewById(R.id.devices_load_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        devices_load = ((RelativeLayout) findViewById(R.id.devices_load));
    }

    @Override
    public void initListener() {
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void initData() {
        getWeiXin();
    }

    private void getWeiXin() {
        mHandler.sendEmptyMessage(EXMSG);
        RequestParams params = new RequestParams(Urlutils.weixinUrl);
        Log.i("zw1014", Urlutils.weixinUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onSuccess(String result) {
                Log.i("zw1014", result);
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    weixinhaoList = gson.fromJson(result, type);
                    Log.i("weixinhaoList", weixinhaoList.size() + "");
                    if (weixinhaoList.size() == 0) {
                        devices_noneww.setVisibility(View.VISIBLE);
                    } else {
                        devices_noneww.setVisibility(View.GONE);
                        adapter = new MyWeixinAdapter(weixinhaoList, WeiXinHaoActivity.this);
                        rl_weixinhao.setAdapter(adapter);
                        adapter.setOnItemClickListener(new MyWeixinAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                Log.i("zw1013", "点击");
                                Intent intent = new Intent(WeiXinHaoActivity.this, WeiXinHaoYouActivity.class);
                                intent.putExtra("weixinhao", weixinhaoList.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                    mHandler.sendEmptyMessage(LOADSUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(LOADFAIL);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("ex", ex + "");
                mHandler.sendEmptyMessage(LOADFAIL);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("zw1014", cex + "");
            }

            @Override
            public void onFinished() {
                Log.e("zw1014","dd");
            }
        });
    }

    @OnClick({R.id.iv_fanhui_weixinhao, R.id.devices_noneww_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fanhui_weixinhao:
                finish();
                break;
            case R.id.devices_noneww_img:
                getWeiXin();
                break;
        }
    }
}
