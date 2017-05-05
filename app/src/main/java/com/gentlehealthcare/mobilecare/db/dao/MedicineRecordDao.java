package com.gentlehealthcare.mobilecare.db.dao;

import android.content.Context;
import android.util.Log;
import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.table.TB_MedicineRecord;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ouyang on 2015/3/18.
 */
public class MedicineRecordDao {
    private static final String TAG="MedicineRecordDao";
    private Dao<TB_MedicineRecord,Integer> dao;
    DBHelper dbHelper = null;
    private static Context context = null;
    private static MedicineRecordDao obj=null;
    public static MedicineRecordDao newInstance(Context context) {
        MedicineRecordDao.context = context;
        if (obj == null)
            obj = new MedicineRecordDao(context);
        return obj;
    }

    MedicineRecordDao(Context context) {
        try {
            dbHelper = DBHelper.newInstance(context);
            dao=dbHelper.getMedicineRecordDao();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "MedicineInfoDao" + e);
        }
    }



    /**
     * 添加药品记录数据
     * @param medicineRecord
     * @return
     */

    public boolean insert(TB_MedicineRecord medicineRecord){
        try {
            dao.create(medicineRecord);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        return false;
        }
    }

    /**
     * 根据病人ID和药品编号查询巡视药品记录
     * @param patientId
     * @param medicineCode
     * @return
     */
    public List<TB_MedicineRecord> getMedicineRecordListByMediciCodeAndPatientId(String patientId,String medicineCode){
        try {
            return dao.queryBuilder().where().eq("patientId",patientId).and().eq("medicineCode",medicineCode).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<List<Map<String,String>>> getMedicineRecordListByMedicineNameAndPatientId(String patientId,String[] medicineNames){
        try {
            List<List<Map<String,String>>> data=new ArrayList<List<Map<String, String>>>();
            if (medicineNames==null)
                return null;
            for (String medicineName:medicineNames){
                List<TB_MedicineRecord> list=dao.queryBuilder().where().eq("patientId",patientId).and().eq("medicineName",medicineName).query();
                List<Map<String,String>> itemList=new ArrayList<Map<String, String>>();
                if(list!=null&&list.size()>0){
                    for (int i=0;i<list.size();i++){
                        TB_MedicineRecord table=list.get(i);
                       Map<String, String> map = new HashMap<String, String>();
                        StringBuffer message=new StringBuffer();
                        message.append("第"+(i+1)+"次巡视结果：");
                        if (table.getRecord()==0){
                            message.append("正常");
                        }else if(table.getRecord()==1){
                            message.append("过快");
                        }else{
                            message.append("过慢");
                        }
                       map.put("type", "1");

                       map.put("message",message.toString());
                        itemList.add(map);
                   }
                }
                data.add(itemList);
            }

           return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * 根据病人ID和药品编号查询巡视药品记录
     * @param patientId
     * @return
     */
    public String[] getMedicineRecordListPatientId(String patientId){
        try {
            List<TB_MedicineRecord> list= dao.queryBuilder().groupBy("medicineName").where().eq("patientId",patientId).query();
            if (list!=null&&list.size()>0){
                String[] strs=new String[list.size()];
             for (int i=0;i<list.size();i++){
                 strs[i]=list.get(i).getMedicineName();
             }
                return strs;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
