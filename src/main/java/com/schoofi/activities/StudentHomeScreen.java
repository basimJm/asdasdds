package com.schoofi.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.schoofi.activitiess.AboutSchoolContactUs;
import com.schoofi.activitiess.AdminHealthCardOptionsActivity;
import com.schoofi.activitiess.AssesmentAllotedScreen;
import com.schoofi.activitiess.AssesmentNewHomeScreen;
import com.schoofi.activitiess.AssesmentNewQuestionsScreen;
import com.schoofi.activitiess.KnowledgeResourceSubjectScreen;
import com.schoofi.activitiess.NotificationIntentClass;
import com.schoofi.activitiess.ParentFees;
import com.schoofi.activitiess.ParentStudentBusAttendance;
import com.schoofi.activitiess.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activitiess.DiaryWallScreen;
import com.schoofi.activitiess.SchoolPlannerMakingFirstScreen;
import com.schoofi.activitiess.StudentAnnouncement;
import com.schoofi.activitiess.StudentAttendancePrimaryOptionScreen;
import com.schoofi.activitiess.StudentBusTrackingRouteScreen;
import com.schoofi.activitiess.StudentChooseElectiveSubject;
import com.schoofi.activitiess.StudentCollegeAttendancePrimaryScreen;
import com.schoofi.activitiess.StudentCollegeResults;
import com.schoofi.activitiess.StudentEventList;
import com.schoofi.activitiess.StudentExamSchedule;
import com.schoofi.activitiess.StudentFeedBack;
import com.schoofi.activitiess.StudentHostelReservationDetailsScreen;
import com.schoofi.activitiess.StudentLeaveRequest;
import com.schoofi.activitiess.StudentNewAssignemntVerion;
import com.schoofi.activitiess.NewPollActivity;
import com.schoofi.activitiess.StudentResult;
import com.schoofi.activitiess.StudentSchedule;
import com.schoofi.activitiess.StudentSettings;
import com.schoofi.activitiess.StudentSubjectList;
import com.schoofi.activitiess.TeacherStudentDiaryScreen;
import com.schoofi.activitiess.UserLibraryHomeScreen;
import com.schoofi.activitiess.VideoLectureHomeScreen;
import com.schoofi.adapters.StudentHomeScreenAdapter;
import com.schoofi.adapters.StudentHomeScreenTabletAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CircularTextView;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.speech.tts.TextToSpeech;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static com.google.firebase.crash.FirebaseCrash.log;
import static com.schoofi.utils.SchoofiApplication.context;


public class StudentHomeScreen extends AppCompatActivity  {

	AppUpdateManagerFactory  mUpdateManager;

	protected ProgressBar progressBar;

