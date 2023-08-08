package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.StudentSubjectElectiveAdapter;
import com.schoofi.adapters.StudentListviewSubmittedPollsAdapter;
import com.schoofi.adapters.StudentSubjectElectiveAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudentChooseElectiveSubject extends AppCompatActivity {

    private TextView screenTitle;
    private ImageView back;
    private ListView studentSubmittedPollListView;
    private JSONArray studentSubmittedPollsArray;

    String studentId = Preferences.getInstance().studentId;
    String userId = Preferences.getInstance().userId;
    String userEmailId = Preferences.getInstance().userEmailId;
    String token = Preferences.getInstance().token;
    String schoolId = Preferences.getInstance().schoolId;
    String userType = Preferences.getInstance().userType;
    StudentSubjectElectiveAdapter studentSubjectElectiveAdapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private Button submit;
    String array1,array2,array3,array4;
    public ArrayList<String> attendance = new ArrayList<String>();
    public ArrayList<String> attendanceName = new ArrayList<String>();
    String date5 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    int y=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_choose_elective_subject);

        screenTitle = (TextView) findViewById(R.id.txt_submittedPolls);
        studentSubmittedPollListView = (ListView) findViewById(R.id.listview_student_submitted_polls);
        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        submit = (Button) findViewById(R.id.btn_submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count=0;
                attendance.clear();

                if(y==1)
                {
                    Utils.showToast(getApplicationContext(),"No subjects selected!");
                }
                else {

                    for (int i = 0; i < studentSubmittedPollsArray.length(); i++) {
                        count = i + 1;
                    }

                    try {


                        int countNo = 0;
                        int countYes = 0;

                        for (int k = 0; k < count; k++) {
                            if (studentSubmittedPollsArray.getJSONObject(k).getString("isAdded").matches("1")) {
                                attendance.add(studentSubmittedPollsArray.getJSONObject(k).getString("subject_id") + "-" + "1");
                                countYes = countYes + 1;
                            } else {
                                attendance.add(studentSubmittedPollsArray.getJSONObject(k).getString("subject_id") + "-" + "0");
                                countNo = countNo + 1;
                            }
                        }

                        array1 = attendance.toString();

                        array2 = array1.substring(1, array1.length() - 1);


                        if (countYes > Integer.parseInt(studentSubmittedPollsArray.getJSONObject(0).getString("total_count"))) {
                            Utils.showToast(getApplicationContext(), "Selected count is more than maximum count");
                        } else if (countYes == 0) {
                            Utils.showToast(getApplicationContext(), "Select the subjects first");
                        } else if (countYes < Integer.parseInt(studentSubmittedPollsArray.getJSONObject(0).getString("total_count"))) {
                            Utils.showToast(getApplicationContext(), "Selected count is less than required count");
                        } else {
                            postAttendance();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                initData();
                getStudentPollList();

            }
        });

        initData();
        getStudentPollList();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_submitted_polls, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData()
    {

        Preferences.getInstance().loadPreference(getApplicationContext());


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ELECTIVE_SUBJECT_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&stu_id="+Preferences.getInstance().studentId+"&semester="+Preferences.getInstance().studentSemester+"&class_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&course_id="+Preferences.getInstance().studentCourse);
            if(e == null)
            {
                studentSubmittedPollsArray= null;
            }
            else
            {
                studentSubmittedPollsArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentSubmittedPollsArray!= null)
        {

            try {
                String freeze_date;
                freeze_date = studentSubmittedPollsArray.getJSONObject(0).getString("freeze_date");
                int count = 1;
                try {
                    Date date1 = formatter.parse(freeze_date);
                    Date date2 = formatter.parse(date5);
                    if(date1.compareTo(date2)<0)
                    {

                        count=0;
                    }
                    else
                    {
                        count =1;
                    }

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                if(count == 1) {
                    studentSubmittedPollListView.invalidateViews();
                    studentSubjectElectiveAdapter = new StudentSubjectElectiveAdapter(StudentChooseElectiveSubject.this, studentSubmittedPollsArray);
                    studentSubmittedPollListView.setAdapter(studentSubjectElectiveAdapter);
                    studentSubjectElectiveAdapter.notifyDataSetChanged();
                    swipyRefreshLayout.setRefreshing(false);
                }
                else
                {
                    studentSubmittedPollListView.invalidateViews();
                    studentSubjectElectiveAdapter = new StudentSubjectElectiveAdapter(StudentChooseElectiveSubject.this, studentSubmittedPollsArray);
                    studentSubmittedPollListView.setAdapter(studentSubjectElectiveAdapter);
                    studentSubjectElectiveAdapter.notifyDataSetChanged();
                    swipyRefreshLayout.setRefreshing(false);

                    submit.setBackgroundResource(R.drawable.button_red_50_percent);
                    submit.setClickable(false);
                    studentSubmittedPollListView.setClickable(false);
                    //Utils.showToast(getApplicationContext(),"Last Date Has Been Over!!");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    protected void getStudentPollList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentChooseElectiveSubject.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ELECTIVE_SUBJECT_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&stu_id="+Preferences.getInstance().studentId+"&semester="+Preferences.getInstance().studentSemester+"&class_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&course_id="+Preferences.getInstance().studentCourse;
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
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(StudentChooseElectiveSubject.this, "No Records Found");
                        y=1;
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentChooseElectiveSubject.this, "Session Expired:Please Login Again");
                        y=1;
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        studentSubmittedPollsArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=studentSubmittedPollsArray && studentSubmittedPollsArray.length()>=0)
                        {
                            y=0;
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentSubmittedPollsArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentChooseElectiveSubject.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ELECTIVE_SUBJECT_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&stu_id="+Preferences.getInstance().studentId+"&semester="+Preferences.getInstance().studentSemester+"&class_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&course_id="+Preferences.getInstance().studentCourse,e);

                            String freeze_date  =studentSubmittedPollsArray.getJSONObject(0).getString("freeze_date");
                            int count = 0;
                            try {
                                Date date1 = formatter.parse(freeze_date);
                                Date date2 = formatter.parse(date5);
                                if(date1.compareTo(date2)<0)
                                {

                                    count=0;
                                }
                                else
                                {
                                    count =1;
                                }

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            if(count == 1) {
                                studentSubmittedPollListView.invalidateViews();
                                studentSubjectElectiveAdapter = new StudentSubjectElectiveAdapter(StudentChooseElectiveSubject.this, studentSubmittedPollsArray);
                                studentSubmittedPollListView.setAdapter(studentSubjectElectiveAdapter);
                                studentSubjectElectiveAdapter.notifyDataSetChanged();
                                swipyRefreshLayout.setRefreshing(false);
                            }
                            else
                            {
                                studentSubmittedPollListView.invalidateViews();
                                studentSubjectElectiveAdapter = new StudentSubjectElectiveAdapter(StudentChooseElectiveSubject.this, studentSubmittedPollsArray);
                                studentSubmittedPollListView.setAdapter(studentSubjectElectiveAdapter);
                                studentSubjectElectiveAdapter.notifyDataSetChanged();
                                swipyRefreshLayout.setRefreshing(false);

                                submit.setBackgroundResource(R.drawable.button_red_50_percent);
                                submit.setClickable(false);
                                studentSubmittedPollListView.setClickable(false);
                                Utils.showToast(getApplicationContext(),"Last Date for Selecting Electives is Over!!");
                            }

                        }
                    }
                    else
                        Utils.showToast(StudentChooseElectiveSubject.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentChooseElectiveSubject.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(StudentChooseElectiveSubject.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentChooseElectiveSubject.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }


    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ELECTIVE_SUBJECT_SUBMIT/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(StudentChooseElectiveSubject.this,"Error Submitting Comment");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(StudentChooseElectiveSubject.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Utils.showToast(StudentChooseElectiveSubject.this,"Successfuly Submitted ");
                        finish();

                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    Utils.showToast(StudentChooseElectiveSubject.this, "Error submitting alert! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Utils.showToast(StudentChooseElectiveSubject.this, "Error submitting alert! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(StudentChooseElectiveSubject.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
                //params.put("Students", jsonObject1.toString());

                //params.put("Students", "harsh");
                //params.put("u_email_id", Preferences.getInstance().userEmailId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("class_id",Preferences.getInstance().studentClassId);
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("course_id",Preferences.getInstance().studentCourse);
                params.put("semester",Preferences.getInstance().studentSemester);
                params.put("subject_id",array2.toString());
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);

                Log.d("ins_id",Preferences.getInstance().institutionId);
                Log.d("sch_id",Preferences.getInstance().schoolId);
                Log.d("class_id",Preferences.getInstance().studentClassId);
                Log.d("sec_id",Preferences.getInstance().studentSectionId);
                Log.d("stu_id",Preferences.getInstance().studentId);
                Log.d("course_id",Preferences.getInstance().studentCourse);
                Log.d("semester",Preferences.getInstance().studentSemester);
                Log.d("subject_id",array2.toString());
                Log.d("token",Preferences.getInstance().token);
                Log.d("device_id",Preferences.getInstance().deviceId);

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
            loading.dismiss();
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            //setSupportProgressBarIndeterminateVisibility(false);
        }



    }

}
