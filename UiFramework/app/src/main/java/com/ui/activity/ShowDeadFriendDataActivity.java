package com.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.widget.ImageView;

import com.base.BaseActivity;
import com.uidemo.R;
import com.utils.Urlutils;

import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: Twisted
 * created on: 2017/10/13 20:28
 * description:
 */

public class ShowDeadFriendDataActivity extends BaseActivity {
    String mTxtUrl = Urlutils.getTxtUrl();
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                webShowDeadfriendata.loadUrl(mTxtUrl);
            }
            super.handleMessage(msg);
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    @BindView(R.id.deadfriend_back) ImageView deadfriendBack;
    @BindView(R.id.web_show_deadfriendata) WebView webShowDeadfriendata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_showdeadfrienddata;
    }

    @Override
    public void findView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.deadfriend_back)
    public void onViewClicked() {
        finish();
    }



}
