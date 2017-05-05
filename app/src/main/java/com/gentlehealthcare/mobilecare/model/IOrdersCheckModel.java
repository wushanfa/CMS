package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.OrdersCheckModel;

/**
 * Created by zhiwei on 2016/5/5.
 *
 * @desp 医嘱列表数据层的接口类
 */
public interface IOrdersCheckModel {
    /**
     * 加载医嘱
     *
     * @param username 用户名
     * @param patId    病人ID
     * @param planTime 0=昨天 1=今天 2=明天
     * @param status   3=全部 0=未核对 1=已核对 2=取消
     */
    void DoLoadOrder(String username, String patId, String planTime, String status, OrdersCheckModel.OnLoadOrdersListListener listener);

    void findPatId(String barCode,String userName,String wardCode, OrdersCheckModel.findPatIdListener listener);

    void checkOrder(String wardCode,String patId, String relatedBarcode,String relatedBarcode2, String checkBy, String planOrderNo, OrdersCheckModel.checkOrderListener listener);

}
