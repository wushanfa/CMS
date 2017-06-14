package com.gentlehealthcare.mobilecare.tool;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SystemInfoSave {
	private static final String name = "systeminfo";
	private static SharedPreferences sp = null;

	public static SystemInfoSave getInstance(Context context) {
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return new SystemInfoSave();
	}

	public void save(Map<String, String> map) {
		if (map == null || map.keySet().size() <= 0)
			return;
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			editor.putString(key, map.get(key));
		}
		editor.commit();
	}
	
	public Map<String,String> get(){
		return (Map<String, String>) sp.getAll();
	}
}
