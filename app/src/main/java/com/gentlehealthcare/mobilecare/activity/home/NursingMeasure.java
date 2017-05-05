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
import android.widget.CheckBox;
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
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuBResultBean;
import com.gentlehealthcare.mobilecare.net.bean.SaveICUBBean;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUBBean;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUBDataBean;
import com.gentlehealthcare.mobilecare.net.bean.UpdateICUBBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NursingMeasure extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "NursingMeasure";

    /**
     * 整理成map
     */
    Map<String, LoadIcuBBean> dictItemMap = new HashMap<String, LoadIcuBBean>();

    /**
     * checkBox
     */
    private LinearLayout ll_conventional_layout, ll_special_layout, ll_treatment_layout,
            ll_prevention_pneumonia_layout;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4 = null;
    private List<LoadIcuBResultBean> cbList1 = new ArrayList<LoadIcuBResultBean>();
    private List<LoadIcuBResultBean> cbList2 = new ArrayList<LoadIcuBResultBean>();
    private List<LoadIcuBResultBean> cbList3 = new ArrayList<LoadIcuBResultBean>();
    private List<LoadIcuBResultBean> cbList4 = new ArrayList<LoadIcuBResultBean>();
    private Map<String, CheckBox> cbMap1 = new HashMap<String, CheckBox>();
    private Map<String, CheckBox> cbMap2 = new HashMap<String, CheckBox>();
    private Map<String, CheckBox> cbMap3 = new HashMap<String, CheckBox>();
    private Map<String, CheckBox> cbMap4 = new HashMap<String, CheckBox>();

    /**
     * 时间
     */
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
    /**
     * 返回每个spinner下item对应list中位置
     */
    private int conventionalPosition, specialPosition, treatmentPosition, pneumoniaPosition = 0;

    private ProgressDialog progressDialog;
    /**
     * 自定义editText
     */
    private EditText etCustom, etCustom2, etCustom3;

    private static int status = 1;

    /**
     * 查询的结果
     */
    private static List<SearchICUBBean> searchList = new ArrayList<SearchICUBBean>();

    private int rowNO = 1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_icu_hlcs, null);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在操作");

        Bundle dataI = getArguments();
        dictItem = (List<LoadIcuBBean>) dataI.getSerializable("measure");

        initView(view);

        turnToMap();

        dynamicCheckBox();

        String rt = (String) ICUResourceSave.getInstance(getActivity()).get("recordingTime");
        if (rt == null || rt.equals("")) {
            /** 把recordingTime存放到sp中 */
            Map<String, Object> recordingTime = new HashMap<String, Object>();
            recordingTime.put("recordingTime", DateTool.getCurrDateTimeS());
            ICUResourceSave.getInstance(getActivity()).save(recordingTime);
        }
        page_time.setText((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));

        searchICUB();

        return view;
    }

    private void initView(View v) {
        page_time = (TextView) v.findViewById(R.id.page_time);
        page_time.setOnClickListener(this);
        save = (Button) v.findViewById(R.id.save);
        save.setOnClickListener(this);
        ll_conventional_layout = (LinearLayout) v.findViewById(R.id.ll_conventional_layout);
        ll_special_layout = (LinearLayout) v.findViewById(R.id.ll_special_layout);
        ll_treatment_layout = (LinearLayout) v.findViewById(R.id.ll_treatment_layout);
        ll_prevention_pneumonia_layout = (LinearLayout) v.findViewById(R.id.ll_prevention_pneumonia_layout);
        etCustom = (EditText) v.findViewById(R.id.et_custom);
        etCustom2 = (EditText) v.findViewById(R.id.et_custom2);
        etCustom3 = (EditText) v.findViewById(R.id.et_custom3);
    }

    private void dynamicCheckBox() {
        cbList1 = getCheckBoxList("59");
        cbList2 = getCheckBoxList("60");
        cbList3 = getCheckBoxList("61");
        cbList4 = getCheckBoxList("62");
        for (LoadIcuBResultBean temp1 : cbList1) {
            checkBox1 = new CheckBox(getActivity());
            checkBox1.setId(5900 + conventionalPosition);
            checkBox1.setText(temp1.getItemName());
            checkBox1.setTextColor(this.getResources().getColor(R.color.black_text_content));
            ll_conventional_layout.addView(checkBox1);
            cbMap1.put(temp1.getItemCode(), checkBox1);
            conventionalPosition++;
        }
        for (LoadIcuBResultBean temp2 : cbList2) {
            checkBox2 = new CheckBox(getActivity());
            checkBox2.setId(6000 + specialPosition);
            checkBox2.setText(temp2.getItemName());
            ll_special_layout.addView(checkBox2);
            checkBox2.setTextColor(this.getResources().getColor(R.color.black_text_content));
            cbMap2.put(temp2.getItemCode(), checkBox2);
            specialPosition++;
        }
        for (LoadIcuBResultBean temp3 : cbList3) {
            checkBox3 = new CheckBox(getActivity());
            checkBox3.setId(6100 + treatmentPosition);
            checkBox3.setText(temp3.getItemName());
            ll_treatment_layout.addView(checkBox3);
            checkBox3.setTextColor(this.getResources().getColor(R.color.black_text_content));
            cbMap3.put(temp3.getItemCode(), checkBox3);
            treatmentPosition++;
        }
        for (LoadIcuBResultBean temp4 : cbList4) {
            checkBox4 = new CheckBox(getActivity());
            checkBox4.setId(6200 + pneumoniaPosition);
            checkBox4.setText(temp4.getItemName());
            ll_prevention_pneumonia_layout.addView(checkBox4);
            checkBox4.setTextColor(this.getResources().getColor(R.color.black_text_content));
            cbMap4.put(temp4.getItemCode(), checkBox4);
            pneumoniaPosition++;
        }
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

        dateView = View.inflate(getActivity(), R.layout.datetimepickdialog, null);

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
        bean.setWardType("I");
        CCLog.i(TAG, "searchIcuB>" + UrlConstant.searchICUBNursingMeasure() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.searchICUBNursingMeasure() + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<SearchICUBDataBean>() {
                        }.getType();
                        SearchICUBDataBean dataBean = gson.fromJson(result, type);
                        rowNO = dataBean.getRowNo();
                        searchList = dataBean.getIcuList();
                        if (!searchList.isEmpty()) {
                            clear();
                            Map<String, SearchICUBBean> mapResult = new HashMap<String, SearchICUBBean>();
                            for (SearchICUBBean temp : searchList) {
                                mapResult.put(temp.getItemNo(), temp);
                            }
                            if (mapResult.containsKey("59") || mapResult.containsKey("60") || mapResult.containsKey
                                    ("61") || mapResult.containsKey("62")) {
                                status = 2;
                            } else {
                                status = 1;
                            }
                            setCheckBoxChange(mapResult, "59", cbMap1, cbList1);
                            setCheckBoxChange(mapResult, "60", cbMap2, cbList2);
                            setCheckBoxChange(mapResult, "61", cbMap3, cbList3);
                            setCheckBoxChange(mapResult, "62", cbMap4, cbList4);
                            if (mapResult.containsKey("82")) {
                                etCustom.setText(mapResult.get("82").getColumnName());
                            }
                            if (mapResult.containsKey("83")) {
                                etCustom2.setText(mapResult.get("83").getColumnName());
                            }
                            if (mapResult.containsKey("84")) {
                                etCustom3.setText(mapResult.get("84").getColumnName());
                            }
                        } else {
                            clear();
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
            getCheckBox(cbMap1, "59", cbList1);
            getCheckBox(cbMap2, "60", cbList2);
            getCheckBox(cbMap3, "61", cbList3);
            getCheckBox(cbMap4, "62", cbList4);
            getEdittext(etCustom, "82");
            getEdittext(etCustom2, "83");
            getEdittext(etCustom3, "84");
            UpdateICUBBean bean = new UpdateICUBBean();
            bean.setWardType("I");
            bean.setLogBy(UserInfo.getUserName());
            bean.setFlag(ICUDataMethod.getFlag(getActivity()));
            bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
            bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
            bean.setRowNo(rowNO);
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
                    valueCodeSb.append(valueCode != null ? URLEncoder.encode(valueCode, "utf-8") : "nothing");
                    valueNameSb.append(valueName != null ? URLEncoder.encode(valueName, "utf-8") : "nothing");
                    columnNameSb.append(columnName != null ? URLEncoder.encode(columnName, "utf-8") : "nothing");
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
            CCLog.i(TAG, UrlConstant.updateICUBNursingMeasure() + bean.toString());
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, UrlConstant.updateICUBNursingMeasure() + bean.toString(), new
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
                                rowNO = temp.getRowNo();
                                ICUDataMethod.saveFlag(getActivity(), 1);
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
                            } else {
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
            getCheckBox(cbMap1, "59", cbList1);
            getCheckBox(cbMap2, "60", cbList2);
            getCheckBox(cbMap3, "61", cbList3);
            getCheckBox(cbMap4, "62", cbList4);
            getEdittext(etCustom, "82");
            getEdittext(etCustom2, "83");
            getEdittext(etCustom3, "84");
            SaveICUBBean bean = new SaveICUBBean();
            bean.setLogBy(UserInfo.getUserName());
//            bean.setFormNo(formNo);
            bean.setWardType("I");
            bean.setFlag(ICUDataMethod.getFlag(getActivity()));
            bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
            bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
//            bean.setRowNo(String.valueOf(rowNO++));
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
                    valueCodeSb.append(valueCode != null ? URLEncoder.encode(valueCode, "utf-8") : "nothing");
                    valueNameSb.append(valueName != null ? URLEncoder.encode(valueName, "utf-8") : "nothing");
                    columnNameSb.append(columnName != null ? URLEncoder.encode(columnName, "utf-8") : "nothing");
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
            CCLog.i(TAG, UrlConstant.saveICUBNursingMeasure() + bean.toString());
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, UrlConstant.saveICUBNursingMeasure() + bean.toString(), new
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
                                rowNO = temp.getRowNo();
                                ICUDataMethod.saveFlag(getActivity(), 1);
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
                            } else {
                                toErrorAct();
                            }
                        }
                    });
        }

    }

    private void setCheckBoxChange(Map<String, SearchICUBBean> mapResult, String requireItemCode, Map<String,
            CheckBox> cbMap, List<LoadIcuBResultBean> cbList) {
        if (mapResult.containsKey(requireItemCode)) {
            String valueCodeArray = mapResult.get(requireItemCode).getValueCode();
            Map<String, LoadIcuBResultBean> map = new HashMap<String, LoadIcuBResultBean>();
            for (LoadIcuBResultBean temp : cbList) {
                map.put(temp.getItemCode(), temp);
            }
            if (valueCodeArray.contains("|")) {
                String[] valueCode = valueCodeArray.split("\\|");
                for (int i = 0; i < valueCode.length; i++) {
                    if (map.containsKey(valueCode[i])) {
                        cbMap.get(valueCode[i]).setChecked(true);
                    }
                }
            } else {
                if (map.containsKey(valueCodeArray)) {
                    cbMap.get(valueCodeArray).setChecked(true);
                }
            }
        }
    }

    /**
     * 获得checkbox的值，放入list，save or update
     *
     * @param cbMap
     * @param requireItemNo
     */
    private void getCheckBox(Map<String, CheckBox> cbMap, String requireItemNo, List<LoadIcuBResultBean> cbList) {
        boolean flag = false;
        LoadIcuBBean temp = dictItemMap.get(requireItemNo);
        StringBuffer valueCodeSb = new StringBuffer();
        StringBuffer valueNameSb = new StringBuffer();
        for (int j = 0; j < cbList.size(); j++) {
            String itemCode = cbList.get(j).getItemCode();
            CheckBox cb = cbMap.get(itemCode);
            if (cb.isChecked()) {
                LoadIcuBResultBean bean = cbList.get(j);
                valueCodeSb.append(bean.getItemCode() == null ? "nothing" : bean.getItemCode());
                valueNameSb.append(bean.getItemName() == null ? "nothing" : bean.getItemName());
                valueCodeSb.append("|");
                valueNameSb.append("|");
                flag = true;
            }
        }
        Map<String, Object> icuB = new HashMap<String, Object>();
        if (flag) {
            icuB = ICUDataMethod.getICUB(temp.getItemNo(), valueCodeSb.toString().substring(0, valueCodeSb
                            .toString().length() - 1), valueNameSb.toString().substring(0, valueNameSb.toString
                            ().length() - 1), cbList.get(0).getItemCodeType(), cbList.get(0).getAbnormalAttr(),
                    "nothing", "nothing");
            ICUDataMethod.addSaveB(icuB);
            ICUDataMethod.addUpdateB(icuB);
        }
    }

    /**
     * 获得editText的值，放入list，save or update
     *
     * @param etCustom
     * @param requireItem
     */
    private void getEdittext(EditText etCustom, String requireItem) {
        String custom = etCustom.getText().toString();
        if (!custom.equals("")) {
            LoadIcuBBean bean = new LoadIcuBBean();
            Map<String, LoadIcuBBean> itemMap = new HashMap<String, LoadIcuBBean>();
            for (LoadIcuBBean item : dictItem) {
                itemMap.put(item.getItemNo(), item);
            }
            if (itemMap.containsKey(requireItem)) {
                bean = itemMap.get(requireItem);
            }
            Map<String, Object> icub = new HashMap<String, Object>();
            if (bean.getItemNo() != null) {
                icub = ICUDataMethod.getICUB(bean.getItemNo(), "nothing", "nothing", "nothing", "nothing",
                        "nothing", custom);
            } else {
                icub = ICUDataMethod.getICUB(requireItem, "nothing", "nothing", "nothing", "nothing", "nothing",
                        custom);
            }
            ICUDataMethod.addUpdateB(icub);
            ICUDataMethod.addSaveB(icub);
        }
    }

    /**
     * 把后台传回来的list转成map，key-itemNo
     */
    private void turnToMap() {
        for (LoadIcuBBean bean : dictItem) {
            dictItemMap.put(bean.getItemNo(), bean);
        }
    }

    /**
     * 为checkBox分解后台传回来的list，提出其中的optios
     *
     * @param requireItemNo
     * @return
     */
    private List<LoadIcuBResultBean> getCheckBoxList(String requireItemNo) {
        List<LoadIcuBResultBean> options = new ArrayList<LoadIcuBResultBean>();
        if (dictItemMap.containsKey(requireItemNo)) {
            LoadIcuBBean temp = dictItemMap.get(requireItemNo);
            options = temp.getOptions();
        }
        return options;
    }

    private void clear() {
//        formNo = "";
        etCustom.setText("");
        etCustom2.setText("");
        etCustom3.setText("");
        for (CheckBox cb1 : cbMap1.values()) {
            cb1.setChecked(false);
        }
        for (CheckBox cb2 : cbMap2.values()) {
            cb2.setChecked(false);
        }
        for (CheckBox cb3 : cbMap3.values()) {
            cb3.setChecked(false);
        }
        for (CheckBox cb4 : cbMap4.values()) {
            cb4.setChecked(false);
        }
        ICUDataMethod.reStartSaveB();
        ICUDataMethod.reStartUpdateB();
    }

    public boolean canSubmit() {
        boolean flag = false;
        for (CheckBox cb1 : cbMap1.values()) {
            if (cb1.isChecked()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            for (CheckBox cb2 : cbMap2.values()) {
                if (cb2.isChecked()) {
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            for (CheckBox cb3 : cbMap3.values()) {
                if (cb3.isChecked()) {
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            for (CheckBox cb4 : cbMap4.values()) {
                if (cb4.isChecked()) {
                    flag = true;
                    break;
                }
            }
        }
        if (!etCustom.getText().toString().equals("") || !etCustom.getText().toString().equals("") || !etCustom
                .getText().toString().equals("")) {
            flag = true;
        }
        return flag;
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_icu_hlcs);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
