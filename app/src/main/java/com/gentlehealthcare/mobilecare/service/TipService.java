package com.gentlehealthcare.mobilecare.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.RemoteViews;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.ActivityControlTool;
import com.gentlehealthcare.mobilecare.activity.notification.Notification2Activity;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HeRen_Zyy E-mail:395670690@qq.com
 * @类说明：本服务用于3分钟轮询服务器
 **/
public class TipService extends Service {

    private static final String TAG = "TipService";
    /**
     * 消息集合
     */
    public List<TipBean> mTips = new ArrayList<TipBean>();
    private Handler handler=null;
    private Runnable runnable;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
        CCLog.i(TAG, "服务onStart");
        RequestTip mRequestTip = new RequestTip();
        mRequestTip.start();
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        // 保持cpu一直运行，不管屏幕是否黑屏
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
        wakeLock.acquire();
        //loadNotice();
    }

    private void loadNotice() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(this, 6000);
            }
        };
        handler.postDelayed(runnable, 6000);
    }

    /**
     * 循环遍历消息集合
     *
     * @author Zyy
     **/
    private void showNotification() {
        // 保证消息集合中至少有一条消息存在
        if (mTips.size() >= 1) {
            // 由于每发送一条需要将消息集合清空涉及到线程安全问题，加判断防止nullpointException出现
            if (mTips != null) {
                for (int i = 0; i < mTips.size(); i++) {
                    TipBean mTipBean = new TipBean();
                    if (mTips != null) {
                        mTipBean = mTips.get(0);
                        String noticeStartTime = mTipBean.getNoticeStartTime();
                        String noticeId = mTipBean.getNoticeId();
                        String creatTime = mTipBean.getCreateTime();
                        String creatUser = mTipBean.getCreateUser();
                        String bedLable = mTipBean.getBedLabel();
                        String patName = mTipBean.getPatName();
                        String messageContent = mTipBean.getMessageContent();
                        compareTimeToShowNotification(noticeStartTime, noticeId, creatTime, creatUser, bedLable, patName, messageContent);
                        ListRemove(noticeId);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * 删除list中的某条数据
     *
     * @author Zyy
     */
    private void ListRemove(String noticeid) {
        for (TipBean tipBean : mTips) {
            if (tipBean.getNoticeId().equals(noticeid)) {
                mTips.remove(tipBean);
                break;
            }
        }
    }

    /**
     * 比较两个日期大小
     *
     * @author Zyy
     **/
    private void compareTimeToShowNotification(String t, String noticeid,
                                               String createTime, String createUser, String bedLabel,
                                               String patName, String messageContent) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date();
        String nowTime = "";
        Date date = null;
        Date now = null;
        nowTime = df.format(dt);
        try {
            now = df.parse(nowTime);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        try {
            date = df.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = date.getTime() - now.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        // day == 0保证消息时当天的
        // hour == 0保证消息是当前时间的
        // min < 3弥补3分钟轮询服务器的时差
        //day == 0 && hour == 0&&min<3
//        if (day == 0) {
//            addNotificaction(noticeid, createTime, createUser, bedLabel,
//                    patName, messageContent);
//        }
        addNotificaction(noticeid, createTime, createUser, bedLabel,
                patName, messageContent);
    }

    /**
     * 访问网络获取提示信息
     *
     * @author Zyy
     **/
    private void accessNet() {
        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        String url = UrlConstant.GetTipMsg() + "username=" + UserInfo.getUserName() + "&wardCode=" + UserInfo.getWardCode();
        CCLog.i(TAG, "加载输血提醒数据>>>" + url);
        params.addBodyParameter("username", UserInfo.getUserName());
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                CCLog.i(TAG, "请求巡视消息时出错了");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<TipBean>>() {
                }.getType();
                try {
                    JSONObject jsonObject = new JSONObject(arg0.result);
                    boolean result = jsonObject.getBoolean("result");
                    if (result) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        mTips = gson.fromJson(jsonArray.toString(), type);
                        showNotification();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 加载通知条数
     */
    private void loadNoticeNumber() {
        HttpUtils http = new HttpUtils();
        String url = UrlConstant.loadNotificationNum() + UserInfo.getUserName() + "&patId=null" +
                "&confirmStatus=1" + "&wardCode=" + UserInfo.getWardCode() + "&flag=0";
        CCLog.i("加载通知条数：" + url);
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                try {
                    JSONObject jsonobject = new JSONObject(responseInfo.result);
                    int count = jsonobject.getInt("count");
                    if (count > 0) {
                        showNotificaction(count + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(HttpException e, String s) {
                CCLog.i(e.toString());
            }
        });
    }

    /**
     * 显示通知
     */
    private void showNotificaction(String num) {
        Intent intent = new Intent();
        ComponentName component = new ComponentName(ActivityControlTool.currentActivity, Notification2Activity
                .class.getName());
        intent.setComponent(component);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent contentIntent = PendingIntent.getActivity(ActivityControlTool.context, 0, intent, 0);
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.number = 0;
        notification.tickerText = "护理通知";
        notification.defaults = Notification.DEFAULT_ALL;
        notification.defaults = Notification.DEFAULT_VIBRATE;
        RemoteViews rv = new RemoteViews(
                ActivityControlTool.currentActivity.getPackageName(),
                R.layout.notification);
        rv.setImageViewResource(R.id.im_notification_, R.drawable.ic_launcher);
        rv.setTextViewText(R.id.tv_notification_title, getResources().getString(R.string.app_name));
        rv.setTextViewText(R.id.tv_notification_currtime, getCurrentHM());
        rv.setTextViewText(R.id.tv_tip_news, "您有" + num + "条护理通知");
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;
        notification.contentView = rv;
        notification.contentIntent = contentIntent;
        NotificationManager nm = (NotificationManager) ActivityControlTool.currentActivity.getSystemService
                (Activity.NOTIFICATION_SERVICE);
        nm.notify(R.string.app_name, notification);
    }

    /**
     * 添加一个notification
     **/
    @SuppressWarnings("deprecation")
    private void addNotificaction(String id, String createTime,
                                  String createUser, String bedLabel, String patName,
                                  String messageContent) {

        Intent intent = new Intent();
        // 消息ID
        intent.putExtra("noticeId", id);
        // 创始人姓名
        intent.putExtra("createUser", createUser);
        // 病人床号
        intent.putExtra("bedLabel", bedLabel);
        // 消息创建时间
        intent.putExtra("createTime", createTime);
        // 病人姓名
        intent.putExtra("patName", patName);
        // 消息体
        intent.putExtra("messageContent", messageContent);
        CCLog.i(TAG, "service将要传的值>>>" + messageContent);

//        ComponentName component = new ComponentName(
//                ActivityControlTool.currentActivity,
//                Notification2Activity.class.getName());
//        intent.setComponent(component);
//        intent.setAction("android.intent.action.MAIN");
//        intent.addCategory("android.intent.category.LAUNCHER");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(
//                ActivityControlTool.context, 0, intent, 0);
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.number = 0;
        notification.tickerText = "护理通知";
        notification.defaults = Notification.DEFAULT_ALL;
        notification.defaults = Notification.DEFAULT_VIBRATE;
        RemoteViews rv = new RemoteViews(
                ActivityControlTool.currentActivity.getPackageName(),
                R.layout.notification);
        rv.setImageViewResource(R.id.im_notification_, R.drawable.ic_launcher);
        rv.setTextViewText(R.id.tv_notification_title, "护理通知");
        rv.setTextViewText(R.id.tv_notification_currtime, getCurrentHM());
        rv.setTextViewText(R.id.tv_tip_news, messageContent + "床输血15分钟,请巡视");
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.contentView = rv;
        // notification.contentIntent = contentIntent;
        NotificationManager nm = (NotificationManager) ActivityControlTool.currentActivity
                .getSystemService(Activity.NOTIFICATION_SERVICE);
        nm.notify(R.string.app_name, notification);
    }

    /**
     * 当前时间
     *
     * @author Zyy
     **/
    private String getCurrentHM() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm");
        String date = sdf.format(new Date());
        return date;
    }

    @Override
    public void onDestroy() {
    	  wakeLock.release();
        handler.removeCallbacks(runnable);
    }

    /**
     * 内部类用来轮询服务器 3minus/次
     *
     * @author Zyy
     **/
    class RequestTip extends Thread {

        public void run() {
            while (true) {
                try {
                    //loadNoticeNumber();
                    accessNet();
                    sleep(180000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
