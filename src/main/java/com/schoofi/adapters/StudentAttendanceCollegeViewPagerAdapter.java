package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.schoofi.fragments.StudentCollegeCustsomAttendance;
import com.schoofi.fragments.StudentCollegeDailyAttendance;
import com.schoofi.fragments.StudentCollegeWeeklyAttendance;
import com.schoofi.fragments.StudentCustomAttendance;
import com.schoofi.fragments.StudentDailyAttendance;
import com.schoofi.fragments.StudentWeeklyAttendance;

/**
 * Created by Schoofi on 10-05-2019.
 */

public class StudentAttendanceCollegeViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "Daily", "Weekly", "Custom" };
    Context context;

    public StudentAttendanceCollegeViewPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0: return StudentCollegeDailyAttendance.newInstance(1);
            case 1: return StudentCollegeWeeklyAttendance.newInstance(2);
            case 2: return StudentCollegeCustsomAttendance.newInstance(3);


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
