package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.Interface.View.ConfirmCallback;
import com.Interface.View.ReplayCallback;
import com.MyApp.MyApplication;
import com.entity.IntelligentUpdateBean;
import com.google.gson.Gson;
import com.ui.Adapter.IntelligentAdapter;
import com.ui.Adapter.IntelligentDialogAdapter;
import com.ui.activity.IntelligentActivity;
import com.ui.activity.MainActivity;
import com.uidemo.R;
import com.utils.MyJsonUtil;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by Eren on 2017/6/27.
 * 自定义带多内容的对话框
 */
public class ContentDialog extends Dialog implements View.OnClickListener {

    ReplayCallback confirmCallback;
    Context context;
    private ImageView ivCancel;
    private EditText etKey;
    private Button lin_btn;
    String content;
    int Num=0;
    private Map<String,String> params=new HashMap<String, String>();
    private List<View> viewList=new ArrayList<View>();
    private HashMap<String, ArrayList<IntelligentUpdateBean.VicCodetextBean>> shoporderMap = new HashMap<String, ArrayList<IntelligentUpdateBean.VicCodetextBean>>();
    private ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>> myArrayList = new ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>>();
    private String devices;
    private String getTextUrl = Urlutils.getIntellinolikegentTextUrl();
    private RecyclerView recyclerView;
    private IntelligentDialogAdapter intelligentDialogAdapter;
    private final int SUCCESS=1;
    private final int FAIL=0;
    private final int addSuccess=2;
    private Button lin_add_btn;
    private LinearLayout container;
    //上传路径
    private String textUrl = Urlutils.getInsertTextUrl();
    private String creatime;
    List<String> enabletext=new ArrayList<String>();
    public ContentDialog(Context context, ReplayCallback callback,String devices) {
        super(context, R.style.ContentDialog);
        this.context = context;
        this.confirmCallback = callback;
        this.devices=devices;
        setContentDialog();
    }

    private void setContentDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_content, null);
        ivCancel = (ImageView) mView.findViewById(R.id.et_cancel);
        etKey = (EditText) mView.findViewById(R.id.et_key);
        lin_btn= (Button) mView.findViewById(R.id.lin_btn);
        recyclerView = (RecyclerView) mView.findViewById(R.id.dialog_intelligentList);

        lin_add_btn= (Button) mView.findViewById(R.id.lin_add_btn);
        lin_add_btn.setVisibility(View.INVISIBLE);
