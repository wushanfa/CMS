package com.gentlehealthcare.mobilecare.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.notification.IntravenouslySealingFra;
import com.gentlehealthcare.mobilecare.net.bean.LoadVariationDictBean;

import java.util.List;

/**
 * 药品异常提示框
 * Created by zyy on 2015/5/29.
 */
public class IntravenouslySealingDialog extends Dialog implements View.OnClickListener{
    private IntravenouslySealingFra context;
    private EditText et_total;
    private Button btn_choosereason;
    private List<LoadVariationDictBean> loadVariationDictBeanList=null;
    /**
     * 异常或者暂停原因
     */
    private String[] reason=null;
    private int positon=-1;

    public IntravenouslySealingDialog(IntravenouslySealingFra context) {
        super(context.getActivity());
        this.context=context;
        init();

    }

    public IntravenouslySealingDialog(IntravenouslySealingFra context, int theme) {
        super(context.getActivity(), theme);
        this.context=context;
        init();
    }


    private void init(){
        setCancelable(false);
        View rootView= LayoutInflater.from(context.getActivity()).inflate(R.layout.activity_medicine_execption,null);
        setContentView(rootView);
        et_total= (EditText) rootView.findViewById(R.id.et_total);
        btn_choosereason= (Button) rootView.findViewById(R.id.btn_choosereason);
        btn_choosereason.setOnClickListener(this);
        rootView.findViewById(R.id.btn_ok).setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        int result=0;
        switch (v.getId()){
            case R.id.btn_choosereason:

            new AlertDialog.Builder(getContext()).setItems(reason, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                     positon=which;
                    btn_choosereason.setText(reason[which]);

                }
            }).create().show();
                break;
            case R.id.btn_ok:
                doOkButton();
                break;

        }
    }

    public int getPositon() {
        return positon;
    }

    /**
     * 确定按钮点击事件
     */
    public void doOkButton(){
        if (positon==-1){
            Toast.makeText(context.getActivity(),"请选择理由..",Toast.LENGTH_SHORT).show();
        }else if (et_total.getText().toString().equals("")){
            Toast.makeText(context.getActivity(),"请填写总量..",Toast.LENGTH_SHORT).show();
        }else{
            context.updateStr(positon,et_total.getText().toString());
            dismiss();
        }
    }

    public List<LoadVariationDictBean> getLoadVariationDictBeanList() {
        return loadVariationDictBeanList;
    }

    public void setLoadVariationDictBeanList(List<LoadVariationDictBean> loadVariationDictBeanList) {
        reason=new String[loadVariationDictBeanList.size()];
        for (int i=0;i<loadVariationDictBeanList.size();i++){
            LoadVariationDictBean info=loadVariationDictBeanList.get(i);
            reason[i]=info.getItemContent();
        }
        this.loadVariationDictBeanList = loadVariationDictBeanList;
    }
}