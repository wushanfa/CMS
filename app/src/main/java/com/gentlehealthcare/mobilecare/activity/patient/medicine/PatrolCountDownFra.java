package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import java.util.ArrayList;
import java.util.List;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.CheckMedicineBean;
import com.gentlehealthcare.mobilecare.net.bean.CompleteExecuteBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadInspectionTimeBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadIntravsSnapshotBean;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.CompleteExecuteRequest;
import com.gentlehealthcare.mobilecare.net.impl.LoadInspectionTimeReq;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtn;
import com.gentlehealthcare.mobilecare.view.adapter.MedicineListAdapter;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author ouyang
 * @ClassName: PatrolCountDown
 * @Description: 给药 巡视计时
 * @date 2015年3月2日 上午9:08:01
 */
@SuppressLint("ValidFragment")
public class PatrolCountDownFra extends BaseFragment implements OnClickListener {
    private long time;
    private long visitime;
    private TextView tvTime1, tvTime2, tvTime3, tvTime4;
    private TextView tv_starttime;
    private SyncPatientBean patient;
    private boolean isruning = false;
    private static final int UPDATE = 0X00EF;
    private ListView lv_medicine;
    private boolean isValid;
    private ProgressDialog progressDialog = null;
    private Button btn_huanyao;
    private OrderItemBean orderItemBean = null;

    private boolean showMedicineInfo = false;
    private RelativeLayout ll_checkmedicine;


    public PatrolCountDownFra(SyncPatientBean patient, OrderItemBean orderItemBean) {
        super();
        this.patient = patient;
        this.orderItemBean = orderItemBean;

    }

    public PatrolCountDownFra(SyncPatientBean patient, OrderItemBean orderItemBean, boolean showMedicineInfo) {
        super();
        this.patient = patient;
        this.orderItemBean = orderItemBean;
        this.showMedicineInfo = showMedicineInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patrol_count_down, null);
        ll_checkmedicine = (RelativeLayout) view.findViewById(R.id.ll_checkmedicine);
        if (!showMedicineInfo) {
            ll_checkmedicine.setVisibility(View.GONE);
        }
        btn_huanyao = (Button) view.findViewById(R.id.btn_huanyao);
        Fragment currentFra = getParentFragment();
        if (currentFra instanceof FlowSheetFra)
            btn_huanyao.setVisibility(View.VISIBLE);
        btn_huanyao.setOnClickListener(this);
        isValid = false;
        progressDialog = new ProgressDialog(getActivity());
        tv_starttime = (TextView) view.findViewById(R.id.tv_injectionTime);
        tvTime1 = (TextView) view.findViewById(R.id.tv_time);
        Button btn_saomiao = (Button) view.findViewById(R.id.btn_saomiao);
        btn_saomiao.setOnClickListener(this);
        //注释于 2015-11-27 by zyy 瓶签码可能有可能没有 并且有点击核对 所以去掉扫描核对
//        btn_saomiao.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                final EditText editText = new EditText(getActivity());
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
//                editText.setLayoutParams(lp);
//               // editText.setText(patient.getPatCode());//修改 byZyy 2015-11-27
//                editText.setText(orderItemBean.getPlanOrderNo());
//                editText.setHint("手动输入药品编号");
//                new AlertDialog.Builder(getActivity()).setTitle("提示").setCancelable(false).setView(editText)
// .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (editText.getText().toString().equals("")) {
//                            ShowToast("请输入编号值");
//                        } else {
//                                DoIntroduction(editText.getText().toString(),false);
//                        }
//                    }
//                }).setPositiveButton("取消", null).create().show();
//                return false;
//            }
//        });
        tvTime2 = (TextView) view.findViewById(R.id.tv_count_down);

        tvTime3 = (TextView) view.findViewById(R.id.tv_count_down1);

        tvTime4 = (TextView) view.findViewById(R.id.tv_count_down2);

        tvTime2.setText("等待时间:  ");

