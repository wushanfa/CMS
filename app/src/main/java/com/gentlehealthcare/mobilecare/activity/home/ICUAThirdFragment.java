package com.gentlehealthcare.mobilecare.activity.home;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.LoadICUAItemBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuOrderBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuOrderResultBean;
import com.gentlehealthcare.mobilecare.net.bean.SaveICUABean;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUABean;
import com.gentlehealthcare.mobilecare.net.bean.UpdateICUABean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.ICUCommonMethod;
import com.gentlehealthcare.mobilecare.tool.ICUDataMethod;
import com.gentlehealthcare.mobilecare.tool.ICUResourceSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.ICUDialogAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.ICUOrderDialogAdapter;
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

public class ICUAThirdFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "ICUAThirdFragment";
    private View view;
    /**
     * editView集合
     */
    private static List<EditText> editeLists = new ArrayList<EditText>();

    private static EditText et_zhiliao, et_yaowu, et_xueye, et_weichang, et_benruyaowu, et_out;
    private static Button btn_zhiliao;
    private static Button btn_yaowu;
    private static Button btn_xueye;
    private static Button btn_weichang;
    private static Button btn_bengruyaowu;
    private static Button btn_chuliang;
    private Button switchpre, switchnext, save;

    /**
     * 弹出相关选项
     */
    // 手动输入
    private Button order_btn_dialog;
    private LinearLayout manually_ll;
    private EditText createEv;
    // 选择医嘱相关
    private ListView order_lv;
    // 选择默认选项
    private ListView default_lv;
    // 选择器
    private RadioGroup radioGroup1;
    private RadioButton radio0, radio1, radio2;
    // 输出值
    private String createTv;
    /**
     * 默认选项
     */
    private List<LoadICUAItemBean> icuItemList = new ArrayList<LoadICUAItemBean>();
    private LoadIcuOrderResultBean orders = new LoadIcuOrderResultBean();
    private ICUDialogAdapter icuAdapter;
    private ICUOrderDialogAdapter icuOrderAdapter;

    private ProgressDialog progressDialog;

    /**
     * 查询的结果
     */
    private static Map<String, SearchICUABean> searchMap = new HashMap<String, SearchICUABean>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.icu_a_io, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在操作");

