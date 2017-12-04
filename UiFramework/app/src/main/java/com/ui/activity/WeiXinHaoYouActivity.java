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
import android.widget.TextView;

import com.base.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.Adapter.MyWeixinHaoYouAdapter;
import com.uidemo.R;
import com.utils.Urlutils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeiXinHaoYouActivity extends BaseActivity {
    private List<String> weixinhaoyouList = new ArrayList<String>();
    private RecyclerView rl_weixinhaoyou;
    private MyWeixinHaoYouAdapter adapter;
    private ImageView iv_fanhui_weixinhaoyou;
    private TextView tv_weixinhaoyou;
    private RelativeLayout devices_noneww;
    //加载动画图片
    private ImageView imageView;
    //动画
    AnimationDrawable loadingDrawable;
    private RelativeLayout devices_load;
    private String weixinhao="";
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
        return R.layout.activity_wei_xin_hao_you;
    }

    @Override
    public void findView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        rl_weixinhaoyou = ((RecyclerView) findViewById(R.id.rl_weixinhaoyou));
        iv_fanhui_weixinhaoyou = ((ImageView) findViewById(R.id.iv_fanhui_weixinhaoyou));
        tv_weixinhaoyou = ((TextView) findViewById(R.id.tv_weixinhaoyou));
        devices_noneww = ((RelativeLayout) findViewById(R.id.devices_noneww));
        //显示加载的动画
        imageView = (ImageView) findViewById(R.id.devices_load_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        devices_load = ((RelativeLayout) findViewById(R.id.devices_load));
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        Intent intent1 = getIntent();
        weixinhao = intent1.getStringExtra("weixinhao");
        tv_weixinhaoyou.setText("与" + weixinhao + "聊天的好友");
        rl_weixinhaoyou.setLayoutManager(new LinearLayoutManager(this));
        getWeiXin(weixinhao);
    }

    private void getWeiXin(final String weixinhao) {
        String weixinhaoNotFinal = weixinhao;
        mHandler.sendEmptyMessage(EXMSG);
        try {
            weixinhaoNotFinal=java.net.URLEncoder.encode(weixinhaoNotFinal, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(Urlutils.weixinhaoyouUrl + "?weixinhao=" + weixinhaoNotFinal);
        Log.i("zw1013", Urlutils.weixinhaoyouUrl + "?weixinhao=" + weixinhao);

//        params.addBodyParameter("weixinname",weixinhao);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onSuccess(String result) {
                Log.i("result", result);
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    weixinhaoyouList = gson.fromJson(replaceBlank(result), type);
                    Log.i("weixinhaoyouList", weixinhaoyouList.size() + "");
                    if (weixinhaoyouList.size() == 0) {
                        devices_noneww.setVisibility(View.VISIBLE);
                    } else {
                        devices_noneww.setVisibility(View.GONE);
                        adapter = new MyWeixinHaoYouAdapter(weixinhaoyouList, WeiXinHaoYouActivity.this);
                        rl_weixinhaoyou.setAdapter(adapter);
                        adapter.setOnItemClickListener(new MyWeixinHaoYouAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                Intent intent = new Intent(WeiXinHaoYouActivity.this, LiaoTianJiLuActivity.class);
                                intent.putExtra("weixinhaoyou", weixinhaoyouList.get(position));
                                intent.putExtra("weixinhao", weixinhao);
                                intent.putExtra("weixinhaoyouname", weixinhaoyouList.get(position));
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
                mHandler.sendEmptyMessage(LOADFAIL);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    @OnClick({R.id.iv_fanhui_weixinhaoyou, R.id.devices_noneww_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fanhui_weixinhaoyou:
                finish();
                break;
            case R.id.devices_noneww_img:
                getWeiXin(weixinhao);
                break;
        }
    }
}
