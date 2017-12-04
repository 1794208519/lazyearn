package com.crash;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by admin on 2017/10/23.
 */
public class CrashApplication extends Application {




@Override
public void onCreate() {
    super.onCreate();
    CrashHandler crashHandler = CrashHandler.getInstance();
    crashHandler.init(getApplicationContext());
    Cockroach.install(new Cockroach.ExceptionHandler() {

        // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

        @Override
        public void handlerException(final Thread thread, final Throwable throwable) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("Cockroach", thread + "\n" + throwable.toString());
                        throwable.printStackTrace();
//                        Toast.makeText(CrashApplication.this, "Exception Happend\n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
//                        throw new RuntimeException("..."+(i++));
                    } catch (Throwable e) {

                    }
                }
            });
        }
    });

    // 卸载代码
//        Cockroach.uninstall();
}
}
