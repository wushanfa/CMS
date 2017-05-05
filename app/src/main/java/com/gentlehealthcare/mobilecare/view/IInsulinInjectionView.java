package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.bean.insulin.InjectionSiteBean;

import java.util.List;

/**
 * Created by zhiwei on 2016/5/15.
 */
public interface IInsulinInjectionView {

    void setSiteBackground(int... params);

    void setSiteBackgroundInit();

    void setDialog(final int num);

    void showToast(int showWhich);

    void setSiteBeans(List<InjectionSiteBean> beans);

    void showProgressDialog();

    void dismissProgressDialog();

    void setSiteChange(int site, int status, String desc, String siteId);

    void replaceFragment();
}
