package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.TeacherAnnouncementAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentHostelReservationDetailsScreen extends AppCompatActivity {

    private TextView hostelName,roomName,beddingType,floorName,accomadationType;
    private ImageView back,newImage;
    private JSONArray studentHostelReservationDetailsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_hostel_reservation_details_screen);

        hostelName = (TextView) findViewById(R.id.text_leave_detail_leave_starting_date1);
        roomName = (TextView) findViewById(R.id.text_leave_detail_leave_description1);
        beddingType = (TextView) findViewById(R.id.text_bedding_type1);
        floorName = (TextView) findViewById(R.id.text_floor_name1);
        accomadationType = (TextView) findViewById(R.id.text_accomadation_type1);

        newImage = (ImageView) findViewById(R.id.img_plus);

        newImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHostelReservationDetailsScreen.this,StudentHostelBookingFirstScreen.class);
                startActivity(intent);
            }
        });

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getTeacherAnnouncementList();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getTeacherAnnouncementList();
    }

    protected void getTeacherAnnouncementList()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentHostelReservationDetailsScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_HOSTEL_RESERVATION_LIST+"?device_id="+ Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&semester="+Preferences.getInstance().studentSemester+"&stu_id="+Preferences.getInstance().studentId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);

                try
                {
                    responseObject = new JSONObject(response);

                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(StudentHostelReservationDetailsScreen.this, "No Records Found");
                        floorName.setVisibility(View.GONE);
                        hostelName.setVisibility(View.GONE);
                        beddingType.setVisibility(View.GONE);
                        accomadationType.setVisibility(View.GONE);
                        roomName.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentHostelReservationDetailsScreen.this, "Session Expired:Please Login Again");
                        floorName.setVisibility(View.GONE);
                        hostelName.setVisibility(View.GONE);
                        beddingType.setVisibility(View.GONE);
                        accomadationType.setVisibility(View.GONE);
                        roomName.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        studentHostelReservationDetailsArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=studentHostelReservationDetailsArray && studentHostelReservationDetailsArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentHostelReservationDetailsArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentHostelReservationDetailsScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_HOSTEL_RESERVATION_LIST+"?device_id="+ Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&semester="+Preferences.getInstance().studentSemester+"&stu_id="+Preferences.getInstance().studentId,e);
                            floorName.setVisibility(View.VISIBLE);
                            hostelName.setVisibility(View.VISIBLE);
                            beddingType.setVisibility(View.VISIBLE);
                            accomadationType.setVisibility(View.VISIBLE);
                            roomName.setVisibility(View.VISIBLE);
                            floorName.setText(studentHostelReservationDetailsArray.getJSONObject(0).getString("floor_name"));
                            hostelName.setText(studentHostelReservationDetailsArray.getJSONObject(0).getString("name"));
                            beddingType.setText(studentHostelReservationDetailsArray.getJSONObject(0).getString("occupancy_type"));
                            accomadationType.setText(studentHostelReservationDetailsArray.getJSONObject(0).getString("ac_nonac"));
                            roomName.setText(studentHostelReservationDetailsArray.getJSONObject(0).getString("room_no"));
                        }
                    }
                    else {
                        Utils.showToast(StudentHostelReservationDetailsScreen.this, "Error Fetching Response");
                        floorName.setVisibility(View.GONE);
                        hostelName.setVisibility(View.GONE);
                        beddingType.setVisibility(View.GONE);
                        accomadationType.setVisibility(View.GONE);
                        roomName.setVisibility(View.GONE);
                    }
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentHostelReservationDetailsScreen.this, "Error fetching modules! Please try after sometime.");
                    floorName.setVisibility(View.GONE);
                    hostelName.setVisibility(View.GONE);
                    beddingType.setVisibility(View.GONE);
                    accomadationType.setVisibility(View.GONE);
                    roomName.setVisibility(View.GONE);
                    setSupportProgressBarIndeterminateVisibility(false);
                }






            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                floorName.setVisibility(View.GONE);
                hostelName.setVisibility(View.GONE);
                beddingType.setVisibility(View.GONE);
                accomadationType.setVisibility(View.GONE);
                roomName.setVisibility(View.GONE);
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
			/*@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(StudentAnnouncement.this);
				Map<String,String> params = new HashMap<String, String>();
				//params.put("student_ID",Preferences.getInstance().studentId);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("cls_id",Preferences.getInstance().studentClassId);
				
				params.put("sch_id", Preferences.getInstance().schoolId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudentHostelReservationDetailsScreen.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentHostelReservationDetailsScreen.this, "Unable to fetch data, kindly enable internet settings!");
            floorName.setVisibility(View.GONE);
            hostelName.setVisibility(View.GONE);
            beddingType.setVisibility(View.GONE);
            accomadationType.setVisibility(View.GONE);
            roomName.setVisibility(View.GONE);
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
