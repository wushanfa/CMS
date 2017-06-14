package com.gentlehealthcare.mobilecare.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.adapter.CommonNavigatorAdapter;
import com.gentlehealthcare.mobilecare.adapter.ExamplePagerAdapter;
import com.gentlehealthcare.mobilecare.bean.sys.BarcodeDict;
import com.gentlehealthcare.mobilecare.config.CommonNavigator;
import com.gentlehealthcare.mobilecare.config.IPagerIndicator;
import com.gentlehealthcare.mobilecare.config.MagicIndicator;
import com.gentlehealthcare.mobilecare.config.UIUtil;
import com.gentlehealthcare.mobilecare.config.ViewPagerHelper;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.fragment.PhysicalSignInBasicFragment;
import com.gentlehealthcare.mobilecare.fragment.PhysicalSignInOtherFragment;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.TemperaturePresenter;
import com.gentlehealthcare.mobilecare.tool.DialogUtils;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.MacAddressUtils;
import com.gentlehealthcare.mobilecare.tool.SharedPreferenceUtils;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.tool.TimeUtils;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.gentlehealthcare.mobilecare.view.BadgePagerTitleView;
import com.gentlehealthcare.mobilecare.view.ClipPagerTitleView;
import com.gentlehealthcare.mobilecare.view.IPagerTitleView;
import com.gentlehealthcare.mobilecare.view.ITemperatureView;
import com.gentlehealthcare.mobilecare.view.LinePagerIndicator;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;
import com.gentlehealthcare.mobilecare.webService.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 体温单录入
 */
