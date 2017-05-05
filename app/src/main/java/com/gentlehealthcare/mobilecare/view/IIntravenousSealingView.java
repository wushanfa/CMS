package com.gentlehealthcare.mobilecare.view;

/**
 * Created by Zyy on 2016/5/17.
 * 类说明：
 */
public interface IIntravenousSealingView {
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
    void showSlideDateTimerPicker();
    void sealing();
    void setNurseNameAndSealingTime(String name, String time, String dossage);
    void receiveData();
    void finishActivity();

}
