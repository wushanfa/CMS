package com.gentlehealthcare.mobilecare.activity.patient.insulin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.InjectionTemplateBean;
import com.gentlehealthcare.mobilecare.net.bean.PlaceStatusBean;
import com.gentlehealthcare.mobilecare.net.bean.PlaceStatusInfo;
import com.gentlehealthcare.mobilecare.net.bean.SetStatusForAreaBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.LoadInjectionTemplateRequest;
import com.gentlehealthcare.mobilecare.net.impl.SetStatusForAreaRequest;
import com.gentlehealthcare.mobilecare.net.impl.SetStatusForSiteNoRequset;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.InjectionSiteDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 胰岛素注射部位选择界面
 * Created by ouyang on 15/6/2.
 */
@SuppressLint("ValidFragment")
public class InsulinPlaceFra extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {
    private ImageView iv_body;
    private TextView tv_rightArm, tv_leftArm, tv_leftTopBelly, tv_rightTopBelly, tv_leftBottomBelly,
            tv_rightBottomBelly, tv_leftLeg, tv_rightLeg;
    private boolean isFront;
    private List<PlaceStatusBean> rightArmSite = new ArrayList<PlaceStatusBean>();
    private List<PlaceStatusBean> leftArmSite = new ArrayList<PlaceStatusBean>();
    private List<PlaceStatusBean> rightTopBellySite = new ArrayList<PlaceStatusBean>();
    private List<PlaceStatusBean> leftTopBellySite = new ArrayList<PlaceStatusBean>();
    private List<PlaceStatusBean> rightBottomBellySite = new ArrayList<PlaceStatusBean>();
    private List<PlaceStatusBean> leftBottomBellySite = new ArrayList<PlaceStatusBean>();
    private List<PlaceStatusBean> rightLegSite = new ArrayList<PlaceStatusBean>();
    private List<PlaceStatusBean> leftLegSite = new ArrayList<PlaceStatusBean>();
    private InjectionSiteDialog dialog = null;
    private SyncPatientBean patient;
    private InjectionTemplateBean injectionTemplateBean = null;
    private ProgressDialog progressDialog;

    public InsulinPlaceFra(SyncPatientBean patient) {
        this.patient = patient;
    }

