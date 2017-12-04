package com.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Interface.View.AdapterCallback;
import com.Interface.View.ConfirmCallback;
import com.Interface.View.OnItemOnclickCallback;
import com.Interface.View.ReplayCallback;
import com.Interface.View.loadMoreCallback;
import com.MyApp.MyApplication;
import com.base.BaseActivity;
import com.entity.IntelligentBean;
import com.entity.IntelligentUpdateBean;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.query.In;
import com.ui.Adapter.IntelligentAdapter;
import com.uidemo.R;
import com.utils.MyJsonUtil;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.CompileDialog;
import com.widget.ContentDialog;
import com.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import okhttp3.Call;

/**
 * Created by Eren on 2017/5/22.
 */
public class IntelligentActivity extends BaseActivity implements View.OnClickListener {

    private Button mCode;
    private Button mEdit;
    private Button mBatch;
    private ImageView mBack;
    private RecyclerView recyclerView;
    private IntelligentAdapter intelligentAdapter;
    private RelativeLayout intelligent_load;
    private RelativeLayout intelligent_error;
    private RelativeLayout intelligent_none;
    private LinearLayout liner_search;
    private EditText lin_et_put;
    private Button lin_btn;
    private SwipeRefreshLayout swipe_refresh_widget;
    private int mBatchNumber = 0;
    private int mNumber = 0;
    private int mEditNumber = 0;
    private static final int BACK = 1;
    private static final int BACKFALUT = 8;
    private static final int BACKNONE = 9;
    private static final int addSuccess = 2;
    private static final int addFalut = 3;
    private static final int addRepet = 4;
    private static final int deleteSuccess = 5;
    private static final int deleteFalse = 6;
    private static final int showLoading = 7;
    private static final int UPDATESUCCESS = 10;
    private static final int UPDATEFAULT = 11;
    private static final int showUpdate = 12;
    public static final int LOADING_LOMSG = 13;
    private static final int XLS = 14;
    public static final int IMPORT_LOMSG = 15;
    public static final int LOADMORE = 16;
    private static final String TAG = "IntelligentActivity";

