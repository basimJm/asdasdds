package com.schoofi.activitiess;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.HealthAndAuditAdapter;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TeacherHomeScreenVO;
import com.schoofi.utils.TrackerName;

import java.util.ArrayList;

public class AuditUserHomeScreen extends AppCompatActivity {

    public ArrayList<TeacherHomeScreenVO> temparr;
    //TeacherHomeScreenAdapter teacherHomeScreenAdapter;
    private ListView healthAndAuditListView;
    ImageView back;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Audit User HomeScreen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_audit_user_home_screen);
        back = (ImageView) findViewById(R.id.img_back);
        logout = (TextView) findViewById(R.id.txt_teacherLogout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        temparr=new ArrayList<TeacherHomeScreenVO>();

        TeacherHomeScreenVO teacherHomeScreenVO = new TeacherHomeScreenVO("Audit Completed Tasks","1");
        TeacherHomeScreenVO teacherHomeScreenVO1 = new TeacherHomeScreenVO("Audit Pending Tasks","2");

        temparr.add(teacherHomeScreenVO);
        temparr.add(teacherHomeScreenVO1);

        Preferences.getInstance().loadPreference(getApplicationContext());
        if(Preferences.getInstance().userRoleId.matches("0"))
        {
            logout.setVisibility(View.VISIBLE);
        }

        else
        {
            logout.setVisibility(View.INVISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuditUserHomeScreen.this, LoginScreen.class);
                Preferences.getInstance().isLoggedIn = false;
                startActivity(intent);
                finish();
            }
        });

        healthAndAuditListView = (ListView) findViewById(R.id.teacher_homescreen_listview);
        HealthAndAuditAdapter healthAndAuditAdapter = new HealthAndAuditAdapter(getApplicationContext(),temparr);
        healthAndAuditListView.setAdapter(healthAndAuditAdapter);
        healthAndAuditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if(temparr.get(position).getFieldName().matches("Audit Completed Tasks"))
                {
                    Intent intent = new Intent(AuditUserHomeScreen.this,HealthAndAuditUserTaskList.class);
                    intent.putExtra("value","1");
                    startActivity(intent);

                }

                else
                if(temparr.get(position).getFieldName().matches("Audit Pending Tasks"))
                {
                    Intent intent = new Intent(AuditUserHomeScreen.this,HealthAndAuditUserTaskList.class);
                    intent.putExtra("value","2");
                    startActivity(intent);

                }

            }
        });

    }
}
