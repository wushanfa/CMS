package com.gentlehealthcare.mobilecare.activity.insulin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.bean.insulin.InjectionSiteBean;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuBBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrdersBean;
import com.gentlehealthcare.mobilecare.presenter.InsulinPresenter;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.IInsulinInjectionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiwei on 2016/5/14.
 */
public class InjectionFragment extends BaseFragment implements IInsulinInjectionView, View.OnClickListener, View
        .OnLongClickListener {

    private InsulinPresenter presenter;

    /**
     * 注射位置
     */
    private TextView tv_rightArm, tv_leftArm, tv_leftTopBelly, tv_rightTopBelly, tv_leftBottomBelly,
            tv_rightBottomBelly, tv_leftLeg, tv_rightLeg;
    private List<TextView> sites = new ArrayList<TextView>();

    private Button btn_reload;

    private int[] siteStatus;

    private String patId;

    private OrderListBean orderListBean;

    private ProgressDialog progressDialog;

    private List<InjectionSiteBean> siteBeans = new ArrayList<InjectionSiteBean>();

    private StartInsulinFragment startInsulinFragment = new StartInsulinFragment();

    private int changeStatus;
    private int changeSite;
    private String changeSiteDesc;
    private String changeSiteId;

    private Thread myThread;

    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insulin_injection, null);
        initView(view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.datasaving));
        Bundle data = getArguments();
        patId = data.getString("patId");
        orderListBean = (OrderListBean) data.getSerializable(GlobalConstant.KEY_ORDERLISTBEAN);
        presenter = new InsulinPresenter(this);
        if (siteStatus == null) {
            presenter.getInjectionSite(patId);
        } else {
            setSiteBackground(siteStatus);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_leftArm:
                presenter.clickDeal(0, siteBeans, siteStatus);
                break;
            case R.id.tv_rightArm:
                presenter.clickDeal(1, siteBeans, siteStatus);
                break;
            case R.id.tv_leftTopBelly:
                presenter.clickDeal(2, siteBeans, siteStatus);
                break;
            case R.id.tv_rightTopBelly:
                presenter.clickDeal(3, siteBeans, siteStatus);
                break;
            case R.id.tv_leftBottomBelly:
                presenter.clickDeal(4, siteBeans, siteStatus);
                break;
            case R.id.tv_rightBottomBelly:
                presenter.clickDeal(5, siteBeans, siteStatus);
                break;
            case R.id.tv_leftLeg:
                presenter.clickDeal(6, siteBeans, siteStatus);
                break;
            case R.id.tv_rightLeg:
                presenter.clickDeal(7, siteBeans, siteStatus);
                break;
            case R.id.btn_reload:
                presenter.reLoadTemplate(patId);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.tv_leftArm:
                longDeal(0);
                break;
            case R.id.tv_rightArm:
                longDeal(1);
                break;
            case R.id.tv_leftTopBelly:
                longDeal(2);
                break;
            case R.id.tv_rightTopBelly:
                longDeal(3);
                break;
            case R.id.tv_leftBottomBelly:
                longDeal(4);
                break;
            case R.id.tv_rightBottomBelly:
                longDeal(5);
                break;
            case R.id.tv_leftLeg:
                longDeal(6);
                break;
            case R.id.tv_rightLeg:
                longDeal(7);
                break;
            default:
                break;
        }
        return false;
    }

    private void longDeal(int num) {
        presenter.longClickDeal(num, siteStatus, patId, siteBeans.get(num).getSiteId(), siteBeans.get(num)
                .getItemNo(), orderListBean.getPlanId());
    }

    /**
     * 设置注射位置的颜色
     * 如果是-99禁打，-98拒打，1已注射，0未打,2上一位置，3下一位置,4推荐位置
     *
     * @param params
     */
    @Override
    public void setSiteBackground(int... params) {
        siteStatus = params;
        for (int i = 0; i < params.length; i++) {
            int status = params[i];
            TextView tempTextView = sites.get(i);
            if (status == -99) {
                tempTextView.setBackgroundResource(R.drawable.not_injection);
            } else if (status == -98) {
                tempTextView.setBackgroundResource(R.drawable.refuse_injection);
            } else if (status == 1) {
                tempTextView.setBackgroundResource(R.drawable.quan_used);
            } else if (status == 0) {
                tempTextView.setBackgroundResource(R.drawable.quan);
            } else if (status == 4) {
                tempTextView.setBackgroundResource(R.drawable.injection_site);
            } else if (status == 3) {
                tempTextView.setBackgroundResource(R.drawable.quan_hou);
            } else if (status == 2) {
                tempTextView.setBackgroundResource(R.drawable.quan_qian);
            } else if (status == 5) {
                tempTextView.setBackgroundResource(R.drawable.quan_myself);
            }
        }
    }

    @Override
    public void setSiteBackgroundInit() {
        sites.get(0).setBackgroundResource(R.drawable.quan_injection);
        for (int i = 1; i < sites.size(); i++) {
            sites.get(i).setBackgroundResource(R.drawable.quan);
        }
    }

    public void showToast(int showWhich) {
        if (showWhich == GlobalConstant.REFUSE) {
            Toast.makeText(getActivity(), "这个部位不能注射，如果依然想注射此位置;，请重配模板", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void setDialog(final int num) {
        String[] strings = new String[]{"区域禁打", "区域拒打"};//-99,-98
        AlertDialog alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setTitle
                ("请选择原因").setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    presenter.updateSite(patId, siteBeans.get(num).getSiteId(), siteBeans.get(num).getItemNo(),
                            -99, orderListBean.getPlanId());
                } else if (i == 1) {
                    presenter.updateSite(patId, siteBeans.get(num).getSiteId(), siteBeans.get(num).getItemNo(),
                            -98, orderListBean.getPlanId());
                }
            }
        }).setNegativeButton(getString(R.string.cancel), null).create();
        alert.show();
        alert.getWindow().setLayout(680, 650);
    }

    public void replaceFragment() {
        Bundle myBundle = new Bundle();
        myBundle.putString("patId", patId);
        myBundle.putSerializable(GlobalConstant.KEY_ORDERLISTBEAN, orderListBean);
        myBundle.putInt("site", changeSite);
        myBundle.putInt("status", changeStatus);
        myBundle.putString("desc", changeSiteDesc);
        myBundle.putString("siteId", changeSiteId);
        startInsulinFragment.setArguments(myBundle);
        getFragmentManager().beginTransaction().replace(R.id.fl_container, startInsulinFragment).addToBackStack
                (null).commit();
    }

    public void setSiteBeans(List<InjectionSiteBean> beans) {
        siteBeans = beans;
    }

    public void setSiteChange(int site, int status, String desc, String siteId) {
        changeSite = site;
        changeStatus = status;
        changeSiteDesc = desc;
        changeSiteId = siteId;
    }

    private void initView(View view) {
        btn_reload = (Button) view.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(this);

        tv_leftArm = (TextView) view.findViewById(R.id.tv_leftArm);
        tv_rightArm = (TextView) view.findViewById(R.id.tv_rightArm);
        tv_leftTopBelly = (TextView) view.findViewById(R.id.tv_leftTopBelly);
        tv_rightTopBelly = (TextView) view.findViewById(R.id.tv_rightTopBelly);
        tv_leftBottomBelly = (TextView) view.findViewById(R.id.tv_leftBottomBelly);
        tv_rightBottomBelly = (TextView) view.findViewById(R.id.tv_rightBottomBelly);
        tv_leftLeg = (TextView) view.findViewById(R.id.tv_leftLeg);
        tv_rightLeg = (TextView) view.findViewById(R.id.tv_rightLeg);
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

        if (siteStatus == null) {
            sites.add(tv_leftArm);
            sites.add(tv_rightArm);
            sites.add(tv_leftTopBelly);
            sites.add(tv_rightTopBelly);
            sites.add(tv_leftBottomBelly);
            sites.add(tv_rightBottomBelly);
            sites.add(tv_leftLeg);
            sites.add(tv_rightLeg);
        } else {
            sites.clear();
            sites.add(tv_leftArm);
            sites.add(tv_rightArm);
            sites.add(tv_leftTopBelly);
            sites.add(tv_rightTopBelly);
            sites.add(tv_leftBottomBelly);
            sites.add(tv_rightBottomBelly);
            sites.add(tv_leftLeg);
            sites.add(tv_rightLeg);
        }
    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_fra_injection);
        SupportDisplay.resetAllChildViewParam(root);
    }

}
