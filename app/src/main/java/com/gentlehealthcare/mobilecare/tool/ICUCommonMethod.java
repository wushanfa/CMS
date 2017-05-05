package com.gentlehealthcare.mobilecare.tool;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuBBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadIcuBResultBean;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUABean;
import com.gentlehealthcare.mobilecare.net.bean.SearchICUBBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dzw on 2015/11/6.
 */
public class ICUCommonMethod {

    private static List<EditText> editListFirst = new ArrayList<EditText>();
    private static List<EditText> editListSecond = new ArrayList<EditText>();
    private static List<EditText> editListThird = new ArrayList<EditText>();
    private static List<EditText> editListThreeFirst = new ArrayList<EditText>();
    private static List<EditText> editListThreeSecond = new ArrayList<EditText>();

    /**
     * 判断list中哪个editText 被选中了
     *
     * @param list
     * @return
     */
    public static int getEdtiText(List<EditText> list) {
        int i = 0;
        for (EditText et : list) {
            if (et.isFocused()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * 插入数组
     *
     * @param editText
     * @param whichOne
     * @return
     */
    public static List<EditText> addEditTextIntoList(EditText editText, int whichOne) {
        if (whichOne == 1) {
            editListFirst.add(editText);
            return editListFirst;
        }
        if (whichOne == 2) {
            editListSecond.add(editText);
            return editListSecond;
        }
        if (whichOne == 3) {
            editListThird.add(editText);
            return editListThird;
        }
        if (whichOne == 4) {
            editListThreeFirst.add(editText);
            return editListThreeFirst;
        }
        if (whichOne == 5) {
            editListThreeSecond.add(editText);
            return editListThreeSecond;
        }
        return null;
    }

    /**
     * 跳转到之前的一个edittext
     *
     * @param list
     * @param num
     */
    public static void perEditText(List<EditText> list, int num) {
        EditText et;
        if (num > 0) {
            et = list.get(num - 1);
            et.requestFocus();
        } else {
            et = list.get(0);
            et.requestFocus();
        }
    }

    /**
     * 跳转之后的一个editetext
     *
     * @param list
     * @param num
     */
    public static void nextEditText(List<EditText> list, int num) {
        EditText et;
        if (num < list.size() - 1) {
            et = list.get(num + 1);
            et.requestFocus();
        } else {
            et = list.get(list.size() - 1);
            et.requestFocus();
        }
    }

    /**
     * 清空list中edittext的值
     */
    public static void clearEditText() {
        for (EditText et : editListFirst) {
            et.setText("");
        }
        for (EditText et : editListSecond) {
            et.setText("");
        }
        for (EditText et : editListThird) {
            et.setText("");
        }
        for (EditText et : editListThreeFirst) {
            et.setText("");
        }
        for (EditText et : editListThreeSecond) {
            et.setText("");
        }
    }

    public static void clearButton(Button b1, Button b2, Button b3, Button b4, Button b5, Button b6) {
        b1.setText("治疗/饮食：");
        b2.setText("药         物：");
        b3.setText("血         液：");
        b4.setText("胃         肠：");
        b5.setText("泵入 药物：");
        b6.setText("出         量：");
    }

    public static void assignmentEt(Map<String, SearchICUABean> searchMap, String vitalSigns, EditText et) {
        for (String key : searchMap.keySet()) {
            if (key.equals(vitalSigns)) {
                SearchICUABean bean = searchMap.get(key);
                et.setText(bean.getVitalSignsValues());
            }
        }
    }

    public static void assignmentEt(Map<String, SearchICUABean> searchMap, String vitalSigns, EditText et,
                                    Button btn) {
        for (String key : searchMap.keySet()) {
            if (key.equals(vitalSigns)) {
                SearchICUABean bean = searchMap.get(key);
                et.setText(bean.getVitalSignsValues());
                btn.setText(bean.getMemo());
            }
        }
    }

    /**
     * 点击显示和隐藏详细视图
     *
     * @param first  thumbnail view
     * @param second detail view
     * @param tv     content conversion
     * @param iv     conversion picture
     */

    public static void clickSwitch(LinearLayout first, LinearLayout second, TextView tv, TextView iv) {
        if (second.getVisibility() == View.GONE) {
            second.setVisibility(View.VISIBLE);
            tv.setText("点击折叠详细内容");
            iv.setBackgroundResource(R.drawable.up);
        } else {
            second.setVisibility(View.GONE);
            tv.setText("点击查看详细内容并进行填写");
            iv.setBackgroundResource(R.drawable.down);
        }
    }

    /**
     * initDate
     *
     * @param calendar
     * @param datepick
     */
    public static void initDate(Calendar calendar, DatePicker datepick) {

        int year = 0;
        int month = 0;
        int day = 0;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datepick.init(year, month, day, null);

    }

    /**
     * initTime
     *
     * @param calendar
     * @param timepick
     */
    public static void initTime(Calendar calendar, TimePicker timepick) {

        int hour = 0;
        int min = 0;

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(calendar.MINUTE);

        timepick.setIs24HourView(true);
        timepick.setCurrentHour(hour);
        timepick.setCurrentMinute(min);
    }

    /**
     * 根据选择，生成时间字符串
     *
     * @param myLongDate
     * @param arrive_year
     * @param arrive_month
     * @param arrive_day
     * @param arrive_hour
     * @param arrive_min
     * @param datepick
     * @param timepick
     * @return
     */
    public static String getCurrentTime(String myLongDate, int arrive_year, int arrive_month, int
            arrive_day, int arrive_hour, int arrive_min, DatePicker datepick, TimePicker timepick) {
        arrive_year = datepick.getYear();
        arrive_month = datepick.getMonth() + 1;
        arrive_day = datepick.getDayOfMonth();

        arrive_hour = timepick.getCurrentHour();
        arrive_min = timepick.getCurrentMinute();

        myLongDate = DateTool.appendDateOrder(arrive_year,
                arrive_month, arrive_day, arrive_hour, arrive_min);
        return myLongDate;
    }

    public static Calendar parseDateForIcu(String waitForParse) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date formatDate = null;
        try {
            formatDate = sf.parse(waitForParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(formatDate);
        return cal;
    }

    /**
     * 解析数据，绑定spinner的布局
     */
    public static int bindingAdapter(Context context, ArrayAdapter<String> adapter, Spinner spn,
                                     List<LoadIcuBBean> list, String requireItemNo) {
        int position = 0;
        for (LoadIcuBBean bean : list) {
            List<LoadIcuBResultBean> options = bean.getOptions();
            if (requireItemNo.equals(bean.getItemNo())) {
                String[] items = new String[(options.size() + 1)];
                for (int i = 0; i < options.size(); i++) {
                    items[i] = options.get(i).getItemName();
                }
                items[options.size()] = "";
                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
                adapter.setDropDownViewResource(R.layout.drop_down_item);
                spn.setAdapter(adapter);
                spn.setSelection(options.size(), true);
                break;
            } else {
                position++;
            }
        }
        return position;
    }

    public static void clearSpinner(Spinner spn, Map<String, LoadIcuBBean> map, String requireItemNo) {
        if (map.containsKey(requireItemNo)) {
            List<LoadIcuBResultBean> options = map.get(requireItemNo).getOptions();
            spn.setSelection(options.size(), true);
        }
    }

    public static boolean canSubmitSpinner(Spinner spn, Map<String, LoadIcuBBean> map, String requireItemNo,
                                           boolean bool) {
        if (map.containsKey(requireItemNo)) {
            List<LoadIcuBResultBean> options = map.get(requireItemNo).getOptions();
            if (spn.getSelectedItemPosition() == options.size()) {
                bool = true;
            }
        }
        return bool;
    }

    public static boolean canSubmitSpinnerSpine(Spinner spn, Map<String, LoadIcuBBean> map, String
            requireItemNo) {
        boolean bool = false;
        if (map.containsKey(requireItemNo)) {
            List<LoadIcuBResultBean> options = map.get(requireItemNo).getOptions();
            if (spn.getSelectedItemPosition() == options.size()) {
                bool = true;
            }
        }
        return bool;
    }

    public static void showDialogForRemindTime(Context context) {
        new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setTitle("请选择时间").setMessage
                ("请先选择时间再操作。").setPositiveButton("确定", null).show();
    }

    public static void showDialogForRemindInspect(Context context) {
        new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setTitle("请填写数值").setMessage
                ("请先填写数值再操作。").setPositiveButton("确定", null).show();
    }

    public static void showDialogContent(Context context) {
        new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setTitle("请填写数值").setMessage
                ("请至少填写一项，再进行提交。").setPositiveButton("确定", null).show();
    }

    /**
     * 护理评价用
     *
     * @param et
     * @param dictItem
     * @param orgItemNo
     */
    public static void textWatcherMethod(EditText et, List<LoadIcuBBean> dictItem, String
            orgItemNo) {
        Map<String, Object> icub = new HashMap<String, Object>();
        String temp = et.getText().toString();
        if (!temp.equals("")) {
            LoadIcuBBean bean = new LoadIcuBBean();
            Map<String, LoadIcuBBean> itemMap = new HashMap<String, LoadIcuBBean>();
            for (LoadIcuBBean item : dictItem) {
                itemMap.put(item.getItemNo(), item);
            }
            if (itemMap.containsKey(orgItemNo)) {
                bean = itemMap.get(orgItemNo);
            }
            icub = ICUDataMethod.getICUB(bean.getItemNo(), "nothing", temp,
                    "nothing", "nothing", "nothing", "nothing");
            ICUDataMethod.addUpdateB(icub);
            ICUDataMethod.addSaveB(icub);
        }
    }

    /**
     * 护理内容用
     */
    public static void textWatcherMethodForContent(EditText et, EditText et2, Map<String, LoadIcuBBean> itemMap,
                                                   String orgItemNo) {
        Map<String, Object> icub = new HashMap<String, Object>();
        String temp = et.getText().toString();
        String temp2 = et2.getText().toString();
        String valueDesc = "nothing";
        if (!temp.equals("")) {
            LoadIcuBBean bean = new LoadIcuBBean();
            if (itemMap.containsKey(orgItemNo)) {
                bean = itemMap.get(orgItemNo);
            }
            if (!temp2.equals("")) {
                valueDesc = temp2;
            }
            icub = ICUDataMethod.getICUB(bean.getItemNo(), "nothing", temp, "nothing", "nothing", valueDesc,
                    "nothing");
            ICUDataMethod.addUpdateB(icub);
            ICUDataMethod.addSaveB(icub);
        }
    }


    /**
     * 护理评价用
     *
     * @param dictItem
     * @param position
     * @param spn
     * @param orgItemNo
     */
    public static void searchListToSpinner(Map<String, SearchICUBBean> map, List<LoadIcuBBean> dictItem, int
            position, Spinner spn, String orgItemNo) {
        if (map.containsKey(orgItemNo)) {
            SearchICUBBean bean = map.get(orgItemNo);
            List<LoadIcuBResultBean> options = dictItem.get(position).getOptions();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).getItemCode().equals(bean.getValueCode())) {
                    spn.setSelection(i, true);
                }
            }
        }
    }

    /**
     * 把查询到的结果放入editText中
     */
    public static String searchForContent(List<SearchICUBBean> searchList, String orgitemNo) {
        String result = "";
        for (int i = 0; i < searchList.size(); i++) {
            SearchICUBBean bean = searchList.get(i);
            if (bean.getItemNo().equals(orgitemNo)) {
                result = bean.getValueName();
                break;
            }
        }
        return result;
    }

    /**
     * 把查询到的结果放入editText中
     */
    public static void searchForContent(List<SearchICUBBean> searchList, String orgitemNo, EditText name,
                                        EditText memo) {
        String result = "";
        String valueDesc = "";
        for (int i = 0; i < searchList.size(); i++) {
            SearchICUBBean bean = searchList.get(i);
            if (bean.getItemNo().equals(orgitemNo)) {
                result = bean.getValueName();
                valueDesc = bean.getValueDesc();
                break;
            }
        }
        name.setText(result);
        memo.setText(valueDesc);
    }

    /**
     * search 放入edittext
     */
    public static void searchListToEditText(Map<String, SearchICUBBean> map, EditText et, String orgITemNo) {
        if (map.containsKey(orgITemNo)) {
            SearchICUBBean searchICUBBean = map.get(orgITemNo);
            et.setText(searchICUBBean.getValueName());
        }
    }

    /**
     * 处理点击spinner
     *
     * @param dictItem
     * @param spnPosition
     * @param position
     */
    public static void getSpinnerResult(List<LoadIcuBBean> dictItem, int spnPosition, int position) {
        LoadIcuBBean bean = dictItem.get(spnPosition);
        if (bean.getOptions().size() != position) {
            LoadIcuBResultBean resultBean = bean.getOptions().get(position);
            Map<String, Object> icub = ICUDataMethod.getICUB(bean.getItemNo(), resultBean.getItemCode(),
                    resultBean.getItemName(), resultBean.getItemCodeType(),
                    resultBean.getAbnormalAttr(), "nothing", "nothing");
            ICUDataMethod.addSaveB(icub);
            ICUDataMethod.addUpdateB(icub);
        }
    }


}
