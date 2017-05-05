package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.util.List;

/**
 * Created by ouyang on 15/7/15.
 */
public class RecordSignsBean {
    private String recordTime;
    private String temperature;//温度
    private String  pulse;//脉搏
    private String breath;//呼吸
    private String pain;//疼痛评分

    private String patId;
    private String  allergy;//过敏
    private String weight;//体重
    private String waterOut;//出水量
    private String waterIn;//进水量
    private String piss;//小便
    private String defecate;//大便
    private String sp1;//高血压 1
    private String dp1;//低血压 1
    private String sp2;//高血压 2
    private String dp2;//低血压 2
    private String sp3;//高血压 3
    private String dp3;//低血压 3
    private String sp4;//高血压 4
    private String dp4;//低血压 4
    private String pdate1;//血压时间点1
    private String pdate2;//血压时间点2
    private String pdate3;//血压时间点3
    private String pdate4;//血压时间点4
    private String drainageType;//引流液类型
    private String drainage;//引流液值
    private List<RecordSecondPageSignInfo> pressure;



    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId()+"&temperature="+getTemperature()+"&pulse="+getPulse()+"&breath="+getBreath()+"&pain="+getPain()+"&recordTime="+getRecordTime()+"&allergy="+getAllergy()+"&weight="+getWeight()+"&waterOut="+getWaterOut()+"&waterIn="+getWaterIn()+"&recordTime="+getRecordTime()
                +"&piss="+getPiss()+"&defecate="+getDefecate()+"&sp1="+getSp1()+"&sp2="+getSp2()+"&sp3="+getSp3()+"&sp4="+getSp4()
                +"&dp1="+getDp1()+"&dp2="+getDp2()+"&dp3="+getDp3()+"&dp4="+getDp4()
                +"&pdate1="+getPdate1()+"&pdate2="+getPdate2()+"&pdate3="+getPdate3()+"&pdate4="+getPdate4()+"&drainageType="+getDrainageType()+"&drainage="+getDrainage();


    }



    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWaterOut() {
        return waterOut;
    }

    public void setWaterOut(String waterOut) {
        this.waterOut = waterOut;
    }

    public String getWaterIn() {
        return waterIn;
    }

    public void setWaterIn(String waterIn) {
        this.waterIn = waterIn;
    }

    public String getPiss() {
        return piss;
    }

    public void setPiss(String piss) {
        this.piss = piss;
    }

    public String getDefecate() {
        return defecate;
    }

    public void setDefecate(String defecate) {
        this.defecate = defecate;
    }

    public String getSp1() {
        return sp1;
    }

    public void setSp1(String sp1) {
        this.sp1 = sp1;
    }

    public String getDp1() {
        return dp1;
    }

    public void setDp1(String dp1) {
        this.dp1 = dp1;
    }

    public String getSp2() {
        return sp2;
    }

    public void setSp2(String sp2) {
        this.sp2 = sp2;
    }

    public String getDp2() {
        return dp2;
    }

    public void setDp2(String dp2) {
        this.dp2 = dp2;
    }

    public String getSp3() {
        return sp3;
    }

    public void setSp3(String sp3) {
        this.sp3 = sp3;
    }

    public String getDp3() {
        return dp3;
    }

    public void setDp3(String dp3) {
        this.dp3 = dp3;
    }

    public String getSp4() {
        return sp4;
    }

    public void setSp4(String sp4) {
        this.sp4 = sp4;
    }

    public String getDp4() {
        return dp4;
    }

    public void setDp4(String dp4) {
        this.dp4 = dp4;
    }

    public String getPdate1() {
        return pdate1;
    }

    public void setPdate1(String pdate1) {
        this.pdate1 = pdate1;
    }

    public String getPdate2() {
        return pdate2;
    }

    public void setPdate2(String pdate2) {
        this.pdate2 = pdate2;
    }

    public String getPdate3() {
        return pdate3;
    }

    public void setPdate3(String pdate3) {
        this.pdate3 = pdate3;
    }

    public String getPdate4() {
        return pdate4;
    }

    public void setPdate4(String pdate4) {
        this.pdate4 = pdate4;
    }


    public List<RecordSecondPageSignInfo> getPressure() {
        return pressure;
    }

    public void setPressure(List<RecordSecondPageSignInfo> pressure) {
        this.pressure = pressure;
    }

    public String getPain() {
        return pain;
    }

    public void setPain(String pain) {
        this.pain = pain;
    }

    public String getBreath() {
        return breath;
    }

    public void setBreath(String breath) {
        this.breath = breath;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }


    public String getDrainageType() {
        return drainageType;
    }

    public void setDrainageType(String drainageType) {
        this.drainageType = drainageType;
    }

    public String getDrainage() {
        return drainage;
    }

    public void setDrainage(String drainage) {
        this.drainage = drainage;
    }
}
