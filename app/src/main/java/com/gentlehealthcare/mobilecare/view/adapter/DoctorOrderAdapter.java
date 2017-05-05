package com.gentlehealthcare.mobilecare.view.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.LoadAppraislRecordBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadDocOrdersBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrdersBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrdersInfo;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.StringTool;

/**
 * 医嘱适配器 Created by ouyang on 2015/5/26.
 */
public class DoctorOrderAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<LoadDocOrdersBean> doctorOrderBeanList;
    private LoadOrdersBean loadOrdersInfo = null;
    private CallBack mCallBack;
    private List<LoadAppraislRecordBean> larList;

    public DoctorOrderAdapter(Context context, List<LoadDocOrdersBean> doctorOrderBeanList, CallBack mcallBack) {
        this.context = context;
        this.doctorOrderBeanList = doctorOrderBeanList;
        this.mCallBack = mcallBack;
    }

    @Override
    public int getGroupCount() {
        return doctorOrderBeanList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (doctorOrderBeanList == null || doctorOrderBeanList.isEmpty() || doctorOrderBeanList.size() == 0) {
            return 0;
        } else {
            if (doctorOrderBeanList.get(groupPosition).getOrders() == null || doctorOrderBeanList.get
                    (groupPosition)
                    .getOrders().isEmpty()) {
                return 0;
            } else {
                return doctorOrderBeanList.get(groupPosition).getOrders().size();
            }
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return doctorOrderBeanList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return doctorOrderBeanList.get(groupPosition).getOrders()
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.act_doctororder_title, null);
            viewHolder.tv_title_left = (TextView) convertView.findViewById(R.id.tv_title_left);
            viewHolder.tv_title_right = (TextView) convertView.findViewById(R.id.tv_title_right);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String title = doctorOrderBeanList.get(groupPosition).getStartTime();
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日#HH点mm分");
        String newTitle = "";
        try {
            Date date = oldFormat.parse(title);
            newTitle = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.tv_title_left.setText(newTitle.split("#")[0]);
        viewHolder.tv_title_right.setText(newTitle.split("#")[1]);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.act_doctororder_content, null);
            viewHolder.tv_do_left = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.cb_check = (TextView) convertView.findViewById(R.id.cb_check);
            viewHolder.iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            viewHolder.tv_content_my = (TextView) convertView.findViewById(R.id.tv_content_my);
            viewHolder.detail = (Button) convertView.findViewById(R.id.detail);
            viewHolder.endtime = (Button) convertView.findViewById(R.id.endtime);
            viewHolder.pingjia = (Button) convertView.findViewById(R.id.pingjia);
            viewHolder.yichang = (Button) convertView.findViewById(R.id.yichang);
            viewHolder.top_right_ll = (LinearLayout) convertView.findViewById(R.id.top_right_ll);
            viewHolder.bottom_relation = (LinearLayout) convertView.findViewById(R.id.bottom_relation);
            viewHolder.comment_tv_image = (TextView) convertView.findViewById(R.id.comment_tv_image);
            viewHolder.comment_tv_text = (TextView) convertView.findViewById(R.id.comment_tv_text);
            viewHolder.execpteion_tv_image = (TextView) convertView.findViewById(R.id.execpteion_tv_image);
            viewHolder.execpteion_tv_text = (TextView) convertView.findViewById(R.id.exeception_tv_text);
            viewHolder.detail_tv_image = (TextView) convertView.findViewById(R.id.detail_tv_image);
            viewHolder.detail_tv_text = (TextView) convertView.findViewById(R.id.detail_tv_text);
            viewHolder.endtime_tv_image = (TextView) convertView.findViewById(R.id.endtime_tv_image);
            viewHolder.endtime_tv_text = (TextView) convertView.findViewById(R.id.endtime_tv_text);
            viewHolder.tv_speed_txt = (TextView) convertView.findViewById(R.id.tv_speed_txt);
            viewHolder.tv_dosage_txt = (TextView) convertView.findViewById(R.id.tv_dosage_txt);
            viewHolder.tv_frequency_txt = (TextView) convertView.findViewById(R.id.tv_frequency_txt);
            viewHolder.tv_pishi_txt = (TextView) convertView.findViewById(R.id.tv_pishi_txt);
            viewHolder.ll_dosage = (LinearLayout) convertView.findViewById(R.id.ll_dosage);
            viewHolder.ll_frequency = (LinearLayout) convertView.findViewById(R.id.ll_frequency);
            viewHolder.ll_piShi = (LinearLayout) convertView.findViewById(R.id.ll_piShi);
            viewHolder.ll_repeat = (LinearLayout) convertView.findViewById(R.id.ll_repeat);
            viewHolder.ll_speed = (LinearLayout) convertView.findViewById(R.id.ll_speed);
            viewHolder.ll_property = (LinearLayout) convertView.findViewById(R.id.ll_property);
            viewHolder.ll_administration = (LinearLayout) convertView.findViewById(R.id.ll_administration);
            viewHolder.tv_repeat = (TextView) convertView.findViewById(R.id.tv_repeat);
            viewHolder.tv_repeat_txt = (TextView) convertView.findViewById(R.id.tv_repeat_txt);
            viewHolder.tv_administration_txt = (TextView) convertView.findViewById(R.id.tv_administration_txt);
//            viewHolder.tv_content_dosage = (TextView) convertView.findViewById(R.id.tv_content_dosage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        List<LoadOrdersInfo> subOrders = doctorOrderBeanList.get(groupPosition).getOrders().get(childPosition)
                .getSubOrders();
        StringBuffer stringBuffer = new StringBuffer();
        if (subOrders != null && !subOrders.isEmpty()) {
            /**
             * 之前中草药的单位textview，现在不要显示
             */
//        StringBuffer stringBufferDosage = new StringBuffer();
            if (subOrders.get(0).getOrderClass().equals("中草药")) {
                if (subOrders.size() > 1) {
                    stringBuffer.append("中草药(" + subOrders.get(0).getOrderText() + subOrders.get(1).getOrderText
                            () +
                            "...)");
                } else {
                    stringBuffer.append("中草药(" + subOrders.get(0).getOrderText() + "...)");
                }
            } else {
                for (int i = 0; i < subOrders.size(); i++) {
                    LoadOrdersInfo li = subOrders.get(i);
                    stringBuffer.append(li.getOrderText());
//            if (subOrders.get(0).getOrderClass().equals("中草药")) {
//                stringBufferDosage.append(li.getDosage());
//            }
                    if (i != subOrders.size() - 1) {
                        stringBuffer.append("\n");
//                stringBufferDosage.append("\n");
                    }
                }
            }

            if (subOrders.get(0).getNurseEffect() != null) {
                stringBuffer.append("\n");
                stringBuffer.append("评价：");
                String nurse = subOrders.get(0).getNurseEffect();
                stringBuffer.append(nurse);
            }
            if (subOrders.get(0).getVarianceCauseDesc() != null) {
                stringBuffer.append("\n");
                stringBuffer.append("异常：");
                stringBuffer.append(subOrders.get(0).getVarianceCauseDesc());
            }
            viewHolder.tv_do_left.setText(stringBuffer.toString());
            /**
             * 如果是中草药：显示tv_content_dosage，隐藏ll_property
             * 如果是西药，显示ll_property，视情况，显示右边的属性
             * 如果是长期医嘱，需要显示频次
             * 如果是临时医嘱，不需要显示频次
             */
            if (subOrders.get(0).getOrderClass().equals("中草药")) {
                viewHolder.ll_property.setVisibility(View.VISIBLE);
                viewHolder.ll_repeat.setVisibility(View.GONE);
                viewHolder.ll_dosage.setVisibility(View.GONE);
                viewHolder.ll_piShi.setVisibility(View.GONE);
                viewHolder.ll_speed.setVisibility(View.GONE);
                viewHolder.ll_frequency.setVisibility(View.GONE);
                viewHolder.ll_administration.setVisibility(View.VISIBLE);
                viewHolder.tv_administration_txt.setText(subOrders.get(0).getAdministration());
//            viewHolder.tv_content_dosage.setVisibility(View.VISIBLE);
//            viewHolder.tv_content_dosage.setText(stringBufferDosage.toString());
            } else if (subOrders.get(0).getOrderClass().equals("西药")) {
                propertyJudgment(viewHolder, subOrders);
                viewHolder.ll_property.setVisibility(View.VISIBLE);
                if (StringTool.isNotEmpty(subOrders.get(0).getRepeatIndicator())) {
                    if (Integer.parseInt(subOrders.get(0).getRepeatIndicator()) == 0) {
                        viewHolder.ll_frequency.setVisibility(View.GONE);
                    }
                }
//            viewHolder.tv_content_dosage.setVisibility(View.VISIBLE);
//            viewHolder.tv_content_dosage.setText("");
                viewHolder.ll_administration.setVisibility(View.GONE);
            } else {
                propertyJudgment(viewHolder, subOrders);
                viewHolder.ll_dosage.setVisibility(View.GONE);
                if (StringTool.isNotEmpty(subOrders.get(0).getRepeatIndicator())) {
                    if (Integer.parseInt(subOrders.get(0).getRepeatIndicator()) == 0) {
                        viewHolder.ll_property.setVisibility(View.VISIBLE);
                        viewHolder.ll_frequency.setVisibility(View.GONE);
                    } else if (Integer.parseInt(subOrders.get(0).getRepeatIndicator()) == 1) {
                        viewHolder.ll_property.setVisibility(View.VISIBLE);
                    }
                }
//            viewHolder.tv_content_dosage.setVisibility(View.VISIBLE);
//            viewHolder.tv_content_dosage.setText("");
                viewHolder.ll_administration.setVisibility(View.GONE);
            }
        }
        /**
         * 左侧图标判断
         */
        loadOrdersInfo = doctorOrderBeanList.get(groupPosition).getOrders().get(childPosition);
        if (loadOrdersInfo.getPerformStatus() != null && loadOrdersInfo.getPerformStatus().equals("1") &&
                loadOrdersInfo.getTemplateId() == null) {
            viewHolder.cb_check.setVisibility(View.GONE);
            viewHolder.tv_content_my.setVisibility(View.VISIBLE);
            viewHolder.iv_check.setVisibility(View.VISIBLE);
            viewHolder.iv_check.setBackgroundResource(R.drawable.performing);
            viewHolder.tv_content_my.setText("执行中");
            viewHolder.bottom_relation.setVisibility(View.GONE);
        } else if (loadOrdersInfo.getPerformStatus() != null && loadOrdersInfo.getPerformStatus().equals("1") &&
                loadOrdersInfo.getTemplateId() != null) {
            viewHolder.cb_check.setVisibility(View.GONE);
            viewHolder.tv_content_my.setVisibility(View.VISIBLE);
            viewHolder.iv_check.setVisibility(View.VISIBLE);
            viewHolder.iv_check.setBackgroundResource(R.drawable.performing_template);
            viewHolder.tv_content_my.setText("执行中");
            viewHolder.bottom_relation.setVisibility(View.GONE);
        } else if (loadOrdersInfo.getPerformStatus() != null && loadOrdersInfo.getPerformStatus().equals("9")) {
            viewHolder.cb_check.setVisibility(View.GONE);
            viewHolder.tv_content_my.setVisibility(View.VISIBLE);
            viewHolder.iv_check.setBackgroundResource(R.drawable.checkmark);
            viewHolder.tv_content_my.setText("已执行");
            viewHolder.iv_check.setVisibility(View.VISIBLE);
            viewHolder.bottom_relation.setVisibility(View.GONE);
        } else if (loadOrdersInfo.getPerformStatus() != null && loadOrdersInfo.getPerformStatus().equals("0")) {
            viewHolder.iv_check.setVisibility(View.GONE);
            viewHolder.tv_content_my.setVisibility(View.GONE);
            viewHolder.cb_check.setVisibility(View.VISIBLE);
            viewHolder.bottom_relation.setVisibility(View.GONE);
        } else if (loadOrdersInfo.getPerformStatus() != null && loadOrdersInfo.getPerformStatus().equals("-1")) {
            viewHolder.cb_check.setVisibility(View.GONE);
            viewHolder.tv_content_my.setText("有异常");
            viewHolder.tv_content_my.setVisibility(View.VISIBLE);
            viewHolder.iv_check.setBackgroundResource(R.drawable.checkmark_negvative);
            viewHolder.iv_check.setVisibility(View.VISIBLE);
            viewHolder.bottom_relation.setVisibility(View.GONE);
        }

        /** 回调接口*/
        viewHolder.detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClick(v, groupPosition, childPosition);
            }
        });
        /** 回调接口*/
        viewHolder.endtime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClick(v, groupPosition, childPosition);
            }
        });
        /** 回调接口*/
        viewHolder.pingjia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClick(v, groupPosition, childPosition);
            }
        });
        /** 回调接口*/
        viewHolder.yichang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClick(v, groupPosition, childPosition);
            }
        });
        /** 回调接口*/
        viewHolder.top_right_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClick(v, groupPosition, childPosition);
            }
        });
        if (subOrders != null && !subOrders.isEmpty()) {
            /**
             * 下面评论，结束，详细的图标
             */
            if (subOrders.get(0).getNurseEffect() != null) {
                viewHolder.pingjia.setClickable(false);
                viewHolder.comment_tv_text.setText("已评价");
                viewHolder.comment_tv_image.setBackgroundResource(R.drawable.comment1_press);
            } else {
                viewHolder.comment_tv_text.setText("评价");
                viewHolder.comment_tv_image.setBackgroundResource(R.drawable.select_order_comment);
            }
            if (subOrders.get(0).getEventEndTime() != null) {
                viewHolder.endtime.setClickable(false);
                viewHolder.endtime_tv_text.setText("已结束");
                viewHolder.endtime_tv_image.setBackgroundResource(R.drawable.endtime_press);
            } else {
                viewHolder.endtime_tv_text.setText("结束");
                viewHolder.endtime_tv_image.setBackgroundResource(R.drawable.select_order_endtime);
            }
            if (subOrders.get(0).getVarianceCauseDesc() != null) {
                viewHolder.yichang.setClickable(false);
                viewHolder.execpteion_tv_text.setText("已记录");
                viewHolder.execpteion_tv_image.setBackgroundResource(R.drawable.exeception1_press);
            } else {
                viewHolder.execpteion_tv_text.setText("异常");
                viewHolder.execpteion_tv_image.setBackgroundResource(R.drawable.select_order_exeception);
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {

        private TextView tv_title_left;
        private TextView tv_title_right;
        private TextView cb_check;
        private TextView tv_do_left;
        private TextView tv_do_right;
        private TextView tv_content;
        private ImageView iv_check;
        private TextView tv_content_my;
        private Button pingjia, yichang, endtime, detail;
        private LinearLayout top_right_ll;
        private LinearLayout bottom_relation;
        private TextView comment_tv_image, comment_tv_text, execpteion_tv_image, execpteion_tv_text,
                endtime_tv_image, endtime_tv_text, detail_tv_image, detail_tv_text;
        private TextView tv_speed_txt, tv_dosage_txt, tv_frequency_txt, tv_pishi_txt;
        private TextView tv_repeat, tv_repeat_txt, tv_administration_txt;
        //        private TextView tv_content_dosage;
        private LinearLayout ll_repeat, ll_speed, ll_dosage, ll_frequency, ll_piShi, ll_property,
                ll_administration;
    }

    public interface CallBack {

        void onClick(View v, int groupPostion, int childPostion);
    }

    public List<LoadAppraislRecordBean> getLarList() {
        return larList;
    }

    public void setLarList(List<LoadAppraislRecordBean> larList) {
        this.larList = larList;
    }

    /**
     * 判断右边的属性是否显示
     *
     * @param viewHolder
     * @param subOrders
     */
    private void propertyJudgment(ViewHolder viewHolder, List<LoadOrdersInfo> subOrders) {
        LoadOrdersInfo info = subOrders.get(0);
        if (StringTool.isNotEmpty(info.getSpeed())) {
            viewHolder.ll_speed.setVisibility(View.VISIBLE);
            viewHolder.tv_speed_txt.setText(info.getSpeed());
        }
        if (StringTool.isEmpty(info.getSkinTestResult())) {
            viewHolder.ll_piShi.setVisibility(View.GONE);
        } else {
            if (info.getSkinTestResult().equals("0")) {
                viewHolder.ll_piShi.setVisibility(View.VISIBLE);
                viewHolder.tv_pishi_txt.setText(" 阴性");
            } else if (info.getSkinTestResult().equals("1")) {
                viewHolder.ll_piShi.setVisibility(View.VISIBLE);
                viewHolder.tv_pishi_txt.setText(" 阳性");
            }
        }
        if (StringTool.isNotEmpty(info.getDosage())) {
            viewHolder.ll_dosage.setVisibility(View.VISIBLE);
            viewHolder.tv_dosage_txt.setText(info.getDosage());
        }
        if (StringTool.isNotEmpty(info.getFrequency())) {
            viewHolder.ll_frequency.setVisibility(View.VISIBLE);
            viewHolder.tv_frequency_txt.setText(info.getFrequency());
        }
        if (StringTool.isNotEmpty(info.getRepeatIndicator())) {
            viewHolder.ll_repeat.setVisibility(View.VISIBLE);
            if (Integer.parseInt(info.getRepeatIndicator()) == 0) {
                viewHolder.tv_repeat_txt.setText("临时");
            } else if (Integer.parseInt(info.getRepeatIndicator()) == 1) {
                viewHolder.tv_repeat_txt.setText("长期");
            } else {
                viewHolder.ll_repeat.setVisibility(View.GONE);
            }
        }
    }

}
