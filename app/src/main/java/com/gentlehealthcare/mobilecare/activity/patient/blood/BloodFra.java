package com.gentlehealthcare.mobilecare.activity.patient.blood;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.patient.insulin.InsulinFlowSheetFra;
import com.gentlehealthcare.mobilecare.constant.StateConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.BloodTestBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadLastDinnerTimeBean;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.BloodTestRequest;
import com.gentlehealthcare.mobilecare.net.impl.LoadLastDinnerTimeReq;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.gentlehealthcare.mobilecare.view.CheckedChangeRadioGroup;
import com.gentlehealthcare.mobilecare.view.LastMealBloodDialog;
import com.gentlehealthcare.mobilecare.view.WaitTimeBloodDialog;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

/**
 * 血糖检测主界面
 * Created by ouyang on 2015/5/25.
 */
@SuppressLint("ValidFragment")
public class BloodFra extends BaseFragment{
    private RadioGroup rg_blood_value;
    private CheckedChangeRadioGroup rg_tab;
    private RadioButton rbtn_blood_high,rbtn_blood_low;
    private EditText et_bloodvalue;
    private  AlertDialogOneBtn dialogOneBtn;
    private  int  value;
    private LastMealBloodDialog lastMealBloodDialog;
    private int type=0;
    private int h=-1,m=-1;
    private WaitTimeBloodDialog dialog;
    private StateConstant stateConstant;
    private ProgressDialog progressDialog=null;
    private String leastMealTime="";
    private boolean isShow=false;
    private SyncPatientBean syncPatientBean=null;
    private OrderItemBean orderItemBean=null;
    private int planTimeAttr=0;
    public BloodFra(OrderItemBean orderItemBean,SyncPatientBean syncPatientBean,StateConstant stateConstant) {
        this.stateConstant=stateConstant;
        this.orderItemBean=orderItemBean;
        this.syncPatientBean=syncPatientBean;
    }

    /**
     * 当选择最近用餐时间对话框提示,Back键返回到上一页
     * @return
     */
    public boolean isShowing(){
        return lastMealBloodDialog!=null&&lastMealBloodDialog.isShowing();
    }




    public int getValue(){
        return value;
    }

