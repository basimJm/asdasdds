package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//import com.schoofi.fragments.ChairmanStudentCustomAttendance;
import com.schoofi.fragments.ChairmanStudentCustomListAttendance;
//import com.schoofi.fragments.ChairmanStudentDailyAttendance;
import com.schoofi.fragments.ChairmanStudentDailyListAttendance;
//import com.schoofi.fragments.ChairmanStudentWeeklyAttendance;
import com.schoofi.fragments.ChairmanStudentWeeklyListAttendance;

public class ChairmanStudentAttendanceAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 3;
	// Tab Titles
	private String tabtitles[] = new String[] { "Daily", "Weekly", "Custom" };
	Context context;

	public ChairmanStudentAttendanceAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0: return ChairmanStudentDailyListAttendance.newInstance(1);
		case 1: return ChairmanStudentWeeklyListAttendance.newInstance(2);
		case 2: return ChairmanStudentCustomListAttendance.newInstance(3);


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
