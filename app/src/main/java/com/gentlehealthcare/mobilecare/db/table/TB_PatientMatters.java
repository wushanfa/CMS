package com.gentlehealthcare.mobilecare.db.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * 
 * @ClassName: TB_PatientMatters 
 * @Description:病人事项表
 * @author ouyang
 * @date 2015年3月10日 下午2:16:50 
 *
 */
@DatabaseTable
public class TB_PatientMatters {
	@DatabaseField(generatedId=true)
	private int itemNo;
	@DatabaseField
	private String patientId;
	@DatabaseField
	private String wordCode;
	@DatabaseField
	private String wordName;
	@DatabaseField
	private String wordAlias;

	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getWordCode() {
		return wordCode;
	}
	public void setWordCode(String wordCode) {
		this.wordCode = wordCode;
	}
	public String getWordName() {
		return wordName;
	}
	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	public String getWordAlias() {
		return wordAlias;
	}
	public void setWordAlias(String wordAlias) {
		this.wordAlias = wordAlias;
	}


    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }
}
