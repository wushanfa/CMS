package com.gentlehealthcare.mobilecare.activity.patient.common;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;

/**
 * @author ouyang
 * @ClassName: IdentityScanSuccess
 * @Description: 给药界面中患者身份确认成功
 * @date 2015年2月28日 下午5:48:15
 */
@SuppressLint("ValidFragment")
public class IdentityScanSuccessFra extends BaseFragment {

    private SyncPatientBean patient;
    private int workType;

    public IdentityScanSuccessFra(SyncPatientBean patient, int workType) {

        super();
        this.workType = workType;
        this.patient = patient;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_identity_scan_success, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_prompt_window);
        String content = "";
        if (workType == 0) {
            content = "输液";
        } else if (workType == 1) {
            content = "胰岛素";
        }
        textView.setText("你好，我将要帮你准备施打" + content + "...");
        return view;
    }


    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_scan_success);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
