package com.gentlehealthcare.mobilecare.view;

import android.view.View;

import com.gentlehealthcare.mobilecare.bean.ImplementationRecordBean;

import java.util.List;

/**
 * Created by Zyy on 2016/5/9.
 * 类说明：
 */
public interface IImplementationRecordView {
    /**
     * 展示progress
     */
    void showProgressDialog();

    /**
     * 销毁progress
     */
    void dismissProgressDialog();
    void addRecord(List<ImplementationRecordBean> beans);

    void showLoadFial();

    void intial(View view);

    void receiveData();

    void changeLayout(boolean flag);
}
