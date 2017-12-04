package com.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Interface.View.Callback;
import com.MyApp.MyApplication;
import com.base.BaseActivity;
import com.entity.DefaultDataBean;
import com.tapadoo.alerter.Alerter;
import com.uidemo.R;
import com.utils.MyJsonUtil;
import com.utils.PrefUtils;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.CustomerDialog;
import com.widget.DefDataDialog;
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
 * author: Twisted
 * created on: 2017/8/11 13:24
 * description:
 */

public class QQDefaultDataActivity extends BaseActivity {


    DefDataDialog dialog;
    @BindView(R.id.rl_title_qq)
    RelativeLayout rlTitleQq;
    @BindView(R.id.addManager_back_qq)
    ImageView addManagerBackQq;
    @BindView(R.id.addManager_title)
    TextView addManagerTitle;
    @BindView(R.id.qq_commit)
    TextView qqCommit;
    @BindView(R.id.addManager_tip)
    RelativeLayout addManagerTip;
    @BindView(R.id.rl_default_QQ_fujinren)
    RelativeLayout rlDefaultQQFujinren;
    @BindView(R.id.qqNearby_num)
    TextView qqNearbyNum;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.qqNearby_content)
    TextView qqNearbyContent;
    @BindView(R.id.layout1_1)
    LinearLayout layout11;
    @BindView(R.id.rl_default_qq_jiahaoyou)
    RelativeLayout rlDefaultQqJiahaoyou;
    @BindView(R.id.qqAddFriend_num)
    TextView qqAddFriendNum;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.rl_default_qq_jiaqunyou)
    RelativeLayout rlDefaultQqJiaqunyou;
    @BindView(R.id.qqAddGroupFriend_num)
    TextView qqAddGroupFriendNum;
    @BindView(R.id.layout3)
    LinearLayout layout3;
    @BindView(R.id.rl_default_qq_gongzhonghao)
    RelativeLayout rlDefaultQqGongzhonghao;
    @BindView(R.id.qqPublic_index)
    TextView qqPublicIndex;
    @BindView(R.id.layout4_0)
    LinearLayout layout40;
    @BindView(R.id.qqPublic_num)
    TextView qqPublicNum;
    @BindView(R.id.layout4_1)
    LinearLayout layout41;
    @BindView(R.id.qqPublic_friend_num)
    TextView qqPublicFriendNum;
    @BindView(R.id.layout4_2)
    LinearLayout layout42;
    @BindView(R.id.rl_default_qq_piaoliuping)
    RelativeLayout rlDefaultQqPiaoliuping;
    @BindView(R.id.qqDriftBottle_num)
    TextView qqDriftBottleNum;
    @BindView(R.id.layout5)
    LinearLayout layout5;
    @BindView(R.id.qqDriftBottle_content)
    TextView qqDriftBottleContent;
    @BindView(R.id.layout5_1)
    LinearLayout layout51;
    @BindView(R.id.rl_default_qq_pengyouquan)
    RelativeLayout rlDefaultQqPengyouquan;
    @BindView(R.id.qqCircle_num)
    TextView qqCircleNum;
    @BindView(R.id.layout6)
    LinearLayout layout6;
    @BindView(R.id.qqCircle_content)
    TextView qqCircleContent;
    @BindView(R.id.layout6_1)
    LinearLayout layout61;
    @BindView(R.id.rl_default_qq_yjfaxiaoxi)
    RelativeLayout rlDefaultQqYjfaxiaoxi;
    @BindView(R.id.qqAKey_content)
    TextView qqAKeyContent;

    private String[] function = {"qqNearbyNum", "qqNearbyContent", "qqAddFriendNum", "qqAddGroupFriendNum", "qqPublicIndex",
            "qqPublicNum", "qqPublicFriendNum",
            "qqDriftBottleNum", "qqDriftBottleContent", "qqCircleNum", "qqCircleContent", "qqAKeySendMessage"};
    //提交成功信息
    private static final int SUCCESS = 1;
    //提交失败信息
    private static final int FAIL = 2;

    private static final int EXMSG = 3;
    private OneLineDialog mOneLineDialog;
    private ThreeLineDialog mThreeLineDialog;
    private TwoLineDialog mTwoLineDialog;
    private OneContentDialog mOneContentDialog;
    public static final int LOAD_SUCCESS = 13;
    public static final int LOAD_FAIL = 14;
    public static final int LOADING_MSG = 12;
    private LoadingDialog mLoadingDialog;

    private MyApplication mapp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_qqdefault_data;
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

        String mQqNearby_num = PrefUtils.getString(this, function[0], "");
        String mQqNearby_content = PrefUtils.getString(this, function[1], "");
        String mQqAddFriend_num = PrefUtils.getString(this, function[2], "");
        String mQqAddGroupFriend_num = PrefUtils.getString(this, function[3], "");
        String mQqAKeyContent = PrefUtils.getString(this, function[11], "");
