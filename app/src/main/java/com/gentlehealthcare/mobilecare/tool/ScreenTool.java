package com.gentlehealthcare.mobilecare.tool;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 获取屏幕属性
 * 
 * @author Torrey
 * 
 */
public class ScreenTool {

	public static DisplayMetrics getDisplayMetrics(Activity activity) {

		DisplayMetrics displayMetrics = new DisplayMetrics();

		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		return displayMetrics;

	}

	/**
	 * 获取屏幕宽
	 * 
	 * @param activity
	 * @return
	 */
	public static int getScreenWidth(Activity activity) {

		return getDisplayMetrics(activity).widthPixels;

	}

	/**
	 * 获取屏幕高
	 * 
	 * @param activity
	 * @return
	 */
	public static int getScreenHeight(Activity activity) {

		return getDisplayMetrics(activity).heightPixels;

	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dipToPx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int pxToDip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
