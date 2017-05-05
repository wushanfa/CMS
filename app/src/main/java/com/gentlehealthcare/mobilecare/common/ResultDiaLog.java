package com.gentlehealthcare.mobilecare.common;

import com.gentlehealthcare.mobilecare.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;



/**
 * Created by user on 2017/2/15.
 */

public class ResultDiaLog extends Dialog {

    RelativeLayout relativeLayout;
    TextView dialogText;
    private String text;
    private Context context;
    private String tag;

    public ResultDiaLog(Context context, String tag, String text, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.tag = tag;
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_animation, null);
        setContentView(view);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        dialogText = (TextView) findViewById(R.id.dialogText);
        dialogText.setText(text);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (tag.equals("correct")) {
            CorrectView correctView = new CorrectView(context);
            relativeLayout.addView(correctView, params);
        } else if (tag.equals("error")) {
            ErrorView errorView = new ErrorView(context);
            relativeLayout.addView(errorView, params);
        }

    }
}
