package com.gentlehealthcare.mobilecare.activity.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuBBean;
import com.gentlehealthcare.mobilecare.net.bean.SaveICUBBean;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUBBean;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUBDataBean;
import com.gentlehealthcare.mobilecare.net.bean.UpdateICUBBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.ICUCommonMethod;
import com.gentlehealthcare.mobilecare.tool.ICUDataMethod;
import com.gentlehealthcare.mobilecare.tool.ICUResourceSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NursingEvaluation extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "Nusring Evaluation";
    /**
     * editext
     */
    private EditText et_oral, et_catheter, et_decubitus, et_turnback, et_xt, et_yswh, et_gndl, et_zyct,
            et_qyzl, et_zswsgf, et_cttg34d, et_xtygwjcz, et_qnywc, et_smxfmwxy, et_jsqdlns, et_gdrgqdfql,
            et_custom, et_custom2, et_custom3;

    private RelativeLayout rl_custom1, rl_custom2, rl_custom3;
    private RelativeLayout rl_oral_layout, rl_catheter_layout, rl_decubitus_layout, rl_turnback_layout,
            rl_xt_layout,
            rl_yswh_layout, rl_gndl_layout, rl_zyct_layout, rl_qyzl_layout, rl_zswsgf_layout, rl_cttg34d_layout,
            rl_xtygwjcz_layout, rl_qnywc_layout, rl_smxfmwxy_layout, rl_jsqdlns_layout, rl_gdrgqdfql_layout;
    //custom
    private LinearLayout detail_custom_ll, thumbnail_custom_ll;
    private TextView detail_custom_tv, detail_custom_iv, tv_custom;
    //prevention
    private LinearLayout detail_prevention_ll, thumbnail_prevention_ll;
    private TextView detail_prevention_tv, detail_prevention_iv, tv_prevention;
    //treatment
    private LinearLayout detail_zhiliao_ll, thumbnail_zhiliao_ll;
    private TextView detail_zhiliao_tv, detail_zhiliao_iv, tv_zhiliao;
    //special
    private LinearLayout detail_teshu_ll, thumbnail_teshu_ll;
    private TextView detail_teshu_tv, detail_teshu_iv, tv_teshu;
    //conventional
    private LinearLayout detail_changgui_ll, thumbnail_changgui_ll;
    private TextView detail_changgui_tv, detail_changgui_iv, tv_changgui;
    // 时间
    private TextView page_time;
    private int arrive_year = 0;
    private int arrive_month = 0;
    private int arrive_day = 0;
    private int arrive_hour = 0;
    private int arrive_min = 0;
    private View dateView;
    private DatePicker datepick;
    private TimePicker timepick;
    private String myLongDate = new String();
    private Button save;
    /**
     * 接受从activity传过来的数据
     */
    private List<LoadIcuBBean> dictItem = new ArrayList<LoadIcuBBean>();

    private ProgressDialog progressDialog;
    /**
     * 查询的结果
     */
    private static List<SearchICUBBean> searchList = new ArrayList<SearchICUBBean>();

    private static int status = 1;

    private List<EditText> etList = new ArrayList<EditText>();

    private int rowNo = 1;

