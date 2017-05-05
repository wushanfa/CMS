package com.gentlehealthcare.mobilecare.activity;


import java.lang.reflect.Method;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.NurseApp;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.umeng.analytics.MobclickAgent;

/**
 * 基类Activity
 */
public abstract class BaseActivity extends FragmentActivity {
    private boolean showGest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SupportDisplay.initLayoutSetParams(BaseActivity.this);
        ActivityControlTool.add(this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        SupportDisplay.initLayoutSetParams(BaseActivity.this);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        SupportDisplay.initLayoutSetParams(BaseActivity.this);
        super.onResume();
        ActivityControlTool.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        if (showGest) {
            ShowGestWindow();
        } else {
            HidnGestWindow();
        }

    }

    @Override
    protected void onPause() {

        super.onPause();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {

        super.onStop();
        ActivityControlTool.onStop(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        ActivityControlTool.onDestroy(this);
        ActivityControlTool.remove(this);

    }

    /**
     * 子类Activity必须实现的方法，用于初始化控件，重新适配等操作
     *
     * @param
     * @return void
     * @Title resetLayout
     * @Description 主要用于多屏幕适配时重新计算
     */
    protected abstract void resetLayout();

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        resetLayout();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        resetLayout();
    }

    /**
     * 显示手势区域
     */
    public void ShowGestWindow() {
        NurseApp appliction = (NurseApp) getApplication();
        appliction.ShowGestWindow();
        showGest = true;
    }

    /**
     * 隐藏手势区域
     */
    public void HidnGestWindow(boolean flag) {
        showGest = false;
    }

    public void HidnGestWindow() {
        NurseApp appliction = (NurseApp) getApplication();
        appliction.HidnGestWindow();
    }


    /**
     * 吐司提示
     *
     * @param text 提示文本
     */
    public void ShowToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    
    public void HidnKey(EditText edt){
        if (android.os.Build.VERSION.SDK_INT <= 10) {
        	edt.setInputType(InputType.TYPE_NULL);
        } else {
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edt, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
