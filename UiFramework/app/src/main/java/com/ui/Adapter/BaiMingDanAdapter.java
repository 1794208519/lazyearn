package com.ui.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Interface.View.AccountCallbak;
import com.entity.Addresstimebean;
import com.entity.BaiMingDanBean;
import com.ui.view.swipedeletelayout.SwipeLayoutManager;
import com.uidemo.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by qq944 on 2017/10/19.
 */

public class BaiMingDanAdapter extends RecyclerView.Adapter{
    private List<String> AddressBean;
    private Context context;
    private AccountCallbak callback;
    private String biaoMing;
    private HashMap<String,Boolean> addressMap=new HashMap<String,Boolean>();
    public BaiMingDanAdapter(List<String> AddressBean, Context context, HashMap<String,Boolean> addressMap, AccountCallbak callback,String biaoMing) {
        this.AddressBean = AddressBean;
        this.context = context;
        this.addressMap=addressMap;
        this.callback = callback;
        this.biaoMing = biaoMing;
    }

    public void setBiaoMing(String biaoMing) {
        this.biaoMing = biaoMing;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.adapter_account_list, parent, false);
        BaiMingDanAdapter.MyViewHolder myViewHolder =new BaiMingDanAdapter.MyViewHolder(view) ;

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BaiMingDanAdapter.MyViewHolder myViewHolder = (BaiMingDanAdapter.MyViewHolder)holder;
        final Integer tag=new Integer(position);//初始化一个Integer实例，其值为position
        myViewHolder.ck_select.setTag(tag);
        myViewHolder.list_re.setBackgroundColor(context.getResources().getColor(R.color.white));
      /*  if (AddressBean.get(position).getDevices()!=null&&!AddressBean.get(position).getDevices().equals("null")){
            myViewHolder.list_re.setBackgroundColor(0xff10ADE0);
        }*/
        if (AddressBean.get(position).equals(biaoMing)){
            Log.i("zw1021",biaoMing);
            myViewHolder.list_re.setBackgroundColor(Color.parseColor("#1296db"));
            addressMap.put(AddressBean.get(tag),true);
        }
        myViewHolder.tv.setText(AddressBean.get(position));
        myViewHolder.img_ico.setImageResource(R.mipmap.biaoge);
        myViewHolder.ck_select.setChecked(false);
        if (addressMap!=null){
            myViewHolder.ck_select.setChecked(addressMap.get(AddressBean.get(tag)));
        }
        myViewHolder.ck_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (myViewHolder.ck_select.isChecked()){
                        Log.i("123","Ttrue"+tag);
                        addressMap.put(AddressBean.get(tag),true);
                    }else {
                        Log.i("123","F"+tag);
                        addressMap.put(AddressBean.get(tag),false);
                    }
            }
        });
        myViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.positiveCallbak(position);
                addressMap.put(AddressBean.get(position),false);
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
        ImageView img_ico;
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
            img_ico = ((ImageView) view.findViewById(R.id.img_ico));
            list_re = ((RelativeLayout) view.findViewById(R.id.list_re));
        }
    }
}
