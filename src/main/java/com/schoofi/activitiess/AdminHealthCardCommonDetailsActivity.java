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

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.adapters.AdminHealthCardCommonDetailsListViewAdapter;
import com.schoofi.adapters.AdminHealthCardCommonListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class AdminHealthCardCommonDetailsActivity extends AppCompatActivity {

    ImageView back;
    SwipyRefreshLayout swipyRefreshLayout;
    ListView adminHealthCardListView;
    private JSONArray adminHealthCardCommonListViewArray;
    private AdminHealthCardCommonDetailsListViewAdapter adminHealthCardCommonDetailsListViewAdapter;
    String URL = "";
    String value;
    String id;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> descriptions = new ArrayList<String>();
    ArrayList<String> aList = new ArrayList<String>();
    String t,t1;
    String array,array1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_health_card_common_details);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adminHealthCardListView = (ListView) findViewById(R.id.listViewAdminBusListView);
        value = getIntent().getStringExtra("value");
        id = getIntent().getStringExtra("id");
        if(value.matches("1"))
        {
            URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ADMIN_HEALTH_COMMON_VACCINATION_DETAILS;
        }

        else
            if(value.matches("2"))
            {
                URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ADMIN_HEALTH_MEDICAL_HISTORY_DETAILS;
            }

            else
            if(value.matches("3"))
            {
                URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ADMIN_HEALTH_COMMON_INSURANCE_DETAILS;
            }

            initData();

            getChairmanStudentLeaveList();

        adminHealthCardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(titles.get(position).matches("Documents"))
                {
                    try {
                        aList = new ArrayList<String>(Arrays.asList(adminHealthCardCommonListViewArray.getJSONObject(0).getString("document").split(",")));
                        Intent intent = new Intent(AdminHealthCardCommonDetailsActivity.this,AdminHealhCardDocumentsActivity.class);
                        intent.putExtra("array",aList);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        getChairmanStudentLeaveList();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(URL+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&id="+id);
            if(e == null)
            {
                adminHealthCardCommonListViewArray= null;
            }
            else
            {
                adminHealthCardCommonListViewArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(adminHealthCardCommonListViewArray!= null)
        {



            try {
                adminHealthCardListView.invalidateViews();
                t = adminHealthCardCommonListViewArray.getJSONObject(0).getString("parameters_values");
                //Utils.showToast(getApplicationContext(),t);
                t1 = adminHealthCardCommonListViewArray.getJSONObject(0).getString("parameters");
                array = t1.substring(1,t1.length()-1);
                array1 = t.substring(1,t.length()-1);
                array =array.replaceAll("\"", "");
                array1 =array1.replaceAll("\"", "");
                titles = new ArrayList<String>(Arrays.asList(array.split(",")));
                descriptions = new ArrayList<String>(Arrays.asList(array1.split(",")));
                int count=0;
                int m=0;
                for(int l=1;l<=titles.size();l++)
                {
                    count++;
                }

                m= count;

                if(value.matches("2")) {

                    titles.add(m, "Documents");
                    descriptions.add(m, adminHealthCardCommonListViewArray.getJSONObject(0).getString("document"));
                }
                adminHealthCardCommonDetailsListViewAdapter = new AdminHealthCardCommonDetailsListViewAdapter(AdminHealthCardCommonDetailsActivity.this, titles,descriptions);
                adminHealthCardListView.setAdapter(adminHealthCardCommonDetailsListViewAdapter);
                adminHealthCardCommonDetailsListViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Utils.showToast(getApplicationContext(),t);

        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AdminHealthCardCommonDetailsActivity.this).getRequestQueue();
        final String url = URL/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                        Utils.showToast(AdminHealthCardCommonDetailsActivity.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(AdminHealthCardCommonDetailsActivity.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        adminHealthCardCommonListViewArray= new JSONObject(response).getJSONArray("responseObject");


                        if(null!=adminHealthCardCommonListViewArray && adminHealthCardCommonListViewArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminHealthCardCommonListViewArray.toString().getBytes();
                            VolleySingleton.getInstance(AdminHealthCardCommonDetailsActivity.this).getRequestQueue().getCache().put(URL+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&id="+id,e);
                            adminHealthCardListView.invalidateViews();

                             t = adminHealthCardCommonListViewArray.getJSONObject(0).getString("parameters_values");
                            //Utils.showToast(getApplicationContext(),t);
                            t1 = adminHealthCardCommonListViewArray.getJSONObject(0).getString("parameters");
                            array = t1.substring(1,t1.length()-1);
                            array1 = t.substring(1,t.length()-1);
                            array =array.replaceAll("\"", "");
                            array1 =array1.replaceAll("\"", "");
                            titles = new ArrayList<String>(Arrays.asList(array.split(",")));
                            descriptions = new ArrayList<String>(Arrays.asList(array1.split(",")));
                            int count=0;
                            int m=0;
                            for(int l=1;l<=titles.size();l++)
                            {
                               count++;
                            }

                            m= count;

                            if(value.matches("2")) {

                                titles.add(m, "Documents");
                                descriptions.add(m, adminHealthCardCommonListViewArray.getJSONObject(0).getString("document"));
                            }
                            adminHealthCardCommonDetailsListViewAdapter = new AdminHealthCardCommonDetailsListViewAdapter(AdminHealthCardCommonDetailsActivity.this, titles,descriptions);
                            adminHealthCardListView.setAdapter(adminHealthCardCommonDetailsListViewAdapter);
                            adminHealthCardCommonDetailsListViewAdapter.notifyDataSetChanged();



                        }
                    }
                    else
                        Utils.showToast(AdminHealthCardCommonDetailsActivity.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AdminHealthCardCommonDetailsActivity.this, "Error fetching modules! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(AdminHealthCardCommonDetailsActivity.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("id",id);
                //params.put("u_id",Preferences.getInstance().userId);

                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(AdminHealthCardCommonDetailsActivity.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(AdminHealthCardCommonDetailsActivity.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
