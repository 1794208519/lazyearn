package com.utils;

/**
 * Created by vicmob_yf002 on 2017/4/25.
 */
public class Urlutils {
    //通信地址
    private static String webUrl = "192.168.108.100";
    //    public final static String HOST = "http://120.55.43.85/";
//    http://101.132.129.64
    public final static String HOST = "http://101.132.129.64/";
    //APK更新地址
    static String apkUrl = "http://www.vicmob.com/dapp/v_2.0.0/LanLanMobile.apk";
    //更新配置文件地址
    static String versionUrl = "http://www.vicmob.com/dapp/v_2.0.0/LanMobile_version.xml";
    //获取二维码地址
    static String codeUrl = "http://192.168.108.100:8888/temp/wxqr.png";
    static String deadUrl = "http://192.168.108.100:8888/temp/qrcode.jpg";

    public static String getTxtUrl() {
        return txtUrl;
    }

    public static void setTxtUrl(String txtUrl) {
        Urlutils.txtUrl = txtUrl;
    }

    static String txtUrl = "http://192.168.108.100:8888/a.txt";


    public static String getDeadUrl() {
        return deadUrl;
    }

    public static void setDeadUrl(String deadUrl) {
        Urlutils.deadUrl = deadUrl;
    }

    //获取账号
    static String selectAccountUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoAccount/selectAccount";
    //插入账号地址
    static String insertAccountUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoAccount/addAccountByAccount";
    //删除账号
    static String deleteAccountUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoAccount/deleteAccount";
    //更新账户
    static String updateAccountUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoAccount/updateAccount";
    //插入地点
//    static String insertAddressUrl = HOST + "weifen/insertAddress.php";
    static String insertAddressUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoData/insertAddress";
    //插入文本回复地址
//    static String insertTextUrl = HOST + "weifen/insertIntelligent.php";
    static String insertTextUrl = HOST + "VicmobLazyEarn/lazyearn-api/vicCodeText/insertIntelligent";
    //获取文本回复内容
//    static String intelligentTextUrl = HOST + "weifen/selectIntelligent.php";
    static String intelligentTextUrl = HOST + "VicmobLazyEarn/lazyearn-api/vicCodeText/selectlikeIntelligent";
    static String intellinolikegentTextUrl = HOST + "VicmobLazyEarn/lazyearn-api/vicCodeText/selectnolikeIntelligent";

    public static String getIntellinolikegentTextUrl() {
        return intellinolikegentTextUrl;
    }
    //删除文本回复内容
//    static String insertTextDelete = HOST + "weifen/deleteIntelligent.php";
    static String insertTextDelete = HOST + "VicmobLazyEarn/lazyearn-api/vicCodeText/deleteIntelligent";
    //更新文本回复内容
//    static String insertTextDelete = HOST + "weifen/deleteIntelligent.php";
    static String updateTextUrl = HOST + "VicmobLazyEarn/lazyearn-api/vicCodeText/updateVicCodeText";
//    static String insertCity = HOST + "weifen/InsertCityOperator.php";
    static String insertCity = HOST + "VicmobLazyEarn/lazyearn-api/autoCity/insertCityOperator";
    //获得城市、运营商数据
//    static String SelectCity = HOST + "weifen/SelectCityOperators.php";
    static String SelectCity = HOST + "VicmobLazyEarn/lazyearn-api/autoCity/selectCityOperator";
    //删除城市、运营商数据
//    static String deleteCity = HOST + "weifen/DeleteCityOperator.php";
    static String deleteCity = HOST + "VicmobLazyEarn/lazyearn-api/autoCity/DeleteCityOperator";
    //更新城市、运营商数据
//    static String updateCity = HOST + "weifen/UpdateCityOperator.php";
    static String updateCity = HOST + "VicmobLazyEarn/lazyearn-api/autoCity/updateCityOperator";

    //上传添加的总的地点
//    static String insertAdd = HOST + "weifen/insertAdd.php";
    static String insertAdd = HOST + "VicmobLazyEarn/lazyearn-api/autoData/insertAddress";
    //获取添加的总的地点
//    static String selectAdd = HOST + "weifen/selectAddress.php";
    static String selectAdd = HOST + "VicmobLazyEarn/lazyearn-api/autoData/selectAddress";
    //删除添加的总的地点
//    static String deleteAdd = HOST + "weifen/deleteAddress.php";
    static String deleteAdd = HOST + "VicmobLazyEarn/lazyearn-api/autoData/deleteAddress";

