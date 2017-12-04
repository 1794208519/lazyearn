package com.base;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ui.view.swipebacklayout.SwipeBackActivity;
import com.ui.view.swipebacklayout.SwipeBackLayout;

/**
 *
 */

public abstract class BaseActivity extends SwipeBackActivity {
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        initSwipeBackLayout();
        setContentView(getLayoutId());
        initTransitionAnim();
        initParamsAndPresenter();
        findView();
        initView();
        initListener();
        initData();
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void findView();

    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();

    protected void initParamsAndPresenter() {
    }


    public void initTransitionAnim() {

    }

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onResume();
    }

    @SuppressWarnings("deprecation")
    public void initSwipeBackLayout() {
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    public void setEdgeTrackingEnabled(int size, int position) {
        if (size == 0) {
        }
        // 只有一个fragment  - 左右滑关闭
        else if (size == 1 && position == 0) {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
        }
        // 多个fragment  - 位于左侧尽头 - 只可左滑关闭
        else if (size != 1 && position == 0) {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
        // 多个fragment  - 位于右侧尽头 - 只可右滑关闭
        else if (size != 1 && position == size - 1) {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_RIGHT);
        }
        // 多个fragment  - 位于中间 - 屏蔽所有左右滑关闭事件
        else {
            mSwipeBackLayout.setEdgeTrackingEnabled(0);
        }
    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        Log.i("123",newConfig.orientation+"1");
//        // 当新设置中，屏幕布局模式为横排时
//        if(newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//
//            //TODO 某些操作
//        }else{
//            //TODO 某些操作
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
//        super.onConfigurationChanged(newConfig);
//    }
}
