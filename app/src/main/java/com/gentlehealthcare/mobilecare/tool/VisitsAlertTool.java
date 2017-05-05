package com.gentlehealthcare.mobilecare.tool;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.gentlehealthcare.mobilecare.db.dao.VisitsAlertDao;
import com.gentlehealthcare.mobilecare.db.table.TB_Patient;
import com.gentlehealthcare.mobilecare.db.table.TB_VisitsAlert;

/**
 * 响铃工具类
 * Created by ouyang on 2015/3/19.
 */
public class VisitsAlertTool {
    private static VisitsAlertTool visitsAlertTool=null;
    private AlarmManager alarm;
    private PendingIntent pendIntent;
    private     Intent intent;
    private Activity activity;
    private VisitsAlertDao visitsAlertDao=null;

    public static VisitsAlertTool newInstance(Activity activity){
        if (visitsAlertTool==null)
            visitsAlertTool=new VisitsAlertTool(activity);
        return visitsAlertTool;
    }

    VisitsAlertTool(Activity activity){
        alarm = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        visitsAlertDao=VisitsAlertDao.newInstance(activity);
        this.activity=activity;
    }

    /**
     * 开启响铃
     * @param tb_visitsAlert
     * @param millisecond
     */
    public void startAlert(TB_VisitsAlert tb_visitsAlert,long millisecond){
            Long time=Long.valueOf(tb_visitsAlert.getAlarmTime());
            Long nowTime=System.currentTimeMillis();
            if (time-nowTime>=0) {
                intent.putExtra("visitsAlert", tb_visitsAlert);
                pendIntent = PendingIntent.getBroadcast(activity, tb_visitsAlert.getVisitasAlertId(), intent, 0);
                alarm.set(AlarmManager.RTC_WAKEUP,  time, pendIntent);
            }
    }

    public void startAlert(TB_VisitsAlert tb_visitsAlert){
        Long time=Long.valueOf(tb_visitsAlert.getVisitsTime());
        Long nowTime=System.currentTimeMillis();
        if (time-nowTime>=0) {
            Log.v("TTT","巡视时间:" +  DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS,time)+ "-->计划ID:"+tb_visitsAlert.getPlanId());
            intent.putExtra("visitsAlert", tb_visitsAlert);
            pendIntent = PendingIntent.getBroadcast(activity, tb_visitsAlert.getVisitasAlertId(), intent, 0);
            alarm.set(AlarmManager.RTC_WAKEUP,  time, pendIntent);
        }
    }

    /**
     * 取消响铃
     * @param tb_patient
     * @param tb_visitsAlert
     */
    public void cancelAlert(TB_Patient tb_patient,TB_VisitsAlert tb_visitsAlert){
        intent.putExtra("patient", tb_patient);
        intent.putExtra("visitsAlert",tb_visitsAlert);

        Log.d("VisitsAlertTool","病人ID为"+tb_patient.getPatientId() +"  药品CODE :"+tb_visitsAlert.getMedicineCode() +"关闭闹钟（Id为"+tb_visitsAlert.getVisitasAlertId()+")");
        alarm.cancel(PendingIntent.getBroadcast(activity,tb_visitsAlert.getVisitasAlertId(),intent,0));
        visitsAlertDao.delById(tb_visitsAlert.getVisitasAlertId());
    }


    public void cancelAlert(TB_VisitsAlert tb_visitsAlert){
        intent.putExtra("visitsAlert",tb_visitsAlert);
        alarm.cancel(PendingIntent.getBroadcast(activity,tb_visitsAlert.getVisitasAlertId(),intent,0));
    }

}
