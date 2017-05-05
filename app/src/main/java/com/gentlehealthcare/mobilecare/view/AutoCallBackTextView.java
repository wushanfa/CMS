package com.gentlehealthcare.mobilecare.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义TextView 自动回滚
 * Created by ouyang on 15/5/18.
 */
public class AutoCallBackTextView extends TextView {
    private int tag=2;
    private String detailStr;//详细内容
    private String contentStr;//简写内容
    private Handler handler;
    private static final int AutoCallBack=0xfa;

    public AutoCallBackTextView(Context context) {
        super(context);
        createHandler();
    }

    public AutoCallBackTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createHandler();
    }

    public AutoCallBackTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        createHandler();
    }

    /**
     * 创建上下文
     */
    private void createHandler(){
         handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==AutoCallBack){
                    collection();
                }
            }
        };
    }

    public String getDetailStr() {
        return detailStr;
    }

    public void setDetailStr(String detailStr) {
        this.detailStr = detailStr;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }

    /**
     *展开
     */
    public void expansion(){
        setText(getDetailStr());
        tag=2;
        new Thread(new Runnable() {
            @Override
            public void run() {
            while (tag>0){
                try {
                    Thread.sleep(1*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tag-=1;
            }
                handler.sendEmptyMessage(AutoCallBack);
            }
        }).start();
    }

    /**
     * 集合
     */
    public void collection(){
        setText(getContentStr());
    }

}
