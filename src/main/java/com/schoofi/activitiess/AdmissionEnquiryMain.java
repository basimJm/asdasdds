package com.schoofi.activitiess;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.LoginScreen;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdmissionEnquiryMain extends AppCompatActivity {

    private EditText name,userName,password,confirmPassword,userPhone,userEmail;
    private Button register;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admission Enquiry Main");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_admission_enquiry_main);
        back = (ImageView) findViewById(R.id.img_back);
        name = (EditText) findViewById(R.id.edit_userFullName);
        userName = (EditText) findViewById(R.id.edit_userName);
        password = (EditText) findViewById(R.id.edit_password);
        confirmPassword = (EditText) findViewById(R.id.edit_confirmPassword);
        userPhone = (EditText) findViewById(R.id.edit_phone);
        userEmail = (EditText) findViewById(R.id.edit_email);
        register = (Button) findViewById(R.id.btn_register);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        final AuthCallback digitsCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumbe) {
                // Do something with the session

                postAttendance();


            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
            }
        };


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!password.getText().toString().matches(confirmPassword.getText().toString()))
                {
                    Utils.showToast(getApplicationContext(),"Password did not matched");
                }

                else
                    if(name.getText().toString().matches("") || userName.getText().toString().matches("") || password.getText().toString().matches("") || confirmPassword.getText().toString().matches("") || userEmail.getText().toString().matches("") || userPhone.getText().toString().matches(""))
                    {
                        Utils.showToast(getApplicationContext(),"Please fill all the fields");
                    }

                else
                    {
                        Digits.getSessionManager().clearActiveSession();
                        Digits.authenticate(digitsCallback,R.style.CustomDigitsTheme,"+91"+userPhone.getText().toString(),false);

                       // postAttendance();
                    }

            }
        });
    }

    protected void postAttendance()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ENQUIRY_USER_REGISTRATION;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
               // System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        //Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Toast.makeText(getApplicationContext(), "registered successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdmissionEnquiryMain.this,LoginScreen.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("2"))
                    {

                        Utils.showToast(getApplicationContext(),"User Already exists");
                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
                    Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_LONG).show();
                }
                setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put("email",userEmail.getText().toString());
                params.put("password",password.getText().toString());
                params.put("phone",userPhone.getText().toString());
                params.put("u_name",userName.getText().toString());
                params.put("name",name.getText().toString());



                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }
}

