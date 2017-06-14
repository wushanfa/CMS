package com.gentlehealthcare.mobilecare.fragment;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.adapter.RecordTimeAdapter;
import com.gentlehealthcare.mobilecare.tool.DialogUtils;
import com.gentlehealthcare.mobilecare.tool.MacAddressUtils;
import com.gentlehealthcare.mobilecare.tool.SharedPreferenceUtils;
import com.gentlehealthcare.mobilecare.tool.TimeUtils;
import com.gentlehealthcare.mobilecare.view.DefineKeyBoardView;
import com.gentlehealthcare.mobilecare.webService.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gentlehealthcare.mobilecare.R.id.tv_measured_temperature;


/**
 * 用户体征录入中的基础信息
 */
public class PhysicalSignInBasicFragment extends Fragment implements View.OnClickListener {
    private ProgressDialog mProgressDialog;
    private ListView listView;
    private RecordTimeAdapter adapter;
    private PopupWindow popupWindow;

    private static PhysicalSignInBasicFragment mPhysicalSignInBasicFragment;
    private ImageView recordTime;
    private EditText measuredTemperature;
    private EditText measuredType;
    private EditText coolType;
    private EditText etRecordTime;
    private Switch useventilator;
    private EditText breatingCount;
    private EditText temperature;
    private DefineKeyBoardView mDefineKeyBoardView;
    private Animation mEnterAnimation;
    private Animation mExitAnimation;
    private Map<String, TextView> mUnChange, mChange;
    private Map<Integer, String> mChangeText = new HashMap<>();
    private EditText spicalTemperature;
    private EditText pulse;
    private EditText heartRate;
    private EditText pain;
    private Button mSave;
    private boolean tag = true;
    private String mPatID;  //唯一流水号GUID
    //记录时间,记录状态,体温,体温测量方式,降温方式,降温后体温
    private String mRecordTime, mRecordStatus, mTemperature, mSignMethod, mCoolingDownMethod, mAfterCoolingDown;
    //脉搏,呼吸,心率,疼痛
    private String mPulse, mBearth, mHeartSpeed, mPain;
    //是否使用呼吸机(1:使用  0:不使用)
    private String isUseRespirator = "0";
    private String logTime, logStatus, twMethod, tw, twJw, twJwfs, mb, xl, hx, hxj, tt;  //基础信息
    private int mSaveResult;  //信息保存结果
    private MyReceiver mMyReceiver = null;
    private ArrayList<String> list = new ArrayList<>();  //项目名称
    private List<String> arrayList = new ArrayList<>();  //代码类型

