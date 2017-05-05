package com.gentlehealthcare.mobilecare.constant;

import java.io.Serializable;

public class FlowConstant {

	public static enum Flow {

		/** 不执行 */
		NOT_EXECUTION,

		/** 身份扫描 **/
		IDENTITY_SCAN,

		/** 身份扫描成功，确认身份 **/
		IDENTITY_SCAN_SUCCESSED,

		/** 身份扫描失败，选择重新扫描或返回 **/
		IDENTITY_SCAN_FAILED,

		/** 用药解释说明 **/
		EXPLAIN,

		/** 药物列表 **/
		MEDICINE_LIST,

		/** 药物扫描成功，选择使用或不使用 **/
		MEDICINE_SCAN_SUCCESSED,

		/** 药物扫描失败，选择重新扫描或返回 **/
		MEDICINE_SCAN_FAILED,

		/** 药物已使用 **/
		MEDICINE_USED,

		/** 药物未使用，选择未使用的原因 **/
		MEDICINE_UNUSED,

		/** 注射液扫描成功，选择注射或不注射 **/
		INTRADERMIC_SCAN_SUCCESSED,

		/** 注射液扫描失败，选择重新扫描或返回 **/
		INTRADERMIC_SCAN_FAILED,

		/** 注射液注射,调节输液速度 **/
		INFLUSION_EXECUTED,

		/** 注射液未注射 ，选择未注射的原因 **/
		INFLUSION_NOT_EXECUTED,

		/** 巡视时扫描病人身份 **/
		PATROL_SCAN,

		/** 巡视时扫描病人身份成功 **/
		PATROL_SCAN_SUCCESSED,

		/** 巡视计时 **/
		PATROL_COUNT_DOWN,

		/** 巡视记录 **/
		PATROL_RECORD,

		/** 封管 **/
		SEALING
	};
	
	
	public  enum PatientFlow implements Serializable{
		/**身份确认*/
		IDENTITY_CONFIRM,
		/** 身份扫描 **/
		IDENTITY_SCAN,
		/**不执行*/
		NOT_EXECUTION,
		/** 身份扫描 **/
		IDENTITY_OK,
		/** 用药解释说明 **/
		EXPLAIN,
		/** 药物列表 **/
		MEDICINE_LIST,
        /**药物结束*/
		MEDICINE_END,

        /**胰岛素注射部位选定*/
        CHOOSESITE,
        /**胰岛素注射部位*/
        INJECTIONSITE,

        /** 药物简介**/
		MEDICINE_INTRODUCTION,
        /**药品信息核对*/
        MEDICINE_CHECK,
		/**药品注射*/
		MEDICINE_INJECTION,
        /**药品巡视*/
        MEDICINE_PATORL,
		/** 药品取消 */
		MEDICINE_CANCEL,
		/** 注射液扫描成功**/
		INTRADERMIC_SCAN,
		INTRADERMIC_CANCEL,
		
		/** 巡视计时 **/
		PATROL_COUNT_DOWN,
		/** 巡视记录 **/
		PATROL_RECORD,
		/**待执行  的巡视倒计时*/
		MEDICINE_COUT_DOWN,
        /**血糖检测*/
        BLOOD_TEST,
        /**血糖检测异常*/
        BLOOD_EXECPITON,

		/** 封管 **/
		SEALING
	};

}
