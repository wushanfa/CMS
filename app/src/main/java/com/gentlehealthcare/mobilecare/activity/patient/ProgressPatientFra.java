package com.gentlehealthcare.mobilecare.activity.patient;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.bloodsugar.BloodSugarActivity;
import com.gentlehealthcare.mobilecare.activity.patient.insulin.InsulinFlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.FlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.trans.TransfusionActivity;
import com.gentlehealthcare.mobilecare.bean.PlanTimeCountBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.PatientConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.db.dao.ExecutionWorkDao;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.LoadPatientPlanCountBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.ProgressWorkAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ouyang
 * @ClassName: ProgressPatient
 * @Description: 病人 操作流程
 * @date 2015年3月2日 上午8:52:52
 */
@SuppressLint("ValidFragment")
public class ProgressPatientFra extends BaseFragment {

    private int[] axis = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23};


    private String[] works = {"  ", "静脉输液", "输   血", "胰岛素", "血   糖"};//注释于 2015-11-30 by zyy 给药流程没有测试人员建议去掉

    private SyncPatientBean patient;

    private int state;

    private HorizontalScrollView scrollView;

    private TextView tvTimeAxis;

    private ListView lvWorks, lvTimeAxis;

    private List<String> workList;

    private List<String> timeList;

    private ExecutionWorkDao executionWorkDao = null;
    private ProgressWorkAdapter adapter = null;

    private ProgressDialog progressDialog = null;

    public ProgressPatientFra() {
        super();
    }

    @SuppressLint("ValidFragment")
    public ProgressPatientFra(SyncPatientBean patient, String state) {
        super();
        this.patient = patient;
        //0表示待执行
        if (state.equals(PatientConstant.STATE_WAITING))
            this.state = 0;
            //1表示执行中
        else if (state.equals(PatientConstant.STATE_EXECUTING))
            this.state = 1;
            //9表示已执行
        else
            this.state = 9;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        workList = new ArrayList<String>();
        timeList = new ArrayList<String>();
        executionWorkDao = ExecutionWorkDao.newInstance(getActivity());
        for (int i = 0; i < works.length; i++) {
            workList.add(works[i]);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patient, null);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载数据,请稍后");
        scrollView = (HorizontalScrollView) view
                .findViewById(R.id.horizontalScrollView);
        lvTimeAxis = (ListView) view.findViewById(R.id.lv_time_axis);
        lvTimeAxis.setAdapter(adapter = new ProgressWorkAdapter(timeList,
                getActivity()));
        notifyData();
        scrollView.post(scrollRunnable);

        tvTimeAxis = (TextView) view.findViewById(R.id.tv_time_axis);

        tvTimeAxis.setText(getTimeAxis(axis));

        lvWorks = (ListView) view.findViewById(R.id.lv_progress);

        lvWorks.setAdapter(new ProgressWorkAdapter(workList, getActivity()));

        lvWorks.setOnItemClickListener(mItemClickListener);

        lvTimeAxis.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                if (i == 1) {
                    Intent intent = new Intent(getActivity(), FlowAct.class);

                    intent.putExtra("patient", patient);

                    getActivity().startActivity(intent);

                    getActivity().overridePendingTransition(
                            R.anim.slide_in_right, R.anim.in_or_out_static);

                } else if (i == 3) {
                    Intent intent = new Intent(getActivity(),
                            InsulinFlowAct.class);

                    intent.putExtra("patient", patient);

                    getActivity().startActivity(intent);

                    getActivity().overridePendingTransition(
                            R.anim.slide_in_right, R.anim.in_or_out_static);
                }

            }
        });
        return view;
    }

    private String getTimeAxis(int[] is) {

        String timeAxis = "";

        String space = " ";

        for (int i = 0; i < is.length; i++) {

            if (is[i] < 10) {
                //
                timeAxis = timeAxis + space + 0 + is[i] + space;
            } else {

                timeAxis = timeAxis + space + is[i] + space;
            }
        }

        return timeAxis;
    }

    OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /**
             * position == 0 口服给药
             * position == 1 静脉输液
             * position == 2 输血
             * position == 3 胰岛素
             * position == 4 测血糖
             */
            if (position == 1) {
                Intent intent = new Intent(getActivity(), FlowAct.class);
                intent.putExtra(GlobalConstant.KEY_PATIENT, patient);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
            } else if (position == 3) {//position == 3
                Intent intent = new Intent(getActivity(), InsulinFlowAct.class);
                intent.putExtra("patient", patient);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
            } else if (position == 4) {//position == 4
                Intent intent = new Intent(getActivity(), BloodSugarActivity.class);
                intent.putExtra("patient", patient);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
            } else if (position == 2) {//position == 2
                Intent intent = new Intent(getActivity(), TransfusionActivity.class);
                intent.putExtra("patient", patient);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
            }

        }
    };

    Runnable scrollRunnable = new Runnable() {

        @Override
        public void run() {

            float width = tvTimeAxis.getWidth() / 24;

            SimpleDateFormat formatter = new SimpleDateFormat("HH");

            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

            int hh = Integer.valueOf(formatter.format(curDate));

            if (hh > 3 && hh < 23) {

                scrollView.scrollTo((int) ((hh - 2) * width), 0);
            }

            if (hh == 23) {

                scrollView.scrollTo((int) ((hh - 3) * width), 0);
            }

        }
    };


    public void notifyData() {
        if (patient == null || progressDialog == null) {
            return;
        }
        progressDialog.show();
        LoadPatientPlanCountBean loadPatientPlanCountBean = new LoadPatientPlanCountBean();
        loadPatientPlanCountBean.setPatId(patient.getPatId());
        loadPatientPlanCountBean.setPerformStatus(state + "");
        String url = UrlConstant.LoadPatientPlanCount() + loadPatientPlanCountBean.toString() +
                "&username=" + UserInfo.getUserName();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                timeList.clear();
                Gson gson = new Gson();
                Map<String, PlanTimeCountBean> retMap = gson.fromJson(result, new TypeToken<Map<String,
                        PlanTimeCountBean>>() {
                }.getType());
                PlanTimeCountBean planTimeCountBean = null;
                if (planTimeCountBean == null) {
                    planTimeCountBean = new PlanTimeCountBean();
                }
                // 注射
                timeList.add(planTimeCountBean.getTimePlanCount());
                planTimeCountBean = retMap.get("AA-1");
                if (planTimeCountBean == null) {
                    planTimeCountBean = new PlanTimeCountBean();
                }
                // 给药
                timeList.add(planTimeCountBean.getTimePlanCount());
                planTimeCountBean = retMap.get("AA-5");
                if (planTimeCountBean == null) {
                    planTimeCountBean = new PlanTimeCountBean();
                }
                // 输血
                timeList.add(planTimeCountBean.getTimePlanCount());
                planTimeCountBean = retMap.get("AA-3");
                if (planTimeCountBean == null) {

                    planTimeCountBean = new PlanTimeCountBean();
                }
                // 血糖
                timeList.add(planTimeCountBean.getTimePlanCount());
                planTimeCountBean = retMap.get("AA-4");
                if (planTimeCountBean == null) {
                    planTimeCountBean = new PlanTimeCountBean();
                }
                // 胰岛素
                timeList.add(planTimeCountBean.getTimePlanCount());
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
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

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view
                .findViewById(R.id.root_fra_patient);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