public class TemperatureAct extends BaseActivity implements ITemperatureView, View.OnClickListener {
    private ProgressDialog mProgressDialog;
    private Button btnBack;
    private LinearLayout changePatient;
    private ViewPager mViewPager;
    private TemperaturePresenter TemperaturePresenter;
    private FragmentManager mFragmentManager = null;
    private List<Fragment> mFragments = new ArrayList<>();
    private static final String[] CHANNELS = new String[]{"基础", "其他"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private int whichPatients = 0;
    private MyPatientDialog dialog;
    private TextView patName;
    private TextView patSex;
    private TextView patBed;
    private TextView patCode;
    private String mPatID; //唯一流水号GUID
    private String logTime, logStatus, twMethod, tw, twJw, twJwfs, mb, xl, hx, hxj,tt;  //基础信息
    private String sg, tz, xy_1, xy_2, xy_3, xy_4, rl, cl, db_cs, nl, ywgm1,db_sj,db_rggm,nl_sj,yzr, othername1, othervalue1, othername2, othervalue2, xybhd, xt;  //其他信息
    List<SyncPatientBean> deptPatient;
    private String timePoint;
    private Boolean isInit;   //是否是初始请求
    private boolean isRefreshPatient;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgressDialog();
            switch (msg.what) {
                case 0:
                    initFragment(logTime, logStatus, twMethod, tw, twJw, twJwfs, mb, xl, hx, hxj ,tt,
                            sg, tz, xy_1, xy_2, xy_3, xy_4, rl, cl, db_cs, nl, ywgm1,db_sj,db_rggm,nl_sj,yzr,othername1,
                            othervalue1, othername2, othervalue2, xybhd, xt);
                    break;
                case 1:
                    sendMessageToFragment();
                    sendInformationToFragment();
                    break;
                case -1:
                    Toast.makeText(TemperatureAct.this, "网络异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_template);
        initView();
    }

    //初始化控件
    private void initView() {
        deptPatient = new ArrayList<>();
        btnBack = (Button) findViewById(R.id.btn_back);
        changePatient = (LinearLayout) findViewById(R.id.ll_pat_name);
        patName = (TextView) findViewById(R.id.tv_pat_name);
        patSex = (TextView) findViewById(R.id.tv_pat_sex);
        patBed = (TextView) findViewById(R.id.tv_pat_bed);
        patCode = (TextView) findViewById(R.id.tv_pat_code);
        mViewPager = (ViewPager) findViewById(R.id.act_template_view_pager);
        TemperaturePresenter = new TemperaturePresenter(this);
        TemperaturePresenter.initialSrc();
        TemperaturePresenter.getPatients(this, whichPatients);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBack.setOnClickListener(this);
        changePatient.setOnClickListener(this);
    }

    //初始化fragment
    private void initFragment(String logTime, String logStatus, String twMethod, String tw,
                              String twJw, String twJwfs, String mb, String xl, String hx, String hxj,String tt,
                              String sg, String tz, String xy_1, String xy_2, String xy_3, String xy_4, String rl,
                              String cl, String db_cs, String nl, String ywgm1,String db_sj,String db_rggm,String nl_sj,String yzr, String othername1, String othervalue1,
                              String othername2, String othervalue2, String xybhd, String xt) {
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();

        PhysicalSignInBasicFragment mPhysicalSignInBasicFragment = (PhysicalSignInBasicFragment)
                mFragmentManager.findFragmentByTag("PhysicalSignInBasicFragment");
        if (mPhysicalSignInBasicFragment == null) {
            mPhysicalSignInBasicFragment = PhysicalSignInBasicFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("PAT_ID", mPatID);
            bundle.putString("logTime", timePoint.substring(11,16));
            bundle.putString("logStatus", logStatus);
            bundle.putString("twMethod", twMethod);
            bundle.putString("tw", tw);
            bundle.putString("twJw", twJw);
            bundle.putString("twJwfs", twJwfs);
            bundle.putString("mb", mb);
            bundle.putString("xl", xl);
            bundle.putString("hx", hx);
            bundle.putString("hxj", hxj);
            bundle.putString("tt", tt);
            if (mPhysicalSignInBasicFragment.isAdded()) {
                return;
            }
            mPhysicalSignInBasicFragment.setArguments(bundle);
            mFragments.add(mPhysicalSignInBasicFragment);
        }

        PhysicalSignInOtherFragment mPhysicalSignInOtherFragment = (PhysicalSignInOtherFragment)
                mFragmentManager.findFragmentByTag("PhysicalSignInOtherFragment");
        if (mPhysicalSignInOtherFragment == null) {
            mPhysicalSignInOtherFragment = PhysicalSignInOtherFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("PAT_ID", mPatID);
            bundle.putString("sg", sg);
            bundle.putString("tz", tz);
            bundle.putString("xy_1", xy_1);
            bundle.putString("xy_2", xy_2);
            bundle.putString("xy_3", xy_3);
            bundle.putString("xy_4", xy_4);
            bundle.putString("rl", rl);
            bundle.putString("cl", cl);
            bundle.putString("db_cs", db_cs);
            bundle.putString("nl", nl);
            bundle.putString("ywgm1", ywgm1);
            bundle.putString("db_sj", db_sj);
            bundle.putString("db_rggm", db_rggm);
            bundle.putString("nl_sj", nl_sj);
            bundle.putString("yzr", yzr);
            bundle.putString("othername1", othername1);
            bundle.putString("othervalue1", othervalue1);
            bundle.putString("othername2", othername2);
            bundle.putString("othervalue2", othervalue2);
            bundle.putString("xybhd", xybhd);
            bundle.putString("xt", xt);
            if (mPhysicalSignInOtherFragment.isAdded()) {
                return;
            }
            mPhysicalSignInOtherFragment.setArguments(bundle);
            mFragments.add(mPhysicalSignInOtherFragment);
        }

        ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mFragmentManager, mFragments, mDataList);
        mViewPager.setAdapter(mExamplePagerAdapter);
        initMagicIndicator3();
    }

    private void initMagicIndicator3() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator3);
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#1FBAF3"));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(clipPagerTitleView);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float navigatorHeight = context.getResources().getDimension(R.dimen.common_navigator_height);
                float borderWidth = UIUtil.dip2px(context, 1);
                float lineHeight = navigatorHeight - 2 * borderWidth;
                indicator.setLineHeight(lineHeight);
                indicator.setRoundRadius(lineHeight / 2);
                indicator.setYOffset(borderWidth);
                indicator.setColors(Color.parseColor("#1FBAF3"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_pat_name:
                //病人切换
                TemperaturePresenter.showPatients();
                break;
            default:
                break;
        }
    }

