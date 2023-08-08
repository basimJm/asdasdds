package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.schoofi.adapters.HealthAndAuditAdapter;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.TeacherHomeScreenVO;

import java.util.ArrayList;


public class TeacherCoOrdinatorHomeScreen extends AppCompatActivity {

    private ImageView back;
    private ListView cordinatorListview;
    public ArrayList<TeacherHomeScreenVO> temparr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_teacher_co_ordinator_home_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        cordinatorListview = (ListView) findViewById(R.id.listview_analysis);
        temparr=new ArrayList<TeacherHomeScreenVO>();
        TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Attendance", "1");
        TeacherHomeScreenVO tecHomeScreenVO1 = new TeacherHomeScreenVO("Bus-Attendance", "2");
        TeacherHomeScreenVO tecHomeScreenVO2 = new TeacherHomeScreenVO("Assignment", "3");
        TeacherHomeScreenVO tecHomeScreenVO3 = new TeacherHomeScreenVO("Fee-Defaulters", "4");
        TeacherHomeScreenVO tecHomeScreenVO4 = new TeacherHomeScreenVO("Pending Leaves", "5");
        TeacherHomeScreenVO teacherHomeScreenVO5 = new TeacherHomeScreenVO("Student Analysis","6");

        Preferences.getInstance().loadPreference(getApplicationContext());
        if(Preferences.getInstance().userRoleId.matches("3"))
        {
            temparr.add(teacherHomeScreenVO5);
        }
        else
        {

        }
        temparr.add(tecHomeScreenVO);
        temparr.add(tecHomeScreenVO1);
        temparr.add(tecHomeScreenVO2);
        temparr.add(tecHomeScreenVO3);
        temparr.add(tecHomeScreenVO4);
        HealthAndAuditAdapter healthAndAuditAdapter = new HealthAndAuditAdapter(getApplicationContext(),temparr);
        cordinatorListview.setAdapter(healthAndAuditAdapter);
        cordinatorListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if(temparr.get(position).getFieldName().matches("Attendance"))
                {
                    Preferences.getInstance().loadPreference(getApplicationContext());
                    if(Preferences.getInstance().userRoleId.matches("3"))
                    {
                        Intent intent = new Intent(TeacherCoOrdinatorHomeScreen.this,ChairmanAttendanceDetailsScreen.class);
                        startActivity(intent
                        );
                    }

                    else {
                        Intent intent = new Intent(TeacherCoOrdinatorHomeScreen.this, TeacherCordinatorAttendanceAnalysisScreen.class);
                        startActivity(intent);
                    }

                }

                if(temparr.get(position).getFieldName().matches("Bus-Attendance"))
                {
                    Intent intent = new Intent(TeacherCoOrdinatorHomeScreen.this,TeacherCoordinaterBusAttendanceAnalysisMainScreen.class);
                    startActivity(intent);

                }

                if(temparr.get(position).getFieldName().matches("Pending Leaves"))
                {
                    Intent intent = new Intent(TeacherCoOrdinatorHomeScreen.this,TeacherCoordinatorPendingLeavesAnalysis.class);
                    startActivity(intent);

                }

                if(temparr.get(position).getFieldName().matches("Fee-Defaulters"))
                {
                    Intent intent = new Intent(TeacherCoOrdinatorHomeScreen.this,TeacherCoordinatorPendingFeesClassWise.class);
                    startActivity(intent);

                }

                if(temparr.get(position).getFieldName().matches("Assignment"))
                {
                    if(Preferences.getInstance().userRoleId.matches("3"))
                    {
                        Intent intent = new Intent(TeacherCoOrdinatorHomeScreen.this, AdminAssignmentAnalysis.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(TeacherCoOrdinatorHomeScreen.this, TeacherCoordinaterAssignmentAnalysisHomeScreen.class);
                        startActivity(intent);
                    }

                }

                if(temparr.get(position).getFieldName().matches("Student Analysis"))
                {
                    Intent intent = new Intent(TeacherCoOrdinatorHomeScreen.this,StudentAnalysis.class);
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
