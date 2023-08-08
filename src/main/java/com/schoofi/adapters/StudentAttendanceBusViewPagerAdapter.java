package com.schoofi.adapters;

import com.schoofi.fragments.ParentStudentBusAttaendanceDaily;
import com.schoofi.fragments.ParentStudentBusAttendanceWeeklyAttendance;
import com.schoofi.fragments.ParentStudentCustomBusAttendance;
import com.schoofi.fragments.StudentCustomAttendance;
import com.schoofi.fragments.StudentWeeklyAttendance;
import com.schoofi.fragments.StudentDailyAttendance;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StudentAttendanceBusViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "Daily", "Weekly", "Custom" };
    Context context;

    public StudentAttendanceBusViewPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0: return ParentStudentBusAttaendanceDaily.newInstance(1);
            case 1: return ParentStudentBusAttendanceWeeklyAttendance.newInstance(2);
            case 2: return ParentStudentCustomBusAttendance.newInstance(3);


        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

}
