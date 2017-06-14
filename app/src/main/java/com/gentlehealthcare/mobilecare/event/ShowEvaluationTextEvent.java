package com.gentlehealthcare.mobilecare.event;

/**
 * Created by Administrator on 2017/6/12.
 */

public class ShowEvaluationTextEvent {
    public int position;
    public String text;

    public ShowEvaluationTextEvent(int position, String text) {
        this.position = position;
        this.text = text;
    }
}
