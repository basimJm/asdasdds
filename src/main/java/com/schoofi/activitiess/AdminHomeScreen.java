package com.schoofi.activitiess;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.StudentHomeScreenAdapter;
import com.schoofi.adapters.StudentHomeScreenTabletAdapter;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

import java.util.ArrayList;

import okhttp3.internal.Util;

public class AdminHomeScreen extends AppCompatActivity {

    GridView studentHomeScreenGridView;
    ImageView settings;
    //int []studentHomeScreenIcon = {R.drawable.events,R.drawable.announcement,R.drawable.gps,R.drawable.attendence,R.drawable.classlist,R.drawable.classattendanceanalysis,R.drawable.poll,R.drawable.busattendance,R.drawable.yearlyplanner,R.drawable.busnotification,R.drawable.assignments,R.drawable.healthcard,R.drawable.busboarding,R.drawable.buspickup,R.drawable.visitor,R.drawable.visitor,R.drawable.visitor};
    //int []studentHomeScreenIcon1 = {R.drawable.eventstablet,R.drawable.announcementtablet,R.drawable.gpstablet,R.drawable.attendancetablet,R.drawable.classlisttablet,R.drawable.classattendanceanalysistablet,R.drawable.polltablet,R.drawable.busattendancetablet,R.drawable.yearlyplannertablet,R.drawable.busnotificationtablet,R.drawable.assignmentstablet,R.drawable.healthcardtablet,R.drawable.busboardingtablet,R.drawable.buspickuptablet,R.drawable.visitortablet,R.drawable.visitortablet,R.drawable.visitortablet};
    //String []studentHomeScreenIconName = {"EVENTS","ANNOUNCEMENTS/NOTICES","BUS TRACKING","ATTENDANCE","CLASS LIST","ATTENDANCE ANALYSIS","POLLS","BUS ATTENDANCE","YEARLY PLANNER","BUS NOTIFICATIONS","ASSIGNMENTS/HOMEWORK ANALYSIS","HEALTH CARD","BUS BOARDING","BUS PICKUP","VISITOR MANAGEMENT","VISITOR ANALYSIS","TEACHER DIRECTORY"};
    String []studentHomeScreenIconName= {"ANALYSIS","ANNOUNCEMENTS/NOTICES","ATTENDANCE","BUS ATTENDANCE","BUS TRACKING","BUS NOTIFICATIONS","FEEDBACK","YEARLY PLANNER","EVENTS","CLASS LIST","HEALTH CARD","EMPLOYEE DIRECTORY","POLLS","VISITOR MANAGEMENT","VISITOR ANALYSIS","ACCESS LOGS","STUDENT BOARDING","STUDENT PICKUP","STUDY PLANNER","LIBRARY","KNOWLEDGE RESOURCE"};
    int []studentHomeScreenIcon = {R.drawable.analysis,R.drawable.announcement,R.drawable.attendence,R.drawable.busattendance,R.drawable.gps,R.drawable.busnotification,R.drawable.feedback,R.drawable.yearlyplanner,R.drawable.events,R.drawable.classlist,R.drawable.healthcard,R.drawable.teacherdirectory,R.drawable.poll,R.drawable.visitor,R.drawable.vistoranalysis,R.drawable.turnstilelog,R.drawable.busboarding,R.drawable.buspickup,R.drawable.studyplanner,R.drawable.library,R.drawable.knowledge_resourse};
    int []studentHomeScreenIcon1= {R.drawable.analysistablet,R.drawable.announcementtablet,R.drawable.attendancetablet,R.drawable.busattendancetablet,R.drawable.gpstablet,R.drawable.busnotificationtablet,R.drawable.feedbacktablet,R.drawable.yearlyplannertablet,R.drawable.eventstablet,R.drawable.classlisttablet,R.drawable.healthcardtablet,R.drawable.teacherdirectorytablet,R.drawable.polltablet,R.drawable.visitortablet,R.drawable.vistoranalysistablet,R.drawable.turnstilelogtablet,R.drawable.busboardingtablet,R.drawable.buspickuptablet,R.drawable.studyplannertablet,R.drawable.librarytablet,R.drawable.knowledge_resourse_tablet};

