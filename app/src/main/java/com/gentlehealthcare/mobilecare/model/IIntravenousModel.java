package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.IntravenousModel;

/**
 * Created by zyy on 2016/5/14.
 */
public interface IIntravenousModel {

    void startIntravenous(String patId, String speed, String startTime, String injectionTool, String
            planOrderNo, String nextPatrolTime, IntravenousModel.startIntravenousListener listener);

    void saveNextPatrolTime(String patId, String username, String planOrderNo, String inspectionTime,
                            IntravenousModel.saveNextPatrolTimeListener listener);

    void savePatrolInfo(String patId, String username, String planOrderNo, String msg, String dosageActual,
                        String skinTestResult, int type, IntravenousModel.saveIntravenousPatrolListener listener);

    void saveExceptionInfo(String patId, String username, String planOrderNo, String status, String
            completeDosage, String varianceCause, String varianceCauseDesc, IntravenousModel
                                   .saveIntravenousExceptionListener listener);

    void loadVariationDict(IntravenousModel.loadVariationDictListener listener);

}
