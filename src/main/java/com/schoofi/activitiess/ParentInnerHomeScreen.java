package com.schoofi.activitiess;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activities.StudentAttendance;
import com.schoofi.activities.StudentHomeScreen;
import com.schoofi.adapters.StudentHomeScreenAdapter;
import com.schoofi.adapters.StudentHomeScreenTabletAdapter;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ParentInnerHomeScreen extends AppCompatActivity {

    GridView studentHomeScreenGridView;
    ImageView settings;
    int []studentHomeScreenIcon = {R.drawable.attendence,R.drawable.leaverequest,R.drawable.schedule,R.drawable.assignments,R.drawable.announcement,R.drawable.feedback,R.drawable.examschedule,R.drawable.result,R.drawable.events,R.drawable.poll,R.drawable.gps,R.drawable.fees,R.drawable.busattendance,R.drawable.busattendance,R.drawable.diary,R.drawable.yearlyplanner,R.drawable.busnotification,R.drawable.healthcard,R.drawable.hostel,R.drawable.studyplanner,R.drawable.library,R.drawable.knowledge_resourse,R.drawable.videolecture,R.drawable.library};
    int []studentHomeScreenIcon1 = {R.drawable.attendancetablet,R.drawable.leaverequesttablet,R.drawable.scheduletablet,R.drawable.assignmentstablet,R.drawable.announcementtablet,R.drawable.feedbacktablet,R.drawable.examscheduletablet,R.drawable.resulttablet,R.drawable.eventstablet,R.drawable.polltablet,R.drawable.gpstablet,R.drawable.feestablet,R.drawable.busattendancetablet,R.drawable.busattendancetablet,R.drawable.diarytablet,R.drawable.yearlyplannertablet,R.drawable.busnotificationtablet,R.drawable.healthcardtablet,R.drawable.hosteltablet,R.drawable.studyplannertablet,R.drawable.librarytablet,R.drawable.knowledge_resourse_tablet,R.drawable.videolecturetablet,R.drawable.library};
    String []studentHomeScreenIconName = {"ATTENDANCE","LEAVE REQUEST","TIME TABLE","ASSIGNMENTS/HOMEWORK","ANNOUNCEMENTS","FEEDBACK","EXAM SCHEDULE","RESULT","EVENTS","POLL","BUS TRACKING","FEES","Group Discussion","BUS ATTENDANCE","DIARY","YEARLY PLANNER","HEALTH CARD","HOSTEL","STUDY PLANNER","LIBRARY","KNOWLEDGE RESOURSE","VIDEO LECTURE","ASSESMENT"};
    ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
    ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
    ArrayList<Integer> studentHomeScreenIconFinal1 = new ArrayList<Integer>();
    int[] COLORS = {
            Color.rgb(102, 155, 76), Color.rgb(10, 173, 162), Color.rgb(248, 88, 129),
            Color.rgb(224, 157, 64), Color.rgb(85, 218, 160), Color.rgb(94, 186, 220), Color.rgb(0, 174, 179),
            Color.rgb(227, 72, 62),  Color.rgb(165, 104, 211), Color.rgb(251, 153, 99), Color.rgb(30,144,255),Color.rgb(46,139,87),Color.rgb(255,228,181),Color.rgb(255,171,64),Color.rgb(141,110,99),Color.rgb(0,96,100),Color.rgb(221, 44, 0),Color.rgb(173, 20, 87),Color.rgb(0, 105, 92),Color.rgb(0, 105, 92),Color.rgb(173, 20, 87),Color.rgb(227, 65, 89),Color.rgb(227, 72, 62),Color.rgb(0, 105, 92),Color.rgb(0, 105, 92)
    };
    StudentHomeScreenAdapter studentHomeScreenAdapter;
    StudentHomeScreenTabletAdapter studentHomeScreenTabletAdapter;
    TextView userName;
    private TextView tickerView;
    Button buttonGone, buttonLogout;
    String u = "";
    TextToSpeech t1;
    String permissions;
    ArrayList<String> permissionsArray= new ArrayList<String>();
    private ImageView bellNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_home_screen);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        if (diagonalInches >= 6.0) {
            // 6.5inch device or bigger
            u = "1";
        } else {
            // smaller device
            u = "2";

        }
        studentHomeScreenGridView = (GridView) findViewById(R.id.studentHomeGridView);
        userName = (TextView) findViewById(R.id.txt_userName);
        buttonGone = (Button) findViewById(R.id.btn_gone);

        buttonGone.setVisibility(View.INVISIBLE);

        settings = (ImageView) findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParentInnerHomeScreen.this, StudentSettings.class);
                startActivity(intent);
            }
        });

        permissions = Preferences.getInstance().permissions;
        permissionsArray = new ArrayList<String>(Arrays.asList(permissions.split(",")));

        for(int h =0;h<permissionsArray.size();h++)
        {
            if(permissionsArray.get(h).matches("A"))
            {

                studentHomeScreenIconNamefinal.add(studentHomeScreenIconName[h]);
                studentHomeScreenIconFinal.add(studentHomeScreenIcon[h]);
                studentHomeScreenIconFinal1.add(studentHomeScreenIcon1[h]);

            }

            else
            {
                Log.d("kk","kkk");
            }
        }



        bellNotification = (ImageView) findViewById(R.id.img_bell);

        bellNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentInnerHomeScreen.this, NotificationIntentClass.class);
                startActivity(intent);
            }
        });


        if (Preferences.getInstance().userRoleId.matches("5")) {
            userName.setText(Preferences.getInstance().Name);
            //t1.speak(Preferences.getInstance().Name, TextToSpeech.QUEUE_FLUSH, null);
        } else if (Preferences.getInstance().userRoleId.matches("6")) {
            userName.setText(Preferences.getInstance().studentName);
            //t1.speak(Preferences.getInstance().studentName, TextToSpeech.QUEUE_FLUSH, null);
        }

        if (u.matches("2")) {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenAdapter(ParentInnerHomeScreen.this, studentHomeScreenIconFinal, studentHomeScreenIconNamefinal, COLORS));
        } else {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenTabletAdapter(ParentInnerHomeScreen.this, studentHomeScreenIconFinal1, studentHomeScreenIconNamefinal, COLORS));

        }
        studentHomeScreenGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        if(studentHomeScreenIconNamefinal.get(0).matches("ATTENDANCE"))
                        {

                            Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                            startActivity(intent);


                        }

                        else

                        if(studentHomeScreenIconNamefinal.get(0).matches("LEAVE REQUEST"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("TIME TABLE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("ASSIGNMENTS/HOMEWORK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                            intent.putExtra("value","1");
                            startActivity(intent);
                            break;

                        }



                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("ANNOUNCEMENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("FEEDBACK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("EXAM SCHEDULE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("RESULT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("EVENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("POLL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("BUS TRACKING"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("FEES"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("DIARY"))
                        {
                            /*Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolDiaryMainScreen.class);
                            startActivity(intent);
                            break;*/
                            Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("BUS ATTENDANCE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("YEARLY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("HEALTH CARD"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("HOSTEL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(0).matches("STUDY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        {
                            Utils.showToast(getApplicationContext(),"No access");
                        }
                        break;




                    case 1:  if(studentHomeScreenIconNamefinal.get(1).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(1).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(1).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;

                    case 2:  if(studentHomeScreenIconNamefinal.get(2).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(2).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(2).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
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

                            Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                            startActivity(intent);


                        }

                        else

                        if(studentHomeScreenIconNamefinal.get(3).matches("LEAVE REQUEST"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("TIME TABLE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("ASSIGNMENTS/HOMEWORK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                            intent.putExtra("value","1");
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("ANNOUNCEMENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("FEEDBACK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("EXAM SCHEDULE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("RESULT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("EVENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("POLL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("BUS TRACKING"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("FEES"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("DIARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("BUS ATTENDANCE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("YEARLY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("HEALTH CARD"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("HOSTEL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(3).matches("STUDY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
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

                            Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                            startActivity(intent);


                        }

                        else

                        if(studentHomeScreenIconNamefinal.get(4).matches("LEAVE REQUEST"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("TIME TABLE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("ASSIGNMENTS/HOMEWORK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                            intent.putExtra("value","1");
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("ANNOUNCEMENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("FEEDBACK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("EXAM SCHEDULE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("RESULT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("EVENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("POLL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("BUS TRACKING"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("FEES"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("DIARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("BUS ATTENDANCE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("YEARLY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("HEALTH CARD"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("HOSTEL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(4).matches("STUDY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
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

                            Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                            startActivity(intent);


                        }

                        else

                        if(studentHomeScreenIconNamefinal.get(5).matches("LEAVE REQUEST"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("TIME TABLE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("ASSIGNMENTS/HOMEWORK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                            intent.putExtra("value","1");
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("ANNOUNCEMENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("FEEDBACK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("EXAM SCHEDULE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("RESULT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("EVENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("POLL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("BUS TRACKING"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("FEES"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("DIARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("BUS ATTENDANCE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("YEARLY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("HEALTH CARD"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("HOSTEL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(5).matches("STUDY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
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

                            Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                            startActivity(intent);


                        }

                        else

                        if(studentHomeScreenIconNamefinal.get(6).matches("LEAVE REQUEST"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("TIME TABLE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("ASSIGNMENTS/HOMEWORK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                            intent.putExtra("value","1");
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("ANNOUNCEMENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("FEEDBACK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("EXAM SCHEDULE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("RESULT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("EVENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("POLL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("BUS TRACKING"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("FEES"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("DIARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("BUS ATTENDANCE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("YEARLY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("HEALTH CARD"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("HOSTEL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(6).matches("STUDY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                            startActivity(intent);
                            break;
                        }
                        break;
                    case 7:
                        if(studentHomeScreenIconNamefinal.get(7).matches("ATTENDANCE"))
                        {

                            Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                            startActivity(intent);


                        }

                        else

                        if(studentHomeScreenIconNamefinal.get(7).matches("LEAVE REQUEST"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("TIME TABLE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("ASSIGNMENTS/HOMEWORK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                            intent.putExtra("value","1");
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("ANNOUNCEMENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("FEEDBACK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("EXAM SCHEDULE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("RESULT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("EVENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("POLL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("BUS TRACKING"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("FEES"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("DIARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("BUS ATTENDANCE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("YEARLY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("HEALTH CARD"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("HOSTEL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(7).matches("STUDY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
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

                            Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                            startActivity(intent);


                        }

                        else

                        if(studentHomeScreenIconNamefinal.get(8).matches("LEAVE REQUEST"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("TIME TABLE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("ASSIGNMENTS/HOMEWORK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                            intent.putExtra("value","1");
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("ANNOUNCEMENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("FEEDBACK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("EXAM SCHEDULE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("RESULT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("EVENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("POLL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("BUS TRACKING"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("FEES"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("DIARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("BUS ATTENDANCE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("YEARLY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("HEALTH CARD"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("HOSTEL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                            startActivity(intent);
                            break;
                        }


                        else
                        if(studentHomeScreenIconNamefinal.get(8).matches("STUDY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
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

                            Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                            startActivity(intent);


                        }

                        else

                        if(studentHomeScreenIconNamefinal.get(9).matches("LEAVE REQUEST"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("TIME TABLE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("ASSIGNMENTS/HOMEWORK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                            intent.putExtra("value","1");
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("ANNOUNCEMENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("FEEDBACK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("EXAM SCHEDULE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("RESULT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("EVENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("POLL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("BUS TRACKING"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("FEES"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("DIARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("BUS ATTENDANCE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("YEARLY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("HEALTH CARD"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("HOSTEL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(9).matches("STUDY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                            startActivity(intent);
                            break;
                        }


                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        {
                            Utils.showToast(getApplicationContext(),"No access");
                        }
                        break;
                    case 10:  if(studentHomeScreenIconNamefinal.get(10).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(10).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(10).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
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

                            Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                            startActivity(intent);


                        }

                        else

                        if(studentHomeScreenIconNamefinal.get(11).matches("LEAVE REQUEST"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("TIME TABLE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("ASSIGNMENTS/HOMEWORK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                            intent.putExtra("value","1");
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("ANNOUNCEMENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("FEEDBACK"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("EXAM SCHEDULE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("RESULT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("EVENTS"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("POLL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("BUS TRACKING"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("FEES"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("DIARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("BUS ATTENDANCE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("YEARLY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("HEALTH CARD"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("HOSTEL"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(11).matches("STUDY PLANNER"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                        {
                            Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                            startActivity(intent);
                            break;
                        }

                        else
                        {
                            Utils.showToast(getApplicationContext(),"No access");
                        }
                        break;

                    case 12:  if(studentHomeScreenIconNamefinal.get(12).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(12).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }



                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(12).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;



                case 13:  if(studentHomeScreenIconNamefinal.get(13).matches("ATTENDANCE"))
                {

                    Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                    startActivity(intent);


                }

                else

                if(studentHomeScreenIconNamefinal.get(13).matches("LEAVE REQUEST"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                    startActivity(intent);
                    break;
                }
                else
                if(studentHomeScreenIconNamefinal.get(13).matches("TIME TABLE"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("ASSIGNMENTS/HOMEWORK"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                    intent.putExtra("value","1");
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("ANNOUNCEMENTS"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("FEEDBACK"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("EXAM SCHEDULE"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("RESULT"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("EVENTS"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("POLL"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("BUS TRACKING"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("FEES"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("DIARY"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("BUS ATTENDANCE"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("YEARLY PLANNER"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("HEALTH CARD"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("HOSTEL"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(13).matches("STUDY PLANNER"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                    startActivity(intent);
                    break;
                }

                else
                if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                {
                    Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                    startActivity(intent);
                    break;
                }

                else
                {
                    Utils.showToast(getApplicationContext(),"No access");
                }
                break;

                    case 14:  if(studentHomeScreenIconNamefinal.get(14).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(14).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(14).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;


                    case 15:  if(studentHomeScreenIconNamefinal.get(15).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(15).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(15).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;

                    case 16:  if(studentHomeScreenIconNamefinal.get(16).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(16).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(16).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;

                    case 17:  if(studentHomeScreenIconNamefinal.get(17).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(17).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(17).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;

                    case 18:  if(studentHomeScreenIconNamefinal.get(18).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(18).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(18).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;

                    case 19:  if(studentHomeScreenIconNamefinal.get(19).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(19).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(19).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;

                    case 20:  if(studentHomeScreenIconNamefinal.get(20).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(20).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(20).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;


                    case 21:  if(studentHomeScreenIconNamefinal.get(21).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(21).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(21).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"No access");
                    }
                        break;


                    case 22:  if(studentHomeScreenIconNamefinal.get(22).matches("ATTENDANCE"))
                    {

                        Intent intent = new Intent(ParentInnerHomeScreen.this, StudentAttendancePrimaryOptionScreen.class);
                        startActivity(intent);


                    }

                    else

                    if(studentHomeScreenIconNamefinal.get(22).matches("LEAVE REQUEST"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentLeaveRequest.class);
                        startActivity(intent);
                        break;
                    }
                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("TIME TABLE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("ASSIGNMENTS/HOMEWORK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentNewAssignemntVerion.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("ANNOUNCEMENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentAnnouncement.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("FEEDBACK"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentFeedBack.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("EXAM SCHEDULE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentExamSchedule.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("RESULT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentResult.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("EVENTS"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentEventList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("POLL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,NewPollActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("BUS TRACKING"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentBusTrackingRouteScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("FEES"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentFees.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("DIARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,DiaryWallScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("BUS ATTENDANCE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,ParentStudentBusAttendance.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("YEARLY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,SchoolPlannerMakingFirstScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("HEALTH CARD"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AdminHealthCardOptionsActivity.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("HOSTEL"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentHostelReservationDetailsScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(22).matches("STUDY PLANNER"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,StudentSubjectList.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("LIBRARY"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,UserLibraryHomeScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("ASSESMENT"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,AssesmentAllotedScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("KNOWLEDGE RESOURSE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,KnowledgeResourceSubjectScreen.class);
                        startActivity(intent);
                        break;
                    }

                    else
                    if(studentHomeScreenIconNamefinal.get(position).matches("VIDEO LECTURE"))
                    {
                        Intent intent = new Intent(ParentInnerHomeScreen.this,VideoLectureHomeScreen.class);
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
}
