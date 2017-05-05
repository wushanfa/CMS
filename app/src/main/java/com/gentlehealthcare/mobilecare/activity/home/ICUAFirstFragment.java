package com.gentlehealthcare.mobilecare.activity.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.SaveICUABean;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUABean;
import com.gentlehealthcare.mobilecare.net.bean.UpdateICUABean;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ICUAFirstFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "ICUAFirstFragment";
    private View view;
    private static EditText et_tiwen, et_xintiao, et_huxi, et_shousuoya,
            et_shuzhangya, et_sp_20_percent, et_cvp, et_yangliuliang,
            et_pingfen, et_zhenjing, et_hunmi, et_ibp;
    private Button switchpre, switchnext, save;

    private ProgressDialog progressDialog;

    /**
     * editview集合
     */
    private static List<EditText> editeLists = new ArrayList<EditText>();

    /**
     * 查询的结果
     */
    private static Map<String, SearchICUABean> searchMap = new HashMap<String, SearchICUABean>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.icu_a_template, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在操作");

        initView(view);

        initClick();

        addEditTextList();

        return view;
    }

    private void initView(View view) {
        et_tiwen = (EditText) view.findViewById(R.id.et_tiwen);
        et_xintiao = (EditText) view.findViewById(R.id.et_xintiao);
        et_huxi = (EditText) view.findViewById(R.id.et_huxi);
        et_shousuoya = (EditText) view.findViewById(R.id.et_shousuoya);
        et_shuzhangya = (EditText) view.findViewById(R.id.et_shuzhangya);
        et_sp_20_percent = (EditText) view.findViewById(R.id.et_sp_20_percent);
        et_cvp = (EditText) view.findViewById(R.id.et_cvp);
        et_yangliuliang = (EditText) view.findViewById(R.id.et_yangliuliang);
        et_pingfen = (EditText) view.findViewById(R.id.et_pingfen);
        et_zhenjing = (EditText) view.findViewById(R.id.et_zhenjing);
        et_hunmi = (EditText) view.findViewById(R.id.et_hunmi);
        switchpre = (Button) view.findViewById(R.id.switchpre);
        switchnext = (Button) view.findViewById(R.id.switchnext);
        save = (Button) view.findViewById(R.id.save);
        et_ibp = (EditText) view.findViewById(R.id.et_ibp);
    }

    private void initClick() {
        switchnext.setOnClickListener(this);
        switchpre.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switchpre:
                int num = ICUCommonMethod.getEdtiText(editeLists);
                ICUCommonMethod.perEditText(editeLists, num);
                break;
            case R.id.switchnext:
                int num2 = ICUCommonMethod.getEdtiText(editeLists);
                ICUCommonMethod.nextEditText(editeLists, num2);
                break;
            case R.id.save:
                if (ICUDataMethod.getStorageStatus(getActivity()) == GlobalConstant.SAVE_CONDITION) {
                    saveICUA();
                } else if (ICUDataMethod.getStorageStatus(getActivity()) == GlobalConstant.UPDATE_CONDITION) {
                    updateICUA();
                }
                break;

            default:
                break;
        }
    }

    private void addEditTextList() {
        editeLists = ICUCommonMethod.addEditTextIntoList(et_tiwen, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_xintiao, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_huxi, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_shousuoya, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_shuzhangya, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_sp_20_percent, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_ibp, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_cvp, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_yangliuliang, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_pingfen, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_zhenjing, GlobalConstant.FIRST_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_hunmi, GlobalConstant.FIRST_POSITION);
    }

    /**
     * 保存icua
     */
    private void saveICUA() {
        progressDialog.show();
        getSpinner();
        ICUASecondFragment.getSpinner();
        ICUAThirdFragment.getSpinner();
        SaveICUABean bean = new SaveICUABean();
        String patId = (String) ICUResourceSave.getInstance(getActivity()).get("patId");
        bean.setPatId(patId);
        String recordingTime = (String) ICUResourceSave.getInstance(getActivity()).get("recordingTime");
        if (recordingTime != null) {
            bean.setRecordingTime(recordingTime);
        } else {
            ICUCommonMethod.showDialogForRemindTime(getActivity());
            return;
        }
        bean.setLogBy(UserInfo.getUserName());
        StringBuffer itemNoSb = new StringBuffer();
        StringBuffer vitalSignsValuesSb = new StringBuffer();
        StringBuffer memoSb = new StringBuffer();
        StringBuffer patternIdSb = new StringBuffer();
        List<Map<String, Object>> localList = ICUDataMethod.getSaveA();
        for (int i = 0; i < localList.size(); i++) {
            Map<String, Object> map = localList.get(i);
            String itemNo = (String) map.get("itemNo");
            String vitalSignsValues = (String) map.get("vitalSignsValues");
            String memo = (String) map.get("memo");
            String patternId = (String) map.get("patternId");
            try {
                itemNoSb.append(itemNo != null ? itemNo : "nothing");
                patternIdSb.append(patternId != null ? java.net.URLEncoder.encode(patternId, "utf-8") :
                        "nothing");
                vitalSignsValuesSb.append(vitalSignsValues != null ? java.net.URLEncoder.encode
                        (vitalSignsValues,
                                "utf-8") : "nothing");
                memoSb.append(memo != null ? java.net.URLEncoder.encode(memo, "utf-8") : "nothing");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (i < localList.size() - 1) {
                itemNoSb.append("&itemNo=");
                vitalSignsValuesSb.append("&vitalSignsValues=");
                memoSb.append("&memo=");
                patternIdSb.append("&patternId=");
            }
        }
        bean.setItemNo(itemNoSb.toString());
        bean.setMemo(memoSb.toString());
        bean.setVitalSignsValues(vitalSignsValuesSb.toString());
        bean.setPatternId(patternIdSb.toString());
        if (itemNoSb.toString().length() > 0) {
            CCLog.i(TAG, UrlConstant.saveICUA() + bean.toString());
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.POST, UrlConstant.saveICUA() + bean.toString(), new
                    RequestCallBack<String>() {


                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            progressDialog.dismiss();
                            String result = responseInfo.result;
                            Gson gson = new Gson();
                            Type type = new TypeToken<SaveICUABean>() {
                            }.getType();
                            SaveICUABean temp = gson.fromJson(result, type);
                            if (temp.getResult().equals("success")) {
                                Toast.makeText(getActivity(), R.string.excutesuccesd, Toast.LENGTH_SHORT).show();
                                ICUCommonMethod.clearEditText();
                                ICUDataMethod.reStartSaveA();
                                searchICUA();
                            } else {
                                Toast.makeText(getActivity(),R.string.excutefail, Toast.LENGTH_SHORT).show();
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
        } else {
            ICUCommonMethod.showDialogContent(getActivity());
            progressDialog.dismiss();
        }
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 更新ICUA
     */
    private void updateICUA() {
        progressDialog.show();
        getSpinner();
        ICUASecondFragment.getSpinner();
        ICUAThirdFragment.getSpinner();
        UpdateICUABean bean = new UpdateICUABean();
        String patId = (String) ICUResourceSave.getInstance(getActivity()).get("patId");
        bean.setPatId(patId);
        bean.setLogBy(UserInfo.getUserName());
        String recordingTime = (String) ICUResourceSave.getInstance(getActivity()).get("recordingTime");
        if (recordingTime != null) {
            bean.setRecordingTime(recordingTime);
        } else {
            ICUCommonMethod.showDialogForRemindTime(getActivity());
            return;
        }
        StringBuffer vsIdSb = new StringBuffer();
        for (SearchICUABean search : searchMap.values()) {
            String vsId = search.getVsId();
            vsIdSb.append("&vsId=");
            vsIdSb.append(vsId != null ? vsId : "nothing");
        }
        StringBuffer vitalSignsValuesSb = new StringBuffer();
        StringBuffer memoSb = new StringBuffer();
        StringBuffer patternIdSb = new StringBuffer();
        StringBuffer itemNoSb = new StringBuffer();
        List<Map<String, Object>> localList = ICUDataMethod.getUpdateA();
        for (int i = 0; i < localList.size(); i++) {
            Map<String, Object> map = localList.get(i);
            String itemNo = (String) map.get("itemNo");
            String vitalSignsValues = (String) map.get("vitalSignsValues");
            String memo = (String) map.get("memo");
            String patternId = (String) map.get("patternId");
            try {
                itemNoSb.append(itemNo != null ? itemNo : "nothing");
                patternIdSb.append(patternId != null ? java.net.URLEncoder.encode(patternId, "utf-8") :
                        "nothing");
                vitalSignsValuesSb.append(vitalSignsValues != null ? java.net.URLEncoder.encode
                        (vitalSignsValues,
                                "utf-8") : "nothing");
                memoSb.append(memo != null ? java.net.URLEncoder.encode(memo, "utf-8") : "nothing");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (i < localList.size() - 1) {
                vitalSignsValuesSb.append("&vitalSignsValues=");
                memoSb.append("&memo=");
                itemNoSb.append("&itemNo=");
                patternIdSb.append("&patternId=");
            }
        }
        bean.setVsId(vsIdSb.toString());
        bean.setMemo(memoSb.toString());
        bean.setVitalSignsValues(vitalSignsValuesSb.toString());
        bean.setPatternId(patternIdSb.toString());
        bean.setItemNo(itemNoSb.toString());
        String url = UrlConstant.updateICUA() + bean.toString();
        CCLog.i(TAG, "updateIcuA>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                progressDialog.dismiss();
                Gson gson = new Gson();
                Type type = new TypeToken<UpdateICUABean>() {
                }.getType();
                UpdateICUABean temp = gson.fromJson(result, type);
                if (temp.getResult().equals("success")) {
                    Toast.makeText(getActivity(), R.string.updatesuccessful, Toast.LENGTH_SHORT).show();
                    ICUCommonMethod.clearEditText();
                    ICUDataMethod.reStartUpdateA();
                    searchICUA();
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

    /**
     * 查询ICUA
     */
    private void searchICUA() {
        progressDialog.show();
        SearchICUABean bean = new SearchICUABean();
        final String patId = (String) ICUResourceSave.getInstance(getActivity()).get("patId");
        bean.setPatId(patId);
        String recordingTime = (String) ICUResourceSave.getInstance(getActivity()).get("recordingTime");
        if (recordingTime != null) {
            bean.setRecordingTime(recordingTime);
        } else {
            ICUCommonMethod.showDialogForRemindTime(getActivity());
            return;
        }
        CCLog.i(TAG, "ICUAActivity-> " + UrlConstant.searchICUA() + bean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.searchICUA() + bean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        String result = responseInfo.result;
                        Type type = new TypeToken<Map<String, SearchICUABean>>() {
                        }.getType();
                        searchMap = gson.fromJson(result, type);
                        int insertType = 0;
                        if (searchMap == null || searchMap.size() == 0) {
                            insertType = 1;
                        } else {
                            insertType = 2;
                        }
                        if (1 == insertType) {
                            Toast.makeText(getActivity(), R.string.thistimethepatientdidnothaveICUrecords, Toast.LENGTH_SHORT).show();
                            ICUDataMethod.changeStorageStatus(getActivity(), GlobalConstant.SAVE_CONDITION);
                        } else if (2 == insertType) {
                            putIntoEtFirst(searchMap);
                            ICUASecondFragment.putIntoEtSecond(searchMap);
                            ICUAThirdFragment.putIntoEtThird(searchMap);
                            ICUASecondFragment.setSearchMap(searchMap);
                            ICUAThirdFragment.setSearchMap(searchMap);
                            ICUDataMethod.changeStorageStatus(getActivity(), GlobalConstant.UPDATE_CONDITION);
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

    /**
     * 把search到的值放入edittext中
     */
    public static void putIntoEtFirst(Map<String, SearchICUABean> searchMap) {
        ICUCommonMethod.assignmentEt(searchMap, "T", et_tiwen);
        ICUCommonMethod.assignmentEt(searchMap, "HEART_RATE", et_xintiao);
        ICUCommonMethod.assignmentEt(searchMap, "R", et_huxi);
        ICUCommonMethod.assignmentEt(searchMap, "SP", et_shousuoya);
        ICUCommonMethod.assignmentEt(searchMap, "DP", et_shuzhangya);
        ICUCommonMethod.assignmentEt(searchMap, "SPO2%", et_sp_20_percent);
        ICUCommonMethod.assignmentEt(searchMap, "IBP", et_ibp);
        ICUCommonMethod.assignmentEt(searchMap, "CVP", et_cvp);
        ICUCommonMethod.assignmentEt(searchMap, "OXYGEN_FLOW_RATE", et_yangliuliang);
        ICUCommonMethod.assignmentEt(searchMap, "PAIN_SCORE", et_pingfen);
        ICUCommonMethod.assignmentEt(searchMap, "RASS_SCORE", et_zhenjing);
        ICUCommonMethod.assignmentEt(searchMap, "GCS_SCORE", et_hunmi);

    }

    /**
     * 把值放入集合
     */
    public static void getSpinner() {
        String tiwen = et_tiwen.getText().toString();
        if (!tiwen.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("1", tiwen, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        String xintiao = et_xintiao.getText().toString();
        if (!xintiao.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("2", xintiao, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        String huxi = et_huxi.getText().toString();
        if (!huxi.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("3", huxi, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        String shousuoya = et_shousuoya.getText().toString();
        if (!shousuoya.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("4", shousuoya, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        String shuzhangya = et_shuzhangya.getText().toString();
        if (!shuzhangya.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("5", shuzhangya, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        String sp20 = et_sp_20_percent.getText().toString();
        if (!sp20.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("7", sp20, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        String ibp = et_ibp.getText().toString();
        if (!ibp.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("6", ibp, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        String cvp = et_cvp.getText().toString();
        if (!cvp.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("8", cvp, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        String yangliuliang = et_yangliuliang.getText().toString();
        if (!yangliuliang.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("9", yangliuliang, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        String pinfen = et_pingfen.getText().toString();
        if (!pinfen.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("10", pinfen, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }

        String zhenjing = et_zhenjing.getText().toString();
        if (!zhenjing.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("11", zhenjing, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }

        String hunmi = et_hunmi.getText().toString();
        if (!hunmi.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("12", hunmi, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
    }

    public static Map<String, SearchICUABean> getSearchMap() {
        return searchMap;
    }

    public static void setSearchMap(Map<String, SearchICUABean> searchMap) {
        ICUAFirstFragment.searchMap = searchMap;
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_icua);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
