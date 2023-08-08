package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
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
import com.schoofi.adapters.DiaryNewDesignAdapter;
import com.schoofi.adapters.EmployeeListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.NewDiaryVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DiaryNewDesignActivity extends AppCompatActivity {

    ImageView back,newPlus;
    ListView teacherDirectoryListView;
    JSONArray teacherDirectoryArray;
    DiaryNewDesignAdapter employeeListAdapter;
    String type="";
    JSONArray jsonArray,jsonArray1;
    JSONObject jsonObject,jsonObject1;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_diary_new_design);

        back = (ImageView) findViewById(R.id.img_back);
        title = (TextView) findViewById(R.id.txt_class);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newPlus = (ImageView) findViewById(R.id.img_calender1);
        Preferences.getInstance().loadPreference(getApplicationContext());
        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
        {
            newPlus.setVisibility(View.GONE);
        }
        else
        {
            newPlus.setVisibility(View.VISIBLE);
        }

        type = getIntent().getStringExtra("type");
        title.setText(type);

        newPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DiaryNewDesignActivity.this,TeacherNewDiaryUploadScreen.class);
                intent.putExtra("type",type);

                startActivity(intent);
            }
        });



        teacherDirectoryListView = (ListView) findViewById(R.id.listViewInnerAllAssignment);

        Preferences.getInstance().loadPreference(getApplicationContext());
        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
        {
            //initData();
            getTeacherClassList();
        }
        else
        {
            //initData1();
            getTeacherClassList1();
        }




    }


    private void initData1()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_URL+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&type="+type+"&teacher_id="+Preferences.getInstance().userId);
            if(e == null)
            {
                teacherDirectoryArray= null;
            }
            else
            {
                teacherDirectoryArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherDirectoryArray!= null)
        {
            employeeListAdapter= new DiaryNewDesignAdapter(DiaryNewDesignActivity.this,teacherDirectoryArray);
            teacherDirectoryListView.setAdapter(employeeListAdapter);
            employeeListAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_URL;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                Log.d("response",response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    //toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))

                    {

                        teacherDirectoryArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=teacherDirectoryArray && teacherDirectoryArray.length()>=0)
                        {
                            jsonObject1 = new JSONObject();
                            jsonArray1 = new JSONArray();
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherDirectoryArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_URL+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&type="+type+"&teacher_id="+Preferences.getInstance().userId,e);
                            /*for(int i=0;i<teacherDirectoryArray.length();i++)
                            {
                                jsonObject = teacherDirectoryArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");
                                for(int j=0;j<jsonArray.length();j++)
                                {

                                }

//                                jsonObject1 = new JSONObject(String.valueOf(jsonArray));
                                jsonArray1.put(jsonArray);

                            }

                            JSONArray jsonArr = new JSONArray(jsonArray1);

                            for (int i = 0; i < jsonArr.length(); i++)
                            {
                                JSONObject jsonObj = jsonArr.getJSONObject(i);

                                System.out.println(jsonObj);
                            }

                            Log.d("p",jsonArray1.toString());
                            //jsonObject1 = jsonArray1.getJSONObject(0);
                            Log.d("p",jsonArray1.getJSONObject(0).getString("role_id"));*/

                            Log.d("po",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_URL+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&type="+type+"&teacher_id="+Preferences.getInstance().userId);

                            NewDiaryVO newDiaryVO = new NewDiaryVO();
                            jsonObject1 = new JSONObject();
                            jsonArray1 = new JSONArray();
                            for(int i=0;i<teacherDirectoryArray.length();i++)
                            {

                                jsonObject = teacherDirectoryArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");
                                for(int j=0;j<jsonArray.length();j++)
                                {
                                    newDiaryVO.setRole_id(jsonArray.getJSONObject(j).getString("role_id"));
                                    jsonObject1 = jsonArray.getJSONObject(j);
                                    jsonArray1.put(jsonObject1);
                                }
                            }

                            Log.d("pooo",newDiaryVO.getRole_id());
                            Log.d("pooo",jsonArray1.toString());
                            int y=0;
                            for(int k=0;k<jsonArray1.length();k++)
                            {
                                //Log.d("poo",jsonArray1.getJSONObject(k).getString("crr_date"));
                                y=k;
                            }

                            Log.d("poo",String.valueOf(y));


                          teacherDirectoryListView.invalidateViews();
                          employeeListAdapter= new DiaryNewDesignAdapter(DiaryNewDesignActivity.this,jsonArray1);
                          teacherDirectoryListView.setAdapter(employeeListAdapter);
                          employeeListAdapter.notifyDataSetChanged();
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
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
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);

                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("type",type);
                params.put("teacher_id",Preferences.getInstance().userId);

                //params.put("crr_date",currentDate);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_URL+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&type="+type+"&stu_id="+Preferences.getInstance().studentId);
            if(e == null)
            {
                teacherDirectoryArray= null;
            }
            else
            {
                teacherDirectoryArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherDirectoryArray!= null)
        {
            employeeListAdapter= new DiaryNewDesignAdapter(DiaryNewDesignActivity.this,teacherDirectoryArray);
            teacherDirectoryListView.setAdapter(employeeListAdapter);
            employeeListAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_URL;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                Log.d("response",response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    //toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))

                    {

                        teacherDirectoryArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=teacherDirectoryArray && teacherDirectoryArray.length()>=0)
                        {
                            jsonObject1 = new JSONObject();
                            jsonArray1 = new JSONArray();
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherDirectoryArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_URL+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&type="+type+"&stu_id="+Preferences.getInstance().studentId,e);
                            NewDiaryVO newDiaryVO = new NewDiaryVO();
                            jsonObject1 = new JSONObject();
                            jsonArray1 = new JSONArray();
                            for(int i=0;i<teacherDirectoryArray.length();i++)
                            {

                                jsonObject = teacherDirectoryArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");
                                for(int j=0;j<jsonArray.length();j++)
                                {
                                    newDiaryVO.setRole_id(jsonArray.getJSONObject(j).getString("role_id"));
                                    jsonObject1 = jsonArray.getJSONObject(j);
                                    jsonArray1.put(jsonObject1);
                                }
                            }

                            Log.d("pooo",newDiaryVO.getRole_id());
                            Log.d("pooo",jsonArray1.toString());
                            int y=0;
                            for(int k=0;k<jsonArray1.length();k++)
                            {
                                //Log.d("poo",jsonArray1.getJSONObject(k).getString("crr_date"));
                                y=k;
                            }

                            Log.d("poo",String.valueOf(y));


                            teacherDirectoryListView.invalidateViews();
                            employeeListAdapter= new DiaryNewDesignAdapter(DiaryNewDesignActivity.this,jsonArray1);
                            teacherDirectoryListView.setAdapter(employeeListAdapter);
                            employeeListAdapter.notifyDataSetChanged();
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
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
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);

                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("type",type);
                params.put("stu_id",Preferences.getInstance().studentId);

                //params.put("crr_date",currentDate);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Preferences.getInstance().loadPreference(getApplicationContext());
        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
        {
            //initData();
            getTeacherClassList();
        }
        else
        {
            //initData1();
            getTeacherClassList1();
        }
    }
}
