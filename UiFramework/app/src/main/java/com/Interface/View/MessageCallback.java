package com.Interface.View;

/**
 * Created by vicmob_yf002 on 2017/6/29.
 * 智能回复回调监听
 */
public interface MessageCallback {
    public void deletCallback(String keyword, String content,String codeid);

    public void updateCallback(String keyword, String content,String codeid);

}
