package com.gentlehealthcare.mobilecare.activity.patient.trans;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.listener.ScanWanDai;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.AuthenticationBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zyy
 * @date 2015-9-18下午1:27:29
 * @category 输血界面身份扫描
 */
@SuppressLint("ValidFragment")
public class TrasIdentifyScanFra extends BaseFragment implements OnClickListener {
    private static final String TAG = "TrasIdentifyScanFra";
    /**
     * 待执行按钮
     */
    RadioButton rb_wait;
    /**
     * 执行中按钮
     */
    RadioButton rb_excuteing;
    /**
     * 扫描按钮
     */
    private Button btn_right_scan;
    /**
     * 同步病人数据
     */
    private SyncPatientBean patient = null;
    /**
     * 扫描失败提示框
     */
    private AlertDialogOneBtn alertDialog = null;
    /**
     * 扫描身份输入框
     */
    private Dialog currentDialog = null;

    /**
     * 主页按钮
     */
    private ImageButton imbtn_home;
    /**
     * 返回按钮
     */
    private ImageButton imbtn_back;
    /**
     * 病人姓名
     */
    private TextView tv_tras_name;
    /**
     * 病人床号
     */
    private TextView tv_tras_bed;
    /**
     * 腕带扫描监听器
     */
    ScanWanDai scanWanDai = null;

    private String eventId = "";

    private ProgressDialog progressDialog = null;

    private TextView tv_prompt_window;

    public TrasIdentifyScanFra(SyncPatientBean patient) {
        super();
        this.patient = patient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 标识当前Fra
        GlobalConstant.mCurrentTrasFra = "s";
        LoadKuaiZhao();
        // 监听腕带扫描
        WanDaiListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_tras_scan, null);
        progressDialog = new ProgressDialog(getActivity());

        initView(view);

        if (patient.getBedLabel() != null) {
            tv_tras_bed.setText(getResources().getString(R.string.bed_label) + patient.getBedLabel());
        } else {
            tv_tras_bed.setText(getResources().getString(R.string.bed_label) + Html.fromHtml
                    ("<small>未分配</small>"));
        }
        tv_tras_name.setText("姓名" + patient.getName());
        return view;
    }

    private void initView(View view) {
        rb_wait = (RadioButton) view.findViewById(R.id.rb_wait);
        rb_wait.setText("待执行" + GlobalConstant.performTask);
        rb_excuteing = (RadioButton) view.findViewById(R.id.rb_excuteing);
        rb_excuteing.setText("执行中" + GlobalConstant.executingTask);
        tv_prompt_window = (TextView) view.findViewById(R.id.tv_prompt_window);
        tv_prompt_window.setOnClickListener(this);
        tv_tras_bed = (TextView) view.findViewById(R.id.tv_tras_bed);
        tv_tras_name = (TextView) view.findViewById(R.id.tv_tras_name);

        imbtn_home = (ImageButton) view.findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) view.findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
