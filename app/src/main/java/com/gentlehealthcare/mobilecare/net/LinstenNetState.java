package com.gentlehealthcare.mobilecare.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 
 * @class LinstenNetState  监听网络状态
 * @author cally
 * @date 2012-12-13 下午1:15:46
 * @description 
 *
 */

public class LinstenNetState {
	
	/**
	 * 
	 * @description  判断当前网络链接
	 * @author cally
	 * @date 2012-12-13 下午1:15:25
	 *
	 * @param context
	 * @return
	 */
	public static  boolean isLinkState(Context context){
		if(context==null){
			return false;
		}
		ConnectivityManager	connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 活跃
		NetworkInfo	netInfo = connManager.getActiveNetworkInfo();
		// 是否有可用的
		if (netInfo != null && netInfo.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
}
