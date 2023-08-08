package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.schoofi.fragments.AdminVisitorLogsCustomPrimaryDetails;
import com.schoofi.fragments.AdminVisitorLogsDailyPrimaryDetails;
import com.schoofi.fragments.AdminVisitorLogsWeeklyDailyPrimaryDetails;
import com.schoofi.fragments.ChairmanAssignmentClassSectionWiseAnalysis;
import com.schoofi.fragments.ChairmanAssignmentSubjectWiseAnalysis;
import com.schoofi.fragments.ChairmanAssignmentTeacherWiseAnalysis;

/**
 * Created by Schoofi on 09-03-2018.
 */

public class AdminVisitorLogsPrimaryDetailsViewPagerAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "Daily", "Weekly", "Custom" };
    Context context;


    public AdminVisitorLogsPrimaryDetailsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return AdminVisitorLogsDailyPrimaryDetails.newInstance(1);
            case 1: return AdminVisitorLogsWeeklyDailyPrimaryDetails.newInstance(2);
            case 2: return AdminVisitorLogsCustomPrimaryDetails.newInstance(3);


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
