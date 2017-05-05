package com.gentlehealthcare.mobilecare.activity.intravenous;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;

/**
 * 静脉给药说明界面
 */
public class IntroductionsFra extends BaseFragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fra_intravenous_introductions,null);
        return view;
    }




    @Override
    protected void resetLayout(View view) {

    }


}
