package com.utils;

import com.uidemo.R;

/**
 * Created by Administrator on 2017/4/17.
 */
public interface Constant {
    //1.链接2.亮屏3.息屏4.停止5.附近的人6.通讯录7.联系人8.群好友9.公众号10.智能回复11.朋友圈12.微信一键发消息22.白名单23.自动换号
// 13.附近的人14.通讯录15.联系人16.群好友17.  20.QQ一键发消息21.一键养号22.自动换号23.白名单24.一键开始
    public static final String[] indexStr = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};

    public static final int[] weixinimgs = {R.drawable.but_sele_square_wechatconnect, R.drawable.but_sele_square_wechatbrightscreen,
            R.drawable.but_sele_square_wechatclosescreen,
            R.drawable.but_sele_square_wechatstop,
            R.drawable.but_sele_square_wechatnearby,
            R.drawable.but_sele_square_wechatonekey,
            R.drawable.but_sele_square_wechatphonelist,
            R.drawable.but_sele_square_wechatlinkman,
            R.drawable.but_sele_square_wechatgroupfriends,
            R.drawable.but_sele_square_wechatpublicnum,
            R.drawable.but_sele_square_wechatsmartreply,
            R.drawable.but_sele_square_wechatfriendcircle,
            R.drawable.but_sele_square_wechatsendmessage,
            R.drawable.but_sele_square_wechatchatmessage,
            R.drawable.but_sele_square_wechatwhitelist,
            R.drawable.but_sele_square_wechatchangenum,
            R.drawable.but_sele_square_wechattimestar};

    public static final int[] musices = {R.raw.link, R.raw.open_screen, R.raw.close_screen, R.raw.ready, R.raw.nearby, R.raw.start, R.raw.stop, R.raw.success, R.raw.contact, R.raw.noopen, R.raw.smart_reply};

    public static String[] weixinnames = {"连接", "亮屏", "息屏", "停止", "附近人", "一键开始", "通讯录", "联系人", "群好友", "公众号", "智能回复", "朋友圈", "一键发消息", "聊天记录", "白名单", "自动换号", "开启定时"};

    public static final String[] setTitles = {"版本更新", "关于版本", "设置定时"};

    public static final String[] setSetTitles2 = {"微信数据中心", "QQ数据中心"};

    public static final int[] setImages = {R.drawable.vision_update_img, R.drawable.about_vision_img, R.drawable.set_dinshi_img};

    public static final int[] setImages2 = {R.drawable.wx_date_img, R.drawable.qq_date_img};

    public static final String[] qqnames = {"连接", "亮屏", "息屏", "停止", "附近人", "通讯录", "联系人", "群好友", "公众号", "智能回复", "朋友圈", "一键发消息"};

    public static final int[] qqimgs = {
            R.drawable.but_sele_square_qqconnect,
            R.drawable.but_sele_square_qqbrightscreen,
            R.drawable.but_sele_square_qqclosescreen,
            R.drawable.but_sele_square_qqstop,
            R.drawable.but_sele_square_qqnearby,
            R.drawable.but_sele_square_qqphonelist,
            R.drawable.but_sele_square_qqlinkman,
            R.drawable.but_sele_square_qqgroupfriends,
            R.drawable.but_sele_square_qqpublicnum,
            R.drawable.but_sele_square_qqsmartreply,
            R.drawable.but_sele_square_qqfriendcircle,
            R.drawable.but_sele_square_qqsendmessage};


}
