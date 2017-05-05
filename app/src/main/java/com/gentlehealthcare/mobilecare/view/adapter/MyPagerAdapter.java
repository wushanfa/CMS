package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ViewPager适配器
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentManager fm;

	/**
	 * 当数据发生改变时的回调接口
	 */
	private OnReloadListener mListener;

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments2) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments2;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int i) {
		return fragments.get(i);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	/**
	 * 重新设置页面内容
	 */
	public void setPagerItems(List<Fragment> items) {
		if (items != null) {
			for (int i = 0; i < fragments.size(); i++) {
				fm.beginTransaction().remove(items.get(i)).commit();
			}
			fragments = items;
		}
	}

	/**
	 * 当页面数据发生改变时你可以调用此方法
	 * 
	 * 重新载入数据，具体载入信息由回调函数实现
	 */
	public void reLoad() {
		if (mListener != null) {
			mListener.OnReload();
		}
		this.notifyDataSetChanged();
	}

	public void setOnReloadListener(OnReloadListener listener) {
		this.mListener = listener;
	}

	public interface OnReloadListener {
		public void OnReload();
	}

}
