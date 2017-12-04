package com.ui.activity;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Interface.View.Callback;
import com.MyApp.MyApplication;
import com.base.BaseActivity;
import com.entity.DefaultDataBean;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;
import com.uidemo.R;
import com.utils.MyJsonUtil;
import com.utils.PrefUtils;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.CustomerDialog;
import com.widget.LoadDialog;
import com.widget.LoadingDialog;
import com.widget.OneContentDialog;
import com.widget.OneLineDialog;
import com.widget.ThreeLineDialog;
import com.widget.TwoLineDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by sunjing on 2017/8/10.
 */

public class DefaultDataActivity extends BaseActivity {
    @BindView(R.id.wxNearby_num)
    TextView wxNearbyNum;
    @BindView(R.id.wxAddFriend_num)
    TextView wxAddFriendNum;
    @BindView(R.id.wxAddGroupFriend_num)
    TextView wxAddGroupFriendNum;
    @BindView(R.id.wxPublic_num)
    TextView wxPublicNum;
    @BindView(R.id.wxDriftBottle_num)
    TextView wxDriftBottleNum;
    @BindView(R.id.wxCircle_num)
    TextView wxCircleNum;
    @BindView(R.id.wxNearby_content)
    TextView wxNearbyContent;
    @BindView(R.id.wxPublic_index)
    TextView wxPublicIndex;
    @BindView(R.id.wxPublic_friend_num)
    TextView wxPublicFriendNum;
    @BindView(R.id.wxDriftBottle_Content)
    TextView wxDriftBottleContent;
    @BindView(R.id.wxCircle_content)
    TextView wxCircleContent;
    @BindView(R.id.rl_default_WeChat_fujinren)
    RelativeLayout rlDefaultWeChatFujinren;
    @BindView(R.id.rl_default_WeChat_jiahaoyou)
    RelativeLayout rlDefaultWeChatJiahaoyou;
    @BindView(R.id.rl_default_WeChat_jiaqunyou)
    RelativeLayout rlDefaultWeChatJiaqunyou;
    @BindView(R.id.rl_default_WeChat_gongzhonghao)
    RelativeLayout rlDefaultWeChatGongzhonghao;
    @BindView(R.id.rl_default_weChat_piaoliuping)
    RelativeLayout rlDefaultWeChatPiaoliuping;
    @BindView(R.id.rl_default_weChat_pengyouquan)
    RelativeLayout rlDefaultWeChatPengyouquan;
    TwoLineDialog mTwoLineDialog;
    OneLineDialog mOneLineDialog;
    ThreeLineDialog mThreeLineDialog;
    OneContentDialog mOneContentDialog;
    @BindView(R.id.rl_default_weChat_yjfaxiaoxi)
    RelativeLayout rlDefaultWeChatYjfaxiaoxi;
    @BindView(R.id.wxAKey_content)
    TextView wxAKeyContent;
    private String[] function = {"wxNearbyNum", "wxNearbyContent", "wxAddFriendNum", "wxAddGroupFriendNum", "wxPublicIndex", "wxPublicNum", "wxPublicFriendNum",
            "wxDriftBottleNum", "wxDriftBottleContent", "wxCircleNum", "wxCircleContent", "wxAKeySendMessage"};
    //提交成功信息
    private static final int SUCCESS = 1;
    //提交失败信息
    private static final int FAIL = 2;

    private static final int EXMSG = 3;

    public static final int LOAD_SUCCESS = 13;
    public static final int LOAD_FAIL = 14;
    public static final int LOADING_MSG = 12;
    private LoadingDialog mLoadingDialog;

