package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Interface.View.AccountCallbak;
import com.entity.AccountBean;
import com.entity.AccountUpdateBean;
import com.ui.view.swipedeletelayout.SwipeLayoutManager;
import com.uidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/6/30.
 */
public class MyAccmanagerAdapter extends RecyclerView.Adapter {
    private List<AccountUpdateBean.AutoAccountBean> accountBeanList = new ArrayList<>();
    private Context context;
    private AccountCallbak accountCallbak;

    public MyAccmanagerAdapter(List<AccountUpdateBean.AutoAccountBean> accountBeanList, Context context, AccountCallbak accountCallbak) {
        this.accountBeanList = accountBeanList;
        this.context = context;
        this.accountCallbak = accountCallbak;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.account_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.account.setText(accountBeanList.get(position).getAccount());
//        myViewHolder.password.setText(accountBeanList.get(position).getPassword());
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountCallbak.positiveCallbak(position);
                accountBeanList.remove(position);
                //这俩句至关重要，实现了删除条目时的过度效果
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, accountBeanList.size());
                SwipeLayoutManager.create().closeLayout();
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView account;
        private TextView password;
        private TextView delete;


        public MyViewHolder(View itemView) {
            super(itemView);
            account = (TextView) itemView.findViewById(R.id.account_item_text);
//            password = (TextView) itemView.findViewById(R.id.password_item_text);
            delete = (TextView) itemView.findViewById(R.id.account_item_delete);
        }
    }
}
