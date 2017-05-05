package com.gentlehealthcare.mobilecare.activity.notification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.NotificationAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author 赵洋洋 E-mail:395670690@qq.com
 * @version 创建时间：2015年8月27日 下午5:02:18
 * @类说明 通知提示页面
 */
public class Notification2Activity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Notification2Activity";
    /**
     * 创建者姓名
     */
    private TextView tv_tip_creat_name_;
    /**
     * 创建时间
     */
    private TextView tv_tip_creat_time_;
    /**
     * 病人姓名
     */
    private TextView tv_tip_pat_name;
    /**
     * 病人病床号
     */
    private TextView tv_tip_pat_num;
    /**
     * 提示消息
     */
    private TextView tv_tip_pat_msg_;
    /**
     * 不处理按钮
     */
    private Button bt_tip_dont;
    /**
     * 处理按钮
     */
    private Button bt_tip_do;
    /**
     * 消息ID
     */
    private String noticeID = "";
    /**
     * 通知列表
     */
    private PullToRefreshListView notificationList = null;
    /**
     * 通知数据集合
     */
    private List<TipBean> notificationDatas = null;
    /**
     * 通知列表适配器
     */
    private NotificationAdapter adapter = null;
    /**
     * 通知列表适配器
     */
    private ImageButton inbtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HidnGestWindow(false);
        setContentView(R.layout.activity_notification);
        intialSource();
        //addData();
        adapter = new NotificationAdapter(this);
        notificationList.setAdapter(adapter);
        loadNotificationData();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_notification);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 界面数据填充
     */
    private void addData() {
        // 获取service传递的信息
        Intent intent = getIntent();
        // 消息ID
        noticeID = intent.getStringExtra("noticeId");
        // 消息创建时间
        String createTime = intent.getStringExtra("createTime");
        // 消息创建者
        String createUser = intent.getStringExtra("createUser");
        // 病人床号
        String bedLabel = intent.getStringExtra("bedLabel");
        // 病人姓名
        String patName = intent.getStringExtra("patName");
        // 消息体
        String messageContent = intent.getStringExtra("messageContent");

        tv_tip_creat_name_.setText(createUser);
        tv_tip_creat_time_.setText(createTime);
        tv_tip_pat_name.setText(patName);
        tv_tip_pat_num.setText(bedLabel);
        tv_tip_pat_msg_.setText(messageContent);
    }

    /**
     * 初始化界面组件
     */
    public void intialSource() {
//		tv_tip_creat_name_ = (TextView) findViewById(R.id.tv_tip_creat_name_);
//		tv_tip_creat_time_ = (TextView) findViewById(R.id.tv_tip_creat_time_);
//		tv_tip_pat_name = (TextView) findViewById(R.id.tv_tip_pat_name);
//		tv_tip_pat_num = (TextView) findViewById(R.id.tv_tip_pat_num);
//		tv_tip_pat_msg_ = (TextView) findViewById(R.id.tv_tip_pat_msg_);
//		bt_tip_dont = (Button) findViewById(R.id.bt_tip_dont);
//		bt_tip_dont.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				accessNetCallBack("-1");
//				Notification2Activity.this.finish();
//			}
//		});
//		bt_tip_do = (Button) findViewById(R.id.bt_tip_do);
//		bt_tip_do.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// 做处理
//				accessNetCallBack("2");
//				tv_tip_pat_msg_.setText("");
//				Notification2Activity.this.finish();
//				overridePendingTransition(0, R.anim.slide_out_right);
//			}
//		});

        inbtnBack = (ImageButton) findViewById(R.id.imbtn_back);
        inbtnBack.setOnClickListener(this);

        notificationList = (PullToRefreshListView) findViewById(R.id.lv_nfs);
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TipBean tipBean = (TipBean) adapter.getItem(i - 1);
                Intent intent = new Intent();
                SyncPatientBean patient = new SyncPatientBean();
                patient.setPatId(tipBean.getPatId());
                patient.setPatCode(tipBean.getPatCode());
                patient.setName(tipBean.getPatName());
                patient.setBedLabel(tipBean.getBedLabel());
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                if (tipBean.getTemplateId().equals("AA-5")) {
//                    BloodProductBean2 bloodProductBean2 = new BloodProductBean2();
//                    bloodProductBean2.setPlanOrderNo(tipBean.getRelatedPlanOrderNo());
//                    bloodProductBean2.setApplyNo(tipBean.getApplyNo());
//                    bloodProductBean2.setItemNo(tipBean.getItemNo() + "");
//                    bloodProductBean2.setBloodId(tipBean.getBloodId());
//                    bloodProductBean2.setBloodCapacity(tipBean.getBloodCapacity() + "");
//                    bloodProductBean2.setUnit(tipBean.getUnit());
//                    bloodProductBean2.setBloodGroup(tipBean.getBloodGroup());
//                    bundle.putSerializable(GlobalConstant.KEY_BLOODPRODUCTBEAN2, bloodProductBean2);
//                    intent.setClass(Notification2Activity.this, TransfusionActivity.class);
//                    intent.putExtras(bundle);
//                    intent.putExtra(GlobalConstant.KEY_NOTIFICATION2TRANSFUSION, GlobalConstant
// .VALUE_NOTIFICATION2TRANSFUSION);
//                    startActivity(intent);
//                    finish();

                    intent.putExtras(bundle);
                    intent.putExtra(GlobalConstant.KEY_TEMPLATEID, "AA-5");
                    intent.putExtra(GlobalConstant.KEY_BLOODID, tipBean.getBloodId());
                    intent.putExtra(GlobalConstant.KEY_NOTICECLASS, tipBean.getNoticeClass());
                    intent.setClass(Notification2Activity.this, StatusScanningAct.class);
                    startActivity(intent);
                    finish();

                } else if (tipBean.getTemplateId().equals("AA-1")) {
//                    OrderItemBean orderItemBean=new OrderItemBean();
//                    orderItemBean.setInjectionTool(tipBean.getInjectionTool());
//                    orderItemBean.setPlanOrderNo(tipBean.getRelatedPlanOrderNo());
//                    orderItemBean.setSpeed(tipBean.getSpeed());
//                    orderItemBean.setPerformStatus("1");
//                    bundle.putSerializable(GlobalConstant.KEY_ORDERITEMBEAN, orderItemBean);
//                    //bundle.putSerializable(GlobalConstant.KEY_PATIENTFLOW, FlowConstant.PatientFlow
// .MEDICINE_END);没有身份核对
//                    bundle.putSerializable(GlobalConstant.KEY_PATIENTFLOW, null);
//                    intent.putExtras(bundle);
//                    intent.setClass(Notification2Activity.this, FlowAct.class);
//                    startActivity(intent);
//                    finish();

                    intent.putExtras(bundle);
                    intent.putExtra(GlobalConstant.KEY_TEMPLATEID, "AA-1");
                    intent.putExtra(GlobalConstant.KEY_PLANORDERNO, tipBean.getRelatedPlanOrderNo());
                    intent.putExtra(GlobalConstant.KEY_NOTICECLASS, tipBean.getNoticeClass());
                    intent.setClass(Notification2Activity.this, StatusScanningAct.class);
                    startActivity(intent);
                    finish();
                } else if (tipBean.getTemplateId().equals("")) {
                    ShowToast(getString(R.string.thelackoforders));
                }

            }
        });
    }

    /**
     * 处理结果回传方法
     */
    public void accessNetCallBack(String result) {

        HttpUtils http = new HttpUtils();
        // 用来封装参数
        RequestParams params = new RequestParams();
        // 用户ID
        Log.v(TAG, "当前用户的ID>>" + UserInfo.getUserName());

        params.addBodyParameter("username", UserInfo.getUserName());

        Log.v(TAG, "当前消息ID>>" + noticeID);

        params.addBodyParameter("noticeId", noticeID);

        Log.v(TAG, "当前消息处理状态 >>" + result);

        params.addBodyParameter("confirmStatus", result);// 1:继续有效 2:已处理 -1:作废

        params.addBodyParameter("delay", "0");

        http.send(HttpRequest.HttpMethod.POST, UrlConstant.PostTipResult()
                        + "username=" + UserInfo.getUserName() + "&noticeId="
                        + noticeID + "&confirmStatus=" + result + "&delay=0",
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        Log.v(TAG, "处理结果返回失败");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        Log.v(TAG, "处理结果返回成功");
                        JSONObject mJsonObject = null;
                        // 网络状态码
                        String code = "";
                        // 网络返回消息
                        String responseMsg = "";
                        try {
                            mJsonObject = new JSONObject(arg0.result);
                            code = mJsonObject.getString("result");
                            if (code.equals("true")) {
                                responseMsg = mJsonObject.getString("msg");
                            } else {
                                responseMsg = mJsonObject.getString("msg");
                            }
                            if (!responseMsg.equals("")) {
                                Toast.makeText(Notification2Activity.this,
                                        responseMsg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            if (LinstenNetState.isLinkState(getApplicationContext())) {
                                Toast.makeText(Notification2Activity.this, getString(R.string.unstable), Toast
                                        .LENGTH_SHORT).show();
                            }else{
                                toErrorAct();
                            }
                        }

                    }
                });
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(Notification2Activity.this, ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 加载列表数据
     */
    public void loadNotificationData() {
        final ProgressDialog pd = ProgressDialog.show(this, "", getResources().getString(R.string.dataloading));
        String url = UrlConstant.GetTipMsg() + "username=" + UserInfo.getUserName() + "&patId=null" +
                "&confirmStatus=1" + "&wardCode=" + UserInfo.getWardCode() + "&flag=0";
        CCLog.i("加载通知数据>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            JSONObject jsonObject=null;
            boolean result=false;
            JSONArray jsonArray=null;
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    jsonObject=new JSONObject(responseInfo.result);
                    result=jsonObject.getBoolean("result");
                    jsonArray=jsonObject.getJSONArray("data");
                    if(result){
                        CCLog.i(TAG, "消息加载成功");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<TipBean>>() {
                        }.getType();

                        notificationDatas = gson.fromJson(jsonArray.toString(), type);
                        adapter.clear();
                        adapter.addAll(notificationDatas);
                    }else{
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                    pd.dismiss();
                } catch (JSONException e) {
                    pd.dismiss();
                    if (LinstenNetState.isLinkState(getApplicationContext())) {
                        Toast.makeText(Notification2Activity.this, getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    }else{
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Toast.makeText(Notification2Activity.this, getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                }else{
                    toErrorAct();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imbtn_back:
                Intent intent = new Intent();
                intent.setClass(Notification2Activity.this, HomeAct.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
