package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.GridReasonAdapter;

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
/**
 * 
 * @ClassName: NotExecution 
 * @Description: 给药  拒绝执行
 * @author ouyang
 * @date 2015年3月2日 上午8:58:10 
 *
 */
@SuppressLint("ValidFragment")
public class NotExecutionFra extends BaseFragment {

	private String[] strings = { "NPO", "检查中", "治疗中", "请假中", "入睡中", "缺药", "拒绝" };
	private SyncPatientBean patient;
	private GridReasonAdapter mAdapter;
    private String notExecutionReason="";//不执行原因

	public NotExecutionFra(SyncPatientBean patient) {
		super();
		this.patient = patient;
	}



	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_not_execution, null);
		GridView gridView = (GridView) view.findViewById(R.id.gv_reason);
		gridView.setSelector(R.color.transparent);
		gridView.setAdapter(mAdapter = new GridReasonAdapter(getActivity(), strings));
		gridView.setOnItemClickListener(mItemClickListener);
        notExecutionReason=strings[0];
		return view;
	}

	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            notExecutionReason=strings[position];
			mAdapter.notifyChanged(position);
		}
	};


    public String getNotExecutionReason() {
        return notExecutionReason;
    }

	@Override
	protected void resetLayout(View view) {

		LinearLayout root = (LinearLayout) view
				.findViewById(R.id.root_fra_execution);
		SupportDisplay.resetAllChildViewParam(root);
	}
}
