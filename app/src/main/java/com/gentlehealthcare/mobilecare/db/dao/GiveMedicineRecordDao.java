package com.gentlehealthcare.mobilecare.db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.table.TB_GiveMedicineRecord;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

public class GiveMedicineRecordDao {
	private static final String TAG="GiveMedicineRecordDao";
	private Dao<TB_GiveMedicineRecord,Integer> dao;
	DBHelper dbHelper = null;
	private static Context context = null;
	private static GiveMedicineRecordDao obj=null;
	public static GiveMedicineRecordDao newInstance(Context context) {
		GiveMedicineRecordDao.context = context;
		if (obj == null)
			obj = new GiveMedicineRecordDao(context);
		return obj;
	}

	GiveMedicineRecordDao(Context context) {
		try {
			dbHelper = DBHelper.newInstance(context);
			dao = dbHelper.getGiveMedicineRecordDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "LoginRecordDao" + e);
		}
	}
	/**
	 * 
	 * @Title: insert 
	 * @Description: TODO 
	 * @param @param giveMedicineRecord
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean insert(TB_GiveMedicineRecord giveMedicineRecord){
		try {
			if(giveMedicineRecord==null)
				return false;
			dao.create(giveMedicineRecord);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 
	 * @Title: getAll 
	 * @Description: 查询所有 
	 * @param @return   
	 * @return List<TB_GiveMedicineRecord>   
	 * @throws
	 */
	public List<TB_GiveMedicineRecord> getAll(){
        List<TB_GiveMedicineRecord> list=new ArrayList<TB_GiveMedicineRecord>();
        try {
           GenericRawResults<String[]> result= dao.queryRaw("SELECT p.bedNo,p.name,pm.medicineCode,m.medicineName,pm.startTime ,p.patientId,pm.\"group\" FROM tb_patientmedicine pm,tb_patient p,tb_medicineinfo m WHERE pm.state=1 AND pm.medicineId=m.medicineId AND pm.patientId=p.patientId AND p.patientType=0 GROUP BY pm.\"group\";");
            List<String[]> results=result.getResults();
            if (results!=null&&results.size()>0){

                for (String[] item:results){
                    TB_GiveMedicineRecord tb_giveMedicineRecord =new TB_GiveMedicineRecord();
                    tb_giveMedicineRecord.setBedNo(item[0]);
                    tb_giveMedicineRecord.setName(item[1]);
                    tb_giveMedicineRecord.setMedicineCode(item[2]);
                    tb_giveMedicineRecord.setMedicineName(item[3]);
                    long starttime=DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS, item[4]);
                    long endtime=starttime+1000*60*15;
                    tb_giveMedicineRecord.setStartTime(starttime);
                    tb_giveMedicineRecord.setVisitsTime(endtime);
                    tb_giveMedicineRecord.setPatientId(item[5]);
                    list.add(tb_giveMedicineRecord);
                }
                return list;
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }

			return list;

	}
	/**
	 * 
	 * @Title: delById 
	 * @Description: 根据ID删除数据 
	 * @param @param gmRecordId
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean delById(int gmRecordId){
		try {
		List<TB_GiveMedicineRecord> giveMedicineRecords=	dao.queryForEq("gmRecordId", gmRecordId);
		if(giveMedicineRecords==null||giveMedicineRecords.size()<=0)
			return true;
		for(TB_GiveMedicineRecord tb:giveMedicineRecords){
			dao.delete(tb);
		}
		return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @Title: delByPatientId 
	 * @Description: 根据病人ID删除数据 
	 * @param @param patientId
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean delByPatientId(String patientId){
		try {
		List<TB_GiveMedicineRecord> giveMedicineRecords=	dao.queryForEq("patientId", patientId);
		if(giveMedicineRecords==null||giveMedicineRecords.size()<=0)
			return true;
		for(TB_GiveMedicineRecord tb:giveMedicineRecords){
			dao.delete(tb);
		}
		return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

    /**
     * 根据病人id与药品编号 删除记录
     * @param patientId
     * @param medicineCode
     * @return
     */
    public boolean delByMedicineCode(String patientId,String medicineCode){
        try {
            List<TB_GiveMedicineRecord> giveMedicineRecords= dao.queryBuilder().where().eq("medicineCode",medicineCode).and().eq("patientId",patientId).query();
            if(giveMedicineRecords==null||giveMedicineRecords.size()<=0)
                return true;
            for(TB_GiveMedicineRecord tb:giveMedicineRecords){
                dao.delete(tb);
            }
            return true;
        } catch (SQLException e) {
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
			List<TB_GiveMedicineRecord> list=dao.queryForAll();
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
