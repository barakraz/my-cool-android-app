package com.example.mycoolapp.singletones;

import android.content.Context;

public class contextObj {
	private static contextObj instance = null;

	public static contextObj getInstance() {
		if (instance == null) {
			instance = new contextObj();
		}
		return instance;
	}

	private Context mContext;

	protected contextObj() {
		// Exists only to defeat instantiation.
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
}