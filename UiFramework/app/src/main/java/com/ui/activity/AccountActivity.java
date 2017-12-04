package com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.base.BaseActivity;
import com.entity.JsonBean;
import com.google.gson.Gson;
import com.uidemo.R;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by vicmob_yf002 on 2017/5/5.
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {
    private EditText account_edit;
    private EditText password_edit;
    private Button addAccount;
    private ImageView back_btn;
    private String name;
    private String password;
    private String insertUrl = Urlutils.getInsertAccountUrl();
    //显示回调数据通知
    private static final int SUCCESS = 1;
    private static final int NONE = 2;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS:
                    JsonBean jsonBean=new Gson().fromJson(msg.obj.toString(),JsonBean.class);
                    Log.i("123",jsonBean.toString());
                    Log.i("123",jsonBean.isSuccess()+"");
                    if ( jsonBean.isSuccess()) {
                        ToastUtils.showShort(AccountActivity.this, "添加成功");
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", "My name is linjiqin");
                        //设置返回数据
                        AccountActivity.this.setResult(1, intent);
                        //关闭Activity
                        AccountActivity.this.finish();
                    } else {
                        ToastUtils.showShort(AccountActivity.this, "添加失败");
                    }
                    break;
                case NONE:
                    ToastUtils.showShort(AccountActivity.this, "添加失败");
                    break;
            }
        }
    };
    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    public void findView() {

    }

    @Override
    public void initView() {
        //初始化控件
        account_edit = (EditText) findViewById(R.id.account_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        addAccount = (Button) findViewById(R.id.add_account);
        back_btn = (ImageView) findViewById(R.id.account_back);
    }

    @Override
    public void initListener() {
        addAccount.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_account:
                name = account_edit.getText().toString().trim();
                password = password_edit.getText().toString().trim();
                if (name == null || password == null || TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                    Snackbar.make(v, "请输入完整的账号与密码", Snackbar.LENGTH_SHORT).setAction("SS", null).show();
                    return;
                }
//                new AccountDao(AccountActivity.this).add(accountBean);
                insertThread(name,password);
                account_edit.setText("");
                password_edit.setText("");
//                Snackbar.make(v, "添加成功", Snackbar.LENGTH_SHORT).setAction("SS", null).show();
                hideKeyboard();
                break;
            case R.id.account_back:
                finish();
                break;
        }
    }
    /**
     * 添加数据
     *
     * @param
     */
    public void insertThread(final String account,final String password) {
        final Map<String,String> params=new HashMap<>();
        params.put("account",account);
        params.put("password",password);
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(insertUrl)
                        .params(params)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(NONE);
                            }

                            @Override
                            public void onResponse(String response, int id) {

                                Message msg = myHandler.obtainMessage();
                                msg.what = SUCCESS;
                                msg.obj = response;
                                myHandler.sendMessage(msg);
                            }
                        });
            }
        };
        th.start();
    }
    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //数据是使用Intent返回
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.putExtra("result", "");
            //设置返回数据
            AccountActivity.this.setResult(1, intent);
            //关闭Activity
            AccountActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
