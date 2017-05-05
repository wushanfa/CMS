package com.gentlehealthcare.mobilecare.constant;

import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.tool.DateTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量定义
 *
 * @author HeRen_zyy
 * @ClassName GlobalConstant
 * @Description 全局常量类
 */
public class GlobalConstant {
    // 是否Release发布模式
    public static final boolean RELEASE = true;
    public static final String KEY_PLANORDERNO = "planOrderNo";
    public static final String KEY_NOTICECLASS = "noticeClass";
    public static final String KEY_TEMPLATEID = "templateid";
    public static final String KEY_BLOODID = "bloodId";
    public static final String KEY_APPLYNO = "applyNo";
    public static final String KEY_ITEMNO = "itemNo";
    public static final String KEY_FLAG_SCAN = "scanflag";
    public static final String VALUE_FLAG_SCAN = "scan";
    public static final String VALUE_FLAG_EXECUTE = "execute";
    public static final String VALUE_FLAG_PATROL = "patrol";
    public static final String KEY_EXECUTE_PATROL = "executeorpatrol";
    public static final String VALUE_PRESS = "pressflag";
    public static final String VALUE_PRESS_PATROL = "presspatrolflag";
    public static final String KEY_SCANDOORDERS = "scanorders";
    public static final String KEY_SCANORDERITEMBEAN = "scanscanorders";
    public static final String KEY_FLAG_SCAN_PATROL = "scanpatrolflag";
    public static final String VALUE_FLAG_SCAN_PATROL = "scanflag";
    public static final String KEY_POSITION = "position";
    public static final String KEY_EXECUTEING_ORDERS = "executeingorders";
    public static final String SUCCEED = "succeed";
    public static final String FAILED = "failed";
    public static final String EXCEPTION = "exception";
    public static final String SAVE_SUCCEED = "保存成功";
    public static final String CORRECT = "correct";
    public static final String ERROR = "error";
    /**
     * 当前时间用来获取PIO记录筛选条件 在PioRecordsActivity界面动态修改
     */
    public static String mCurrentDate = DateTool.getCurrDate();
    /**
     * 输血界面当前的Frangment 在Frangment界面动态修改 s:TrasIdentifyScanFra
     * t:threeEightToCheckFra
     */
    public static String mCurrentTrasFra = "s";
    /**
     * 执行中条目
     */
    public static String performTask = "0";
    /**
     * 待执行条目
     */
    public static String executingTask = "0";
    /**
     * 血袋号
     */
    public static String xdh = null;
    /**
     * 有效期
     */
    public static String yxq = null;
    /**
     * 腕带
     */
    public static String wd = null;
    /**
     * 当前界面代号 :
     * 输血开始时间
     */
    public static String bloodStartTime = null;
    /**
     * 判断执行医嘱是不是属于一批
     */
    public static String BATCH_NO = null;

    /**
     * 通知跳转到输血结束和巡视界面标识
     */
    public static final String KEY_NOTIFICATION2TRANSFUSION = "nf2tr";
    public static final char VALUE_NOTIFICATION2TRANSFUSION = 'y';
    /**
     * 病人标识
     */
    public static final String KEY_PATIENT = "patient";
    /**
     * 医嘱列表bean
     */
    public static final String KEY_ORDERLISTBEAN = "orderListBean";
    public static boolean ISBATCH = false;
    public static int countOfBatchOrders = 0;
    public static boolean isSwipeOpen = false;
    public static boolean isSwipeOpenMsg = false;

