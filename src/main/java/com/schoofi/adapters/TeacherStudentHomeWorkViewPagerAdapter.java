package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.schoofi.fragments.StudentNotSubmittedHomeWorkList;
import com.schoofi.fragments.StudentSubmittedHomeWorkList;

public class TeacherStudentHomeWorkViewPagerAdapter extends FragmentPagerAdapter{
	
	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] {"Submitted","Not-Submitted"};
	Context context;

	public TeacherStudentHomeWorkViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0: return StudentSubmittedHomeWorkList.newInstance(1);
		case 1: return StudentNotSubmittedHomeWorkList.newInstance(2);
		
		

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
