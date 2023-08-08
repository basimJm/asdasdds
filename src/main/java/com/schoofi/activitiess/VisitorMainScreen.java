package com.schoofi.activitiess;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.schoofi.adapters.ParentHomeScreenAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ClassSectionVO;
import com.schoofi.utils.ClassVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SectionVO;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VisitorTypeVO;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smtchahal.materialspinner.MaterialSpinner;


public class VisitorMainScreen extends AppCompatActivity {

    private ImageView back;
    private MaterialSpinner materialSpinnerClassSection,materialVisitorType,materialParentType;
    private ListView studentListView;
    ArrayList<String> className;
    ArrayList<String> visitorName;
    ArrayList<VisitorTypeVO> visitorId;
    ArrayList<ClassSectionVO> sectionId,classId;
    JSONObject jsonobject,jsonobject1;
    JSONArray jsonarray,jsonarray1;
    private JSONArray adminHealthArray;
    String visitorId1,sectionId1,classId1,parentId1;
    private HealthCardStudentListAdapter healthCardStudentListAdapter;
    private Button visitorSubmitButton,scan;
    public static final CharSequence[] DAYS_OPTIONS  = {"Mother","Father","Other"};
    private int STORAGE_PERMISSION_CODE = 23;
    private String value;
    private JSONArray parentHomeScreenArray;
    private ParentHomeScreenAdapter parentHomeScreenAdapter;
    private String  cool="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_visitor_main_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scan = (Button) findViewById(R.id.btn_visitor_scan);

