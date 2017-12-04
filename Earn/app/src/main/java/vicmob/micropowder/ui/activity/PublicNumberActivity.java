package vicmob.micropowder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sevenheaven.iosswitch.ShSwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 公众号
 */
public class PublicNumberActivity extends BaseActivity {
    @BindView(R.id.iv_back_pn)
    ImageView mIvBackPn;
    @BindView(R.id.iv_begin_pn)
    ShSwitchView mIvBeginPn;
    @BindView(R.id.rl_open_pn)
    RelativeLayout mRlOpenPn;
    @BindView(R.id.et_content_pn)
    EditText mEtContentPn;
    @BindView(R.id.but_begin_pn)
    Button mButBeginPn;
    @BindView(R.id.et_content_pn_people)
    EditText mEtContentPnPeople;
    @BindView(R.id.et_content_pn_start)
    EditText mEtContentPnStart;
    /**
     * 全局变量
     */
    private MyApplication app;
    /**
     * 开启服务对话框
     */
    private ConfirmDialog mConfirmDialog;
    /**
     * 公众号输入框输入的内容
     */
    private String mText;
    /**
     * 人数输入框输入的内容
     */
    private String mPeopleText;
    /**
     * 添加人数
     */
    private int mPeopleNum;
    /**
     * 添加公众号的个数
     */
    private int mPublicText;
    /**
     * 从第几个微信公众号开始输入框输入的内容0
     */
    private String mPublicNumStart;
    /**
     * 从第几个微信公众号开始
     */
    private int mPublicNumStartText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_number);
        ButterKnife.bind(this);
        initView();
    }

    //初始化参数
    public void initView() {
        super.initData();
        String wx_public_index = PrefUtils.getString(PublicNumberActivity.this, Constant.wxFunction[4], "0");
        String wx_public_num = PrefUtils.getString(PublicNumberActivity.this, Constant.wxFunction[5], "0");
        String wx_publicfriend_num = PrefUtils.getString(PublicNumberActivity.this, Constant.wxFunction[6], "0");
        if (TextUtils.isEmpty(wx_public_index) || wx_public_index.equals("0")) {
            wx_public_index = "1";
        }
        if (TextUtils.isEmpty(wx_publicfriend_num) || wx_publicfriend_num.equals("0")) {
            wx_publicfriend_num = "30";
        }
        if (TextUtils.isEmpty(wx_public_num) || wx_public_num.equals("0")) {
            wx_public_num = "1";
        }
        mEtContentPnStart.setText(wx_public_index);
        mEtContentPn.setText(wx_public_num);
        mEtContentPnPeople.setText(wx_publicfriend_num);
        mEtContentPn.setSelection(mEtContentPn.getText().toString().trim().length());   //输入公众号数
        mEtContentPnPeople.setSelection(mEtContentPnPeople.getText().toString().trim().length());  //输入推送人数
        mEtContentPnStart.setSelection(mEtContentPnStart.getText().toString().trim().length());//输入从第几个公众号开始

        mEtContentPnPeople.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mText = mEtContentPn.getText().toString().trim();     //输入公众号数
                    mPeopleText = mEtContentPnPeople.getText().toString().trim();  //输入推送人数
                    mPublicNumStart = mEtContentPnStart.getText().toString().trim();//输入从第几个公众号开始

                    try {
                        mPublicText = Integer.parseInt(mText);
                        mPeopleNum = Integer.parseInt(mPeopleText);
                        mPublicNumStartText = Integer.parseInt(mPublicNumStart);//string类型转换成int
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (mPublicText <= 0 || mPeopleNum <= 0 || mPublicNumStartText <= 0) {
                        MyToast.show(PublicNumberActivity.this, "编辑框不能为零");
                    } else {
                        PrefUtils.putInt(getApplication(), "mPublicText", mPublicText); //存储输入公众号个数
                        PrefUtils.putInt(getApplication(), "mPeopleNum", mPeopleNum);  //存储输入推送人数
                        PrefUtils.putInt(getApplication(), "mPublicNumStart", mPublicNumStartText);//存储从第几个公众号开始数
                        if (isServiceOpening(PublicNumberActivity.this)) {
                            app = (MyApplication) getApplication();
                            app.setPublicNumber(true);  //开启公众号模块
                            app.setAllowPublicNum(true);
                            intentWechat(); //跳转微信主界面
                        } else {
                            //跳转服务界面对话框
                            mConfirmDialog = new ConfirmDialog(PublicNumberActivity.this, new Callback() {
                                @Override
                                public void Positive() {
                                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                                }
                                @Override
                                public void Negative() {
                                    mConfirmDialog.dismiss();   //关闭
                                }
                            });
                            mConfirmDialog.setContent("提示：" + "\n服务没有开启不能进行下一步");
                            mConfirmDialog.setCancelable(true);
                            mConfirmDialog.show();
                        }
                    }
                    return false;
                }
                return false;
            }
        });
    }

    /**
     * 布局可见时调用
     */
    @Override
    protected void onStart() {
        super.onStart();
        initOpenState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 布局交互时调用
     */
    @Override
    public void onResume() {
        super.onResume();
        SwitchChanged();

    }

    @OnClick({R.id.iv_back_pn, R.id.rl_open_pn, R.id.but_begin_pn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_pn:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                outAnimation();     //返回按钮
                break;
            case R.id.rl_open_pn:    //服务开关切换
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;
            case R.id.but_begin_pn:  //
                mText = mEtContentPn.getText().toString().trim();     //输入公众号数
                mPeopleText = mEtContentPnPeople.getText().toString().trim();  //输入推送人数
                mPublicNumStart = mEtContentPnStart.getText().toString().trim();//输入从第几个公众号开始

                try {
                    mPublicText = Integer.parseInt(mText);
                    mPeopleNum = Integer.parseInt(mPeopleText);
                    mPublicNumStartText = Integer.parseInt(mPublicNumStart);//string类型转换成int
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (mPublicText <= 0 || mPeopleNum <= 0 || mPublicNumStartText <= 0) {
                    MyToast.show(PublicNumberActivity.this, "编辑框不能为零");
                } else {
                    PrefUtils.putInt(getApplication(), "mPublicText", mPublicText); //存储输入公众号个数
                    PrefUtils.putInt(getApplication(), "mPeopleNum", mPeopleNum);  //存储输入推送人数
                    PrefUtils.putInt(getApplication(), "mPublicNumStart", mPublicNumStartText);//存储从第几个公众号开始数
                    if (isServiceOpening(PublicNumberActivity.this)) {
                        app = (MyApplication) getApplication();
                        app.setPublicNumber(true);  //开启公众号模块
                        app.setAllowPublicNum(true);
                        intentWechat(); //跳转微信主界面
                    } else {
                        //跳转服务界面对话框
                        mConfirmDialog = new ConfirmDialog(PublicNumberActivity.this, new Callback() {
                            @Override
                            public void Positive() {
                                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                            }

                            @Override
                            public void Negative() {
                                mConfirmDialog.dismiss();   //关闭
                            }
                        });
                        mConfirmDialog.setContent("提示：" + "\n服务没有开启不能进行下一步");
                        mConfirmDialog.setCancelable(true);
                        mConfirmDialog.show();
                    }
                }
                break;
        }
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        mIvBeginPn.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
            }
        });
    }

    /**
     * 初始化服务按钮开启的状态
     */
    private void initOpenState() {
        if (isServiceOpening(PublicNumberActivity.this)) {

            mIvBeginPn.setOn(true);   //打开

        } else {

            mIvBeginPn.setOn(false);  //关闭
        }
    }

}
