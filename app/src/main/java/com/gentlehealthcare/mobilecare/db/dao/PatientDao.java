package com.gentlehealthcare.mobilecare.db.dao;

import android.content.Context;
import android.util.Log;

import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.table.TB_Patient;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class PatientDao {
	private static final String TAG="PatientDao";
	private Dao<TB_Patient,Integer> dao;
	DBHelper dbHelper = null;
	private static Context context = null;
    private GiveMedicineRecordDao giveMedicineRecordDao=null;
	private static PatientDao obj=null;
	public static PatientDao newInstance(Context context) {
		PatientDao.context = context;
		if (obj == null)
			obj = new PatientDao(context);
		return obj;
	}

	PatientDao(Context context) {
		try {
			dbHelper = DBHelper.newInstance(context);
			dao=dbHelper.getPatientDao();
            giveMedicineRecordDao=GiveMedicineRecordDao.newInstance(context);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "PatientDao" + e);
		}
	}
	
	/**
	 * 
	 * @Title: insert 
	 * @Description: 添加一条病人数据
	 * @param @param tb
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean insert(TB_Patient tb){
		if(tb==null)
			return false;
		try {
//			List<TB_Patient> list=dao.queryForEq("patientId", tb.getPatientId());
//			if(list!=null&&list.size()>0){
//				for(TB_Patient table:list){
//					dao.delete(table);
//				}
//			}
			dao.create(tb);
            giveMedicineRecordDao.delByPatientId(tb.getPatientId());
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "PatientDao   insert  " + e);
			return false;
		}
	}
	/**
	 * 
	 * @Title: queryAll 
	 * @Description: 查询所有 
	 * @param @return   
	 * @return List<TB_Patient>   
	 * @throws
	 */
	public List<TB_Patient> queryAllByPatientType(int patientType){
		try {

			return dao.queryForEq("patientType", patientType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "PatientDao   queryAll  " + e);
			return null;
		}
	}
	
	
	/**
	 * 
	 * @Title: queryAll 
	 * @Description: 获取所有病人数据 
	 * @param @return   
	 * @return List<TB_Patient>   
	 * @throws
	 */
	public List<TB_Patient> queryAll(){
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "PatientDao   queryAll  " + e);
			return null;
		}
	}

	/**
	 * 清空所有数据
	 * @return
	 */
	public boolean removeAll() {
		try {
			List<TB_Patient> list = queryAll();
			if (list != null && list.size() > 0) {
				dao.delete(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "PatientDao   queryAll  " + e);
			return false;
		}
		return true;
	}

	/**
	 * 根据病人ID删除数据
	 * @param patId
	 * @return
	 */
	public boolean delTableByPatId(String patId){
		try {
			List<TB_Patient> list=dao.queryForEq("patientId",patId);
			for (TB_Patient info:list){
				dao.delete(info);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 
	 * @Title: clear 
	 * @Description: 清空表数据 
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean clear(int patientType){
		try {
			List<TB_Patient> list=queryAllByPatientType(patientType);
			if(list!=null&&list.size()>0){
				for(TB_Patient info:list)
					dao.delete(info);
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: getPatientById 
	 * @Description: 根据ID获取病人信息 
	 * @param @param patientId
	 * @param @return   
	 * @return TB_Patient   
	 * @throws
	 */
	public TB_Patient getPatientById(String patientId){
		try {
			List<TB_Patient> list=dao.queryForEq("patientId", patientId);
			if(list==null||list.size()<=0)
				return null;
			return list.get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	/**
	 *
	 * @Title: getPatientByPatCode
	 * @Description: 根据Code获取病人信息
	 * @param @param patCode
	 * @param @return
	 * @return TB_Patient
	 * @throws
	 */
	public TB_Patient getPatientByPatCode(String patCode){
		try {
			List<TB_Patient> list=dao.queryForEq("patCode", patCode);
			if(list==null||list.size()<=0)
				return null;
			return list.get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 
	 * @Title: updatePatient 
	 * @Description: 更新病人信息 
	 * @param @param patient
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean updatePatient(TB_Patient patient){
		try {
			dao.update(patient);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}


}
