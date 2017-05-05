package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.HelpListAdapter;

import java.util.List;
import java.util.Map;
/**
 * 
 * @ClassName: HelpActivity 
 * @Description: 帮助界面
 * @author ouyang
 * @date 2015年2月28日 下午5:41:37 
 *
 */
public class HelpAct extends BaseActivity implements OnClickListener, OnItemClickListener {
    private ListView listView;
    private TextView textView;
    private List<Map<String, String>> maps;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_help);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_help);
        int Flowpath = getIntent().getIntExtra("flow", 1);
		initView();
        HelpListAdapter adapter = new HelpListAdapter(this, maps);
        listView.setAdapter(adapter);
        textView.setText(maps.get(0).get("text"));
		ShowGestWindow();
	}

	@Override
	protected void resetLayout() {
		RelativeLayout root = (RelativeLayout) findViewById(R.id.root_help);
		SupportDisplay.resetAllChildViewParam(root);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_back) {
			finish();
			overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
		} else if (v.getId() == R.id.btn_search) {
			Button bt = (Button) findViewById(R.id.help_serach);
			final EditText et = (EditText) findViewById(R.id.help_et);
			final View vv = findViewById(R.id.help_ll);
			vv.setVisibility(View.VISIBLE);
			et.requestFocus();
			final InputMethodManager imm = (InputMethodManager) et.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					vv.setVisibility(View.GONE);
					et.clearFocus();
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(et.getApplicationWindowToken(), 0);
					}
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		textView.setText(maps.get(position).get("text"));
	}

	private void initView() {

		listView = (ListView) findViewById(R.id.lv_title);
		textView = (TextView) findViewById(R.id.tv_text);
		ImageButton button = (ImageButton) findViewById(R.id.btn_back);
		ImageButton ib_search = (ImageButton) findViewById(R.id.btn_search);
		listView.setOnItemClickListener(this);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		button.setOnClickListener(this);
		ib_search.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {

		finish();

		overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);

		super.onBackPressed();
	}

}
