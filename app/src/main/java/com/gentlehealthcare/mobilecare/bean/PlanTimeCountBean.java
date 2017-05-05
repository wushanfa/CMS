package com.gentlehealthcare.mobilecare.bean;

/**
 *
 * Created by ouyang on 15/8/26.
 */
public class PlanTimeCountBean {
    private int H_0;
    private int H_1;
    private int H_2;
    private int H_3;
    private int H_4;
    private int H_5;
    private int H_6;
    private int H_7;
    private int H_8;
    private int H_9;
    private int H_10;
    private int H_11;
    private int H_12;
    private int H_13;
    private int H_14;
    private int H_15;
    private int H_16;
    private int H_17;
    private int H_18;
    private int H_19;
    private int H_20;
    private int H_21;
    private int H_22;
    private int H_23;
    public int getH_8() {
        return H_8;
    }

    public void setH_8(int h_8) {
        H_8 = h_8;
    }


    public int getH_0() {
        return H_0;
    }

    public void setH_0(int h_0) {
        H_0 = h_0;
    }

    public int getH_1() {
        return H_1;
    }

    public void setH_1(int h_1) {
        H_1 = h_1;
    }

    public int getH_2() {
        return H_2;
    }

    public void setH_2(int h_2) {
        H_2 = h_2;
    }

    public int getH_3() {
        return H_3;
    }

    public void setH_3(int h_3) {
        H_3 = h_3;
    }

    public int getH_4() {
        return H_4;
    }

    public void setH_4(int h_4) {
        H_4 = h_4;
    }

    public int getH_5() {
        return H_5;
    }

    public void setH_5(int h_5) {
        H_5 = h_5;
    }

    public int getH_6() {
        return H_6;
    }

    public void setH_6(int h_6) {
        H_6 = h_6;
    }

    public int getH_7() {
        return H_7;
    }

    public void setH_7(int h_7) {
        H_7 = h_7;
    }

    public int getH_9() {
        return H_9;
    }

    public void setH_9(int h_9) {
        H_9 = h_9;
    }

    public int getH_10() {
        return H_10;
    }

    public void setH_10(int h_10) {
        H_10 = h_10;
    }

    public int getH_11() {
        return H_11;
    }

    public void setH_11(int h_11) {
        H_11 = h_11;
    }

    public int getH_12() {
        return H_12;
    }

    public void setH_12(int h_12) {
        H_12 = h_12;
    }

    public int getH_13() {
        return H_13;
    }

    public void setH_13(int h_13) {
        H_13 = h_13;
    }

    public int getH_14() {
        return H_14;
    }

    public void setH_14(int h_14) {
        H_14 = h_14;
    }

    public int getH_15() {
        return H_15;
    }

    public void setH_15(int h_15) {
        H_15 = h_15;
    }

    public int getH_16() {
        return H_16;
    }

    public void setH_16(int h_16) {
        H_16 = h_16;
    }

    public int getH_17() {
        return H_17;
    }

    public void setH_17(int h_17) {
        H_17 = h_17;
    }

    public int getH_18() {
        return H_18;
    }

    public void setH_18(int h_18) {
        H_18 = h_18;
    }

    public int getH_19() {
        return H_19;
    }

    public void setH_19(int h_19) {
        H_19 = h_19;
    }

    public int getH_20() {
        return H_20;
    }

    public void setH_20(int h_20) {
        H_20 = h_20;
    }

    public int getH_21() {
        return H_21;
    }

    public void setH_21(int h_21) {
        H_21 = h_21;
    }

    public int getH_22() {
        return H_22;
    }

    public void setH_22(int h_22) {
        H_22 = h_22;
    }

    public int getH_23() {
        return H_23;
    }

    public void setH_23(int h_23) {
        H_23 = h_23;
    }


    private String getTimeAxis(int[] is) {

        String timeAxis = "";

        String space = " ";

        for (int i = 0; i < is.length; i++) {

            if (is[i] < 10) {
//
                timeAxis = timeAxis + space +  0+ is[i] + space;
            } else {

                timeAxis = timeAxis + space + is[i] + space;
            }
        }

        return timeAxis;
    }


    public String getTimePlanCount(){

        int[] items= new int[]{getH_0(),getH_1(),getH_2(),getH_3(),getH_4(),getH_5(),getH_6(),getH_7(),getH_8(),getH_9(),getH_10(),getH_11(),getH_12(),getH_13(),getH_14(),getH_15(),getH_16(),getH_17(),getH_18(),getH_19(),getH_20(),getH_21(),getH_22(),getH_23()};
        return   getTimeAxis(items);
    }

}
