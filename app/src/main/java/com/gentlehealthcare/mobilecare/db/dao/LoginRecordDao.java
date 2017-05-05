package com.gentlehealthcare.mobilecare.db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.table.TB_LoginRecord;
import com.j256.ormlite.dao.Dao;

public class LoginRecordDao {
	private static final String TAG="LoginRecordDao";
	private Dao<TB_LoginRecord,Integer> loginRecordDao;
	DBHelper dbHelper = null;
	private static Context context = null;
	private static LoginRecordDao obj=null;
	public static LoginRecordDao newInstance(Context context) {
		LoginRecordDao.context = context;
		if (obj == null)
			obj = new LoginRecordDao(context);
		return obj;
	}

	LoginRecordDao(Context context) {
		try {
			dbHelper = DBHelper.newInstance(context);
			loginRecordDao = dbHelper.loginRecordDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LoginRecordDao" + e);
		}
	}
	/**
	 * 
	 * @Title: insert 
	 * @Description: 添加一条登录记录 
	 * @param @param tb
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean insert(TB_LoginRecord tb){
		if(tb==null||tb.getUserName()==null||"".equals(tb.getUserName()))
			return false;
		try {
			List<TB_LoginRecord> list=loginRecordDao.queryForEq("userName", tb.getUserName());
			if(list!=null&&list.size()>0){
				for(TB_LoginRecord table:list){
					loginRecordDao.delete(table);
				}
			}
			loginRecordDao.create(tb);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LoginRecordDao   insert  " + e);
			return false;
		}
	}

	/**
	 * 
	 * @Title: update 
	 * @Description: 更新 
	 * @param @param tb
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean update(TB_LoginRecord tb){
		if(tb==null||tb.getUserName()==null||"".equals(tb.getUserName()))
			return false;
		try {
			List<TB_LoginRecord> list=loginRecordDao.queryForEq("userName", tb.getUserName());
			if(list!=null&&list.size()>0){
				for(TB_LoginRecord table:list){
                    tb.setLoginrecordId(table.getLoginrecordId());
					loginRecordDao.update(tb);
				}
			}else{
			loginRecordDao.create(tb);
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LoginRecordDao   insert  " + e);
			return false;
		}
	}
	
	/**
	 * 
	 * @Title: deleteByUserName 
	 * @Description: 根据用户名 删除数据 
	 * @param @param userName
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean deleteByUserName(String userName){
		if(userName==null||"".equals(userName))
			return false;
		try {
			List<TB_LoginRecord> list=loginRecordDao.queryForEq("userName",userName);
			if(list!=null&&list.size()>0){
				for(TB_LoginRecord table:list){
					loginRecordDao.delete(table);
				}
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LoginRecordDao   deleteByUserName  " + e);
			return false;
		}
	}
	
	/**
	 * 
	 * @Title: queryAllRecordOfUserName 
	 * @Description: 获取表中 
	 * @param @return   
	 * @return List<String>   
	 * @throws
	 */
	public List<String> queryAllRecordOfUserName(){
		try {
			List<TB_LoginRecord> 	list = loginRecordDao.queryBuilder().orderBy("updateTime", false).query();
			if (list == null || list.size() <= 0)
				return null;
			List<String> strList = new ArrayList<String>();
			for (TB_LoginRecord loginRecord : list) {
				strList.add(loginRecord.getUserName());
			}
			return strList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
	}

	/**
	 * 最近一次登陆的账号名
	 * @return
	 */
	public String queryRecordOfUserName(){
		try {
			List<TB_LoginRecord> 	list = loginRecordDao.queryBuilder().orderBy("updateTime", false).query();
			if (list == null || list.size() <= 0)
				return "";
			return list.get(0).getUserName();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	



	
}
