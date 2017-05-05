package com.gentlehealthcare.mobilecare.tool;

import android.util.Log;

import com.gentlehealthcare.mobilecare.constant.GlobalConstant;

/**
 * 应用程序调试LOG
 * 
 * @ClassName: TMLog
 * @Description: (Release模式，正式发布时关闭log输出)
 * @author HeRen_zyy
 */
public class CCLog {
	public static final String TAG = "移动护理";

	public static void e(String tag, String msg) {
		if (GlobalConstant.RELEASE) {
			return;
		}
		Log.e(tag, msg);
	}

	public static void e(String msg) {
		CCLog.e(TAG, msg);
	}

	public static void d(String tag, String msg) {
		if (GlobalConstant.RELEASE) {
			return;
		}
		Log.d(tag, msg);
	}

	public static void d(String msg) {
		CCLog.d(TAG, msg);
	}

	public static void w(String tag, String msg) {
		if (GlobalConstant.RELEASE) {
			return;
		}
		Log.w(tag, msg);
	}

	public static void w(String msg) {
		CCLog.w(TAG, msg);
	}

	public static void v(String tag, String msg) {
		if (GlobalConstant.RELEASE) {
			return;
		}
		Log.v(tag, msg);
	}

	public static void v(String msg) {
		CCLog.v(TAG, msg);
	}
	public static void i(String tag, String msg) {
		if (GlobalConstant.RELEASE) {
			return;
		}
		Log.i(tag, msg);
	}
	
	public static void i(String msg) {
		CCLog.i(TAG, msg);
	}

}
