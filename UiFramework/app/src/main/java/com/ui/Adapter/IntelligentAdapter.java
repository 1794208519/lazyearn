package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.Interface.View.AdapterCallback;
import com.Interface.View.OnItemOnclickCallback;
import com.Interface.View.loadMoreCallback;
import com.entity.IntelligentUpdateBean;
import com.uidemo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eren on 2017/5/22.
 */
public class IntelligentAdapter extends RecyclerView.Adapter {
    private int iteem_footer = 1, item_list = 2,item_header=3;
    //关键词集合，用于适配器加载数据
    private Context context;
    private OnItemOnclickCallback adapterCallback;
    private loadMoreCallback loadMoreCallback;
    private ReplayAdapter replayAdapter;
    IntelligentUpdateBean intelligentUpdateBean = new IntelligentUpdateBean();
    private HashMap<String, ArrayList<IntelligentUpdateBean.VicCodetextBean>> shoporderMap = new HashMap<String, ArrayList<IntelligentUpdateBean.VicCodetextBean>>();

    private ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>> myArrayList = new ArrayList<ArrayList<IntelligentUpdateBean.VicCodetextBean>>();
    ArrayList<IntelligentUpdateBean.VicCodetextBean> listdata=new ArrayList<IntelligentUpdateBean.VicCodetextBean>();

    public IntelligentAdapter(IntelligentUpdateBean intelligentUpdateBean , Context context, OnItemOnclickCallback adapterCallback,loadMoreCallback loadMoreCallback) {
        this.intelligentUpdateBean=intelligentUpdateBean;
        this.context = context;
        this.adapterCallback = adapterCallback;
        this.loadMoreCallback=loadMoreCallback;
        if (intelligentUpdateBean.getVic_codetext()!=null&&intelligentUpdateBean.getVic_codetext().size()>0) {
            listdata.addAll(intelligentUpdateBean.getVic_codetext());
            for (IntelligentUpdateBean.VicCodetextBean i : listdata) {
                String keyword = i.getKeyword().toString();
                if (!shoporderMap.containsKey(keyword)) {
                    shoporderMap.put(keyword, new ArrayList<IntelligentUpdateBean.VicCodetextBean>());
                    myArrayList.add(shoporderMap.get(keyword));

                }
                shoporderMap.get(keyword).add(i);
            }
        }
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == iteem_footer) {
            return new FootViewHolder(getLayoutInflater().inflate(R.layout.layout_footer_text, parent, false));
        }if (viewType == item_header){
            return new HeaderViewHolder(getLayoutInflater().inflate(R.layout.layout_header_text, parent, false));
        } else {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.intelligent_list, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof FootViewHolder) {
            FootViewHolder myViewHolder = (FootViewHolder) holder;

        }else if (holder instanceof HeaderViewHolder){
            HeaderViewHolder myViewHolder = (HeaderViewHolder) holder;
        }else {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.key.setText("关键词：" + myArrayList.get(position).get(0).getKeyword());
            Log.e("1233", "1111" + myArrayList.get(position).get(0).getKeyword() + "  " + position);

            replayAdapter = new ReplayAdapter(myArrayList.get(position), context, new AdapterCallback() {
                @Override
                public void deletCallback(String keyword, String content) {

                }

                @Override
                public void updateCallback(String keyword, String content) {

                }
            });
            myViewHolder.content.setAdapter(replayAdapter);
            myViewHolder.linear_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallback.OnItemClick(v, position,myArrayList.get(position));
                }
            });
        }
    }

    public void loadMorenotifyDataChanged(IntelligentUpdateBean intelligentUpdateBean) {
        this.intelligentUpdateBean=intelligentUpdateBean;
        if (intelligentUpdateBean.getVic_codetext()!=null&&intelligentUpdateBean.getVic_codetext().size()>0) {
            shoporderMap.clear();
            myArrayList.clear();
            listdata.addAll(intelligentUpdateBean.getVic_codetext());
            for (IntelligentUpdateBean.VicCodetextBean i : listdata) {
                String keyword = i.getKeyword().toString();
                if (!shoporderMap.containsKey(keyword)) {
                    shoporderMap.put(keyword, new ArrayList<IntelligentUpdateBean.VicCodetextBean>());
                    myArrayList.add(shoporderMap.get(keyword));

                }
                shoporderMap.get(keyword).add(i);
            }
            notifyDataSetChanged();
        }
    }
    public void notifyDataChanged(IntelligentUpdateBean intelligentUpdateBean) {
        this.intelligentUpdateBean=intelligentUpdateBean;
        if (intelligentUpdateBean.getVic_codetext()!=null&&intelligentUpdateBean.getVic_codetext().size()>0) {
            listdata.clear();
            shoporderMap.clear();
            myArrayList.clear();
            listdata.addAll(intelligentUpdateBean.getVic_codetext());
            for (IntelligentUpdateBean.VicCodetextBean i : listdata) {

                String keyword = i.getKeyword().toString();
                if (!shoporderMap.containsKey(keyword)) {
                    shoporderMap.put(keyword, new ArrayList<IntelligentUpdateBean.VicCodetextBean>());
                    myArrayList.add(shoporderMap.get(keyword));

                }
                shoporderMap.get(keyword).add(i);
            }
            notifyDataSetChanged();
        }else if (intelligentUpdateBean.getVic_codetext()!=null&&intelligentUpdateBean.getVic_codetext().size()==0){
            listdata.clear();
            shoporderMap.clear();
            myArrayList.clear();
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        if (myArrayList!=null&&myArrayList.size() == 0&&intelligentUpdateBean.getVic_codetext()==null) {
             return  0;
        }else if (intelligentUpdateBean.getVic_codetext()!=null&&intelligentUpdateBean.getVic_codetext().size() == 20){
            return myArrayList.size()+1;
        }else if (intelligentUpdateBean.getVic_codetext()!=null&&intelligentUpdateBean.getVic_codetext().size() < 20){
            return myArrayList.size()+1;
        }
        return myArrayList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (intelligentUpdateBean.getVic_codetext()!=null&&intelligentUpdateBean.getVic_codetext().size() == 20&&(position+1)==getItemCount()) {
            return iteem_footer;
        }else if(intelligentUpdateBean.getVic_codetext()!=null&&intelligentUpdateBean.getVic_codetext().size() < 20&&(position+1)==getItemCount()){
            return  item_header;
        }
            return item_list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView key;
        RecyclerView content;
        LinearLayout linear_main;


        public MyViewHolder(View view) {
            super(view);
            linear_main = (LinearLayout) view.findViewById(R.id.linear_main);
            key = (TextView) view.findViewById(R.id.intelligent_itemkey);
            content = (RecyclerView) view.findViewById(R.id.intelligent_itemcontent);
            content.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        TextView footer_text;


        public FootViewHolder(View view) {
            super(view);
            footer_text = (TextView) view.findViewById(R.id.footer_text);
        }
    }
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView footer_text;


        public HeaderViewHolder(View view) {
            super(view);
            footer_text = (TextView) view.findViewById(R.id.footer_text);
        }
    }
}
