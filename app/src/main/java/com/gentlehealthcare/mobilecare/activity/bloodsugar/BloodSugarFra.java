package com.gentlehealthcare.mobilecare.activity.bloodsugar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BSResultItemBean;
import com.gentlehealthcare.mobilecare.net.bean.BloodSugarItemBean;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.net.bean.bloodSugarBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Zyy
 * @date 2015-9-18下午2:30:51
 * @category 血糖测试值界面
 */
@SuppressLint("ValidFragment")
public class BloodSugarFra extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "BloodSugarFra";
    /**
     * 待执行按钮
     */
    RadioButton rb_wait;
    /**
     * 执行中按钮
     */
    RadioButton rb_excuteing;
    /**
     * 执行按钮
     */
    private Button btn_left_;
    /**
     * 扫描按钮
     */
    private Button btn_right_;
    /**
     * 病人ID
     */
    private String mpatId = null;
    /**
     * 加载条
     */
    private ProgressDialog progressDialog = null;
    /**
     * 同步病人数据
     */
    private SyncPatientBean patient = null;

    /**
     * 巡视 or 扫描 false:待执行 true:执行中
     */
    private boolean Flag = false;

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
     * 项目下拉框
     */
    private Spinner sp_xm;
    /**
     * 血糖值
     */
    private EditText edt_value = null;
    /**
     * 项目adapter
     */
    private ArrayAdapter<String> adapterXm;
    /**
     * 异常信息
     */
    private TextView tv_exception_content1, tv_exception_content2, tv_exception_content3;
    private LinearLayout ll_exception;
    /**
     * 正常信息
     */
    private TextView tv_normal_content1, tv_normal_content2, tv_normal_content3;
    private LinearLayout ll_normal;
    /**
     * 是否是返回键
     */
    private boolean isBack;
    /**
     * 血成分
     */
    private String[] xm;
    private List<BloodSugarItemBean> temp = new ArrayList<BloodSugarItemBean>();
    private BSResultItemBean itemBean = new BSResultItemBean();
    private String itemCode = "";
    private bloodSugarBean bsBean = new bloodSugarBean();

    private String xtplanOrderNo = null;

    public BloodSugarFra(SyncPatientBean patient, String xtplanOrderNo) {
        super();
        this.patient = patient;
        this.xtplanOrderNo = xtplanOrderNo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_bloodsugar, null);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载数据,请稍后..");
        progressDialog.show();
        btn_right_ = (Button) view.findViewById(R.id.btn_right_);
        btn_right_.setOnClickListener(this);
        edt_value = (EditText) view.findViewById(R.id.edt_value);
        sp_xm = (Spinner) view.findViewById(R.id.sp_xm);
        tv_exception_content1 = (TextView) view.findViewById(R.id.tv_exception_content1);
        tv_exception_content2 = (TextView) view.findViewById(R.id.tv_exception_content2);
        tv_exception_content3 = (TextView) view.findViewById(R.id.tv_exception_content3);
        ll_exception = (LinearLayout) view.findViewById(R.id.ll_exception);
        tv_normal_content1 = (TextView) view.findViewById(R.id.tv_normal_content1);
        tv_normal_content2 = (TextView) view.findViewById(R.id.tv_normal_content1);
        tv_normal_content3 = (TextView) view.findViewById(R.id.tv_normal_content1);
        ll_normal = (LinearLayout) view.findViewById(R.id.ll_normal);

