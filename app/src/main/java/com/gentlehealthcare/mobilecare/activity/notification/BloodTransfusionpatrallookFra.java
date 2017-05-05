package com.gentlehealthcare.mobilecare.activity.notification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
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
 * 类说明：通知输血巡视观察界面
 */
@SuppressLint("ValidFragment")
public class BloodTransfusionpatrallookFra extends BaseFragment implements View.OnClickListener {
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
    private Button btnLook;
    /**
     * 当前医嘱id
     */
    private String bloodId = null;
    /**
     * 医嘱数据集合存放一条医嘱
     */
    private List<BloodProductBean2> orders = null;
    /**
     * 输血开始时间
     */
    private TextView tvBloodStartTime;
    /**
     * 输血巡视时间
     */
    private TextView tvBloodPatrolTime;

    private String playOrderNo=null;

    private String itemNo=null;

    private String applyNo=null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        createOrders();
        loadData(bloodId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_bloodpatral, null);
        intialSource(view);
        return view;
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
        btnLook = (Button) view.findViewById(R.id.btn_patrolsure);
        btnLook.setOnClickListener(this);
        tvBloodStartTime = (TextView) view.findViewById(R.id.tv_tras_time1);
        tvBloodPatrolTime = (TextView) view.findViewById(R.id.tv_tras_time2);

    }

    private void componentAssignments() {

        if (!orders.isEmpty()) {
            if (orders.get(0).getTransDate().equals(null)) {
                tv_time.setText("");
            } else {
                tv_time.setText(orders.get(0).getTransDate());
            }
            if (orders.get(0).getBloodId().equals(null)) {
                tv_qd.setText("");
            } else {
                tv_qd.setText(orders.get(0).getBloodId());
            }
            if (orders.get(0).getBloodCapacity().equals("")) {
                tv_speed.setText("");
            } else {
                tv_speed.setText(orders.get(0).getBloodCapacity() + orders.get(0).getUnit());
            }
            if (orders.get(0).getBloodGroup().equals(null)) {
                tv_type.setText("");
            } else {
                tv_type.setText(orders.get(0).getBloodGroup());
            }
            if (orders.get(0).getBloodTypeName().equals(null)) {
                tv_context.setText("");
            } else {
                tv_context.setText(orders.get(0).getBloodTypeName());
            }
        } else {
            ShowToast(getResources().getString(R.string.dataexception));
        }
        playOrderNo=orders.get(0).getPlanOrderNo();
        itemNo=orders.get(0).getItemNo();
        applyNo=orders.get(0).getApplyNo();
        tvBloodPatrolTime.setText("巡视时间:" + DateTool.getCurrDateTime());
        tvBloodStartTime.setText("开始时间:" + orders.get(0).getTransDate());
    }

    private void createOrders() {
        if (orders == null) {
            orders = new ArrayList<BloodProductBean2>();
        }
    }

    private void getData() {
        patient = (SyncPatientBean) getArguments().getSerializable(GlobalConstant.KEY_PATIENT);
        bloodId = getArguments().getString(GlobalConstant.KEY_BLOODID);
    }

    private void loadData(String bloodId) {
        CCLog.i("输血巡视界面加载医嘱信息>>>", UrlConstant.loadBloodNoticeDetail() + UserInfo.getUserName() + "&bloodId=" + bloodId + "&patId=" + patient.getPatId());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadBloodNoticeDetail() + UserInfo.getUserName() + "&bloodId=" + bloodId + "&patId=" + patient.getPatId(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                Boolean result = false;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    result = jsonObject.getBoolean("result");
                    if (result) {
                        jsonArray = jsonObject.getJSONArray("msg");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<BloodProductBean2>>() {
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

    private void intialFragment() {
        BloodTransfusionpatralTprSpeedLookFra bloodTransfusionpatralTprSpeedLookFra = null;
        if (bloodTransfusionpatralTprSpeedLookFra == null) {
            bloodTransfusionpatralTprSpeedLookFra = new BloodTransfusionpatralTprSpeedLookFra();
        }
        if (bloodId != null && patient != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
            bundle.putString(GlobalConstant.KEY_BLOODID, bloodId);
            bundle.putString(GlobalConstant.KEY_PLANORDERNO,playOrderNo);
            bundle.putString(GlobalConstant.KEY_ITEMNO,itemNo);
            bundle.putString(GlobalConstant.KEY_APPLYNO,applyNo);
            bloodTransfusionpatralTprSpeedLookFra.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fl_container, bloodTransfusionpatralTprSpeedLookFra).commit();
        } else {
            ShowToast(getString(R.string.dataexception));
        }

    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_fra_bloodpatrol);
        SupportDisplay.resetAllChildViewParam(root);
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
                intialFragment();
                break;
            default:
                break;
        }
    }
}
