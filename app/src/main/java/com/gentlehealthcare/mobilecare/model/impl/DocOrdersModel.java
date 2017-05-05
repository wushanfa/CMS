package com.gentlehealthcare.mobilecare.model.impl;


import android.os.Handler;
import android.text.TextUtils;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.IDocOrdersModel;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.net.bean.ExecuteToOrgRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.UrlTool;
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

/**
 * Created by zhiwei on 2016/5/5.
 *
 * @desp 医嘱列表的数据层
 */
public class DocOrdersModel implements IDocOrdersModel {

  /**
   * 变更医嘱到暂不执行状态
   */
  @Override
  public void changeOrderStatus(String ChangeStatus, final String planorderNo,
      final String username, final String patId, final String planTime, final String orderType,
      final String status,
      final String templateId, final String orderClass, final String resultFlag, final int typeFlag,
      final changeOrderStatusListener listener) {
    ExecuteToOrgRecordBean eo = new ExecuteToOrgRecordBean();
    eo.setPerformStatus(ChangeStatus);
    eo.setPlanOrderNo(planorderNo);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, UrlConstant.ExecuteToOrg() + eo.toString(),
        new RequestCallBack<String>() {


          @Override
          public void onSuccess(ResponseInfo<String> responseInfo) {
            String result = responseInfo.result;
            Gson gson = new Gson();
            Type type = new TypeToken<ExecuteToOrgRecordBean>() {}.getType();
            ExecuteToOrgRecordBean temp = gson.fromJson(result, type);
            if (temp.getResult().equals("success")) {
              listener.changeStatusSuccess("success", planorderNo, username, patId, planTime,
                  orderType, status, templateId, orderClass, resultFlag, typeFlag);
            } else {
              listener.changeStatusFailure("failed", null);
            }
          }

          @Override
          public void onFailure(HttpException error, String msg) {
            listener.changeStatusFailure("failed", error);
          }
        });
  }

  /**
   * 执行请求
   */
  @Override
  public void executionOrder(String username, String patId, String planId, String wardCode,
      String nurseInOperate, String planOrderNo, final ExecuteOrderListener listener) {
    String url =
        UrlConstant.ExecutionOrders() + "?username=" + username + "&patId=" + patId + "&planId="
            + planId + "&wardCode=" + wardCode + "&nurseInOperate=" + nurseInOperate
            + "&planOrderNo=" + UrlTool.transWord(planOrderNo);
    CCLog.i("医嘱执行>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url,
        new RequestCallBack<String>() {

          @Override
          public void onSuccess(ResponseInfo<String> responseInfo) {
            String result = responseInfo.result;
            try {
              JSONObject jsonObject = new JSONObject(result.toString());
              String status = jsonObject.getString("result");
              if (TextUtils.equals(status, "success")) {
                listener.onExecuteSuccess("success");
              } else {
                listener.onExecuteFailure("faile", null);
              }
            } catch (JSONException e) {
              e.printStackTrace();
              listener.onExecuteFailure("faile", e);
            }
          }

          @Override
          public void onFailure(HttpException error, String msg) {
            listener.onExecuteFailure("faile", null);
          }
        });
  }

  @Override
  public void DoLoadOrder(String username, String patId, String planTime, String orderType,
      String status,
      String templateId, String orderClass, final OnLoadOrdersListListener listener) {
    String url =
        UrlConstant.loadNewOrders() + username + "&patId=" + patId + "&planTime=" + planTime +
            "&orderType=" + orderType + "&status=" + status + "&orderClass=" + orderClass
            + "&templateId=" +
            templateId;
    CCLog.i("医嘱加载>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

      @Override
      public void onFailure(HttpException arg0, String arg1) {
        listener.onOrderFailure(arg1, arg0);
      }

      @Override
      public void onSuccess(ResponseInfo<String> arg0) {
        String result = arg0.result;
        JSONObject mJsonObject = null;
        JSONArray jsonArray = null;
        boolean state = false;
        try {
          mJsonObject = new JSONObject(result);
          GlobalConstant.SERVICE_TIME = mJsonObject.getString(GlobalConstant.KEY_SERVICE_TIME);
          state = mJsonObject.getBoolean("result");
          if (state) {
            jsonArray = mJsonObject.getJSONArray("order");
            Gson gson = new Gson();
            Type type = new TypeToken<List<OrderListBean>>() {}.getType();
            List<OrderListBean> orderListBeans = gson.fromJson(jsonArray.toString(), type);
            listener.onOrderSuccess(orderListBeans);
          } else {
            listener.onOrderFailure("数据解析异常", null);
          }
        } catch (JSONException e1) {
          e1.printStackTrace();
          listener.onOrderFailure("数据解析异常", e1);
        }
      }

    });
  }

  @Override
  public void DoLoadExecutingOrder(String username, String patCode, String warCode,
      final OnLoadExecutingOrdersListListener listener) {
    String url = UrlConstant.loadNoticesByConformStatus() + username + "&patCode="
        + UrlTool.transWord(patCode) + "&wardCode=" + warCode;
    CCLog.i("消息加载>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

      @Override
      public void onFailure(HttpException arg0, String arg1) {
        listener.onOrderExecutingFailure(arg1, arg0);
      }

      @Override
      public void onSuccess(ResponseInfo<String> arg0) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<TipBean>>() {}.getType();
        try {
          JSONObject jsonObject = new JSONObject(arg0.result);
          boolean result = jsonObject.getBoolean("result");
          if (result) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            List<TipBean> mTips = gson.fromJson(jsonArray.toString(), type);
            listener.onOrderExecutingSuccess(mTips);
          }
        } catch (JSONException e) {
          e.printStackTrace();
          listener.onOrderExecutingFailure("数据解析异常", e);
        }
      }

    });
  }

  @Override
  public void getBloodDonorCode(String patId, String bloodDonorCode, String planOrderNo,
      final getBlood listener) {
    String url =
        UrlConstant.loadOrdersByBloodDonorCode() + UserInfo.getUserName() + "&patId=" + patId +
            "&bloodDonorCode=" + bloodDonorCode + "&planOrderNo=" + planOrderNo;
    CCLog.i("输血加载一袋血>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

      @Override
      public void onFailure(HttpException arg0, String arg1) {
        listener.onBloodFail("加载血袋时失败", arg0);
      }

      @Override
      public void onSuccess(ResponseInfo<String> arg0) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        Boolean result = false;
        String status = null;
        String template = null;
        String msg = null;
        String planOrderNo = null;
        List<BloodProductBean2> bloodOrders = null;
        try {
          jsonObject = new JSONObject(arg0.result);
          result = jsonObject.getBoolean("result");
          template = jsonObject.getString("template");
          jsonArray = jsonObject.getJSONArray("data");
          msg = jsonObject.getString("msg");
          planOrderNo = jsonObject.getString("planOrderNo");
          if (result && template.equals("AA-5")) {
            bloodOrders = new ArrayList<BloodProductBean2>();
            Gson gson = new Gson();
            Type type = new TypeToken<List<BloodProductBean2>>() {}.getType();
            bloodOrders.clear();
            bloodOrders = gson.fromJson(jsonArray.toString(), type);
            listener.onBloodSuccess(bloodOrders, planOrderNo);
          } else {
            listener.onBloodFail("血品表中没有关联血袋信息", null);
          }
        } catch (JSONException e1) {
          e1.printStackTrace();
          listener.onBloodFail("数据解析异常", e1);
        }
      }

    });
  }

  @Override
  public void batchExecuted(final String username, final String patId, String warCode,
      String planOrderNo, final String planTime, final String orderType, final String status,
      final String templateId, final String orderClass, final String resultFlag, final int typeFlag,
      final bantchExecuteListener listener) {
    String url = UrlConstant.batchExecuted() + username + "&patId=" + patId + "&wardCode=" + warCode
        + "&planOrderNo=" + planOrderNo;
    CCLog.i("批量执行>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

      @Override
      public void onFailure(HttpException arg0, String arg1) {
        // listener.bantchExecuteFailure("failed",null);
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            listener.bantchExecuteSuccess("success", username, patId, planTime, orderType, status,
                templateId, orderClass, resultFlag, typeFlag);
          }
        }, 3000);
      }

      @Override
      public void onSuccess(ResponseInfo<String> arg0) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(arg0.result);
          if (jsonObject.getString("result").equals("success")) {
            listener.bantchExecuteSuccess("success", username, patId, planTime, orderType, status,
                templateId, orderClass, resultFlag, typeFlag);
          } else {
            listener.bantchExecuteFailure("failed", null);
          }
        } catch (JSONException e) {
          e.printStackTrace();
          listener.bantchExecuteFailure("failed", null);
        }
      }

    });
  }

  @Override
  public void loadAllNotExecute(String patId, String performStatus,
      final loadAllNotExecuteListener listener) {
    String url = UrlConstant.loadAllNotExecute() + patId + "&performStatus=" + performStatus;
    CCLog.i("所有未执行>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

      @Override
      public void onFailure(HttpException arg0, String arg1) {

        listener.LoadAllNotExecuteFailed(GlobalConstant.FAILED, arg0);
      }

      @Override
      public void onSuccess(ResponseInfo<String> arg0) {
        String result = arg0.result;
        JSONObject mJsonObject = null;
        JSONArray jsonArray = null;
        boolean state = false;
        try {
          mJsonObject = new JSONObject(result);
          state = mJsonObject.getBoolean("result");
          if (state) {
            jsonArray = mJsonObject.getJSONArray("order");
            Gson gson = new Gson();
            Type type = new TypeToken<List<OrderListBean>>() {}.getType();
            List<OrderListBean> orderListBeans = gson.fromJson(jsonArray.toString(), type);
            listener.loadAllNotExecuteSuccess(orderListBeans, GlobalConstant.SUCCEED);
          } else {
            listener.LoadAllNotExecuteFailed("数据解析异常", null);
          }
        } catch (JSONException e1) {
          e1.printStackTrace();
          listener.LoadAllNotExecuteFailed("数据解析异常", e1);
        }
      }
    });

  }

  @Override
  public void patrolRecord(String patId, String username, final patrolRecordListener listener) {
    String url = UrlConstant.patrolRecord() + patId + "&username=" + username;
    CCLog.i("病人执行中医嘱巡视记录>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {

      @Override
      public void onFailure(HttpException arg0, String arg1) {

        listener.patrolRecordFailed(GlobalConstant.FAILED, arg0);
      }

      @Override
      public void onSuccess(ResponseInfo<String> arg0) {
        String result = arg0.result;
        JSONObject mJsonObject = null;
        boolean state = false;
        try {
          mJsonObject = new JSONObject(result);
          state = mJsonObject.getBoolean("result");
          if (state) {
            listener.patrolRecordSuccess(GlobalConstant.SUCCEED, null);
          } else {
            listener.patrolRecordFailed(GlobalConstant.FAILED, null);
          }
        } catch (JSONException e1) {
          e1.printStackTrace();
          listener.patrolRecordFailed(GlobalConstant.FAILED, null);
        }
      }
    });

  }

  @Override
  public void callProcedureBill(String patId, String patCode, int visitId, String relatedBarCode,
      String userName,
      final CallProcedureBillListener listener) {
    String url = UrlConstant.callProcedureBill() + patId + "&patCode=" + patCode + "&visitId="
        + visitId + "&relatedBarCode=" + relatedBarCode + "&userName=" + userName;
    CCLog.i("计费>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
      @Override
      public void onSuccess(ResponseInfo<String> arg0) {
        JSONObject mJsonObject = null;
        String code = "";
        String costs = "";
        String count = "";
        String text = "";
        try {
          mJsonObject = new JSONObject(arg0.result);
          code = mJsonObject.getString("code");
          if (code.equals("0")) {
            costs = mJsonObject.getString("costs");
            count = mJsonObject.getString("count");
            listener.billSuccess(code, costs, count);
          } else {
            text = mJsonObject.getString("text");
            listener.billFailure(code, text, null);
          }
        } catch (JSONException e1) {
          e1.printStackTrace();
          listener.billFailure(code, "计费异常", null);
        }
      }

      @Override
      public void onFailure(HttpException e, String s) {

      }
    });
  }

  @Override
  public void commRec(String userName, String barCode, String deviceId) {
    String url = UrlConstant.commonRec() + userName+"&deviceId" + UrlTool.transWord(deviceId) + "&barCode=" + UrlTool.transWord(barCode);
    CCLog.i("记录>>>" + url);
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, url, new RequestCallBack<String>() {
      @Override
      public void onSuccess(ResponseInfo<String> arg0) {

      }

      @Override
      public void onFailure(HttpException e, String s) {

      }
    });
  }

  public interface CallProcedureBillListener {
    void billSuccess(String code, String cost, String count);

    void billFailure(String code, String msg, Exception e);
  }


  public interface OnLoadOrdersListListener {
    void onOrderSuccess(List<OrderListBean> list);

    void onOrderFailure(String msg, Exception e);
  }

  public interface OnLoadExecutingOrdersListListener {
    void onOrderExecutingSuccess(List<TipBean> list);

    void onOrderExecutingFailure(String msg, Exception e);
  }

  public interface ExecuteOrderListener {
    void onExecuteSuccess(String status);

    void onExecuteFailure(String msg, Exception e);
  }

  public interface getBlood {
    void onBloodSuccess(List<BloodProductBean2> result, String msg);

    void onBloodFail(String msg, Exception e);
  }

  public interface changeOrderStatusListener {

    void changeStatusSuccess(String msg, String planorderNo, String username, String patId,
        String planTime, String orderType, String status,
        String templateId, String orderClass, String resultFlag, int typeFlag);

    void changeStatusFailure(String msg, Exception e);
  }

  public interface bantchExecuteListener {

    void bantchExecuteSuccess(String msg, String username, String patId, String planTime,
        String orderType, String status,
        String templateId, String orderClass, String resultFlag, int typeFlag);

    void bantchExecuteFailure(String msg, Exception e);
  }

  public interface loadAllNotExecuteListener {
    void loadAllNotExecuteSuccess(List<OrderListBean> list, String msg);

    void LoadAllNotExecuteFailed(String msg, Exception e);
  }

  public interface patrolRecordListener {
    void patrolRecordSuccess(String result, String msg);

    void patrolRecordFailed(String msg, Exception e);
  }


}
