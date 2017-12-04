package com.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.Interface.View.WhiteCallback;
import com.uidemo.R;

/**
 * Created by vicmob_yf002 on 2017/5/11.
 */
public class PopupWindowUtil {
    private static PopupWindowUtil sInstance;
    Context context;
    PopupWindow popupWindow = null;
    WhiteCallback callback;

    public PopupWindowUtil(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public static synchronized PopupWindowUtil getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PopupWindowUtil(context.getApplicationContext());
        }
        return sInstance;
    }

    public PopupWindowUtil(Context context, View v, WhiteCallback _callback) {
        this.context = context;
        this.callback = _callback;
        whitebottomwindow(v, context);
    }

    public void window(View view, Context context) {

        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        LinearLayout layout;
        layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.popup_window, null);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.Popupwindow);//包括进入和退出两个动画
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        //设置消失监听
        popupWindow.setOnDismissListener(new poponDismissListener());
        //popupWindow.showAsDropDown(view);
        setButtonListeners(layout);

    }

    public void bottomwindow(View view, Context context) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.popup_window, null);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        //设置消失监听
        popupWindow.setOnDismissListener(new poponDismissListener());
        popupWindow.setAnimationStyle(R.style.Popupwindow);//包括进入和退出两个动画
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        //popupWindow.showAsDropDown(view);
        setButtonListeners(layout);
    }

    public void whitebottomwindow(View view, Context context) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.white_popup_window, null);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        //设置消失监听
        popupWindow.setOnDismissListener(new poponDismissListener());
        popupWindow.setAnimationStyle(R.style.Popupwindow);//包括进入和退出两个动画
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        backgroundAlpha(0.5f);
        //popupWindow.showAsDropDown(view);
        setWhiteButtonListeners(layout);
    }

    private void setButtonListeners(LinearLayout layout) {
//        Button music = (Button) layout.findViewById(R.id.music);
//        music.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null && popupWindow.isShowing()) {
//                    ToastUtils.showShort(context, "添加");
//                    backgroundAlpha(1f);
//                    popupWindow.dismiss();
//                }
//            }
//        });
//
//        Button movie = (Button) layout.findViewById(R.id.movie);
//        movie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null && popupWindow.isShowing()) {
//                    ToastUtils.showShort(context, "删除");
//                    backgroundAlpha(1f);
//                    popupWindow.dismiss();
//                }
//            }
//        });

        Button cancel = (Button) layout.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    backgroundAlpha(1f);
                    popupWindow.dismiss();
                }
            }
        });

    }

    private void setWhiteButtonListeners(LinearLayout layout) {
        Button cancel = (Button) layout.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    backgroundAlpha(1f);
                    popupWindow.dismiss();
                }
            }
        });
        Button import_table = (Button) layout.findViewById(R.id.import_table);
        import_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    backgroundAlpha(1f);
                    callback.excelCallback();
                    popupWindow.dismiss();
                }
            }
        });
        Button import_directory = (Button) layout.findViewById(R.id.import_directory);
        import_directory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    backgroundAlpha(1f);
                    callback.addressbookCallback();
                    popupWindow.dismiss();
                }
            }
        });
        Button add_white_acc = (Button) layout.findViewById(R.id.add_white_acc);
        add_white_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    backgroundAlpha(1f);
                    callback.addCallback();
                    popupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        Window window = ((Activity) getContext()).getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = bgAlpha;
        window.setAttributes(layoutParams);
    }

    /**
     * 添加新笔记时弹出的popupWindow关闭的事件，主要是为了将背景透明度改回来,同时对返回监听
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }
}
