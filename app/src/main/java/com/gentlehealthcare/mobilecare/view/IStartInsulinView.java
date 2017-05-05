package com.gentlehealthcare.mobilecare.view;

/**
 * Created by zhiwei on 2016/5/16.
 */
public interface IStartInsulinView {

    void finishActivity();

    void setBlood(String blood);

    void showProgressDialog();

    void dismissProgressDialog();
}
