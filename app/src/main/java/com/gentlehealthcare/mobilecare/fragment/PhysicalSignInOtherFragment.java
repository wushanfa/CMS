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
import com.gentlehealthcare.mobilecare.tool.TimeUtils;
import com.gentlehealthcare.mobilecare.view.DefineKeyBoardView;
import com.gentlehealthcare.mobilecare.webService.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.gentlehealthcare.mobilecare.R.id.iv_drug_allergy;

/**
 * 用户体征录入的其他信息
 */
public class PhysicalSignInOtherFragment extends Fragment implements View.OnClickListener {

    private static PhysicalSignInOtherFragment mPhysicalSignInOtherFragment;
    private ProgressDialog mProgressDialog;
    private EditText customFirst;
    private EditText customSecond;
    private ListView listView;
    private PopupWindow popupWindow;
    private RecordTimeAdapter adapter;
    private DefineKeyBoardView mDefineKeyBoardView;
    private Animation mEnterAnimation;
    private Animation mExitAnimation;
    private Map<String, TextView> mUnChange, mChange;
    private Map<Integer, String> mChangeText = new HashMap<>();
    private EditText height;
    private EditText weight;
    private EditText morning_pressure_first;
    private EditText morning_pressure_second;
    private EditText morning_pressure_third;
    private EditText morning_pressure_fourth;
    private EditText afternoon_pressure_first;
    private EditText afternoon_pressure_second;
    private EditText afternoon_pressure_third;
    private EditText afternoon_pressure_fourch;
    private EditText inside;
    private EditText outside;
    private EditText bowels;
    private EditText urine;
    private EditText custom_first_count;
    private EditText blood_oxygen_saturation;
    private EditText blood_sugar;
    private EditText custom_second_count;
    private Button mSave;
    private ImageView ivdrugAllergy;
    private EditText drugAllergy;
    private boolean tagBowels = true;
    private boolean tagUrine =true;
    //大便失禁，小便失禁，人工肛门，移植日
    private Switch fecaIncontinence, urinaryIncontinence,artificialAnal,whetherTransplant;
    private String mPatID;
    //身高,体重
    private String mHeight,mWeight;
    //上午血压01,上午血压02,下午血压01,下午血压02
    private String mAMBloodpressureOne,mAMBloodpressureTwo,mPMBloodpressureOne,mPMBloodpressureTwo;
    //入量,出量,大便,尿量,药物过敏
    private String mJoinWeight,mOutWeight,mShit,mUrine,mMedicineAllergy;
    //自定义1：名称,数量
    private String mDefineOneName,mDefineOneWeight;
    //自定义2：名称,数量
    private String mDefineTwoName,mDefineTwoWeight;
    //血氧饱和度,血糖
    private String mBloodOxygen,mGlycemia;
    //大便失禁(0:正常 1:失禁),人工肛门(0:正常 1:人工肛门),小便失禁(0:正常 1:小便失禁),是否移植日(0:是 1:否)
    private String mShitSituation,mArtificialAnus,mUrinate,mIsNaturalizationDay;
    //其他信息
    private String sg,tz,xy_1,xy_2,xy_3,xy_4,rl,cl,db_cs,nl,ywgm1,db_sj,db_rggm,nl_sj, yzr,othername1,othervalue1,othername2,othervalue2,xybhd,xt;
    private int mSaveResult; //信息保存结果
    private MyReceiver mMyReceiver;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> arrayList = new ArrayList<>();

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("com.herench.onItemClick".equals(action)){
                mPatID = intent.getStringExtra("PAT_ID");
            }else if("com.herench.change.person.other".equals(action)){
                getBroadcastData(intent);
                setText();
            }
        }
    }


    private Handler handler = new Handler();
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            drugAllergy.setCursorVisible(false);
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgressDialog();
            switch (msg.what){
                case 0:
                    switch (mSaveResult){
                        case 0:
                            Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
                            break;
                        case -1:
                            Toast.makeText(getActivity(),"保存失败",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 1:
                    popupWindow.showAtLocation(getActivity().findViewById(R.id.rl_recore_time), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    break;
                case -1:
                    Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    synchronized public static PhysicalSignInOtherFragment newInstance() {
        if (mPhysicalSignInOtherFragment == null) {
            mPhysicalSignInOtherFragment = new PhysicalSignInOtherFragment();
        }
        return mPhysicalSignInOtherFragment;
    }

    public PhysicalSignInOtherFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyReceiver = new MyReceiver();
        getActivity().registerReceiver(mMyReceiver,new IntentFilter("com.herench.onItemClick"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initAnimation();
        getActivityData();
        View inflate = inflater.inflate(R.layout.fragment_physical_sign_in_other, container, false);
        initView(inflate);
        return inflate;
    }

    private void getBroadcastData(Intent intent){
        mPatID = intent.getStringExtra("PAT_ID");
        sg = intent.getStringExtra("sg");
        tz = intent.getStringExtra("tz");
        xy_1 = intent.getStringExtra("xy_1");
        xy_2 = intent.getStringExtra("xy_2");
        xy_3 = intent.getStringExtra("xy_3");
        xy_4 = intent.getStringExtra("xy_4");
        rl = intent.getStringExtra("rl");
        cl = intent.getStringExtra("cl");
        nl = intent.getStringExtra("nl");
        db_cs = intent.getStringExtra("db_cs");
        ywgm1 = intent.getStringExtra("ywgm1");
        db_sj = intent.getStringExtra("db_sj");
        db_rggm = intent.getStringExtra("db_rggm");
        nl_sj = intent.getStringExtra("nl_sj");
        yzr = intent.getStringExtra("yzr");

        othername1 = intent.getStringExtra("othername1");
        othervalue1 = intent.getStringExtra("othervalue1");
        othername2 = intent.getStringExtra("othername2");
        othervalue2 = intent.getStringExtra("othervalue2");
        xybhd = intent.getStringExtra("xybhd");
        xt = intent.getStringExtra("xt");
    }

    private void getActivityData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPatID = bundle.getString("PAT_ID");
            sg = bundle.getString("sg");
            tz = bundle.getString("tz");
            xy_1 = bundle.getString("xy_1");
            xy_2 = bundle.getString("xy_2");
            xy_3 = bundle.getString("xy_3");
            xy_4 = bundle.getString("xy_4");
            rl = bundle.getString("rl");
            cl = bundle.getString("cl");
            db_cs = bundle.getString("db_cs");
            nl = bundle.getString("nl");
            ywgm1 = bundle.getString("ywgm1");
            db_sj = bundle.getString("db_sj");
            db_rggm = bundle.getString("db_rggm");
            nl_sj = bundle.getString("nl_sj");
            yzr = bundle.getString("yzr");
            othername1 = bundle.getString("othername1");
            othervalue1 = bundle.getString("othervalue1");
            othername2 = bundle.getString("othername2");
            othervalue2 = bundle.getString("othervalue2");
            xybhd = bundle.getString("xybhd");
            xt = bundle.getString("xt");
        }
    }

    //给UI设置数据
    private void setText() {
        if("null".equals(sg)){
            sg = "";
        }
        if("null".equals(tz)){
            tz = "";
        }
        if("null".equals(xy_1)){
            xy_1 = "";
        }
        if("null".equals(xy_2)){
            xy_2 = "";
        }
        if("null".equals(xy_3)){
            xy_3 = "";
        }
        if("null".equals(xy_4)){
            xy_4 = "";
        }
        if("null".equals(rl)){
            rl = "";
        }
        if("null".equals(cl)){
            cl = "";
        }
        if("null".equals(db_cs)){
            db_cs = "";
        }
        if("null".equals(nl)){
            nl = "";
        }
        if("null".equals(ywgm1)){
            ywgm1 = "";
        }
        if("null".equals(db_sj)){
            db_sj = "";
        }
        if("null".equals(db_rggm)){
            db_rggm = "";
        }
        if("null".equals(nl_sj)){
            nl_sj = "";
        }
        if("null".equals(yzr)){
            yzr = "";
        }
        if("null".equals(othername1)){
            othername1 = "";
        }
        if("null".equals(othervalue1)){
            othervalue1 = "";
        }
        if("null".equals(othername2)){
            othername2 = "";
        }
        if("null".equals(othervalue2)){
            othervalue2 = "";
        }
        if("null".equals(xybhd)){
            xybhd = "";
        }
        if("null".equals(xt)){
            xt = "";
        }

        height.setText(sg);
        weight.setText(tz);
        String[] split = xy_1.split("/");
        if(split.length == 2){
            morning_pressure_first.setText(split[0]);
            morning_pressure_second.setText(split[1]);
        }else{
            morning_pressure_first.setText("");
            morning_pressure_second.setText("");
        }

        String[] split1 = xy_2.split("/");
        if(split1.length == 2){
            morning_pressure_third.setText(split1[0]);
            morning_pressure_fourth.setText(split1[1]);
        }else{
            morning_pressure_third.setText("");
            morning_pressure_fourth.setText("");
        }

        String[] split2 = xy_3.split("/");
        if(split2.length == 2){
            afternoon_pressure_first.setText(split2[0]);
            afternoon_pressure_second.setText(split2[1]);
        }else{
            afternoon_pressure_first.setText("");
            afternoon_pressure_second.setText("");
        }

        String[] split3 = xy_4.split("/");
        if(split3.length == 2){
            afternoon_pressure_third.setText(split3[0]);
            afternoon_pressure_fourch.setText(split3[1]);
        }else{
            afternoon_pressure_third.setText("");
            afternoon_pressure_fourch.setText("");
        }

        inside.setText(rl);
        outside.setText(cl);
        bowels.setText(db_cs);
        urine.setText(nl);
        drugAllergy.setText(ywgm1);
        //TODO:开关设置
        if("0".equals(db_cs)){

        }else if("1".equals(db_cs)){

        }
        if("0".equals(db_rggm)){

        }else if("1".equals(db_rggm)){

        }
        if("0".equals(nl_sj)){

        }else if("1".equals(nl_sj)){

        }
        if("0".equals(yzr)){

        }else if("1".equals(yzr)){

        }
        customFirst.setText(othername1);
        custom_first_count.setText(othervalue1);
        customSecond.setText(othername2);
        custom_second_count.setText(othervalue2);
        blood_oxygen_saturation.setText(xybhd);
        blood_sugar.setText(xt);
    }

    private void initView(View inflate) {
        customFirst = (EditText) inflate.findViewById(R.id.tv_custom_first);
        customSecond = (EditText) inflate.findViewById(R.id.tv_custom_second);
        height = (EditText) inflate.findViewById(R.id.tv_height);
        weight = (EditText) inflate.findViewById(R.id.tv_weight);
        morning_pressure_first = (EditText) inflate.findViewById(R.id.tv_morning_pressure_first);
        morning_pressure_second = (EditText) inflate.findViewById(R.id.tv_morning_pressure_second);
        morning_pressure_third = (EditText) inflate.findViewById(R.id.tv_morning_pressure_third);
        morning_pressure_fourth = (EditText) inflate.findViewById(R.id.tv_morning_pressure_fourth);
        afternoon_pressure_first = (EditText) inflate.findViewById(R.id.tv_afternoon_pressure_first);
        afternoon_pressure_second = (EditText) inflate.findViewById(R.id.tv_afternoon_pressure_second);
        afternoon_pressure_third = (EditText) inflate.findViewById(R.id.tv_afternoon_pressure_third);
        afternoon_pressure_fourch = (EditText) inflate.findViewById(R.id.tv_afternoon_pressure_fourch);
        inside = (EditText) inflate.findViewById(R.id.tv_inside);
        outside = (EditText) inflate.findViewById(R.id.tv_outside);
        bowels = (EditText) inflate.findViewById(R.id.tv_bowels);
        urine = (EditText) inflate.findViewById(R.id.tv_urine);
        custom_first_count = (EditText) inflate.findViewById(R.id.tv_custom_first_count);
        custom_second_count = (EditText) inflate.findViewById(R.id.tv_custom_second_count);
        blood_oxygen_saturation = (EditText) inflate.findViewById(R.id.tv_blood_oxygen_saturation);
        blood_sugar = (EditText) inflate.findViewById(R.id.tv_blood_sugar);
        mDefineKeyBoardView = (DefineKeyBoardView) inflate.findViewById(R.id.layout_define_keyboard_define_key_board_view);
        mSave = (Button) inflate.findViewById(R.id.btn_fragment_physical_sign_in_other_save);
        ivdrugAllergy = (ImageView) inflate.findViewById(iv_drug_allergy);
        drugAllergy = (EditText) inflate.findViewById(R.id.et_drug_allergy);
        fecaIncontinence = (Switch) inflate.findViewById(R.id.sw_fecal_incontinence);
        urinaryIncontinence = (Switch) inflate.findViewById(R.id.sw_urinary_incontinence);
        artificialAnal = (Switch) inflate.findViewById(R.id.sw_artificial_anal);
        whetherTransplant = (Switch) inflate.findViewById(R.id.sw_whether_transplant);
        setText();

        hidekeybord(customFirst);
        hidekeybord(customSecond);
        hidekeybord(height);
        hidekeybord(weight);
        hidekeybord(morning_pressure_first);
        hidekeybord(morning_pressure_second);
        hidekeybord(morning_pressure_third);
        hidekeybord(morning_pressure_fourth);
        hidekeybord(afternoon_pressure_first);
        hidekeybord(afternoon_pressure_second);
        hidekeybord(afternoon_pressure_third);
        hidekeybord(afternoon_pressure_fourch);
        hidekeybord(inside);
        hidekeybord(outside);
        hidekeybord(bowels);
        hidekeybord(urine);
        hidekeybord(custom_first_count);
        hidekeybord(custom_second_count);
        hidekeybord(blood_oxygen_saturation);
        hidekeybord(blood_sugar);

        customFirst.setOnClickListener(this);
        customSecond.setOnClickListener(this);
        height.setOnClickListener(this);
        weight.setOnClickListener(this);
        morning_pressure_first.setOnClickListener(this);
        morning_pressure_second.setOnClickListener(this);
        morning_pressure_third.setOnClickListener(this);
        morning_pressure_fourth.setOnClickListener(this);
        afternoon_pressure_first.setOnClickListener(this);
        afternoon_pressure_second.setOnClickListener(this);
        afternoon_pressure_third.setOnClickListener(this);
        afternoon_pressure_fourch.setOnClickListener(this);
        ivdrugAllergy.setOnClickListener(this);
        inside.setOnClickListener(this);
        outside.setOnClickListener(this);
        bowels.setOnClickListener(this);
        urine.setOnClickListener(this);
        custom_first_count.setOnClickListener(this);
        custom_second_count.setOnClickListener(this);
        blood_oxygen_saturation.setOnClickListener(this);
        blood_sugar.setOnClickListener(this);
        mSave.setOnClickListener(this);
        artificialAnal.setOnClickListener(this);
        fecaIncontinence.setOnClickListener(this);
        urinaryIncontinence.setOnClickListener(this);
        fecaIncontinence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //是否大便失禁
                if(isChecked){
                    mShitSituation = "1";
                }else{
                    mShitSituation = "0";
                }
            }
        });
        artificialAnal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //是否人工肛门
                if(isChecked){
                    mArtificialAnus = "1";
                }else{
                    mArtificialAnus = "0";
                }
            }
        });
        urinaryIncontinence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //是否小便失禁
                if (isChecked) {
                    mUrinate = "1";
                }else{
                    mUrinate = "0";
                }
            }
        });
        whetherTransplant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //是否是移植日
                if(isChecked){
                    mIsNaturalizationDay = "1";
                }else{
                    mIsNaturalizationDay = "0";
                }
            }
        });

