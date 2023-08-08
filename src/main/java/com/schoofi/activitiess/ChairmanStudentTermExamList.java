package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.ChairmanStudentExamListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChairmanStudentTermExamList extends AppCompatActivity {

    private ListView chairmanStudentTermExamListView;
    private JSONArray chairmanStudentTermExamListArray;
    ChairmanStudentExamListAdapter chairmanStudentExamListAdapter;
    private ImageView back;
    String classId,sectionId;
    TextView overall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Student Term Exam List");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_chairman_student_term_exam_list);

        back = (ImageView) findViewById(R.id.img_back);

        chairmanStudentTermExamListView = (ListView) findViewById(R.id.listViewAddTask);

        classId = getIntent().getStringExtra("classId");
        sectionId = getIntent().getStringExtra("sectionId");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        overall = (TextView) findViewById(R.id.text_admin_class);
        if(classId.matches("") || classId.matches("null"))
        {
            overall.setVisibility(View.GONE);
        }

        else {

            overall.setVisibility(View.VISIBLE);

            overall.setText("Overall");
        }

        overall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChairmanStudentTermExamList.this,ChairmanStudentResultTermWise.class);
                intent.putExtra("termId","");
                if(sectionId.matches("") || sectionId.matches("null") ) {
                    intent.putExtra("value", "8");
                }
                else
                {
                    intent.putExtra("value", "9");
                }
                intent.putExtra("examId","");
                intent.putExtra("classId",classId);
                intent.putExtra("sectionId",sectionId);
                startActivity(intent);

            }
        });

        initData();
        getClassList();

        chairmanStudentTermExamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChairmanStudentTermExamList.this,ChairmanStudentResultTermWise.class);
                try {
                    if(chairmanStudentTermExamListArray.getJSONObject(i).getString("exam_name").matches("") || chairmanStudentTermExamListArray.getJSONObject(i).getString("exam_name").matches("0") || chairmanStudentTermExamListArray.getJSONObject(i).getString("exam_name").matches("null"))
                    {
                        if(classId.matches("") || classId.matches("null")) {
                            intent.putExtra("termId", chairmanStudentTermExamListArray.getJSONObject(i).getString("term"));
                            intent.putExtra("examId", "");
                            intent.putExtra("value", "2");
                        }

                        else
                        if(classId.length()>=1)
                        {
                            if(sectionId.matches("") || sectionId.matches("null"))
                            {
                                intent.putExtra("termId", chairmanStudentTermExamListArray.getJSONObject(i).getString("term"));
                                intent.putExtra("examId", "");
                                intent.putExtra("value", "4");
                            }

                            else
                            {
                                intent.putExtra("termId", chairmanStudentTermExamListArray.getJSONObject(i).getString("term"));
                                intent.putExtra("examId", "");
                                intent.putExtra("value", "6");
                            }

                        }
                    }

                    else {
                        if(classId.matches("") || classId.matches("null")) {
                            intent.putExtra("termId", chairmanStudentTermExamListArray.getJSONObject(i).getString("term"));
                            intent.putExtra("examId", chairmanStudentTermExamListArray.getJSONObject(i).getString("exam_id"));
                            intent.putExtra("value", "3");
                        }

                        else
                        if(classId.length()>=1) {
                            if (sectionId.matches("") || sectionId.matches("null")) {
                                intent.putExtra("termId", chairmanStudentTermExamListArray.getJSONObject(i).getString("term"));
                                intent.putExtra("examId", chairmanStudentTermExamListArray.getJSONObject(i).getString("exam_id"));
                                intent.putExtra("value", "5");
                            } else {
                                intent.putExtra("termId", chairmanStudentTermExamListArray.getJSONObject(i).getString("term"));
                                intent.putExtra("examId", chairmanStudentTermExamListArray.getJSONObject(i).getString("exam_id"));
                                intent.putExtra("value", "7");
                            }
                        }

                    }

                    intent.putExtra("classId",classId);
                    intent.putExtra("secttionId",sectionId);
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_TERM_EXAM_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&value="+"1");
            if(e == null)
            {
                chairmanStudentTermExamListArray= null;
            }
            else
            {
                chairmanStudentTermExamListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanStudentTermExamListArray!= null)
        {
            chairmanStudentExamListAdapter= new ChairmanStudentExamListAdapter(ChairmanStudentTermExamList.this,chairmanStudentTermExamListArray);
            chairmanStudentTermExamListView.setAdapter(chairmanStudentExamListAdapter);
            chairmanStudentExamListAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_TERM_EXAM_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&value="+"1";
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
                    if(responseObject.has("Exam_list"))
                    {
                        chairmanStudentTermExamListArray= new JSONObject(response).getJSONArray("Exam_list");
                        if(null!=chairmanStudentTermExamListArray && chairmanStudentTermExamListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanStudentTermExamListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_TERM_EXAM_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&value="+"1",e);
                            chairmanStudentTermExamListView.invalidateViews();
                            chairmanStudentExamListAdapter= new ChairmanStudentExamListAdapter(ChairmanStudentTermExamList.this,chairmanStudentTermExamListArray);
                            chairmanStudentTermExamListView.setAdapter(chairmanStudentExamListAdapter);
                            chairmanStudentExamListAdapter.notifyDataSetChanged();

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
}
