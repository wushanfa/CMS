package com.gentlehealthcare.mobilecare.view.adapter;

import java.util.List;

import android.support.v4.app.*;

public class MyFragmentAdapter extends FragmentPagerAdapter  {

    private List<Fragment> fragments;
    private FragmentManager fragmentManager=null;


    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {

        super(fm);
        this.fragmentManager=fm;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }




}