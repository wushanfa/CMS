package com.gentlehealthcare.mobilecare.activity.implementation;

import android.support.v4.app.Fragment;

/**
 * Created by Zyy on 2016/5/16.
 * 类说明：执行记录fra工厂类
 */
public class ImplementationFraFactory {
    public  static Fragment getInstanceByIndex(int index){
        Fragment fragment = null;
        switch (index) {
            case 1:
                fragment = new IndexFra();
                break;
            case 2:
                fragment = new recordFra();
                break;
            case 3:
                fragment = new IntroductionsFra();
                break;
        }
        return fragment;
    }
}
