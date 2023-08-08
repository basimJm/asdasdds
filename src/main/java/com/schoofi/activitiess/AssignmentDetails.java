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
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.AssignmentDetailAdapter;
import com.schoofi.adapters.AssignmentDetailAdapter1;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ExpandableHeightGridView;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

import java.util.ArrayList;

public class AssignmentDetails extends AppCompatActivity {

	ArrayList<String> myList;
	ArrayList<String> myList1;
	GridView studentFeedBackImagesGrid,studentFeedBackImagesGrid1;
	AssignmentDetailAdapter assignmentDetailAdapter;
	AssignmentDetailAdapter1 assignmentDetailAdapter1;
	ImageView back;
	int value;
	String description,title2,asnDate,teacherName,subject,submitBy;
	TextView className,description1,title1,submitBy1,subjectName;
	String teacherId,assignId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Assignment Details");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.assignment_new_details_layout);
		studentFeedBackImagesGrid = (ExpandableHeightGridView) findViewById(R.id.studentFeedbackGridView);
		studentFeedBackImagesGrid1 = (ExpandableHeightGridView) findViewById(R.id.studentFeedbackGridView1);
		description1 = (TextView) findViewById(R.id.text_description);
		myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
		teacherId = getIntent().getStringExtra("teacherId");
		assignId = getIntent().getStringExtra("assign_id");
		description = getIntent().getStringExtra("desc");
		title2 = getIntent().getStringExtra("title");
		asnDate = getIntent().getStringExtra("asn_date");
		teacherName = getIntent().getStringExtra("teach_name");
		subject = getIntent().getStringExtra("subject");
		submitBy = getIntent().getStringExtra("last_date");
		title1 = (TextView) findViewById(R.id.text_title);
		submitBy1 = (TextView) findViewById(R.id.text_submit_by);
		subjectName = (TextView) findViewById(R.id.text_subject_name);

		title1.setText(title2);
		submitBy1.setText(submitBy);
		subjectName.setText(subject);
		description1.setText(description);
		//ExpandableHeightGridView gridView = new ExpandableHeightGridView(this);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


		//value = getIntent().getExtras().getInt("value");






			myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
			assignmentDetailAdapter = new AssignmentDetailAdapter(getApplicationContext(), myList);
			studentFeedBackImagesGrid.setNumColumns(4);
			studentFeedBackImagesGrid.setAdapter(assignmentDetailAdapter);
			((ExpandableHeightGridView) studentFeedBackImagesGrid).setExpanded(true);




		
		myList1 = (ArrayList<String>)getIntent().getSerializableExtra("array1");






		assignmentDetailAdapter1 = new AssignmentDetailAdapter1(getApplicationContext(), myList1);


		studentFeedBackImagesGrid1.setNumColumns(4);
		studentFeedBackImagesGrid1.setAdapter(assignmentDetailAdapter1);
		((ExpandableHeightGridView) studentFeedBackImagesGrid1).setExpanded(true);

		studentFeedBackImagesGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (myList.get(position).endsWith("DOC") || myList.get(position).endsWith("doc") || myList.get(position).endsWith("Doc") || myList.get(position).endsWith("docx") || myList.get(position).endsWith("Docx") || myList.get(position).endsWith("DOCX") || myList.get(position).endsWith("pdf") || myList.get(position).endsWith("PDF") || myList.get(position).endsWith("Pdf") || myList.get(position).endsWith("txt") || myList.get(position).endsWith("Txt") || myList.get(position).endsWith("TXT") || myList.get(position).endsWith("mp3") || myList.get(position).endsWith("MP3") || myList.get(position).endsWith("Mp3") || myList.get(position).endsWith("mp4") || myList.get(position).endsWith("MP4") || myList.get(position).endsWith("Mp4") || myList.get(position).endsWith("MOV") || myList.get(position).endsWith("mov") || myList.get(position).endsWith("Mov")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL + myList.get(position)));
					startActivity(intent);
				} else if (myList.get(position).endsWith("PNG") || myList.get(position).endsWith("png") || myList.get(position).endsWith("Png") || myList.get(position).endsWith("JPG") || myList.get(position).endsWith("jpg") || myList.get(position).endsWith("Jpg") || myList.get(position).endsWith("JPEG") || myList.get(position).endsWith("Jpeg") || myList.get(position).endsWith("jpeg")) {


					Intent intent = new Intent(AssignmentDetails.this, TeacherStudentImageDetails.class);
					intent.putExtra("imageUrl", myList.get(position));
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL + myList.get(position)));
					startActivity(intent);
				}


			}
		});

		studentFeedBackImagesGrid1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (myList1.get(position).endsWith("DOC") || myList1.get(position).endsWith("doc") || myList1.get(position).endsWith("Doc") || myList1.get(position).endsWith("docx") || myList1.get(position).endsWith("Docx") || myList1.get(position).endsWith("DOCX") || myList1.get(position).endsWith("pdf") || myList1.get(position).endsWith("PDF") || myList1.get(position).endsWith("Pdf") || myList1.get(position).endsWith("txt") || myList1.get(position).endsWith("Txt") || myList1.get(position).endsWith("TXT") || myList1.get(position).endsWith("mp3") || myList1.get(position).endsWith("MP3") || myList1.get(position).endsWith("Mp3") || myList1.get(position).endsWith("mp4") || myList1.get(position).endsWith("MP4") || myList1.get(position).endsWith("Mp4") || myList1.get(position).endsWith("MOV") || myList1.get(position).endsWith("mov") || myList1.get(position).endsWith("Mov")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL + myList1.get(position)));
					startActivity(intent);
				} else if (myList1.get(position).endsWith("PNG") || myList1.get(position).endsWith("png") || myList1.get(position).endsWith("Png") || myList1.get(position).endsWith("JPG") || myList1.get(position).endsWith("jpg") || myList1.get(position).endsWith("Jpg") || myList1.get(position).endsWith("JPEG") || myList1.get(position).endsWith("Jpeg") || myList1.get(position).endsWith("jpeg")) {


					Intent intent = new Intent(AssignmentDetails.this, TeacherStudentImageDetails.class);
					intent.putExtra("imageUrl", myList1.get(position));
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL + myList1.get(position)));
					startActivity(intent);
				}



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
