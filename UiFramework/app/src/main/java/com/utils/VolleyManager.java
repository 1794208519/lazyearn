package com.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyManager {
	private static VolleyManager instance;
	private RequestQueue mQueue;

	/** Returns singleton class instance */
	public static VolleyManager getInstance() {
		if (instance == null) {
			synchronized (VolleyManager.class) {
				if (instance == null) {
					instance = new VolleyManager();
				}
			}
		}
		return instance;
	}

	private VolleyManager() {
	}

	public void init(Context context) {
		if (context == null) {
			new NullPointerException("init VolleyManager error: context is null.");
		}

		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context.getApplicationContext());
			mQueue.start();
		}
	}

	@SuppressWarnings("rawtypes")
	public Request addRequest(Request request, Object tag) {
		if (mQueue == null) {
			new RuntimeException("VolleyManager is not inited.");
		}

		if (tag != null) {
			request.setTag(tag);
		}
		return mQueue.add(request);
	}

	public void cancelAll(Object tag) {
		if (mQueue == null) {
			new RuntimeException("VolleyManager is not inited.");
		}

		mQueue.cancelAll(tag);
	}

	public RequestQueue getRequestQueue() {
		return mQueue;
	}
}
