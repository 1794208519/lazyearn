package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.Interface.View.Callback;
import com.ui.activity.DefaultDataActivity;
import com.uidemo.R;
import com.utils.PrefUtils;
import com.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * author: Twisted
 * created on: 2017/8/12 14:31
 * description:
 */

public class TwoLineDialog extends Dialog implements View.OnClickListener {
    private PickerView dialog_nearby_wheel;
    private Button dialog_nearby_no;
    private EditText dialog_nearby_et;
    private Button dialog_nearby_yes;
    private TextView dialog_nearby_tv;
    private TextView dialog_nearby_tv1;
    private Callback callback;
    private Context context;
    private String function, function1;
    private List<String> mStringList = new ArrayList<>();
    private String mCurrentSelect;
    private int poi;
    private boolean isMoved = false;

    public TwoLineDialog(Context context, int poi, String function, String function1, Callback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.function = function;
        this.function1 = function1;
        this.poi = poi;
        setContentDialog();
    }

    private void setContentDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_nearby, null);
        dialog_nearby_wheel = ((PickerView) mView.findViewById(R.id.dialog_nearby_wheel));
        dialog_nearby_no = ((Button) mView.findViewById(R.id.dialog_nearby_no));
        dialog_nearby_et = ((EditText) mView.findViewById(R.id.dialog_nearby_et));
        dialog_nearby_yes = ((Button) mView.findViewById(R.id.dialog_nearby_yes));
        dialog_nearby_tv = ((TextView) mView.findViewById(R.id.dialog_nearby_tv));
        dialog_nearby_tv1 = ((TextView) mView.findViewById(R.id.dialog_nearby_tv1));

        dialog_nearby_no.setOnClickListener(this);
        dialog_nearby_yes.setOnClickListener(this);
        if (poi == 0) {//附近人
            for (int i = 41; i <= 80; i++) {
                mStringList.add(i + "");
            }
            for (int i = 0; i <= 40; i++) {
                mStringList.add(i + "");
            }
        } else if (poi == 4) {//漂流瓶
            for (int i = 11; i <= 20; i++) {
                mStringList.add(i + "");
            }
            for (int i = 0; i <= 10; i++) {
                mStringList.add(i + "");
            }
        } else if (poi == 5) {//朋友圈
            for (int i = 31; i <= 60; i++) {
                mStringList.add(i + "");
            }
            for (int i = 0; i <= 30; i++) {
                mStringList.add(i + "");
            }
        }
        dialog_nearby_et.setText(PrefUtils.getString(context, function1, ""));
        dialog_nearby_et.setSelection(dialog_nearby_et.getText().length());
        //mCurrentSelect = mStringList.size() / 2 + "";
        dialog_nearby_wheel.setData(mStringList);
        dialog_nearby_wheel.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                isMoved = true;
                mCurrentSelect = text;
                Log.i("123", mCurrentSelect);

            }
        });
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
                //                dialog_nearby_wheel.getdata();

                String ContentDialogText = dialog_nearby_et.getText().toString().trim();
                if (isMoved) {
                    PrefUtils.putString(context, function, mCurrentSelect);
                }
                Log.i("123456", poi + "...." + ContentDialogText.length());
                if (poi == 0 && !TextUtils.isEmpty(ContentDialogText) && ContentDialogText.length() > 50) {
                    ToastUtils.showShort(context, "附近人打招呼内容不能超过50个字");
                } else if (poi == 4 && !TextUtils.isEmpty(ContentDialogText) && ContentDialogText.length() < 5) {
                    ToastUtils.showShort(context, "漂流瓶编辑的内容不能少于5个字");
                } else if (poi == 5 && !TextUtils.isEmpty(ContentDialogText) && ContentDialogText.length() > 9998) {
                    ToastUtils.showShort(context, "朋友圈评论内容不能超过9998个字");
                } else {
                    PrefUtils.putString(context, function1, ContentDialogText);
                    callback.positiveCallback();
                    this.cancel();
                }
                break;
        }
    }

    public void setMessage(String numtext, String contenttext) {
        dialog_nearby_tv.setText(numtext);
        dialog_nearby_tv1.setText(contenttext);
    }

    public void setWheelPickerTwo(String num) {
        for (int i = 0; i < mStringList.size(); i++) {
            if (mStringList.get(i).equals(num + "")) {
                dialog_nearby_wheel.setSelected(i);
            }
        }

    }
}