    private String injectionSiteNo = "";
    private String injectionSite = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_insulin_place, null);
        iv_body = (ImageView) rootView.findViewById(R.id.iv_body);
        isFront = true;
        progressDialog = new ProgressDialog(getActivity());
        rootView.findViewById(R.id.iv_around).setOnClickListener(this);
        tv_leftArm = (TextView) rootView.findViewById(R.id.tv_leftArm);
        tv_rightArm = (TextView) rootView.findViewById(R.id.tv_rightArm);
        tv_leftTopBelly = (TextView) rootView.findViewById(R.id.tv_leftTopBelly);
        tv_rightTopBelly = (TextView) rootView.findViewById(R.id.tv_rightTopBelly);
        tv_leftBottomBelly = (TextView) rootView.findViewById(R.id.tv_leftBottomBelly);
        tv_rightBottomBelly = (TextView) rootView.findViewById(R.id.tv_rightBottomBelly);
        tv_leftLeg = (TextView) rootView.findViewById(R.id.tv_leftLeg);
        tv_rightLeg = (TextView) rootView.findViewById(R.id.tv_rightLeg);
        tv_leftArm.setOnLongClickListener(this);
        tv_rightArm.setOnLongClickListener(this);
        tv_leftTopBelly.setOnLongClickListener(this);
        tv_rightTopBelly.setOnLongClickListener(this);
        tv_leftBottomBelly.setOnLongClickListener(this);
        tv_rightBottomBelly.setOnLongClickListener(this);
        tv_leftLeg.setOnLongClickListener(this);
        tv_rightLeg.setOnLongClickListener(this);

        tv_leftArm.setOnClickListener(this);
        tv_rightArm.setOnClickListener(this);
        tv_leftTopBelly.setOnClickListener(this);
        tv_rightTopBelly.setOnClickListener(this);
        tv_leftBottomBelly.setOnClickListener(this);
        tv_rightBottomBelly.setOnClickListener(this);
        tv_leftLeg.setOnClickListener(this);
        tv_rightLeg.setOnClickListener(this);
        rootView.findViewById(R.id.btn_reload).setOnClickListener(this);

        LoadInjectionTemplates(new InjectionTemplateBean(0));
        return rootView;
    }

    /**
     * 判断该显示前面还是后面
     */
    private void updateShow() {
        if (isFront) {
            iv_body.setBackgroundResource(R.drawable.insulin_place_h);
            tv_leftArm.setVisibility(View.GONE);
            tv_rightArm.setVisibility(View.GONE);
//            tv_leftBelly.setVisibility(View.GONE);
//            tv_rightBelly.setVisibility(View.GONE);
            tv_leftLeg.setVisibility(View.GONE);
            tv_rightLeg.setVisibility(View.GONE);
        } else {
            iv_body.setBackgroundResource(R.drawable.insulin_place_q);
            tv_leftArm.setVisibility(View.VISIBLE);
            tv_rightArm.setVisibility(View.VISIBLE);
            tv_leftLeg.setVisibility(View.VISIBLE);
            tv_rightLeg.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        dialog = new InjectionSiteDialog(getActivity(), R.style.myDialogTheme);
        dialog.setStyle(0);
        dialog.setInsulinPlaceFra(this);
        switch (view.getId()) {
            case R.id.iv_around:
                updateShow();
                isFront = !isFront;
                break;
            case R.id.tv_leftArm:
                dialog.setTitle("A");
                dialog.setItems(leftArmSite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("A"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_rightArm:
                dialog.setTitle("B");
                dialog.setItems(rightArmSite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("B"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_leftTopBelly:
                dialog.setTitle("C");
                dialog.setItems(leftTopBellySite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("C"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_rightTopBelly:
                dialog.setTitle("D");
                dialog.setItems(rightTopBellySite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("D"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_leftBottomBelly:
                dialog.setTitle("E");
                dialog.setItems(leftBottomBellySite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("E"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_rightBottomBelly:
                dialog.setTitle("F");
                dialog.setItems(rightBottomBellySite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("F"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_leftLeg:
                dialog.setTitle("G");
                dialog.setItems(leftLegSite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("G"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_rightLeg:
                dialog.setTitle("H");
                dialog.setItems(rightLegSite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("H"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.btn_reload:
                LoadInjectionTemplates(new InjectionTemplateBean(1));
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        dialog = new InjectionSiteDialog(getActivity(), R.style.myDialogTheme);
        dialog.setStyle(1);
        dialog.setInsulinPlaceFra(this);
        dialog.setLongOnclick(true);
        switch (v.getId()) {
            case R.id.tv_leftArm:
                dialog.setTitle("A");
                dialog.setItems(leftArmSite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("A"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_rightArm:
                dialog.setTitle("B");
                dialog.setItems(rightArmSite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("B"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_leftTopBelly:
                dialog.setTitle("C");
                dialog.setItems(leftTopBellySite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("C"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_rightTopBelly:
                dialog.setTitle("D");
                dialog.setItems(rightTopBellySite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("D"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_leftBottomBelly:
                dialog.setTitle("E");
                dialog.setItems(leftBottomBellySite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("E"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_rightBottomBelly:
                dialog.setTitle("F");
                dialog.setItems(rightBottomBellySite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("F"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_leftLeg:
                dialog.setTitle("G");
                dialog.setItems(leftLegSite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("G"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
            case R.id.tv_rightLeg:
                dialog.setTitle("H");
                dialog.setItems(rightLegSite, 4, injectionSiteNo);
                dialog.setIsinjectionSite(injectionSite.equals("H"));
                dialog.setInjectionSiteAdapter();
                dialog.show();
                break;
        }
        return false;
    }

    /**
     * 修改一个/多个位置状态请求,（没有调用）
     *
     * @param operNo
     */
    public void DoSetStatusForSiteNo(String operNo, String siteCode) {
        progressDialog.setMessage("正在提交数据,请稍后...");
        progressDialog.show();
        SetStatusForAreaBean setStatusForAreaBean = new SetStatusForAreaBean();
        setStatusForAreaBean.setPatId(patient.getPatId());
        setStatusForAreaBean.setSiteNo(itemNo);
        setStatusForAreaBean.setSiteCode(injectionSite);
        setStatusForAreaBean.setOperNo(operNo);
        RequestManager.connection(new SetStatusForSiteNoRequset(getActivity(), new
                IRespose<SetStatusForAreaBean>() {
                    @Override
                    public void doResult(SetStatusForAreaBean setStatusForAreaBean, int id) {

                    }

                    @Override
                    public void doResult(String result) throws KeyObsoleteException {
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        InsulinPlaceFra.this.injectionTemplateBean = gson.fromJson(result, InjectionTemplateBean
                                .class);
                        if (InsulinPlaceFra.this.injectionTemplateBean.isResult())
                            DoRefreshUIByTemplateData(true);
                        else
                            ShowToast(InsulinPlaceFra.this.injectionTemplateBean.getMsg());

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
                }, 0, true, setStatusForAreaBean));
    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 修改区域块状态请求（没有调用）
     *
     * @param area
     * @param operNo
     */
    public void DoSetStatusForArea(String area, String operNo) {
        progressDialog.setMessage("正在提交数据,请稍后...");
        progressDialog.show();
        SetStatusForAreaBean setStatusForAreaBean = new SetStatusForAreaBean();
        setStatusForAreaBean.setPatId(patient.getPatId());
        setStatusForAreaBean.setSiteCode(area);
        setStatusForAreaBean.setOperNo(operNo);
        RequestManager.connection(new SetStatusForAreaRequest(getActivity(), new IRespose<SetStatusForAreaBean>() {
            @Override
            public void doResult(SetStatusForAreaBean setStatusForAreaBean, int id) {

            }

            @Override
            public void doResult(String result) throws KeyObsoleteException {
                progressDialog.dismiss();
                InsulinPlaceFra.this.injectionTemplateBean = new Gson().fromJson(result, InjectionTemplateBean
                        .class);
                if (InsulinPlaceFra.this.injectionTemplateBean.isResult()) {
                    DoRefreshUIByTemplateData(true);
                } else {
                    ShowToast(InsulinPlaceFra.this.injectionTemplateBean.getMsg());
                }
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
        }, 0, true, setStatusForAreaBean));
    }

    /**
     * 注射该部位
     * @param siteCode   区域代码
     * @param siteNo
     * @param isinjectionSite
     */
    public void injectionSite(String siteCode, String siteNo, boolean isinjectionSite) {
        if (getParentFragment() instanceof InsulinFlowSheetFra) {
            if (isinjectionSite){
                ((InsulinFlowSheetFra) getParentFragment()).DoInjectionSite(injectionSite, itemNo);
            }
        }
    }

    /**
     * 加载注射模版
     * @param injectionTemplateBean
     */
    private void LoadInjectionTemplates(InjectionTemplateBean injectionTemplateBean) {
        if (injectionTemplateBean.getIsReload() == 0){
            progressDialog.setMessage("加载注射胰岛素模版数据,请稍后...");
        } else{
            progressDialog.setMessage("重新生成注射胰岛素模版数据,请稍后...");
        }
        progressDialog.show();
        injectionTemplateBean.setPatId(patient.getPatId());
        RequestManager.connection(new LoadInjectionTemplateRequest(getActivity(), new
                IRespose<InjectionTemplateBean>() {
                    @Override
                    public void doResult(InjectionTemplateBean injectionTemplateBean, int id) {

                    }

                    @Override
                    public void doResult(String result) throws KeyObsoleteException {
                        progressDialog.dismiss();
                        InsulinPlaceFra.this.injectionTemplateBean = new Gson().fromJson(result,
                                InjectionTemplateBean
                                .class);
                        DoRefreshUIByTemplateData(true);
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
                }, 0, true, injectionTemplateBean));
    }


    private String itemNo = "";

    /**
     * 根据从服务器获取模版数据刷新界面
     */
    private void DoRefreshUIByTemplateData(boolean showDialog) {
        if (injectionTemplateBean != null) {
            PlaceStatusInfo a = injectionTemplateBean.getA();
            PlaceStatusInfo b = injectionTemplateBean.getB();
            PlaceStatusInfo c = injectionTemplateBean.getC();
            PlaceStatusInfo d = injectionTemplateBean.getD();
            PlaceStatusInfo e = injectionTemplateBean.getE();
            PlaceStatusInfo f = injectionTemplateBean.getF();
            PlaceStatusInfo g = injectionTemplateBean.getG();
            PlaceStatusInfo h = injectionTemplateBean.getH();
            if (a.getStatus().equals("1")) {
                tv_leftArm.setBackgroundResource(R.drawable.not_injection);
            } else {
                tv_leftArm.setBackgroundResource(R.drawable.can_injection);
            }
            if (b.getStatus().equals("1")) {
                tv_rightArm.setBackgroundResource(R.drawable.not_injection);
            } else {
                tv_rightArm.setBackgroundResource(R.drawable.can_injection);
            }
            if (c.getStatus().equals("1")) {
                tv_leftTopBelly.setBackgroundResource(R.drawable.not_injection);
            } else {
                tv_leftTopBelly.setBackgroundResource(R.drawable.can_injection);
            }
            if (d.getStatus().equals("1")) {
                tv_rightTopBelly.setBackgroundResource(R.drawable.not_injection);
            } else {
                tv_rightTopBelly.setBackgroundResource(R.drawable.can_injection);
            }
            if (e.getStatus().equals("1")) {
                tv_leftBottomBelly.setBackgroundResource(R.drawable.not_injection);
            } else {
                tv_leftBottomBelly.setBackgroundResource(R.drawable.can_injection);
            }
            if (f.getStatus().equals("1")) {
                tv_rightBottomBelly.setBackgroundResource(R.drawable.not_injection);
            } else {
                tv_rightBottomBelly.setBackgroundResource(R.drawable.can_injection);
            }
            if (g.getStatus().equals("1")) {
                tv_leftLeg.setBackgroundResource(R.drawable.not_injection);
            } else {
                tv_leftLeg.setBackgroundResource(R.drawable.can_injection);
            }
            if (h.getStatus().equals("1")) {
                tv_rightLeg.setBackgroundResource(R.drawable.not_injection);
            } else {
                tv_rightLeg.setBackgroundResource(R.drawable.can_injection);
            }
            Map<String, String> map = injectionTemplateBean.GetLastInjectionedSiteNo();
            if (!map.get("siteNo").equals("0")) {
                GetPlaceStatusInfoBySite(map.get("site")).setBackgroundResource(R.drawable.can_injection);
                GetPlaceStatusInfoBySite(map.get("site")).setText("-1");
            }
            map = injectionTemplateBean.GetWillInjectionSiteNo();
            if (map.get("siteNo").equals("0")) {
                //超过数额，需自动重新配置
                LoadInjectionTemplates(new InjectionTemplateBean(1));
                return;
            }
            GetPlaceStatusInfoBySite(map.get("site")).setBackgroundResource(R.drawable.injection);
            GetPlaceStatusInfoBySite(map.get("site")).setText("");

            injectionSiteNo = map.get("siteNo");
            injectionSite = map.get("site");
            itemNo = map.get("itemNo");
            leftArmSite.clear();
            rightArmSite.clear();
            leftTopBellySite.clear();
            rightTopBellySite.clear();
            leftBottomBellySite.clear();
            rightBottomBellySite.clear();
            leftLegSite.clear();
            rightLegSite.clear();
            leftArmSite.addAll(injectionTemplateBean.getA().getStatusList());
            rightArmSite.addAll(injectionTemplateBean.getB().getStatusList());
            rightTopBellySite.addAll(injectionTemplateBean.getD().getStatusList());
            leftTopBellySite.addAll(injectionTemplateBean.getC().getStatusList());
            rightBottomBellySite.addAll(injectionTemplateBean.getF().getStatusList());
            leftBottomBellySite.addAll(injectionTemplateBean.getE().getStatusList());
            rightLegSite.addAll(injectionTemplateBean.getH().getStatusList());
            leftLegSite.addAll(injectionTemplateBean.getG().getStatusList());

            if (showDialog) {
                dialog = new InjectionSiteDialog(getActivity(), R.style.myDialogTheme);
                dialog.setStyle(0);
                dialog.setIsinjectionSite(true);
                dialog.setInjectionSiteAdapter();
                dialog.setInsulinPlaceFra(InsulinPlaceFra.this);
                dialog.setTitle(map.get("site"));
                List<PlaceStatusBean> list = GetPlaceStatusInfoBySite(injectionTemplateBean, map.get("site"))
                        .getStatusList();
                dialog.setItems(list, list.size() % 4 == 0 ? 4 : 3, injectionSiteNo);
                dialog.show();
            }
        }

    }


    private PlaceStatusInfo GetPlaceStatusInfoBySite(InjectionTemplateBean injectionTemplateBean, String site) {
        if (site.equals("A"))
            return injectionTemplateBean.getA();
        else if (site.equals("B"))
            return injectionTemplateBean.getB();
        else if (site.equals("C"))
            return injectionTemplateBean.getC();
        else if (site.equals("D"))
            return injectionTemplateBean.getD();
        else if (site.equals("E"))
            return injectionTemplateBean.getE();
        else if (site.equals("F"))
            return injectionTemplateBean.getF();
        else if (site.equals("G"))
            return injectionTemplateBean.getG();
        else if (site.equals("H"))
            return injectionTemplateBean.getH();
        else
            return new PlaceStatusInfo();
    }


    private TextView GetPlaceStatusInfoBySite(String site) {
        if (site.equals("A"))
            return tv_leftArm;
        else if (site.equals("B"))
            return tv_rightArm;
        else if (site.equals("C"))
            return tv_leftTopBelly;
        else if (site.equals("D"))
            return tv_rightTopBelly;
        else if (site.equals("E"))
            return tv_leftBottomBelly;
        else if (site.equals("F"))
            return tv_rightBottomBelly;
        else if (site.equals("G"))
            return tv_leftLeg;
        else if (site.equals("H"))
            return tv_rightLeg;
        else
            return null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_place);
        SupportDisplay.resetAllChildViewParam(root);
    }

    public void doOnClick() {
        if (dialog != null && dialog.isShowing()) {
            dialog.DoDialogFinish();
        }
    }


}
