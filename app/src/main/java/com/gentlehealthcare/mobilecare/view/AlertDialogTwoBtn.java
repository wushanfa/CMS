package com.gentlehealthcare.mobilecare.view;

import android.app.AlertDialog;
import android.content.Context;

import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

public class AlertDialogTwoBtn {

    private AlertDialog dialog;

    private TextView tvTitle, tvMessage;

    private Button btnLeft, btnRight;

    public AlertDialogTwoBtn(Context context) {

        super();

        dialog = new AlertDialog.Builder(context).create();

        dialog.setCancelable(false);

        dialog.show();

        Window window = dialog.getWindow();

        window.setContentView(R.layout.dialog_two_btn);

        tvTitle = (TextView) window.findViewById(R.id.tv_title);

        tvMessage = (TextView) window.findViewById(R.id.tv_message);

        btnLeft = (Button) window.findViewById(R.id.btn_left);

        btnRight = (Button) window.findViewById(R.id.btn_right);
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
