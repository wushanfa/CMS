package com.gentlehealthcare.mobilecare.constant;

public class MedicineConstant {

	/** 待执行 **/
	public static final String STATE_WAITING = "0";

	/** 执行中 **/
	public static final String STATE_EXECUTING = "1";

	/** 已执行 **/
	public static final String STATE_EXECUTED = "9";
    /** 取消执行 */
    public static final String STATE_CANCEL="-1";

	/** 不执行 **/
	public static final String STATE_NOT_EXECUT = "STATE_NOT_EXECUT";
	
	/** 终止执行 **/
	public static final String STATE_STOP = "STATE_STOP";

	/** 异常 **/
	public static final String STATE_EXCEPTION = "STATE_EXCEPTION";

}
