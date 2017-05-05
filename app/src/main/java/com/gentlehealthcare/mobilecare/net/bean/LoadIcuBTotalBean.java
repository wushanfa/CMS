package com.gentlehealthcare.mobilecare.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dzw on 2015/11/13.
 */
public class LoadIcuBTotalBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<LoadIcuBBean> D;
    private List<LoadIcuBBean> F;
    private List<LoadIcuBBean> G;
    private List<LoadIcuBBean> H;
    private List<LoadIcuBBean> I;
    private List<LoadIcuBBean> J;
    private List<LoadIcuBBean> Z;

    @Override
    public String toString() {
        return "";
    }

    public List<LoadIcuBBean> getD() {
        return D;
    }

    public void setD(List<LoadIcuBBean> d) {
        D = d;
    }

    public List<LoadIcuBBean> getF() {
        return F;
    }

    public void setF(List<LoadIcuBBean> f) {
        F = f;
    }

    public List<LoadIcuBBean> getG() {
        return G;
    }

    public void setG(List<LoadIcuBBean> g) {
        G = g;
    }

    public List<LoadIcuBBean> getH() {
        return H;
    }

    public void setH(List<LoadIcuBBean> h) {
        H = h;
    }

    public List<LoadIcuBBean> getI() {
        return I;
    }

    public void setI(List<LoadIcuBBean> i) {
        I = i;
    }

    public List<LoadIcuBBean> getJ() {
        return J;
    }

    public void setJ(List<LoadIcuBBean> j) {
        J = j;
    }

    public List<LoadIcuBBean> getZ() {
        return Z;
    }

    public void setZ(List<LoadIcuBBean> z) {
        Z = z;
    }
}
