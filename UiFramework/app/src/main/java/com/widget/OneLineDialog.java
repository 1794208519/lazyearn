package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Interface.View.Callback;
import com.uidemo.R;
import com.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Twisted
 * created on: 2017/8/15 15:58
 * description:
 */

public class OneLineDialog extends Dialog implements View.OnClickListener {
    private PickerView dialog_oneLine_wheel;
    private Button dialog_oneLine_no;
    private Button dialog_oneLine_yes;
    private Callback callback;
    private Context context;
    private String function;
    private List<String> mStringList = new ArrayList<>();
    private String mCurrentSelect;
    private TextView dialog_oneLine_tv;
    private int poi;
    private boolean isMoved=false;

    public OneLineDialog(Context context, int poi, String function, Callback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.function = function;
        this.poi = poi;
        setContentDialog();
    }

    private void setContentDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.one_line_dialog, null);
        dialog_oneLine_wheel = ((PickerView) mView.findViewById(R.id.dialog_oneLine_wheel));
        dialog_oneLine_no = ((Button) mView.findViewById(R.id.dialog_oneLine_no));
        dialog_oneLine_yes = ((Button) mView.findViewById(R.id.dialog_oneLine_yes));
        dialog_oneLine_tv = ((TextView) mView.findViewById(R.id.dialog_oneLine_tv));
        dialog_oneLine_no.setOnClickListener(this);
        dialog_oneLine_yes.setOnClickListener(this);
        if (poi == 1) {//一键加好友
            for (int i = 41; i <= 80; i++) {
                mStringList.add(i + "");
            }
            for (int i = 0; i <= 40; i++) {
                mStringList.add(i + "");
            }
        } else if (poi == 2) {//一键加群友
            for (int i = 6; i <= 10; i++) {
                mStringList.add(i + "");
            }
            for (int i = 0; i <= 5; i++) {
                mStringList.add(i + "");
            }
        }
        dialog_oneLine_wheel.setData(mStringList);
        dialog_oneLine_wheel.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                isMoved = true;
                mCurrentSelect = text;
                Log.i("1234", mCurrentSelect);
            }
        });
        super.setContentView(mView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_oneLine_no:
                callback.negativeCallback();
                //this.cancel();
                break;

            case R.id.dialog_oneLine_yes:
                if (isMoved) {
                    PrefUtils.putString(context, function, mCurrentSelect);
                }
                callback.positiveCallback();
                this.cancel();
                break;
        }
    }

    public void setMessage(String numtext) {
        dialog_oneLine_tv.setText(numtext);
    }

    public void setWheelPicker(String num) {
        for (int i = 0; i < mStringList.size(); i++) {
            if (mStringList.get(i).equals(num + "")) {
                dialog_oneLine_wheel.setSelected(i);
            }
        }

    }
}
