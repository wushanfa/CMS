package com.gentlehealthcare.mobilecare.activity.bloodbagnuclearcharge;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.adapter.BloodBagNuclearChangeAdapter;
import com.gentlehealthcare.mobilecare.bean.sys.BarcodeDict;
import com.gentlehealthcare.mobilecare.common.MyToast;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.presenter.BloodBagNuclearChangePresenter;
import com.gentlehealthcare.mobilecare.swipe.view.XListView;
import com.gentlehealthcare.mobilecare.tool.CollectionsTool;
import com.gentlehealthcare.mobilecare.tool.DeviceTool;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.view.IBloodBagNuclearChangeView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zyy on 2016/9/13.
 * 类说明：血品核收
 */

public class BloodBagNuclearChargeAct extends BaseActivity
    implements
      IBloodBagNuclearChangeView,
      View.OnClickListener {
  public BloodBagNuclearChangePresenter bloodBagNuclearChangePresenter = null;
  public com.gentlehealthcare.mobilecare.swipe.view.XListView lv_orders;
  public Button btn_back;
  List<BloodProductBean2> bloodProductBean2s = new ArrayList<BloodProductBean2>();
  private static String currentLogId = "";
  public BloodBagNuclearChangeAdapter bloodProductsListAdapter;
  // 将多袋血置顶，护士手动向左滑动退回血库
  List<BloodProductBean2> topList = new ArrayList<BloodProductBean2>();
  private static String currentBloodDonorCode = "";
  private Vibrator vibrator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_blood_blag_nuclear_change);
    if (bloodBagNuclearChangePresenter == null) {
      bloodBagNuclearChangePresenter = new BloodBagNuclearChangePresenter(this);
    }
    intialView();
    bloodBagNuclearChangePresenter.collect(UserInfo.getWardCode(), "9");
  }

  private void intialView() {
    lv_orders = (XListView) findViewById(R.id.lv_orders);
    btn_back = (Button) findViewById(R.id.btn_back);
    btn_back.setOnClickListener(this);
  }

  @Override
  protected void resetLayout() {

  }

  @Override
  public void setBloodBag(final List<BloodProductBean2> params) {
    if (!StringTool.isEmpty(currentLogId)) {
      for (int i = 0; i < params.size(); i++) {
        BloodProductBean2 blood = params.get(i);
        if (currentLogId.equals(blood.getLogId())) {
          params.remove(i);
          params.add(0, blood);
          break;
        }
      }
    }
    lv_orders.setPullLoadEnable(true);
    lv_orders.setPullRefreshEnable(false);
    lv_orders.setXListViewListener(new XListView.IXListViewListener() {
      @Override
      public void onRefresh() {
        lv_orders.stopRefresh();
      }

      @Override
      public void onLoadMore() {
        loadBag();
        lv_orders.stopLoadMore();
      }
    });
    bloodProductsListAdapter = new BloodBagNuclearChangeAdapter(this, params);
    bloodProductBean2s.clear();
    bloodProductBean2s = params;
    lv_orders.setAdapter(bloodProductsListAdapter);
    bloodProductsListAdapter.notifyDataSetChanged();
  }

  @Override
  public void showToast(String msg) {
    ShowToast(msg);
  }

  @Override
  public void loadBag() {
    bloodBagNuclearChangePresenter.collect(UserInfo.getWardCode(), "9");
  }

  @Override
  public void checkOrdersFailed() {
    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
    vibrator.vibrate(pattern, -1);
  }

  public void bloodBack(String username, String logId, String wardCode) {
    bloodBagNuclearChangePresenter.bloodBack(username, wardCode, logId);
  }

  /**
   * 红外扫描获取的值
   *
   * @param result
   */
  public void DoCameraResult(String result) {
    bloodBagNuclearChangePresenter.commRec(UserInfo.getUserName(), result,
        DeviceTool.getDeviceId(this));
    List<BarcodeDict> barcodeDicts = new ArrayList<BarcodeDict>();
    barcodeDicts = LocalSave.getDataList(getApplicationContext(),
        GlobalConstant.KEY_BARCODE);

    String barcodeName = "";
    for (int i = 0; i < barcodeDicts.size(); i++) {

      if (patternCode(barcodeDicts.get(i).getRegularExp(), result)) {
        barcodeName = barcodeDicts.get(i).getBarcodeName();
        break;
      }
    }

    if (barcodeName.equals("BLOOD_PRODUCT_CODE")) {// 产品码
      boolean flag = false;
      if (CollectionsTool.isEmpty(topList)) {
        flag = false;
      } else {

        for (int i = 0; i < topList.size(); i++) {
          if ((result.equals(topList.get(i).getBloodProductCode())
              && topList.get(i).getBloodDonorCode().equals(currentBloodDonorCode))) {
            currentLogId = topList.get(i).getLogId();
            if ("1".equals(topList.get(i).getBloodStatus())) {
              tostMsg(true, "此血品已在执行中");
            } else if ("9".equals(topList.get(i).getBloodStatus())) {
              tostMsg(true, "此血品已执行完毕");
            } else if ("0".equals(topList.get(i).getBloodStatus())) {
              tostMsg(true, "此血品已核收");
            } else if ("-1".equals(topList.get(i).getBloodStatus())) {
              tostMsg(true, "此血品已取消");
            } else if ("U".equals(topList.get(i).getBloodStatus())) {
              //核对完当前的之后清理头部容器，避免扫描血袋号相同的第二袋产品码时直接通过
              topList.clear();
              bloodBagNuclearChangePresenter.bloodBagNuclearChange(currentLogId,
                  UserInfo.getName(), UserInfo.getUserName(), UserInfo.getWardCode());

            }
            flag = true;
            break;
          } else {
            flag = false;
          }
        }

      }
      if (!flag) {
        // 提示先扫描血袋码
        tostMsg(true, "扫码错误，请先扫描血袋码");
      }
    } else if (barcodeName.equals("BLOOD_DONOR_CODE")) {// 血袋号
      currentBloodDonorCode = result;
      final List<BloodProductBean2> temp = new ArrayList<BloodProductBean2>();
      List<BloodProductBean2> others = new ArrayList<BloodProductBean2>();
      List<BloodProductBean2> commonTop = new ArrayList<BloodProductBean2>();
      temp.clear();
      others.clear();
      commonTop.clear();
      for (int i = 0; i < bloodProductBean2s.size(); i++) {
        if (result.equals(bloodProductBean2s.get(i).getBloodDonorCode())) {
          // 只要血袋号相同、不论状态提取到头部显示
          commonTop.add(bloodProductBean2s.get(i));
        } else {
          others.add(bloodProductBean2s.get(i));
        }
      }
      // 对应多条未核收的的血品，不判断状态，提示“请扫描产品码二次确认”
      if (commonTop.size() > 1) {
        MyToast myToast = new MyToast(this, 0, "请扫描产品码二次确认", 3 * 1000);
        myToast.showToast();
      } else if (commonTop.size() == 1) {
        // 只有一条时判断状态并做具体操作
        if (commonTop.get(0).getBloodStatus().equals("0")) {
          tostMsg(true, "此血品已核收");
        } else if ("1".equals(commonTop.get(0).getBloodStatus())) {
          tostMsg(true, "此血品正在执行中");
        } else if ("9".equals(commonTop.get(0).getBloodStatus())) {
          tostMsg(true, "此血品已执行完毕");
        } else if ("-1".equals(commonTop.get(0).getBloodStatus())) {
          tostMsg(true, "此血品已取消");
        } else if (commonTop.get(0).getBloodStatus().equals("U")) {
          // 需要核对的
          temp.add(commonTop.get(0));
        }
      } else {
        // 血袋码未找到
        tostMsg(true, "未找到该血品信息");
      }

      topList.addAll(commonTop);
      topList.addAll(others);
      bloodProductsListAdapter.notifyDataSetChanged(topList);

      if (temp.size() == 1) {
        currentLogId = temp.get(0).getLogId();
        bloodBagNuclearChangePresenter.bloodBagNuclearChange(temp.get(0).getLogId(),
            UserInfo.getName(), UserInfo.getUserName(), UserInfo.getWardCode());
      }
    } else {
      // 本界面扫描其他类型条码
      tostMsg(true, "扫描条码不符");
    }

  }

  private void tostMsg(boolean isVibration, String msg) {
    if (isVibration) {
      checkOrdersFailed();
    }
    MyToast myToast = new MyToast(this, 0, msg, 3 * 1000);
    myToast.showToast();
  }

  private boolean patternCode(String patternStr, String matcherStr) {
    Pattern p = Pattern.compile(patternStr.trim());
    Matcher m = p.matcher(matcherStr.trim());
    boolean b = m.matches();
    return b;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    bloodProductBean2s.clear();
    topList.clear();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_back:
        finish();
        break;
    }
  }
}
