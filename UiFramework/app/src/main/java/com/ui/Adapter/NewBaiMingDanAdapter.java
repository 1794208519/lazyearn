package com.ui.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.uidemo.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qq944 on 2017/10/21.
 */

public class NewBaiMingDanAdapter extends RecyclerView.Adapter<NewBaiMingDanAdapter.MyViewHolder> implements View.OnClickListener{
    private List<String> weixinhaoList = new ArrayList<String>();
    private Context context;

    private Boolean isuse;
    private Map<String,Boolean> map = new HashMap<String, Boolean>();

    public Map<String, Boolean> getMap() {
        return map;
    }

    public void setMap(Map<String, Boolean> map) {
        this.map = map;
    }


   // private NewBaiMingDanAdapter.OnItemClickListener mOnItemClickListener = null;
   private OnItemClickListener mOnItemClickListener = null;

    public NewBaiMingDanAdapter(List<String> weixinhaoList, Context context,Map<String,Boolean> map) {
        this.weixinhaoList = weixinhaoList;
        this.context = context;
        this.map = map;
    }
    public NewBaiMingDanAdapter() {

    }

    @Override
    public NewBaiMingDanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_baimingdan, parent, false);
        NewBaiMingDanAdapter.MyViewHolder holder = new NewBaiMingDanAdapter.MyViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewBaiMingDanAdapter.MyViewHolder holder, final int position) {
        holder.tv.setText(weixinhaoList.get(position));
       /* if(biaoming!=null){
            if (biaoming.equals(weixinhaoList.get(position))){
                map.put(weixinhaoList.get(position),true);
                holder.rl_biao.setBackgroundColor(Color.parseColor("#1296db"));
            }
        }*/
       if (map.get(weixinhaoList.get(position))){
           holder.tv.setTextColor(Color.parseColor("#1296db"));
       }else {
           holder.tv.setTextColor(Color.parseColor("#000000"));
       }
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return weixinhaoList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private final RelativeLayout rl_biao;
        TextView tv;
        ImageView iv;
        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_biao);
            iv = (ImageView) view.findViewById(R.id.iv_biao);
            rl_biao = ((RelativeLayout) view.findViewById(R.id.rl_biao));
        }
    }
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
