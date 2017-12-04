package com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.uidemo.R;
import com.utils.ToastUtils;
import com.utils.Urlutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Twisted
 * created on: 2017/10/13 9:06
 * description:
 */

public class DeleteDeadFriendsActivity extends BaseActivity implements View.OnClickListener {


    private static Bitmap bitmap;
    @BindView(R.id.rl_title) RelativeLayout rlTitle;
    @BindView(R.id.code_back) ImageView codeBack;
    @BindView(R.id.code_refresh) TextView codeRefresh;
    @BindView(R.id.loading_img) ImageView loadingImg;
    @BindView(R.id.loading_error_screen) LinearLayout loadingErrorScreen;
    //连接成功
    private static final int SUCCESS = 1;
    //连接图片资源失败
    private static final int FALSE = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
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
                                    setContentView(R.layout.activity_deadfriendscode);
                                    //初始化图片
                                    imageView = (ImageView) findViewById(R.id.image_code);
                                    if (bitmap != null) {
                                        imageView.setImageBitmap(bitmap);
                                    }
                                    ToastUtils.showShort(DeleteDeadFriendsActivity.this, "获取成功");
                                    //初始化按钮键
                                    mBack = (ImageView) findViewById(R.id.code_back);
                                    refreshCode = (TextView) findViewById(R.id.code_refresh);
                                   findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent=new Intent(DeleteDeadFriendsActivity.this,ShowDeadFriendDataActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    initlistener();
                                }
                            });
                        }
                    }).start();
                    break;
                case FALSE:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //模拟加载数据
                            try {
                                Thread.sleep(1200);
                            } catch (InterruptedException e) {
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setContentView(R.layout.activity_deletefriendsfalse);
                                    //初始化按钮键
                                    mBack = (ImageView) findViewById(R.id.code_back);
                                    refreshCode = (TextView) findViewById(R.id.code_refresh);
                                    initlistener();
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
    };
    private ImageView imageView;
    private AnimationDrawable loadingDrawable;
    private ImageView mBack;
    private TextView refreshCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_deletedeadfriends;
    }

    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }

    @Override
    public void findView() {
        GetLoadingData();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void initlistener() {
        //初始化监听
        mBack.setOnClickListener(this);
        refreshCode.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private void GetLoadingData() {
        setContentView(R.layout.activity_deletedeadfriends);
        //开启线程
        CodePicRunnable sr1 = new CodePicRunnable();
        Thread td1 = new Thread(sr1, "td1");
        //多个Thread也可以同时使用一个Runbale，
        //由于多个Thread操作同一个Runnable对象，这样同步锁就需要使用了
        td1.start();
        //显示加载的那个动画
        imageView = (ImageView) findViewById(R.id.loading_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        loadingDrawable.start();
        //初始化按钮键
        mBack = (ImageView) findViewById(R.id.code_back);
        refreshCode = (TextView) findViewById(R.id.code_refresh);
        initlistener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_back:
                finish();
                break;
            case R.id.code_refresh:
                //重新加载数据
                GetLoadingData();
                break;
        }
    }

    /**
     * 解析图片相关线程相关接口
     */
    public class CodePicRunnable implements Runnable {
        @Override
        public void run() {
            Socket socket = null;
            String socketUrl = Urlutils.getWebUrl();
            String codeUrl = Urlutils.getDeadUrl();
            try {
                socket = new Socket(socketUrl, 30100);
                String strVal = "1";
                OutputStream os = socket.getOutputStream();
                os.write(strVal.getBytes());
                // 写关闭,不然会影响读:不然会一直阻塞着,服务器会认为客户端还一直在写数据
                // 由于从客户端发送的消息长度是任意的，客户端需要关闭连接以通知服务器消息发送完毕，如果客户端在发送完最后一个字节后
                // 关闭输出流，此时服务端将知道"我已经读到了客户端发送过来的数据的末尾了,即-1",就会读取出数据并关闭服务端的读数据流,在之后就可以
                // 自己(服务端)的输出流了,往客户端写数据了
                socket.shutdownOutput();
                //访问url获取图片
                try {
                    Thread.sleep(5000);
                    getBitmap(codeUrl);
                    //                    bitmap= ImageLoader.getInstance().loadImageSync(codeUrl);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //                DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                //                int size = dataInput.readInt();
                //                byte[] data = new byte[size];
                //                int len = 0;
                //                while (len < size) {
                //                    len += dataInput.read(data, len, size - len);
                //                }
                //                ByteArrayOutputStream outPut = new ByteArrayOutputStream();
                //                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                //                bmp.compress(Bitmap.CompressFormat.PNG, 100, outPut);
                mHandler.sendEmptyMessage(SUCCESS);

                //                dataInput.close();
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(FALSE);
            } finally {
                try {
                    socket.close();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(FALSE);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(FALSE);
                }
            }
        }
    }

    /**
     * 通过url路径获取图片流
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    /**
     * 加载失败的点击事件
     */
    public void reloadContent(View view) {
        GetLoadingData();
    }
}
