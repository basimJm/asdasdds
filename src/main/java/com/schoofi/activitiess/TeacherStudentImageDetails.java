package com.schoofi.activitiess;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TouchImageView;
import com.schoofi.utils.TrackerName;

public class TeacherStudentImageDetails extends AppCompatActivity {
	
	private ImageView back,rotate1;
	private TouchImageView screenImage;
	private Button rotate;
	private TextView screenTitle;
	String imageUrl;
	String u = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		float yInches= metrics.heightPixels/metrics.ydpi;
		float xInches= metrics.widthPixels/metrics.xdpi;
		double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
		if (diagonalInches>=6.0){
			// 6.5inch device or bigger
			u = "1";
			setContentView(R.layout.activity_teacher_student_image_details);
		}else{
			// smaller device
			u = "2";
			setContentView(R.layout.image_details);

		}

		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Student ImageDetails");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		screenImage = (TouchImageView) findViewById(R.id.teacherLeaveImage);
		rotate = (Button) findViewById(R.id.btn_rotate);
		rotate1 = (ImageView) findViewById(R.id.img_rotation);
		screenTitle = (TextView) findViewById(R.id.txt_teacherLeaveDetails0);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		imageUrl = getIntent().getStringExtra("imageUrl");
		System.out.print(AppConstants.SERVER_URLS.IMAGE_URL+imageUrl);
		//Picasso.with(TeacherStudentImageDetails.this).load(AppConstants.SERVER_URLS.SERVER_URL+imageUrl).placeholder(R.drawable.ic_launcher).error(R.drawable.schoofisplash).into(screenImage);
		Glide.with(TeacherStudentImageDetails.this).load(AppConstants.SERVER_URLS.IMAGE_URL+imageUrl).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(screenImage);
		rotate.setVisibility(View.INVISIBLE);
		rotate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				screenImage.setRotation(screenImage.getRotation() + 90);
				
			}
		});
		
		rotate1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				screenImage.setRotation(screenImage.getRotation() + 90);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_student_image_details, menu);
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
