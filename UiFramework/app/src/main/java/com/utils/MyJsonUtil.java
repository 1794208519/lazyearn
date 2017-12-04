package com.utils;

import android.text.TextUtils;
import android.util.Log;

import com.entity.JsonBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;


/**
 * Created by qq944 on 2017/9/29.
 */

public class MyJsonUtil {
    /**
     * 获得对应的实体类
     * @param response
     * @param c
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T>  T  getBeanByJson(String response,Class<T> c) throws JsonSyntaxException{
        Gson gson = new Gson();
        JsonBean jsonBean = new JsonBean();
        String jsonBodyStr = null;
        T t = null;
        if ((response != null && !TextUtils.isEmpty(response) && !response.equals("[]"))){
            Log.d("Data", "数据为：" + response);
            jsonBean = gson.fromJson(response,JsonBean.class);
            Log.d("Data",jsonBean.getBody().toString());
            jsonBodyStr = gson.toJson(jsonBean.getBody());
        }
        Log.d("Data", "数据为：" + jsonBodyStr);
        if (jsonBean.isSuccess()&&jsonBodyStr != null && !TextUtils.isEmpty(jsonBodyStr) && !jsonBodyStr.equals("[]")&&!"".equals(jsonBodyStr)) {
            t =  (T)gson.fromJson(jsonBodyStr,c);
        }else {
            Log.e("err",jsonBean.getMsg()+":"+jsonBean.getErrorCode());
        }
        return t;
    }

    /**
     * 获得对应的实体类或List
     * @param response
     * @param type
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T>  T  getBeanByJson(String response, Type type) throws JsonSyntaxException{
        Gson gson = new Gson();
        JsonBean jsonBean = new JsonBean();
        String jsonBodyStr = null;
        T t = null;
        if ((response != null && !TextUtils.isEmpty(response) && !response.equals("[]"))){
            jsonBean = gson.fromJson(response,JsonBean.class);
            jsonBodyStr = gson.toJson(jsonBean.getBody());
        }
        Log.d("Data", "数据为：" + jsonBodyStr);
        if (jsonBean.isSuccess()&&jsonBodyStr != null && !TextUtils.isEmpty(jsonBodyStr) && !jsonBodyStr.equals("{}")) {
           t = (T) gson.fromJson(jsonBodyStr,type);
            //new TypeToken<T>(){}.getType()
        }else {
            Log.e("err",jsonBean.getMsg()+":"+jsonBean.getErrorCode());
        }
        return t;
    }

    public static Boolean getBeanByJson(String response){
        Gson gson = new Gson();
        JsonBean jsonBean = new JsonBean();
        String jsonBodyStr = null;
        if ((response != null && !TextUtils.isEmpty(response) && !response.equals("[]"))){
            jsonBean = gson.fromJson(response,JsonBean.class);
            jsonBodyStr = jsonBean.getBody().toString();
        }
        Log.d("Data", "数据为：" + jsonBodyStr);
        if (jsonBean.isSuccess()) {
            return true;
        }else {
            Log.e("err",jsonBean.getMsg()+":"+jsonBean.getErrorCode());
            return false;
        }
    }
}
