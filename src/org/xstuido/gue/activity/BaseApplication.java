package org.xstuido.gue.activity;

import android.app.Application;
import android.content.Context;

/**
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 */
public class BaseApplication extends Application {

	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
	}

	public static Context getContext() {
		return mContext;
	}

}
