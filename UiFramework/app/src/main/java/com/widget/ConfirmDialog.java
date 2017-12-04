package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.Interface.View.Callback;
import com.uidemo.R;

import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/6/20.
 * 自定义提交对话框
 */
public class ConfirmDialog extends Dialog implements View.OnClickListener {
    Callback callback;
    private TextView content;
    private TextView sureBtn;

    public ConfirmDialog(Context context, Callback callback) {
        super(context, R.style.CustomDialog);
        this.callback = callback;
        setConfirmDialog();
    }

    private void setConfirmDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm, null);
        sureBtn = (TextView) mView.findViewById(R.id.dialog_confirm_sure);
        content = (TextView) mView.findViewById(R.id.dialog_confirm_title);
        sureBtn.setOnClickListener(this);
        super.setContentView(mView);
    }


    public ConfirmDialog setContent(String s) {
        content.setText(s);
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_confirm_sure:
                callback.positiveCallback();
                this.cancel();
                break;
        }
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }
}
