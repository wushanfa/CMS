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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class NursingPlan extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "NursingPlan";
    /**
     * spinner
     */
    private Spinner level, guardianship, oral_nursing, decubitus, catheter, beatRear, oxygen,
            cvp_measuring, inhale, eat, prevention, exercise, applicator, acupuncturePoint, pressure;
    private ArrayAdapter<String> dictAdapter;
    private List<Spinner> spnList = new ArrayList<Spinner>();

    private LinearLayout detail_ll, changgui_ll, detail_ll_teshu, teshu_ll;
    private TextView detail_tv, detail_tv2, detail_iv, detail_iv2;
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

    /**
     * 查询的结果
     */
    private static List<SearchICUBBean> searchList = new ArrayList<SearchICUBBean>();

    private static int status = 1;

    /**
     * 返回每个spinner下item对应list中位置
     */
    private int levelPosition, guardianshipPosition, oral_nursingPosition, decubitusPosition,
            catheterPosition, beatRearPosition, oxygenPosition,
            cvp_measuringPosition, inhalePosition, eatPosition, preventionPosition, exercisePosition,
            applicatorPosition, acupuncturePointPosition, pressurePosition = 0;

    private ProgressDialog progressDialog;

    private EditText edtYll;

    private int rowNo = 1;

//    private String formNo = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_icu_hljh, null);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在操作");

        Bundle dataI = getArguments();
        dictItem = (List<LoadIcuBBean>) dataI.getSerializable("plan");
        for (LoadIcuBBean bean : dictItem) {
            map.put(bean.getItemNo(), bean);
        }

        initView(view);

        initAdapter();

        initOnItemSelectedListener();

        putBooleanIntoList();//把spinner放入list中

        page_time.setText((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));

        searchICUB();

        return view;
    }

    private void initView(View v) {
        detail_ll = (LinearLayout) v.findViewById(R.id.detail_ll);
        detail_ll.setOnClickListener(this);
        changgui_ll = (LinearLayout) v.findViewById(R.id.changgui_ll);
        detail_ll_teshu = (LinearLayout) v.findViewById(R.id.detail_ll_teshu);
        detail_ll_teshu.setOnClickListener(this);
        teshu_ll = (LinearLayout) v.findViewById(R.id.teshu_ll);
        detail_tv = (TextView) v.findViewById(R.id.detail_tv);
        detail_tv2 = (TextView) v.findViewById(R.id.detail_tv2);
        detail_iv = (TextView) v.findViewById(R.id.detail_iv);
        detail_iv2 = (TextView) v.findViewById(R.id.detail_iv2);
        page_time = (TextView) v.findViewById(R.id.page_time);
        page_time.setOnClickListener(this);
        level = (Spinner) v.findViewById(R.id.spn_level);
        guardianship = (Spinner) v.findViewById(R.id.spn_guardianship);
        oral_nursing = (Spinner) v.findViewById(R.id.spn_oral_nursing);
        decubitus = (Spinner) v.findViewById(R.id.spn_decubitus);
        catheter = (Spinner) v.findViewById(R.id.spn_catheter);
        beatRear = (Spinner) v.findViewById(R.id.spn_beatRear);
        oxygen = (Spinner) v.findViewById(R.id.spn_oxygen);
        cvp_measuring = (Spinner) v.findViewById(R.id.spn_cvp_measuring);
        inhale = (Spinner) v.findViewById(R.id.spn_inhale);
        eat = (Spinner) v.findViewById(R.id.spn_eat);
        prevention = (Spinner) v.findViewById(R.id.spn_prevention);
        exercise = (Spinner) v.findViewById(R.id.spn_exercise);
        applicator = (Spinner) v.findViewById(R.id.spn_applicator);
        acupuncturePoint = (Spinner) v.findViewById(R.id.spn_acupuncture_point);
        pressure = (Spinner) v.findViewById(R.id.spn_pressure);
        edtYll = (EditText) v.findViewById(R.id.edt_yll);
        save = (Button) v.findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    private void initAdapter() {
        levelPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, level, dictItem, "41");
        guardianshipPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, guardianship,
                dictItem, "42");
        oral_nursingPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, oral_nursing,
                dictItem, "43");
        decubitusPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, decubitus, dictItem,
                "44");
        catheterPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, catheter, dictItem,
                "45");
        beatRearPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, beatRear, dictItem,
                "46");
        oxygenPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, oxygen, dictItem, "47");
        cvp_measuringPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, cvp_measuring,
                dictItem,
                "49");
        inhalePosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, inhale, dictItem, "50");
        eatPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, eat, dictItem, "51");
        preventionPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, prevention,
                dictItem, "52");
        exercisePosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, exercise, dictItem,
                "53");
        applicatorPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, applicator,
                dictItem, "54");
        acupuncturePointPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter,
                acupuncturePoint,
                dictItem, "55");
        pressurePosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, pressure, dictItem,
                "56");
    }

    private void initOnItemSelectedListener() {
        level.setOnItemSelectedListener(this);
        guardianship.setOnItemSelectedListener(this);
        oral_nursing.setOnItemSelectedListener(this);
        decubitus.setOnItemSelectedListener(this);
        catheter.setOnItemSelectedListener(this);
        beatRear.setOnItemSelectedListener(this);
        oxygen.setOnItemSelectedListener(this);
        cvp_measuring.setOnItemSelectedListener(this);
        inhale.setOnItemSelectedListener(this);
        eat.setOnItemSelectedListener(this);
        prevention.setOnItemSelectedListener(this);
        exercise.setOnItemSelectedListener(this);
        applicator.setOnItemSelectedListener(this);
        acupuncturePoint.setOnItemSelectedListener(this);
        pressure.setOnItemSelectedListener(this);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_ll:
                ICUCommonMethod.clickSwitch(detail_ll, changgui_ll, detail_tv, detail_iv);
                break;
            case R.id.detail_ll_teshu:
                ICUCommonMethod.clickSwitch(detail_ll_teshu, teshu_ll, detail_tv2, detail_iv2);
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
     * spinner的选择事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spn_level:
                ICUCommonMethod.getSpinnerResult(dictItem, levelPosition, position);
                break;
            case R.id.spn_guardianship:
                ICUCommonMethod.getSpinnerResult(dictItem, guardianshipPosition, position);
                break;
            case R.id.spn_oral_nursing:
                ICUCommonMethod.getSpinnerResult(dictItem, oral_nursingPosition, position);
                break;
            case R.id.spn_decubitus:
                ICUCommonMethod.getSpinnerResult(dictItem, decubitusPosition, position);
                break;
            case R.id.spn_catheter:
                ICUCommonMethod.getSpinnerResult(dictItem, catheterPosition, position);
                break;
            case R.id.spn_beatRear:
                ICUCommonMethod.getSpinnerResult(dictItem, beatRearPosition, position);
                break;
            case R.id.spn_oxygen:
                ICUCommonMethod.getSpinnerResult(dictItem, oxygenPosition, position);
                break;
            case R.id.spn_cvp_measuring:
                ICUCommonMethod.getSpinnerResult(dictItem, cvp_measuringPosition, position);
                break;
            case R.id.spn_inhale:
                ICUCommonMethod.getSpinnerResult(dictItem, inhalePosition, position);
                break;
            case R.id.spn_eat:
                ICUCommonMethod.getSpinnerResult(dictItem, eatPosition, position);
                break;
            case R.id.spn_prevention:
                ICUCommonMethod.getSpinnerResult(dictItem, preventionPosition, position);
                break;
            case R.id.spn_exercise:
                ICUCommonMethod.getSpinnerResult(dictItem, exercisePosition, position);
                break;
            case R.id.spn_applicator:
                ICUCommonMethod.getSpinnerResult(dictItem, applicatorPosition, position);
                break;
            case R.id.spn_acupuncture_point:
                ICUCommonMethod.getSpinnerResult(dictItem, acupuncturePointPosition, position);
                break;
            case R.id.spn_pressure:
                ICUCommonMethod.getSpinnerResult(dictItem, pressurePosition, position);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
     * 把spinner放入集合里，方便保存和显示
     */
    private void putBooleanIntoList() {
        spnList.add(level);
        spnList.add(guardianship);
        spnList.add(oral_nursing);
        spnList.add(decubitus);
        spnList.add(catheter);
        spnList.add(beatRear);
        spnList.add(oxygen);
        spnList.add(cvp_measuring);
        spnList.add(inhale);
        spnList.add(eat);
        spnList.add(prevention);
        spnList.add(exercise);
        spnList.add(applicator);
        spnList.add(acupuncturePoint);
        spnList.add(pressure);
    }

    /**
     * 查询icu b
     */
    private void searchICUB() {
        progressDialog.show();
        SearchICUBBean bean = new SearchICUBBean();
        bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
        bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
        bean.setWardType("H");
        CCLog.i(TAG, UrlConstant.searchICUBNursingPlan() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.searchICUBNursingPlan() + bean.toString(), new
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
                            ICUCommonMethod.searchListToSpinner(map, dictItem, levelPosition, level, "41");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, guardianshipPosition,
                                    guardianship, "42");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, oral_nursingPosition,
                                    oral_nursing, "43");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, decubitusPosition, decubitus,
                                    "44");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, catheterPosition, catheter,
                                    "45");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, beatRearPosition, beatRear,
                                    "46");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, oxygenPosition, oxygen,
                                    "47");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, cvp_measuringPosition,
                                    cvp_measuring,
                                    "49");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, inhalePosition, inhale,
                                    "50");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, eatPosition, eat, "51");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, preventionPosition,
                                    prevention, "52");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, exercisePosition, exercise,
                                    "53");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, applicatorPosition,
                                    applicator, "54");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, acupuncturePointPosition,
                                    acupuncturePoint, "55");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, pressurePosition, pressure,
                                    "56");
                            if (map.containsKey("48")) {
                                edtYll.setText(map.get("48").getValueName());
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
            getEdittext();
            UpdateICUBBean bean = new UpdateICUBBean();
            bean.setWardType("H");
            bean.setRowNo(rowNo);
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
                    valueCodeSb.append(valueCode != null ? java.net.URLEncoder.encode(valueCode, "utf-8") :
                            "nothing");
                    valueNameSb.append(valueName != null ? java.net.URLEncoder.encode(valueName, "utf-8") :
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
            CCLog.i(TAG, UrlConstant.updateICUBNursingPlan() + bean.toString());
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, UrlConstant.updateICUBNursingPlan() + bean.toString(), new
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
            getEdittext();
            SaveICUBBean bean = new SaveICUBBean();
            bean.setLogBy(UserInfo.getUserName());
            bean.setWardType("H");
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
                    valueCodeSb.append(valueCode != null ? java.net.URLEncoder.encode(valueCode, "utf-8") :
                            "nothing");
                    valueNameSb.append(valueName != null ? java.net.URLEncoder.encode(valueName, "utf-8") :
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
            CCLog.i(TAG,UrlConstant.saveICUBNursingPlan()+bean.toString());
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, UrlConstant.saveICUBNursingPlan() + bean.toString(), new
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

    private void clear() {
//        formNo = "";
        edtYll.setText("");
        ICUCommonMethod.clearSpinner(level, map, "41");
        ICUCommonMethod.clearSpinner(guardianship, map, "42");
        ICUCommonMethod.clearSpinner(oral_nursing, map, "43");
        ICUCommonMethod.clearSpinner(decubitus, map, "44");
        ICUCommonMethod.clearSpinner(catheter, map, "45");
        ICUCommonMethod.clearSpinner(beatRear, map, "46");
        ICUCommonMethod.clearSpinner(oxygen, map, "47");
        ICUCommonMethod.clearSpinner(cvp_measuring, map, "49");
        ICUCommonMethod.clearSpinner(inhale, map, "50");
        ICUCommonMethod.clearSpinner(eat, map, "51");
        ICUCommonMethod.clearSpinner(prevention, map, "52");
        ICUCommonMethod.clearSpinner(exercise, map, "53");
        ICUCommonMethod.clearSpinner(applicator, map, "54");
        ICUCommonMethod.clearSpinner(acupuncturePoint, map, "55");
        ICUCommonMethod.clearSpinner(pressure, map, "56");
        ICUDataMethod.reStartUpdateB();
        ICUDataMethod.reStartSaveB();
    }

    private void getEdittext() {
        String Yll = edtYll.getText().toString();
        if (!Yll.equals("")) {
            LoadIcuBBean bean = new LoadIcuBBean();
            if (map.containsKey("48")) {
                bean = map.get("48");
            }
            Map<String, Object> icub = ICUDataMethod.getICUB(bean.getItemNo(), "nothing", Yll, "nothing",
                    "nothing", "nothing", "nothing");
            ICUDataMethod.addUpdateB(icub);
            ICUDataMethod.addSaveB(icub);
        }
    }

    public boolean canSubmit() {
        boolean flag = false;
        flag = ICUCommonMethod.canSubmitSpinner(level, map, "41", flag);
        flag = ICUCommonMethod.canSubmitSpinner(guardianship, map, "42", flag);
        flag = ICUCommonMethod.canSubmitSpinner(oral_nursing, map, "43", flag);
        flag = ICUCommonMethod.canSubmitSpinner(decubitus, map, "44", flag);
        flag = ICUCommonMethod.canSubmitSpinner(catheter, map, "45", flag);
        flag = ICUCommonMethod.canSubmitSpinner(beatRear, map, "46", flag);
        flag = ICUCommonMethod.canSubmitSpinner(oxygen, map, "47", flag);
        flag = ICUCommonMethod.canSubmitSpinner(cvp_measuring, map, "49", flag);
        flag = ICUCommonMethod.canSubmitSpinner(inhale, map, "50", flag);
        flag = ICUCommonMethod.canSubmitSpinner(eat, map, "51", flag);
        flag = ICUCommonMethod.canSubmitSpinner(prevention, map, "52", flag);
        flag = ICUCommonMethod.canSubmitSpinner(exercise, map, "53", flag);
        flag = ICUCommonMethod.canSubmitSpinner(applicator, map, "54", flag);
        flag = ICUCommonMethod.canSubmitSpinner(acupuncturePoint, map, "55", flag);
        flag = ICUCommonMethod.canSubmitSpinner(pressure, map, "56", flag);
        if (!edtYll.getText().toString().equals("") && !flag) {
            flag = true;
        }
        return flag;
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_fra_icu_hlljh);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
