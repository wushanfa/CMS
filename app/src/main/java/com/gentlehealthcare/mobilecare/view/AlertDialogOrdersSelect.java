package com.gentlehealthcare.mobilecare.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.swipe.view.XListView;
import com.gentlehealthcare.mobilecare.view.adapter.OrdersSelectAdapter;

import java.util.List;

/**
 * Created by zyy on 2016/08/19.
 */
public class AlertDialogOrdersSelect extends Dialog implements AdapterView.OnItemClickListener{

    private Context context;

    private OrdersSelectAdapter adapter;

    private XListView listView;

    private MySnListener mysnlistener;

    private List<BloodProductBean2> bloodProductBean2s;

    public AlertDialogOrdersSelect(Context context, MySnListener mysnlistener, List<BloodProductBean2>
            orderListBeanList) {
        super(context);
        this.context = context;
        this.mysnlistener = mysnlistener;
        this.bloodProductBean2s = orderListBeanList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_selectblood);
        /** 单击dialog外dismiss dialog*/
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width=WindowManager.LayoutParams.FILL_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;


        getWindow().setAttributes(params);
        getWindow().getDecorView().setPadding(0,0,0,0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setGravity(Gravity.BOTTOM);

        initView();
        listView.setAdapter(adapter = new OrdersSelectAdapter(context, bloodProductBean2s));
        listView.setOnItemClickListener(this);
        listView.setPullLoadEnable(false);
    }

    private void initView() {
        listView = (XListView) findViewById(R.id.lv_orders);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mysnlistener.myOnItemClick(view, position, id);
        this.dismiss();
    }

    public interface MySnListener {
        void myOnItemClick(View view, int position, long id);
    }
}
