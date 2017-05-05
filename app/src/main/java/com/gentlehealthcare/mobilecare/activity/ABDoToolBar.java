package com.gentlehealthcare.mobilecare.activity;



/**
 * 工具栏
 * Created by ouyang on 15/5/20.
 */
public interface ABDoToolBar {
    /**
     * 工具栏 功能icon 切换
     * @param i
     */
    public void onCheckedChanged(int i);

    /**
     * 点击左按钮
     */
    public void onLeftBtnClick();

    /**
     * 点击右按钮
     */
    public void onRightBtnClick();

}