    /**
     * 血品标识
     */
    public static final String KEY_BLOODPRODUCTBEAN2 = "bloodproductbean2";
    /**
     * 输血巡视界面返回标识
     */
    public static final String KEY_TRANSFUSION2NOTIFICATION = "trf2nf";
    /**
     * 静脉给药标识
     */
    public static final String KEY_ORDERITEMBEAN = "orderitembean";
    /**
     * 通知到静脉给药标识
     */
    public static final String KEY_NOTIFICATION2MEDICINE = "nf2medcine";
    /**
     * 静脉给药阶段标识
     */
    public static final String KEY_PATIENTFLOW = "flow";
    public static final String KEY_EVENTID = "eventId";
    public static final String KEY_PATID = "patId";
    /**
     * 通知到静脉给药标识
     */
    public static final int VALUE_NOTIFICATION2MEDICINE = 1000;
    /**
     * 当前界面代号 :
     * 输血巡视时间
     */
    public static String bloodPatroyTime = null;
    /**
     * icu中对比的值
     */
    public static final int FIRST_POSITION = 1;
    public static final int SECOND_POSITION = 2;
    public static final int THIRD_POSITION = 3;
    /**
     * icu的保存状态
     */
    public static final int SAVE_CONDITION = 1;
    public static final int UPDATE_CONDITION = 2;
    public static final int DELETE_CONDITION = 3;

    /**
     * 胰岛素注射部位
     */
    public static String INJECT_SITENO = null;
    public static String INJECT_SITECODE = null;
    public static char ISBACKNOTIFITION = 'n';

    public static int RESUlt_CODE = 3001;
    public static int REQUEST_CODE = 1002;

    public static boolean isSamplePerform = false;

    public static final String NOT_PERFORM = "0";
    public static final String PERFORMING = "1";
    public static final String PERFORMED = "9";
    public static final String PERFORM_WITH_EXCEPTION = "-1";

    public static final int PASS = 0;
    public static final int REFUSE = 1;

    public static final int GUO_MIN = 1;
    public static final int DI_XUE_TANG = 2;
    public static final int ZHU_SHE = 3;

    public static final int TIME = 1;
    public static final int DATE = 2;
    public static final int DATE_TIME = 13;
    public static final int DATE_TIME_SIMPLE = 14;
    public static final int DATE_TIME_PART = 15;

    public static final String KEY_ORDER_LIMITED_TIME ="orderlimitedtime";
    public static final String KEY_CEILING ="ceiling";
    public static final String KEY_FLOOR ="floor";
    public static final String KEY_SERVICE_TIME = "serviceTime";
    public static String SERVICE_TIME = "";
    /**
     * 条码相关
     */
    public static final String KEY_BARCODE = "barcode";
    public static final String KEY_BLOODPRODUCTCODE = "bloodProductCode";
    public static final String KEY_PATBARCODE = "patCode";
    public static final String KEY_LABAPPLYCODE = "labApplyCode";
   // public static final String KEY_LABAPPLYCODE = "labApplyCode";
    public static final String KEY_BLOODINVALCODE = "bloodInvalCode";
    public static final String KEY_PLANBARCODE = "planBarcode";
    public static final String KEY_BLOODDONORCODE = "bloodDonorCode";
    public static final String KEY_BLOODGROUPCODE = "bloodGroupCode";
    public static final String KEY_REQID = "keyReqId";
    public static boolean isCurrentShowAllNotExecute=false;

    public static final String KEY_FLAG = "keyflag";

    public static final String KEY_STATUS = "status";

    public static final String KEY_BLOOD = "bloodTrans";

    public static final int ORDER_PLAN_ORDER_NO = 1;
    public static final int ORDER_ORG = 0;
    public static final int ORDER_REQ_ID = 2;

    public static List<OrderListBean> tempOrderListBean = new ArrayList<OrderListBean>();

    public static String tempEventId = "";

    public static String PATID ="";
    public static String DEFAULT_TEXT ="";
    public static boolean isLoadAllNotExecuteOrToday = false;

    public static final String HAVE_CHECKED="haveChecked";
    public static final String IMPLEMENTATION="implementation";
    public static final String HAVE_BEEN_IMPLEMENTED="haveBeenImplemented";
    public static final String FLAG="flag";
    public static final String HAVE_BEEN_CANCEL="haveBeenCancel";
}
