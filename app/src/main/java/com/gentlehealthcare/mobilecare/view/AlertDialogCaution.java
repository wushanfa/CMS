package com.gentlehealthcare.mobilecare.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.gentlehealthcare.mobilecare.R;

public class AlertDialogCaution extends Dialog {
	Context context;

	public AlertDialogCaution(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_caution);
		
	}
}
