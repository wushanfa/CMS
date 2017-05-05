package com.gentlehealthcare.mobilecare.activity.notification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
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
 * 类说明：通知静脉给药封管回执界面
 */
public class IntravenouslySealingReturnReceiptFra extends BaseFragment implements View.OnClickListener {
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
     * 静脉给药结束时间
     */
    private TextView tvIntravenouslyEndTime;
    /**
     * 静脉给药开始时间
     */
    private TextView tvIntravenouslyStartTime;
    /**
     * 当前医嘱id
     */
    private String planOlderNo = null;
    /**
     * 医嘱数据集合存放一条医嘱
     */
    private List<OrderItemBean> orders = null;

    /**
     * 延迟2s
     */
    private final int SPLASH_DISPLAY_LENGHT = 1000 *4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        createOrders();
        loadData(planOlderNo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_intravenouslyendreturn, null);
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
        //开始&结束时间
        tvIntravenouslyEndTime = (TextView) view.findViewById(R.id.tv_intravenouslyendtime);
        tvIntravenouslyStartTime = (TextView) view.findViewById(R.id.tv_intravenouslystarttime);
    }
    private void createOrders(){
        if(orders==null){
            orders=new ArrayList<OrderItemBean>();
        }
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
//            if(orders.get(0).getEventStartTime().equals(null)){
//                tvIntravenouslyStartTime.setText(orders.get(0).getEventStartTime());
//            }else{
//                tvIntravenouslyStartTime.setText(orders.get(0).getPlanStartTime());
//            }
//            if(orders.get(0).getEventEndTime().equals(null)){
//                tvIntravenouslyEndTime.setText(orders.get(0).getEventEndTime());
//            }else{
//                tvIntravenouslyEndTime.setText(DateTool.getCurrDateTime());
//            }
        } else {
            ShowToast(getResources().getString(R.string.dataexception));
        }

    }

    private void getData() {
        patient = (SyncPatientBean) getArguments().getSerializable(GlobalConstant.KEY_PATIENT);
        planOlderNo = getArguments().getString(GlobalConstant.KEY_PLANORDERNO);
    }

    private void loadData(String planOlderNo) {
        CCLog.i("回执界面加载静脉给药医嘱信息>>>" + UrlConstant.loadIntrraveslyData() + UserInfo.getUserName() + "&templateId=AA-1" + "&patId=" + patient.getPatId() + "&performStatus=9" + "&planOrderNo=" + planOlderNo);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadIntrraveslyData() + UserInfo.getUserName() + "&templateId=AA-1" + "&patId=" + patient.getPatId() + "&performStatus=9" + "&planOrderNo=" + planOlderNo, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                Boolean result = false;
                try {
                    jsonObject=new JSONObject(responseInfo.result);
                    result = jsonObject.getBoolean("result");
                    jsonArray = jsonObject.getJSONArray("msg");
                    if (result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<OrderItemBean>>() {
                        }.getType();
                        orders.clear();
                        orders = gson.fromJson(jsonArray.toString(), type);
                        CCLog.i("http"+">>>"+orders.size());
                        componentAssignments();
                        returnReceipt();
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

    private void returnReceipt() {
        CCLog.i("returnReceipt()"+">>>"+orders.size());
        if(orders.get(1).getEventStartTime().equals(null)){
           ShowToast(getResources().getString(R.string.dataexception));
        }else{
            tvIntravenouslyStartTime.setText(orders.get(0).getEventStartTime());
        }
        if(orders.get(0).getEventEndTime().equals(null)){
            ShowToast(getResources().getString(R.string.dataexception));
        }else{
            tvIntravenouslyEndTime.setText(DateTool.getCurrDateTime());
        }
        delayedJump();
    }

    private void delayedJump() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Notification2Activity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_intrareturn);
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
            default:
                break;
        }
    }
}