	GridView studentHomeScreenGridView;
	ImageView settings,bellNotification,subjectIcon;
	int []studentHomeScreenIcon = {R.drawable.attendence,R.drawable.leaverequest,R.drawable.schedule,R.drawable.assignments,R.drawable.announcement,R.drawable.feedback,R.drawable.examschedule,R.drawable.result,R.drawable.events,R.drawable.poll,R.drawable.gps,R.drawable.fees,R.drawable.busattendance,R.drawable.busattendance,R.drawable.diary,R.drawable.yearlyplanner,R.drawable.healthcard,R.drawable.hostel,R.drawable.studyplanner,R.drawable.library,R.drawable.knowledge_resourse,R.drawable.videolecture,R.drawable.knowledge_resourse};
	int []studentHomeScreenIcon1 = {R.drawable.attendancetablet,R.drawable.leaverequesttablet,R.drawable.scheduletablet,R.drawable.assignmentstablet,R.drawable.announcementtablet,R.drawable.feedbacktablet,R.drawable.examscheduletablet,R.drawable.resulttablet,R.drawable.eventstablet,R.drawable.polltablet,R.drawable.gpstablet,R.drawable.feestablet,R.drawable.busattendancetablet,R.drawable.busattendancetablet,R.drawable.diarytablet,R.drawable.yearlyplannertablet,R.drawable.healthcardtablet,R.drawable.hosteltablet,R.drawable.studyplannertablet,R.drawable.librarytablet,R.drawable.knowledge_resourse_tablet,R.drawable.videolecturetablet,R.drawable.librarytablet};
	String []studentHomeScreenIconName = {"ATTENDANCE","LEAVE REQUEST","TIME TABLE","ASSIGNMENTS/HOMEWORK","ANNOUNCEMENTS","FEEDBACK","EXAM SCHEDULE","RESULT","EVENTS","POLL","BUS TRACKING","FEES","Group Discussion","BUS ATTENDANCE","DIARY","YEARLY PLANNER","HEALTH CARD","HOSTEL","STUDY PLANNER","LIBRARY","KNOWLEDGE RESOURSE","VIDEO LECTURE","ASSESMENT"};
	ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
	ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
	ArrayList<Integer> studentHomeScreenIconFinal1 = new ArrayList<Integer>();
	int[] COLORS = {
			Color.rgb(102, 155, 76), Color.rgb(10, 173, 162), Color.rgb(248, 88, 129),
			Color.rgb(224, 157, 64), Color.rgb(85, 218, 160), Color.rgb(94, 186, 220), Color.rgb(0, 174, 179),
			Color.rgb(227, 72, 62),  Color.rgb(165, 104, 211), Color.rgb(251, 153, 99), Color.rgb(30,144,255),Color.rgb(46,139,87),Color.rgb(255,228,181),Color.rgb(255,171,64),Color.rgb(141,110,99),Color.rgb(0,96,100),Color.rgb(221, 44, 0),Color.rgb(173, 20, 87),Color.rgb(0, 105, 92),Color.rgb(227, 72, 62),Color.rgb(227, 65, 89),Color.rgb(227, 72, 62)
	};
	//1 Attendance A 2 Announcements A 3 Assignment A 4 Exam A 5 Exam Result A 6 Class Schedule A 7 Leave Request A 8 Feedback A 9 Group Discussion A 10 Poll A
	StudentHomeScreenAdapter studentHomeScreenAdapter;
	StudentHomeScreenTabletAdapter studentHomeScreenTabletAdapter;
	TextView userName;
	private TextView tickerView;
	Button buttonGone,buttonLogout;
	String u = "";
	TextToSpeech t1;
	String permissions;
	ArrayList<String> permissionsArray= new ArrayList<String>();
	CircularTextView circularTextView;
	private JSONArray badgeArray;

	private AppUpdateManager appUpdateManager;
	InstallStateUpdatedListener listener;

	//String []studentHomeScreenMainArray = {"Attendance","Announcements"};

	private int REQUEST_APP_UPDATE = 23;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		
		setContentView(R.layout.activity_student_home_screen);

		appUpdateManager = AppUpdateManagerFactory.create(StudentHomeScreen.this);

		listener = new InstallStateUpdatedListener() {
			@Override
			public void onStateUpdate(InstallState state) {
				Log.d("installState", state.toString());
				if (state.installStatus() == InstallStatus.DOWNLOADED) {
					// After the update is downloaded, show a notification
					// and request user confirmation to restart the app.
					// SnackBarManager.getSnackBarManagerInstance().showSnackBar(GaanaActivity.this, "An update has just been downloaded.", true);
					popupSnackbarForCompleteUpdate();
				}
			}
		};


		appUpdateManager.registerListener(listener);

		Task<AppUpdateInfo> appUpdateInfo = appUpdateManager.getAppUpdateInfo();

