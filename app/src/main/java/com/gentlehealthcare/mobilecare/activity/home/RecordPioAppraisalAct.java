package com.gentlehealthcare.mobilecare.activity.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.LoadPioRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.PioBean;
import com.gentlehealthcare.mobilecare.net.bean.PioItemInfo;
import com.gentlehealthcare.mobilecare.net.bean.RecordAppraisalBean;
import com.gentlehealthcare.mobilecare.net.bean.RecordPioBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.LoadPioDictRequest;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.AddPioApprisalAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * PIO评价界面
 *
 * @author zhiwei
 */
public class RecordPioAppraisalAct extends BaseActivity implements OnClickListener, OnItemLongClickListener {

    private static final String TAG = "RecordPioAppraisalActivity";

    private TextView tv_time, tv_problem, tv_cause, tv_target, tv_measures,
            tv_patient;
    private Button huoqu;
    private ListView appraisalList;

    private List<PioItemInfo> appraisaList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> appraisaChoosenList = new ArrayList<PioItemInfo>();
    private ProgressDialog progressDialog;
    private LoadPioRecordBean loadPioRecordBean;
    private AddPioApprisalAdapter adapter;
    private String time;
    private String yuanyin;
    private String wenti;
    private String mubiao;
    private String cuoshi;
    private String patientName;
    private SyncPatientBean patient;
    @ViewInject(R.id.tv_head)
    private TextView tv_head;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_lable;
    @ViewInject(R.id.tv_parent)
    private TextView tv_parent;
    @ViewInject(R.id.btn_back)
    private Button btn_back;
    @ViewInject(R.id.pio_nursing_appraisal)
    private Button pio_nursing_appraisal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pio_recordappraisal);
        ViewUtils.inject(this);
        HidnGestWindow(true);
        initView();
        progressDialog = new ProgressDialog(this);
        loadPioRecordBean = (LoadPioRecordBean) getIntent().getSerializableExtra("pio");
        patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");
        assignment();

        tv_head.setText("护理文记录");
        tv_bed_lable.setText(patient.getBedLabel() + "床");
        tv_parent.setText(patient.getName());

        adapter = new AddPioApprisalAdapter(RecordPioAppraisalAct.this, appraisaChoosenList);
        appraisalList.setAdapter(adapter);

        initBinding();
    }

    @Override
    protected void resetLayout() {
        LinearLayout root = (LinearLayout) findViewById(R.id.root_piorecordappraisal);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void initBinding() {
        appraisalList.setOnItemLongClickListener(this);
        pio_nursing_appraisal.setOnClickListener(this);
        huoqu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void initView() {
        appraisalList = (ListView) findViewById(R.id.elv_pio_appraisal);
        huoqu = (Button) findViewById(R.id.btn_pingjia);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_problem = (TextView) findViewById(R.id.tv_problem);
        tv_target = (TextView) findViewById(R.id.tv_target);
        tv_cause = (TextView) findViewById(R.id.tv_cause);
        tv_measures = (TextView) findViewById(R.id.tv_measures);
        tv_patient = (TextView) findViewById(R.id.tv_patient);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int myPosition = position;
        new AlertDialog.Builder(RecordPioAppraisalAct.this, AlertDialog.THEME_HOLO_LIGHT).setMessage
                ("确定删除吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                appraisaChoosenList.remove(myPosition);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("取消", null).show();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                new AlertDialog.Builder(RecordPioAppraisalAct.this, AlertDialog.THEME_HOLO_LIGHT).setMessage
                        ("是否保存评价信息").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (appraisaChoosenList.isEmpty()) {
                            Toast.makeText(RecordPioAppraisalAct.this, "请输入评价，再保存", Toast.LENGTH_SHORT).show();
                        } else {
                            DoRecordAppraisalReq();
                        }
                    }
                }).setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }
                ).show();
                break;
            case R.id.pio_nursing_appraisal:
                DoRecordPioAppraisalReq(new PioBean("", 4));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(RecordPioAppraisalAct.this, AlertDialog.THEME_HOLO_LIGHT).setMessage
                    ("是否保存评价信息").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (appraisaChoosenList.isEmpty()) {
                        Toast.makeText(RecordPioAppraisalAct.this, "请输入评价，再保存", Toast.LENGTH_SHORT).show();
                    } else {
                        DoRecordAppraisalReq();
                    }
                }
            }).setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }
            ).show();
        }
        return false;
    }

    private String getItemNameToString(List<PioItemInfo> list) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemName() == null) {
                sb.append("没有相关数据");
            } else {
                sb.append("(" + (i + 1) + ")");
                sb.append(".");
                sb.append(list.get(i).getItemName());
            }
            if (i != list.size() - 1) {
                sb.append("\n");
            }

        }
        return sb.toString();
    }

    public void assignment() {
        wenti = getProblemToString(loadPioRecordBean.getProblemName());
        yuanyin = getItemNameToString(loadPioRecordBean.getCauses());
        mubiao = getItemNameToString(loadPioRecordBean.getTarget());
        cuoshi = getItemNameToString(loadPioRecordBean.getMeasure());
        time = loadPioRecordBean.getLogTim1();
        patientName = patient.getName();

        tv_time.setText("时间:" + time);
        tv_problem.setText("问题:" + "\n" + wenti);
        tv_cause.setText("致因:" + "\n" + yuanyin);
        tv_measures.setText("措施:" + "\n" + cuoshi);
        tv_target.setText("目标:" + "\n" + mubiao);
        tv_patient.setText("病人：" + "\n" + patientName);
    }

    private String getProblemToString(String info) {
        StringBuffer sb = new StringBuffer();
        if (info == null) {
            sb.append("没有相关数据");
        } else {
            sb.append("(" + 1 + ")");
            sb.append(".");
            sb.append(info);
        }

        return sb.toString();
    }

    /**
     * 提交评价请求
     */
    private void DoRecordAppraisalReq() {
        progressDialog.setMessage("正在提交数据,请稍后...");
        progressDialog.show();
        RecordAppraisalBean recordAppraisalBean = new RecordAppraisalBean();
        recordAppraisalBean.setAppraisal(appraisaChoosenList.get(0).getItemName());
        recordAppraisalBean.setAppraisalCode(appraisaChoosenList.get(0).getItemCode());
        recordAppraisalBean.setPatId(loadPioRecordBean.getPatId());
        recordAppraisalBean.setPioId(loadPioRecordBean.getPioId());
        CCLog.i(TAG, "DoRecordAppraisalReq>" + UrlConstant.RecordAppraisal() + recordAppraisalBean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.RecordAppraisal() + recordAppraisalBean.toString(),
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        try {
                            Gson gson = new Gson();
                            RecordPioBean recordPioBean = gson.fromJson(result, RecordPioBean.class);
                            if (recordPioBean.getResult().equals("success")) {
                                Toast.makeText(RecordPioAppraisalAct.this, "提交成功.", Toast.LENGTH_SHORT).show();
                                appraisaChoosenList.clear();
                                adapter.notifyDataSetChanged();
                                finish();
                            } else {
                                Toast.makeText(RecordPioAppraisalAct.this, "提交失败.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(RecordPioAppraisalAct.this, getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }else{
                            toErrorAct();
                        }
                    }
                });
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(RecordPioAppraisalAct.this, ErrorAct.class);
        startActivity(intent);
    }

    /**
     * PIO 评价 请求
     *
     * @param pioBean
     */
    private void DoRecordPioAppraisalReq(final PioBean pioBean) {
        progressDialog.setMessage("获取数据中,请稍后..");
        progressDialog.show();
        RequestManager.connection(new LoadPioDictRequest(
                RecordPioAppraisalAct.this, new IRespose<PioBean>() {
            @Override
            public void doResult(PioBean pioBean, int id) {

            }

            @Override
            public void doResult(String result)
                    throws KeyObsoleteException {
                progressDialog.dismiss();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<PioItemInfo>>() {
                    }.getType();
                    List<PioItemInfo> list = gson.fromJson(result, type);
                    appraisaChoosenList.clear();
                    appraisaList.clear();
                    if (list != null && list.size() > 0) {
                        appraisaList.addAll(list);
                        ShowSighChooseAlert(appraisaList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState) {
                    Toast.makeText(RecordPioAppraisalAct.this, "操作异常.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecordPioAppraisalAct.this, getString(R.string.netstate_content), Toast
                            .LENGTH_SHORT).show();

                }
            }
        }, 0, true, pioBean));
    }

    /**
     * 显示单选列表对话框
     *
     * @param list
     */
    private void ShowSighChooseAlert(final List<PioItemInfo> list) {
        String[] strs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strs[i] = list.get(i).getItemName();
        }
        AlertDialog dialog = new AlertDialog.Builder(RecordPioAppraisalAct.this, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("请选择评价内容")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        appraisaChoosenList.add(list.get(i));
                        adapter.notifyDataSetChanged();
                    }
                }).create();
        dialog.show();
        dialog.getWindow().setLayout(680, 800);
    }

}
