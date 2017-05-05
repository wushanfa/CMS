package com.gentlehealthcare.mobilecare.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.patient.insulin.InjectionSiteFra;
import com.gentlehealthcare.mobilecare.activity.patient.insulin.InsulinPlaceFra;
import com.gentlehealthcare.mobilecare.net.bean.PlaceStatusBean;
import com.gentlehealthcare.mobilecare.net.bean.PlaceStatusInfo;
import com.gentlehealthcare.mobilecare.net.bean.SetStatusForAreaBean;
import com.gentlehealthcare.mobilecare.view.adapter.CopySealAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.InjectionSiteAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.InjectionSiteTxtAdapter;

import java.util.List;

/**
 * 注射部位选择框
 * Created by ouyang on 2015/6/10.
 */
public class InjectionSiteDialog extends Dialog implements View.OnClickListener{
    private ListView lv_injection;
    private GridView gv_injection;
    private TextView tv_injection;
    private InjectionSiteAdapter adapter=null;
    private InjectionSiteTxtAdapter txtAdapter=null;
    private InsulinPlaceFra insulinPlaceFra=null;
   private List<PlaceStatusBean> items=null;
    private boolean isLongOnclick=false;
    private boolean isinjectionSite;
    private ProgressDialog progressDialog;
    private String area;



    public InjectionSiteDialog(BaseActivity context) {
        super(context);
        init(context);
    }

    public InjectionSiteDialog(Context context, int theme) {
        super(context, theme);
        init(context);

    }

    protected InjectionSiteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context){
        progressDialog=new ProgressDialog(context);
        setCancelable(false);
        View rootview= LayoutInflater.from(context).inflate(R.layout.dialog_injectionsite,null);
        setContentView(rootview);
        lv_injection= (ListView) rootview.findViewById(R.id.lv_injectionsite);
        gv_injection= (GridView) rootview.findViewById(R.id.gv_injectionsite);
        tv_injection= (TextView) rootview.findViewById(R.id.tv_injectionsite);

        rootview.findViewById(R.id.btn_finish).setOnClickListener(this);
        rootview.findViewById(R.id.btn_cancel).setOnClickListener(this);

//getWindow().setBackgroundDrawable();
    }


    public void setStyle(int style){
        if (style==0){
            lv_injection.setVisibility(View.GONE);
        }else{
            lv_injection.setVisibility(View.VISIBLE);
        }
    }


    public void setInjectionSiteAdapter(){
    String[] str=null;
//        if (isinjectionSite){
            str=new String[]{"改本次打","区域拒打","区域禁打"};
//        }else{
//            str=new String[]{"区域拒打","区域禁打"};
//        }
        adapter=new InjectionSiteAdapter(getContext(),str);
        lv_injection.setAdapter(adapter);
        lv_injection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.notifyChanged(position);
            }
        });
    }

    public void setItems(List<PlaceStatusBean> items,int numCloumns,String siteNo){
        this.items=items;
        gv_injection.setNumColumns(numCloumns);
        txtAdapter=new InjectionSiteTxtAdapter(items,getContext());
        txtAdapter.setSiteNo(siteNo);
        gv_injection.setAdapter(txtAdapter);
        gv_injection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                txtAdapter.notifyByPosition(position);

            }
        });

    }

    public void setTitle(String title){
        area=title;
        if(title.equals("A")){
            title="左上臂";
        }else if (title.equals("B")){
            title="右上臂";
        }else if (title.equals("C")){
            title="左上腹";
        }else if (title.equals("D")){
            title="右上腹";
        }else if (title.equals("E")){
            title="左下腹";
        }else if (title.equals("F")){
            title="右下腹";
        }else if(title.equals("G")){
            title="左大腿";
        }else {
            title="右大腿";
        }
        tv_injection.setText(title);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_finish:
                DoDialogFinish();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    public InsulinPlaceFra getInsulinPlaceFra() {
        return insulinPlaceFra;
    }

    public void setInsulinPlaceFra(InsulinPlaceFra insulinPlaceFra) {
        this.insulinPlaceFra = insulinPlaceFra;
    }

    /**
     * 确定按钮
     */
    public void DoDialogFinish(){
        if (isLongOnclick&&adapter.getPosition()!=-1){
            int position=adapter.getPosition();
            String operNo="";
            if (position==0) {
                getInsulinPlaceFra().DoSetStatusForSiteNo("-98",area);
            }else {
                if (position == 1) {
                    operNo = "-98";
                } else if (position == 2) {
                    operNo = "-99";
                }
                getInsulinPlaceFra().DoSetStatusForArea(area, operNo);
            }
        } else
            getInsulinPlaceFra().injectionSite(tv_injection.getText().toString() ,txtAdapter.getSiteNo(),isinjectionSite);
        dismiss();
    }





    public boolean isIsinjectionSite() {
        return isinjectionSite;
    }

    public void setIsinjectionSite(boolean isinjectionSite) {
        this.isinjectionSite = isinjectionSite;
    }

    public boolean isLongOnclick() {
        return isLongOnclick;
    }

    public void setLongOnclick(boolean isLongOnclick) {
        this.isLongOnclick = isLongOnclick;
    }
}
