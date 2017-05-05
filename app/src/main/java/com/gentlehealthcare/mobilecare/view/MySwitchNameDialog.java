package com.gentlehealthcare.mobilecare.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.view.adapter.GridViewICUAdapter;

import java.util.List;

/**
 * Created by zhiwei on 2015/11/7.
 */
public class MySwitchNameDialog extends Dialog implements AdapterView.OnItemClickListener {

    private mySwitchNameListener mysnlistener;
    private GridView gv_patient;
    private GridViewICUAdapter adapter;
    private List<SyncPatientBean> patients;
    private Context context;
    private int marginTop = 127;

    /**
     * 哪个被选择病人
     */
    private int whichPatients = 0;


    public MySwitchNameDialog(Context context, int theme, mySwitchNameListener mysnlistener, List<SyncPatientBean>
            patients, int whichPatients) {
        super(context, theme);
        this.context = context;
        this.mysnlistener = mysnlistener;
        this.patients = patients;
        this.whichPatients = whichPatients;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.grid_patient_list_relationlayout);
        /** 单击dialog外dismiss dialog*/
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.TOP;
        params.y = marginTop;
        params.dimAmount = 0.0f;
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setWindowAnimations(R.style.mykeyboard);

        initView();

        gv_patient.setAdapter(adapter = new GridViewICUAdapter(context, patients, whichPatients));
        gv_patient.setOnItemClickListener(this);

    }

    private void initView() {
        gv_patient = (GridView) findViewById(R.id.gv_patient);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /**
         * 调用onitemclick方法的时候传递三个params
         */
        mysnlistener.myOnItemClick(view, position, id, adapter, gv_patient);

    }

    public void setWhichPatients(int whichPatients) {
        this.whichPatients = whichPatients;
    }

    public interface mySwitchNameListener {
        /**
         * 自定义的接口调用方法
         *
         * @param view
         * @param position
         * @param id
         */
        public void myOnItemClick(View view, int position, long id, GridViewICUAdapter adapter, GridView gv_patient);
    }
}
