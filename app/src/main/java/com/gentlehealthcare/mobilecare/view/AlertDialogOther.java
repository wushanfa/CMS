package com.gentlehealthcare.mobilecare.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.gentlehealthcare.mobilecare.R;

/**
 * Created by zhiwei on 2016/3/3.
 */
public class AlertDialogOther extends Dialog {

    Context context;

    public AlertDialogOther(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_other);

    }
}
