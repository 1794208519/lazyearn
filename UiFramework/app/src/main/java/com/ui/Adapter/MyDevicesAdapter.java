package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Interface.View.MyItemClickListener;
import com.uidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/7/1.
 */
public class MyDevicesAdapter extends RecyclerView.Adapter {
    public List<String> list = new ArrayList<>();
    private Context context;
    private MyItemClickListener mItemClickListener;

    public MyDevicesAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.right_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener);
        return viewHolder;
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvDevice.setText("设备"+(position+1));

        viewHolder.tvDeviceid.setText("("+list.get(position).trim().substring(0,3)+"**"+list.get(position).trim().substring(list.get(position).trim().length()-3,list.get(position).trim().length())+")");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvDevice;
        public TextView tvDeviceid;
        public ImageView ivLight;
        private MyItemClickListener mListener;

        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            tvDevice = (TextView) itemView.findViewById(R.id.tv_device);
            tvDeviceid = (TextView) itemView.findViewById(R.id.tv_deviceid);
            ivLight = (ImageView) itemView.findViewById(R.id.iv_light);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
}
