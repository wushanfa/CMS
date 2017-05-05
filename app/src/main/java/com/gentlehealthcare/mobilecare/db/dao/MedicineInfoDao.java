package com.gentlehealthcare.mobilecare.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.table.TB_MedicineInfo;
import com.j256.ormlite.dao.Dao;

public class MedicineInfoDao {
	private static final String TAG="MedicineInfoDao";
	private Dao<TB_MedicineInfo,Integer> dao;
	DBHelper dbHelper = null;
	private static Context context = null;
	private static MedicineInfoDao obj=null;
	public static MedicineInfoDao newInstance(Context context) {
		MedicineInfoDao.context = context;
		if (obj == null)
			obj = new MedicineInfoDao(context);
		return obj;
	}

	MedicineInfoDao(Context context) {
		try {
			dbHelper = DBHelper.newInstance(context);
			dao=dbHelper.getMedicineInfoDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "MedicineInfoDao" + e);
		}
	}
	/**
	 * 
	 * @Title: getMecineByCode 
	 * @Description: 根据药品编号获取药品 
	 * @param @param medicineCode
	 * @param @return   
	 * @return List<TB_MedicineInfo>   
	 * @throws
	 */
	public List<TB_MedicineInfo> getMecineByCode(String medicineCode){
		try {
			return dao.queryForEq("panId", medicineCode);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @Title: getMecineByCodeAndState 
	 * @Description: 根据药品编号和药品状态获取药品 
	 * @param @param medicineCode
	 * @param @param state
	 * @param @return   
	 * @return List<TB_MedicineInfo>   
	 * @throws
	 */
	public List<TB_MedicineInfo> getMecineByCodeAndState(String medicineCode,String state){
		try {
			
			return dao.queryBuilder().where().eq("medicineCode", medicineCode).query();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

    public List<TB_MedicineInfo> getAllMedicine(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
	
	/**
	 * 
	 * @Title: updateMedicine 
	 * @Description:  更新药品信息
	 * @param @param medicine
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean updateMedicine(TB_MedicineInfo medicine){
		try {
            List<TB_MedicineInfo> list=dao.queryBuilder().where().eq("panId",medicine.getPanId()).and().eq("medicineId",medicine.getMedicineId()).query();
            if (list==null||list.size()<=0){
                dao.create(medicine);
            }else
            {
                for (TB_MedicineInfo info:list){
                    medicine.setId(info.getId());
                    dao.update(medicine);
                }
            }
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

    /**
     * 根据计划ID获取列表
     * @param PlanId
     * @return
     */
    public TB_MedicineInfo GetMedicineInfoByPlanId(String PlanId){
        try {
            List<TB_MedicineInfo> medicineInfos= dao.queryForEq("panId",PlanId);
            if (medicineInfos!=null&&medicineInfos.size()>0)
                return medicineInfos.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }
	/**
	 * 清空表数据
	 * @return
	 */
	public boolean removeAll(){
		try {
			List<TB_MedicineInfo> list=dao.queryForAll();
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
