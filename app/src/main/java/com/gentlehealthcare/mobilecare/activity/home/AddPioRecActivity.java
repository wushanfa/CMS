package com.gentlehealthcare.mobilecare.activity.home;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.PioBean;
import com.gentlehealthcare.mobilecare.net.bean.PioItemInfo;
import com.gentlehealthcare.mobilecare.net.bean.RecordPioBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.impl.LoadPioDictRequest;
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
public class AddPioRecActivity extends BaseActivity implements OnClickListener, OnItemLongClickListener {

    private static final String TAG = "AddPioRecActivity";
    private ProgressDialog progressDialog = null;
    private List<PioItemInfo> problemList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> problemChoosenList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseCausesList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutCausesList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseTargetList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutTargetList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutMeasureList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseMeasureList = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseCausesListTemp = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseTargetListTemp = new ArrayList<PioItemInfo>();
    private List<PioItemInfo> mutChooseMeasureListTemp = new ArrayList<PioItemInfo>();
    private String currentCode = "";// 当前的护理问题编号

    private SyncPatientBean patient = null;
    private ListView lvPioProblem, lvPioCauses, lvPioTarget, lvPioMeasure;
    private Button btnHulizhiyin, btnHuliyuanyin, btnHulifangfa,
            btnHulicuoshi;
    private ImageButton addBtn;
    private AddPioAdapter adapterProblem = null;
    private AddPioAdapter adapterCauses = null;
    private AddPioAdapter adapterTarget = null;
    private AddPioAdapter adapterMeasure = null;

    private int lastTimeChoose = -1;
    private EditText createEditText;
    private String createText;
    private String logTime;
    private RelativeLayout rl_huliyuanyin, rl_hulizhiyin, rl_hulifangfa,
            rl_hulicuoshi;
    private TextView tv_head;

    @ViewInject(R.id.btn_back)
    private Button goBack;
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

    public AlertDialog secondDialog = null;
    public AlertDialog builder = null;
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
        setContentView(R.layout.add_pio_rec_main);
        ViewUtils.inject(this);
        progressDialog = new ProgressDialog(AddPioRecActivity.this);
        patient = (SyncPatientBean) getIntent().getSerializableExtra("patient");
        logTime = getIntent().getStringExtra("logTime");
        initView();

        tv_head.setText(R.string.nursing_documentation);
        if (patient.getBedLabel() != null) {
            tv_bed_lable.setText(patient.getBedLabel() + getString(R.string.bed));
        } else {
            tv_bed_lable.setText(Html.fromHtml("<small>未分配</small>"));
        }

        tv_parent.setText(patient.getName());

        DoPioDictReq(new PioBean(currentCode, 0));

        lvPioProblem.setAdapter(adapterProblem = new AddPioAdapter(AddPioRecActivity.this,
                problemChoosenList));
        lvPioCauses.setAdapter(adapterCauses = new AddPioAdapter(AddPioRecActivity.this,
                mutChooseCausesList));
        lvPioTarget.setAdapter(adapterTarget = new AddPioAdapter(AddPioRecActivity.this,
                mutChooseTargetList));
        lvPioMeasure.setAdapter(adapterMeasure = new AddPioAdapter(AddPioRecActivity.this,
                mutChooseMeasureList));

