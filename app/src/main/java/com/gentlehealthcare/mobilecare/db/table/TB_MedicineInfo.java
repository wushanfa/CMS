package com.gentlehealthcare.mobilecare.db.table;

import android.text.TextUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class TB_MedicineInfo implements Serializable {
	@DatabaseField(generatedId=true)
	private int id;
	/** 药品ID **/
	@DatabaseField
	private String medicineId;

	/** 药品编码 **/
	@DatabaseField
	private String medicineCode;

	/** 药品名称  orderText**/
	@DatabaseField
	private String medicineName;


	/** 药品疗效  nursingDesc **/
	@DatabaseField
	private String effect;

	/** 药品注意事项 **/
	@DatabaseField
	private String notice;

	/** 药品状态，见MedicineConstant.java **/

	private String state;
	
	private String medicineState;
    /**
     * 该药品是否为组液
     */
    private String group;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    private String starttime;

    /**剂量*/
    private String dosage;
    /**剂量单位*/
    private String dosageUnits;
    /**给药频次*/
    private String frequency;

    private String name;//操作人姓名

@DatabaseField
    private String isSolvent;//是否为溶剂 1-溶剂 0 溶质
    @DatabaseField
    private String skin_test;//是否需要皮试 1-需要 0 不需要

	private String endTime;//结束时间
@DatabaseField
	private String panId;//计划ID

	private String speed;//滴速
	private String speedUnits;//滴速单位
	private  String planTimeAttr;//0 临时 1饭前 2饭后
	private String templateId;//AA-3 胰岛素  AA-4血糖测试
	private String templateName;

	private String injectionTool;//0 留置针  1钢针




	public String getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(String medicineId) {
		this.medicineId = medicineId;
	}

	public String getMedicineCode() {
		return medicineCode;
	}

	public void setMedicineCode(String medicineCode) {
		this.medicineCode = medicineCode;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getNotice() {
		if (notice==null)
            return "";
        return notice;
	}

	public void setNotice(String notice) {
		if (notice==null)
            notice="";
        this.notice = notice;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMedicineState() {
		return medicineState;
	}

	public void setMedicineState(String medicineState) {
		this.medicineState = medicineState;
	}




    public String getDosage() {
        return dosage==null?"":dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDosageUnits() {
        return dosageUnits==null?"":dosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        this.dosageUnits = dosageUnits;
    }

    public String getFrequency() {
        return frequency==null?"":frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIsSolvent() {
        return isSolvent;
    }

    public void setIsSolvent(String isSolvent) {
        this.isSolvent = isSolvent;
    }

    public String getSkin_test() {
        return skin_test;
    }

    public void setSkin_test(String skin_test) {
        this.skin_test = skin_test;
    }

    public String getName() {

		return name==null?"":name;
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

	public String getPanId() {
		return panId;
	}

	public void setPanId(String panId) {
		this.panId = panId;
	}

	public String getSpeed() {
		return TextUtils.isEmpty(speed)?"1":speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getSpeedUnits() {
		return speedUnits==null?"":speedUnits;
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

	public String getInjectionTool() {
		return injectionTool==null?"1":injectionTool;
	}

	public void setInjectionTool(String injectionTool) {
		this.injectionTool = injectionTool;
	}
}
