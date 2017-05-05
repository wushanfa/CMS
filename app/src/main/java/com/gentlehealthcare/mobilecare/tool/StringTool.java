package com.gentlehealthcare.mobilecare.tool;

import android.annotation.SuppressLint;

import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {

  private final static ThreadLocal<SimpleDateFormat> dateFormater =
      new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
          return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
      };

  private final static ThreadLocal<SimpleDateFormat> dateFormater2 =
      new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
          return new SimpleDateFormat("yyyy-MM-dd");
        }
      };

  public static String splitString(String str) {

    String[] measures = str.split("\\[\\d\\]");
    final int mStrLen = measures.length;
    String measureSB = "";
    for (int a = 1; a < mStrLen; a++) {
      measureSB += (a) + "." + measures[a] + "\n";
      CCLog.i("AB", measureSB);
    }
    if (!measureSB.equals("")) {
      return measureSB;
    } else {
      return null;
    }
  }

  public static boolean isEmpty(Object str) {
    return str == null || ((String) str).length() == 0 || str.equals("");
  }

  public static boolean isEmpty(String str) {
    if (str == null && str == "") {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  public static boolean isNotEmpty(String str, String str2) {
    return isNotEmpty(str) && isNotEmpty(str2);
  }

  /**
   * 2016-05-13 08:00:00转换成08:00
   */
  public static String dateToTime(String date) {
    if (isNotEmpty(date)) {
      return date.substring(11, 16);
    }
    return "00:00";
  }

  /**
   * 2016-05-13 08:00:00转换成05-13 08:00
   */
  public static String monthDayTimeNoSecond(String date) {
    if (isNotEmpty(date)) {
      return date.substring(5, 16);
    }
    return " ";
  }

  /**
   * null替换成""
   */
  public static String nullToSpace(String str) {
    String string = " ";
    if (str != null && !str.equals("")) {
      string = str;
    }
    return string;
  }

  /**
   * 半角转换为全角,去掉一些符号
   *
   * @param input
   * @return
   */
  public static String toUnify(String input) {
    char[] c = input.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == 12288) {
        c[i] = (char) 32;
        continue;
      }
      if (c[i] > 65280 && c[i] < 65375)
        c[i] = (char) (c[i] - 65248);
    }
    String temp = new String(c);
    temp = temp.replaceAll("【", "[").replaceAll("】", "]").replaceAll("%", "%").replaceAll("！", "!")
        .replaceAll("：", ":").replaceAll("（", "(").replace("）", ")");
    String regEx = "[『』]";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(temp);
    String result = m.replaceAll("").trim();
    return result;
  }

  /**
   * str中child字符串个数
   *
   * @param str2
   * @param child
   * @return
   */
  public static int numberOfChar(String str2, String child) {
    String str = str2 + "F";
    if (str.indexOf(child) != -1) {
      String str1 = "\\" + child;
      String[] strings = str.split(str1);
      return strings.length - 1;
    } else {
      return 0;
    }
  }

  /**
   * @param str
   * @param num 复制几分
   * @return
   */
  public static String cloneOrg(String str, int num) {
    StringBuffer result = new StringBuffer(str);
    if (num > 0) {
      for (int i = 0; i < num; i++) {
        result.append(str);
      }
    }
    return result.toString();
  }

  public static String replaceStr(String str) {
    String resultStr = null;
    if (str.contains("||")) {
      resultStr = str.replace("||", "\n");
    } else {
      resultStr = str;
    }
    return resultStr;
  }

  public static String replaceStrForLine(OrderListBean bean, int lineCount) {
    String result;
    String orderText = bean.getOrderText();
    String dosage = bean.getDosage();
    int line = getLine(orderText);
    if (dosage.contains("||")) {
      int count = (int) Math.floor(lineCount / line);
      String temp = cloneOrg("\n", count);
      result = dosage.replace("||", temp);
    } else {
      result = dosage;
    }
    return result;
  }

  public static int getLine(String str) {
    String[] split = str.split("\\|\\|");
    return split.length;
  }

  /**
   * 将字符串转位日期类型
   *
   * @param sdate
   * @return
   */
  public static Date toDate(String sdate) {
    try {
      return dateFormater.get().parse(sdate);
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * 以友好的方式显示时间
   * 是指实际间隔24小时算
   *
   * @param sdate
   * @return
   */
  public static String friendlyTime(String sdate) {
    Date time = toDate(sdate);
    if (time == null) {
      return "00-00";
    }
    String ftime = "";
    Calendar cal = Calendar.getInstance();
    // 判断是否是同一天
    String curDate = dateFormater2.get().format(cal.getTime());
    String paramDate = dateFormater2.get().format(time);
    if (curDate.equals(paramDate)) {
      int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
      if (hour == 0) {
        ftime = Math.max(
            (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
            + "分钟前";
      } else {
        ftime = hour + "小时前";
      }
      return " ";
    }

    long lt = time.getTime() / 86400000;
    long ct = cal.getTimeInMillis() / 86400000;
    int days = (int) (ct - lt);
    if (days == 0) {
      int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
      if (hour == 0)
        ftime = Math.max(
            (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
            + "分钟前";
      else
        ftime = hour + "小时前";
      ftime = " ";
    } else if (days == 1) {
      ftime = "昨天";
    } else if (days == 2) {
      ftime = "2天前";
    } else if (days > 2) {
      ftime = days + "天前";
    } else if (days == -1) {
      ftime = "明天";
    } else if (days < -1) {
      ftime = Math.abs(days) + "天后";
    }
    return ftime;
  }

  /**
   * 判断是否在6小时以内
   *
   * @param sdate
   * @return
   */
  public static boolean isSixHours(String sdate) {
    Date time = toDate(sdate);
    if (time == null) {
      return false;
    }
    String ftime = "";
    Calendar cal = Calendar.getInstance();
    // 判断是否是同一天
    String curDate = dateFormater2.get().format(cal.getTime());
    String paramDate = dateFormater2.get().format(time);
    if (curDate.equals(paramDate)) {
      int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
      if (hour < 6) {
        return true;
      } else {
        return false;
      }
    }
    long lt = time.getTime() / 86400000;
    long ct = cal.getTimeInMillis() / 86400000;
    int days = (int) (ct - lt);
    if (days == 0) {
      int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
      if (hour < 6) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * 拼合方法，在静脉，胰岛素，医嘱里都有使用，方便修改,title显示方式
   *
   * @param orderListBean
   * @return
   */
  public static String pieceSection(OrderListBean orderListBean) {
    String title = StringTool.dateToTime(orderListBean.getStartTime()) + " ";
    if (orderListBean.getNursingDesc() != null) {
      title += orderListBean.getNursingDesc();
    }
    return title;
  }

  public static boolean lastScanPatient(String scanMode, String patId) {

    if (patId.equals(GlobalConstant.PATID)) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean patternCode(String patternStr, String matcherStr) {
    Pattern p = Pattern.compile(patternStr.trim());
    Matcher m = p.matcher(matcherStr.trim());
    boolean b = m.matches();
    return b;
  }

    /**
     * 删除StringBuffer最后一个字符（包括空字符）
     * @param sb
     * @return
     */
  public static String delateLastChar(StringBuffer sb) {
    if(sb.length()==0){
      return "";
    }else{
    while (sb.charAt(sb.length() - 1) == ' ' || sb.charAt(sb.length() - 1) == '\t') {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
    }
  }

  public static String isBedNumber(String str){
    if(str.isEmpty()){
      return " ";
    }else{
      if(str.contains("床")){
        return str;
      }else{
        return str+"床";
      }
    }
  }
}
