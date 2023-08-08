package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.AdmissionStatusListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminAdmissionEnquiryList extends AppCompatActivity {

    ImageView back;
    ListView admissionStatusListView;
    private JSONArray admissionStatusArray;
    AdmissionStatusListViewAdapter admissionStatusListViewAdapter;
    SwipyRefreshLayout swipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_admission_enquiry_list);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        admissionStatusListView = (ListView) findViewById(R.id.admission_listView_status);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getChairmanStudentLeaveList();
            }
        });
        initData();
        getChairmanStudentLeaveList();

        admissionStatusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posistion, long l) {

                Intent intent = new Intent(AdminAdmissionEnquiryList.this,AdminAdmissionEnquiryListDetails.class);
                intent.putExtra("position",posistion);
                startActivity(intent);
            }
        });

    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMISSION_ENQUIRY_STATUS_LIST+"?token="+ Preferences.getInstance().token+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                admissionStatusArray= null;
            }
            else
            {
                admissionStatusArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(admissionStatusArray!= null)
        {
            admissionStatusListViewAdapter= new AdmissionStatusListViewAdapter(AdminAdmissionEnquiryList.this,admissionStatusArray);
            admissionStatusListView.setAdapter(admissionStatusListViewAdapter);
            admissionStatusListViewAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AdminAdmissionEnquiryList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_ADMISSION_ENQUIRY_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                        Utils.showToast(AdminAdmissionEnquiryList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(AdminAdmissionEnquiryList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("enq_list"))
                    {
                        admissionStatusArray= new JSONObject(response).getJSONArray("enq_list");
                        if(null!=admissionStatusArray && admissionStatusArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = admissionStatusArray.toString().getBytes();
                            VolleySingleton.getInstance(AdminAdmissionEnquiryList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_ADMISSION_ENQUIRY_LIST+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId,e);
                            admissionStatusListView.invalidateViews();
                            admissionStatusListViewAdapter= new AdmissionStatusListViewAdapter(AdminAdmissionEnquiryList.this,admissionStatusArray);
                            admissionStatusListView.setAdapter(admissionStatusListViewAdapter);
                            admissionStatusListViewAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(AdminAdmissionEnquiryList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AdminAdmissionEnquiryList.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(AdminAdmissionEnquiryList.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                //params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(AdminAdmissionEnquiryList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(AdminAdmissionEnquiryList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }

    @Override
    protected void onResume() {

        initData();
        getChairmanStudentLeaveList();
        super.onResume();
    }
}
