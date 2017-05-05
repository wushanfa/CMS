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

public class Orthopedics extends BaseFragment implements View.OnClickListener, AdapterView
        .OnItemSelectedListener {
    /**
     *
     */
    private EditText custom1, custom2;

    /**
     * spinner
     */
    private Spinner dressing, feel, artery, peripheral, exerciseOrthopedics, dressing2, feel2, artery2,
            peripheral2, exerciseOrthopedics2;
    private ArrayAdapter<String> dictAdapter;
    private List<Spinner> spnList = new ArrayList<Spinner>();

    private LinearLayout detail_ll_gushangkeqingkuang, detail_ll_gushangkeqingkuang2, gushangkeqingkuang,
            gushangkeqingkuang2;
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
     * 返回每个spinner下item对应list中位置
     */
    private int dressingPoint, feelPoint, arteryPoint, peripheralPoint, exerciseOrthopedicsPoint,
            dressing2Point, feel2Point, artery2Point, peripheral2Point, exerciseOrthopedics2Point = 0;
    private ProgressDialog progressDialog;

    /**
     * 从sharePer中取得status
     */
    private static int status = 1;

    private int rowNo = 1;

//    private String formNo = "";
    /**
     * 查询的结果
     */
    private static List<SearchICUBBean> searchList = new ArrayList<SearchICUBBean>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_icu_gskqk, null);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在操作");

        Bundle dataI = getArguments();
        dictItem = (List<LoadIcuBBean>) dataI.getSerializable("orthopedics");
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
        detail_ll_gushangkeqingkuang = (LinearLayout) v.findViewById(R.id.detail_ll_gushangkeqingkuang);
        detail_ll_gushangkeqingkuang2 = (LinearLayout) v.findViewById(R.id.detail_ll_gushangkeqingkuang2);
        gushangkeqingkuang = (LinearLayout) v.findViewById(R.id.gushangkeqingkuang);
        gushangkeqingkuang2 = (LinearLayout) v.findViewById(R.id.gushangkeqingkuang2);
        detail_ll_gushangkeqingkuang2.setOnClickListener(this);
        detail_ll_gushangkeqingkuang.setOnClickListener(this);
        detail_iv = (TextView) v.findViewById(R.id.detail_iv);
        detail_iv2 = (TextView) v.findViewById(R.id.detail_iv2);
        detail_tv = (TextView) v.findViewById(R.id.detail_tv);
        detail_tv2 = (TextView) v.findViewById(R.id.detail_tv2);
        page_time = (TextView) v.findViewById(R.id.page_time);
        page_time.setOnClickListener(this);
        dressing = (Spinner) v.findViewById(R.id.spn_dressing);
        feel = (Spinner) v.findViewById(R.id.spn_feel);
        artery = (Spinner) v.findViewById(R.id.spn_artery);
        peripheral = (Spinner) v.findViewById(R.id.spn_peripheral);
        exerciseOrthopedics = (Spinner) v.findViewById(R.id.spn_exercise_orthopedics);
        dressing2 = (Spinner) v.findViewById(R.id.spn_dressing2);
        feel2 = (Spinner) v.findViewById(R.id.spn_feel2);
        artery2 = (Spinner) v.findViewById(R.id.spn_artery2);
        peripheral2 = (Spinner) v.findViewById(R.id.spn_peripheral2);
        exerciseOrthopedics2 = (Spinner) v.findViewById(R.id.spn_exercise_orthopedics2);
        custom1 = (EditText) v.findViewById(R.id.et_custom_orthopedics1);
        custom2 = (EditText) v.findViewById(R.id.et_custom_orthopedics2);
        save = (Button) v.findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    private void initAdapter() {
        dressingPoint = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, dressing, dictItem, "17");
        feelPoint = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, feel, dictItem, "18");
        arteryPoint = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, artery, dictItem, "19");
        peripheralPoint = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, peripheral, dictItem,
                "20");
        exerciseOrthopedicsPoint = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter,
                exerciseOrthopedics, dictItem, "21");
        dressing2Point = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, dressing2, dictItem,
                "23");
        feel2Point = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, feel2, dictItem, "24");
        artery2Point = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, artery2, dictItem, "25");
        peripheral2Point = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, peripheral2,
                dictItem, "26");
        exerciseOrthopedics2Point = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter,
                exerciseOrthopedics2, dictItem, "27");
    }

    private void initOnItemSelectedListener() {
        dressing.setOnItemSelectedListener(this);
        feel.setOnItemSelectedListener(this);
        artery.setOnItemSelectedListener(this);
        peripheral.setOnItemSelectedListener(this);
        exerciseOrthopedics.setOnItemSelectedListener(this);
        dressing2.setOnItemSelectedListener(this);
        feel2.setOnItemSelectedListener(this);
        artery2.setOnItemSelectedListener(this);
        peripheral2.setOnItemSelectedListener(this);
        exerciseOrthopedics2.setOnItemSelectedListener(this);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_ll_gushangkeqingkuang:
                ICUCommonMethod.clickSwitch(detail_ll_gushangkeqingkuang, gushangkeqingkuang, detail_tv,
                        detail_iv);
                break;
            case R.id.detail_ll_gushangkeqingkuang2:
                ICUCommonMethod.clickSwitch(detail_ll_gushangkeqingkuang2, gushangkeqingkuang2, detail_tv2,
                        detail_iv2);
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
     * spinner选择事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spn_dressing:
                ICUCommonMethod.getSpinnerResult(dictItem, dressingPoint, position);
                break;
            case R.id.spn_feel:
                ICUCommonMethod.getSpinnerResult(dictItem, feelPoint, position);
                break;
            case R.id.spn_artery:
                ICUCommonMethod.getSpinnerResult(dictItem, arteryPoint, position);
                break;
            case R.id.spn_peripheral:
                ICUCommonMethod.getSpinnerResult(dictItem, peripheralPoint, position);
                break;
            case R.id.spn_exercise_orthopedics:
                ICUCommonMethod.getSpinnerResult(dictItem, exerciseOrthopedicsPoint, position);
                break;
            case R.id.spn_dressing2:
                ICUCommonMethod.getSpinnerResult(dictItem, dressing2Point, position);
                break;
            case R.id.spn_feel2:
                ICUCommonMethod.getSpinnerResult(dictItem, feel2Point, position);
                break;
            case R.id.spn_artery2:
                ICUCommonMethod.getSpinnerResult(dictItem, artery2Point, position);
                break;
            case R.id.spn_peripheral2:
                ICUCommonMethod.getSpinnerResult(dictItem, peripheral2Point, position);
                break;
            case R.id.spn_exercise_orthopedics2:
                ICUCommonMethod.getSpinnerResult(dictItem, exerciseOrthopedics2Point, position);
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
     * 把spinner放入集合里，方便保存和显示
     */
    private void putBooleanIntoList() {
        spnList.add(dressing);
        spnList.add(feel);
        spnList.add(artery);
        spnList.add(peripheral);
        spnList.add(exerciseOrthopedics);
        spnList.add(dressing2);
        spnList.add(feel2);
        spnList.add(artery2);
        spnList.add(peripheral2);
        spnList.add(exerciseOrthopedics2);
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
        bean.setWardType("G");
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
                            Map<String, SearchICUBBean> map = new HashMap<String, SearchICUBBean>();
                            for (SearchICUBBean bean : searchList) {
                                map.put(bean.getItemNo(), bean);
                            }
                            ICUCommonMethod.searchListToSpinner(map, dictItem, dressingPoint, dressing,
                                    "17");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, feelPoint, feel, "18");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, arteryPoint, artery, "19");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, peripheralPoint, peripheral,
                                    "20");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, exerciseOrthopedicsPoint,
                                    exerciseOrthopedics, "21");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, dressing2Point, dressing2,
                                    "23");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, feel2Point, feel2, "24");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, artery2Point, artery2, "25");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, peripheral2Point,
                                    peripheral2, "26");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, exerciseOrthopedics2Point,
                                    exerciseOrthopedics2, "27");
                            ICUCommonMethod.searchListToEditText(map, custom1, "22");
                            ICUCommonMethod.searchListToEditText(map, custom2, "28");

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
            bean.setWardType("G");
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
//            bean.setFormNo(formNo);
            bean.setWardType("G");
            bean.setFlag(ICUDataMethod.getFlag(getActivity()));
            bean.setLogBy(UserInfo.getUserName());
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
//        formNo="";
        custom1.setText("");
        custom2.setText("");
        ICUCommonMethod.clearSpinner(dressing, map, "17");
        ICUCommonMethod.clearSpinner(feel, map, "18");
        ICUCommonMethod.clearSpinner(artery, map, "19");
        ICUCommonMethod.clearSpinner(peripheral, map, "20");
        ICUCommonMethod.clearSpinner(exerciseOrthopedics, map, "21");
        ICUCommonMethod.clearSpinner(dressing2, map, "23");
        ICUCommonMethod.clearSpinner(feel2, map, "24");
        ICUCommonMethod.clearSpinner(artery2, map, "25");
        ICUCommonMethod.clearSpinner(peripheral2, map, "26");
        ICUCommonMethod.clearSpinner(exerciseOrthopedics2, map, "27");
        ICUDataMethod.reStartUpdateB();
        ICUDataMethod.reStartSaveB();
    }

    private void getEdittext() {
        String c1 = custom1.getText().toString();
        if (!c1.equals("")) {
            LoadIcuBBean bean = new LoadIcuBBean();
            Map<String, LoadIcuBBean> itemMap = new HashMap<String, LoadIcuBBean>();
            for (LoadIcuBBean item : dictItem) {
                itemMap.put(item.getItemNo(), item);
            }
            if (itemMap.containsKey("22")) {
                bean = itemMap.get("22");
            }
            Map<String, Object> icub = ICUDataMethod.getICUB(bean.getItemNo(), "nothing", c1, "nothing",
                    "nothing", "nothing", "nothing");
            ICUDataMethod.addUpdateB(icub);
            ICUDataMethod.addSaveB(icub);
        }
        String c2 = custom2.getText().toString();
        if (!c2.equals("")) {
            LoadIcuBBean bean = new LoadIcuBBean();
            Map<String, LoadIcuBBean> itemMap = new HashMap<String, LoadIcuBBean>();
            for (LoadIcuBBean item : dictItem) {
                itemMap.put(item.getItemNo(), item);
            }
            if (itemMap.containsKey("28")) {
                bean = itemMap.get("28");
            }
            Map<String, Object> icub = ICUDataMethod.getICUB(bean.getItemNo(), "nothing", c2, "nothing",
                    "nothing", "nothing", "nothing");
            ICUDataMethod.addUpdateB(icub);
            ICUDataMethod.addSaveB(icub);
        }
    }

    public boolean canSubmit() {
        boolean flag = false;
        Map<String, LoadIcuBBean> map = new HashMap<String, LoadIcuBBean>();
        for (LoadIcuBBean bean : dictItem) {
            map.put(bean.getItemNo(), bean);
        }
        flag =ICUCommonMethod.canSubmitSpinner(dressing, map, "17", flag);
        flag =ICUCommonMethod.canSubmitSpinner(feel, map, "18", flag);
        flag =ICUCommonMethod.canSubmitSpinner(artery, map, "19", flag);
        flag =ICUCommonMethod.canSubmitSpinner(peripheral, map, "20", flag);
        flag =ICUCommonMethod.canSubmitSpinner(exerciseOrthopedics, map, "21", flag);
        flag =ICUCommonMethod.canSubmitSpinner(dressing2, map, "23", flag);
        flag =ICUCommonMethod.canSubmitSpinner(feel2, map, "24", flag);
        flag =ICUCommonMethod.canSubmitSpinner(artery2, map, "25", flag);
        flag =ICUCommonMethod.canSubmitSpinner(peripheral2, map, "26", flag);
        flag =ICUCommonMethod.canSubmitSpinner(exerciseOrthopedics2, map, "27", flag);
        if (!"".equals(custom1.getText().toString()) && !flag) {
            flag = true;
        }
        if (!"".equals(custom2.getText().toString()) && !flag) {
            flag = true;
        }
        return flag;
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_icu_gskqk);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
