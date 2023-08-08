package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.schoofi.fragments.TeacherAllFeedBack;
import com.schoofi.fragments.TeacherAssignedFeedBack;

public class TeacherFeedBackPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] { "Assigned", "All"};
	Context context;

	public TeacherFeedBackPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		
		case 0: return TeacherAllFeedBack.newInstance(1);
		case 1: return TeacherAssignedFeedBack.newInstance(2);
		

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
