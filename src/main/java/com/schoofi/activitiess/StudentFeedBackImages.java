package com.schoofi.activitiess;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.StudentFeedBackGridAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class StudentFeedBackImages extends AppCompatActivity {
	ArrayList<String> myList;
	GridView studentFeedBackImagesGrid;
	StudentFeedBackGridAdapter studentFeedBackGridAdapter;
	ImageView back,share;
	ArrayList<String> myList1;
	String value,param;
	int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_grid_image_details);
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student FeedbackImages");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		studentFeedBackImagesGrid = (GridView) findViewById(R.id.studentHomeGridView);
		
		myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
		value = getIntent().getStringExtra("value");
		share = (ImageView) findViewById(R.id.img_upload);
		share.setVisibility(View.GONE);

		myList1 = new ArrayList<String>();
		position = getIntent().getExtras().getInt("position");
		param = getIntent().getStringExtra("param");

		//Utils.showToast(getApplicationContext(),String.valueOf(position)+param+value);

		for(int i=0;i<myList.size();i++)
		{
			myList1.add(AppConstants.SERVER_URLS.IMAGE_URL+myList.get(i));
		}

		share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND_MULTIPLE);
				intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
				intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*

				ArrayList<Uri> files = new ArrayList<Uri>();

				for(String path : myList1 *//* List of the files you want to send *//*) {

					Uri uri = Uri.parse(path);
					files.add(uri);
				}

				intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
				startActivity(intent);*/

				Uri uri1 = Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+"images/I001/I001sc01/2016-17/event_gallery/879978372528IMG4.JPG");
				Uri uri2 = Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+"images/I001/I001sc01/2016-17/event_gallery/879978372528IMG4.JPG");
				Uri uri3 = Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+"images/I001/I001sc01/2016-17/event_gallery/879978372528IMG4.JPG");

				ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
				imageUriArray.add(uri1);
				imageUriArray.add(uri2);

				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND_MULTIPLE);
				//intent.putExtra(Intent.EXTRA_TEXT, "Text caption message!!");
				//intent.setType("text/plain");
				intent.setType("image/*");
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				//intent.setPackage("com.whatsapp");
				intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
				startActivity(Intent.createChooser(intent, "Share images..."));
			}

		});


		
		
		
		studentFeedBackGridAdapter = new StudentFeedBackGridAdapter(getApplicationContext(), myList);
		studentFeedBackImagesGrid.setAdapter(studentFeedBackGridAdapter);
		
		studentFeedBackImagesGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position1, long id) {
				// TODO Auto-generated method stub
				
				
				
				Intent intent = new Intent(StudentFeedBackImages.this,StudentImageFeedBackSlider.class);
				intent.putExtra("array", myList);
				intent.putExtra("position1", position1);
				intent.putExtra("position",position);
				intent.putExtra("param",param);
				intent.putExtra("value",value);
				startActivity(intent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_feed_back_images, menu);
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
