package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.schoofi.fragments.ChairmanAssignmentClassSectionWiseAnalysis;
import com.schoofi.fragments.ChairmanAssignmentSubjectWiseAnalysis;
import com.schoofi.fragments.ChairmanAssignmentTeacherWiseAnalysis;
import com.schoofi.fragments.CustomChairmanAssignmentAnalysis;
import com.schoofi.fragments.DailyChairmanAssignmentAttendanceAnalysis;
import com.schoofi.fragments.WeeklyChairmanAssignmentAnalysis;

/**
 * Created by Schoofi on 11-08-2017.
 */

public class ChairmanAssignmentAnalysisDetailScreen1Adapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String value;
    private String tabtitles[] = new String[] { "Daily", "Weekly", "Custom" };
    Context context;

    public ChairmanAssignmentAnalysisDetailScreen1Adapter(FragmentManager fm,String value) {
        super(fm);
        this.value = value;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return DailyChairmanAssignmentAttendanceAnalysis.newInstance(1,value);
            case 1: return WeeklyChairmanAssignmentAnalysis.newInstance(2,value);
            case 2: return CustomChairmanAssignmentAnalysis.newInstance(3,value);


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
