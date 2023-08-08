package com.schoofi.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.schoofi.fragments.AssignMentAll;
import com.schoofi.fragments.AssignmentNotSubmitted;
import com.schoofi.fragments.AssignmentSubmitted;

public class AssignmentInnerAdapter extends FragmentPagerAdapter{
	
	final int PAGE_COUNT = 3;
	// Tab Titles
	private String tabtitles[] = new String[] { "All", "Submitted","Not-Submitted"};
	Context context;

	public AssignmentInnerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0: return AssignMentAll.newInstance(1);
		case 1: return AssignmentSubmitted.newInstance(2);
		case 2:  return AssignmentNotSubmitted.newInstance(3);
		

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
