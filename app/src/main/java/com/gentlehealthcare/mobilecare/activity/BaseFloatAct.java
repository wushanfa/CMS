package com.gentlehealthcare.mobilecare.activity;

/**
 * 手势区域父类Activity
 */
public  class BaseFloatAct extends BaseActivity {
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//只用于显示手势区域
		ShowGestWindow();
	}

	@Override
	protected void resetLayout() {

	}

}
