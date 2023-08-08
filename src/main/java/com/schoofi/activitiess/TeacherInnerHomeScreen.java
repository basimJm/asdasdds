package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.StudentAttendance;
import com.schoofi.adapters.StudentHomeScreenAdapter;
import com.schoofi.adapters.StudentHomeScreenTabletAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CircularTextView;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TeacherInnerHomeScreen extends AppCompatActivity {
	
	GridView teacherInnerHomeScreenGridView;
	int []studentHomeScreenIcon = {R.drawable.attendence,R.drawable.leaverequest,R.drawable.schedule,R.drawable.assignments,R.drawable.announcement,R.drawable.feedback,R.drawable.examschedule,R.drawable.result,R.drawable.events,R.drawable.poll,R.drawable.gps,R.drawable.fees,R.drawable.fees,R.drawable.busattendance,R.drawable.diary,R.drawable.yearlyplanner,R.drawable.busnotification,R.drawable.healthcard,R.drawable.busboarding,R.drawable.buspickup,R.drawable.healthandsafety,R.drawable.studyplanner,R.drawable.library,R.drawable.videolecture,R.drawable.knowledge_resourse};
	int []studentHomeScreenIcon1 = {R.drawable.attendancetablet,R.drawable.leaverequesttablet,R.drawable.scheduletablet,R.drawable.assignmentstablet,R.drawable.announcementtablet,R.drawable.feedbacktablet,R.drawable.examscheduletablet,R.drawable.resulttablet,R.drawable.eventstablet,R.drawable.polltablet,R.drawable.gpstablet,R.drawable.feestablet,R.drawable.feestablet,R.drawable.busattendancetablet,R.drawable.diarytablet,R.drawable.yearlyplannertablet,R.drawable.busnotificationtablet,R.drawable.healthcardtablet,R.drawable.busboardingtablet,R.drawable.buspickuptablet,R.drawable.healtandsafetytablet,R.drawable.studyplannertablet,R.drawable.librarytablet,R.drawable.videolecturetablet,R.drawable.knowledge_resourse_tablet};
	String []studentHomeScreenIconName = {"ATTENDANCE","LEAVE REQUEST","TIME TABLE","ASSIGNMENTS/HOMEWORK","ANNOUNCEMENTS","FEEDBACK","EXAM SCHEDULE","RESULT","EVENTS","POLL","BUS TRACKING","FEES","Group Discussion","BUS ATTENDANCE","DIARY","YEARLY PLANNER","BUS NOTIFICATIONS","HEALTH CARD","STUDENT PICKUP","STUDENT BOARDING","HEALTH AND SAFETY","STUDY PLANNER","LIBRARY","VIDEO LECTURE","KNOWLEDGE RESOURSE"};

	int[] COLORS = {
			Color.rgb(102, 155, 76), Color.rgb(10, 173, 162), Color.rgb(248, 88, 129),
			Color.rgb(224, 157, 64), Color.rgb(85, 218, 160), Color.rgb(94, 186, 220), Color.rgb(0, 174, 179),
			Color.rgb(227, 72, 62),  Color.rgb(165, 104, 211), Color.rgb(251, 153, 99), Color.rgb(30,144,255),Color.rgb(46,139,87),Color.rgb(255,228,181),Color.rgb(255,171,64),Color.rgb(141,110,99),Color.rgb(0,96,100),Color.rgb(221, 44, 0),Color.rgb(173, 20, 87),Color.rgb(0, 105, 92),Color.rgb(250, 128, 114),Color.rgb(240, 128, 128), Color.rgb(248, 88, 129)
	};
	ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
	ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
	ArrayList<Integer> studentHomeScreenIconFinal1 = new ArrayList<Integer>();
	StudentHomeScreenAdapter studentHomeScreenAdapter;
	StudentHomeScreenTabletAdapter studentHomeScreenTabletAdapter;
	String u = "";
	TextView userName;
	Button buttonGone,buttonLogout;
	ImageView settings,studentList,bellNotification;
	String permissions;
	ArrayList<String> permissionsArray= new ArrayList<String>();
	CircularTextView circularTextView;
	private JSONArray badgeArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_teacher_inner_home_screen);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		float yInches= metrics.heightPixels/metrics.ydpi;
		float xInches= metrics.widthPixels/metrics.xdpi;
		double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
		if (diagonalInches>=6.0){
		    // 6.5inch device or bigger
			u = "1";
		}else{
		    // smaller device
			u = "2";
			
		}
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Inner HomeScreen");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		userName = (TextView) findViewById(R.id.txt_teacherName);
		studentList = (ImageView) findViewById(R.id.studentList);
		teacherInnerHomeScreenGridView = (GridView) findViewById(R.id.teacherHomeGridView);
		buttonGone = (Button) findViewById(R.id.btn_gone);
		buttonGone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		circularTextView = (CircularTextView) findViewById(R.id.circularTextView);
		circularTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(TeacherInnerHomeScreen.this, NotificationIntentClass.class);
				startActivity(intent);

			}
		});

		bellNotification = (ImageView) findViewById(R.id.img_bell);

		Preferences.getInstance().loadPreference(getApplicationContext());
		//System.out.print("ll"+Preferences.getInstance().teachId);

		Log.d("ll","kkk"+Preferences.getInstance().teachId);

		bellNotification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(TeacherInnerHomeScreen.this, NotificationIntentClass.class);
				startActivity(intent);
			}
		});

		studentList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSubjectStudentList.class);
				intent.putExtra("value","1");
				startActivity(intent);
			}
		});

		permissions = Preferences.getInstance().permissions;
		permissionsArray = new ArrayList<String>(Arrays.asList(permissions.split(",")));

		for(int h =0;h<permissionsArray.size();h++)
		{
			if(permissionsArray.get(h).matches("A"))
			{
				if(studentHomeScreenIconName[h].matches("FEES") || studentHomeScreenIconName[h].matches("BUS TRACKING") || studentHomeScreenIconName[h].matches("HEALTH CARD"))
				{

				}
				else {

					studentHomeScreenIconNamefinal.add(studentHomeScreenIconName[h]);
					studentHomeScreenIconFinal.add(studentHomeScreenIcon[h]);
					studentHomeScreenIconFinal1.add(studentHomeScreenIcon1[h]);
				}

			}

			else
			{
				Log.d("kk","kkk");
			}
		}

		/*for(int g=0;g<studentHomeScreenIconNamefinal.size();g++)
		{
			if(studentHomeScreenIconNamefinal.get(g).matches("Bus Attendance"))
			{
				studentHomeScreenIconNamefinal.remove(g);
				studentHomeScreenIconFinal.remove(g);
				studentHomeScreenIconFinal1.remove(g);
			}
		}*/
		
		if(u.matches("2"))
		{
		teacherInnerHomeScreenGridView.setAdapter(new StudentHomeScreenAdapter(TeacherInnerHomeScreen.this, studentHomeScreenIconFinal,studentHomeScreenIconNamefinal,COLORS));
		}
		
		else
		{
			teacherInnerHomeScreenGridView.setAdapter(new StudentHomeScreenTabletAdapter(TeacherInnerHomeScreen.this, studentHomeScreenIconFinal1,studentHomeScreenIconNamefinal,COLORS));
			
		}
		
		
		//buttonLogout = (Button) findViewById(R.id.btn_TeacherLogout);
		/*buttonLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(TeacherInnerHomeScreen.this,LoginScreen.class);
				startActivity(intent);
				
			}
		});*/
		
		userName.setText(Preferences.getInstance().Name);
		
		/*buttonLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(TeacherInnerHomeScreen.this);
				Intent intent = new Intent(TeacherInnerHomeScreen.this,LoginScreen.class);
				startActivity(intent);
				finish();
				
			}
		});*/

		settings = (ImageView) findViewById(R.id.settings);

		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentSettings.class);
				startActivity(intent);
			}
		});

		postAttendance();
		
	    //teacherInnerHomeScreenGridView.setAdapter(new TeacherInnerHomeScreenAdapter(TeacherInnerHomeScreen.this, studentHomeScreenIcon,studentHomeScreenIconName,COLORS));
		teacherInnerHomeScreenGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						if(studentHomeScreenIconNamefinal.get(0).matches("ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
							startActivity(intent);
							break;

							/*Intent intent = new Intent(TeacherInnerHomeScreen.this, GalleryActivity.class);
							startActivity(intent);
							break;*/
						}

						else

						if(studentHomeScreenIconNamefinal.get(0).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(0).matches("TIME TABLE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("FEEDBACK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("RESULT"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("EVENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("POLL"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("FEES"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("DIARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("BUS NOTIFICATIONS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("STUDENT PICKUP"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("STUDENT BOARDING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}



						else
						if(studentHomeScreenIconNamefinal.get(0).matches("HEALTH AND SAFETY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
							if(studentHomeScreenIconNamefinal.get(0).matches("STUDY PLANNER"))
							{
								if(Preferences.getInstance().schoolType.matches("College"))
								{
									Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

									startActivity(intent24);
									Preferences.getInstance().loadPreference(getApplicationContext());
									Preferences.getInstance().studyPlanner = "studyPlanner";
									Preferences.getInstance().savePreference(getApplicationContext());
									//Utils.showToast(getApplicationContext(),"Coming Soon");
									break;
								}
								else {
									Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

									startActivity(intent24);
									//Utils.showToast(getApplicationContext(),"Coming Soon");
									Preferences.getInstance().loadPreference(getApplicationContext());
									Preferences.getInstance().studyPlanner = "studyPlanner";
									Preferences.getInstance().savePreference(getApplicationContext());
									break;
								}
							}

							else
							if(studentHomeScreenIconNamefinal.get(0).matches("LIBRARY"))
							{
								Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
								startActivity(intent);
								break;
							}

							else
							if(studentHomeScreenIconNamefinal.get(0).matches("KNOWLEDGE RESOURSE"))
							{
								Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
								startActivity(intent);
								break;
							}

							else
							if(studentHomeScreenIconNamefinal.get(0).matches("VIDEO LECTURE"))
							{
								Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
								startActivity(intent);
								break;
							}



						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;




					case 1: if(studentHomeScreenIconNamefinal.get(1).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(1).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(1).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);

						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(1).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 2: if(studentHomeScreenIconNamefinal.get(2).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(2).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(2).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(2).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;
					case 3:
						if(studentHomeScreenIconNamefinal.get(position).matches("ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(position).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(position).matches("TIME TABLE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(position).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(position).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(position).matches("FEEDBACK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(position).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(position).matches("RESULT"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(position).matches("EVENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(position).matches("POLL"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(position).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(position).matches("FEES"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("DIARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("BUS NOTIFICATIONS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("STUDENT PICKUP"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("STUDENT BOARDING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("HEALTH AND SAFETY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("STUDY PLANNER"))
						{
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

								startActivity(intent24);
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								break;
							}
							else {
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

								startActivity(intent24);
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								break;
							}
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("LIBRARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(3).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;
					case 4:
						if(studentHomeScreenIconNamefinal.get(4).matches("ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(4).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(4).matches("TIME TABLE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("FEEDBACK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("RESULT"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("EVENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("POLL"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("FEES"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("DIARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("BUS NOTIFICATIONS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("STUDENT PICKUP"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("STUDENT BOARDING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("HEALTH AND SAFETY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("STUDY PLANNER"))
						{
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

								startActivity(intent24);
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								break;
							}
							else {
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

								startActivity(intent24);
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								break;
							}
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("LIBRARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(4).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;
					case 5:
						if(studentHomeScreenIconNamefinal.get(5).matches("ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(5).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(5).matches("TIME TABLE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("FEEDBACK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("RESULT"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("EVENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("POLL"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("FEES"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("DIARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("BUS NOTIFICATIONS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("STUDENT PICKUP"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("STUDENT BOARDING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("HEALTH AND SAFETY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("STUDY PLANNER"))
						{
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

								startActivity(intent24);
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								break;
							}
							else {
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

								startActivity(intent24);
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								break;
							}
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("LIBRARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(5).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;
					case 6:
						if(studentHomeScreenIconNamefinal.get(6).matches("ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(6).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(6).matches("TIME TABLE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("FEEDBACK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("RESULT"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("EVENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("POLL"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("FEES"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("DIARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("BUS NOTIFICATIONS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("STUDENT PICKUP"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("STUDENT BOARDING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("HEALTH AND SAFETY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("STUDY PLANNER"))
						{
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

								startActivity(intent24);
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								break;
							}
							else {
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

								startActivity(intent24);
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								break;
							}
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("LIBRARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(6).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;
					case 7:
						if(studentHomeScreenIconNamefinal.get(7).matches("ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(7).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(7).matches("TIME TABLE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("FEEDBACK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("RESULT"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("EVENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("POLL"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("FEES"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("DIARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("BUS NOTIFICATIONS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("STUDENT PICKUP"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("STUDENT BOARDING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("HEALTH AND SAFETY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("STUDY PLANNER"))
						{
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

								startActivity(intent24);
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								break;
							}
							else {
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

								startActivity(intent24);
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								break;
							}
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("LIBRARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(7).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;
					case 8:
						if(studentHomeScreenIconNamefinal.get(8).matches("ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(8).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(8).matches("TIME TABLE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("FEEDBACK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("RESULT"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("EVENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("POLL"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("FEES"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("DIARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("BUS NOTIFICATIONS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("STUDENT PICKUP"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("STUDENT BOARDING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("HEALTH AND SAFETY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("STUDY PLANNER"))
						{
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

								startActivity(intent24);
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								break;
							}
							else {
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

								startActivity(intent24);
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								break;
							}
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("LIBRARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(8).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;
					case 9:
						if(studentHomeScreenIconNamefinal.get(9).matches("ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(9).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(9).matches("TIME TABLE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("FEEDBACK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("RESULT"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("EVENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("POLL"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("FEES"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("DIARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("BUS NOTIFICATIONS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("STUDENT PICKUP"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("STUDENT BOARDING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("HEALTH AND SAFETY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("STUDY PLANNER"))
						{
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

								startActivity(intent24);
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								break;
							}
							else {
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

								startActivity(intent24);
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								break;
							}
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("LIBRARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(9).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;
					case 10: if(studentHomeScreenIconNamefinal.get(10).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(10).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(10).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 11:
						if(studentHomeScreenIconNamefinal.get(11).matches("ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(11).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(11).matches("TIME TABLE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("FEEDBACK"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("RESULT"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("EVENTS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("POLL"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("FEES"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("DIARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("BUS NOTIFICATIONS"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
							intent.putExtra("value","2");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("STUDENT PICKUP"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("STUDENT BOARDING"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
							intent.putExtra("value","0");
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("HEALTH AND SAFETY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("STUDY PLANNER"))
						{
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

								startActivity(intent24);
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								break;
							}
							else {
								Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

								startActivity(intent24);
								//Utils.showToast(getApplicationContext(),"Coming Soon");
								Preferences.getInstance().loadPreference(getApplicationContext());
								Preferences.getInstance().studyPlanner = "studyPlanner";
								Preferences.getInstance().savePreference(getApplicationContext());
								break;
							}
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("LIBRARY"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(11).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;

					case 12:if(studentHomeScreenIconNamefinal.get(12).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(12).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(12).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 13:if(studentHomeScreenIconNamefinal.get(13).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(13).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(13).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(13).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;


					case 14:if(studentHomeScreenIconNamefinal.get(14).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(14).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(14).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(14).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 15:if(studentHomeScreenIconNamefinal.get(15).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(15).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(15).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(15).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 16:if(studentHomeScreenIconNamefinal.get(16).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(16).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(16).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(16).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 17:if(studentHomeScreenIconNamefinal.get(17).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(17).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(17).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(17).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 18:if(studentHomeScreenIconNamefinal.get(18).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(18).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(18).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(18).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(18).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 19:if(studentHomeScreenIconNamefinal.get(19).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(19).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(19).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(19).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 20:if(studentHomeScreenIconNamefinal.get(20).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(20).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(20).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(20).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 21:if(studentHomeScreenIconNamefinal.get(21).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(21).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(21).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;


					case 22:if(studentHomeScreenIconNamefinal.get(22).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(22).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(22).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(21).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;


					}

					else
					if(studentHomeScreenIconNamefinal.get(22).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;


					case 23:if(studentHomeScreenIconNamefinal.get(23).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,EmployeeAttendanceOptionScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(23).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(22).matches("TIME TABLE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherNewAssignmentVersion.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("FEEDBACK"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentFeedback.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("RESULT"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentResult.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("EVENTS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("POLL"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,AboutSchoolContactUs.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("FEES"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("DIARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,DiaryWallScreen.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("BUS NOTIFICATIONS"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, BusAdminBusList.class);
						intent.putExtra("value","2");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("STUDENT PICKUP"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentDropFormNewActivity.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("STUDENT BOARDING"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AdminStudentBusBoardingScreen.class);
						intent.putExtra("value","0");
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, AuditUserHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("STUDY PLANNER"))
					{
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassList.class);

							startActivity(intent24);
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							break;
						}
						else {
							Intent intent24 = new Intent(TeacherInnerHomeScreen.this, StudyPlannerClassListScreen.class);

							startActivity(intent24);
							//Utils.showToast(getApplicationContext(),"Coming Soon");
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().studyPlanner = "studyPlanner";
							Preferences.getInstance().savePreference(getApplicationContext());
							break;
						}
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("LIBRARY"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, KnowledgeResourseClassListScreen.class);
						startActivity(intent);
						break;


					}

					else
					if(studentHomeScreenIconNamefinal.get(23).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(TeacherInnerHomeScreen.this, TeacherVideoLectureSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;





				}



			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_inner_home_screen, menu);
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

	protected void postAttendance()
	{
		//setSupportProgressBarIndeterminateVisibility(true);
		final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




		final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_BADGE_COUNT/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				//Utils.showToast(getApplicationContext(), ""+response);
				//System.out.println(url1);
				try {
					responseObject = new JSONObject(response);

					if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
						loading.dismiss();
						//Utils.showToast(TeacherInnerHomeScreen.this, "Error Submitting Comment");

					} else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
						loading.dismiss();
						Utils.showToast(TeacherInnerHomeScreen.this, "Session Expired:Please Login Again");
					} else if (responseObject.has("ResponseObject")) {
						loading.dismiss();
						badgeArray = new JSONObject(response).getJSONArray("ResponseObject");
						if (null != badgeArray && badgeArray.length() >= 0) {
							Cache.Entry e = new Cache.Entry();
							e.data = badgeArray.toString().getBytes();
							VolleySingleton.getInstance(TeacherInnerHomeScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_BADGE_COUNT + "?user_id=" + Preferences.getInstance().userId + "&device_id=" + Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&email_id="+Preferences.getInstance().userEmailId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&teach_id="+Preferences.getInstance().teachId, e);

							circularTextView.setText(badgeArray.getJSONObject(0).getString("badge"));
                            Preferences.getInstance().loadPreference(getApplicationContext());
                            Preferences.getInstance().teacherBadgeCount = badgeArray.getJSONObject(0).getString("pending_leaves_count");
                            Preferences.getInstance().savePreference(getApplicationContext());
							//loading.dismiss();

						}
					} else
						//Utils.showToast(TeacherInnerHomeScreen.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}


				catch(JSONException e)
				{
					e.printStackTrace();
					loading.dismiss();
					//Utils.showToast(TeacherStudentAttendanceDetails.this, "Error s! Please try after sometime.");
				}
				//setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error) {
				loading.dismiss();
				//Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
				//setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(TeacherInnerHomeScreen.this);
				Map<String,String> params = new HashMap<String, String>();
				//params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
				//params.put("Students", jsonObject1.toString());

				params.put("user_id",Preferences.getInstance().userId);
				//params.put("tea_id", Preferences.getInstance().userId);
				params.put("device_id",Preferences.getInstance().deviceId);
				params.put("token",Preferences.getInstance().token);
				params.put("role_id",Preferences.getInstance().userRoleId);
				params.put("email_id",Preferences.getInstance().userEmailId);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("cls_id",Preferences.getInstance().studentClassId);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("teach_id",Preferences.getInstance().teachId);
				//params.put("Students", "harsh");
				//params.put("u_email_id", Preferences.getInstance().userEmailId);
				return params;
			}};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(this))
			queue.add(requestObject);
		else
		{
			loading.dismiss();
			Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
			//setSupportProgressBarIndeterminateVisibility(false);
		}



	}
}
