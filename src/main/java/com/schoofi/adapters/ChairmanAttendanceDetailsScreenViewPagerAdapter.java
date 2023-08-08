package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.schoofi.fragments.ChairmanStudentAttendanceDetailsNotRecordedAttendance;
import com.schoofi.fragments.ChairmanStudentAttendanceDetailsRecordedAttendance;
import com.schoofi.fragments.StudentAssignment;
import com.schoofi.fragments.StudentHomework;

/**
 * Created by Schoofi on 04-08-2016.
 */
public class ChairmanAttendanceDetailsScreenViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    // Tab Titles
    private String tabtitles[] = new String[] { "Recorded", "Not-Recorded"};
    Context context;

    public ChairmanAttendanceDetailsScreenViewPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0: return ChairmanStudentAttendanceDetailsRecordedAttendance.newInstance(1);
            case 1: return ChairmanStudentAttendanceDetailsNotRecordedAttendance.newInstance(2);


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
