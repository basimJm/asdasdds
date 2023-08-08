package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.HealthAndAuditAdapter;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TeacherHomeScreenVO;
import com.schoofi.utils.TrackerName;

import java.util.ArrayList;

public class HealthAndAuditHomeScreen extends AppCompatActivity {

	public ArrayList<TeacherHomeScreenVO> temparr;
	//TeacherHomeScreenAdapter teacherHomeScreenAdapter;
	private ListView healthAndAuditListView;
	ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Health And Audit HomeScreen");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_health_and_audit_home_screen);

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		temparr=new ArrayList<TeacherHomeScreenVO>();
		TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Manage User", "1");
		TeacherHomeScreenVO tecHomeScreenVO1 = new TeacherHomeScreenVO("Manage Task", "2");
		TeacherHomeScreenVO tecHomeScreenVO2 = new TeacherHomeScreenVO("Review Task", "3");
		
		temparr.add(tecHomeScreenVO);
		temparr.add(tecHomeScreenVO1);
		temparr.add(tecHomeScreenVO2);
		
		healthAndAuditListView = (ListView) findViewById(R.id.teacher_homescreen_listview);
        HealthAndAuditAdapter healthAndAuditAdapter = new HealthAndAuditAdapter(getApplicationContext(),temparr);
		healthAndAuditListView.setAdapter(healthAndAuditAdapter);
		healthAndAuditListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(temparr.get(position).getFieldName().matches("Manage User"))
				{
					Intent intent = new Intent(HealthAndAuditHomeScreen.this,HealthAndAuditManageUser.class);
					startActivity(intent);
					
				}
				
				else
					if(temparr.get(position).getFieldName().matches("Manage Task"))
					{
						Intent intent = new Intent(HealthAndAuditHomeScreen.this,HealthAndAuditTaskList.class);
						startActivity(intent);
						
					}
				
					else
						if(temparr.get(position).getFieldName().matches("Review Task"))
						{
							Intent intent  = new Intent(HealthAndAuditHomeScreen.this,HealthAndAuditAuditsList.class);
							startActivity(intent);
						}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_and_audit_home_screen, menu);
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
