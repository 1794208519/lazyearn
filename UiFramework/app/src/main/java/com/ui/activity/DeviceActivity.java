package com.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.Interface.View.Callback;
import com.MyApp.MyApplication;
import com.base.BaseActivity;
import com.ui.fragment.LeftFragment;
import com.uidemo.R;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.CustomerDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by vicmob_yf002 on 2017/5/8.
 */
public class DeviceActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvDevice;
    private Button mButUsername;
    private Button mButAddress;
    private Button mButPreserve;
    private ListView mLvUsername;
    private ListView mLvAddress;
    private MyApplication app;
    private ImageView device_back;

    //账号适配器
    private UserNameAdapter mAdapterUsername;
    //地点适配器
    private AddressAdapter mAdapterAddress;
    //账号数据集合，用于适配器加载数据
    List<String> account_data = new ArrayList<String>();
    //地点数据集合，用于适配器加载数据
    List<String> address_data = new ArrayList<String>();
    //经纬度数据结合
    List<String> location_data = new ArrayList<>();
    //获取传过来的设备名称
    private String deviceName;
    private int deviceIndex;
    private String deviceSN;
    //用于存储选中的地点集合
    private List<String> mAddressList = new ArrayList<>();
    //用于存储选择的账号集合
    private List<String> mUserList = new ArrayList<>();
    //用于存储选择的经纬度集合
    private List<String> mLocationList = new ArrayList<>();
    //存储账号名 结合
    private List<String> mNameList = new ArrayList<>();
    //存储密码集合
    private List<String> mPasswordList = new ArrayList<>();
    //存储的经度集合
    private List<String> mLongitude = new ArrayList<>();
    //存储的纬度结合
    private List<String> mLatitude = new ArrayList<>();
    private Dialog dialog = null;

    private static final int addSuccess = 1;
    private static final int addFalut = 2;
    public static final int usernameRenovate = 3;
    public static final int addressRenovate = 4;
    //数据库地址
    private String accounturl = Urlutils.getInsertAccountUrl();
    private String addressurl = Urlutils.getInsertAddressUrl();
    private CustomerDialog customerDialog;

    // 更新UI
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case addSuccess:
                    ToastUtils.showShort(DeviceActivity.this, "上传成功");
                    break;
                case addFalut:
                    ToastUtils.showShort(DeviceActivity.this, "数据库连接异常");
                    break;
                case usernameRenovate:
                    mLvUsername.setVisibility(View.VISIBLE);
                    account_data = new ArrayList<>();
