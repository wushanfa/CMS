package com.gentlehealthcare.mobilecare.db.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TB_ExecutionWork {
    @DatabaseField(id = true)
    private String  plan_id;
	@DatabaseField
	private String orderCode;// 工作编号
	@DatabaseField
	private String orderId;// 工作ID
	@DatabaseField
	private String orderText;// 工作内容
	@DatabaseField
	private String dosage;// 药品剂量
	@DatabaseField
	private String dosageUnits;// 药品剂量单位
	@DatabaseField
	private String administration;// 管理
	@DatabaseField
	private int type;// 类型 0待执行 1 执行中 2 已执行
	@DatabaseField
	private int workType;//0 注射 1 给药 2 输血 3 胰岛素
	@DatabaseField
	private String patientId;
@DatabaseField
    private String startTime;
@DatabaseField
    private String orderNo;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderText() {
		return orderText;
	}

	public void setOrderText(String orderText) {
		this.orderText = orderText;
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

	public String getAdministration() {
		return administration;
	}

	public void setAdministration(String administration) {
		this.administration = administration;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getWorkType() {
		return workType;
	}

	public void setWorkType(int workType) {
		this.workType = workType;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }
}
