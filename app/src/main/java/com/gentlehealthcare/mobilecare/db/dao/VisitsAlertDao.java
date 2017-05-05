package com.gentlehealthcare.mobilecare.db.dao;

import android.app.Activity;
import com.gentlehealthcare.mobilecare.bean.SettingsConfigBean;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.table.TB_VisitsAlert;
import com.gentlehealthcare.mobilecare.net.JsonFileUtil;
import com.gentlehealthcare.mobilecare.tool.VisitsAlertTool;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import java.sql.SQLException;
import java.util.List;

/**
 * 巡视报警DAO
 * Created by ouyang on 2015/3/19.
 */
public class VisitsAlertDao {
    private static final String TAG="VisitsAlertDao";
    private static VisitsAlertDao obj;
    private DBHelper dbHelper;
    private Dao<TB_VisitsAlert,Integer> dao;
    private  static Activity activity;
//    private static  PatientDao patientDao=null;


    public static VisitsAlertDao  newInstance(Activity activity){
        if (obj==null)
            obj=new VisitsAlertDao(activity);
        VisitsAlertDao.activity=activity;
//        VisitsAlertDao.patientDao=PatientDao.newInstance(activity);
        return  obj;
    }
    VisitsAlertDao(Activity activity){

        try {
            dbHelper=DBHelper.newInstance(activity);
            dao=dbHelper.getVisitsAlertDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据病人ID删除所有数据
     * @param patientId 病人ID
     * @return
     */
    public boolean delAll(String patientId){
        try {
            List<TB_VisitsAlert> list= dao.queryForEq("patientId",patientId);
            if (list!=null&&list.size()>0){
                for (TB_VisitsAlert tb_visitsAlert:list){
                  delete(tb_visitsAlert);

                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void add(TB_VisitsAlert visitsAlert,long visitime){
        try {
        TB_VisitsAlert table=new TB_VisitsAlert();
        table.setState(visitsAlert.getState());
        table.setInjectionTime(visitsAlert.getInjectionTime());
        table.setPlanId(visitsAlert.getPlanId());
        table.setIgnore(false);
        table.setMedicineCode(visitsAlert.getMedicineCode());
        table.setPatientId(visitsAlert.getPatientId());
        table.setVisitsTime(String.valueOf(visitime));
            table.setAlarmTime(visitsAlert.getAlarmTime());
        create(table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加数据
     * @param visitsAlert
     * @return
     */
    public boolean insert(TB_VisitsAlert visitsAlert){
        if (visitsAlert.getPlanId().endsWith("835111")){
            System.err.print("===");
        }
//       String patId=visitsAlert.getPatientId();
//        TB_Patient tb_patient=patientDao.getPatientById(patId);
//
//        if (tb_patient.getPatientType()!=0)
//            return false;
        visitsAlert.setIgnore(false);
        try {
            List<TB_VisitsAlert> list=dao.queryForEq("planId", visitsAlert.getPlanId());
            if (list!=null&&list.size()>0){
                for (TB_VisitsAlert tb_visitsAlert:list){
                 delete(tb_visitsAlert);
                }
            }
            long nowtime=System.currentTimeMillis();
            long visittime=Long.valueOf(visitsAlert.getVisitsTime());
            visitsAlert.setAlarmTime(visitsAlert.getVisitsTime());
            if (visitsAlert.getState().equals(MedicineConstant.STATE_WAITING)){
              long t= visittime+ SettingsConfigBean.ONE_HOUR_TIME;
                if (t-nowtime>0){
                    add(visitsAlert,t);
                    t=visittime+ SettingsConfigBean.TEN_MIN_TIME;
                    if (t-nowtime>0){
                        add(visitsAlert,t);
                        t=visittime-SettingsConfigBean.TEN_MIN_TIME;
                        if (t-nowtime>0)
                            add(visitsAlert,t);
                    }
                }
            }else if (visitsAlert.getState().equals(MedicineConstant.STATE_EXECUTING)){
                long t= visittime+ SettingsConfigBean.FIVE_MIN_TIEM;
                if (t-nowtime>0){
                    add(visitsAlert,t);
                    t=visittime-SettingsConfigBean.TEN_MIN_TIME;
                        if (t-nowtime>0)
                            add(visitsAlert,t);

                }else{
                    add(visitsAlert,visittime);
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据ID获取数据
     * @param visitsAlertId
     * @return
     */
    public TB_VisitsAlert GetVisitsAlertById(int visitsAlertId){
        try {
            List<TB_VisitsAlert>  list=dao.queryForEq("visitasAlertId",visitsAlertId);
            if (list!=null&&list.size()>0)
                return list.get(0);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID删除数据
     * @param visitsAlertId
     * @return
     */
    public boolean delById(int visitsAlertId){
        try {
            List<TB_VisitsAlert>  list=dao.queryForEq("visitasAlertId",visitsAlertId);
            if (list!=null&&list.size()>0){
                for (TB_VisitsAlert table:list){
                   delete(table);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据病人ID和药品编号获取数据
     * @param patientId
     * @param medicineCode
     * @return
     */
    public TB_VisitsAlert GetVisitsAlertByPatientIdAndMedicineCode(String patientId,String medicineCode){
        try {
            if (JsonFileUtil.isLocal())
                medicineCode="";
         List<TB_VisitsAlert> list=dao.queryBuilder().orderBy("visitasAlertId",false).where().eq("patientId",patientId).and().like("medicineCode","%"+medicineCode+"%").query();
            if (list!=null&&list.size()>0){
                return list.get(0);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID设置忽略状态
     * @param visitsAlertId
     * @return
     */
    public boolean UpdateIgnoreStateById(int visitsAlertId ){
        try {
            List<TB_VisitsAlert>  list=dao.queryForEq("visitasAlertId",visitsAlertId);
            if (list!=null&&list.size()>0){
                for (TB_VisitsAlert table:list){
                    table.setIgnore(true);
                    dao.update(table);
                }
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
            List<TB_VisitsAlert> list=dao.queryForAll();
            if (list!=null&&list.size()>0){
                for (TB_VisitsAlert tb_visitsAlert:list){
                    delete(tb_visitsAlert);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据巡视表ID获取数据
     * @param visitasAlertId  巡视表ID
     * @return String[] 0病人名字 1 病人标签号  2 单元名字 3模板名 4计划ID
     */
    public String[] GetPatientDataByVisitasAlertId(String visitasAlertId){
        try {
            GenericRawResults<String[]> results=  dao.queryRaw("select p.name,p.bedLabel,pm.state,pm.templateId from tb_visitsalert t,tb_patientmedicine pm,tb_patient p WHERE t.planId=pm.planId and t.visitasAlertId="+visitasAlertId+ " and t.patientId=pm.patientId AND pm.patientId=p.patientId;");
            List<String[]> result=results.getResults();
//            String planId="";
//            List<String[]> arr=new ArrayList<String[]>();
//            for (String[] strings:result){
//                if (!strings[4].equals(planId))
//                arr.add(strings);
//                planId=strings[4];
//            }
            if (result!=null&&result.size()>0)
                return result.get(0);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改病人执行状态,对应删除数据
     * @param planId
     * @param state
     */
    public void deleteTableByPlanIdAndState(String planId,String state){
        try {
            List<TB_VisitsAlert> list= dao.queryBuilder().where().like("planId","%"+planId+"%").and().eq("state",state).query();
            if (list!=null&&list.size()>0)
            {
                for (TB_VisitsAlert visitsAlert:list){
                    delete(visitsAlert);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    public int create(TB_VisitsAlert visitsAlert) throws SQLException {
        int falg=dao.create(visitsAlert);
        VisitsAlertTool.newInstance(activity).startAlert(visitsAlert);
        return falg;
    }

    public int delete(TB_VisitsAlert visitsAlert) throws SQLException {
        VisitsAlertTool.newInstance(activity).cancelAlert(visitsAlert);
        return dao.delete(visitsAlert);
    }
}
