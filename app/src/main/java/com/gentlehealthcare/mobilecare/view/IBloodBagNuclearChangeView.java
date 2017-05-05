package com.gentlehealthcare.mobilecare.view;

import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;

import java.util.List;

/**
 * Created by Zyy on 2016/9/14.
 * 类说明：
 */

public interface IBloodBagNuclearChangeView {

    public void setBloodBag(List<BloodProductBean2> params);

    public void showToast(String msg);
    
    public void loadBag() ;

    public void checkOrdersFailed() ;

}