    int[] COLORS = {
            Color.rgb(102, 155, 76), Color.rgb(10, 173, 162), Color.rgb(248, 88, 129),
            Color.rgb(224, 157, 64), Color.rgb(85, 218, 160), Color.rgb(94, 186, 220), Color.rgb(0, 174, 179),
            Color.rgb(227, 72, 62), Color.rgb(165, 104, 211), Color.rgb(251, 153, 99), Color.rgb(255,228,181),Color.rgb(255,171,64),Color.rgb(141,110,99),Color.rgb(0,96,100),Color.rgb(221, 44, 0),Color.rgb(173, 20, 87),Color.rgb(205, 133, 63),Color.rgb(0, 0, 102),Color.rgb(153, 102, 0),Color.rgb(0, 105, 92),Color.rgb(0, 105, 92) ,Color.rgb(0, 105, 92),Color.rgb(0, 105, 92)
    };
    ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
    ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
    ArrayList<Integer> studentHomeScreenIconFinal1 = new ArrayList<Integer>();
    StudentHomeScreenAdapter studentHomeScreenAdapter;
    StudentHomeScreenTabletAdapter studentHomeScreenTabletAdapter;
    TextView userName;
    private TextView tickerView;
    Button buttonGone,buttonLogout;
    String u = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admin Home Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_student_home_screen);

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

        for(int i=0;i<studentHomeScreenIcon.length;i++)
        {
            studentHomeScreenIconFinal.add(studentHomeScreenIcon[i]);
            studentHomeScreenIconFinal1.add(studentHomeScreenIcon1[i]);
            studentHomeScreenIconNamefinal.add(studentHomeScreenIconName[i]);
        }


            studentHomeScreenIconFinal.add(R.drawable.healthandsafety);
            studentHomeScreenIconFinal1.add(R.drawable.healtandsafetytablet);
            studentHomeScreenIconNamefinal.add("HEALTH AND SAFETY");
            studentHomeScreenIconFinal.add(R.drawable.healthandsafety);
            studentHomeScreenIconFinal1.add(R.drawable.healtandsafetytablet);
            studentHomeScreenIconNamefinal.add("AUDIT USER");




        studentHomeScreenGridView = (GridView) findViewById(R.id.studentHomeGridView);
        userName = (TextView) findViewById(R.id.txt_userName);
        buttonGone = (Button) findViewById(R.id.btn_gone);

        buttonGone.setVisibility(View.INVISIBLE);

        settings = (ImageView) findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeScreen.this,StudentSettings.class);
                startActivity(intent);
            }
        });

        userName.setText(Preferences.getInstance().Name);

        if(u.matches("2"))
        {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenAdapter(AdminHomeScreen.this, studentHomeScreenIconFinal,studentHomeScreenIconNamefinal,COLORS));
        }

        else
        {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenTabletAdapter(AdminHomeScreen.this, studentHomeScreenIconFinal1,studentHomeScreenIconNamefinal,COLORS));

        }

        studentHomeScreenGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0: Intent intent = new Intent(AdminHomeScreen.this,TeacherCoOrdinatorHomeScreen.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent6 = new Intent(AdminHomeScreen.this,TeacherAnnouncement.class);
                        startActivity(intent6);
                        break;




                    case 2:
                        Preferences.getInstance().loadPreference(getApplicationContext());

                        Intent intent4 = new Intent(AdminHomeScreen.this,AdminAttendanceFirtsScreen.class);
                        //intent5.putExtra("value3","0");
                        startActivity(intent4);
                        break;


                    case 3:
                        Intent intent9 = new Intent(AdminHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
                        //intent5.putExtra("value3","0");
                        startActivity(intent9);
                        break;

                    case 4:
                        Intent intent3 = new Intent(AdminHomeScreen.this,BusAdminBusList.class);
                        intent3.putExtra("value","1");
                        startActivity(intent3);
                        break;


                    case 5:
                        Intent intent11 = new Intent(AdminHomeScreen.this, BusAdminBusList.class);
                        intent11.putExtra("value","2");
                        startActivity(intent11);
                        break;

                    case 6:
                        Intent intent27 = new Intent(AdminHomeScreen.this, AdminFeedback.class);

                        startActivity(intent27);
                        break;

                    case 7:
                        Intent intent10 = new Intent(AdminHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        Preferences.getInstance().loadPreference(getApplicationContext());
                        Preferences.getInstance().studyPlanner = "yearlyPlanner";
                        Preferences.getInstance().savePreference(getApplicationContext());
                        //intent5.putExtra("value3","0");
                        startActivity(intent10);
                        break;

                    case 8:
                        Intent intent2 = new Intent(AdminHomeScreen.this,TeacherEventList.class);

                        startActivity(intent2);
                        break;

                    case 9:

                        Intent intent5 = new Intent(AdminHomeScreen.this,AdminClassListScreen.class);
                        intent5.putExtra("value3","0");
                        startActivity(intent5);
                        break;

                    case 10:
                        Intent intent15 = new Intent(AdminHomeScreen.this, AdminHealthCard.class);
                        startActivity(intent15);
                        break;

                    case 11:

                        Intent intent20 = new Intent(AdminHomeScreen.this, AdminEmployeeList.class);
                        startActivity(intent20);
                        break;


                    case 12:

                        Intent intent8 = new Intent(AdminHomeScreen.this,NewPollActivity.class);
                        //intent5.putExtra("value3","0");
                        startActivity(intent8);
                        break;



                    case 13:
                        Intent intent18 = new Intent(AdminHomeScreen.this, SecurityVisitorListScreen.class);
                        intent18.putExtra("value","0");
                        startActivity(intent18);
                        break;

                    case 14:
                        Intent intent19 = new Intent(AdminHomeScreen.this, AdminVisitorLogs.class);
                        startActivity(intent19);
                        break;


                    case 15:
                        Intent intent23 = new Intent(AdminHomeScreen.this, TurnstileLogsMainScreen.class);
                        startActivity(intent23);
                        break;


                    case 16:
                        Intent intent16 = new Intent(AdminHomeScreen.this, AdminStudentBusBoardingScreen.class);
                        intent16.putExtra("value","0");
                        startActivity(intent16);
                        break;

                    case 17:
                        Intent intent17 = new Intent(AdminHomeScreen.this, AdminStudentDropFormNewActivity.class);
                        intent17.putExtra("value","0");
                        startActivity(intent17);
                        break;

                    case 18:
                        if(Preferences.getInstance().schoolType.matches("College"))
                        {
                            Intent intent24 = new Intent(AdminHomeScreen.this, StudyPlannerClassList.class);

                            startActivity(intent24);
                            Preferences.getInstance().loadPreference(getApplicationContext());
                            Preferences.getInstance().studyPlanner = "studyPlanner";
                            Preferences.getInstance().savePreference(getApplicationContext());
                            //Utils.showToast(getApplicationContext(),"Coming Soon");
                            break;
                        }
                        else {
                            Intent intent24 = new Intent(AdminHomeScreen.this, StudyPlannerClassListScreen.class);

                            startActivity(intent24);
                            //Utils.showToast(getApplicationContext(),"Coming Soon");
                            Preferences.getInstance().loadPreference(getApplicationContext());
                            Preferences.getInstance().studyPlanner = "studyPlanner";
                            Preferences.getInstance().savePreference(getApplicationContext());
                            break;
                        }

                    case 19:
                        Intent intent25 = new Intent(AdminHomeScreen.this, UserLibraryHomeScreen.class);
                        startActivity(intent25);
                        break;

                    case 20:
//                        Intent intent26 = new Intent(AdminHomeScreen.this, KnowledgeResourseClassListScreen.class);
//                        startActivity(intent26);
//                        break;

                        Utils.showToast(getApplicationContext(),"Coming Soon!!");
break;

                    case 21:
                        Intent intent21 = new Intent(AdminHomeScreen.this, HealthAndAuditHomeScreen.class);
                        startActivity(intent21);
                        break;

                    case 22:
                        Intent intent22 = new Intent(AdminHomeScreen.this, AuditUserHomeScreen.class);
                        startActivity(intent22);
                        break;






                }

            }
        });
    }
}