//        View child = LayoutInflater.from(context).inflate(R.layout.dialog_edt, container, false);
        Button but_content = (Button) mView.findViewById(R.id.but_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        intelligentDialogAdapter=new IntelligentDialogAdapter(myArrayList,context);
        recyclerView.setAdapter(intelligentDialogAdapter);
//        btn.setText("+");
//        // 监听点击事件
//        btn.setOnClickListener(this);
        // 添加进父控件
//        viewList.add(child);
//        container.addView(child);
        but_content.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        lin_btn.setOnClickListener(this);
        lin_add_btn.setOnClickListener(this);
        super.setContentView(mView);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn:
//                // 父控件
//                LinearLayout container = (LinearLayout) findViewById(R.id.main);
//                // 根据tag区分是新增view还是删除view
//
//                String tag = (String) v.getTag();
//                if ("-".equals(tag)) {
//                    // 删除view
//                    // 获取子控件
//                    Num--;
//                    View child = (View) v.getParent();
//                    // 从父控件中移除子控件
//                    container.removeView(child);
//                    viewList.remove(child);
//                } else if(Num < 4){
//                    // 新增view
//                    // 创建子控件实例
//                    View child = LayoutInflater.from(context).inflate(R.layout.dialog_edt, container, false);
//
//                    // 获取其中的button
//                    Button btn = (Button) child.findViewById(R.id.btn);
//                    // 监听点击事件
//                    btn.setOnClickListener(this);
//                    // 设置删除的tag
//                    btn.setTag("-");
//                    Num++;
//                    btn.setText(Num+"");
//
//                    // 添加进父控件
//                    viewList.add(child);
//
//                    container.addView(child);
//
//                }
//                // 请求重绘
//                container.invalidate();
//                break;
            case R.id.et_cancel:
                confirmCallback.negativeCallback();
                this.cancel();
                break;

            case R.id.lin_btn:
                lin_btn.setEnabled(false);
                etKey.setEnabled(false);
                if (etKey.getText().toString().trim().length()<=0){
                   ToastUtils.showLong(context,"关键词不能为空");
                    lin_btn.setEnabled(true);
                    etKey.setEnabled(true);
                    return;
                }
                InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (im.isActive()) {
                    im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                params.put("keyword",etKey.getText().toString().trim());

                accessWebData();
                break;
            case R.id.lin_add_btn:
                container = (LinearLayout)findViewById(R.id.main);
                int len=0;
                if (myArrayList!=null&&myArrayList.size()>0&&myArrayList.get(0).size()<5){
                   len=myArrayList.get(0).size();
                }
                if (viewList.size()>0){
                    len=len+viewList.size();
                }
                if (len>=4){
                    lin_add_btn.setVisibility(View.INVISIBLE);
                }
                final View child = LayoutInflater.from(context).inflate(R.layout.dialog_edt, container, false);
                TextView child_t= (TextView) child.findViewById(R.id.lin_dialog_num);
//                final EditText child_et= (EditText) child.findViewById(R.id.lin_et_put);
//                final Button child_btn= (Button) child.findViewById(R.id.lin_btn);
                child_t.setText("回复"+(len+1)+"：");
//                child_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        child_btn.setEnabled(false);
//                        child_et.setEnabled(false);
//                        if (etKey.getText().toString().trim().length()==0){
//                            ToastUtils.showLong(context,"关键词不能为空");
//                            child_btn.setEnabled(true);
//                            child_et.setEnabled(true);
//                            return;
//                        }
//                        if (child_et.getText().toString().trim().length()==0){
//                            ToastUtils.showLong(context,"回复不能为空");
//                            child_btn.setEnabled(true);
//                            child_et.setEnabled(true);
//                            return;
//                        }
//
//                        upLoadThread(etKey.getText().toString().trim(),child_et.getText().toString().trim(),child);
//                    }
//                });
                viewList.add(child);
                container.addView(child);
                break;
            case R.id.but_content:
                etKey.setEnabled(false);
                if (etKey.getText().toString().trim().length()==0){
                            ToastUtils.showLong(context,"关键词不能为空");
                    etKey.setEnabled(true);
                            return;
                }

                String text[]=new String[5];

                Log.i("123",viewList.size()+"nnn");
                for (int i=0;i<viewList.size();i++){
                    final EditText child_et= (EditText) viewList.get(i).findViewById(R.id.lin_et_put);
                    if (child_et.isEnabled()) {
                        Log.i("123",child_et.getText().toString()+"ggggggggg");
                        if (child_et.getText().toString().trim().length() > 0) {
                            child_et.setEnabled(false);
                            for (int j=0;j<enabletext.size();j++){
                                if (enabletext.contains(child_et.getText().toString().trim())){
                                    ToastUtils.showLong(context,"回复内容已存在");
                                    child_et.setEnabled(true);
                                }
                            }
                            if (!child_et.isEnabled()){
                                text[i] = child_et.getText().toString().trim();
                                enabletext.add(child_et.getText().toString().trim());
                            }

                        }
                    }

                }

                for (int j=0;j<text.length;j++){

                    Log.i("123",text[j]+"sss");
                    if (text[j]!=null&&text[j].length()>0) {
                        upLoadThread(etKey.getText().toString().trim(), text[j]);
                    }
                }
                break;
        }
    }
    /**
     * 接收数据
     */
    private void accessWebData() {
        Log.i("123",devices);
        params.put("devices",devices);
        params.put("pager",0+"");
        params.put("row",5+"");
        Thread thr = new Thread() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url(getTextUrl)
                        .params(params)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    IntelligentUpdateBean intelligentBean = MyJsonUtil.getBeanByJson(response,IntelligentUpdateBean.class);
                                    myArrayList.clear();
                                    shoporderMap.clear();
                                    for (IntelligentUpdateBean.VicCodetextBean i : intelligentBean.getVic_codetext()){
                                        String keyword=i.getKeyword().toString();
                                        if (!shoporderMap.containsKey(keyword)){
                                            shoporderMap.put(keyword,new ArrayList<IntelligentUpdateBean.VicCodetextBean>());
                                            myArrayList.add(shoporderMap.get(keyword));

                                        }
                                        shoporderMap.get(keyword).add(i);
                                    }
                                    Log.i("123",new Gson().toJson(shoporderMap));
                                    Log.i("123",new Gson().toJson(myArrayList));
                                    handler.sendEmptyMessage(SUCCESS);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            }
                        });
            }
        };
        thr.start();
    }
    public void upLoadThread(final String keyword, final String content) {
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        creatime = formatter.format(curDate);
        final Map<String,String> params1=new HashMap<String, String>();
        params1.put("keyword", keyword);
        params1.put("text", content);
        params1.put("creatime", creatime);
        params1.put("type", "text");
        params1.put("devices", devices);
        Log.i("123",new Gson().toJson(params1)+"aaaaaaaaaaa");
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(textUrl)
                        .params(params1)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {

                                Message message = handler.obtainMessage();
                                message.what = addSuccess;
                                message.obj = MyJsonUtil.getBeanByJson(response);
                                handler.sendMessage(message);
                            }
                        });
            }
        };
        th.start();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:

                    intelligentDialogAdapter.notifyChanged(myArrayList);
                    int len=0;
                    if (myArrayList!=null&&myArrayList.size()>0){
                        len=myArrayList.get(0).size();
                        if (viewList.size()>0){
                            len=len+viewList.size();
                        }
                        Log.i("123",myArrayList.get(0).size()+":"+viewList.size()+":"+len);


                    }
                    if (len>=5){
                        lin_add_btn.setVisibility(View.INVISIBLE);
                    }
                    if (len<5) {
                        lin_add_btn.setVisibility(View.VISIBLE);
                    }
                    if (myArrayList!=null&&myArrayList.size()>0) {
                        for (int i = 0; i < myArrayList.get(0).size(); i++) {
                            enabletext.add(myArrayList.get(0).get(i).getText());
                        }
                    }
                    break;
                case addSuccess:
                    if ((Boolean) msg.obj) {
                        ToastUtils.showShort(context, "上传成功");
                    }
                    confirmCallback.positiveCallback(etKey.getText().toString().trim(),null);
                    break;
            }
        }
    };
}
