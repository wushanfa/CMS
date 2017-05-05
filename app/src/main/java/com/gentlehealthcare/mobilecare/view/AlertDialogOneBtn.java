package com.gentlehealthcare.mobilecare.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

public class AlertDialogOneBtn {

	private AlertDialog dialog;

	private TextView tvTitle, tvMessage;

	private Button btnConfirm;

	public AlertDialogOneBtn(Context context) {

		super();

		dialog = new AlertDialog.Builder(context).create();

		dialog.setCancelable(false);

		dialog.show();

		Window window = dialog.getWindow();

		window.setContentView(R.layout.dialog_one_btn);

		tvTitle = (TextView) window.findViewById(R.id.tv_title);

		tvMessage = (TextView) window.findViewById(R.id.tv_message);

		btnConfirm = (Button) window.findViewById(R.id.btn_confirm);
	}

	public void setTitle(String title) {

		tvTitle.setText(title);
	}

	public void setMessage(String msg) {

		tvMessage.setText(msg);
	}

	public void setButton(String button) {

		btnConfirm.setText(button);
	}

	public void setButtonListener(boolean isPositive, OnClickListener clickListener) {

		btnConfirm.setOnClickListener(clickListener);
	}

	public void show() {

		dialog.show();
	}

	public void dismiss() {

		dialog.dismiss();
	}

    public boolean isShowing(){
        return dialog.isShowing();
    }

}
