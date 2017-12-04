package com.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Interface.View.FragmentBackListener;
import com.MyApp.MyApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.base.BaseFragment;
import com.entity.GpsBean;
import com.ui.Adapter.MyAdapter;
import com.ui.activity.MainActivity;
import com.uidemo.R;
import com.utils.DialogUtil;
import com.utils.GPSUtil;
import com.utils.MyJsonUtil;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/1/6.
 */
public class AddressFragment extends BaseFragment implements View.OnTouchListener, OnGetPoiSearchResultListener, AdapterView.OnItemClickListener, OnGetSuggestionResultListener, OnGetGeoCoderResultListener, View.OnClickListener, BDLocationListener, BaiduMap.OnMapClickListener, BaiduMap.OnMarkerDragListener, BaiduMap.OnMapStatusChangeListener, FragmentBackListener {
    private static final int BAIDU_READ_PHONE_STATE = 1;
    // 全局变量对象
    private MyApplication app;
    private String mMockProviderName = LocationManager.GPS_PROVIDER;
    private Button bt_Ok;
    private ImageButton focus;
    private LocationManager locationManager;
    private double latitude = 31.3029742, longitude = 120.6097126;// 默认常州
    private Thread thread;// 需要一个线程一直刷新
    private Boolean RUN = true;

