package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.schoofi.adapters.ParentHomeScreenAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VisitorTypeVO;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smtchahal.materialspinner.MaterialSpinner;

public class VisitorNewMainScreen extends AppCompatActivity {
    private ImageView back;
    private MaterialSpinner materialVisitorType;
    private ListView studentListView;
    private Button scan,submit;
    private EditText cardNumberIssued;
    ArrayList<String> visitorName;
    ArrayList<VisitorTypeVO> visitorId;
    JSONObject jsonobject,jsonobject1;
    JSONArray jsonarray,jsonarray1;
    private JSONArray adminHealthArray;
    String visitorId1,sectionId1,classId1,parentId1;
    private int STORAGE_PERMISSION_CODE = 23;
    private JSONArray parentHomeScreenArray;
    private ParentHomeScreenAdapter parentHomeScreenAdapter;
    String value="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_visitor_new_main_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        materialVisitorType = (MaterialSpinner) findViewById(R.id.spinner_visitor_type);
        materialVisitorType.setBackgroundResource(R.drawable.grey_button);
        studentListView = (ListView) findViewById(R.id.listView_students);
        //cardNumberIssued = (EditText) findViewById(R.id.edit_visitor_card_issued);
       // materialVisitorType.setVisibility(View.GONE);
        studentListView.setVisibility(View.GONE);
        scan = (Button) findViewById(R.id.btn_visitor_scan);
        value = getIntent().getStringExtra("value");

        submit = (Button) findViewById(R.id.btn_visitor_submit);
       // submit.setVisibility(View.GONE);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions()) {
                    Intent intent = new Intent(VisitorNewMainScreen.this, VisitorQrCodeScanner.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        if(value.matches("1"))
        {
            //Preferences.getInstance().loadPreference(getApplicationContext());
            //cardNumberIssued.setText(Preferences.getInstance().visitorQrCode);
            getStudentFeedList();
        }

        else
        {

        }

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                   // Utils.showToast(getApplicationContext(),parentHomeScreenArray.getJSONObject(position).getString("stu_id"));
                    Intent intent = new Intent(VisitorNewMainScreen.this,VisitorEntryFrom.class);
                    intent.putExtra("stu_id",parentHomeScreenArray.getJSONObject(position).getString("stu_id"));
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitorNewMainScreen.this,VisitorEntryFrom.class);
                intent.putExtra("visitor_type",visitorId1);
                intent.putExtra("value","0");
                startActivity(intent);
            }
        });


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
            Log.d("URL", AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_TYPE+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
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
                    .setAdapter(new ArrayAdapter<String>(VisitorNewMainScreen.this,
                            android.R.layout.simple_spinner_dropdown_item,visitorName
                    ));

            materialVisitorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    visitorId1 = visitorId.get(position).getVisitorTypeId().toString();
                    //System.out.println(groupId1);











                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }




    protected void getStudentFeedList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(VisitorNewMainScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_QR_CODE_MATCH+"?qr_code="+Preferences.getInstance().visitorQrCode+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId;
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
                        submit.setVisibility(View.VISIBLE);
                        new DownloadJSON1().execute();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        //studentListView.setVisibility(View.GONE);
                        materialVisitorType.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("Student_details"))
                    {
                        studentListView.setVisibility(View.VISIBLE);
                        materialVisitorType.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                        parentHomeScreenArray= new JSONObject(response).getJSONArray("Student_details");
                        if(null!=parentHomeScreenArray && parentHomeScreenArray.length()>=0)
                        {

                            Cache.Entry e = new Cache.Entry();
                            e.data = parentHomeScreenArray.toString().getBytes();
                            VolleySingleton.getInstance(VisitorNewMainScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_QR_CODE_MATCH+"?qr_code="+Preferences.getInstance().visitorQrCode+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId,e);

                            studentListView.invalidateViews();
                            parentHomeScreenAdapter = new ParentHomeScreenAdapter(VisitorNewMainScreen.this, parentHomeScreenArray);
                            studentListView.setAdapter(parentHomeScreenAdapter);
                            parentHomeScreenAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);
                   // studentListView.setVisibility(View.GONE);
                    materialVisitorType.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                   // studentListView.setVisibility(View.GONE);
                    materialVisitorType.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
               // studentListView.setVisibility(View.GONE);
                materialVisitorType.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
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
            materialVisitorType.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }





    }

