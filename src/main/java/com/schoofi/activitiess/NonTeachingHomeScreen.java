package com.schoofi.activitiess;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.adapters.StudentHomeScreenAdapter;
import com.schoofi.adapters.StudentHomeScreenTabletAdapter;
import com.schoofi.utils.Preferences;

import java.util.ArrayList;

public class NonTeachingHomeScreen extends AppCompatActivity {

    GridView studentHomeScreenGridView;
    ImageView settings;
    //int []studentHomeScreenIcon = {R.drawable.events,R.drawable.announcement,R.drawable.gps,R.drawable.attendence,R.drawable.classlist,R.drawable.classattendanceanalysis,R.drawable.poll,R.drawable.busattendance,R.drawable.yearlyplanner,R.drawable.busnotification,R.drawable.assignments,R.drawable.healthcard,R.drawable.busboarding,R.drawable.buspickup,R.drawable.visitor,R.drawable.visitor,R.drawable.visitor};
    //int []studentHomeScreenIcon1 = {R.drawable.eventstablet,R.drawable.announcementtablet,R.drawable.gpstablet,R.drawable.attendancetablet,R.drawable.classlisttablet,R.drawable.classattendanceanalysistablet,R.drawable.polltablet,R.drawable.busattendancetablet,R.drawable.yearlyplannertablet,R.drawable.busnotificationtablet,R.drawable.assignmentstablet,R.drawable.healthcardtablet,R.drawable.busboardingtablet,R.drawable.buspickuptablet,R.drawable.visitortablet,R.drawable.visitortablet,R.drawable.visitortablet};
    //String []studentHomeScreenIconName = {"EVENTS","ANNOUNCEMENTS/NOTICES","BUS TRACKING","ATTENDANCE","CLASS LIST","ATTENDANCE ANALYSIS","POLLS","BUS ATTENDANCE","YEARLY PLANNER","BUS NOTIFICATIONS","ASSIGNMENTS/HOMEWORK ANALYSIS","HEALTH CARD","BUS BOARDING","BUS PICKUP","VISITOR MANAGEMENT","VISITOR ANALYSIS","TEACHER DIRECTORY"};
    String []studentHomeScreenIconName= {"ANNOUNCEMENTS/NOTICES","ATTENDANCE","LEAVE REQUEST","BUS ATTENDANCE","BUS NOTIFICATIONS","FEEDBACK","YEARLY PLANNER","EVENTS","POLLS"};
    int []studentHomeScreenIcon = {R.drawable.announcement,R.drawable.attendence,R.drawable.leaverequest,R.drawable.busattendance,R.drawable.busnotification,R.drawable.feedback,R.drawable.yearlyplanner,R.drawable.events,R.drawable.poll};
    int []studentHomeScreenIcon1= {R.drawable.announcementtablet,R.drawable.attendancetablet,R.drawable.leaverequesttablet,R.drawable.busattendancetablet,R.drawable.busnotificationtablet,R.drawable.feedbacktablet,R.drawable.yearlyplannertablet,R.drawable.eventstablet,R.drawable.polltablet};

    int[] COLORS = {
            Color.rgb(102, 155, 76), Color.rgb(10, 173, 162), Color.rgb(248, 88, 129),
            Color.rgb(224, 157, 64), Color.rgb(85, 218, 160), Color.rgb(94, 186, 220), Color.rgb(0, 174, 179),
            Color.rgb(227, 72, 62), Color.rgb(165, 104, 211), Color.rgb(251, 153, 99), Color.rgb(255,228,181),Color.rgb(255,171,64),Color.rgb(141,110,99),Color.rgb(0,96,100),Color.rgb(221, 44, 0),Color.rgb(173, 20, 87),Color.rgb(205, 133, 63),Color.rgb(0, 0, 102),Color.rgb(153, 102, 0),Color.rgb(0, 105, 92),Color.rgb(0, 105, 92) ,Color.rgb(0, 105, 92)
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
        setContentView(R.layout.activity_non_teaching_home_screen);
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







        studentHomeScreenGridView = (GridView) findViewById(R.id.studentHomeGridView);
        userName = (TextView) findViewById(R.id.txt_userName);
        buttonGone = (Button) findViewById(R.id.btn_gone);

        buttonGone.setVisibility(View.INVISIBLE);

        settings = (ImageView) findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NonTeachingHomeScreen.this,StudentSettings.class);
                startActivity(intent);
            }
        });

        userName.setText(Preferences.getInstance().Name);

        if(u.matches("2"))
        {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenAdapter(NonTeachingHomeScreen.this, studentHomeScreenIconFinal,studentHomeScreenIconNamefinal,COLORS));
        }

        else
        {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenTabletAdapter(NonTeachingHomeScreen.this, studentHomeScreenIconFinal1,studentHomeScreenIconNamefinal,COLORS));

        }

        studentHomeScreenGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {


                    case 0:
                        Intent intent6 = new Intent(NonTeachingHomeScreen.this,TeacherAnnouncement.class);
                        startActivity(intent6);
                        break;




                    case 1:
                        Preferences.getInstance().loadPreference(getApplicationContext());

                        Intent intent = new Intent(NonTeachingHomeScreen.this,EmployeeAttendanceOptionScreen.class);
                        startActivity(intent);
                        break;

                    case 2:
                        Intent intent2 = new Intent(NonTeachingHomeScreen.this,TeacherLeaveNewPrimaryScreen.class);
                        //intent5.putExtra("value3","0");
                        startActivity(intent2);
                        break;


                    case 3:
                        Intent intent9 = new Intent(NonTeachingHomeScreen.this,TeacherStudentBusAttendancePrimaryScreen.class);
                        //intent5.putExtra("value3","0");
                        startActivity(intent9);
                        break;




                    case 4:
                        Intent intent11 = new Intent(NonTeachingHomeScreen.this, BusAdminBusList.class);
                        intent11.putExtra("value","2");
                        startActivity(intent11);
                        break;

                    case 5:
                        Intent intent27 = new Intent(NonTeachingHomeScreen.this, TeacherStudentFeedback.class);

                        startActivity(intent27);
                        break;

                    case 6:
                        Intent intent10 = new Intent(NonTeachingHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        //intent5.putExtra("value3","0");
                        startActivity(intent10);
                        break;

                    case 7:
                        Intent intent22 = new Intent(NonTeachingHomeScreen.this,TeacherEventList.class);

                        startActivity(intent22);
                        break;

                    case 8:

                        Intent intent5 = new Intent(NonTeachingHomeScreen.this,NewPollActivity.class);
                        intent5.putExtra("value3","0");
                        startActivity(intent5);
                        break;































                }

            }
        });
    }
}
