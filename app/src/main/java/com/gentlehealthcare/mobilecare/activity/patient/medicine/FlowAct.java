package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.FlowConstant;
import com.gentlehealthcare.mobilecare.constant.FlowConstant.PatientFlow;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.db.dao.MedicineRecordDao;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.JsonFileUtil;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.ExecuteMedicineInfo;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.PatrolBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.PatrolRequest;
import com.gentlehealthcare.mobilecare.tool.DesUtil;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.gentlehealthcare.mobilecare.view.CustomEditTextDialog;
import com.gentlehealthcare.mobilecare.view.adapter.FlowsheetAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.MyFragmentAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;

/**
 * 
 * @ClassName: FlowActivity 
 * @Description: 给药流程界面
 * @author ouyang
 * @date 2015年2月28日 下午5:42:17 
 *
 */
public class FlowAct extends ABToolBarActivity implements OnClickListener {

	private String[] fuctions = { "药", "注", "历" };

	private String[] flows = { "身份", "说明", "给药", "巡视", "封管" };

	private List<Map<String, String>> maps;

	private FlowsheetAdapter mAdapter;

	private FlowSheetFra flowSheet;

	private MarginLayoutParams lvParams;

	private int marginRight;

	private boolean isVisible = false;

	private int Flowpath = 1;

	private int Publishfp = 0;

	private SyncPatientBean patient;

	private int Publishflag = 0;

	private SharedPreferences preferences;

	private SharedPreferences.Editor editor;

	private String patientId;
	

	private ViewPager vp_flow;

	
	private RadioGroup radioGroup;
	
	private ListView lv_right;

    private MyFragmentAdapter vpAdapter=null;
    private ProgressDialog progressDialog=null;
    private MedicineRecordDao medicineRecordDao=null;
    private CustomEditTextDialog dialog;
	private FlowConstant.PatientFlow flow;

    public Button getBtn_patientflow() {
        return btn_patientflow;
    }

    private Button btn_patientflow;
	private ImageView iv_backSheet;
	
