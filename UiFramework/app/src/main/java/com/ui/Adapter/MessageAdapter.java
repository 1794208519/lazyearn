package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Interface.View.AdapterCallback;
import com.Interface.View.MessageCallback;
import com.entity.IntelligentUpdateBean;
import com.entity.MessageBean;
import com.ui.view.swipedeletelayout.SwipedeleteLayout;
import com.uidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/27.
 */
public class MessageAdapter extends RecyclerView.Adapter{
    private List<MessageBean> myArrayList=new ArrayList<MessageBean>();
    private Context context;
    private MessageCallback adapterCallback;

    public MessageAdapter( List<MessageBean> myArrayList, Context context, MessageCallback adapterCallback) {
        this.myArrayList=myArrayList;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }
    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.message_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.swipeDelete.close();
        myViewHolder.key.setText("回复"+(myViewHolder.getPosition()+1)+"：" + myArrayList.get(position).getText());
        myViewHolder.message_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.updateCallback(myArrayList.get(position).getKeyword(),myArrayList.get(position).getText(),myArrayList.get(position).getCodeTextId()+"");
            }
        });
        myViewHolder.message_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.deletCallback(myArrayList.get(position).getKeyword(),myArrayList.get(position).getText(),myArrayList.get(position).getCodeTextId()+"");
            }
        });
    }
    @Override
    public int getItemCount() {
        if (myArrayList.size()>=5){
            return 5;
        }
        return myArrayList.size();
    }
    public void noitychanged(List<MessageBean> myArrayList){
        this.myArrayList=myArrayList;
        notifyDataSetChanged();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView key;
        TextView message_edit;
        TextView message_delete;
        SwipedeleteLayout swipeDelete;
        public MyViewHolder(View view) {
            super(view);
            swipeDelete= (SwipedeleteLayout) view.findViewById(R.id.swipeDelete);
            key = (TextView) view.findViewById(R.id.intelligent_item_text);
            message_delete= (TextView) view.findViewById(R.id.message_delete);
            message_edit= (TextView) view.findViewById(R.id.message_editt);
        }
    }



}
