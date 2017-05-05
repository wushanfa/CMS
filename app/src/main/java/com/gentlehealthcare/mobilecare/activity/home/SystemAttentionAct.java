package com.gentlehealthcare.mobilecare.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.ABDoToolBar;
import com.gentlehealthcare.mobilecare.activity.ABToolBarActivity;
import com.gentlehealthcare.mobilecare.activity.patient.DeptPatientAct;
import com.gentlehealthcare.mobilecare.activity.work.WorkAct;
import com.gentlehealthcare.mobilecare.net.bean.SystemAttentionInfo;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;

import java.util.ArrayList;
import java.util.List;

/**
 * 注意事项界面
 * Created by ouyang on 15/5/18.
 */
public class SystemAttentionAct extends ABToolBarActivity {
    private ListView lv_attention;
    private List<String> attentionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemattention);
        lv_attention = (ListView) findViewById(R.id.lv_attenttion);
        attentionList = new ArrayList<String>();
        List<SystemAttentionInfo> systemAttentionInfos = UserInfo.getAttentions();
        if (systemAttentionInfos != null && systemAttentionInfos.size() > 0) {
            for (SystemAttentionInfo info : systemAttentionInfos) {
                attentionList.add(info.getNoticeContent());
            }
        }
        lv_attention.setAdapter(new ArrayAdapter<String>(SystemAttentionAct.this, android.R.layout.simple_list_item_1, attentionList));
//        lv_attention.setAdapter(new ArrayAdapter<String>(SystemAttentionAct.this,R.layout.activity_commond_text,R.id.tv_command_text,attentionList));
        ((Button) findViewById(R.id.btn_back_1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        setToolBarDrawable(new int[]{R.drawable.act_home_deptpatbtn, R.drawable.act_home_mypatbtn, R.drawable.act_home_homebtn, R.drawable.act_home_workbtn, R.drawable.act_home_attentionbtn});
        setRightBtnDrawable(R.drawable.act_home_chaxunbtn);
        setAbDoToolBar(new ABDoToolBar() {
            @Override
            public void onCheckedChanged(int i) {
                switch (i) {
                    case 0:
                        startActivity(new Intent(SystemAttentionAct.this, DeptPatientAct.class));
                        break;
                    case 1:
                        Intent intent = new Intent(SystemAttentionAct.this, HomeAct.class);
                        intent.putExtra("toMyPatient", true);
                        startActivity(intent);
                        break;
                    case 2:
                        startActivity(new Intent(SystemAttentionAct.this, HomeAct.class));
                        break;
                    case 3:
                        startActivity(new Intent(SystemAttentionAct.this, WorkAct.class));
                        break;
                    case 4:
                        break;
                }
            }

            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValid(false);
        setCheck(4);
    }

    @Override
    protected void resetLayout() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_systemattention);
        SupportDisplay.resetAllChildViewParam(root);
    }
}