//        customFirst.setOnTouchListener(new View.OnTouchListener() {
//            //按住和松开的标识
//            int touch_flag = 0;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                touch_flag++;
//                if (touch_flag == 2) {
//                    touch_flag = 0;
//                    initDate("custom_first");
//                    initPop("custom_first");
//                    popupWindow.showAtLocation(getActivity().findViewById(R.id.rl_recore_time), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
//                }
//                return false;
//            }
//        });
//        customSecond.setOnTouchListener(new View.OnTouchListener() {
//            //按住和松开的标识
//            int touch_flag = 0;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                touch_flag++;
//                if (touch_flag == 2) {
//                    touch_flag = 0;
//                    initDate("custom_second");
//                    initPop("custom_second");
//                    popupWindow.showAtLocation(getActivity().findViewById(R.id.rl_recore_time), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
//                }
//                return false;
//            }
//        });

        height.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initHeightData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(height);
                }
                return false;
            }
        });
        weight.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initWeightData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(weight);
                }
                return false;
            }
        });
        morning_pressure_first.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initSystolicPressureData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(morning_pressure_first);
                }
                return false;
            }
        });
        morning_pressure_second.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initDiastolicPressureData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(morning_pressure_second);
                }
                return false;
            }
        });
        morning_pressure_third.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initSystolicPressureData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(morning_pressure_third);
                }
                return false;
            }
        });
        morning_pressure_fourth.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initDiastolicPressureData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(morning_pressure_fourth);
                }
                return false;
            }
        });
        afternoon_pressure_first.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initSystolicPressureData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(afternoon_pressure_first);
                }
                return false;
            }
        });
        afternoon_pressure_second.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initDiastolicPressureData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(afternoon_pressure_second);
                }
                return false;
            }
        });
        afternoon_pressure_third.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initSystolicPressureData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(afternoon_pressure_third);
                }
                return false;
            }
        });
        afternoon_pressure_fourch.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initDiastolicPressureData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(afternoon_pressure_fourch);
                }
                return false;
            }
        });
        inside.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(inside);
                }
                return false;
            }
        });
        outside.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(outside);
                }
                return false;
            }
        });

        bowels.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    if (tagBowels) {
                        initShitCountData();
                        mDefineKeyBoardView.setVisibility(View.VISIBLE);
                        keyboard(bowels);
                    }
                }
                return false;
            }
        });
        urine.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    if (tagUrine) {
                        touch_flag = 0;
                        initData();
                        mDefineKeyBoardView.setVisibility(View.VISIBLE);
                        keyboard(urine);
                    }
                }
                return false;
            }
        });
        custom_first_count.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(custom_first_count);
                }
                return false;
            }
        });
        custom_second_count.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(custom_second_count);
                }
                return false;
            }
        });
        blood_oxygen_saturation.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(blood_oxygen_saturation);
                }
                return false;
            }
        });
        blood_sugar.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    initData();
                    mDefineKeyBoardView.setVisibility(View.VISIBLE);
                    keyboard(blood_sugar);
                }
                return false;
            }
        });
        drugAllergy.addTextChangedListener(new TextWatcher() {
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
                handler.postDelayed(delayRun, 2000);
            }
        });
    }

    //初始化大便次数键盘数据
    private void initShitCountData() {
        mChangeText.clear();
        for (int i = 0; i < 8; i++) {
            switch (i){
                case 0:
                    mChangeText.put(i, String.valueOf(i));
                    break;
                case 1:
                    mChangeText.put(i, "1/E");
                    break;
                case 2:
                    mChangeText.put(i, "0/E");
                    break;
                case 3:
                    mChangeText.put(i, "1/E");
                    break;
            }
        }
    }

    //初始化舒张压键盘数据
    private void initDiastolicPressureData() {
        mChangeText.clear();
        int number = 60;
        for (int i = 0; i < 8; i++) {
            mChangeText.put(i, String.valueOf(number+=10));
        }
    }

    //初始化收缩压键盘数据
    private void initSystolicPressureData() {
        mChangeText.clear();
        int number = 90;
        for (int i = 0; i < 8; i++) {
            mChangeText.put(i, String.valueOf(number+=10));
        }
    }

    //初始化体重键盘数据
    private void initWeightData() {
        mChangeText.clear();
        for (int i = 0; i < 8; i++) {
            mChangeText.put(i, 50 + i + "");
        }
    }

    //初始化身高键盘数据
    private void initHeightData() {
        mChangeText.clear();
        for (int i = 0; i < 8; i++) {
            mChangeText.put(i, 11 + i + "");
        }
    }


    private void initAnimation() {
        mEnterAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_bottom_in);
        mExitAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_bottom_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case iv_drug_allergy:
                mDefineKeyBoardView.setVisibility(View.GONE);
                initPop("drug_allergy");
                initDate("drug_allergy");
                break;
            case R.id.tv_custom_first:
                mDefineKeyBoardView.setVisibility(View.GONE);
                initPop("custom_first");
                initDate("custom_first");
                break;
            case R.id.tv_custom_second:
                mDefineKeyBoardView.setVisibility(View.GONE);
                initPop("custom_second");
                initDate("custom_second");
                break;
            case R.id.sw_fecal_incontinence:
                if (fecaIncontinence.isChecked()) {
                    bowels.setText("");
                    bowels.setBackground(getActivity().getResources().getDrawable(R.drawable.rectangular_border_gray_bg));
                    bowels.setClickable(false);
                    bowels.setCursorVisible(false);
                    tagBowels = false;
                } else {
                    bowels.setText("");
                    bowels.setBackground(getActivity().getResources().getDrawable(R.drawable.rectangular_border_bg));
                    bowels.setClickable(true);
                    bowels.setCursorVisible(true);
                    tagBowels = true;
                }
                break;
            case R.id.sw_urinary_incontinence:
                if (urinaryIncontinence.isChecked()) {
                    urine.setText("");
                    urine.setBackground(getActivity().getResources().getDrawable(R.drawable.rectangular_border_gray_bg));
                    urine.setClickable(false);
                    urine.setFocusable(false);
                    urine.setCursorVisible(false);
                    tagUrine = false;
                } else {
                    urine.setText("");
                    urine.setBackground(getActivity().getResources().getDrawable(R.drawable.rectangular_border_bg));
                    urine.setClickable(true);
                    urine.setFocusable(true);
                    urine.setCursorVisible(true);
                    tagUrine = true;
                }
                break;
            case R.id.btn_fragment_physical_sign_in_other_save:
                final HashMap<String, String> map = new HashMap<>();
                map.put("action", "V0012");
                mHeight = height.getText().toString().trim();
