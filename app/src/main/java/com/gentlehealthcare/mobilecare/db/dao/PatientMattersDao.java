package com.gentlehealthcare.mobilecare.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.table.TB_PatientMatters;
import com.j256.ormlite.dao.Dao;
/**
 * 
 * @ClassName: PatientMattersDao 
 * @Description: 病人事项表Dao
 * 
 * @author ouyang
 * @date 2015年3月10日 下午2:22:46 
 *
 */
public class PatientMattersDao {
	private static final String TAG="PatientMattersDao";
	private Dao<TB_PatientMatters,Integer> dao;
	DBHelper dbHelper = null;
	private static Context context = null;
	private static PatientMattersDao obj=null;
	public static PatientMattersDao newInstance(Context context) {
		PatientMattersDao.context = context;
		if (obj == null)
			obj = new PatientMattersDao(context);
		return obj;
	}

	PatientMattersDao(Context context) {
		try {
			dbHelper = DBHelper.newInstance(context);
			dao=dbHelper.getPatientMattersDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "PatientDao" + e);
		}
	}
	/**
	 * 
	 * @Title: insert 
	 * @Description: 添加一个病人事项 
	 * @param @param patientMattters
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean insert(TB_PatientMatters patientMattters){
		try {
			dao.create(patientMattters);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 
	 * @Title: getPatientMatterByPatientId 
	 * @Description: 根据病人ID查询病人事项 
	 * @param @param patientId
	 * @param @return   
	 * @return List<TB_PatientMatters>   
	 * @throws
	 */
	public List<TB_PatientMatters> getPatientMatterByPatientId(String patientId){
		try {
			return dao.queryForEq("patientId", patientId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @Title: delPatientMatterByPatientId 
	 * @Description: 根据病人ID删除该病人所有的事项
	 * @param @param patientId
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean delPatientMatterByPatientId(String patientId){
		List<TB_PatientMatters> list=getPatientMatterByPatientId(patientId);
		if(list==null||list.size()<=0)
			return true;
		try {
			for(TB_PatientMatters tb:list)
				dao.delete(tb);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	/**
	 * 
	 * @Title: delPatientMatterById 
	 * @Description: 根据ID删除对应的事项 
	 * @param @param patientMatterId
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean delPatientMatterById(String patientMatterId){
		
		try {
			List<TB_PatientMatters> list=dao.queryForEq("patientMatterId", patientMatterId);
			if(list==null||list.size()<=0)
				return true;
			for(TB_PatientMatters tb:list)
				dao.delete(tb);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 清空表数据
	 * @return
	 */
	public boolean removeAll(){
		try {
			List<TB_PatientMatters> list=dao.queryForAll();
			if (list!=null&&list.size()>0){
				dao.delete(list);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
