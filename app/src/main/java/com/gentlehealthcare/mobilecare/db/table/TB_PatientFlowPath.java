package com.gentlehealthcare.mobilecare.db.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * 
 * @ClassName: TB_PatientFlowPath 
 * @Description:病人流程说明
 * @author ouyang
 * @date 2015年3月11日 下午12:55:10 
 *
 */
@DatabaseTable
public class TB_PatientFlowPath {
	@DatabaseField(generatedId=true)
	private int flowpathId;
	@DatabaseField
	private String patientname;
	@DatabaseField
	private String updatetime;
	@DatabaseField
	private String flowpath;
	public int getFlowpathId() {
		return flowpathId;
	}
	public void setFlowpathId(int flowpathId) {
		this.flowpathId = flowpathId;
	}
	public String getPatientname() {
		return patientname;
	}
	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getFlowpath() {
		return flowpath;
	}
	public void setFlowpath(String flowpath) {
		this.flowpath = flowpath;
	}
}
