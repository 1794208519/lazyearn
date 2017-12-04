package com.ui.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.entity.LiaoTianJiLuBean;
import com.uidemo.R;
import com.utils.MyStringUtil;
import com.utils.Urlutils;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by qq944 on 2017/10/14.
 */

public class MyLiaoTianJiLuAdpter extends RecyclerView.Adapter<MyLiaoTianJiLuAdpter.MyViewHolder> {
    private List<LiaoTianJiLuBean> weixinhaoList = new ArrayList<LiaoTianJiLuBean>();
    private Context context;

    private MyLiaoTianJiLuAdpter.OnItemClickListener mOnItemClickListener = null;

    public MyLiaoTianJiLuAdpter(List<LiaoTianJiLuBean> weixinhaoList, Context context) {
        this.weixinhaoList = weixinhaoList;
        this.context = context;
    }

    public MyLiaoTianJiLuAdpter() {

    }

    @Override
    public MyLiaoTianJiLuAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.liaotianliebiao_item, parent, false);
        MyLiaoTianJiLuAdpter.MyViewHolder holder = new MyLiaoTianJiLuAdpter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyLiaoTianJiLuAdpter.MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        //Log.i("zw1016",weixinhaoList.get(position));
        LiaoTianJiLuBean liaoTianJiLuBean = weixinhaoList.get(weixinhaoList.size() - position - 1);
        String name = null;
        String liaotianflag = liaoTianJiLuBean.getLiaotianflag();
        if (liaotianflag.equals("0")) {
            name = liaoTianJiLuBean.getLiaotianzhe();
        } else if (liaotianflag.equals("1")) {
            name = liaoTianJiLuBean.getLiaotianhaoyou();
        } else {
            name = liaotianflag;
        }
        String liaotianshijian = liaoTianJiLuBean.getLiaotianshijian();
        String liaotianneirong = liaoTianJiLuBean.getLiaotianneirong();
        Log.i("123", liaotianneirong + ":");
        if (liaotianneirong.length() >= 4 && liaotianneirong.substring(0, 4).equals("img_")) {
            holder.iv_liaotiantupian.setImageResource(R.mipmap.morentupian);
            String tag = Urlutils.liaoTianTuPianUrl + liaotianneirong;
            //holder.iv_liaotiantupian.setTag(tag);
               /* ImageOptions options = new ImageOptions.Builder()
                        //设置使用缓存
                        .setUseMemCache(true)
                        .setLoadingDrawableId(R.mipmap.weixinicon)
                        .setFailureDrawableId(R.mipmap.weixinicon)
                        .build();*/
            //Log.i("123",(String) holder.iv_liaotiantupian.getTag());
//                holder.iv_liaotiantupian.setImageBitmap(null);
            //x.image().bind(holder.iv_liaotiantupian, (String) holder.iv_liaotiantupian.getTag(),options);
            Glide.with(context)
                    .load(tag)
                    .placeholder(R.mipmap.morentupian)
                    .crossFade()
                    .into(holder.iv_liaotiantupian);
            holder.tv.setVisibility(View.GONE);
        } else {
            holder.tv.setText("    " + liaotianneirong);
            holder.iv_liaotiantupian.setVisibility(View.GONE);
        }

        holder.tv_name_liaotianjilu.setText(name + liaotianshijian);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, weixinhaoList.size() - position - 1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return weixinhaoList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name_liaotianjilu;
        private final ImageView iv_liaotiantupian;
        private final TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_liaotianliebiao);
            tv_name_liaotianjilu = ((TextView) view.findViewById(R.id.tv_name_liaotianjilu));
            iv_liaotiantupian = ((ImageView) view.findViewById(R.id.iv_liaotiantupian));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(MyLiaoTianJiLuAdpter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
