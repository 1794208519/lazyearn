package com.Interface.View;

/**
 * Created by vicmob_yf002 on 2017/6/29.
 */
public interface MessageConfirmCallback {
    public void positiveCallback(String key, String content,String codeid);

    public void negativeCallback();
}
