package com.gentlehealthcare.mobilecare.activity.patient.trans;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.activity.ErrorAct;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.bean.RecBean;
import com.gentlehealthcare.mobilecare.bean.ThreeEightCheckBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.listener.ScanWanDai;
import com.gentlehealthcare.mobilecare.listener.ScanXiaoQi;
import com.gentlehealthcare.mobilecare.listener.ScanXiaoQi.OnTestListening;
import com.gentlehealthcare.mobilecare.listener.ScanXueDaiHao;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.gentlehealthcare.mobilecare.view.AlertDialogTwoBtn;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zyy
 * @category 查8对界面
 * @date 2015-9-15下午3:22:08
 */
@SuppressLint("ValidFragment")
public class ThreeEightCheckFragment extends BaseFragment implements OnClickListener {

  private static final String TAG = "ThreeEightCheckFragment";
  /**
   * 效期按钮
   */
  private Button btn_xiaoqi;
  /**
   * 质量按钮
   */
  private Spinner btn_zhiliang;
  /**
   * 包装按钮
   */
  private Spinner btn_baozhuang;
  /**
   * 姓名按钮
   */
  private Button btn_name;
  /**
   * 床号按钮
   */
  private Button btn_bed;
  /**
   * 病历号按钮
   */
  private Button btn_bingli;
  /**
   * 血袋按钮
   */
  private Button btn_xuedai;
  /**
   * 血型按钮
   */
  private Button btn_xuexing;
  /**
   * 血品按钮
   */
  private Button btn_xuepin;
  /**
   * 配血按钮
   */
  private Spinner btn_peixue;
  /**
   * 血量按钮
   */
  private Button btn_xueliang;
  /**
   * 确认按钮
   */
  private Button btn_sure;
  /**
   * 效期标识
   */
  private Boolean flag_xq = false;
  /**
   * 质量标识
   */
  private Boolean flag_zl = false;
  /**
   * 包装标识
   */
  private Boolean flag_bz = false;
  /**
   * 姓名标识
   */
  private Boolean flag_xm = false;
  /**
   * 床号标识
   */
  private Boolean flag_ch = false;
  /**
   * 病历号标识
   */
  private Boolean flag_blh = false;
  /**
   * 血袋号标识
   */
  private Boolean flag_xdh = false;
  /**
   * 血型
   */
  private Boolean flag_xx = false;
  /**
   * 配血结果标识
   */
  private Boolean flag_jg = false;
  /**
   * 血量标识
   */
  private Boolean flag_xl = false;
  /**
   * 血品标识
   */
  private Boolean flag_xp = false;
  /**
   * 姓名正确与否
   */
  private Boolean yn_name = false;
  /**
   * 床号正确与否
   */
  private Boolean yn_bed = false;
  /**
   * id正确与否
   */
  private Boolean yn_id = false;
  /**
   * 血袋号正确与否
   */
  private Boolean yn_xdh = false;
  /**
   * 血型正确与否
   */
  private Boolean yn_xx = false;
  /**
   * 血成分正确与否
   */
  private Boolean yn_xcf = false;
  /**
   * 血量正确与否
   */
  private Boolean yn_xl = false;
  /**
   * 输入框
   */
  private Dialog currentDialog = null;
  /**
   * 3查8对Bean
   */
  private ThreeEightCheckBean mThreeEightCheckBean = null;

  /**
   * 过期提示框
   */
  private AlertDialogOneBtn alertDialog = null;
  /**
   * 同步病人数据
   */
  private SyncPatientBean patient = null;
  private String mStr = null;
  /**
   * 质量
   */
  private static final String[] zl = {"合 格", "不合格"};
  /**
   * 包装
   */
  private static final String[] bz = {"完 好", "破 损"};
  /**
   * 血成分
   */
  private static final String[] xcf = {"白细胞全血", "悬浮红细胞", "洗涤红细胞"};
  /**
   * 配血结果
   */
  private static final String[] px = {"相 符", "不 符"};
  /**
   * 包装adapter
   */
  private ArrayAdapter<String> adapterBz;
  /**
   * 质量adapter
   */
  private ArrayAdapter<String> adapterZl;
  /**
   * 血品adapter
   */
  private ArrayAdapter<String> adapterXp;
  /**
   * 配血adapter
   */
  private ArrayAdapter<String> adapterPx;
  TextView spinnertxText;
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
   * 血品信息
   */
  private BloodProductBean2 mBloodProductBean2;
  /**
   * 效期扫描监听器
   */
  ScanXiaoQi scanXiaoQi = null;
  /**
   * 血袋号扫描监听器
   */
  ScanXueDaiHao scanXueDaiHao = null;
  /**
   * 腕带扫描监听器
   */
  ScanWanDai scanWanDai = null;

