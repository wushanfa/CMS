package com.gentlehealthcare.mobilecare.db.table;

import com.j256.ormlite.field.DatabaseField;

/**
 * 
 * @ClassName: TB_GiveMedicineRecord
 * @Description: 给药记录
 * @author ouyang
 * @date 2015年3月12日 下午4:24:22
 *
 */
public class TB_GiveMedicineRecord {
	@DatabaseField(generatedId=true)
	private int gmRecordId;
	/** 病床号 **/
	@DatabaseField
	private String bedNo;

	/** 病人姓名 **/
	@DatabaseField
	private String name;
	/** 药品名称 */
	@DatabaseField
	private String medicineName;
    /**药品标号*/
    @DatabaseField
    private String medicineCode;
	/** 给药时间 */
	@DatabaseField
	private long startTime;
	/** 巡视时间 */
	@DatabaseField
	private long visitsTime;
	/**病人ID*/
	@DatabaseField
	private String patientId;

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getVisitsTime() {
		return visitsTime;
	}

	public void setVisitsTime(long visitsTime) {
		this.visitsTime = visitsTime;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }
}
