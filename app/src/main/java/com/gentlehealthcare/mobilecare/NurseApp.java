package com.gentlehealthcare.mobilecare;

import android.app.Application;
import android.media.AudioManager;

import com.gentlehealthcare.mobilecare.view.GestFloatWindow;

public class NurseApp extends Application {

    private GestFloatWindow gestView;
    private boolean isVisible;
    private AudioManager audioManager;

    @Override
    public void onCreate() {

        super.onCreate();
        setVisible(false);
     //   gestView = new GestFloatWindow(getApplicationContext());
    }


    public void ShowGestWindow() {
        setVisible(true);
       // gestView.setVisibility(View.GONE);
    }

    public void HidnGestWindow() {
        setVisible(false);
       // gestView.setVisibility(View.GONE);
    }

    public boolean isVisible() {
        return isVisible;
    }


    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

}
