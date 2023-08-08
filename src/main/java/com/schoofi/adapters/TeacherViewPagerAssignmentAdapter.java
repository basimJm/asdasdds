package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.schoofi.fragments.TeacherAssignment;
import com.schoofi.fragments.TeacherCircularView;
import com.schoofi.fragments.TeacherHomework;

public class TeacherViewPagerAssignmentAdapter extends FragmentPagerAdapter{

	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] { "Homework/Assignment", "Circular"};
	Context context;

	public TeacherViewPagerAssignmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position)
		{
		case 0: return TeacherHomework.newInstance(1);
		case 1: return TeacherCircularView.newInstance(2);
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
