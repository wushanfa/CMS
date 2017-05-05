package com.gentlehealthcare.mobilecare.db.table;


import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable
public class TB_Patient implements Serializable{
    @DatabaseField(generatedId = true)
    private int patId;
	/**病人id**/
	@DatabaseField
	private String patientId;

	/**病床号**/
	@DatabaseField
	private String bedNo;
    /**病床标签*/
    @DatabaseField
    private String bedLabel;

	/**病人姓名**/
	@DatabaseField
	private String name;

	/**病人性别**/
	@DatabaseField
	private String sex;

	/**病人年龄**/
	@DatabaseField
	private String age;

	/**病人身高**/
	@DatabaseField
	private String height;

	/**病人体重**/
	@DatabaseField
	private String weight;

	/**病人血型**/
	@DatabaseField
	private String bloodType;

	/**疾病名称**/
	@DatabaseField
	private String disease;
    /**疾病编号*/
    @DatabaseField
    private String diagCode;

	/**病人患病历史**/
	@DatabaseField
	private String diseaseHistory;

	/**主治医生**/
	@DatabaseField
	private String doctor;

	/**给药建议、说明**/
	@DatabaseField
	private String advise;

	/**护士ID**/
	@DatabaseField
	private String nurseId;

//	/**药品编码，根据编码从数据库获取药品**/
//	@DatabaseField
	private String medicineCode;

	/**护理工作类型，如"给药", "注射", "输血", "胰岛素"**/
	@DatabaseField
	private String type;

	/**护理工作状态，如待、中、已**/
	@DatabaseField
	private int state;//0等待  1进行中 2已完成

//	/**护理工作开始时间**/
//	@DatabaseField
//	private String startTime;

	/**护理等级**/
	@DatabaseField
	private String grade;
	@DatabaseField
	private int patientType;//0护理单元病人  1 部门病人
    /**单元名称*/
    @DatabaseField
    private String wardName;
   /**单元编号*/
    @DatabaseField
    private String wardCode;
   /** 病人编号*/
    @DatabaseField
    private String patCode;
    /**部门编号*/
    @DatabaseField
    private String deptCode;
    /**部门名称*/
    @DatabaseField
    private String deptName;
    /**入院日期*/
    @DatabaseField
    private String admissionDate;


	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getBedNo() {
		return bedNo==null?"":bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getName() {
		return name==null?"":name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
        if (age==null)
            return "";
		return age;
	}

	public void setAge(String age) {
		if (age==null)
            age="";
        this.age = age;
	}

	public String getHeight() {
		return height==null?"":height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight==null?"":weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getBloodType() {
		return bloodType==null?"":bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getDisease() {
		return disease==null?"":disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getDiseaseHistory() {
        if (diseaseHistory==null)
            return "";
		return diseaseHistory;
	}

	public void setDiseaseHistory(String diseaseHistory) {
		if (diseaseHistory==null)
            diseaseHistory="";
        this.diseaseHistory = diseaseHistory;
	}

	public String getDoctor() {
		return doctor==null?"":doctor;
	}

	public void setDoctor(String doctor) {
		if (doctor==null)
            doctor="";
        this.doctor = doctor;
	}

	public String getAdvise() {
		return advise;
	}

	public void setAdvise(String advise) {
		this.advise = advise;
	}

	public String getNurseId() {
		return nurseId;
	}

	public void setNurseId(String nurseId) {
		this.nurseId = nurseId;
	}

	public String getMedicineCode() {
		return medicineCode;
	}

	public void setMedicineCode(String medicineCode) {
		this.medicineCode = medicineCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

//	public String getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(String startTime) {
//		this.startTime = startTime;
//	}


	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getPatientType() {
		return patientType;
	}

	public void setPatientType(int patientType) {
		this.patientType = patientType;
	}


    public String getBedLabel() {
        return bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }

    public String getDiagCode() {
        return diagCode;
    }

    public void setDiagCode(String diagCode) {
        this.diagCode = diagCode;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getPatCode() {
        return patCode;
    }

    public void setPatCode(String patCode) {
        this.patCode = patCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public int getPatId() {
        return patId;
    }

    public void setPatId(int patId) {
        this.patId = patId;
    }

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}
}
