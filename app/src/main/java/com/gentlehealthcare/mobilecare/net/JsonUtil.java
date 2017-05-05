package com.gentlehealthcare.mobilecare.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.tool.DesUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
	private static final String TAG="JSON";
	private  Gson gson=null;
	public JsonUtil(){
		gson = new GsonBuilder()  
		.excludeFieldsWithoutExposeAnnotation()
	    .serializeNulls().setDateFormat("yyyyMMddHHmmssSSS")//时间转化为特定格式    
	    .setPrettyPrinting() //对json结果格式化.  
	    .setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
	                        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
	                        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  
	    .create(); 
	}
	
	
	/**
	 * 生成JSON数据
	 * @description 
	 * @author ouyang
	 * @date 2013年9月18日 下午10:44:04
	 *
	 * @param src
	 * @return
	 */
	public String toJson(Object src){
		String json= gson.toJson(src);
		Log.d(TAG, "发送服务器"+src.getClass().getSimpleName()+"的json数据:\n"+json);
		return json;
	}
	/**
	 * 解析JSON数据
	 * @description 
	 * @author ouyang
	 * @date 2013年9月18日 下午10:43:30
	 *
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public <T>T formJson(String json,Class<T> classOfT){
			
		try {
			Gson gson=new Gson();

			Log.d(TAG, "从服务器获取到"+classOfT.getSimpleName()+"的json数据:\n"+json);

            return gson.fromJson(json, classOfT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("JSON 解析错误");
			
		} 
	}

    /**
     * 解析无数据头的JSON (以[{开始的)
     * @param json
     * @param classOfT
     * @return
     */
   public <T>List<?> fromJsonList(String json,Class<T> classOfT){

       try {
           Gson gson=new Gson();
    T obj=classOfT.newInstance();
           Log.d(TAG, "从服务器获取到"+classOfT.getSimpleName()+"的json数据:\n"+json);

           Type type = new TypeToken<List<T>>(){}.getType();
           List<Object> list=new ArrayList<Object>();

           List<?> list1=gson.fromJson(json,type);
          obj= (T) list1.get(0);
        list.add(obj);
           System.out.print("list"+list);
          return list;

       } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           throw new RuntimeException("JSON 解析错误");

       }
   }

	/**
	 * 解析JSON数据
	 * @description 
	 * @author ouyang
	 * @date 2013年9月19日 上午12:06:06
	 *
	 * @param is
	 * @param classOfT
	 * @return
	 */
	public Object doFromJson(String json,Class<?> classOfT) {
        if ( classOfT == null)
            return null;
        if ("".equals(json))
            return null;
        if (UserInfo.getKey() != null && !"".equals(UserInfo.getKey())){
        try {
            json = DesUtil.decrypt(json, UserInfo.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        if (json.startsWith("[{"))
        {
            return fromJsonList(json, classOfT);
        }
        return formJson(json, classOfT);
		
	}
	
	private String getReaderJson(InputStream is){
		InputStreamReader reader=null;
		BufferedReader br=null;
		
		try {
			
			 br=new BufferedReader(reader=new InputStreamReader(is));
			StringBuffer buffer = new StringBuffer();

			String string;

			while ((string = br.readLine()) != null) {

				buffer.append(string);
			}

			return buffer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}finally{
			try {
				if(reader!=null)
					reader.close();
				if(br!=null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}



	/**
	 * 根据IO流获取JSON字符串
	 * @description 
	 * @author ouyang
	 * @date 2013年9月19日 上午12:05:28
	 *
	 * @param is
	 * @return
	 */
	private String getJson(InputStream is){
		try {
			int len=-1;
			byte[] buffer=new byte[1024];
			ByteArrayOutputStream bao=new ByteArrayOutputStream();
			while((len=is.read(buffer))!=-1){
				bao.write(buffer, 0, len);
			}
			return new String(bao.toByteArray(),"utf-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
}
