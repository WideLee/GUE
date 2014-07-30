﻿package org.xstuido.gue.util;

import java.util.Calendar;

import org.xstuido.gue.R;
import org.xstuido.gue.activity.BaseApplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 工具类包含了所有常用的函数
 * 
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * 
 */

public class Tool {
	public static final int NETWORK_NOT_CONNECTED = -1;
	public static final int NETWORK_TYPE_2G = 0;
	public static final int NETWORK_TYPE_3G = 1;
	public static final int NETWORK_TYPE_WIFI = 2;

	protected Tool() {
	}

	/**
	 * 获取当前联网的网络类型，可能是3G，2G，WiFi或者未连接
	 * 
	 * @return 网络类型代码
	 */
	public static int getNetworkType() {
		ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		int networkType = NETWORK_TYPE_2G;
		if (info == null) {
			networkType = NETWORK_NOT_CONNECTED;
		} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			networkType = NETWORK_TYPE_WIFI;
		} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			switch (info.getSubtype()) {
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_IDEN:
				networkType = NETWORK_TYPE_2G;
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
			case TelephonyManager.NETWORK_TYPE_LTE:
				networkType = NETWORK_TYPE_3G;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			default:
				networkType = NETWORK_TYPE_2G;
				break;
			}
		}
		return networkType;
	}

	/**
	 * dip屏幕密度与px像素转换
	 * 
	 * @param dip
	 *            要转换的屏幕密度
	 * @return 对应的像素值
	 */
	public static int dip2px(float dip) {
		final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * dip屏幕密度与px像素转换
	 * 
	 * @param dip
	 *            要转换的像素值
	 * @return 对应的屏幕密度
	 */
	public static float px2dip(float px) {
		final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
		return px / scale + 0.5f;
	}

	private static int screenW = -1, screenH = -1;

	/**
	 * 获取当前屏幕的宽度
	 * 
	 * @return 屏幕宽度
	 */
	public static int getScreenW() {
		if (screenW < 0) {
			initScreenDisplayParams();
		}
		return screenW;
	}

	/**
	 * 获取当前屏幕的高度
	 * 
	 * @return 屏幕高度
	 */
	public static int getScreenH() {
		if (screenH < 0) {
			initScreenDisplayParams();
		}
		return screenH;
	}

	/**
	 * 初始化屏幕显示参数
	 */
	private static void initScreenDisplayParams() {
		DisplayMetrics dm = BaseApplication.getContext().getResources().getDisplayMetrics();
		screenW = dm.widthPixels;
		screenH = dm.heightPixels;
	}

	/**
	 * 弹出正常时间的Toast通知
	 * 
	 * @param toast
	 *            需要显示通知的内容
	 */
	public static void showToast(String toast) {
		if (!TextUtils.isEmpty(toast)) {
			Toast.makeText(BaseApplication.getContext(), toast, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 弹出长时间的Toast通知
	 * 
	 * @param toast
	 *            需要显示通知的内容
	 */
	public static void showLongToast(String toast) {
		if (!TextUtils.isEmpty(toast)) {
			Toast.makeText(BaseApplication.getContext(), toast, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 获取字符资源中的字符串常量
	 * 
	 * @param resId
	 *            字符串常量的ID
	 * @return 字符串常量的值
	 */
	public static String getString(int resId) {
		return BaseApplication.getContext().getString(resId);
	}

	/**
	 * 弹出输入法键盘
	 * 
	 * @param view
	 */
	public static void showKeyBoard(View view) {
		InputMethodManager imm = (InputMethodManager) BaseApplication.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 隐藏输入法键盘
	 * 
	 * @param view
	 */
	public static void hideKeyBoard(View view) {
		InputMethodManager imm = (InputMethodManager) BaseApplication.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static long seconds(int y, int m, int d) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(y, m, d);
		return calendar.getTimeInMillis() / 1000;
	}

	/**
	 * 缩放图片为给定的大小
	 * 
	 * @param src
	 *            原始尺寸的图片
	 * @param width
	 *            需要缩放到的宽
	 * @param height
	 *            需要缩放到的高
	 * @return 缩放后得到的图片
	 */
	public static Bitmap scaleBitmap(Bitmap src, int width, int height) {
		int mWidth = src.getWidth();
		int mHeight = src.getHeight();

		// 获取缩放的比例
		float scaleWidth = ((float) width) / mWidth;
		float scaleHeight = ((float) height) / mHeight;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(src, 0, 0, mWidth, mHeight, matrix, true);
	}

	/**
	 * 判断两个日期是否为同一天
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isSameDay(Calendar a, Calendar b) {
		return a.get(Calendar.YEAR) == b.get(Calendar.YEAR)
				&& a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
				&& a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回一个扇形的剪裁区
	 * 
	 * @param canvas
	 *            画笔
	 * @param center_X
	 *            圆心X坐标
	 * @param center_Y
	 *            圆心Y坐标
	 * @param r
	 *            半径
	 * @param startAngle
	 *            起始角度
	 * @param sweepAngle
	 *            终点角度
	 */
	public static void getSectorClip(Canvas canvas, float center_X, float center_Y, float r,
			float startAngle, float sweepAngle) {
		Path path = new Path();
		// 下面是获得一个三角形的剪裁区
		path.moveTo(center_X, center_Y); // 圆心
		path.lineTo((float) (center_X + r * Math.cos(startAngle * Math.PI / 180)), // 起始点角度在圆上对应的横坐标
				(float) (center_Y + r * Math.sin(startAngle * Math.PI / 180))); // 起始点角度在圆上对应的纵坐标
		path.lineTo((float) (center_X + r * Math.cos(sweepAngle * Math.PI / 180)), // 终点角度在圆上对应的横坐标
				(float) (center_Y + r * Math.sin(sweepAngle * Math.PI / 180))); // 终点点角度在圆上对应的纵坐标
		path.close();
		// 设置一个正方形,内切圆
		RectF rectF = new RectF(center_X - r, center_Y - r, center_X + r, center_Y + r);
		// 下面是获得弧形剪裁区的方法
		path.addArc(rectF, startAngle, sweepAngle - startAngle);
		canvas.clipPath(path);
	}

	/**
	 * 转换日期为字符串，转换结果为 "xx月xx日"
	 * 
	 * @param c
	 *            需要转换的日期
	 * @return 转换的结果字符串
	 */
	public static String getMonthDate(Calendar c) {
		int cm = c.get(Calendar.MONTH);
		int cd = c.get(Calendar.DAY_OF_MONTH);
		return String.format(Tool.getString(R.string.date_fmt), cm + 1, cd);
	}

	/**
	 * 转换日期格式为字符串，结果如 "一月2014"
	 * 
	 * @param c
	 *            需要转换的日期
	 * @return 转换结果的字符串
	 */
	public static String getYearMonth(Calendar c) {
		int cy = c.get(Calendar.YEAR);
		int cm = c.get(Calendar.MONTH);
		String month = new String();
		switch (cm) {
		case Calendar.JANUARY:
			month = Tool.getString(R.string.one);
			break;
		case Calendar.FEBRUARY:
			month = Tool.getString(R.string.two);
			break;
		case Calendar.MARCH:
			month = Tool.getString(R.string.three);
			break;
		case Calendar.APRIL:
			month = Tool.getString(R.string.four);
			break;
		case Calendar.MAY:
			month = Tool.getString(R.string.five);
			break;
		case Calendar.JUNE:
			month = Tool.getString(R.string.six);
			break;
		case Calendar.JULY:
			month = Tool.getString(R.string.seven);
			break;
		case Calendar.AUGUST:
			month = Tool.getString(R.string.eight);
			break;
		case Calendar.SEPTEMBER:
			month = Tool.getString(R.string.nine);
			break;
		case Calendar.OCTOBER:
			month = Tool.getString(R.string.ten);
			break;
		case Calendar.NOVEMBER:
			month = Tool.getString(R.string.eleven);
			break;
		case Calendar.DECEMBER:
			month = Tool.getString(R.string.twelve);
			break;
		}
		month += Tool.getString(R.string.month);
		return month + cy;
	}

	/**
	 * 获取一个月的第一天，指的是这个月的第一周的周日，可能是上个月的月末
	 * 
	 * @param calendar
	 *            一个月的某一天
	 * @return 这个月的第一天
	 */
	public static Calendar getFirstDayOfMonth(Calendar calendar) {
		Calendar firstDay = Calendar.getInstance();
		firstDay.setTimeInMillis(calendar.getTimeInMillis());

		int numWeek = firstDay.get(Calendar.WEEK_OF_MONTH) - 1;
		int offset = numWeek * 7 + firstDay.get(Calendar.DAY_OF_WEEK) - 1;
		firstDay.add(Calendar.DAY_OF_MONTH, -offset);

		return firstDay;
	}

	/**
	 * 获取两个日期相差的月份
	 * 
	 * @param start
	 *            开始的日期
	 * @param end
	 *            结束的日期
	 * @return 开始时间与结束时间相差的月份
	 */
	public static int getDiffMonth(Calendar start, Calendar end) {
		return (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12 + end.get(Calendar.MONTH)
				- start.get(Calendar.MONTH);
	}

	public static int getDiffDate(Calendar start, Calendar end) {
		start.set(java.util.Calendar.HOUR_OF_DAY, 0);
		start.set(java.util.Calendar.MINUTE, 0);
		start.set(java.util.Calendar.SECOND, 0);
		end.set(java.util.Calendar.HOUR_OF_DAY, 0);
		end.set(java.util.Calendar.MINUTE, 0);
		end.set(java.util.Calendar.SECOND, 0);

		int days = ((int) (end.getTime().getTime() / 1000) - (int) (start.getTime().getTime() / 1000)) / 3600 / 24 + 1;
		return days;
	}

	/**
	 * 获取当前日期
	 * 
	 * @return 当前日期Calendar的实例
	 */
	public static Calendar getCurCalendar() {
		return Calendar.getInstance();
	}

	/**
	 * 根据每周的第几天获取到中文的星期几
	 * 
	 * @param index
	 *            要获取的第几天
	 * @return 这一天的中文符号
	 */
	public static String getWeekDayName(int index) {
		String weekdayName = new String();
		switch (index) {
		case 0:
			weekdayName = getString(R.string._sunday);
			break;
		case 1:
			weekdayName = getString(R.string._monday);
			break;
		case 2:
			weekdayName = getString(R.string._tuesday);
			break;
		case 3:
			weekdayName = getString(R.string._wednesday);
			break;
		case 4:
			weekdayName = getString(R.string._thursday);
			break;
		case 5:
			weekdayName = getString(R.string._friday);
			break;
		case 6:
			weekdayName = getString(R.string._saturday);
			break;
		default:
			break;
		}
		return weekdayName;
	}
}