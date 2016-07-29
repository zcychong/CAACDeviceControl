package com.caac.android.caacdevicecontrol.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YHT on 2016/7/27.
 */
public class MyFragmentPgerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    public MyFragmentPgerAdapter(FragmentManager fm,  List<Fragment> fragments) {
        super(fm);
        for(Fragment item : fragments){
            fragmentList.add(item);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