    private class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("com.herench.onItemClick".equals(action)){
                mPatID = intent.getStringExtra("PAT_ID");
            }else if("com.herench.change.person".equals(action)){
                getBroadcastData(intent);
                setText();
            }
        }
    }

    private Handler handler = new Handler();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgressDialog();
            switch (msg.what) {
                case 0:
                    switch (mSaveResult) {
                        case 0:
                            Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                            break;
                        case -1:
                            Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 1:
                    popupWindow.showAtLocation(getActivity().findViewById(R.id.rl_recore_time), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    break;
                case -1:
                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //获取扫描数据
    private void getBroadcastData(Intent intent) {
        mPatID = intent.getStringExtra("PAT_ID");
        logTime = intent.getStringExtra("logTime");
        logStatus = intent.getStringExtra("logStatus");
        twMethod = intent.getStringExtra("twMethod");
        tw = intent.getStringExtra("tw");
        twJw = intent.getStringExtra("twJw");
        twJwfs = intent.getStringExtra("twJwfs");
        mb = intent.getStringExtra("mb");
        xl = intent.getStringExtra("xl");
        hx = intent.getStringExtra("hx");
        hxj = intent.getStringExtra("hxj");
        tt = intent.getStringExtra("tt");
    }

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            etRecordTime.setCursorVisible(false);
        }
    };

    synchronized public static PhysicalSignInBasicFragment newInstance() {
        if (mPhysicalSignInBasicFragment == null) {
            mPhysicalSignInBasicFragment = new PhysicalSignInBasicFragment();
        }
        return mPhysicalSignInBasicFragment;
    }

    public PhysicalSignInBasicFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyReceiver = new MyReceiver();
        getActivity().registerReceiver(mMyReceiver,new IntentFilter("com.herench.onItemClick"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initAnimation();
        getActivityIntentData();
        View inflate = inflater.inflate(R.layout.fragment_physical_sign_in_basic, container, false);
        initView(inflate);
        return inflate;
    }

    //获取从Activity获取的值
    private void getActivityIntentData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPatID = bundle.getString("PAT_ID");
            logTime = bundle.getString("logTime");
            logStatus = bundle.getString("logStatus");
            twMethod = bundle.getString("twMethod");
            tw = bundle.getString("tw");
            twJw = bundle.getString("twJw");
            twJwfs = bundle.getString("twJwfs");
            mb = bundle.getString("mb");
            xl = bundle.getString("xl");
            hx = bundle.getString("hx");
            hxj = bundle.getString("hxj");
            tt = bundle.getString("tt");
        }
    }

    private void initView(View inflate) {
        recordTime = (ImageView) inflate.findViewById(R.id.iv_record_time);
        measuredTemperature = (EditText) inflate.findViewById(tv_measured_temperature);
        etRecordTime = (EditText) inflate.findViewById(R.id.et_record_time);
        etRecordTime.setText(logTime);
        measuredType = (EditText) inflate.findViewById(R.id.tv_measure_type);
        coolType = (EditText) inflate.findViewById(R.id.tv_cool_type);
        useventilator = (Switch) inflate.findViewById(R.id.sw_use_ventilator);
        breatingCount = (EditText) inflate.findViewById(R.id.tv_breathing_count);
        temperature = (EditText) inflate.findViewById(R.id.tv_temperature);
        mDefineKeyBoardView = (DefineKeyBoardView) inflate.findViewById(R.id.layout_define_keyboard_define_key_board_view);
        spicalTemperature = (EditText) inflate.findViewById(R.id.tv_spical_temperature);
        pulse = (EditText) inflate.findViewById(R.id.tv_pulse);
        heartRate = (EditText) inflate.findViewById(R.id.tv_heart_rate);
        pain = (EditText) inflate.findViewById(R.id.tv_pain);
        mSave = (Button) inflate.findViewById(R.id.fragment_physical_sign_in_basic_save);
        setText();
        hideKeybord(measuredTemperature);
        hideKeybord(measuredType);
        hideKeybord(coolType);
        hideKeybord(breatingCount);
        hideKeybord(temperature);
        hideKeybord(spicalTemperature);
        hideKeybord(pulse);
        hideKeybord(heartRate);
        hideKeybord(pain);

        spicalTemperature.setOnTouchListener(new View.OnTouchListener() {

            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(spicalTemperature);
                }
                return false;
            }
        });
        breatingCount.setOnTouchListener(new View.OnTouchListener() {

            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    if (tag) {
                        initBrothData();
                        mDefineKeyBoardView.setVisibility(View.VISIBLE);
                        keyboard(breatingCount);
                    }
                }
                return false;
            }
        });
        pulse.setOnTouchListener(new View.OnTouchListener() {

            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initPulseData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(pulse);
                }
                return false;
            }
        });
        heartRate.setOnTouchListener(new View.OnTouchListener() {

            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initPulseData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(heartRate);
                }
                return false;
            }
        });
        pain.setOnTouchListener(new View.OnTouchListener() {

            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initBasicData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(pain);
                }
                return false;
            }
        });
