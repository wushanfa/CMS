package com.gentlehealthcare.mobilecare.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.patient.PatientMedicineDao;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.db.dao.MedicineInfoDao;
import com.gentlehealthcare.mobilecare.db.dao.PatientDao;
import com.gentlehealthcare.mobilecare.db.dao.PatientMattersDao;
import com.gentlehealthcare.mobilecare.db.dao.VisitsAlertDao;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.WardInfoItem;
import com.gentlehealthcare.mobilecare.net.impl.SyncDeptPatientRequest;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DeviceTool;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.tool.UrlTool;
import com.gentlehealthcare.mobilecare.view.adapter.GroupAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择分组界面
 */
public class ChooseGroupAct extends BaseActivity {
    private static final String TAG = "分组界面";
    private ListView lv_group;
    private List<String> groupNames;
    private ProgressDialog progressDialog;
    private List<WardInfoItem> wardInfoItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosegroup_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.gettingsectiondatalater));
        groupNames = new ArrayList<>();
        PatientDao.newInstance(this);
        PatientMattersDao.newInstance(this);
        PatientMedicineDao.newInstance(this);
        MedicineInfoDao.newInstance(this);
        VisitsAlertDao.newInstance(this);
        wardInfoItemList = UserInfo.getWardList();
        if (wardInfoItemList != null && wardInfoItemList.size() > 0) {
            for (WardInfoItem infoItem : wardInfoItemList) {
                groupNames.add(infoItem.getWardName());
            }
        } else {
            finish();
        }
        lv_group = (ListView) findViewById(R.id.lv_choosegroup);
        lv_group.setAdapter(new GroupAdapter(ChooseGroupAct.this, groupNames));
        lv_group.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog.show();
                syncDeptPatientTable(position);
            }
        });
    }

    private void changeCurrentDept(String userName,String wardCode){
        String url = UrlConstant.changeCurrentDept()+UrlTool.transWord(userName)+"&wardCode="+UrlTool.transWord(wardCode)+"&deviceId="+UrlTool.transWord(DeviceTool.getDeviceId(this));
        CCLog.i("更换科室>>>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonbject=new JSONObject(responseInfo.result);
                    boolean resuslt=jsonbject.getBoolean("result");
                   if (resuslt) {
                       ShowToast("当前科室已切换");
                   }else{
                        ShowToast("科室切换失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ShowToast("科室切换异常");
            }
        });
    }

    @Override
    protected void resetLayout() {
        LinearLayout root = (LinearLayout) findViewById(R.id.root_choosegroup);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            // 监控/拦截菜单键
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            // 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 同步科室病人数据
     *
     * @param position 当前选中的分组POSITION
     */
    public void syncDeptPatientTable(final int position) {
        UserInfo.setWardCode(wardInfoItemList.get(position).getWardCode());
        SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
        syncDeptPatientBean.setWardCode(wardInfoItemList.get(position).getWardCode());
        RequestManager.connection(new SyncDeptPatientRequest(ChooseGroupAct.this, new
                IRespose<SyncDeptPatientBean>() {

                    @Override
                    public void doResult(SyncDeptPatientBean t, int id) {
                    }

                    @Override
                    public void doResult(String result) throws KeyObsoleteException {
                        result = result.replaceAll("null", "\"\"");
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<SyncPatientBean>>() {
                        }.getType();
                        List<SyncPatientBean> list = gson.fromJson(result, type);

                        if (list != null && list.size() > 0) {
                            UserInfo.setDeptPatient(list);
                        }
                        changeCurrentDept(UserInfo.getUserName(),UserInfo.getWardCode());//跟新后修改数据表
                        GroupInfoSave.getInstance(ChooseGroupAct.this).save(groupNames.get(position));
                        finish();
                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        CCLog.i(TAG, "网络数据获取失败");
                        progressDialog.dismiss();
                        finish();
                        if (networkState) {
                            Toast.makeText(ChooseGroupAct.this, "获取护士单元数据失败", Toast.LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                }, 0, true, syncDeptPatientBean));
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(ChooseGroupAct.this, ErrorAct.class);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        progressDialog.dismiss();
    }
}
