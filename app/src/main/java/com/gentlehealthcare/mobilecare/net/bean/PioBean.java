package com.gentlehealthcare.mobilecare.net.bean;


/**
 * PIO 实体类 Created by ouyang on 15/7/3.
 */
public class PioBean {
	private String problemCode;

	public PioBean(String problemCode, int type) {
		this.problemCode = problemCode;
		this.type = type;
	}

	private int type;// 0 问题 1致因 2目标 3措施

	public String getProblemCode() {
		return problemCode;
	}

	public void setProblemCode(String problemCode) {
		this.problemCode = problemCode;
	}

	@Override
	public String toString() {
		return "?relatedItemcode=" + getProblemCode();
	}

	public int getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            0问题 1致因 2目标 3措施
	 */
	public void setType(int type) {
		this.type = type;
	}
}
