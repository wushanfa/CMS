package com.gentlehealthcare.mobilecare.view;

/**
 * Created by Zyy on 2016/5/17.
 * 类说明：
 */
public interface IIntravenousExecuteView {
    /**
     * 展示progress
     */
    void showProgressDialog();

    /**
     * 销毁progress
     */
    void dismissProgressDialog();
    void saveNextPatrolTime();

    void showToast(String msg);

    void handException();

    void backToDoctorOrdersAct();
    void showSlideDateTimerPicker();

}
