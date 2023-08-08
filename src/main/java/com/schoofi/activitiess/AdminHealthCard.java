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
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.HealthCardStudentListAdapter;
import com.schoofi.adapters.StudentPolllListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ClassVO;
import com.schoofi.utils.EventUploadAudienceVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SectionVO;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smtchahal.materialspinner.MaterialSpinner;


public class AdminHealthCard extends AppCompatActivity {

   private ImageView back;
    private Button next;
    private MaterialSpinner materialSpinnerClass,materialSpinnerSection;
    private ListView studentListView;
    private JSONArray adminHealthArray;
    private HealthCardStudentListAdapter healthCardStudentListAdapter;
    ArrayList<String> className;
    ArrayList<String> sectionName;
    ArrayList<ClassVO> classId;
    ArrayList<SectionVO> sectionId;
    JSONObject jsonobject,jsonobject1;
    JSONArray jsonarray,jsonarray1;
    String classId1,sectionId1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_health_card);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next = (Button) findViewById(R.id.btn_next);
        materialSpinnerClass = (MaterialSpinner) findViewById(R.id.spinner_class);
        materialSpinnerSection = (MaterialSpinner) findViewById(R.id.spinner_section);

        materialSpinnerClass.setBackgroundResource(R.drawable.grey_button);
        materialSpinnerSection.setBackgroundResource(R.drawable.grey_button);
        studentListView = (ListView) findViewById(R.id.listview_student_list);
        next.setVisibility(View.GONE);

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    Intent intent = new Intent(AdminHealthCard.this,AdminHealthCardOptionsActivity.class);
                    Preferences.getInstance().loadPreference(getApplicationContext());
                    Preferences.getInstance().studentId = adminHealthArray.getJSONObject(position).getString("stu_id");
                    Preferences.getInstance().session1 = adminHealthArray.getJSONObject(position).getString("session");
                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //initData();
        //getStudentPollList();

        new DownloadJSON().execute();

    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId1+"&sec_id="+sectionId1);
            if(e == null)
            {
                adminHealthArray= null;
            }
            else
            {
                adminHealthArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(adminHealthArray!= null)
        {
            healthCardStudentListAdapter= new HealthCardStudentListAdapter(AdminHealthCard.this,adminHealthArray);
            studentListView.setAdapter(healthCardStudentListAdapter);
            healthCardStudentListAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentPollList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AdminHealthCard.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId1+"&sec_id="+sectionId1;
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
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("classSectionStudents"))
                    {
                        adminHealthArray= new JSONObject(response).getJSONArray("classSectionStudents");
                        if(null!=adminHealthArray && adminHealthArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminHealthArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId1+"&sec_id="+sectionId1,e);
                            studentListView.invalidateViews();
                            healthCardStudentListAdapter= new HealthCardStudentListAdapter(AdminHealthCard.this,adminHealthArray);
                            studentListView.setAdapter(healthCardStudentListAdapter);
                            healthCardStudentListAdapter.notifyDataSetChanged();

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
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }


    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            classId = new ArrayList<ClassVO>();
            className = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_CLASS_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("classSectionStudents");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    ClassVO classVO = new ClassVO();

                    classVO.setClassId(jsonobject.optString("class_id"));
                    classId.add(classVO);

                    className.add(jsonobject.optString("class_name"));

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

            materialSpinnerClass
                    .setAdapter(new ArrayAdapter<String>(AdminHealthCard.this,
                            android.R.layout.simple_spinner_dropdown_item,className
                    ));

            materialSpinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    classId1 = classId.get(position).getClassId().toString();
                    //System.out.println(groupId1);

                    new DownloadJSON1().execute();



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


            // Create an array to populate the spinner
            sectionId = new ArrayList<SectionVO>();
            sectionName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            Log.d("URL",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_SECTION_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId1);
            jsonobject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_SECTION_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId1);
            try {
                // Locate the NodeList name
                jsonarray1 = jsonobject1.getJSONArray("classSectionStudents");
                for (int i = 0; i < jsonarray1.length(); i++) {
                    jsonobject1 = jsonarray1.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    //ClassVO classVO = new ClassVO();
                    SectionVO sectionVO = new SectionVO();

                    sectionVO.setSectionId(jsonobject1.optString("class_section_id"));
                    sectionId.add(sectionVO);

                    sectionName.add(jsonobject1.optString("section_name"));

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

            materialSpinnerSection
                    .setAdapter(new ArrayAdapter<String>(AdminHealthCard.this,
                            android.R.layout.simple_spinner_dropdown_item,sectionName
                    ));

            materialSpinnerSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    sectionId1 = sectionId.get(position).getSectionId().toString();
                    //System.out.println(groupId1);

                    initData();
                    getStudentPollList();



                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }
}
