package com.gentlehealthcare.mobilecare.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.bloodbagnuclearcharge.BloodBagNuclearChargeAct;
import com.gentlehealthcare.mobilecare.net.bean.BloodProductBean2;
import com.gentlehealthcare.mobilecare.swipe.SwipeLayout;
import com.gentlehealthcare.mobilecare.swipe.adapters.BaseSwipeAdapter;
import com.gentlehealthcare.mobilecare.tool.StringTool;

import java.util.List;

/**
 * tpr history list adapter
 * 
 * @author zyy
 * @date 2016/02/29
 */
public class BloodBagNuclearChangeAdapter extends BaseSwipeAdapter {

  private BloodBagNuclearChargeAct context;

  private List<BloodProductBean2> bloodProductBean2s;

  public BloodBagNuclearChangeAdapter(BloodBagNuclearChargeAct context,
      List<BloodProductBean2> bloodProductBean2s) {
    super();
    this.context = context;
    this.bloodProductBean2s = bloodProductBean2s;
  }

  public void notifyDataSetChanged(List<BloodProductBean2> list) {
    this.bloodProductBean2s = list;
    super.notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return bloodProductBean2s.size();
  }

  @Override
  public Object getItem(int position) {

    return bloodProductBean2s.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getSwipeLayoutResourceId(int position) {
    return R.id.swipe;
  }

  @Override
  public View generateView(final int position, ViewGroup parent) {
    View convertView = LayoutInflater.from(context).inflate(
        R.layout.item_orders_record, parent, false);
    final SwipeLayout swipeLayout = (SwipeLayout) convertView
        .findViewById(getSwipeLayoutResourceId(position));

    convertView.findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        context.bloodBack(UserInfo.getUserName(), bloodProductBean2s.get(position).getLogId(),
            UserInfo.getWardCode());
          swipeLayout.close();
      }
    });
    return convertView;
  }

  @Override
  public void fillValues(final int position, final View convertView) {

    TextView tv_order_title = (TextView) convertView
        .findViewById(R.id.tv_order_title);
    TextView tv_delete = (TextView) convertView
        .findViewById(R.id.delete);
    TextView tv_order_context = (TextView) convertView
        .findViewById(R.id.tv_order_context);
    TextView tv_dosage = (TextView) convertView
        .findViewById(R.id.tv_dosage);
    TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
    TextView btn_record = (TextView) convertView.findViewById(R.id.btn_record);
    btn_record.setVisibility(View.INVISIBLE);
    TextView btn_check = (TextView) convertView
        .findViewById(R.id.btn_check);
    tv_delete.setText("退回");
    String patInfo = bloodProductBean2s.get(position).getPatName() + " "
        + bloodProductBean2s.get(position).getPatCode();
    String date = StringTool.dateToTime(bloodProductBean2s.get(position).getBloodOutDate());
    tv_order_title.setText(date + " " + patInfo);
//    tv_date.setText(
//        DateTool.todayTomorryYesterday(bloodProductBean2s.get(position).getTransStartDate()));//核收前 trans_start_date为空
    tv_date.setText(" ");
    String txt;
    if (StringTool.isEmpty(bloodProductBean2s.get(position).getBloodTypeName())) {
      txt = " ";
    } else {
      txt = bloodProductBean2s.get(position).getBloodTypeName() + "\n";
    }
    String rh;
    if (StringTool.isEmpty(bloodProductBean2s.get(position).getBloodGroupDesc())) {
      rh = " ";
    } else {
      rh = bloodProductBean2s.get(position).getBloodGroupDesc();
    }
    tv_order_context.setText(txt + "(" + bloodProductBean2s.get(position).getBloodDonorCode() + ","
        + bloodProductBean2s.get(position).getBloodProductCode() + "," + rh + ")");

    String str;
    if (StringTool.isEmpty(bloodProductBean2s.get(position).getBloodUnit())) {
      str = " ";
    } else {
      str = bloodProductBean2s.get(position).getBloodUnit();
    }
    tv_dosage.setText(str);
    if (TextUtils.equals(bloodProductBean2s.get(position).getBloodStatus(), "U")) {
      btn_check.setBackgroundResource(R.drawable.btn_check_off_holo_light);
    } else if (TextUtils.equals(bloodProductBean2s.get(position).getBloodStatus(),
        "0")) {
      btn_check.setBackgroundResource(R.drawable.btn_finish);
    }else if(TextUtils.equals(bloodProductBean2s.get(position).getBloodStatus(),
            "-1")){
        btn_check.setBackgroundResource(R.drawable.btn_check_cancel);
    }

  }

}
