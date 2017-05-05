package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.CopySealAdapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 
 * @ClassName: SealTube 
 * @Description: 给药模块  封管
 * @author ouyang
 * @date 2015年3月2日 上午8:43:29 
 *
 */
@SuppressLint("ValidFragment")
public class SealTubeFra extends BaseFragment  {
	private ListView lv_seal;
	private CopySealAdapter adapter=null;
	private String[] strings;

	public SealTubeFra(String[] strings) {

		super();
		this.strings=strings;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_copysealing, null);

		lv_seal= (ListView) view.findViewById(R.id.lv_seal);
		adapter=new CopySealAdapter(getActivity(),strings);
		lv_seal.setAdapter(adapter);
		lv_seal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.notifyChanged(position);
			}
		});
		return view;
	}

	@Override
	protected void resetLayout(View view) {
		LinearLayout root = (LinearLayout) view
				.findViewById(R.id.root_fra_copy);
		SupportDisplay.resetAllChildViewParam(root);
	}
}
