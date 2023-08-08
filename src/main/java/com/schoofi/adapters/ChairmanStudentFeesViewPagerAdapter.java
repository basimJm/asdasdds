package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//import com.schoofi.fragments.ChairmanStudentCustomAttendance;
//import com.schoofi.fragments.ChairmanStudentDailyAttendance;
import com.schoofi.fragments.ChairmanStudentFragmentAgeingFees;
import com.schoofi.fragments.ChairmanStudentFragmentFees;
//import com.schoofi.fragments.ChairmanStudentWeeklyAttendance;

/**
 * Created by Schoofi on 22-06-2016.
 */
public class ChairmanStudentFeesViewPagerAdapter extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 2;
        // Tab Titles
        private String tabtitles[] = new String[] { "Fees", "Ageing Analysis" };
        Context context;

        public ChairmanStudentFeesViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            switch (position) {
                case 0: return ChairmanStudentFragmentFees.newInstance(1);
                case 1: return ChairmanStudentFragmentAgeingFees.newInstance(2);
                //case 2: return ChairmanStudentCustomAttendance.newInstance(3);


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
