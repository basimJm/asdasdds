package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import com.schoofi.adapters.ChairmanDiscountSectionAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChairmanDiscountSectionWise extends AppCompatActivity {

    ImageView back;
    ListView feeClass;
    String Rs;
    ChairmanDiscountSectionAdapter chairmanStudentFragmentFeesClassAdapter;
    private JSONArray ChairmanStudentFragmentFeesClassArray;
    int value;
    String temp,class_id,className;
    private TextView totalDiscount;
    float total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_chairman_discount_class_wise);

        back = (ImageView) findViewById(R.id.img_back);
        totalDiscount = (TextView) findViewById(R.id.text_total_number_students);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //value = getIntent().getExtras().getInt("value");
        class_id = getIntent().getStringExtra("cls_id");
        temp = getIntent().getStringExtra("temp");
        className = getIntent().getStringExtra("class_name");


        feeClass = (ListView) findViewById(R.id.listViewAddTask);
        Rs = getApplicationContext().getString(R.string.Rs);

       // initData();
        getChairmanStudentLeaveList();

        feeClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    Intent intent = new Intent(ChairmanDiscountSectionWise.this,ChairmanDiscountStudentWise.class);
                    intent.putExtra("section_id",ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("class_section_id"));
                    intent.putExtra("class_name",className);
                    intent.putExtra("section_name",ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("section_name"));
                    intent.putExtra("cls_id",class_id);
                    intent.putExtra("temp",temp);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    /*@Override
    protected void onResume() {
        super.onResume();
        initData();
        getChairmanStudentLeaveList();
    }*/

   /* private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_ANALYSIS_SECTION_WISE_SCREEN+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&class_id="+class_id+"&discount_type="+temp+"&session1="+Preferences.getInstance().session1);
            if(e == null)
            {
                ChairmanStudentFragmentFeesClassArray= null;
            }
            else
            {
                ChairmanStudentFragmentFeesClassArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(ChairmanStudentFragmentFeesClassArray!= null)
        {
            for (int i=0;i<ChairmanStudentFragmentFeesClassArray.length();i++)
            {
                try {
                    total = total+Float.parseFloat(ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("total_discount"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            totalDiscount.setText(Rs+String.valueOf(total));
            chairmanStudentFragmentFeesClassAdapter= new ChairmanDiscountSectionAdapter(ChairmanDiscountSectionWise.this,ChairmanStudentFragmentFeesClassArray,className);
            feeClass.setAdapter(chairmanStudentFragmentFeesClassAdapter);
            chairmanStudentFragmentFeesClassAdapter.notifyDataSetChanged();
        }
    }*/

    protected void getChairmanStudentLeaveList()
    {
        total=0;
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_ANALYSIS_SECTION_WISE_SCREEN/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                // System.out.println(response);
                // System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        feeClass.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        ChairmanStudentFragmentFeesClassArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=ChairmanStudentFragmentFeesClassArray && ChairmanStudentFragmentFeesClassArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = ChairmanStudentFragmentFeesClassArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_ANALYSIS_SECTION_WISE_SCREEN+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&class_id="+class_id+"&discount_type="+temp+"&session1="+Preferences.getInstance().session1,e);
                            feeClass.setVisibility(View.VISIBLE);
                            for (int i=0;i<ChairmanStudentFragmentFeesClassArray.length();i++)
                            {
                                try {
                                    total = total+Float.parseFloat(ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("total_discount"));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            totalDiscount.setText("Class "+className+"  "+Rs+String.valueOf(total));
                            feeClass.invalidateViews();
                            chairmanStudentFragmentFeesClassAdapter= new ChairmanDiscountSectionAdapter(ChairmanDiscountSectionWise.this,ChairmanStudentFragmentFeesClassArray,className);
                            feeClass.setAdapter(chairmanStudentFragmentFeesClassAdapter);
                            chairmanStudentFragmentFeesClassAdapter.notifyDataSetChanged();
                            //swipyRefreshLayout.setRefreshing(false);

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
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("cls_id", Preferences.getInstance().studentClassId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("discount_type",temp);
                params.put("class_id",class_id);
                params.put("session",Preferences.getInstance().session1);
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


    private void toa()
    {
        System.out.println("aaa");
    }
}
