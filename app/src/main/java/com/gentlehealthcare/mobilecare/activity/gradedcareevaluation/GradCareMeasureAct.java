package com.gentlehealthcare.mobilecare.activity.gradedcareevaluation;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.adapter.Pop_Grad_Care_MeasureAdapter;
import com.gentlehealthcare.mobilecare.bean.PopGradedCareMeasureBean;
import com.gentlehealthcare.mobilecare.bean.sys.BarcodeDict;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.presenter.IntravenousPresenter;
import com.gentlehealthcare.mobilecare.presenter.TemperaturePresenter;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimeListener;
import com.gentlehealthcare.mobilecare.slidedatetimepicker.SlideDateTimePicker;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.gentlehealthcare.mobilecare.view.IIntravenousExecuteView;
import com.gentlehealthcare.mobilecare.view.ITemperatureView;
import com.gentlehealthcare.mobilecare.view.MyPatientDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* 分级护理评估评估列表
* */
public class GradCareMeasureAct extends BaseActivity implements ITemperatureView, IIntravenousExecuteView, View.OnClickListener {
    private String dateString;
    private IntravenousPresenter intravenousPresenter;
    private PopupWindow mPopupWindow;
    private View contentView;
    private MyPatientDialog dialog;
    private boolean isRefreshPatient;
    //时间选择器完成后显示选择后的时间
    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            dateString = DateTool.formatDate(GlobalConstant.DATE_TIME_SIMPLE, date);
            date_checked.setText(dateString);
        }
    };
    private TextView date_checked;
    private TextView tv_pop_eating;
    private ListView lv_grad_care_measure;
    private TextView tv_score_eating;
    private TextView tv_pop_wash;
    private TextView tv_score_wash;
    private TextView tv_pop_modified;
    private TextView tv_score_modified;
    private TextView tv_pop_dress;
    private TextView tv_score_dress;
    private TextView tv_pop_bowels;
    private TextView tv_score_bowels;
    private TextView tv_pop_urinate;
    private TextView tv_score_urinate;
    private TextView tv_pop_toilet;
    private TextView tv_score_toilet;
    private TextView tv_pop_bed;
    private TextView tv_score_bed;
    private TextView tv_pop_walk;
    private TextView tv_score_walk;
    private TextView tv_pop_stairs;
    private TextView tv_score_stairs;
    private TextView tv_score;
    private LinearLayout rl_top;
    private TemperaturePresenter TemperaturePresenter;
    private int whichPatients = 0;
    private TextView patName;
    private TextView patSex;
    private TextView patBed;
    private TextView patCode;
    private String patId;
    private SyncPatientBean currentPatient;
    private ArrayList<PopGradedCareMeasureBean> list;
    private Button btn_save_assessment;
    private Button btn_back;
    List<SyncPatientBean> deptPatient;
    //时间选择器监听
    @Override
    public void showSlideDateTimerPicker() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                // .setMinDate(0)
                // .setMaxDate(maxDate)
                .setIs24HourTime(true)
                .setTheme(SlideDateTimePicker.HOLO_LIGHT)
                .setIndicatorColor(Color.parseColor("#2ba3d5"))
                .build()
                .show();
    }

    @Override
    protected void resetLayout() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grad_care_measure);
        list = new ArrayList();
        rl_top = (LinearLayout) findViewById(R.id.rl_top);
        patName = (TextView) findViewById(R.id.tv_pat_name);
        patSex = (TextView) findViewById(R.id.tv_pat_sex);
        patBed = (TextView) findViewById(R.id.tv_pat_bed);
        patCode = (TextView) findViewById(R.id.tv_pat_code);

        //时间选择器
        date_checked = (TextView) findViewById(R.id.tv_date_checked);

        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_score.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv_score.setTextColor(getResources().getColor(R.color.red));

        tv_pop_eating = (TextView) findViewById(R.id.tv_pop_eating);
        tv_score_eating = (TextView) findViewById(R.id.tv_score_eating);

        tv_pop_wash = (TextView) findViewById(R.id.tv_pop_wash);
        tv_score_wash = (TextView) findViewById(R.id.tv_score_wash);

        tv_pop_modified = (TextView) findViewById(R.id.tv_pop_modified);
        tv_score_modified = (TextView) findViewById(R.id.tv_score_modified);

        tv_pop_dress = (TextView) findViewById(R.id.tv_pop_dress);
        tv_score_dress = (TextView) findViewById(R.id.tv_score_dress);

        tv_pop_bowels = (TextView) findViewById(R.id.tv_pop_bowels);
        tv_score_bowels = (TextView) findViewById(R.id.tv_score_bowels);

        tv_pop_urinate = (TextView) findViewById(R.id.tv_pop_urinate);
        tv_score_urinate = (TextView) findViewById(R.id.tv_score_urinate);

        tv_pop_toilet = (TextView) findViewById(R.id.tv_pop_toilet);
        tv_score_toilet = (TextView) findViewById(R.id.tv_score_toilet);

        tv_pop_bed = (TextView) findViewById(R.id.tv_pop_bed);
        tv_score_bed = (TextView) findViewById(R.id.tv_score_bed);

        tv_pop_walk = (TextView) findViewById(R.id.tv_pop_walk);
        tv_score_walk = (TextView) findViewById(R.id.tv_score_walk);

        tv_pop_stairs = (TextView) findViewById(R.id.tv_pop_stairs);
        tv_score_stairs = (TextView) findViewById(R.id.tv_score_stairs);

        btn_save_assessment = (Button) findViewById(R.id.btn_save_assessment);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        date_checked.setOnClickListener(this);
        tv_pop_eating.setOnClickListener(this);
        tv_pop_wash.setOnClickListener(this);
        tv_pop_modified.setOnClickListener(this);
        tv_pop_dress.setOnClickListener(this);
        tv_pop_bowels.setOnClickListener(this);
        tv_pop_urinate.setOnClickListener(this);
        tv_pop_toilet.setOnClickListener(this);
        tv_pop_bed.setOnClickListener(this);
        tv_pop_walk.setOnClickListener(this);
        tv_pop_stairs.setOnClickListener(this);
        rl_top.setOnClickListener(this);
        btn_save_assessment.setOnClickListener(this);
        intravenousPresenter = new IntravenousPresenter(null, GradCareMeasureAct.this, null, null);
        TemperaturePresenter = new TemperaturePresenter(this);
        TemperaturePresenter.initialSrc();
        TemperaturePresenter.getPatients(this, whichPatients);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_top:
                TemperaturePresenter.showPatients();
                break;
            case R.id.tv_date_checked:
                intravenousPresenter.showSlideDateTimerPicker();
                break;
            //进食
            case R.id.tv_pop_eating:
                list.clear();
                list.add(new PopGradedCareMeasureBean("依赖别人"));
                list.add(new PopGradedCareMeasureBean("需要部分帮助(如切割食物，搅拌食物)"));
                list.add(new PopGradedCareMeasureBean("能使用任何需要的装置，在适当的时间内独立进食"));
                showPopupWindow(tv_pop_eating, tv_score_eating);
                break;
            //洗澡
            case R.id.tv_pop_wash:
                list.clear();
                list.add(new PopGradedCareMeasureBean("依赖"));
                list.add(new PopGradedCareMeasureBean("自理"));
                showPopupWindow(tv_pop_wash, tv_score_wash);
                break;
            //装饰
            case R.id.tv_pop_modified:
                list.clear();
                list.add(new PopGradedCareMeasureBean("需要帮助"));
                list.add(new PopGradedCareMeasureBean("独立洗脸、梳头、刷牙、剃须"));
                showPopupWindow(tv_pop_modified, tv_score_modified);
                break;
            //穿衣
            case R.id.tv_pop_dress:
                list.clear();
                list.add(new PopGradedCareMeasureBean("依赖"));
                list.add(new PopGradedCareMeasureBean("需要帮助，但在适当的时间内至少完成一半的工作"));
                list.add(new PopGradedCareMeasureBean("能使用任何需要的装置，自理（系、开纽扣，关开拉锁和穿脱支具）"));
                showPopupWindow(tv_pop_dress, tv_score_dress);
                break;
            //控制大便
            case R.id.tv_pop_bowels:
                list.clear();
                list.add(new PopGradedCareMeasureBean("失禁"));
                list.add(new PopGradedCareMeasureBean("偶尔失禁或需要器具帮助"));
                list.add(new PopGradedCareMeasureBean("能控制；如果需要，能使用灌肠剂或栓剂"));
                showPopupWindow(tv_pop_bowels, tv_score_bowels);
                break;
            //控制小便
            case R.id.tv_pop_urinate:
                list.clear();
                list.add(new PopGradedCareMeasureBean("失禁"));
                list.add(new PopGradedCareMeasureBean("偶尔失禁或需要器具帮助"));
                list.add(new PopGradedCareMeasureBean("能控制；如果需要，能使用集尿器"));
                showPopupWindow(tv_pop_urinate, tv_score_urinate);
                break;
            //如厕
            case R.id.tv_pop_toilet:
                list.clear();
                list.add(new PopGradedCareMeasureBean("依赖别人"));
                list.add(new PopGradedCareMeasureBean("需要部分帮助，在穿脱衣裤或使用卫生纸时需要帮助"));
                list.add(new PopGradedCareMeasureBean("独立使用厕所或便盆，穿脱衣裤，冲洗或清洗便盆"));
                showPopupWindow(tv_pop_toilet, tv_score_toilet);
                break;
            //床椅转移
            case R.id.tv_pop_bed:
                list.clear();
                list.add(new PopGradedCareMeasureBean("完全依赖别人，不能坐"));
                list.add(new PopGradedCareMeasureBean("能坐，但需要大量帮助（2人）才能转移"));
                list.add(new PopGradedCareMeasureBean("需要少量帮助（1人）或指导"));
                list.add(new PopGradedCareMeasureBean("独立从床到轮椅，再从轮椅到床，包括从床上坐起、刹住轮椅、抬起"));
                showPopupWindow(tv_pop_bed, tv_score_bed);
                break;
            //平地行走
            case R.id.tv_pop_walk:
                list.clear();
                list.add(new PopGradedCareMeasureBean("不能动"));
                list.add(new PopGradedCareMeasureBean("在轮椅上独立行走，能行走45米"));
                list.add(new PopGradedCareMeasureBean("需要1人帮助行走（体力或语言指导)45米"));
                list.add(new PopGradedCareMeasureBean("能在水平路面上行走45米，可以使用辅助装置，不包括带轮的助行"));
                showPopupWindow(tv_pop_walk, tv_score_walk);
                break;
            //上下楼梯
            case R.id.tv_pop_stairs:
                list.clear();
                list.add(new PopGradedCareMeasureBean("不能"));
                list.add(new PopGradedCareMeasureBean("需要帮助和监督"));
                list.add(new PopGradedCareMeasureBean("独立，可以使用辅助装置"));
                showPopupWindow(tv_pop_stairs, tv_score_stairs);
                break;
            //下一步
            case R.id.btn_save_assessment:
                String[] split = tv_score.getText().toString().split(":");
                Intent Intent = new Intent();
                Intent.setClass(GradCareMeasureAct.this, NursingMeasuresAct.class);
                Intent.putExtra("type", split[0]);
                startActivity(Intent);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void showPopupWindow(final TextView text, final TextView score) {
        contentView = LayoutInflater.from(GradCareMeasureAct.this).inflate(
                R.layout.pop_grad_care_measure, null);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);// 取得焦点

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失

        //设置可以点击
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams a = getWindow().getAttributes();
                a.alpha = 1f;
                getWindow().setAttributes(a);
            }
        });
        //进入退出的动画
        mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        mPopupWindow.showAtLocation(contentView, Gravity.CENTER, 150, 150);
        lv_grad_care_measure = (ListView) contentView.findViewById(R.id.lv_grad_care_measure);

        Pop_Grad_Care_MeasureAdapter adapter = new Pop_Grad_Care_MeasureAdapter(list, GradCareMeasureAct.this);
        lv_grad_care_measure.setAdapter(adapter);
        lv_grad_care_measure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                text.setText(list.get(position).getMeasure().toString());
                if (position == 0) {
                    score.setText(0 + "分");
                } else if (position == 1) {
                    score.setText(5 + "分");
                } else if (position == 2) {
                    score.setText(10 + "分");
                } else if (position == 3) {
                    score.setText(15 + "分");
                }
                mPopupWindow.dismiss();
                int i = split(tv_score_eating) + split(tv_score_wash) + split(tv_score_modified) + split(tv_score_dress)
                        + split(tv_score_bowels) + split(tv_score_urinate) + split(tv_score_toilet) + split(tv_score_bed)
                        + split(tv_score_walk) + split(tv_score_stairs);
                String type = "";
                //判断分数设置护理等级以及颜色
                if (i <= 40) {
                    type = "重度依赖";
                    tv_score.setTextColor(getResources().getColor(R.color.red));
                } else if (i < 60 && i > 40) {
                    type = "中度依赖";
                    tv_score.setTextColor(getResources().getColor(R.color.red));
                } else if (i < 100 && i > 60) {
                    type = "轻度依赖";
                    tv_score.setTextColor(getResources().getColor(R.color.red));
                } else if (i == 100) {
                    type = "无需依赖";
                    tv_score.setTextColor(getResources().getColor(R.color.black));
                }
                tv_score.setText(type + ":" + i + "分");
            }
        });
    }

    private int split(TextView TextView) {
        String[] split = TextView.getText().toString().split("分");
        return Integer.parseInt(split[0]);
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void showProgressDialog(String msg) {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void saveNextPatrolTime() {

    }

    @Override
    public void showToast(String msg) {

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
        patId = patientInfo.getPatId();
        currentPatient = patientInfo;
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

    @Override
    public void showPatients() {
        dialog = new MyPatientDialog(GradCareMeasureAct.this,
                new MyPatientDialog.MySnListener() {

                    @Override
                    public void myOnItemClick(View view, int position, long id) {
                        TemperaturePresenter.setPatientInfo(deptPatient
                                .get(position));
                        whichPatients = position;
                        dialog.dismiss();
                    }

                    @Override
                    public void onRefresh() {
                         isRefreshPatient = true;
                        TemperaturePresenter.getPatients(GradCareMeasureAct.this,
                                1024);// 1000标记用来刷新
                        dialog.dismiss();
                    }
                }, whichPatients, deptPatient);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
    //扫描腕带
    public void DoCameraResult(String result) {
        List<BarcodeDict> barcodeDicts = new ArrayList<BarcodeDict>();
        barcodeDicts = LocalSave.getDataList(getApplicationContext(), GlobalConstant.KEY_BARCODE);
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
                    TemperaturePresenter.setPatientInfo(deptPatient.get(i));
                    String patCodeResult = result;
                    // 扫描腕带后执行的逻辑

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
    public void handException() {

    }

    @Override
    public void backToDoctorOrdersAct() {

    }
}