  private String eventId = "";

  public TransfusionActivity transfusionActivity;
  private String reqId;
  private TextView tv_memo;

  public ThreeEightCheckFragment() {}

  public ThreeEightCheckFragment(SyncPatientBean patient, BloodProductBean2 mProductBean2,
      String reqId) {
    super();
    this.patient = patient;
    this.mBloodProductBean2 = mProductBean2;
    this.reqId = reqId;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null && getArguments().containsKey("eventId")) {
      eventId = getArguments().getString("eventId");
    }
    mThreeEightCheckBean = new ThreeEightCheckBean();
    // 监听扫描有效期
    // XiaoQiListener();
    // 监听扫描血袋号
    // XueDaiHaoListener();
    // 监听腕带扫描
    // WanDaiListener();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tranfusion_three_eight_check, null);
    // 初始化UI组件
    initializationModule(view);
    setBloodInfo(mBloodProductBean2);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onPause() {
    super.onPause();
    // 当用户离开此页面时，关闭自定义监听器为了腾出更多内存
    if (scanWanDai != null) {
      scanWanDai.stop();
    }
    if (scanXiaoQi != null) {
      scanXiaoQi.stop();
    }
    if (scanXueDaiHao != null) {
      scanXueDaiHao.stop();
    }
  }

  @Override
  protected void resetLayout(View view) {
    RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root_fra_tras_check);
    SupportDisplay.resetAllChildViewParam(root);
  }

  private void setBloodInfo(BloodProductBean2 bloodProductBean2) {
    String invalDate = bloodProductBean2.getBloodInvalDate().substring(0, 16);
    long serviceTme = DateTool.parseDate(1, GlobalConstant.SERVICE_TIME);
    long invalTime = DateTool.parseDate(1, invalDate);
    if (invalTime < serviceTme) { // 修改平哥发现的BUG 避免本地pda可能没电或时间被修改
      // 已过期
      mThreeEightCheckBean.setEffective("0");
      mThreeEightCheckBean.setEffectiveCheckTime(DateTool.getCurrDateTime());
      btn_xiaoqi.setText(mBloodProductBean2.getBloodInvalDate().substring(0, 16));
      btn_xiaoqi.setBackgroundResource(R.drawable.three_eight_red);
      btn_xiaoqi.setTextColor(this.getResources().getColor(R.color.white));
      alertDialog = new AlertDialogOneBtn(getActivity());
      alertDialog.setTitle("提示");
      alertDialog.setMessage("血品已过期，请退回血库");
      alertDialog.setButton("确定");
      alertDialog.setButtonListener(true, new OnClickListener() {
        @Override
        public void onClick(View v) {
          // 调用记录rec接口
          RecBean recBean = new RecBean();
          recBean.setPlanOrderNo(mBloodProductBean2.getPlanOrderNo());
          recBean.setPatId(patient.getPatId());
          recBean.setLogBy(UserInfo.getUserName());
          recBean.setWardCode(UserInfo.getWardCode());
          if (!TextUtils.isEmpty(mBloodProductBean2.getBloodGroupDesc())
              && !TextUtils.isEmpty(mBloodProductBean2.getBloodDonorCode())
              && !TextUtils.isEmpty(mBloodProductBean2.getBloodInvalDate())) {
            recBean.setPerformDesc(mBloodProductBean2.getBloodTypeName() + "||"
                + mBloodProductBean2.getBloodDonorCode() + "||"
                + mBloodProductBean2.getBloodInvalDate());
          } else {
            recBean.setPerformDesc("3查8对核对错误,血袋已过期");
          }
          recBean.setPerformType("记录");
          validityRec(recBean);
          alertDialog.dismiss();
          getActivity().finish();
        }
      });
    } else {
      mThreeEightCheckBean.setEffective("1");
      mThreeEightCheckBean.setEffectiveCheckTime(DateTool.getCurrDateTime());
      btn_xiaoqi.setTextSize(16);
      btn_xiaoqi.setText(mBloodProductBean2.getBloodInvalDate().substring(0, 16));
      btn_xiaoqi.setBackgroundResource(R.drawable.three_eight_green_long);
      btn_xiaoqi.setTextColor(this.getResources().getColor(R.color.white));
    }

    if (patient.getBedLabel() != null) {
      tv_tras_bed.setText("床号：" + patient.getBedLabel());
    } else {
      tv_tras_bed.setText(
          getResources().getString(R.string.bed_label) + Html.fromHtml("<small>未分配</small>"));
    }
    tv_tras_name.setText("姓名：" + patient.getName());
    imbtn_back.setOnClickListener(this);
    imbtn_home.setOnClickListener(this);

    // 姓名
    btn_name.setText(patient.getName());
    btn_name.setTextColor(getActivity().getResources().getColor(R.color.white));
    btn_name.setBackgroundResource(R.drawable.three_eight_green_long);
    mThreeEightCheckBean.setName("1");
    mThreeEightCheckBean.setNameCheckTime(DateTool.getCurrDateTime());
    // 床号
    btn_bed.setText(patient.getBedLabel() + "床");
    btn_bed.setTextColor(getActivity().getResources().getColor(R.color.white));
    btn_bed.setBackgroundResource(R.drawable.three_eight_green_long);
    mThreeEightCheckBean.setBedLabel("1");
    mThreeEightCheckBean.setBedLabelCheckTime(DateTool.getCurrDateTime());
    // 病历号
    btn_bingli.setText(patient.getPatId());
    btn_bingli.setTextColor(getActivity().getResources().getColor(R.color.white));
    btn_bingli.setBackgroundResource(R.drawable.three_eight_green_long);
    mThreeEightCheckBean.setPatCode("1");
    mThreeEightCheckBean.setPatCodeCheckTime(DateTool.getCurrDateTime());
    btn_bingli.setBackgroundResource(R.drawable.three_eight_green_long);

    btn_xuedai.setText(bloodProductBean2.getBloodDonorCode());
    btn_xuedai.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    btn_xuedai.setTextColor(getActivity().getResources().getColor(R.color.white));
    btn_xuedai.setBackgroundResource(R.drawable.three_eight_green_long);
    if (TextUtils.isEmpty(bloodProductBean2.getUnit())) {
      btn_xueliang.setText(bloodProductBean2.getBloodCapacity());
    } else {
      btn_xueliang.setText(bloodProductBean2.getBloodCapacity() + bloodProductBean2.getUnit());
    }
    btn_xueliang.setBackgroundResource(R.drawable.three_eight_green_long);
    btn_xueliang.setTextColor(getActivity().getResources().getColor(R.color.white));
    mThreeEightCheckBean.setBloodCapacity("1");
    mThreeEightCheckBean.setBloodCapacityCheckTime(DateTool.getCurrDateTime());

    btn_xuepin.setText(bloodProductBean2.getBloodTypeName());
    btn_xuepin.setBackgroundResource(R.drawable.three_eight_green_long);
    btn_xuepin.setTextColor(getActivity().getResources().getColor(R.color.white));
    mThreeEightCheckBean.setBloodType("1");
    mThreeEightCheckBean.setBloodTypeCheckTime(DateTool.getCurrDateTime());

    btn_xuexing.setText(bloodProductBean2.getBloodGroupDesc());
    btn_xuexing.setBackgroundResource(R.drawable.three_eight_green_long);
    btn_xuexing.setTextColor(getActivity().getResources().getColor(R.color.white));
    mThreeEightCheckBean.setBloodGroup("1");
    mThreeEightCheckBean.setBloodGroupCheckTime(DateTool.getCurrDateTime());

    mThreeEightCheckBean.setBloodIdCheckTime(DateTool.getCurrDateTime());
    mThreeEightCheckBean.setBloodId("1");
    mThreeEightCheckBean.setPatId(patient.getPatId());
    mThreeEightCheckBean.setHandling("1");
    mThreeEightCheckBean.setUsername(UserInfo.getUserName());
    mThreeEightCheckBean.setPlanOrderNo(mBloodProductBean2.getPlanOrderNo());
    mThreeEightCheckBean.setBloodStatus("1");
    mThreeEightCheckBean.setLogId(mBloodProductBean2.getLogId());
    if (!TextUtils.isEmpty(bloodProductBean2.getMemo())) {
      tv_memo.setText(bloodProductBean2.getMemo());
    }
  }

  /**
   * 初始化界面组件
   */
  private void initializationModule(View view) {

    spinnertxText = (TextView) view.findViewById(R.id.text1);

    /****************** 有效期 ***********************/
    btn_xiaoqi = (Button) view.findViewById(R.id.btn_xiaoqi);
    btn_xiaoqi.setOnClickListener(this);
    /****************** 质量 ***********************/
    btn_zhiliang = (Spinner) view.findViewById(R.id.btn_zhiliang);
    adapterZl = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, zl);
    adapterZl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    btn_zhiliang.setAdapter(adapterZl);
    btn_zhiliang.setOnItemSelectedListener(new SpinnerZlSelectedListener());
    /****************** 包装 ***********************/
    btn_baozhuang = (Spinner) view.findViewById(R.id.btn_baozhuang);
    adapterBz = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, bz);
    adapterBz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    btn_baozhuang.setAdapter(adapterBz);
    btn_baozhuang.setOnItemSelectedListener(new SpinnerBzSelectedListener());
    /****************** 姓名 ***********************/
    btn_name = (Button) view.findViewById(R.id.btn_name);
    btn_name.setOnClickListener(this);
    /****************** 床号 ***********************/
    btn_bed = (Button) view.findViewById(R.id.btn_bed);
    btn_bed.setOnClickListener(this);
    /****************** 病历号==病人ID ***********************/
    btn_bingli = (Button) view.findViewById(R.id.btn_bingli);
    btn_bingli.setOnClickListener(this);
    /****************** 血袋号 ***********************/
    btn_xuedai = (Button) view.findViewById(R.id.btn_xuedai);
    btn_xuedai.setOnClickListener(this);
    /****************** 血型 血型扫描出来的是血型代号 ***********************/
    btn_xuexing = (Button) view.findViewById(R.id.btn_xuexing);
    /****************** 血成分 ***********************/
    btn_xuepin = (Button) view.findViewById(R.id.btn_xuepin);
    /****************** 配血结果 ***********************/
    btn_peixue = (Spinner) view.findViewById(R.id.btn_peixue);
    adapterPx = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, px);
    adapterPx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    btn_peixue.setAdapter(adapterPx);
    btn_peixue.setOnItemSelectedListener(new SpinnerPxSelectedListener());
    /****************** 血量 ***********************/
    btn_xueliang = (Button) view.findViewById(R.id.btn_xueliang);
    btn_sure = (Button) view.findViewById(R.id.btn_sure);
    btn_sure.setOnClickListener(this);
    tv_tras_bed = (TextView) view.findViewById(R.id.tv_tras_bed);
    if (patient.getBedLabel() != null) {
      tv_tras_bed.setText("床号：" + patient.getBedLabel());
    } else {
      tv_tras_bed.setText(
          getResources().getString(R.string.bed_label) + Html.fromHtml("<small>未分配</small>"));
    }
    tv_tras_name = (TextView) view.findViewById(R.id.tv_tras_name);
    imbtn_home = (ImageButton) view.findViewById(R.id.imbtn_home);
    imbtn_back = (ImageButton) view.findViewById(R.id.imbtn_back);
    imbtn_back.setOnClickListener(this);
    imbtn_home.setOnClickListener(this);
    tv_memo = (TextView) view.findViewById(R.id.tv_memo);

  }

  @Override
  public void onClick(View arg0) {
    switch (arg0.getId()) {
      case R.id.btn_xiaoqi:
        break;
      case R.id.btn_name:
        break;
      case R.id.btn_bed:
        break;
      case R.id.btn_bingli:
        break;
      case R.id.btn_xuedai:
        break;
      case R.id.btn_xuexing:
        break;
      case R.id.btn_xuepin:
        break;
      case R.id.btn_xueliang:
        break;
      case R.id.imbtn_back:
        // 血品列表界面
        Intent intent = new Intent();
        intent.putExtra(GlobalConstant.KEY_REQID, reqId);
        getActivity().setResult(GlobalConstant.RESUlt_CODE, intent);
        super.getActivity().onBackPressed();
        break;
      case R.id.imbtn_home:
        getActivity().startActivity(new Intent(getActivity(), HomeAct.class));
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.in_or_out_static, R.anim.slide_out_right);
        break;
      case R.id.btn_sure:
        upLoadThreeEightCheck();
        // // 到观察界面
        // getFragmentManager().beginTransaction()
        // .replace(R.id.fl_container, new TPRSpeedObserveFra(patient,
        // null, mBloodProductBean2, 1, 900000))
        // .addToBackStack(null).commit();
        break;
      default:
        break;
    }

  }

  /**
   * 上传3查8对结果
   */
  private void upLoadThreeEightCheck() {
    String url = UrlConstant.UpLoadThreeEight() + mThreeEightCheckBean.toString() + "&eventId=" +
        eventId + "&applyNo=" + mBloodProductBean2.getApplyNo();
    CCLog.i(TAG, "上传3查8对结果》" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

      @Override
      public void onFailure(HttpException arg0, String arg1) {
        ShowToast("3查8对失败");
        if (LinstenNetState.isLinkState(getActivity())) {
          Toast.makeText(getActivity(), getString(R.string.unstable), Toast.LENGTH_SHORT).show();
        } else {
          toErrorAct();
        }
      }

      @Override
      public void onSuccess(ResponseInfo<String> arg0) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(arg0.result);
          boolean result = jsonObject.getBoolean("result");
          if (result) {
            boolean end = jsonObject.getBoolean("end");
            if (end) {
              // 退出三叉八度，提示核对异常
              getActivity().finish();
              ShowToast("血品核对不符,异常结束");
            } else {
              // 到观察界面
              getFragmentManager().beginTransaction()
                  .replace(R.id.fl_container, new TPRSpeedObserveFra(patient,
                      null, mBloodProductBean2, 1, 900000))
                  .addToBackStack(null).commit();
            }
          } else {
            // 到观察界面
            getFragmentManager().beginTransaction()
                .replace(R.id.fl_container, new TPRSpeedObserveFra(patient,
                    null, mBloodProductBean2, 1, 900000))
                .addToBackStack(null).commit();
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private String spaceToT(String str) {
    if (str != null && str.length() > 0 && !str.equals("null")) {
      return str.replace(" ", "T");
    } else {
      return str;
    }
  }

  private void toErrorAct() {
    Intent intent1 = new Intent(getActivity(), ErrorAct.class);
    intent1.setClass(transfusionActivity, ErrorAct.class);
    startActivity(intent1);
  }

  /**
   * 判断输入的效期都是数字组成
   *
   * @param str
   * @return
   */
  public boolean isNumeric(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
      return false;
    }
    return true;
  }

  /**
   * 核对警示框
   *
   * @param msg
   */
  private void showWarDialog(String msg) {
    // 已过期
    final AlertDialogTwoBtn mAlertDialog = new AlertDialogTwoBtn(getActivity());
    mAlertDialog.setTitle("提示");
    mAlertDialog.setMessage(msg);
    mAlertDialog.setLeftButton("是", new OnClickListener() {
      @Override
      public void onClick(View view) {
        mAlertDialog.dismiss();
      }
    });
    mAlertDialog.setRightButton("否", new OnClickListener() {
      @Override
      public void onClick(View view) {
        // getActivity().finish();
        mAlertDialog.dismiss();
      }
    });
  }

  /**
   * 监听效期扫描成功监听器，当扫描成功后立刻显示效期, 0表示: 扫描的是有效期
   */
  private void XiaoQiListener() {
    scanXiaoQi = new ScanXiaoQi();
    scanXiaoQi.run();
    scanXiaoQi.setOnTestListening(new OnTestListening() {

      @Override
      public void TestListening(int i) {
        if (i == 0) {
          btn_xiaoqi.setText(GlobalConstant.yxq);
          // JudgeOverTime();
          GlobalConstant.yxq = null;
          scanXiaoQi.stop();
        }
      }
    });
  }

  /**
   * 监听血袋号扫描成功监听器
   */
  // private void XueDaiHaoListener() {
  // scanXueDaiHao = new ScanXueDaiHao();
  // scanXueDaiHao.run();
  // scanXueDaiHao.setOnTestListening(new ScanXueDaiHao.OnTestListening() {
  // @Override
  // public void TestListening(int i) {
  // if (i == 1) {
  // SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
  // GlobalConstant.KEY_BARCODE, Activity.MODE_PRIVATE);
  // String bloodDonorCode = sharedPreferences.getString(
  // GlobalConstant.KEY_BLOODDONORCODE, "");
  // if (StringTool.patternCode(bloodDonorCode, GlobalConstant.xdh)) {
  // loadBloodByDonorCode(patient.getPatId(), GlobalConstant.xdh);
  // GlobalConstant.xdh = null;
  // scanXueDaiHao.stop();
  // } else {
  // ShowToast("请扫描血袋码");
  // }
  // }
  // }
  // });
  // }

  public void setScanResult(String result) {
    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
        GlobalConstant.KEY_BARCODE, Activity.MODE_PRIVATE);
    String bloodDonorCode = sharedPreferences.getString(
        GlobalConstant.KEY_BLOODDONORCODE, "");
    if (StringTool.patternCode(bloodDonorCode, result)) {
      loadBloodByDonorCode(patient.getPatId(), result, mBloodProductBean2.getPlanOrderNo());
    } else {
      ShowToast("请扫描血袋码");
    }
  }

  /**
   * 监听扫描腕带
   */
  private void WanDaiListener() {
    scanWanDai = new ScanWanDai();
    scanWanDai.run();
    scanWanDai.setOnTestListening(new ScanWanDai.OnTestListening() {
      @Override
      public void TestListening(int i) {
        if (i == 2) {
          if (GlobalConstant.wd.equals(patient.getPatCode())) {
            // 姓名
            btn_name.setText(patient.getName());
            btn_name.setTextColor(getActivity().getResources().getColor(R.color.white));
            btn_name.setBackgroundResource(R.drawable.three_eight_green_long);
            mThreeEightCheckBean.setName("1");
            mThreeEightCheckBean.setNameCheckTime(DateTool.getCurrDateTime());
            yn_name = true;
            // 床号
            btn_bed.setText(patient.getBedLabel() + "床");
            btn_bed.setTextColor(getActivity().getResources().getColor(R.color.white));
            btn_bed.setBackgroundResource(R.drawable.three_eight_green_long);
            mThreeEightCheckBean.setBedLabel("1");
            mThreeEightCheckBean.setBedLabelCheckTime(DateTool.getCurrDateTime());
            yn_bed = true;
            // 病历号
            btn_bingli.setText(patient.getPatId());
            btn_bingli.setTextColor(getActivity().getResources().getColor(R.color.white));
            btn_bingli.setBackgroundResource(R.drawable.three_eight_green_long);
            mThreeEightCheckBean.setPatCode("1");
            mThreeEightCheckBean.setPatCodeCheckTime(DateTool.getCurrDateTime());
            btn_bingli.setBackgroundResource(R.drawable.three_eight_green_long);
            yn_id = true;
            GlobalConstant.wd = null;
            scanWanDai.stop();
          } else {
            ShowToast("患者身份核对错误，请再次核对");
          }
        }
      }
    });
  }

  public void loadBloodByDonorCode(String patId, String bloodDonorCode, String planOrderNo) {

    upLoadThreeEightCheck();
    String url =
        UrlConstant.loadOrdersByBloodDonorCode() + UserInfo.getUserName() + "&patId=" + patId +
            "&bloodDonorCode=" + bloodDonorCode + "&planOrderNo=" + planOrderNo;
    CCLog.i("输血加载一袋血>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

      @Override
      public void onFailure(HttpException arg0, String arg1) {
        ShowToast("本血品信息加载异常，请联系运维");
      }

      @Override
      public void onSuccess(ResponseInfo<String> arg0) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        Boolean result = false;
        List<BloodProductBean2> bloodOrders = null;
        try {
          jsonObject = new JSONObject(arg0.result);
          result = jsonObject.getBoolean("result");
          jsonArray = jsonObject.getJSONArray("data");
          if (result) {
            bloodOrders = new ArrayList<BloodProductBean2>();
            Gson gson = new Gson();
            Type type = new TypeToken<List<BloodProductBean2>>() {}.getType();
            bloodOrders.clear();
            bloodOrders = gson.fromJson(jsonArray.toString(), type);
            mBloodProductBean2 = bloodOrders.get(0);
            if ("1".equals(mBloodProductBean2.getBloodStatus())) {
              // 到巡视界面
              TrasPatrolFra trasPatrolFra = new TrasPatrolFra(patient, bloodOrders.get(0));
              getFragmentManager().beginTransaction().replace(R.id.fl_container, trasPatrolFra)
                  .commit();
            } else {
              setBloodInfo(bloodOrders.get(0));
            }
          } else {
            ShowToast("本血品信息异常，请联系运维");
          }
        } catch (JSONException e1) {
          e1.printStackTrace();
          ShowToast("本血品信息加载异常，请联系运维");
        }
      }

    });
  }

  private void validityRec(RecBean recBean) {
    String url = UrlConstant.validityRec() + recBean.toString();
    CCLog.i("记录失效>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
      @Override
      public void onSuccess(ResponseInfo<String> responseInfo) {
        try {
          JSONObject jsonobject = new JSONObject(responseInfo.result);
          boolean result = jsonobject.getBoolean("result");
          if (result) {
            CCLog.i("失效期记录成功");
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(HttpException e, String s) {
        CCLog.i("失效期记录成功");
      }
    });
  }

  /**
   * 包装下拉列表监听器
   */
  class SpinnerBzSelectedListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> arg0, View arg1, final int position, long arg3) {

      switch (position) {

        case 0:
          btn_baozhuang.setBackgroundResource(R.drawable.three_eight_green_spinner);
          mThreeEightCheckBean.setPacking("1");// 1:完好 0:碎坏
          mThreeEightCheckBean.setPackingCheckTime(DateTool.getCurrDateTime());
          flag_bz = true;
          break;
        case 1:
          // 给回传参数赋值
          btn_baozhuang.setBackgroundResource(R.drawable.three_eight_red_spinner);
          mThreeEightCheckBean.setPacking("0");// 1:完好 0:碎坏
          mThreeEightCheckBean.setPackingCheckTime(DateTool.getCurrDateTime());
          flag_bz = true;
          break;
        default:
          break;
      }
    }

    public void onNothingSelected(AdapterView<?> arg0) {}
  }

  // 质量下拉列表监听器
  class SpinnerZlSelectedListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> arg0, View arg1, final int position, long arg3) {

      switch (position) {

        case 0:
          btn_zhiliang.setBackgroundResource(R.drawable.three_eight_green_spinner);
          // 给回传参数赋值
          mThreeEightCheckBean.setQuality("1");// 1:完好 0:碎坏
          mThreeEightCheckBean.setQualityCheckTime(DateTool.getCurrDateTime());
          flag_zl = true;
          break;
        case 1:// 不合格
          btn_zhiliang.setBackgroundResource(R.drawable.three_eight_red_spinner);
          // 给回传参数赋值
          mThreeEightCheckBean.setQuality("0");// 1:完好 0:碎坏
          mThreeEightCheckBean.setQualityCheckTime(DateTool.getCurrDateTime());
          flag_zl = true;
          // 点击完确认后由后台判断
          // showWarDialog("本血品质量不合格,是否继续核对？");
          break;

        default:
          break;
      }
    }

    public void onNothingSelected(AdapterView<?> arg0) {}
  }

  // 配血下拉列表监听器
  class SpinnerPxSelectedListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> arg0, View arg1,
        final int position, long arg3) {

      switch (position) {

        case 0:
          btn_peixue.setBackgroundResource(R.drawable.three_eight_green_spinner_long);
          mThreeEightCheckBean.setMatchResult("1");// 1:ok 0: no
          mThreeEightCheckBean.setMatchResultCheckTime(DateTool.getCurrDateTime());
          flag_jg = true;
          break;
        case 1:// 不相符
          btn_peixue.setBackgroundResource(R.drawable.three_eight_red_spinner_long);
          mThreeEightCheckBean.setMatchResult("0");// 1:ok 0: no
          mThreeEightCheckBean.setMatchResultCheckTime(DateTool.getCurrDateTime());
          flag_jg = true;
          // 点击完确定后交由后台系统判断
          // showWarDialog("本血品配血结果不符,是否继续核对？");
          break;

        default:
          break;
      }
    }

    public void onNothingSelected(AdapterView<?> arg0) {}
  }

}
