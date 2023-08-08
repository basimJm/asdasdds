package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.schoofi.activitiess.StudentCircularView;
import com.schoofi.fragments.StudentAssignment;
import com.schoofi.fragments.StudentHomework;

public class StudentAssignmentViewPagerAdapter  extends FragmentPagerAdapter {
	
	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] { "HomeWork/Assignment", "Circular"};
	Context context;

	public StudentAssignmentViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0: return StudentAssignment.newInstance(1);
		case 1: return StudentCircularView.newInstance(2);
		

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