    public void setType(int type){
        this.type=type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.activity_blood_main,null);
        rg_tab= (CheckedChangeRadioGroup) view.findViewById(R.id.rg_tab);
        et_bloodvalue= (EditText) view.findViewById(R.id.et_bloodvalue);
        rg_blood_value= (RadioGroup) view.findViewById(R.id.rg_blood_value);
        rbtn_blood_high= (RadioButton) view.findViewById(R.id.rbtn_blood_high);
        rbtn_blood_low= (RadioButton) view.findViewById(R.id.rbtn_blood_low);
        value=0;
        progressDialog=new ProgressDialog(getActivity());
        rbtn_blood_high.setVisibility(View.GONE);
        rbtn_blood_low.setVisibility(View.GONE);
        if (stateConstant.equals(StateConstant.STATE_WAITING)){
            if (orderItemBean.getPlanTimeAttr()==null||"".equals(orderItemBean.getPlanStartTime())) {
                new AlertDialog.Builder(getActivity()).setTitle("请选择血糖检测类型").setCancelable(false).setItems(new String[]{"临时", "饭前", "饭后"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        planTimeAttr = i;
                        orderItemBean.setPlanTimeAttr(i + "");
                        int[] ids = new int[]{R.id.rbtn_linshi, R.id.rbtn_qian, R.id.rbtn_hou};
                        rg_tab.check(ids[i]);
                        //弹出选择最后一次用餐时间对话框
                        if (i != 0) {
                            if (lastMealBloodDialog == null)
                                lastMealBloodDialog = new LastMealBloodDialog(getActivity(), R.style.myDialogTheme, BloodFra.this);
                            lastMealBloodDialog.show();
                        } else {
                            BloodTestBean bloodTestBean = new BloodTestBean();
                            bloodTestBean.setPlanTimeAttr(orderItemBean.getPlanTimeAttr());
                            bloodTestBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
                            bloodTestBean.setPatId(syncPatientBean.getPatId());
                            DoBloodTestReq(bloodTestBean);
                        }
                    }
                }).create().show();
            }else{
                planTimeAttr=Integer.valueOf(orderItemBean.getPlanTimeAttr());
                if (lastMealBloodDialog==null)
                lastMealBloodDialog = new LastMealBloodDialog(getActivity(), R.style.myDialogTheme, BloodFra.this);
                lastMealBloodDialog.show();
            }
        }else if (stateConstant.equals(StateConstant.STATE_EXECUTING)){
            //获取最后一次用餐时间
            planTimeAttr=Integer.valueOf(orderItemBean.getPlanTimeAttr());
            DoLoadLastDinnerTimeReq();
        }


     et_bloodvalue.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
             if (event.getAction() == MotionEvent.ACTION_DOWN) {
                 //判断是否已经超过规定时间，可录入血糖值
                 if (planTimeAttr!= -1&&planTimeAttr!=0) {
                     if (stateConstant==StateConstant.STATE_EXECUTING) {
                         if (!"".equals(leastMealTime)) {
                             long nowtime = System.currentTimeMillis();
                             long eventStartTime = DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS, leastMealTime);
                             long time = nowtime - eventStartTime;
                             time /= 1000 * 60;
                             h = (int) time / (60);
                             time -= 60 * h;
                             m = (int) time;
                         }else{
                             h=m=0;
                         }

                     }else if (stateConstant==StateConstant.STATE_WAITING) {
                            if (!leastMealTime.equals("")) {
                                long nowtime = System.currentTimeMillis();
                                String string=leastMealTime.replace("T"," ");
                                long eventStartTime = DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS, string);
                                long time = nowtime - eventStartTime;
                                time /= 1000*60;
                                h = (int) time / (60 );
                                time -= 60  * h;
                                m = (int) time ;
                            }else
                                h=m=0;
                     }


                     int leastH = 0;
                     String str = "";
                     boolean flag = false;
                     if (planTimeAttr == 1) {
                         leastH = 3;
                         flag = h >= leastH;
                         str = "3小时";
                     } else if (planTimeAttr == 2) {
                         leastH = 2;
                         flag = h >= leastH;
                         str = "2小时";
                     } else {
                         leastH = 0;
                         flag = h > 0 || (h == 0 && m >= 30);
                         str = "半小时";
                     }
                     if (flag) {
                             isShow=true;
                         dialogOneBtn = new AlertDialogOneBtn(getActivity());
                         dialogOneBtn.setTitle("提示");
                         StringBuffer stringBuffer = new StringBuffer();
//                         stringBuffer.append("距离上次进餐时间有：" + (h < 10 ? ("0" + h) : h) + ":" + (m < 10 ? ("0" + m) : m) + "\n");
//                         stringBuffer.append("距离上次进餐时间");
                         stringBuffer.append("已超过规定的" + str + ",可以进行");
                         if (type == 0) {
                             stringBuffer.append("饭前");
                         } else if (type == 1) {
                             stringBuffer.append("饭后");
                         } else {
                             stringBuffer.append("临时");
                         }
                         stringBuffer.append("血糖检测.");
                         dialogOneBtn.setButtonListener(false, new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 et_bloodvalue.setEnabled(true);
                                 dialogOneBtn.dismiss();
                                 type = -1;
                             }
                         });
                         dialogOneBtn.setMessage(stringBuffer.toString());
                         dialogOneBtn.setButton("知道了");
                     } else if (!flag){
                         int waitMinute = 60 - m;
                         int waitHour = leastH - 1 - h;
                          dialog = new WaitTimeBloodDialog(getActivity(), R.style.myDialogTheme, BloodFra.this);
                         dialog.setWaitTime(waitHour, waitMinute);
                         dialog.show();

                     }
                 }else{
                     et_bloodvalue.setFocusable(true);
                 }
             }
             return false;
         }

     });

        if (planTimeAttr==0)
            rg_tab.check(R.id.rbtn_linshi);
        else if (planTimeAttr==1)
        rg_tab.check(R.id.rbtn_qian);
        else if (planTimeAttr==2) {
            rg_tab.check(R.id.rbtn_hou);


        }

       rg_tab.setCanChange(false);
        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                et_bloodvalue.setText("");
                rbtn_blood_low.setBackgroundResource(R.drawable.blood_tab_unselect);
                rbtn_blood_high.setBackgroundResource(R.drawable.blood_tab_unselect);
//                et_bloodvalue.setFocusable(false);
                value=0;
