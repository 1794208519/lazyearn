package com.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Interface.View.AdapterCallback;
import com.Interface.View.ConfirmCallback;
import com.Interface.View.MessageCallback;
import com.Interface.View.MessageConfirmCallback;
import com.MyApp.MyApplication;
import com.base.BaseActivity;
import com.entity.IntelligentUpdateBean;
import com.entity.MessageBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ui.Adapter.MessageAdapter;
import com.uidemo.R;
import com.utils.MyJsonUtil;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.ContentDialog;
import com.widget.LoadingDialog;
import com.widget.MessageContentDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MessageReplyActivity extends BaseActivity {
    private String message;
    List<MessageBean> mArraylist=new ArrayList<MessageBean>();
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private Context mContext;
    private TextView intelligent_itemkey;
    //删除文本回复
    private String deleteTextUrl = Urlutils.getInsertTextDelete();
    private String textUrl = Urlutils.getUpdateTextUrl();
    private static final  int addSuccess=2;
    private static final int deleteSuccess = 5;
    private static final int deleteFalse = 6;
    private static final int UPDATESUCCESS = 10;
    private static final int UPDATEFAULT = 11;
    private static final int BACK = 1;
    private static final int BACKFALUT = 8;
    private MessageContentDialog contentDialog;
    private String creatime = "";
    private static final int showUpdate = 12;
    private LoadingDialog loadingDialog;
    //获取文本回复
    private String getTextUrl = Urlutils.getIntelligentTextUrl();
    private MyApplication app;
    private HashMap<String, ArrayList<IntelligentUpdateBean.VicCodetextBean>> shoporderMap = new HashMap<String, ArrayList<IntelligentUpdateBean.VicCodetextBean>>();

    private ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>> biglist = new ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>>();
    private Button message_add;
    private String inserttextUrl = Urlutils.getInsertTextUrl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras()!=null){
            message=getIntent().getStringExtra("message");
        }

        mContext=this;
        app= (MyApplication) getApplication();
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(message).getAsJsonArray();
        Gson gson=new Gson();
        Log.i("123",message);
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            MessageBean userBean = gson.fromJson(user, MessageBean.class);
            mArraylist.add(userBean);
        }
        if (mArraylist!=null&&mArraylist.size()>0){
            intelligent_itemkey.setText(mArraylist.get(0).getKeyword());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_reply;
    }

    @Override
    public void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.message_intelligentList);
        intelligent_itemkey= (TextView) findViewById(R.id.intelligent_itemkey);
        message_add= (Button) findViewById(R.id.message_add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter=new MessageAdapter(mArraylist, this, new MessageCallback() {
            @Override
            public void deletCallback(String keyword, String content,String codeid) {
                deleteThread(keyword, content,codeid);
            }

            @Override
            public void updateCallback(String keyword, String content, String codeid) {
                contentDialog = new MessageContentDialog(MessageReplyActivity.this, new MessageConfirmCallback() {
                    @Override
                    public void positiveCallback(String key, String content,String codeid) {
                        upDate(key, content,codeid);
                    }

                    @Override
                    public void negativeCallback() {

                    }
                });
                contentDialog.setKeyWord(keyword);
                contentDialog.setCon(content);
                contentDialog.setCodeid(codeid);
                contentDialog.show();
            }

        });
        recyclerView.setAdapter(messageAdapter);
        Log.i("123",new Gson().toJson(mArraylist));

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        findViewById(R.id.intelligent_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        message_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentDialog = new MessageContentDialog(MessageReplyActivity.this, new MessageConfirmCallback() {
                    @Override
                    public void positiveCallback(String key, String content,String codeid) {
                        upLoadThread(key,content);
                    }

                    @Override
                    public void negativeCallback() {

                    }
                });
                contentDialog.setKeyWordfalse(intelligent_itemkey.getText().toString());
                contentDialog.show();
            }
        });
    }

    @Override
    public void initData() {

    }
    /**
     * 删除数据
     */
    public void deleteThread(final String key, final String content,final String codeid) {
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(deleteTextUrl)
                        .addParams("keyword", key)
                        .addParams("text", content)
                        .addParams("codeTextId",codeid)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(deleteFalse);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("123",response);
                                Message message = handler.obtainMessage();
                                message.what = deleteSuccess;
                                message.obj = MyJsonUtil.getBeanByJson(response);
                                handler.sendMessage(message);

                            }
                        });
            }
        };
        th.start();
    }
    /**
     * 更新数据库的方法
     *
     * @param key
     * @param content
     */
    public void upDate(String key, String content,String codeid) {
        handler.sendEmptyMessage(showUpdate);
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        creatime = formatter.format(curDate);
        upDateThread(key, content,codeid);
    }

    /**
     * 更新数据
     */
    public void upDateThread(final String keyword, final String content, final String codeid) {
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(textUrl)
                        .addParams("codeTextId",codeid)
                        .addParams("keyword", keyword)
                        .addParams("text", content)
                        .addParams("creatime", creatime)
                        .addParams("type", "text")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(UPDATEFAULT);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.i("123",response);
                                Message message = handler.obtainMessage();
                                message.what = UPDATESUCCESS;
                                message.obj = MyJsonUtil.getBeanByJson(response);
                                handler.sendMessage(message);
                            }
                        });
            }
        };
        th.start();
    }
    /**
     * 接收数据
     */
    private void accessWebData() {

        Thread thr = new Thread() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url(getTextUrl)
                        .addParams("devices",app.getDevices())
                        .addParams("keyword",mArraylist.get(0).getKeyword())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                handler.sendEmptyMessage(BACKFALUT);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    IntelligentUpdateBean intelligentBean = MyJsonUtil.getBeanByJson(response,IntelligentUpdateBean.class);
                                    biglist.clear();
                                    shoporderMap.clear();
                                    for (IntelligentUpdateBean.VicCodetextBean i : intelligentBean.getVic_codetext()){
                                        String keyword=i.getKeyword().toString();
                                        if (!shoporderMap.containsKey(keyword)){
                                            shoporderMap.put(keyword,new ArrayList<IntelligentUpdateBean.VicCodetextBean>());
                                            biglist.add(shoporderMap.get(keyword));

                                        }
                                        shoporderMap.get(keyword).add(i);
                                    }

                                    handler.sendEmptyMessage(BACK);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(BACKFALUT);
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
        final Map<String, String> params = new HashMap<String, String>();
        params.put("keyword", keyword);
        params.put("text", content);
        params.put("creatime", creatime);
        params.put("type", "text");
        params.put("devices", app.getDevices());
        Log.i("123",new Gson().toJson(params));
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(inserttextUrl)
                        .params(params)
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
            case deleteSuccess:
                if ((Boolean) msg.obj) {
                    ToastUtils.showShort(MessageReplyActivity.this, "删除成功");
                    accessWebData();
                } else {
                    ToastUtils.showShort(MessageReplyActivity.this, "删除失败");
                }
                break;
            case deleteFalse:
                ToastUtils.showShort(MessageReplyActivity.this, "删除失败");
                break;
            case UPDATEFAULT:
                ToastUtils.showShort(MessageReplyActivity.this, "更新失败");
                if (loadingDialog!=null){
                    loadingDialog.cancel();
                }
                break;
            case UPDATESUCCESS:
                if ((Boolean) msg.obj) {
                    ToastUtils.showShort(MessageReplyActivity.this, "更新成功");
                    accessWebData();
                } else {
                    ToastUtils.showShort(MessageReplyActivity.this, "更新失败");

                }
                if (loadingDialog!=null){
                    loadingDialog.cancel();
                }
                break;
            case showUpdate:
                loadingDialog = new LoadingDialog(MessageReplyActivity.this);
                loadingDialog.setContent("正在更新...            ");
                loadingDialog.setIndicator(25);
                loadingDialog.setCancelable(false);
                loadingDialog.show();
                break;
            case BACK:
                if (biglist!=null&&biglist.size()>0){
                    mArraylist.clear();
                    for (int i=0;i<biglist.get(0).size();i++){
                        MessageBean messageBean =new MessageBean();
                        messageBean.setCodeTextId(biglist.get(0).get(i).getCodeTextId());
                        messageBean.setCreatime(biglist.get(0).get(i).getCreatime());
                        messageBean.setKeyword(biglist.get(0).get(i).getKeyword());
                        messageBean.setText(biglist.get(0).get(i).getText());
                        messageBean.setType(biglist.get(0).get(i).getType());
                        mArraylist.add(messageBean);
                    }
                    messageAdapter.noitychanged(mArraylist);
                }else {
                    mArraylist.clear();
                    messageAdapter.noitychanged(mArraylist);
                    MessageReplyActivity.this.finish();
                }

                break;
            case BACKFALUT:
                mArraylist.clear();
                messageAdapter.noitychanged(mArraylist);
                break;
            case addSuccess:
                if ((Boolean) msg.obj) {
                    ToastUtils.showShort(MessageReplyActivity.this, "添加成功");
                    accessWebData();
                } else {
                    ToastUtils.showShort(MessageReplyActivity.this, "添加失败");

                }
                break;
        }
    }
};
}
