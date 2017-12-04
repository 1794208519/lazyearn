package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Interface.View.AccountCallbak;
import com.entity.AccountUpdateBean;
import com.entity.OperatorsCityBean;
import com.ui.view.swipedeletelayout.SwipeLayoutManager;
import com.uidemo.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/6/28.
 */
public class DeviceAccountAdapter extends RecyclerView.Adapter {
    private List<AccountUpdateBean.AutoAccountBean> autoAccountBeen;
    private Context context;
    private AccountCallbak callback;
    private HashMap<String,Boolean> accountMap=new HashMap<String,Boolean>();
    private int selectnumber;

    public int getSelectnumber() {
        return selectnumber;
    }

    public void setSelectnumber(int selectnumber) {
        this.selectnumber = selectnumber;
    }

    public DeviceAccountAdapter(List<AccountUpdateBean.AutoAccountBean> accountBean, Context context, HashMap<String,Boolean> accountMap, AccountCallbak callback) {
        this.autoAccountBeen = accountBean;
        this.context = context;
        this.accountMap=accountMap;
        this.callback = callback;
    }
    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.adapter_account_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final Integer tag=new Integer(position);//初始化一个Integer实例，其值为position
        myViewHolder.list_re.setBackgroundColor(context.getResources().getColor(R.color.white));
        if (autoAccountBeen.get(position).getDevices()!=null&&!autoAccountBeen.get(position).getDevices().equals("null")&&!autoAccountBeen.get(position).getDevices().equals("")){
            myViewHolder.list_re.setBackgroundColor(0xff10ADE0);
            Log.i("123",getSelectnumber()+"aaa");
            setSelectnumber(getSelectnumber()+1);
        }
        myViewHolder.tv.setText(autoAccountBeen.get(position).getAccount());

        myViewHolder.ck_select.setChecked(false);
        if (accountMap!=null){
            myViewHolder.ck_select.setChecked(accountMap.get(autoAccountBeen.get(tag).getAccountId()+""));
        }
        myViewHolder.ck_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myViewHolder.ck_select.isChecked()){
                    accountMap.put(autoAccountBeen.get(tag).getAccountId()+"",true);
                }else {
                    accountMap.put(autoAccountBeen.get(tag).getAccountId()+"",false);
                }
            }
        });
        myViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.positiveCallbak(position);
                accountMap.put(autoAccountBeen.get(position).getAccountId()+"",false);
                //删除bean里的值
                autoAccountBeen.remove(position);
                //这俩句至关重要，实现了删除条目时的过度效果
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, autoAccountBeen.size());
                SwipeLayoutManager.create().closeLayout();
                notifyDataSetChanged();

            }
        });
    }
    public void refresh(List<AccountUpdateBean.AutoAccountBean> accountBean){
        this.autoAccountBeen = accountBean;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (autoAccountBeen!=null){
            return autoAccountBeen.size();
        }
        return 0;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextView tv_delete;
        CheckBox ck_select;
        RelativeLayout list_re;
        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_city);
            tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            ck_select = (CheckBox) view.findViewById(R.id.ck_select);
            list_re= (RelativeLayout) view.findViewById(R.id.list_re);

        }
    }
}
