package com.gentlehealthcare.mobilecare.db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.table.TB_ExecutionWork;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

public class ExecutionWorkDao {
	private static final String TAG = "ExecutionWorkDao";
	private Dao<TB_ExecutionWork, Integer> dao;
	DBHelper dbHelper = null;
	private static Context context = null;
	private static ExecutionWorkDao obj = null;

	public static ExecutionWorkDao newInstance(Context context) {
		ExecutionWorkDao.context = context;
		if (obj == null)
			obj = new ExecutionWorkDao(context);
		return obj;
	}

	ExecutionWorkDao(Context context) {
		try {
			dbHelper = DBHelper.newInstance(context);
			dao = dbHelper.getExecutionWorkDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "PatientDao" + e);
		}
	}

	/**
	 * 
	 * @Title: getAllWorkByType
	 * @Description: 根据类型获取所有工作
	 * @param @param type 0待执行 1 执行中 2 已执行
	 * @param @return
	 * @return List<TB_ExecutionWork>
	 * @throws
	 */
	public List<TB_ExecutionWork> getAllWorkByType(int type) {
		try {
			return dao.queryForEq("type", type);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

    public  List<String[]>  GetAllWorkGroupByPatientIdByType(int type){
        try {
           GenericRawResults<String[]> results= dao.queryRaw("select p.name,p.patientId,count(t.orderId) from tb_executionwork t,tb_patient p  WHERE t.type="+type +" AND t.patientId=p.patientId AND p.patientType=0 group by t.patientId  ;");

            List<String[]> list=results.getResults();
           return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取进行中工作表数据
     * @return
     */
    public List<String[]> GetExecutioningWorkTableData(){
        try {
            GenericRawResults<String[]> results= dao.queryRaw("SELECT p.name,p.bedNo,w.workType,w.orderText,pm.startTime,p.patientId FROM tb_executionwork w,tb_patient p,tb_patientmedicine pm WHERE w.patientId=p.patientId AND p.patientId=pm.patientId AND pm.state=1 AND p.patientType=0 AND w.type=1 AND pm.medicineCode=w.orderCode GROUP BY pm.\"group\";");
            return results.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


	
	
	public List<TB_ExecutionWork> getAllWorkByTypeAndWrokType(int type,int workType,String patId) {
		try {
			return dao.queryBuilder().where().eq("type", type).and().eq("workType", workType).and().eq("patientId",patId).query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 
	 * @Title: getAllWorkSizeByType
	 * @Description: 根据类型获取所有工作)
	 * @param @param type 0待执行 1 执行中 2 已执行
	 * @param @return
	 * @return List<TB_ExecutionWork>
	 * @throws
	 */
	public int getAllWorkSizeByType(int type, int patientId){

        try {
			List<TB_ExecutionWork> list = dao.queryBuilder().where()
					.eq("type", type).and().eq("patientId", patientId).query();
			;
			if (list == null)
				return 0;
			else
				return list.size();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * 
	 * @Title: clearAllWorkByType
	 * @Description: 根据类型清楚所有工作
	 * @param @param type 0待执行 1 执行中 2 已执行
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean clearAllWorkByType(int type) {
		try {
			List<TB_ExecutionWork> list = getAllWorkByType(type);
			if (list == null || list.size() <= 0)
				return true;
			for (TB_ExecutionWork tb : list) {
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
	 * @Title: insert
	 * @Description: 添加数据
	 * @param @param executionwork
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean insert(TB_ExecutionWork executionwork) {
		try {
			List<TB_ExecutionWork> list = dao.queryForEq("plan_id",
					executionwork.getPlan_id());
			if (list != null && list.size() > 0) {
				for (TB_ExecutionWork info : list)
					dao.delete(info);
			}
			int flag = dao.create(executionwork);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @Title: updateByPatientId
	 * @Description: 根据病人ID更新工作状态
	 * @param @param patientId
	 * @param @param worktype
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean updateByPatientId(String patientId, String mediciCode,int type) {
		try {
            List<TB_ExecutionWork> list =dao.queryBuilder().where().eq("patientId",patientId).and().eq("plan_id",mediciCode).query();
//			List<TB_ExecutionWork> list = dao.queryForEq("patientId", patientId);
			if (list != null && list.size() > 0) {
				for (TB_ExecutionWork work : list) {
					if (work.getType() != 2) {
						work.setType(type);
						dao.update(work);
					}
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
     * 根据病人ID,工作类型，类型获取开始时间数据
     * @param patientId
     * @param workType //0 注射 1 给药 2 输血 3 胰岛素
     * @param type 0待执行 1 执行中 2 已执行
     * @return
     */
    public List<String[]> getStartTimeByPatientIdAndWorkTypeAndType(String patientId,int workType,int type){
        try {

            GenericRawResults<String[]> results=dao.queryRaw("SELECT t.startTime ,count(t.startTime) FROM tb_executionwork t  WHERE t.workType= "+ workType+" AND t.type="+type +" And t.patientId=\""+patientId +"\" GROUP BY t.startTime ORDER BY t.startTime ;");
            return  results.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 根据病人ID,工作类型，类型获取开始时间数据
	 * @param patientId
	 * @param workType //0 注射 1 给药 2 输血 3 胰岛素
	 * @param type 0待执行 1 执行中 2 已执行
	 * @return
	 */
	public List<String[]> getStartTimeByPatientIdAndWorkTypeAndTypeAndDayTime(String patientId,int workType,int type,String daytime){
		try {
			String sql="SELECT t.startTime ,count(t.startTime) FROM tb_executionwork t  WHERE t.workType= "+ workType+" AND t.type="+type +" And t.patientId=\""+patientId +"\" And t.startTime>=\""+daytime+" 00:00:00\" And t.startTime<=\""+daytime+" 23:59:59\""+ " GROUP BY t.startTime ORDER BY t.startTime ";
			GenericRawResults<String[]> results=dao.queryRaw(sql);
			return  results.getResults();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


    /**
     * 根据工作类型，类型获取病人名称 开始时间数据
     * @param workType //0 注射 1 给药 2 输血 3 胰岛素
     * @param type 0待执行 1 执行中 2 已执行
     * @return
     */
    public Map<String,List<String[]>> getStartTimeByWorkTypeAndType(int workType,int type){
        try {
            GenericRawResults<String[]> results=   dao.queryRaw("SELECT p.name ,t.startTime,count(t.startTime),t.patientId FROM tb_executionwork t ,tb_patient p  WHERE t.workType= "+ workType+" AND t.type="+type +" And t.patientId=p.patientId AND p.patientType=0 GROUP BY t.startTime,p.name ORDER BY t.startTime ;");
            Map<String,List<String[]>> map=new HashMap<String,List<String[]>>();
            List<String[]> result=results.getResults();
            if (result!=null&&result.size()>0){
                for (String[] item:result){
                    if (map.containsKey(item[0])){
                        List<String[]> value=map.get(item[0]);
                        value.add(new String[]{item[1],item[2],item[3]});
//                        map.put(item[0],value);
                    }else{
                List<String[]> value=new ArrayList<String[]>();
                        value.add(new String[]{item[1],item[2],item[3]});
                        map.put(item[0],value);
                    }
                }
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据病人ID和工作类型获取执行工作数据
     * @param patientId 病人ID
     * @param state 工作类型 0待执行 1 执行中 2 已执行
     * @return
     */
    public List<TB_ExecutionWork> getPatientMedicineListByPatientIdAndState(String patientId,String state){
        try {
           return dao.queryBuilder().where().eq("patientId", patientId).and().eq("type", state).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 根据病人ID删除已完成执行的工作
	 * @param patientId 病人ID
	 * @return
	 */
	public boolean delCompleteExecutionWorkListByPatientId(String patientId){
		try {
			List<TB_ExecutionWork> tbExecutionWorks = dao.queryBuilder().where().eq("patientId", patientId).and().eq("type", "2").query();
			if (tbExecutionWorks!=null&&tbExecutionWorks.size()>0){
				dao.delete(tbExecutionWorks);
			}
			return true;
		}catch (SQLException e){
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
			List<TB_ExecutionWork> list=dao.queryForAll();
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
