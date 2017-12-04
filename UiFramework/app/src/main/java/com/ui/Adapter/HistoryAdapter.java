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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq944 on 2017/11/3.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    private List<String> keywordList = new ArrayList<String>();
    private Context context;

    private HistoryAdapter.OnItemClickListener mOnItemClickListener = null;

    public HistoryAdapter(List<String> keywordList, Context context) {
        this.keywordList = keywordList;
        this.context = context;
    }
    public HistoryAdapter() {

    }
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_history_item, parent, false);
        HistoryAdapter.MyViewHolder holder = new HistoryAdapter.MyViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(final HistoryAdapter.MyViewHolder holder, final int position) {
        holder.tv.setText(keywordList.get(keywordList.size()-position-1));
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
        return keywordList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_history);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    public void setOnItemClickListener(HistoryAdapter.OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }
}
