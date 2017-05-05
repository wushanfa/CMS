package com.gentlehealthcare.mobilecare.tool;

import android.util.Log;

import com.gentlehealthcare.mobilecare.constant.GlobalConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static com.gentlehealthcare.mobilecare.tool.StringTool.toDate;

/**
 * @author cally
 * @class DateTool
 * @date 2012-11-23 上午12:31:20
 * @description 时间工具类
 */
public class DateTool {

  public static final String TAG = DateTool.class.getSimpleName();
  /**
   * 年月（六位）
   */
  public static final int YYYY_MM = -1;
  /**
   * 年月日（八位）
   */
  public static final int YYYY_MM_DD = 0;
  /**
   * 年月日时分（十二位）
   */
  public static final int YYYY_MM_DD_HH_MM = 1;
  /**
   * 年月日时分秒（十四位）
   */
  public static final int YYYY_MM_DD_HH_MM_SS = 2;

  public static final int YYYY_MM_DD_HH_MM_SS_STYPE = 4;

  /**
   * 年月日格式（八位）
   */
  public static final int YYYY_MM_DD_STYPE = 3;

  /**
   * 年月日时分秒毫秒（十四位）
   */
  public static final int YYYY_MM_DD_HH_MM_SS_SSS = 5;

  public static final int HH_MM_SS = 6;

  public static final int HH_MM = 7;
  /**
   * 月日格式
   */
  public static final int MM_DD = 8;

  /**
   * 日历对象
   */
  static Calendar cal = Calendar.getInstance();

