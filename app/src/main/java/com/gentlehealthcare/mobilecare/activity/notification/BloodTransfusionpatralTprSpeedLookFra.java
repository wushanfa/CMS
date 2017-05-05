package com.gentlehealthcare.mobilecare.activity.notification;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.bean.LookBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.RecInspectResultBean;
import com.gentlehealthcare.mobilecare.net.bean.SpeedDateBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.TPRRecordBean;
import com.gentlehealthcare.mobilecare.net.impl.RecInspectDateRequest;
import com.gentlehealthcare.mobilecare.net.impl.SpeedRecordDateRequest;
import com.gentlehealthcare.mobilecare.net.impl.TPRRecordDateRequest;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogCaution;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtn;
import com.gentlehealthcare.mobilecare.view.adapter.GridViewLookAdapter;
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

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Zyy on 2016/2/23.
 * 类说明：通知输血巡视Tpr&Speed&Look界面
 */
@SuppressLint("ValidFragment")
public class BloodTransfusionpatralTprSpeedLookFra extends BaseFragment implements View.OnClickListener {
    /**
     * 主页按钮
     */
    private ImageButton imbtn_home;
    /**
     * 返回按钮
     */
    private ImageButton imbtn_back;
    /**
     * 病人姓名
     */
    private TextView tv_tras_name;
    /**
     * 病人床号
     */
    private TextView tv_tras_bed;
    /**
     * 病人信息
     */
    private SyncPatientBean patient = null;
    /**
     * 当前医嘱id
     */
    private String bloodId = null;
    /**
     * 医嘱数据集合存放一条医嘱
     */
    private List<BloodProductBean2> orders = null;
    private RadioButton rbTpr;
    private RadioButton rbSpeed;
    private RadioButton rbLook;
    /**
     * TPR
     */
    private RelativeLayout rl_tpr;
    private EditText tiwenEt;
    private EditText xintiaoEt;
    private EditText huxiEt;
    /**
     * 滴速
     */
    private RelativeLayout rl_speed;
    private EditText disuEt;

    /**
     * 观察
     */
    private RelativeLayout rl_look;
    private GridViewLookAdapter lookAdapter;

