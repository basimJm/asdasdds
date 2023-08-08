package com.schoofi.activitiess;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.AdminClassSectionListAdapter;
import com.schoofi.adapters.AdminStudentListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminStudentList extends AppCompatActivity {

    ImageView back;
    ListView adminStudentListView;
    AdminStudentListAdapter adminStudentListAdapter;
    SwipyRefreshLayout swipyRefreshLayout;
    JSONArray adminStudentListArray;
    String classId, sectionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d3d3d3")));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tracker t = ((SchoofiApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admin Student List");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admin_student_list);



        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        adminStudentListView = (ListView) findViewById(R.id.listViewAdminBusListView);


        classId = getIntent().getStringExtra("class_id");
        sectionId = getIntent().getStringExtra("sec_id");

        getSupportActionBar().setIcon(android.R.color.transparent);

        initData();
        getChairmanStudentLeaveList();

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                initData();
                getChairmanStudentLeaveList();
            }
        });

        adminStudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                try {
                    Intent intent = new Intent(AdminStudentList.this, AdminStudentProfile.class);
                    intent.putExtra("stu_id", adminStudentListArray.getJSONObject(i).getString("stu_id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void initData() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&cls_id=" + classId + "&sec_id=" + sectionId);
            if (e == null) {
                adminStudentListArray = null;
            } else {
                adminStudentListArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (adminStudentListArray != null) {

            adminStudentListAdapter = new AdminStudentListAdapter(AdminStudentList.this, adminStudentListArray);
            adminStudentListView.setAdapter(adminStudentListAdapter);
            adminStudentListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList() {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AdminStudentList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                Log.d("ui", response);
                //System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
                        Utils.showToast(AdminStudentList.this, "No Records Found");
                    else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(AdminStudentList.this, "Session Expired:Please Login Again");
                    } else if (responseObject.has("classSectionStudents")) {
                        adminStudentListArray = new JSONObject(response).getJSONArray("classSectionStudents");


                        if (null != adminStudentListArray && adminStudentListArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminStudentListView.toString().getBytes();
                            VolleySingleton.getInstance(AdminStudentList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_STUDENT_LIST + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&cls_id=" + classId + "&sec_id=" + sectionId, e);
                            adminStudentListView.invalidateViews();

                            adminStudentListAdapter = new AdminStudentListAdapter(AdminStudentList.this, adminStudentListArray);
                            adminStudentListView.setAdapter(adminStudentListAdapter);
                            adminStudentListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    } else
                        Utils.showToast(AdminStudentList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(AdminStudentList.this, "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(AdminStudentList.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("ins_id", Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token", Preferences.getInstance().token);
                params.put("sch_id", Preferences.getInstance().schoolId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("cls_id", classId);
                params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(AdminStudentList.this))
            queue.add(requestObject);
        else {
            Utils.showToast(AdminStudentList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa() {
        System.out.println("aaa");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_student_list_menu,
                menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                adminStudentListAdapter.getFilter().filter(newText);


                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // this is your adapter that will be filtered

                adminStudentListAdapter.getFilter().filter(query);


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
}
