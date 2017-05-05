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

public class Spine extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText et_other;
    /**
     * spinner
     */
    private Spinner leftUpFeel, leftUpSport, leftDownFeel, leftDownSport, rightUpFeel, rightUpSport,
            rightDownFeel, rightDownSport, spineDressing, spineFeelOther;
    private ArrayAdapter<String> dictAdapter;
    private List<Spinner> spnList = new ArrayList<Spinner>();

    //left upper limb
    private LinearLayout detail_l_u_limb_ll, thumbnail_l_u_limb_ll;
    private TextView detail_l_u_limb_tv, detail_l_u_limb_iv;
    //left lower limb
    private LinearLayout thumbnail_l_d_limb_ll, detail_l_d_limb_ll;
    private TextView detail_l_d_limb_tv, detail_l_d_limb_iv;
    //right upper limb
    private LinearLayout thumbnail_r_u_limb, detail_r_u_limb_ll;
    private TextView detail_r_u_limb_tv, detail_r_u_limb_iv;
    //right lower limb
    private LinearLayout thumbnail_r_d_limb_ll, detail_r_d_limb_ll;
    private TextView detail_r_d_limb_tv, detail_r_d_limb_iv;
    //blank
    private LinearLayout thumbnail_blank_ll, detail_blank_ll;
    private TextView detail_blank_tv, detail_blank_iv;
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
    private int leftUpFeelPosition, leftUpSportPosition, leftDownFeelPosition, leftDownSportPosition,
            rightUpFeelPosition, rightUpSportPosition, rightDownFeelPosition, rightDownSportPosition,
            spineDressingPosition, spineFeelOtherPosition = 0;

    private ProgressDialog progressDialog;

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
        View view = inflater.inflate(R.layout.fra_icu_jzwkqk, null);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在操作");

        Bundle dataI = getArguments();
        dictItem = (List<LoadIcuBBean>) dataI.getSerializable("spine");
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
        detail_l_u_limb_ll = (LinearLayout) v.findViewById(R.id.detail_l_u_limb_ll);
        detail_l_u_limb_ll.setOnClickListener(this);
        thumbnail_l_u_limb_ll = (LinearLayout) v.findViewById(R.id.thumbnail_l_u_limb_ll);
        thumbnail_l_d_limb_ll = (LinearLayout) v.findViewById(R.id.thumbnail_l_d_limb_ll);
        detail_l_d_limb_ll = (LinearLayout) v.findViewById(R.id.detail_l_d_limb_ll);
        detail_l_d_limb_ll.setOnClickListener(this);
        thumbnail_r_u_limb = (LinearLayout) v.findViewById(R.id.thumbnail_r_u_limb);
        detail_r_u_limb_ll = (LinearLayout) v.findViewById(R.id.detail_r_u_limb_ll);
        detail_r_u_limb_ll.setOnClickListener(this);
        thumbnail_r_d_limb_ll = (LinearLayout) v.findViewById(R.id.thumbnail_r_d_limb_ll);
        detail_r_d_limb_ll = (LinearLayout) v.findViewById(R.id.detail_r_d_limb_ll);
        detail_r_d_limb_ll.setOnClickListener(this);
        thumbnail_blank_ll = (LinearLayout) v.findViewById(R.id.thumbnail_blank_ll);
        detail_blank_ll = (LinearLayout) v.findViewById(R.id.detail_blank_ll);
        detail_blank_ll.setOnClickListener(this);
        detail_l_u_limb_tv = (TextView) v.findViewById(R.id.detail_l_u_limb_tv);
        detail_l_u_limb_iv = (TextView) v.findViewById(R.id.detail_l_u_limb_iv);
        detail_l_d_limb_tv = (TextView) v.findViewById(R.id.detail_l_d_limb_tv);
        detail_l_d_limb_iv = (TextView) v.findViewById(R.id.detail_l_d_limb_iv);
        detail_r_u_limb_tv = (TextView) v.findViewById(R.id.detail_r_u_limb_tv);
        detail_r_u_limb_iv = (TextView) v.findViewById(R.id.detail_r_u_limb_iv);
        detail_r_d_limb_tv = (TextView) v.findViewById(R.id.detail_r_d_limb_tv);
        detail_r_d_limb_iv = (TextView) v.findViewById(R.id.detail_r_d_limb_iv);
        detail_blank_tv = (TextView) v.findViewById(R.id.detail_blank_tv);
        detail_blank_iv = (TextView) v.findViewById(R.id.detail_blank_iv);
        page_time = (TextView) v.findViewById(R.id.page_time);
        page_time.setOnClickListener(this);
        leftUpFeel = (Spinner) v.findViewById(R.id.spn_left_up_feel);
        leftUpSport = (Spinner) v.findViewById(R.id.spn_left_up_sport);
        leftDownFeel = (Spinner) v.findViewById(R.id.spn_left_down_feel);
        leftDownSport = (Spinner) v.findViewById(R.id.spn_left_down_sport);
        rightUpFeel = (Spinner) v.findViewById(R.id.spn_right_up_feel);
        rightUpSport = (Spinner) v.findViewById(R.id.spn_right_up_sport);
        rightDownFeel = (Spinner) v.findViewById(R.id.spn_right_down_feel);
        rightDownSport = (Spinner) v.findViewById(R.id.spn_right_down_sport);
        spineDressing = (Spinner) v.findViewById(R.id.spn_spine_dressing);
        spineFeelOther = (Spinner) v.findViewById(R.id.spn_spine_feel_other);
        save = (Button) v.findViewById(R.id.save);
        save.setOnClickListener(this);
        et_other = (EditText) v.findViewById(R.id.et_other);
    }

    private void initOnItemSelectedListener() {
        leftUpFeel.setOnItemSelectedListener(this);
        leftUpSport.setOnItemSelectedListener(this);
        leftDownFeel.setOnItemSelectedListener(this);
        leftDownSport.setOnItemSelectedListener(this);
        rightUpFeel.setOnItemSelectedListener(this);
        rightUpSport.setOnItemSelectedListener(this);
        rightDownFeel.setOnItemSelectedListener(this);
        rightDownSport.setOnItemSelectedListener(this);
        spineDressing.setOnItemSelectedListener(this);
        spineFeelOther.setOnItemSelectedListener(this);
    }

    private void initAdapter() {
        leftUpFeelPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, leftUpFeel,
                dictItem, "29");
        leftUpSportPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, leftUpSport,
                dictItem, "30");
        leftDownFeelPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, leftDownFeel,
                dictItem, "31");
        leftDownSportPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, leftDownSport,
                dictItem, "32");
        rightUpFeelPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, rightUpFeel,
                dictItem, "33");
        rightUpSportPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, rightUpSport,
                dictItem, "34");
        rightDownFeelPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, rightDownFeel,
                dictItem, "35");
        rightDownSportPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, rightDownSport,
                dictItem, "36");
        spineDressingPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, spineDressing,
                dictItem, "37");
        spineFeelOtherPosition = ICUCommonMethod.bindingAdapter(getActivity(), dictAdapter, spineFeelOther,
                dictItem, "38");
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.detail_l_u_limb_ll:
                ICUCommonMethod.clickSwitch(detail_l_u_limb_ll, thumbnail_l_u_limb_ll, detail_l_u_limb_tv,
                        detail_l_u_limb_iv);
                break;
            case R.id.detail_l_d_limb_ll:
                ICUCommonMethod.clickSwitch(detail_l_d_limb_ll, thumbnail_l_d_limb_ll, detail_l_d_limb_tv,
                        detail_l_d_limb_iv);
                break;
            case R.id.detail_r_u_limb_ll:
                ICUCommonMethod.clickSwitch(detail_r_u_limb_ll, thumbnail_r_u_limb, detail_r_u_limb_tv,
                        detail_r_u_limb_iv);
                break;
            case R.id.detail_r_d_limb_ll:
                ICUCommonMethod.clickSwitch(detail_r_d_limb_ll, thumbnail_r_d_limb_ll, detail_r_d_limb_tv,
                        detail_r_d_limb_iv);
                break;
            case R.id.detail_blank_ll:
                ICUCommonMethod.clickSwitch(detail_blank_ll, thumbnail_blank_ll, detail_blank_tv,
                        detail_blank_iv);
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
            case R.id.spn_left_up_feel:
                ICUCommonMethod.getSpinnerResult(dictItem, leftUpFeelPosition, position);
                break;
            case R.id.spn_left_up_sport:
                ICUCommonMethod.getSpinnerResult(dictItem, leftUpSportPosition, position);
                break;
            case R.id.spn_left_down_feel:
                ICUCommonMethod.getSpinnerResult(dictItem, leftDownFeelPosition, position);
                break;
            case R.id.spn_left_down_sport:
                ICUCommonMethod.getSpinnerResult(dictItem, leftDownSportPosition, position);
                break;
            case R.id.spn_right_up_feel:
                ICUCommonMethod.getSpinnerResult(dictItem, rightUpFeelPosition, position);
                break;
            case R.id.spn_right_up_sport:
                ICUCommonMethod.getSpinnerResult(dictItem, rightUpSportPosition, position);
                break;
            case R.id.spn_right_down_feel:
                ICUCommonMethod.getSpinnerResult(dictItem, rightDownFeelPosition, position);
                break;
            case R.id.spn_right_down_sport:
                ICUCommonMethod.getSpinnerResult(dictItem, rightDownSportPosition, position);
                break;
            case R.id.spn_spine_dressing:
                ICUCommonMethod.getSpinnerResult(dictItem, spineDressingPosition, position);
                break;
            case R.id.spn_spine_feel_other:
                ICUCommonMethod.getSpinnerResult(dictItem, spineFeelOtherPosition, position);
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
        spnList.add(leftUpFeel);
        spnList.add(leftUpSport);
        spnList.add(leftDownFeel);
        spnList.add(leftDownSport);
        spnList.add(rightUpFeel);
        spnList.add(rightUpSport);
        spnList.add(rightDownFeel);
        spnList.add(rightDownSport);
        spnList.add(spineDressing);
        spnList.add(spineFeelOther);
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
        bean.setWardType("F");
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
                            ICUCommonMethod.searchListToSpinner(map, dictItem, leftUpFeelPosition,
                                    leftUpFeel, "29");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, leftUpSportPosition,
                                    leftUpSport, "30");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, leftDownFeelPosition,
                                    leftDownFeel, "31");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, leftDownSportPosition,
                                    leftDownSport, "32");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, rightUpFeelPosition,
                                    rightUpFeel, "33");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, rightUpSportPosition,
                                    rightUpSport, "34");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, rightDownFeelPosition,
                                    rightDownFeel, "35");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, rightDownSportPosition,
                                    rightDownSport, "36");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, spineDressingPosition,
                                    spineDressing, "37");
                            ICUCommonMethod.searchListToSpinner(map, dictItem, spineFeelOtherPosition,
                                    spineFeelOther, "38");
                            ICUCommonMethod.searchListToEditText(map, et_other, "39");

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
            bean.setWardType("F");
