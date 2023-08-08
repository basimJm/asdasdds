package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.schoofi.activities.StudentAttendance;
import com.schoofi.activities.StudentHomeScreen;
import com.schoofi.utils.Preferences;

public class StudentAttendancePrimaryOptionScreen extends AppCompatActivity {

    private ImageView back;
    private Button classAttendance,biometricAttendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.student_attendance_primary_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        biometricAttendance = (Button) findViewById(R.id.btn_teacher_leaves);
        classAttendance = (Button) findViewById(R.id.btn_student_leaves);
        Preferences.getInstance().loadPreference(getApplicationContext());

        classAttendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Preferences.getInstance().schoolType.matches("College"))
                {
                    Intent intent = new Intent(StudentAttendancePrimaryOptionScreen.this, StudentCollegeAttendancePrimaryScreen.class);
                    startActivity(intent);

                }
                else {
                    Intent intent = new Intent(StudentAttendancePrimaryOptionScreen.this, StudentAttendance.class);
                    startActivity(intent);

                }
            }
        });

        biometricAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StudentAttendancePrimaryOptionScreen.this,BiometricAttendanceStudentScreen.class);
                startActivity(intent);

            }
        });


    }
}
