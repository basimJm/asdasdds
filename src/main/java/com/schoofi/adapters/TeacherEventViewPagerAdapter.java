package com.schoofi.adapters;

import com.schoofi.fragments.StudentCustomAttendance;
import com.schoofi.fragments.StudentPassedEvent;
import com.schoofi.fragments.StudentUpcomingEvent;
import com.schoofi.fragments.StudentWeeklyAttendance;
import com.schoofi.fragments.StudentDailyAttendance;
import com.schoofi.fragments.TeacherAllEvents;
import com.schoofi.fragments.TeacherPassedEvent;
import com.schoofi.fragments.TeacherUpcomingEvents;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TeacherEventViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "All","Up Coming" ,"Past"};
    Context context;

    public TeacherEventViewPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0: return TeacherAllEvents.newInstance(1);
            case 1: return TeacherUpcomingEvents.newInstance(2);
            case 2: return TeacherPassedEvent.newInstance(3);



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