    //获取关键字集合
    List<String> keylist = new ArrayList<>();
    //获取内容集合
    List<String> textlist = new ArrayList<>();
    //上传路径
    private String textUrl = Urlutils.getInsertTextUrl();
    //获取文本回复
    private String getTextUrl = Urlutils.getIntelligentTextUrl();
    //删除文本回复
    private String deleteTextUrl = Urlutils.getInsertTextDelete();
    //上传的类型
    private String type = "text";
    //上传时间
    private String creatime = "";
    private LoadingDialog loadingDialog;
    private ContentDialog contentDialog;
    private CompileDialog compileDialog;
    //加载动画图片
    private ImageView imageView;
    //动画
    AnimationDrawable loadingDrawable;
//    private HashMap<String, ArrayList<IntelligentUpdateBean.VicCodetextBean>> shoporderMap = new HashMap<String, ArrayList<IntelligentUpdateBean.VicCodetextBean>>();
//
//    private ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>> myArrayList = new ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>>();
    private Map<String,String>params=new HashMap<String, String>();
    private MyApplication app;
    LinearLayoutManager mLayoutManager;
    private int pager=0;int row=20;
    private int lastVisibleItem;
    IntelligentUpdateBean intelligentBean=new IntelligentUpdateBean();
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BACK:
                    //更新ui
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //加载数据
                            try {
                                Thread.sleep(1200);
                            } catch (InterruptedException e) {
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    intelligent_load.setVisibility(View.INVISIBLE);
                                    liner_search.setVisibility(View.VISIBLE);
                                    swipe_refresh_widget.setVisibility(View.VISIBLE);
                                    intelligent_error.setVisibility(View.INVISIBLE);
                                    intelligent_none.setVisibility(View.INVISIBLE);
                                    if (recyclerView.isComputingLayout()==false) {
                                        intelligentAdapter.notifyDataChanged(intelligentBean);
                                    }
                                    if (loadingDialog != null) {
                                        loadingDialog.cancel();
                                    }
                                }
                            });
                        }
                    }).start();

                    break;
                case BACKNONE:
                    //更新ui
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //加载数据
                            try {
                                Thread.sleep(1200);
                            } catch (InterruptedException e) {
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    intelligent_load.setVisibility(View.INVISIBLE);
                                    liner_search.setVisibility(View.INVISIBLE);
                                    swipe_refresh_widget.setVisibility(View.INVISIBLE);
                                    intelligent_error.setVisibility(View.INVISIBLE);
                                    intelligent_none.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }).start();
                    break;
                case BACKFALUT:
                    //更新ui
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //加载数据
                            try {
                                Thread.sleep(1200);
                            } catch (InterruptedException e) {
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    intelligent_load.setVisibility(View.INVISIBLE);
                                    liner_search.setVisibility(View.INVISIBLE);
                                    swipe_refresh_widget.setVisibility(View.INVISIBLE);
                                    intelligent_none.setVisibility(View.INVISIBLE);
                                    intelligent_error.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }).start();
                    break;
                case addSuccess:
                    if (mNumber >= mEditNumber-1) {
                        if ((Boolean) msg.obj) {
                            accessWebData();
                            ToastUtils.showShort(IntelligentActivity.this, "上传成功");
                        }

                    }
                     else {
                            ToastUtils.showShort(IntelligentActivity.this, "上传失败");
                            if (loadingDialog != null) {
                                loadingDialog.cancel();
                            }
                        }

                    break;
                case addFalut:
                    if (loadingDialog != null) {
                        loadingDialog.cancel();
                    }
                    ToastUtils.showShort(IntelligentActivity.this, "数据库连接异常");
                    break;
                case addRepet:
                    ToastUtils.showShort(IntelligentActivity.this, "关键词重复，请重新输入");
                    break;
                case deleteSuccess:
                    if ((Boolean) msg.obj) {
                        ToastUtils.showShort(IntelligentActivity.this, "删除成功");
                        //                        accessWebData();
                    } else {
                        ToastUtils.showShort(IntelligentActivity.this, "删除失败");
                    }
                    break;
                case deleteFalse:
                    ToastUtils.showShort(IntelligentActivity.this, "删除失败");
                    break;
                case showLoading:
                    loadingDialog = new LoadingDialog(IntelligentActivity.this);
                    loadingDialog.setContent("正在上传...            ");
                    loadingDialog.setIndicator(25);
                    loadingDialog.setCancelable(false);
                    loadingDialog.show();
                    break;
                case showUpdate:
                    loadingDialog = new LoadingDialog(IntelligentActivity.this);
                    loadingDialog.setContent("正在更新...            ");
                    loadingDialog.setIndicator(25);
                    loadingDialog.setCancelable(false);
                    loadingDialog.show();
                    break;
                case UPDATEFAULT:
                    if (loadingDialog != null) {
                        loadingDialog.cancel();
                    }
                    ToastUtils.showShort(IntelligentActivity.this, "更新失败");
                    break;
                case UPDATESUCCESS:
                    if ((Boolean) msg.obj) {
                        ToastUtils.showShort(IntelligentActivity.this, "更新成功");
                        accessWebData();
                    } else {
                        ToastUtils.showShort(IntelligentActivity.this, "更新失败");
                        if (loadingDialog != null) {
                            loadingDialog.cancel();
                        }
                    }
                    break;
                case LOADING_LOMSG:
                    //加载进度显示
                    loadingDialog = new LoadingDialog(IntelligentActivity.this);
                    loadingDialog.setContent("正在导入本地文件...");
                    loadingDialog.setCancelable(false);
                    loadingDialog.setIndicator(25);
                    loadingDialog.show();
                    intelligent_none.setVisibility(View.INVISIBLE);
                    break;
                case IMPORT_LOMSG:
                    if (mBatchNumber >= keylist.size() - 1) {
                        ToastUtils.showShort(IntelligentActivity.this, "导入成功");
                        mBatchNumber = 0;
                        accessWebData();
                    }
                    break;
                case LOADMORE:
                    if (recyclerView.isComputingLayout()==false) {
                        intelligentAdapter.loadMorenotifyDataChanged(intelligentBean);
                    }
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.intelligent_activity;
    }

    @Override
    public void findView() {
        app= (MyApplication) getApplication();
        mBack = (ImageView) findViewById(R.id.intelligent_back);
        mCode = (Button) findViewById(R.id.intelligent_code);
        mEdit = (Button) findViewById(R.id.intelligent_edit);
        mBatch = (Button) findViewById(R.id.batch_in);
        intelligent_load = (RelativeLayout) findViewById(R.id.intelligent_load);
        intelligent_error = (RelativeLayout) findViewById(R.id.intelligent_error);
        intelligent_none = (RelativeLayout) findViewById(R.id.intelligent_noneww);
        recyclerView = (RecyclerView) findViewById(R.id.main_intelligentList);
        lin_btn= (Button) findViewById(R.id.lin_btn);
        lin_et_put= (EditText) findViewById(R.id.lin_et_put);
        liner_search= (LinearLayout) findViewById(R.id.liner_search);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        swipe_refresh_widget= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        mBack.setOnClickListener(this);
        mCode.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mBatch.setOnClickListener(this);
        lin_btn.setOnClickListener(this);
        intelligentAdapter = new IntelligentAdapter(intelligentBean,IntelligentActivity.this, new OnItemOnclickCallback() {

            @Override
            public void OnItemClick(View view, int postion, ArrayList<IntelligentUpdateBean.VicCodetextBean> listdata) {
                Intent intent=new Intent(IntelligentActivity.this,MessageReplyActivity.class);
                intent.putExtra("message",new Gson().toJson(listdata));
                startActivity(intent);
            }
        }, new loadMoreCallback() {
            @Override
            public void OnItemClick(View view) {

            }
        });
        recyclerView.setAdapter(intelligentAdapter);
        swipe_refresh_widget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pager=0;
                if (lin_et_put.getText().toString().trim().length()>0){
                    params.put("keyword",lin_et_put.getText().toString().trim());
                }else {
                    if (params.containsKey("keyword")){
                        params.remove("keyword");
                    }
                }
                accessWebData();
            }
        });
        recyclerView.addOnScrollListener(new OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem=mLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("123",lastVisibleItem+":"+intelligentAdapter.getItemCount());
                if (newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1==intelligentAdapter.getItemCount()&&intelligentBean.getVic_codetext()!=null&&intelligentBean.getVic_codetext().size()==20) {
                    pager++;
                    loadMoreData();
                }
            }
        });
    }

    @Override
    public void initView() {
        liner_search.setVisibility(View.INVISIBLE);
        swipe_refresh_widget.setVisibility(View.INVISIBLE);
        intelligent_load.setVisibility(View.VISIBLE);
        intelligent_error.setVisibility(View.INVISIBLE);
        intelligent_none.setVisibility(View.INVISIBLE);
        accessWebData();
        //显示加载的那个动画
        imageView = (ImageView) findViewById(R.id.loading_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        loadingDrawable.start();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //重新加载数据
    public void reLoadIntelligent(View view) {
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intelligent_code:
                Intent intent = new Intent(this, CodeActivity.class);
                startActivity(intent);
                break;
            case R.id.intelligent_back:
                finish();
                break;
            case R.id.intelligent_edit:
                contentDialog = new ContentDialog(IntelligentActivity.this, new ReplayCallback() {
                    @Override
                    public void positiveCallback(String key, String content[]) {
                        liner_search.setVisibility(View.INVISIBLE);
                        swipe_refresh_widget.setVisibility(View.INVISIBLE);
                        intelligent_load.setVisibility(View.VISIBLE);
                        intelligent_error.setVisibility(View.INVISIBLE);
                        intelligent_none.setVisibility(View.INVISIBLE);
                        loadingDrawable.start();

                       accessWebData();
                    }

                    @Override
                    public void negativeCallback() {

                    }
                },app.getDevices());
                contentDialog.show();
                break;
            case R.id.batch_in:
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent_batch = new Intent(Intent.ACTION_GET_CONTENT);
                    intent_batch.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent_batch.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent_batch, 1);
                }
                break;
            case R.id.lin_btn:
                pager=0;
                if (lin_et_put.getText().toString().trim().length()>0){
                    params.put("keyword",lin_et_put.getText().toString().trim());
                }else {
                    if (params.containsKey("keyword")){
                        params.remove("keyword");
                    }
                }
                accessWebData();
                break;
        }
    }

    /**
     * 文件夹获取excl表的返回回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String filePath = uri.toString();
            int fileIndex = filePath.indexOf("/");//获取第一个/在整个字符串中的位置
            String files = filePath.substring(fileIndex + 2).trim();
            File file = new File(files);//string转化成File对象
            getFileContent(file);   //点击文本储存到数据库
        } else {
            Toast.makeText(IntelligentActivity.this, "您没有选择任何文件", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 读取指定目录下的所有Excl文件的文件内容
     *
     * @param file
     */
    protected void getFileContent(final File file) {
        final Thread thr = new Thread() {
            @Override
            public void run() {
                super.run();
                if (file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
                    Log.i("zeng", "The File doesn't not exist "
                            + file.getName().toString() + file.getPath().toString());
                } else {
                    if (file.getName().endsWith(".xls")) {
                        myHandler.sendEmptyMessage(LOADING_LOMSG);
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            if (fileInputStream != null) {
                                Workbook workbook = Workbook.getWorkbook(fileInputStream);
                                workbook.getNumberOfSheets();
                                //获得第一个工作对象表
                                Sheet sheet = workbook.getSheet(0);
                                int rows = sheet.getRows();
                                //必须对其实例化
                                keylist = new ArrayList<>();
                                textlist = new ArrayList<>();
                                //添加数据库
                                for (int i = 0; i < rows; i++) {
                                    String name = (sheet.getCell(0, i)).getContents();
                                    keylist.add(name);
                                    String number = (sheet.getCell(1, i)).getContents();
                                    textlist.add(number);
                                }
                                batchupload(keylist, textlist);
                                workbook.close();
                            }
                        } catch (IOException e) {
                            Log.i("123", "IOException");
                            e.printStackTrace();
                        } catch (BiffException e) {
                            Log.i("123", "BiffException ");
                            e.printStackTrace();
                        }
                        //myHandler.sendEmptyMessage(IMPORT_LOMSG);
                    } else {
                        Toast.makeText(IntelligentActivity.this, "文件格式不对，请选择.xls文件！", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        thr.start();

    }

    /**
     * 批量关键字和内容上传网络
     *
     * @param key
     * @param content
     */
    private void upload(String key, String content) {

        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        creatime = formatter.format(curDate);
        upLoadThread(key, content);
    }

    /**
     * 批量关键字和内容上传网络
     *
     * @param keylist
     * @param textlist
     */
    private void batchupload(List<String> keylist, List<String> textlist) {
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        creatime = formatter.format(curDate);
        Log.i("123", keylist.size()+":"+textlist.size());
        batchLoadThread(keylist, textlist);
    }

    /**
     * 更新数据库的方法
     *
     * @param key
     * @param content
     */
    public void upDate(String key, String content) {
        myHandler.sendEmptyMessage(showUpdate);
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        creatime = formatter.format(curDate);
        upDateThread(key, content);
    }

    /**
     * 上传数据
     */
    public void upLoadThread(final String keyword, final String content) {
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(textUrl)
                        .addParams("keyword", keyword)
                        .addParams("text", content)
                        .addParams("creatime", creatime)
                        .addParams("type", type)
                        .addParams("devices",MainActivity.getSerialNumber())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(addFalut);
                            }

                            @Override
                            public void onResponse(String response, int id) {

                                Message message = myHandler.obtainMessage();
                                mNumber++;
                                message.what = addSuccess;
                                message.obj = MyJsonUtil.getBeanByJson(response);
                                myHandler.sendMessage(message);
                            }
                        });
            }
        };
        th.start();
    }

    /**
     * 批量上传
     */
    public void batchLoadThread(final List<String> keylist, final List<String> textlist) {
        Thread th = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < keylist.size(); i++) {
                    OkHttpUtils
                            .post()
                            .url(textUrl)
                            .addParams("devices",app.getDevices())
                            .addParams("keyword", keylist.get(i))
                            .addParams("text", textlist.get(i))
                            .addParams("creatime", creatime)
                            .addParams("type", type)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    myHandler.sendEmptyMessage(addFalut);
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.i("123",response);
                                    if (MyJsonUtil.getBeanByJson(response)) {
                                        mBatchNumber++;
                                        myHandler.sendEmptyMessage(IMPORT_LOMSG);
                                        Log.d(TAG, "onResponse: " + mBatchNumber);
                                    }
                                }
                            });

                }
            }
        };
        th.start();
    }

    /**
     * 更新数据
     */
    public void upDateThread(final String keyword, final String content) {
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(textUrl)
                        .addParams("keyword", keyword)
                        .addParams("text", content)
                        .addParams("creatime", creatime)
                        .addParams("type", type)
                        .addParams("devices",MainActivity.getSerialNumber())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(UPDATEFAULT);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Message message = myHandler.obtainMessage();
                                message.what = UPDATESUCCESS;
                                message.obj = MyJsonUtil.getBeanByJson(response);
                                myHandler.sendMessage(message);
                            }
                        });
            }
        };
        th.start();
    }

    /**
     * 删除数据
     */
    public void deleteThread(final String key, final String content) {
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(deleteTextUrl)
                        .addParams("keyword", key)
                        .addParams("text", content)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(deleteFalse);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Message message = myHandler.obtainMessage();
                                message.what = deleteSuccess;
                                message.obj = MyJsonUtil.getBeanByJson(response);
                                myHandler.sendMessage(message);

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

        params.put("devices",app.getDevices());
        params.put("pager",pager+"");
        params.put("row",row+"");
        Log.i("123",new Gson().toJson(params));
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
                                myHandler.sendEmptyMessage(BACKFALUT);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    intelligentBean = MyJsonUtil.getBeanByJson(response,IntelligentUpdateBean.class);
//                                    myArrayList.clear();
//                                    shoporderMap.clear();
//                                    for (IntelligentUpdateBean.VicCodetextBean i : intelligentBean.getVic_codetext()){
//                                            String keyword=i.getKeyword().toString();
//                                            if (!shoporderMap.containsKey(keyword)){
//                                                shoporderMap.put(keyword,new ArrayList<IntelligentUpdateBean.VicCodetextBean>());
//                                                myArrayList.add(shoporderMap.get(keyword));
//
//                                            }
//                                        shoporderMap.get(keyword).add(i);
//                                    }
                                    swipe_refresh_widget.setRefreshing(false);
//                                    Log.i("123",new Gson().toJson(shoporderMap));
//                                    Log.i("123",new Gson().toJson(myArrayList));
                                    myHandler.sendEmptyMessage(BACK);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    myHandler.sendEmptyMessage(BACKNONE);
                                }
                            }
                        });
            }
        };
        thr.start();
    }
    /**
     * 接收数据
     */
    private void loadMoreData() {
        Log.i("123",app.getDevices());
        params.put("devices",app.getDevices());
        params.put("pager",pager+"");
        params.put("row",row+"");
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
                                myHandler.sendEmptyMessage(BACKFALUT);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {

                                    intelligentBean = MyJsonUtil.getBeanByJson(response,IntelligentUpdateBean.class);
//                                    myArrayList.clear();
//                                    shoporderMap.clear();
//                                    for (IntelligentUpdateBean.VicCodetextBean i : intelligentBean.getVic_codetext()){
//                                        String keyword=i.getKeyword().toString();
//                                        if (!shoporderMap.containsKey(keyword)){
//                                            shoporderMap.put(keyword,new ArrayList<IntelligentUpdateBean.VicCodetextBean>());
//                                            myArrayList.add(shoporderMap.get(keyword));
//
//                                        }
//                                        shoporderMap.get(keyword).add(i);
//                                    }

                                    swipe_refresh_widget.setRefreshing(false);
//                                    Log.i("123",new Gson().toJson(shoporderMap));
//                                    Log.i("123",new Gson().toJson(myArrayList));
                                    myHandler.sendEmptyMessage(LOADMORE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    myHandler.sendEmptyMessage(BACKNONE);
                                }
                            }
                        });
            }
        };
        thr.start();
    }
    //把Json格式的字符串转换成对应的模型对象
    public IntelligentBean processData(String json) {
        Gson gson = new Gson();
        IntelligentBean intelligentBean = gson.fromJson(json, IntelligentBean.class);
        return intelligentBean;
    }

}


