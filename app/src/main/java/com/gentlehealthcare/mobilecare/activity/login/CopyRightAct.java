package com.gentlehealthcare.mobilecare.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.tool.SystemInfoSave;

import java.util.Map;

/**
 * 版权界面 TODO
 * 
 */
public class CopyRightAct extends BaseActivity {
	private Button btnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_copyright);
		initWidget();
		TextView tvCopyRight = (TextView) findViewById(R.id.tv_copyright);
		CheckBox cbAgreement = (CheckBox) findViewById(R.id.cbox_agreement);
		btnStart = (Button) findViewById(R.id.btn_start);
		tvCopyRight.setMovementMethod(ScrollingMovementMethod.getInstance());
		cbAgreement.setOnCheckedChangeListener(checkedChangeListener);
		btnStart.setOnClickListener(clickListener);
        Map<String,String> map=SystemInfoSave.getInstance(this).get();
		tvCopyRight.setText( map.get("appCopyright"));


	}

	@Override
	protected void resetLayout() {
		LinearLayout root = (LinearLayout) findViewById(R.id.root_copyright);
		SupportDisplay.resetAllChildViewParam(root);
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// 保存第一次登录信息
            SharedPreferences preferences = getSharedPreferences("NURSE_MOBILE_IS_FIRST", MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("isFirstIn", true);
            edit.commit();

            // 跳转到登录界面,finish掉当前界面
            Intent intent = new Intent(CopyRightAct.this,LoginAct.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}
	};

	OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			if (isChecked) {

				btnStart.setEnabled(true);
			} else {

				btnStart.setEnabled(false);
			}
		}
	};

    /**
     * 初始化控件
     */
	private void initWidget(){
		Map<String,String> map=SystemInfoSave.getInstance(CopyRightAct.this).get();
		if(map==null||map.keySet().size()<=0)
			return ;
		((TextView)findViewById(R.id.tv_copyright)).setText(map.get("appCopyright")==null?"本移动护理系统由和仁（天津）科技有限公司开发":map.get("appCopyright"));
	}

}
