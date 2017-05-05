package com.gentlehealthcare.mobilecare.activity.patient.insulin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.InjectionPatrolAdapter;

/**
 * 胰岛素巡视界面
 * Created by ouyang on 2015/6/10.
 */
@SuppressLint("ValidFragment")
public class InsulinPatrolFra extends BaseFragment {

    public String[] allergy={"红晕","肿胀","紫癜","硬块","疼痛","荨麻疹"};//过敏
    public String[] lowBloodSugar={"头晕","心慌","饥饿感","无力","抽搐","出冷汗","昏迷"};//低血糖
    public String[] skin={"皮下出血","瘀斑","硬结"};//注射部位皮肤
    private InjectionPatrolAdapter gouminAdapter,dixuetangAdapter,skinAdapter,normalAdapter,otherAdapter;
    private GridView gouminGv,dixuetangGv,skinGv,gv_normal,gv_other;
    private String txtStr="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fra_insulinpatrol,null);
        gv_normal= (GridView) rootView.findViewById(R.id.gv_normal);
        gv_other= (GridView) rootView.findViewById(R.id.gv_other);
        gouminGv= (GridView) rootView.findViewById(R.id.gv_guomin);
        dixuetangGv= (GridView) rootView.findViewById(R.id.gv_dixuetang);
        skinGv= (GridView) rootView.findViewById(R.id.gv_pifu);
        gouminGv.setAdapter(gouminAdapter=new InjectionPatrolAdapter(getActivity(),allergy));
        dixuetangGv.setAdapter(dixuetangAdapter=new InjectionPatrolAdapter(getActivity(),lowBloodSugar));
        skinGv.setAdapter(skinAdapter=new InjectionPatrolAdapter(getActivity(),skin));
        gv_normal.setAdapter(normalAdapter=new InjectionPatrolAdapter(getActivity(),new String[]{"正常"}));
        gv_other.setAdapter(otherAdapter=new InjectionPatrolAdapter(getActivity(),new String[]{"其他"}));
        gouminAdapter.notifyChanged(-1);
        dixuetangAdapter.notifyChanged(-1);
        skinAdapter.notifyChanged(-1);
        normalAdapter.notifyChanged(0);
        otherAdapter.notifyChanged(-1);
        txtStr="正常";
        gv_normal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                normalAdapter.notifyChanged(i);
                gouminAdapter.notifyChanged(-1);
                dixuetangAdapter.notifyChanged(-1);
                skinAdapter.notifyChanged(-1);
                otherAdapter.notifyChanged(-1);
                txtStr="正常";

            }
        });
        gv_other.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                otherAdapter.notifyChanged(i);
                gouminAdapter.notifyChanged(-1);
                dixuetangAdapter.notifyChanged(-1);
                skinAdapter.notifyChanged(-1);
                normalAdapter.notifyChanged(-1);
                txtStr="其他";
            }
        });
        gouminGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gouminAdapter.notifyChanged(position);
                dixuetangAdapter.notifyChanged(-1);
                skinAdapter.notifyChanged(-1);
                otherAdapter.notifyChanged(-1);
                normalAdapter.notifyChanged(-1);
                txtStr=allergy[position];
            }
        });
        dixuetangGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gouminAdapter.notifyChanged(-1);
                dixuetangAdapter.notifyChanged(position);
                skinAdapter.notifyChanged(-1);
                otherAdapter.notifyChanged(-1);
                normalAdapter.notifyChanged(-1);
                txtStr=lowBloodSugar[position];
            }
        });
        skinGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gouminAdapter.notifyChanged(-1);
                dixuetangAdapter.notifyChanged(-1);
                skinAdapter.notifyChanged(position);
                otherAdapter.notifyChanged(-1);
                normalAdapter.notifyChanged(-1);
                txtStr=skin[position];
            }
        });

        return  rootView;

    }




    public String getTxtStr(){

        return txtStr;
    }

    @Override
    protected void resetLayout(View view) {
        ScrollView root = (ScrollView) view
                .findViewById(R.id.root_fra_insupatrol);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
