package com.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.entity.LiaoTianJiLuBean;
import com.entity.SelectBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.Adapter.ChatRecordListAdapter;
import com.uidemo.R;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.CustomerDialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiaoTianJiLuActivity extends BaseActivity {
    @BindView(R.id.iv_fanhui_liaotianjilu)
    ImageView ivFanhuiLiaotianjilu;
    @BindView(R.id.tv_liaotianjilu)
    TextView tvLiaotianjilu;
    @BindView(R.id.devices_load_img)
    ImageView devicesLoadImg;
    @BindView(R.id.devices_load)
    RelativeLayout devicesLoad;
    @BindView(R.id.devices_noneww_img)
    ImageView devicesNonewwImg;
    @BindView(R.id.rl_liaotianjilu)
    RecyclerView rlLiaotianjilu;
    @BindView(R.id.devices_noneww)
    RelativeLayout devicesNoneww;
    @BindView(R.id.tv_shijian)
    TextView tvShijian;
    @BindView(R.id.et_search)
    TextView etSearch;
    @BindView(R.id.bt_quanbu_liaotianjilu)
    Button btQuanbuLiaotianjilu;
    @BindView(R.id.bt_qingkong_liaotianjilu)
    Button btQingkongLiaotianjilu;

    private List<LiaoTianJiLuBean> listData = new ArrayList<>();
    private List<LiaoTianJiLuBean> liaotianjiluList = new ArrayList<LiaoTianJiLuBean>();
    private ChatRecordListAdapter adapter;
    //动画
    private AnimationDrawable loadingDrawable;
    private SelectBean selectBean;
    private int lastVisibleItem;

    private String weixinhao = "";
    private String weixinhaoyou = "";
    private static final int EXMSG = 1;
    private static final int LOADSUCCESS = 2;
    private static final int LOADFAIL = 3;
    private static final int SEARCH_RESULT = 6;
    private static final int DELETESUCCESS = 4;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXMSG:
                    devicesLoad.setVisibility(View.VISIBLE);
                    devicesNoneww.setVisibility(View.GONE);
                    loadingDrawable.start();
                    break;
                case LOADSUCCESS:
//                    mSwipeRefreshLayout.setRefreshing(false);
                    devicesLoad.setVisibility(View.GONE);
                    break;
                case LOADFAIL:
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            mSwipeRefreshLayout.setRefreshing(false);
                            devicesLoad.setVisibility(View.GONE);
                            devicesNoneww.setVisibility(View.VISIBLE);
                        }
                    }, 4000);
                    break;
                case DELETESUCCESS:
                    break;
            }
        }
    };
    private String keyword = "";
    private String date = "";
    private int start = 0;
    private int numOfpages = 15;
    private boolean isLoad = true;
    private int loadIndex = 0;
    private String shijian;
    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    @Override
    public int getLayoutId() {
        return R.layout.activity_liao_tian_ji_lu;
    }

    @Override
    public void findView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        //显示加载的动画
        loadingDrawable = (AnimationDrawable) devicesLoadImg.getBackground();
        rlLiaotianjilu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!LiaoTianJiLuActivity.this.isDestroyed()) {
                        Glide.with(LiaoTianJiLuActivity.this).resumeRequests();
                    }
                } else {
                    if (!LiaoTianJiLuActivity.this.isDestroyed()) {
                        Glide.with(LiaoTianJiLuActivity.this).pauseRequests();
                    }

                }
            }
        });


    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Intent intent1 = getIntent();
        weixinhao = intent1.getStringExtra("weixinhao");
        weixinhaoyou = intent1.getStringExtra("weixinhaoyou");
        String weixinhaoyouname = intent1.getStringExtra("weixinhaoyouname");
        tvLiaotianjilu.setText(weixinhao + "与" + weixinhaoyouname + "的聊天");
        final LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rlLiaotianjilu.setLayoutManager(manager);

        tvShijian.setText(year + "-" + month + "-" + day + "");

        adapter = new ChatRecordListAdapter(LiaoTianJiLuActivity.this, listData);
        rlLiaotianjilu.setAdapter(adapter);
        adapter.setLoad(false, 0);

        selectBean = new SelectBean(keyword, date, start, numOfpages);
        selectLiaoTianNeiRongByBean(weixinhao, weixinhaoyou, selectBean);

        rlLiaotianjilu.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //RecyclerView没有拖动而且已经到达了最后一个item，执行自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    if (loadIndex > 1) {
                        isLoad = false;
                        adapter.setLoad(true, 2);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                start += numOfpages;
                                Log.i("zw1102", start + "");
                                selectBean = new SelectBean(keyword, date, start, numOfpages);
                                selectLiaoTianNeiRongByBean(weixinhao, weixinhaoyou, selectBean);
                                adapter.notifyDataSetChanged();
                            }
                        }, 2500);
                    } else {
                        adapter.setLoad(false, 0);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }

        });
    }

    @OnClick({R.id.iv_fanhui_liaotianjilu, R.id.devices_noneww_img, R.id.et_search, R.id.tv_shijian, R.id.bt_quanbu_liaotianjilu, R.id.bt_qingkong_liaotianjilu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fanhui_liaotianjilu:
                finish();
                break;
            case R.id.devices_noneww_img:
                start = 0;
                loadIndex = 0;
                adapter.setLoad(false, 0);
                selectBean = new SelectBean(keyword, date, start, numOfpages);
                selectLiaoTianNeiRongByBean(weixinhao, weixinhaoyou, selectBean);
                break;
            case R.id.et_search:
                Intent intent = new Intent(LiaoTianJiLuActivity.this, SearchActivity.class);
                startActivityForResult(intent, SEARCH_RESULT);
                break;
            case R.id.tv_shijian:
                showDatePickDlg();
                break;
            case R.id.bt_quanbu_liaotianjilu:
                etSearch.setText("请输入想要搜索的内容");
                selectBean = new SelectBean("", "", 0, 15);
                listData.clear();
                loadIndex = 0;
                adapter.setLoad(false, 0);
                selectLiaoTianNeiRongByBean(weixinhao, weixinhaoyou, selectBean);
                break;
            case R.id.bt_qingkong_liaotianjilu:
                setDialog5();
                break;
        }
    }

    /**
     * 查找相应聊天记录
     *
     * @param weixinhao
     * @param weixinhaoyou
     * @param selectBean
     */
    private void selectLiaoTianNeiRongByBean(String weixinhao, String weixinhaoyou, SelectBean selectBean) {
        if (loadIndex == 0) {
            mHandler.sendEmptyMessage(EXMSG);
        }
        //String url = Urlutils.selectLiaoTianJiLuUrl;

        String keyword = selectBean.getKeyword();
        String date = selectBean.getDate();
        Log.i("zw1102", date + "");
        int start = selectBean.getStart();
        int numOfpages = selectBean.getNumOfpages();

        try {
            weixinhao=java.net.URLEncoder.encode(weixinhao, "UTF-8");
            weixinhaoyou=java.net.URLEncoder.encode(weixinhaoyou, "UTF-8");
            keyword=java.net.URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = Urlutils.selectLiaoTianJiLuUrl
                + "?weixinhao=" + weixinhao
                + "&weixinhaoyou=" + weixinhaoyou
                + "&keyword=" + keyword
                + "&date=" + date
                + "&start=" + start
                + "&numOfpages=" + numOfpages;
        Log.i("zw1102", url);
        RequestParams params = new RequestParams(url);
      /*  params.addBodyParameter("weixinhao", weixinhao);
        params.addBodyParameter("weixinhaoyou", weixinhaoyou);
        params.addBodyParameter("keyword", keyword);
        params.addBodyParameter("date", date + "");
        params.addBodyParameter("start", start + "");
        params.addBodyParameter("numOfpages", numOfpages + "");*/
        //Log.i("zw1102", params.toJSONString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onSuccess(String result) {
                Log.i("zw1102", result);
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<LiaoTianJiLuBean>>() {
                    }.getType();
                    liaotianjiluList = gson.fromJson(result, type);
                    if (liaotianjiluList.size() == 0) {
                        Log.i("zw1102", liaotianjiluList.size() + "123");
                        if (loadIndex > 0) {
//                            isLoad = false;
                            adapter.setLoad(false, 1);
                            devicesNoneww.setVisibility(View.GONE);
                        } else {
                            adapter.setLoad(false, 0);
                            devicesNoneww.setVisibility(View.VISIBLE);
//                            rlSerch.setVisibility(View.GONE);
                        }

                        loadIndex = 1;
                        adapter.notifyDataSetChanged();
                    } else {
                        etSearch.setVisibility(View.VISIBLE);
                        devicesNoneww.setVisibility(View.GONE);
                        Log.i("weixinhaoyouList", liaotianjiluList.size() + "");
                        listData.addAll(liaotianjiluList);
                        adapter.setLoad(true, 2);
                        loadIndex = loadIndex + 2;
                        adapter.notifyDataSetChanged();

                        String shijian = liaotianjiluList.get(0).getLiaotianshijian();
                        shijian = shijian.substring(0, 10);
                        tvShijian.setText(shijian);
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

    /**
     * 删除聊天记录
     *
     * @param weixinhao
     * @param weixinhaoyou
     */
    private void deleteAlldateByWxhy(String weixinhao, String weixinhaoyou) {
        mHandler.sendEmptyMessage(EXMSG);
        try {
            weixinhao=java.net.URLEncoder.encode(weixinhao, "UTF-8");
            weixinhaoyou=java.net.URLEncoder.encode(weixinhaoyou, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = Urlutils.deleteAlldateByWxhyUrl
                + "?weixinhao=" + weixinhao
                + "&weixinhaoyou=" + weixinhaoyou;
        Log.i("zw1104", url);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onSuccess(String result) {
                Log.i("zw1104", result);
                if (result.trim().contains("true")) {
                    Log.i("zw1104", result + "......");
                    rlLiaotianjilu.setVisibility(View.GONE);
                    adapter.setLoad(false, 0);
                    adapter.notifyDataSetChanged();
                    devicesLoad.setVisibility(View.GONE);
                    devicesNoneww.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 日期选择器
     */
    protected void showDatePickDlg() {
        if (liaotianjiluList.size() > 0) {
            shijian = liaotianjiluList.get(0).getLiaotianshijian();
            year = Integer.parseInt(shijian.substring(0, 4));
            month = Integer.parseInt(shijian.substring(5, 7));
            day = Integer.parseInt(shijian.substring(8, 10));
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(LiaoTianJiLuActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String monthOfYearStr = String.format("%02d", monthOfYear + 1);
                String dayOfMonthStr = String.format("%02d", dayOfMonth);
                date = year + "-" + monthOfYearStr + "-" + dayOfMonthStr;
                tvShijian.setText(date + "");
                listData.clear();
                start = 0;
                loadIndex = 0;
                adapter.setLoad(false, 0);
                selectBean = new SelectBean(keyword, date, start, numOfpages);
                selectLiaoTianNeiRongByBean(weixinhao, weixinhaoyou, selectBean);
            }
        }, year, month - 1, day);
        datePickerDialog.show();
    }

    public void setDialog5() {
        CustomerDialog customerDialog = new CustomerDialog(LiaoTianJiLuActivity.this, new com.Interface.View.Callback() {
            @Override
            public void positiveCallback() {
                deleteAlldateByWxhy(weixinhao, weixinhaoyou);
            }

            @Override
            public void negativeCallback() {
                ToastUtils.showShort(LiaoTianJiLuActivity.this, "不要");
            }
        });
        customerDialog.setContent("提示" + "\n主人，是否清空当前聊天记录?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SEARCH_RESULT && data != null) {
            listData.clear();
            start = 0;
            loadIndex = 0;
            adapter.setLoad(false, 0);
            keyword = data.getStringExtra("keyword");
            etSearch.setText(keyword);
            Log.i("zw1103", keyword + " LLLLLLLLLLLLLLLLLLL");
            selectBean = new SelectBean(keyword, "", start, numOfpages);
            selectLiaoTianNeiRongByBean(weixinhao, weixinhaoyou, selectBean);
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    }
}