//        /** 获得从activity传递的dictionary */
//        Bundle data = getArguments();
//        orders = (LoadIcuOrderResultBean) data.getSerializable("orders");

        initView(view);

        addEditTextList();

        return view;
    }

    private void initView(View view) {
        et_zhiliao = (EditText) view.findViewById(R.id.et_zhiliao);
        et_yaowu = (EditText) view.findViewById(R.id.et_yaowu);
        et_xueye = (EditText) view.findViewById(R.id.et_xueye);
        et_weichang = (EditText) view.findViewById(R.id.et_weichang);
        et_benruyaowu = (EditText) view.findViewById(R.id.et_benruyaowu);
        et_out = (EditText) view.findViewById(R.id.et_out);
        btn_zhiliao = (Button) view.findViewById(R.id.btn_zhiliao);
        btn_zhiliao.setOnClickListener(this);
        btn_yaowu = (Button) view.findViewById(R.id.btn_yaowu);
        btn_yaowu.setOnClickListener(this);
        btn_xueye = (Button) view.findViewById(R.id.btn_xueye);
        btn_xueye.setOnClickListener(this);
        btn_weichang = (Button) view.findViewById(R.id.btn_weichang);
        btn_weichang.setOnClickListener(this);
        btn_bengruyaowu = (Button) view.findViewById(R.id.btn_bengruyaowu);
        btn_bengruyaowu.setOnClickListener(this);
        btn_chuliang = (Button) view.findViewById(R.id.btn_chuliang);
        btn_chuliang.setOnClickListener(this);
        switchpre = (Button) view.findViewById(R.id.switchpre);
        switchpre.setOnClickListener(this);
        switchnext = (Button) view.findViewById(R.id.switchnext);
        switchnext.setOnClickListener(this);
        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zhiliao:
                showEvaluationDialog(1);
                break;
            case R.id.btn_yaowu:
                showEvaluationDialog(2);
                break;
            case R.id.btn_xueye:
                showEvaluationDialog(3);
                break;
            case R.id.btn_weichang:
                showEvaluationDialog(4);
                break;
            case R.id.btn_bengruyaowu:
                showEvaluationDialog(5);
                break;
            case R.id.btn_chuliang:
                showEvaluationDialog(6);
                break;
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

    public void showEvaluationDialog(final int whichOrders) {
        orders = ICUAActivity.getOrders();
        final AlertDialog secondDialog;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.add_icu_alertdilog, null);
        order_btn_dialog = (Button) view.findViewById(R.id.order_btn_dialog);
        manually_ll = (LinearLayout) view.findViewById(R.id.manually_ll);
        createEv = (EditText) view.findViewById(R.id.createEv);
        order_lv = (ListView) view.findViewById(R.id.order_lv);
        default_lv = (ListView) view.findViewById(R.id.default_lv);
        radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
        radio0 = (RadioButton) view.findViewById(R.id.radio0);
        radio1 = (RadioButton) view.findViewById(R.id.radio1);
        radio2 = (RadioButton) view.findViewById(R.id.radio2);
        /** 关联医嘱是默认选项 */
        radio1.setChecked(true);
        Builder myBuilder = new Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        secondDialog = myBuilder.create();
        secondDialog.setTitle("请选择选项");
        secondDialog.setView(view);
        secondDialog.show();
        secondDialog.getWindow().setLayout(700, 800);
        /** 判断是那个选项点击的 */
        List<List<LoadIcuOrderBean>> subOrder = new ArrayList<List<LoadIcuOrderBean>>();
        switch (whichOrders) {
            case 1:
                subOrder = orders.getIN1();
                break;
            case 2:
                subOrder = orders.getIN2();
                break;
            case 3:
                subOrder = orders.getIN3();
                break;
            case 4:
                subOrder = orders.getIN4();
                break;
            case 5:
                subOrder = orders.getIN9();
                break;
            case 6:
                subOrder = orders.getOUT();
                break;
            default:
                break;
        }
        if (subOrder.isEmpty()) {
//            radio1.setVisibility(View.GONE);
            radio1.setClickable(false);
            order_lv.setVisibility(View.GONE);
            radio2.setChecked(true);
            manually_ll.setVisibility(View.VISIBLE);
            createEv.setVisibility(View.VISIBLE);
        }
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    /** 现在没有默认选项，保留*/
                    case R.id.radio0:
                        createEv.setVisibility(View.GONE);
                        manually_ll.setVisibility(View.GONE);
                        default_lv.setVisibility(View.VISIBLE);
                        order_lv.setVisibility(View.GONE);
                        break;
                    case R.id.radio1:
                        createEv.setVisibility(View.GONE);
                        manually_ll.setVisibility(View.GONE);
                        default_lv.setVisibility(View.GONE);
                        order_lv.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio2:
                        createEv.setVisibility(View.VISIBLE);
                        manually_ll.setVisibility(View.VISIBLE);
                        default_lv.setVisibility(View.GONE);
                        order_lv.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }
            }
        });
        order_btn_dialog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createTv = createEv.getText().toString();
                if (createTv.equals("")) {
                    Toast.makeText(getActivity(), "请先输入信息", Toast.LENGTH_SHORT).show();
                } else {
                    switch (whichOrders) {
                        case 1:
                            btn_zhiliao.setText(createTv);
                            break;
                        case 2:
                            btn_yaowu.setText(createTv);
                            break;
                        case 3:
                            btn_xueye.setText(createTv);
                            break;
                        case 4:
                            btn_weichang.setText(createTv);
                            break;
                        case 5:
                            btn_bengruyaowu.setText(createTv);
                            break;
                        case 6:
                            btn_chuliang.setText(createTv);
                            break;
                        default:
                            break;
                    }
                }
                secondDialog.dismiss();
            }
        });
        default_lv.setAdapter(icuAdapter = new ICUDialogAdapter(getActivity(), icuItemList));
        default_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                createTv = icuItemList.get(position).getItemCode();
                switch (whichOrders) {
                    case 1:
                        btn_zhiliao.setText(createTv);
                        break;
                    case 2:
                        btn_yaowu.setText(createTv);
                        break;
                    case 3:
                        btn_xueye.setText(createTv);
                        break;
                    case 4:
                        btn_weichang.setText(createTv);
                        break;
                    case 5:
                        btn_bengruyaowu.setText(createTv);
                        break;
                    case 6:
                        btn_chuliang.setText(createTv);
                        break;
                    default:
                        break;
                }
                secondDialog.dismiss();
            }
        });

        order_lv.setAdapter(icuOrderAdapter = new ICUOrderDialogAdapter(getActivity(), subOrder));
        final List<List<LoadIcuOrderBean>> finalSubOrder = subOrder;
        order_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringBuffer sb = new StringBuffer();
                for (LoadIcuOrderBean loadIcuOrderBean : finalSubOrder.get(position)) {
                    sb.append(loadIcuOrderBean.getOrderText());
                }
                createTv = sb.toString();
                switch (whichOrders) {
                    case 1:
                        btn_zhiliao.setText(createTv);
                        break;
                    case 2:
                        btn_yaowu.setText(createTv);
                        break;
                    case 3:
                        btn_xueye.setText(createTv);
                        break;
                    case 4:
                        btn_weichang.setText(createTv);
                        break;
                    case 5:
                        btn_bengruyaowu.setText(createTv);
                        break;
                    case 6:
                        btn_chuliang.setText(createTv);
                        break;
                    default:
                        break;
                }
                secondDialog.dismiss();
            }
        });
    }


    private void addEditTextList() {
        editeLists = ICUCommonMethod.addEditTextIntoList(et_zhiliao, GlobalConstant.THIRD_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_yaowu, GlobalConstant.THIRD_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_xueye, GlobalConstant.THIRD_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_weichang, GlobalConstant.THIRD_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_benruyaowu, GlobalConstant.THIRD_POSITION);
        editeLists = ICUCommonMethod.addEditTextIntoList(et_out, GlobalConstant.THIRD_POSITION);
    }

    /**
     * 保存icua
     */
    private void saveICUA() {
        progressDialog.show();
        ICUAFirstFragment.getSpinner();
        ICUASecondFragment.getSpinner();
        getSpinner();
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
                                ICUCommonMethod.clearButton(btn_zhiliao, btn_yaowu, btn_xueye, btn_weichang,
                                        btn_bengruyaowu, btn_chuliang);
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
        ICUAFirstFragment.getSpinner();
        ICUASecondFragment.getSpinner();
        getSpinner();
        UpdateICUABean bean = new UpdateICUABean();
        String patId = (String) ICUResourceSave.getInstance(getActivity()).get("patId");
        bean.setPatId(patId);
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
        bean.setVsId(vsIdSb.toString());
        bean.setItemNo(itemNoSb.toString());
        bean.setMemo(memoSb.toString());
        bean.setVitalSignsValues(vitalSignsValuesSb.toString());
        bean.setPatternId(patternIdSb.toString());
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
                            Toast.makeText(getActivity(),R.string.updatesuccessful, Toast.LENGTH_SHORT).show();
                            ICUCommonMethod.clearEditText();
                            ICUCommonMethod.clearButton(btn_zhiliao, btn_yaowu, btn_xueye, btn_weichang,
                                    btn_bengruyaowu, btn_chuliang);
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
                            Toast.makeText(getActivity(), R.string.thistimethepatientdidnothaveICUrecords, Toast.LENGTH_SHORT).show();
                            ICUDataMethod.changeStorageStatus(getActivity(), GlobalConstant.SAVE_CONDITION);
                        } else if (2 == insertType) {
                            ICUAFirstFragment.putIntoEtFirst(searchMap);
                            ICUASecondFragment.putIntoEtSecond(searchMap);
                            putIntoEtThird(searchMap);
                            ICUAFirstFragment.setSearchMap(searchMap);
                            ICUASecondFragment.setSearchMap(searchMap);
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
    public static void putIntoEtThird(Map<String, SearchICUABean> searchMap) {
        ICUCommonMethod.assignmentEt(searchMap, "IN1", et_zhiliao, btn_zhiliao);
        ICUCommonMethod.assignmentEt(searchMap, "IN2", et_yaowu, btn_yaowu);
        ICUCommonMethod.assignmentEt(searchMap, "IN3", et_xueye, btn_xueye);
        ICUCommonMethod.assignmentEt(searchMap, "IN4", et_weichang, btn_weichang);
        ICUCommonMethod.assignmentEt(searchMap, "IN9", et_benruyaowu, btn_bengruyaowu);
        ICUCommonMethod.assignmentEt(searchMap, "OUT", et_out, btn_chuliang);
    }

    /**
     * 把值放入集合
     */
    public static void getSpinner() {
        String zhiliao = et_zhiliao.getText().toString();
        String yaowu = et_yaowu.getText().toString();
        String xueye = et_xueye.getText().toString();
        String weichang = et_weichang.getText().toString();
        String benruyaowu = et_benruyaowu.getText().toString();
        String out = et_out.getText().toString();
        if (!zhiliao.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("1", zhiliao, btn_zhiliao.getText().toString(),
                    "2");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!yaowu.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("2", yaowu, btn_yaowu.getText()
                    .toString(), "2");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!xueye.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("3", xueye, btn_xueye.getText()
                    .toString(), "2");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!weichang.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("4", weichang, btn_weichang.getText()
                    .toString(), "2");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!benruyaowu.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("5", benruyaowu, btn_bengruyaowu.getText()
                    .toString(), "2");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
        if (!out.equals("")) {
            Map<String, Object> saveMap = ICUDataMethod.getSaveICUA("6", out, btn_chuliang.getText()
                    .toString(), "2");
            ICUDataMethod.addSaveA(saveMap);
            ICUDataMethod.addUpdateA(saveMap);
        }
    }

    public static Map<String, SearchICUABean> getSearchMap() {
        return searchMap;
    }

    public static void setSearchMap(Map<String, SearchICUABean> searchMap) {
        ICUAThirdFragment.searchMap = searchMap;
    }

    public static Button getBtn_zhiliao() {
        return btn_zhiliao;
    }

    public static void setBtn_zhiliao(Button btn_zhiliao) {
        ICUAThirdFragment.btn_zhiliao = btn_zhiliao;
    }

    public static Button getBtn_yaowu() {
        return btn_yaowu;
    }

    public static void setBtn_yaowu(Button btn_yaowu) {
        ICUAThirdFragment.btn_yaowu = btn_yaowu;
    }

    public static Button getBtn_xueye() {
        return btn_xueye;
    }

    public static void setBtn_xueye(Button btn_xueye) {
        ICUAThirdFragment.btn_xueye = btn_xueye;
    }

    public static Button getBtn_weichang() {
        return btn_weichang;
    }

    public static void setBtn_weichang(Button btn_weichang) {
        ICUAThirdFragment.btn_weichang = btn_weichang;
    }

    public static Button getBtn_bengruyaowu() {
        return btn_bengruyaowu;
    }

    public static void setBtn_bengruyaowu(Button btn_bengruyaowu) {
        ICUAThirdFragment.btn_bengruyaowu = btn_bengruyaowu;
    }

    public static Button getBtn_chuliang() {
        return btn_chuliang;
    }

    public static void setBtn_chuliang(Button btn_chuliang) {
        ICUAThirdFragment.btn_chuliang = btn_chuliang;
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_icu_io);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
