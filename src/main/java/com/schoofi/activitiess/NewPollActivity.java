package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.schoofi.adapters.StudentPolllListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewPollActivity extends AppCompatActivity {

    private JSONArray studentPollArray;
    private ImageView imageBack;
    private TextView pollTitle;
    private ListView studentPollListView;
    private Button submittedPolls;
    StudentPolllListAdapter studentPolllListAdapter;
    String studentId = Preferences.getInstance().studentId;
    String userId = Preferences.getInstance().userId;
    String userEmailId = Preferences.getInstance().userEmailId;
    String token = Preferences.getInstance().token;
    String schoolId = Preferences.getInstance().schoolId;
    String userType = Preferences.getInstance().userType;
    private SwipyRefreshLayout swipyRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_new_poll);
        Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Student Poll");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        pollTitle = (TextView) findViewById(R.id.txt_poll);
        submittedPolls = (Button) findViewById(R.id.btn_submittedPolls);
        studentPollListView = (ListView) findViewById(R.id.listView_students_polls);
        studentPollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub


                try {
                    Preferences.getInstance().loadPreference(getApplicationContext());
                    if(Preferences.getInstance().userRoleId.matches("3") || Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8") || Preferences.getInstance().userRoleId.matches("2"))
                    {
                        Intent intent = new Intent(NewPollActivity.this, NewSubmittedPollQuestionList.class);
                        intent.putExtra("index", 0);
                        intent.putExtra("poll_id", studentPollArray.getJSONObject(position).getString("poll_id"));
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(NewPollActivity.this, NewPollQuestionList.class);
                        intent.putExtra("index", 0);
                        intent.putExtra("poll_id", studentPollArray.getJSONObject(position).getString("poll_id"));
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }


        });
        imageBack = (ImageView) findViewById(R.id.img_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
        getStudentPollList();
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getStudentPollList();
            }
        });
		/*{
		    "poll_List": [
		        {
		            "q_id": "11",
		            "ques": "want diwali leave ?",
		            "choice_a": "yes",
		            "ques_for": "2",
		            "choice_b": "no",
		            "choice_c": "may be",
		            "choice_d": "dont know",
		            "pub_date": "2015-11-10"
		        }
		    ]
		}*/

		Preferences.getInstance().loadPreference(getApplicationContext());
		if(Preferences.getInstance().userRoleId.matches("3") || Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8") || Preferences.getInstance().userRoleId.matches("2"))
        {
            submittedPolls.setVisibility(View.GONE);
        }
        else
        {
            submittedPolls.setVisibility(View.VISIBLE);
        }

        submittedPolls.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(NewPollActivity.this,NewSubmittedPollList.class);
                startActivity(intent);



            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //initData();
        getStudentPollList();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_poll, menu);
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&session="+Preferences.getInstance().session1);
            if(e == null)
            {
                studentPollArray= null;
            }
            else
            {
                studentPollArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentPollArray!= null)
        {
            studentPollListView.invalidateViews();
            studentPolllListAdapter= new StudentPolllListAdapter(NewPollActivity.this,studentPollArray);
            studentPollListView.setAdapter(studentPolllListAdapter);
            studentPolllListAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentPollList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(NewPollActivity.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&session="+Preferences.getInstance().session1;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        Utils.showToast(NewPollActivity.this,"No Records Found");
                        studentPollListView.setVisibility(View.GONE);
                    }

                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(NewPollActivity.this, "Session Expired:Please Login Again");
                        studentPollListView.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("poll_List"))
                    {
                        studentPollArray= new JSONObject(response).getJSONArray("poll_List");
                        studentPollListView.setVisibility(View.VISIBLE);
                        if(null!=studentPollArray && studentPollArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentPollArray.toString().getBytes();
                            VolleySingleton.getInstance(NewPollActivity.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&session="+Preferences.getInstance().session1,e);
                            studentPollListView.invalidateViews();
                            studentPolllListAdapter = new StudentPolllListAdapter(NewPollActivity.this, studentPollArray);
                            studentPollListView.setAdapter(studentPolllListAdapter);
                            studentPolllListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(NewPollActivity.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(NewPollActivity.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(NewPollActivity.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(NewPollActivity.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