//            bean.setFormNo(!formNo.equals("") ? formNo : "nothing");
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
            bean.setWardType("F");
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

    private void getEdittext() {
        String other = et_other.getText().toString();
        if (!other.equals("")) {
            LoadIcuBBean bean = new LoadIcuBBean();
            if (map.containsKey("39")) {
                bean = map.get("39");
            }
            Map<String, Object> icub = ICUDataMethod.getICUB(bean.getItemNo(), "nothing", other, "nothing",
                    "nothing", "nothing", "nothing");
            ICUDataMethod.addUpdateB(icub);
            ICUDataMethod.addSaveB(icub);
        }
    }

    private void clear() {
//        formNo = "";
        et_other.setText("");
        ICUCommonMethod.clearSpinner(leftUpFeel, map, "29");
        ICUCommonMethod.clearSpinner(leftUpSport, map, "30");
        ICUCommonMethod.clearSpinner(leftDownFeel, map, "31");
        ICUCommonMethod.clearSpinner(leftDownSport, map, "32");
        ICUCommonMethod.clearSpinner(rightUpFeel, map, "33");
        ICUCommonMethod.clearSpinner(rightUpSport, map, "34");
        ICUCommonMethod.clearSpinner(rightDownFeel, map, "35");
        ICUCommonMethod.clearSpinner(rightDownSport, map, "36");
        ICUCommonMethod.clearSpinner(spineDressing, map, "37");
        ICUCommonMethod.clearSpinner(spineFeelOther, map, "38");
        ICUDataMethod.reStartUpdateB();
        ICUDataMethod.reStartSaveB();
    }

    public boolean canSubmit() {
        boolean flag = false;
        boolean flag1 = ICUCommonMethod.canSubmitSpinnerSpine(leftUpFeel, map, "29");
        boolean flag2 = ICUCommonMethod.canSubmitSpinnerSpine(leftUpSport, map, "30");
        boolean flag3 = ICUCommonMethod.canSubmitSpinnerSpine(leftDownFeel, map, "31");
        boolean flag4 = ICUCommonMethod.canSubmitSpinnerSpine(leftDownSport, map, "32");
        boolean flag5 = ICUCommonMethod.canSubmitSpinnerSpine(rightUpFeel, map, "33");
        boolean flag6 = ICUCommonMethod.canSubmitSpinnerSpine(rightUpSport, map, "34");
        boolean flag7 = ICUCommonMethod.canSubmitSpinnerSpine(rightDownFeel, map, "35");
        boolean flag8 = ICUCommonMethod.canSubmitSpinnerSpine(rightDownSport, map, "36");
        boolean flag9 = ICUCommonMethod.canSubmitSpinnerSpine(spineDressing, map, "37");
        boolean flag10 = ICUCommonMethod.canSubmitSpinnerSpine(spineFeelOther, map, "38");
        boolean flag11 = "".equals(et_other.getText().toString()) ? false : true;
        if (flag1 || flag2 || flag3 || flag4 || flag5 || flag6 || flag7 || flag8 || flag9 || flag10 || flag11) {
            flag = true;
        }
        return flag;
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_icu_jzwkqk);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
