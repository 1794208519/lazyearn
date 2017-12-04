package com.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entity.MainItemBean;
import com.uidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/5/4.
 */
public class GridViewAdapter extends BaseAdapter {
    private List<MainItemBean> mMainItemBases;
    private LayoutInflater inflater;
    private int[] imgs;
    public GridViewAdapter(List<String> names, int[] imgs, Context context) {
        super();
        this.imgs=imgs;
        mMainItemBases = new ArrayList<MainItemBean>();
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < imgs.length; i++) {
            MainItemBean mainItemBase = new MainItemBean(imgs[i], names.get(i));
            mMainItemBases.add(mainItemBase);
        }
    }

    @Override
    public int getCount() {
        if (mMainItemBases != null) {
            return mMainItemBases.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mMainItemBases.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void notifyChanged(List<String> names){
        mMainItemBases.clear();
        for (int i = 0; i < imgs.length; i++) {
            MainItemBean mainItemBase = new MainItemBean(imgs[i], names.get(i));
            mMainItemBases.add(mainItemBase);
        }
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.card_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_item);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mMainItemBases.get(position).getIcon());
        viewHolder.image.setImageResource(mMainItemBases.get(position).getImageId());
        return convertView;
    }

    class ViewHolder {
        public TextView name;
        public ImageView image;
    }
}
