package com.schoofi.activitiess;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.HostelListViewAdapter;
import com.schoofi.adapters.StudentPolllListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.EmployeeLeaveTypeVO;
import com.schoofi.utils.HostelFloorVO;
import com.schoofi.utils.HostelRoomBeddingTypeVO;
import com.schoofi.utils.HostelRoomTypeVO;
import com.schoofi.utils.HostelVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import polypicker.model.Image;
import smtchahal.materialspinner.MaterialSpinner;

public class StudentHostelBookingFirstScreen extends AppCompatActivity {

    private ImageView back;
    private MaterialSpinner spinnerHostel,spinnerFloor,spinnerBedding,spinnerRoomType;

    private ListView roomListView;
    private JSONArray jsonarray,jsonarray1,jsonarray2,jsonarray3,roomListArray;
    private JSONObject jsonobject,jsonobject1,jsonobject2,jsonobject3;
    private HostelListViewAdapter hostelListViewAdapter;

    ArrayList<String> hostelName;
    ArrayList<String> hostelFloorName;
    ArrayList<String> hostelBeddingName;
    ArrayList<String> hostelRoomName;
    ArrayList<HostelVO> hostelVOs;
    ArrayList<HostelFloorVO> hostelFloorVOs;
    ArrayList<HostelRoomBeddingTypeVO> hostelRoomBeddingTypeVOs;
    ArrayList<HostelRoomTypeVO> hostelRoomTypeVOs;

    String hostelId="0";
    String hostelFloorId="0";
    String hostelRoomBeddingId="0";
    String hostelRoomTypeId="0";
    SwipyRefreshLayout swipyRefreshLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_hostel_booking_first_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinnerHostel = (MaterialSpinner) findViewById(R.id.spinner_hostel);
        spinnerBedding = (MaterialSpinner) findViewById(R.id.spinner_bedding_type);
        spinnerRoomType = (MaterialSpinner) findViewById(R.id.spinner_room_type);

        roomListView = (ListView) findViewById(R.id.listview_student_submitted_polls);


        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        spinnerHostel.setBackgroundResource(R.drawable.grey_button);
        spinnerBedding.setBackgroundResource(R.drawable.grey_button);
        spinnerRoomType.setBackgroundResource(R.drawable.grey_button);

        spinnerRoomType.setVisibility(View.GONE);
        spinnerBedding.setVisibility(View.GONE);

        roomListView.setVisibility(View.GONE);


