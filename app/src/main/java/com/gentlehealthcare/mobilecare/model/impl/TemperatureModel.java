package com.gentlehealthcare.mobilecare.model.impl;

import android.text.TextUtils;

import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.model.ITemperatureModel;
import com.gentlehealthcare.mobilecare.net.bean.SyncPatientBean;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zyy on 2016/5/6.
 */
public class TemperatureModel implements ITemperatureModel {

	private static final String TAG = "TeperatureModel";

	public interface OnLoadPatientListListener {
		void onSuccess(List<SyncPatientBean> list);

		void onFailure(String msg, Exception e);
	}

	public interface SaveSignListener {
		void onSuccess(String msg);

		void onFailure(String msg, Exception e);
	}

	@Override
	public void saveData(String patId, String patCode, String visitId,
			String time, String temperature, String temperatureType,
			String pulse, String heartRate, String breathing,
			String bloodPressure, String bloodSugar, String pain,
			final SaveSignListener listener) {
		// TODO Auto-generated method stub
		String url = UrlConstant.saveSigns() + patId + "&patCode=" + patCode
				+ "&visitId=" + visitId + "&time=" + time + "&temperature="
				+ temperature + "&temperatureType=" + temperatureType
				+ "&pulse=" + pulse + "&heartRate=" + heartRate + "&breathing="
				+ breathing + "&bloodPressure=" + bloodPressure + "&bloodSugar="
				+ bloodSugar + "&pain=" + pain;
		CCLog.i("体征保存>>>" + url);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						try {
							JSONObject jsonObject = new JSONObject(result
									.toString());
							Boolean status = jsonObject.getBoolean("result");
							if (status) {
								listener.onSuccess(GlobalConstant.SUCCEED);
							} else {
								listener.onFailure(GlobalConstant.FAILED, null);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							listener.onFailure(GlobalConstant.FAILED, null);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						listener.onFailure(GlobalConstant.FAILED, null);
					}
				});

	}

}