    //更新账户
    static String updateAccount = HOST + "weifen/updateAccount.php";
    //更新地址
//    static String updateAddress = HOST + "weifen/updateAddress.php";
    static String updateAddress = HOST + "VicmobLazyEarn/lazyearn-api/autoData/updateAddress";


    //判断城市运营商是否存在
    static String JudgeExistUrl = HOST + "weifen/JudgeExist.php";
    //提交所有默认数据
//    static String insertDefaultDataUrl = HOST + "weifen/insertDefaultData.php";
    static String insertDefaultDataUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoDefaultData/insertDefaultData";
    //獲取所有默认数据
//    static String getDefaultDataUrl = HOST + "weifen/SelectDefaultData.php";
    static String getDefaultDataUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoDefaultData/selectDefaultData";
    //插入所有子设备
    static String insertDevicesData = HOST + "VicmobLazyEarn/lazyearn-api/autoControl/insertData";

    public static String weixinUrl = "http://"+webUrl+"/GetWechatNameList_.php";
    public static String weixinhaoyouUrl = "http://"+webUrl+"/GetHaoYouNameList_.php";
    public static String liaotianjiluUrl = "http://"+webUrl+"/GetLiaoTianJiLuList_.php";
    public static String selectLiaoTianJiLuUrl = "http://"+webUrl+"/SelectLiaoTianJiLuBean_.php";
    public static String deleteAlldateByWxhyUrl = "http://"+webUrl+"/deleteAlldateByWxhy_.php";
    public static String myweixiniconUrl = "http://"+webUrl+":8888/temp/icon/";
    public static String myweixinheadUrl = "http://"+webUrl+":8888/temp/head/";
    public static String liaoTianTuPianUrl = "http://"+webUrl+":8888/temp/images/";
    public static String baiMingDanUrl = "http://"+webUrl+"/BaiMingDan/GetBaiMingDanNameList_.php";
    public static String setBaiMingDan = HOST + "VicmobLazyEarn/lazyearn-api/autoDevices/addDevicesByFile";
    public static String deleteBaiMingDan = HOST + "VicmobLazyEarn/lazyearn-api/autoDevices/deleteDevices";
    public static String getBaiMingDanInYunUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoDevices/findDevices";
    public static String updateBaiMingDanStatus = HOST + "VicmobLazyEarn/lazyearn-api/autoDevices/updateAllStatus";
    public static String boundDeviceUrl = HOST+"VicmobLazyEarn/lazyearn-api/autoControl/insertData";

    public static String getInsertDevicesData() {
        return insertDevicesData;
    }

    public static void setInsertDevicesData(String insertDevicesData) {
        Urlutils.insertDevicesData = insertDevicesData;
    }

    public static String getUpdateTextUrl() {
        return updateTextUrl;
    }

    public static String getInsertDefaultDataUrl() {
        return insertDefaultDataUrl;
    }

    public static String getGetDefaultDataUrl() {
        return getDefaultDataUrl;
    }

    public static String getUpdateAccount() {
        return updateAccount;
    }

    public static String getUpdateAddress() {
        return updateAddress;
    }

    public static String getDeleteAccountUrl() {
        return deleteAccountUrl;
    }

    public static String getInsertTextUrl() {
        return insertTextUrl;
    }

    public static String getApkUrl() {
        return apkUrl;
    }

    public static String getVersionUrl() {
        return versionUrl;
    }

    public static String getWebUrl() {
        return webUrl;
    }

    public static String getCodeUrl() {
        return codeUrl;
    }

    public static String getInsertAccountUrl() {
        return insertAccountUrl;
    }

    public static String getInsertAddressUrl() {
        return insertAddressUrl;
    }

    public static String getIntelligentTextUrl() {
        return intelligentTextUrl;
    }

    public static String getInsertTextDelete() {
        return insertTextDelete;
    }

    public static String getInsertAdd() {
        return insertAdd;
    }

    public static String getSelectAdd() {
        return selectAdd;
    }

    public static String getDeleteAdd() {
        return deleteAdd;
    }

    public static String getSelectAccountUrl() {
        return selectAccountUrl;
    }

    public static String getInsertCity() {
        return insertCity;
    }

    public static String getSelectCity() {
        return SelectCity;
    }

    public static String getDeleteCity() {
        return deleteCity;
    }

    public static String getUpdateCity() {
        return updateCity;
    }

    public static String getJudgeExistUrl() {
        return JudgeExistUrl;
    }

    public static String getUpdateAccountUrl() {
        return updateAccountUrl;
    }
}
