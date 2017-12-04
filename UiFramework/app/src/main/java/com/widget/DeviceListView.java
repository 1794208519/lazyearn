package com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Eren on 2017/5/8.
 * 自定义listview，防止scrollview嵌套listview的冲突
 */
public class DeviceListView extends ListView {
    public DeviceListView(Context context) {
        super(context);
    }

    public DeviceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeviceListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
