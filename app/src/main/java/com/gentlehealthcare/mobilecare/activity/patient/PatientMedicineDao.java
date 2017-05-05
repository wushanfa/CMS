package com.gentlehealthcare.mobilecare.activity.patient;

import android.content.Context;
import android.util.Log;

import com.gentlehealthcare.mobilecare.activity.ActivityControlTool;
import com.gentlehealthcare.mobilecare.bean.SettingsConfigBean;
import com.gentlehealthcare.mobilecare.constant.MedicineConstant;
import com.gentlehealthcare.mobilecare.constant.TemplateConstant;
import com.gentlehealthcare.mobilecare.db.DBHelper;
import com.gentlehealthcare.mobilecare.db.dao.VisitsAlertDao;
import com.gentlehealthcare.mobilecare.db.table.TB_MedicineInfo;
import com.gentlehealthcare.mobilecare.db.table.TB_PatientMedicine;
import com.gentlehealthcare.mobilecare.db.table.TB_VisitsAlert;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class PatientMedicineDao {
    private static final String TAG = "PatientMedicineDao";
    private Dao<TB_PatientMedicine, Integer> dao;
    DBHelper dbHelper = null;
    private static Context context = null;
    private static PatientMedicineDao obj = null;
    private static VisitsAlertDao visitsAlertDao = null;

    public static PatientMedicineDao newInstance(Context context) {
        PatientMedicineDao.context = context;
        if (obj == null)
            obj = new PatientMedicineDao(context);
        return obj;
    }

    PatientMedicineDao(Context context) {
        try {
            dbHelper = DBHelper.newInstance(context);
            dao = dbHelper.getPatientMedicineDao();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "PatientDao" + e);
        }
    }

    /**
     * 根据执行状态,获取巡视时间
     *
     * @param tb_patientMedicine
     * @return
     */
    private static String GetVisitsTime(TB_PatientMedicine tb_patientMedicine) {
        if (tb_patientMedicine.getState().equals("0")) {
            long startTime = DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS, tb_patientMedicine.getStartTime());
            return startTime + "";
        } else {
            long time = Long.valueOf(tb_patientMedicine.getEventStartTime()) + SettingsConfigBean.ThIRTY_MIN_TIME;
            return String.valueOf(time);
        }
    }

    private static String GetInjectTime(TB_PatientMedicine tb_patientMedicine) {
        if (tb_patientMedicine.getState().equals("0")) {
            long startTime = DateTool.parseDate(DateTool.YYYY_MM_DD_HH_MM_SS, tb_patientMedicine.getStartTime());
            return startTime + "";
        } else {
            return tb_patientMedicine.getEventStartTime();
        }
    }

    private static String GetAlarmTime(TB_PatientMedicine tb_patientMedicine) {
        if (tb_patientMedicine.getState().equals("0")) {
            return tb_patientMedicine.getStartTime();
        } else {
            return tb_patientMedicine.getInspectionTime();
        }
    }

    public boolean updateArr(TB_PatientMedicine patientMedicine, String arr) {
        List<TB_PatientMedicine> list = null;
        try {
            list = dao.queryBuilder().where().eq("planId", patientMedicine.getPlanId()).query();
            if (list != null && list.size() > 0) {
                for (TB_PatientMedicine pm : list) {
                    pm.setPlanTimeAttr(arr);
                    dao.update(pm);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param @param  patientMedicine
     * @param @return
     * @return boolean
     * @throws
     * @Title: updateMedicineState
     * @Description: 更新关联表药品状态
     */
    public boolean updateMedicineState(TB_PatientMedicine patientMedicine, boolean isgroup) {
        try {
            List<TB_PatientMedicine> list = dao.queryBuilder().where().eq("planId", patientMedicine.getPlanId()).query();
            if (list != null && list.size() > 0) {

                VisitsAlertDao visitsAlertDao = VisitsAlertDao.newInstance(ActivityControlTool.currentActivity);
                for (TB_PatientMedicine pm : list) {
                    if (!isgroup) {
                        visitsAlertDao.deleteTableByPlanIdAndState(pm.getPlanId(), pm.getState());
                        if (pm.getState().equals(MedicineConstant.STATE_WAITING) && !pm.getTemplateId().equals(TemplateConstant.BLOOD_TEST.GetTemplateId())) {
                            TB_VisitsAlert tb_visitsAlert = new TB_VisitsAlert();
                            tb_visitsAlert.setPatientId(pm.getPatientId());
                            tb_visitsAlert.setVisitsTime(GetVisitsTime(patientMedicine));
                            tb_visitsAlert.setState(patientMedicine.getState());
                            tb_visitsAlert.setInjectionTime(GetInjectTime(patientMedicine));
                            tb_visitsAlert.setMedicineCode(pm.getPlanId());
                            tb_visitsAlert.setPlanId(pm.getPlanId());
                            tb_visitsAlert.setAlarmTime(GetAlarmTime(pm));
                            visitsAlertDao.insert(tb_visitsAlert);
                        } else if (patientMedicine.getState().equals(MedicineConstant.STATE_EXECUTED)) {
                            visitsAlertDao.deleteTableByPlanIdAndState(pm.getPlanId(), pm.getState());
                        }
                    }
                    if (!pm.getState().equals(MedicineConstant.STATE_EXECUTED)) {

                        pm.setState(patientMedicine.getState());
                        dao.update(pm);
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

    public boolean updateMedicineState(TB_PatientMedicine patientMedicine) {
        return updateMedicineState(patientMedicine, false);
    }

    /**
     * @param @param  patientId
     * @param @return
     * @return List<TB_PatientMedicine>
     * @throws
     * @Title: getPatientMedicineListByPatientId
     * @Description: 根据病人ID获取数据
     */
    public List<TB_PatientMedicine> getNotCompletePatientMedicineListByPatientId(String patientId) {
        try {
            return dao.queryBuilder().orderBy("state", true).orderBy("group", false).orderBy("startTime", false).where().eq("patientId", patientId).and().eq("state", MedicineConstant.STATE_WAITING).query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, TAG + "--getPatientMedicineListByPatientId");
            return null;
        }
    }

    /**
     * 根据病人ID和计划ID获取数据表
     *
     * @param patientId
     * @param planId
     * @return
     */
    public TB_PatientMedicine GetPatientMedicineByPatientIdAndPlanId(String patientId, String planId) {
        try {
            List<TB_PatientMedicine> list = dao.queryBuilder().orderBy("state", true).orderBy("group", false).orderBy("startTime", false).where().eq("patientId", patientId).and().eq("planId", planId).query();
            if (list != null && list.size() > 0)
                return list.get(0);
            else
                return null;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, TAG + "--getPatientMedicineListByPatientIdAndState");
            return null;
        }
    }

    /**
     * 根据病人ID,执行状态,模板ID来查询病人和药品关联表数据,并且按状态和开始时间排序
     *
     * @param patientId  病人ID
     * @param state      执行状态
     * @param templateId 模板ID
     * @return
     */
    public List<TB_PatientMedicine> getPatientMedicineListByPatientIdAndStateAndTemplateId(String patientId, String state, String... templateId) {
        try {
            String[] strings = templateId;
            if (strings.length > 1) {
                return dao.queryBuilder().orderBy("state", true).orderBy("startTime", true).orderBy("group", true).where().eq("patientId", patientId).and().eq("state", state).and().in("templateId",  templateId).query();
            } else {
                return dao.queryBuilder().orderBy("state", true).orderBy("startTime", true).orderBy("group", true).where().eq("patientId", patientId).and().eq("state", state).and().eq("templateId", strings[0]).query();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, TAG + "--getPatientMedicineListByPatientIdAndState");
            return null;
        }
    }

    /**
     * 根据病人ID,模板ID来查询已完成和已取消病人和药品关联表数据,并且按状态和开始时间排序
     *
     * @param patientId
     * @param templateId
     * @return
     */
    public List<TB_PatientMedicine> getCompletePatientMedicineListByPatientIdAndStateAndTemplateId(String patientId, String... templateId) {
        String[] state = new String[]{MedicineConstant.STATE_EXECUTED, MedicineConstant.STATE_CANCEL};
        try {
            return dao.queryBuilder().orderBy("templateId", true).orderBy("startTime", true).orderBy("group", true).where().eq("patientId", patientId).and().in("state", state).and().in("templateId", templateId).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据病人ID,执行状态来查询病人和药品关联表数据,并且按组,模板ID和开始时间排序
     *
     * @param patientId 病人ID
     * @param state     状态 MedicineConstant
     * @return
     */
    public List<TB_PatientMedicine> GetPatientMedicineListByPatientIdAndState(String patientId, String... state) {
        try {
            return dao.queryBuilder().orderBy("templateId", true).orderBy("startTime", true).orderBy("group", true).where().eq("patientId", patientId).and().in("state", state).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<TB_PatientMedicine> getPatientMedicineListByPatientIdAndNeState(String patientId, String state) {
        try {
            return dao.queryBuilder().orderBy("state", true).orderBy("group", false).orderBy("startTime", false).where().eq("patientId", patientId).and().ne("state", state).query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, TAG + "--getPatientMedicineListByPatientIdAndState");
            return null;
        }
    }

    /**
     * 根据病人ID获取已完成或者已取消工作
     *
     * @param patientId
     * @return
     */
    public List<TB_PatientMedicine> getCompletePatientMedicineListByPatientId(String patientId) {
        try {

            return dao.queryBuilder().orderBy("state", true).orderBy("group", false).orderBy("startTime", false).where().eq("state", MedicineConstant.STATE_EXECUTED).or().eq("state", MedicineConstant.STATE_CANCEL).and().eq("patientId", patientId).query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, TAG + "--getPatientMedicineListByPatientIdAndState");
            return null;
        }
    }

    /**
     * 根据病人ID删除已完成或者已取消工作
     *
     * @param patientId
     * @return
     */
    public boolean delCompletePatientMedicineListByPatientId(String patientId) {
        List<TB_PatientMedicine> list = getCompletePatientMedicineListByPatientId(patientId);
        if (list != null && list.size() > 0) {
            for (TB_PatientMedicine tb_patientMedicine : list) {
                try {
                    dao.delete(tb_patientMedicine);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 根据病人ID和药品执行状态并按group字段排序获取数据
     *
     * @param patientId
     * @param state
     * @return
     */
    public List<TB_PatientMedicine> getPatientMedicineListOrderGroupByPatientIdAndState(String patientId, String state) {
        try {
            return dao.queryBuilder().orderBy("group", false).where().eq("patientId", patientId).and().eq("state", state).query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, TAG + "--getPatientMedicineListByPatientIdAndState");
            return null;
        }
    }


    /**
     * @param @param  patientId
     * @param @return
     * @return List<TB_PatientMedicine>
     * @throws
     * @Title: GetCompleteMedicineByPatientId
     * @Description: 根据病人ID获取还未完成的药品信息
     */
    public List<TB_PatientMedicine> GetNoCompleteMedicineByPatientId(String patientId) {
        try {
            return dao.queryBuilder().where().eq("patientId", patientId).and().ne("state", MedicineConstant.STATE_EXECUTED).and().ne("state", MedicineConstant.STATE_CANCEL).query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, TAG + "--getPatientMedicineListByPatientIdAndState");
            return null;
        }
    }

    /**
     * @param @param  patientId
     * @param @param  medicineCode
     * @param @return
     * @return List<TB_PatientMedicine>
     * @throws
     * @Title: getPatientMedicineListByPatientIdAndMedicineCode
     * @Description: 根据药品编号与病人id获取信息
     */
    public TB_PatientMedicine getPatientNotWattingMedicineByPatientIdAndMedicineCode(String patientId, String medicineCode) {
        try {
            List<TB_PatientMedicine> list = dao.queryBuilder().orderBy("state", true).where().eq("patientId", patientId).and().eq("medicineCode", medicineCode).and().ne("state", MedicineConstant.STATE_WAITING).query();
            if (list != null && list.size() <= 0)
                return null;
            return list.get(0);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, TAG + "--getPatientMedicineListByPatientIdAndState");
            return null;
        }
    }

    /**
     * @param patientId
     * @param medicineCode
     * @return
     */
    public TB_PatientMedicine getPatientMedicineByPatientIdAndMedicineCode(String patientId, String medicineCode) {
        try {
            List<TB_PatientMedicine> list = dao.queryBuilder().where().eq("patientId", patientId).and().eq("planId", medicineCode).query();
            if (list != null && list.size() <= 0)
                return null;
            return list.get(0);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, TAG + "--getPatientMedicineListByPatientIdAndState");
            return null;
        }
    }

    /**
     * 根据病人ID和Group 获取信息
     *
     * @param patientId
     * @param group
     * @return
     */
    public List<TB_PatientMedicine> getPatientMedicineListByPatientIdAndGroup(String patientId, String group) {
        try {
            return dao.queryBuilder().where().eq("patientId", patientId).and().eq("group", group).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<TB_PatientMedicine> getPatientMedicineListByPatientIdAndGroupAndState(String patientId, String group, String state, String starttime) {
        try {
            //and().eq("state", state)
            return dao.queryBuilder().where().eq("patientId", patientId).and().eq("group", group).and().eq("startTime", starttime).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean delCompelteAndCancelPatientMedicinesByPatientId(String patientId) {
        try {

            List<TB_PatientMedicine> list = dao.queryBuilder().where().eq("patientId", patientId).and().eq("state", "9").or().eq("state", -1).query();
            if (list != null && list.size() > 0) {
                for (TB_PatientMedicine info : list) {
                    dao.delete(info);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据病人ID删除数据
     *
     * @param patientId
     * @return
     */
    public boolean delPatientMedicinesByPatientId(String patientId) {
        try {
            List<TB_PatientMedicine> list = dao.queryForEq("patientId", patientId);
            if (list != null && list.size() > 0) {
                for (TB_PatientMedicine info : list) {
                    dao.delete(info);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据病人ID和状态删除数据
     *
     * @param patientId 病人ID
     * @param state     执行状态
     * @return
     */
    public boolean delPatientMedicinesByPatIdAndState(String patientId, String... state) {
        try {
            List<TB_PatientMedicine> list = dao.queryBuilder().where().eq("patientId", patientId).and().in("state", state).query();
            if (list != null && list.size() > 0) {
                for (TB_PatientMedicine info : list) {
                    dao.delete(info);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加药品
     *
     * @param patientMedicine
     * @return
     */
    public boolean insert(TB_PatientMedicine patientMedicine) {
        try {
            List<TB_PatientMedicine> list = dao.queryForEq("planId", patientMedicine.getPlanId());
            if (list != null && list.size() > 0) {
                for (TB_PatientMedicine pm : list) {
                    dao.delete(pm);
                }
            }
            dao.create(patientMedicine);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据计划ID获取病人药品关联表
     *
     * @param planId 计划ID
     * @return
     */
    public TB_PatientMedicine GetPatientMedicineByPlanId(String planId) {
        try {
            List<TB_PatientMedicine> list = dao.queryForEq("planId", planId);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据胰岛素获取最近的一条血糖检测
     *
     * @param medicineInfo
     * @return
     */
    public TB_PatientMedicine GetLatelyBloodByInsulin(TB_MedicineInfo medicineInfo) {
        try {
            List<TB_PatientMedicine> list = dao.queryBuilder().orderBy("planId", false).where().eq("templateId", TemplateConstant.BLOOD_TEST.GetTemplateId()).and().gt("planId", medicineInfo.getPanId()).query();
            if (list != null && list.size() > 0)
                return list.get(0);
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据计划ID和状态更新表数据
     *
     * @param planId 计划ID
     * @param state  状态
     * @return
     */
    public boolean UpdatePatientMedineStateByPlanId(String planId, String state) {
        try {
            List<TB_PatientMedicine> list = dao.queryForEq("planId", planId);
            if (list != null && list.size() > 0) {
                for (TB_PatientMedicine pm : list) {
                    pm.setState(state);
                    dao.update(pm);
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
     *
     * @return
     */
    public boolean removeAll() {
        try {
            List<TB_PatientMedicine> list = dao.queryForAll();
            if (list != null && list.size() > 0) {
                dao.delete(list);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
