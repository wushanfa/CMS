package com.gentlehealthcare.mobilecare.db.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ouyang on 2015/3/18.
 */
@DatabaseTable
public class TB_MedicineRecord {
    @DatabaseField(generatedId = true)
    private int medicineRecordId;
    @DatabaseField
    private String patientId;
    @DatabaseField
    private String medicineCode;

    @DatabaseField
    private String medicineName;

    @DatabaseField
    private int record;//0 正常 -1过慢 1过快 2异常
    @DatabaseField
    private String recordTime;

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getMedicineRecordId() {
        return medicineRecordId;
    }

    public void setMedicineRecordId(int medicineRecordId) {
        this.medicineRecordId = medicineRecordId;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
}