    private MyApplication mapp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_default_data;
    }

    @Override
    public void findView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        mapp = (MyApplication) getApplication();
        getDefaultData();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    public void initLoadData() {

        String mWxNearby_num = PrefUtils.getString(this, function[0], "");
        String mWxNearby_content = PrefUtils.getString(this, function[1], "");
        String mWxAddFriend_num = PrefUtils.getString(this, function[2], "");
        String mWxAddGroupFriend_num = PrefUtils.getString(this, function[3], "");
        String mWxPublic_index = PrefUtils.getString(this, function[4], "");
        String mWxPublic_num = PrefUtils.getString(this, function[5], "");
        String mWxPublicFriend_num = PrefUtils.getString(this, function[6], "");
        String mWxDriftBottle_num = PrefUtils.getString(this, function[7], "");
        String mWxDriftBottle_content = PrefUtils.getString(this, function[8], "");
        String mWxCircle_num = PrefUtils.getString(this, function[9], "");
        String mWxCircle_content = PrefUtils.getString(this, function[10], "");
        String mWxAKey_message = PrefUtils.getString(this, function[11], "");

        wxNearbyNum.setText(mWxNearby_num);
        wxNearbyContent.setText(mWxNearby_content);
        wxAddFriendNum.setText(mWxAddFriend_num);
        wxAddGroupFriendNum.setText(mWxAddGroupFriend_num);
        wxPublicIndex.setText(mWxPublic_index);
        wxPublicNum.setText(mWxPublic_num);
        wxPublicFriendNum.setText(mWxPublicFriend_num);
        wxDriftBottleNum.setText(mWxDriftBottle_num);
        wxDriftBottleContent.setText(mWxDriftBottle_content);
        wxCircleNum.setText(mWxCircle_num);
        wxCircleContent.setText(mWxCircle_content);
        wxAKeyContent.setText(mWxAKey_message);
    }

    //点击弹框
    public void setOneDialog(final int position, final String function) {
        mOneLineDialog = new OneLineDialog(DefaultDataActivity.this, position, function, new Callback() {
            @Override
            public void positiveCallback() {
                String num = PrefUtils.getString(DefaultDataActivity.this, function, "0");
                switch (position) {
                    case 1:
                        wxAddFriendNum.setText(num);
                        break;
                    case 2:
                        wxAddGroupFriendNum.setText(num);
                        break;
                }
                mOneLineDialog.dismiss();
            }

            @Override
            public void negativeCallback() {
                mOneLineDialog.dismiss();

            }
        });
        mOneLineDialog.setCancelable(true);
        mOneLineDialog.show();
    }

    //点击弹框
    public void setThreeDialog(final int position, final String function, final String function1, final String function2) {
        mThreeLineDialog = new ThreeLineDialog(DefaultDataActivity.this, position, function, function1, function2, new Callback() {
            @Override
            public void positiveCallback() {
                String num1 = PrefUtils.getString(DefaultDataActivity.this, function, "0");
                String num2 = PrefUtils.getString(DefaultDataActivity.this, function1, "0");
                String num3 = PrefUtils.getString(DefaultDataActivity.this, function2, "0");
                switch (position) {
                    case 3:
                        wxPublicIndex.setText(num1);
                        wxPublicNum.setText(num2);
                        wxPublicFriendNum.setText(num3);
                        break;
                    default:
                        break;
                }
                mThreeLineDialog.dismiss();
            }

            @Override
            public void negativeCallback() {
                mThreeLineDialog.dismiss();

            }
        });
        mThreeLineDialog.setCancelable(true);
        mThreeLineDialog.show();
    }

    //点击弹框
    public void setDataDialog(final int position, final String function, final String function1) {
        mTwoLineDialog = new TwoLineDialog(DefaultDataActivity.this, position, function, function1, new Callback() {
            @Override
            public void positiveCallback() {
                String num = PrefUtils.getString(DefaultDataActivity.this, function, "0");
                String content = PrefUtils.getString(DefaultDataActivity.this, function1, "0");
                switch (position) {
                    case 0:
                        wxNearbyNum.setText(num);
                        wxNearbyContent.setText(content);
                        break;
                    case 4:
                        wxDriftBottleNum.setText(num);
                        wxDriftBottleContent.setText(content);
                        break;
                    case 5:
                        wxCircleNum.setText(num);
                        wxCircleContent.setText(content);
                        break;
                }
                mTwoLineDialog.dismiss();
            }

            @Override
            public void negativeCallback() {
                mTwoLineDialog.dismiss();

            }
        });

        mTwoLineDialog.setCancelable(true);
        mTwoLineDialog.show();
    }

    //点击弹框
    public void setOneContentDialog(final int position, final String function) {
        mOneContentDialog = new OneContentDialog(DefaultDataActivity.this, position, function, new Callback() {
            @Override
            public void positiveCallback() {
                String content = PrefUtils.getString(DefaultDataActivity.this, function, "0");
                switch (position) {
                    case 6:
                        wxAKeyContent.setText(content);
                        break;
                }
                mOneContentDialog.dismiss();
            }

            @Override
            public void negativeCallback() {
                mOneContentDialog.dismiss();

            }
        });
        mOneContentDialog.setCancelable(true);
        mOneContentDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLoadData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.addManager_back, R.id.rl_default_WeChat_fujinren, R.id.rl_default_WeChat_jiahaoyou, R.id.rl_default_WeChat_jiaqunyou, R.id.rl_default_WeChat_gongzhonghao, R.id.rl_default_weChat_piaoliuping, R.id.rl_default_weChat_pengyouquan, R.id.rl_default_weChat_yjfaxiaoxi, R.id.wx_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addManager_back:
                finish();
                break;
            case R.id.rl_default_WeChat_fujinren:
                setDataDialog(0, function[0], function[1]);
                mTwoLineDialog.setWheelPickerTwo(PrefUtils.getString(DefaultDataActivity.this, function[0], ""));
                mTwoLineDialog.setMessage("微信打招呼人数", "微信打招呼内容");
                break;
            case R.id.rl_default_WeChat_jiahaoyou:
                setOneDialog(1, function[2]);
                mOneLineDialog.setWheelPicker(PrefUtils.getString(DefaultDataActivity.this, function[2], ""));
                mOneLineDialog.setMessage("微信加好友人数");
                break;
            case R.id.rl_default_WeChat_jiaqunyou:
                setOneDialog(2, function[3]);
                mOneLineDialog.setWheelPicker(PrefUtils.getString(DefaultDataActivity.this, function[3], ""));
                mOneLineDialog.setMessage("微信起始群");
                break;
            case R.id.rl_default_WeChat_gongzhonghao:
                setThreeDialog(3, function[4], function[5], function[6]);
                String a4 = PrefUtils.getString(DefaultDataActivity.this, function[4], "");
                String a5 = PrefUtils.getString(DefaultDataActivity.this, function[5], "");
                String a6 = PrefUtils.getString(DefaultDataActivity.this, function[6], "");
                mThreeLineDialog.setWheelPickerThree(a4, a5, a6);
                mThreeLineDialog.setMessage("从第几个公众号添加", "推送公众号个数", "推送好友数");
                break;
            case R.id.rl_default_weChat_piaoliuping:
                setDataDialog(4, function[7], function[8]);
                mTwoLineDialog.setWheelPickerTwo(PrefUtils.getString(DefaultDataActivity.this, function[7], ""));
                mTwoLineDialog.setMessage("扔瓶子个数", "漂流瓶内容");
                break;
            case R.id.rl_default_weChat_pengyouquan:
                setDataDialog(5, function[9], function[10]);
                mTwoLineDialog.setWheelPickerTwo(PrefUtils.getString(DefaultDataActivity.this, function[9], ""));
                mTwoLineDialog.setMessage("评论条数", "评论内容");
                break;
            case R.id.rl_default_weChat_yjfaxiaoxi:
                setOneContentDialog(6, function[11]);
                mOneContentDialog.setMessage("打招呼内容");
                break;
            case R.id.wx_commit:
                String nearbyContent = PrefUtils.getString(DefaultDataActivity.this, function[1], "");
                String bottle = PrefUtils.getString(DefaultDataActivity.this, function[8], "");
                String circleContent = PrefUtils.getString(DefaultDataActivity.this, function[10], "");

                if (!TextUtils.isEmpty(bottle) && bottle.length() < 5) {
                    ToastUtils.showShort(DefaultDataActivity.this, "漂流瓶编辑的内容不能少于5个字");
                } else if (!TextUtils.isEmpty(nearbyContent) && nearbyContent.length() > 50) {
                    ToastUtils.showShort(DefaultDataActivity.this, "附近人打招呼内容不能超过50个字");
                } else if (!TextUtils.isEmpty(circleContent) && circleContent.length() > 9998) {
                    ToastUtils.showShort(DefaultDataActivity.this, "朋友圈评论内容不能超过9998个字");
                } else {
                    setDialogSubmit();
                }
                break;
        }
    }

    CustomerDialog customerDialog;

    //其他对话框
    public void setDialogSubmit() {
        customerDialog = new CustomerDialog(this, new Callback() {
            @Override
            public void positiveCallback() {
                LoadDialog.show(DefaultDataActivity.this, "加载中。。。");
                upWXLoadThread();
            }

            @Override
            public void negativeCallback() {
                //stop();
                ToastUtils.showShort(DefaultDataActivity.this, "取消");
            }
        });
        customerDialog.setContent("提示" + "\n提交自定义数据?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    //更新ui
    private Handler myHandler = new Handler() {
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    LoadDialog.dismiss(DefaultDataActivity.this);
                    Alerter.create(DefaultDataActivity.this)
                            .setTitle("提交成功")
                            .setDuration(500)
                            .setBackgroundColor(R.color.text_blue)
                            .show();
                    break;
                case FAIL:
                    LoadDialog.dismiss(DefaultDataActivity.this);
                    Alerter.create(DefaultDataActivity.this)
                            .setTitle("提交失败")
                            .setDuration(500)
                            .setBackgroundColor(R.color.text_blue)
                            .show();
                    break;
                case EXMSG:
                    LoadDialog.dismiss(DefaultDataActivity.this);
                    Alerter.create(DefaultDataActivity.this)
                            .setTitle("连接服务器异常，请检查")
                            .setDuration(600)
                            .setBackgroundColor(R.color.text_blue)
                            .show();
                    break;
                case LOADING_MSG:
                    //加载进度显示
                    //加载进度
                    mLoadingDialog = new LoadingDialog(DefaultDataActivity.this);
                    mLoadingDialog.setContent("正在玩命加载...");
                    mLoadingDialog.setCancelable(false);
                    mLoadingDialog.show();
                    break;
                case LOAD_SUCCESS:
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    ToastUtils.showShort(DefaultDataActivity.this, "获取成功");
                    break;
                case LOAD_FAIL:
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    ToastUtils.showShort(DefaultDataActivity.this, "网络获取失败");
                    break;
            }
        }
    };

    /**
     * 点击添加即上传数据
     */
    private void upWXLoadThread() {
        Thread th = new Thread() {
            @Override
            public void run() {

//                String data = "wx_nearby_num=" + PrefUtils.getString(DefaultDataActivity.this, function[0], "0")
//                        + "&wx_friend_num=" + PrefUtils.getString(DefaultDataActivity.this, function[2], "0")
//                        + "&wx_nearby_content=" + getEmojiStringByUnicode(PrefUtils.getString(DefaultDataActivity.this, function[1], ""))
//                        + "&wx_groupfriend_num=" + PrefUtils.getString(DefaultDataActivity.this, function[3], "0")
//                        + "&wx_public_num=" + PrefUtils.getString(DefaultDataActivity.this, function[5], "0")
//                        + "&wx_driftbottle_num=" + PrefUtils.getString(DefaultDataActivity.this, function[7], "0")
//                        + "&wx_circle_num=" + PrefUtils.getString(DefaultDataActivity.this, function[9], "0");
//                Log.i("123", data);

                String url1 = Urlutils.getInsertDefaultDataUrl();
                final Map<String, String> params = new HashMap<>();
                params.put("wx_nearby_num", PrefUtils.getString(DefaultDataActivity.this, function[0], "0"));
                params.put("wx_nearby_content", getEmojiStringByUnicode(PrefUtils.getString(DefaultDataActivity.this, function[1], "")));

                params.put("wx_friend_num", PrefUtils.getString(DefaultDataActivity.this, function[2], "0"));
                params.put("wx_groupfriend_num", PrefUtils.getString(DefaultDataActivity.this, function[3], "0"));

                params.put("wx_public_index", PrefUtils.getString(DefaultDataActivity.this, function[4], "0"));
                params.put("wx_public_num", PrefUtils.getString(DefaultDataActivity.this, function[5], "0"));
                params.put("wx_publicfriend_num", PrefUtils.getString(DefaultDataActivity.this, function[6], "0"));

                params.put("wx_driftbottle_num", PrefUtils.getString(DefaultDataActivity.this, function[7], "0"));
                params.put("wx_driftbottle_content", getEmojiStringByUnicode(PrefUtils.getString(DefaultDataActivity.this, function[8], "")));

                params.put("wx_circle_num", PrefUtils.getString(DefaultDataActivity.this, function[9], "0"));
                params.put("wx_circle_content", getEmojiStringByUnicode(PrefUtils.getString(DefaultDataActivity.this, function[10], "")));
                params.put("wx_akey_content", getEmojiStringByUnicode(PrefUtils.getString(DefaultDataActivity.this, function[11], "")));
                params.put("devices", mapp.getDevices());

                Log.i("zw810", new Gson().toJson(params));
                OkHttpUtils
                        .post()
                        .url(url1)
                        .params(params)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(EXMSG);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("123", response);
                                if (MyJsonUtil.getBeanByJson(response)) {
                                    myHandler.sendEmptyMessage(SUCCESS);
                                } else {
                                    myHandler.sendEmptyMessage(FAIL);
                                }
                            }
                        });
            }
        };
        th.start();
    }

    //转换带表情的字符串
    private String getEmojiStringByUnicode(String unicode) {
        String msgStr = null;
        try {
            msgStr = URLEncoder.encode(unicode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return msgStr;
    }

    /******
     * 获取自定义数据
     */
    public void getDefaultData() {
        myHandler.sendEmptyMessage(LOADING_MSG);
        String urlString = Urlutils.getGetDefaultDataUrl() + "?devices=" + mapp.getDevices();
        OkHttpUtils.get()
                .url(urlString)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myHandler.sendEmptyMessage(LOAD_FAIL);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("456", response);
                        if (response != null && !response.equals("[]") && !TextUtils.isEmpty(response)) {
                            try {
                                DefaultDataBean defaultDataStrList = new DefaultDataBean();
                                defaultDataStrList = MyJsonUtil.getBeanByJson(response, DefaultDataBean.class);
                                Log.i("456", defaultDataStrList.getAuto_default_data() + "");
                                if (defaultDataStrList.getAuto_default_data() != null && !defaultDataStrList.getAuto_default_data().equals("[]") && defaultDataStrList.getAuto_default_data().size() != 0) {
                                    PrefUtils.putString(getApplicationContext(), function[0], defaultDataStrList.getAuto_default_data().get(0).getWxNearbyNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxNearbyNum() : "");
                                    PrefUtils.putString(getApplicationContext(), function[1], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getWxNearbyContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxNearbyContent().toString() : "", "UTF-8"));
                                    PrefUtils.putString(getApplicationContext(), function[2], defaultDataStrList.getAuto_default_data().get(0).getWxFriendNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxFriendNum() : "");
                                    PrefUtils.putString(getApplicationContext(), function[3], defaultDataStrList.getAuto_default_data().get(0).getWxGroupfriendNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxGroupfriendNum() : "");
                                    PrefUtils.putString(getApplicationContext(), function[4], defaultDataStrList.getAuto_default_data().get(0).getWxPublicIndex() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxPublicIndex() : "");
                                    PrefUtils.putString(getApplicationContext(), function[5], defaultDataStrList.getAuto_default_data().get(0).getWxPublicNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxPublicNum() : "");
                                    PrefUtils.putString(getApplicationContext(), function[6], defaultDataStrList.getAuto_default_data().get(0).getWxPublicfriendNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxPublicfriendNum() : "");
                                    PrefUtils.putString(getApplicationContext(), function[7], defaultDataStrList.getAuto_default_data().get(0).getWxDriftbottleNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxDriftbottleNum() : "");
                                    PrefUtils.putString(getApplicationContext(), function[8], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getWxDriftbottleContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxDriftbottleContent().toString() : "", "UTF-8"));
                                    PrefUtils.putString(getApplicationContext(), function[9], defaultDataStrList.getAuto_default_data().get(0).getWxCircleNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxCircleNum() : "");
                                    PrefUtils.putString(getApplicationContext(), function[10], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getWxCircleContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxCircleContent().toString() : "", "UTF-8"));
                                    PrefUtils.putString(getApplicationContext(), function[11], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getWxAkeyContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxAkeyContent().toString() : "", "UTF-8"));

                                }
                                initLoadData();
                                myHandler.sendEmptyMessage(LOAD_SUCCESS);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                myHandler.sendEmptyMessage(LOAD_FAIL);
                            }

                        } else {
                            myHandler.sendEmptyMessage(LOAD_FAIL);
                            //ToastUtils.showShort(DefaultDataActivity.this, "数据为空");
                        }
                    }
                });
    }
}
