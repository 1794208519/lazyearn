package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.uidemo.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by vicmob_yf002 on 2017/6/21.
 * 自定义加载对话框，设置了加载样式的多样化
 */
public class LoadingDialog extends Dialog {
    private TextView content;
    private AVLoadingIndicatorView avi;

    public LoadingDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_load, null);
        avi = (AVLoadingIndicatorView) mView.findViewById(R.id.avi);
        content = (TextView) mView.findViewById(R.id.dialog_customer_title);
        super.setContentView(mView);
    }

    /**
     * 设置内容
     *
     * @param s
     * @return
     */
    public LoadingDialog setContent(String s) {
        content.setText(s);
        return this;
    }


    /**
     * 开始显示动画
     */
    private void startAnim() {
        avi.show();
    }

    /**
     * 隐藏动画
     */
    private void hideAnim() {
        avi.hide();
    }

    /**
     * 设置加载样式
     *
     * @param index
     * @return
     */
    public LoadingDialog setIndicator(int index) {
        avi.setIndicator(INDICATORS[index]);
        return this;
    }

    @Override
    public void show() {
        super.show();
        startAnim();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        hideAnim();
    }

    /**
     * 加载样式数组
     */
    private static final String[] INDICATORS = new String[]{
            "BallPulseIndicator",
            "BallGridPulseIndicator",
            "BallClipRotateIndicator",
            "BallClipRotatePulseIndicator",
            "SquareSpinIndicator",
            "BallClipRotateMultipleIndicator",
            "BallPulseRiseIndicator",
            "BallRotateIndicator",
            "CubeTransitionIndicator",
            "BallZigZagIndicator",
            "BallZigZagDeflectIndicator",
            "BallTrianglePathIndicator",
            "BallScaleIndicator",
            "LineScaleIndicator",
            "LineScalePartyIndicator",
            "BallScaleMultipleIndicator",
            "BallPulseSyncIndicator",
            "BallBeatIndicator",
            "LineScalePulseOutIndicator",
            "LineScalePulseOutRapidIndicator",
            "BallScaleRippleIndicator",
            "BallScaleRippleMultipleIndicator",
            "BallSpinFadeLoaderIndicator",
            "LineSpinFadeLoaderIndicator",
            "TriangleSkewSpinIndicator",
            "PacmanIndicator",
            "BallGridBeatIndicator",
            "SemiCircleSpinIndicator",
            "com.wang.avi.sample.MyCustomIndicator"
    };
}