//                if(TextUtils.isEmpty(mHeight) || "".equals(mHeight)){
//                    Toast.makeText(getActivity(),"请输入身高",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mWeight = weight.getText().toString().trim();
//                if(TextUtils.isEmpty(mWeight) || "".equals(mWeight)){
//                    Toast.makeText(getActivity(),"请输入体重",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                String amMorning01 = morning_pressure_first.getText().toString().trim();
                String amMorning02 = morning_pressure_second.getText().toString().trim();
//                if(TextUtils.isEmpty(amMorning01) || "".equals(amMorning01) || TextUtils.isEmpty(amMorning02) ||"".equals(amMorning02)){
//                    Toast.makeText(getActivity(),"请将上午血压填写完整",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mAMBloodpressureOne = amMorning01+"/"+amMorning02;
                String amMorning03 =  morning_pressure_third.getText().toString().trim();
                String amMorning04 =  morning_pressure_fourth.getText().toString().trim();
//                if(TextUtils.isEmpty(amMorning03) || "".equals(amMorning03) || TextUtils.isEmpty(amMorning04) ||"".equals(amMorning04)){
//                    Toast.makeText(getActivity(),"请将上午血压填写完整",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mAMBloodpressureTwo = amMorning03+"/"+amMorning04;
                String afternoon01 = afternoon_pressure_first.getText().toString().trim();
                String afternoon02 = afternoon_pressure_second.getText().toString().trim();
