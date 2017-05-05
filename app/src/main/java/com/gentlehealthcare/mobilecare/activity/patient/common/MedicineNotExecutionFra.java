package com.gentlehealthcare.mobilecare.activity.patient.common;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.GridReasonAdapter;
/**
 * 
 * @ClassName: MedicineNotExecution 
 * @Description: 给药  拒绝执行
 * @author ouyang
 * @date 2015年2月28日 下午5:55:42 
 *
 */
@SuppressLint("ValidFragment")
public class MedicineNotExecutionFra extends BaseFragment {
	private String[] strings = { "暂不执行" };
	private GridReasonAdapter mAdapter;
	private SyncPatientBean patient;

	public MedicineNotExecutionFra(SyncPatientBean patient) {
		super();
		this.patient = patient;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medicine_not_execution, null);
		GridView gridView = (GridView) view.findViewById(R.id.gv_reason);
		gridView.setSelector(R.color.transparent);
		mAdapter = new GridReasonAdapter(getActivity(), strings);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(mItemClickListener);
		return view;
	}

	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mAdapter.notifyChanged(position);
		}
	};


	@Override
	protected void resetLayout(View view) {
		LinearLayout root = (LinearLayout) view
				.findViewById(R.id.root_fra_not_excute);
		SupportDisplay.resetAllChildViewParam(root);
	}
}
