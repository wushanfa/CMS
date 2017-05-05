package com.gentlehealthcare.mobilecare.activity.patient.insulin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.CheckMedicineBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadInspectionTimeBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadIntravsSnapshotBean;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.CheckMedicineRequest;
import com.gentlehealthcare.mobilecare.net.impl.LoadInspectionTimeReq;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.MedicineListAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: PatrolCountDown 
 * @Description: 给药 巡视计时 
 * @author ouyang
 * @date 2015年3月2日 上午9:08:01 
 *
 */
@SuppressLint("ValidFragment")
public class InsulinPatrolCountDownFra extends BaseFragment  implements OnClickListener{


	private long time;
    private long visitime;

	private TextView tvTime1, tvTime2, tvTime3, tvTime4;
    private TextView tv_starttime;
    private OrderItemBean orderItemBean=null;
    private SyncPatientBean patient=null;

    private boolean isruning=false;
    private static final int UPDATE=0X00EF;
    private ProgressDialog progressDialog=null;
    private ListView lv_medicine;
    private RelativeLayout ll_checkmedicine;

    private boolean hasCheck;

    public InsulinPatrolCountDownFra(){}

	public InsulinPatrolCountDownFra(SyncPatientBean patient, OrderItemBean orderItemBean,boolean hasCheck) {

		super();
		this.patient = patient;
        this.orderItemBean=orderItemBean;
        this.hasCheck=hasCheck;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_patrol_count_down, null);
        progressDialog=new ProgressDialog(getActivity());
        DoLoadInspectionTimeReq();
        ll_checkmedicine= (RelativeLayout) view.findViewById(R.id.ll_checkmedicine);
        if (!hasCheck)
            ll_checkmedicine.setVisibility(View.GONE);
        tv_starttime= (TextView) view.findViewById(R.id.tv_injectionTime);
		tvTime1 = (TextView) view.findViewById(R.id.tv_time);
		tvTime2 = (TextView) view.findViewById(R.id.tv_count_down);
		tvTime3 = (TextView) view.findViewById(R.id.tv_count_down1);
		tvTime4 = (TextView) view.findViewById(R.id.tv_count_down2);
		tvTime2.setText("等待时间:  ");
		tvTime4.setText("");
        lv_medicine= (ListView) view.findViewById(R.id.lv_medicine);
        Button btn_saomiao= (Button) view.findViewById(R.id.btn_saomiao);
        btn_saomiao.setOnClickListener(this);
        btn_saomiao.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final EditText editText = new EditText(getActivity());
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                editText.setLayoutParams(lp);
                editText.setText(patient.getPatCode());
                editText.setHint("手动输入病人腕带编号");
                new AlertDialog.Builder(getActivity()).setTitle("提示").setCancelable(false).setView(editText).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editText.getText().toString().equals("")) {
                            ShowToast("请输入编号值");
                        } else {
                            DoMedicineCheckReq();
                        }
                    }
                }).setPositiveButton("取消", null).create().show();
                return false;
            }
        });
        List<OrderItemBean> list=new ArrayList<OrderItemBean>();
        list.add(orderItemBean);
        lv_medicine.setAdapter( new MedicineListAdapter(getActivity(), list));

		return view;
	}


	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE:
                    tvTime3.setText((String)msg.obj);
                    break;
            }
        }
    };

    /**
     * 格式化时间
     * @param time
     * @return
     */
	public String getTime(long time) {
		long h = time / 3600;
		time = time % 3600;
		long m = time / 60;
		time = time % 60;
		long s = time;
		return getTimeString(h) + ":" + getTimeString(m) + ":" + getTimeString(s);
	}


    /**
     * Long 转换String
     * @param l
     * @return
     */
	public String getTimeString(long l) {
		if (l < 10)
			return  "0" + l;
		else
			return String.valueOf(l);
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isruning=false;
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view
                .findViewById(R.id.root_fra_patrol_count);
        SupportDisplay.resetAllChildViewParam(root);
    }


    @Override
    public void onClick(View v) {
         if(v.getId()==R.id.btn_saomiao){
             DoMedicineCheckReq();
         }
    }


    /**
     * 获取巡视时间和注射时间
     */
    private void DoLoadInspectionTimeReq(){
        if (progressDialog==null)
            return;
        progressDialog.setMessage("正在加载数据,请提交.");
        progressDialog.show();
        final LoadInspectionTimeBean loadInspectionTimeBean=new LoadInspectionTimeBean();
        loadInspectionTimeBean.setPatId(patient.getPatId());
        loadInspectionTimeBean.setType(1);
        loadInspectionTimeBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        RequestManager.connection(new LoadInspectionTimeReq(getActivity(), new IRespose<LoadInspectionTimeBean>() {
            @Override
            public void doResult(LoadInspectionTimeBean loadInspectionTimeBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {
                LoadInspectionTimeBean loadInspectionTimeBean=new Gson().fromJson(result,LoadIntravsSnapshotBean.class);
                if (loadInspectionTimeBean.isResult()){
                    long visitTime=DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS,loadInspectionTimeBean.getInspectionTime());
                    tv_starttime.setText("注射时间:  " + DateTool.getHourMinuteSecond(DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS,loadInspectionTimeBean.getEventStartTime())+""));
                    tvTime1.setText("巡视时间:  "+ DateTool.getHourMinuteSecond(visitTime+""));
                    long nowTime=System.currentTimeMillis();
                    time=visitTime-nowTime;
                    if (time>0){
                        time=time/1000;
                        isruning=true;
                    }else{
                        tvTime3.setText(getTime(0));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isruning){
                                if (time <= 0) {
                                    time = 0;
                                    isruning=false;
                                }
                                Message msg= handler.obtainMessage(UPDATE);
                                msg.obj=getTime(time);
                                handler.sendMessage(msg);
                                time--;
                                try {
                                    Thread.sleep(1*1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }else{
                    ShowToast(loadInspectionTimeBean.getMsg());
                }
                if (progressDialog!=null&&progressDialog.isShowing()&&getActivity()!=null&&!getActivity().isFinishing())
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
        },0,true,loadInspectionTimeBean));

    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }


    /**
     * 执行核对药品接口
     */
    private void DoMedicineCheckReq(){
        progressDialog.setMessage("正在提交数据，请稍后...");
        progressDialog.show();
        CheckMedicineBean checkMedicineBean=new CheckMedicineBean();
        checkMedicineBean.setPatId(patient.getPatId());
        checkMedicineBean.setPlanOrderNo(orderItemBean.getPlanOrderNo());
        checkMedicineBean.setType(1);
        RequestManager.connection(new CheckMedicineRequest(getActivity(), new IRespose<CheckMedicineBean>() {
            @Override
            public void doResult(CheckMedicineBean checkMedicineBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException{
                CheckMedicineBean checkMedicineBean= new Gson().fromJson(result,CheckMedicineBean.class);
                if (checkMedicineBean.getResult()){
                    DoMedicineList();
                }else{
                    ShowToast(checkMedicineBean.getMsg());
                }
                progressDialog.dismiss();
            }

            @Override
            public void doException(Exception e,boolean networkState) {
                progressDialog.dismiss();
                if (networkState)
                    Toast.makeText(getActivity(), R.string.unstable, Toast.LENGTH_SHORT).show();
                else {
                    toErrorAct();
                }
            }
        }, 0, true, checkMedicineBean));

    }


    /**
     * 扫描药品列表
     */
    private void DoMedicineList(){
        Toast.makeText(getActivity(),"药品扫描成功",Toast.LENGTH_SHORT).show();
        InsulinFlowSheetFra flowSheetFra= (InsulinFlowSheetFra) getParentFragment();
        flowSheetFra.showBottomBtn();
    }
}
