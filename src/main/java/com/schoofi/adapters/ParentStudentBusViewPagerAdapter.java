package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.schoofi.fragments.StudentCustomAttendance;
import com.schoofi.fragments.StudentDailyAttendance;
import com.schoofi.fragments.StudentWeeklyAttendance;

/**
 * Created by Schoofi on 06-03-2017.
 */

public class ParentStudentBusViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "Daily", "Weekly", "Custom" };
    Context context;

    public ParentStudentBusViewPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0: return StudentDailyAttendance.newInstance(1);
            case 1: return StudentWeeklyAttendance.newInstance(2);
            case 2: return StudentCustomAttendance.newInstance(3);


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