        initBinding();
    }

    private void initBinding() {
        lvPioCauses.setOnItemLongClickListener(this);
        lvPioMeasure.setOnItemLongClickListener(this);
        lvPioTarget.setOnItemLongClickListener(this);
        lvPioProblem.setOnItemLongClickListener(this);
        addBtn.setOnClickListener(AddPioRecActivity.this);
        goBack.setOnClickListener(AddPioRecActivity.this);
        btnHulicuoshi.setOnClickListener(AddPioRecActivity.this);
        btnHulifangfa.setOnClickListener(AddPioRecActivity.this);
        btnHulizhiyin.setOnClickListener(AddPioRecActivity.this);
        btnHuliyuanyin.setOnClickListener(AddPioRecActivity.this);
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
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_addpio);
        SupportDisplay.resetAllChildViewParam(root);
    }

    /**
     * 长按删除
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
                new Builder(AddPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage(R.string
                        .make_sure_delete)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                problemChoosenList.remove(myPosition);
                                adapterProblem.notifyDataSetChanged();
                                clearOtherAndRefresh();
                            }
                        }).setNegativeButton(R.string.cancel, null).show();
                break;
            case R.id.elv_pio_target:
                new Builder(AddPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage(R
                        .string.make_sure_delete)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mutChooseTargetListTemp.remove(mutChooseTargetList.get(myPosition));
                                mutChooseTargetList.remove(myPosition);
                                adapterTarget.notifyDataSetChanged();
                            }
                        }).setNegativeButton(R.string.cancel, null).show();
                break;
            case R.id.elv_pio_measure:
                new Builder(AddPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage(R
                        .string.make_sure_delete)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mutChooseMeasureListTemp.remove(mutChooseMeasureList.get(myPosition));
                                mutChooseMeasureList.remove(myPosition);
                                adapterMeasure.notifyDataSetChanged();
                            }
                        }).setNegativeButton(R.string.cancel, null).show();
                break;
            case R.id.elv_pio_causes:
                new Builder(AddPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage(R
                        .string.make_sure_delete)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mutChooseCausesListTemp.remove(mutChooseCausesList.get(myPosition));
                                mutChooseCausesList.remove(myPosition);
                                adapterCauses.notifyDataSetChanged();
                            }
                        }).setNegativeButton(R.string.cancel, null).show();
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
     * 加载PIO护理字典
     *
     * @param pioBean
     */
    private void DoPioDictReq(final PioBean pioBean) {
        progressDialog.setMessage(getString(R.string.dataloading));
        progressDialog.show();
        RequestManager.connection(new LoadPioDictRequest(AddPioRecActivity.this, new IRespose<PioBean>() {
            @Override
            public void doResult(PioBean pioBean, int id) {

            }

            @Override
            public void doResult(String result)
                    throws KeyObsoleteException {
                CCLog.i(TAG, getString(R.string.get_pio_success));
                progressDialog.dismiss();
                Gson gson = new Gson();
                Type type = new TypeToken<List<PioItemInfo>>() {
                }.getType();
                List<PioItemInfo> list = gson.fromJson(result, type);
                problemList.clear();
                if (pioBean.getType() == 0) {
                    if (list != null && list.size() > 0) {
                        problemList.addAll(list);
                        ShowSighChooseAlert(problemList);
                        changeBtn(PIO_PROBLEM);
                    }
                }
                if (pioBean.getType() == 1) {
                    if (list != null && list.size() > 0) {
                        mutCausesList.clear();
                        mutCausesList.addAll(list);
                        ShowMutChooseAlert(mutCausesList, 1);
                        changeBtn(PIO_CAUSES);
                    } else {
                        Toast.makeText(AddPioRecActivity.this, R.string.do_not_have_relate_date, Toast
                                .LENGTH_SHORT).show();
                        manualInput(PIO_CAUSES);
                    }
                }
                if (pioBean.getType() == 2) {
                    if (list != null && list.size() > 0) {
                        mutTargetList.clear();
                        mutTargetList.addAll(list);
                        ShowMutChooseAlert(mutTargetList, 2);
                        changeBtn(PIO_TARGET);
                    } else {
                        Toast.makeText(AddPioRecActivity.this, R.string.do_not_have_relate_date, Toast
                                .LENGTH_SHORT).show();
                        manualInput(PIO_TARGET);
                    }
                }
                if (pioBean.getType() == 3) {
                    if (list != null && list.size() > 0) {
                        mutMeasureList.clear();
                        mutMeasureList.addAll(list);
                        ShowMutChooseAlert(mutMeasureList, 3);
                        changeBtn(PIO_MEASURE);
                    } else {
                        Toast.makeText(AddPioRecActivity.this, R.string.do_not_have_relate_date, Toast
                                .LENGTH_SHORT).show();
                        manualInput(PIO_MEASURE);
                    }
                }

            }

            @Override
            public void doException(Exception e, boolean networkState) {
                progressDialog.dismiss();
                CCLog.i(TAG, getString(R.string.get_pio_fail));
                if (networkState) {
                    Toast.makeText(AddPioRecActivity.this, R.string.execute_exception, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    toErrorAct();
                    Toast.makeText(AddPioRecActivity.this, getString(R.string.netstate_content), Toast
                            .LENGTH_SHORT).show();
                }
            }
        }, 0, true, pioBean));

    }

    private void toErrorAct(){
        Intent intent=new Intent();
        intent.setClass(AddPioRecActivity.this, ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 手动输入
     *
     * @param type
     */
    public void manualInput(int type) {
        LayoutInflater inflater = (LayoutInflater) AddPioRecActivity.this.getSystemService
                (LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_pio_alertdilog_edittext_other, null);
        createEditText = (EditText) view.findViewById(R.id.createEv);
        Builder myBuilder = new Builder(AddPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        switch (type) {
            case PIO_PROBLEM:
                changeBtn(type);
                myBuilder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createText = createEditText.getText().toString();
                        if (createText != null && !createText.equals("")) {
                            PioItemInfo info = new PioItemInfo();
                            info.setItemName(createText);
                            info.setItemClass(getString(R.string.pio_item_class_p));
                            currentCode = "";
                            problemChoosenList.clear();
                            problemChoosenList.add(info);
                            adapterProblem.notifyDataSetChanged();
                            clearOtherAndRefresh();
                        }
                    }
                });
                myBuilder.setNegativeButton(R.string.cancel, null);
                secondDialog = myBuilder.create();
                secondDialog.setTitle(getString(R.string.please_input_nursing_problem));
                secondDialog.setView(view);
                secondDialog.show();
                secondDialog.getWindow().setLayout(650, 600);
                break;
            case PIO_CAUSES:
                changeBtn(type);
                myBuilder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createText = createEditText.getText().toString();
                        if (createText != null && !createText.equals("")) {
                            PioItemInfo info = new PioItemInfo();
                            info.setItemName(createText);
                            info.setItemClass(getString(R.string.pio_item_clas_c));
                            mutChooseCausesList.add(info);
                            mutChooseCausesListTemp.add(info);
                            adapterCauses.notifyDataSetChanged();
                        }
                    }
                });
                myBuilder.setNegativeButton(R.string.cancel, null);
                secondDialog = myBuilder.create();
                secondDialog.setTitle(getString(R.string.please_input_nursing_cause));
                secondDialog.setView(view);
                secondDialog.show();
                secondDialog.getWindow().setLayout(650, 600);
                break;
            case PIO_TARGET:
                changeBtn(type);
                myBuilder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createText = createEditText.getText().toString();
                        if (createText != null && !createText.equals("")) {
                            PioItemInfo info = new PioItemInfo();
                            info.setItemName(createText);
                            info.setItemClass(getString(R.string.pio_item_class_i));
                            mutChooseTargetList.add(info);
                            mutChooseTargetListTemp.add(info);
                            adapterTarget.notifyDataSetChanged();
                        }
                    }
                });

                myBuilder.setNegativeButton(R.string.cancel, null);
                secondDialog = myBuilder.create();
                secondDialog.setTitle(getString(R.string.please_input_nursing_target));
                secondDialog.setView(view);
                secondDialog.show();
                secondDialog.getWindow().setLayout(650, 600);
                break;
            case PIO_MEASURE:
                changeBtn(type);
                myBuilder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createText = createEditText.getText().toString();
                        if (createText != null && !createText.equals("")) {
                            PioItemInfo info = new PioItemInfo();
                            info.setItemName(createText);
                            info.setItemClass(getString(R.string.pio_item_class_o));
                            mutChooseMeasureList.add(info);
                            mutChooseMeasureListTemp.add(info);
                            adapterMeasure.notifyDataSetChanged();
                        }
                    }
                });
                myBuilder.setNegativeButton(R.string.cancel, null);
                secondDialog = myBuilder.create();
                secondDialog.setTitle(getString(R.string.please_input_nursing_measure));
                secondDialog.setView(view);
                secondDialog.show();
                secondDialog.getWindow().setLayout(650, 600);
                break;
            default:
                break;
        }
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
        AlertDialog alert = new Builder(AddPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle(R.string.please_choose_nursing_problem).setItems(strs, new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (lastTimeChoose != i) {
                            lastTimeChoose = i;
                            currentCode = list.get(i).getItemCode();
                            problemChoosenList.clear();
                            problemChoosenList.add(list.get(i));
                            adapterProblem.notifyDataSetChanged();
                            clearOtherAndRefresh();
                        }
                    }
                }).setNegativeButton(R.string.cancel, null).create();
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
                        if (mutChooseCausesList.get(z).getItemCode().equals(list.get(i).getItemCode())) {
                            booleans[i] = true;
                        }
                    }
                }
                if (type == 2) {
                    for (int z = 0; z < mutChooseTargetList.size(); z++) {
                        if (mutChooseTargetList.get(z).getItemCode().equals(list.get(i).getItemCode())) {
                            booleans[i] = true;
                        }
                    }
                }
                if (type == 3) {
                    for (int z = 0; z < mutChooseMeasureList.size(); z++) {
                        if (mutChooseMeasureList.get(z).getItemCode().equals(list.get(i).getItemCode())) {
                            booleans[i] = true;
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
            builder = new Builder(AddPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setTitle
                    (getString(R.string.please_choose_relate_item)).setMultiChoiceItems(strs, booleans, null)
                    .setPositiveButton(R.string.make_sure, new
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
                            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

    /**
     * 提交，插入数据库
     */

    private void DoSubmitPioRecordReq(List<PioItemInfo> pclist, List<PioItemInfo> cclist, List<PioItemInfo>
            tclist, List<PioItemInfo> mclist) {
        progressDialog.setMessage(this.getString(R.string.dataloading));
        progressDialog.show();
        String problemCode = pclist.get(0).getItemCode();
        String problemName = pclist.get(0).getItemName();
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
        recordPioBean.setLogTime(logTime);
        CCLog.i(TAG, "addPioRecActivity add>" + UrlConstant.RecordPio() + recordPioBean.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.RecordPio() + recordPioBean.toString(), new
                RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        CCLog.i(TAG, "PIO数据提交成功");
                        Gson gson = new Gson();
                        RecordPioBean recordPioBean = gson.fromJson(result, RecordPioBean.class);
                        if (recordPioBean.getResult().equals("success")) {
                            Toast.makeText(AddPioRecActivity.this, R.string.submit_success, Toast
                                    .LENGTH_SHORT).show();
                            clearAll();
                            finish();
                        } else {
                            Toast.makeText(AddPioRecActivity.this, R.string.submit_fail, Toast .LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressDialog.dismiss();
                        CCLog.i(TAG, "PIO数据提交失败");
                        if (LinstenNetState.isLinkState(getApplicationContext())) {
                            Toast.makeText(AddPioRecActivity.this, R.string.unstable, Toast
                                    .LENGTH_SHORT).show();
                        }else{
                            toErrorAct();
                        }

                    }
                });
    }

    private void judgeIsSubmit() {
        new Builder(AddPioRecActivity.this, AlertDialog.THEME_HOLO_LIGHT).setMessage(R.string
                .is_save_input_message).setPositiveButton(R.string.save, new DialogInterface
                .OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // if (mutChooseCausesList.isEmpty()) {
                // Toast.makeText(
                // AddPioRecActivity.this,
                // "请输入护理致因再提交",
                // Toast.LENGTH_SHORT).show();
                // } else
                if (mutChooseMeasureList.isEmpty()) {
                    Toast.makeText(AddPioRecActivity.this, R.string.please_input_measure_then_submit, Toast
                            .LENGTH_SHORT).show();
                }
                // else if (mutChooseTargetList
                // .isEmpty()) {
                // Toast.makeText(
                // AddPioRecActivity.this,
                // "请输入护理方法再提交",
                // Toast.LENGTH_SHORT).show();
                // }
                else if (problemChoosenList.isEmpty()) {
                    Toast.makeText(AddPioRecActivity.this, R.string.please_input_problem_then_submit, Toast
                            .LENGTH_SHORT).show();
                } else {
                    DoSubmitPioRecordReq(problemChoosenList, mutChooseCausesList, mutChooseTargetList,
                            mutChooseMeasureList);
                }

            }
        }).setNegativeButton(R.string.not_save, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            judgeIsSubmit();
        }
        return false;
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
     * 红外扫描获取的值
     *
     * @param result
     */
    public void DoCameraResult(String result) {

    }
}
