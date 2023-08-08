package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.KnowledgeResourseAdminSubjectListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KnowledgeResourseAdminSubjectList extends AppCompatActivity {
    ImageView back;
    SwipyRefreshLayout swipyRefreshLayout;
    ListView adminClassListView;
    KnowledgeResourseAdminSubjectListAdapter adminClassListAdapter;
    JSONArray adminClassListArray;
    String className1;
    private TextView totalClassStudents,titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_knowledge_resourse_class_list_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.txt_class);
        titleTextView.setText("Subjects");

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        adminClassListView = (ListView) findViewById(R.id.listViewAdminBusListView);

        className1 = getIntent().getStringExtra("class_number");

        Log.d("op",className1);


        initData();
        getChairmanStudentLeaveList();

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                initData();
                getChairmanStudentLeaveList();
            }
        });

        adminClassListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                try {
                    Intent intent = new Intent(KnowledgeResourseAdminSubjectList.this,KnowledgeResourseList.class);
                    intent.putExtra("class_id",className1);
                    intent.putExtra("subject_id",adminClassListArray.getJSONObject(i).getString("subject_id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_SUBJECT_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&class_number="+className1);
            if(e == null)
            {
                adminClassListArray= null;
            }
            else
            {
                adminClassListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(adminClassListArray!= null)
        {

            adminClassListAdapter = new KnowledgeResourseAdminSubjectListAdapter(KnowledgeResourseAdminSubjectList.this, adminClassListArray);
            adminClassListView.setAdapter(adminClassListAdapter);
            adminClassListAdapter.notifyDataSetChanged();
            swipyRefreshLayout.setRefreshing(false);
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(KnowledgeResourseAdminSubjectList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_SUBJECT_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                Log.d("po",response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(KnowledgeResourseAdminSubjectList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(KnowledgeResourseAdminSubjectList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("subject_lists"))
                    {
                        adminClassListArray= new JSONObject(response).getJSONArray("subject_lists");


                        if(null!=adminClassListArray && adminClassListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminClassListArray.toString().getBytes();
                            VolleySingleton.getInstance(KnowledgeResourseAdminSubjectList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_SUBJECT_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&class_number="+className1,e);

                            adminClassListView.invalidateViews();

                            adminClassListAdapter = new KnowledgeResourseAdminSubjectListAdapter(KnowledgeResourseAdminSubjectList.this, adminClassListArray);
                            adminClassListView.setAdapter(adminClassListAdapter);
                            adminClassListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(KnowledgeResourseAdminSubjectList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(KnowledgeResourseAdminSubjectList.this, "Error fetching modules! Please try after sometime.");
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
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(KnowledgeResourseAdminSubjectList.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("class_number",className1);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(KnowledgeResourseAdminSubjectList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(KnowledgeResourseAdminSubjectList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
