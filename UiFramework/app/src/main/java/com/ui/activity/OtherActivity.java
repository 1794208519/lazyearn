package com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.base.BaseActivity;
import com.uidemo.R;

/**
 * Created by vicmob_yf002 on 2017/4/21.
 */
public class OtherActivity extends BaseActivity {

    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_other;
    }

    @Override
    public void findView() {

    }

    @Override
    public void initView() {

//        setStatusBar();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
