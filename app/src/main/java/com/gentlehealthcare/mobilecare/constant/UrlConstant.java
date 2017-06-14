package com.gentlehealthcare.mobilecare.constant;

import com.gentlehealthcare.mobilecare.activity.ActivityControlTool;
import com.gentlehealthcare.mobilecare.tool.SystemInfoSave;

import java.util.Map;

public class UrlConstant {

    private static String ip = "";
    private static String port = "";

    public static String GetBaseUrl() {
        return "http://" + UrlConstant.getIp() + ":" + UrlConstant.getPort() + "/mnis";
    }

    public static String GetIpAndPort(){
        return "http://" + UrlConstant.getIp() + ":" + UrlConstant.getPort()+"/";
    }

    /**
     * 根据类型获取工作类型后台根目录路径
     *
     * @param type
     * @return
     */
    public static String GetWorkTypeRoot(int type) {
        if (type == 0) {
            return "intrav";
        } else if (type == 1) {
            return "insulin";
        }
        return "";
    }

    public static void setIp(String ip) {
        Map<String, String> map = SystemInfoSave.getInstance(
                ActivityControlTool.currentActivity).get();
        map.put("ip", ip);
        SystemInfoSave.getInstance(ActivityControlTool.currentActivity).save(
                map);
        UrlConstant.ip = ip;
    }

    public static String getIp() {
        Map<String, String> map = SystemInfoSave.getInstance(
                ActivityControlTool.currentActivity).get();
        String ip = map.get("ip");
        if (!UrlConstant.ip.equals(ip))
            UrlConstant.ip = ip;
        return UrlConstant.ip;
    }

    public static void setPort(String port) {
        Map<String, String> map = SystemInfoSave.getInstance(
                ActivityControlTool.currentActivity).get();
        map.put("port", port);
        SystemInfoSave.getInstance(ActivityControlTool.currentActivity).save(
                map);
        UrlConstant.port = port;
    }

    public static String getPort() {
        Map<String, String> map = SystemInfoSave.getInstance(
                ActivityControlTool.currentActivity).get();
        String port = map.get("port");
        if (!UrlConstant.port.equals(port))
            UrlConstant.port = port;
        return UrlConstant.port;
    }

    /**
     * 登录
     */
    public static String GetLogin() {
        return "http://" + UrlConstant.getIp() + ":" + UrlConstant.getPort() + "/mnis" + "/sys/userLogin";
    }

    /**
     * 系统信息
     */
    public static String GetSystemInfo() {
        return GetBaseUrl() + "/sys/loadSysInfo";
    }

    /**
     * 同步病人数据
     */

    public static String GetSyncPatient() {
        return GetBaseUrl() + "/patient/loadMyPatient";
    }

    /**
     * 同步部门病人
     */

    public static String GetSyncDeptPatient() {
        return GetBaseUrl() + "/patient/loadDeptPatient";
    }

    /**
     * 同步已完成工作
     */
    public static String GetSyncComlitedWorkList() {
        return GetBaseUrl() + "/pat/loadComplitedWorkList";
    }

    /**
     * 获取病人待执行任务
     */
    public static String GetSyncPatperformWrokList() {
        return GetBaseUrl() + "/pat/loadPatPerformWokList";
    }

    /**
     * 获取病人主要信息
     */
    public static String GetPatmajorInfo() {
        return GetBaseUrl() + "/pat/loadPatMajorinfo";
    }

    /**
     * 加载医嘱信息
     *
     * @param type
     * @return
     */
    public static String GetOrders(int type) {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                + "/loadIntravsByPatId";
    }

    /**
     * 获取待执行和执行中数据量
     *
     * @param type
     * @return
     */
    public static String GetIntravsSnapshot(int type) {
        if (type == 0)
            return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                    + "/loadIntravsSnapshot";
        else
            return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                    + "/loadInsulinsSnapshot";
    }

    /**
     * 获取注射时间和巡视时间
     *
     * @param type
     * @return
     */
    public static String GetInspectionTime(int type) {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                + "/loadInspectionTime";
    }

