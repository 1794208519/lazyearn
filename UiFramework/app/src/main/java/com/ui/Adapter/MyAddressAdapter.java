package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Interface.View.AccountCallbak;
import com.entity.Addresstimebean;
import com.ui.view.swipedeletelayout.SwipeLayoutManager;
import com.uidemo.R;

import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/6/28.
 */
public class MyAddressAdapter extends RecyclerView.Adapter {
    private List<Addresstimebean> addressBeen;
    private Context context;
    private AccountCallbak callback;

    public MyAddressAdapter(List<Addresstimebean> addressBeen, Context context, AccountCallbak callback) {
        this.addressBeen = addressBeen;
        this.context = context;
        this.callback = callback;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.address_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.tv.setText(addressBeen.get(position).getAddress());
        myViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.positiveCallbak(position);
                //删除bean里的值
                addressBeen.remove(position);
                //这俩句至关重要，实现了删除条目时的过度效果
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, addressBeen.size());
                SwipeLayoutManager.create().closeLayout();
            }
        });

    }


    @Override
    public int getItemCount() {
        return addressBeen.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView tv_delete;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_name);
            tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        }
    }
}
