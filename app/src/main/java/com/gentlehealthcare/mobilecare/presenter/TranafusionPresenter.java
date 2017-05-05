package com.gentlehealthcare.mobilecare.presenter;

import com.gentlehealthcare.mobilecare.model.IDocOrdersModel;
import com.gentlehealthcare.mobilecare.model.impl.DocOrdersModel;

/**
 * Created by Zyy on 2016/8/31.
 * 类说明：
 */

public class TranafusionPresenter {

    private IDocOrdersModel docOrdersModel;

    public void commRec(String userName, String barCode, String deviceId) {
        if(null==docOrdersModel){
            docOrdersModel=new DocOrdersModel();
        }
        docOrdersModel.commRec(userName, barCode, deviceId);
    }

}
