package com.gentlehealthcare.mobilecare.activity.work;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.bloodsugar.BloodSugarActivity;
import com.gentlehealthcare.mobilecare.activity.patient.insulin.InsulinFlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.medicine.FlowAct;
import com.gentlehealthcare.mobilecare.activity.patient.trans.TransfusionActivity;
import com.gentlehealthcare.mobilecare.bean.PlanTimeCountBean;
import com.gentlehealthcare.mobilecare.constant.PatientConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.LoadNursingPlanForCategoryRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.ProgressWorkAdapter;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author ouyang
 * @ClassName: ProgressWork
 * @Description: 工作流程
 * @date 2015年3月2日 上午9:21:19
 */
@SuppressLint("ValidFragment")
public class ProgressWorkFra extends BaseFragment {
    private int[] axis = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    private HorizontalScrollView scrollView;
    private TextView tvTimeAxis;
    private ListView lvProgress, lvTimeAxis;
    private List<String> progressList;
    private int state;
    private List<String> timeList;
    private List<SyncPatientBean> myPatientList = null;
    private List<SyncPatientBean> patientBeanList = null;
    private String templateId;
    private ProgressDialog progressDialog = null;
    private ProgressWorkAdapter adapter = null;
    private ArrayAdapter patAdapter = null;

    public ProgressWorkFra(String templateId, String state) {
        super();
        this.templateId = templateId;
        if (state.equals(PatientConstant.STATE_WAITING)) {
            this.state = 0;
        } else if (state.equals(PatientConstant.STATE_EXECUTING)) {
            this.state = 1;
        } else {
            this.state = 9;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_work, null);
       // myPatientList = UserInfo.getMyPatient(); by zyy 2016-03-09
        myPatientList=UserInfo.getDeptPatient();
        scrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
        scrollView.post(scrollRunnable);
        progressList = new ArrayList<String>();
        patientBeanList = new ArrayList<SyncPatientBean>();
        timeList = new ArrayList<String>();

        progressDialog = new ProgressDialog(getActivity());
        tvTimeAxis = (TextView) view.findViewById(R.id.tv_time_axis);

        tvTimeAxis.setText(getTimeAxis(axis, true));

        lvProgress = (ListView) view.findViewById(R.id.lv_progress);

        lvProgress.setAdapter(patAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_progress,
                progressList));

        lvProgress.setOnTouchListener(touchListener);

        lvProgress.setOnItemClickListener(mItemClickListener);

        lvTimeAxis = (ListView) view.findViewById(R.id.lv_time_axis);

        lvTimeAxis.setAdapter(adapter = new ProgressWorkAdapter(timeList, getActivity()));

        lvTimeAxis.setOnScrollListener(scrollListener);
        lvTimeAxis.setOnItemClickListener(mItemClickListener);
        DoLoadNursingPlanForCategoryRecordReq();
        return view;
    }

    private String getTimeAxis(int[] is, boolean flag) {

        String timeAxis = "";

        String space = " ";

        for (int i = 0; i < is.length; i++) {

            if (is[i] < 10) {
                boolean bo = flag;
                if (is[i] > 0)
                    bo = true;
                String content = "";
                if (bo) {
                    content = "<font color=\"black\">" + space + "0" + is[i] + space + "</font>";
                } else
                    content = "<font color=transparent>" + space + "00" + space + "</font>";
                content = space + "0" + is[i] + space;
                timeAxis = timeAxis + content;
            } else {
                String content = "<font color=\"black\">" + space + is[i] + space + "</font>";
                content = space + is[i] + space;
                timeAxis = timeAxis + content;
            }
        }
        return timeAxis;
    }

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

    OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            Intent intent = new Intent(getActivity(), FlowAct.class);
//
//            intent.putExtra("patient", patientBeanList.get(position));
//
//            getActivity().startActivity(intent);
//
//            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
            /**
             * position == 0 口服给药
             * position == 1 静脉输液
             * position == 2 输血
             * position == 3 胰岛素
             * position == 4 测血糖
             */
            SyncPatientBean patient = patientBeanList.get(position);
            if (templateId.equals("AA-1")) {
                if (state == 0) {
                    Intent intent = new Intent(getActivity(), FlowAct.class);
                    intent.putExtra("patient", patient);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
                } else if (state == 1) {
                    Intent intent = new Intent(getActivity(), FlowAct.class);
                    intent.putExtra("patient", patient);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
                }
            } else if (templateId.equals("AA-3")) {
                Intent intent = new Intent(getActivity(), InsulinFlowAct.class);
                intent.putExtra("patient", patient);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
            } else if (templateId.equals("AA-4")) {
                Intent intent = new Intent(getActivity(), BloodSugarActivity.class);
                intent.putExtra("patient", patient);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
            } else if (templateId.equals("AA-5")) {
                Intent intent = new Intent(getActivity(), TransfusionActivity.class);
                intent.putExtra("patient", patient);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.in_or_out_static);
            }
        }
    };

    OnTouchListener touchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            lvTimeAxis.dispatchTouchEvent(event);

            return false;
        }
    };

    OnScrollListener scrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (lvTimeAxis.getChildAt(0) != null) {

                lvProgress.setSelectionFromTop(lvTimeAxis.getFirstVisiblePosition(), lvTimeAxis.getChildAt(0)
                        .getTop());
            }
        }
    };

//    @Override
//    public void onResume() {
//        super.onResume();
//        DoLoadNursingPlanForCategoryRecordReq();
//    }

    public void DoLoadNursingPlanForCategoryRecordReq() {
        progressDialog.setMessage(getResources().getString(R.string.loadingdata));
        progressDialog.show();
        LoadNursingPlanForCategoryRecordBean bean = new LoadNursingPlanForCategoryRecordBean();
        bean.setPerformStatus(state + "");
        bean.setTemplateId(templateId);
        String url = UrlConstant.LoadNursingPlanForCategoryRecord() + bean.toString();
        CCLog.i("progressWorkFRA>>>>"+url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result.toString();
                JSONObject jsonObject=null;
                boolean resultFlag=false;
                try {
                    jsonObject=new JSONObject(result);
                    resultFlag=jsonObject.getBoolean("result");
                    if (resultFlag) {
                        Gson gson = new Gson();
                        LoadNursingPlanForCategoryRecordBean bean = gson.fromJson(result,
                                LoadNursingPlanForCategoryRecordBean.class);
                        Map<String, PlanTimeCountBean> retMap = bean.getPatient();
                        patientBeanList.clear();
                        timeList.clear();
                        progressList.clear();
                        for (String key : retMap.keySet()) {
                            for (int i = 0; i < myPatientList.size(); i++) {
                                if (key.equals(myPatientList.get(i).getPatId())) {
                                    patientBeanList.add(myPatientList.get(i));
                                    progressList.add(myPatientList.get(i).getName()+myPatientList.get(i).getBedLabel()+getResources().getString(R.string.bed));
                                    PlanTimeCountBean planTimeCountBean = retMap.get(key);
                                    if (planTimeCountBean != null) {
                                        timeList.add(planTimeCountBean.getTimePlanCount());
                                    }
                                    continue;
                                }
                            }

                        }
                        patAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    }else{
                        toErrorAct();
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getActivity(), R.string.getprogressworkfial, Toast.LENGTH_SHORT).show();
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
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_work);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
