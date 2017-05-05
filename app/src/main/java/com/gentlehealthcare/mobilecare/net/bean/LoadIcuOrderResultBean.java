package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dzw on 2015/11/13.
 */
public class LoadIcuOrderResultBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<List<LoadIcuOrderBean>> IN2;
    private List<List<LoadIcuOrderBean>> IN1;
    private List<List<LoadIcuOrderBean>> IN4;
    private List<List<LoadIcuOrderBean>> IN3;
    private List<List<LoadIcuOrderBean>> IN9;

    private List<List<LoadIcuOrderBean>> OUT;

    public List<List<LoadIcuOrderBean>> getIN2() {
        return IN2;
    }

    public void setIN2(List<List<LoadIcuOrderBean>> IN2) {
        this.IN2 = IN2;
    }

    public List<List<LoadIcuOrderBean>> getIN1() {
        return IN1;
    }

    public void setIN1(List<List<LoadIcuOrderBean>> IN1) {
        this.IN1 = IN1;
    }

    public List<List<LoadIcuOrderBean>> getIN4() {
        return IN4;
    }

    public void setIN4(List<List<LoadIcuOrderBean>> IN4) {
        this.IN4 = IN4;
    }

    public List<List<LoadIcuOrderBean>> getIN3() {
        return IN3;
    }

    public void setIN3(List<List<LoadIcuOrderBean>> IN3) {
        this.IN3 = IN3;
    }

    public List<List<LoadIcuOrderBean>> getIN9() {
        return IN9;
    }

    public void setIN9(List<List<LoadIcuOrderBean>> IN9) {
        this.IN9 = IN9;
    }

    public List<List<LoadIcuOrderBean>> getOUT() {
        return OUT;
    }

    public void setOUT(List<List<LoadIcuOrderBean>> OUT) {
        this.OUT = OUT;
    }
}
