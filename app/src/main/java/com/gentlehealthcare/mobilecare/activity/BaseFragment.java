package com.gentlehealthcare.mobilecare.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;

/**
 * 基类Fragment
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, getClass().getName() + "---  onDestroyView");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        resetLayout(getView());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, getClass().getName() + "---  onDestroy");
    }

    protected abstract void resetLayout(View view);

    /**
     * 吐司提示
     *
     * @param text 提示文本
     */
    public void ShowToast(String text) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast提示
     *
     * @param txtPath 提示文本
     */
    public void ShowToast(int txtPath) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), this.getString(txtPath), Toast.LENGTH_SHORT).show();
        }
    }
}
