package com.ui.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Interface.View.AccountCallbak;
import com.entity.Addresstimebean;
import com.entity.DeviceAddressBean;
import com.ui.view.swipedeletelayout.SwipeLayoutManager;
import com.ui.view.swipedeletelayout.SwipedeleteLayout;
import com.uidemo.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/6/28.
 */
public class DeviceAddressAdapter extends RecyclerView.Adapter {
    private List<Addresstimebean> AddressBean;
    private Context context;
    private AccountCallbak callback;
    private HashMap<String,Boolean> addressMap=new HashMap<String,Boolean>();
    public DeviceAddressAdapter(List<Addresstimebean> AddressBean, Context context, HashMap<String,Boolean> addressMap, AccountCallbak callback) {
        this.AddressBean = AddressBean;
        this.context = context;
        this.addressMap=addressMap;
        this.callback = callback;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.adapter_account_list, parent, false);
        MyViewHolder myViewHolder =new MyViewHolder(view) ;

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final Integer tag=new Integer(position);//初始化一个Integer实例，其值为position
        myViewHolder.ck_select.setTag(tag);
        myViewHolder.list_re.setBackgroundColor(context.getResources().getColor(R.color.white));
        if (AddressBean.get(position).getDevices()!=null&&!AddressBean.get(position).getDevices().equals("null")&&!AddressBean.get(position).getDevices().equals("")){
            myViewHolder.list_re.setBackgroundColor(0xff10ADE0);
        }
        myViewHolder.tv.setText(AddressBean.get(position).getAddress());
        myViewHolder.ck_select.setChecked(false);
        if (addressMap!=null){
            myViewHolder.ck_select.setChecked(addressMap.get(AddressBean.get(tag).getDataId()));
        }
        myViewHolder.ck_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myViewHolder.ck_select.isChecked()){
                                        Log.i("123","T"+tag);
                    addressMap.put(AddressBean.get(tag).getDataId(),true);
                }else {
                                        Log.i("123","F"+tag);
                    addressMap.put(AddressBean.get(tag).getDataId(),false);
                }
            }
        });
        myViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.positiveCallbak(position);
                addressMap.put(AddressBean.get(position).getDataId(),false);
                //删除bean里的值
                AddressBean .remove(position);
                //这俩句至关重要，实现了删除条目时的过度效果
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, AddressBean.size());
                SwipeLayoutManager.create().closeLayout();
                notifyDataSetChanged();
            }
        });


    }
    @Override
    public int getItemCount() {
        return AddressBean.size();
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
