package com.gentlehealthcare.mobilecare.activity.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.LoadInspectionBean;
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

public class ICUASecondFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "ICUASecondFragment";
    private View view;
    private Button switchpre, switchnext, save;
    private static EditText et_shenzhi, et_zuotongkong_zhijing, et_zuotongkong_fanying, et_youtongkong_zhijing,
            et_youtongkong_fanying, et_mode, et_vt, et_fi02, et_f, et_peep, et_jiachashuzhi, et_hulijilu;

    private static Spinner spn_jiachaxiangmu;
    private static ArrayAdapter<String> adapter;
    /**
     * editview集合
     */
    private static List<EditText> editeLists = new ArrayList<EditText>();

    private ProgressDialog progressDialog;

    /**
     * 查询的结果
     */
    private static Map<String, SearchICUABean> searchMap = new HashMap<String, SearchICUABean>();

    private static String inspectMenu = "";
    private static String inspectNum = "";

    private static List<LoadInspectionBean> inspection = new ArrayList<LoadInspectionBean>();
    private static int resultInspectionNum = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.icu_a_other, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在操作");

        loadInspection();

        initView(view);

        addEditTextList();

        return view;
    }

    private void initView(View view) {
        switchpre = (Button) view.findViewById(R.id.switchpre);
        switchpre.setOnClickListener(this);
        switchnext = (Button) view.findViewById(R.id.switchnext);
        switchnext.setOnClickListener(this);
        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(this);
        et_shenzhi = (EditText) view.findViewById(R.id.et_shenzhi);
        et_zuotongkong_zhijing = (EditText) view.findViewById(R.id.et_zuotongkong_zhijing);
        et_zuotongkong_fanying = (EditText) view.findViewById(R.id.et_zuotongkong_fanying);
        et_youtongkong_zhijing = (EditText) view.findViewById(R.id.et_youtongkong_zhijing);
        et_youtongkong_fanying = (EditText) view.findViewById(R.id.et_youtongkong_fanying);
        et_mode = (EditText) view.findViewById(R.id.et_mode);
        et_vt = (EditText) view.findViewById(R.id.et_vt);
        et_fi02 = (EditText) view.findViewById(R.id.et_fi02);
        et_f = (EditText) view.findViewById(R.id.et_f);
        et_peep = (EditText) view.findViewById(R.id.et_peep);
        spn_jiachaxiangmu = (Spinner) view.findViewById(R.id.spn_jiachaxiangmu);
        spn_jiachaxiangmu.setOnItemSelectedListener(new resultInspection());
        et_jiachashuzhi = (EditText) view.findViewById(R.id.et_jiachashuzhi);
        et_hulijilu = (EditText) view.findViewById(R.id.et_hulijilu);
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
        editeLists = ICUCommonMethod.addEditTextIntoList(et_shenzhi, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_zuotongkong_zhijing, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_zuotongkong_fanying, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_youtongkong_zhijing, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_youtongkong_fanying, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_mode, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_vt, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_fi02, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_f, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_peep, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_jiachashuzhi, GlobalConstant.SECOND_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_hulijilu, GlobalConstant.SECOND_POSITION);
    }

    /**
     * 加载检查项目字典表
     */
    private void loadInspection() {
        CCLog.i(TAG, UrlConstant.loadInspection());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadInspection(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Gson gson = new Gson();
                Type type = new TypeToken<List<LoadInspectionBean>>() {
                }.getType();
                inspection = gson.fromJson(result, type);

                String[] items = new String[3];
                for (int i = 0; i < inspection.size(); i++) {
                    items[i] = inspection.get(i).getItemName();
                }
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
                adapter.setDropDownViewResource(R.layout.drop_down_item);
                spn_jiachaxiangmu.setAdapter(adapter);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                CCLog.e(TAG, msg);
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

    /**
     * 保存icua
     */
    private void saveICUA() {
        progressDialog.show();
        ICUAFirstFragment.getSpinner();
        getSpinner();
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
        if (!inspectMenu.equals("") && inspectNum.equals("")) {
            ICUCommonMethod.showDialogForRemindInspect(getActivity());
            return;
        }
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
                patternIdSb.append(patternId != null ? java.net.URLEncoder.encode(patternId, "utf-8") : "nothing");
                vitalSignsValuesSb.append(vitalSignsValues != null ? java.net.URLEncoder.encode(vitalSignsValues,
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
                            String result = responseInfo.result;
                            progressDialog.dismiss();
                            Gson gson = new Gson();
                            Type type = new TypeToken<SaveICUABean>() {
                            }.getType();
                            SaveICUABean temp = gson.fromJson(result, type);
                            if (temp.getResult().equals("success")) {
                                Toast.makeText(getActivity(), R.string.savesuccessed, Toast.LENGTH_SHORT).show();
                                ICUCommonMethod.clearEditText();
                                ICUDataMethod.reStartSaveA();
                                searchICUA();
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
        } else {
            ICUCommonMethod.showDialogContent(getActivity());
            progressDialog.dismiss();
        }
    }

    /**
     * 更新ICUA
     */
    private void updateICUA() {
        progressDialog.show();
        ICUAFirstFragment.getSpinner();
        getSpinner();
        ICUAThirdFragment.getSpinner();
        UpdateICUABean bean = new UpdateICUABean();
        String patId = (String) ICUResourceSave.getInstance(getActivity()).get("patId");
        bean.setPatId(patId);
        bean.setLogBy(UserInfo.getUserName());
        if (!inspectMenu.equals("") && inspectNum.equals("")) {
            ICUCommonMethod.showDialogForRemindInspect(getActivity());
            return;
        }
        String recordingTime = (String) ICUResourceSave.getInstance(getActivity()).get
                ("recordingTime");
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
                patternIdSb.append(patternId != null ? java.net.URLEncoder.encode(patternId, "utf-8") : "nothing");
                vitalSignsValuesSb.append(vitalSignsValues != null ? java.net.URLEncoder.encode(vitalSignsValues,
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
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.updateICUA() + bean.toString(), new
                RequestCallBack<String>() {

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
        String patId = (String) ICUResourceSave.getInstance(getActivity()).get("patId");
        bean.setPatId(patId);
        String recordingTime = (String) ICUResourceSave.getInstance(getActivity()).get
                ("recordingTime");
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
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
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
                            Toast.makeText(getActivity(),R.string.thistimethepatientdidnothaveICUrecords, Toast
                                    .LENGTH_SHORT).show();
                            ICUDataMethod.changeStorageStatus(getActivity(), GlobalConstant
                                    .SAVE_CONDITION);
                        } else if (2 == insertType) {
                            ICUAFirstFragment.putIntoEtFirst(searchMap);
                            putIntoEtSecond(searchMap);
                            ICUAThirdFragment.putIntoEtThird(searchMap);
                            ICUAFirstFragment.setSearchMap(searchMap);
                            ICUAThirdFragment.setSearchMap(searchMap);
                            ICUDataMethod.changeStorageStatus(getActivity(), GlobalConstant
                                    .UPDATE_CONDITION);
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
    public static void putIntoEtSecond(Map<String, SearchICUABean> searchMap) {
        ICUCommonMethod.assignmentEt(searchMap, "CONSCIOUSNESS", et_shenzhi);
        ICUCommonMethod.assignmentEt(searchMap, "PUPIL_LEFT", et_zuotongkong_zhijing);
        ICUCommonMethod.assignmentEt(searchMap, "PUPIL_LEFT_RESPONSE", et_zuotongkong_fanying);
        ICUCommonMethod.assignmentEt(searchMap, "PUPIL_RIGHT", et_youtongkong_zhijing);
        ICUCommonMethod.assignmentEt(searchMap, "PUPIL_RIGHT_RESPONSE", et_youtongkong_fanying);
        ICUCommonMethod.assignmentEt(searchMap, "BM_MODE", et_mode);
        ICUCommonMethod.assignmentEt(searchMap, "BM_VT", et_vt);
        ICUCommonMethod.assignmentEt(searchMap, "BM_FiO2", et_fi02);
        ICUCommonMethod.assignmentEt(searchMap, "BM_F", et_f);
        ICUCommonMethod.assignmentEt(searchMap, "BM_PEEP", et_peep);
        if (searchMap.containsKey("LAB_ITEM")) {
            SearchICUABean bean = (SearchICUABean) searchMap.get("LAB_ITEM");
            spn_jiachaxiangmu.setSelection(Integer.parseInt(bean.getValueType()), true);
            et_jiachashuzhi.setText(bean.getVitalSignsValues());
        }
        ICUCommonMethod.assignmentEt(searchMap, "WARD_LOG", et_hulijilu);
    }

    /**
     * 把值放入集合
     */
    public static void getSpinner() {
        String shenzi = et_shenzhi.getText().toString();
        String zuotongkong_zhijing = et_zuotongkong_zhijing.getText().toString();
        String zuotongkong_fanying = et_zuotongkong_fanying.getText().toString();
        String youtongkong_zhijing = et_youtongkong_zhijing.getText().toString();
        String youtongkong_fanying = et_youtongkong_fanying.getText().toString();
        String mode = et_mode.getText().toString();
        String vt = et_vt.getText().toString();
        String fi02 = et_fi02.getText().toString();
        String f = et_f.getText().toString();
        String peep = et_peep.getText().toString();
        String jiachaDataCode = inspection.get(resultInspectionNum).getDataCode();
        String jiachaMemo = inspection.get(resultInspectionNum).getItemCode();
        String jiachaVitalSignsValues = et_jiachashuzhi.getText().toString();
        String hulijilu = et_hulijilu.getText().toString();
        final Map<String, Object> inspectMap = new HashMap<String, Object>();
        if (!shenzi.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("13", shenzi, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!zuotongkong_zhijing.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("14", zuotongkong_zhijing, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!zuotongkong_fanying.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("15", zuotongkong_fanying, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!youtongkong_zhijing.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("16", youtongkong_zhijing, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!youtongkong_fanying.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("17", youtongkong_fanying,
                    "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!mode.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("18", mode, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!vt.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("19", vt, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!fi02.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("20", fi02, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!f.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("21", f, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!peep.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("22", peep, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!jiachaVitalSignsValues.equals("")) {
            inspectMap.put("itemNo", "23");
            inspectMap.put("vitalSignsValues", jiachaVitalSignsValues);
            inspectMap.put("patternId", "1");
            inspectMenu = jiachaDataCode;
            inspectMap.put("memo", jiachaMemo);
            ICUDataMethod.addSaveA(inspectMap);
            ICUDataMethod.addUpdateA(inspectMap);
            inspectNum = jiachaVitalSignsValues;
        }
        if (!hulijilu.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("99", hulijilu, "nothing", "1");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
    }

    public static Map<String, SearchICUABean> getSearchMap() {
        return searchMap;
    }

    public static void setSearchMap(Map<String, SearchICUABean> searchMap) {
        ICUASecondFragment.searchMap = searchMap;
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_icu_other);
        SupportDisplay.resetAllChildViewParam(root);
    }

    public class resultInspection implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            resultInspectionNum = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
