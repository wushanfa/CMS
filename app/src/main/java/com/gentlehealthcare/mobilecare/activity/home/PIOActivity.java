package com.gentlehealthcare.mobilecare.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.SyncDeptPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.PIOAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * PIO主界面 科室病人界面
 */
public class PIOActivity extends BaseActivity {
    private static final String TAG = "PIO列表";
    private ListView listview;// 科室病人列表界面
    private PIOAdapter adapter;
    private List<SyncPatientBean> patients;// 科室病人数据
    @ViewInject(R.id.tv_head)
    private TextView tv_head;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_label;
    @ViewInject(R.id.tv_parent)
    private TextView tv_parent;
    @ViewInject(R.id.btn_back)
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ViewUtils.inject(this);
        initView();
        HidnGestWindow(true);

        patients = new ArrayList<SyncPatientBean>();
        patients.addAll(UserInfo.getDeptPatient());
        syncDeptPatientTable();
        adapter = new PIOAdapter(this, patients);
        listview.setAdapter(adapter);
        tv_head.setText("护理记录");
        tv_bed_label.setVisibility(View.GONE);
        tv_parent.setVisibility(View.GONE);
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.lv_orders);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                Intent intent = new Intent(PIOActivity.this,
                        PioRecordsActivity.class);
                intent.putExtra("patient", patients.get(i));
                intent.putExtra("allpatients", (Serializable) patients);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        patients.clear();
        patients.addAll(UserInfo.getDeptPatient());
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void resetLayout() {
        LinearLayout root = (LinearLayout) findViewById(R.id.root_orders);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @OnClick(R.id.btn_back)
    public void setOnClick(View view) {
        finish();
    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        CCLog.i(TAG, TAG + "扫描的结果:" + result);
        boolean isGetResult = false;
        int flag = 0;
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatCode().equals(result)) {
                isGetResult = true;
                flag = i;
                break;
            }
        }

        if (isGetResult) {
            Intent intent = new Intent(PIOActivity.this, PioRecordsActivity.class);
            intent.putExtra("patient", patients.get(flag));
            intent.putExtra("allpatients", (Serializable) patients);
            startActivity(intent);
        } else {
            Toast.makeText(getApplication(), "没有找到改病人信息", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 同步部门病人
     */
    public void syncDeptPatientTable() {
        String str = GroupInfoSave.getInstance(PIOActivity.this).get();
        if (str == null || "".equals(str)) {
            return;
        }
        SyncDeptPatientBean syncDeptPatientBean = new SyncDeptPatientBean();
        syncDeptPatientBean.setWardCode(str);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetSyncDeptPatient() + syncDeptPatientBean.toString()
                , new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CCLog.i(TAG, "PIO列表数据获取成功");
                result.replaceAll("null", "\"\"");
                Gson gson = new Gson();
                Type type = new TypeToken<List<SyncPatientBean>>() {
                }.getType();
                List<SyncPatientBean> list = gson.fromJson(result, type);
                UserInfo.setDeptPatient(list);
                patients.clear();
                patients.addAll(UserInfo.getDeptPatient());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                CCLog.i(TAG, "PIO列表数据获取失败");
                if (LinstenNetState.isLinkState(PIOActivity.this)) {
                    Toast.makeText(PIOActivity.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(PIOActivity.this, ErrorAct.class);
        startActivity(intent);
    }
}
