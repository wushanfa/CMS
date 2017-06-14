package com.gentlehealthcare.mobilecare.event;

/**
 * Created by Administrator on 2017/6/12.
 */

public class ShowEvaluationMeasureTextEvent {
    public int position;
    public String text;

    public ShowEvaluationMeasureTextEvent(int position, String text) {
        this.position = position;
        this.text = text;
    }
}
