package com.gentlehealthcare.mobilecare.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.view.adapter.OrdersAdapter;

import java.util.List;

/**
 * Created by zhiwei on 2016/4/19.
 */
public class MyPatientDialog extends Dialog implements AdapterView.OnItemClickListener ,View.OnClickListener{

    private Context context;

    private OrdersAdapter adapter;

    private ListView listView;

    private MySnListener mysnlistener;

    private List<SyncPatientBean> patients;

    private int whichPatients;

    TextView tv_head;

    public MyPatientDialog(Context context, MySnListener mysnlistener, int whichPatients, List<SyncPatientBean>
            patients) {
        super(context);
        this.context = context;
        this.mysnlistener = mysnlistener;
        this.whichPatients = whichPatients;
        this.patients = patients;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_parent_list);
        /** 单击dialog外dismiss dialog*/
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.dimAmount = 0.3f;
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setWindowAnimations(R.style.mykeyboard);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(700, 1000);

        initView();

        listView.setAdapter(adapter = new OrdersAdapter(context, patients));
        listView.setOnItemClickListener(this);
        listView.setSelectionFromTop(whichPatients, 0);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.lv_orders);
        tv_head= (TextView) findViewById(R.id.tv_head);
        tv_head.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mysnlistener.myOnItemClick(view, position, id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_head:
                mysnlistener.onRefresh();
                break;
        }
    }

    public interface MySnListener {
        void myOnItemClick(View view, int position, long id);
        void onRefresh();
    }
}
