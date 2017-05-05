package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.DocOrdersModel;

/**
 * Created by zhiwei on 2016/5/5.
 *
 * @desp 医嘱列表数据层的接口类
 */
public interface IDocOrdersModel {

    /**
     * 变更医嘱到暂不执行状态
     */
    void changeOrderStatus(String ChangeStatus, String planorderNo, String username, String patId, String planTime, String orderType, String status,
                           String templateId, String orderClass, String result, int type, DocOrdersModel.changeOrderStatusListener listener);

    /**
     * 执行请求
     */
    void executionOrder(String username, String patId, String planId, String wardCode, String nurseInOperate, String planOrderNo, DocOrdersModel.ExecuteOrderListener listener);

    /**
     * 加载医嘱
     *
     * @param username   用户名
     * @param patId      病人ID
     * @param planTime   0=昨天 1=今天 2=明天
     * @param orderType  0=长期 1=临时 2=全部
     * @param status     2=全部 0=未执行 9=已执行
     * @param orderClass NURSING_TYPE_3=其他 NURSING_TYPE_1=治疗 NURSING_TYPE_2=药疗
     * @param templateId 0-6(全部)
     */
     void DoLoadOrder(String username, String patId, String planTime, String orderType, String status, String templateId, String orderClass, DocOrdersModel.OnLoadOrdersListListener listener);

     void DoLoadExecutingOrder(String username, String patCode, String WardCode, DocOrdersModel.OnLoadExecutingOrdersListListener listener);

    void getBloodDonorCode(String patId, String bloodDonorCode, String planOrderNo, final DocOrdersModel.getBlood listener);

    void batchExecuted(String username, String patId, String warCode, String planOrderNo, String planTime, String orderType, String status,
                       String templateId, String orderClass, String resultFlag, int typeFlagfinal, DocOrdersModel.bantchExecuteListener listener);
    
    void loadAllNotExecute(String patId, String performStatus, DocOrdersModel.loadAllNotExecuteListener listener);

    void patrolRecord(String patId, String username, DocOrdersModel.patrolRecordListener listener);
    /**
     * 调用存储过程计费
     */
    void callProcedureBill(String patId, String patCode, int visitId, String relatedBarCode, String userName, DocOrdersModel.CallProcedureBillListener listener);

    /**
     * 扫描条码记录
     */
    void commRec( String userName,String barCode,String deviceId);
}