//        String mQqPublic_num = PrefUtils.getString(this, function[5], "");
//        String mQqPublicFriend_num = PrefUtils.getString(this, function[6], "");
//        String mQqDriftBottle_num = PrefUtils.getString(this, function[7], "");
//        String mQqDriftBottle_content = PrefUtils.getString(this, function[8], "");
//        String mQqCircle_num = PrefUtils.getString(this, function[9], "");
//        String mQqCircle_content = PrefUtils.getString(this, function[10], "");

        qqNearbyNum.setText(mQqNearby_num);
        qqNearbyContent.setText(mQqNearby_content);
        qqAddFriendNum.setText(mQqAddFriend_num);
        qqAddGroupFriendNum.setText(mQqAddGroupFriend_num);
        qqAKeyContent.setText(mQqAKeyContent);
//        qqPublicIndex.setText(mQqPublic_index);
//        qqPublicNum.setText(mQqPublic_num);
//        qqPublicFriendNum.setText(mQqPublicFriend_num);
//        qqDriftBottleNum.setText(mQqDriftBottle_num);
//        qqDriftBottleContent.setText(mQqDriftBottle_content);
//        qqCircleNum.setText(mQqCircle_num);
//        qqCircleContent.setText(mQqCircle_content);
    }

    //点击弹框
    public void setOneDialog(final int position, final String function) {
        mOneLineDialog = new OneLineDialog(QQDefaultDataActivity.this, position, function, new Callback() {
            @Override
            public void positiveCallback() {
                String num = PrefUtils.getString(QQDefaultDataActivity.this, function, "0");
                switch (position) {
                    case 1:
                        qqAddFriendNum.setText(num);
                        break;
                    case 2:
                        qqAddGroupFriendNum.setText(num);
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
        mThreeLineDialog = new ThreeLineDialog(QQDefaultDataActivity.this, position, function, function1, function2, new Callback() {
            @Override
            public void positiveCallback() {
                String num1 = PrefUtils.getString(QQDefaultDataActivity.this, function, "0");
                String num2 = PrefUtils.getString(QQDefaultDataActivity.this, function1, "0");
                String num3 = PrefUtils.getString(QQDefaultDataActivity.this, function2, "0");
                switch (position) {
                    case 3:
                        qqPublicIndex.setText(num1);
                        qqPublicNum.setText(num2);
                        qqPublicFriendNum.setText(num3);
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
        mTwoLineDialog = new TwoLineDialog(QQDefaultDataActivity.this, position, function, function1, new Callback() {
            @Override
            public void positiveCallback() {
                String num = PrefUtils.getString(QQDefaultDataActivity.this, function, "0");
                String content = PrefUtils.getString(QQDefaultDataActivity.this, function1, "0");
                switch (position) {
                    case 0:
                        qqNearbyNum.setText(num);
                        qqNearbyContent.setText(content);
                        break;
                    case 4:
                        qqDriftBottleNum.setText(num);
                        qqDriftBottleContent.setText(content);
                        break;
                    case 5:
                        qqCircleNum.setText(num);
                        qqCircleContent.setText(content);
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
        mOneContentDialog = new OneContentDialog(QQDefaultDataActivity.this, position, function, new Callback() {
            @Override
            public void positiveCallback() {
                String content = PrefUtils.getString(QQDefaultDataActivity.this, function, "0");
                switch (position) {
                    case 3:
                        qqAKeyContent.setText(content);
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


    @OnClick({R.id.addManager_back_qq, R.id.rl_default_QQ_fujinren, R.id.rl_default_qq_jiahaoyou, R.id.rl_default_qq_jiaqunyou, R.id.rl_default_qq_yjfaxiaoxi, R.id.rl_default_qq_gongzhonghao, R.id.rl_default_qq_piaoliuping, R.id.rl_default_qq_pengyouquan, R.id.qq_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addManager_back_qq:
                finish();
                break;
            case R.id.rl_default_QQ_fujinren:
                setDataDialog(0, function[0], function[1]);
                mTwoLineDialog.setWheelPickerTwo(PrefUtils.getString(QQDefaultDataActivity.this, function[0], ""));
                mTwoLineDialog.setMessage("QQ打招呼人数", "QQ打招呼内容");
                break;
            case R.id.rl_default_qq_jiahaoyou:
                setOneDialog(1, function[2]);
                mOneLineDialog.setWheelPicker(PrefUtils.getString(QQDefaultDataActivity.this, function[2], ""));
                mOneLineDialog.setMessage("QQ加好友人数");
                break;
            case R.id.rl_default_qq_jiaqunyou:
                setOneDialog(2, function[3]);
                mOneLineDialog.setWheelPicker(PrefUtils.getString(QQDefaultDataActivity.this, function[3], ""));
                mOneLineDialog.setMessage("QQ起始群");
                break;
            case R.id.rl_default_qq_yjfaxiaoxi:
                setOneContentDialog(3, function[11]);
                mOneContentDialog.setMessage("打招呼内容");
                break;
//            case R.id.rl_default_qq_gongzhonghao:
//                setDataDialog(3, function[3]);
//                break;
//            case R.id.rl_default_qq_piaoliuping:
//                setDataDialog(4, function[4]);
//                break;
//            case R.id.rl_default_qq_pengyouquan:
//                setDataDialog(5, function[5]);
//                break;
            case R.id.qq_commit:
                setDialogSubmit();
                break;
        }
    }

    CustomerDialog customerDialog;

    //提交对话框
    public void setDialogSubmit() {
        customerDialog = new CustomerDialog(this, new Callback() {
            @Override
            public void positiveCallback() {
                //stop();
                //start(Constant.musices[5]);

                upQQLoadThread();
            }

            @Override
            public void negativeCallback() {
                //stop();
                ToastUtils.showShort(QQDefaultDataActivity.this, "取消");
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
                    LoadDialog.dismiss(QQDefaultDataActivity.this);
                    Alerter.create(QQDefaultDataActivity.this)
                            .setTitle("提交成功")
                            .setDuration(500)
                            .setBackgroundColor(R.color.text_blue)
                            .show();
                    break;
                case FAIL:
                    LoadDialog.dismiss(QQDefaultDataActivity.this);
                    Alerter.create(QQDefaultDataActivity.this)
                            .setTitle("提交失败")
                            .setDuration(500)
                            .setBackgroundColor(R.color.text_blue)
                            .show();
                    break;
                case EXMSG:
                    LoadDialog.dismiss(QQDefaultDataActivity.this);
                    Alerter.create(QQDefaultDataActivity.this)
                            .setTitle("连接服务器异常，请检查")
                            .setDuration(600)
                            .setBackgroundColor(R.color.text_blue)
                            .show();
                    break;
                case LOADING_MSG:
                    //加载进度显示
                    //加载进度
                    mLoadingDialog = new LoadingDialog(QQDefaultDataActivity.this);
                    mLoadingDialog.setContent("正在玩命加载...");
                    mLoadingDialog.setCancelable(false);
                    mLoadingDialog.show();
                    break;
                case LOAD_SUCCESS:
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }

                    ToastUtils.showShort(QQDefaultDataActivity.this, "获取成功");
                    break;
                case LOAD_FAIL:
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    ToastUtils.showShort(QQDefaultDataActivity.this, "网络获取失败");
                    break;
            }
        }
    };

    /**
     * 点击添加即上传数据
     */
    private void upQQLoadThread() {
        Thread th = new Thread() {
            @Override
            public void run() {
                String url1 = Urlutils.getInsertDefaultDataUrl();
                final Map<String, String> params = new HashMap<>();
                params.put("qq_nearby_num", PrefUtils.getString(QQDefaultDataActivity.this, function[0], "0"));
                params.put("qq_nearby_content", getEmojiStringByUnicode(PrefUtils.getString(QQDefaultDataActivity.this, function[1], "")));
                params.put("qq_friend_num", PrefUtils.getString(QQDefaultDataActivity.this, function[2], "0"));
                params.put("qq_groupfriend_num", PrefUtils.getString(QQDefaultDataActivity.this, function[3], "0"));
                params.put("qq_akey_content", getEmojiStringByUnicode(PrefUtils.getString(QQDefaultDataActivity.this, function[11], "")));
                params.put("devices", mapp.getDevices());

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
        String urlString = Urlutils.getGetDefaultDataUrl();
        OkHttpUtils.get()
                .url(urlString)
                .addParams("devices", mapp.getDevices())
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
                                if (defaultDataStrList.getAuto_default_data() != null && !defaultDataStrList.getAuto_default_data().equals("[]") && defaultDataStrList.getAuto_default_data().size() != 0) {
                                    PrefUtils.putString(getApplicationContext(), function[0], defaultDataStrList.getAuto_default_data().get(0).getQqNearbyNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqNearbyNum() : "");
                                    PrefUtils.putString(getApplicationContext(), function[1], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getQqNearbyContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqNearbyContent().toString() : "", "UTF-8"));
                                    PrefUtils.putString(getApplicationContext(), function[2], defaultDataStrList.getAuto_default_data().get(0).getQqFriendNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqFriendNum() : "");
                                    PrefUtils.putString(getApplicationContext(), function[3], defaultDataStrList.getAuto_default_data().get(0).getQqGroupfriendGum() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqGroupfriendGum().toString() : "");
                                    PrefUtils.putString(getApplicationContext(), function[11], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getQqAkeyContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqAkeyContent().toString() : "", "UTF-8"));
                                }
                                initLoadData();
                                myHandler.sendEmptyMessage(LOAD_SUCCESS);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                myHandler.sendEmptyMessage(LOAD_FAIL);
                            }

                        } else {
                            myHandler.sendEmptyMessage(LOAD_FAIL);
                            //ToastUtils.showShort(QQDefaultDataActivity.this, "数据为空");
                        }
                    }
                });
    }
}
