package com.gentlehealthcare.mobilecare.model;

import com.gentlehealthcare.mobilecare.model.impl.TemperatureModel;

/**
 * Created by zyy on 2016/5/5.
 *
 * @desp 体温单模型层
 */
public interface ITemperatureModel {
	
	void saveData(String patId, String patCode, String visitId, String time, String temperature, String temperatureType, String pulse, String heartRate, String breathing, String bloodPressure, String bloodSugar, String pain, TemperatureModel.SaveSignListener listener);

}
