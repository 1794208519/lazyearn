package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Interface.View.AdapterCallback;
import com.Interface.View.OnItemOnclickCallback;
import com.entity.IntelligentUpdateBean;
import com.uidemo.R;

import java.util.ArrayList;

/**
 * Created by Eren on 2017/5/22.
 */
public class IntelligentDialogAdapter extends RecyclerView.Adapter {

    //关键词集合，用于适配器加载数据
    private ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>> myArrayList = new ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>>();
    private Context context;

    public IntelligentDialogAdapter(ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>> myArrayListt, Context context) {
        this.myArrayList=myArrayListt;
        this.context = context;

    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.intelligent_dialog_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (myArrayList!=null&&myArrayList.size()>0) {
            myViewHolder.intelligent_item_text.setText("回复" + (position + 1) + "：" + myArrayList.get(0).get(position).getText());
        }
    }

    @Override
    public int getItemCount() {
        if (myArrayList!=null&&myArrayList.size()>0) {
            if (myArrayList.get(0).size() >= 5) {
                return 5;
            }
            return myArrayList.get(0).size();
        }else {
            return 0;
        }
    }
    public void notifyChanged(ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>> myArrayListt){
        this.myArrayList=myArrayListt;
        notifyDataSetChanged();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView intelligent_item_text;

        public MyViewHolder(View view) {
            super(view);
            intelligent_item_text= (TextView) view.findViewById(R.id.intelligent_item_text);
        }
    }

}
