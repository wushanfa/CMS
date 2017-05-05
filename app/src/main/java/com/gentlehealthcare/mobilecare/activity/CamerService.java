package com.gentlehealthcare.mobilecare.activity;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * 红外扫描监听Service
 * Created by zyy on 16/6/13
 */
public class CamerService extends Service {
    private CameraDevicerReceiver cameraDevicerReceiver=null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //start CameraDevicerReceiver
        cameraDevicerReceiver=new CameraDevicerReceiver();
        IntentFilter filter = new IntentFilter();
        //filter.addAction("df.scanservice.result");//研华
        filter.addAction("com.honeywell.tools.action.scan_result");//霍尼韦尔
        registerReceiver(cameraDevicerReceiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //close CameraDevicerReceiver
        unregisterReceiver(cameraDevicerReceiver);
    }
}
