package com.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.base.BaseFragment;
import com.ui.Adapter.MyFragmentPagerAdapter;
import com.uidemo.R;

import java.util.ArrayList;

/**
 * Created by vicmob_yf002 on 2017/5/3.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager viewPager;// 页卡内容
    private int currIndex = 0;// 当前页卡编号
    // viewpager页面fragment列表
    private ArrayList<Fragment> fragmentsList;
    private LeftUpdateFragment leftFragment;
    private RightFragment rightFragment;
    private Button mLeftBtn;
    private Button mRightBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.home_frag_layout, null);
        InitViewPager(view);
        return view;
    }

    /**
     * viewpager页面初始化
     *
     * @param parentView
     */
    private void InitViewPager(View parentView) {
        viewPager = (ViewPager) parentView.findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();
        leftFragment = new LeftUpdateFragment();
        rightFragment = new RightFragment();
        fragmentsList.add(leftFragment);
        fragmentsList.add(rightFragment);
        // viewpager使用适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(mActivity.getFragmentManager(), fragmentsList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setOffscreenPageLimit(1);
    }

    @Override
    public void initView() {
        //初始化控件
        mLeftBtn = (Button) mActivity.findViewById(R.id.buttonLeft);
        mRightBtn = (Button) mActivity.findViewById(R.id.buttonRight);

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        //初始化选中的状态
        mLeftBtn.setBackground(getResources().getDrawable(R.drawable.button_left1));
        mLeftBtn.setTextColor(getResources().getColor(R.color.font_blue));

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLeft:
                clearSelection();
                mLeftBtn.setBackground(getResources().getDrawable(R.drawable.button_left1));
                mLeftBtn.setTextColor(getResources().getColor(R.color.font_blue));
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.buttonRight:
                clearSelection();
                mRightBtn.setBackground(getResources().getDrawable(R.drawable.button_right1));
                mRightBtn.setTextColor(getResources().getColor(R.color.font_blue));
                viewPager.setCurrentItem(1, false);//false表示切换的时候，不需要切换时间
                break;
        }
    }

    /**
     * 切换到左边
     */
    public void clickLeft() {
        clearSelection();
        mLeftBtn.setBackground(getResources().getDrawable(R.drawable.button_left1));
        mLeftBtn.setTextColor(getResources().getColor(R.color.font_blue));
        viewPager.setCurrentItem(0, false);
    }

    /**
     * 清空所有状态
     */
    public void clearSelection() {
        mLeftBtn.setBackground(getResources().getDrawable(R.drawable.button_left));
        mLeftBtn.setTextColor(getResources().getColor(R.color.white));
        mRightBtn.setBackground(getResources().getDrawable(R.drawable.button_right));
        mRightBtn.setTextColor(getResources().getColor(R.color.white));
    }


    /**
     * 页面改变监听
     *
     * @author Administrator
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageSelected(int arg0) {

            currIndex = arg0;
            switch (currIndex) {
                case 0:
                    clearSelection();
                    mLeftBtn.setBackground(getResources().getDrawable(R.drawable.button_left1));
                    mLeftBtn.setTextColor(getResources().getColor(R.color.font_blue));
                    break;
                case 1:
                    clearSelection();
                    mRightBtn.setBackground(getResources().getDrawable(R.drawable.button_right1));
                    mRightBtn.setTextColor(getResources().getColor(R.color.font_blue));
                    break;
            }
        }

    }

}
