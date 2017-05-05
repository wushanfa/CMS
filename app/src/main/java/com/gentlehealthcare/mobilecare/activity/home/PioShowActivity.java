package com.gentlehealthcare.mobilecare.activity.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.net.bean.LoadPioRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.PioItemInfo;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class PioShowActivity extends BaseActivity implements OnTouchListener, OnClickListener {
    private ProgressDialog progressDialog = null;
    private LoadPioRecordBean perPio = null;
    private TextView tvTime, tvProblem, tvCause, tvTarget, tvMeasures,
            tvEstimatetime, tvPerson, tvEffect, tv_patient;
    private SyncPatientBean patient = null;
    @ViewInject(R.id.btn_back)
    private Button btn_back;
    @ViewInject(R.id.tv_head)
    private TextView tv_head;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_label;
    @ViewInject(R.id.tv_parent)
    private TextView tv_parent;

    // 手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 200;

    // 手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 150;

    // 记录手指按下时的横坐标。
    private float xDown;

    // 记录手指移动时的横坐标。
    private float xMove;

    // 用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pio_recordshow);
        ViewUtils.inject(this);
        initView();
        HidnGestWindow(true);

        progressDialog = new ProgressDialog(PioShowActivity.this);
        progressDialog.setMessage("获取当前PIO数据中,请稍后...");

        perPio = (LoadPioRecordBean) getIntent().getSerializableExtra("pio");
        patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");

        LoadPerPio();

        tv_head.setText("护理记录");
        tv_bed_label.setText(patient.getBedLabel() + "床");
        tv_parent.setText(patient.getName());

        ScrollView activityGoBack = (ScrollView) findViewById(R.id.activitygoback);
        activityGoBack.setOnTouchListener(PioShowActivity.this);

        progressDialog.dismiss();
    }

    @Override
    protected void resetLayout() {
        ScrollView root = (ScrollView) findViewById(R.id.activitygoback);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void initView() {
        tvCause = (TextView) findViewById(R.id.tv_cause);
        tvEffect = (TextView) findViewById(R.id.tv_effect);
        tvEstimatetime = (TextView) findViewById(R.id.tv_estimatetime);
        tvTarget = (TextView) findViewById(R.id.tv_target);
        tvMeasures = (TextView) findViewById(R.id.tv_measures);
        tvProblem = (TextView) findViewById(R.id.tv_problem);
        tvPerson = (TextView) findViewById(R.id.tv_person);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tv_patient = (TextView) findViewById(R.id.tv_patient);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    /**
     * 加载当条pio数据
     */
    public void LoadPerPio() {
        progressDialog.show();

        tv_patient.setText("病人：" + patient.getName());
        if (!perPio.getAppraisal().isEmpty()) {
            tvPerson.setVisibility(View.VISIBLE);
            tvEstimatetime.setVisibility(View.VISIBLE);
            tvEffect.setVisibility(View.VISIBLE);
            tvEstimatetime.setText("评价时间:" + perPio.getLogTime2());
            tvEffect.setText("评价内容:" + getItemNameToString(perPio.getAppraisal()));
            tvPerson.setText("评价人:" + perPio.getLogBy2());
        } else {
            tvPerson.setVisibility(View.GONE);
            tvEstimatetime.setVisibility(View.GONE);
            tvEffect.setVisibility(View.GONE);
        }
        tvTime.setText("时间:" + perPio.getLogTim1());
        tvProblem.setText("问题:" + getProblemToString(perPio.getProblemName()));
        String measure = "";
        if (!perPio.getMeasure().isEmpty()) {
            measure = getItemNameToString(perPio.getMeasure());
            tvMeasures.setText("措施:" + measure);
        }
        String target = "";
        if (!perPio.getTarget().isEmpty()) {
            target = getItemNameToString(perPio.getTarget());
            tvTarget.setText("目标:" + target);
        }
        String cause = "";
        if (!perPio.getCauses().isEmpty()) {
            cause = getItemNameToString(perPio.getCauses());
            tvCause.setText("致因:" + cause);
        }

    }

    private String getProblemToString(String info) {
        StringBuffer sb = new StringBuffer();
        if (info == null) {
            sb.append("没有相关数据");
        } else {
            sb.append("(" + 1 + ")");
            sb.append(".");
            sb.append(info);
        }

        return sb.toString();
    }

    private String getItemNameToString(List<PioItemInfo> list) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemName() == null) {
                sb.append("");
            } else {
                sb.append("(" + (i + 1) + ")");
                sb.append(".");
                sb.append(list.get(i).getItemName());
            }
            if (i != list.size() - 1) {
                sb.append("\n");
            }

        }
        return sb.toString();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                int distanceX = (int) (xMove - xDown);
                int xSpeed = getScrollVelocity();
                if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                    finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }
}
