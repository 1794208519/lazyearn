package com.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.uidemo.R;
import com.utils.ToastUtils;
import com.utils.Urlutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

/**
 * Created by vicmob_yf002 on 2017/5/18.
 */
public class CodeActivity extends BaseActivity implements View.OnClickListener {
    //加载动画图片
    private ImageView imageView;
    //返回按键
    private ImageView mBack;
    //刷新按键
    private TextView refreshCode;
    //图片资源
    static Bitmap bitmap = null;
    //连接图片资源信息
    private static final int IMAGEMSG = 4;
    //连接图片资源失败
    private static final int FALSE = 5;
    //连接成功
    private static final int SUCCESS = 6;
    private View mView;
    //动画
    AnimationDrawable loadingDrawable;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
                                    setContentView(R.layout.activity_code);
                                    //初始化图片
                                    imageView = (ImageView) findViewById(R.id.image_code);
                                    if(bitmap != null){
                                        imageView.setImageBitmap(bitmap);
                                    }
                                    ToastUtils.showShort(CodeActivity.this,"获取成功");
                                    //初始化按钮键
                                    mBack = (ImageView) findViewById(R.id.code_back);
                                    refreshCode = (TextView) findViewById(R.id.code_refresh);
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
                                    setContentView(R.layout.loading_error);
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

    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_load;
    }

    @Override
    public void findView() {
        //加载数据
        loadContent();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_back:
                finish();
                break;
            case R.id.code_refresh:
                //重新加载数据
                loadContent();
                break;
        }
    }

    private void initlistener() {
        //初始化监听
        mBack.setOnClickListener(this);
        refreshCode.setOnClickListener(this);
    }

    /**
     * 加载数据
     */
    private void loadContent() {
        setContentView(R.layout.activity_load);
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

    /**
     * 解析图片相关线程相关接口
     */
    public class CodePicRunnable implements Runnable {
        @Override
        public void run() {
            Socket socket = null;
            String socketUrl = Urlutils.getWebUrl();
            String codeUrl = Urlutils.getCodeUrl();
            try {
                socket = new Socket(socketUrl, 31100);
                String strVal = "1"+";"+getSerialNumber();
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
                myHandler.sendEmptyMessage(SUCCESS);

//                dataInput.close();
            } catch (IOException e) {
                e.printStackTrace();
                myHandler.sendEmptyMessage(FALSE);
            } finally {
                try {
                    socket.close();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    myHandler.sendEmptyMessage(FALSE);
                } catch (Exception e) {
                    e.printStackTrace();
                    myHandler.sendEmptyMessage(FALSE);
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
        loadContent();
    }
    public String getSerialNumber(){

        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }
}
