package com.gentlehealthcare.mobilecare.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.service.TipService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Activity控制工具类
 */
public class ActivityControlTool {
    public static final String TAG = ActivityControlTool.class.getSimpleName();
    public static boolean SHOW = false;
    public static Context context;
    public static BaseActivity currentActivity = null;
    public static ArrayList<BaseActivity> activityList = new ArrayList<BaseActivity>();
    public static boolean destroy = true;
    private static List<BaseActivity> backActivityList=new ArrayList<BaseActivity>();
    private static List<Map<String ,Object>> forwordActivityList=new ArrayList<Map<String, Object>>();
    static HashMap<BaseActivity, Boolean> map = new HashMap<BaseActivity, Boolean>();

    /**
     * 添加Activity
     * @param activity
     */
    public static void add(BaseActivity activity) {
        context = activity;
        activityList.add(activity);
        currentActivity = activity;
        Log.d("ActivityControlTool",
                "add " + ((BaseActivity) activity).getLocalClassName());
    }


    public static void onStop(BaseActivity activityName) {
        map.put(activityName, false);
        onCheckStop(activityName);
    }

    public static void onDestroy(BaseActivity activityName) {
        map.remove(activityName);
        check(activityName);
    }

    public static void onResume(BaseActivity activityName) {
        map.put(activityName, true);
    }

    /**
     * 所有activity进入onstop（）中，隐藏手势区域
     * @param activityName
     */
    private synchronized static void onCheckStop(BaseActivity activityName){
        int active = 0;
        Set<BaseActivity> keys = map.keySet();
        for (Activity key : keys) {
            if (map.get(key)) {
                active++;
            }
        }
        if(active==0) {
            activityName.HidnGestWindow();
        }
    }

    /**
     * 当所有Activity销毁，停止红外扫描广播
     * @param activityName
     */
    private synchronized static void check(BaseActivity activityName) {
        int active = 0;
        Set<BaseActivity> keys = map.keySet();
        for (Activity key : keys) {
            if (map.get(key)) {
                active++;
            }
        }
        if(active==0){
            currentActivity.stopService(new Intent(currentActivity, CamerService.class));
        }
    }

    /**
     *
     * @author tangshuai
     * @param destroy
     */
    public static void finishAll(boolean destroy) {
        ActivityControlTool.destroy = destroy;
        finishAll();
        ActivityManager am = (ActivityManager) currentActivity
                .getSystemService(Activity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(currentActivity.getPackageName()); // API
        currentActivity.stopService(new Intent(currentActivity, CamerService.class));
        currentActivity.stopService(new Intent(currentActivity,TipService.class));
        System.exit(0);
    }

    private synchronized static void finishAll() {
        Log.i(TAG, "finishAll->>>");
        SHOW = false;
        synchronized (activityList) {//防止list同步被修改
            if (currentActivity != null) {
            }
            int size = activityList.size();
            if (size > 0) {
                @SuppressWarnings("unchecked")
                ArrayList<BaseActivity> as = (ArrayList<BaseActivity>) activityList
                        .clone();
                for (int i = 0; i < size; i++) {
                    if (!as.get(i).isFinishing()) {
                        as.get(i).finish();
                    }
                }
                activityList.clear();
            }
            if (destroy) {
                Toast.makeText(context,"停止服务...",Toast.LENGTH_LONG).show();
                currentActivity.stopService(new Intent(currentActivity, CamerService.class));
                currentActivity.stopService(new Intent(currentActivity,TipService.class));
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                destroy = true;
            }

        }
    }

    public static void remove(BaseActivity activity) {
        synchronized (activityList) {
            int size = activityList.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    if (activityList.get(i) == activity) {
                        activityList.remove(i);
                        break;
                    }
                }
            }
            if (activityList.size() > 0) {
                currentActivity = activityList.get(activityList.size() - 1);
            }
            for (int i = 0; i < activityList.size(); i++) {
                Log.d("ActivityControlTool", "after remove "
                        + activityList.get(i).getLocalClassName() + "  " + i);
            }
        }
    }
}
