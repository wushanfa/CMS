package com.gentlehealthcare.mobilecare.tool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

public class DialogUtils {

    public static ProgressDialog createSimpleProgressDialog(Activity activity, String message) {
        Log.d("pull", activity + "-progressDialog--" + activity.isFinishing());
        if (!activity.isFinishing()) {
            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(true);
            progressDialog.show();
            return progressDialog;
        }
        return null;
    }
}