    /**
     * 待执行工作
     */
    public static String GetSyncWillExecutionWork() {
        return GetBaseUrl() + "/workList/performWorkList";
    }

    /**
     * 执行中工作
     */
    public static String GetSyncExecutingWork() {
        return GetBaseUrl() + "/workList/executingWorkList";
    }

    /**
     * 核对患者身份
     */
    public static String GetCheckAuthentication(int worktype) {
        // return GetBaseUrl()+"/"+GetWorkTypeRoot(worktype)+"/authentication";
        return GetBaseUrl() + "/patient/checkPatientIdentification";
    }

    /**
     * 核对药品
     */
    public static String GetCheckMedicine(int type) {
        if (type == 0) {
            return GetBaseUrl() + "/" + GetWorkTypeRoot(type) + "/intravDrugsCheck";
        } else {
            return GetBaseUrl() + "/" + GetWorkTypeRoot(type) + "/drugsCheck";
        }
    }

    /**
     * 执行给药
     */
    public static String GetExecuteMedicine() {
        return GetBaseUrl() + "/intrav/startInfusion";
    }

    /**
     * 巡视
     */
    public static String GetPatrol(int type) {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(type) + "/recInspectResult";
    }

    /**
     * 取消执行
     */
    public static String GetCancelExecute(int type) {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(type) + "/cancelInfusion";
    }

    /**
     * z执行完成
     */
    public static String GetCompleteExecute(int type) {
        if (type == 0)
            return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                    + "/completeInfusion";
        else
            return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                    + "/recInspectResult";
    }

    /**
     * 加载医嘱
     *
     * @return
     */
    public static String LoadOrders() {
        return GetBaseUrl() + "/order/loadOrders";
    }

    /**
     * 执行医嘱
     *
     * @return
     */
    public static String ExecutionOrders() {
        return GetBaseUrl() + "/order/execute";
    }

    /**
     * 执行医嘱到没执行
     *
     * @return
     */
    public static String ExecuteToOrg() {
        return GetBaseUrl() + "/order/executeToOrg";
    }

    /**
     * 记录体征一
     *
     * @return
     */
    public static String RecordFirstPageSigns() {
        return GetBaseUrl() + "/signs/recordFirstPageSigns";
    }

    /**
     * 记录体征二
     *
     * @return
     */
    public static String RecordSecondPageSigns() {
        return GetBaseUrl() + "/signs/recordSecondPageSigns";
    }

    /**
     * 加载体征一
     *
     * @return
     */
    public static String LoadSignsByConditions() {
        return GetBaseUrl() + "/signs/loadSignsByConditions";
    }

    /**
     * 加载体征一
     *
     * @return
     */
    public static String LoadSecondPageSignsByConditions() {
        return GetBaseUrl() + "/signs/loadSecondPageSigns";
    }

    // ----------------------胰岛素---------