//        btn_right_scan = (Button) getActivity().findViewById(   R.id.btn_right_scan);
//        btn_right_scan.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                ShowToast("请使用红外扫描核对患者");
//            }
//        });
//        btn_right_scan.setOnLongClickListener(new OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                // 病人身份扫描 弹框 可手动输入Code
//                final EditText editText = new EditText(getActivity());
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
//                editText.setLayoutParams(lp);
//                editText.setText(patient.getPatCode());
//                editText.setHint("手动输入病人腕带编号");
//                currentDialog = new AlertDialog.Builder(getActivity()).setTitle("提示").setCancelable(false)
//                        .setView(editText).setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                if (editText.getText().toString().equals("")) {
//                                    ShowToast("请输入腕带编号");
//                                } else if (editText.getText().toString().trim().equals(patient.getPatCode())) {
//                                    scanWanDai.stop();
//                                    checkPatient(1);
//                                } else {
//                                    checkPatient(-1);
//                                    ShowToast("患者身份核对失败");
//                                }
//                            }
//                        }).setPositiveButton("取消", null).create();
//                currentDialog.show();
//                return false;
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        } else if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_fra_tras_scan);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 加载快照
     */
    private void LoadKuaiZhao() {
        CCLog.i(TAG, "快照请求>>>" + UrlConstant.LoadKuaiZhao() + patient.getPatId());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.LoadKuaiZhao() + patient.getPatId(), new
                RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        CCLog.i(TAG, "快照数据获取失败");
                        if (LinstenNetState.isLinkState(getActivity())) {
                            Toast.makeText(getActivity(), getString(R.string.unstable), Toast.LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        CCLog.i(TAG, "快照数据获取成功");
                        JSONObject object = null;
                        Boolean result = false;
                        try {
                            object = new JSONObject(arg0.result);
                            result = object.getBoolean("result");

                            if (result) {
                                CCLog.i(TAG, object.optString("executingTask"));
                                GlobalConstant.executingTask = object.optString("executingTask");
                                GlobalConstant.performTask = object.optString("performTask");
                                rb_wait.setText("待执行" + GlobalConstant.performTask);
                                rb_excuteing.setText("执行中" + GlobalConstant.executingTask);
                            }
                        } catch (JSONException e) {
                            CCLog.e(TAG, "快照数据加载异常》》" + e);
                            if (LinstenNetState.isLinkState(getActivity())) {
                                Toast.makeText(getActivity(), getString(R.string.unstable), Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                toErrorAct();
                            }
                        }
                    }
                });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 扫描处理方法
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        //ShowToast("患者身份已核对" + result);
        // 此处目前是病人的id
        //DoIdentityConfirm(result);
    }

    /**
     * 扫描身份确认
     *
     * @param patcodeString 已过期 when:2015年1月12日  why:功能已在监听器中实现
     */
    private void DoIdentityConfirm(String patcodeString) {

        if (patcodeString.equals(patient.getPatCode())) {
            ShowToast("患者身份已核对");
            // 到血品界面
            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, new TransfusionFra(patient))
                    .commit();
        } else {
            alertDialog = new AlertDialogOneBtn(getActivity());

            alertDialog.setTitle("提示");

            alertDialog.setMessage("患者身份核对错误");

            alertDialog.setButton("再次核对");

            alertDialog.setButtonListener(false, new OnClickListener() {

                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();

                }
            });
        }

    }

    // 监听器响应事件
    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.imbtn_back:
                getActivity().finish();
                break;
            case R.id.imbtn_home:
                getActivity().startActivity(new Intent(getActivity(), HomeAct.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
                break;
            case R.id.tv_prompt_window:
                final EditText editText = new EditText(getActivity());
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                editText.setLayoutParams(lp);
                editText.setText(patient.getPatCode());
                editText.setHint("手动输入病人腕带编号");
                currentDialog = new AlertDialog.Builder(getActivity()).setTitle("提示").setCancelable(false)
                        .setView(editText).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (editText.getText().toString().equals("")) {
                                    ShowToast("请输入腕带编号");
                                } else if (editText.getText().toString().trim().equals(patient.getPatCode())) {
                                    scanWanDai.stop();
                                    checkPatient(1);
                                } else {
                                    checkPatient(-1);
                                    ShowToast("患者身份核对失败");
                                }
                            }
                        }).setPositiveButton("取消", null).create();
                currentDialog.show();
                break;
            default:
                break;
        }
    }


    /**
     * 监听扫描腕带
     */
    private void WanDaiListener() {
        scanWanDai = new ScanWanDai();
        scanWanDai.run();
        scanWanDai.setOnTestListening(new ScanWanDai.OnTestListening() {
            @Override
            public void TestListening(int i) {
                if (i == 2) {
                    if (GlobalConstant.wd.equals(patient.getPatCode())) {
                        GlobalConstant.wd = null;
                        scanWanDai.stop();
                        CCLog.e(">>>>scanWanDai.stop();");
                        checkPatient(1);
                    } else {
                        checkPatient(-1);
                        alertDialog = new AlertDialogOneBtn(TrasIdentifyScanFra.this.getActivity());
                        alertDialog.setTitle("提示");
                        alertDialog.setMessage("患者身份核对错误");
                        alertDialog.setButton("再次核对");
                        alertDialog.setButtonListener(false, new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                scanWanDai = new ScanWanDai();
                                scanWanDai.run();
                                alertDialog.dismiss();
                            }
                        });
                    }
                } else {
                    ShowToast("长按扫描按钮手动核对");
                }
            }
        });
    }

    public void checkPatient(int isCheckResult) {
        progressDialog.setMessage("正在核对身份,请稍后..");
        progressDialog.show();
        String url = UrlConstant.GetCheckAuthentication(0) + "?username=" + UserInfo.getUserName() + "&patId=" +
                patient.getPatId() + "&patCode=" + patient.getPatCode() + "&templateId=AA-5" +
                "&eventAttr=" + isCheckResult;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                progressDialog.dismiss();
                AuthenticationBean authenticationBean = new Gson().fromJson(result, AuthenticationBean.class);
                if (authenticationBean.isResult()) {
                    eventId = authenticationBean.getEventId();
                    TransfusionFra transfusionFra = new TransfusionFra(patient);
                    // 到血品界面
                    Bundle bundle = new Bundle();
                    bundle.putString("eventId", eventId);
                    transfusionFra.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fl_container, transfusionFra).commit();
                } else {
                    ShowToast(authenticationBean.getMsg());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressDialog.dismiss();

                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }
        });
    }
}