        materialSpinnerClassSection = (MaterialSpinner) findViewById(R.id.spinner_class);
        materialSpinnerClassSection.setBackgroundResource(R.drawable.grey_button);
        materialParentType = (MaterialSpinner) findViewById(R.id.spinner_parent_type);
        materialParentType.setBackgroundResource(R.drawable.grey_button);
        materialParentType.setVisibility(View.GONE);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions()) {
                    Intent intent = new Intent(VisitorMainScreen.this, VisitorQrCodeScanner.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        value = getIntent().getStringExtra("value");


        materialVisitorType = (MaterialSpinner) findViewById(R.id.spinner_visitor_type);
        materialVisitorType.setBackgroundResource(R.drawable.grey_button);


        studentListView = (ListView) findViewById(R.id.listView_students);

        materialSpinnerClassSection.setVisibility(View.GONE);
        studentListView.setVisibility(View.GONE);

        visitorSubmitButton = (Button) findViewById(R.id.btn_visitor_submit);
        visitorSubmitButton.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (VisitorMainScreen.this, R.layout.support_simple_spinner_dropdown_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        materialParentType.setAdapter(adapter); // Apply the adapter to the spinner







        materialParentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                parentId1 = "" + parent.getItemAtPosition(position);

            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });

        new DownloadJSON1().execute();

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    if(cool.matches("1"))
                    {
                        Preferences.getInstance().loadPreference(getApplicationContext());
                        Preferences.getInstance().studentId = parentHomeScreenArray.getJSONObject(position).getString("stu_id");
                        Preferences.getInstance().savePreference(getApplicationContext());
                        Intent intent = new Intent(VisitorMainScreen.this,VisitorEntryFrom.class);

                        intent.putExtra("class_id",parentHomeScreenArray.getJSONObject(position).getString("class_id"));
                        intent.putExtra("section_id",parentHomeScreenArray.getJSONObject(position).getString("section_id"));
                        if(parentHomeScreenArray.getJSONObject(position).getString("mother_qr_code").matches(Preferences.getInstance().visitorQrCode))
                        {
                            intent.putExtra("parent_type","Mother");
                        }

                        else
                        {
                            intent.putExtra("parent_type","Father");
                        }
                        //intent.putExtra("visitor_type",visitorId1);

                        intent.putExtra("value","2");
                        intent.putExtra("visitor_type","1");
                        //Utils.showToast(getApplicationContext(),classId1+"-"+sectionId1+"-"+visitorId1+"-"+parentId1);
                        startActivity(intent);
                    }

                    else {
                        Preferences.getInstance().loadPreference(getApplicationContext());
                        Preferences.getInstance().studentId = adminHealthArray.getJSONObject(position).getString("stu_id");
                        Preferences.getInstance().savePreference(getApplicationContext());
                        Intent intent = new Intent(VisitorMainScreen.this, VisitorEntryFrom.class);

                        intent.putExtra("class_id", classId1);
                        intent.putExtra("section_id", sectionId1);
                        intent.putExtra("visitor_type", visitorId1);
                        intent.putExtra("parent_type", parentId1);
                        intent.putExtra("value", "2");
                        //Utils.showToast(getApplicationContext(),classId1+"-"+sectionId1+"-"+visitorId1+"-"+parentId1);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        visitorSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(VisitorMainScreen.this,VisitorEntryFrom.class);
                intent.putExtra("visitor_type",visitorId1);
                intent.putExtra("value","0");
                startActivity(intent);

            }
        });

        if(value.matches("1"))
        {
            getStudentFeedList();

        }

        else
        {

        }




    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            sectionId = new ArrayList<ClassSectionVO>();
            className = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            Log.d("k",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_CLASS_SECTION_LIST_VISITOR+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_CLASS_SECTION_LIST_VISITOR+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("responseObject");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    ClassSectionVO classSectionVO = new ClassSectionVO();

                    classSectionVO.setSectionId(jsonobject.optString("class_section_id"));
                    classSectionVO.setClassId(jsonobject.optString("class_id"));
                    sectionId.add(classSectionVO);


                    className.add(jsonobject.optString("section_class"));

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

            materialSpinnerClassSection
                    .setAdapter(new ArrayAdapter<String>(VisitorMainScreen.this,
                            android.R.layout.simple_spinner_dropdown_item,className
                    ));

            materialSpinnerClassSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    sectionId1 = sectionId.get(position).getSectionId().toString();
                    classId1 = sectionId.get(position).getClassId().toString();
                    visitorSubmitButton.setVisibility(View.GONE);
                    //System.out.println(groupId1);

                    //new AdminHealthCard.DownloadJSON1().execute();

                    studentListView.setVisibility(View.VISIBLE);

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

    private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            visitorId = new ArrayList<VisitorTypeVO>();
            visitorName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            Log.d("URL",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_TYPE+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
            jsonobject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_TYPE+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                jsonarray1 = jsonobject1.getJSONArray("responseObject");
                for (int i = 0; i < jsonarray1.length(); i++) {
                    jsonobject1 = jsonarray1.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    //ClassVO classVO = new ClassVO();
                    VisitorTypeVO visitorTypeVO = new VisitorTypeVO();

                    visitorTypeVO.setVisitorTypeId(jsonobject1.optString("id"));
                    visitorId.add(visitorTypeVO);

                    visitorName.add(jsonobject1.optString("name"));

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

            materialVisitorType
                    .setAdapter(new ArrayAdapter<String>(VisitorMainScreen.this,
                            android.R.layout.simple_spinner_dropdown_item,visitorName
                    ));

            materialVisitorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    visitorId1 = visitorId.get(position).getVisitorTypeId().toString();
                    //System.out.println(groupId1);

                    if(visitorName.get(position).matches("Parent"))
                    {
                        materialSpinnerClassSection.setVisibility(View.VISIBLE);
                        materialParentType.setVisibility(View.VISIBLE);
                        new DownloadJSON().execute();

                        visitorSubmitButton.setVisibility(View.GONE);
                    }

                    else
                    {
                        materialSpinnerClassSection.setVisibility(View.GONE);
                        visitorSubmitButton.setVisibility(View.VISIBLE);
                        materialParentType.setVisibility(View.GONE);
                        studentListView.setVisibility(View.GONE);

                    }







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
            healthCardStudentListAdapter= new HealthCardStudentListAdapter(VisitorMainScreen.this,adminHealthArray);
            studentListView.setAdapter(healthCardStudentListAdapter);
            healthCardStudentListAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentPollList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(VisitorMainScreen.this).getRequestQueue();
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
                            healthCardStudentListAdapter= new HealthCardStudentListAdapter(VisitorMainScreen.this,adminHealthArray);
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

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),STORAGE_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    protected void getStudentFeedList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_QR_CODE_MATCH+"?qr_code="+Preferences.getInstance().visitorQrCode+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId;
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
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Students Found");
                        //studentListView.setVisibility(View.GONE);
                        materialVisitorType.setVisibility(View.VISIBLE);
                        studentListView.setVisibility(View.GONE);
                        cool="0";

                        //new VisitorNewMainScreen.DownloadJSON1().execute();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        materialVisitorType.setVisibility(View.GONE);
                        studentListView.setVisibility(View.GONE);
                        //studentListView.setVisibility(View.GONE);

                    }
                    else
                    if(responseObject.has("Student_details"))
                    {
                        studentListView.setVisibility(View.VISIBLE);
                        materialVisitorType.setVisibility(View.GONE);
                        cool="1";

                        parentHomeScreenArray= new JSONObject(response).getJSONArray("Student_details");
                        if(null!=parentHomeScreenArray && parentHomeScreenArray.length()>=0)
                        {

                            Cache.Entry e = new Cache.Entry();
                            e.data = parentHomeScreenArray.toString().getBytes();
                            VolleySingleton.getInstance(VisitorMainScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_QR_CODE_MATCH+"?qr_code="+Preferences.getInstance().visitorQrCode+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId,e);

                            studentListView.invalidateViews();
                            parentHomeScreenAdapter = new ParentHomeScreenAdapter(VisitorMainScreen.this, parentHomeScreenArray);
                            studentListView.setAdapter(parentHomeScreenAdapter);
                            parentHomeScreenAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);
                    // studentListView.setVisibility(View.GONE);
                   // studentListView.setVisibility(View.GONE);
                    //materialVisitorType.setVisibility(View.VISIBLE);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                   // studentListView.setVisibility(View.GONE);
                    //materialVisitorType.setVisibility(View.VISIBLE);
                    // studentListView.setVisibility(View.GONE);

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
              //  studentListView.setVisibility(View.GONE);
               // materialVisitorType.setVisibility(View.VISIBLE);
                // studentListView.setVisibility(View.GONE);

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
            //studentListView.setVisibility(View.GONE);
            //materialVisitorType.setVisibility(View.VISIBLE);
            //studentListView.setVisibility(View.GONE);

        }
    }
}
