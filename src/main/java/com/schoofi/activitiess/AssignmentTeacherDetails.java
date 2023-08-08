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
import com.schoofi.adapters.AssignmentDetailAdapter;
import com.schoofi.adapters.AssignmentDetailAdapter1;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ExpandableHeightGridView;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

import java.util.ArrayList;

public class AssignmentTeacherDetails extends AppCompatActivity {

	ArrayList<String> myList;
	ArrayList<String> myList1;
	GridView studentFeedBackImagesGrid,studentFeedBackImagesGrid1;
	AssignmentDetailAdapter assignmentDetailAdapter;
	AssignmentDetailAdapter1 assignmentDetailAdapter1;
	ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Assignment Teacher Details");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_student_feed_back_images);
		studentFeedBackImagesGrid = (ExpandableHeightGridView) findViewById(R.id.studentFeedbackGridView);
		//studentFeedBackImagesGrid1 = (ExpandableHeightGridView) findViewById(R.id.studentFeedbackGridView1);
		//ExpandableHeightGridView gridView = new ExpandableHeightGridView(this);



		myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
		
		//myList1 = (ArrayList<String>)getIntent().getSerializableExtra("array1");

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});





		assignmentDetailAdapter = new AssignmentDetailAdapter(getApplicationContext(), myList);
		//assignmentDetailAdapter1 = new AssignmentDetailAdapter1(getApplicationContext(), myList1);
		studentFeedBackImagesGrid.setNumColumns(4);
		studentFeedBackImagesGrid.setAdapter(assignmentDetailAdapter);
		((ExpandableHeightGridView) studentFeedBackImagesGrid).setExpanded(true);


		//studentFeedBackImagesGrid1.setNumColumns(4);
		//studentFeedBackImagesGrid1.setAdapter(assignmentDetailAdapter1);
		//((ExpandableHeightGridView) studentFeedBackImagesGrid1).setExpanded(true);

		studentFeedBackImagesGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if(myList.get(position).endsWith("DOC") || myList.get(position).endsWith("doc") || myList.get(position).endsWith("Doc") || myList.get(position).endsWith("docx") || myList.get(position).endsWith("Docx") || myList.get(position).endsWith("DOCX") || myList.get(position).endsWith("pdf") || myList.get(position).endsWith("PDF") || myList.get(position).endsWith("Pdf") || myList.get(position).endsWith("txt") || myList.get(position).endsWith("Txt") || myList.get(position).endsWith("TXT"))
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+myList.get(position)));
					startActivity(intent);
				}

				else
				{
					Intent intent = new Intent(AssignmentTeacherDetails.this,TeacherStudentImageDetails.class);
					intent.putExtra("imageUrl", myList.get(position));
					startActivity(intent);
				}



			}
		});

		/*studentFeedBackImagesGrid1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if(myList.get(position).endsWith("DOC") || myList1.get(position).endsWith("doc") || myList1.get(position).endsWith("Doc") || myList1.get(position).endsWith("docx") || myList1.get(position).endsWith("Docx") || myList1.get(position).endsWith("DOCX") || myList1.get(position).endsWith("pdf") || myList1.get(position).endsWith("PDF") || myList1.get(position).endsWith("Pdf") || myList1.get(position).endsWith("txt") || myList1.get(position).endsWith("Txt") || myList1.get(position).endsWith("TXT"))
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.SERVER_URL+myList1.get(position)));
					startActivity(intent);
				}

				else
				{
					Intent intent = new Intent(AssignmentDetails.this,TeacherStudentImageDetails.class);
					intent.putExtra("imageUrl", myList1.get(position));
					startActivity(intent);
				}



			}
		});*/
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
