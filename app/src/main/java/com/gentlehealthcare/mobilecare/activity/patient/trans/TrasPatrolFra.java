package com.gentlehealthcare.mobilecare.activity.patient.trans;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.activity.notification.Notification2Activity;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtn;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zyy
 * @date 2015-9-25下午2:30:51
 * @category 输血巡视界面
 */
@SuppressLint("ValidFragment")
public class TrasPatrolFra extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "TransfusionFra";
    /**
     * 血袋号
     */
    TextView tv_xuedaihao;
    /**
     * 血型
     */
    TextView tv_xuexing;
    /**
     * 血型
     */
    TextView tv_xueliang;

    /**
     * 数学时间
     */
    TextView tv_tras_time1;
    /**
     * 巡视点
     */
    TextView tv_tras_time2;
    /**
     * 倒计时
     */
    TextView tv_tras_time3;
    /**
     * 观察按钮
     */
    private Button btn_look;
    /**
     * 确认按钮
     */
    private Button btn_patrolsure;
    private Button btn_continue;
    /**
     * 同步病人数据
     */
    private SyncPatientBean patient = null;

    /**
     * 选中血品信息
     */
    private BloodProductBean2 mBloodProductBean2 = null;

    /**
     * 加载进度条
     */
    private ProgressDialog progressDialog = null;

    /**
     * 下次巡视时间对话框
     */
    private Dialog currentDialog = null;

    /**
     * 根据时间判断是否正在执行
     */
    private boolean isruning = false;

    /**
     * 巡视时间和当前时间差
     */
    private long mSpaceTime = 0;

    /**
     * HANDLER消息
     */
    private static final int UPDATE = 0X00CF;
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
     * 血袋成分
     */
    private TextView tv_medicineinfo;

    /**
     * 时间
     */
    private TextView tv_time;

    private LinearLayout ll_first_patral;
    private LinearLayout last_patral_time;
    /**
     * 结束输血提示框
     */
    private AlertDialogTwoBtn endAlertDialogTwoBtn = null;

    private TextView tv_tras_time_title;

    private long myGapTime = 0;

    private TextView tv_medicineother;

    public TrasPatrolFra(SyncPatientBean patient, BloodProductBean2 mBloodProductBean2) {
        super();
        this.patient = patient;
        this.mBloodProductBean2 = mBloodProductBean2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause(patient.getPatId(),mBloodProductBean2.getPlanOrderNo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_patrol, null);
        // 初始化UI组件
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载数据,请稍后..");
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        btn_look = (Button) getActivity().findViewById(R.id.btn_look);
        btn_look.setOnClickListener(this);
        btn_patrolsure = (Button) getActivity().findViewById(R.id.btn_patrolsure);
        btn_patrolsure.setOnClickListener(this);
        btn_continue = (Button) getActivity().findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);

        tv_xuedaihao = (TextView) getActivity().findViewById(R.id.tv_xuedaihao);
        tv_xueliang = (TextView) getActivity().findViewById(R.id.tv_xueliang);
        tv_xuexing = (TextView) getActivity().findViewById(R.id.tv_xuexing);
        tv_tras_time1 = (TextView) getActivity().findViewById(R.id.tv_tras_time1);

        tv_tras_time2 = (TextView) getActivity().findViewById(R.id.tv_tras_time2);
        tv_tras_time3 = (TextView) getActivity().findViewById(R.id.tv_tras_time3);
        tv_tras_bed = (TextView) getActivity().findViewById(R.id.tv_tras_bed);
        if (patient.getBedLabel() != null) {
            tv_tras_bed.setText("床号：" + patient.getBedLabel());
        } else {
            tv_tras_bed.setText(getResources().getString(R.string.bed_label) + Html.fromHtml
                    ("<small>未分配</small>"));
        }
        tv_tras_name = (TextView) getActivity().findViewById(R.id.tv_tras_name);
        tv_tras_name.setText("姓名:" + patient.getName());
        tv_medicineinfo = (TextView) getActivity().findViewById(R.id.tv_medicineinfo);
        tv_time = (TextView) getActivity().findViewById(R.id.tv_time);
        imbtn_home = (ImageButton) getActivity().findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) getActivity().findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);
        tv_tras_time_title = (TextView) getActivity().findViewById(R.id.tv_tras_time_title);
        ll_first_patral = (LinearLayout) getActivity().findViewById(R.id.ll_first_patral);
        last_patral_time = (LinearLayout) getActivity().findViewById(R.id.last_patral_time);
        // 加载输血开始时间
        LoadEsTimeAndIsTime();
        tv_medicineother = (TextView) getActivity().findViewById(R.id.tv_medicineother);
        tv_medicineinfo.setText(mBloodProductBean2.getBloodTypeName());
        tv_xuedaihao.setText(mBloodProductBean2.getBloodDonorCode());
        tv_xuexing.setText(mBloodProductBean2.getBloodGroupDesc());
        tv_medicineother.setText(mBloodProductBean2.getBloodCapacity());
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        isruning = false;
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_fra_patrol);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            // 观察按钮
            case R.id.btn_look:
                TPRSpeedObserveFra tsl = new TPRSpeedObserveFra(patient, null, mBloodProductBean2, 2, 900000);
                getFragmentManager().beginTransaction().replace(R.id.fl_container, tsl).addToBackStack(null).
                        commit();
                break;
            case R.id.btn_patrolsure:
                endAlertDialogTwoBtn = new AlertDialogTwoBtn(getActivity());
                endAlertDialogTwoBtn.setTitle("提示");
                endAlertDialogTwoBtn.setMessage("是否结束输血流程");
                endAlertDialogTwoBtn.setRightButton("结束输血", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        compliteTransfusion();
                        endAlertDialogTwoBtn.dismiss();
                    }
                });
                endAlertDialogTwoBtn.setLeftButton("继续巡视", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        endAlertDialogTwoBtn.dismiss();
                    }
                });
                break;
            case R.id.imbtn_back:

                //有了身份扫描界面
                if (GlobalConstant.ISBACKNOTIFITION == 'y') {
                    //返回到通知列表界面
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Notification2Activity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
                    getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                    super.getActivity().onBackPressed();
                }
                break;
            case R.id.imbtn_home:
                getActivity().startActivity(
                        new Intent(getActivity(), HomeAct.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.in_or_out_static,
                        R.anim.slide_out_right);
                break;
            case R.id.btn_continue:
                continueToBloodTransfusion(patient.getPatId(),UserInfo.getUserName(), mBloodProductBean2.getPlanOrderNo(), "恢复继续输血", "0");
                break;
            default:
                break;
        }

    }

    /**
     * 加载输血开始时间和下次巡视时间
     */
    private void LoadEsTimeAndIsTime() {
        progressDialog.show();
        String url = UrlConstant.LoadEsTimeAndIsTime() + UserInfo.getUserName() + "&patId=" + patient.getPatId()
                + "&planOrderNo=" + mBloodProductBean2.getPlanOrderNo();
        CCLog.i(TAG, "加载输血开始时间：" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                progressDialog.dismiss();

                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;
                JSONObject mJsonObject = null;
                String mStratTime = null;
                String mPatrolTime = null;
                boolean state = false;
                try {
                    mJsonObject = new JSONObject(result);
                    state = mJsonObject.getBoolean("result");
                    if (state) {
                        mStratTime = mJsonObject.getString("eventStartTime");
                        mPatrolTime = mJsonObject.getString("inspectionTime");
                    }

                } catch (JSONException e1) {
                    CCLog.e(TAG, "加载输血时间异常");

                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }

                //观察界面用在这儿赋值
                GlobalConstant.bloodStartTime = mStratTime;
                GlobalConstant.bloodPatroyTime = mPatrolTime;

                tv_tras_time1.setText(DateTool.toMinAndSecond(mStratTime));
                tv_tras_time2.setText(DateTool.toMinAndSecond(mPatrolTime));
                long startTime = DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS, mStratTime);
                long visitTime = DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS, mPatrolTime);
                long nowTime = System.currentTimeMillis();

                mSpaceTime = visitTime - nowTime;
                myGapTime = nowTime - startTime;
                int gap = 900000;

                if (myGapTime <= gap) {
                    tv_tras_time_title.setText("首次巡视时间:");
                } else if (myGapTime > gap && myGapTime <= gap * 2) {
                    tv_tras_time_title.setText("二次巡视时间:");
                } else if (myGapTime > gap * 2 && myGapTime <= gap * 3) {
                    tv_tras_time_title.setText("三次巡视时间:");
                }

                if (mSpaceTime > 0) {// 表示可进行巡视
                    mSpaceTime = mSpaceTime / 1000;
                    isruning = true;
                } else {
                    tv_tras_time3.setText("巡视时间到");
                }
                progressDialog.dismiss();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (isruning) {
                            if (mSpaceTime <= 0) {
                                mSpaceTime = 0;
                               // tv_tras_time3.setText("巡视时间到");
                                isruning = false;
                            }
                            Message msg = handler.obtainMessage(UPDATE);
                            msg.obj = getTime(mSpaceTime);
                            handler.sendMessage(msg);
                            mSpaceTime--;
                            try {
                                Thread.sleep(1 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 完成输血
     */
    private void compliteTransfusion() {
        String url = UrlConstant.CompliteTransfusion() + UserInfo.getUserName() + "&patId=" + patient
                .getPatId() + "&applyNo=" + mBloodProductBean2.getApplyNo() + "&itemNo=" + mBloodProductBean2
                .getItemNo() + "&bloodId=" + mBloodProductBean2.getBloodId() + "&planOrderNo=" +
                mBloodProductBean2.getPlanOrderNo() + "&status=1";
        CCLog.i("完成输血>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            JSONObject mJsonObject = null;
            boolean result = false;
            JSONArray jsonArray = null;

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                CCLog.i("巡视界面加载输血医嘱失败了");
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                CCLog.i("巡视界面加载输血医嘱成功了");
                try {
                    mJsonObject = new JSONObject(arg0.result);
                    result = mJsonObject.getBoolean("result");
                    if (result) {
                        ShowToast("系统已结束输血");
                        Intent intent = new Intent();
                        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
                        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }
            }
        });
    }

    /**
     * 继续输血
     */
    private void continueToBloodTransfusion(String patId,String username, String planOrderNo, String performDesc, String suspEndAttr) {
        String url = UrlConstant.pausedTrans() + patId + "&username="+username+"&planOrderNo=" + planOrderNo + "&performDesc=" + performDesc + "&suspEndAttr=" + suspEndAttr;
        CCLog.i("继续输血>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonobject = new JSONObject(responseInfo.result);
                    boolean result = jsonobject.getBoolean("result");
                    if (result) {
                        //继续输血按钮消失
                        btn_continue.setVisibility(View.INVISIBLE);
                        ShowToast("系统开始继续输血");
//                        Intent intent = new Intent();
//                        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
//                        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
//                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ShowToast("继续输血失败,请检查数据");
            }
        });
    }

    /**
     * 是否暂停输血
     */
    private void isPause(String patId, String planOrderNo) {
        String url = UrlConstant.isPausedTrans() + patId + "&planOrderNo=" + planOrderNo;
        CCLog.i("是否暂停输血>>>", url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonobject = new JSONObject(responseInfo.result);
                    boolean result = jsonobject.getBoolean("result");
                    if (result) {
                        String suspendAttr = jsonobject.getString("suspendAttr");
                        if ("1".equals(suspendAttr)) {
                            btn_continue.setVisibility(View.VISIBLE);
                            ll_first_patral.setVisibility(View.INVISIBLE);
                            last_patral_time.setVisibility(View.INVISIBLE);
                        }
                        //继续输血按钮消失
//                        Intent intent = new Intent();
//                        intent.putExtra(GlobalConstant.KEY_PLANBARCODE, mBloodProductBean2.getPlanOrderNo());
//                        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
//                        getActivity().finish();
                    }else {
                        ShowToast("计划表中没有此planOrderNo,请联系运维人员");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ShowToast("继续输血失败,请检查数据");
            }
        });
    }


    /**
     * 显示时间
     *
     * @param time
     * @return
     */
    private String getTime(long time) {

        long h = time / 3600;

        time = time % 3600;

        long m = time / 60;

        time = time % 60;

        long s = time;

        return getTimeString(h) + ":" + getTimeString(m) + ":"
                + getTimeString(s);
    }

    /**
     * 时间转化
     *
     * @param l
     * @return
     */
    private String getTimeString(long l) {

        String string;

        if (l < 10) {
            string = "0" + l;
        } else {
            string = String.valueOf(l);
        }
        return string;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE:
                    tv_tras_time3.setText((String) msg.obj);
                    break;
            }
        }
    };

}
