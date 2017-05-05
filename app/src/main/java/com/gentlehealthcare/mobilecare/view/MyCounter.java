package com.gentlehealthcare.mobilecare.view;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MyCounter extends CountDownTimer {
    private long millisInFuture;
    private long countDownInterval;
    private TextView tv;
    private int sureLookStatus;

    private TextView submit;

    public MyCounter(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.countDownInterval = countDownInterval;
        this.millisInFuture = millisInFuture;
    }

    public MyCounter(long millisInFuture, long countDownInterval, TextView tv, int sureLookStatus, TextView
            submit) {
        super(millisInFuture, countDownInterval);
        this.tv = tv;
        this.countDownInterval = countDownInterval;
        this.millisInFuture = millisInFuture;
        this.sureLookStatus = sureLookStatus;
        this.submit = submit;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        SimpleDateFormat sf = new SimpleDateFormat("mm:ss");
        String format = sf.format(millisUntilFinished);
        tv.setText(format);
    }

    @Override
    public void onFinish() {
        tv.setText("00:00");
        submit.setText("退 出");
        //现在只有一种情况 》输血
        sureLookStatus = 0;
    }

    public int getSureLookStatus() {
        return sureLookStatus;
    }

    public void setSureLookStatus(int sureLookStatus) {
        this.sureLookStatus = sureLookStatus;
    }

}