        new DownloadJSON().execute();



        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                initData();
                getStudentPollList();
            }
        });

        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    postMessage(roomListArray.getJSONObject(position).getString("room_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            Preferences.getInstance().loadPreference(getApplicationContext());
            hostelVOs= new ArrayList<HostelVO>();
            hostelName = new ArrayList<String>();
            // JSON file URL address
            Log.d("op",AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.HOSTEL_LIST+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&allowed_gender="+Preferences.getInstance().studentGender);
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.HOSTEL_LIST+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&allowed_gender="+Preferences.getInstance().studentGender);
            try {
                // Locate the NodeList name
                System.out.print(jsonobject.toString());
                jsonarray = jsonobject.getJSONArray("responseObject");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    HostelVO hostelVO = new HostelVO();
                    hostelVO.setHostelId(jsonobject.optString("hostel_id"));
                    hostelVOs.add(hostelVO);
                    hostelName.add(jsonobject.optString("name"));


                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            spinnerHostel
                    .setAdapter(new ArrayAdapter<String>(StudentHostelBookingFirstScreen.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            hostelName));

            spinnerHostel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub





                    hostelId = hostelVOs.get(position).getHostelId().toString();


                    spinnerBedding.setVisibility(View.VISIBLE);
                    new DownloadJSON2().execute();

                    // getStudentLeaveList();




                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }




    }


    private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Preferences.getInstance().loadPreference(getApplicationContext());


            // Create an array to populate the spinner
            hostelFloorVOs= new ArrayList<HostelFloorVO>();
            hostelFloorName = new ArrayList<String>();
            // JSON file URL address
            jsonobject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_TYPE+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                System.out.print(jsonobject1.toString());
                jsonarray1 = jsonobject1.getJSONArray("leave_type");
                for (int i = 0; i < jsonarray1.length(); i++) {
                    jsonobject1 = jsonarray1.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    HostelFloorVO hostelFloorVO = new HostelFloorVO();
                    hostelFloorVO.setFloorId(jsonobject1.optString(""));
                    hostelFloorVOs.add(hostelFloorVO);
                    hostelFloorName.add(jsonobject1.optString(""));


                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            spinnerFloor
                    .setAdapter(new ArrayAdapter<String>(StudentHostelBookingFirstScreen.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            hostelFloorName));

            spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub





                    hostelFloorId = hostelFloorVOs.get(position).getFloorId().toString();

                    // getStudentLeaveList();




                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }




    }


    private class DownloadJSON2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Preferences.getInstance().loadPreference(getApplicationContext());


            Log.d("op1",AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.HOSTEL_ROOM_BEDDING_TYPE+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&floor_preference="+hostelFloorId+"&hostel_id="+hostelId);
            // Create an array to populate the spinner
            hostelRoomBeddingTypeVOs= new ArrayList<HostelRoomBeddingTypeVO>();
            hostelBeddingName = new ArrayList<String>();
            // JSON file URL address
            jsonobject2 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.HOSTEL_ROOM_BEDDING_TYPE+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&hostel_id="+hostelId);
            try {
                // Locate the NodeList name
                System.out.print(jsonobject2.toString());
                jsonarray2 = jsonobject2.getJSONArray("responseObject");
                for (int i = 0; i < jsonarray2.length(); i++) {
                    jsonobject2 = jsonarray2.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    HostelRoomBeddingTypeVO hostelRoomBeddingTypeVO = new HostelRoomBeddingTypeVO();
                    hostelRoomBeddingTypeVO.setRoomBeddingTypeId(jsonobject2.optString("occupancy_type"));
                    hostelRoomBeddingTypeVOs.add(hostelRoomBeddingTypeVO);
                    hostelBeddingName.add(jsonobject2.optString("occupancy_type"));


                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            spinnerBedding
                    .setAdapter(new ArrayAdapter<String>(StudentHostelBookingFirstScreen.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            hostelBeddingName));

            spinnerBedding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub





                    hostelRoomBeddingId = hostelRoomBeddingTypeVOs.get(position).getRoomBeddingTypeId().toString();
                    spinnerRoomType.setVisibility(View.VISIBLE);
                    new DownloadJSON3().execute();

                    // getStudentLeaveList();




                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }




    }



    private class DownloadJSON3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Preferences.getInstance().loadPreference(getApplicationContext());


            // Create an array to populate the spinner
            hostelRoomTypeVOs= new ArrayList<HostelRoomTypeVO>();
            hostelRoomName = new ArrayList<String>();
            // JSON file URL address
            jsonobject3 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.HOSTEL_ROOM_ACCOMADATION_TYPE+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&bedding_type="+hostelRoomBeddingId);
            try {
                // Locate the NodeList name
                System.out.print(jsonobject3.toString());
                jsonarray3 = jsonobject3.getJSONArray("responseObject");
                for (int i = 0; i < jsonarray3.length(); i++) {
                    jsonobject3 = jsonarray3.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    HostelRoomTypeVO hostelRoomTypeVO = new HostelRoomTypeVO();
                    hostelRoomTypeVO.setRoomTypeId(jsonobject3.optString("ac_nonac"));
                    hostelRoomTypeVOs.add(hostelRoomTypeVO);
                    hostelRoomName.add(jsonobject3.optString("ac_nonac"));


                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            spinnerRoomType
                    .setAdapter(new ArrayAdapter<String>(StudentHostelBookingFirstScreen.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            hostelRoomName));

            spinnerRoomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub





                    hostelRoomTypeId = hostelRoomTypeVOs.get(position).getRoomTypeId().toString();

                    roomListView.setVisibility(View.VISIBLE);

                    initData();
                    getStudentPollList();

                    // getStudentLeaveList();




                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }




    }

    private void initData()
    {

        Preferences.getInstance().loadPreference(getApplicationContext());


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HOSTEL_ROOM_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&bedding_type="+hostelRoomBeddingId+"&accomadation_type="+hostelRoomTypeId+"&hostel_id="+hostelId);
            if(e == null)
            {
                roomListArray= null;
            }
            else
            {
                roomListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(roomListArray!= null)
        {
            roomListView.invalidateViews();
            hostelListViewAdapter = new HostelListViewAdapter(StudentHostelBookingFirstScreen.this, roomListArray);
            roomListView.setAdapter(hostelListViewAdapter);
            hostelListViewAdapter.notifyDataSetChanged();
            swipyRefreshLayout.setRefreshing(false);
        }
    }

    protected void getStudentPollList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentHostelBookingFirstScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HOSTEL_ROOM_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&bedding_type="+hostelRoomBeddingId+"&accomadation_type="+hostelRoomTypeId+"&hostel_id="+hostelId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                Log.d("op2",url);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(StudentHostelBookingFirstScreen.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentHostelBookingFirstScreen.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        roomListArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=roomListArray && roomListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = roomListArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentHostelBookingFirstScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HOSTEL_ROOM_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&bedding_type="+hostelRoomBeddingId+"&accomadation_type="+hostelRoomTypeId+"&hostel_id="+hostelId,e);
                            roomListView.invalidateViews();
                            hostelListViewAdapter = new HostelListViewAdapter(StudentHostelBookingFirstScreen.this, roomListArray);
                            roomListView.setAdapter(hostelListViewAdapter);
                            hostelListViewAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(StudentHostelBookingFirstScreen.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentHostelBookingFirstScreen.this, "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudentHostelBookingFirstScreen.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentHostelBookingFirstScreen.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    private void postMessage(final String roomId)
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_HOSTEL_RESERVATION;
        System.out.println(url1);

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    System.out.println(responseObject.toString());
                    Log.d("opq",response);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(StudentHostelBookingFirstScreen.this,"Error Submitting Comment");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentHostelBookingFirstScreen.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1")) {

                        Utils.showToast(StudentHostelBookingFirstScreen.this, "Reserved Successfully");
                        finish();
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("2")) {

                        Utils.showToast(StudentHostelBookingFirstScreen.this, "Already Reserved One room ");
                        //finish();
                    }



                }



                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentHostelBookingFirstScreen.this, "Error submitting alert! Please try after sometime.");
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(StudentHostelBookingFirstScreen.this, "Error submitting alert! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				Preferences.getInstance().loadPreference(getApplicationContext());
				params.put("token",Preferences.getInstance().token);
				params.put("device_id",Preferences.getInstance().deviceId);
				params.put("hostel_id",hostelId);
				params.put("room_id",roomId);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("stu_id",Preferences.getInstance().studentId);
				params.put("semester",Preferences.getInstance().studentSemester);
				return params;
			}};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            setSupportProgressBarIndeterminateVisibility(false);
        }
    }
}
