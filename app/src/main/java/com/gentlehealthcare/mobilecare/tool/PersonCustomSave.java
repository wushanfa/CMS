package com.gentlehealthcare.mobilecare.tool;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 *
 * Created by ouyang on 2015-04-16.
 */
public class PersonCustomSave {
    private static final String name = "personcustom";
    private static SharedPreferences sp = null;

    public static PersonCustomSave getInstance(Context context) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return new PersonCustomSave();
    }
    private PersonCustomSave(){

    }

    public void save(Map<String ,String> map) {
        SharedPreferences.Editor editor = sp.edit();

       Set<String> set= map.keySet();
        for (String str:set){
            editor.remove(str);
            editor.putString(str,map.get(str));
        }
        editor.commit();
    }

    public Map<String,?> get(){

        return sp.getAll();
    }
}
