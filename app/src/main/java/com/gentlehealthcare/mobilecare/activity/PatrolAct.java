package com.gentlehealthcare.mobilecare.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.adapter.NursingPatrolAdapter;
import com.gentlehealthcare.mobilecare.adapter.NursingPatrolDictAdapter;
import com.gentlehealthcare.mobilecare.bean.orders.DictCommonBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.bean.sys.BarcodeDict;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.NursingPatrolPresenter;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.DeviceTool;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.INursingPatrolView;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.gentlehealthcare.mobilecare.tool.StringTool.patternCode;

public class PatrolAct extends BaseActivity implements INursingPatrolView, View.OnClickListener {
  private ListView lv_order_list;
  private TextView tv_pat_name;
  private TextView tv_time_patrol;
  private Button btn_menu;
  private Button btn_back;
  private Button btn_history;
  private GridView gv_patrol_text;
  private NursingPatrolPresenter nursingPatrolPresenter;
  List<SyncPatientBean> deptPatient;
  private SyncPatientBean patientBean = null;
  private int position = 0;
  private ProgressDialog progressDialog;
  private boolean isRefreshPatient = false;
  private MyPatientDialog dialog;
  private int whichPatients = 0;
  private String itemCodes = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_patrol);
    nursingPatrolPresenter = new NursingPatrolPresenter(this);
    nursingPatrolPresenter.initalSrc();
    patientBean = new SyncPatientBean();
    deptPatient = new ArrayList<SyncPatientBean>();
    nursingPatrolPresenter.getPatrolDict();
    nursingPatrolPresenter.getPatients(this, position);
    nursingPatrolPresenter.loadOrders(UserInfo.getUserName(), patientBean.getPatId(), "1", "2", "1",
        "8", null);
  }

  @Override
  protected void resetLayout() {

  }

  @Override
  public void setPatientInfo(SyncPatientBean patient) {
    patientBean = patient;
    tv_pat_name.setText(patient.getName() + " " + patient.getPatCode() + " " + patient.getSex());
  }

  @Override
  public void setOrderContext(List<OrderListBean> orderContext) {
    lv_order_list.setAdapter(new NursingPatrolAdapter(this, orderContext));
  }

  @Override
  public void showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
    }
    progressDialog.setMessage(getString(R.string.loadingdata));
    progressDialog.show();
  }

  @Override
  public void dismissProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  @Override
  public void setPatient(List<SyncPatientBean> list) {
    deptPatient.clear();
    deptPatient.addAll(list);
    if (isRefreshPatient) {
      isRefreshPatient = false;
      showPatients();
    }
  }

  @Override
  public void receiveData() {

  }

  @Override
  public void initialSrc() {
    gv_patrol_text = (GridView) findViewById(R.id.gv_patrol_text);
    lv_order_list = (ListView) findViewById(R.id.lv_order_list);
    tv_pat_name = (TextView) findViewById(R.id.tv_pat_name);
    tv_time_patrol = (TextView) findViewById(R.id.tv_time_patrol);
    tv_time_patrol.setText(DateTool.getCurrDateTime());
    btn_back = (Button) findViewById(R.id.btn_back);
    btn_back.setOnClickListener(this);
    btn_history = (Button) findViewById(R.id.btn_history);
    btn_history.setOnClickListener(this);
    btn_menu = (Button) findViewById(R.id.btn_menu);
    btn_menu.setOnClickListener(this);
  }

  @Override
  public void showPatients() {
    dialog = new MyPatientDialog(PatrolAct.this,
        new MyPatientDialog.MySnListener() {

          @Override
          public void myOnItemClick(View view, int position, long id) {
            nursingPatrolPresenter.setPatients(deptPatient.get(position));
            whichPatients = position;
            String patId = deptPatient.get(position).getPatId();
            nursingPatrolPresenter.loadOrders(UserInfo.getUserName(), patId, "1", "2", "1", "8",
                null);
            dialog.dismiss();
          }

          @Override
          public void onRefresh() {
            isRefreshPatient = true;
            nursingPatrolPresenter.getPatients(PatrolAct.this, 1000);// 1000标记用来刷新
            dialog.dismiss();
          }
        }, whichPatients, deptPatient);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.show();
  }

  @Override
  public void showToast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void setPatrolDict(final List<DictCommonBean> data) {
    final NursingPatrolDictAdapter adapter = new NursingPatrolDictAdapter(this, data);
    gv_patrol_text.setAdapter(adapter);
    gv_patrol_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (data.get(position).isSelect()) {
          data.get(position).setSelect(false);
        } else {
          data.get(position).setSelect(true);
        }
        itemCodes += data.get(position).getItemCode() + "||";
        adapter.notifyDataSetChanged();
      }
    });
  }

  public void filterPatrolText() {
    if (!StringTool.isEmpty(itemCodes)) {
      String[] ics = itemCodes.split("\\|\\|");
      String[] icsCopy = new String[ics.length];
      StringBuffer sb = new StringBuffer();
      System.arraycopy(ics, 0, icsCopy, 0, ics.length);
      for (int i = 0; i < ics.length; i++) {
        int num = 0;
        for (int j = 0; j < icsCopy.length; j++) {
          if (ics[i].equals(icsCopy[j])) {
            num++;
          }
        }
        if ((num & 1) != 0) {// 奇数
          sb.append(ics[i]);
          sb.append("||");
        }
      }
      if (!StringTool.isEmpty(sb.toString())) {
        // 去重
        String[] icsResult = sb.toString().split("\\|\\|");
        Set<String> icsArray = new HashSet<String>();
        for (int i = 0; i < icsResult.length; i++) {
          icsArray.add(icsResult[i]);
        }
        StringBuffer sb2 = new StringBuffer();
        Iterator<String> it = icsArray.iterator();
        while (it.hasNext()) {
          sb2.append(it.next());
          sb2.append("||");
        }
        nursingPatrolPresenter.saveNursingPatrol(patientBean.getPatId(), UserInfo.getWardCode(),
            sb2.toString(), UserInfo.getName());
      }
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_back:
        finish();
        break;
      case R.id.btn_menu:
        filterPatrolText();
        break;
      case R.id.btn_history:
        Intent intent = new Intent();
        intent.setClass(this, PatrolHistoryAct.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalConstant.KEY_PATIENT, patientBean);
        intent.putExtras(bundle);
        startActivity(intent);
        break;
    }
  }

  /**
   * 红外扫描获取的值
   *
   * @param result
   */
  public void DoCameraResult(String result) {
    nursingPatrolPresenter.commRec(UserInfo.getUserName(), result, DeviceTool.getDeviceId(this));

    List<BarcodeDict> barcodeDicts = new ArrayList<BarcodeDict>();
    barcodeDicts = LocalSave.getDataList(getApplicationContext(), GlobalConstant.KEY_BARCODE);
    String barcodeName = "";
    for (int i = 0; i < barcodeDicts.size(); i++) {
      if (patternCode(barcodeDicts.get(i).getRegularExp(), result)) {
        barcodeName = barcodeDicts.get(i).getBarcodeName();
        break;
      }
    }
    if (barcodeName.equals("PAT_BARCODE")) {
      for (int i = 0; i < deptPatient.size(); i++) {
        if (result.equals(deptPatient.get(i).getPatBarcode())) {
          nursingPatrolPresenter.setPatients(deptPatient.get(i));
          // 扫描时加载执行中医嘱
          // nursingPatrolPresenter.loadOrders(UserInfo.getUserName(),
          // deptPatient.get(i).getPatId(), "1", "2", "1", "8", null);
        }
      }
    }
  }
}
