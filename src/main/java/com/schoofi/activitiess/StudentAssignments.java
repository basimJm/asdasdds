package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.StudentAssignmentViewPagerAdapter;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

import astuetz.PagerSlidingTabStrip;

public class StudentAssignments extends AppCompatActivity {

	ImageView back;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_assignments);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student Assignments");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new StudentAssignmentViewPagerAdapter(getSupportFragmentManager()));
		back = (ImageView) findViewById(R.id.img_back);


		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				/*final ProgressDialog loading = ProgressDialog.show(StudentAssignments.this, "Loading new Data Please wait!!!", "",false,false);

				final Thread thread = new Thread(){
					@Override
					public void run() {
						try {


							Thread.sleep(7000); // As I am using LENGTH_LONG in Toast
							loading.dismiss();

							StudentAssignments.this.finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				//finish();

				thread.start();*/

				finish();

			}
		});
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

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}*/

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		/*final ProgressDialog loading = ProgressDialog.show(StudentAssignments.this, "Loading new Data Please wait!!!", "",false,false);
		final Thread thread = new Thread(){
			@Override
			public void run() {
				try {

					Thread.sleep(7000); // As I am using LENGTH_LONG in Toast
					loading.dismiss();
					StudentAssignments.this.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		thread.start();*/

		finish();
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_assignments, menu);
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