        tvTime4.setText("");
        lv_medicine = (ListView) view.findViewById(R.id.lv_medicine);
        List<OrderItemBean> list = new ArrayList<OrderItemBean>();
        list.add(orderItemBean);
        lv_medicine.setAdapter(new MedicineListAdapter(getActivity(), list));
        DoLoadInspectionTimeReq();
        return view;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE:
                    tvTime3.setText((String) msg.obj);
                    break;
            }
        }
    };

    public String getTime(long time) {


        long h = time / 3600;

        time = time % 3600;

        long m = time / 60;

        time = time % 60;

        long s = time;

        return getTimeString(h) + ":" + getTimeString(m) + ":" + getTimeString(s);
    }


    public String getTimeString(long l) {

        String string;

        if (l < 10) {
            string = "0" + l;
        } else {
            string = String.valueOf(l);
        }

        return string;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isruning = false;
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view
                .findViewById(R.id.root_fra_patrol_count);
        SupportDisplay.resetAllChildViewParam(root);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_saomiao:
                new AlertDialog.Builder(getActivity()).setTitle("提示").setCancelable(false).setMessage
                        ("您确定手动核对药品信息的正确吗?").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DoIntroduction(null, true);
                    }
                }).setPositiveButton("取消", null).create().show();
                break;
            case R.id.btn_huanyao:
                final AlertDialogTwoBtn alertDialogTwoBtn = new AlertDialogTwoBtn(getActivity());
                alertDialogTwoBtn.setTitle("提示");
                alertDialogTwoBtn.setMessage("确定执行结束该药品并更换下一药品操作吗?");
                alertDialogTwoBtn.setLeftButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogTwoBtn.dismiss();
                    }
                });
                alertDialogTwoBtn.setRightButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogTwoBtn.dismiss();
                        stopMedicine();
                    }
                });
                alertDialogTwoBtn.show();
                break;
        }
    }

    public boolean isValid() {
        return isValid;
    }

    /**
     * 停止给药
     */
    public void stopMedicine() {
        CompleteExecuteBean completeExecuteBean = new CompleteExecuteBean();
        completeExecuteBean.setStatus("1");
        completeExecuteBean.setPatId(patient.getPatId());
        completeExecuteBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        completeExecuteBean.setType(0);
        progressDialog.setMessage(getResources().getString(R.string.endmedicial));
        progressDialog.show();
        RequestManager.connection(new CompleteExecuteRequest(getActivity(), new IRespose<CompleteExecuteBean>() {
            @Override
            public void doResult(CompleteExecuteBean completeExecuteBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {

                CompleteExecuteBean completeExecuteBean = new Gson().fromJson(result, CompleteExecuteBean.class);
                if (completeExecuteBean.getResult()) {
                    if (completeExecuteBean.getPerformTask() <= 0) {
                        getActivity().overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
                        getActivity().finish();
                    } else {
                        Fragment currentFra = getParentFragment();
                        ((FlowSheetFra) currentFra).DoGotoMedicineListFra();
                    }
                } else {
                    ShowToast(completeExecuteBean.getMsg());
                }
                progressDialog.dismiss();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                else {
                    toErrorAct();
                }
            }
        }, 0, true, completeExecuteBean));
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }
    /**
     * 扫描药品列表
     */
    private void DoMedicineList() {

        this.isValid = true;
        Toast.makeText(getActivity(), "药品扫描成功", Toast.LENGTH_SHORT).show();

        FlowSheetFra flowSheetFra = (FlowSheetFra) getParentFragment();
        flowSheetFra.showBottomBtn();
    }

    /**
     * 获取巡视时间和注射时间
     */
    private void DoLoadInspectionTimeReq() {
        progressDialog.setMessage("正在加载数据,请提交.");
        progressDialog.show();
        final LoadInspectionTimeBean loadInspectionTimeBean = new LoadInspectionTimeBean();
        loadInspectionTimeBean.setPatId(patient.getPatId());
        loadInspectionTimeBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        RequestManager.connection(new LoadInspectionTimeReq(getActivity(), new IRespose<LoadInspectionTimeBean>() {
            @Override
            public void doResult(LoadInspectionTimeBean loadInspectionTimeBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {
                LoadInspectionTimeBean loadInspectionTimeBean = new Gson().fromJson(result,
                        LoadIntravsSnapshotBean.class);
                if (loadInspectionTimeBean.isResult()) {
                    long visitTime = DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS, loadInspectionTimeBean
                            .getInspectionTime());
                    tv_starttime.setText("注射时间:  " + DateTool.getHourMinuteSecond(DateTool.parseDate(DateTool
                            .YYYY_MM_DD_HH_MM_SS, loadInspectionTimeBean.getEventStartTime()) + ""));
                    tvTime1.setText("巡视时间:  " + DateTool.getHourMinuteSecond(visitTime + ""));
                    long nowTime = System.currentTimeMillis();
                    time = visitTime - nowTime;
                    if (time > 0) {
//            time=visitime-System.currentTimeMillis();
                        time = time / 1000;
                        isruning = true;
                    } else {
                        tvTime3.setText(getTime(0));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isruning) {
                                if (time <= 0) {
                                    time = 0;
                                    isruning = false;
                                }
                                Message msg = handler.obtainMessage(UPDATE);
                                msg.obj = getTime(time);
                                handler.sendMessage(msg);
                                time--;
                                try {
                                    Thread.sleep(1 * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else {
                    ShowToast(loadInspectionTimeBean.getMsg());
                }
                progressDialog.dismiss();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                else {
                    toErrorAct();
                }
            }
        }, 0, true, loadInspectionTimeBean));

    }


    /**
     * 核对药品信息
     */
    public void DoIntroduction(final String relatedBarcode, boolean okMode) {
        progressDialog.show();
        CheckMedicineBean checkMedicineBean = new CheckMedicineBean();
        checkMedicineBean.setPatId(patient.getPatId());
        if (okMode)
            checkMedicineBean.setCheckMode("manually");
        checkMedicineBean.setInjectionTool(orderItemBean.getInjectionTool());
        checkMedicineBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        checkMedicineBean.setRelatedBarcode(relatedBarcode);
        checkMedicineBean.setType(0);
        String url = UrlConstant.GetCheckMedicine(checkMedicineBean.getType()) + checkMedicineBean.toString();
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CheckMedicineBean checkMedicineBean = new Gson().fromJson(result, CheckMedicineBean.class);
                if (checkMedicineBean.getResult()) {
                    DoMedicineList();
                } else {
                    ShowToast(checkMedicineBean.getMsg());
                }
                progressDialog.dismiss();
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

    public boolean isShow() {
        return progressDialog.isShowing();
    }

}
