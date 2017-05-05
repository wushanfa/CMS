package com.gentlehealthcare.mobilecare.db.table;

import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * 
 * @ClassName: TB_PatientMedicine 
 * @Description: 病人药品关联表
 * @author ouyang
 * @date 2015年3月11日 下午3:15:29 
 *
 */
@DatabaseTable
public class TB_PatientMedicine{
	@DatabaseField(generatedId=true)
	private int pmId;
	
	/** 药品编码 **/
	@DatabaseField
	private String medicineCode;
	/** 药品状态，见MedicineConstant.java **/
	@DatabaseField
	private String state;//@0-待执行；1-执行开始；9-执行完成；-1取消执行
	/**病人ID*/
	@DatabaseField
	private String patientId;
    /**剂量*/
    @DatabaseField
    private String dosage;
    /**剂量单位*/
    @DatabaseField
    private String dosageUnits;
    /**开始时间*/
    @DatabaseField
    private String startTime;
    /**给药频次*/
    @DatabaseField
    private String frequency;


	/**组合药品*/
	@DatabaseField
	private String group="";
    /**药品ID*/
    @DatabaseField
    private String medicineId;
    @DatabaseField
    private String planId;//计划ID
    @DatabaseField
    private String name;//操作人姓名
    @DatabaseField
    private String endTime;//结束时间

    @DatabaseField
    private String templateId;//AA-3 胰岛素  AA-4血糖测试
    @DatabaseField
    private  String templateName;//模版名称
    @DatabaseField
    private String eventStartTime;
    @DatabaseField
    private String speed;//滴速
@DatabaseField
    private String speedUnits;//滴速单位

    @DatabaseField
    private  String planTimeAttr;//0 临时 1饭前 2饭后
    @DatabaseField
    private String injectionTool;//0 留置针  1钢针

    @DatabaseField
    private String inspectionTime;//巡视时间
	public int getPmId() {
		return pmId;
	}
	public void setPmId(int pmId) {
		this.pmId = pmId;
	}
	public String getMedicineCode() {
		return medicineCode;
	}
	public void setMedicineCode(String medicineCode) {

            this.medicineCode = medicineCode;

        }
	public String getState() {
		return state;
	}
	public void setState(String state) {
        if (state==null) {
            this.state= "";
        }else if (state.equals("0")){
            this.state= MedicineConstant.STATE_WAITING;
        }else if(state.equals(1)){
            this.state=MedicineConstant.STATE_EXECUTING;
        }else if (state.equals("9")){
            this.state=MedicineConstant.STATE_EXECUTED;
        }else if (state.equals("-1")){
            this.state=MedicineConstant.STATE_CANCEL;
        }else {
            this.state = state;
        }

	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}


    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDosageUnits() {
        return dosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        this.dosageUnits = dosageUnits;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime=eventStartTime;
//        if (eventStartTime==null||"".equals(eventStartTime))
//            this.eventStartTime="";
//        else
//            this.eventStartTime= DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS,eventStartTime)+"";
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSpeedUnits() {
        return speedUnits;
    }

    public void setSpeedUnits(String speedUnits) {
        this.speedUnits = speedUnits;
    }

    public String getPlanTimeAttr() {
        return planTimeAttr;
    }

    public void setPlanTimeAttr(String planTimeAttr) {
        this.planTimeAttr = planTimeAttr;
    }

    public String getInjectionTool() {
        return injectionTool;
    }

    public void setInjectionTool(String injectionTool) {
        this.injectionTool = injectionTool;
    }

    public String getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(String inspectionTime) {
        if (inspectionTime==null||"".equals(inspectionTime))
            this.inspectionTime="";
        else
          this.inspectionTime= DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS,inspectionTime)+"";
    }


}