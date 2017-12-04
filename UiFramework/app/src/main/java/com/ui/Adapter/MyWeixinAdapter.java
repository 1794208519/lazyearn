package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uidemo.R;
import com.utils.Urlutils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq944 on 2017/10/13.
 */

public class MyWeixinAdapter extends RecyclerView.Adapter<MyWeixinAdapter.MyViewHolder> {
    private List<String> weixinhaoList = new ArrayList<String>();
    private Context context;

    private OnItemClickListener mOnItemClickListener = null;

    public MyWeixinAdapter(List<String> weixinhaoList, Context context) {
        this.weixinhaoList = weixinhaoList;
        this.context = context;
    }

    public MyWeixinAdapter() {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weixinhao_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(weixinhaoList.get(position));
        String iconName = "";
        try {
            iconName = java.net.URLEncoder.encode(weixinhaoList.get(position), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = Urlutils.myweixiniconUrl + "icon_" + iconName + ".jpg";
        Log.i("zw1014", url);
        setIcon(holder.iv, url);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return weixinhaoList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_weixinhao_item);
            iv = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setIcon(ImageView iv, String url) {
        ImageOptions options = new ImageOptions.Builder()
                .setFailureDrawableId(R.mipmap.weixinicon).build();
        x.image().bind(iv, url, options);
    }
}
