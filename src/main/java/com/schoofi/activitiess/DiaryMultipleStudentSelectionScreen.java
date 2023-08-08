package com.schoofi.activitiess;

import android.app.SearchManager;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.AdminStudentListAdapter;
import com.schoofi.adapters.DiaryMultipleStudentSelectionScreenListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DiaryMultipleStudentSelectionScreen extends AppCompatActivity {

    private ListView diaryMultipleSelectionListView;
    private Button submit;
    private JSONArray diaryMultipleStudentSelectionArray;
    private DiaryMultipleStudentSelectionScreenListViewAdapter diaryMultipleStudentSelectionScreenListViewAdapter;
    String array1,array2;
    public ArrayList<String> attendance = new ArrayList<String>();
    String typeId,ratingId,ratingBar,subjectId;
    private CheckBox checkBox;
    private String indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Diary Multiple Student Selection Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_diary_multiple_student_selection_screen);

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        typeId = getIntent().getStringExtra("typeId");
        ratingId = getIntent().getStringExtra("ratingId");
        ratingBar = getIntent().getStringExtra("ratingBar");
        subjectId = getIntent().getStringExtra("subjectId");

        submit = (Button) findViewById(R.id.btn_submitAttendanceList);
        diaryMultipleSelectionListView = (ListView) findViewById(R.id.listViewTeacherStudentAttendanceDetails);

        initData();
        getChairmanStudentLeaveList();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int k=0;k<diaryMultipleStudentSelectionScreenListViewAdapter.teacherStudentAttendanceArray1.length();k++)
                {
                    try {
                        if(diaryMultipleStudentSelectionScreenListViewAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isAdded").matches("1"))
                        {
                            attendance.add(diaryMultipleStudentSelectionScreenListViewAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_id"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Preferences.getInstance().loadPreference(getApplicationContext());

                //attendance.add(Preferences.getInstance().studentId);

                array1 = attendance.toString();
                array2 = array1.substring(1, array1.length()-1);

                Preferences.getInstance().studentId = array2.toString();
                Log.d("p",Preferences.getInstance().studentId);

                finish();
                //push from top to bottom
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

            }
        });

        if(checkBox.isChecked())
        {
            indicator = "true";
        }

        else
        {
            indicator = "false";
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkBox.isChecked())
                {
                    indicator = "true";
                    for (int j = 0; j < diaryMultipleStudentSelectionArray.length(); j++) {
                        try {
                            diaryMultipleStudentSelectionArray.getJSONObject(j).put("isAdded", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)

                    }

                    Cache.Entry e = new Cache.Entry();
                    e.data = diaryMultipleStudentSelectionArray.toString().getBytes();
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId,e);
                    diaryMultipleSelectionListView.invalidateViews();

                    diaryMultipleStudentSelectionScreenListViewAdapter= new DiaryMultipleStudentSelectionScreenListViewAdapter(DiaryMultipleStudentSelectionScreen.this, diaryMultipleStudentSelectionArray);
                    diaryMultipleSelectionListView.setAdapter(diaryMultipleStudentSelectionScreenListViewAdapter);
                    diaryMultipleStudentSelectionScreenListViewAdapter.notifyDataSetChanged();
                }

                else {
                    indicator = "false";
                    for (int j = 0; j < diaryMultipleStudentSelectionArray.length(); j++) {
                        try {
                            diaryMultipleStudentSelectionArray.getJSONObject(j).put("isAdded", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)

                    }

                    Cache.Entry e = new Cache.Entry();
                    e.data = diaryMultipleStudentSelectionArray.toString().getBytes();
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId, e);
                    diaryMultipleSelectionListView.invalidateViews();

                    diaryMultipleStudentSelectionScreenListViewAdapter= new DiaryMultipleStudentSelectionScreenListViewAdapter(DiaryMultipleStudentSelectionScreen.this, diaryMultipleStudentSelectionArray);
                    diaryMultipleSelectionListView.setAdapter(diaryMultipleStudentSelectionScreenListViewAdapter);
                    diaryMultipleStudentSelectionScreenListViewAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId);
            if(e == null)
            {
                diaryMultipleStudentSelectionArray= null;
            }
            else
            {
                diaryMultipleStudentSelectionArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(diaryMultipleStudentSelectionArray!= null)
        {

            diaryMultipleStudentSelectionScreenListViewAdapter= new DiaryMultipleStudentSelectionScreenListViewAdapter(DiaryMultipleStudentSelectionScreen.this, diaryMultipleStudentSelectionArray);
            diaryMultipleSelectionListView.setAdapter(diaryMultipleStudentSelectionScreenListViewAdapter);
            diaryMultipleStudentSelectionScreenListViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.multiple_student_selection,
                menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {

                diaryMultipleStudentSelectionScreenListViewAdapter.getFilter().filter(newText);



                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered

                diaryMultipleStudentSelectionScreenListViewAdapter.getFilter().filter(query);



                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch(item.getItemId())
        {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(DiaryMultipleStudentSelectionScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("classSectionStudents"))
                    {
                        diaryMultipleStudentSelectionArray= new JSONObject(response).getJSONArray("classSectionStudents");

                        for(int i=0;i<diaryMultipleStudentSelectionArray.length();i++)
                        {
                            diaryMultipleStudentSelectionArray.getJSONObject(i).put("isAdded","0");
                        }


                        if(null!=diaryMultipleStudentSelectionArray && diaryMultipleStudentSelectionArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = diaryMultipleSelectionListView.toString().getBytes();
                            VolleySingleton.getInstance(DiaryMultipleStudentSelectionScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId,e);
                            diaryMultipleSelectionListView.invalidateViews();

                            diaryMultipleStudentSelectionScreenListViewAdapter= new DiaryMultipleStudentSelectionScreenListViewAdapter(DiaryMultipleStudentSelectionScreen.this, diaryMultipleStudentSelectionArray);
                            diaryMultipleSelectionListView.setAdapter(diaryMultipleStudentSelectionScreenListViewAdapter);
                            diaryMultipleStudentSelectionScreenListViewAdapter.notifyDataSetChanged();



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
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);

                //params.put("u_id",Preferences.getInstance().userId);
                params.put("cls_id", Preferences.getInstance().studentClassId);
                params.put("sec_id", Preferences.getInstance().studentSectionId);


                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
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
