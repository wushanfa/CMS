package com.gentlehealthcare.mobilecare.activity.notification;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.LoadVariationDictBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadVariationDictReq;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.IntravenouslySealingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zyy on 2016/2/23.
 * 类说明：通知静脉给药封管界面
 */
@SuppressLint("ValidFragment")
public class IntravenouslySealingFra extends BaseFragment implements View.OnClickListener {
    /**
     * 主页按钮
     */
    private ImageButton imbtn_home;
    /**
     * 返回按钮
     */
    private ImageButton imbtn_back;
    /**
     * 病人姓名
     */
    private TextView tv_tras_name;
    /**
     * 病人床号
     */
    private TextView tv_tras_bed;
    /**
     * 病人信息
     */
    private SyncPatientBean patient = null;
    /**
     * 医嘱时间
     */
    private TextView tv_time;
    /**
     * 医嘱信息
     */
    private TextView tv_qd;
    /**
     * 医嘱滴速
     */
    private TextView tv_speed;
    /**
     * 医嘱静滴
     */
    private TextView tv_type;
    /**
     * 医嘱内容
     */
    private TextView tv_context;
    /**
     * 结束按钮
     */
    private Button btnSealing;
    /**
     * 当前医嘱id
     */
    private String planOlderNo = null;
    /**
     * 正常按钮
     */
    private Button btnNomal;
    /**
     * 过快按钮
     */
    private Button btnFast;
    /**
     * 过慢按钮
     */
    private Button btnSlow;
    /**
     * 异常按钮
     */
    private Button btnException;
    /**
     * 暂停按钮
     */
    private Button btnPause;
    /**
     * 异常对话框
     */
    private IntravenouslySealingDialog dialog = null;
    /**
     * 异常信息集合
     */
    private List<LoadVariationDictBean> variationDictBeanList = null;
    /**
     * 异常信息数据Bean
     */
    private LoadVariationDictBean loadVariationDictBean = null;
    /**
     * 医嘱数据集合存放一条医嘱
     */
    private List<OrderItemBean> orders = null;