//        measuredTemperature.setOnTouchListener(new View.OnTouchListener() {
//            //按住和松开的标识
//            int touch_flag = 0;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                touch_flag++;
//                if (touch_flag == 2) {
//                    touch_flag = 0;
//                    initDate("measured_temperature");
//                    initPop("measured_temperature");
//                    popupWindow.showAtLocation(getActivity().findViewById(R.id.rl_recore_time), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
//                }
//                return false;
//            }
//        });
//        measuredType.setOnTouchListener(new View.OnTouchListener() {
//            //按住和松开的标识
//            int touch_flag = 0;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                touch_flag++;
//                if (touch_flag == 2) {
//                    touch_flag = 0;
//                    initDate("measure_type");
//                    initPop("measure_type");
//                    popupWindow.showAtLocation(getActivity().findViewById(R.id.rl_recore_time), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
//                }
//                return false;
//            }
//        });
//        coolType.setOnTouchListener(new View.OnTouchListener() {
//            //按住和松开的标识
//            int touch_flag = 0;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                touch_flag++;
//                if (touch_flag == 2) {
//                    touch_flag = 0;
//                    initDate("cool_type");
//                    initPop("cool_type");
//                    popupWindow.showAtLocation(getActivity().findViewById(R.id.rl_recore_time), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
//                }
//                return false;
//            }
//        });
        etRecordTime.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    etRecordTime.setCursorVisible(true);
                }
                return false;
            }
        });
        temperature.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(temperature);
                }
                return false;
            }
        });
        recordTime.setOnClickListener(this);
        useventilator.setOnClickListener(this);
        etRecordTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (delayRun != null) {
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 5000);
            }
        });

        breatingCount.setClickable(true);
        spicalTemperature.setOnClickListener(this);
        breatingCount.setOnClickListener(this);
        pulse.setOnClickListener(this);
        heartRate.setOnClickListener(this);
        pain.setOnClickListener(this);
        recordTime.setOnClickListener(this);
        measuredTemperature.setOnClickListener(this);
        measuredType.setOnClickListener(this);
        coolType.setOnClickListener(this);
        etRecordTime.setOnClickListener(this);
        useventilator.setOnClickListener(this);
        temperature.setOnClickListener(this);
        mSave.setOnClickListener(this);

        useventilator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isUseRespirator = "1";
                } else {
                    isUseRespirator = "0";
                }
            }
        });
    }

    //给UI设置数据
    private void setText() {
        if ("null".equals(logTime)) {
            logTime = "";
        }
        if ("null".equals(logStatus)) {
            logStatus = "";
        }
        if ("null".equals(twMethod)) {
            twMethod = "";
        }
        if ("null".equals(tw)) {
            tw = "";
        }
        if ("null".equals(twJw)) {
            twJw = "";
        }
        if ("null".equals(twJwfs)) {
            twJwfs = "";
        }
        if ("null".equals(mb)) {
            mb = "";
        }
        if ("null".equals(xl)) {
            xl = "";
        }
        if ("null".equals(hx)) {
            hx = "";
        }
        if("null".equals(hxj)){
            hxj = "";
        }
        if ("null".equals(tt)) {
            tt = "";
        }

        measuredTemperature.setText(logStatus);
        measuredType.setText(twMethod);
        temperature.setText(tw);
        spicalTemperature.setText(twJw);
        coolType.setText(twJwfs);
        pulse.setText(mb);
        heartRate.setText(xl);
        breatingCount.setText(hx);
        if("0".equals(hxj)){
            breatingCount.setText("");
            breatingCount.setBackground(getActivity().getResources().getDrawable(R.drawable.rectangular_border_bg));
            breatingCount.setClickable(true);
            breatingCount.setFocusable(true);
            breatingCount.setCursorVisible(true);
            tag = true;
        }else if("1".equals(hxj)){
            breatingCount.setText("");
            breatingCount.setBackground(getActivity().getResources().getDrawable(R.drawable.rectangular_border_gray_bg));
            breatingCount.setClickable(false);
            breatingCount.setFocusable(false);
            breatingCount.setCursorVisible(false);
            tag = false;
        }
        pain.setText(tt);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_record_time:
                hintKbTwo();
                mDefineKeyBoardView.setVisibility(View.GONE);
                initPop("record_time");
                initDate("record_time");
                break;
            case R.id.tv_measured_temperature:
                hintKbTwo();
                mDefineKeyBoardView.setVisibility(View.GONE);
                initPop("measured_temperature");
                initDate("measured_temperature");
                break;
            case R.id.tv_measure_type:
                hintKbTwo();
                mDefineKeyBoardView.setVisibility(View.GONE);
                initPop("measure_type");
                initDate("measure_type");
                break;
            case R.id.tv_cool_type:
                hintKbTwo();
                mDefineKeyBoardView.setVisibility(View.GONE);
                initPop("cool_type");
                initDate("cool_type");
                break;
            case R.id.sw_use_ventilator:
                if (useventilator.isChecked()) {
                    breatingCount.setText("");
                    breatingCount.setBackground(getActivity().getResources().getDrawable(R.drawable.rectangular_border_gray_bg));
                    breatingCount.setClickable(false);
                    breatingCount.setFocusable(false);
                    breatingCount.setCursorVisible(false);
                    tag = false;
                } else {
                    breatingCount.setText("");
                    breatingCount.setBackground(getActivity().getResources().getDrawable(R.drawable.rectangular_border_bg));
                    breatingCount.setClickable(true);
                    breatingCount.setFocusable(true);
                    breatingCount.setCursorVisible(true);
                    tag = true;
                }
                break;
            case R.id.fragment_physical_sign_in_basic_save:
                //保存
                final HashMap<String, String> map = new HashMap<>();
                map.put("action", "V0011");
                mRecordTime = etRecordTime.getText().toString().trim();
//                if(TextUtils.isEmpty(mRecordTime) || mRecordTime.equals("")){
//                    Toast.makeText(getActivity(),"请选择记录时间",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                mRecordStatus = measuredTemperature.getText().toString().trim();
//                if(TextUtils.isEmpty(mRecordStatus) || mRecordStatus.equals("")){
//                    Toast.makeText(getActivity(),"请选择记录状态",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mTemperature = temperature.getText().toString().trim();
//                if(TextUtils.isEmpty(mTemperature) || mTemperature.equals("")){
//                    Toast.makeText(getActivity(),"请输入体温",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mSignMethod = measuredType.getText().toString().trim();
//                if(TextUtils.isEmpty(mSignMethod) || mSignMethod.equals("")){
//                    Toast.makeText(getActivity(),"请选择测量方式",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mCoolingDownMethod = coolType.getText().toString().trim();
//                if(TextUtils.isEmpty(mCoolingDownMethod) || mCoolingDownMethod.equals("")){
//                    Toast.makeText(getActivity(),"请选择降温方式",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mAfterCoolingDown = spicalTemperature.getText().toString().trim();
//                if(TextUtils.isEmpty(mAfterCoolingDown) || mAfterCoolingDown.equals("")){
//                    Toast.makeText(getActivity(),"请输入特殊体温",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mPulse = pulse.getText().toString().trim();
//                if(TextUtils.isEmpty(mPulse) || mPulse.equals("")){
//                    Toast.makeText(getActivity(),"请输入脉搏",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mBearth = breatingCount.getText().toString().trim();
                mHeartSpeed = heartRate.getText().toString().trim();
//                if(TextUtils.isEmpty(mHeartSpeed) || mHeartSpeed.equals("")){
//                    Toast.makeText(getActivity(),"请输入心率",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mPain = pain.getText().toString().trim();
//                if(TextUtils.isEmpty(mPain) || mPain.equals("")){
//                    Toast.makeText(getActivity(),"请输入疼痛",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mProgressDialog = DialogUtils.createSimpleProgressDialog(getActivity(), "正在保存");
                JSONObject message = new JSONObject();
                try {
                    message.put("SourceSystem", "移动护理");
                    message.put("SourceID", MacAddressUtils.getMacAddress(getActivity()));
                    message.put("MessageID", "");
                    message.put("UserCode", (UserInfo.getUserName()));
                    message.put("UserName", UserInfo.getName());
                    message.put("PAT_ID", mPatID);
                    message.put("RECORDING_TIME", mRecordTime);  //记录时间
                    SharedPreferenceUtils.saveTimePoint(getActivity(),TimeUtils.getCurrentTime(mRecordTime));
                    message.put("LOG_BY", UserInfo.getUserName());  //执行护士
                    message.put("LOG_STATUS", mRecordStatus);  //记录状态
                    message.put("TW", mTemperature);  //体温
                    message.put("TW_METHOD", mSignMethod);  //体温测量方式
//                  message.put("TW_BS", "体温不升");  //TODO:体温不升(待考虑)
                    message.put("TW_JWFS", mCoolingDownMethod);  //降温方式
                    message.put("TW_JW", mAfterCoolingDown);  //降温后体温
//                  message.put("TW_CC","");  //TODO:重测体温(待考虑)
                    message.put("MB", mPulse); //脉搏
                    message.put("HX", mBearth); //呼吸
                    message.put("HX_HXJ", isUseRespirator);  //使用呼吸机
                    message.put("XL", mHeartSpeed);  //心率
                    message.put("TT", mPain);  //疼痛
                    message.put("AccessToken", "");
                    map.put("message", message.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = WebServiceUtils.callWebService(map);
                        if (s != null && !TextUtils.isEmpty(s)) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                mSaveResult = jsonObject.optInt("ResultCode");
                                mHandler.sendEmptyMessage(0);
                            } catch (JSONException e) {
                                mHandler.sendEmptyMessage(-1);
                            }
                        } else {
                            mHandler.sendEmptyMessage(-1);
                        }
                    }
                }).start();
                break;
        }
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getActivity().getCurrentFocus() != null) {
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void hideKeybord(EditText textAmount) {
        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            textAmount.setInputType(InputType.TYPE_NULL);
        } else {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(textAmount, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //初始化基本键盘数据
    private void initBasicData() {
        mChangeText.clear();
        for (int i = 0; i < 8; i++) {
            mChangeText.put(i, 10 + i + "");
        }
    }

    private void initData() {
        mChangeText.clear();
        for (int i = 0; i < 8; i++) {
            mChangeText.put(i, 35 + i + "");
        }
    }

    //初始化脉搏键盘数据
    private void initPulseData() {
        mChangeText.clear();
        int number = 50;
        for (int i = 0; i < 8; i++) {
            mChangeText.put(i, String.valueOf(number += 10));
        }
    }

    //初始化呼吸键盘数据
    private void initBrothData() {
        mChangeText.clear();
        for (int i = 0; i < 8; i++) {
            mChangeText.put(i, 15 + i + "");
        }
    }


    private void initDate(String tag) {
        list.clear();
        arrayList.clear();
        //获取时间节点
        if (tag.equals("record_time")) {
            mProgressDialog = DialogUtils.createSimpleProgressDialog(getActivity(), "正在获取时间点...");
            final HashMap<String, String> map = new HashMap<>();
            map.put("action", "V0002");
            JSONObject message = new JSONObject();
            try {
                message.put("SourceSystem", "移动护理");
                message.put("SourceID", MacAddressUtils.getMacAddress(getActivity()));
                message.put("MessageID", "");
                message.put("UserCode", UserInfo.getUserName());
                message.put("UserName", UserInfo.getName());
                message.put("PAT_ID", mPatID);
                message.put("DATA_DATE", TimeUtils.getCurrentTime());
                message.put("AccessToken", "");
                map.put("message", message.toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = WebServiceUtils.callWebService(map);
                        if (s != null && !TextUtils.isEmpty(s)) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String resultContent = jsonObject.optString("ResultContent");
                                if("Success".equals(resultContent)){
                                    JSONArray successIDList = jsonObject.getJSONArray("SuccessIDList");
                                    int length = successIDList.length();
                                    for (int i = 0; i < length; i++) {
                                        JSONObject timePoint = (JSONObject) successIDList.get(i);
                                        String time_point = timePoint.optString("TIME_POINT").trim();
                                        String substring1 = time_point.substring(11, 16);
                                        list.add(substring1);
                                        mHandler.sendEmptyMessage(1);
//                                        String[] split = time_point.split(" ");
//                                        String dateTime = null;
//                                        for (int j = 0; j < split.length; j++) {
//                                            if(j == 2){
//                                                dateTime = split[2];
//                                            }
//                                        }
//                                        if (dateTime != null) {
//                                            String substring = dateTime.substring(0, 5);
//                                            list.add(substring);
//                                            mHandler.sendEmptyMessage(1);
//                                        }
                                    }
                                }else{
                                    mHandler.sendEmptyMessage(-1);
                                }
                            } catch (JSONException e) {
                                mHandler.sendEmptyMessage(-1);
                            }
                        } else {
                            mHandler.sendEmptyMessage(-1);
                        }
                    }
                }).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }

        //获取记录状态  SELECT * FROM cp_dict_detail FOR UPDATE ;
        if (tag.equals("measured_temperature")) {
            mProgressDialog = DialogUtils.createSimpleProgressDialog(getActivity(), "正在获取记录状态...");
            final HashMap<String, String> map = new HashMap<>();
            map.put("action", "C0010");
            JSONObject message = new JSONObject();
            try {
                message.put("SourceSystem", "移动护理");
                message.put("SourceID", MacAddressUtils.getMacAddress(getActivity()));
                message.put("MessageID", "");
                message.put("UserCode", UserInfo.getUserName());
                message.put("UserName", UserInfo.getName());
                message.put("CLASS_CODE", "LOG_STATUS");
                message.put("AccessToken", "");
                map.put("message",message.toString());

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String result = WebServiceUtils.callWebService(map);
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String resultContent = jsonObject.optString("ResultContent");
                                if("Success".equals(resultContent)){
                                    JSONArray successIDList = jsonObject.getJSONArray("SuccessIDList");
                                    int length = successIDList.length();
                                    for (int i = 0; i < length; i++) {
                                        JSONObject jsonObject1 = (JSONObject) successIDList.get(i);
                                        String item_name = jsonObject1.optString("ITEM_NAME");
                                        String item_code = jsonObject1.optString("ITEM_CODE");
                                        list.add(item_name);
                                        arrayList.add(item_code);
                                        mHandler.sendEmptyMessage(1);
                                    }
                                }else{
                                    mHandler.sendEmptyMessage(-1);
                                }


                            } catch (JSONException e) {
                                mHandler.sendEmptyMessage(-1);
                            }
                        } else {
                            mHandler.sendEmptyMessage(-1);
                        }
                    }
                }).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }

        //体温测量方式
        if (tag.equals("measure_type")) {
            mProgressDialog = DialogUtils.createSimpleProgressDialog(getActivity(), "正在获取体温测量方式...");
            final HashMap<String, String> map = new HashMap<>();
            map.put("action", "C0010");
            JSONObject message = new JSONObject();
            try {
                message.put("SourceSystem", "移动护理");
                message.put("SourceID", MacAddressUtils.getMacAddress(getActivity()));
                message.put("MessageID", "");
                message.put("UserCode", UserInfo.getUserName());
                message.put("UserName", UserInfo.getName());
                message.put("CLASS_CODE", "TW_METHOD");  //EXAM_METHOD_T
                message.put("AccessToken", "");
                map.put("message",message.toString());

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String result = WebServiceUtils.callWebService(map);
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String resultContent = jsonObject.optString("ResultContent");
                                if("Success".equals(resultContent)){
                                    JSONArray successIDList = jsonObject.getJSONArray("SuccessIDList");
                                    int length = successIDList.length();
                                    for (int i = 0; i < length; i++) {
                                        JSONObject jsonObject1 = (JSONObject) successIDList.get(i);
                                        String item_name = jsonObject1.optString("ITEM_NAME");
                                        String item_code = jsonObject1.optString("ITEM_CODE");
                                        list.add(item_name);
                                        arrayList.add(item_code);
                                        mHandler.sendEmptyMessage(1);
                                    }
                                }else{
                                    mHandler.sendEmptyMessage(-1);
                                }


                            } catch (JSONException e) {
                                mHandler.sendEmptyMessage(-1);
                            }
                        } else {
                            mHandler.sendEmptyMessage(-1);
                        }
                    }
                }).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        //降温方式
        if (tag.equals("cool_type")) {
            mProgressDialog = DialogUtils.createSimpleProgressDialog(getActivity(), "正在获取降温方式...");
            final HashMap<String, String> map = new HashMap<>();
            map.put("action", "C0010");
            JSONObject message = new JSONObject();
            try {
                message.put("SourceSystem", "移动护理");
                message.put("SourceID", MacAddressUtils.getMacAddress(getActivity()));
                message.put("MessageID", "");
                message.put("UserCode", UserInfo.getUserName());
                message.put("UserName", UserInfo.getName());
                message.put("CLASS_CODE", "TW_JWFS");
                message.put("AccessToken", "");
                map.put("message",message.toString());

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String result = WebServiceUtils.callWebService(map);
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String resultContent = jsonObject.optString("ResultContent");
                                if("Success".equals(resultContent)){
                                    JSONArray successIDList = jsonObject.getJSONArray("SuccessIDList");
                                    int length = successIDList.length();
                                    for (int i = 0; i < length; i++) {
                                        JSONObject jsonObject1 = (JSONObject) successIDList.get(i);
                                        String item_name = jsonObject1.optString("ITEM_NAME");
                                        String item_code = jsonObject1.optString("ITEM_CODE");
                                        list.add(item_name);
                                        arrayList.add(item_code);
                                        mHandler.sendEmptyMessage(1);
                                    }
                                }else{
                                    mHandler.sendEmptyMessage(-1);
                                }


                            } catch (JSONException e) {
                                mHandler.sendEmptyMessage(-1);
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
    }

    private void initPop(final String tag) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_record_time, null);
        adapter = new RecordTimeAdapter(getActivity(), list, tag);
        listView = (ListView) view.findViewById(R.id.lv_record_time);
        listView.setAdapter(adapter);
        ViewGroup.LayoutParams LayoutParams = listView.getLayoutParams();
        WindowManager WindowManager = (android.view.WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int herig = WindowManager.getDefaultDisplay().getHeight();
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        int i = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        if (i > herig / 3) {
            LayoutParams.height = herig / 3;
        } else {
            LayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        listView.setLayoutParams(LayoutParams);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (tag.equals("record_time")) {
                    etRecordTime.setText(list.get(arg2));
                    SharedPreferenceUtils.saveTimePoint(getActivity(),etRecordTime.getText().toString().trim());
                    etRecordTime.setSelection(etRecordTime.getText().toString().length());
                    popupWindow.dismiss();
                }
                if (tag.equals("measured_temperature")) {
                    mRecordStatus = arrayList.get(arg2);
                    measuredTemperature.setText(list.get(arg2));
                    measuredTemperature.setSelection(measuredTemperature.getText().toString().length());
                    popupWindow.dismiss();

                }
                if (tag.equals("measure_type")) {
                    measuredType.setText(list.get(arg2));
                    measuredType.setSelection(measuredType.getText().toString().length());
                    popupWindow.dismiss();
                }
                if (tag.equals("cool_type")) {
                    coolType.setText(list.get(arg2));
                    coolType.setSelection(coolType.getText().toString().length());
                    popupWindow.dismiss();
                }
            }
        });
    }

    private void initAnimation() {
        mEnterAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_bottom_in);
        mExitAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_bottom_out);
    }

    private void keyboard(final EditText mEditText) {
        mEditText.setOnClickListener(this);
        mDefineKeyBoardView.getLayoutBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefineKeyBoardView.startAnimation(mExitAnimation);
                mDefineKeyBoardView.setVisibility(View.GONE);
            }
        });

        mUnChange = mDefineKeyBoardView.getUnChangeableView();
        mUnChange.get("<").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = mEditText.getText().toString().trim();
                if (amount.length() > 0) {
                    amount = amount.substring(0, amount.length() - 1);
                    mEditText.setText(amount);
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("清").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText(null);
            }
        });
        mUnChange.get(".").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("."));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText(".");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("关闭").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefineKeyBoardView.startAnimation(mExitAnimation);
                mDefineKeyBoardView.setVisibility(View.GONE);
            }
        });
        mUnChange.get("1").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("1"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("1");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("2").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("2"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("2");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("3").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("3"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("3");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("4").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("4"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("4");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("5").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("5"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("5");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("6").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("6"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("6");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("7").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("7"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("7");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("8").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("8"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("8");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("9").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("9"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("9");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mUnChange.get("0").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append("0"));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText("0");
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });

        mChange = mDefineKeyBoardView.getChangeableView();
        mChange.get("one").setText(mChangeText.get(0));
        mChange.get("one").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append(mChangeText.get(0)));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText(mChangeText.get(0));
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mChange.get("two").setText(mChangeText.get(1));
        mChange.get("two").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append(mChangeText.get(1)));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText(mChangeText.get(1));
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mChange.get("three").setText(mChangeText.get(2));
        mChange.get("three").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append(mChangeText.get(2)));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText(mChangeText.get(2));
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mChange.get("four").setText(mChangeText.get(3));
        mChange.get("four").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append(mChangeText.get(3)));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText(mChangeText.get(3));
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mChange.get("five").setText(mChangeText.get(4));
        mChange.get("five").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append(mChangeText.get(4)));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText(mChangeText.get(4));
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mChange.get("six").setText(mChangeText.get(5));
        mChange.get("six").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append(mChangeText.get(5)));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText(mChangeText.get(5));
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mChange.get("seven").setText(mChangeText.get(6));
        mChange.get("seven").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append(mChangeText.get(6)));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText(mChangeText.get(6));
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
        mChange.get("eight").setText(mChangeText.get(7));
        mChange.get("eight").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null) {
                    StringBuilder sb = new StringBuilder(mEditText.getText());
                    mEditText.setText(sb.append(mChangeText.get(7)));
                    mEditText.setSelection(sb.length());
                } else {
                    mEditText.setText(mChangeText.get(7));
                    mEditText.setSelection(mEditText.getText().length());
                }
            }
        });
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
