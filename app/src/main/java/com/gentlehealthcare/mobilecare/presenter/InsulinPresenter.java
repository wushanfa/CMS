package com.gentlehealthcare.mobilecare.presenter;

import com.gentlehealthcare.mobilecare.bean.insulin.InjectionSiteBean;
import com.gentlehealthcare.mobilecare.model.IInsulinModel;
import com.gentlehealthcare.mobilecare.model.IPatientModel;
import com.gentlehealthcare.mobilecare.model.impl.InsulinModel;
import com.gentlehealthcare.mobilecare.model.impl.PatientModel;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.view.IInsulinInjectionView;

import java.util.List;

/**
 * Created by zhiwei on 2016/5/14.
 */
public class InsulinPresenter {

    private IInsulinInjectionView injectionView;
    private IInsulinModel insulinModel;
    private int[] siteStatus;

    public InsulinPresenter(IInsulinInjectionView injectionView) {
        this.injectionView = injectionView;
        insulinModel = new InsulinModel();
    }

    /**
     * 获取注射位置
     */
    public void getInjectionSite(String patId) {
        injectionView.showProgressDialog();
        insulinModel.getSite(patId, new InsulinModel.onLoadSite() {
            @Override
            public void onLoadSuccess(List<InjectionSiteBean> siteBeans) {
                injectionView.dismissProgressDialog();
                siteStatus = new int[siteBeans.size()];
                for (int i = 0; i < siteBeans.size(); i++) {
                    siteStatus[i] = siteBeans.get(i).getStatus();
                    injectionView.setSiteBackground(siteStatus);
                }
                injectionView.setSiteBeans(siteBeans);
            }

            @Override
            public void onLoadFailure(String msg, Exception e) {
                injectionView.dismissProgressDialog();
            }
        });
    }

    /**
     * 重新加载模板
     */
    public void reLoadTemplate(String patId) {
        injectionView.showProgressDialog();
        insulinModel.reloadInjection(patId, new InsulinModel.onReloadSite() {
            @Override
            public void onReloadSiteSuccess(List<InjectionSiteBean> siteBeans) {
                injectionView.dismissProgressDialog();
                siteStatus = new int[siteBeans.size()];
                for (int i = 0; i < siteBeans.size(); i++) {
                    siteStatus[i] = siteBeans.get(i).getStatus();
                    injectionView.setSiteBackground(siteStatus);
                }
                injectionView.setSiteBeans(siteBeans);
            }

            @Override
            public void onReloadSiteFailure(String msg, Exception e) {
                injectionView.dismissProgressDialog();
                CCLog.e(msg + ";Exception:" + e.getMessage());
            }
        });
    }

    /**
     * 设置区域的各种问题
     */
    public void updateSite(String patId, String siteId, String itemNo, final int status, String planId) {
        injectionView.showProgressDialog();
        insulinModel.updateSite(patId, siteId, String.valueOf(status), itemNo, planId, new InsulinModel
                .onUpdateSite() {

            @Override
            public void onUpdateSiteSuccess(List<InjectionSiteBean> siteBeans) {
                injectionView.dismissProgressDialog();
                siteStatus = new int[siteBeans.size()];
                for (int i = 0; i < siteBeans.size(); i++) {
                    siteStatus[i] = siteBeans.get(i).getStatus();
                    injectionView.setSiteBackground(siteStatus);
                }
                injectionView.setSiteBeans(siteBeans);
            }

            @Override
            public void onUpdateSiteFailure(String msg, Exception e) {
                injectionView.dismissProgressDialog();
                CCLog.e(msg + ";Exception:" + e.getMessage());
            }
        });
    }

    /**
     * 注射位置的点击处理
     * -99禁打，-98拒打，1已注射，0未打,2上一位置，3下一位置,4推荐位置,5自己选了哪
     *
     * @param num
     * @param beans
     */
    public void clickDeal(int num, List<InjectionSiteBean> beans, int[] params) {
        int status = beans.get(num).getStatus();
        for (int i = 0; i < params.length; i++) {
            if (params[i] == 3) {
                params[i] = 0;
            }
            if (params[i] == 5) {
                params[i] = 0;
            }
        }
        if (status == 0 || status == 3 || status == 4) {
            injectionView.setSiteChange(Integer.parseInt(beans.get(num).getItemNo()), status, beans.get(num)
                    .getSiteDesc(), beans.get(num).getSiteId());
            params[num] = 5;
            injectionView.setSiteBackground(params);
            injectionView.replaceFragment();
        }
    }

    /**
     * 注射位置的长按处理
     * -99禁打，-98拒打，1已注射，0未打,2上一位置，3下一位置,4推荐位置
     *
     * @param num
     * @param myStatus
     */
    public void longClickDeal(int num, int[] myStatus, String patId, String siteId, String itemNo, String planId) {
        int tempStatus = myStatus[num];
        if (tempStatus == 0 || tempStatus == 3 || tempStatus == 4 || tempStatus == 5) {
            injectionView.setDialog(num);
        } else if (tempStatus == -98 || tempStatus == -99) {
            updateSite(patId, siteId, itemNo, 0, planId);
        }
    }

}
