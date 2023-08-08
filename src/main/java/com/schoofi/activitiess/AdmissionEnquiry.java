package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.LoginScreen;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

public class AdmissionEnquiry extends AppCompatActivity {


    Button status,form,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admission Enquiry");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admission_enquiry);


        logout = (Button) findViewById(R.id.btn_Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(AdmissionEnquiry.this, LoginScreen.class);
                 Preferences.getInstance().isLoggedIn = false;
                 Preferences.getInstance().savePreference(getApplicationContext());
                 startActivity(intent);
                 finish();
            }
        });
        status = (Button) findViewById(R.id.btn_status);
        form = (Button) findViewById(R.id.btn_enquiry);



        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lll","jjj");
            }
        });

        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdmissionEnquiry.this,AddmissionFormPrimaryScreen.class);
                startActivity(intent);
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdmissionEnquiry.this,AdmissionEnquiryStatus.class);
                startActivity(intent);
            }
        });
    }
}
