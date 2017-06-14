package com.gentlehealthcare.mobilecare.presenter;

import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.model.IBloodBagNuclearChangeModel;
import com.gentlehealthcare.mobilecare.model.IDocOrdersModel;
import com.gentlehealthcare.mobilecare.model.impl.BloodBagNuclearChangeModel;
import com.gentlehealthcare.mobilecare.model.impl.DocOrdersModel;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.view.IBloodBagNuclearChangeView;

import java.util.List;

/**
 * Created by zyy on 2016/5/5.
 *
 * @desp 血制品核收 presenter的控制层，完成交互
 */
public class BloodBagNuclearChangePresenter {

  private IBloodBagNuclearChangeView view;
  private IBloodBagNuclearChangeModel bloodBagNuclearChangeModel;


  public BloodBagNuclearChangePresenter(IBloodBagNuclearChangeView view) {
    this.view = view;
    bloodBagNuclearChangeModel = new BloodBagNuclearChangeModel();
  }

  public void bloodBagNuclearChange(final String code, final String user, String username, String wardCode) {
    if (bloodBagNuclearChangeModel == null) {
      bloodBagNuclearChangeModel = new BloodBagNuclearChangeModel();
    }
    bloodBagNuclearChangeModel.BloodBagNuclearChange(code, user, username, wardCode,
        new BloodBagNuclearChangeModel.BloodBagNuclearChangeModelListener() {
          @Override
          public void bloodBagNuclearChangeModelSucessed(List<BloodProductBean2> list) {
             view.loadBag();
          }

          @Override
          public void bloodBagNuclearChangeModelFailed(String msg, Exception e) {
            view.showToast("核收失败,联系运维人员");
          }
        });
  }

    public void bloodBack(String username,String wardCode,String logId){
        if (bloodBagNuclearChangeModel == null) {
            bloodBagNuclearChangeModel = new BloodBagNuclearChangeModel();
        }
       bloodBagNuclearChangeModel.bloodBack(wardCode, username, logId, new BloodBagNuclearChangeModel.bloodBackListener() {
           @Override
           public void bloodBackSucessed(boolean flag) {
               if(flag){
                   view.showToast("血品已退回血库");
                   view.loadBag();
               }

           }

           @Override
           public void bloodBackFailed(String msg, Exception e) {
               view.showToast("退回血库失败");
           }
       });
    }

    public void commRec(String userName, String barCode, String deviceId) {
        IDocOrdersModel docOrdersModel = new DocOrdersModel();
        docOrdersModel.commRec(userName, barCode, deviceId);
    }

  public void collect(String wardCode, String status) {
    if (bloodBagNuclearChangeModel == null) {
      bloodBagNuclearChangeModel = new BloodBagNuclearChangeModel();
    }
    bloodBagNuclearChangeModel.loaddatas(wardCode, status,
        new BloodBagNuclearChangeModel.loadDatasSuccessedlListener() {
          @Override
          public void loadDatasSucessed(List<BloodProductBean2> list) {
            view.setBloodBag(list);
          }

          @Override
          public void loadDatasFailed(String msg, Exception e) {
              if(msg.equals(GlobalConstant.FAILED)){
                  view.showToast("没有待核收血品");
              }else{
                  view.showToast("待核收血品加载异常");
              }
          }
        });
  }
}