    /**
     * 左侧菜单
     */
    private RadioGroup rgTab;
    /**
     * Tpr确认按钮
     */
    private Button btnSureTpr;
    /**
     * Speed确认按钮
     */
    private Button btnSureSpeed;
    /**
     * Look确认按钮
     */
    private Button btnSureLook;
    /**
     * 数据加载进度条
     */
    private ProgressDialog progressDialog;
    /**
     * 观察中动态按钮内容集合
     */
    public List<LookBean> mLookBeans = null;
    private String playOrderNo = null;
    private GridView gvLook = null;
    private String applyNo = null;
    private String itemNo = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        progressDialog = new ProgressDialog(getActivity());
        getLookObserveDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_blood_tpr_speed_look, null);
        intialSource(view);
        initView(view);
        return view;
    }

    private void intialSource(View view) {

        tv_tras_bed = (TextView) view.findViewById(R.id.tv_tras_bed);
        tv_tras_bed.setText(getString(R.string.bed_label) + patient.getBedLabel());
        tv_tras_name = (TextView) view.findViewById(R.id.tv_tras_name);
        tv_tras_name.setText(getString(R.string.patient_name) + patient.getName());
        //返回&主页按钮
        imbtn_home = (ImageButton) view.findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) view.findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);
    }

    private void getData() {
        patient = (SyncPatientBean) getArguments().getSerializable(GlobalConstant.KEY_PATIENT);
        bloodId = getArguments().getString(GlobalConstant.KEY_BLOODID);
        playOrderNo = getArguments().getString(GlobalConstant.KEY_PLANORDERNO);
        applyNo = getArguments().getString(GlobalConstant.KEY_APPLYNO);
        itemNo = getArguments().getString(GlobalConstant.KEY_ITEMNO);
    }

    /**
     * 初始化界面资源
     */
    private void initView(final View view) {

        rgTab = (RadioGroup) view.findViewById(R.id.rg_tab_list);
        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbtn_tpr:
                        btnSureLook.setVisibility(View.GONE);
                        rbTpr.setBackground(getResources().getDrawable(R.drawable.check_txt_bg));
                        rbLook.setBackgroundColor(getResources().getColor(R.color.grey_200));
                        rbSpeed.setBackgroundColor(getResources().getColor(R.color.grey_200));
                        rl_look.setVisibility(View.GONE);
                        rl_speed.setVisibility(View.GONE);
                        rl_tpr.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbtn_speed:
                        btnSureLook.setVisibility(View.GONE);
                        rbSpeed.setBackground(getResources().getDrawable(R.drawable.check_txt_bg));
                        rbLook.setBackgroundColor(getResources().getColor(R.color.grey_200));
                        rbTpr.setBackgroundColor(getResources().getColor(R.color.grey_200));
                        rl_look.setVisibility(View.GONE);
                        rl_tpr.setVisibility(View.GONE);
                        rl_speed.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbtn_look:
                        btnSureLook.setVisibility(View.VISIBLE);
                        rbSpeed.setBackgroundColor(getResources().getColor(R.color.grey_200));
                        rbTpr.setBackgroundColor(getResources().getColor(R.color.grey_200));
                        rbLook.setBackground(getResources().getDrawable(R.drawable.check_txt_bg));
                        rl_speed.setVisibility(View.GONE);
                        rl_tpr.setVisibility(View.GONE);
                        rl_look.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
        rbTpr = (RadioButton) view.findViewById(R.id.rbtn_tpr);
        rbSpeed = (RadioButton) view.findViewById(R.id.rbtn_speed);
        rbLook = (RadioButton) view.findViewById(R.id.rbtn_look);
        rl_tpr = (RelativeLayout) view.findViewById(R.id.rl_tpr);
        rl_speed = (RelativeLayout) view.findViewById(R.id.rl_speed);
        rl_look = (RelativeLayout) view.findViewById(R.id.rl_look);
        gvLook = (GridView) view.findViewById(R.id.gv_look);

        // 体温
        tiwenEt = (EditText) view.findViewById(R.id.tv_item_fyh_type_);
        tiwenEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (stringPasFloat(tiwenEt.getText().toString().trim()) > 40.0 || stringPasFloat(tiwenEt.getText
                        ().toString().trim()) < 0.0) {
                    tiwenEt.setTextColor(getResources().getColor(R.color.three_eight_red));
                } else {
                    tiwenEt.setTextColor(getResources().getColor(R.color.black_text_content));
                }
            }
        });
        // 心跳
        xintiaoEt = (EditText) view.findViewById(R.id.tv_item_fyh_type_2);
        xintiaoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (stringPasFloat(xintiaoEt.getText().toString().trim()) > 170.0 || stringPasFloat(tiwenEt
                        .getText().toString().trim()) < 0.0) {
                    xintiaoEt.setTextColor(getResources().getColor(R.color.three_eight_red));
                } else {
                    xintiaoEt.setTextColor(getResources().getColor(R.color.black_text_content));
                }
            }
        });
        // 呼吸
        huxiEt = (EditText) view.findViewById(R.id.tv_item_fyh_type_3);
        huxiEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (stringPasFloat(huxiEt.getText().toString().trim()) > 50.0 || stringPasFloat(tiwenEt.getText
                        ().toString().trim()) < 0.0) {
                    huxiEt.setTextColor(getResources().getColor(R.color.three_eight_red));
                } else {
                    huxiEt.setTextColor(getResources().getColor(R.color.black_text_content));
                }
            }
        });
        // 滴速
        disuEt = (EditText) view.findViewById(R.id.tv_item_fyh_type_4);
        btnSureTpr = (Button) view.findViewById(R.id.tv_sure_tpr);
        btnSureTpr.setOnClickListener(this);
        btnSureSpeed = (Button) view.findViewById(R.id.tv_sure_speed);
        btnSureSpeed.setOnClickListener(this);
        btnSureLook = (Button) view.findViewById(R.id.btn_look_save);
        btnSureLook.setOnClickListener(this);
    }

    /**
     * String转换为Float
     */
    private float stringPasFloat(String str) {
        if (str.length() > 1) {
            return Float.valueOf(str);
        } else {
            return 0;
        }
    }

    /**
     * 保存TPR信息 需要传递的参数： 1.username//用户名 2.patId//病人id 3.planOrderNo//医嘱执行任务id
     * 4.temperature//体温 5.pulse//脉搏 6.respire//呼吸
     */
    private void saveTPRDate() {
        progressDialog.setMessage("正在保存..");
        progressDialog.show();
        TPRRecordBean tprBean = new TPRRecordBean();
        tprBean.setPatId(patient.getPatId());
        tprBean.setPlanOrderNo(playOrderNo);
        tprBean.setPulse(xintiaoEt.getText().toString());
        tprBean.setRespire(huxiEt.getText().toString());
        tprBean.setTemperature(tiwenEt.getText().toString());
        tprBean.setUsername(UserInfo.getUserName());

        RequestManager.connection(new TPRRecordDateRequest(getActivity(),
                new IRespose<TPRRecordBean>() {

                    @Override
                    public void doResult(String result)
                            throws KeyObsoleteException {
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        TPRRecordBean tpr = gson.fromJson(result,
                                TPRRecordBean.class);
                        if (tpr.isResult()) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.savesuccessed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.save_fail),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void doResult(TPRRecordBean t, int id) {

                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        progressDialog.dismiss();
                        if (networkState) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.unstable),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }


                    }
                }, 0, true, tprBean));
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    /**
     * 获得观察dict的值
     */
    private void getLookObserveDate() {
        progressDialog.setMessage(getResources().getString(R.string.loadingdata));
        CCLog.i("获取观察项目>>>" + UrlConstant.GetLookObserve());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.GetLookObserve(), new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                progressDialog.dismiss();
                if (LinstenNetState.isLinkState(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                            .LENGTH_SHORT).show();
                } else {
                    toErrorAct();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                JSONObject mJsonObject = null;
                JSONArray mJsonArray = null;
                boolean responseState = false;
                try {
                    mJsonObject = new JSONObject(arg0.result);
                    responseState = mJsonObject.getBoolean("result");
                    if (responseState) {
                        progressDialog.dismiss();
                        mJsonArray = mJsonObject.getJSONArray("dict");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<LookBean>>() {
                        }.getType();
                        mLookBeans = gson.fromJson(mJsonArray.toString(), type);
                        lookAdapter = new GridViewLookAdapter(getActivity(), mLookBeans);
                        gvLook.setAdapter(lookAdapter);
                        gvLook.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mLookBeans.get(position).setIsSelected(true);
                                lookAdapter.notifyDataSetChanged();
                            }
                        });
                        gvLook.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                                    id) {
                                mLookBeans.get(position).setIsSelected(false);
                                lookAdapter.notifyDataSetChanged();
                                return true;
                            }
                        });
                    } else {
                        mLookBeans = null;
                        progressDialog.dismiss();
                        ShowToast(getResources().getString(R.string.dataexception));
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    if (LinstenNetState.isLinkState(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                .LENGTH_SHORT).show();
                    } else {
                        toErrorAct();
                    }
                }

            }
        });
    }

    /**
     * 保存输血速度的数值
     */
    private void saveSpeedDate() {
        progressDialog.setMessage(getResources().getString(R.string.saveing));
        progressDialog.show();

        SpeedDateBean speed = new SpeedDateBean();
        speed.setPatId(patient.getPatId());
        speed.setPlanOrderNo(playOrderNo);
        speed.setSpeed(disuEt.getText().toString());
        speed.setUsername(UserInfo.getUserName());

        RequestManager.connection(new SpeedRecordDateRequest(getActivity(),
                new IRespose<SpeedDateBean>() {

                    @Override
                    public void doResult(String result)
                            throws KeyObsoleteException {
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        SpeedDateBean spe = gson.fromJson(result,
                                SpeedDateBean.class);
                        if (spe.isResult()) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.savesuccessed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.save_fail),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void doResult(SpeedDateBean t, int id) {
                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        progressDialog.dismiss();
                        if (networkState){
                            toErrorAct();
                        }else{
                            Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }

                    }
                }, 0, true, speed));
    }

    /**
     * 记录观察问题数据
     *
     * @param status
     * @param itemCode
     * @param dosage
     * @param otherDesc
     */
    private void recInspectResult(final String status, String itemCode,
                                  String dosage, String otherDesc) {
        RecInspectResultBean rec = new RecInspectResultBean();

        rec.setUsername(UserInfo.getUserName());
        rec.setPatId(patient.getPatId());
        rec.setPlanOrderNO(playOrderNo);
        rec.setItemCode(itemCode);
        rec.setStatus(status);
        rec.setHandleText("停止输血，通知医师");
        rec.setBloodId(bloodId);
        if (otherDesc != null) {
            rec.setOtherDesc(otherDesc);
        }
        if (dosage != null) {
            rec.setDosage(dosage);
        }

        RequestManager.connection(new RecInspectDateRequest(getActivity(),
                new IRespose<RecInspectResultBean>() {

                    @Override
                    public void doResult(String result)
                            throws KeyObsoleteException {
                        Gson gson = new Gson();
                        RecInspectResultBean recResult = gson.fromJson(result,
                                RecInspectResultBean.class);
                        if (recResult.isResult()) {
                            CCLog.i("recResult.isResult()>>>" + recResult.getMsg());
                            Toast.makeText(getActivity(), recResult.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            CCLog.i("!recResult.isResult()>>>" + recResult.getMsg());
                            Toast.makeText(getActivity(), recResult.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void doResult(RecInspectResultBean t, int id) {

                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                        if (networkState){
                            toErrorAct();
                        }else{
                            Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }
                    }
                }, 0, true, rec));
    }

    /**
     * 观察问题的dialog
     */
    private void showAlertDialog(final String str) {
        final AlertDialogTwoBtn alertDialogTwoBtn = new AlertDialogTwoBtn(getActivity());
        alertDialogTwoBtn.setTitle("输血反应");
        alertDialogTwoBtn.setMessage("是否继续输血");
        alertDialogTwoBtn.setLeftButton("否", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 异常停止输血
                showInputDialog(str);
                alertDialogTwoBtn.dismiss();
            }
        });
        alertDialogTwoBtn.setRightButton("是", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                recInspectResult("1", str, null, null);
                lookAdapter.notifyDataSetChanged();
                alertDialogTwoBtn.dismiss();
            }
        });
        alertDialogTwoBtn.show();
    }

    /**
     * 观察问题输入输血量的dialog
     *
     * @param itemCode
     */
    private void showInputDialog(final String itemCode) {
        final AlertDialogCaution mCautionDialog = new AlertDialogCaution(getActivity());
        mCautionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCautionDialog.show();
        final EditText edt = (EditText) mCautionDialog
                .findViewById(R.id.et_caution);
        Button confirm = (Button) mCautionDialog.findViewById(R.id.btn_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                recInspectResult("-1", itemCode, edt.getText().toString(), null);
                //异常结束输血
                compliteTransfusionNew("-1", edt.getText().toString(), playOrderNo, bloodId, applyNo, itemNo);
                lookAdapter.notifyDataSetChanged();
                mCautionDialog.dismiss();
            }
        });

    }

    /**
     * 完成输血 新接口
     *
     * @param status:1表示正常结束 -1表示异常结束
     * @param dosage:异常结束输入量 正常结束可传null
     */
    private void compliteTransfusionNew(String status, String dosage, String planOrderNo, String bloodId, String applyNo, String itemNo) {
        CCLog.i("完成输血>>>", UrlConstant.CompliteTransfusion() + UserInfo.getUserName() + "&patId=" + patient
                .getPatId() + "&applyNo=" + applyNo + "&itemNo=" + itemNo + "&bloodId=" + bloodId + "&planOrderNo=" +
                planOrderNo + "&status=" + status + "&dosage=" + dosage);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                UrlConstant.CompliteTransfusion() + UserInfo.getUserName() + "&patId=" + patient
                        .getPatId() + "&applyNo=" + applyNo + "&itemNo=" + itemNo + "&bloodId=" + bloodId + "&planOrderNo=" +
                        planOrderNo + "&status=" + status + "&dosage=" + dosage,
                new RequestCallBack<String>() {
                    JSONObject mJsonObject = null;
                    boolean result = false;
                    JSONArray jsonArray = null;

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if (LinstenNetState.isLinkState(getActivity())) {
                            Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        }else{
                            toErrorAct();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        try {
                            mJsonObject = new JSONObject(arg0.result);
                            result = mJsonObject.getBoolean("result");
                            if (result) {
                                ShowToast("系统已结束输血");
                                getActivity().finish();
                            }

                        } catch (JSONException e) {
                            if (LinstenNetState.isLinkState(getActivity())) {
                                Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                        .LENGTH_SHORT).show();
                            }else{
                                toErrorAct();
                            }
                        }
                    }
                });
    }

    @Override
    protected void resetLayout(View view) {
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root_fra_tpr_sp_look);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imbtn_back:
                Intent intent = new Intent();
                intent.setClass(getActivity(), Notification2Activity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.imbtn_home:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), HomeAct.class);
                startActivity(intent2);
                getActivity().finish();
                break;
            case R.id.tv_sure_tpr:
                if (TextUtils.isEmpty(huxiEt.getText())) {
                    ShowToast(getResources().getString(R.string.inputrespiration));
                    return;
                }
                if (TextUtils.isEmpty(xintiaoEt.getText())) {
                    ShowToast(getResources().getString(R.string.inputpulsvalue));
                    return;
                }
                if (TextUtils.isEmpty(tiwenEt.getText())) {
                    ShowToast(getResources().getString(R.string.inputtemperaturvalue));
                    return;
                }
                saveTPRDate();
                break;
            case R.id.tv_sure_speed:
                if (TextUtils.isEmpty(disuEt.getText())) {
                    ShowToast(getResources().getString(R.string.inputspeed));
                } else {
                    saveSpeedDate();
                }
                break;
            case R.id.btn_look_save:
                bloodLookSure();
                break;
            default:
                break;
        }
    }

    private void bloodLookSure() {
        if (!mLookBeans.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mLookBeans.size(); i++) {
                if (mLookBeans.get(i).isSelected()) {
                    sb.append(mLookBeans.get(i).getItemCode() + ",");
                }
            }
            if (sb.length() > 1) {
                showAlertDialog(sb.toString());
            } else {
                ShowToast("患者输血正常");
            }

        } else {
            ShowToast(getResources().getString(R.string.dataexception));
        }
    }
}