    private String total = "";
    /**
     * 标识正常 or 异常 默认false异常
     */
    private Boolean isNormal = true;
    /**
     * 加载进度条
     */
    private ProgressDialog progressDialog=null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        createProgress();
        creatProgressDialog();
        createOrders();
        loadData(planOlderNo);
        createVariationDictBeanList();
        DoLoadVariationDictReq();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_intravenouslysealing, null);
        intialSource(view);
        return view;
    }

    private void creatProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage(getResources().getString(R.string.loadingdata));
    }

    private void intialSource(View view) {
        tv_tras_bed = (TextView) view.findViewById(R.id.tv_tras_bed);
        tv_tras_bed.setText(getString(R.string.bed_label) + patient.getBedLabel());
        tv_tras_name = (TextView) view.findViewById(R.id.tv_tras_name);
        tv_tras_name.setText(getString(R.string.patient_name) + patient.getName());
        // 返回&主页按钮
        imbtn_home = (ImageButton) view.findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) view.findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);
        //医嘱信息
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_qd = (TextView) view.findViewById(R.id.tv_xuedaihao);
        tv_speed = (TextView) view.findViewById(R.id.tv_xuexing);
        tv_type = (TextView) view.findViewById(R.id.tv_xueliang);
        tv_context = (TextView) view.findViewById(R.id.tv_medicineinfo);
        //观察指标
        btnNomal = (Button) view.findViewById(R.id.btn_nomal);
        btnNomal.setOnClickListener(this);
        btnFast = (Button) view.findViewById(R.id.btn_fast);
        btnFast.setOnClickListener(this);
        btnSlow = (Button) view.findViewById(R.id.btn_slow);
        btnSlow.setOnClickListener(this);
        btnException = (Button) view.findViewById(R.id.btn_exception);
        btnException.setOnClickListener(this);
        btnPause = (Button) view.findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(this);
        btnSealing = (Button) view.findViewById(R.id.btn_patrolsure);
        btnSealing.setOnClickListener(this);
    }

    private void getData() {
        patient = (SyncPatientBean) getArguments().getSerializable(GlobalConstant.KEY_PATIENT);
        planOlderNo = getArguments().getString(GlobalConstant.KEY_PLANORDERNO);
    }

    private void createVariationDictBeanList() {
        if (variationDictBeanList == null) {
            variationDictBeanList = new ArrayList<LoadVariationDictBean>();
        }
    }

    private void createOrders(){
        if(orders==null){
            orders=new ArrayList<OrderItemBean>();
        }
    }

    private void createProgress(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
        }
    }

    private void loadData(String planOlderNo) {
        CCLog.i("封管界面加载静脉给药医嘱信息>>>" + UrlConstant.loadIntrraveslyData() + UserInfo.getUserName() + "&templateId=AA-1" + "&patId=" + patient.getPatId() + "&performStatus=1" + "&planOrderNo=" + planOlderNo);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadIntrraveslyData() + UserInfo.getUserName() + "&templateId=AA-1" + "&patId=" + patient.getPatId() + "&performStatus=1" + "&planOrderNo=" + planOlderNo, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                Boolean result = false;
                try {
                    jsonObject=new JSONObject(responseInfo.result.toString());
                    result = jsonObject.getBoolean("result");
                    jsonArray = jsonObject.getJSONArray("msg");
                    if (result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<OrderItemBean>>() {
                        }.getType();
                        orders.clear();
                        orders = gson.fromJson(jsonArray.toString(), type);
                        componentAssignments();
                    } else {
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    }else{
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
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

    private void creatDialog() {
        if (dialog == null) {
            dialog = new IntravenouslySealingDialog(IntravenouslySealingFra.this, R.style.myDialogTheme);
        }
        dialog.setLoadVariationDictBeanList(variationDictBeanList);
        dialog.show();
    }

    /**
     * 点击确定按钮事件
     */
    public void DoOkButton() {
        dialog.doOkButton();
    }

    /**
     * 加载异常字典请求
     */
    private void DoLoadVariationDictReq() {
        progressDialog.show();
        RequestManager.connection(new LoadVariationDictReq(getActivity(), new IRespose<LoadVariationDictBean>() {
            @Override
            public void doResult(LoadVariationDictBean loadVariationDictBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {

                List<LoadVariationDictBean> loadVariationDictBeans = new Gson().fromJson(result, new TypeToken<List<LoadVariationDictBean>>() {
                }.getType());
                variationDictBeanList.clear();
                variationDictBeanList.addAll(loadVariationDictBeans);
                progressDialog.dismiss();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                }else{
                    toErrorAct();
                }
            }
        }, 0, true, new LoadVariationDictBean()));
    }

    public void updateStr(int index, String total) {
        setLoadVariationDictBean(variationDictBeanList.get(index));
        this.total = total;
    }

    public LoadVariationDictBean getLoadVariationDictBean() {
        return loadVariationDictBean;
    }

    public void setLoadVariationDictBean(LoadVariationDictBean loadVariationDictBean) {
        this.loadVariationDictBean = loadVariationDictBean;
    }

    private void componentAssignments() {
        if (!orders.isEmpty()) {
            if (orders.get(0).getEventStartTime().equals(null)) {
                tv_time.setText("");
            } else {
                tv_time.setText(orders.get(0).getEventStartTime());
            }
            if (orders.get(0).getFrequency().equals(null)) {
                tv_qd.setText("");
            } else {
                tv_qd.setText(orders.get(0).getFrequency());
            }
            if (orders.get(0).getSpeed().equals("")) {
                tv_speed.setText("");
            } else {
                tv_speed.setText(orders.get(0).getSpeed());
            }
            if (orders.get(0).getAdministration().equals(null)) {
                tv_type.setText("");
            } else {
                tv_type.setText(orders.get(0).getAdministration());
            }
            if (orders.get(0).getOrderText().equals(null)) {
                tv_context.setText("");
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < orders.size(); i++) {
                    stringBuffer.append(orders.get(i).getOrderText() + "\n");
                }
                tv_context.setText(stringBuffer);
            }
        } else {
            ShowToast(getResources().getString(R.string.dataexception));
        }

    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_fra_patrol);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void completeInfusion(String status, String completeDosage, String varianceCause, String varianceCauseDesc) {
        progressDialog.setMessage(getResources().getString(R.string.sealinging));
        progressDialog.show();
        CCLog.i("封管结束静脉给药>>>", UrlConstant.GetCompleteExecute(0) + "?patId=" + patient.getPatId() + "&username=" + UserInfo.getUserName() + "&planOrderNo=" + planOlderNo + "&status=" + status + "&completeDosage=" + completeDosage + "&varianceCause=" + varianceCause + "&varianceCauseDesc=" + varianceCauseDesc);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetCompleteExecute(0) + "?patId=" + patient.getPatId() + "&username=" + UserInfo.getUserName() + "&planOrderNo=" + planOlderNo + "&status=" + status + "&completeDosage=" + completeDosage + "&varianceCause=" + varianceCause + "&varianceCauseDesc=" + varianceCauseDesc, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonobject = null;
                String msg="";
                try {
                    jsonobject = new JSONObject(responseInfo.result);
                    Boolean result = false;
                    result = jsonobject.getBoolean("result");
                    progressDialog.dismiss();
                    if (result) {
                        ShowToast(getResources().getString(R.string.sealingsucessed));
                        switchFra();
                    }else{
                        msg=jsonobject.getString("msg");
                        ShowToast(msg);
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    }else{
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
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

    private void switchFra() {
        IntravenouslySealingReturnReceiptFra intravenouslySealingReturnReceiptFra = null;
        if (intravenouslySealingReturnReceiptFra == null) {
            intravenouslySealingReturnReceiptFra = new IntravenouslySealingReturnReceiptFra();
        }
        if (planOlderNo != null && patient != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
            bundle.putString(GlobalConstant.KEY_PLANORDERNO, planOlderNo);
            intravenouslySealingReturnReceiptFra.setArguments(bundle);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fl_container, intravenouslySealingReturnReceiptFra);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            // getFragmentManager().beginTransaction().replace(R.id.fl_container,intravenouslySealingReturnReceiptFra).commit();

        } else {
            ShowToast(getResources().getString(R.string.dataexception));
        }

    }

    private String subStringItemContent(String itemContent) {
        String str[] = itemContent.split(" ");
        if (str[1].equals(null)) {
            return "其他原因";
        }
        return str[1];
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imbtn_back:
                Intent intent = new Intent();
                intent.setClass(getActivity(), Notification2Activity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.imbtn_home:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), HomeAct.class);
                startActivity(intent2);
                getActivity().finish();
                break;
            case R.id.btn_patrolsure:
                if (isNormal) {
                    completeInfusion("1", null, null, null);
                } else {
                    LoadVariationDictBean loadVariationDictBean = null;
                    if (loadVariationDictBean == null) {
                        loadVariationDictBean = new LoadVariationDictBean();
                    }
                    loadVariationDictBean = getLoadVariationDictBean();
                    completeInfusion("-1", total, loadVariationDictBean.getItemCode(), subStringItemContent(loadVariationDictBean.getItemContent().toString()));
                }
                break;
            case R.id.btn_nomal:
                isNormal = true;
                btnNomal.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                btnNomal.setTextColor(getResources().getColor(R.color.white));
                btnSlow.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnSlow.setTextColor(getResources().getColor(R.color.blue_normal));
                btnFast.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnFast.setTextColor(getResources().getColor(R.color.blue_normal));
                btnPause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnPause.setTextColor(getResources().getColor(R.color.red));
                btnException.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnException.setTextColor(getResources().getColor(R.color.red));
                break;
            case R.id.btn_fast:
                isNormal = true;
                btnNomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnNomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btnSlow.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnSlow.setTextColor(getResources().getColor(R.color.blue_normal));
                btnException.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnException.setTextColor(getResources().getColor(R.color.red));
                btnPause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnPause.setTextColor(getResources().getColor(R.color.red));
                btnFast.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                btnFast.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.btn_slow:
                isNormal = true;
                btnNomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnNomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btnFast.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnFast.setTextColor(getResources().getColor(R.color.blue_normal));
                btnException.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnException.setTextColor(getResources().getColor(R.color.red));
                btnPause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnPause.setTextColor(getResources().getColor(R.color.red));
                btnSlow.setBackground(getResources().getDrawable(R.drawable.select_button_blue));
                btnSlow.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.btn_exception:
                isNormal = false;
                btnNomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnNomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btnSlow.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnSlow.setTextColor(getResources().getColor(R.color.blue_normal));
                btnFast.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnFast.setTextColor(getResources().getColor(R.color.blue_normal));
                btnPause.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnPause.setTextColor(getResources().getColor(R.color.red));
                btnException.setBackground(getResources().getDrawable(R.drawable.skin_btn_red));
                btnException.setTextColor(getResources().getColor(R.color.white));
                creatDialog();
                break;
            case R.id.btn_pause:
                isNormal = false;
                btnNomal.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnNomal.setTextColor(getResources().getColor(R.color.blue_normal));
                btnSlow.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnSlow.setTextColor(getResources().getColor(R.color.blue_normal));
                btnFast.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnFast.setTextColor(getResources().getColor(R.color.blue_normal));
                btnException.setBackground(getResources().getDrawable(R.drawable.skin_btn_frame));
                btnException.setTextColor(getResources().getColor(R.color.red));
                btnPause.setBackground(getResources().getDrawable(R.drawable.skin_btn_red));
                btnPause.setTextColor(getResources().getColor(R.color.white));
                creatDialog();
                break;
            default:
                break;
        }
    }
}
