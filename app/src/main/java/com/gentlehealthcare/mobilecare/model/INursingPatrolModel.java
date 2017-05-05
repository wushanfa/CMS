package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.NursingPatrolModel;

/**
 * Created by zhiwei on 2016/5/6.
 */
public interface INursingPatrolModel {

    /**
     * 获取巡视字典
     */
    void getPatrolDict(String url, NursingPatrolModel.OnLoadPatrolDictListener listListener);
    /**
     * 加载医嘱
     *
     * @param username   用户名
     * @param patId      病人ID
     * @param planTime   0=昨天 1=今天 2=明天
     * @param orderType  0=长期 1=临时 2=全部
     * @param status     2=全部 0=未执行 9=已执行 1=执行中
     * @param orderClass NURSING_TYPE_3=其他 NURSING_TYPE_1=治疗 NURSING_TYPE_2=药疗
     * @param templateId 0-6(全部)
     */
    void DoLoadOrder(String username, String patId, String planTime, String orderType, String status, String templateId, String orderClass, NursingPatrolModel.OnLoadOrdersListListener listener);

    void saveNursingPatrol(String patId, String wardCode, String performType, String nurseInOperate, NursingPatrolModel.SaveNursingPatrolListener listener);
}
