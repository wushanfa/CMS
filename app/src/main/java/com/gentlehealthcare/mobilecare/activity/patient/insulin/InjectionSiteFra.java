package com.gentlehealthcare.mobilecare.activity.patient.insulin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.LoadLastGlucoseValCheckValBean;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 胰岛素注射部位
 * Created by ouyang on 2015/6/9.
 */
@SuppressLint("ValidFragment")
public class InjectionSiteFra extends BaseFragment {
    private String siteCode;
    private String siteNo;
    private EditText et_injectionsite;
    private TextView et_planStartTime, et_eventStartTime, et_glucoseVal, et_bloodvaluetype;
    private ProgressDialog progressDialog = null;
    private String patId = "";
    private LinearLayout ll_leastBloodTest;
    private OrderItemBean orderItemBean;

    public InjectionSiteFra(String siteCode, String siteNo, String patId, OrderItemBean orderItemBean) {
        this.siteNo = siteNo;
        this.siteCode = getSiteCode(siteCode);
        this.patId = patId;
        this.orderItemBean = orderItemBean;
    }

    private String getSiteCode(String siteCode) {
        if (siteCode.equals("A"))
            return "左上臂";
        else if (siteCode.equals("B"))
            return "右上臂";
        else if (siteCode.equals("C"))
            return "左上腹";
        else if (siteCode.equals("D"))
            return "右上腹";
        else if (siteCode.equals("E"))
            return "左下腹";
        else if (siteCode.equals("F"))
            return "右下腹";
        else if (siteCode.equals("G"))
            return "左腿";
        else
            return "右腿";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_injectionsite_main, null);
        et_injectionsite = (EditText) rootView.findViewById(R.id.et_injectionsite);
        et_planStartTime = (TextView) rootView.findViewById(R.id.et_planStartTime);
        et_eventStartTime = (TextView) rootView.findViewById(R.id.et_eventStartTime);
        et_glucoseVal = (TextView) rootView.findViewById(R.id.et_glucoseVal);
        et_bloodvaluetype = (TextView) rootView.findViewById(R.id.et_bloodvaluetype);
        ll_leastBloodTest = (LinearLayout) rootView.findViewById(R.id.ll_leastBloodTest);
        et_injectionsite.setText(siteCode + " " + siteNo);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载数据,请稍后...");
        ll_leastBloodTest.setVisibility(View.GONE);
        DoLoadLastGlucoseValCheckValReq();
        return rootView;
    }

    public String getSite() {
        return et_injectionsite.getText().toString();
    }

    /**
     * 获取最近一次血糖检测值
     */
    private void DoLoadLastGlucoseValCheckValReq() {
        progressDialog.show();
        LoadLastGlucoseValCheckValBean loadLastGlucoseValCheckValBean = new LoadLastGlucoseValCheckValBean();
        loadLastGlucoseValCheckValBean.setPatId(patId);
        loadLastGlucoseValCheckValBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        HttpUtils http = new HttpUtils();
        String url = UrlConstant.LoadLastGlucoseValCheckVal() + loadLastGlucoseValCheckValBean.toString();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                progressDialog.dismiss();
                LoadLastGlucoseValCheckValBean info = new Gson().fromJson(result, LoadLastGlucoseValCheckValBean
                        .class);
                if (info.getResult()) {
                    if (info.getEventStartTime() == null || "".equals(info.getEventStartTime())) {
                        ll_leastBloodTest.setVisibility(View.GONE);
                    } else {
                        ll_leastBloodTest.setVisibility(View.VISIBLE);
                        et_planStartTime.setText(info.getPlanStartTime());
                        et_eventStartTime.setText(info.getEventStartTime());
                        et_glucoseVal.setText(info.getGlucoseVal());
                        et_bloodvaluetype.setText(info.getItemName());
                    }
                } else {
                    ShowToast(info.getMsg());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                }else{
                    toErrorAct();
                }
            }
        });
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view
                .findViewById(R.id.root_fra_injection);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
