package com.gentlehealthcare.mobilecare.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

public class AlertDialogThreeBtnNoicon {

    private AlertDialog dialog;

    private TextView tvTitle, tvMessage;

    private Button btnLeft, btnRight,btnPause;

    public AlertDialogThreeBtnNoicon(Context context) {

        super();

        dialog = new AlertDialog.Builder(context).create();

        dialog.setCancelable(false);

        dialog.show();

        Window window = dialog.getWindow();

        window.setContentView(R.layout.layout_three_btn);

        tvTitle = (TextView) window.findViewById(R.id.tv_execute);

        tvMessage = (TextView) window.findViewById(R.id.tv_execute_num);

        btnLeft = (Button) window.findViewById(R.id.btn_continue);

        btnRight = (Button) window.findViewById(R.id.btn_end);
        btnPause= (Button) window.findViewById(R.id.btn_pause);
    }

    public void setTitle(String title) {

        tvTitle.setText(title);
    }

    public void setMessage(String msg) {

        tvMessage.setText(msg);
    }

    public void setLeftButton(String button, OnClickListener clickListener) {

        btnLeft.setText(button);
        btnLeft.setOnClickListener(clickListener);
    }

    public void setRightButton(String button, OnClickListener clickListener) {

        btnRight.setText(button);
        btnRight.setOnClickListener(clickListener);
    }

    public void setBtnPauseButton(String button, OnClickListener clickListener){
        btnPause.setText(button);
        btnPause.setOnClickListener(clickListener);
    }


    public void show() {

        dialog.show();
    }

    public void dismiss() {

        dialog.dismiss();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }
}
