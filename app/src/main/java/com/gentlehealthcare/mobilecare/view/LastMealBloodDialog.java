package com.gentlehealthcare.mobilecare.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.patient.blood.BloodFra;

import java.util.Calendar;
import java.util.Date;

/**
 * 最后一次用餐时间
 * Created by ouyang on 2015/5/28.
 */
public class LastMealBloodDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private TextView tv_lasemeal_hour,tv_lasemeal_minute;
    private Fragment fragment;
    public LastMealBloodDialog(Context context, int theme,Fragment fragment) {
        super(context, theme);
        this.context=context;
        this.fragment=fragment;
        init();
    }

    private void init(){
        setCancelable(false);
        View rootView= LayoutInflater.from(context).inflate(R.layout.activity_blood_lasemeal,null);
        setContentView(rootView);
        tv_lasemeal_hour= (TextView) rootView.findViewById(R.id.tv_lasemeal_hour);
        tv_lasemeal_minute= (TextView) rootView.findViewById(R.id.tv_lasemeal_minute);
        rootView.findViewById(R.id.btn_addHour).setOnClickListener(this);
        rootView.findViewById(R.id.btn_reduceHour).setOnClickListener(this);
        rootView.findViewById(R.id.btn_addMinute).setOnClickListener(this);
        rootView.findViewById(R.id.btn_reduceMinute).setOnClickListener(this);
        rootView.findViewById(R.id.btn_finish).setOnClickListener(this);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        int result=calendar.get(Calendar.HOUR_OF_DAY);
        tv_lasemeal_hour.setText(result<10?("0"+result):(result+""));
        result=calendar.get(Calendar.MINUTE);
        result=result/10;
        tv_lasemeal_minute.setText(result+"0");
    }

    @Override
    public void show() {
        if (!isShowing())
        super.show();

    }

    @Override
    public void onClick(View v) {
      int result=0;
      switch (v.getId()){
          case R.id.btn_reduceHour:
              result= Integer.valueOf(tv_lasemeal_hour.getText().toString())+1;
              if (result>=24)
                  result=0;
              tv_lasemeal_hour.setText(result<10?("0"+result):(result+""));
              break;
          case R.id.btn_addHour:
              result= Integer.valueOf(tv_lasemeal_hour.getText().toString())-1;
              if (result<0)
                  result=23;
              tv_lasemeal_hour.setText(result<10?("0"+result):(result+""));
              break;
          case R.id.btn_addMinute:
              result= Integer.valueOf(tv_lasemeal_minute.getText().toString())+10;
              if (result>=60)
                  result=0;
              tv_lasemeal_minute.setText(result<10?("0"+result):(result+""));
              break;
          case R.id.btn_reduceMinute:
              result= Integer.valueOf(tv_lasemeal_minute.getText().toString())-10;
              if (result<0)
                  result=50;
              tv_lasemeal_minute.setText(result<10?("0"+result):(result+""));
              break;
          case R.id.btn_finish:
            if (fragment!=null){
                if (fragment instanceof BloodFra){
                    int hour= Integer.valueOf(tv_lasemeal_hour.getText().toString());
                    int minute= Integer.valueOf(tv_lasemeal_minute.getText().toString());
                    ((BloodFra)fragment).GetLastMeal(hour,minute);
                }
            }
              dismiss();
              break;

      }
    }
}

