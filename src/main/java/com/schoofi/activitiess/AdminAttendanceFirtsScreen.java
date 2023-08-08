package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.schoofi.utils.Preferences;

public class AdminAttendanceFirtsScreen extends AppCompatActivity {

    private Button teacherLeaves, yourLeaves, studentLeaves;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_attendance_firts_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        teacherLeaves = (Button) findViewById(R.id.btn_teacher_leaves);

        studentLeaves = (Button) findViewById(R.id.btn_student_leaves);

        studentLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.getInstance().loadPreference(getApplicationContext());
                Log.d("op",Preferences.getInstance().schoolType);
                if(Preferences.getInstance().schoolType.matches("College"))
                {

                    Intent intent = new Intent(AdminAttendanceFirtsScreen.this, TeacherClassListCollegeAttendance.class);
                    intent.putExtra("value","1");
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(AdminAttendanceFirtsScreen.this, TeacherStudentAttendance.class);
                    startActivity(intent);
                }
            }
        });


        teacherLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminAttendanceFirtsScreen.this, AdminEmployeeAtttendanceFirstScreen.class);
                intent.putExtra("value",0);
                startActivity(intent);

            }
        });
    }
}