		appUpdateInfo.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
			@Override
			public void onSuccess(AppUpdateInfo appUpdateInfo) {
				if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
					if (appUpdateInfo.isUpdateTypeAllowed(FLEXIBLE)) {
						Log.d("App update A", "Flexible");
						int updateType = FLEXIBLE;
						requestUpdate(appUpdateInfo, updateType);
					} else if (appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
						Log.d("App update B", "IMMEDIATE");
						int updateType = IMMEDIATE;
						requestUpdate(appUpdateInfo, updateType);
					}
				}
			}
		});




		
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
		t.setScreenName("Student HomeScreen");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		/*t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR) {
					t1.setLanguage(Locale.UK);
				}
			}
		});

		t1.speak("Welcome", TextToSpeech.QUEUE_FLUSH, null);*/

		circularTextView = (CircularTextView) findViewById(R.id.circularTextView);
		circularTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(StudentHomeScreen.this, NotificationIntentClass.class);
				startActivity(intent);

			}
		});

		bellNotification = (ImageView) findViewById(R.id.img_bell);

		bellNotification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(StudentHomeScreen.this, NotificationIntentClass.class);
				startActivity(intent);
			}
		});





		studentHomeScreenGridView = (GridView) findViewById(R.id.studentHomeGridView);
		userName = (TextView) findViewById(R.id.txt_userName);
		buttonGone = (Button) findViewById(R.id.btn_gone);
		subjectIcon = (ImageView) findViewById(R.id.subjects);
		subjectIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StudentHomeScreen.this, StudentChooseElectiveSubject.class);
				startActivity(intent);
			}
		});
		Preferences.getInstance().loadPreference(StudentHomeScreen.this);

		permissions = Preferences.getInstance().permissions;
		permissionsArray = new ArrayList<String>(Arrays.asList(permissions.split(",")));



		for(int h =0;h<permissionsArray.size();h++)
		{

			if(permissionsArray.get(h).matches("A"))
			{

				studentHomeScreenIconNamefinal.add(studentHomeScreenIconName[h]);
//				Log.d("po",studentHomeScreenIconNamefinal.get(h));
				studentHomeScreenIconFinal.add(studentHomeScreenIcon[h]);
				studentHomeScreenIconFinal1.add(studentHomeScreenIcon1[h]);

			}

			else
			{
				Log.d("kk","kkk");
			}


		}

		/*for(int g=0;g<studentHomeScreenIconNamefinal.size();g++)
		{
			if(studentHomeScreenIconNamefinal.get(g).matches("BUS NOTIFICATIONS"))
			{
				studentHomeScreenIconNamefinal.remove(g);
				studentHomeScreenIconFinal.remove(g);
				studentHomeScreenIconFinal1.remove(g);
			}
		}*/

		//Utils.showToast(getApplicationContext(),studentHomeScreenIconNamefinal.toString());

		//Utils.showToast(getApplicationContext(),permissionsArray.toString());

		for(int u=0;u<permissionsArray.size();u++)
		{
			Log.d("poo",permissionsArray.get(u));
		}

		for(int o=0;o<studentHomeScreenIconNamefinal.size();o++)
		{
			Log.d("po",studentHomeScreenIconNamefinal.get(o));
		}

		buttonGone.setVisibility(View.INVISIBLE);

		settings = (ImageView) findViewById(R.id.settings);

		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StudentHomeScreen.this,StudentSettings.class);
				startActivity(intent);
			}
		});




	
		
		if(Preferences.getInstance().userRoleId.matches("5"))
		{
		userName.setText(Preferences.getInstance().Name);
			//t1.speak(Preferences.getInstance().Name, TextToSpeech.QUEUE_FLUSH, null);
		}
		else
			if(Preferences.getInstance().userRoleId.matches("6"))
			{
				userName.setText(Preferences.getInstance().studentName);
				//t1.speak(Preferences.getInstance().studentName, TextToSpeech.QUEUE_FLUSH, null);
			}
		
		if(u.matches("2"))
		{
		studentHomeScreenGridView.setAdapter(new StudentHomeScreenAdapter(StudentHomeScreen.this, studentHomeScreenIconFinal,studentHomeScreenIconNamefinal,COLORS));
		}
		
		else
		{
			studentHomeScreenGridView.setAdapter(new StudentHomeScreenTabletAdapter(StudentHomeScreen.this, studentHomeScreenIconFinal1,studentHomeScreenIconNamefinal,COLORS));
			
		}

		postAttendance();
		studentHomeScreenGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Preferences.getInstance().loadPreference(getApplicationContext());
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					if(studentHomeScreenIconNamefinal.get(0).matches("ATTENDANCE"))
					{

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);

					}

					else

						if(studentHomeScreenIconNamefinal.get(0).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
					else
						if(studentHomeScreenIconNamefinal.get(0).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

					else
						if(studentHomeScreenIconNamefinal.get(0).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

					else
						if(studentHomeScreenIconNamefinal.get(0).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

					else
						if(studentHomeScreenIconNamefinal.get(0).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

					else
						if(studentHomeScreenIconNamefinal.get(0).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(0).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}


						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
					break;




				case 1:
					if(studentHomeScreenIconNamefinal.get(1).matches("ATTENDANCE"))
					{

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);


				}

				else

				if(studentHomeScreenIconNamefinal.get(1).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(1).matches("TIME TABLE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("ASSIGNMENTS/HOMEWORK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
					intent.putExtra("value","1");

					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("ANNOUNCEMENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("FEEDBACK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("EXAM SCHEDULE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("RESULT"))
				{
					Preferences.getInstance().loadPreference(getApplicationContext());
					if(Preferences.getInstance().schoolType.matches("College"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
						startActivity(intent);
						break;
					}
					else
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
						startActivity(intent);
						break;
					}

				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("EVENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("POLL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("FEES"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("DIARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("BUS ATTENDANCE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("HEALTH CARD"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("HOSTEL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("STUDY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("LIBRARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("ASSESMENT"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(1).matches("KNOWLEDGE RESOURSE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
					startActivity(intent);
					break;
				}
				else

					if(studentHomeScreenIconNamefinal.get(1).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
						startActivity(intent);
						break;
					}

				else
				{
					Utils.showToast(getApplicationContext(),"No access");
				}
					break;

				case 2:
					if(studentHomeScreenIconNamefinal.get(2).matches("ATTENDANCE"))
					{

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);

					}


				else

				if(studentHomeScreenIconNamefinal.get(2).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(2).matches("TIME TABLE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("ASSIGNMENTS/HOMEWORK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
					intent.putExtra("value","1");

					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("ANNOUNCEMENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("FEEDBACK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("EXAM SCHEDULE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("RESULT"))
				{
					Preferences.getInstance().loadPreference(getApplicationContext());
					if(Preferences.getInstance().schoolType.matches("College"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
						startActivity(intent);
						break;
					}
					else
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
						startActivity(intent);
						break;
					}

				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("EVENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("POLL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AboutSchoolContactUs.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("FEES"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("DIARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("BUS ATTENDANCE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("HEALTH CARD"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("HOSTEL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("STUDY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("LIBRARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("ASSESMENT"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(2).matches("KNOWLEDGE RESOURSE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
					startActivity(intent);
					break;
				}

				else

				if(studentHomeScreenIconNamefinal.get(2).matches("VIDEO LECTURE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				{
					Utils.showToast(getApplicationContext(),"No access");
				}
					break;
				case 3:

					if(studentHomeScreenIconNamefinal.get(3).matches("ATTENDANCE"))
					{

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);

					}


				else

				if(studentHomeScreenIconNamefinal.get(3).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(3).matches("TIME TABLE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("ASSIGNMENTS/HOMEWORK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
					intent.putExtra("value","1");

					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("ANNOUNCEMENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("FEEDBACK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("EXAM SCHEDULE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("RESULT"))
				{
					Preferences.getInstance().loadPreference(getApplicationContext());
					if(Preferences.getInstance().schoolType.matches("College"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
						startActivity(intent);
						break;
					}
					else
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
						startActivity(intent);
						break;
					}

				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("EVENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("POLL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("FEES"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("DIARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("BUS ATTENDANCE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("HEALTH CARD"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("HOSTEL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("STUDY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("LIBRARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("ASSESMENT"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(3).matches("KNOWLEDGE RESOURSE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
					startActivity(intent);
					break;
				}

				else

				if(studentHomeScreenIconNamefinal.get(3).matches("VIDEO LECTURE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
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

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);

					}


				else

				if(studentHomeScreenIconNamefinal.get(4).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(4).matches("TIME TABLE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("ASSIGNMENTS/HOMEWORK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
					intent.putExtra("value","1");

					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("ANNOUNCEMENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("FEEDBACK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("EXAM SCHEDULE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("RESULT"))
				{
					Preferences.getInstance().loadPreference(getApplicationContext());
					if(Preferences.getInstance().schoolType.matches("College"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
						startActivity(intent);
						break;
					}
					else
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
						startActivity(intent);
						break;
					}

				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("EVENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("POLL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("FEES"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("DIARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("BUS ATTENDANCE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("HEALTH CARD"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("HOSTEL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("STUDY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("LIBRARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("ASSESMENT"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(4).matches("KNOWLEDGE RESOURSE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
					startActivity(intent);
					break;
				}

				else

				if(studentHomeScreenIconNamefinal.get(4).matches("VIDEO LECTURE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
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

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);

					}


				else

				if(studentHomeScreenIconNamefinal.get(5).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(5).matches("TIME TABLE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("ASSIGNMENTS/HOMEWORK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
					intent.putExtra("value","1");

					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("ANNOUNCEMENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("FEEDBACK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("EXAM SCHEDULE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("RESULT"))
				{
					Preferences.getInstance().loadPreference(getApplicationContext());
					if(Preferences.getInstance().schoolType.matches("College"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
						startActivity(intent);
						break;
					}
					else
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
						startActivity(intent);
						break;
					}

				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("EVENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("POLL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("FEES"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("DIARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("BUS ATTENDANCE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("HEALTH CARD"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("HOSTEL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("STUDY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("LIBRARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("ASSESMENT"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(5).matches("KNOWLEDGE RESOURSE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
					startActivity(intent);
					break;
				}

				else

				if(studentHomeScreenIconNamefinal.get(5).matches("VIDEO LECTURE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
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

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);


					}

				else

				if(studentHomeScreenIconNamefinal.get(6).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(6).matches("TIME TABLE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("ASSIGNMENTS/HOMEWORK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
					intent.putExtra("value","1");

					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("ANNOUNCEMENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("FEEDBACK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("EXAM SCHEDULE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("RESULT"))
				{
					Preferences.getInstance().loadPreference(getApplicationContext());
					if(Preferences.getInstance().schoolType.matches("College"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
						startActivity(intent);
						break;
					}
					else
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
						startActivity(intent);
						break;
					}

				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("EVENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("POLL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("FEES"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("DIARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("BUS ATTENDANCE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("HEALTH CARD"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("HOSTEL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("STUDY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("LIBRARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("ASSESMENT"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(6).matches("KNOWLEDGE RESOURSE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
					startActivity(intent);
					break;
				}

				else

				if(studentHomeScreenIconNamefinal.get(6).matches("VIDEO LECTURE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
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

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);

					}

				else

				if(studentHomeScreenIconNamefinal.get(7).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(7).matches("TIME TABLE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("ASSIGNMENTS/HOMEWORK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
					intent.putExtra("value","1");

					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("ANNOUNCEMENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("FEEDBACK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("EXAM SCHEDULE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("RESULT"))
				{
					Preferences.getInstance().loadPreference(getApplicationContext());
					if(Preferences.getInstance().schoolType.matches("College"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
						startActivity(intent);
						break;
					}
					else
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
						startActivity(intent);
						break;
					}

				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("EVENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("POLL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("FEES"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("DIARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("BUS ATTENDANCE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("HEALTH CARD"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("HOSTEL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("STUDY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("LIBRARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("ASSESMENT"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(7).matches("KNOWLEDGE RESOURSE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
					startActivity(intent);
					break;
				}

				else

				if(studentHomeScreenIconNamefinal.get(7).matches("VIDEO LECTURE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
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

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);



				}

				else

				if(studentHomeScreenIconNamefinal.get(8).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(8).matches("TIME TABLE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("ASSIGNMENTS/HOMEWORK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
					intent.putExtra("value","1");

					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("ANNOUNCEMENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("FEEDBACK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("EXAM SCHEDULE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("RESULT"))
				{
					Preferences.getInstance().loadPreference(getApplicationContext());
					if(Preferences.getInstance().schoolType.matches("College"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
						startActivity(intent);
						break;
					}
					else
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
						startActivity(intent);
						break;
					}

				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("EVENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("POLL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("FEES"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("DIARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("BUS ATTENDANCE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("HEALTH CARD"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("HOSTEL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("STUDY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("LIBRARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("ASSESMENT"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(8).matches("KNOWLEDGE RESOURSE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
					startActivity(intent);
					break;
				}

				else

				if(studentHomeScreenIconNamefinal.get(8).matches("VIDEO LECTURE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
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

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

					else

					if(studentHomeScreenIconNamefinal.get(9).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(9).matches("TIME TABLE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
						intent.putExtra("value","1");

						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("FEEDBACK"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("RESULT"))
					{
						Preferences.getInstance().loadPreference(getApplicationContext());
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
							startActivity(intent);
							break;
						}
						else
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
							startActivity(intent);
							break;
						}

					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("EVENTS"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("POLL"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("FEES"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("DIARY"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("HEALTH CARD"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("HOSTEL"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("STUDY PLANNER"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("LIBRARY"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("ASSESMENT"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(9).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(9).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
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

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);


					}

					else

					if(studentHomeScreenIconNamefinal.get(10).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(10).matches("TIME TABLE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
						intent.putExtra("value","1");

						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("FEEDBACK"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("RESULT"))
					{
						Preferences.getInstance().loadPreference(getApplicationContext());
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
							startActivity(intent);
							break;
						}
						else
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
							startActivity(intent);
							break;
						}

					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("EVENTS"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("POLL"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("FEES"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("DIARY"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("HEALTH CARD"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("HOSTEL"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("STUDY PLANNER"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("LIBRARY"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("ASSESMENT"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(10).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(10).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
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

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

					else

					if(studentHomeScreenIconNamefinal.get(11).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(11).matches("TIME TABLE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
						intent.putExtra("value","1");

						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("FEEDBACK"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("RESULT"))
					{
						Preferences.getInstance().loadPreference(getApplicationContext());
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
							startActivity(intent);
							break;
						}
						else
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
							startActivity(intent);
							break;
						}

					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("EVENTS"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("POLL"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("FEES"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("DIARY"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("HEALTH CARD"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("HOSTEL"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("STUDY PLANNER"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("LIBRARY"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("ASSESMENT"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(11).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(11).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
						startActivity(intent);
						break;
					}


					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;

					case 12: if(studentHomeScreenIconNamefinal.get(12).matches("ATTENDANCE"))
					{

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);


					}

					else

					if(studentHomeScreenIconNamefinal.get(12).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
						startActivity(intent);
						break;
					}
					else
					if(studentHomeScreenIconNamefinal.get(12).matches("TIME TABLE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("ASSIGNMENTS/HOMEWORK"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
						intent.putExtra("value","1");

						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("ANNOUNCEMENTS"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("FEEDBACK"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("EXAM SCHEDULE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("RESULT"))
					{
						Preferences.getInstance().loadPreference(getApplicationContext());
						if(Preferences.getInstance().schoolType.matches("College"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
							startActivity(intent);
							break;
						}
						else
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
							startActivity(intent);
							break;
						}

					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("EVENTS"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("POLL"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("FEES"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("DIARY"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("BUS ATTENDANCE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("HEALTH CARD"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("HOSTEL"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("STUDY PLANNER"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("LIBRARY"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("ASSESMENT"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
						startActivity(intent);
						break;
					}

					else
					if(studentHomeScreenIconNamefinal.get(12).matches("KNOWLEDGE RESOURSE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
						startActivity(intent);
						break;
					}

					else

					if(studentHomeScreenIconNamefinal.get(12).matches("VIDEO LECTURE"))
					{
						Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
						startActivity(intent);
						break;
					}


					else
					{
						Utils.showToast(getApplicationContext(),"No access");
					}
						break;
				


				case 13:
					if(studentHomeScreenIconNamefinal.get(13).matches("ATTENDANCE"))
					{

						Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
						startActivity(intent);


					}

				else

				if(studentHomeScreenIconNamefinal.get(13).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(13).matches("TIME TABLE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("ASSIGNMENTS/HOMEWORK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
					intent.putExtra("value","1");

					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("ANNOUNCEMENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("FEEDBACK"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("EXAM SCHEDULE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("RESULT"))
				{
					Preferences.getInstance().loadPreference(getApplicationContext());
					if(Preferences.getInstance().schoolType.matches("College"))
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
						startActivity(intent);
						break;
					}
					else
					{
						Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
						startActivity(intent);
						break;
					}

				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("EVENTS"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("POLL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("FEES"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("DIARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("BUS ATTENDANCE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("HEALTH CARD"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("HOSTEL"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("STUDY PLANNER"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("LIBRARY"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
					startActivity(intent);
					break;
				}
				else
				if(studentHomeScreenIconNamefinal.get(13).matches("ASSESMENT"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
					startActivity(intent);
					break;
				}

				else
				if(studentHomeScreenIconNamefinal.get(13).matches("KNOWLEDGE RESOURSE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
					startActivity(intent);
					break;
				}

				else

				if(studentHomeScreenIconNamefinal.get(13).matches("VIDEO LECTURE"))
				{
					Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
					startActivity(intent);
					break;
				}

				else
				{
					Utils.showToast(getApplicationContext(),"No access");
				}
				break;

					case 14:
						if(studentHomeScreenIconNamefinal.get(14).matches("ATTENDANCE"))
						{

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

						else

						if(studentHomeScreenIconNamefinal.get(14).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(14).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(14).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(14).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access");
						}
						break;


					case 15:
						if(studentHomeScreenIconNamefinal.get(15).matches("ATTENDANCE"))
						{

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

						else

						if(studentHomeScreenIconNamefinal.get(15).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(15).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(15).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(15).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access!");
						}

					case 16:
						if(studentHomeScreenIconNamefinal.get(16).matches("ATTENDANCE"))
						{

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

						else

						if(studentHomeScreenIconNamefinal.get(16).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(16).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(16).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access!");
						}

					case 17:
						if(studentHomeScreenIconNamefinal.get(17).matches("ATTENDANCE"))
						{

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

						else

						if(studentHomeScreenIconNamefinal.get(17).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(17).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(16).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(17).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(17).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access!");
						}

					case 18:
						if(studentHomeScreenIconNamefinal.get(18).matches("ATTENDANCE"))
						{

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

						else

						if(studentHomeScreenIconNamefinal.get(18).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(18).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(18).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(18).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access!");
						}


					case 19:
						if(studentHomeScreenIconNamefinal.get(19).matches("ATTENDANCE"))
						{

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

						else

						if(studentHomeScreenIconNamefinal.get(19).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(19).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AssesmentNewHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(19).matches("KNOWLEDGE RESOURSE"))
						{
							/*Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;*/

							Intent intent = new Intent(StudentHomeScreen.this, KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(19).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access!");
						}

					case 20:
						if(studentHomeScreenIconNamefinal.get(20).matches("ATTENDANCE"))
						{

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

						else

						if(studentHomeScreenIconNamefinal.get(20).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(20).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AssesmentNewHomeScreen.class);
							startActivity(intent);
							break;

							/*Intent intent = new Intent(StudentHomeScreen.this, TeacherStudentDiaryScreen.class);
							startActivity(intent);
							break;*/
						}

						else
						if(studentHomeScreenIconNamefinal.get(20).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(20).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access!");
						}

					case 21:
						if(studentHomeScreenIconNamefinal.get(21).matches("ATTENDANCE"))
						{

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

						else

						if(studentHomeScreenIconNamefinal.get(21).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(21).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(21).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(21).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access!");
						}

					case 22:
						if(studentHomeScreenIconNamefinal.get(22).matches("ATTENDANCE"))
						{

							Intent intent = new Intent(StudentHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
							startActivity(intent);


						}

						else

						if(studentHomeScreenIconNamefinal.get(22).matches("LEAVE REQUEST"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentLeaveRequest.class);
							startActivity(intent);
							break;
						}
						else
						if(studentHomeScreenIconNamefinal.get(22).matches("TIME TABLE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("ASSIGNMENTS/HOMEWORK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentNewAssignemntVerion.class);
							intent.putExtra("value","1");

							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("ANNOUNCEMENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentAnnouncement.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("FEEDBACK"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentFeedBack.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("EXAM SCHEDULE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentExamSchedule.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("RESULT"))
						{
							Preferences.getInstance().loadPreference(getApplicationContext());
							if(Preferences.getInstance().schoolType.matches("College"))
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentCollegeResults.class);
								startActivity(intent);
								break;
							}
							else
							{
								Intent intent = new Intent(StudentHomeScreen.this,StudentResult.class);
								startActivity(intent);
								break;
							}

						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("EVENTS"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentEventList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("POLL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,NewPollActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("BUS TRACKING"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentBusTrackingRouteScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("FEES"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentFees.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("DIARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,DiaryWallScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("BUS ATTENDANCE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,ParentStudentBusAttendance.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("YEARLY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("HEALTH CARD"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AdminHealthCardOptionsActivity.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("HOSTEL"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentHostelReservationDetailsScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("STUDY PLANNER"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,StudentSubjectList.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("LIBRARY"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,UserLibraryHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("ASSESMENT"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,AssesmentAllotedScreen.class);
							startActivity(intent);
							break;
						}

						else
						if(studentHomeScreenIconNamefinal.get(22).matches("KNOWLEDGE RESOURSE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this,KnowledgeResourceSubjectScreen.class);
							startActivity(intent);
							break;
						}

						else

						if(studentHomeScreenIconNamefinal.get(22).matches("VIDEO LECTURE"))
						{
							Intent intent = new Intent(StudentHomeScreen.this, VideoLectureHomeScreen.class);
							startActivity(intent);
							break;
						}

						else
						{
							Utils.showToast(getApplicationContext(),"No access!");
						}

			}

			}
		});
		







	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		finish();
	}

	/*public void onPause(){
		if(t1 !=null){
			t1.stop();
			t1.shutdown();
		}
		super.onPause();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_home_screen, menu);
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




		final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BADGE_COUNT/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//Utils.showToast(getApplicationContext(), ""+response);
				//System.out.println(url1);
				try {
					responseObject = new JSONObject(response);

					if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
						loading.dismiss();
						Utils.showToast(StudentHomeScreen.this, "Error Submitting Comment");

					} else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
						loading.dismiss();
						Utils.showToast(StudentHomeScreen.this, "Session Expired:Please Login Again");
					} else if (responseObject.has("badge")) {
						loading.dismiss();
						badgeArray = new JSONObject(response).getJSONArray("badge");
						if (null != badgeArray && badgeArray.length() >= 0) {
							Cache.Entry e = new Cache.Entry();
							e.data = badgeArray.toString().getBytes();
							VolleySingleton.getInstance(StudentHomeScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BADGE_COUNT + "?user_id=" + Preferences.getInstance().userId + "&device_id=" + Preferences.getInstance().deviceId, e);

							circularTextView.setText(badgeArray.getJSONObject(0).getString("badge"));

						}
					} else
						Utils.showToast(StudentHomeScreen.this, "Error Fetching Response");
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
				Preferences.getInstance().loadPreference(StudentHomeScreen.this);
				Map<String,String> params = new HashMap<String, String>();
				//params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
				//params.put("Students", jsonObject1.toString());

				params.put("user_id",Preferences.getInstance().userId);
				//params.put("tea_id", Preferences.getInstance().userId);
				params.put("device_id",Preferences.getInstance().deviceId);
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

	private void requestUpdate(AppUpdateInfo appUpdateInfo, int updateType) {
		try {
			appUpdateManager.startUpdateFlowForResult(appUpdateInfo, updateType, StudentHomeScreen.this, REQUEST_APP_UPDATE);
		} catch (IntentSender.SendIntentException e) {
			e.printStackTrace();
		}
	}


	private void popupSnackbarForCompleteUpdate() {
		Snackbar snackbar =
				Snackbar.make(findViewById(android.R.id.content), "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
		snackbar.setAction("Restart", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				appUpdateManager.completeUpdate();
			}
		});
		snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
		snackbar.show();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_APP_UPDATE) {
			System.out.println("App Update = " + resultCode);
			if (resultCode != RESULT_OK) {
				System.out.println("Update flow failed! Result code: " + resultCode);
				// If the update is cancelled or fails,
				// you can request to start the update again.
			}
		}
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("installState", "destroy");
		appUpdateManager.unregisterListener(listener);
	}









}
