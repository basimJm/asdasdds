package com.schoofi.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schoofi.activitiess.R;
import com.schoofi.adapters.HomeWorkInnerAdapter;

import astuetz.PagerSlidingTabStrip;

public class StudentHomework extends Fragment{
	
	public static final String ARG_PAGE = "ARG_PAGE";

	private int mPage;

	public static StudentHomework newInstance(int page) {
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, page);
		StudentHomework studentHomework = new StudentHomework();
		studentHomework.setArguments(args);
		return studentHomework;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPage = getArguments().getInt(ARG_PAGE);
	}

	// Inflate the fragment layout we defined above for this fragment
	// Set the associated text for the title
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.homework_layout, container, false);
		
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		viewPager.setAdapter(new HomeWorkInnerAdapter(getChildFragmentManager()));
		PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabsHomework);
		// Attach the view pager to the tab strip
		tabsStrip.setViewPager(viewPager);
		tabsStrip.setOnPageChangeListener(new OnPageChangeListener() {

			// This method will be invoked when a new page becomes selected.
			@Override
			public void onPageSelected(int position) {
				
			}

			// This method will be invoked when the current page is scrolled
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// Code goes here
			}

			// Called when the scroll state changes: 
			// SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
			@Override
			public void onPageScrollStateChanged(int state) {
				// Code goes here
			}
		});
		return view;
	}

	
		
		
		
		
}
