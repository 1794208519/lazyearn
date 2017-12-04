package com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Interface.View.Callback;
import com.base.BaseActivity;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.uidemo.R;
import com.utils.MyJsonUtil;
import com.utils.ToastUtils;
import com.utils.Urlutils;
import com.widget.CustomerDialog;
import com.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by vicmob_yf002 on 2017/6/9.
 */
public class CityActivity extends BaseActivity {

    private static final int addFalut = 1;
    private static final int addSuccess = 2;
    @BindView(R.id.city_back)
    ImageView cityBack;
    @BindView(R.id.mobile_item_img)
    ImageView mobileItemImg;
    @BindView(R.id.mobile_item_text)
    TextView mobileItemText;
    @BindView(R.id.mobile_item_select)
    ImageView mobileItemSelect;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.unicom_item_img)
    ImageView unicomItemImg;
    @BindView(R.id.unicom_item_text)
    TextView unicomItemText;
    @BindView(R.id.unicom_item_select)
    ImageView unicomItemSelect;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.chinanet_item_img)
    ImageView chinanetItemImg;
    @BindView(R.id.chinanet_item_text)
    TextView chinanetItemText;
    @BindView(R.id.chinanet_item_select)
    ImageView chinanetItemSelect;
    @BindView(R.id.layout3)
    LinearLayout layout3;
    @BindView(R.id.city_title11)
    TextView cityTitle11;
    @BindView(R.id.city_item_text)
    TextView cityItemText;
    @BindView(R.id.city_item_select)
    ImageView cityItemSelect;
    @BindView(R.id.city_select)
    LinearLayout citySelect;
    @BindView(R.id.city_preserve)
    Button cityPreserve;
    //选择的运营商
    private String operation = "";
    //城市选择
    private CityPicker cityPicker;//城市选择器
    //选择的城市
    private String city_selected = "";
    private LoadingDialog loadingDialog;//加载框
    private CustomerDialog customerDialog;
  //  private boolean judgeExist = false;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case addFalut:
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    ToastUtils.showShort(CityActivity.this, "上传失败");
                    break;
                case addSuccess:
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
//                    int a = Integer.parseInt(((String)msg.obj).trim());
//                    int b = 1;
/*

                    int x = Integer.valueOf((String)msg.obj);
                    int y = Integer.valueOf("1");

                    Log.i("zw810",x+"+"+y);
*/
                   // String s= (String) msg.obj;
                   // Log.i("zw810",s+"+*****");
                    if ((Boolean) msg.obj) {
                        ToastUtils.showShort(CityActivity.this, "上传成功");
                    } else {
                        ToastUtils.showShort(CityActivity.this, "上传失败");
                    }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_city;
    }

    @Override
    public void findView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {


    }
    @Override
    public void initListener() {

    }
    @Override
    public void initData() {
        setLayout(0);
        operation = "移动";


    }
    @OnClick({R.id.city_back, R.id.layout1, R.id.layout2, R.id.layout3, R.id.city_item_select, R.id.city_preserve})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.city_back:
                setResult(1);
                finish();
                break;
            case R.id.layout1:
                setLayout(0);
                operation = "移动";
                break;
            case R.id.layout2:
                setLayout(1);
                operation = "联通";
                break;
            case R.id.layout3:
                setLayout(2);
                operation = "电信";
                break;
            case R.id.city_item_select:
                selectCity();
                break;
            case R.id.city_preserve:
               // judgeExistThread(city_selected,operation);
                if (operation.equals("") || TextUtils.isEmpty(operation) || city_selected.equals("") || TextUtils.isEmpty(city_selected)) {
                    ToastUtils.showShort(CityActivity.this, "上传的数据信息不完整");
                    return;
                }
           /*     if (judgeExist){
                    Toast.makeText(getApplicationContext(),"该城市运营商已存在，请勿重复添加!",Toast.LENGTH_SHORT).show();
                }else {
                    addDataDialog();
                }*/
                addDataDialog();
                break;

        }
    }
    /**
     * 条目点击监听
     *
     * @param index
     */
    private void setLayout(int index) {
        //先执行清空
        clear();
        switch (index) {
            case 0:
                mobileItemImg.setImageResource(R.mipmap.mobile_1);
                mobileItemText.setTextColor(getResources().getColor(R.color.begin_buttnon1));
                mobileItemSelect.setImageResource(R.mipmap.opera_selected);
                break;
            case 1:
                unicomItemImg.setImageResource(R.mipmap.unicom_1);
                unicomItemText.setTextColor(getResources().getColor(R.color.begin_buttnon1));
                unicomItemSelect.setImageResource(R.mipmap.opera_selected);
                break;
            case 2:
                chinanetItemImg.setImageResource(R.mipmap.chinanet_1);
                chinanetItemText.setTextColor(getResources().getColor(R.color.begin_buttnon1));
                chinanetItemSelect.setImageResource(R.mipmap.opera_selected);
                break;
        }
    }
    /**
     * 条目状态清空
     */
    private void clear() {
        mobileItemImg.setImageResource(R.mipmap.mobile);
        mobileItemText.setTextColor(getResources().getColor(R.color.bg_gray));
        mobileItemSelect.setImageResource(R.mipmap.opera_unselected);
        unicomItemImg.setImageResource(R.mipmap.unicom);
        unicomItemText.setTextColor(getResources().getColor(R.color.bg_gray));
        unicomItemSelect.setImageResource(R.mipmap.opera_unselected);
        chinanetItemImg.setImageResource(R.mipmap.chinanet);
        chinanetItemText.setTextColor(getResources().getColor(R.color.bg_gray));
        chinanetItemSelect.setImageResource(R.mipmap.opera_unselected);
    }
    /**
     * 选择城市
     */
    private void selectCity() {
        cityPicker = new CityPicker.Builder(CityActivity.this)
                .textSize(18)
                .title("城市选择")
                .backgroundPop(0x66000000)
                .titleBackgroundColor("#FFFFFF")
                .titleTextColor("#696969")
                .confirTextColor("#0076FF")
                .cancelTextColor("#0076FF")
                .province("北京市")
                .city("北京市")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(11)
                .onlyShowProvinceAndCity(true)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //城市
                String city = citySelected[1];
                if (city.contains("市")) {
                    String city1 = city.substring(0, city.length() - 1);
                    city_selected = city1;
                    cityItemText.setText(city1);
                } else if (city.contains("地区")) {
                    String city1 = city.substring(0, city.length() - 2);
                    city_selected = city1;
                    cityItemText.setText(city1);
                } else {
                    city_selected = city;
                    cityItemText.setText(city);
                }
            }
            @Override
            public void onCancel() {
                ToastUtils.showShort(CityActivity.this, "已取消");
            }
        });
    }
    private void addDataDialog() {

        customerDialog = new CustomerDialog(CityActivity.this, new Callback() {
            @Override
            public void positiveCallback() {
                loadingDialog = new LoadingDialog(CityActivity.this);
                loadingDialog.setContent("正在上传...            ");
                loadingDialog.setIndicator(25);
                loadingDialog.setCancelable(false);
                loadingDialog.show();
                upLoadThread(city_selected, operation);
            }
            @Override
            public void negativeCallback() {
            }
        });
        customerDialog.setContent("提示" + "\n确认添加 " + "( " + city_selected + "-" + operation + " )" + " ?");
        customerDialog.setCancelable(false);
        customerDialog.show();
    }
    /**
     * 上传数据
     */
    private void upLoadThread(final String city, final String operator) {
        Thread th = new Thread() {
            @Override
            public void run() {
                String url = Urlutils.getInsertCity();
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("city",city);
                hashMap.put("operator",operator);
                OkHttpUtils
                        .post()
                        .url(url)
                        .params(hashMap)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(addFalut);
                            }
                            @Override
                            public void onResponse(String response, int id) {
                                Message message = myHandler.obtainMessage();
                                message.what = addSuccess;
                                message.obj = MyJsonUtil.getBeanByJson(response);
                               // message.arg1 = Integer.parseInt(response);
                                myHandler.sendMessage(message);
                                Log.i("zw810",response+"+上传");
                            }
                        });
            }
        };
        th.start();
    }
    /**
     * 判断城市运营商是否存在
     */
   /* private void judgeExistThread(final String city, final String operator) {
        Thread th = new Thread() {
            @Override
            public void run() {
                String url = Urlutils.getJudgeExistUrl();
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("city",city);
                hashMap.put("operator",operator);
                OkHttpUtils
                        .post()
                        .url(url)
                        .params(hashMap)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                myHandler.sendEmptyMessage(addFalut);
                            }
                            @Override
                            public void onResponse(String response, int id) {
                                Message message = myHandler.obtainMessage();
                                message.what = addSuccess;
                                message.obj = response;
                                Log.i("zw811",response+"+");
                                // message.arg1 = Integer.parseInt(response);
                                myHandler.sendMessage(message);

                            }
                        });
            }
        };
        th.start();

    }*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            setResult(1);
            finish();
        }

        return false;

    }

}
