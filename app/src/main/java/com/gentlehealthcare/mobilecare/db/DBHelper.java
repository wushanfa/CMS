package com.gentlehealthcare.mobilecare.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gentlehealthcare.mobilecare.db.table.TB_ExecutionWork;
import com.gentlehealthcare.mobilecare.db.table.TB_GiveMedicineRecord;
import com.gentlehealthcare.mobilecare.db.table.TB_LoginRecord;
import com.gentlehealthcare.mobilecare.db.table.TB_MedicineInfo;
import com.gentlehealthcare.mobilecare.db.table.TB_MedicineRecord;
import com.gentlehealthcare.mobilecare.db.table.TB_Patient;
import com.gentlehealthcare.mobilecare.db.table.TB_PatientFlowPath;
import com.gentlehealthcare.mobilecare.db.table.TB_PatientMatters;
import com.gentlehealthcare.mobilecare.db.table.TB_PatientMedicine;
import com.gentlehealthcare.mobilecare.db.table.TB_VisitsAlert;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper{
	static final String DB_NAME = "nurse.db";
	static final int DB_VERSION = 3;
	static DBHelper obj;
	static Context context;
	Dao<TB_LoginRecord, Integer> loginRecordDao = null;
	Dao<TB_Patient, Integer> patientDao = null;
	Dao<TB_ExecutionWork, Integer> executionworkDao = null;
	Dao<TB_MedicineInfo, Integer> medicineInfoDao = null;
	Dao<TB_PatientMatters, Integer> patientMattersDao = null;
	Dao<TB_PatientFlowPath,Integer> patientFlowPathDao=null;
	Dao<TB_PatientMedicine,Integer> patientMedicineDao=null;
	Dao<TB_GiveMedicineRecord,Integer> giveMedicineRecordDao=null;
	Dao<TB_MedicineRecord,Integer> medicineRecordDao=null;

	Dao<TB_VisitsAlert,Integer> visitsAlertsDao=null;
	public static DBHelper newInstance(Context context) {
		if (obj == null)
			obj = new DBHelper(context);
		DBHelper.context = context;
		return obj;
	}

	DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(arg1, TB_LoginRecord.class);
			TableUtils.createTable(arg1, TB_ExecutionWork.class);
			TableUtils.createTable(arg1, TB_MedicineInfo.class);
			TableUtils.createTable(arg1, TB_Patient.class);
			TableUtils.createTable(arg1, TB_PatientMatters.class);
			TableUtils.createTable(arg1, TB_PatientFlowPath.class);
			TableUtils.createTable(arg1, TB_PatientMedicine.class);
			TableUtils.createTable(arg1, TB_GiveMedicineRecord.class);
			TableUtils.createTable(arg1,TB_MedicineRecord.class);
			TableUtils.createTable(arg1,TB_VisitsAlert.class);


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		try {
			TableUtils.dropTable(arg1, TB_LoginRecord.class, true);
			TableUtils.dropTable(arg1, TB_ExecutionWork.class, true);
			TableUtils.dropTable(arg1, TB_MedicineInfo.class, true);
			TableUtils.dropTable(arg1, TB_Patient.class, true);
			TableUtils.dropTable(arg1, TB_PatientMatters.class, true);
			TableUtils.dropTable(arg1, TB_PatientFlowPath.class, true);
			TableUtils.dropTable(arg1, TB_PatientMedicine.class, true);
			TableUtils.dropTable(arg1,TB_MedicineRecord.class,true);
			TableUtils.dropTable(arg1,TB_VisitsAlert.class,true);
			onCreate(arg0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @Title: loginRecordDao
	 * @Description: 获取登录记录Dao
	 * @param @return
	 * @param @throws SQLException
	 * @return Dao<TB_LoginRecord,Integer>
	 * @throws
	 */
	public Dao<TB_LoginRecord, Integer> loginRecordDao() throws SQLException {
		if (loginRecordDao == null)
			loginRecordDao = getDao(TB_LoginRecord.class);
		return loginRecordDao;
	}

	/**
	 *
	 * @Title: loginRecordDao
	 * @Description: 护理病人 Dao
	 * @param @return
	 * @param @throws SQLException
	 * @return Dao<TB_LoginRecord,Integer>
	 * @throws
	 */
	public Dao<TB_Patient, Integer> getPatientDao() throws SQLException {
		if (patientDao == null)
			patientDao = getDao(TB_Patient.class);
		return patientDao;
	}

	/**
	 *
	 * @Title: getExecutionWorkDao
	 * @Description: 工作表Dao
	 * @param @return
	 * @param @throws SQLException
	 * @return Dao<TB_ExecutionWork,Integer>
	 * @throws
	 */
	public Dao<TB_ExecutionWork, Integer> getExecutionWorkDao()
			throws SQLException {
		if (executionworkDao == null)
			executionworkDao = getDao(TB_ExecutionWork.class);
		return executionworkDao;
	}

	/**
	 *
	 * @Title: getMedicineInfoDao
	 * @Description: 药品信息表Dao
	 * @param @return
	 * @param @throws SQLException
	 * @return Dao<TB_MedicineInfo,Integer>
	 * @throws
	 */
	public Dao<TB_MedicineInfo, Integer> getMedicineInfoDao()
			throws SQLException {
		if (medicineInfoDao == null)
			medicineInfoDao = getDao(TB_MedicineInfo.class);
		return medicineInfoDao;
	}

	/**
	 *
	 * @Title: getPatientMattersDao
	 * @Description: TODO
	 * @param @return
	 * @param @throws SQLException
	 * @return Dao<TB_PatientMatters,Integer>
	 * @throws
	 */
	public Dao<TB_PatientMatters, Integer> getPatientMattersDao()
			throws SQLException {
		if (patientMattersDao == null)
			patientMattersDao = getDao(TB_PatientMatters.class);
		return patientMattersDao;
	}

	/**
	 *
	 * @Title: getPatientFlowPathDao
	 * @Description: 病人流程帮助Dao
	 * @param @return
	 * @param @throws SQLException
	 * @return Dao<TB_PatientFlowPath,Integer>
	 * @throws
	 */
	public Dao<TB_PatientFlowPath,Integer> getPatientFlowPathDao() throws SQLException{
		if(patientFlowPathDao==null)
			patientFlowPathDao=getDao(TB_PatientFlowPath.class);
		return patientFlowPathDao;
	}

	/**
	 *
	 * @Title: getPatientMedicineDao
	 * @Description: 病人和药品的关联表DAO
	 * @param @return
	 * @param @throws SQLException
	 * @return Dao<TB_PatientMedicine,Integer>
	 * @throws
	 */
	public Dao<TB_PatientMedicine,Integer>  getPatientMedicineDao() throws SQLException{
		if(patientMedicineDao==null)
			patientMedicineDao=getDao(TB_PatientMedicine.class);
		return patientMedicineDao;
	}

	/**
	 * 给药记录表DAO
	 * @return
	 * @throws SQLException
	 */
	public Dao<TB_GiveMedicineRecord,Integer> getGiveMedicineRecordDao() throws SQLException{
		if(giveMedicineRecordDao==null)
			giveMedicineRecordDao=getDao(TB_GiveMedicineRecord.class);
		return giveMedicineRecordDao;
	}

	/**
	 *  药品工作记录 Dao
	 * @return
	 * @throws SQLException
	 */
	public Dao<TB_MedicineRecord, Integer> getMedicineRecordDao()
			throws SQLException {
		if (medicineRecordDao == null)
			medicineRecordDao = getDao(TB_MedicineRecord.class);
		return medicineRecordDao;
	}

	/**
	 * 巡视报警记录DAO
	 * @return
	 * @throws SQLException
	 */
	public Dao<TB_VisitsAlert,Integer> getVisitsAlertDao() throws SQLException {
		if (visitsAlertsDao==null)
			visitsAlertsDao=getDao(TB_VisitsAlert.class);
		return visitsAlertsDao;
	}
}