//                    PrefUtils.getArray(DeviceActivity.this, "Account");
//
//                    //账号集合
//                    String[] account = app.getAccount();
//                    for (int i = 0; i < account.length; i++) {
//                        if (account[0] == null || TextUtils.isEmpty(account[0])) {
//                            mLvUsername.setVisibility(View.GONE);
//                            ToastUtils.showShort(DeviceActivity.this, "还未添加账号");
//                            return;
//                        }
//                        if (account[i] != null && !TextUtils.isEmpty(account[i])) {
//                            account_data.add(account[i]);
//                        }
//                    }
                    mAdapterUsername = new UserNameAdapter(account_data, DeviceActivity.this);
                    mLvUsername.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    mLvUsername.setAdapter(mAdapterUsername);
                    break;
                case addressRenovate:
                    mLvAddress.setVisibility(View.VISIBLE);
                    location_data = new ArrayList<>();
                    address_data = new ArrayList<>();


                    mAdapterAddress = new AddressAdapter(address_data, DeviceActivity.this);
                    mLvAddress.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    mLvAddress.setAdapter(mAdapterAddress);


                    break;
                default:
                    break;
            }
        }
    };

    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.device_activity;
    }


    @Override
    public void findView() {
        mTvDevice = (TextView) findViewById(R.id.tv_devices);
        mButUsername = (Button) findViewById(R.id.username);
        mButAddress = (Button) findViewById(R.id.address);
        mButPreserve = (Button) findViewById(R.id.preserve);
        mLvUsername = (ListView) findViewById(R.id.usernameList);
        mLvAddress = (ListView) findViewById(R.id.addressList);
        device_back = (ImageView) findViewById(R.id.device_back);
        app = MyApplication.getInstance();
        Intent intent = getIntent();
        deviceName = intent.getStringExtra("device");
        Log.i("123",deviceName);
        if (deviceName == null || TextUtils.isEmpty(deviceName) || "".equals(deviceName)) {

        } else {
            deviceIndex = Integer.parseInt(deviceName.substring(2, deviceName.length()));
            deviceSN = LeftFragment.devices[deviceIndex - 1].trim();
            deviceSN = deviceName;
            Log.d("SN", deviceSN);
        }
    }

    @Override
    public void initView() {


    }

    @Override
    public void initListener() {
        mButUsername.setOnClickListener(this);
        mButAddress.setOnClickListener(this);
        mButPreserve.setOnClickListener(this);
        device_back.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTvDevice.setText(deviceName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.username:
                if (mLvUsername.getVisibility() == View.VISIBLE) {
                    mLvUsername.setVisibility(View.GONE);
                    return;
                }
                myHandler.sendEmptyMessage(usernameRenovate);
                break;

            case R.id.address:
//                if (app.getAddress()[0] == null || TextUtils.isEmpty(app.getAddress()[0])) {
//                    Snackbar.make(v, "还未添加地点", Snackbar.LENGTH_SHORT).setAction("SS", null).show();
//                    return;
//                }
                if (mLvAddress.getVisibility() == View.VISIBLE) {
                    mLvAddress.setVisibility(View.GONE);
                    return;
                }
                myHandler.sendEmptyMessage(addressRenovate);
                break;

            case R.id.preserve:
                mLongitude.clear();
                mLatitude.clear();
                mNameList.clear();
                mPasswordList.clear();
                for (int i = 0; i < mLocationList.size(); i++) {
                    if (mLocationList.get(0) == null || TextUtils.isEmpty(mLocationList.get(0))) {
                        return;
                    }
                    if (mLocationList.get(i) != null && !TextUtils.isEmpty(mLocationList.get(i))) {
                        int j = mLocationList.get(i).lastIndexOf(";");
                        String longitude = mLocationList.get(i).substring(0, j);
                        String latitude = mLocationList.get(i).substring(j + 1);
                        mLongitude.add(longitude);
                        mLatitude.add(latitude);
                    }
                }
                for (int i = 0; i < mUserList.size(); i++) {
                    if (mUserList.get(0) == null || TextUtils.isEmpty(mUserList.get(0))) {
                        return;
                    }
                    if (mUserList.get(i) != null && !TextUtils.isEmpty(mUserList.get(i))) {
                        int j = mUserList.get(i).lastIndexOf(";");
                        String name = mUserList.get(i).substring(0, j);
                        String password = mUserList.get(i).substring(j + 1);
                        mNameList.add(name);
                        mPasswordList.add(password);
                    }
                }
                Log.d("qwer", "account" + mNameList + mPasswordList);
                Log.d("qwer", "address" + mAddressList + mLongitude + mLatitude);
                addDataDialog();
                break;
            case R.id.device_back:
                finish();
                break;
        }
    }
    public void addDataDialog() {
        customerDialog = new CustomerDialog(DeviceActivity.this, new Callback() {
            @Override
            public void positiveCallback() {
                if (mNameList.size() != 0 || mLongitude.size() != 0) {
                    for (int i = 0; i < mNameList.size(); i++) {
                        upLoadThread(i);
                    }
                    for (int i = 0; i < mAddressList.size(); i++) {
                        upLoadThread1(i);
                    }
                } else {
                    ToastUtils.showShort(DeviceActivity.this, "未选择您要添加的");
                    return;
                }
            }
            @Override
            public void negativeCallback() {
            }
        });
        customerDialog.setContent("提示" + "\n确认添加吗?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLvUsername.getVisibility() == View.VISIBLE || mLvAddress.getVisibility() == View.VISIBLE) {
                mLvAddress.setVisibility(View.GONE);
                mLvUsername.setVisibility(View.GONE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 上传账号数据
     *
     * @param index
     */
    private void upLoadThread(final int index) {
        Thread th = new Thread() {
            @Override
            public void run() {
                String url = accounturl;
                OkHttpUtils
                        .post()
                        .url(url)
                        .addParams("account", mNameList.get(index))
                        .addParams("password", mPasswordList.get(index))
                        .addParams("devices", deviceSN)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(addFalut);
                            }
                            @Override
                            public void onResponse(String response, int id) {
                                myHandler.sendEmptyMessage(addSuccess);
                            }
                        });
            }
        };
        th.start();
    }
    /**
     * 上传地点数据
     *
     * @param index
     */
    private void upLoadThread1(final int index) {
        Thread th = new Thread() {
            @Override
            public void run() {
                String url = addressurl;
                OkHttpUtils
                        .post()
                        .url(url)
                        .addParams("address", mAddressList.get(index))
                        .addParams("hello", "你好")
                        .addParams("longitude", mLongitude.get(index))
                        .addParams("latitude", mLatitude.get(index))
                        .addParams("devices", deviceSN)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(addFalut);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                myHandler.sendEmptyMessage(addSuccess);
                            }
                        });
            }
        };
        th.start();
    }

    /**
     * 账号适配器
     */
    class UserNameAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<String> accounts = new ArrayList<String>();
        private List<String> accounts_list = new ArrayList<String>();

        public UserNameAdapter(List<String> strings, Context context) {
            layoutInflater = LayoutInflater.from(context);
            for (int i = 0; i < strings.size(); i++) {
                int j = strings.get(i).lastIndexOf(";");
                String account = strings.get(i).substring(0, j);
                String password = strings.get(i).substring(j + 1);
                accounts.add(strings.get(i));
                accounts_list.add("微信号：" + "\u3000\u3000　" + account + "\u3000\u3000　　　　" + password);
            }
        }
        @Override
        public int getCount() {
            return accounts == null ? 0 : accounts.size();
        }

        @Override
        public Object getItem(int position) {
            return accounts_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.username_item, null);
            final CheckedTextView checkedUsername = (CheckedTextView) view.findViewById(R.id.checktv_username);
            checkedUsername.setText(accounts_list.get(position));
            checkedUsername.setChecked(false);
            checkedUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedUsername.toggle();
                    String userNameItem = accounts.get(position).toString();
                    boolean checked = checkedUsername.isChecked();
                    if (checked) {
                        mUserList.add(userNameItem);
                    } else {
                        mUserList.remove(userNameItem);
                    }
                }
            });
            ViewGroup vg = (ViewGroup) view;
            vg.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            return view;
        }
    }

    /**
     * 地点适配器
     */
    class AddressAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<String> address = new ArrayList<String>();

        public AddressAdapter(List<String> strings, Context context) {
            layoutInflater = LayoutInflater.from(context);
            for (int i = 0; i < strings.size(); i++) {
                address.add(strings.get(i));
            }
        }

        @Override
        public int getCount() {
            return address == null ? 0 : address.size();
        }

        @Override
        public Object getItem(int position) {
            return address.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.address_item, null);
            final CheckedTextView checkedAddress = (CheckedTextView) view.findViewById(R.id.checktv_address);
            checkedAddress.setText(address.get(position));
            checkedAddress.setChecked(false);
            checkedAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedAddress.toggle();
                    String addressItem = checkedAddress.getText().toString();
                    boolean checkeds = checkedAddress.isChecked();
                    if (checkeds) {
                        mAddressList.add(addressItem);
                        mLocationList.add(location_data.get(position));
                    } else {
                        mAddressList.remove(addressItem);
                        mLocationList.remove(location_data.get(position));
                        Log.d("qwer", String.valueOf(mLocationList.size()));
                    }
                }
            });

            ViewGroup vg = (ViewGroup) view;
            vg.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            return view;
        }
    }


}
