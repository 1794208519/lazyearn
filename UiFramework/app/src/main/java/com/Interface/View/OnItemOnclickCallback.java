package com.Interface.View;

import android.view.View;

import com.entity.IntelligentUpdateBean;

import java.util.ArrayList;

/**
 * Created by fxw on 2017/10/27.
 */

public interface OnItemOnclickCallback {
    public void OnItemClick(View view, int postion, ArrayList<IntelligentUpdateBean.VicCodetextBean> listdata);
}
