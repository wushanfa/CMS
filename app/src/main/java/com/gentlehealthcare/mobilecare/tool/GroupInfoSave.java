package com.gentlehealthcare.mobilecare.tool;


import android.content.Context;
import android.content.SharedPreferences;

public class GroupInfoSave {
	private static final String name = "group";
	private static SharedPreferences sp = null;

	public static GroupInfoSave getInstance(Context context) {
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return new GroupInfoSave();
	}

	public void save(String groupname) {
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
			editor.putString("groupname", groupname);
		editor.commit();
	}
	
	public String get(){
		return sp.getString("groupname", "");
	}
}
