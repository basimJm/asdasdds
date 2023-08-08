package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.schoofi.fragments.ParentStudentFeesHistory;
import com.schoofi.fragments.ParentStudentFeesUnpaid;
import com.schoofi.fragments.ParentStudentFeesUnpaidNewMultilevel;

/**
 * Created by harsh malhotra on 4/11/2016.
 */
public class ParentFeesViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    // Tab Titles
    private String tabtitles[] = new String[] { "Unpaid", "History"};
    Context context;

    public ParentFeesViewPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0: return ParentStudentFeesUnpaidNewMultilevel.newInstance(1);
            case 1: return ParentStudentFeesHistory.newInstance(2);


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
