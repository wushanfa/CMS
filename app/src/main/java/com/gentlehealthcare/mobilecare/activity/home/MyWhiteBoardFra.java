package com.gentlehealthcare.mobilecare.activity.home;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.CamerService;
import com.gentlehealthcare.mobilecare.activity.PatrolAct;
import com.gentlehealthcare.mobilecare.activity.TemperatureAct;
import com.gentlehealthcare.mobilecare.activity.bloodbagnuclearcharge.BloodBagNuclearChargeAct;
import com.gentlehealthcare.mobilecare.activity.login.ChooseGroupAct;
import com.gentlehealthcare.mobilecare.activity.login.LoginAct;
import com.gentlehealthcare.mobilecare.activity.orders.DocOrdersActivity;
import com.gentlehealthcare.mobilecare.activity.orders.OrdersCheckActivity;
import com.gentlehealthcare.mobilecare.bean.HomeBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtn;
import com.gentlehealthcare.mobilecare.view.adapter.HomeBoardAdapter;

import java.util.ArrayList;
import java.util.List;
/**
 * 首页 我的白板 TODO
 */
public class MyWhiteBoardFra extends BaseFragment {
    private TextView tv_userGroup;
    private TextView tv_user;
    private List<HomeBean> homeBeanList = null;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_white_board, null);
        tv_userGroup = (TextView) view.findViewById(R.id.tv_userGroup);
        tv_user = (TextView) view.findViewById(R.id.tv_user);
        String mytext = UserInfo.getName();
        tv_user.setText(mytext);
        homeBeanList = new ArrayList<HomeBean>();
        homeBeanList.add(new HomeBean("医嘱\n执行", 3, true));
        homeBeanList.add(new HomeBean("生命\n体征", 0, true));
        homeBeanList.add(new HomeBean("护理\n记录", 0, true));
        homeBeanList.add(new HomeBean("重症\n监护", 0, true));
        homeBeanList.add(new HomeBean("执行\n核对", 0, true));
        homeBeanList.add(new HomeBean("血品\n核收", 0, true));
        homeBeanList.add(new HomeBean("护理\n巡视", 0, true));
        homeBeanList.add(new HomeBean("体征\n录入", 0, true));

        GridView gridView = (GridView) view.findViewById(R.id.gv_home);
        gridView.setAdapter(new HomeBoardAdapter(getActivity(), homeBeanList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    // 医嘱护理 治疗
                    Intent intent = new Intent(getActivity(), DocOrdersActivity.class);
                    intent.putExtra(GlobalConstant.KEY_POSITION,0);
                    getActivity().startActivity(intent);
                } else if (position == 1) {
                    // 护理评估模块 三测
                    getActivity().startActivity(new Intent(getActivity(), ThreeTestActivity.class));
                } else if (position == 2) {
                    // POI 护嘱护理
                    getActivity().startActivity(new Intent(getActivity(), PioRecordsActivity.class));
                } else if (position == 3) {
                    //ICU 表单
                    getActivity().startActivity(new Intent(getActivity(), ICUAActivity.class));
                } else if(position==4){
                    Intent intent = new Intent(getActivity(), OrdersCheckActivity.class);
                    intent.putExtra(GlobalConstant.KEY_POSITION,0);
                    getActivity().startActivity(intent);
                }else if (position == 5) {
                    Intent intent = new Intent(getActivity(), BloodBagNuclearChargeAct.class);
                    getActivity().startActivity(intent);
                }else if(position==6){
                    Intent intent = new Intent(getActivity(), PatrolAct.class);
                    getActivity().startActivity(intent);
                }else if(position==7){
                    Intent intent = new Intent(getActivity(), TemperatureAct.class);
                    getActivity().startActivity(intent);
                }
            }
        });
        tv_userGroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 选择分组界面
                getActivity().startActivity(
                        new Intent(getActivity(), ChooseGroupAct.class));
            }
        });

        view.findViewById(R.id.iv_loginout).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialogTwoBtn showTipDialog = new AlertDialogTwoBtn(getActivity());
                        showTipDialog.setTitle(getResources().getString(R.string.notification));
                        showTipDialog.setMessage(getResources().getString(R.string.loginoutapp));
                        showTipDialog.setLeftButton(getResources().getString(R.string.cancel), new
                                OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showTipDialog.dismiss();
                                    }
                                });
                        showTipDialog.setRightButton(getResources().getString(R.string.make_sure), new
                                OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LocalSave.deleteData(getActivity(),GlobalConstant.KEY_BLOODDONORCODE);
                                        LocalSave.deleteData(getActivity(),GlobalConstant.KEY_BLOODPRODUCTCODE);
                                        LocalSave.deleteData(getActivity(),GlobalConstant.KEY_PATBARCODE) ;
                                        LocalSave.deleteData(getActivity(),GlobalConstant.KEY_LABAPPLYCODE);
                                        LocalSave.deleteData(getActivity(),GlobalConstant.KEY_BLOODINVALCODE);
                                        LocalSave.deleteData(getActivity(),GlobalConstant.KEY_PLANBARCODE);
                                        LocalSave.deleteData(getActivity(),GlobalConstant.KEY_BLOODGROUPCODE);
                                        NotificationManager notificationManager = null;
                                        notificationManager = (NotificationManager) getActivity().getSystemService
                                                (Context.NOTIFICATION_SERVICE);
                                        notificationManager.cancelAll();//清理notification
                                        // android.os.Process.killProcess(android.os.Process.myPid());//整个结束掉
                                        getActivity().finish();
                                        Intent intent = new Intent(getActivity(),
                                                LoginAct.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        getActivity().stopService(
                                                new Intent(getActivity(), CamerService.class));

                                        getActivity().overridePendingTransition(
                                                R.anim.slide_in_right, R.anim.slide_out_left);
                                        showTipDialog.dismiss();
                                    }
                                });

                   }
                });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String str = GroupInfoSave.getInstance(getActivity()).get();

        if (str.contains("-")) {
            tv_userGroup.setText(TextUtils.isEmpty(str) ? "" : str.split("-")[1]);
        } else if (str.contains("（") && str.contains("）")) {
            String groupName = str.split("（")[1];
            groupName = groupName.split("）")[0];
            tv_userGroup.setText(TextUtils.isEmpty(str) ? "" : groupName);
        } else if (str.contains("(") && str.contains(")")) {
            String groupName = str.split("\\(")[1];
            groupName = groupName.split("\\)")[0];
            tv_userGroup.setText(TextUtils.isEmpty(str) ? "" : groupName);
        }else{
            tv_userGroup.setText(str);
        }
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view
                .findViewById(R.id.root_fra_white_board);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
