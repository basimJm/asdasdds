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

public class EmployeeAttendanceOptionScreen extends AppCompatActivity {

    private Button teacherLeaves, yourLeaves, studentLeaves,checkInCheckOut;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_employee_attendance_option_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        teacherLeaves = (Button) findViewById(R.id.btn_teacher_leaves);

        studentLeaves = (Button) findViewById(R.id.btn_student_leaves);
        checkInCheckOut = (Button) findViewById(R.id.btn_check_in);

        studentLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.getInstance().loadPreference(getApplicationContext());
                Log.d("op",Preferences.getInstance().schoolType);
                if(Preferences.getInstance().schoolType.matches("College"))
                {
                    Intent intent = new Intent(EmployeeAttendanceOptionScreen.this, TeacherClassListCollegeAttendance.class);
                    intent.putExtra("value","1");
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(EmployeeAttendanceOptionScreen.this, TeacherStudentAttendance.class);
                    startActivity(intent);
                }
            }
        });


        teacherLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmployeeAttendanceOptionScreen.this, EmployeeAttendanceMainScreen.class);
                intent.putExtra("value",1);
                startActivity(intent);

            }
        });

        checkInCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeAttendanceOptionScreen.this,TeacherCheckInCheckOut.class);
                startActivity(intent);
            }
        });
    }
}
