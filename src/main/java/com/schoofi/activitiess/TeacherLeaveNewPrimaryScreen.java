package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class TeacherLeaveNewPrimaryScreen extends AppCompatActivity {

    private Button teacherLeaves,yourLeaves,studentLeaves;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_teacher_leave_new_primary_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        teacherLeaves = (Button) findViewById(R.id.btn_teacher_leaves);
        yourLeaves = (Button) findViewById(R.id.btn_your_leaves);
        studentLeaves = (Button) findViewById(R.id.btn_student_leaves);

        studentLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherLeaveNewPrimaryScreen.this,TeacherStudentLeave.class);
                startActivity(intent);
            }
        });

        yourLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherLeaveNewPrimaryScreen.this,TeacherSelfLeaveList.class);
                startActivity(intent);
            }
        });

        teacherLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TeacherLeaveNewPrimaryScreen.this,EmployeeLeaveList.class);
                startActivity(intent);

            }
        });






    }
}
