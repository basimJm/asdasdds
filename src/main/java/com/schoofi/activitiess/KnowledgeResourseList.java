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
import com.schoofi.adapters.KnowledgeListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KnowledgeResourseList extends AppCompatActivity {

    private ListView healthAndAuditTaskList;
    private JSONArray healthAndAuditTaskListArray;
    private KnowledgeListAdapter knowledgeResourseSubjectScreenAdapter;
    ImageView back;
    String subjectId,classId;
    private TextView titleScreen,add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_knowledge_resource_subject_screen);
        healthAndAuditTaskList = (ListView) findViewById(R.id.listViewAddTask);
        back = (ImageView) findViewById(R.id.img_back);
        titleScreen = (TextView) findViewById(R.id.txt_class);
        add = (TextView) findViewById(R.id.txt_teacherLogout);



        titleScreen.setText("Resourse");

        subjectId = getIntent().getStringExtra("subject_id");
        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
        {
           add.setVisibility(View.INVISIBLE);
        }
        else
        {
            classId = getIntent().getStringExtra("class_id");
            add.setVisibility(View.VISIBLE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(KnowledgeResourseList.this,KnowledgeResourseUploadingScreen.class);
                startActivity(intent);
            }
        });

        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
        {
            initData();
            getClassList();
            Log.d("oiu",Preferences.getInstance().userRoleId);
        }

        else
        {
            initData1();
            getClassList1();
        }



        healthAndAuditTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(KnowledgeResourseList.this,KnowledgeResourseDetails.class);
                intent.putExtra("position",position);
                intent.putExtra("subject_id",subjectId);
                if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
                {

                }
                else
                {
                    intent.putExtra("class_id",classId);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if(!Preferences.getInstance().userRoleId.matches("5") || !Preferences.getInstance().userRoleId.matches("6"))
        {
            initData();
            getClassList();
        }

        else
        {
            initData1();
            getClassList1();
        }
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_LIST+"?stu_id="+ Preferences.getInstance().studentId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&class_id="+Preferences.getInstance().studentClassId+"&subject_id="+subjectId);
            if(e == null)
            {
                healthAndAuditTaskListArray= null;
            }
            else
            {
                healthAndAuditTaskListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditTaskListArray!= null)
        {
            knowledgeResourseSubjectScreenAdapter= new KnowledgeListAdapter(KnowledgeResourseList.this,healthAndAuditTaskListArray);
            healthAndAuditTaskList.setAdapter(knowledgeResourseSubjectScreenAdapter);
            knowledgeResourseSubjectScreenAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(KnowledgeResourseList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_LIST+"?stu_id="+ Preferences.getInstance().studentId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&class_id="+Preferences.getInstance().studentClassId+"&subject_id="+subjectId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(KnowledgeResourseList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(KnowledgeResourseList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("knowledge_resource"))
                    {
                        healthAndAuditTaskListArray= new JSONObject(response).getJSONArray("knowledge_resource");
                        if(null!=healthAndAuditTaskListArray && healthAndAuditTaskListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = healthAndAuditTaskListArray.toString().getBytes();
                            VolleySingleton.getInstance(KnowledgeResourseList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_LIST+"?stu_id="+ Preferences.getInstance().studentId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&class_id="+Preferences.getInstance().studentClassId+"&subject_id="+subjectId,e);
                            healthAndAuditTaskList.invalidateViews();
                            knowledgeResourseSubjectScreenAdapter= new KnowledgeListAdapter(KnowledgeResourseList.this,healthAndAuditTaskListArray);
                            healthAndAuditTaskList.setAdapter(knowledgeResourseSubjectScreenAdapter);
                            knowledgeResourseSubjectScreenAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(KnowledgeResourseList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(KnowledgeResourseList.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(KnowledgeResourseList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(KnowledgeResourseList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void initData1()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&class_number="+classId+"&subject_id="+subjectId);
            if(e == null)
            {
                healthAndAuditTaskListArray= null;
            }
            else
            {
                healthAndAuditTaskListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditTaskListArray!= null)
        {
            knowledgeResourseSubjectScreenAdapter= new KnowledgeListAdapter(KnowledgeResourseList.this,healthAndAuditTaskListArray);
            healthAndAuditTaskList.setAdapter(knowledgeResourseSubjectScreenAdapter);
            knowledgeResourseSubjectScreenAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(KnowledgeResourseList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&class_number="+classId+"&subject_id="+subjectId;
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
                        Utils.showToast(KnowledgeResourseList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(KnowledgeResourseList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("knowledge_resource"))
                    {
                        healthAndAuditTaskListArray= new JSONObject(response).getJSONArray("knowledge_resource");
                        if(null!=healthAndAuditTaskListArray && healthAndAuditTaskListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = healthAndAuditTaskListArray.toString().getBytes();
                            VolleySingleton.getInstance(KnowledgeResourseList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&class_number="+classId+"&subject_id="+subjectId,e);
                            healthAndAuditTaskList.invalidateViews();
                            knowledgeResourseSubjectScreenAdapter= new KnowledgeListAdapter(KnowledgeResourseList.this,healthAndAuditTaskListArray);
                            healthAndAuditTaskList.setAdapter(knowledgeResourseSubjectScreenAdapter);
                            knowledgeResourseSubjectScreenAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(KnowledgeResourseList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(KnowledgeResourseList.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(KnowledgeResourseList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(KnowledgeResourseList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }

    private void toa()
    {
        System.out.println("aaa");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.health_and_audit_task_list, menu);
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
}