  /**
   * @param type 转换类型
   * @param date 需转换的时间
   * @return
   * @description 时间格式转换
   * @author cally
   * @date 2012-11-23 上午12:40:50
   */
  public static String formatDate(int type, Date date) {
    String format = "";
    if (date == null) {
      return "";
    }
    if (type == YYYY_MM_DD) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM_DD_HH_MM) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM_DD_HH_MM_SS) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM_DD_STYPE) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM_DD_HH_MM_SS_SSS) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm:ss:SSS");
      format = dateFormat.format(date);
    } else if (type == HH_MM) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
      format = dateFormat.format(date);
    } else if (type == GlobalConstant.DATE_TIME) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      format = dateFormat.format(date);
    } else if (type == GlobalConstant.DATE_TIME_SIMPLE) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      format = dateFormat.format(date);
    } else if (type == MM_DD) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
      format = dateFormat.format(date);
    }
    return format;
  }

  /**
   * @param type 转换类型
   * @param date 需转换的时间
   * @return
   * @description 时间格式转换
   * @author cally
   * @date 2012-11-23 上午12:40:50
   */
  public static long parseDate(int type, String date) {
    long longtime = 0;
    if (date == null) {
      return 0;
    }
    if (type == YYYY_MM_DD_HH_MM_SS) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm:ss");
      try {
        longtime = dateFormat.parse(date).getTime();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return longtime;
  }

  /**
   * 根据HH:MM时间格式获取总分钟数
   *
   * @param date
   * @return
   */
  public static int GetMinuteByHourAndMinute(String date) {
    if (date.length() >= 5) {
      int hour = Integer.valueOf(date.substring(0, 2));
      int minute = Integer.valueOf(date.substring(3, 5));
      return hour * 60 + minute;
    } else {
      return 0;
    }
  }

  /**
   * @param type 转换类型
   * @param date 需转换的时间
   * @return
   * @description 时间格式转换
   * @author cally
   * @date 2012-11-23 上午12:40:50
   */
  public static String parseDate(int type, Long date) {
    String format = "";
    if (date == null) {
      return "";
    }
    if (type == YYYY_MM_DD) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM_DD_HH_MM) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM_DD_HH_MM_SS) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM_DD_HH_MM_SS_STYPE) {

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      format = dateFormat.format(date);
      format += "T";
      dateFormat = new SimpleDateFormat("HH:mm:ss");
      format += dateFormat.format(date);
    } else if (type == YYYY_MM_DD_STYPE) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
      format = dateFormat.format(date);
    } else if (type == YYYY_MM_DD_HH_MM_SS_SSS) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(
          "yyyy-MM-dd HH:mm:ss:SSS");
      format = dateFormat.format(date);
    } else if (type == HH_MM_SS) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      format = dateFormat.format(date);
    } else if (type == HH_MM) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
      format = dateFormat.format(date);
    }
    return format;
  }

  /**
   * @param str
   * @return
   * @description 解析Date
   * @author cally
   * @date 2012-12-14 下午3:30:20
   */
  public static String parseDate(String str) {
    if (str == null || "".equals(str) || str.length() < 12) {
      return "";
    }
    return str.substring(0, 4) + "-" + str.substring(4, 6) + "-"
        + str.substring(6, 8) + " " + str.substring(8, 10) + ":"
        + str.substring(10, 12);
  }

  /**
   * @param str
   * @return
   * @description 解析Date
   * @author cally
   * @date 2012-12-14 下午3:30:20
   */
  public static String parseDate8(String str) {
    if (str == null || "".equals(str) || str.length() < 8) {
      return "";
    }
    return str.substring(0, 4) + "-" + str.substring(4, 6) + "-"
        + str.substring(6, 8);
  }

  public static String getHourAndMinute(String fomartStr) {
    if (fomartStr == null || "".equals(fomartStr)
        || fomartStr.length() < 16) {
      return "";
    }
    return fomartStr.substring(11, 16);
  }

  public static String getHourAndMinuteAndSecond(String fomartStr) {
    if (fomartStr == null || "".equals(fomartStr)
        || fomartStr.length() < 19) {
      return "";
    }
    return fomartStr.substring(11, 19);
  }

  public static boolean isOverdue(String startTimeStr) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    try {
      Calendar startTime = Calendar.getInstance();
      startTime.setTime(dateFormat.parse(startTimeStr));
      Calendar endTime = Calendar.getInstance();
      endTime.setTime(new Date());
      Log.d("OY", "starttime hour:" + startTime.get(Calendar.HOUR_OF_DAY));
      Log.d("OY", "endTime hour:" + endTime.get(Calendar.HOUR_OF_DAY));
      Log.d("OY", "starttime minute:" + startTime.get(Calendar.MINUTE));
      Log.d("OY", "endTime minute:" + endTime.get(Calendar.MINUTE));

      if (endTime.get(Calendar.HOUR_OF_DAY)
          - startTime.get(Calendar.HOUR_OF_DAY) > 0) {
        Log.d("OY", " hour 逾期");
        return true;
      } else if (endTime.get(Calendar.HOUR_OF_DAY) == startTime
          .get(Calendar.HOUR_OF_DAY)
          && endTime.get(Calendar.MINUTE)
              - startTime.get(Calendar.MINUTE) > 30) {
        Log.d("OY", " minute 逾期");
        return true;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 根据时间戳获取
   *
   * @param Timestamp
   * @return
   */
  public static String getHourMinuteSecond(String Timestamp) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    return dateFormat.format(Long.valueOf(Timestamp));
  }


  public static String getHourMinuteSecond() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    return dateFormat.format(new Date());
  }

  public static String getHMS30Min() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Calendar nowTime = Calendar.getInstance();
    nowTime.add(Calendar.MINUTE, 30);
    return dateFormat.format(nowTime.getTime());
  }

  /**
   * @return
   * @description 当前时间+随机数
   * @author cally
   * @date 2012-12-4 下午7:34:06
   */
  public static String getTmName() {
    SimpleDateFormat simpleFormat = new SimpleDateFormat("MMddHHmmsss");
    String generationfileName = simpleFormat.format(new Date())
        + new Random().nextInt(1000);
    return generationfileName;
  }

  /**
   * 获取月初
   */
  public static String yueChu(Date date) {
    if (date == null) {
      return null;
    }
    // SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
    return formatDate(YYYY_MM, date) + "01000000";/* format1.format(date); */
  }

  /***
   * 添加一天
   *
   * @param year
   * @param monthOfYear
   * @param dayOfMonth
   * @return
   */
  public static String addOneDay(int year, int monthOfYear, int dayOfMonth) {
    Calendar c = Calendar.getInstance();
    if (year == 0 && monthOfYear == 0 && dayOfMonth == 0) {
      c.setTime(new Date());
      year = c.get(Calendar.YEAR);
      monthOfYear = c.get(Calendar.MONTH);
      dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    } else {
      c.set(Calendar.YEAR, year);
      c.set(Calendar.MONTH, monthOfYear);
    }
    int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
    if (dayOfMonth < maxDay) {
      dayOfMonth += 1;
    } else if (dayOfMonth == maxDay) {
      if (monthOfYear == 11) {
        year += 1;
        monthOfYear = 0;
        dayOfMonth = 1;
      } else {
        monthOfYear += 1;
        dayOfMonth = 1;
      }
    }
    c.set(year, monthOfYear, dayOfMonth);
    return formatDate(YYYY_MM_DD, c.getTime());
  }

  public static int getHour(String timestemp) {
    if (timestemp == null || "".equals(timestemp)
        || timestemp.length() < 13) {
      return 0;
    }
    return Integer.valueOf(timestemp.substring(11, 13));
  }

  /**
   * 获取系统当前时间
   *
   * @return
   */
  public static String getCurrDate() {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date dt = new Date();

    return df.format(dt);

  }

  /**
   * 获取系统当前时间
   *
   * @return
   */
  public static String getCurrTime() {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    Date dt = new Date();

    return df.format(dt);

  }

  /**
   * 获取当前年份
   */
  public static int getCurrYear() {

    return cal.get(Calendar.YEAR);

  }

  /**
   * 获取当前月份
   */
  public static int getCurrMonth() {

    return cal.get(Calendar.MONTH) + 1;

  }

  /**
   * 获取当前day
   */
  public static int getCurrDay() {

    return cal.get(Calendar.DATE);

  }

  /**
   * 获取系统当前时间精确到秒
   *
   * @return
   */
  public static String getCurrDateTime() {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date dt = new Date();

    return df.format(dt);

  }

  /**
   * 根据年月日拼字符串
   *
   * @param arrive_year
   * @param arrive_month
   * @param arrive_day
   * @param arrive_time
   * @return
   */
  public static String appendDate(int arrive_year, int arrive_month, int arrive_day,
      int arrive_time) {
    return arrive_year + "-" + arrive_month + "-" + arrive_day + " " + arrive_time + ":00";
  }

  public static String appendDateOrder(int arrive_year, int arrive_month,
      int arrive_day, int arrive_time, int arrive_min) {
    return arrive_year + "-" + arrive_month + "-" + arrive_day + " "
        + arrive_time + ":" + arrive_min;
  }

  public static String appendDateDouleDay(int arrive_year, int arrive_month,
      int arrive_day) {
    return arrive_year + "-" + arrive_month + "-" + (arrive_day - 1) + "~"
        + arrive_year + "-" + arrive_month + "-" + arrive_day;
  }

  /**
   * 获取当前day
   */
  public static int getCurrHour() {

    return cal.get(Calendar.HOUR_OF_DAY);

  }

  /**
   * 获取当前day
   */
  public static int getCurrMIn() {

    return cal.get(Calendar.MINUTE);

  }

  /**
   * 根据当前时间，获取年--0,月--1,日--2
   *
   * @return
   */
  public static int getCurrentTimeByOrder(String date, int type) {
    int result = 0;
    if (date.length() > 10) {
      date = date.substring(0, 10);
    }
    String[] ds = date.split("-");
    switch (type) {
      case 0:
        result = Integer.parseInt(ds[type]);
        break;
      case 1:
        result = (Integer.parseInt(ds[type]) - 1);
        break;
      case 2:
        result = Integer.parseInt(ds[type]);
        break;

      default:
        break;
    }
    return result;
  }

  /**
   * 获取系统当前时间精确到fen
   *
   * @return
   */
  public static String getCurrDateTimeS() {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date dt = new Date();

    return df.format(dt);

  }

  /**
   * yyyyMMddhhmm转换成 yyyy-MM-dd HH:mm
   *
   * @return
   */
  public static String yyyyMMddhhmmToDate(String yyyyMMddhhmm) {

    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmm");
    Date date = null;
    try {
      date = (Date) sdf1.parse(yyyyMMddhhmm);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    return sdf2.format(date);
  }

  public static String toMinAndSecond(String time) {
    SimpleDateFormat sdfOld = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat sdf = new SimpleDateFormat("HH点mm分ss");
    Date date = null;
    try {
      date = sdfOld.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return sdf.format(date);
  }

  public static String toTpr(String time) {
    SimpleDateFormat sdfOld = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    Date date = null;
    try {
      date = sdfOld.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return sdf.format(date);
  }

  public static String toGetComplete(String time) {
    String newTime = "";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date parse = new Date();
    try {
      parse = sdf.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    newTime = sdf.format(parse);
    return newTime;
  }

  public static String spliteDate(int type, String time) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd HH:mm");
    Date parse = null;
    try {
      parse = sdf.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (GlobalConstant.TIME == type) {
      return sdf2.format(parse);
    } else if (GlobalConstant.DATE == type) {
      return sdf1.format(parse);
    } else if (GlobalConstant.DATE_TIME_PART == type) {
      return sdf3.format(parse);
    } else {
      return time;
    }
  }

  public static String todayTomorryYesterday(String sDate) {
    if (StringTool.isEmpty(sDate)) {
      return " ";
    } else {

      Calendar aCalendar = Calendar.getInstance();
      aCalendar.setTime(new Date());
      int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
      aCalendar.setTime(toDate(sDate));
      int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
      int day = day2 - day1;// 时间-系统时间
      if (day == -1) {
        return "昨天";
      } else if (day == 1) {
        return "明天";
      } else if (day == 0) {
        return " ";
      } else {
        return formatDate(8, toDate(sDate));
      }

    }

  }

}
