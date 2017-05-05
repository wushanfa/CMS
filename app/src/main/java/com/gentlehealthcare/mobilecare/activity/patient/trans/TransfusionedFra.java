package com.gentlehealthcare.mobilecare.activity.patient.trans;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.TransfusionedAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zyy
 * @date 2015-9-18下午2:30:51
 * @category 已完成血品列表界面
 */
@SuppressLint("ValidFragment")
public class TransfusionedFra extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "TransfusionedFra";
    /**
     * 血品列表
     */
    private PullToRefreshListView lv_bloodproducts;
    /**
     * 血品适配器
     */
    private TransfusionedAdapter transfusionedAdapter;
    /**
     * 血品集合
     */
    public List<BloodProductBean2> mBloodProducts = null;
    /**
     * 加载条
     */
    private ProgressDialog progressDialog = null;
    /**
     * 同步病人数据
     */
    private SyncPatientBean patient = null;
    /**
     * 列表
     */
    ListView listView = null;

    private ImageButton imbtnback;

    private TextView tv_title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBloodProducts = new ArrayList<BloodProductBean2>();
        patient = (SyncPatientBean) getArguments().getSerializable(GlobalConstant.KEY_PATIENT);
        LoadBloodProducts(patient.getPatId(), "9");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_transfusioned, null);
        imbtnback = (ImageButton) view.findViewById(R.id.imbtn_back);
        imbtnback.setOnClickListener(this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(patient.getBedLabel() + "床输血记录");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("数据加载...");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv_bloodproducts = (PullToRefreshListView) getActivity().findViewById(R.id.lv_transfusionexcuted);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing() || progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_trans_end);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 加载已完成血品列表
     */
    private void LoadBloodProducts(String patId, String bloodStatus) {
        mBloodProducts.clear();
        CCLog.i(TAG, UrlConstant.LoadBloodProduct() + patId + "&bloodStatus=" + bloodStatus);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.LoadBloodProduct()
                        + patId + "&bloodStatus=" + bloodStatus,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject object = null;
                        JSONArray jsonArray = null;
                        Boolean result = false;
                        String msg = null;
                        try {
                            object = new JSONObject(arg0.result);
                            result = object.getBoolean("result");
                            msg = object.getString("msg");
                            if (result) {

                                jsonArray = object.getJSONArray("orders");
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<BloodProductBean2>>() {
                                }.getType();

                                mBloodProducts = gson.fromJson(
                                        jsonArray.toString(), type);

                                listView = lv_bloodproducts.getRefreshableView();
                                listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                                listView.setAdapter(transfusionedAdapter = new TransfusionedAdapter(
                                        getActivity()));
                                transfusionedAdapter.addAll(mBloodProducts);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> arg0,
                                                            View arg1, int arg2, long arg3) {
                                        arg2 -= 1;
                                        //arg1.setBackgroundResource(R.drawable.skin_btnblue_normal);
                                        String planOrderNo = mBloodProducts.get(arg2).getPlanOrderNo();
                                        if (patient != null && planOrderNo != null) {
                                            TransfusionFifteenFra transfusionFifteenFra = new
                                                    TransfusionFifteenFra();
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(GlobalConstant.KEY_PATIENT, patient);
                                            bundle.putString(GlobalConstant.KEY_PLANORDERNO, planOrderNo);
                                            transfusionFifteenFra.setArguments(bundle);
                                            getFragmentManager().beginTransaction().replace(R.id.fl_container,
                                                    transfusionFifteenFra).commit();
                                        } else {
                                            ShowToast("血品缺少planOrderNo");
                                        }
                                    }
                                });
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                ShowToast(msg);
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            ShowToast(e + "");
                            if (LinstenNetState.isLinkState(getActivity())) {
                                Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                        .LENGTH_SHORT).show();
                            } else {
                                toErrorAct();
                            }
                        }

                    }
                });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imbtn_back:
//                Intent intent=new Intent();
//                Bundle bundle=new Bundle();
//                bundle.putSerializable(GlobalConstant.KEY_PATIENT,patient);
//                intent.putExtras(bundle);
//                intent.setClass(getActivity(), TransfusionActivity.class);
//                startActivity(intent);
                getActivity().setResult(GlobalConstant.REQUEST_CODE);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
