package com.gentlehealthcare.mobilecare.common;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gentlehealthcare.mobilecare.R;

/**
 * Created by user on 2017/3/16.
 */

public class MyToast {
    private final Activity activity;
    private final int image;
    private final String text;
    private final int time;

    public MyToast(Activity activity, int image, String text, int time) {
        this.activity = activity;
        this.image = image;
        this.text = text;
        this.time = time;
    }

    public void showToast() {
        View view = LayoutInflater.from(activity).inflate(R.layout.lyaout_mytoast, null);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.mytoast);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        ImageView img = (ImageView) view.findViewById(R.id.toast_img);
        TextView textview = (TextView) view.findViewById(R.id.toast_text);
        if(image!=0){
            img.setImageResource(image);
        }else{
            img.setVisibility(View.GONE);
        }
        textview.setText(text);
        Toast toast = new Toast(activity);
        toast.setDuration(time);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toast.show();
    }
}
