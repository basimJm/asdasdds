package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mzelzoghbi.zgallery.adapters.ViewPagerAdapter;
import com.schoofi.fragments.AdminVisitorFirstScreenCustom;
import com.schoofi.fragments.AdminVisitorFirstScreenDaily;
import com.schoofi.fragments.AdminVisitorFirstScreenWeekly;
import com.schoofi.fragments.AdminVisitorLogsCustomPrimaryDetails;
import com.schoofi.fragments.AdminVisitorLogsDailyPrimaryDetails;
import com.schoofi.fragments.AdminVisitorLogsWeeklyDailyPrimaryDetails;

/**
 * Created by Schoofi on 23-04-2018.
 */

public class AdminVisitorLogsDetailsFirstActivityViewPagerAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "Daily", "Weekly", "Custom" };
    Context context;


    public AdminVisitorLogsDetailsFirstActivityViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return AdminVisitorFirstScreenDaily.newInstance(1);
            case 1: return AdminVisitorFirstScreenWeekly.newInstance(2);
            case 2: return AdminVisitorFirstScreenCustom.newInstance(3);


        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
