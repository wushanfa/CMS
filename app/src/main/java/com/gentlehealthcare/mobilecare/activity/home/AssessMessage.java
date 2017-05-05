package com.gentlehealthcare.mobilecare.activity.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class AssessMessage extends BaseFragment implements View.OnClickListener {
    /**
     * editext
     */
    private EditText et_cg, et_cg_memo, et_jmcbj, et_jmcbj_memo, et_qnyl, et_qnyl_memo, et_bw, et_bw_memo,
            et_zgts, et_zgts_memo, et_ccpf, et_ccpf_memo, et_zt, et_zt_memo, et_pf, et_pf_memo, et_cvc_bw,
            et_cvc_bw_memo, et_cvc_zgts, et_cvc_zgts_memo, et_cvc_tw, et_cvc_tw_memo, et_cvc_cccpf,
            et_cvc_cccpf_memo, et_cvc_zt, et_cvc_zt_memo, et_sz, et_sz_memo, et_st, et_st_memo, et_mai,
            et_mai_memo;

    //artificial airway
    private LinearLayout detail_airway_ll, thumbnail_airway_ll;
    private TextView detail_airway_tv, detail_airway_iv;
    //arterial catheter
    private LinearLayout thumbnail_artery_ll, detail_artery_ll;
    private TextView detail_artery_tv, detail_artery_iv;
    //fall
    private LinearLayout thumbnail_fall_ll, detail_fall_ll;
    private TextView detail_fall_tv, detail_fall_iv;
    //cvc
    private LinearLayout thumbnail_cvc_ll, detail_cvc_ll;
    private TextView detail_cvc_tv, detail_cvc_iv;
    //tongue and pulse
    private LinearLayout thumbnail_tongue_ll, detail_tongue_ll;
    private TextView detail_tongue_tv, detail_tongue_iv;
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
    private Map<String, LoadIcuBBean> map = new HashMap<String, LoadIcuBBean>();

    private ProgressDialog progressDialog;

    private int rowNo = 1;

//    private String formNo = "";

    /**
     * 查询的结果
     */
    private static List<SearchICUBBean> searchList = new ArrayList<SearchICUBBean>();

    private static int status = 1;

    private List<EditText> editList = new ArrayList<EditText>();
    /**
     * for valueDesc
     */
    private Map<String, Object> cgmap = new HashMap<String, Object>();
    private Map<String, Object> jmcbjmap = new HashMap<String, Object>();
    private Map<String, Object> qnylmap = new HashMap<String, Object>();
    private Map<String, Object> bwmap = new HashMap<String, Object>();
    private Map<String, Object> zgtsmap = new HashMap<String, Object>();
    private Map<String, Object> ccpfmap = new HashMap<String, Object>();
    private Map<String, Object> ztmap = new HashMap<String, Object>();
    private Map<String, Object> pfmap = new HashMap<String, Object>();
    private Map<String, Object> cvc_bwmap = new HashMap<String, Object>();
    private Map<String, Object> cvc_zgtsmap = new HashMap<String, Object>();
    private Map<String, Object> cvc_twmap = new HashMap<String, Object>();
    private Map<String, Object> cvc_cccpfmap = new HashMap<String, Object>();
    private Map<String, Object> cvc_ztmap = new HashMap<String, Object>();
    private Map<String, Object> szmap = new HashMap<String, Object>();
    private Map<String, Object> stmap = new HashMap<String, Object>();
    private Map<String, Object> maimap = new HashMap<String, Object>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_icu_pgnr, null);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.operating));

        Bundle dataI = getArguments();
        dictItem = (List<LoadIcuBBean>) dataI.getSerializable("assessMessage");
        for (LoadIcuBBean bean : dictItem) {
            map.put(bean.getItemNo(), bean);
        }

        initView(view);

        addEditIntoList();

        page_time.setText((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));

        searchICUB();

        return view;
    }

    private void initView(View v) {
        detail_airway_ll = (LinearLayout) v.findViewById(R.id.detail_airway_ll);
        detail_airway_ll.setOnClickListener(this);
        thumbnail_airway_ll = (LinearLayout) v.findViewById(R.id.thumbnail_airway_ll);
        thumbnail_artery_ll = (LinearLayout) v.findViewById(R.id.thumbnail_artery_ll);
        detail_artery_ll = (LinearLayout) v.findViewById(R.id.detail_artery_ll);
        detail_artery_ll.setOnClickListener(this);
        thumbnail_fall_ll = (LinearLayout) v.findViewById(R.id.thumbnail_fall_ll);
        detail_fall_ll = (LinearLayout) v.findViewById(R.id.detail_fall_ll);
        detail_fall_ll.setOnClickListener(this);
        thumbnail_cvc_ll = (LinearLayout) v.findViewById(R.id.thumbnail_cvc_ll);
        detail_cvc_ll = (LinearLayout) v.findViewById(R.id.detail_cvc_ll);
        detail_cvc_ll.setOnClickListener(this);
        thumbnail_tongue_ll = (LinearLayout) v.findViewById(R.id.thumbnail_tongue_ll);
        detail_tongue_ll = (LinearLayout) v.findViewById(R.id.detail_tongue_ll);
        detail_tongue_ll.setOnClickListener(this);
        detail_airway_tv = (TextView) v.findViewById(R.id.detail_airway_tv);
        detail_airway_iv = (TextView) v.findViewById(R.id.detail_airway_iv);
        detail_artery_tv = (TextView) v.findViewById(R.id.detail_artery_tv);
        detail_artery_iv = (TextView) v.findViewById(R.id.detail_artery_iv);
        detail_fall_tv = (TextView) v.findViewById(R.id.detail_fall_tv);
        detail_fall_iv = (TextView) v.findViewById(R.id.detail_fall_iv);
        detail_cvc_tv = (TextView) v.findViewById(R.id.detail_cvc_tv);
        detail_cvc_iv = (TextView) v.findViewById(R.id.detail_cvc_iv);
        detail_tongue_tv = (TextView) v.findViewById(R.id.detail_tongue_tv);
        detail_tongue_iv = (TextView) v.findViewById(R.id.detail_tongue_iv);
        page_time = (TextView) v.findViewById(R.id.page_time);
        page_time.setOnClickListener(this);
        save = (Button) v.findViewById(R.id.save);
        save.setOnClickListener(this);
        et_cg = (EditText) v.findViewById(R.id.et_cg);
        et_cg_memo = (EditText) v.findViewById(R.id.et_cg_memo);
        et_jmcbj = (EditText) v.findViewById(R.id.et_jmcbj);
        et_jmcbj_memo = (EditText) v.findViewById(R.id.et_jmcbj_memo);
        et_qnyl = (EditText) v.findViewById(R.id.et_qnyl);
        et_qnyl_memo = (EditText) v.findViewById(R.id.et_qnyl_memo);
        et_bw = (EditText) v.findViewById(R.id.et_bw);
        et_bw_memo = (EditText) v.findViewById(R.id.et_bw_memo);
        et_zgts = (EditText) v.findViewById(R.id.et_zgts);
        et_zgts_memo = (EditText) v.findViewById(R.id.et_zgts_memo);
        et_ccpf = (EditText) v.findViewById(R.id.et_ccpf);
        et_ccpf_memo = (EditText) v.findViewById(R.id.et_ccpf_memo);
        et_zt = (EditText) v.findViewById(R.id.et_zt);
        et_zt_memo = (EditText) v.findViewById(R.id.et_zt_memo);
        et_pf = (EditText) v.findViewById(R.id.et_pf);
        et_pf_memo = (EditText) v.findViewById(R.id.et_pf_memo);
        et_cvc_bw = (EditText) v.findViewById(R.id.et_cvc_bw);
        et_cvc_bw_memo = (EditText) v.findViewById(R.id.et_cvc_bw_memo);
        et_cvc_zgts = (EditText) v.findViewById(R.id.et_cvc_zgts);
        et_cvc_zgts_memo = (EditText) v.findViewById(R.id.et_cvc_zgts_memo);
        et_cvc_tw = (EditText) v.findViewById(R.id.et_cvc_tw);
        et_cvc_tw_memo = (EditText) v.findViewById(R.id.et_cvc_tw_memo);
        et_cvc_cccpf = (EditText) v.findViewById(R.id.et_cvc_cccpf);
        et_cvc_cccpf_memo = (EditText) v.findViewById(R.id.et_cvc_cccpf_memo);
        et_cvc_zt = (EditText) v.findViewById(R.id.et_cvc_zt);
        et_cvc_zt_memo = (EditText) v.findViewById(R.id.et_cvc_zt_memo);
        et_sz = (EditText) v.findViewById(R.id.et_sz);
        et_sz_memo = (EditText) v.findViewById(R.id.et_sz_memo);
        et_st = (EditText) v.findViewById(R.id.et_st);
        et_st_memo = (EditText) v.findViewById(R.id.et_st_memo);
        et_mai = (EditText) v.findViewById(R.id.et_mai);
        et_mai_memo = (EditText) v.findViewById(R.id.et_mai_memo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_airway_ll:
                ICUCommonMethod.clickSwitch(detail_airway_ll, thumbnail_airway_ll, detail_airway_tv,
                        detail_airway_iv);
                break;
            case R.id.detail_artery_ll:
                ICUCommonMethod.clickSwitch(detail_artery_ll, thumbnail_artery_ll, detail_artery_tv,
                        detail_artery_iv);
                break;
            case R.id.detail_fall_ll:
                ICUCommonMethod.clickSwitch(detail_fall_ll, thumbnail_fall_ll, detail_fall_tv,
                        detail_fall_iv);
                break;
            case R.id.detail_cvc_ll:
                ICUCommonMethod.clickSwitch(detail_cvc_ll, thumbnail_cvc_ll, detail_cvc_tv, detail_cvc_iv);
                break;
            case R.id.detail_tongue_ll:
                ICUCommonMethod.clickSwitch(detail_tongue_ll, thumbnail_tongue_ll, detail_tongue_tv,
                        detail_tongue_iv);
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
        builder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener() {

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
        builder.setNegativeButton(R.string.cancel, null);
        mDialog = builder.create();
        mDialog.setView(dateView);
        mDialog.setTitle(AssessMessage.this.getString(R.string.please_choose_time));

        mDialog.show();
    }

    /**
     * 查询icu b
     */
    private void searchICUB() {
        progressDialog.show();
        SearchICUBBean bean = new SearchICUBBean();
        bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
//        bean.setFormNo(!formNo.equals("") ? formNo : "nothing");
        bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
        bean.setWardType("D");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.searchICUB() + bean.toString(), new
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
                            ICUCommonMethod.searchForContent(searchList, "1", et_cg, et_cg_memo);
                            ICUCommonMethod.searchForContent(searchList, "2", et_jmcbj, et_jmcbj_memo);
                            ICUCommonMethod.searchForContent(searchList, "3", et_qnyl, et_qnyl_memo);
                            ICUCommonMethod.searchForContent(searchList, "9", et_bw, et_bw_memo);
                            ICUCommonMethod.searchForContent(searchList, "10", et_zgts, et_zgts_memo);
                            ICUCommonMethod.searchForContent(searchList, "11", et_ccpf, et_ccpf_memo);
                            ICUCommonMethod.searchForContent(searchList, "12", et_zt, et_zt_memo);
                            ICUCommonMethod.searchForContent(searchList, "16", et_pf, et_pf_memo);
                            ICUCommonMethod.searchForContent(searchList, "4", et_cvc_bw, et_cvc_bw_memo);
                            ICUCommonMethod.searchForContent(searchList, "5", et_cvc_zgts, et_cvc_zgts_memo);
                            ICUCommonMethod.searchForContent(searchList, "6", et_cvc_tw, et_cvc_tw_memo);
                            ICUCommonMethod.searchForContent(searchList, "7", et_cvc_cccpf, et_cvc_cccpf_memo);
                            ICUCommonMethod.searchForContent(searchList, "8", et_cvc_zt, et_cvc_zt_memo);
                            ICUCommonMethod.searchForContent(searchList, "13", et_sz, et_sz_memo);
                            ICUCommonMethod.searchForContent(searchList, "14", et_st, et_st_memo);
                            ICUCommonMethod.searchForContent(searchList, "15", et_mai, et_mai_memo);
                        } else {
                            clear();
                            Toast.makeText(getActivity(), R.string.this_patient_do_not_have_icu_record, Toast.LENGTH_SHORT).show();
                            status = 1;
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        if (LinstenNetState.isLinkState(getActivity())) {
                            Toast.makeText(getActivity(), R.string.execute_exception, Toast.LENGTH_SHORT).show();
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
            bean.setWardType("D");
            bean.setRowNo(rowNo);
//            bean.setFormNo(!formNo.equals("") ? formNo : "nothing");
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
            List<Map<String, Object>> localList = ICUDataMethod.getUpdateB();
            for (int i = 0; i < localList.size(); i++) {
                Map<String, Object> map = localList.get(i);
                String itemNo = (String) map.get("itemNo");
                String valueCode = (String) map.get("valueCode");
                String valueName = (String) map.get("valueName");
                String valueType = (String) map.get("valueType");
                String abnormalAttr = (String) map.get("abnormalAttr");
                String valueDesc = (String) map.get("valueDesc");
                itemNoSb.append(itemNo != null ? itemNo : "nothing");
                try {
                    valueNameSb.append(valueName != null ? java.net.URLEncoder.encode(valueName, "utf-8") :
                            "nothing");
                    valueCodeSb.append(valueCode != null ? java.net.URLEncoder.encode(valueCode, "utf-8") :
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
                }
            }
            bean.setItemNo(itemNoSb.toString());
            bean.setValueCode(valueCodeSb.toString());
            bean.setValueType(valueTypeSb.toString());
            bean.setValueName(valueNameSb.toString());
            bean.setAbnormalAttr(abnormalAttrSb.toString());
            bean.setValueDesc(valueDescSb.toString());
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, UrlConstant.updateICUBRecord() + bean.toString(), new
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
                                ICUDataMethod.saveFlag(getActivity(), 1);
                                searchICUB();
                            } else {
                                Toast.makeText(getActivity(), R.string.update_fail, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
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
            bean.setLogBy(UserInfo.getUserName());
            bean.setRowNo(rowNo);
//            bean.setFormNo(formNo);
            bean.setWardType("D");
            bean.setFlag(ICUDataMethod.getFlag(getActivity()));
            bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
            bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
            StringBuffer itemNoSb = new StringBuffer();
            StringBuffer valueCodeSb = new StringBuffer();
            StringBuffer valueNameSb = new StringBuffer();
            StringBuffer valueTypeSb = new StringBuffer();
            StringBuffer abnormalAttrSb = new StringBuffer();
            StringBuffer valueDescSb = new StringBuffer();
            List<Map<String, Object>> localList = ICUDataMethod.getSaveB();
            for (int i = 0; i < localList.size(); i++) {
                Map<String, Object> map = localList.get(i);
                String itemNo = (String) map.get("itemNo");
                String valueCode = (String) map.get("valueCode");
                String valueName = (String) map.get("valueName");
                String valueType = (String) map.get("valueType");
                String abnormalAttr = (String) map.get("abnormalAttr");
                String valueDesc = (String) map.get("valueDesc");
                itemNoSb.append(itemNo != null ? itemNo : "nothing");
                try {
                    valueNameSb.append(valueName != null ? java.net.URLEncoder.encode(valueName, "utf-8") :
                            "nothing");
                    valueCodeSb.append(valueCode != null ? java.net.URLEncoder.encode(valueCode, "utf-8") :
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
                }
            }
            bean.setItemNo(itemNoSb.toString());
            bean.setValueCode(valueCodeSb.toString());
            bean.setValueType(valueTypeSb.toString());
            bean.setValueName(valueNameSb.toString());
            bean.setAbnormalAttr(abnormalAttrSb.toString());
            bean.setValueDesc(valueDescSb.toString());
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, UrlConstant.saveICUB() + bean.toString(), new
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
                                ICUDataMethod.saveFlag(getActivity(), 1);
                                searchICUB();
                            } else {
                                Toast.makeText(getActivity(), R.string.save_fail, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void getEdittext() {
        ICUCommonMethod.textWatcherMethodForContent(et_cg, et_cg_memo, map, "1");
        ICUCommonMethod.textWatcherMethodForContent(et_jmcbj, et_jmcbj_memo, map, "2");
        ICUCommonMethod.textWatcherMethodForContent(et_qnyl, et_qnyl_memo, map, "3");
        ICUCommonMethod.textWatcherMethodForContent(et_bw, et_bw_memo, map, "9");
        ICUCommonMethod.textWatcherMethodForContent(et_zgts, et_zgts_memo, map, "10");
        ICUCommonMethod.textWatcherMethodForContent(et_ccpf, et_ccpf_memo, map, "11");
        ICUCommonMethod.textWatcherMethodForContent(et_zt, et_zt_memo, map, "12");
        ICUCommonMethod.textWatcherMethodForContent(et_pf, et_pf_memo, map, "16");
        ICUCommonMethod.textWatcherMethodForContent(et_cvc_bw, et_cvc_bw_memo, map, "4");
        ICUCommonMethod.textWatcherMethodForContent(et_cvc_zgts, et_cvc_zgts_memo, map, "5");
        ICUCommonMethod.textWatcherMethodForContent(et_cvc_tw, et_cvc_tw_memo, map, "6");
        ICUCommonMethod.textWatcherMethodForContent(et_cvc_cccpf, et_cvc_cccpf_memo, map, "7");
        ICUCommonMethod.textWatcherMethodForContent(et_cvc_zt, et_cvc_zt_memo, map, "8");
        ICUCommonMethod.textWatcherMethodForContent(et_sz, et_sz_memo, map, "9");
        ICUCommonMethod.textWatcherMethodForContent(et_st, et_st_memo, map, "10");
        ICUCommonMethod.textWatcherMethodForContent(et_mai, et_mai_memo, map, "11");

    }

    /**
     * 把edittext放入list
     */
    private void addEditIntoList() {
        editList.add(et_cg);
        editList.add(et_cg_memo);
        editList.add(et_jmcbj);
        editList.add(et_jmcbj_memo);
        editList.add(et_qnyl);
        editList.add(et_qnyl_memo);
        editList.add(et_bw);
        editList.add(et_bw_memo);
        editList.add(et_zgts);
        editList.add(et_zgts_memo);
        editList.add(et_ccpf);
        editList.add(et_ccpf_memo);
        editList.add(et_zt);
        editList.add(et_zt_memo);
        editList.add(et_pf);
        editList.add(et_pf_memo);
        editList.add(et_cvc_bw);
        editList.add(et_cvc_bw_memo);
        editList.add(et_cvc_zgts);
        editList.add(et_cvc_zgts_memo);
        editList.add(et_cvc_tw);
        editList.add(et_cvc_tw_memo);
        editList.add(et_cvc_cccpf);
        editList.add(et_cvc_cccpf_memo);
        editList.add(et_cvc_zt);
        editList.add(et_cvc_zt_memo);
        editList.add(et_sz);
        editList.add(et_sz_memo);
        editList.add(et_st);
        editList.add(et_st_memo);
        editList.add(et_mai);
        editList.add(et_mai_memo);
    }

    private void clear() {
//        formNo="";
        for (EditText et : editList) {
            et.setText("");
        }
        ICUDataMethod.reStartUpdateB();
        ICUDataMethod.reStartSaveB();
    }

    public boolean canSubmit() {
        boolean flag = false;
        for (EditText et : editList) {
            if (!"".equals(et.getText().toString())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_icu);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
