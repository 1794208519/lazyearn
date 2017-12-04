package com.ui.activity;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.MyApp.MyApplication;
import com.base.BaseActivity;
import com.uidemo.R;
import com.utils.PrefUtils;

import java.text.ParseException;
import java.util.Date;

public class SetTimerActivity extends BaseActivity {
    private ImageView iv_fanhui_set_timer;
    private TimePicker tp_set_timer;
    private EditText et_interval;
    private Button bt_interval;
  /*  private int hour;
    private int minute;
    private int interval;*/


    @Override
    public int getLayoutId() {
        return R.layout.activity_set_timer;
    }

    @Override
    public void findView() {
        iv_fanhui_set_timer = ((ImageView) findViewById(R.id.iv_fanhui_set_timer));
        tp_set_timer = ((TimePicker) findViewById(R.id.tp_set_timer));
        et_interval = ((EditText) findViewById(R.id.et_interval));
        bt_interval = ((Button) findViewById(R.id.bt_interval));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initView() {
        int hour = PrefUtils.getInt(SetTimerActivity.this,"hour",8);
        int minute = PrefUtils.getInt(SetTimerActivity.this,"minute",30);
        int interval = PrefUtils.getInt(SetTimerActivity.this,"interval",24);
        tp_set_timer.setIs24HourView(true);
        tp_set_timer.setHour(hour);
        tp_set_timer.setMinute(minute);
        et_interval.setText(interval+"");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initListener() {
        et_interval.setSelection(et_interval.getText().length());
        //SimpleDateFormat bartDateFormat = new SimpleDateFormat("HH:mm");

        iv_fanhui_set_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = tp_set_timer.getHour();
                int minute = tp_set_timer.getMinute();
                int interval = 1;
                if (et_interval.getText()!=null&&!"".equals(et_interval.getText().toString().trim())){
                    interval = Integer.parseInt(et_interval.getText().toString().trim());
                }
                interval = (interval<1)?1:interval;
                et_interval.setText(interval+"");
                et_interval.setSelection(et_interval.getText().length());
                PrefUtils.putInt(SetTimerActivity.this,"hour", hour);
                PrefUtils.putInt(SetTimerActivity.this,"minute", minute);
                PrefUtils.putInt(SetTimerActivity.this,"interval", interval);
                Log.i("zw1027","hour:"+hour+",minute:"+minute+",interval:"+interval+",,AAAAA");
                Toast.makeText(SetTimerActivity.this,"设置定时成功！！",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void initData() {
        /*String timeStringToParse = hour+":"+minute;
        try {
            MyApplication.date = bartDateFormat.parse(timeStringToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }
}
