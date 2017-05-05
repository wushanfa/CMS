package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.BloodBagNuclearChangeModel;

/**
 * Created by Zyy on 2016/9/5.
 * 类说明：血品核收
 */

public interface IBloodBagNuclearChangeModel {

     void BloodBagNuclearChange(String code, String user, String username, String wardCode, BloodBagNuclearChangeModel.BloodBagNuclearChangeModelListener listener);

     void loaddatas(String wardCode, String status, BloodBagNuclearChangeModel.loadDatasSuccessedlListener listener);

     void bloodBack(String wardCode, String username,String logId, BloodBagNuclearChangeModel.bloodBackListener listener);
}
