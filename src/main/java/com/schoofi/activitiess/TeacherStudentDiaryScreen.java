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

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.adapters.TeacherStudentDiaryScreenAdapter;
import com.schoofi.adapters.TeacherStudentHomeWorkStudentSubmittedDetailsAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeacherStudentDiaryScreen extends AppCompatActivity {

    ListView studentNotSubmittedListView;
    TeacherStudentDiaryScreenAdapter teacherStudentHomeWorkStudentSubmittedDetailsAdapter;
    private JSONArray studentListArray;
    String asnId;
    SwipyRefreshLayout swipyRefreshLayout;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_teacher_student_diary_screen);

        //swipyRefreshLayout= (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        studentNotSubmittedListView = (ListView) findViewById(R.id.listView_teacher_feedback);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Utils.showToast(getApplicationContext(),"poo");

        initData();
        getStudentFeedList();

        studentNotSubmittedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    Intent intent = new Intent(TeacherStudentDiaryScreen.this,StudentChatScreen.class);

                    intent.putExtra("type",studentListArray.getJSONObject(position).getString("type"));
                    if(studentListArray.getJSONObject(position).getString("type").matches("group"))
                    {
                        intent.putExtra("recipient_id",studentListArray.getJSONObject(position).getString("group_id"));
                    }
                    else {
                        intent.putExtra("recipient_id", studentListArray.getJSONObject(position).getString("user_id"));
                    }
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
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_DIARY_TEACHER_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&class_id="+Preferences.getInstance().studentClassId+"&sch_id="+Preferences.getInstance().schoolId+"&section_id="+Preferences.getInstance().studentSectionId+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1);
            if(e == null)
            {
                studentListArray= null;
            }
            else
            {
                studentListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentListArray!= null)
        {
            teacherStudentHomeWorkStudentSubmittedDetailsAdapter= new TeacherStudentDiaryScreenAdapter(TeacherStudentDiaryScreen.this,studentListArray);
            studentNotSubmittedListView.setAdapter(teacherStudentHomeWorkStudentSubmittedDetailsAdapter);
            teacherStudentHomeWorkStudentSubmittedDetailsAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_DIARY_TEACHER_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&class_id="+Preferences.getInstance().studentClassId+"&sch_id="+Preferences.getInstance().schoolId+"&section_id="+Preferences.getInstance().studentSectionId+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1;
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
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        studentListArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=studentListArray && studentListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_DIARY_TEACHER_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&class_id="+Preferences.getInstance().studentClassId+"&sch_id="+Preferences.getInstance().schoolId+"&section_id="+Preferences.getInstance().studentSectionId+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1,e);
                            studentNotSubmittedListView.invalidateViews();
                            teacherStudentHomeWorkStudentSubmittedDetailsAdapter= new TeacherStudentDiaryScreenAdapter(TeacherStudentDiaryScreen.this,studentListArray);
                            studentNotSubmittedListView.setAdapter(teacherStudentHomeWorkStudentSubmittedDetailsAdapter);
                            teacherStudentHomeWorkStudentSubmittedDetailsAdapter.notifyDataSetChanged();
                           // swipyRefreshLayout.setRefreshing(false);


                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");

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

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        getStudentFeedList();
    }
}
