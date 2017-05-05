package com.gentlehealthcare.mobilecare.net;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.gentlehealthcare.mobilecare.activity.ActivityControlTool;
import com.gentlehealthcare.mobilecare.tool.LocalSave;

import java.io.*;
import java.util.Map;

/**
 * Created by ouyang on 2015/5/14.
 */
public class JsonFileUtil {

    public static void createJsonFile(String fileName,String content){
        try {
            Activity act = ActivityControlTool.currentActivity;
            File file = act.getFilesDir();
            if (file != null) {
                if (!file.exists())
                    file.mkdirs();
                File jsonFile = new File(file.getPath() + File.separator + fileName);
                if (!jsonFile.exists())
                    jsonFile.createNewFile();
                FileWriter fileWriter =new FileWriter(jsonFile);
                fileWriter.write(content);
                fileWriter.flush();
                fileWriter.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isLocal(){
       Map<String,String> map= LocalSave.getInstance(ActivityControlTool.currentActivity).get();
        if (map!=null&&map.get("version")!=null&&"local".equals(map.get("version"))){
            return true;
        }
        return false;
    }


    public static String  getJsonFileContent(String fileName){
        try {
            Activity act = ActivityControlTool.currentActivity;
            File file = act.getFilesDir();
            if (file != null) {
                File jsonFile = new File(file.getPath() + File.separator + fileName);
                if (!jsonFile.exists())
                   return "";
                FileReader fileReader=new FileReader(jsonFile);
                char[] buffer=new char[1024];
                int len=0;
                StringBuffer sb=new StringBuffer();
                while ((len=fileReader.read(buffer))>0){
                    String item=new String(buffer,0,len);
                    sb.append(item);
                }
                return sb.toString();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return "";
    }



    public static void copyJsonFile( ){
        try {
        Activity act=ActivityControlTool.currentActivity;
        SharedPreferences preferences = act.getSharedPreferences("NURSE_MOBILE_IS_FIRST", Context.MODE_PRIVATE);

        boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {

            String[] files = act.getAssets().list("gson");
            File rootFile = act.getFilesDir();
            for (String file : files) {

                InputStream inputStream = act.getAssets().open("gson"+File.separator+file);
                File jsonFile = new File(rootFile.getPath() + File.separator + file);
                if (!jsonFile.exists())
                    jsonFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(jsonFile);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.flush();
                fileOutputStream.close();

            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class JsonFileFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String filename) {
            if (filename.endsWith(".json"))
                return true;
            return false;
        }
    }


}
