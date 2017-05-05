package com.gentlehealthcare.mobilecare.activity.patient.trans;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zyy
 * @类说名 输血15分钟后录入TPR界面
 * @date 2015-9-14下午2:47:57
 */
@SuppressLint("ValidFragment")
public class TransfusionFifteenFra extends BaseFragment implements
        View.OnClickListener {
    private final static String TAG = "TransfusionFifteenFra";
    /**
     * 体温&脉搏&呼吸
     */
    private EditText edt_tw;
    private EditText edt_mb;
    private EditText edt_hx;
    /**
     * 同步病人数据
     */
    private SyncPatientBean patient = null;
    /**
     * 提交&结束按钮
     */
    private Button bt_submit;
    /**
     * 返回按钮
     */
    private ImageButton bt_back;
    /**
     * 头部信息
     */
    private TextView tvTitle;
    /**
     * 进度条
     */
    private ProgressDialog progressDialog = null;

    private String planOrderNo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_tras_after_tpr, null);
        intial(view);
        tvTitle.setText(patient.getBedLabel() + "床输血15分钟后体征");
        return view;
    }

    private void intial(View view) {
        edt_tw = (EditText) view.findViewById(R.id.tv_item_fyh_type_);
        edt_mb = (EditText) view.findViewById(R.id.tv_item_fyh_type_2);
        edt_hx = (EditText) view.findViewById(R.id.tv_item_fyh_type_3);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        bt_submit = (Button) view.findViewById(R.id.btn_submit);
        bt_submit.setOnClickListener(this);
        bt_back = (ImageButton) view.findViewById(R.id.imbtn_back);
        bt_back.setOnClickListener(this);
    }

    /**
     * 获取数据源
     */
    private void getValue() {
        patient = (SyncPatientBean) getArguments().getSerializable(GlobalConstant.KEY_PATIENT);
        planOrderNo = getArguments().getString(GlobalConstant.KEY_PLANORDERNO);
    }

    /**
     * 保存TPR信息 需要传递的参数：
     * 1.username//用户名
     * 2.patId//病人id
     * 3.planOrderNo//医嘱执行任务id
     * 4.temperature//体温
     * 5.pulse//脉搏
     * 6.respire//呼吸
     */
    private void saveTPRDate(String username, String patId, String planOrderNo, String temperature, String
            pulse, String respire) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.saving));
        progressDialog.show();
        HttpUtils http = new HttpUtils();
        String url = UrlConstant.saveTPRDate() + "?username=" + username + "&patId=" + patId + "&planOrderNo="
                + planOrderNo + "&temperature=" + temperature + "&pulse="
                + pulse + "&respire=" + respire;
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            JSONObject jsonobject = null;
            boolean result = false;

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    jsonobject = new JSONObject(responseInfo.result);
                    result = jsonobject.getBoolean("result");
                    if (result) {
                        progressDialog.dismiss();
                        //返回列表，并将对应血品录入状态改为“已录入”
                        ShowToast("记录成功");
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
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

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.imbtn_back:
                TransfusionedFra transfusionedFra = new TransfusionedFra();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                transfusionedFra.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_container, transfusionedFra).commit();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(edt_tw.getText())) {
                    ShowToast(getResources().getString(R.string.pleaseinputtemperature));
                    return;
                }
                if (TextUtils.isEmpty(edt_mb.getText())) {
                    ShowToast(getResources().getString(R.string.pleaseinputpluse));
                    return;
                }
                if (TextUtils.isEmpty(edt_hx.getText())) {
                    ShowToast(getResources().getString(R.string.pleaseinputrespire));
                    return;
                }

                saveTPRDate(UserInfo.getUserName(), patient.getPatId(), planOrderNo, edt_tw.getText().toString()
                        , edt_mb.getText().toString(), edt_hx.getText().toString());
                break;

            default:
                break;
        }

    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_transafter);
        SupportDisplay.resetAllChildViewParam(root);
    }

}
