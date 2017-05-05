package com.gentlehealthcare.mobilecare.activity.bloodsugar;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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

/**
 * @author Zyy
 * @date 2015-9-18下午1:27:29
 * @category 血糖测试身份扫描
 */
@SuppressLint("ValidFragment")
public class BloodSugarIdentifyScanFra extends BaseFragment implements
        OnClickListener {
    private static final String TAG = "BloodSugarIdentifyScanFra";
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

    public BloodSugarIdentifyScanFra(SyncPatientBean patient) {
        super();
        this.patient = patient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_bloodsugar_scan, null);
        // 初始化UI组件
        rb_wait = (RadioButton) view.findViewById(R.id.rb_wait);
        rb_wait.setText("待执行" + GlobalConstant.performTask);
        rb_excuteing = (RadioButton) view.findViewById(R.id.rb_excuteing);
        rb_excuteing.setText("执行中" + GlobalConstant.executingTask);
        tv_tras_bed = (TextView) view.findViewById(R.id.tv_tras_bed);
        if (patient.getBedLabel() != null) {
            tv_tras_bed.setText("床号：" + patient.getBedLabel());
        } else {
            tv_tras_bed.setText(getResources().getString(R.string.bed_label) + Html.fromHtml
                    ("<small>未分配</small>"));
        }

        tv_tras_name = (TextView) view.findViewById(R.id.tv_tras_name);
        tv_tras_name.setText("姓名：" + patient.getName());
        // 返回&主页按钮
        imbtn_home = (ImageButton) view.findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) view.findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        btn_right_scan = (Button) getActivity().findViewById(
                R.id.btn_right_scan);
        btn_right_scan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowToast("请使用红外扫描核对患者");
            }
        });

        btn_right_scan.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 病人身份扫描 弹框 可手动输入Code
                final EditText editText = new EditText(getActivity());
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 100);
                editText.setLayoutParams(lp);
                editText.setText(patient.getPatCode());
                editText.setHint("手动输入病人腕带编号");
                currentDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setCancelable(false)
                        .setView(editText)
                        .setNegativeButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface,
                                            int i) {
                                        if (editText.getText().toString()
                                                .equals("")) {
                                            ShowToast("请输入腕带编号");
                                        } else if (editText.getText()
                                                .toString().trim()
                                                .equals(patient.getPatCode())) {
                                            //到血糖医嘱列表界面
                                            getFragmentManager().beginTransaction()
                                                    .replace(R.id.fl_container, new BloodSugarListFra(patient))
                                                    .commit();
                                        } else {
                                            ShowToast("患者身份核对失败");
                                        }
                                    }
                                }).setPositiveButton("取消", null).create();
                currentDialog.show();
                return false;
            }
        });

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
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_bs_scan);
        SupportDisplay.resetAllChildViewParam(root);
    }


    /**
     * 扫描处理方法
     *
     * @param result
     */
    public void DoCameraResult(String result) {
        // 此处目前是病人的id
        DoIdentityConfirm(result);
    }

    /**
     * 扫描身份确认
     *
     * @param patcodeString
     */
    private void DoIdentityConfirm(String patcodeString) {

        if (patcodeString.equals(patient.getPatCode())) {
            //到血糖医嘱列表界面
            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, new BloodSugarListFra(patient))
                    .commit();
            ShowToast("患者身份已核对");

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
                getActivity().startActivity(
                        new Intent(getActivity(), HomeAct.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.in_or_out_static,
                        R.anim.slide_out_right);
                break;

            default:
                break;
        }
    }

}
