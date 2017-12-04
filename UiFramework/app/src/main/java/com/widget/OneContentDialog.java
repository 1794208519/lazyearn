package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.Interface.View.Callback;
import com.uidemo.R;
import com.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjing on 2017/9/4.
 */

public class OneContentDialog extends Dialog implements View.OnClickListener {
    private Button dialog_nearby_no;
    private EditText dialog_nearby_et;
    private Button dialog_nearby_yes;
    private TextView dialog_nearby_tv1;
    private Callback callback;
    private Context context;
    private String function;
    private int poi;

    public OneContentDialog(Context context, int poi, String function, Callback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.function = function;
        this.poi = poi;
        setContentDialog();
    }

    private void setContentDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.one_content_dialog, null);
        dialog_nearby_no = ((Button) mView.findViewById(R.id.dialog_nearby_no));
        dialog_nearby_et = ((EditText) mView.findViewById(R.id.dialog_nearby_et));
        dialog_nearby_yes = ((Button) mView.findViewById(R.id.dialog_nearby_yes));
        dialog_nearby_tv1 = ((TextView) mView.findViewById(R.id.dialog_nearby_tv1));
        dialog_nearby_no.setOnClickListener(this);
        dialog_nearby_yes.setOnClickListener(this);
        dialog_nearby_et.setText(PrefUtils.getString(context, function, ""));
        dialog_nearby_et.setSelection(dialog_nearby_et.getText().length());
        super.setContentView(mView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_nearby_no:
                callback.negativeCallback();
                //this.cancel();
                break;

            case R.id.dialog_nearby_yes:
                //把输入内容储存到SP中
                String ContentDialogText = dialog_nearby_et.getText().toString().trim();
                PrefUtils.putString(context, function, ContentDialogText);
                callback.positiveCallback();
                this.cancel();
                break;
        }
    }
    public void setMessage(String contenttext) {
        dialog_nearby_tv1.setText(contenttext);
    }
}
