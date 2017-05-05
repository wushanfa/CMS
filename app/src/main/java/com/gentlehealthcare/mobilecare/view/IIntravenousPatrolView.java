package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.IntraDontExcuteBean;

import java.util.List;

/**
 * Created by Zyy on 2016/5/17.
 * 类说明：
 */
public interface IIntravenousPatrolView {
    /**
     * 展示progress
     */
    void showProgressDialog();
    /**
     * 销毁progress
     */
    void dismissProgressDialog();

    void showToast(String msg);

    void handException();

    void setSaveButtonbackGroud();
    void showSlideDateTimerPicker();
    void addVariationDict(List<IntraDontExcuteBean> list);
    void setExceptionText(String text);
    void changeLayout();
    void showPiShi();
    void showVariationDict(List<IntraDontExcuteBean> list);
    void setPatrolTime(String time);
}
