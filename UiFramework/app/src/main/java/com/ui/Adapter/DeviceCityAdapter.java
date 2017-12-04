package com.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class DeviceCityAdapter extends RecyclerView.Adapter {
    private List<OperatorsCityBean.AutoCityBean> autoAccountBeen;
    private Context context;
    private AccountCallbak callback;
    private HashMap<String,Boolean> accountMap=new HashMap<String,Boolean>();
    public DeviceCityAdapter(List<OperatorsCityBean.AutoCityBean> accountBean, Context context, HashMap<String,Boolean> accountMap, AccountCallbak callback) {
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
        final int position_b = autoAccountBeen.size()-position-1;
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final Integer tag=new Integer(position_b);//初始化一个Integer实例，其值为position
        myViewHolder.list_re.setBackgroundColor(context.getResources().getColor(R.color.white));
        if (autoAccountBeen.get(position_b).getDevices()!=null&&!autoAccountBeen.get(position_b).getDevices().equals("null")&&!autoAccountBeen.get(position_b).getDevices().equals("")){
            myViewHolder.list_re.setBackgroundColor(0xff10ADE0);
        }
        myViewHolder.tv.setText(autoAccountBeen.get(position_b).getCity()+"-");
        myViewHolder.tv_operators_name.setText(autoAccountBeen.get(position_b).getOperator());
        myViewHolder.ck_select.setChecked(false);
        if (accountMap!=null){
            myViewHolder.ck_select.setChecked(accountMap.get(autoAccountBeen.get(tag).getCityId()+""));
        }
        myViewHolder.ck_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myViewHolder.ck_select.isChecked()){
                    accountMap.put(autoAccountBeen.get(tag).getCityId()+"",true);
                }else {
                    accountMap.put(autoAccountBeen.get(tag).getCityId()+"",false);
                }
            }
        });
        myViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.positiveCallbak(position_b);
                accountMap.put(autoAccountBeen.get(position_b).getCityId()+"",false);
                //删除bean里的值
                autoAccountBeen.remove(position_b);
                //这俩句至关重要，实现了删除条目时的过度效果
                notifyItemRemoved(position_b);
                notifyItemRangeChanged(position_b, autoAccountBeen.size());
                SwipeLayoutManager.create().closeLayout();
                notifyDataSetChanged();
            }
        });
    }
    public void refresh(List<OperatorsCityBean.AutoCityBean> accountBean){
        this.autoAccountBeen = accountBean;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return autoAccountBeen.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_operators_name;
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
            tv_operators_name= (TextView) view.findViewById(R.id.tv_operators_name);
        }
    }
}
