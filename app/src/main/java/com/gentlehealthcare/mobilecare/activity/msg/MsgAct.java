package com.gentlehealthcare.mobilecare.activity.msg;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.adapter.MsgAdapter;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;
import com.gentlehealthcare.mobilecare.presenter.MsgPresenter;
import com.gentlehealthcare.mobilecare.swipe.view.XListView;
import com.gentlehealthcare.mobilecare.view.IMsgView;

import java.util.ArrayList;
import java.util.List;

public class MsgAct extends BaseActivity implements IMsgView, View.OnClickListener {
    public String patCode;
    public List<TipBean> executingOrders;
    MsgAdapter msgAdapter;
    XListView lv_orders;
    MsgPresenter presenter;
    TextView tv_close;
    TextView tv_patientinfo;
    TextView tv_date;
    TextView tv_sure_msg;
    TextView tv_pause;
    boolean isDo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        presenter = new MsgPresenter(this);
        presenter.receiveData();
        //handler.postDelayed(runnable, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.intialView();
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 5000);
            if (isDo) {
               finish();
            }
            handler.removeCallbacks(this);
        }
    };

    @Override
    protected void resetLayout() {

    }

    @Override
    public void receiveData() {
        if (executingOrders == null) {
            executingOrders = new ArrayList<TipBean>();
        }
        patCode = getIntent().getStringExtra(GlobalConstant.KEY_BARCODE);
        executingOrders = (List<TipBean>) getIntent().getSerializableExtra(GlobalConstant.KEY_EXECUTEING_ORDERS);
        presenter.getPatients(this, patCode);
    }

    @Override
    public void setPatientInfo(SyncPatientBean patientInfo) {
        tv_patientinfo = (TextView) findViewById(R.id.tv_patientinfo);
        String bed = patientInfo.getBedLabel();
        if (!bed.contains("床")) {
            bed = bed + "床";
        }
        tv_patientinfo.setText(bed + "  " + patientInfo.getName());
    }

    @Override
    public void intialView() {
        lv_orders = (XListView) findViewById(R.id.lv_orders);
        tv_close = (TextView) findViewById(R.id.tv_close);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_sure_msg = (TextView) findViewById(R.id.tv_sure_msg);
        tv_pause = (TextView) findViewById(R.id.tv_pause);
        tv_sure_msg.setOnClickListener(this);
        tv_pause.setOnClickListener(this);
        tv_close.setOnClickListener(this);

        if (msgAdapter == null) {
            msgAdapter = new MsgAdapter(this, executingOrders);
        }
        lv_orders.setAdapter(msgAdapter);
        lv_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isDo = false;
                if (GlobalConstant.isSwipeOpenMsg) {
                    //取消
                    executingOrders.remove(position-1);
                    if (executingOrders.size() == 0) {
                        finish();
                    }
                    //msgAdapter.notifyDataSetChanged(executingOrders);
                    msgAdapter.notifyDataSetChanged();
                } else {
                    //到场景
                }
                GlobalConstant.isSwipeOpenMsg = false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure_msg:
                handlerMsg("1");
                finish();
                break;
            case R.id.tv_pause:
                handlerMsg("-1");
                finish();
                break;
            case R.id.tv_close:
                finish();
                break;
        }
    }

    private void handlerMsg(String conformStatus) {
        if (executingOrders.isEmpty()) {
            finish();
        } else {
            StringBuffer sb=new StringBuffer();
            for (int i = 0; i < executingOrders.size(); i++) {
                sb.append(executingOrders.get(i).getNoticeId());
                if(i!=executingOrders.size()-1){
                    sb.append("|");
                }
            }
            presenter.handlerMsg(conformStatus, UserInfo.getUserName(),sb.toString());
        }
    }
}
