package com.gentlehealthcare.mobilecare.activity.intravenous;

import android.support.v4.app.Fragment;

import com.gentlehealthcare.mobilecare.R;

/**
 * Created by Zyy on 2016/5/16.
 * 类说明：静脉给药碎片工厂
 */
public class IntravenousFraFactory {
    public  static Fragment getInstanceByIndex(int index){
        Fragment fragment = null;
        switch (index) {
            case R.id.rd_gy:
                fragment = new IntravExecuteFra();
                break;
            case R.id.rd_xs:
                fragment = new IntravPatrolFra();
                break;
            case R.id.rd_fg:
                fragment = new IntravSealingFra();
                break;
            case R.id.rd_sm:
                fragment = new IntroductionsFra();
                break;
        }
        return fragment;
    }
}
