package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;

/**
 * 
 * @ClassName: FlowZhu 
 * @Description: 注意事项
 * @author ouyang
 * @date 2015年2月28日 下午5:43:51 
 *
 */
@SuppressLint("ValidFragment")
public class FlowZhuFra extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_zhu, null);
		return view;
	}

	@Override
	protected void resetLayout(View view) {
		LinearLayout root = (LinearLayout) view
				.findViewById(R.id.root_fra_zhu);
		SupportDisplay.resetAllChildViewParam(root);
	}
}
