package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import java.util.ArrayList;
import java.util.List;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.db.table.TB_MedicineInfo;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.MedicineListAdapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 
 * @ClassName: MedicineScanSuccess 
 * @Description: 给药  药品扫描成功
 * @author ouyang
 * @date 2015年2月28日 下午5:57:06 
 *
 */
@SuppressLint("ValidFragment")
public class MedicineScanSuccessFra extends BaseFragment{
	private SyncPatientBean patient;
	private TB_MedicineInfo medicine;
    private ListView lv_medicine;
    private List<List<TB_MedicineInfo>> medicines;
    private OrderItemBean orderItemBean;

	public MedicineScanSuccessFra(SyncPatientBean patient,OrderItemBean orderItemBean) {
		super();
		this.patient = patient;
        this.orderItemBean=orderItemBean;
	}


    @Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_medicine_img, null);
        lv_medicine= (ListView) view.findViewById(R.id.lv_medicine);
        List<OrderItemBean> list=new ArrayList<OrderItemBean>();
        list.add(orderItemBean);
        lv_medicine.setAdapter( new MedicineListAdapter(getActivity(), list));

		return view;
	}

	@Override
	protected void resetLayout(View view) {
		LinearLayout root = (LinearLayout) view
				.findViewById(R.id.root_fra_medicine);
		SupportDisplay.resetAllChildViewParam(root);
	}
}