    boolean isFirstLoc = true;// 是否首次定位
    // 定位相关
    private LocationClient mLocClient;// 声明LocationClient类
    private MyLocationConfiguration.LocationMode mCurrentMode;// 定位模式
    private BitmapDescriptor mCurrentMarker;// 定位图标
    private TextureMapView mMapView;// 存放地图的控件
    private BaiduMap mBaiduMap;// BaiduMap对象

    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.mipmap.gcoding);
    private LatLng curLatlng;
    private GeoCoder mSearch;
    // 存放地址文本的控件
    private EditText keyOrResult;

    private PoiSearch mPoiSearch = null;
    // 地址listView
    private ListView searchList;
    // 搜索地址名集合
    private List<String> searchAddressesName = new ArrayList<String>();
    // 搜索地址集合
    private List<String> searchAddresses = new ArrayList<String>();
    // 经纬度集合
    private List<Double> latitudes = new ArrayList<Double>();
    private List<Double> longtitudes = new ArrayList<Double>();
    // 地址结果adapter
    private MyAdapter adapter;
    private boolean isListV = false;
    private Button search_ok;
    // 模糊搜索
    private SuggestionSearch mSuggestionSearch;
    // 存放城市字符串
    private String city = "";
    private ImageView bMapMark;
    //显示当前模拟的位置的详细信息
    private RelativeLayout detailMsg;
    //显示或者隐藏模拟按钮
    private static final int showMockBtn_MSG = 1;
    private static final int hideMockBtn_MSG = 2;
    //对话框
    Dialog dialog = null;
    //数据库地址
    private String url = Urlutils.getInsertAdd();
    //地点
    private String city1, latitude1, longitude1;
    //
    private static final int addSuccess = 4;
    private static final int addFalut = 5;
    private List<GpsBean.LacCidBean> mGpsBeanList = new ArrayList<>();
    private int lac = 0, cid = 0, mnc = 0;
    private double lat = 0.0, lon = 0.0;
    // 更新UI
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case showMockBtn_MSG:
                    //坐标动画
                    Animation shake = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.shake_y);
                    shake.reset();
                    shake.setFillAfter(true);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    bMapMark.startAnimation(shake);
                    break;
                case hideMockBtn_MSG:
                    keyOrResult.setText("正在加载...");
                    detailMsg.setVisibility(View.GONE);
                    searchList.setVisibility(View.GONE);
                    break;
                case addSuccess:

                    ToastUtils.showShort(mActivity.getApplicationContext(), "添加成功");
                    break;
                case addFalut:
                    ToastUtils.showShort(mActivity.getApplicationContext(), "添加异常");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.address_frag_layout, container, false);
        mMapView = ((TextureMapView) messageLayout.findViewById(R.id.bmapView));
        messageLayout.setOnTouchListener(this);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            //动态获取权限
            showContacts();
        } else {
            initView();
            iniListner();
            iniData();
        }

    }

    /**
     * 百度地图动态权限申请
     */
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showShort(mActivity.getApplicationContext(), "没有权限,请手动开启定位权限");
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        } else {
            initView();
            iniListner();
            iniData();
        }
    }

    /**
     * Android6.0申请权限的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initView();
                    iniListner();
                    iniData();
                } else {
                    // 没有获取到权限，做特殊处理
                    ToastUtils.showShort(mActivity.getApplicationContext(), "获取位置权限失败，请手动开启");
                }
                break;
            default:
                break;
        }
    }

    /**
     * iniView 界面初始化
     */
    @Override
    public void initView() {
        // 全局变量对象初始化
        app = MyApplication.getInstance();
        bt_Ok = (Button) mActivity.findViewById(R.id.baidumap_addAddress);
        search_ok = (Button) mActivity.findViewById(R.id.baidumap_searchButton);
        focus = (ImageButton) mActivity.findViewById(R.id.baidumap_centerToMyLocation);
        keyOrResult = (EditText) mActivity.findViewById(R.id.baidumap_keyOrResult);
        bMapMark = (ImageView) mActivity.findViewById(R.id.bmapMark);
        detailMsg = (RelativeLayout) mActivity.findViewById(R.id.detailMsg);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        // 定位初始化
        mLocClient = new LocationClient(mActivity);
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // listView初始化
        searchList = (ListView) getActivity().findViewById(R.id.baidumap_searchResultListView);
        searchList.setOnItemClickListener(this);
        // 初始化模糊搜索，注册搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }


    /**
     * iniListner 接口初始化
     */
    private void iniListner() {
        bt_Ok.setOnClickListener(this);
        search_ok.setOnClickListener(this);
        focus.setOnClickListener(this);
        mLocClient.registerLocationListener(this);
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMarkerDragListener(this);
        mBaiduMap.setOnMapStatusChangeListener(this);
        //        startMock.setOnClickListener(this);
        //        stopMock.setOnClickListener(this);

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    /**
     * iniData 数据初始化
     */
    private void iniData() {
        // inilocation();
        iniMap();
    }

    /**
     * iniMap 初始化地图
     */
    private void iniMap() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        // 设置正常地图显示 , MyLocationConfiguration 的包
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 缩放比例控制的大一些（地图感官上更细腻）
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        detailMsg.setVisibility(View.GONE);
        return true;
    }


    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Log.i("gps", String.format("location: x=%s y=%s", lat, lng));
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }


    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }


    @Override
    public void onDestroy() {
        RUN = false;
        thread = null;
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        bd.recycle();
        super.onDestroy();
    }

    /**
     * 获取POI搜索结果
     */
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<PoiInfo> list = poiResult.getAllPoi();
            if (list != null && list.size() > 0) {
                searchList.setVisibility(View.VISIBLE);
                isListV = true;
                for (int i = 0; i < list.size(); i++) {
                    // 添加数据
                    latitudes.add(list.get(i).location.latitude);
                    longtitudes.add(list.get(i).location.longitude);
                    searchAddressesName.add(list.get(i).name.toString().trim());
                    searchAddresses.add(list.get(i).address.toString().trim());
                }
                // listView数据绑定
                adapter = new MyAdapter(searchAddressesName, searchAddresses, getActivity().getApplicationContext());
                searchList.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int arg2, long id) {
        searchList.setVisibility(View.GONE);
        isListV = false;

        // 设置监听反馈为空，进入判断为false
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBackListener(null);
            ((MainActivity) getActivity()).setInterception(false);
        }
        keyOrResult.setText(searchAddressesName.get(arg2));

        // 定义Maker坐标点
        LatLng latLng = new LatLng(latitudes.get(arg2), longtitudes.get(arg2));
        setCurrentMapLatLng(latLng);
        // 点击列表效果
        searchList.setItemChecked(arg2, true);
    }

    /**
     * 模糊搜索监听，应对搜索不出来的状况
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        try {
            if (res == null || res.getAllSuggestions() == null || res.equals("")) {
                ToastUtils.showShort(mActivity.getApplicationContext(), "没有更多结果，可以换个关键词");
                return;
                // 未找到相关结果
            } else {

                List<SuggestionResult.SuggestionInfo> list = res.getAllSuggestions();
                searchList.setVisibility(View.VISIBLE);
                isListV = true;
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        latitudes.add(list.get(i).pt.latitude);
                        longtitudes.add(list.get(i).pt.longitude);
                        searchAddresses.add(list.get(i).district);
                        searchAddressesName.add(list.get(i).key);
                    }
                    adapter = new MyAdapter(searchAddressesName, searchAddresses,
                            getActivity().getApplicationContext());
                    searchList.setAdapter(adapter);
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
            if (!isListV) {
                ToastUtils.showShort(mActivity.getApplicationContext(), "没有更多结果，可以换个关键词");
            }
            e.printStackTrace();
        }
    }

    /**
     * onGetGeoCodeResult 搜索（根据实地信息-->经纬坐标）
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            // 没有检索到结果
            ToastUtils.showShort(mActivity.getApplicationContext(), "抱歉，未能找到结果");
        }
        // 获取地理编码结果
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(geoCodeResult.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.gcoding)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(geoCodeResult.getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f", geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude);

        keyOrResult.setText(String.format("%s", geoCodeResult.getAddress()));
        Toast.makeText(getActivity().getApplicationContext(), strInfo, Toast.LENGTH_LONG).show();
    }

    /**
     * onGetReverseGeoCodeResult 搜索（根据坐标-->实地信息）
     */
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.showShort(mActivity.getApplicationContext(), "抱歉，未能找到结果");
            return;
        }

        keyOrResult.setText(String.format(reverseGeoCodeResult.getAddress()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.baidumap_addAddress:
                // 获取结果中的信息
                city1 = keyOrResult.getText().toString().trim();
                latitude1 = String.valueOf(curLatlng.latitude);
                longitude1 = String.valueOf(curLatlng.longitude);
                // 若存在信息
                if (city1.equals("正在加载...")) {
                    ToastUtils.showShort(mActivity, "正在加载中请稍后添加");

//                    MyToast.show(mActivity, "正在加载中请稍后添加");

                }else if(city1 != null && !TextUtils.isEmpty(city1)) {
                    loadData();
                }
//                else {
//                    ToastUtils.showShort(mActivity, "信息不完整");
//                }
                break;
            case R.id.baidumap_centerToMyLocation:
                isFirstLoc = true;// 请求地图定位
                Snackbar.make(v, "回到当前位置", Snackbar.LENGTH_SHORT).setAction("sss", null).show();
                break;
            case R.id.baidumap_searchButton:
                // 点击时将本类内存中对象清空
                searchAddressesName.clear();
                searchAddresses.clear();
                latitudes.clear();
                longtitudes.clear();
                // 先进行poi搜索
                mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city)
                        .keyword(keyOrResult.getText().toString().trim()).pageNum(3).pageCapacity(20));
                // 所搜之后，若此参数还是空的，表示搜索没有成功
                if (searchAddressesName.size() == 0) {
                    // 进行模糊搜索
                    mSuggestionSearch.requestSuggestion(
                            (new SuggestionSearchOption()).keyword(keyOrResult.getText().toString().trim()).city(city));
                }
                // 得到监听反馈，进入判断为true
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).setBackListener(this);
                    ((MainActivity) getActivity()).setInterception(true);
                }
                break;
        }
    }


    /**
     * 定位SDK监听函数
     */
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // map view 销毁后不在处理新接收的位置
        if (bdLocation == null || mMapView == null) {
            return;
        }
        // 是否第一次定位
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            setCurrentMapLatLng(ll);
        }
    }

    // 地图点击
    @Override
    public void onMapClick(LatLng latLng) {
        if (detailMsg.getVisibility() == View.VISIBLE) {
            detailMsg.setVisibility(View.GONE);
        } else {
            setCurrentMapLatLng(latLng);
            searchList.setVisibility(View.GONE);
            //            startMock.setVisibility(View.GONE);
            //            stopMock.setVisibility(View.GONE);
        }
    }

    /**
     * setCurrentMapLatLng 设置当前坐标
     */
    private void setCurrentMapLatLng(LatLng arg0) {
        curLatlng = arg0;

        // 设置地图中心点为这是位置
        LatLng ll = new LatLng(arg0.latitude, arg0.longitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);

        // 根据经纬度坐标 找到实地信息，会在接口onGetReverseGeoCodeResult中呈现结果
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0));
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    /**
     * onMarkerDrag 地图上标记拖动结束
     */
    @Override
    public void onMarkerDrag(Marker marker) {

    }

    /**
     * onMarkerDragEnd 地图上标记拖动结束
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        setCurrentMapLatLng(marker.getPosition());
        myHandler.sendEmptyMessage(showMockBtn_MSG);
    }

    /**
     * onMarkerDragStart 地图上标记拖动开始
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        myHandler.sendEmptyMessage(hideMockBtn_MSG);
    }


    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        myHandler.sendEmptyMessage(hideMockBtn_MSG);
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        // 根据经纬度坐标 找到实地信息，会在接口onGetReverseGeoCodeResult中呈现结果
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mapStatus.target));
        curLatlng = mapStatus.target;
        myHandler.sendEmptyMessage(showMockBtn_MSG);
    }

    @Override
    public void onbackForward() {
        // 处理fragment的返回事件
        if (isListV) {
            searchList.setVisibility(View.GONE);
            isListV = false;
            // 设置监听反馈为空，进入判断为false
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setBackListener(null);
                ((MainActivity) getActivity()).setInterception(false);
            }
        } else {

        }
    }

    public void addDataDialog() {
        dialog = DialogUtil.createConfirmDialog(mActivity, "提示", "确认添加 “" + city1 + "” 吗?", "确认", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.showShort(mActivity, "好的");
                if (city1 == null || TextUtils.isEmpty(city1)) {
                    ToastUtils.showShort(mActivity, "数据不完整");
                } else {
                    upLoadThread();
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.showShort(mActivity, "不要");

            }
        }, R.mipmap.head);
        dialog.show();
    }

    /****
     * 获取基站信息
     */
    public void loadData() {
//        double[] gps = GPSUtil.bd09_To_Gcj02(curLatlng.latitude, curLatlng.longitude);
//        //String url = "http://api.cellocation.com/recell/?incoord=bd09ll&coord=bd09ll&lat=" + curLatlng.latitude + "&lon=" + curLatlng.longitude;
//        String url = "http://api.cellocation.com/recell/?incoord=gcj02&coord=gcj02&lat=" + gps[0] + "&lon=" + gps[1];

//        OkHttpUtils
//                .get()
//                .url(url)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        ToastUtils.showShort(mActivity, "网络获取失败");
//                    }
//
//                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                    @Override
//                    public void onResponse(String response, int id) {
//                        if (response != null && !TextUtils.isEmpty(response) && !response.equals("[]")) {
//                            try {
//                                if (mGpsBeanList.size() != 0 && mGpsBeanList != null) {
//                                    mGpsBeanList.clear();
//                                }
//                                JSONArray jsonArray = new JSONArray(response);
//                                mGpsBeanList = GpsBean.getJSONlist(jsonArray);
                                getLatCidInfo();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//                            ToastUtils.showShort(mActivity, "获取基站信息失败");
//                        }
//
//                    }
//                });
    }

    public void getLatCidInfo() {
//        /******
//         * mnc 中国移动系统使用00、02、04、07，
//         * 中国联通GSM系统使用01、06、09，
//         * 中国电信CDMA系统使用03、05、电信4G使用11，
//         * 中国铁通系统使用20
//         */
//        for (int i = 0; i < mGpsBeanList.size(); i++) {
//            if (mGpsBeanList.get(i).lac > mGpsBeanList.get(i).ci || mGpsBeanList.get(i).mnc == 11 || mGpsBeanList.get(i).mnc == 20) {
//            } else {
//                lac = mGpsBeanList.get(i).lac;
//                cid = mGpsBeanList.get(i).ci;
//                mnc = mGpsBeanList.get(i).mnc;
//                lat = mGpsBeanList.get(i).location.get(0).lat;
//                lon = mGpsBeanList.get(i).location.get(0).lon;
//                break;
//            }
//        }
//        if (lac == 0 || cid == 0) {
//            ToastUtils.showShort(mActivity, "添加地点失败");
//        } else {
            upLoadThread();
//        }
    }

    /**
     * 点击添加即上传地点
     */
    private void upLoadThread() {
        Thread th = new Thread() {
            @Override
            public void run() {
                String url1 = url;
                OkHttpUtils
                        .post()
                        .url(url1)
                        .addParams("address", city1)
                        .addParams("hello", "你好")
                        .addParams("longitude", longitude1)
                        .addParams("latitude", latitude1)
                        .addParams("lac", lac + "")
                        .addParams("cid", cid + "")
                        .addParams("mnc", mnc + "")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(addFalut);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (MyJsonUtil.getBeanByJson(response)) {
                                    myHandler.sendEmptyMessage(addSuccess);
                                }
                            }
                        });
            }
        };
        th.start();
    }

}
