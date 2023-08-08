package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.schoofi.fragments.ChairmanAssignmentClassSectionWiseAnalysis;
import com.schoofi.fragments.ChairmanAssignmentSubjectWiseAnalysis;
import com.schoofi.fragments.ChairmanAssignmentTeacherWiseAnalysis;
import com.schoofi.fragments.StudentCustomAttendance;
import com.schoofi.fragments.StudentDailyAttendance;
import com.schoofi.fragments.StudentWeeklyAttendance;

/**
 * Created by Schoofi on 10-08-2017.
 */

public class ChairmanAssignmentAnalysisDetailAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "Class Wise", "Teacher Wise", "Subject Wise" };
    Context context;


    public ChairmanAssignmentAnalysisDetailAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return ChairmanAssignmentClassSectionWiseAnalysis.newInstance(1);
            case 1: return ChairmanAssignmentTeacherWiseAnalysis.newInstance(2);
            case 2: return ChairmanAssignmentSubjectWiseAnalysis.newInstance(3);


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
