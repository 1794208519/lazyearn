package com.base;

import java.lang.reflect.Field;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.uidemo.R;
import com.utils.VolleyManager;


public abstract class BaseDeviceFgmnt extends Fragment
{
	protected String TAG = getClass().getName();
	protected Object requestTag = new Object();
	protected View mRootView;
	protected Context context;
	protected ProgressDialog dialog;
	public static DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
			.considerExifParams(true).showImageOnFail(R.drawable.a)
			.showImageOnLoading(R.drawable.a).showImageForEmptyUri(R.drawable.a).build();


	@Override
	public void onAttach(Context context)
	{
		this.context = context;
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (mRootView == null)
		{
			mRootView = inflater.inflate(getLayoutId(), container, false);
			init(inflater, container, savedInstanceState);
		}

		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null)
		{
			parent.removeAllViews();
		}
		return mRootView;
	}

	protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	}

	protected View findViewById(int id)
	{
		return mRootView.findViewById(id);
	}

	/**
	 * 设置一个layout id. 子类不调用此父类的
	 * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)},则此方法没用
	 * 
	 * @return
	 */
	protected abstract @LayoutRes
	int getLayoutId();

	@Override
	public void onDetach()
	{
		VolleyManager.getInstance().cancelAll(requestTag);
		try
		{
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e)
		{

		} catch (IllegalAccessException e)
		{

		}
		super.onDetach();

	}


	protected void showProgressDialog(String message) {
		if (dialog == null) {
			dialog = ProgressDialog.show(context, null, message);
		} else {
			dialog.setMessage(message);
			dialog.show();
		}
	}

	protected void dismissProgressDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}


}
