package com.gentlehealthcare.mobilecare.view;

/**
 * Created by zhiwei on 2016/5/17.
 */
public interface IPatrolView {

    void showDialog(String str, String[] items, boolean[] booleans, int type);

    String[] getGuoMin();

    String[] getDixueTang();

    String[] getZhushe();

    void setGuoMin(String str);

    void setDiXueTang(String str);

    void setZhuShe(String str);

    void initTextView();

    void showToast(String str);

    void setTime(String date, String time);

    boolean[] getGuominBoolean();

    boolean[] getDixuetangBoolean();

    boolean[] getZhusheBoolean();

    void showProgressDialog();

    void dismissProgressDialog();

    void activityFinish();
}
