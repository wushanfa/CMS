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

public class TurnOverDuty extends BaseFragment implements View.OnClickListener {

    private EditText et_text;

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
     * 从sharePer中取得status
     */
    private static int status = 1;

    /**
     * 查询的结果
     */
    private static List<SearchICUBBean> searchList = new ArrayList<SearchICUBBean>();

    private int rowNo = 1;

//    private String formNo = "";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_icu_jbxj, null);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在操作");

        Bundle dataI = getArguments();
        dictItem = (List<LoadIcuBBean>) dataI.getSerializable("turnOverDuty");

        initView(view);

        page_time.setText((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));

        searchICUB();

        return view;
    }

    private void initView(View v) {
        page_time = (TextView) v.findViewById(R.id.page_time);
        page_time.setOnClickListener(this);
        et_text = (EditText) v.findViewById(R.id.et_text);
        save = (Button) v.findViewById(R.id.save);
        save.setOnClickListener(this);
    }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                myLongDate = ICUCommonMethod.getCurrentTime(myLongDate, arrive_year, arrive_month,
                        arrive_day, arrive_hour, arrive_min, datepick, timepick);
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
//        bean.setFormNo(!formNo.equals("") ? formNo : "nothing");
        bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
        bean.setWardType("Z");
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
                            ICUCommonMethod.searchListToEditText(map, et_text, "40");
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
        if ("".equals(et_text.getText().toString())) {
            ICUCommonMethod.showDialogContent(getActivity());
            progressDialog.dismiss();
        } else {
            getEdittext();
            UpdateICUBBean bean = new UpdateICUBBean();
            bean.setWardType("Z");
//            bean.setFormNo(!formNo.equals("") ? formNo : "nothing");
            bean.setFlag(ICUDataMethod.getFlag(getActivity()));
            bean.setRowNo(rowNo);
            bean.setLogBy(UserInfo.getUserName());
            bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
            bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
            Map<String, Object> map = ICUDataMethod.getUpdateB().get(0);
            String itemNo = (String) map.get("itemNo");
            String valueCode = (String) map.get("valueCode");
            String valueName = (String) map.get("valueName");
            String valueType = (String) map.get("valueType");
            String abnormalAttr = (String) map.get("abnormalAttr");
            String valueDesc = (String) map.get("valueDesc");
            try {
                valueName = valueName != null ? java.net.URLEncoder.encode(valueName, "utf-8") : "nothing";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            bean.setItemNo(itemNo != null ? itemNo : "nothing");
            bean.setValueCode(valueCode != null ? valueCode : "nothing");
            bean.setValueType(valueType != null ? valueType : "nothing");
            bean.setValueName(valueName != null ? valueName : "nothing");
            bean.setAbnormalAttr(abnormalAttr != null ? abnormalAttr : "nothing");
            bean.setValueDesc(valueDesc != null ? valueDesc : "nothing");
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
        if ("".equals(et_text.getText().toString())) {
            ICUCommonMethod.showDialogContent(getActivity());
            progressDialog.dismiss();
        } else {
            getEdittext();
            SaveICUBBean bean = new SaveICUBBean();
            bean.setLogBy(UserInfo.getUserName());
//            bean.setFormNo(formNo);
            bean.setWardType("Z");
            bean.setFlag(ICUDataMethod.getFlag(getActivity()));
            bean.setRowNo(rowNo);
            bean.setPatId((String) ICUResourceSave.getInstance(getActivity()).get("patId"));
            bean.setRecordingTime((String) ICUResourceSave.getInstance(getActivity()).get("recordingTime"));
            Map<String, Object> map = ICUDataMethod.getSaveB().get(0);
            String itemNo = (String) map.get("itemNo");
            String valueCode = (String) map.get("valueCode");
            String valueName = (String) map.get("valueName");
            String valueType = (String) map.get("valueType");
            String abnormalAttr = (String) map.get("abnormalAttr");
            String valueDesc = (String) map.get("valueDesc");
            try {
                valueName = valueName != null ? java.net.URLEncoder.encode(valueName, "utf-8") : "nothing";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            bean.setItemNo(itemNo != null ? itemNo : "nothing");
            bean.setValueCode(valueCode != null ? valueCode : "nothing");
            bean.setValueType(valueType != null ? valueType : "nothing");
            bean.setValueName(valueName != null ? valueName : "nothing");
            bean.setAbnormalAttr(abnormalAttr != null ? abnormalAttr : "nothing");
            bean.setValueDesc(valueDesc != null ? valueDesc : "nothing");
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
        et_text.setText("");
        ICUDataMethod.reStartSaveB();
        ICUDataMethod.reStartUpdateB();
    }

    private void getEdittext() {
        String text = et_text.getText().toString();
        if (!text.equals("")) {
            LoadIcuBBean bean = new LoadIcuBBean();
            Map<String, LoadIcuBBean> itemMap = new HashMap<String, LoadIcuBBean>();
            for (LoadIcuBBean item : dictItem) {
                itemMap.put(item.getItemNo(), item);
            }
            if (itemMap.containsKey("40")) {
                bean = itemMap.get("40");
            }
            Map<String, Object> icub = ICUDataMethod.getICUB(bean.getItemNo(), "nothing", text, "nothing",
                    "nothing", "nothing", "nothing");
            ICUDataMethod.addUpdateB(icub);
            ICUDataMethod.addSaveB(icub);
        }
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_icu_jbxj);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
