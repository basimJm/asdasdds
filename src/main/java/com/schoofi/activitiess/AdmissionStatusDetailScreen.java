package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdmissionStatusDetailScreen extends AppCompatActivity {

    private int position;
    private JSONArray admissionStatusArray;
    TextView applicationStatus,candidateName,candidateClass,candidateReferenceNumber,candidateGender,candidateDOB,candidateEmail,candidatePhone,candidateDetails,candidateFathersName,candidateFathersEmail,candidateFathersPhone,candidateMothersName,candidateMothersEmail,candidateMothersPhone,candidatePreviousSchoolName,candidatePreviousSchoolClass,candidatePreviousSchoolStream,candidatePreviousSchoolAggregrate,candidatePreviousSchoolCGPA;
    String date,date1,date3,date4;
    Date date5,date6;
    ImageView back;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admission Status Detail Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admission_status_detail_screen);

        position = getIntent().getExtras().getInt("position");
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        applicationStatus = (TextView) findViewById(R.id.text_status);
        candidateName = (TextView) findViewById(R.id.text_candidate_name);
        candidateClass = (TextView) findViewById(R.id.text_candidate_class);
        candidateReferenceNumber = (TextView) findViewById(R.id.text_candidate_ref_id);
        candidateGender = (TextView) findViewById(R.id.text_candidate_gender);
        candidateDOB = (TextView) findViewById(R.id.text_candidate_dob);
        candidateEmail = (TextView) findViewById(R.id.text_candidate_email);
        candidatePhone = (TextView) findViewById(R.id.text_candidate_phone);
        candidateDetails = (TextView) findViewById(R.id.text_candidate_details);
        candidateFathersName = (TextView) findViewById(R.id.text_candidate_father_name);
        candidateFathersPhone = (TextView) findViewById(R.id.text_candidate_father_phone);
        candidateFathersEmail = (TextView) findViewById(R.id.text_candidate_father_email);
        candidateMothersName = (TextView) findViewById(R.id.text_candidate_mother_name);
        candidateMothersEmail = (TextView) findViewById(R.id.text_candidate_mother_email);
        candidateMothersPhone = (TextView) findViewById(R.id.text_candidate_mother_phone);
        candidatePreviousSchoolName = (TextView) findViewById(R.id.text_candidate_prevous_school_name);
        candidatePreviousSchoolClass = (TextView) findViewById(R.id.text_candidate_prevous_class);
        candidatePreviousSchoolAggregrate = (TextView) findViewById(R.id.text_candidate_prevous_aggregate);
        candidatePreviousSchoolStream = (TextView) findViewById(R.id.text_candidate_prevous_stream);
        candidatePreviousSchoolCGPA = (TextView) findViewById(R.id.text_candidate_prevous_cgpa);

        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(AdmissionStatusDetailScreen.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMISSION_ENQUIRY_STATUS_LIST+"?token="+Preferences.getInstance().token+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId);
            admissionStatusArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (admissionStatusArray != null) {

            try {

                date = admissionStatusArray.getJSONObject(position).getString("confirmation_date");
                date5 = formatter.parse(date);
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date1 = formatter1.format(date5);
                date3 = admissionStatusArray.getJSONObject(position).getString("dob");
                date6 = formatter.parse(date3);
                date4 = formatter1.format(date6);
                if(admissionStatusArray.getJSONObject(position).getString("confirmation_status").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("confirmation_status").toString().matches("null"))
                {
                    applicationStatus.setText("Not mentioned");
                }

                else
                if(admissionStatusArray.getJSONObject(position).getString("confirmation_date").toString().matches("0000-00-00"))
                {
                    applicationStatus.setText(admissionStatusArray.getJSONObject(position).getString("confirmation_status"));
                }
                else
                {
                    applicationStatus.setText(admissionStatusArray.getJSONObject(position).getString("confirmation_status") + "," + date1);
                }
                
                if(admissionStatusArray.getJSONObject(position).getString("candidate_name").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("candidate_name").toString().matches("null"))
                {
                    candidateName.setText("Not mentioned");
                }

                else {
                    candidateName.setText("Name: " + admissionStatusArray.getJSONObject(position).getString("candidate_name"));
                }
                if(admissionStatusArray.getJSONObject(position).getString("confirmation_detail").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("confirmation_detail").toString().matches("null"))
                {
                    candidateDetails.setText("Not mentioned");
                }

                else {
                    candidateDetails.setText("Details: " + admissionStatusArray.getJSONObject(position).getString("confirmation_detail"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("required_class").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("required_class").toString().matches("null"))
                {
                    candidateClass.setText("Not mentioned");
                }

                else {
                    candidateClass.setText("Class: " + admissionStatusArray.getJSONObject(position).getString("required_class"));
                }
                if(admissionStatusArray.getJSONObject(position).getString("req_ref_id").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("req_ref_id").toString().matches("null"))
                {
                    candidateReferenceNumber.setText("Not mentioned");
                }
                else {
                    candidateReferenceNumber.setText("Application Reference No. " + admissionStatusArray.getJSONObject(position).getString("req_ref_id"));
                }
                if(admissionStatusArray.getJSONObject(position).getString("gender").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("gender").toString().matches("null")) {

                    candidateGender.setText("Not mentioned");
                }

                else
                {
                    candidateGender.setText("Gender: " + admissionStatusArray.getJSONObject(position).getString("gender"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("email").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("email").toString().matches("null"))
                {
                    candidateEmail.setText("Not mentioned");
                }
                else {
                    candidateEmail.setText("Email: " + admissionStatusArray.getJSONObject(position).getString("email"));
                }
                if(admissionStatusArray.getJSONObject(position).getString("dob").toString().matches("0000-00-00"))
                {
                    candidateDOB.setText("Not mentioned");
                }
                else {
                    candidateDOB.setText("DOB: " + date4);
                }
                if(admissionStatusArray.getJSONObject(position).getString("phone").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("phone").toString().matches("null"))
                {
                    candidatePhone.setText("Not mentioned");
                }

                else {
                    candidatePhone.setText("Phone: " + admissionStatusArray.getJSONObject(position).getString("phone"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("father_name").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("father_name").toString().matches("null"))
                {
                    candidatePhone.setText("Not mentioned");
                }

                else {
                    candidateFathersName.setText("Father Name: " + admissionStatusArray.getJSONObject(position).getString("father_name"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("f_phone").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("f_phone").toString().matches("null"))
                {
                    candidateFathersPhone.setText("Not mentioned");
                }

                else {
                    candidateFathersPhone.setText("Father Phone: " + admissionStatusArray.getJSONObject(position).getString("f_phone"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("f_email").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("f_email").toString().matches("null"))
                {
                    candidateFathersEmail.setText("Not mentioned");
                }

                else
                {
                    candidateFathersEmail.setText("Father Email: "+admissionStatusArray.getJSONObject(position).getString("f_email"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("mother_name").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("mother_name").toString().matches("null"))
                {
                    candidateMothersName.setText("Not mentioned");
                }

                else {

                    candidateMothersName.setText("Mother Name: " + admissionStatusArray.getJSONObject(position).getString("mother_name"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("m_phone").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("m_phone").toString().matches("null")) {

                    candidateMothersPhone.setText("Not mentioned");
                }

                else
                {
                    candidateMothersPhone.setText("Mother Phone: " + admissionStatusArray.getJSONObject(position).getString("m_phone"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("m_email").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("m_email").toString().matches("null"))
                {
                    candidateMothersEmail.setText("Not mentioned");
                }

                else {
                    candidateMothersEmail.setText("Mother Email: " + admissionStatusArray.getJSONObject(position).getString("m_email"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("previous_school_name").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("previous_school_name").toString().matches("null"))
                {
                    candidatePreviousSchoolName.setText("Not mentioned");
                }

                else {
                    candidatePreviousSchoolName.setText("School Name: " + admissionStatusArray.getJSONObject(position).getString("previous_school_name"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("total_percentage").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("total_percentage").toString().matches("null"))
                {
                    candidatePreviousSchoolAggregrate.setText("Not mentioned");
                }

                else {
                    candidatePreviousSchoolAggregrate.setText("Aggregrate: " + admissionStatusArray.getJSONObject(position).getString("total_percentage"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("CGPA").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("CGPA").toString().matches("null"))
                {
                    candidatePreviousSchoolCGPA.setText("Not mentioned");
                }

                else {
                    candidatePreviousSchoolCGPA.setText("CGPA: " + admissionStatusArray.getJSONObject(position).getString("CGPA"));
                }

                if(admissionStatusArray.getJSONObject(position).getString("previous_class").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("previous_class").toString().matches("null"))
                {
                    candidatePreviousSchoolClass.setText("Not mentioned");
                }

                else {
                    candidatePreviousSchoolClass.setText("Class: " + admissionStatusArray.getJSONObject(position).getString("previous_class"));
                }
                if(admissionStatusArray.getJSONObject(position).getString("previous_stream").toString().matches("") || admissionStatusArray.getJSONObject(position).getString("previous_stream").toString().matches("null"))
                {
                    candidatePreviousSchoolStream.setText("Not mentioned");
                }

                else {
                    candidatePreviousSchoolStream.setText("Stream: " + admissionStatusArray.getJSONObject(position).getString("previous_stream"));
                }




            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }



    }

