package com.gentlehealthcare.mobilecare.tool;

import android.content.Context;
import android.telephony.TelephonyManager;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * @author zyy
 * @class DateTool
 * @date 2012-11-23 上午12:31:20
 * @description 获取设备ID
 */
public class DeviceTool {

 public static String getDeviceId(Context context){
     TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
     String szImei = TelephonyMgr.getDeviceId();
     return szImei;
 }

}