//                if(TextUtils.isEmpty(afternoon01) || "".equals(afternoon01) || TextUtils.isEmpty(afternoon02) ||"".equals(afternoon02)){
//                    Toast.makeText(getActivity(),"请将下午血压填写完整",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mPMBloodpressureOne = afternoon01+"/"+afternoon02;
                String afternoon03 = afternoon_pressure_third.getText().toString().trim();
                String afternoon04 = afternoon_pressure_fourch.getText().toString().trim();
//                if(TextUtils.isEmpty(afternoon03) || "".equals(afternoon03) || TextUtils.isEmpty(afternoon04) ||"".equals(afternoon04)){
//                    Toast.makeText(getActivity(),"请将下午血压填写完整",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mPMBloodpressureTwo = afternoon03+"/"+afternoon04;
                mJoinWeight = inside.getText().toString().trim();
//                if(TextUtils.isEmpty(mJoinWeight) || "".equals(mJoinWeight)){
//                    Toast.makeText(getActivity(),"请填写入量",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mOutWeight = outside.getText().toString().trim();
//                if(TextUtils.isEmpty(mOutWeight) || "".equals(mOutWeight)){
//                    Toast.makeText(getActivity(),"请填写出量",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mShit = bowels.getText().toString().trim();
//                if(TextUtils.isEmpty(mShit) || "".equals(mShit)){
//                    Toast.makeText(getActivity(),"请填写大便次数",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mUrine = urine.getText().toString().trim();
//                if(TextUtils.isEmpty(mUrine) || "".equals(mUrine)){
//                    Toast.makeText(getActivity(),"请填写尿量",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mMedicineAllergy = drugAllergy.getText().toString().trim();
//                if(TextUtils.isEmpty(mMedicineAllergy) || "".equals(mMedicineAllergy)){
//                    Toast.makeText(getActivity(),"请填写药物过敏",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mDefineOneName = customFirst.getText().toString().trim();
//                if(TextUtils.isEmpty(mDefineOneName) || "".equals(mDefineOneName)){
//                    Toast.makeText(getActivity(),"请填写自定义1项目",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mDefineOneWeight = custom_first_count.getText().toString().trim();
//                if(TextUtils.isEmpty(mDefineOneWeight) || "".equals(mDefineOneWeight)){
//                    Toast.makeText(getActivity(),"请填写自定义1数量",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mDefineTwoName = customSecond.getText().toString().trim();
//                if(TextUtils.isEmpty(mDefineTwoName) || "".equals(mDefineTwoName)){
//                    Toast.makeText(getActivity(),"请填写自定义2项目",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mDefineTwoWeight = custom_second_count.getText().toString().trim();
//                if(TextUtils.isEmpty(mDefineTwoWeight) || "".equals(mDefineTwoWeight)){
//                    Toast.makeText(getActivity(),"请填写自定义2数量",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mBloodOxygen = blood_oxygen_saturation.getText().toString().trim();
//                if(TextUtils.isEmpty(mBloodOxygen) || "".equals(mBloodOxygen)){
//                    Toast.makeText(getActivity(),"请填写血氧饱和度",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mGlycemia = blood_sugar.getText().toString().trim();
//                if(TextUtils.isEmpty(mGlycemia) || "".equals(mGlycemia)){
//                    Toast.makeText(getActivity(),"请填写血糖",Toast.LENGTH_SHORT).show();
//                    return;
//                }

                mProgressDialog = DialogUtils.createSimpleProgressDialog(getActivity(), "正在保存");
                JSONObject message = new JSONObject();
                try {
                    message.put("SourceSystem","移动护理");
                    message.put("SourceID",MacAddressUtils.getMacAddress(getActivity()));
                    message.put("MessageID","");
                    message.put("UserCode",UserInfo.getUserName());
                    message.put("UserName",UserInfo.getName());
                    message.put("PAT_ID",mPatID);
                    message.put("RECORDING_TIME", TimeUtils.getCurrentTime());
                    message.put("LOG_BY",UserInfo.getUserName());
                    message.put("RL",mJoinWeight);  //入量
                    message.put("CL",mOutWeight);  //出量
                    message.put("DB_CS",mShit);  //大便次数
//                  message.put("DB_ZL","");  //大便重量
                    message.put("DB_SJ",mShitSituation);  //大便失禁
                    message.put("DB_RGGM",mArtificialAnus);  //人工肛门
                    message.put("NL",mUrine);  //尿量
                    message.put("NL_SJ",mUrinate);  //小便失禁
                    message.put("TZ",mWeight);  //体重
                    message.put("SG",mHeight);  //身高
                    message.put("XY_1",mAMBloodpressureOne);  //上午血压1
                    message.put("XY_2",mAMBloodpressureTwo);  //上午血压2
                    message.put("XY_3",mPMBloodpressureOne);  //下午血压1
                    message.put("XY_4",mPMBloodpressureTwo);  //下午血压2
//                  message.put("SHTS","");  //术后天数
                    message.put("YWGM1",mMedicineAllergy);  //药物过敏1
                    message.put("OTHERNAME1",mDefineOneName);  //自定义项目1
                    message.put("OTHERVALUE1",mDefineOneWeight);  //自定义对应值1
                    message.put("OTHERNAME2",mDefineTwoName);  //自定义项目2
                    message.put("OTHERVALUE2",mDefineTwoWeight);  //自定义对应值2
                    message.put("XYBHD",mBloodOxygen);  //血氧饱和度
                    message.put("YZR",mIsNaturalizationDay);  //移植日
                    message.put("XT",mGlycemia);  //血糖
                    message.put("AccessToken","");

                    map.put("message",message.toString());
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
                        }else{
                            mHandler.sendEmptyMessage(-1);
                        }
                    }
                }).start();
                break;
        }
    }

    private void initDate(String tag) {
        list.clear();
        arrayList.clear();
        //自定义项目1
        if (tag.equals("custom_first")) {
            mProgressDialog = DialogUtils.createSimpleProgressDialog(getActivity(), "正在获取自定义项目1...");
            final HashMap<String, String> map = new HashMap<>();
            map.put("action", "C0010");
            JSONObject message = new JSONObject();
            try {
                message.put("SourceSystem", "移动护理");
                message.put("SourceID", MacAddressUtils.getMacAddress(getActivity()));
                message.put("MessageID", "");
                message.put("UserCode", UserInfo.getUserName());
                message.put("UserName", UserInfo.getName());
                message.put("CLASS_CODE", "OTHERNAME1");
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
        //自定义项目2
        if (tag.equals("custom_second")) {
            mProgressDialog = DialogUtils.createSimpleProgressDialog(getActivity(), "正在获取自定义项目2...");
            final HashMap<String, String> map = new HashMap<>();
            map.put("action", "C0010");
            JSONObject message = new JSONObject();
            try {
                message.put("SourceSystem", "移动护理");
                message.put("SourceID", MacAddressUtils.getMacAddress(getActivity()));
                message.put("MessageID", "");
                message.put("UserCode", UserInfo.getUserName());
                message.put("UserName", UserInfo.getName());
                message.put("CLASS_CODE", "OTHERNAME2");
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
        //药物过敏
        if (tag.equals("drug_allergy")) {
            mProgressDialog = DialogUtils.createSimpleProgressDialog(getActivity(), "正在获取药物过敏...");
            final HashMap<String, String> map = new HashMap<>();
            map.put("action", "C0010");
            JSONObject message = new JSONObject();
            try {
                message.put("SourceSystem", "移动护理");
                message.put("SourceID", MacAddressUtils.getMacAddress(getActivity()));
                message.put("MessageID", "");
                message.put("UserCode", UserInfo.getUserName());
                message.put("UserName", UserInfo.getName());
                message.put("CLASS_CODE", "YWGM1");
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

    private void initData() {
        mChangeText.clear();
        for (int i = 0; i < 8; i++) {
            mChangeText.put(i, 10 + i + "");
        }
    }

    private void initPop(final String tag) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_record_time, null);
        adapter = new RecordTimeAdapter(getActivity(), list, tag);
        listView = (ListView) view.findViewById(R.id.lv_record_time);
//      setListViewHeightBasedOnChildren(listView);
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
        if (i> herig / 3) {
            LayoutParams.height = herig / 3;
        }
        else {
            LayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        listView.setLayoutParams(LayoutParams);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                if("custom_first".equals(tag)){
                    customFirst.setText(list.get(arg2));
                    customFirst.setSelection(customFirst.getText().toString().length());
                    popupWindow.dismiss();
                }

                if("custom_second".equals(tag)){
                    customSecond.setText(list.get(arg2));
                    customSecond.setSelection(customSecond.getText().toString().length());
                    popupWindow.dismiss();
                }

                if("drug_allergy".equals(tag)){
                    if (drugAllergy.getText().toString().isEmpty()) {
                        drugAllergy.setText(list.get(arg2));
                        drugAllergy.setSelection(drugAllergy.getText().toString().length());
                        popupWindow.dismiss();
                    } else {
                        drugAllergy.setText(drugAllergy.getText().toString() + "、" + list.get(arg2));
                        drugAllergy.setSelection(drugAllergy.getText().toString().length());
                        popupWindow.dismiss();
                    }
                }
            }
        });
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

    private void hidekeybord(EditText textAmount) {
        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            textAmount.setInputType(InputType.TYPE_NULL);
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(textAmount, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