//    private String formNo = "";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_icu_hlpj, null);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在操作");

        Bundle dataI = getArguments();
        dictItem = (List<LoadIcuBBean>) dataI.getSerializable("evaluation");

        initView(view);

        addEditTextList();

        page_time.setText((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));

        searchICUB();

        return view;
    }

    private void initView(View v) {
        detail_custom_ll = (LinearLayout) v.findViewById(R.id.detail_custom_ll);
        detail_custom_ll.setOnClickListener(this);
        thumbnail_custom_ll = (LinearLayout) v.findViewById(R.id.thumbnail_custom_ll);
        detail_prevention_ll = (LinearLayout) v.findViewById(R.id.detail_prevention_ll);
        detail_prevention_ll.setOnClickListener(this);
        thumbnail_prevention_ll = (LinearLayout) v.findViewById(R.id.thumbnail_prevention_ll);
        detail_zhiliao_ll = (LinearLayout) v.findViewById(R.id.detail_zhiliao_ll);
        detail_zhiliao_ll.setOnClickListener(this);
        thumbnail_zhiliao_ll = (LinearLayout) v.findViewById(R.id.thumbnail_zhiliao_ll);
        detail_teshu_ll = (LinearLayout) v.findViewById(R.id.detail_teshu_ll);
        detail_teshu_ll.setOnClickListener(this);
        thumbnail_teshu_ll = (LinearLayout) v.findViewById(R.id.thumbnail_teshu_ll);
        detail_changgui_ll = (LinearLayout) v.findViewById(R.id.detail_changgui_ll);
        detail_changgui_ll.setOnClickListener(this);
        thumbnail_changgui_ll = (LinearLayout) v.findViewById(R.id.thumbnail_changgui_ll);
        detail_custom_tv = (TextView) v.findViewById(R.id.detail_custom_tv);
        detail_custom_iv = (TextView) v.findViewById(R.id.detail_custom_iv);
        detail_prevention_tv = (TextView) v.findViewById(R.id.detail_prevention_tv);
        detail_prevention_iv = (TextView) v.findViewById(R.id.detail_prevention_iv);
        detail_zhiliao_tv = (TextView) v.findViewById(R.id.detail_zhiliao_tv);
        detail_zhiliao_iv = (TextView) v.findViewById(R.id.detail_zhiliao_iv);
        detail_teshu_tv = (TextView) v.findViewById(R.id.detail_teshu_tv);
        detail_teshu_iv = (TextView) v.findViewById(R.id.detail_teshu_iv);
        detail_changgui_tv = (TextView) v.findViewById(R.id.detail_changgui_tv);
        detail_changgui_iv = (TextView) v.findViewById(R.id.detail_changgui_iv);
        page_time = (TextView) v.findViewById(R.id.page_time);
        page_time.setOnClickListener(this);
        save = (Button) v.findViewById(R.id.save);
        save.setOnClickListener(this);
        et_oral = (EditText) v.findViewById(R.id.et_oral);
        et_catheter = (EditText) v.findViewById(R.id.et_catheter);
        et_decubitus = (EditText) v.findViewById(R.id.et_decubitus);
        et_turnback = (EditText) v.findViewById(R.id.et_turnback);
        et_xt = (EditText) v.findViewById(R.id.et_xt);
        et_yswh = (EditText) v.findViewById(R.id.et_yswh);
        et_gndl = (EditText) v.findViewById(R.id.et_gndl);
        et_zyct = (EditText) v.findViewById(R.id.et_zyct);
        et_qyzl = (EditText) v.findViewById(R.id.et_qyzl);
        et_zswsgf = (EditText) v.findViewById(R.id.et_zswsgf);
        et_cttg34d = (EditText) v.findViewById(R.id.et_cttg34d);
        et_xtygwjcz = (EditText) v.findViewById(R.id.et_xtygwjcz);
        et_qnywc = (EditText) v.findViewById(R.id.et_qnywc);
        et_smxfmwxy = (EditText) v.findViewById(R.id.et_smxfmwxy);
        et_jsqdlns = (EditText) v.findViewById(R.id.et_jsqdlns);
        et_gdrgqdfql = (EditText) v.findViewById(R.id.et_gdrgqdfql);
        et_custom = (EditText) v.findViewById(R.id.et_custom);
        et_custom2 = (EditText) v.findViewById(R.id.et_custom2);
        et_custom3 = (EditText) v.findViewById(R.id.et_custom3);
        rl_custom1 = (RelativeLayout) v.findViewById(R.id.rl_custom1);
        rl_custom2 = (RelativeLayout) v.findViewById(R.id.rl_custom2);
        rl_custom3 = (RelativeLayout) v.findViewById(R.id.rl_custom3);
        rl_oral_layout = (RelativeLayout) v.findViewById(R.id.rl_oral_layout);
        rl_catheter_layout = (RelativeLayout) v.findViewById(R.id.rl_catheter_layout);
        rl_decubitus_layout = (RelativeLayout) v.findViewById(R.id.rl_decubitus_layout);
        rl_turnback_layout = (RelativeLayout) v.findViewById(R.id.rl_turnback_layout);
        rl_xt_layout = (RelativeLayout) v.findViewById(R.id.rl_xt_layout);
        rl_yswh_layout = (RelativeLayout) v.findViewById(R.id.rl_yswh_layout);
        rl_gndl_layout = (RelativeLayout) v.findViewById(R.id.rl_gndl_layout);
        rl_zyct_layout = (RelativeLayout) v.findViewById(R.id.rl_zyct_layout);
        rl_qyzl_layout = (RelativeLayout) v.findViewById(R.id.rl_qyzl_layout);
        rl_zswsgf_layout = (RelativeLayout) v.findViewById(R.id.rl_zswsgf_layout);
        rl_cttg34d_layout = (RelativeLayout) v.findViewById(R.id.rl_cttg34d_layout);
        rl_xtygwjcz_layout = (RelativeLayout) v.findViewById(R.id.rl_xtygwjcz_layout);
        rl_qnywc_layout = (RelativeLayout) v.findViewById(R.id.rl_qnywc_layout);
        rl_smxfmwxy_layout = (RelativeLayout) v.findViewById(R.id.rl_smxfmwxy_layout);
        rl_jsqdlns_layout = (RelativeLayout) v.findViewById(R.id.rl_jsqdlns_layout);
        rl_gdrgqdfql_layout = (RelativeLayout) v.findViewById(R.id.rl_gdrgqdfql_layout);
        tv_changgui = (TextView) v.findViewById(R.id.tv_changgui);
        tv_teshu = (TextView) v.findViewById(R.id.tv_teshu);
        tv_zhiliao = (TextView) v.findViewById(R.id.tv_zhiliao);
        tv_prevention = (TextView) v.findViewById(R.id.tv_prevention);
        tv_custom = (TextView) v.findViewById(R.id.tv_custom);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_custom_ll:
                ICUCommonMethod.clickSwitch(detail_custom_ll, thumbnail_custom_ll, detail_custom_tv,
                        detail_custom_iv);
                break;
            case R.id.detail_prevention_ll:
                ICUCommonMethod.clickSwitch(detail_prevention_ll, thumbnail_prevention_ll,
                        detail_prevention_tv,
                        detail_prevention_iv);
                break;
            case R.id.detail_zhiliao_ll:
                ICUCommonMethod.clickSwitch(detail_zhiliao_ll, thumbnail_zhiliao_ll, detail_zhiliao_tv,
                        detail_zhiliao_iv);
                break;
            case R.id.detail_teshu_ll:
                ICUCommonMethod.clickSwitch(detail_teshu_ll, thumbnail_teshu_ll, detail_teshu_tv,
                        detail_teshu_iv);
                break;
            case R.id.detail_changgui_ll:
                ICUCommonMethod.clickSwitch(detail_changgui_ll, thumbnail_changgui_ll, detail_changgui_tv,
                        detail_changgui_iv);
                break;
            case R.id.page_time:
                DateAlertDialog();
                break;
            case R.id.save:
                if (status == 1) {
                    saveICUB();
                } else if (status == 2) {
                    updateICUB();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 时间提示框
     */
    public void DateAlertDialog() {

        dateView = View.inflate(getActivity(), R.layout.datetimepickdialog,
                null);

        datepick = (DatePicker) dateView.findViewById(R.id.new_act_date_picker);

        timepick = (TimePicker) dateView.findViewById(R.id.new_act_time_picker);

        String recordingTime = (String) ICUResourceSave.getInstance(getActivity()).get("recordingTime");
        Calendar calendar = Calendar.getInstance();
        if (recordingTime != null) {
            if (!recordingTime.equals("")) {
                calendar = ICUCommonMethod.parseDateForIcu(recordingTime);
            }
        }
        /** initDate */
        ICUCommonMethod.initDate(calendar, datepick);
        /**  initTime*/
        ICUCommonMethod.initTime(calendar, timepick);

        AlertDialog mDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                myLongDate = ICUCommonMethod.getCurrentTime(myLongDate, arrive_year, arrive_month, arrive_day,
                        arrive_hour, arrive_min, datepick, timepick);
                page_time.setText(myLongDate);
                /** 把recordingTime存放到sp中 */
                Map<String, Object> recordingTime = new HashMap<String, Object>();
                recordingTime.put("recordingTime", myLongDate);
                ICUResourceSave.getInstance(getActivity()).save(recordingTime);
                searchICUB();
            }
        });
        builder.setNegativeButton("取消", null);
        mDialog = builder.create();
        mDialog.setView(dateView);
        mDialog.setTitle("请选择时间");

        mDialog.show();
    }

    /**
     * 查询icu b
     */
    private void searchICUB() {
        progressDialog.show();
        SearchICUBBean bean = new SearchICUBBean();
        bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
        bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
        bean.setWardType("J");
        CCLog.i(TAG, "nursingEvaluation searchICUB>" + UrlConstant.searchICUBEvaluation() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.searchICUBEvaluation() + bean.toString(), new
                RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<SearchICUBDataBean>() {
                        }.getType();
                        SearchICUBDataBean dataBean = gson.fromJson(result, type);
                        rowNo = dataBean.getRowNo();
                        searchList = dataBean.getIcuList();
                        if (!searchList.isEmpty()) {
                            clear();
                            status = 2;
                            Map<String, SearchICUBBean> map = new HashMap<String, SearchICUBBean>();
                            for (SearchICUBBean bean : searchList) {
                                map.put(bean.getItemNo(), bean);
                            }
                            controlVis(map, searchList, true);
                            ICUCommonMethod.searchListToEditText(map, et_oral, "66");
                            ICUCommonMethod.searchListToEditText(map, et_catheter, "67");
                            ICUCommonMethod.searchListToEditText(map, et_decubitus, "68");
                            ICUCommonMethod.searchListToEditText(map, et_turnback, "69");
                            ICUCommonMethod.searchListToEditText(map, et_xt, "70");
                            ICUCommonMethod.searchListToEditText(map, et_yswh, "71");
                            ICUCommonMethod.searchListToEditText(map, et_gndl, "72");
                            ICUCommonMethod.searchListToEditText(map, et_zyct, "73");
                            ICUCommonMethod.searchListToEditText(map, et_qyzl, "74");
                            ICUCommonMethod.searchListToEditText(map, et_zswsgf, "75");
                            ICUCommonMethod.searchListToEditText(map, et_cttg34d, "76");
                            ICUCommonMethod.searchListToEditText(map, et_xtygwjcz, "77");
                            ICUCommonMethod.searchListToEditText(map, et_qnywc, "78");
                            ICUCommonMethod.searchListToEditText(map, et_smxfmwxy, "79");
                            ICUCommonMethod.searchListToEditText(map, et_jsqdlns, "80");
                            ICUCommonMethod.searchListToEditText(map, et_gdrgqdfql, "81");
                            isShowCustom("82", et_custom);
                            isShowCustom("83", et_custom2);
                            isShowCustom("84", et_custom3);
                        } else {
                            clear();
                            Map<String, SearchICUBBean> map = new HashMap<String, SearchICUBBean>();
                            controlVis(map, searchList, false);
                            Toast.makeText(getActivity(), R.string.thistimethepatientdidnothaveICUrecords, Toast.LENGTH_SHORT).show();
                            status = 1;
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

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 更新icu b
     */
    private void updateICUB() {
        progressDialog.show();
        if (!canSubmit()) {
            ICUCommonMethod.showDialogContent(getActivity());
            progressDialog.dismiss();
        } else {
            getEdittext();
            UpdateICUBBean bean = new UpdateICUBBean();
            bean.setWardType("J");
            bean.setLogBy(UserInfo.getUserName());
            bean.setFlag(ICUDataMethod.getFlag(getActivity()));
            bean.setRowNo(rowNo);
            bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
            bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
            StringBuffer itemNoSb = new StringBuffer();
            StringBuffer valueCodeSb = new StringBuffer();
            StringBuffer valueNameSb = new StringBuffer();
            StringBuffer valueTypeSb = new StringBuffer();
            StringBuffer abnormalAttrSb = new StringBuffer();
            StringBuffer valueDescSb = new StringBuffer();
            StringBuffer columnNameSb = new StringBuffer();
            List<Map<String, Object>> localList = ICUDataMethod.getUpdateB();
            for (int i = 0; i < localList.size(); i++) {
                Map<String, Object> map = localList.get(i);
                String itemNo = (String) map.get("itemNo");
                String valueCode = (String) map.get("valueCode");
                String valueName = (String) map.get("valueName");
                String valueType = (String) map.get("valueType");
                String abnormalAttr = (String) map.get("abnormalAttr");
                String valueDesc = (String) map.get("valueDesc");
                String columnName = (String) map.get("columnName");
                itemNoSb.append(itemNo != null ? itemNo : "nothing");
                try {
                    valueNameSb.append(valueName != null ? java.net.URLEncoder.encode(valueName, "utf-8") :
                            "nothing");
                    valueCodeSb.append(valueCode != null ? java.net.URLEncoder.encode(valueCode, "utf-8") :
                            "nothing");
                    columnNameSb.append(columnName != null ? java.net.URLEncoder.encode(columnName, "utf-8") :
                            "nothing");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                valueTypeSb.append(valueType != null ? valueType : "nothing");
                abnormalAttrSb.append(abnormalAttr != null ? abnormalAttr : "nothing");
                valueDescSb.append(valueDesc != null ? valueDesc : "nothing");
                if (i < localList.size() - 1) {
                    itemNoSb.append("&itemNo=");
                    valueCodeSb.append("&valueCode=");
                    valueNameSb.append("&valueName=");
                    valueTypeSb.append("&valueType=");
                    abnormalAttrSb.append("&abnormalAttr=");
                    valueDescSb.append("&valueDesc=");
                    columnNameSb.append("&columnName=");
                }
            }
            bean.setItemNo(itemNoSb.toString());
            bean.setValueCode(valueCodeSb.toString());
            bean.setValueType(valueTypeSb.toString());
            bean.setValueName(valueNameSb.toString());
            bean.setAbnormalAttr(abnormalAttrSb.toString());
            bean.setValueDesc(valueDescSb.toString());
            bean.setColumnName(columnNameSb.toString());
            CCLog.i(TAG, "nursingEvaluation updateICUB>" + UrlConstant.updateICUBNursingEvaluation() + bean.toString());
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, UrlConstant.updateICUBNursingEvaluation() + bean.toString(), new
                    RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            String result = responseInfo.result;
                            progressDialog.dismiss();
                            Gson gson = new Gson();
                            Type type = new TypeToken<UpdateICUBBean>() {
                            }.getType();
                            UpdateICUBBean temp = gson.fromJson(result, type);
                            if (temp.getResult().equals("success")) {
                                rowNo = temp.getRowNo();
                                searchICUB();
                            } else {
                                Toast.makeText(getActivity(), R.string.updatefailed, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            progressDialog.dismiss();
                            if (LinstenNetState.isLinkState(getActivity())) {
                                Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                        .LENGTH_SHORT).show();
                            }else{
                                toErrorAct();
                            }
                        }
                    });
        }
    }

    /**
     * 保存icu B
     */
    private void saveICUB() {
        progressDialog.show();
        if (!canSubmit()) {
            ICUCommonMethod.showDialogContent(getActivity());
            progressDialog.dismiss();
        } else {
            getEdittext();
            SaveICUBBean bean = new SaveICUBBean();
            bean.setRowNo(rowNo);
            bean.setWardType("J");
            bean.setLogBy(UserInfo.getUserName());
            bean.setFlag(ICUDataMethod.getFlag(getActivity()));
            bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
            bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
            StringBuffer itemNoSb = new StringBuffer();
            StringBuffer valueCodeSb = new StringBuffer();
            StringBuffer valueNameSb = new StringBuffer();
            StringBuffer valueTypeSb = new StringBuffer();
            StringBuffer abnormalAttrSb = new StringBuffer();
            StringBuffer valueDescSb = new StringBuffer();
            StringBuffer columnNameSb = new StringBuffer();
            List<Map<String, Object>> localList = ICUDataMethod.getSaveB();
            for (int i = 0; i < localList.size(); i++) {
                Map<String, Object> map = localList.get(i);
                String itemNo = (String) map.get("itemNo");
                String valueCode = (String) map.get("valueCode");
                String valueName = (String) map.get("valueName");
                String valueType = (String) map.get("valueType");
                String abnormalAttr = (String) map.get("abnormalAttr");
                String valueDesc = (String) map.get("valueDesc");
                String columnName = (String) map.get("columnName");
                itemNoSb.append(itemNo != null ? itemNo : "nothing");
                try {
                    valueNameSb.append(valueName != null ? java.net.URLEncoder.encode(valueName, "utf-8") :
                            "nothing");
                    valueCodeSb.append(valueCode != null ? java.net.URLEncoder.encode(valueCode, "utf-8") :
                            "nothing");
                    columnNameSb.append(columnName != null ? java.net.URLEncoder.encode(columnName, "utf-8") :
                            "nothing");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                valueTypeSb.append(valueType != null ? valueType : "nothing");
                abnormalAttrSb.append(abnormalAttr != null ? abnormalAttr : "nothing");
                valueDescSb.append(valueDesc != null ? valueDesc : "nothing");
                if (i < localList.size() - 1) {
                    itemNoSb.append("&itemNo=");
                    valueCodeSb.append("&valueCode=");
                    valueNameSb.append("&valueName=");
                    valueTypeSb.append("&valueType=");
                    abnormalAttrSb.append("&abnormalAttr=");
                    valueDescSb.append("&valueDesc=");
                    columnNameSb.append("&columnName=");
                }
            }
            bean.setItemNo(itemNoSb.toString());
            bean.setValueCode(valueCodeSb.toString());
            bean.setValueType(valueTypeSb.toString());
            bean.setValueName(valueNameSb.toString());
            bean.setAbnormalAttr(abnormalAttrSb.toString());
            bean.setValueDesc(valueDescSb.toString());
            bean.setColumnName(columnNameSb.toString());
            CCLog.i(TAG, UrlConstant.saveICUBNursingEvaluation() + bean.toString());
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, UrlConstant.saveICUBNursingEvaluation() + bean.toString(), new
                    RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            String result = responseInfo.result;
                            progressDialog.dismiss();
                            Gson gson = new Gson();
                            Type type = new TypeToken<SaveICUBBean>() {
                            }.getType();
                            SaveICUBBean temp = gson.fromJson(result, type);
                            if (temp.getResult().equals("success")) {
                                rowNo = temp.getRowNo();
                                searchICUB();
                            } else {
                                Toast.makeText(getActivity(), R.string.save_fail, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            progressDialog.dismiss();
                            if (LinstenNetState.isLinkState(getActivity())) {
                                Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                        .LENGTH_SHORT).show();
                            }else{
                                toErrorAct();
                            }
                        }
                    });
        }
    }

    private void addEditTextList() {
        etList.add(et_oral);
        etList.add(et_catheter);
        etList.add(et_decubitus);
        etList.add(et_turnback);
        etList.add(et_xt);
        etList.add(et_yswh);
        etList.add(et_gndl);
        etList.add(et_zyct);
        etList.add(et_qyzl);
        etList.add(et_zswsgf);
        etList.add(et_cttg34d);
        etList.add(et_xtygwjcz);
        etList.add(et_qnywc);
        etList.add(et_smxfmwxy);
        etList.add(et_jsqdlns);
        etList.add(et_gdrgqdfql);
        etList.add(et_custom);
        etList.add(et_custom2);
        etList.add(et_custom3);
    }

    private void getEdittext() {
        ICUCommonMethod.textWatcherMethod(et_oral, dictItem, "66");
        ICUCommonMethod.textWatcherMethod(et_catheter, dictItem, "67");
        ICUCommonMethod.textWatcherMethod(et_decubitus, dictItem, "68");
        ICUCommonMethod.textWatcherMethod(et_turnback, dictItem, "69");
        ICUCommonMethod.textWatcherMethod(et_xt, dictItem, "70");
        ICUCommonMethod.textWatcherMethod(et_yswh, dictItem, "71");
        ICUCommonMethod.textWatcherMethod(et_gndl, dictItem, "72");
        ICUCommonMethod.textWatcherMethod(et_zyct, dictItem, "73");
        ICUCommonMethod.textWatcherMethod(et_qyzl, dictItem, "74");
        ICUCommonMethod.textWatcherMethod(et_zswsgf, dictItem, "75");
        ICUCommonMethod.textWatcherMethod(et_cttg34d, dictItem, "76");
        ICUCommonMethod.textWatcherMethod(et_xtygwjcz, dictItem, "77");
        ICUCommonMethod.textWatcherMethod(et_qnywc, dictItem, "78");
        ICUCommonMethod.textWatcherMethod(et_smxfmwxy, dictItem, "79");
        ICUCommonMethod.textWatcherMethod(et_jsqdlns, dictItem, "80");
        ICUCommonMethod.textWatcherMethod(et_gdrgqdfql, dictItem, "81");
        ICUCommonMethod.textWatcherMethod(et_custom, dictItem, "82");
        ICUCommonMethod.textWatcherMethod(et_custom2, dictItem, "83");
        ICUCommonMethod.textWatcherMethod(et_custom3, dictItem, "84");
    }

    private void isShowCustom(String orgITemNo, EditText et) {
        Map<String, SearchICUBBean> map = new HashMap<String, SearchICUBBean>();
        for (SearchICUBBean bean : searchList) {
            map.put(bean.getItemNo(), bean);
        }
        if (map.containsKey(orgITemNo)) {
            SearchICUBBean searchICUBBean = map.get(orgITemNo);
            et.setText(searchICUBBean.getValueName());
        } else {
            if (orgITemNo.equals("82")) {
                rl_custom1.setVisibility(View.GONE);
            }
            if (orgITemNo.equals("83")) {
                rl_custom2.setVisibility(View.GONE);
            }
            if (orgITemNo.equals("84")) {
                rl_custom3.setVisibility(View.GONE);
            }
        }
    }

    private void clear() {
//        formNo = "";
        for (EditText editText : etList) {
            editText.setText("");
        }
        ICUDataMethod.reStartUpdateB();
        ICUDataMethod.reStartSaveB();
    }

    public boolean canSubmit() {
        boolean flag = false;
        for (EditText editText : etList) {
            if (!"".equals(editText.getText().toString())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void controlVis(Map<String, SearchICUBBean> map, List<SearchICUBBean> list, boolean flag) {
        if (flag) {
            StringBuffer valueCode59 = new StringBuffer();
            StringBuffer valueCode60 = new StringBuffer();
            StringBuffer valueCode61 = new StringBuffer();
            StringBuffer valueCode62 = new StringBuffer();
            for (SearchICUBBean searchICUBBean : list) {
                String valueCode = searchICUBBean.getValueCode();
                if (searchICUBBean.getItemNo().equals("59")) {
                    valueCode59.append(valueCode + "|");
                }
                if (searchICUBBean.getItemNo().equals("60")) {
                    valueCode60.append(valueCode + "|");
                }
                if (searchICUBBean.getItemNo().equals("61")) {
                    valueCode61.append(valueCode + "|");
                }
                if (searchICUBBean.getItemNo().equals("62")) {
                    valueCode62.append(valueCode + "|");
                }
            }
            String[] valueCodes59 = getStrings(valueCode59);
            if (!valueCode59.toString().equals("")) {
                thumbnail_changgui_ll.setVisibility(View.VISIBLE);
                detail_changgui_ll.setVisibility(View.VISIBLE);
                tv_changgui.setVisibility(View.GONE);
                for (String code : valueCodes59) {
                    if (code.equals("66")) {
                        rl_oral_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("67")) {
                        rl_catheter_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("68")) {
                        rl_decubitus_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("69")) {
                        rl_turnback_layout.setVisibility(View.VISIBLE);
                    }
                }
            }
            String[] valueCodes60 = getStrings(valueCode60);
            if (!valueCode60.toString().equals("")) {
                thumbnail_teshu_ll.setVisibility(View.VISIBLE);
                detail_teshu_ll.setVisibility(View.VISIBLE);
                tv_teshu.setVisibility(View.GONE);
                for (String code : valueCodes60) {
                    if (code.equals("70")) {
                        rl_xt_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("71")) {
                        rl_yswh_layout.setVisibility(View.VISIBLE);
                    }
                }
            }
            String[] valueCodes61 = getStrings(valueCode61);
            if (!valueCode61.toString().equals("")) {
                thumbnail_zhiliao_ll.setVisibility(View.VISIBLE);
                detail_zhiliao_ll.setVisibility(View.VISIBLE);
                tv_zhiliao.setVisibility(View.GONE);
                for (String code : valueCodes61) {
                    if (code.equals("72")) {
                        rl_gndl_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("73")) {
                        rl_zyct_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("74")) {
                        rl_qyzl_layout.setVisibility(View.VISIBLE);
                    }
                }
            }
            String[] valueCodes62 = getStrings(valueCode62);
            if (!valueCode62.toString().equals("")) {
                thumbnail_prevention_ll.setVisibility(View.VISIBLE);
                detail_prevention_ll.setVisibility(View.VISIBLE);
                tv_prevention.setVisibility(View.GONE);
                for (String code : valueCodes62) {
                    if (code.equals("75")) {
                        rl_zswsgf_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("76")) {
                        rl_cttg34d_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("77")) {
                        rl_xtygwjcz_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("78")) {
                        rl_qnywc_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("79")) {
                        rl_smxfmwxy_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("80")) {
                        rl_jsqdlns_layout.setVisibility(View.VISIBLE);
                    }
                    if (code.equals("81")) {
                        rl_gdrgqdfql_layout.setVisibility(View.VISIBLE);
                    }
                }
            }
            if (map.containsKey("82") || map.containsKey("83") || map.containsKey("84")) {
                thumbnail_custom_ll.setVisibility(View.VISIBLE);
                detail_custom_ll.setVisibility(View.VISIBLE);
                tv_custom.setVisibility(View.GONE);
                if (map.containsKey("82")) {
                    rl_custom1.setVisibility(View.VISIBLE);
                }
                if (map.containsKey("83")) {
                    rl_custom2.setVisibility(View.VISIBLE);
                }
                if (map.containsKey("84")) {
                    rl_custom3.setVisibility(View.VISIBLE);
                }
            }
        } else {
            rl_custom1.setVisibility(View.GONE);
            rl_custom2.setVisibility(View.GONE);
            rl_custom3.setVisibility(View.GONE);
            rl_oral_layout.setVisibility(View.GONE);
            rl_catheter_layout.setVisibility(View.GONE);
            rl_decubitus_layout.setVisibility(View.GONE);
            rl_turnback_layout.setVisibility(View.GONE);
            rl_xt_layout.setVisibility(View.GONE);
            rl_yswh_layout.setVisibility(View.GONE);
            rl_gndl_layout.setVisibility(View.GONE);
            rl_zyct_layout.setVisibility(View.GONE);
            rl_qyzl_layout.setVisibility(View.GONE);
            rl_zswsgf_layout.setVisibility(View.GONE);
            rl_cttg34d_layout.setVisibility(View.GONE);
            rl_xtygwjcz_layout.setVisibility(View.GONE);
            rl_qnywc_layout.setVisibility(View.GONE);
            rl_smxfmwxy_layout.setVisibility(View.GONE);
            rl_jsqdlns_layout.setVisibility(View.GONE);
            rl_gdrgqdfql_layout.setVisibility(View.GONE);
            tv_changgui.setVisibility(View.VISIBLE);
            tv_teshu.setVisibility(View.VISIBLE);
            tv_zhiliao.setVisibility(View.VISIBLE);
            tv_prevention.setVisibility(View.VISIBLE);
            tv_custom.setVisibility(View.VISIBLE);
            detail_changgui_ll.setVisibility(View.GONE);
            detail_teshu_ll.setVisibility(View.GONE);
            detail_zhiliao_ll.setVisibility(View.GONE);
            detail_prevention_ll.setVisibility(View.GONE);
            detail_custom_ll.setVisibility(View.GONE);
            thumbnail_changgui_ll.setVisibility(View.GONE);
            thumbnail_teshu_ll.setVisibility(View.GONE);
            thumbnail_zhiliao_ll.setVisibility(View.GONE);
            thumbnail_prevention_ll.setVisibility(View.GONE);
            thumbnail_custom_ll.setVisibility(View.GONE);
        }
    }

    @NonNull
    private String[] getStrings(StringBuffer valueCode) {
        String[] array = valueCode.toString().split("\\|");
        StringBuffer sb = new StringBuffer();
        sb.append(array[0]);
        if (array.length > 1) {
            for (String item : array) {
                if (sb.toString().indexOf(item) != -1) {
                    continue;
                }
                sb.append("|" + item);
            }
        }
        return sb.toString().split("\\|");
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_icu_hlpj);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
