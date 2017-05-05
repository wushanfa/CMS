package com.gentlehealthcare.mobilecare.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.table.TB_PatientFlowPath;
import com.j256.ormlite.dao.Dao;
/**
 * 
 * @ClassName: PatientFlowPathDao 
 * @Description: 病人流程说明Dao
 * @author ouyang
 * @date 2015年3月11日 下午1:58:52 
 *
 */
public class PatientFlowPathDao {
	private static final String TAG="PatientFlowPathDao";
	private Dao<TB_PatientFlowPath,Integer> dao;
	DBHelper dbHelper = null;
	private static Context context = null;
	private static PatientFlowPathDao obj=null;
	public static PatientFlowPathDao newInstance(Context context) {
		PatientFlowPathDao.context = context;
		if (obj == null)
			obj = new PatientFlowPathDao(context);
		return obj;
	}

	PatientFlowPathDao(Context context) {
		try {
			dbHelper = DBHelper.newInstance(context);
			dao=dbHelper.getPatientFlowPathDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "PatientDao" + e);
		}
	}
	
	/**
	 * 
	 * @Title: getPatientFlowPathListByPatientName 
	 * @Description: 根据病人名获取信息 
	 * @param @param patientname
	 * @param @return   
	 * @return List<TB_PatientFlowPath>   
	 * @throws
	 */
	public List<TB_PatientFlowPath> getPatientFlowPathListByPatientName(String patientname)
	{
		try {
			return dao.queryBuilder().orderBy("updatetime", false).where().eq("patientname", patientname).query();
//			return dao.queryForEq("patientname", patientname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public boolean inseart(TB_PatientFlowPath patientFlowPath){
		try {
			dao.create(patientFlowPath);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
