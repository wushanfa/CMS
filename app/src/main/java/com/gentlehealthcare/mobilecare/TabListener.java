package com.gentlehealthcare.mobilecare;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * ViewPager RadioGroup切换
 */
public class TabListener implements OnCheckedChangeListener, OnPageChangeListener {

	private RadioGroup radioGroup;

	private ViewPager viewPager;

	public TabListener(RadioGroup radioGroup, ViewPager viewPager) {

		super();

		this.radioGroup = radioGroup;

		this.viewPager = viewPager;

		radioGroup.setOnCheckedChangeListener(this);

		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		if (viewPager != null) {

			viewPager.setCurrentItem(checkedId);
		}
	}

	@Override
	public void onPageSelected(int arg0) {

		if (radioGroup != null) {

			radioGroup.getChildAt(arg0).performClick();
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

}
