package com.schoofi.activitiess;

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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.StudentListviewSubmittedPollsAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewSubmittedPollQuestionList extends AppCompatActivity {

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
    StudentListviewSubmittedPollsAdapter studentListviewSubmittedPollsAdapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    String poll_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_new_submitted_poll_question_list);

        Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Student SubmittedPolls");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
		/*{
		    "action_List": [
		        {
		            "ques": "student parent check",
		            "action_values": "bc"
		        },
		        {
		            "ques": "student check",
		            "action_values": "c"
		        },
		        {
		            "ques": "hskfhadlghle",
		            "action_values": "fejfg"
		        },
		        {
		            "ques": "check check",
		            "action_values": "d"
		        },
		        {
		            "ques": "adgw",
		            "action_values": "gasggagad"
		        },
		        {
		            "ques": "f",
		            "action_values": "s"
		        }
		    ]
		}*/

        screenTitle = (TextView) findViewById(R.id.txt_submittedPolls);
        studentSubmittedPollListView = (ListView) findViewById(R.id.listview_student_submitted_polls);
        back = (ImageView) findViewById(R.id.img_back);
        poll_id = getIntent().getStringExtra("poll_id");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        Preferences.getInstance().loadPreference(getApplicationContext());
        if (Preferences.getInstance().userRoleId.matches("3") || Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8") || Preferences.getInstance().userRoleId.matches("2")) {

            initData1();
            getStudentPollList();

        }
        else
        {
            initData();
            getStudentSubmittedPollList();
        }



        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (Preferences.getInstance().userRoleId.matches("3") || Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8") || Preferences.getInstance().userRoleId.matches("2")) {

                    initData1();
                    getStudentPollList();

                }
                else
                {
                    initData();
                    getStudentSubmittedPollList();
                }
            }
        });

        studentSubmittedPollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(NewSubmittedPollQuestionList.this,NewStudentSubmittedPollDetails.class);
                intent.putExtra("position",i);
                intent.putExtra("poll_id",poll_id);
                if (Preferences.getInstance().userRoleId.matches("3") || Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8") || Preferences.getInstance().userRoleId.matches("2")) {

                   intent.putExtra("value","4");
                    startActivity(intent);

                }
                else
                {

                }


            }
        });
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


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_SUBMITTED_QUESTION_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&poll_id="+poll_id);
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
            studentListviewSubmittedPollsAdapter= new StudentListviewSubmittedPollsAdapter(NewSubmittedPollQuestionList.this,studentSubmittedPollsArray,"1");
            studentSubmittedPollListView.setAdapter(studentListviewSubmittedPollsAdapter);
            studentListviewSubmittedPollsAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentSubmittedPollList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(NewSubmittedPollQuestionList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_SUBMITTED_QUESTION_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&poll_id="+poll_id;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                Log.d("tag",response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(NewSubmittedPollQuestionList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(NewSubmittedPollQuestionList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("action_List"))
                    {
                        studentSubmittedPollsArray= new JSONObject(response).getJSONArray("action_List");
                        if(null!=studentSubmittedPollsArray && studentSubmittedPollsArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentSubmittedPollsArray.toString().getBytes();
                            VolleySingleton.getInstance(NewSubmittedPollQuestionList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_SUBMITTED_QUESTION_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&poll_id="+poll_id,e);
                            studentSubmittedPollListView.invalidateViews();
                            studentListviewSubmittedPollsAdapter = new StudentListviewSubmittedPollsAdapter(NewSubmittedPollQuestionList.this, studentSubmittedPollsArray,"1");
                            studentSubmittedPollListView.setAdapter(studentListviewSubmittedPollsAdapter);
                            studentListviewSubmittedPollsAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(NewSubmittedPollQuestionList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(NewSubmittedPollQuestionList.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(NewSubmittedPollQuestionList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(NewSubmittedPollQuestionList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Preferences.getInstance().loadPreference(getApplicationContext());
        if (Preferences.getInstance().userRoleId.matches("3") || Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8") || Preferences.getInstance().userRoleId.matches("2")) {

            initData1();
            getStudentPollList();

        }
        else
        {
            initData();
            getStudentSubmittedPollList();
        }
        super.onResume();
    }

    private void initData1()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_QUESTION_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+ Preferences.getInstance().deviceId+"&poll_id="+poll_id);
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


                studentSubmittedPollListView.invalidateViews();
                studentListviewSubmittedPollsAdapter = new StudentListviewSubmittedPollsAdapter(NewSubmittedPollQuestionList.this, studentSubmittedPollsArray,"3");
                studentSubmittedPollListView.setAdapter(studentListviewSubmittedPollsAdapter);
                studentListviewSubmittedPollsAdapter.notifyDataSetChanged();
//                swipyRefreshLayout.setRefreshing(false);
            swipyRefreshLayout.setRefreshing(false);




        }
    }

    protected void getStudentPollList() {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(NewSubmittedPollQuestionList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_QUESTION_LIST + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&u_type=" + userType + "&sch_id=" + schoolId + "&device_id=" + Preferences.getInstance().deviceId  + "&poll_id=" + poll_id;
        StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
                        Utils.showToast(NewSubmittedPollQuestionList.this, "No Records Found");
                    else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(NewSubmittedPollQuestionList.this, "Session Expired:Please Login Again");
                    } else if (responseObject.has("poll_List")) {
                        studentSubmittedPollsArray = new JSONObject(response).getJSONArray("poll_List");
                        if (null != studentSubmittedPollsArray && studentSubmittedPollsArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentSubmittedPollsArray.toString().getBytes();
                            VolleySingleton.getInstance(NewSubmittedPollQuestionList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_QUESTION_LIST + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&u_type=" + userType + "&sch_id=" + schoolId + "&device_id=" + Preferences.getInstance().deviceId  + "&poll_id=" + poll_id, e);
                            studentSubmittedPollListView.invalidateViews();
                            studentListviewSubmittedPollsAdapter = new StudentListviewSubmittedPollsAdapter(NewSubmittedPollQuestionList.this, studentSubmittedPollsArray,"3");
                            studentSubmittedPollListView.setAdapter(studentListviewSubmittedPollsAdapter);
                            studentListviewSubmittedPollsAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                           // swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    } else
                        Utils.showToast(NewSubmittedPollQuestionList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(NewSubmittedPollQuestionList.this, "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
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
			}*/
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(NewSubmittedPollQuestionList.this))
            queue.add(requestObject);
        else {
            Utils.showToast(NewSubmittedPollQuestionList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
}
