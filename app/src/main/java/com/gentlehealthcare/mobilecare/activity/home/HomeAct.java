package com.gentlehealthcare.mobilecare.activity.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.activity.ActivityControlTool;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.login.ChooseGroupAct;
import com.gentlehealthcare.mobilecare.activity.msg.MsgAct;
import com.gentlehealthcare.mobilecare.activity.patient.DeptPatientAct;
import com.gentlehealthcare.mobilecare.activity.patient.MyPatientListFra;
import com.gentlehealthcare.mobilecare.activity.patient.PatientAct;
import com.gentlehealthcare.mobilecare.activity.work.WorkAct;
import com.gentlehealthcare.mobilecare.bean.sys.AppConfig;
import com.gentlehealthcare.mobilecare.bean.sys.OrdersClassDict;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;
import com.gentlehealthcare.mobilecare.net.bean.WardInfoItem;
import com.gentlehealthcare.mobilecare.service.TipService;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.GroupInfoSave;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.ScreenTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.MyFragmentAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主界面
 */
public class HomeAct extends ABToolBarActivity {
    private static final String TAG = "首页";
    private int screenWidth;
    private ViewPager viewPager;
    private Button btn_work;
    private RelativeLayout rl_home_content;
    private GroupInfoSave groupInfoSave;
    private RelativeLayout ll_home_bg;
    float x = 0, moveX = 0;
    private LayoutParams params;
    MyWhiteBoardFra myWhiteBoardFra = null;
    private boolean toMyPatient = false;
    private boolean isNotRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, TipService.class));
        toMyPatient = getIntent().getBooleanExtra("toMyPatient", false);
        loadOrderClass();
        getOrderLimitedTime();
        // 工具栏配置
        int[] toolBar = new int[]{R.drawable.act_home_deptpatbtn, R.drawable.act_home_mypatbtn, R.drawable
                .act_home_homebtn, R.drawable.act_home_workbtn};
        setToolBarDrawable(toolBar);
        setRightBtnDrawable(R.drawable.act_home_chaxunbtn);
        setAbDoToolBar(new ABDoToolBar() {
            @Override
            public void onCheckedChanged(int i) {
                switch (i) {
                    case 0:
                        // 护理单元病人
                        startActivity(new Intent(HomeAct.this, DeptPatientAct.class));
                        break;
                    case 1:
                        startActivity(new Intent(HomeAct.this, DeptPatientAct.class));
                        break;
                    case 2:
                        if (!isNotRefresh) {
                            viewPager.setCurrentItem(1);
                        }
                        isNotRefresh = false;
                        break;
                    case 3:
                        // 交接事项
                        // startActivity(new Intent(HomeAct.this, WorkAct.class));
                        break;
                    case 4:
                        // 系统注意事项
                        startActivity(new Intent(HomeAct.this, SystemAttentionAct.class));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {

            }
        });

        ll_home_bg = (RelativeLayout) findViewById(R.id.ll_home_bg);
        viewPager = (ViewPager) findViewById(R.id.vp_home);
        btn_work = (Button) findViewById(R.id.btn_work);
        btn_work.setBackgroundResource(R.drawable.skin_forward);
        rl_home_content = (RelativeLayout) findViewById(R.id.rl_home_content);
        groupInfoSave = GroupInfoSave.getInstance(this);
        String str = groupInfoSave.get();
        List<WardInfoItem> list = UserInfo.getWardList();
        boolean flag = false;
        if (!str.equals("")) {
            if (list != null && list.size() > 0) {
                for (WardInfoItem wardInfoItem : list) {
                    if (wardInfoItem.getWardName().equals(str)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                str = "";
            }
        } else if (list == null || list.size() <= 0) {
            str = "";
        }
        GroupInfoSave.getInstance(HomeAct.this).save(str);
        if (str.equals("") && list != null && list.size() > 0) {
            startActivity(new Intent(HomeAct.this, ChooseGroupAct.class));
        }
        params = (LayoutParams) viewPager.getLayoutParams();
        screenWidth = ScreenTool.getScreenWidth(this);
        params.width = screenWidth / 12 * 11;
        List<Fragment> fragments = new ArrayList<Fragment>();
        // 病人清单
        fragments.add(new MyPatientListFra());
        // 我的白板
        fragments.add(myWhiteBoardFra = new MyWhiteBoardFra());
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOnPageChangeListener(pageChangeListener);
        viewPager.setPageTransformer(true, pageTransformer);
        viewPager.setCurrentItem(toMyPatient ? 0 : 1);
        btn_work.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HomeAct.this, WorkAct.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitApp();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCheck(toMyPatient ? 1 : 2);
        if (UserInfo.getUserName() == null) {
            finish();
        }
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.ll_home_bg);
        SupportDisplay.resetAllChildViewParam(root);
    }

    OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == 1) {
                viewPager.setX(screenWidth / 10);
                params.width = screenWidth * 9 / 10;
                isNotRefresh = true;
                changeCheck(2);
            } else if (arg0 == 0) {
                params.width = screenWidth * 9 / 10;
                viewPager.setX(-screenWidth / 60);
                isNotRefresh = true;
                changeCheck(1);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    PageTransformer pageTransformer = new PageTransformer() {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void transformPage(View view, float position) {

            float MIN_SCALE = 0.85f;
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position) / 3);
                float vertMargin = pageHeight * (1 - scaleFactor) / 7;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0);
            }
        }
    };

    private long exitTime = 0;

    /**
     * 退出程序
     */
    public void ExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(HomeAct.this, R.string.pressoncetoexittheprogram, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            ActivityControlTool.finishAll(true);
        }
    }

    /**
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {
//        SharedPreferences sharedPreferences = getSharedPreferences(
//                GlobalConstant.KEY_BARCODE, Activity.MODE_PRIVATE);
//        String patBarcode = sharedPreferences.getString(
//                GlobalConstant.KEY_PATBARCODE, "");
//        if(patternCode(patBarcode,result)){
//            String patCodeResult=result;
//            if(patBarcode.contains("*")){
//                patCodeResult=patCodeResult.split("\\*")[0];
//            }
//            //loadCurrentPatient(UserInfo.getUserName(), result);
//            loadNoticesByConformStatus(patCodeResult);
//        }else{
//            ShowToast("请扫描病人腕带进行巡视");
//        }
    }

    private boolean patternCode(String patternStr, String matcherStr) {
        Pattern p = Pattern.compile(patternStr.trim());
        Matcher m = p.matcher(matcherStr.trim());
        boolean b = m.matches();
        return b;
    }

    public void loadNoticesByConformStatus(final String patCode) {
        HttpUtils http = new HttpUtils();
        String url = UrlConstant.loadNoticesByConformStatus() + UserInfo.getUserName() + "&patCode=" + patCode + "&wardCode=" + UserInfo.getWardCode();
        CCLog.i(TAG, "加载病人提示消息>>" + url);
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                CCLog.i(TAG, "加载给病人提示消息时出错了");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<TipBean>>() {
                }.getType();
                try {
                    JSONObject jsonObject = new JSONObject(arg0.result);
                    boolean result = jsonObject.getBoolean("result");
                    if (result) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        List<TipBean> mTips = gson.fromJson(jsonArray.toString(), type);
                        if (mTips != null && !mTips.isEmpty()) {
                            toMsgAct(mTips, patCode);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void toMsgAct(List<TipBean> orders, String patCode) {

        if (orders != null && !orders.isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra(GlobalConstant.KEY_BARCODE,
                    patCode);
            intent.putExtra(GlobalConstant.KEY_EXECUTEING_ORDERS, (Serializable) orders);
            intent.setClass(HomeAct.this,
                    MsgAct.class);
            startActivityForResult(intent,
                    GlobalConstant.REQUEST_CODE);
        }
    }

    /**
     * 加载当前的病人
     */
    public void loadCurrentPatient(String username, String patCode) {
        final ProgressDialog pd = ProgressDialog.show(this, null, getResources().getString(R.string.dataloading));
        CCLog.i(getResources().getString(R.string.HomeActLoadCurrentPatient), UrlConstant.loadCurrentPatient() +
                username + "&patCode=" + patCode + "&wardCode=null");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadCurrentPatient() + username + "&patCode=" +
                patCode + "&wardCode=null", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd.dismiss();
                SyncPatientBean patient = new SyncPatientBean();
                JSONObject jsonobject = null;
                JSONObject data = null;
                String eventId = "";
                boolean result = false;
                try {
                    jsonobject = new JSONObject(responseInfo.result);
                    data = jsonobject.getJSONObject(GlobalConstant.KEY_PATIENT);
                    eventId = (String) jsonobject.get("eventId");
                    result = jsonobject.getBoolean("result");
                    if (result) {
                        patient.setBedLabel(data.getString("bedLabel"));
                        patient.setPatId(data.getString("patId"));
                        patient.setName(data.getString("name"));
                        patient.setPatCode(data.getString("patCode"));
                        patient.setAge(data.getString("age"));
                        patient.setSex(data.getString("sex"));
                        patient.setVisitId(data.getString("visitId"));
                        patient.setAdmissionDate(data.getString("admissionDate"));
                        patient.setComplitedTask(data.getInt("complitedTask"));
                        patient.setExecutingTask(data.getInt("executingTask"));
                        patient.setAdmissionDiagName(data.getString("admissionDiagName"));
                        patient.setDoctorInCharge(data.getString("doctorInCharge"));
                        patient.setNursingGrade(data.getString("nursingGrade"));
                        patient.setPerformTask(data.getInt("performTask"));
                        patient.setWardName(data.getString("wardName"));
                        // patient.setNursingKeywords(null);
                        patient.setWardCode(data.getString("wardCode"));
                        if (patient != null) {
                            Intent intent = new Intent();
                            intent.putExtra(GlobalConstant.KEY_PATIENT, patient);
                            intent.putExtra("eventId", eventId);
                            intent.setClass(HomeAct.this, PatientAct.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ShowToast(getResources().getString(R.string.fialtoloadpatient) + null);
                        }
                    } else {
                        ShowToast(getResources().getString(R.string.fialtoloadpatient));
                    }
                } catch (JSONException e) {
                    if (LinstenNetState.isLinkState(getApplicationContext())) {
                        ShowToast(getResources().getString(R.string.fialtoloadpatient));
                    } else {
                        toErrorAct();
                    }

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
                toErrorAct();
            }
        });
    }

    /**
     * 加载医嘱界面筛选条件
     */
    public void loadOrderClass() {

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.loadOrderClass(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONArray jsonArray = null;
                try {
                    List<OrdersClassDict> list = new ArrayList<OrdersClassDict>();
                    jsonArray = new JSONArray(responseInfo.result);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<OrdersClassDict>>() {
                    }.getType();
                    list = gson.fromJson(jsonArray.toString(), type);
                    for (int i = 0; i < list.size(); i++) {
                        LocalSave.saveStringData(HomeAct.this, "item" + i, list.get(i).getNameCode());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(HomeAct.this, ErrorAct.class);
        startActivity(intent);
    }

    private void getOrderLimitedTime() {
        HttpUtils http = new HttpUtils();
        String url = UrlConstant.getOrderLimitedTime() + "mobilecare" + "&fileName=orderlimitedtime&dbUser=comm";
        CCLog.i("加载医嘱执行上下限>>>",url);
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SharedPreferences sp = getSharedPreferences(GlobalConstant.KEY_ORDER_LIMITED_TIME, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                int mfloortime=60;
                int mceiltime=60;
                JSONObject object = null;
                boolean status = false;
                try {
                    object = new JSONObject(responseInfo.result);
                    status = object.getBoolean("result");
                    if (status) {
                        JSONArray jsary=object.getJSONArray("data");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<AppConfig>>() {
                        }.getType();
                        List<AppConfig> appConfigs = gson.fromJson(jsary.toString(), type);
                        if (appConfigs.size() == 2) {
                            for (int i = 0; i < 2; i++) {
                                if (appConfigs.get(i).getKey().equals("floortime")) {
                                    mfloortime = Integer.parseInt(appConfigs.get(i).getValue());
                                } else if ("ceiltime".equals(appConfigs.get(i).getKey())) {
                                    mceiltime = Integer.parseInt(appConfigs.get(i).getValue());
                                }
                            }
                            editor.putInt(GlobalConstant.KEY_CEILING, mceiltime);
                            editor.putInt(GlobalConstant.KEY_FLOOR, mfloortime);
                            editor.commit();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
