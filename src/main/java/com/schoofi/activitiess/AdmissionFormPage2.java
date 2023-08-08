package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

public class AdmissionFormPage2 extends AppCompatActivity {

    ImageView back;
    EditText fathersName,fathersEmail,fathersPhone,mothersName,mothersEmail,mothersPhone;
    String brochureFees,courierCharges,admissionCharges,name,dob,gender,email,phone,address,type,schoolId,studentClass,stream,fatherName,fatherPhone,fatherEmail,motherName,motherEmail,motherPhone;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admission FormPage2");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admission_form_page2);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fathersName = (EditText) findViewById(R.id.edit_FatherName);
        fathersPhone = (EditText) findViewById(R.id.edit_FatherPhone);
        fathersEmail = (EditText) findViewById(R.id.edit_FatherEmail);
        mothersEmail = (EditText) findViewById(R.id.edit_MotherEmail);
        mothersName = (EditText) findViewById(R.id.edit_MotherName);
        mothersPhone = (EditText) findViewById(R.id.edit_MotherPhone);


        next = (Button) findViewById(R.id.btn_next);

        name = getIntent().getStringExtra("name");
        dob = getIntent().getStringExtra("dob");
        gender = getIntent().getStringExtra("gender");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        schoolId = getIntent().getStringExtra("schoolId");
        studentClass = getIntent().getStringExtra("studentClass");
        stream = getIntent().getStringExtra("stream");
        type = getIntent().getStringExtra("type");
        brochureFees = getIntent().getStringExtra("brochureFees");
        admissionCharges = getIntent().getStringExtra("admissionCharges");
        courierCharges = getIntent().getStringExtra("courier_charges");



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fatherName = fathersName.getText().toString();
                fatherPhone = fathersPhone.getText().toString();
                fatherEmail = fathersEmail.getText().toString();
                motherName = mothersName.getText().toString();
                motherEmail = mothersEmail.getText().toString();
                motherPhone = mothersPhone.getText().toString();

                if(fatherName.matches("") || motherName.matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please fill the parents names");
                }

                else
                {
                    Intent intent = new Intent(AdmissionFormPage2.this,AdmissionFormPage3.class);
                    intent.putExtra("name",name);
                    intent.putExtra("dob",dob);
                    intent.putExtra("gender",gender);
                    intent.putExtra("email",email);
                    intent.putExtra("phone",phone);
                    intent.putExtra("address",address);
                    intent.putExtra("schoolId",schoolId);
                    intent.putExtra("type",type);
                    intent.putExtra("studentClass",studentClass);
                    intent.putExtra("stream",stream);
                    intent.putExtra("fatherName",fatherName);
                    intent.putExtra("fatherPhone",fatherPhone);
                    intent.putExtra("fatherEmail",fatherEmail);
                    intent.putExtra("motherName",motherName);
                    intent.putExtra("motherEmail",motherEmail);
                    intent.putExtra("motherPhone",motherPhone);
                    intent.putExtra("brochureFees",brochureFees);
                    intent.putExtra("admissionCharges",admissionCharges);
                    intent.putExtra("courier_charges",courierCharges);
                    startActivity(intent);
                }

            }
        });
    }
}
