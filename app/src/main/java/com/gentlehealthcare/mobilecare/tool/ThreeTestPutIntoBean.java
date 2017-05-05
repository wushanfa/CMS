package com.gentlehealthcare.mobilecare.tool;

import java.util.Map;

import com.gentlehealthcare.mobilecare.net.bean.LoadThreeTestBean;

public class ThreeTestPutIntoBean {

	public static void putTTIntoMap(LoadThreeTestBean t,
			Map<String, LoadThreeTestBean> m) {
		String vitalSigns = t.getVitalSigns();
		// 心率（次/分）
		if ("HEART_RATE".equals(vitalSigns)) {
			m.put("heart_rate", t);
		}
		// 体温（℃）
		if ("T".equals(vitalSigns)) {
			CCLog.e("t+>>>>>>>>>>>" + t.getVitalSignsValues());
			m.put("t", t);
		}
		// 收缩压（mmHg）
		if ("SP".equals(vitalSigns)) {
			if (t.getRecordingTime().substring(11).equals("07:00:00")) {
				m.put("sp7", t);
			}
			if (t.getRecordingTime().substring(11).equals("11:00:00")) {
				m.put("sp11", t);
			}
			if (t.getRecordingTime().substring(11).equals("15:00:00")) {
				m.put("sp15", t);
			}
			if (t.getRecordingTime().substring(11).equals("19:00:00")) {
				m.put("sp19", t);
			}
		}
		// 舒张压（mmHg）
		if ("DP".equals(vitalSigns)) {
			if (t.getRecordingTime().substring(11).equals("07:00:00")) {
				m.put("dp7", t);
			}
			if (t.getRecordingTime().substring(11).equals("11:00:00")) {
				m.put("dp11", t);
			}
			if (t.getRecordingTime().substring(11).equals("15:00:00")) {
				m.put("dp15", t);
			}
			if (t.getRecordingTime().substring(11).equals("19:00:00")) {
				m.put("dp19", t);
			}
		}
		// 脉博（次/分）
		if ("P".equals(vitalSigns)) {
			m.put("p", t);
		}
		// 呼吸（次/分）
		if ("R".equals(vitalSigns)) {
			m.put("r", t);
		}

		// 物理降温
		if ("T1".equals(vitalSigns)) {
			m.put("t1", t);
		}
		// 体温不升
		if ("T2".equals(vitalSigns)) {
			m.put("t2", t);
		}
		// 呼吸机
		if ("R1".equals(vitalSigns)) {
			m.put("r1", t);
		}
		// 表顶注释
		if ("COLUMN_DESC".equals(vitalSigns)) {
			m.put("column_desc", t);
		}
		// 疼痛评分
		if ("PAIN_SCORE".equals(vitalSigns)) {
			m.put("pain_score", t);
		}
		// 大便
		if ("F".equals(vitalSigns)) {
			m.put("f", t);
		}
		// 小便（次）
		if ("U".equals(vitalSigns)) {
			m.put("u", t);
		}
		// 入水量
		if ("WATER_IN".equals(vitalSigns)) {
			m.put("water_in", t);
		}
		// 出水量
		if ("WATER_OUT".equals(vitalSigns)) {
			m.put("water_out", t);
		}
		// 药物过敏
		if ("DRUG_ALLERGY".equals(vitalSigns)) {
			m.put("drug_allery", t);
		}

		// 体重
		if ("WEIGHT".equals(vitalSigns)) {
			m.put("weight", t);
		}
		// 血氧饱和度
		if ("SPO2%".equals(vitalSigns)) {
			m.put("spo2%", t);
		}
		// 切口负压引流（ml）
		if ("WOUND_DRAINAGE1".equals(vitalSigns)) {
			m.put("WOUND_DRAINAGE1", t);
		}
		// 饭后
		if ("BLOOD_GLUCOSE1".equals(vitalSigns)) {
			m.put("blood_glucose1", t);
		}
		// 饭前
		if ("BLOOD_GLUCOSE2".equals(vitalSigns)) {
			m.put("blood_glucose2", t);
		}
		// 临时
		if ("BLOOD_GLUCOSE3".equals(vitalSigns)) {
			m.put("blood_glucose3", t);
		}
		// 空腹血糖
		if ("BLOOD_GLUCOSE01".equals(vitalSigns)) {
			m.put("blood_glucose01", t);
		}
		// 早餐前30min
		if ("BLOOD_GLUCOSE02".equals(vitalSigns)) {
			m.put("blood_glucose02", t);
		}
		// 早餐后2h
		if ("BLOOD_GLUCOSE03".equals(vitalSigns)) {
			m.put("blood_glucose03", t);
		}
		// 午餐前30min
		if ("BLOOD_GLUCOSE04".equals(vitalSigns)) {
			m.put("blood_glucose04", t);
		}
		// 午餐后2h
		if ("BLOOD_GLUCOSE05".equals(vitalSigns)) {
			m.put("blood_glucose05", t);
		}
		// 晚餐前30min
		if ("BLOOD_GLUCOSE06".equals(vitalSigns)) {
			m.put("blood_glucose06", t);
		}
		// 晚餐后2h
		if ("BLOOD_GLUCOSE07".equals(vitalSigns)) {
			m.put("blood_glucose07", t);
		}
		// 晚10点
		if ("BLOOD_GLUCOSE08".equals(vitalSigns)) {
			m.put("blood_glucose08", t);
		}
		// 凌晨0点
		if ("BLOOD_GLUCOSE09".equals(vitalSigns)) {
			m.put("blood_glucose09", t);
		}
		// 手术时间
		if ("B".equals(vitalSigns)) {
			m.put("B", t);
		}
		// 胸腔闭式引流（ml）
		if ("WOUND_DRAINAGE2".equals(vitalSigns)) {
			m.put("WOUND_DRAINAGE2", t);
		}
		// 脑室引流（ml）
		if ("WOUND_DRAINAGE3".equals(vitalSigns)) {
			m.put("WOUND_DRAINAGE3", t);
		}
		// 头皮下引流（ml）
		if ("WOUND_DRAINAGE4".equals(vitalSigns)) {
			m.put("WOUND_DRAINAGE4", t);
		}
		// 硬膜下引流（ml）
		if ("WOUND_DRAINAGE5".equals(vitalSigns)) {
			m.put("WOUND_DRAINAGE5", t);
		}
		// 硬膜外引流（ml）
		if ("WOUND_DRAINAGE6".equals(vitalSigns)) {
			m.put("WOUND_DRAINAGE6", t);
		}
		// 鼻腔出血（ml）
		if ("WOUND_DRAINAGE7".equals(vitalSigns)) {
			m.put("WOUND_DRAINAGE7", t);
		}
		// 护理记录
		if ("WARD_LOG".equals(vitalSigns)) {
			m.put("ward_log", t);
		}
		// 随机血糖
		if ("BLOOD_GLUCOSE10".equals(vitalSigns)) {
			m.put("blood_glucose10", t);
		}

	}
}