    @Override
    public void showProgressDialog(String msg) {

    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showToast(String str) {
        ShowToast(str);
    }

    @Override
    public void initialSrc() {

    }

    @Override
    public void showKey(String[] strs, int flag) {

    }

    @Override
    public void setPatientInfo(SyncPatientBean patientInfo) {
        patName.setText(patientInfo.getName());
        patSex.setText(patientInfo.getSex());
        patBed.setText(StringTool.isBedNumber(patientInfo.getBedLabel()));
        patCode.setText(patientInfo.getPatCode());
        mPatID = patientInfo.getPatId();
        isInit = true;
        initData(isInit);
    }

    @Override
    public void setPatient(List<SyncPatientBean> list) {
        deptPatient.clear();
        deptPatient.addAll(list);
        if (isRefreshPatient) {
            isRefreshPatient = false;
            showPatients();
        }
    }

    //初始化数据
    private void initData(final Boolean isInit) {
        mProgressDialog = DialogUtils.createSimpleProgressDialog(this, "正在加载数据...");
        final HashMap<String, String> map = new HashMap<>() ;
        map.put("action","V0001");
        JSONObject message = new JSONObject();
        try {
            message.put("SourceSystem", "移动护理");
            message.put("SourceID", MacAddressUtils.getMacAddress(this));
            message.put("MessageID", "");
            message.put("UserCode", UserInfo.getUserName());
            message.put("UserName", UserInfo.getName());
            message.put("PAT_ID", mPatID);
            timePoint = SharedPreferenceUtils.getTimePoint(TemperatureAct.this);
            if (timePoint != null || !"null".equals(timePoint) || !"".equals(timePoint)) {
                timePoint = SharedPreferenceUtils.getTimePoint(TemperatureAct.this);
            }else{
                timePoint=TimeUtils.getCurrentTime().concat(" 02:00:00");
            }
			message.put("RECORDING_TIME", timePoint);
            message.put("AccessToken", "");
            map.put("message", message.toString());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = WebServiceUtils.callWebService(map);
                    if (!TextUtils.isEmpty(result)) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String resultContent = jsonObject.optString("ResultContent");
                            if ("Success".equals(resultContent)) {
                                JSONArray successIDList = jsonObject.getJSONArray("SuccessIDList");
                                JSONObject object = successIDList.getJSONObject(0);
                                /**基础信息**/
                                logTime = object.optString("LOG_TIME");  //记录时间
                                logStatus = object.optString("LOG_STATUS"); //记录状态
                                twMethod = object.optString("TW_METHOD");  //体温测量方式
                                tw = object.optString("TW");  //体温
                                twJw = object.optString("TW_JW");  //降温后体温
                                twJwfs = object.optString("TW_JWFS");//降温方式
                                mb = object.optString("MB");//脉搏
                                xl = object.optString("XL"); //心率
                                hx = object.optString("HX");//呼吸
                                hxj = object.optString("HX_HXJ"); //呼吸机
                                tt = object.optString("TT");//疼痛
                                /**其他信息**/
                                sg = object.optString("SG"); //身高
                                tz = object.optString("TZ");//体重
                                xy_1 = object.optString("XY_1");//上午血压1
                                xy_2 = object.optString("XY_2"); //上午血压2
                                xy_3 = object.optString("XY_3");//下午血压1
                                xy_4 = object.optString("XY_4"); //下午血压2
                                rl = object.optString("RL"); //入量
                                cl = object.optString("CL"); //出量
                                db_cs = object.optString("DB_CS");  //大便次数
                                nl = object.optString("NL");  //尿量
                                ywgm1 = object.optString("YWGM1");//药物过敏
                                db_sj = object.optString("DB_SJ");   //大便失禁
                                db_rggm = object.optString("DB_RGGM");  //人工肛门
                                nl_sj = object.optString("NL_SJ");  //小便失禁
                                yzr = object.optString("YZR");  //移植日
                                othername1 = object.optString("OTHERNAME1");  //自定义项目1
                                othervalue1 = object.optString("OTHERVALUE1"); //自定义对应值1
                                othername2 = object.optString("OTHERNAME2"); //自定义项目2
                                othervalue2 = object.optString("OTHERVALUE2"); //自定义对应值2
                                xybhd = object.optString("XYBHD"); //血氧饱和度
                                xt = object.optString("XT"); //血糖
                                if(isInit){
                                    mHandler.sendEmptyMessage(0);
                                }else{
                                    mHandler.sendEmptyMessage(1);
                                }
                            } else {
                                mHandler.sendEmptyMessage(-1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mHandler.sendEmptyMessage(-1);
                    }
                }
            }).start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void resetLayout() {
    }

    private boolean patternCode(String patternStr, String matcherStr) {
        Pattern p = Pattern.compile(patternStr.trim());
        Matcher m = p.matcher(matcherStr.trim());
        boolean b = m.matches();
        return b;
    }

    public void showExceptionDialog(String title, String msg) {
        final AlertDialogOneBtn alertDialogOneBtn = new AlertDialogOneBtn(this);
        alertDialogOneBtn.setButton(getResources().getString(R.string.make_sure));
        alertDialogOneBtn.setTitle(title);
        alertDialogOneBtn.setMessage(msg);
        alertDialogOneBtn.show();
        alertDialogOneBtn.setButtonListener(true, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogOneBtn.dismiss();
            }
        });
    }

    @Override
    public void showPatients() {
        dialog = new MyPatientDialog(TemperatureAct.this, new MyPatientDialog.MySnListener() {

            @Override
            public void myOnItemClick(View view, int position, long id) {
                TemperaturePresenter.setPatientInfo(deptPatient.get(position));
                whichPatients = position;
                Intent intent = new Intent();
                intent.putExtra("PAT_ID",deptPatient.get(position).getPatId());
                intent.setAction("com.herench.onItemClick");
                sendBroadcast(intent);
                //TODO:切换病人信息
//                isInit = false;
//                initData(isInit);
                dialog.dismiss();
            }

            @Override
            public void onRefresh() {
                isRefreshPatient = true;
                TemperaturePresenter.getPatients(TemperatureAct.this, 1024);// 1000标记用来刷新
                dialog.dismiss();
            }
        }, whichPatients, deptPatient);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    //扫描腕带
    public void DoCameraResult(String result) {
		List<BarcodeDict> barcodeDicts = LocalSave.getDataList(getApplicationContext(), GlobalConstant.KEY_BARCODE);
        String barcodeName = "";
        for (int i = 0; i < barcodeDicts.size(); i++) {
            if (patternCode(barcodeDicts.get(i).getRegularExp(), result)) {
                barcodeName = barcodeDicts.get(i).getBarcodeName();
                break;
            }
        }
        if (barcodeName.equals("PAT_BARCODE")) {
            boolean flag = true;
            for (int i = 0; i < deptPatient.size(); i++) {
                if (result.equals(deptPatient.get(i).getPatBarcode())) {
                    GlobalConstant.PATID = deptPatient.get(i).getPatId();
                    TemperaturePresenter.setPatients(deptPatient.get(i));
                    //切换病人 435951*3*
                    mPatID = result.replace("*", "_").substring(0,result.length()-1);
                    //TODO:切换病人信息
//                    isInit = false;
//                    initData(isInit);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                showExceptionDialog(getResources().getString(R.string.scan_excetion),
                        getResources().getString(R.string.scan_patient_no));
            }
        }
    }

    //将扫描到的基础信息传递到fragment中
    private void sendMessageToFragment() {
        Intent intent = new Intent();
        intent.putExtra("PAT_ID", mPatID);
        intent.putExtra("logTime", timePoint);
        intent.putExtra("logStatus", logStatus);
        intent.putExtra("twMethod", twMethod);
        intent.putExtra("tw", tw);
        intent.putExtra("twJw", twJw);
        intent.putExtra("twJwfs", twJwfs);
        intent.putExtra("mb", mb);
        intent.putExtra("xl", xl);
        intent.putExtra("hx", hx);
        intent.putExtra("hxj", hxj);
        intent.putExtra("tt", tt);
        intent.setAction("com.herench.change.person");
        sendBroadcast(intent);
    }

    //将扫描到的其他信息传递到fragment中
    private void sendInformationToFragment(){
        Intent intent = new Intent();
        intent.putExtra("PAT_ID", mPatID);
        intent.putExtra("sg", sg);
        intent.putExtra("tz", tz);
        intent.putExtra("xy_1", xy_1);
        intent.putExtra("xy_2", xy_2);
        intent.putExtra("xy_3", xy_3);
        intent.putExtra("xy_4", xy_4);
        intent.putExtra("rl", rl);
        intent.putExtra("cl", cl);
        intent.putExtra("db_cs", db_cs);
        intent.putExtra("nl", nl);
        intent.putExtra("ywgm1", ywgm1);
        intent.putExtra("db_sj", db_sj);
        intent.putExtra("db_rggm", db_rggm);
        intent.putExtra("nl_sj", nl_sj);
        intent.putExtra("yzr", yzr);
        intent.putExtra("othername1", othername1);
        intent.putExtra("othervalue1", othervalue1);
        intent.putExtra("othername2", othername2);
        intent.putExtra("othervalue2", othervalue2);
        intent.putExtra("xybhd", xybhd);
        intent.putExtra("xt", xt);
        intent.setAction("com.herench.change.person.other");
        sendBroadcast(intent);
    }

}
