package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.AdmissionEnquirySubjectAdapter;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

public class AdmissionFormPage3 extends AppCompatActivity {

    ImageView back;
    EditText previousSchoolName,previousSchoolAddress,lastClassAttended,lastStream,totalPercentage,grade;
    Button register,addSubject;
    String name,dob,gender,email,brochureFees,courierCharges,admissionFees,phone,address,schoolId,studentClass,stream,fatherName,type,fatherPhone,fatherEmail,motherName,motherPhone,motherEmail,previousSchoolName1,previousSchoolAddress1,lastClassAttended1,previousStream1,totalPercentage1,grade1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admission FormPage3");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admission_form_page3);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        previousSchoolName = (EditText) findViewById(R.id.edit_school_name);
        previousSchoolAddress = (EditText) findViewById(R.id.edit_school_address);
        lastClassAttended = (EditText) findViewById(R.id.edit_last_class_attended);

        lastStream = (EditText) findViewById(R.id.edit_last_stream);
        totalPercentage = (EditText) findViewById(R.id.edit_percentage);
        grade = (EditText) findViewById(R.id.edit_grade);
        register = (Button) findViewById(R.id.btn_register);
        addSubject = (Button) findViewById(R.id.btn_add_subject);

        name = getIntent().getStringExtra("name");
        dob = getIntent().getStringExtra("dob");
        gender = getIntent().getStringExtra("gender");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        schoolId = getIntent().getStringExtra("schoolId");
        studentClass = getIntent().getStringExtra("studentClass");
        stream = getIntent().getStringExtra("stream");
        fatherName = getIntent().getStringExtra("fatherName");
        fatherEmail = getIntent().getStringExtra("fatherEmail");
        fatherPhone = getIntent().getStringExtra("fatherPhone");
        motherName = getIntent().getStringExtra("motherName");
        motherEmail = getIntent().getStringExtra("motherEmail");
        motherPhone = getIntent().getStringExtra("motherPhone");
        type = getIntent().getStringExtra("type");
        brochureFees = getIntent().getStringExtra("brochureFees");
        admissionFees = getIntent().getStringExtra("admissionCharges");
        courierCharges = getIntent().getStringExtra("courier_charges");
       /* previousSchoolName1 = previousSchoolName.getText().toString();
        previousSchoolAddress1 = previousSchoolAddress.getText().toString();
        lastClassAttended1 = lastClassAttended.getText().toString();
        previousStream1 = lastStream.getText().toString();
        totalPercentage1 = totalPercentage.getText().toString();
        grade1 = grade.getText().toString();*/

        AdmissionEnquirySubjectAdapter.add(this,addSubject);
        AdmissionEnquirySubjectAdapter.display(this,register,name,type,dob,gender,email,phone,address,schoolId,studentClass,stream,fatherName,fatherEmail,fatherPhone,motherName,motherEmail,motherPhone,brochureFees,admissionFees,courierCharges);





    }
}