    public static String LoadPatInsulinPlans() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1) + "/loadPatInsulinPlans";
    }

    /**
     * 测血糖
     *
     * @return
     */
    public static String GetStartBloodSugarTest() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1) + "/startBloodSugarTest";
    }

    /**
     * 血糖值核对
     *
     * @return
     */
    public static String CheckBloodTestVal() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1) + "/glucoseValCheck";
    }

    /**
     * 血糖值核对
     *
     * @return
     */
    public static String BloodSugarGetItem() {
        return GetBaseUrl() + "/insulin/loadGlucoseItems";
    }

    /**
     * 加载注射模块
     *
     * @return
     */
    public static String LoadInjectionTemplate() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1)
                + "/loadInjectionTemplates";
    }

    /**
     * 重新生成模板数据
     *
     * @return
     */
    public static String GeneratorTemplate() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1) + "/generatorTemplates";
    }

    /**
     * 修改区域块状态
     *
     * @return
     */
    public static String SetStatusForArea() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1) + "/setStatusForArea";
    }

    /**
     * 修改一个/多个位置状态
     *
     * @return
     */
    public static String SetStatusForSiteNo() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1) + "/setStatusForSiteNo";
    }

    /**
     * 给病人注射胰岛素
     *
     * @return
     */
    public static String GetInjectInsulin() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1) + "/injectInsulin";
    }

    /**
     * 加载PIO记录
     *
     * @return
     */
    public static String LoadPioRecords() {
        return GetBaseUrl() + "/pio/loadPioFromPatientID";
    }

    /**
     * 记录PIO数据
     *
     * @return
     */
    public static String RecordPio() {
        return GetBaseUrl() + "/pio/addProblemCauseTargetMeasure";
    }

    /**
     * 删除并删除pio记录
     *
     * @return
     */
    public static String delAndModPio() {
        return GetBaseUrl() + "/pio/delAndModPio";
    }

    /**
     * 加载问题列表
     *
     * @return
     */
    public static String LoadProblemDict() {
        return GetBaseUrl() + "/pio/loadItemsProblem";
    }

    /**
     * 根据问题代码加载致因
     *
     * @return
     */
    public static String LoadCausesDict() {
        return GetBaseUrl() + "/pio/loadItemsCausesByProblem";
    }

    /**
     * 根据问题代码加载目标
     *
     * @return
     */
    public static String LoadTargetDict() {
        return GetBaseUrl() + "/pio/loadItemsTargetByProblem";
    }

    /**
     * 根据问题代码加载措施
     *
     * @return
     */
    public static String LoadMeasure() {
        return GetBaseUrl() + "/pio/loadItemsMeasureByProblem";
    }

    /**
     * 获取评价字典
     *
     * @return
     */
    public static String LoadAppraisal() {
        return GetBaseUrl() + "/pio/loadItemsAppraisl";
    }

    /**
     * 评价PIO
     *
     * @return
     */
    public static String RecordAppraisal() {
        return GetBaseUrl() + "/pio/addAppraisal";
    }

    /**
     * 加载pio列表
     *
     * @return
     */
    public static String loadPioFromPatientID() {
        return GetBaseUrl() + "/pio/loadPioFromPatientID";
    }

    /**
     * 加载病人数据
     *
     * @return
     */
    public static String LoadPatientById() {
        return GetBaseUrl() + "/patient/scanningPatientWristbands";
    }

    /**
     * 根据护理任务id获取护理记录
     *
     * @return
     */
    public static String LoadPlanNursingRec() {
        return GetBaseUrl() + "/pat/loadPlanNursingRec";
    }

    /**
     * 血糖异常处理界面
     *
     * @return
     */
    public static String CompleteGlucoseValCheck() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1)
                + "/recordGlucoseValHandle";
    }

    /**
     * 获取最近一次血糖值
     *
     * @return
     */
    public static String LoadLastGlucoseValCheckVal() {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(1)
                + "/loadLastGlucoseValCheckVal";
    }

    /**
     * 记录整体体征
     *
     * @return
     */
    public static String RecordSigns() {
        return GetBaseUrl() + "/signs/recordSigns";
    }

    /**
     * 加载整体体征
     *
     * @return
     */
    public static String LoadSigns() {
        return GetBaseUrl() + "/signs/loadSigns";
    }

    /**
     * 记录巡视时间
     *
     * @param type
     * @return
     */
    public static String RecordInspectionTime(int type) {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                + "/recordInspectionTime";
    }

    /**
     * 加载病人时间段的计划数
     *
     * @return
     */
    public static String LoadPatientPlanCount() {
        return GetBaseUrl() + "/patient/loadNursingPlanRecordByPatId";
    }

    /**
     * 加载异常字典
     *
     * @param type
     * @return
     */
    public static String LoadVariationDict(int type) {
        return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                + "/loadVariationDict";
    }

    /**
     * 通知信息接口 http://10.0.0.143:8080/mnis/notice/loadNotices?
     */
    public static String GetTipMsg() {
        return GetBaseUrl() + "/notice/loadNotices?";
    }

    /**
     * Tip处理结果返回接口url http://10.0.0.143:8080/mnis/notice/handler?
     */
    public static String PostTipResult() {
        return GetBaseUrl() + "/notice/handler?";
    }

    /**
     * 静脉给药 获取历史记录
     *
     * @return
     */
    public static String LoadCompletedIntravs(int type) {
        if (type == 0)
            return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                    + "/loadCompletedIntravsByPatId";
        else
            return GetBaseUrl() + "/" + GetWorkTypeRoot(type)
                    + "/loadCompletedInsulins";
    }

    /**
     * 加载异常字典表
     *
     * @return
     */
    public static String loadExe() {
        return GetBaseUrl() + "/order/loadExe";
    }

    /**
     * 存入异常
     *
     * @return
     */
    public static String insetExe() {
        return GetBaseUrl() + "/order/insetExe";
    }

    /**
     * 存入异常
     *
     * @return
     */
    public static String executeToNoPerformed() {
        return GetBaseUrl() + "/order/executeNoPerformed";
    }

    /**
     * 存入评论
     *
     * @return
     */
    public static String insertAppriasl() {
        return GetBaseUrl() + "/order/insertAppriasl";
    }

    /**
     * 存入皮试结果
     *
     * @return
     */
    public static String insertSkinResult() {
        return GetBaseUrl() + "/order/insertSkinResult";
    }

    /**
     * 加载评论
     *
     * @return
     */
    public static String LoadAppriaslRecord() {
        return GetBaseUrl() + "/order/loadAppriaslRecord";
    }

    /**
     * 加载结束时间
     *
     * @return
     */
    public static String executeToEndTime() {
        return GetBaseUrl() + "/order/executeToEndTime";
    }

    /**
     * 胰岛素药品列表
     *
     * @return
     */
    public static String LoadPatInsulinPlansByPatId() {
        return GetBaseUrl() + "/insulin/loadPatInsulinPlansByPatId";
    }

    /**
     * 获取最近一次用餐时间
     *
     * @return
     */
    public static String LoadLastDinnerTime() {
        return GetBaseUrl() + "/insulin/loadLastDinnerTime";
    }

    /**
     * 获取病人基础信息
     *
     * @return
     */
    public static String LoadPatient() {
        return GetBaseUrl() + "/patient/loadPatient";
    }

    /**
     * 加载病人工作项目
     *
     * @return
     */
    public static String LoadNursingPlanForCategoryRecord() {
        return GetBaseUrl() + "/patient/loadNursingPlanForCategoryRecord";
    }

    /**
     * 加载病人工作项目 数量
     *
     * @return
     */
    public static String LoadTaskByCategoryRecord() {
        return GetBaseUrl() + "/patient/loadTaskByCategoryRecord";
    }

    /**
     * 获取PIO
     *
     * @return
     */
    public static String GetPio() {
        return GetBaseUrl() + "/pio/loadPioFromPatientID?";
    }

    /**
     * 获取引流数据
     *
     * @return
     */
    public static String LoadYinLiu() {
        return GetBaseUrl() + "/threeTest/loadOtherProject";
    }

    /**
     * 加载输血快照
     *
     * @return
     */
    public static String LoadKuaiZhao() {
        return GetBaseUrl() + "/transfusion/loadTransfusionsSnapshot?username=";
    }

    /**
     * 加载血品列表
     *
     * @return
     */
    public static String LoadBloodProduct() {
        return GetBaseUrl() + "/transfusion/loadTransfusions?patId=";
    }

    /**
     * 上传3查8对结果
     *
     * @return
     */
    public static String UpLoadThreeEight() {
        return GetBaseUrl() + "/transfusion/checkBloodBagInfo";
    }

    /**
     * 获得药物过敏数据
     *
     * @return
     */
    public static String LoadDrugsAllergy() {
        return GetBaseUrl() + "/threeTest/loadDrugsAllergy";
    }

    /**
     * 保存输血的tpr数据
     */
    public static String saveTPRDate() {
        return GetBaseUrl() + "/transfusion/recordTPR";
    }

    /**
     * 保存输血的滴速数据
     */
    public static String saveSpeedDate() {
        return GetBaseUrl() + "/transfusion/recSpeed";
    }

    /**
     * 获得其他体温单其他栏目
     *
     * @return
     */
    public static String LoadOtherProject() {
        return GetBaseUrl() + "/threeTest/loadOtherProject";
    }

    /**
     * 保存体温单
     *
     * @return
     */
    public static String saveThreeTest() {
        return GetBaseUrl() + "/threeTest/saveThreeTest";
    }

    /**
     * 加载体温单
     *
     * @return
     */
    public static String loadThreeTest() {
        return GetBaseUrl() + "/threeTest/loadThreeTest";
    }

    /**
     * 修改体温单
     *
     * @return
     */
    public static String modifyThreeTest() {
        return GetBaseUrl() + "/threeTest/modifyThreeTest";
    }

    /**
     * 加载输血开始时间和下次巡视时间
     *
     * @return
     */
    public static String LoadEsTimeAndIsTime() {
        return GetBaseUrl() + "/transfusion/loadEsTimeAndIsTime?username=";
    }

    /**
     * 记录下次巡视时间
     *
     * @return
     */
    public static String RecInspectionTime() {
        return GetBaseUrl() + "/transfusion/recordInspectionTime?username=";
    }

    /**
     * 获得输血观察字典表
     */
    public static String GetLookObserve() {
        return GetBaseUrl() + "/transfusion/loadObserveDict";
    }

    /**
     * 开始输血
     */
    public static String StartTrasfusion() {
        return GetBaseUrl() + "/transfusion/startTransfusion?username=";
    }

    /**
     *记录输血异常
     */
    public static String RecInspectDate() {
        return GetBaseUrl() + "/transfusion/recInspectResult";
    }

    /**
     * 记录正常
     * @return
     */
    public static String recNormal() {
        return GetBaseUrl() + "/transfusion/recNormal";
    }

    /**
     * 记录下次巡视时间
     */
    public static String RecordInspectionTime() {
        return GetBaseUrl() + "/transfusion/recordInspectionTime?username=";
    }

    /**
     * 加载冲管相关医嘱
     */
    public static String LoadIntravsByPatId() {
        return GetBaseUrl() + "/intrav/loadIntravsForFlushingByPatId?patId=";
    }


    /**
     * 记录关联的冲管医嘱任务
     */
    public static String RecRelatedPlanOrderNo() {
        return GetBaseUrl() + "/transfusion/recRelatedPlanOrderNo?username=";
    }

    /**
     * 完成输血
     */
    public static String CompliteTransfusion() {
        return GetBaseUrl() + "/transfusion/compliteTransfusion?username=";
    }

    /**
     * 结束输血流程
     */
    public static String EndProcess() {
        return GetBaseUrl() + "/transfusion/endProcess?username=";
    }

    /**
     * 加载正在执行过程中输血医嘱的状态 status : 值为1 表示正在输血 ；值为 0 表示正在冲管
     */
    public static String LoadPlanStatus() {
        return GetBaseUrl() + "/transfusion/loadPlanStatus?patId=";
    }

    /**
     * 加载正在冲管的医嘱信息
     */
    public static String LoadIntravsByPlanOrderNo() {
        return GetBaseUrl() + "/intrav/loadIntravsByPlanOrderNo?patId=";
    }

    /**
     * 加载tpr的历史记录
     *
     * @return
     */
    public static String loadTprHistory() {
        return GetBaseUrl() + "/transfusion/loadTprRec";
    }

    /**
     * 加载speed的历史记录
     *
     * @return
     */
    public static String loadSpeedHistory() {
        return GetBaseUrl() + "/transfusion/loadSpeedRec";
    }

    /**
     * 结束冲管医嘱
     */
    public static String CompleteInfusion() {
        return GetBaseUrl() + "/intrav/completeInfusion?patId=";
    }

    /**
     * 开始冲管医嘱
     */
    public static String StartInfusion() {
        return GetBaseUrl() + "/intrav/startInfusion?patId=";
    }

    /**
     * 开始静脉给药
     */
    public static String StartInfusion2() {
        return GetBaseUrl() + "/intrav/startInfusion";
    }

    /**
     * 加载血品 根据 血品PlanOrderNo
     */
    public static String LoadTransfusionByPlanOrderNo() {
        return GetBaseUrl()
                + "/transfusion/loadTransfusionByPlanOrderNo?patId=";
    }

    /**
     * 加载icu io关联医嘱
     *
     * @return
     */
    public static String loadICUOrders() {
        return GetBaseUrl() + "/icu/loadICUOrders";
    }

    /**
     * 加载icu 反面的字典表
     *
     * @return
     */
    public static String loadIntensiveCareBItems() {
        return GetBaseUrl() + "/icu/loadIntensiveCareBItems";
    }

    /**
     * 加载icu 检查字典表
     *
     * @return
     */
    public static String loadInspection() {
        return GetBaseUrl() + "/icu/loadInspection";
    }

    /**
     * 保存icu 正面
     *
     * @return
     */
    public static String saveICUA() {
        return GetBaseUrl() + "/icu/saveICUA";
    }

    /**
     * 更新 icu 正面
     *
     * @return
     */
    public static String updateICUA() {
        return GetBaseUrl() + "/icu/updateICUA";
    }

    /**
     * 查找icu 正面
     *
     * @return
     */
    public static String searchICUA() {
        return GetBaseUrl() + "/icu/searchICUA";
    }

    /**
     * 保存icu 反面
     *
     * @return
     */
    public static String saveICUB() {
        return GetBaseUrl() + "/icu/saveICUB";
    }

    /**
     * 保存icu反面 nursing measure
     *
     * @return
     */
    public static String saveICUBNursingMeasure() {
        return GetBaseUrl() + "/icu/saveICUBNursingMeasure";
    }

    /**
     * 更新icu 反面
     *
     * @return
     */
    public static String updateICUBRecord() {
        return GetBaseUrl() + "/icu/updateICUB";
    }

    /**
     * 保存icu 反面 nursing plan
     *
     * @return
     */
    public static String saveICUBNursingEvaluation() {
        return GetBaseUrl() + "/icu/saveICUBNursingEvaluation";
    }

    /**
     * 保存icu 反面 nursing plan
     *
     * @return
     */
    public static String saveICUBNursingPlan() {
        return GetBaseUrl() + "/icu/saveICUBNursingPlan";
    }

    /**
     * 更新icu 反面 nursing plan
     *
     * @return
     */
    public static String updateICUBNursingPlan() {
        return GetBaseUrl() + "/icu/updateICUBNursingPlan";
    }

    /**
     * 更新 icuB nursing measure
     *
     * @return
     */
    public static String updateICUBNursingEvaluation() {
        return GetBaseUrl() + "/icu/updateICUBNursingEvaluation";
    }

    /**
     * 更新 icuB nursing measure
     *
     * @return
     */
    public static String updateICUBNursingMeasure() {
        return GetBaseUrl() + "/icu/updateICUBNursingMeasure";
    }

    /**
     * 查找icu 反面
     *
     * @return
     */
    public static String searchICUB() {
        return GetBaseUrl() + "/icu/searchICUB";
    }

    /**
     * 查找icu 反面
     *
     * @return
     */
    public static String searchICUBNursingPlan() {
        return GetBaseUrl() + "/icu/searchICUBNursingPlan";
    }

    /**
     * 查找icu 反面
     *
     * @return
     */
    public static String searchICUBNursingMeasure() {
        return GetBaseUrl() + "/icu/searchICUBNursingMeasure";
    }

    /**
     * 查找icu 反面
     *
     * @return
     */
    public static String searchICUBEvaluation() {
        return GetBaseUrl() + "/icu/searchICUBEvaluation";
    }

    /**
     * 加载静脉给药异常字典表
     *
     * @return
     */
    public static String loadIntravenousException() {
        return GetBaseUrl() + "/intrav/loadVariationDict";
    }

    /**
     * 加载巡视时间
     */
    public static String loadInspectionTime() {
        return GetBaseUrl() + "/intrav/loadInspectionTime";
    }

    /**
     * 根据条件查询相关的输血医嘱数据
     *
     * @return
     */
    public static String loadTransfusionByConditions() {
        return GetBaseUrl() + "/transfusion/loadTransfusionByConditions";
    }

    /**
     * 传回planorderno判断是哪个流程
     */
    public static String getOrderScan() {
        return GetBaseUrl() + "/order/scanDealOrder";
    }

    /**
     * 加载已完成血糖记录
     *
     * @return
     */
    public static String loadGlucoseResults() {
        return GetBaseUrl() + "/insulin/loadGlucoseResults?patId=";
    }
    /**
     * 加载医嘱筛选条件
     *
     * @return
     */
    public static String loadOrderClass() {
        return GetBaseUrl() + "/order/loadOrderClass";
    }

    /**
     * 加载血袋信息
     *
     * @return
     */
    public static String loadBloodTransByPlanOrderNo() {
        return GetBaseUrl() + "/transfusion/loadBloodTransByPlanOrderNo?patId=";
    }

    /**
     * 加载血袋码编码规则
     *
     * @return
     */
    public static String loadBloodCodeRule() {
        return GetBaseUrl() + "/sys/configRule";
    }

    /**
     * 加载通知数量
     *
     * @return
     */
    public static String loadNotificationNum() {
        return GetBaseUrl() + "/notice/noticeCount?username=";
    }

    /**
     * 加载当前病人数据
     *
     * @return
     */
    public static String loadCurrentPatient() {
        return GetBaseUrl() + "/patient/scanningPatientWristbandsCode?username=";
    }

    /**
     * 加载静脉给药医嘱根据playorderno
     *
     * @return
     */
    public static String loadIntrraveslyData() {
        return GetBaseUrl() + "/notice/noTransNotice?username=";
    }

    /**
     * 加载输血医嘱根据playorderno&bloodid
     *
     * @return
     */
    public static String loadBloodNoticeDetail() {
        return GetBaseUrl() + "/notice/loadBloodNoticeDetail?username=";
    }

    public static String loadOrdersByRelatedBarcode() {
        return GetBaseUrl() + "/order/scanTotal?username=";
    }

    /**
     * 根据bloodDonorCode加载信息
     *
     * @return
     */
    public static String loadOrdersByBloodDonorCode() {
        return GetBaseUrl() + "/order/scanBlood?username=";
    }

    /**
     * 加载医嘱列表界面医嘱信息
     *
     * @return
     */
    public static String loadNewOrders() {
        return GetBaseUrl() + "/order/loadNewOrders?username=";
    }

    /**
     * 加载执行记录信息
     *
     * @return
     */
    public static String loadImplementationRecor() {
        return GetBaseUrl() + "/order/getRec?patId=";
    }


    /**
     * 加载胰岛素注射位置
     *
     * @return
     */
    public static String loadInjection() {
        return GetBaseUrl() + "/insulin/loadInjection?username=";
    }

    /**
     * 更新胰岛素注射位置状态
     *
     * @return
     */
    public static String updateInjection() {
        return GetBaseUrl() + "/insulin/updateInjection?username=";
    }

    /**
     * 重新加载模板
     *
     * @return
     */
    public static String reloadInjection() {
        return GetBaseUrl() + "/insulin/reloadInjection?username=";
    }

    /**
     * 加载最后一个血糖值
     *
     * @return
     */
    public static String getBlood() {
        return GetBaseUrl() + "/insulin/getBlood?username=";
    }

    /**
     * 开始胰岛素
     *
     * @return
     */
    public static String startInsulin() {
        return GetBaseUrl() + "/insulin/startInsulin?username=";
    }

    /**
     * 巡视
     */
    public static String insulinPatrol() {
        return GetBaseUrl() + "/insulin/recInspectResult?username=";
    }

    /**
     * 加载条码规则
     */
    public static String getBarcodeDict() {
        return GetBaseUrl() + "/sys/barcodeDict";
    }

    /**
     * 加载医嘱限制时间
     */
    public static String getOrderLimitedTime() {
        return GetBaseUrl() + "/sys/getOrderLimitedTime?appName=";
    }
    /**
     *
     *
     * @return
     */
    public static String loadNewBlood() {
        return GetBaseUrl() + "/transfusion/loadNewTransfusions?username=";
    }

    /**
     * 医嘱批量执行
     *
     * @return
     */
    public static String batchExecuted() {
        return GetBaseUrl() + "/batch/normalDC?username=";
    }

    /**
     * 遗嘱执行详情界面录入巡视记录
     *
     * @return
     */
    public static String addAllRecord() {
        return GetBaseUrl() + "/order/allRecord?username=";
    }
    /**
     * 医嘱取消执行
     *
     * @return
     */
    public static String cancelOrder() {
        return GetBaseUrl() + "/order/cancelOrder?username=";
    }
    /**
     * 全部未执行医嘱
     *
     * @return
     */
    public static String loadAllNotExecute() {
        return GetBaseUrl() + "/order/loadAllNotExecute?patId=";
    }
    /**
     * 输血暂停
     *
     * @return
     */
    public static String pausedTrans() {
        return GetBaseUrl() + "/transfusion/pausedTrans?patId=";
    }

    /**
     * 记录失效rec
     * @return
     */
    public static String validityRec(){
        return GetBaseUrl() + "/transfusion/validityRec?patId=";
    }

    /**
     * 是否暂停输血
     *
     * @return
     */
    public static String isPausedTrans() {
        return GetBaseUrl() + "/transfusion/querySusperendAttr?patId=";
    }

    /**
     * 加载执行核对医嘱
     * @return
     */
    public static String loadCheckOrders(){
        return GetBaseUrl()+"/order/loadCheckOrders?username=";
    }

    /**
     * 通过医嘱码找病人
     * @return
     */
    public static String findPatIdByBarcode(){
        return GetBaseUrl()+"/order/findPatIdByBarcode?barCode=";
    }

    /**
     * 医嘱核对
     * @return
     */
    public static String checkOrder(){
        return GetBaseUrl()+"/order/checkOrder?patId=";
    }

    /**
     * 巡视记录
     * @return
     */
    public static String patrolRecord(){
        return GetBaseUrl()+"/order/patrolRecord?patId=";
    }

    /**
     * 加载Msg
     * @return
     */
    public static String loadNoticesByConformStatus(){
        return GetBaseUrl()+"/notice/loadNoticesByConformStatus?username=";
    }

    public static String  downloaderApk(){
        return GetBaseUrl()+"/sys/downloaderApk";
    }

    /**
     * 血品核收
     * @return
     */
    public static String bloodBagNuclearChange(){
        return GetBaseUrl()+"/transfusion/modifyCollect?username=";

    }

    /**
     * 加载待核收
     * @return
     */
    public static String collect(){
        return GetBaseUrl()+"/transfusion/collect?wardCode=";
    }

    /**
     * 加载巡视历史记录
     * @return
     */
    public static String getNursingPatrolHistroy() {
        return GetBaseUrl() + "/sys/getNursingPatrolHistroy?patId=";
    }

    /**
     * 保存护理巡视
     * @return
     */
    public static String saveNursingPatrol() {
        return GetBaseUrl() + "/sys/saveNursingPatrol?patId=";
    }

    /**
     * 加载护理巡视字典
     * @return
     */
    public static String loadNursingPatrolDict() {
        return GetBaseUrl() + "/sys/loadNursingPatrolDict?";
    }

    /**
     * 保存体征信息
     * @return
     */
    public static String saveSigns() {
        return GetBaseUrl() + "/threeTest/saveThreeTest?patId=";
    }
    
    /**
     * 计费
     *
     * @return
     */
    public static String callProcedureBill() {
        return GetBaseUrl() + "/order/callProcedureBill?patId=";
    }

    /**
     * 退回血库
     *
     * @return
     */
    public static String bloodBack() {
        return GetBaseUrl() + "/transfusion/bloodBack?username=";
    }

    /**
     * 切换科室
     *
     * @return
     */
    public static String changeCurrentDept() {
        return GetBaseUrl() + "/sys/changeCurrentDept?userName=";
    }

    /**
     * 公共记录扫码
     *
     * @return
     */
    public static String commonRec() {
        return GetBaseUrl() + "/rec/commonRec?userName=";
    }
}