	private AlertDialogOneBtn alertDialog=null;
	private OrderItemBean orderItemBean=null;
	//private List<OrderItemBean> orderItemBeans=null;
	
	
	@Override
	protected void onDestroy() {

		editor.putBoolean(patientId, false);

		editor.commit();

		super.onDestroy();
        if (alertDialog!=null&&alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        if (dialog!=null&&dialog.isShowing())
            dialog.dismiss();
	}

	@Override
	protected void resetLayout() {
		RelativeLayout root = (RelativeLayout) findViewById(R.id.root_flow);
		SupportDisplay.resetAllChildViewParam(root);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flow);
		flow= (PatientFlow) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENTFLOW);
		initWidget();
        medicineRecordDao=MedicineRecordDao.newInstance(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在提交数据,请稍后");
		patient = (SyncPatientBean) getIntent().getSerializableExtra(GlobalConstant.KEY_PATIENT);
//		if(getIntent().hasExtra(GlobalConstant.KEY_SCANORDERITEMBEAN)){
//			orderItemBeans= (List<OrderItemBean>) getIntent().getSerializableExtra(GlobalConstant.KEY_SCANORDERITEMBEAN);
//		}else{
//			orderItemBean= (OrderItemBean) getIntent().getSerializableExtra(GlobalConstant.KEY_ORDERITEMBEAN);
//		}
		orderItemBean= (OrderItemBean) getIntent().getSerializableExtra(GlobalConstant.KEY_ORDERITEMBEAN);
		preferences = getSharedPreferences("NURSE_MOBILE", MODE_PRIVATE);
		editor = preferences.edit();
		if (patient==null){
			finish();
		}
		patientId = patient.getPatId();
		editor.putBoolean(patientId, true);
		editor.commit();
		maps = new ArrayList<Map<String, String>>();

		for (int i = 0; i < flows.length; i++) {

			Map<String, String> map = new HashMap<String, String>();

			map.put("name", flows[i]);

			if (i == 0) {

				map.put("state", "checked");
			} else {

				map.put("state", "normal");
			}

			maps.add(map);
		}
		showWitchOne();
		final List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(flowSheet);
		fragments.add(new FlowZhuFra());
		fragments.add(new FlowLiFra(patient,0));
		vp_flow.setAdapter(vpAdapter=new MyFragmentAdapter(getSupportFragmentManager(), fragments));
		vp_flow.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return false;
			}
		});
		lvParams = (MarginLayoutParams) lv_right.getLayoutParams();
		lv_right.setAdapter(mAdapter=new FlowsheetAdapter(this, maps));
		lv_right.postDelayed(lvRunnable, 1000);
		lv_right.setOnTouchListener(new MyTouchListener());
		lv_right.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				alertDialog = new AlertDialogOneBtn(FlowAct.this);

				alertDialog.setTitle(flows[position] + "流程说明");

				alertDialog.setMessage("流程说明" + (position + 1));

				alertDialog.setButton("知道了");

				alertDialog.setButtonListener(false, new OnClickListener() {

					@Override
					public void onClick(View v) {

						alertDialog.dismiss();

					}
				});
				return false;
			}
		});
		iv_backSheet=(ImageView)findViewById(R.id.iv_backSheet);
		iv_backSheet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_flowcontent);
				if (fragment instanceof FlowSheetFra) {
					((FlowSheetFra) fragment).backSheet();
				}
			}
		});

        setToolBarDrawable(new int[]{R.drawable.act_home_workbtn, R.drawable.act_home_notesbtn, R.drawable.act_home_historybtn});
        setRightBtnDrawable(R.drawable.act_home_chaxunbtn);
        setValid(true);
        setCheck(0);
        setAbDoToolBar(new ABDoToolBar() {
            @Override
            public void onCheckedChanged(int checkedId) {
                if (checkedId==0){
                    iv_backSheet.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent,flowSheet).commit();
                }else if (checkedId==1){
                    iv_backSheet.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent,new FlowZhuFra()).commit();
                }else{
                    iv_backSheet.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent,new FlowLiFra(patient,0)).commit();
                }
            }
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
            }
        });
		
	}

	private void showWitchOne(){
		//注释于2016-02-19 通知进来直接显示医嘱列表，后来被修改成通知进来先扫描患者腕带信息
//		if(flow==PatientFlow.MEDICINE_END){
//			flowSheet = new FlowSheetFra(patient,PatientFlow.MEDICINE_LIST,orderItemBean);
//			Bundle bundle=new Bundle();
//			bundle.putInt(GlobalConstant.KEY_NOTIFICATION2MEDICINE,GlobalConstant.VALUE_NOTIFICATION2MEDICINE);
//			flowSheet.setArguments(bundle);
//			getSupportFragmentManager().beginTransaction().add(R.id.fl_flowcontent,flowSheet).commit();
//		}else{
//			flowSheet = new FlowSheetFra(patient,flow,orderItemBean);
//			getSupportFragmentManager().beginTransaction().add(R.id.fl_flowcontent,flowSheet).commit();
//		}
//		if(flow==PatientFlow.MEDICINE_INJECTION){
//			flowSheet=new FlowSheetFra(patient,PatientFlow.MEDICINE_INJECTION,orderItemBean);
//			Bundle bundle=new Bundle();
//			bundle.putString(GlobalConstant.KEY_FLAG_SCAN,GlobalConstant.VALUE_FLAG_SCAN);
//			flowSheet.setArguments(bundle);
//			getSupportFragmentManager().beginTransaction().add(R.id.fl_flowcontent,flowSheet).commit();
//		}else{
//			flowSheet = new FlowSheetFra(patient,flow,orderItemBean);
//			getSupportFragmentManager().beginTransaction().add(R.id.fl_flowcontent,flowSheet).commit();
//		}
		if(flow==PatientFlow.MEDICINE_INTRODUCTION){
			flowSheet=new FlowSheetFra(patient,PatientFlow.MEDICINE_LIST,orderItemBean);
			Bundle bundle=new Bundle();
			bundle.putString(GlobalConstant.KEY_FLAG_SCAN,GlobalConstant.VALUE_FLAG_SCAN);
			flowSheet.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().add(R.id.fl_flowcontent,flowSheet).commit();
		}else{
			flowSheet = new FlowSheetFra(patient,flow,orderItemBean);
			if(flow==PatientFlow.PATROL_COUNT_DOWN){
				Bundle bundle=new Bundle();
				bundle.putString(GlobalConstant.KEY_FLAG_SCAN_PATROL,GlobalConstant.VALUE_FLAG_SCAN_PATROL);
				flowSheet.setArguments(bundle);
			}
			getSupportFragmentManager().beginTransaction().add(R.id.fl_flowcontent,flowSheet).commit();
		}
	}

	private void initWidget(){
		vp_flow=(ViewPager) findViewById(R.id.vp_flow);
		lv_right=(ListView) findViewById(R.id.lv_right);
		radioGroup=(RadioGroup) findViewById(R.id.rgp_bottom);
        btn_patientflow= (Button) findViewById(R.id.btn_patientflow);
        btn_patientflow.setVisibility(View.GONE);
		RadioButton button;

		for (int i = 0; i < radioGroup.getChildCount(); i++) {

			button = (RadioButton) radioGroup.getChildAt(i);

			button.setId(i);

			button.setText(fuctions[i]);
		}

		radioGroup.check(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId==0){
					iv_backSheet.setVisibility(View.VISIBLE);
					getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent,flowSheet).commit();
				}else if (checkedId==1){
					iv_backSheet.setVisibility(View.INVISIBLE);
					getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent,new FlowZhuFra()).commit();
				}else{
					iv_backSheet.setVisibility(View.INVISIBLE);
					getSupportFragmentManager().beginTransaction().replace(R.id.fl_flowcontent,new FlowLiFra(patient,0)).commit();
				}
            }
        });
        vp_flow.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                FlowSheetFra fra= (FlowSheetFra) vpAdapter.getItem(0);
                if (fra!=null){
                    fra.DoCurrentViewIsPatrol(i);
                }
                radioGroup.getChildAt(i).performClick();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
		findViewById(R.id.btn_back).setOnClickListener(this);
        btn_patientflow.setOnClickListener(this);
		findViewById(R.id.btn_patient_help).setOnClickListener(this);
		findViewById(R.id.btn_patient_search).setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: updateFlow 
	 * @Description: 更新给药进度 
	 * @param @param flow
	 * @param @param medicine   
	 * @return void   
	 * @throws
	 */
	public void updateFlow(PatientFlow flow){
		changeFlowsColor( flow);
		int j = 0;
		for (int i = 0; i < maps.size(); i++) {
			if (maps.get(i).get("state").equals("checked")) {

				Flowpath = i + 1;
			} else if (maps.get(i).get("state").equals("excuted")) {
				j++;
			}
		}
	}
	



	private void changeFlowsColor(PatientFlow flow) {

		boolean isChange = false;

		int position = 0;

		switch (flow) {

		case EXPLAIN:

			position = 1;

			isChange = true;
			break;

		case MEDICINE_LIST:
			position = 2;
			isChange = true;
			break;

		case PATROL_RECORD:
			position = 3;
			isChange = true;
			break;

		case SEALING:
			position = 4;
			isChange = true;
			break;
		default:
			break;
		}

		if (isChange) {
			for (int i = 0; i < maps.size(); i++) {
				if (i < position) {
					maps.get(i).put("state", "excuted");
				} else if (i == position) {
					maps.get(i).put("state", "checked");
				}
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	Runnable lvRunnable = new Runnable() {

		@Override
		public void run() {
			marginRight = -lv_right.getWidth() / 4 * 3;
			lvParams.rightMargin = marginRight;
			lv_right.setLayoutParams(lvParams);
		}
	};



	class MyTouchListener implements OnTouchListener {

		private float xUp;

		private float xDown;

		private float yUp;

		private float yDown;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:

				xDown = event.getX();

				yDown = event.getY();

				break;

			case MotionEvent.ACTION_UP:

				xUp = event.getX();

				yUp = event.getY();

				if (Math.abs(yUp - yDown) < 100) {

					if (xUp - xDown > 50) {

						if (isVisible) {

							isVisible = false;

							lvParams.rightMargin = marginRight;

							lv_right.setLayoutParams(lvParams);
						}
					}
					if (xDown - xUp > 50) {

						if (!isVisible) {

							isVisible = true;

							lvParams.rightMargin = 0;

							lv_right.setLayoutParams(lvParams);
						}
					}
				}

				break;
			}

			return false;
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
		super.onBackPressed();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_back:
            startActivity(new Intent(FlowAct.this, HomeAct.class));
			finish();
			overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
			break;
		case R.id.btn_patientflow:
			  dialog=new CustomEditTextDialog(FlowAct.this,R.style.myDialogTheme);
			dialog.setContent(new String[]{"记录内容:"});
			dialog.setTitle(getResources().getString(R.string.notification));
			dialog.setLeftButton(getResources().getString(R.string.cancel), new OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
				}
			});
			dialog.setRightButton(getResources().getString(R.string.make_sure), new OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					progressDialog.show();
					final FlowSheetFra fra= (FlowSheetFra) vpAdapter.getItem(0);
					PatrolBean patrolBean=new PatrolBean();
					patrolBean.setPerformDesc(dialog.getText()[0]);
					RequestManager.connection(new PatrolRequest(FlowAct.this, new IRespose<PatrolBean>() {
						@Override
						public void doResult(PatrolBean patrolBean, int id) {

						}

						@Override
						public void doResult(String result) throws KeyObsoleteException{
							progressDialog.dismiss();
							if (UserInfo.getKey() != null && !"".equals(UserInfo.getKey())){
								try {
									result = DesUtil.decrypt(result, UserInfo.getKey());
									Gson gson = new Gson();
									Type type = new TypeToken<List<ExecuteMedicineInfo>>() {
									}.getType();
									List<ExecuteMedicineInfo> list = gson.fromJson(result, type);
									if (list != null && list.size() > 0) {
										boolean flag = true;
										for (ExecuteMedicineInfo executeMedicineInfo : list) {
											if (!flag)
												break;
											flag = executeMedicineInfo.getResult().equals("success");
										}
										if (!flag) {
											Toast.makeText(FlowAct.this, "执行注射操作失败（部分药品正在注射或者已注射)", Toast.LENGTH_SHORT).show();
										}
									}
								}catch (BadPaddingException e){
									throw new KeyObsoleteException("密钥错误");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

						@Override
						public void doException(Exception e,boolean networkState) {
							progressDialog.dismiss();
							if (JsonFileUtil.isLocal()){

								return;
							}
							if (networkState)
								Toast.makeText(FlowAct.this, R.string.unstable, Toast.LENGTH_SHORT).show();
							else {
								toErrorAct();
							}
						}
					},0,true,patrolBean));
				}

			});
			dialog.show();
			break;
		case R.id.btn_patient_help:
//			Intent intent = new Intent(FlowAct.this,HelpAct.class);
//			intent.putExtra("flow", Flowpath);
//			startActivity(intent);
//			overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
			break;
		case R.id.btn_patient_search:
			break;
		default:
			break;
		}
	}

	private void toErrorAct(){
		Intent intent=new Intent();
		intent.setClass(FlowAct.this, ErrorAct.class);
		startActivity(intent);
	}

	/**
	 * 红外扫描获取的值
	 * @param result
	 */
	public void DoCameraResult(String result){
		if (vp_flow.getCurrentItem()==0){
			flowSheet.DoCameraResult(result);
		}
	}


}
