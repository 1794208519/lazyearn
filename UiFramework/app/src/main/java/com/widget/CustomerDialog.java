package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.Interface.View.Callback;
import com.uidemo.R;

/**
 * Created by Teprinciple on 2016/10/13.
 * 自定义常规对话框
 */
public class CustomerDialog extends Dialog implements View.OnClickListener {

    Callback callback;
    private TextView content;
    private TextView sureBtn;
    private TextView cancleBtn;

    public CustomerDialog(Context context, Callback callback) {
        super(context, R.style.CustomDialog);
        this.callback = callback;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_customer, null);
        sureBtn = (TextView) mView.findViewById(R.id.dialog_customer_sure);
        cancleBtn = (TextView) mView.findViewById(R.id.dialog_customer_cancle);
        content = (TextView) mView.findViewById(R.id.dialog_customer_title);
        sureBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
        super.setContentView(mView);
    }


    public CustomerDialog setContent(String s) {
        content.setText(s);
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_customer_cancle:
                callback.negativeCallback();
                this.cancel();
                break;

            case R.id.dialog_customer_sure:
                callback.positiveCallback();
                this.cancel();
                break;
        }
    }

}
