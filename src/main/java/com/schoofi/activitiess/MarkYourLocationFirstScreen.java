package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.AssignmentAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MarkYourLocationFirstScreen extends AppCompatActivity {

    private ImageView back;
    private TextView markLocationAddress,lattitudeText,longitudeText;
    private Button mark;
    private JSONArray markYourLocationArray;
    private Button EditProfile;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Mark Your Location First Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_mark_your_location_first_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        markLocationAddress = (TextView) findViewById(R.id.text_address);
        lattitudeText = (TextView) findViewById(R.id.txt_lattitude);
        longitudeText = (TextView) findViewById(R.id.txt_longitude);
        mark = (Button) findViewById(R.id.btn_done);
        EditProfile = (Button) findViewById(R.id.btn_edit_profile);
        EditProfile.setVisibility(View.GONE);
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarkYourLocationFirstScreen.this,EditProfileActivity.class);
                startActivity(intent);
            }
        });
        context = MarkYourLocationFirstScreen.this;

        getChairmanStudentLeaveList();
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.new_submit_layout, null);



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // get user input and set it to result
                              Intent intent = new Intent(MarkYourLocationFirstScreen.this,MarkYourLocation.class);
                                        startActivity(intent);

                                        // edit text


                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



            }


        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getChairmanStudentLeaveList();
    }

    protected void getChairmanStudentLeaveList() {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(MarkYourLocationFirstScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.MARK_LOCATION_FIRST_URL/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                 System.out.println(response);
                 System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Location Found!!");
                        // notificationListView.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }

                    else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("2")) {

                        Utils.showToast(getApplicationContext(), "No Bus data found! Please contact your school to get your bus data updated!");
                        finish();

                    }else if (responseObject.has("responseObject")) {

                        markYourLocationArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=markYourLocationArray && markYourLocationArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = markYourLocationArray.toString().getBytes();
                           // VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNMENT_URL+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&temp="+"1"+"&status="+"0",e);
                            markLocationAddress.setText("Address: "+markYourLocationArray.getJSONObject(0).getString("address"));
                            lattitudeText.setText("Latitude: "+markYourLocationArray.getJSONObject(0).getString("latitude"));
                            longitudeText.setText("Longitude: "+markYourLocationArray.getJSONObject(0).getString("longitude"));

                        }



                    } else
                        Utils.showToast(getApplicationContext(), "Something went wrong!!");
                    setSupportProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                /*;
                params.put("sec_id",Preferences.getInstance().studentSectionId);

                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);

                params.put("cls_id", Preferences.getInstance().studentClassId);*/
                params.put("stu_id", Preferences.getInstance().studentId);
                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);



                return params;
            }
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else {
            Utils.showToast(getApplicationContext(), "Unable to upload data, kindly enable internet settings!");
        }
    }


    private void toa() {
        System.out.println("aaa");
    }
}
