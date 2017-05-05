package com.gentlehealthcare.mobilecare.listener;

import android.os.Handler;
import android.os.Message;

import com.gentlehealthcare.mobilecare.constant.GlobalConstant;

import java.util.Timer;
import java.util.TimerTask;

public class ScanWanDai {
    Timer mTimer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();

            handler.sendMessage(message);

        }

    };
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
//            if (mOnTestListening != null && !GlobalConstant.wd.equals("xxxxx")) {
//                mOnTestListening.TestListening(2);
//            }注释于2015年1月11日 why:条码规则从后台加载
            if(mOnTestListening!=null&& GlobalConstant.wd!=null){
                mOnTestListening.TestListening(2);
            }
            super.handleMessage(msg);
        }

    };

    public void run() {
        mTimer.schedule(task, 2000, 2000);//每五秒执行一次handler
    }

    public void stop(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
        if(task!=null){
            task.cancel();
            task=null;
        }
    }
    public interface OnTestListening {
        public abstract void TestListening(int i);
    }

    OnTestListening mOnTestListening = null;

    public void setOnTestListening(OnTestListening e) {
        mOnTestListening = e;
    }
}
