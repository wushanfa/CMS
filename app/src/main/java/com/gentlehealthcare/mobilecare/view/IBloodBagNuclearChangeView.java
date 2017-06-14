package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;

import java.util.List;

/**
 * Created by Zyy on 2016/9/14.
 * 类说明：
 */

public interface IBloodBagNuclearChangeView {

     void setBloodBag(List<BloodProductBean2> params);

     void showToast(String msg);
    
     void loadBag() ;

     void checkOrdersFailed() ;

}
