package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.entity.LiaoTianJiLuBean;
import com.uidemo.R;
import com.utils.Urlutils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjing on 2017/11/2.
 */

public class ChatRecordListAdapter extends RecyclerView.Adapter {
    private List<LiaoTianJiLuBean> weixinhaoList = new ArrayList<LiaoTianJiLuBean>();
    private Context mContext;
    private static int TYPE_NORMAL = 0;
    private static int TYPE_FOOTER = 1;

    private LayoutInflater mInflater;
    private boolean isLoad;
    private int loadIndex;

    public ChatRecordListAdapter(Context context, List<LiaoTianJiLuBean> data) {
        this.mContext = context;
        this.weixinhaoList = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_NORMAL == viewType) {
            View view = mInflater.inflate(R.layout.liaotianliebiao_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        } else if (TYPE_FOOTER == viewType) {
            View view = mInflater.inflate(R.layout.recy_footer, parent, false);
            FooterViewHolder holder = new FooterViewHolder(view);
            return holder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.i("load123", isLoad + "123");
        if (holder instanceof MyViewHolder) {
            final MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.setIsRecyclable(false);
            //Log.i("zw1016",weixinhaoList.get(position));
            LiaoTianJiLuBean liaoTianJiLuBean = weixinhaoList.get(position);
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
                holder1.iv_liaotiantupian.setImageResource(R.mipmap.morentupian);
                String tag = Urlutils.liaoTianTuPianUrl + liaotianneirong;
                Glide.with(mContext)
                        .load(tag)
                        .placeholder(R.mipmap.morentupian)
                        .crossFade()
                        .into(holder1.iv_liaotiantupian);
                holder1.tv.setVisibility(View.GONE);
            } else {
                holder1.tv.setText("    " + liaotianneirong);
                holder1.iv_liaotiantupian.setVisibility(View.GONE);
            }

            holder1.tv_name_liaotianjilu.setText(name + liaotianshijian);
            if (mOnItemClickListener != null) {
                holder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder1.itemView, position);
                    }
                });
            }
        } else {
            Log.i("load123", isLoad + "456");
//            final FooterViewHolder holder2 = (FooterViewHolder) holder;
            if (isLoad) {
                Log.i("load123", isLoad + "789");
                if(weixinhaoList.size()<15){
                    ((FooterViewHolder) holder).footer.setText("没有更多数据");
                    ((FooterViewHolder) holder).loadBar.setVisibility(View.GONE);
                }
                ((FooterViewHolder) holder).rl_load.setVisibility(View.VISIBLE);
            } else {
                Log.i("load123", isLoad + "4567");
                if (loadIndex == 0) {
                    ((FooterViewHolder) holder).rl_load.setVisibility(View.GONE);
                } else {
                    ((FooterViewHolder) holder).rl_load.setVisibility(View.VISIBLE);
                    ((FooterViewHolder) holder).footer.setText("没有更多数据");
                    ((FooterViewHolder) holder).loadBar.setVisibility(View.GONE);

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return weixinhaoList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
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

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public TextView footer;
        public RelativeLayout rl_load;
        public ProgressBar loadBar;

        public FooterViewHolder(View itemView) {
            super(itemView);

            footer = (TextView) itemView.findViewById(R.id.footer);
            rl_load = (RelativeLayout) itemView.findViewById(R.id.rl_load);
            loadBar = (ProgressBar) itemView.findViewById(R.id.loading_progress);
        }
    }

    private ChatRecordListAdapter.OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(ChatRecordListAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load, int loadIndex) {
        isLoad = load;
        this.loadIndex = loadIndex;
    }

}
