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
import com.schoofi.utils.TeacherHomeScreenVO;

import java.util.ArrayList;

public class UserLibraryHomeScreen extends AppCompatActivity {

    public ArrayList<TeacherHomeScreenVO> temparr;
    //TeacherHomeScreenAdapter teacherHomeScreenAdapter;
    private ListView healthAndAuditListView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_user_library_home_screen);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        temparr=new ArrayList<TeacherHomeScreenVO>();
        TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Book Finder", "1");
        TeacherHomeScreenVO tecHomeScreenVO1 = new TeacherHomeScreenVO("Transaction History", "2");


        temparr.add(tecHomeScreenVO);
        temparr.add(tecHomeScreenVO1);


        healthAndAuditListView = (ListView) findViewById(R.id.teacher_homescreen_listview);
        HealthAndAuditAdapter healthAndAuditAdapter = new HealthAndAuditAdapter(getApplicationContext(),temparr);
        healthAndAuditListView.setAdapter(healthAndAuditAdapter);
        healthAndAuditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (temparr.get(position).getFieldName().matches("Book Finder")) {
                    Intent intent = new Intent(UserLibraryHomeScreen.this, StudentLibraryBookFinder.class);
                    startActivity(intent);

                } else if (temparr.get(position).getFieldName().matches("Transaction History")) {
                    Intent intent = new Intent(UserLibraryHomeScreen.this, UserLibraryHistory.class);
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

