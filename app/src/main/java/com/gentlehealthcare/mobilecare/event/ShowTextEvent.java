package com.gentlehealthcare.mobilecare.event;

/**
 * Created by Administrator on 2017/6/12.
 */

public class ShowTextEvent {
    public String text;
    public int position;

    public ShowTextEvent(String text, int position) {
        this.text = text;
        this.position = position;
    }
}