//                switch (checkedId){
//                    case R.id.rbtn_qian:
//                        type=0;
//                        if (h==-1) {
//                            lastMealBloodDialog = new LastMealBloodDialog(getActivity(), R.style.myDialogTheme, BloodFra.this);
//                            lastMealBloodDialog.show();
//                        }
//                            break;
//                    case R.id.rbtn_hou:
//                        type=1;
//                        if (h==-1) {
//                            lastMealBloodDialog = new LastMealBloodDialog(getActivity(), R.style.myDialogTheme, BloodFra.this);
//                            lastMealBloodDialog.show();
//                        }
//                        break;
//                    case R.id.rbtn_linshi:
//                        type=2;
//                        if (h==-1) {
//                            lastMealBloodDialog = new LastMealBloodDialog(getActivity(), R.style.myDialogTheme, BloodFra.this);
//                            lastMealBloodDialog.show();
//                        }
//                        break;
                }
//            }
        });
        return view;
    }

    /**
     * 提交最后一次用餐时间
     * @param bloodTestBean
     */
    private void DoBloodTestReq(final BloodTestBean bloodTestBean){

        progressDialog.setMessage("正在提交数据,请稍等");
        progressDialog.show();
        RequestManager.connection(new BloodTestRequest(getActivity(), new IRespose<BloodTestBean>() {
            @Override
            public void doResult(BloodTestBean bloodTestBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException{
                progressDialog.dismiss();
                BloodTestBean info = new Gson().fromJson(result, BloodTestBean.class);
                    ShowToast(info.getMsg());
                if(info.isResult()) {
                    String lastDinnerTime = info.getLastDinnerTime();
                    InsulinFlowSheetFra fra = (InsulinFlowSheetFra) getParentFragment();
                    fra.DoLoadInsulinSnapshotRequest();
                }else{
                    ShowToast(info.getMsg());
                }
                if(lastMealBloodDialog!=null)
                lastMealBloodDialog.dismiss();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                else{
                    toErrorAct();
                }
            }
        },0,true,bloodTestBean));
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }


    /**
     * 获取最近一次进餐时间
     * @param hour 小时
     * @param minute 分钟
     */
    public void GetLastMeal(int hour,int minute){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        int nowHour=calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute=calendar.get(Calendar.MINUTE);
         h=nowHour-hour;
        if (h<0)
            h+=24;//进餐时间为昨天
         m=nowMinute-minute;
        if (m<0){
            if (h==0){
                h+=24;
            }
            h-=1;

            m+=60;
        }
        Toast.makeText(getActivity(),"距离进餐时间有："+h+"小时"+m+"分钟",Toast.LENGTH_LONG).show();
        long time=System.currentTimeMillis()-1000*60*m-1000*60*60*h;
          BloodTestBean bloodTestBean=new BloodTestBean();
        bloodTestBean.setLastDinnerTime(leastMealTime=DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS_STYPE,time));
        bloodTestBean.setPlanTimeAttr(orderItemBean.getPlanTimeAttr());
        bloodTestBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        bloodTestBean.setPatId(syncPatientBean.getPatId());
        DoBloodTestReq(bloodTestBean);


    }

    public String GetBloodValue(){
        return et_bloodvalue.getText().toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialogOneBtn!=null&&dialogOneBtn.isShowing()){
            dialogOneBtn.dismiss();
        }
        if (lastMealBloodDialog!=null&&lastMealBloodDialog.isShowing())
            lastMealBloodDialog.dismiss();
        if (dialog!=null&&dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view
                .findViewById(R.id.root_fra_blood_main);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 获取最后一次用餐时间
     */
    private void DoLoadLastDinnerTimeReq(){
        progressDialog.setMessage("正在获取数据,请稍后...");
        progressDialog.show();
        LoadLastDinnerTimeBean loadLastDinnerTimeBean=new LoadLastDinnerTimeBean();
        loadLastDinnerTimeBean.setPatId(syncPatientBean.getPatId());
        loadLastDinnerTimeBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        RequestManager.connection(new LoadLastDinnerTimeReq(getActivity(), new IRespose<LoadLastDinnerTimeBean>() {
            @Override
            public void doResult(LoadLastDinnerTimeBean loadLastDinnerTimeBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {
                LoadLastDinnerTimeBean loadLastDinnerTimeBean=new Gson().fromJson(result,LoadLastDinnerTimeBean.class);
                if (loadLastDinnerTimeBean.isResult()){
                    leastMealTime=loadLastDinnerTimeBean.getLastDinnerTime();
                }else{
                    ShowToast(loadLastDinnerTimeBean.getMsg());
                }
                progressDialog.dismiss();
            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                else{
                    toErrorAct();
                }
            }
        },0,true,loadLastDinnerTimeBean));
    }
}
