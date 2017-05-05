package com.gentlehealthcare.mobilecare.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.activity.patient.blood.BloodFra;
import com.gentlehealthcare.mobilecare.tool.DateTool;


/**
 * 采血时间不OK,弹框等待
 * Created by ouyang on 2015/5/28.
 */
public class WaitTimeBloodDialog extends Dialog implements View.OnClickListener{
    private Activity act;
    private TextView tv_title,tv_endtime,tv_time,tv_notes;
    private Button btn_blue;
    private int waithour, waitminute;
    private static final int TIME_UPDATE=0xFA;
    private static final int TIME_STOP=0xAF;
    private Fragment fragment;
    public WaitTimeBloodDialog(Activity act) {
        super(act);
        this.act=act;
        init();

    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_UPDATE:
                    tv_time.setText((waithour < 10 ? "0" + waithour : waithour) + ":" + (waitminute < 10 ? "0" + waitminute : waitminute));
                    break;
                case TIME_STOP:
                    if (act!=null)
                    dismiss();
                    if (fragment!=null&& fragment instanceof BloodFra)
                        ((BloodFra)fragment).setType(-1);
                    break;
            }
        }
    };

    public WaitTimeBloodDialog(Activity act, int theme,Fragment fragment) {
        super(act, theme);
        this.act=act;
        this.fragment=fragment;
        init();
    }


    private void init(){
        setCancelable(false);
        View rootView= LayoutInflater.from(this.act).inflate(R.layout.act_waitblood_main,null);
        setContentView(rootView);
        tv_title= (TextView) rootView.findViewById(R.id.tv_title);
        tv_endtime= (TextView) rootView.findViewById(R.id.tv_endtime);
        tv_time= (TextView) rootView.findViewById(R.id.tv_time);
        tv_notes= (TextView) rootView.findViewById(R.id.tv_notes);
        btn_blue= (Button) rootView.findViewById(R.id.btn_next);
        btn_blue.setOnClickListener(this);
        tv_title.setText(Html.fromHtml("<font color=\"black\">"+ "更改"+"</font><font color=\"red\">"+"采血时间为"+"</font>"));
        tv_notes.setText(Html.fromHtml("<font color=\"red\">"+"之前"+"</font>"+"<font color=\"black\">"+ "请勿"+"</font><font color=\"red\">"+"用餐"+"</font>"));
    }

    public void setWaitTime( int waitHour,  int waitMinute){
        tv_time.setText((waitHour < 10 ? "0" + waitHour : waitHour) + ":" + (waitMinute < 10 ? "0" + waitMinute : waitMinute));
        long endtime=System.currentTimeMillis()+1000*60*waitMinute+1000*60*60*waitHour;
        tv_endtime.setText(DateTool.parseDate(DateTool.HH_MM,endtime));
        waithour=waitHour;
        waitminute=waitMinute;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!(waithour<=0&&waitminute<=0)){
                    try {
                        Thread.sleep(1000*60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (waitminute>0){
                        waitminute-=1;
                    }else if (waitminute<=0&&waithour>0){
                        waitminute=59;
                        waithour-=1;
                    }
                    handler.sendEmptyMessage(TIME_UPDATE);
                }
                handler.sendEmptyMessage(TIME_STOP);
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_next){
            act.startActivity(new Intent(act, HomeAct.class));
            act.finish();
        }
    }
}
