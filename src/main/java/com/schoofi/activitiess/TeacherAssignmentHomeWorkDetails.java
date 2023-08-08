package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.TeacherStudentHomeWorkViewPagerAdapter;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

import java.util.ArrayList;

import astuetz.PagerSlidingTabStrip;

public class TeacherAssignmentHomeWorkDetails extends AppCompatActivity {
	
	ArrayList<String> myList;
	private Button viewAssignment;
	ImageView back;
	String description,title,submitBy,subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Assignment HomeWork Details");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_teacher_assignment_home_work_details);
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewAssignment = (Button) findViewById(R.id.btn_viewAssignment);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		description = getIntent().getStringExtra("desc");
		title = getIntent().getStringExtra("title");
		submitBy = getIntent().getStringExtra("last_date");
		subject = getIntent().getStringExtra("subject");
		if(getIntent().getStringExtra("array2").matches("1"))
		{
			viewAssignment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(TeacherAssignmentHomeWorkDetails.this,TeacherStudentViewAssignment.class);
					intent.putExtra("array", "");
					intent.putExtra("array2","1");
					intent.putExtra("desc",description);
					intent.putExtra("title", title);
					intent.putExtra("last_date", submitBy);
					intent.putExtra("subject",subject);


					startActivity(intent);

				}
			});

		}

		else {
			myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
			viewAssignment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(TeacherAssignmentHomeWorkDetails.this,TeacherStudentViewAssignment.class);
					intent.putExtra("array", myList);
					intent.putExtra("array2","2");
					intent.putExtra("desc",description);
					intent.putExtra("title", title);
					intent.putExtra("last_date", submitBy);
					intent.putExtra("subject",subject);
					startActivity(intent);

				}
			});
		}

		
		
		
		viewPager.setAdapter(new TeacherStudentHomeWorkViewPagerAdapter(getSupportFragmentManager()));
		PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
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
		

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

		
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_assignment_home_work_details,
				menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
