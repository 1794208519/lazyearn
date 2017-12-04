package com.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Eren on 2017/4/28.
 */
public class MyVideoView extends VideoView {

    private int mWidthSize;
    private int mWidthMode;
    private int mHeightSize;
    private int mHeightMode;

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        mHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (mWidthMode == MeasureSpec.EXACTLY && mHeightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(mWidthSize,mHeightSize);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