//        temp = (List<BloodSugarItemBean>) getActivity().getIntent().getSerializableExtra("itemList");
        getItem();

        // 添加事件Spinner事件监听
        sp_xm.setOnItemSelectedListener(new SpinnerXmSelectedListener());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        btn_right_ = (Button) getActivity().findViewById(R.id.btn_right_);
        btn_right_.setOnClickListener(this);
        btn_left_ = (Button) getActivity().findViewById(R.id.btn_left_);
        btn_left_.setOnClickListener(this);
        tv_tras_bed = (TextView) getActivity().findViewById(R.id.tv_tras_bed);
        if (patient.getBedLabel() != null) {
            tv_tras_bed.setText("床号：" + patient.getBedLabel());
        } else {
            tv_tras_bed.setText(getResources().getString(R.string.bed_label) + Html.fromHtml
                    ("<small>未分配</small>"));
        }
        tv_tras_name = (TextView) getActivity().findViewById(R.id.tv_tras_name);
        tv_tras_name.setText("姓名：" + patient.getName());

        // 返回&主页按钮
        imbtn_home = (ImageButton) getActivity().findViewById(R.id.imbtn_home);
        imbtn_back = (ImageButton) getActivity().findViewById(R.id.imbtn_back);
        imbtn_back.setOnClickListener(this);
        imbtn_home.setOnClickListener(this);

    }

    @Override
    protected void resetLayout(View view) {
        RelativeLayout root = (RelativeLayout) view
                .findViewById(R.id.root_fra_bloodsugar);
        SupportDisplay.resetAllChildViewParam(root);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_right_:
                if (! isBack) {
                    if (edt_value.getText().toString().length() >= 1) {
                        UpLoadBloodSugra();//上传血糖值
                    } else {
                        ShowToast("请输入血糖值");
                    }
                } else {
                    getActivity().finish();
                }
                break;
            case R.id.imbtn_back:
                // 身份核对界面
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container,
                                new BloodSugarIdentifyScanFra(patient)).commit();
                break;
            case R.id.imbtn_home:
                getActivity().startActivity(
                        new Intent(getActivity(), HomeAct.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.in_or_out_static,
                        R.anim.slide_out_right);
                break;
            default:
                break;
        }

    }

    /**
     * 上传血糖值
     */
    private void UpLoadBloodSugra() {
        // UrlConstant.UpLoadThreeEight()
        CCLog.i(TAG, "上传血糖结果》" + UrlConstant.CheckBloodTestVal() + "?username="
                + UserInfo.getUserName() + "&patId=" + patient.getPatId()
                + "&planOrderNo=" + xtplanOrderNo + "&glucoseVal=" + edt_value.getText().toString() +
                "&itemCode=" + itemCode);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.CheckBloodTestVal() + "?username=" + UserInfo
                        .getUserName() + "&patId=" + patient.getPatId() + "&planOrderNo=" + xtplanOrderNo +
                        "&glucoseVal=" + edt_value.getText().toString() + "&itemCode=" + itemCode,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if (LinstenNetState.isLinkState(getActivity())) {
                            Toast.makeText(getActivity(), getString(R.string.unstable), Toast.LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                        getActivity().finish();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ShowToast("血糖保存成功");
                        String result = arg0.result;
                        Gson gson = new Gson();
                        Type type = new TypeToken<bloodSugarBean>() {
                        }.getType();
                        bsBean = gson.fromJson(result, type);
                        if (bsBean.isResult()) {
                            if (bsBean.isFlag()) {
                                ll_normal.setVisibility(View.VISIBLE);
//                                tv_normal_content1.setText("上限：" + bsBean.getHig());
//                                tv_normal_content2.setText("下限：" + bsBean.getLow());
                                tv_normal_content3.setText("结果：" + bsBean.getDesc());
                                edt_value.setKeyListener(null);
                                btn_right_.setText("返 回");
                                isBack = true;
                            } else {
                                ll_exception.setVisibility(View.VISIBLE);
//                                tv_exception_content1.setText("上限：" + bsBean.getHig());
//                                tv_exception_content2.setText("下限：" + bsBean.getLow());
                                tv_exception_content3.setText("结果：" + bsBean.getDesc());
                                btn_right_.setText("返 回");
                                edt_value.setKeyListener(null);
                                isBack = true;
                            }
                        } else {
                            ShowToast("血糖保存失败");
                        }
                    }
                });
    }

    /**
     * 获取血糖项目
     */
    private void getItem() {
        CCLog.i(TAG, "获得血糖选项:->" + UrlConstant.BloodSugarGetItem());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlConstant.BloodSugarGetItem(),
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        Type type = new TypeToken<BSResultItemBean>() {
                        }.getType();
                        itemBean = gson.fromJson(result, type);
                        if (itemBean.isResult()) {
                            temp = itemBean.getData();
                            CCLog.i(TAG, itemBean.getMsg());
                            xm = new String[temp.size()];
                            int i = 0;
                            for (BloodSugarItemBean bean : temp) {
                                xm[i] = bean.getItemName();
                                i++;
                            }
                            // 将可选内容与ArrayAdapter连接起来
                            adapterXm = new ArrayAdapter<String>(getActivity(), R.layout
                                    .simple_spinner_item_black, xm);
                            // 设置下拉列表的风格
                            adapterXm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // 将adapter 添加到spinner中
                            sp_xm.setAdapter(adapterXm);
                        } else {
                            CCLog.i(TAG, "血糖列表加载失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        ShowToast("请求异常");
                        if (LinstenNetState.isLinkState(getActivity())) {
                            Toast.makeText(getActivity(), getString(R.string.unstable), Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            toErrorAct();
                        }
                    }
                });
    }

    private void toErrorAct() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ErrorAct.class);
        startActivity(intent);
    }

    // 配血下拉列表监听器
    class SpinnerXmSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                   final int position, long arg3) {
            itemCode = temp.get(position).getItemCode();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}
