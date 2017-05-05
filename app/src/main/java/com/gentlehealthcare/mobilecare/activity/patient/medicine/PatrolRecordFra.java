package com.gentlehealthcare.mobilecare.activity.patient.medicine;

import java.util.ArrayList;
import java.util.List;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.LoadVariationDictBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadVariationDictReq;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.MedicineExecptionDialog;
import com.gentlehealthcare.mobilecare.view.adapter.GridReasonAdapter;
import com.gentlehealthcare.mobilecare.view.adapter.MedicineListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author ouyang
 * @ClassName: PatrolRecord
 * @Description: 巡视记录
 * @date 2015年3月2日 上午9:06:26
 */
@SuppressLint("ValidFragment")
public class PatrolRecordFra extends BaseFragment {

    private String[] strings = {"正常", "过快", "过慢", "异常", "暂停"};
    private String reason = "";
    private String total = "";
    private GridReasonAdapter mAdapter;
    private SyncPatientBean patient;
    private int currentRecord;
    private MedicineExecptionDialog dialog;
    private String recordTxt;
    private ListView lv_medicine;
    private OrderItemBean orderItemBean = null;
    private ProgressDialog progressDialog = null;
    private List<LoadVariationDictBean> variationDictBeanList = null;
    private LoadVariationDictBean loadVariationDictBean;

    private boolean isException;

    public String getRecordTxt() {
        return recordTxt;
    }

    public PatrolRecordFra(SyncPatientBean patient, OrderItemBean orderItemBean) {
        super();
        this.patient = patient;
        this.orderItemBean = orderItemBean;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patrol_record, null);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载数据");
        DoLoadVariationDictReq();
        variationDictBeanList = new ArrayList<LoadVariationDictBean>();
        GridView gridView = (GridView) view.findViewById(R.id.gv_reason);

        gridView.setSelector(R.color.transparent);
        gridView.setAdapter(mAdapter = new GridReasonAdapter(getActivity(), strings));
        gridView.setOnItemClickListener(mItemClickListener);
        lv_medicine = (ListView) view.findViewById(R.id.lv_medicine);
        List<OrderItemBean> list = new ArrayList<OrderItemBean>();
        list.add(orderItemBean);
        lv_medicine.setAdapter(new MedicineListAdapter(getActivity(), list));
        currentRecord = 0;
        recordTxt = strings[0];
        return view;
    }

    OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position >= 3) {
                dialog = new MedicineExecptionDialog(PatrolRecordFra.this, R.style.myDialogTheme);
                dialog.setLoadVariationDictBeanList(variationDictBeanList);
                dialog.show();
                setException(true);
            } else {
                updateStr(0, "");
                setException(false);
            }
            currentRecord = position;
            recordTxt = strings[position];
            mAdapter.notifyChanged(position);
        }
    };


    public int getCurrentRecord() {
        return currentRecord;
    }

    public void updateStr(int index, String total) {
        setLoadVariationDictBean(variationDictBeanList.get(index));
        this.total = total;
    }

    public String getTotal() {
        return total;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissDialog();
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_patrol_recode);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 对话框是否显示
     *
     * @return
     */
    public boolean isShowDialog() {
        if (dialog != null && dialog.isShowing())
            return true;
        return false;
    }

    /**
     * 隐藏对话框
     */
    public void dismissDialog() {
        if (isShowDialog())
            dialog.dismiss();
    }

    /**
     * 点击确定按钮事件
     */
    public void DoOkButton() {
        dialog.doOkButton();
    }

    /**
     * 加载异常字典请求
     */
    private void DoLoadVariationDictReq() {
        progressDialog.show();
        RequestManager.connection(new LoadVariationDictReq(getActivity(), new IRespose<LoadVariationDictBean>() {
            @Override
            public void doResult(LoadVariationDictBean loadVariationDictBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {

                List<LoadVariationDictBean> loadVariationDictBeans = new Gson().fromJson(result, new
                        TypeToken<List<LoadVariationDictBean>>() {
                        }.getType());
                variationDictBeanList.clear();
                variationDictBeanList.addAll(loadVariationDictBeans);
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
        }, 0, true, new LoadVariationDictBean()));
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    public LoadVariationDictBean getLoadVariationDictBean() {
        return loadVariationDictBean;
    }

    public void setLoadVariationDictBean(LoadVariationDictBean loadVariationDictBean) {
        this.loadVariationDictBean = loadVariationDictBean;
    }

    public boolean isException() {
        return isException;
    }

    public void setException(boolean isException) {
        this.isException = isException;
    }
}
