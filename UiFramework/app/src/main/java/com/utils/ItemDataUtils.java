package com.utils;


import com.entity.ItemBean;
import com.uidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前类注释:
 */
public final class ItemDataUtils {

    private ItemDataUtils() {
    }

    public static List<ItemBean> getItemBeans() {
        List<ItemBean> itemBeans = new ArrayList<>();
//        itemBeans.add(new ItemBean(R.drawable.sidebar_purse, "我的账号", false));
        itemBeans.add(new ItemBean(R.drawable.sidebar_decoration, "我的地址", false));
        itemBeans.add(new ItemBean(R.drawable.sidebar_favorit, "城市管理", false));
        itemBeans.add(new ItemBean(R.drawable.sidebar_clean, "清除死粉", false));
        itemBeans.add(new ItemBean(R.drawable.sidebar_onekey, "微信一键养号", false));
        itemBeans.add(new ItemBean(R.drawable.sidebar_album, "订单管理", false));
        itemBeans.add(new ItemBean(R.drawable.sidebar_file, "我的文件", false));
//        itemBeans.add(new ItemBean(R.drawable.sidebar_wxdate, "微信数据中心", false));
//        itemBeans.add(new ItemBean(R.drawable.sidebar_qqdate, "QQ数据中心", false));
//        itemBeans.add(new ItemBean(R.drawable.sidebar_time, "设置定时", false));
        return itemBeans;
    }

}
