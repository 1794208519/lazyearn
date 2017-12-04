package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.Interface.View.Callback;
import com.uidemo.R;
import com.utils.PrefUtils;

/**
 * Created by sunjing on 2017/8/10.
 */

public class DefDataDialog extends Dialog implements View.OnClickListener {

    Callback callback;
    Context context;
    private ImageView ivCancel;
    private EditText etContent;
    private Button butContent;
    private String function;

    /**
     * 仿苹果自定义Dialog
     *
     * @param context  上下文
     * @param callback 按钮回调
     */
    public DefDataDialog(Context context, String function, Callback callback) {
        super(context, R.style.ContentDialog);
        this.context = context;
        this.callback = callback;
        this.function = function;
        setContentDialog();
    }

    private void setContentDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_data, null);
        ivCancel = (ImageView) mView.findViewById(R.id.iv_cancel);
        etContent = (EditText) mView.findViewById(R.id.et_content);
        butContent = (Button) mView.findViewById(R.id.but_content);

        ivCancel.setOnClickListener(this);
        butContent.setOnClickListener(this);
        super.setContentView(mView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                callback.negativeCallback();
                //this.cancel();
                break;

            case R.id.but_content:
                //把输入内容储存到SP中
                String ContentDialogText = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(ContentDialogText)) {
                    PrefUtils.putString(context, function, "0");
                } else {
                    PrefUtils.putString(context, function, ContentDialogText);
                }
                callback.positiveCallback();
                //this.cancel();
                break;
        }
    }
}
