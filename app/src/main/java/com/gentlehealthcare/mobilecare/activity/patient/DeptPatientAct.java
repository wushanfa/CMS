package com.gentlehealthcare.mobilecare.activity.patient;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.activity.orders.DocOrdersActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.db.dao.MedicineInfoDao;
import com.gentlehealthcare.mobilecare.db.dao.PatientMattersDao;
import com.gentlehealthcare.mobilecare.db.dao.VisitsAlertDao;
import com.gentlehealthcare.mobilecare.dbnew.DBManager;
import com.gentlehealthcare.mobilecare.dbnew.dao.PatientDao;
import com.gentlehealthcare.mobilecare.dbnew.impl.PatientDaoImpl;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.PatientListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 科室病人界面
 * Created by ouyang on 15/5/19.
 */
public class DeptPatientAct extends ABToolBarActivity {
    private List<SyncPatientBean> patients;
    private PatientListAdapter adapter = null;
    private int type = 0;//0点击跳入对应的病人工作界面 1点击跳入对应的病人医嘱界面
    public PullToRefreshListView pullToRefreshListView = null;
    private PatientMattersDao patientMattersDao = null;
    private VisitsAlertDao visitsAlertDao = null;
    private MedicineInfoDao medicineInfoDao = null;
    private DBManager dbManager;
    private SQLiteDatabase sqLiteDatabase;
    private PatientDao patientDao = new PatientDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptpatient);
       // initDatabase();
        type = getIntent().getIntExtra("type", 0);
        patientMattersDao = PatientMattersDao.newInstance(this);
        visitsAlertDao = VisitsAlertDao.newInstance(this);
        medicineInfoDao = MedicineInfoDao.newInstance(this);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_patients);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新 同步部门病人数据
                syncDeptPatientTable();
            }
        });
        ListView lvPatients = pullToRefreshListView.getRefreshableView();
        lvPatients.setOnItemClickListener(mItemClickListener);
        patients = new ArrayList<SyncPatientBean>();
        patients.addAll(UserInfo.getDeptPatient());
       lvPatients.setAdapter(adapter = new PatientListAdapter(DeptPatientAct.this, patients));
        findViewById(R.id.btn_back_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //配置工具栏
        setToolBarDrawable(new int[]{R.drawable.act_home_deptpatbtn, R.drawable.act_home_mypatbtn, R.drawable
                .act_home_homebtn, R.drawable.act_home_workbtn});
        setRightBtnDrawable(R.drawable.act_home_chaxunbtn);
        setAbDoToolBar(new ABDoToolBar() {
            @Override
            public void onCheckedChanged(int i) {
                switch (i) {
                    case 0:
                        break;
                    case 1:
//                        Intent intent = new Intent(DeptPatientAct.this, HomeAct.class);
//                        intent.putExtra("toMyPatient", true);
//                        startActivity(intent);
                        break;
                    case 2:
                        startActivity(new Intent(DeptPatientAct.this, HomeAct.class));
                        break;
                    case 3:
                      //  startActivity(new Intent(DeptPatientAct.this, WorkAct.class));
                        break;
                    default:
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
//        if(!getLocalData()){
//            //初始加载病人数据
//            syncDeptPatientTable();
//        }
        //初始加载病人数据
        syncDeptPatientTable();
    }

    @Override
    public void onResume() {
        super.onResume();
        setValid(false);
        setCheck(0);
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_deptment);
        SupportDisplay.resetAllChildViewParam(root);
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            position -= 1;
//            if (type == 0) {
//                Intent intent = new Intent(DeptPatientAct.this, PatientAct.class);
//                intent.putExtra("patient", patients.get(position));
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            } else if (type == 1) {
//                Intent intent = new Intent(DeptPatientAct.this, DoctorOrdersAct.class);
//                intent.putExtra("patient", patients.get(position));
//                intent.putExtra("patId", patients.get(position).getPatId());
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
                Intent intent = new Intent(DeptPatientAct.this, DocOrdersActivity.class);
                intent.putExtra(GlobalConstant.KEY_POSITION, position-1);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    };

    /**
     * @param
     * @return void
     * @throws
     * @Title: syncDeptPatientTable
     * @Description: 同步部门病人
     */
    public void syncDeptPatientTable() {
        String str = GroupInfoSave.getInstance(DeptPatientAct.this).get();
        if (str == null || "".equals(str)) {
            pullToRefreshListView.onRefreshComplete();
            return;
        }
        SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
        syncDeptPatientBean.setWardCode(str);
        String url = UrlConstant.GetSyncDeptPatient() + syncDeptPatientBean.toString();
        CCLog.i("加载科室病人.>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                result.replaceAll("null", "\"\"");
                Gson gson = new Gson();
                Type type = new TypeToken<List<SyncPatientBean>>() {
                }.getType();
                List<SyncPatientBean> list = gson.fromJson(result, type);
                UserInfo.setDeptPatient(list);
             // patientDao.updatePatient(sqLiteDatabase,list); 4月25日发现项目中用了ormlite
                patients.clear();
                patients.addAll(UserInfo.getDeptPatient());
                adapter.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                pullToRefreshListView.onRefreshComplete();
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(DeptPatientAct.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(DeptPatientAct.this, ErrorAct.class);
        startActivity(intent);
    }

    private void initDatabase() {
        dbManager = new DBManager(this);
        dbManager.openDatabase();
        sqLiteDatabase = dbManager.getDatabase();
    }

    private boolean getLocalData() {
        boolean flag = false;

       // List<SyncPatientBean> syncPatientBeans = patientDao.queryPatients(sqLiteDatabase);
        patients.clear();
        patients= patientDao.queryPatients(sqLiteDatabase);
        if (!patients.isEmpty()) {
            adapter.notifyDataSetChanged();
            pullToRefreshListView.onRefreshComplete();
            flag = true;
        }
        return flag;
    }

}
