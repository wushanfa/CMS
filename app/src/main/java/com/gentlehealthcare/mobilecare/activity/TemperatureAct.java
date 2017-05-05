package com.gentlehealthcare.mobilecare.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.StaticLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.TemperaturePresenter;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.ITemperatureView;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import u.aly.br;

/**
 * Created by Zyy on 2016/12/7. 类说明：体温单录入
 */

public class TemperatureAct extends BaseActivity implements ITemperatureView,
		View.OnClickListener, View.OnFocusChangeListener {
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.btn_menu)
	private Button btn_menu;
	@ViewInject(R.id.tv_pat_name)
	private TextView tv_pat_name;
	@ViewInject(R.id.sp_time)
	private Spinner sp_time;
	@ViewInject(R.id.sp_temperature_type)
	private Spinner sp_temperature_type;
	@ViewInject(R.id.cb_pulse)
	private CheckBox cb_pulse;
	@ViewInject(R.id.cb_pacemaker)
	private CheckBox cb_pacemaker;
	@ViewInject(R.id.cb_ventilator)
	private CheckBox cb_ventilator;
	@ViewInject(R.id.et_temperature)
	private EditText et_temperature;
	@ViewInject(R.id.et_bloodsugar)
	private EditText et_bloodsugar;
	@ViewInject(R.id.et_pulse)
	private EditText et_pulse;
	@ViewInject(R.id.et_heartrate)
	private EditText et_heartrate;
	@ViewInject(R.id.et_breathing)
	private EditText et_breathing;
	@ViewInject(R.id.et_systolicpressure)
	private EditText et_systolicpressure;
	@ViewInject(R.id.et_diastolicpressure)
	private EditText et_diastolicpressure;
	@ViewInject(R.id.et_pain)
	private EditText et_pain;
	@ViewInject(R.id.btn_top_1)
	private Button btn_top_1;
	@ViewInject(R.id.btn_top_2)
	private Button btn_top_2;
	@ViewInject(R.id.btn_top_3)
	private Button btn_top_3;
	@ViewInject(R.id.btn_top_4)
	private Button btn_top_4;
	@ViewInject(R.id.btn_top_5)
	private Button btn_top_5;
	@ViewInject(R.id.btn_top_6)
	private Button btn_top_6;
	@ViewInject(R.id.table_num)
	private LinearLayout table_num;
	@ViewInject(R.id.btn_1)
	private Button btn_1;
	@ViewInject(R.id.btn_2)
	private Button btn_2;
	@ViewInject(R.id.btn_3)
	private Button btn_3;
	@ViewInject(R.id.btn_4)
	private Button btn_4;
	@ViewInject(R.id.btn_5)
	private Button btn_5;
	@ViewInject(R.id.btn_6)
	private Button btn_6;
	@ViewInject(R.id.btn_7)
	private Button btn_7;
	@ViewInject(R.id.btn_8)
	private Button btn_8;
	@ViewInject(R.id.btn_9)
	private Button btn_9;
	@ViewInject(R.id.btn_0)
	private Button btn_0;
	@ViewInject(R.id.btn_poit)
	private Button btn_poit;
	@ViewInject(R.id.btn_delete)
	private ImageButton btn_delete;
	@ViewInject(R.id.btn_finished)
	private Button btn_finished;

	private ProgressDialog progressDialog;
	private TemperaturePresenter temperaturePresenter;
	private int keyType = 0;
	private static StringBuffer sbTemperature;
	private static StringBuffer sbPulse;
	private static StringBuffer sbBreathing;
	private static StringBuffer sbSystolicpressure;
	private static StringBuffer sbDiastolicpressure;
	private static StringBuffer sbHeartRate;
	private MyPatientDialog dialog;
	private int whichPatients = 0;
	private SyncPatientBean currentPatient;
	private static int timeFlag = 0;
	private static int temperatureTypeFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_template);
		ViewUtils.inject(this);
		temperaturePresenter = new TemperaturePresenter(this);
		temperaturePresenter.initialSrc();
		sbTemperature = new StringBuffer();
		sbPulse = new StringBuffer();
		sbBreathing = new StringBuffer();
		sbSystolicpressure = new StringBuffer();
		sbDiastolicpressure = new StringBuffer();
		sbHeartRate=new StringBuffer();
		temperaturePresenter.getPatients(this, whichPatients);
	}

	@Override
	protected void resetLayout() {

	}

	@Override
	public void showProgressDialog(String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setMessage(msg);
		progressDialog.show();
	}

	@Override
	public void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void showToast(String str) {
		ShowToast(str);
	}

	@Override
	public void initialSrc() {
		btn_back.setOnClickListener(this);
		btn_menu.setOnClickListener(this);
		et_temperature.setOnFocusChangeListener(this);
		tv_pat_name.setOnClickListener(this);
		et_pulse.setOnFocusChangeListener(this);
		et_breathing.setOnFocusChangeListener(this);
		et_systolicpressure.setOnFocusChangeListener(this);
		et_diastolicpressure.setOnFocusChangeListener(this);
		et_heartrate.setOnFocusChangeListener(this);
		btn_top_1.setOnClickListener(this);
		btn_top_2.setOnClickListener(this);
		btn_top_3.setOnClickListener(this);
		btn_top_4.setOnClickListener(this);
		btn_top_5.setOnClickListener(this);
		btn_top_6.setOnClickListener(this);
		btn_1.setOnClickListener(this);
		btn_2.setOnClickListener(this);
		btn_3.setOnClickListener(this);
		btn_4.setOnClickListener(this);
		btn_5.setOnClickListener(this);
		btn_6.setOnClickListener(this);
		btn_7.setOnClickListener(this);
		btn_8.setOnClickListener(this);
		btn_9.setOnClickListener(this);
		btn_0.setOnClickListener(this);
		btn_poit.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_finished.setOnClickListener(this);
		List<String> time = new ArrayList<String>();
		time.add("6:00");
		time.add("10:00");
		time.add("14:00");
		time.add("18:00");
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, time);
		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		sp_time.setAdapter(arrayAdapter);
		sp_time.setOnItemSelectedListener(new SpinerTimeOnItemListener());

		List<String> temperature = new ArrayList<String>();
		temperature.add("腋温");
		temperature.add("口温");
		temperature.add("肛温");
		ArrayAdapter<String> arrayAdapterTemp = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, temperature);
		arrayAdapterTemp
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		sp_temperature_type.setAdapter(arrayAdapterTemp);
		sp_temperature_type
				.setOnItemSelectedListener(new SpinerTemperatureOnItemListener());

		cb_pacemaker
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							et_heartrate.setText("");
						}
					}
				});

		cb_pulse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					et_pulse.setText("");
				}
			}
		});
		cb_ventilator
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							et_breathing.setText("");
						}
					}
				});
		HidnKey(et_temperature);
		HidnKey(et_pulse);
		HidnKey(et_heartrate);
		HidnKey(et_breathing);
		HidnKey(et_systolicpressure);
		HidnKey(et_diastolicpressure);
	}

	@Override
	public void showKey(String[] strs, int flag) {
		table_num.setVisibility(View.VISIBLE);
		switch (flag) {
		case 1:
			keyType = 1;
			setKeyNum(strs);
			break;
		case 2:
			keyType = 2;
			setKeyNum(strs);
			break;
		case 3:
			keyType = 3;
			setKeyNum(strs);
			break;
		case 4:
			keyType = 4;
			setKeyNum(strs);
			break;
		case 5:
			keyType = 5;
			setKeyNum(strs);
			break;
		case 6:
			keyType = 6;
			setKeyNum(strs);
			break;

		}
	}

	@Override
	public void setPatientInfo(SyncPatientBean patientInfo) {
		tv_pat_name.setText(patientInfo.getName() +" "+ StringTool.isBedNumber(patientInfo.getBedLabel())+" "+patientInfo.getPatCode());
		currentPatient = patientInfo;
	}

	private void setKeyNum(String[] strs) {
		btn_top_1.setText(strs[0]);
		btn_top_2.setText(strs[1]);
		btn_top_3.setText(strs[2]);
		btn_top_4.setText(strs[3]);
		btn_top_5.setText(strs[4]);
		btn_top_6.setText(strs[5]);
	}

	@Override
	public void showPatients(final List<SyncPatientBean> deptPatient) {
		dialog = new MyPatientDialog(TemperatureAct.this,
				new MyPatientDialog.MySnListener() {

					@Override
					public void myOnItemClick(View view, int position, long id) {
						temperaturePresenter.setPatientInfo(deptPatient
								.get(position));
						whichPatients = position;
						dialog.dismiss();
					}

					@Override
					public void onRefresh() {
						// isRefreshPatient = true;
						temperaturePresenter.getPatients(TemperatureAct.this,
								1024);// 1000标记用来刷新
						dialog.dismiss();
					}
				}, whichPatients, deptPatient);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_pat_name:
			temperaturePresenter.showPatients();
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_menu:
			String bloodPressure = et_systolicpressure.getText() + "/"
					+ et_diastolicpressure.getText();
			temperaturePresenter.saveTemperature(currentPatient.getPatId(),currentPatient.getPatCode(),currentPatient.getVisitId(),String.valueOf(timeFlag),
					et_temperature.getText().toString(), String
							.valueOf(temperatureTypeFlag), et_pulse.getText()
							.toString(), et_heartrate.getText().toString(),
					et_breathing.getText().toString(), bloodPressure,
					et_bloodsugar.getText().toString(), et_pain.getText()
							.toString());
			break;
		case R.id.btn_top_1:
			switch (keyType) {
			case 1:
				sbTemperature.append(btn_top_1.getText());
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append(btn_top_1.getText());
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append(btn_top_1.getText());
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append(btn_top_1.getText());
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append(btn_top_1.getText());
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(btn_top_1.getText());
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_top_2:
			switch (keyType) {
			case 1:
				sbTemperature.append(btn_top_2.getText());
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append(btn_top_2.getText());
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append(btn_top_2.getText());
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append(btn_top_2.getText());
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append(btn_top_2.getText());
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(btn_top_2.getText());
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_top_3:
			switch (keyType) {
			case 1:
				sbTemperature.append(btn_top_3.getText());
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append(btn_top_3.getText());
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append(btn_top_3.getText());
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append(btn_top_3.getText());
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append(btn_top_3.getText());
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(btn_top_3.getText());
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_top_4:
			switch (keyType) {
			case 1:
				sbTemperature.append(btn_top_4.getText());
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append(btn_top_4.getText());
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append(btn_top_4.getText());
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append(btn_top_4.getText());
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append(btn_top_4.getText());
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(btn_top_4.getText());
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_top_5:
			switch (keyType) {
			case 1:
				sbTemperature.append(btn_top_5.getText());
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append(btn_top_5.getText());
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append(btn_top_5.getText());
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append(btn_top_5.getText());
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append(btn_top_5.getText());
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(btn_top_5.getText());
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_top_6:
			switch (keyType) {
			case 1:
				sbTemperature.append(btn_top_6.getText());
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append(btn_top_6.getText());
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append(btn_top_6.getText());
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append(btn_top_6.getText());
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append(btn_top_6.getText());
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(btn_top_6.getText());
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_1:
			switch (keyType) {
			case 1:
				sbTemperature.append("1");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("1");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("1");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("1");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("1");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append("1");
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_2:
			switch (keyType) {
			case 1:
				sbTemperature.append("2");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("2");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("2");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("2");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("2");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(2);
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_3:
			switch (keyType) {
			case 1:
				sbTemperature.append("3");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("3");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("3");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("3");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("3");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(3);
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_4:
			switch (keyType) {
			case 1:
				sbTemperature.append("4");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("4");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("4");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("4");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("4");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(4);
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_5:
			switch (keyType) {
			case 1:
				sbTemperature.append("5");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("5");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("5");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("5");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("5");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(5);
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_6:
			switch (keyType) {
			case 1:
				sbTemperature.append("6");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("6");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("6");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("6");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("6");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append("6");
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_7:
			switch (keyType) {
			case 1:
				sbTemperature.append("7");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("7");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("7");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("7");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("7");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append("7");
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_8:
			switch (keyType) {
			case 1:
				sbTemperature.append("8");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("8");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("8");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("8");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("8");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append("8");
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_9:
			switch (keyType) {
			case 1:
				sbTemperature.append("9");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("9");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("9");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("9");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("9");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append("9");
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_0:
			switch (keyType) {
			case 1:
				sbTemperature.append("0");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append("0");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append("0");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append("0");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append("0");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append("0");
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_poit:
			switch (keyType) {
			case 1:
				sbTemperature.append(".");
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				sbPulse.append(".");
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				sbBreathing.append(".");
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				sbSystolicpressure.append(".");
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				sbDiastolicpressure.append(".");
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				sbHeartRate.append(".");
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_delete:
			switch (keyType) {
			case 1:
				String temp = StringTool.delateLastChar(sbTemperature);
				sbTemperature.delete(0, sbTemperature.length());
				sbTemperature.append(temp);
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				String pulse = StringTool.delateLastChar(sbPulse);
				sbPulse.delete(0, sbPulse.length());
				sbPulse.append(pulse);
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				String breathing = StringTool.delateLastChar(sbBreathing);
				sbBreathing.delete(0, sbBreathing.length());
				sbBreathing.append(breathing);
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				String systolicpressure = StringTool
						.delateLastChar(sbSystolicpressure);
				sbSystolicpressure.delete(0, sbSystolicpressure.length());
				sbSystolicpressure.append(systolicpressure);
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				String diastolicpressure = StringTool
						.delateLastChar(sbDiastolicpressure);
				sbDiastolicpressure.delete(0, sbDiastolicpressure.length());
				sbDiastolicpressure.append(diastolicpressure);
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				String heartRate = StringTool.delateLastChar(sbHeartRate);
				sbHeartRate.delete(0, heartRate.length());
				sbHeartRate.append(heartRate);
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;
		case R.id.btn_finished:
			table_num.setVisibility(View.INVISIBLE);
			switch (keyType) {
			case 1:
				et_temperature.setText(sbTemperature.toString());
				break;
			case 2:
				et_pulse.setText(sbPulse.toString());
				break;
			case 3:
				et_breathing.setText(sbBreathing.toString());
				break;
			case 4:
				et_systolicpressure.setText(sbSystolicpressure.toString());
				break;
			case 5:
				et_diastolicpressure.setText(sbDiastolicpressure.toString());
				break;
			case 6:
				et_heartrate.setText(sbHeartRate.toString());
				break;
			}
			break;

		}

	}

	class SpinerTimeOnItemListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			timeFlag = position;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	}

	class SpinerTemperatureOnItemListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			temperatureTypeFlag = position;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			temperatureTypeFlag = 0;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.et_temperature:
			if (hasFocus) {
				temperaturePresenter.showKey(new String[] { "35.", "36.",
						"37.", "38.", "39.", "40." }, 1);
			}
			break;
		case R.id.et_pulse:
			if (hasFocus) {
				temperaturePresenter.showKey(new String[] { "6", "7", "8", "9",
						"10", "11" }, 2);
			}
			break;
		case R.id.et_breathing:
			if (hasFocus) {
				temperaturePresenter.showKey(new String[] { "15", "19", "20",
						"21", "22", "30" }, 3);
			}
			break;
		case R.id.et_systolicpressure:
			if (hasFocus) {
				temperaturePresenter.showKey(new String[] { "10", "90", "100",
						"110", "120", "130" }, 4);
			}
			break;
		case R.id.et_diastolicpressure:
			if (hasFocus) {
				temperaturePresenter.showKey(new String[] { "10", "90", "100",
						"110", "120", "130" }, 5);
			}
			break;
		case R.id.et_heartrate:
			if (hasFocus) {
				temperaturePresenter.showKey(new String[] { "60", "70", "80",
						"90", "100", "110" }, 6);
			}
			break;
		default:
			break;
		}
	};
}
