package com.gentlehealthcare.mobilecare.activity.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.activity.patient.DeptPatientAct;
import com.gentlehealthcare.mobilecare.activity.patient.insulin.InsulinFlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.FlowAct;
import com.gentlehealthcare.mobilecare.activity.work.WorkAct;
import com.gentlehealthcare.mobilecare.constant.FlowConstant;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.constant.TemplateConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.LoadPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientWorkBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientWorkInfo;
import com.gentlehealthcare.mobilecare.net.impl.LoadPatientReq;
import com.gentlehealthcare.mobilecare.net.impl.SyncPatientWorkRequest;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AutoCallBackTextView;
import com.gentlehealthcare.mobilecare.view.adapter.PatientWorkAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 病人工作列表界面 Created by ouyang on 15/7/5.
 */
public class PatientWorkAct extends ABToolBarActivity {

	private SyncPatientBean patient = null;
	private AutoCallBackTextView tv_info = null;
	private ProgressDialog progressDialog = null;
	private PullToRefreshListView pullToRefreshListView = null;
	private PatientWorkAdapter adapter = null;
	private List<SyncPatientWorkInfo> list = null;
	private ListView listView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_home_patientwork);
		progressDialog = new ProgressDialog(PatientWorkAct.this);
		patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");
		initWidget();

		DoIdentityConfirm();
		// 配置工具栏
		setToolBarDrawable(new int[] { R.drawable.act_home_deptpatbtn,
				R.drawable.act_home_mypatbtn, R.drawable.act_home_homebtn,
				R.drawable.act_home_workbtn, R.drawable.act_home_attentionbtn });
		setRightBtnDrawable(R.drawable.act_home_chaxunbtn);
		setAbDoToolBar(new ABDoToolBar() {
			@Override
			public void onCheckedChanged(int i) {
				switch (i) {
				case 0:
					startActivity(new Intent(PatientWorkAct.this,
							DeptPatientAct.class));
					break;
				case 1:
					startActivity(new Intent(PatientWorkAct.this, HomeAct.class));
					break;
				case 2:
					startActivity(new Intent(PatientWorkAct.this, HomeAct.class));
					break;
				case 3:
					startActivity(new Intent(PatientWorkAct.this, WorkAct.class));
					break;
				case 4:
					startActivity(new Intent(PatientWorkAct.this,
							SystemAttentionAct.class));
					break;
				}
			}

			@Override
			public void onLeftBtnClick() {

			}

			@Override
			public void onRightBtnClick() {

			}
		});

		pullToRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						DoLoadPatientWorkRuq();
					}
				});

		listView = pullToRefreshListView.getRefreshableView();
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.setDividerHeight(10);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				SyncPatientWorkInfo info = list.get(i - 1);
				String templateId = info.getTemplateId();
				if (info.getPerformStatus().equals(
						MedicineConstant.STATE_WAITING)) {
					// 待执行
					if (templateId.equals(TemplateConstant.MEDICINE
							.GetTemplateId())) {
						// 药品
						OrderItemBean orderItemBean = new OrderItemBean();
						orderItemBean.setPlanOrderNo(info.getPlanOrderNo());
						Intent intent = new Intent(PatientWorkAct.this,
								FlowAct.class);
						intent.putExtra("flow",
								FlowConstant.PatientFlow.MEDICINE_LIST);
						intent.putExtra("orderItemBean", orderItemBean);
						intent.putExtra("patient", patient);
						startActivity(intent);
					} else {
						// 胰岛素
						OrderItemBean orderItemBean = new OrderItemBean();
						orderItemBean.setTemplateId(info.getTemplateId());
						orderItemBean.setPlanOrderNo(info.getPlanOrderNo());
						Intent intent = new Intent(PatientWorkAct.this,
								InsulinFlowAct.class);
						intent.putExtra("flow",
								FlowConstant.PatientFlow.MEDICINE_LIST);
						intent.putExtra("patient", patient);
						intent.putExtra("orderItemBean", orderItemBean);
						startActivity(intent);
					}
				} else if (info.getPerformStatus().equals(
						MedicineConstant.STATE_EXECUTING)) {

					OrderItemBean orderItemBean = new OrderItemBean();
					orderItemBean.setPlanOrderNo(info.getPlanOrderNo());
					orderItemBean.setPlanTimeAttr(info.getPlanTimeAttr());
					// 执行中
					if (templateId.equals(TemplateConstant.MEDICINE
							.GetTemplateId())) {
						// 药品
						Intent intent = new Intent(PatientWorkAct.this,
								FlowAct.class);
						intent.putExtra("flow",
								FlowConstant.PatientFlow.PATROL_COUNT_DOWN);
						intent.putExtra("patient", patient);
						intent.putExtra("orderItemBean", orderItemBean);
						startActivity(intent);

					} else if (templateId.equals(TemplateConstant.INSULIN
							.GetTemplateId())) {
						// 胰岛素
						Intent intent = new Intent(PatientWorkAct.this,
								InsulinFlowAct.class);
						intent.putExtra("flow",
								FlowConstant.PatientFlow.PATROL_COUNT_DOWN);
						intent.putExtra("patient", patient);
						intent.putExtra("orderItemBean", orderItemBean);
						startActivity(intent);
					} else {
						// 血糖
						Intent intent = new Intent(PatientWorkAct.this,
								InsulinFlowAct.class);
						intent.putExtra("flow",
								FlowConstant.PatientFlow.BLOOD_TEST);
						intent.putExtra("patient", patient);
						intent.putExtra("orderItemBean", orderItemBean);
						startActivity(intent);
					}

				} else {
					// 已执行
					ShowToast("该医嘱已完成");
				}
			}
		});
		list = new ArrayList<SyncPatientWorkInfo>();
		listView.setAdapter(adapter = new PatientWorkAdapter(
				PatientWorkAct.this, list));

	}

	@Override
	protected void resetLayout() {
		RelativeLayout root = (RelativeLayout) findViewById(R.id.root_patientwork);
		SupportDisplay.resetAllChildViewParam(root);
	}

	/**
	 * 初始化控件
	 */
	private void initWidget() {
		tv_info = (AutoCallBackTextView) findViewById(R.id.tv_info);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listView);
		findViewById(R.id.btn_back).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						finish();
					}
				});
		tv_info.setDetailStr(getPatientInfoDetail());
		tv_info.setContentStr(getPatientInfo());
		tv_info.collection();
	}

	/**
	 * 根据工作状态，来加载服务器最新数据
	 */
	private void DoLoadPatientWorkRuq() {
		progressDialog.show();
		SyncPatientWorkBean syncPatientWorkBean = new SyncPatientWorkBean();
		syncPatientWorkBean.setPatId(patient.getPatId());
		RequestManager.connection(new SyncPatientWorkRequest(
				PatientWorkAct.this, new IRespose<SyncPatientWorkBean>() {
					@Override
					public void doResult(
							SyncPatientWorkBean syncPatientWorkBean, int id) {

					}

					@Override
					public void doResult(String result)
							throws KeyObsoleteException {
						SyncPatientWorkBean syncPatientWorkBean = new Gson()
								.fromJson(result, SyncPatientWorkBean.class);
						if (syncPatientWorkBean.isResult()) {
							List<SyncPatientWorkInfo> beans = syncPatientWorkBean
									.getPlans();
							list.clear();
							int index = 0;
							if (beans != null && beans.size() > 0) {

								String time = "";

								Calendar calendar = Calendar.getInstance();
								calendar.setTime(new Date());
								int totalMinute = calendar
										.get(Calendar.HOUR_OF_DAY)
										* 60
										+ calendar.get(Calendar.MINUTE);
								int minMinute = DateTool
										.GetMinuteByHourAndMinute(beans.get(0)
												.getTime());
								for (int i = 0; i < beans.size(); i++) {
									SyncPatientWorkInfo info = beans.get(i);
									if (info.getTime().equals(time)) {
										info.setTime("");
									} else {
										time = info.getTime();
										int minute = Math.abs(totalMinute
												- DateTool
														.GetMinuteByHourAndMinute(time));
										if (minute < minMinute) {
											index = i;
										}
									}
									list.add(info);
								}
							}
							adapter.notifyDataSetChanged();
							listView.setSelection(index);
						} else {
							ShowToast(syncPatientWorkBean.getMsg());
						}
						pullToRefreshListView.onRefreshComplete();
						progressDialog.dismiss();
					}

					@Override
					public void doException(Exception e, boolean networkState) {
						if (networkState)
							Toast.makeText(PatientWorkAct.this, "操作异常.",
									Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(PatientWorkAct.this,
									getString(R.string.netstate_content),
									Toast.LENGTH_SHORT).show();
						pullToRefreshListView.onRefreshComplete();
						progressDialog.dismiss();
					}
				}, 0, true, syncPatientWorkBean));
	}

	/**
	 * 患者身份核实处理 仅仅用于后台记录信息
	 */
	private void DoIdentityConfirm() {
		progressDialog.setMessage("正在核对身份,请稍后..");
		progressDialog.show();
		LoadPatientBean loadPatientBean = new LoadPatientBean();
		loadPatientBean.setPatId(patient.getPatId());
		RequestManager.connection(new LoadPatientReq(PatientWorkAct.this,
				new IRespose<LoadPatientBean>() {
					@Override
					public void doResult(LoadPatientBean loadPatientBean, int id) {

					}

					@Override
					public void doResult(String result)
							throws KeyObsoleteException {
						progressDialog.dismiss();
						LoadPatientBean loadPatientBean = new Gson().fromJson(
								result, LoadPatientBean.class);
						// if (loadPatientBean.)
						DoLoadPatientWorkRuq();
					}

					@Override
					public void doException(Exception e, boolean networkState) {
						progressDialog.dismiss();
						if (networkState)
							Toast.makeText(PatientWorkAct.this, "操作异常.",
									Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(PatientWorkAct.this,
									getString(R.string.netstate_content),
									Toast.LENGTH_SHORT).show();
					}
				}, 0, true, loadPatientBean));

	}

	/**
	 * 
	 * @Title: getPatientInfo
	 * @Description: 获取病人信息
	 * @param @return
	 * @return String
	 * @throws
	 */
	private String getPatientInfo() {
		return "床号: " + patient.getBedLabel() + "\n" + "姓名: "
				+ patient.getName();
	}

	/**
	 * 
	 * @Title: getPatientInfoDetail
	 * @Description: 获取病人详细信息
	 * @param @return
	 * @return String
	 * @throws
	 */
	private String getPatientInfoDetail() {
		return getPatientInfo();
	}
}
