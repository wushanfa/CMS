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
 * 
 * @ClassName: IdentityScan 
 * @Description: 给药界面中患者身份确认
 * @author ouyang
 * @date 2015年2月28日 下午5:47:16 
 *
 */
@SuppressLint("ValidFragment")
public class IdentityScanFra extends BaseFragment{
	private SyncPatientBean patient;
	@SuppressLint("ValidFragment")
	public IdentityScanFra(SyncPatientBean patient) {

		super();
     this.patient=patient;
	}

    public  IdentityScanFra(){

    }

    @Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_insulin_overview, null);

		TextView textView = (TextView) view.findViewById(R.id.tv_prompt_window);

		return view;
	}


	@Override
	protected void resetLayout(View view) {
		LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_overview);
		SupportDisplay.resetAllChildViewParam(root);
	}
}
