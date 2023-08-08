package com.schoofi.activitiess;

import android.content.Intent;
import android.net.Uri;
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

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.ResultPDFAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultPdfActivity extends AppCompatActivity {

    private JSONArray studentPollArray;
    private ImageView imageBack;
    private TextView pollTitle;
    private ListView studentPollListView;
    private Button submittedPolls;
    ResultPDFAdapter studentPolllListAdapter;
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
        setContentView(R.layout.activity_result_pdf);
        pollTitle = (TextView) findViewById(R.id.txt_poll);
        //submittedPolls = (Button) findViewById(R.id.btn_submittedPolls);
        studentPollListView = (ListView) findViewById(R.id.listView_students_polls);
       studentPollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               try {
                   if(studentPollArray.getJSONObject(position).getString("show_student_result").matches("1") && studentPollArray.getJSONObject(position).getString("result_pdf").matches("")) {
                       String url;
                       url = "http://schoofi.com" + studentPollArray.getJSONObject(position).getString("form_target") + "?stu_id=" + Preferences.getInstance().studentId + "&session=" + studentPollArray.getJSONObject(position).getString("session") + "&class_id=" + studentPollArray.getJSONObject(position).getString("class_id") + "&ins_id=" + Preferences.getInstance().institutionId + "&school_id=" + Preferences.getInstance().schoolId + "&class_sec_id=" + studentPollArray.getJSONObject(position).getString("class_sec_id") + "&term=" + studentPollArray.getJSONObject(position).getString("term") + "&template_id=" + studentPollArray.getJSONObject(position).getString("template_id") + "&single_page=" + studentPollArray.getJSONObject(position).getString("single_page") + "&show_student_result=" + studentPollArray.getJSONObject(position).getString("show_student_result");
                       Log.d("pp", url);
                       Intent intent = new Intent(ResultPdfActivity.this, WebViewActivity.class);
                       intent.putExtra("position", 1);
                       intent.putExtra("url", url);
                       startActivity(intent);
                   }
                   else
                       if(studentPollArray.getJSONObject(position).getString("show_student_result").matches("1") && !studentPollArray.getJSONObject(position).getString("result_pdf").matches(""))
                       {  String url;
                           url = "http://schoofi.com"+studentPollArray.getJSONObject(position).getString("result_pdf");
                          /* Intent intent = new Intent(ResultPdfActivity.this, WebViewActivity.class);
                           intent.putExtra("position", 1);
                           intent.putExtra("url", url);
                           startActivity(intent);*/

                           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                           startActivity(intent);
                       }
               } catch (Exception e) {
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_RESULT_LIST_STATUS+"?sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&stu_id="+Preferences.getInstance().studentId+"&session="+Preferences.getInstance().session1);
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
            studentPolllListAdapter= new ResultPDFAdapter(ResultPdfActivity.this,studentPollArray);
            studentPollListView.setAdapter(studentPolllListAdapter);
            studentPolllListAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentPollList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(ResultPdfActivity.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_RESULT_LIST_STATUS+"?sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&stu_id="+Preferences.getInstance().studentId+"&session="+Preferences.getInstance().session1;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(ResultPdfActivity.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(ResultPdfActivity.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        studentPollArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=studentPollArray && studentPollArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentPollArray.toString().getBytes();
                            VolleySingleton.getInstance(ResultPdfActivity.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_RESULT_LIST_STATUS+"?sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&stu_id="+Preferences.getInstance().studentId+"&session="+Preferences.getInstance().session1,e);
                            studentPollListView.invalidateViews();
                            studentPolllListAdapter = new ResultPDFAdapter(ResultPdfActivity.this, studentPollArray);
                            studentPollListView.setAdapter(studentPolllListAdapter);
                            studentPolllListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(ResultPdfActivity.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(ResultPdfActivity.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(ResultPdfActivity.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(ResultPdfActivity.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }


}
