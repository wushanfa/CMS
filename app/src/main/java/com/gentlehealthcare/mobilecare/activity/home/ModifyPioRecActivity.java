package com.gentlehealthcare.mobilecare.activity.home;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.bean.DeleteAndModPioBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadPioRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.PioBean;
import com.gentlehealthcare.mobilecare.net.bean.PioItemInfo;
import com.gentlehealthcare.mobilecare.net.bean.RecordPioBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.adapter.AddPioAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 增加pio记录页面
 *
 * @author zhiwei
 */
public class ModifyPioRecActivity extends BaseActivity implements OnClickListener,
        OnItemClickListener, OnItemLongClickListener {

    private static final String TAG = "ModifyPioRecActivity";
    private ProgressDialog progressDialog = null;
    private List<PioItemInfo> orgProblemList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> problemList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> problemChoosenList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutCausesList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutTargetList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutMeasureList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseCausesList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseMeasureList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseCausesListTemp = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseTargetListTemp = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseMeasureListTemp = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseTargetList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> orgmutChooseCausesList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> orgmutChooseMeasureList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> orgmutChooseCausesListTemp = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> orgmutChooseTargetListTemp = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> orgmutChooseMeasureListTemp = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> orgmutChooseTargetList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> delList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> modifyList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> addCausesList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> addTargetList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> addMeasureList = new ArrayList<PioItemInfo>();
    private String currentCode = "";// 当前的护理问题编号
    private boolean isNewCode = false;// 是否为新问题编号
    private SyncPatientBean patient = null;
    private ListView lvPioProblem, lvPioCauses, lvPioTarget, lvPioMeasure;
    private Button goBack, btnHulizhiyin, btnHuliyuanyin, btnHulifangfa, btnHulicuoshi;
    private ImageButton addBtn;
    private AddPioAdapter adapterProblem = null;
    private AddPioAdapter adapterCauses = null;
    private AddPioAdapter adapterTarget = null;
    private AddPioAdapter adapterMeasure = null;

    private int flag;
    private int lastTimeChoose = -1;
    private EditText createEditText;
    private String createText;
    private String logTime;

    private LoadPioRecordBean pio;
    private TextView tv_head;

    private AlertDialog builder = null;
    private AlertDialog secondDialog;

    private RelativeLayout rl_huliyuanyin, rl_hulizhiyin, rl_hulifangfa, rl_hulicuoshi;
    @ViewInject(R.id.tv_bed_label)
    private TextView tv_bed_lable;
    @ViewInject(R.id.tv_parent)
    private TextView tv_parent;
    @ViewInject(R.id.pio_shouxie_nursing_measure)
    private Button pio_shouxie_nursing_measure;
    @ViewInject(R.id.pio_moren_nursing_measure)
    private Button pio_moren_nursing_measure;

    @ViewInject(R.id.pio_shouxie_nursing_cast)
    private Button pio_shouxie_nursing_cast;
    @ViewInject(R.id.pio_moren_nursing_cast)
    private Button pio_moren_nursing_cast;

    @ViewInject(R.id.pio_shouxie_nursing_mubiao)
    private Button pio_shouxie_nursing_mubiao;
    @ViewInject(R.id.pio_moren_nursing_mubiao)
    private Button pio_moren_nursing_mubiao;

    @ViewInject(R.id.pio_shouxie_nursing_cuoshi)
    private Button pio_shouxie_nursing_cuoshi;
    @ViewInject(R.id.pio_moren_nursing_cuoshi)
    private Button pio_moren_nursing_cuoshi;
    /**
     * 1护理问题 2护理致因 3护理目标 4护理措施
     */
    public static final int PIO_PROBLEM = 1;
    public static final int PIO_CAUSES = 2;
    public static final int PIO_TARGET = 3;
    public static final int PIO_MEASURE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_pio_rec_main);
        HidnGestWindow(true);
        ViewUtils.inject(this);
        progressDialog = new ProgressDialog(ModifyPioRecActivity.this);

        patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");
        pio = (LoadPioRecordBean) getIntent().getSerializableExtra("pio");
        logTime = getIntent().getStringExtra("logTime");

        initView();
        initAssignment();//赋值

        assignment(mutChooseCausesListTemp, mutChooseCausesList, orgmutChooseCausesListTemp,
                orgmutChooseCausesList, pio.getCauses());
        assignment(mutChooseTargetListTemp, mutChooseTargetList, orgmutChooseTargetListTemp,
                orgmutChooseTargetList, pio.getTarget());
        assignment(mutChooseMeasureListTemp, mutChooseMeasureList, orgmutChooseMeasureListTemp,
                orgmutChooseMeasureList, pio.getMeasure());

        DoPioDictReq(new PioBean(currentCode, 0));
        adapterProblem = new AddPioAdapter(ModifyPioRecActivity.this, problemChoosenList);
        lvPioProblem.setAdapter(adapterProblem);
        if (mutChooseCausesList.get(0).getItemName() == null) {
            mutChooseCausesList.clear();
        }
        adapterCauses = new AddPioAdapter(ModifyPioRecActivity.this, mutChooseCausesList);
        lvPioCauses.setAdapter(adapterCauses);
        if (mutChooseTargetList.get(0).getItemName() == null) {
            mutChooseTargetList.clear();
        }
        adapterTarget = new AddPioAdapter(ModifyPioRecActivity.this, mutChooseTargetList);
        lvPioTarget.setAdapter(adapterTarget);
        adapterMeasure = new AddPioAdapter(ModifyPioRecActivity.this, mutChooseMeasureList);
        lvPioMeasure.setAdapter(adapterMeasure);
        initBinding();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapterCauses.notifyDataSetChanged();
        adapterMeasure.notifyDataSetChanged();
        adapterProblem.notifyDataSetChanged();
        adapterTarget.notifyDataSetChanged();
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_recpio);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void initAssignment() {
        PioItemInfo problemInfo = new PioItemInfo();
        problemInfo.setItemName(pio.getProblemName());
        problemInfo.setItemCode(pio.getProblemCode());
        problemInfo.setItemClass("P");
        problemChoosenList.add(problemInfo);
        currentCode = pio.getProblemCode();
        tv_head.setText("修改护理事件");
        tv_bed_lable.setText(patient.getBedLabel());
        tv_parent.setText(patient.getName());
        orgProblemList.addAll(problemChoosenList);
    }

    private void initBinding() {
        lvPioCauses.setOnItemLongClickListener(this);
        lvPioCauses.setOnItemClickListener(this);
        lvPioMeasure.setOnItemLongClickListener(this);
        lvPioMeasure.setOnItemClickListener(this);
        lvPioTarget.setOnItemLongClickListener(this);
        lvPioTarget.setOnItemClickListener(this);
        lvPioProblem.setOnItemLongClickListener(this);
        addBtn.setOnClickListener(ModifyPioRecActivity.this);
        goBack.setOnClickListener(ModifyPioRecActivity.this);
        btnHulicuoshi.setOnClickListener(ModifyPioRecActivity.this);
        btnHulifangfa.setOnClickListener(ModifyPioRecActivity.this);
        btnHulizhiyin.setOnClickListener(ModifyPioRecActivity.this);
        btnHuliyuanyin.setOnClickListener(ModifyPioRecActivity.this);
        pio_shouxie_nursing_measure.setOnClickListener(this);
        pio_moren_nursing_measure.setOnClickListener(this);
        pio_shouxie_nursing_cast.setOnClickListener(this);
        pio_moren_nursing_cast.setOnClickListener(this);
        pio_shouxie_nursing_mubiao.setOnClickListener(this);
        pio_moren_nursing_mubiao.setOnClickListener(this);
        pio_shouxie_nursing_cuoshi.setOnClickListener(this);
        pio_moren_nursing_cuoshi.setOnClickListener(this);
    }

    private void initView() {
        goBack = (Button) findViewById(R.id.btn_back);
        addBtn = (ImageButton) findViewById(R.id.add_btn);
        lvPioProblem = (ListView) findViewById(R.id.elv_pio_problem);
        lvPioCauses = (ListView) findViewById(R.id.elv_pio_causes);
        lvPioTarget = (ListView) findViewById(R.id.elv_pio_target);
        lvPioMeasure = (ListView) findViewById(R.id.elv_pio_measure);
        btnHulicuoshi = (Button) findViewById(R.id.btn_hulicuoshi);
        btnHulifangfa = (Button) findViewById(R.id.btn_hulifangfa);
        btnHuliyuanyin = (Button) findViewById(R.id.btn_huliyuanyin);
        btnHulizhiyin = (Button) findViewById(R.id.btn_hulizhiyin);
        tv_head = (TextView) findViewById(R.id.tv_head);
        rl_huliyuanyin = (RelativeLayout) findViewById(R.id.rl_huliyuanyin);
        rl_hulizhiyin = (RelativeLayout) findViewById(R.id.rl_hulizhiyin);
        rl_hulifangfa = (RelativeLayout) findViewById(R.id.rl_hulifangfa);
        rl_hulicuoshi = (RelativeLayout) findViewById(R.id.rl_hulicuoshi);
    }

    private void assignment(List<PioItemInfo> tempList, List<PioItemInfo> list, List<PioItemInfo> orgtempList,
                            List<PioItemInfo> orglist, List<PioItemInfo> mylist) {
        for (int a = 0; a < mylist.size(); a++) {
            PioItemInfo info1 = new PioItemInfo();
            PioItemInfo info2 = new PioItemInfo();
            info1.setItemName(mylist.get(a).getItemName());
            info1.setItemClass(mylist.get(a).getItemClass());
            info1.setItemNo(mylist.get(a).getItemNo());
            if (mylist.get(a).getItemCode().equals("thisIsNull")) {
                info2.setItemName(mylist.get(a).getItemName());
                info2.setItemClass(mylist.get(a).getItemClass());
                info2.setItemNo(mylist.get(a).getItemNo());
                tempList.add(info2);
                orgtempList.add(info2);
            } else {
                info1.setItemCode(mylist.get(a).getItemCode());
                list.add(info1);
                orglist.add(info1);
            }
        }
        list.addAll(tempList);
        orglist.addAll(orgtempList);

    }

    /**
     * 点击修改
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int myPosition = position;
        switch (parent.getId()) {
            case R.id.elv_pio_target:
                createEditText = new EditText(ModifyPioRecActivity.this);
                createEditText.setText(mutChooseTargetList.get(myPosition).getItemName());
                if (mutChooseTargetList.get(myPosition).getItemCode() != null) {
                    Toast.makeText(ModifyPioRecActivity.this, "护理提示项不可修改，请长按删除后重新选择", Toast.LENGTH_LONG)
                            .show();
                } else {
                    AlertDialog modifyDialog = new Builder(ModifyPioRecActivity.this, AlertDialog
                            .THEME_HOLO_LIGHT).setTitle("请修改护理方法").setView(createEditText).setPositiveButton
                            ("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PioItemInfo info = mutChooseTargetList.get(myPosition);
                                    info.setItemName(createEditText.getText().toString());
                                    mutChooseTargetList.remove(myPosition);
                                    mutChooseTargetListTemp.remove(myPosition);
                                    mutChooseTargetList.add(info);
                                    mutChooseTargetListTemp.add(info);
                                    adapterTarget.notifyDataSetChanged();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapterTarget.notifyDataSetChanged();
                        }
                    }).create();
                    modifyDialog.show();
                    modifyDialog.getWindow().setLayout(680, 600);
                }
                break;
            case R.id.elv_pio_measure:
                createEditText = new EditText(ModifyPioRecActivity.this);
                createEditText.setText(mutChooseMeasureList.get(myPosition).getItemName());
                if (mutChooseMeasureList.get(myPosition).getItemCode() != null) {
                    Toast.makeText(ModifyPioRecActivity.this, "护理提示项不可修改，请长按删除后重新选择", Toast.LENGTH_LONG)
                            .show();
                } else {
                    AlertDialog modifyDialog = new Builder(ModifyPioRecActivity.this, AlertDialog
                            .THEME_HOLO_LIGHT).setTitle("请修改护理方法").setView(createEditText).setPositiveButton
                            ("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PioItemInfo info = mutChooseMeasureList.get(myPosition);
                                    info.setItemName(createEditText.getText().toString());
                                    mutChooseMeasureList.remove(myPosition);
                                    mutChooseMeasureListTemp.remove(myPosition);
                                    mutChooseMeasureList.add(info);
                                    mutChooseMeasureListTemp.add(info);
                                    adapterMeasure.notifyDataSetChanged();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapterMeasure.notifyDataSetChanged();
                        }
                    }).create();
                    modifyDialog.show();
                    modifyDialog.getWindow().setLayout(680, 600);
                }
                break;
            case R.id.elv_pio_causes:
                createEditText = new EditText(ModifyPioRecActivity.this);
                createEditText.setText(mutChooseCausesList.get(myPosition).getItemName());
                if (mutChooseCausesList.get(myPosition).getItemCode() != null) {
                    Toast.makeText(ModifyPioRecActivity.this, "护理提示项不可修改，请长按删除后重新选择", Toast.LENGTH_LONG)
                            .show();
                } else {
                    AlertDialog modifyDialog = new Builder(ModifyPioRecActivity.this, AlertDialog
                            .THEME_HOLO_LIGHT).setTitle("请修改护理方法").setView(createEditText).setPositiveButton
                            ("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PioItemInfo info = mutChooseCausesList.get(myPosition);
                                    info.setItemName(createEditText.getText().toString());
                                    mutChooseCausesList.remove(myPosition);
                                    mutChooseCausesListTemp.remove(myPosition);
                                    mutChooseCausesList.add(info);
                                    mutChooseCausesListTemp.add(info);
                                    adapterCauses.notifyDataSetChanged();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapterCauses.notifyDataSetChanged();
                        }
                    }).create();
                    modifyDialog.show();
                    modifyDialog.getWindow().setLayout(680, 600);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 长按删除事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int myPosition = position;
        switch (parent.getId()) {
            case R.id.elv_pio_problem:
                new Builder(ModifyPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage
                        ("确定删除吗").setPositiveButton("删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        problemChoosenList.remove(myPosition);
                        adapterProblem.notifyDataSetChanged();
                        clearOtherAndRefresh();
                    }
                }).setNegativeButton("不删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterProblem.notifyDataSetChanged();
                    }
                }).show();
                break;
            case R.id.elv_pio_target:
                new Builder(ModifyPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage
                        ("确定删除吗").setPositiveButton("删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mutChooseTargetListTemp.remove(mutChooseTargetList.get(myPosition));
                        mutChooseTargetList.remove(myPosition);
                        adapterTarget.notifyDataSetChanged();
                    }
                }).setNegativeButton("不删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterTarget.notifyDataSetChanged();
                    }
                }).show();
                break;
            case R.id.elv_pio_measure:
                new Builder(ModifyPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage
                        ("确定删除吗").setPositiveButton("删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mutChooseMeasureListTemp.remove(mutChooseMeasureList.get(myPosition));
                        mutChooseMeasureList.remove(myPosition);
                        adapterMeasure.notifyDataSetChanged();
                    }
                }).setNegativeButton("不删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterMeasure.notifyDataSetChanged();
                    }
                }).show();
                break;
            case R.id.elv_pio_causes:
                new Builder(ModifyPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage
                        ("确定删除吗").setPositiveButton("删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mutChooseCausesListTemp.remove(mutChooseCausesList.get(myPosition));
                        mutChooseCausesList.remove(myPosition);
                        adapterCauses.notifyDataSetChanged();
                    }
                }).setNegativeButton("不删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterCauses.notifyDataSetChanged();
                    }
                }).show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                judgeIsSubmit();
                break;
            case R.id.pio_shouxie_nursing_measure:
                manualInput(PIO_PROBLEM);
                break;
            case R.id.pio_moren_nursing_measure:
                DoPioDictReq(new PioBean(currentCode, 0));
                break;
            case R.id.pio_shouxie_nursing_cast:
                manualInput(PIO_CAUSES);
                break;
            case R.id.pio_moren_nursing_cast:
                DoPioDictReq(new PioBean(currentCode, 1));
                break;
            case R.id.pio_shouxie_nursing_mubiao:
                manualInput(PIO_TARGET);
                break;
            case R.id.pio_moren_nursing_mubiao:
                DoPioDictReq(new PioBean(currentCode, 2));
                break;
            case R.id.pio_shouxie_nursing_cuoshi:
                manualInput(PIO_MEASURE);
                break;
            case R.id.pio_moren_nursing_cuoshi:
                DoPioDictReq(new PioBean(currentCode, 3));
                break;
            default:
                break;
        }
    }

    /**
     * 变化按钮颜色
     *
     * @param whichBarChoosen
     */
    private void changeBtn(int whichBarChoosen) {
        switch (whichBarChoosen) {
            case PIO_PROBLEM:
                rl_huliyuanyin.setBackgroundResource(R.color.btn_blue_pio_choosen);
                rl_hulizhiyin.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                rl_hulifangfa.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                rl_hulicuoshi.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                break;
            case PIO_CAUSES:
                rl_huliyuanyin.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                rl_hulizhiyin.setBackgroundResource(R.color.btn_blue_pio_choosen);
                rl_hulifangfa.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                rl_hulicuoshi.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                break;
            case PIO_TARGET:
                rl_huliyuanyin.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                rl_hulizhiyin.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                rl_hulifangfa.setBackgroundResource(R.color.btn_blue_pio_choosen);
                rl_hulicuoshi.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                break;
            case PIO_MEASURE:
                rl_huliyuanyin.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                rl_hulizhiyin.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                rl_hulifangfa.setBackgroundResource(R.color.btn_blue_pio_not_choosen);
                rl_hulicuoshi.setBackgroundResource(R.color.btn_blue_pio_choosen);
                break;
            default:
                break;
        }
    }

    /**
     * 手动输入
     *
     * @param type
     */
    public void manualInput(int type) {
        LayoutInflater inflater = (LayoutInflater) ModifyPioRecActivity.this.getSystemService
                (LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_pio_alertdilog_edittext_other, null);
        createEditText = (EditText) view.findViewById(R.id.createEv);
        Builder myBuilder = new Builder(ModifyPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        switch (type) {
            case PIO_PROBLEM:
                changeBtn(type);
                myBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createText = createEditText.getText().toString();
                        if (createText != null && !createText.equals("")) {
                            PioItemInfo info = new PioItemInfo();
                            info.setItemName(createText);
                            info.setItemClass("P");
                            problemChoosenList.clear();
                            problemChoosenList.add(info);
                            adapterProblem.notifyDataSetChanged();
                            clearOtherAndRefresh();
                            changeBtn(PIO_PROBLEM);
                        }
                    }
                });
                myBuilder.setNegativeButton("取消", null);
                secondDialog = myBuilder.create();
                secondDialog.setTitle("请手动输入护理问题");
                secondDialog.setView(view);
                secondDialog.show();
                secondDialog.getWindow().setLayout(650, 600);
                break;
            case PIO_CAUSES:
                changeBtn(type);
                myBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createText = createEditText.getText().toString();
                        if (createText != null && !createText.equals("")) {
                            PioItemInfo info = new PioItemInfo();
                            info.setItemName(createText);
                            info.setItemClass("C");
                            mutChooseCausesList.add(info);
                            mutChooseCausesListTemp.add(info);
                            adapterCauses.notifyDataSetChanged();
                            changeBtn(PIO_CAUSES);
                        }
                    }
                });
                myBuilder.setNegativeButton("取消", null);
                secondDialog = myBuilder.create();
                secondDialog.setTitle("请手动输入护理致因");
                secondDialog.setView(view);
                secondDialog.show();
                secondDialog.getWindow().setLayout(650, 600);
                break;
            case PIO_TARGET:
                changeBtn(type);
                myBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createText = createEditText.getText().toString();
                        if (createText != null && !createText.equals("")) {
                            PioItemInfo info = new PioItemInfo();
                            info.setItemName(createText);
                            info.setItemClass("I");
                            mutChooseTargetList.add(info);
                            mutChooseTargetListTemp.add(info);
                            adapterTarget.notifyDataSetChanged();
                            changeBtn(PIO_TARGET);
                        }
                    }
                });

                myBuilder.setNegativeButton("取消", null);
                secondDialog = myBuilder.create();
                secondDialog.setTitle("请手动输入护理目标");
                secondDialog.setView(view);
                secondDialog.show();
                secondDialog.getWindow().setLayout(650, 600);
                break;
            case PIO_MEASURE:
                changeBtn(type);
                myBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createText = createEditText.getText().toString();
                        if (createText != null && !createText.equals("")) {
                            PioItemInfo info = new PioItemInfo();
                            info.setItemName(createText);
                            info.setItemClass("O");
                            mutChooseMeasureList.add(info);
                            mutChooseMeasureListTemp.add(info);
                            adapterMeasure.notifyDataSetChanged();
                            changeBtn(PIO_MEASURE);
                        }
                    }
                });
                myBuilder.setNegativeButton("取消", null);
                secondDialog = myBuilder.create();
                secondDialog.setTitle("请手动输入护理措施");
                secondDialog.setView(view);
                secondDialog.show();
                secondDialog.getWindow().setLayout(650, 600);
                break;
            default:
                break;
        }
    }

    /**
     * 禁用系统返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            judgeIsSubmit();
        }
        return false;
    }

    /**
     * 加载PIO护理字典
     *
     * @param pioBean
     */
    private void DoPioDictReq(final PioBean pioBean) {
        progressDialog.setMessage("获取数据中,请稍后..");
        progressDialog.show();
        String url = "";
        int type = pioBean.getType();
        switch (type) {
            case 0:
                url = UrlConstant.LoadProblemDict();
                break;
            case 1:
                url = UrlConstant.LoadCausesDict();
                break;
            case 2:
                url = UrlConstant.LoadTargetDict();
                break;
            case 3:
                url = UrlConstant.LoadMeasure();
                break;
            case 4:
                url = UrlConstant.LoadAppraisal();
                break;
            default:
                break;
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CCLog.i(TAG, "获取pio记录成功");
                progressDialog.dismiss();
                Gson gson = new Gson();
                Type type = new TypeToken<List<PioItemInfo>>() {
                }.getType();
                List<PioItemInfo> list = gson.fromJson(result, type);
                problemList.clear();
                // mutCausesList.clear();
                // mutTargetList.clear();
                // mutMeasureList.clear();
                if (pioBean.getType() == PIO_PROBLEM - 1) {
                    if (list != null && list.size() > 0) {
                        if (flag == 2) {
                            problemList.addAll(list);
                            ShowSighChooseAlert(problemList);
                            changeBtn(PIO_PROBLEM);
                        }
                        flag = 2;
                    }
                }
                if (pioBean.getType() == PIO_CAUSES - 1) {
                    if (list != null && list.size() > 0) {
                        mutCausesList.clear();
                        mutCausesList.addAll(list);
                        ShowMutChooseAlert(mutCausesList, 1);
                        changeBtn(PIO_CAUSES);
                    } else {
                        Toast.makeText(ModifyPioRecActivity.this, "暂无关联数据,请手动输入.", Toast.LENGTH_SHORT).show();
                        manualInput(PIO_CAUSES);
                    }
                }
                if (pioBean.getType() == PIO_TARGET - 1) {
                    if (list != null && list.size() > 0) {
                        mutTargetList.clear();
                        mutTargetList.addAll(list);
                        ShowMutChooseAlert(mutTargetList, 2);
                        changeBtn(PIO_TARGET);
                    } else {
                        Toast.makeText(ModifyPioRecActivity.this, "暂无关联数据,请手动输入.", Toast.LENGTH_SHORT).show();
                        manualInput(PIO_TARGET);
                    }
                }
                if (pioBean.getType() == PIO_MEASURE - 1) {
                    if (list != null && list.size() > 0) {
                        mutMeasureList.clear();
                        mutMeasureList.addAll(list);
                        ShowMutChooseAlert(mutMeasureList, 3);
                        changeBtn(PIO_MEASURE);
                    } else {
                        Toast.makeText(ModifyPioRecActivity.this, "暂无关联数据,请手动输入.", Toast.LENGTH_SHORT).show();
                        manualInput(PIO_MEASURE);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                progressDialog.dismiss();
                CCLog.i(TAG, "获取pio记录失败");
                Toast.makeText(ModifyPioRecActivity.this, "操作异常.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示单选列表对话框
     *
     * @param list 列表数据
     */
    private void ShowSighChooseAlert(final List<PioItemInfo> list) {
        String[] strs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strs[i] = list.get(i).getItemName();
        }
        AlertDialog alert = new Builder(ModifyPioRecActivity.this,
                AlertDialog.THEME_HOLO_LIGHT).setTitle("请选择护理问题")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isNewCode = true;
                        String code = list.get(i).getItemCode();
                        if (currentCode != null) {

                            if (!currentCode.equals(code)) {
                                lastTimeChoose = i;
                                currentCode = list.get(i).getItemCode();
                                problemChoosenList.clear();
                                problemChoosenList.add(list.get(i));
                                adapterProblem.notifyDataSetChanged();
                                clearOtherAndRefresh();
                            }
                        } else {
                            lastTimeChoose = i;
                            currentCode = list.get(i).getItemCode();
                            problemChoosenList.clear();
                            problemChoosenList.add(list.get(i));
                            adapterProblem.notifyDataSetChanged();
                            clearOtherAndRefresh();
                        }
                    }
                }).create();
        alert.show();
        alert.getWindow().setLayout(680, 950);
    }

    /**
     * 显示多选列表对话框
     *
     * @param list 列表数据
     * @param type 类型 1护理原因 2 护理目标 3 护理措施
     */
    public void ShowMutChooseAlert(final List<PioItemInfo> list, final int type) {
        int mLen = 0;
        boolean[] booleans = new boolean[mLen];
        if (!list.isEmpty()) {
            mLen = list.size();
            booleans = new boolean[mLen];
            for (int i = 0; i < list.size(); i++) {
                if (type == 1) {
                    for (int z = 0; z < mutChooseCausesList.size(); z++) {
                        if (mutChooseCausesList.get(z).getItemCode() != null) {
                            if (mutChooseCausesList.get(z).getItemCode().equals(list.get(i).getItemCode())) {
                                booleans[i] = true;
                            }
                        }
                    }
                }
                if (type == 2) {
                    for (int z = 0; z < mutChooseTargetList.size(); z++) {
                        if (mutChooseTargetList.get(z).getItemCode() != null) {
                            if (mutChooseTargetList.get(z).getItemCode().equals(list.get(i).getItemCode())) {
                                booleans[i] = true;
                            }
                        }
                    }
                }
                if (type == 3) {
                    for (int z = 0; z < mutChooseMeasureList.size(); z++) {
                        if (mutChooseMeasureList.get(z).getItemCode() != null) {
                            if (mutChooseMeasureList.get(z).getItemCode().equals(list.get(i).getItemCode())) {
                                booleans[i] = true;
                            }
                        }
                    }
                }
            }
        }
        if (type == 1) {
            mutChooseCausesList.clear();
            if (!mutChooseCausesListTemp.isEmpty()) {
                mutChooseCausesList.addAll(mutChooseCausesListTemp);
            }
        }
        if (type == 2) {
            mutChooseTargetList.clear();
            if (!mutChooseTargetListTemp.isEmpty()) {
                mutChooseTargetList.addAll(mutChooseTargetListTemp);
            }
        }
        if (type == 3) {
            mutChooseMeasureList.clear();
            if (!mutChooseMeasureListTemp.isEmpty()) {
                mutChooseMeasureList.addAll(mutChooseMeasureListTemp);
            }
        }
        if (!list.isEmpty()) {
            final String[] strs = new String[mLen];
            for (int i = 0; i < list.size(); i++) {
                strs[i] = list.get(i).getItemName();
            }
            builder = new Builder(ModifyPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setTitle
                    ("请选择相关选项").setMultiChoiceItems(strs, booleans, null).setPositiveButton("确定", new
                    DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    boolean flag = false;
                    for (int j = 0; j < strs.length; j++) {
                        if (builder.getListView().getCheckedItemPositions().get(j)) {
                            if (type == PIO_CAUSES - 1) {
                                mutChooseCausesList.add(list.get(j));
                                adapterCauses.notifyDataSetChanged();
                            }
                            if (type == PIO_TARGET - 1) {
                                mutChooseTargetList.add(list.get(j));
                                adapterTarget.notifyDataSetChanged();
                            }
                            if (type == PIO_MEASURE - 1) {
                                mutChooseMeasureList.add(list.get(j));
                                adapterMeasure.notifyDataSetChanged();
                            }
                            adapterCauses.notifyDataSetChanged();
                            adapterTarget.notifyDataSetChanged();
                            adapterMeasure.notifyDataSetChanged();
                            flag = true;
                        }
                    }
                    if (!flag) {
                        if (type == PIO_CAUSES - 1) {
                            mutChooseCausesList.clear();
                            adapterCauses.notifyDataSetChanged();
                        }
                        if (type == PIO_TARGET - 1) {
                            mutChooseTargetList.clear();
                            adapterTarget.notifyDataSetChanged();
                        }
                        if (type == PIO_MEASURE - 1) {
                            mutChooseMeasureList.clear();
                            adapterMeasure.notifyDataSetChanged();
                        }
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapterCauses.notifyDataSetChanged();
                    adapterTarget.notifyDataSetChanged();
                    adapterMeasure.notifyDataSetChanged();
                }
            }).create();
            builder.show();
            builder.getWindow().setLayout(680, 750);
        }
    }

    private void clearOtherAndRefresh() {
        mutChooseCausesList.clear();
        mutChooseMeasureList.clear();
        mutChooseTargetList.clear();
        mutChooseCausesList.addAll(mutChooseCausesListTemp);
        mutChooseMeasureList.addAll(mutChooseMeasureListTemp);
        mutChooseTargetList.addAll(mutChooseTargetListTemp);
        adapterCauses.notifyDataSetChanged();
        adapterMeasure.notifyDataSetChanged();
        adapterTarget.notifyDataSetChanged();
    }

    private void clearAll() {
        problemList.clear();
        problemChoosenList.clear();
        mutCausesList.clear();
        mutChooseCausesList.clear();
        mutChooseCausesListTemp.clear();
        mutChooseMeasureList.clear();
        mutChooseMeasureListTemp.clear();
        mutChooseTargetList.clear();
        mutChooseTargetListTemp.clear();
        mutMeasureList.clear();
        mutTargetList.clear();
        currentCode = "";
    }

    /**
     * 提交，插入数据库
     */

    private void DoSubmitPioRecordReq(List<PioItemInfo> cclist, List<PioItemInfo> tclist, List<PioItemInfo>
            mclist) {
        String problemCode = "thisIsNull";
        String problemName = "thisIsNull";
        StringBuffer causesCode = new StringBuffer();
        StringBuffer causesName = new StringBuffer();
        StringBuffer targetCode = new StringBuffer();
        StringBuffer targetName = new StringBuffer();
        StringBuffer measureCode = new StringBuffer();
        StringBuffer measureName = new StringBuffer();
        for (int i = 0; i < cclist.size(); i++) {
            PioItemInfo info = cclist.get(i);
            if (info.getItemCode() == null) {
                causesCode.append("thisIsNull");
                if (i != cclist.size() - 1) {
                    causesCode.append("<<");
                }
            } else {
                causesCode.append(info.getItemCode());
                if (i != cclist.size() - 1) {
                    causesCode.append("<<");
                }
            }
            causesName.append(info.getItemName());
            if (i != cclist.size() - 1) {
                causesName.append("<<");
            }
        }
        for (int j = 0; j < tclist.size(); j++) {
            PioItemInfo info = tclist.get(j);
            if (info.getItemCode() == null) {
                targetCode.append("thisIsNull");
                if (j != tclist.size() - 1) {
                    targetCode.append("<<");
                }
            } else {
                targetCode.append(info.getItemCode());
                if (j != tclist.size() - 1) {
                    targetCode.append("<<");
                }
            }
            targetName.append(info.getItemName());
            if (j != tclist.size() - 1) {
                targetName.append("<<");
            }
        }
        for (int k = 0; k < mclist.size(); k++) {
            PioItemInfo info = mclist.get(k);
            if (info.getItemCode() == null) {
                measureCode.append("thisIsNull");
                if (k != mclist.size() - 1) {
                    measureCode.append("<<");
                }
            } else {
                measureCode.append(info.getItemCode());
                if (k != mclist.size() - 1) {
                    measureCode.append("<<");
                }
            }
            measureName.append(info.getItemName());
            if (k != mclist.size() - 1) {
                measureName.append("<<");
            }
        }
        RecordPioBean recordPioBean = new RecordPioBean();
        recordPioBean.setPatId(patient.getPatId());
        recordPioBean.setProblemCode(problemCode);
        recordPioBean.setProblemName(problemName);
        recordPioBean.setCauseCode(causesCode.toString());
        recordPioBean.setCauseName(causesName.toString());
        recordPioBean.setMeasureCode(measureCode.toString());
        recordPioBean.setMeasureName(measureName.toString());
        recordPioBean.setTargetCode(targetCode.toString());
        recordPioBean.setTargetName(targetName.toString());
        recordPioBean.setLogBy(UserInfo.getName());
        recordPioBean.setPioId(pio.getPioId());
        recordPioBean.setLogTime(logTime);
        if (!cclist.isEmpty()) {
            recordPioBean.setCausesNo(cclist.get(cclist.size() - 1).getItemNo() + "");
        }
        if (!tclist.isEmpty()) {
            recordPioBean.setTargetNo(tclist.get(tclist.size() - 1).getItemNo() + "");
        }
        if (!mclist.isEmpty()) {
            recordPioBean.setMeasureNo(mclist.get(mclist.size() - 1).getItemNo() + "");
        }
        CCLog.i(TAG, "DoSubmitPioRecordReq>" + UrlConstant.RecordPio() + recordPioBean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.RecordPio() + recordPioBean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        CCLog.i(TAG, "PIO数据提交成功");
                        Gson gson = new Gson();
                        RecordPioBean recordPioBean = gson.fromJson(result, RecordPioBean.class);
                        if (recordPioBean.getResult().equals("success")) {
                            clearAll();
                            finish();
                        } else {
                            Toast.makeText(ModifyPioRecActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        CCLog.i(TAG, "PIO数据提交失败");
                        Toast.makeText(ModifyPioRecActivity.this,R.string.unstable, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 删除，修改记录
     */
    public void delAndModPioRecordReq(List<PioItemInfo> del, List<PioItemInfo> mod) {
        progressDialog.setMessage("获取数据中,请稍后..");
        progressDialog.show();
        judgeWhichOneTodelAndMod();
        StringBuffer itemno = new StringBuffer();
        for (int i = 0; i < delList.size(); i++) {
            itemno.append(delList.get(i).getItemNo());
            if (i != delList.size() - 1) {
                itemno.append("<<");
            }
        }
        DeleteAndModPioBean dmPioBean = new DeleteAndModPioBean();
        dmPioBean.setPatId(patient.getPatId());
        if (!modifyList.isEmpty()) {
            dmPioBean.setProblemCode(modifyList.get(0).getItemCode());
            dmPioBean.setProblemName(modifyList.get(0).getItemName());
        }
        dmPioBean.setItemNo(itemno.toString());
        dmPioBean.setPioId(pio.getPioId());
        dmPioBean.setLogBy(UserInfo.getName());
        String url = UrlConstant.delAndModPio() + dmPioBean.toString();
        CCLog.i(TAG, "delAndModPioRecordReq>" + url);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                progressDialog.dismiss();
                CCLog.i(TAG, "PIO数据删除成功");
                Gson gson = new Gson();
                DeleteAndModPioBean dmPioBean = gson.fromJson(result, DeleteAndModPioBean.class);
                if (dmPioBean.getResult().equals("success")) {
                    Toast.makeText(ModifyPioRecActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
//                    clearAll();
                    delList.clear();
                    modifyList.clear();
                    DoSubmitPioRecordReq(addCausesList, addTargetList, addMeasureList);
                } else {
                    Toast.makeText(ModifyPioRecActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressDialog.dismiss();
                CCLog.i(TAG, "PIO数据提交失败");
                Toast.makeText(ModifyPioRecActivity.this, R.string.unstable, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void judgeWhichOneTodelAndMod() {
        String orgProblemListProblemName = orgProblemList.get(0).getItemName();
        String problemListChoosenProblem = problemChoosenList.get(0).getItemName();
        if (orgProblemListProblemName != problemListChoosenProblem) {
            modifyList.addAll(problemChoosenList);
            delList.addAll(orgmutChooseCausesList);
            delList.addAll(orgmutChooseMeasureList);
            delList.addAll(orgmutChooseTargetList);
            addCausesList.addAll(mutChooseCausesList);
            addTargetList.addAll(mutChooseTargetList);
            addMeasureList.addAll(mutChooseMeasureList);
        } else {
            delList.addAll(orgmutChooseCausesList);
            delList.addAll(orgmutChooseMeasureList);
            delList.addAll(orgmutChooseTargetList);
            addCausesList.addAll(mutChooseCausesList);
            addTargetList.addAll(mutChooseTargetList);
            addMeasureList.addAll(mutChooseMeasureList);
        }
    }

    /**
     * 判断是否提交
     */
    private void judgeIsSubmit() {
        new Builder(ModifyPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage
                ("是否保存输入的信息")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // if (mutChooseCausesList.isEmpty()) {
                        // Toast.makeText(ModifyPioRecActivity.this,
                        // "请输入护理致因再提交", Toast.LENGTH_SHORT).show();
                        // } else
                        if (mutChooseMeasureList.isEmpty()) {
                            Toast.makeText(ModifyPioRecActivity.this, "请输入护理措施再提交", Toast.LENGTH_SHORT)
                                    .show();
                        } else
                            // if (mutChooseTargetList.isEmpty()) {
                            // Toast.makeText(ModifyPioRecActivity.this,
                            // "请输入护理方法再提交", Toast.LENGTH_SHORT).show();
                            // } else
                            if (problemChoosenList.isEmpty()) {
                                Toast.makeText(ModifyPioRecActivity.this, "请输入护理问题再提交", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                delAndModPioRecordReq(delList, modifyList);
                            }
                    }
                }).setNegativeButton("不保存", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }
}
