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
 * created on: 2017/8/15 16:19
 * description:
 */

public class ThreeLineDialog extends Dialog implements View.OnClickListener {
    private PickerView dialog_threeLine_wheel1;
    private PickerView dialog_threeLine_wheel2;
    private PickerView dialog_threeLine_wheel3;
    private Button dialog_ThreeLine_no;
    private Button dialog_ThreeLine_yes;
    private Callback callback;
    private Context context;
    private String function;
    private String function1;
    private String function2;
    private List<String> mStringList = new ArrayList<>();
    private List<String> mStringList1 = new ArrayList<>();
    private List<String> mStringList2 = new ArrayList<>();

    private String mCurrentSelect1;
    private String mCurrentSelect2;
    private String mCurrentSelect3;
    private TextView dialog_threeLine_tv1, dialog_threeLine_tv2, dialog_threeLine_tv3;
    private int p;
    private boolean isMoved1 = false;
    private boolean isMoved2 = false;
    private boolean isMoved3 = false;

    public ThreeLineDialog(Context context, int p, String function, String function1, String function2, Callback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.function = function;
        this.function1 = function1;
        this.function2 = function2;
        this.p = p;
        setContentDialog();
    }

    private void setContentDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.three_line_dialog, null);
        dialog_threeLine_wheel1 = ((PickerView) mView.findViewById(R.id.dialog_threeLine_wheel1));
        dialog_threeLine_wheel2 = ((PickerView) mView.findViewById(R.id.dialog_threeLine_wheel2));
        dialog_threeLine_wheel3 = ((PickerView) mView.findViewById(R.id.dialog_threeLine_wheel3));
        dialog_ThreeLine_no = ((Button) mView.findViewById(R.id.dialog_ThreeLine_no));
        dialog_ThreeLine_yes = ((Button) mView.findViewById(R.id.dialog_ThreeLine_yes));
        dialog_threeLine_tv1 = ((TextView) mView.findViewById(R.id.dialog_threeLine_tv1));
        dialog_threeLine_tv2 = ((TextView) mView.findViewById(R.id.dialog_threeLine_tv2));
        dialog_threeLine_tv3 = ((TextView) mView.findViewById(R.id.dialog_threeLine_tv3));

        dialog_ThreeLine_no.setOnClickListener(this);
        dialog_ThreeLine_yes.setOnClickListener(this);
        if (p == 3) {
            for (int i = 7; i <= 12; i++) {
                mStringList.add(i + "");
            }
            for (int i = 0; i <= 6; i++) {
                mStringList.add(i + "");
            }
            for (int i = 7; i <= 12; i++) {
                mStringList1.add(i + "");
            }
            for (int i = 0; i <= 6; i++) {
                mStringList1.add(i + "");
            }
            for (int i = 105; i <= 200; i += 5) {
                mStringList2.add(i + "");
            }
            for (int i = 0; i <= 100; i += 5) {
                mStringList2.add(i + "");
            }
        }

        dialog_threeLine_wheel1.setData(mStringList);
        dialog_threeLine_wheel2.setData(mStringList1);
        dialog_threeLine_wheel3.setData(mStringList2);
        dialog_threeLine_wheel1.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                isMoved1 = true;
                mCurrentSelect1 = text;
                Log.i("123", mCurrentSelect1);
            }
        });
        dialog_threeLine_wheel2.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                isMoved2 = true;
                mCurrentSelect2 = text;
                Log.i("123", mCurrentSelect2);
            }
        });
        dialog_threeLine_wheel3.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                isMoved3 = true;
                mCurrentSelect3 = text;
                Log.i("123", mCurrentSelect3);
            }
        });
        super.setContentView(mView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ThreeLine_no:
                callback.negativeCallback();
                //this.cancel();
                break;

            case R.id.dialog_ThreeLine_yes:
                if (isMoved1) {
                    PrefUtils.putString(context, function, mCurrentSelect1);
                }
                if (isMoved2) {
                    PrefUtils.putString(context, function1, mCurrentSelect2);
                }
                if (isMoved3) {
                    PrefUtils.putString(context, function2, mCurrentSelect3);
                }
                callback.positiveCallback();
                this.cancel();
                break;
        }
    }

    public void setMessage(String index, String num, String friend) {
        dialog_threeLine_tv1.setText(index);
        dialog_threeLine_tv2.setText(num);
        dialog_threeLine_tv3.setText(friend);
    }

    public void setWheelPickerThree(String index, String num, String friend) {
        for (int i = 0; i < mStringList.size(); i++) {
            if (mStringList.get(i).equals(index + "")) {
                dialog_threeLine_wheel1.setSelected(i);
            }
        }
        for (int i = 0; i < mStringList1.size(); i++) {
            if (mStringList1.get(i).equals(num)) {
                dialog_threeLine_wheel2.setSelected(i);
            }
        }
        for (int i = 0; i < mStringList2.size(); i++) {
            if (mStringList2.get(i).equals(friend + "")) {
                dialog_threeLine_wheel3.setSelected(i);
            }
        }
    }
}
