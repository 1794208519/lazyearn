package com.ui.fragment;


import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Interface.View.OnLongClickListener;
import com.MyApp.MyApplication;
import com.base.BaseFragment;
import com.nineoldandroids.view.ViewHelper;
import com.ui.activity.AccountActivity;
import com.uidemo.R;
import com.utils.VibratorUtil;
import com.widget.DragLayout;

import java.lang.reflect.Field;


/**
 * 当前类注释:第一个Fragment
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    /**
     * 用于展示内容页面的Fragment
     */
    public HomeFragment homeFragment;
    /**
     * 用于展示地图的Fragment
     */
    public AddressFragment addressFragment;
    /**
     * 用于展示设置的Fragment
     */
    public SettingFragment settingFragment;

    public LeftFragment leftFragment;
    public RightFragment rightFragment;

    /**
     * 主页界面布局
     */
    private View contentlayout;
    /**
     * 地图界面布局
     */
    private View addresslayout;
    /**
     * 设置界面布局
     */
    private View setlayout;

    /**
     * 在Tab布局上显示主页图标的控件
     */
    private ImageView Imagecontent;
    /**
     * 在Tab布局上显示地图图标的控件
     */
    private ImageView Imageaddress;
    /**
     * 在Tab布局上显示设置图标的控件
     */
    private ImageView Imagesetting;

    private TextView Textcontent;
    private TextView Textaddress;
    private TextView Textsetting;
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    private LinearLayout mLlBut;
    private TextView mTitle;

    private ImageView ivIcon;
    private DragLayout dl;
    private int currentIndex = 0;
    private MyApplication app;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.base_fragment, container, false);
        mView.setOnTouchListener(this);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // 实例化fragment
        fragmentManager = getFragmentManager();
        setTabSelection(0);
        app = MyApplication.getInstance();
        currentIndex = 1;
    }
    /**
     * 初始化视图
     */
    public void initView() {
        //初始化控件
        contentlayout = mActivity.findViewById(R.id.content_layout);
        addresslayout = mActivity.findViewById(R.id.addre_layout);
        setlayout = mActivity.findViewById(R.id.setting_layout);

        Imagecontent = (ImageView) mActivity.findViewById(R.id.content_image);
        Imageaddress = (ImageView) mActivity.findViewById(R.id.address_image);
        Imagesetting = (ImageView) mActivity.findViewById(R.id.setting_image);

        Textcontent = (TextView) mActivity.findViewById(R.id.content_text);
        Textaddress = (TextView) mActivity.findViewById(R.id.address_text);
        Textsetting = (TextView) mActivity.findViewById(R.id.setting_text);

        mTitle = (TextView) mActivity.findViewById(R.id.tv_title);
        mLlBut = (LinearLayout) mActivity.findViewById(R.id.ll_but);

        //侧滑菜单控件
        dl = (DragLayout) mActivity.findViewById(R.id.dl);
        ivIcon = (ImageView) mActivity.findViewById(R.id.iv_icon);

        leftFragment = new LeftFragment();
        rightFragment = new RightFragment();

        contentlayout.setOnClickListener(this);
        addresslayout.setOnClickListener(this);
        setlayout.setOnClickListener(this);
        ivIcon.setOnClickListener(this);
       // ivIcon.setOnLongClickListener(new OnLongClickListenerImpl());
        dl.setDragListener(new DragLayout.DragListener() {
            //界面打开的时候
            @Override
            public void onOpen() {
            }

            //界面关闭的时候
            @Override
            public void onClose() {
                // 让图标晃动
                ObjectAnimator mAnim = ObjectAnimator.ofFloat(ivIcon, "translationX", 5.0f);
                mAnim.setInterpolator(new CycleInterpolator(2));
                mAnim.setDuration(300);
                mAnim.start();
            }

            //界面滑动的时候
            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(ivIcon, 1 - percent);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_layout:
                if (currentIndex != 1) {
                    currentIndex = 1;
                    // 当点击了主页tab时，选中第1个tab
                    setTabSelection(0);
                } else {
                    //切换到左边fragment
                    homeFragment.clickLeft();
                }
                break;
            case R.id.addre_layout:
                currentIndex = 2;
                // 当点击了地图tab时，选中第1个tab
                setTabSelection(1);
                mTitle.setText("地图");
                break;
            case R.id.setting_layout:
                currentIndex = 3;
                // 当点击了设置tab时，选中第1个tab
                setTabSelection(2);
                mTitle.setText("设置");
                break;
            case R.id.iv_icon:
                dl.open();
                break;
            default:
                break;
        }
    }


    /**
     * 长按事件监听
     */
    private class OnLongClickListenerImpl implements OnLongClickListener, View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            try {
                showPopupMenu(v);
                backgroundAlpha(0.7f);
                //震动
                VibratorUtil.Vibrate(mActivity, 100);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示主页，1表示地图，2表示设置。
     */
    public void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        mTitle.setVisibility(View.VISIBLE);
        switch (index) {
            case 0:
                mLlBut.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.INVISIBLE);
                // 当点击了主页tab时，改变控件的图片和文字颜色
                Imagecontent.setImageResource(R.mipmap.mainbtn1);
                Textcontent.setTextColor(Color.parseColor("#1d84ef"));
                if (homeFragment == null) {
                    // 如果contentFragment为空，则创建一个并添加到界面上
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.realcontent, homeFragment);
                    // transaction.addToBackStack(null);//回退方法
                } else {
                    // 如果contentFragment不为空，则直接将它显示出来
                    transaction.show(homeFragment);
                }

                break;
            case 1:
                mLlBut.setVisibility(View.GONE);
                // 当点击了地图tab时，改变控件的图片和文字颜色
                Imageaddress.setImageResource(R.mipmap.addressbtn1);
                Textaddress.setTextColor(Color.parseColor("#1d84ef"));
                if (addressFragment == null) {
                    // 如果addressFragment为空，则创建一个并添加到界面上
                    addressFragment = new AddressFragment();

                    transaction.add(R.id.realcontent, addressFragment);
                } else {
                    // 如果addressFragment不为空，则直接将它显示出来
                    transaction.show(addressFragment);
                }

                break;
            case 2:
                mLlBut.setVisibility(View.GONE);
                // 当点击了设置tab时，改变控件的图片和文字颜色
                Imagesetting.setImageResource(R.mipmap.settingbtn1);
                Textsetting.setTextColor(Color.parseColor("#1d84ef"));
                if (settingFragment == null) {
                    // 如果settingFragment为空，则创建一个并添加到界面上
                    settingFragment = new SettingFragment();

                    transaction.add(R.id.realcontent, settingFragment);
                } else {
                    // 如果settingFragment不为空，则直接将它显示出来
                    transaction.show(settingFragment);
                }

                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        Imagecontent.setImageResource(R.mipmap.mainbtn);
        Textcontent.setTextColor(Color.parseColor("#999999"));
        Imageaddress.setImageResource(R.mipmap.addressbtn);
        Textaddress.setTextColor(Color.parseColor("#999999"));
        Imagesetting.setImageResource(R.mipmap.settingbtn);
        Textsetting.setTextColor(Color.parseColor("#999999"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (addressFragment != null) {
            transaction.hide(addressFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }

    }

    /**
     * 显示popupMenu
     *
     * @param view
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void showPopupMenu(View view) throws NoSuchFieldException, IllegalAccessException {

        PopupMenu popupMenu = new PopupMenu(mActivity, view);
        //设置 PopupMenu 的显示菜单项
        popupMenu.inflate(R.menu.main);
        // popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());//与一行没什么区别
        //默认 PopupMenu 不显示条目icon，可以通过反射来强制使其显示icon
        Field field = popupMenu.getClass().getDeclaredField("mPopup");
        field.setAccessible(true);
        final MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
        mHelper.setForceShowIcon(true);
        //设置 PopupMenu 的条目点击事件（点击后会自动dismiss）
        popupMenu.setOnMenuItemClickListener(new popupMenuItemClickListener());
        //设置消失监听
        popupMenu.setOnDismissListener(new poponDismissListener());
        //显示 PopupMenu
        popupMenu.show();
    }

    /**
     * 添加popupMenu点击item监听
     */
    class popupMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pop_menu1:
                    Intent intent = new Intent(mActivity, AccountActivity.class);
                    startActivity(intent);
                    startThread();
                    Toast.makeText(mActivity, item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
//                case R.id.pop_menu2:
//                    backgroundAlpha(1f);
//                    Toast.makeText(mActivity, item.getTitle(), Toast.LENGTH_SHORT).show();
//                    return true;
            }
            return false;
        }
    }

    /**
     * 添加弹出的popupWindow关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupMenu.OnDismissListener {

        @Override
        public void onDismiss(PopupMenu menu) {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        VibratorUtil.Vibrate(mActivity);
    }

    @Override
    public void onStop() {
        super.onStop();
        VibratorUtil.Vibrate(mActivity);
    }

    private void startThread() {
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                backgroundAlpha(1f);
            }
        }, 300);
    }

}
