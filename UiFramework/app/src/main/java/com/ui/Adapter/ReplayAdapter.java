package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.Interface.View.AdapterCallback;
import com.entity.IntelligentUpdateBean;
import com.uidemo.R;

import java.util.ArrayList;

/**
 * Created by admin on 2017/10/27.
 */
public class ReplayAdapter extends RecyclerView.Adapter{
    private ArrayList<IntelligentUpdateBean.VicCodetextBean> myArrayList = new ArrayList<IntelligentUpdateBean.VicCodetextBean>();
    private Context context;
    private AdapterCallback adapterCallback;

    public ReplayAdapter(ArrayList<IntelligentUpdateBean.VicCodetextBean> myArrayListt, Context context, AdapterCallback adapterCallback) {
        this.myArrayList=myArrayListt;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }
    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.intelligent_dialog_list_text, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.key.setText("回复"+(myViewHolder.getPosition()+1)+"：" + myArrayList.get(position).getText());

        Log.e("998", "回复：" + myArrayList.get(position).getText());


    }
    @Override
    public int getItemCount() {
        if (myArrayList.size()>=5){
            return 5;
        }
        return myArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView key;


        public MyViewHolder(View view) {
            super(view);
            key = (TextView) view.findViewById(R.id.intelligent_item_text);

        }
    }



}
