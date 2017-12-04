package com.ui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.ui.fragment.AddressFragment;
import com.uidemo.R;
import com.widget.DragLayout;

public class MapupdateActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapupdate);
        AddressFragment fragment = new AddressFragment();
        FragmentTransaction fragmentManager=getFragmentManager().beginTransaction();
        fragmentManager.add(R.id.map_container,fragment).commit();
        findViewById(R.id.device_back).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", "My name is linjiqin");
                //设置返回数据
                MapupdateActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                MapupdateActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //数据是使用Intent返回
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.putExtra("result", "");
            //设置返回数据
            MapupdateActivity.this.setResult(2, intent);
            //关闭Activity
            MapupdateActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
