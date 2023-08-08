package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.StudentCOScholasticResult2AAdapter;
import com.schoofi.adapters.StudentResultSpinnerAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentResultPart2COScholasticAreas1 extends AppCompatActivity {

    ImageView back;
    ListView studentResultPart2COScholasticAreas1ListView;
    StudentCOScholasticResult2AAdapter studentCOScholasticResult2AAdapter;
    private JSONArray studentCOScholasticResult2AArray,jsArray;
    String termId="";
    Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_result_part2_coscholastic_areas1);

        back = (ImageView) findViewById(R.id.img_back);
        studentResultPart2COScholasticAreas1ListView = (ListView) findViewById(R.id.listView_co_scholastic2A);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        termId = getIntent().getStringExtra("termId");

        next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentResultPart2COScholasticAreas1.this,StudentCOScholasticResultAreas2.class);
                intent.putExtra("termId",termId);
                startActivity(intent);
            }
        });

        initData4();
        getChairmanExamClassList1();






    }

    private void initData4()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_CO_SCHOLASTIC_RESULTS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&stu_id="+Preferences.getInstance().studentId);
            if(e == null)
            {
                studentCOScholasticResult2AArray= null;
            }
            else
            {
                studentCOScholasticResult2AArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentCOScholasticResult2AArray!= null)
        {
            try {
            ArrayList<String> list = new ArrayList<String>();

            for(int i=0;i<studentCOScholasticResult2AArray.length();i++)
            {

                    if(studentCOScholasticResult2AArray.getJSONObject(i).getString("grading_area").matches("Life Skills"))
                    {
                        list.add(studentCOScholasticResult2AArray.get(i).toString());
                    }
                    String list1= list.toString();
                    String list2 = "{"+'"'+"Result"+'"'+":"+list1+"}";
                    JSONObject jsonObject = new JSONObject(list2);
                    jsArray = jsonObject.getJSONArray("Result");
                    studentResultPart2COScholasticAreas1ListView.invalidateViews();
                    studentCOScholasticResult2AAdapter= new StudentCOScholasticResult2AAdapter(StudentResultPart2COScholasticAreas1.this,jsArray);
                    studentResultPart2COScholasticAreas1ListView.setAdapter(studentCOScholasticResult2AAdapter);
                    studentCOScholasticResult2AAdapter.notifyDataSetChanged();

            }
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }

    protected void getChairmanExamClassList1()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentResultPart2COScholasticAreas1.this).getRequestQueue();
        final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_CO_SCHOLASTIC_RESULTS;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        Utils.showToast(StudentResultPart2COScholasticAreas1.this,"No Records Found");
                        //mChart.setVisibility(View.INVISIBLE);
                    }

                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentResultPart2COScholasticAreas1.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Result"))
                    {
                        //mChart.setVisibility(View.VISIBLE);
                        studentCOScholasticResult2AArray= new JSONObject(response).getJSONArray("Result");
                        if(null!=studentCOScholasticResult2AArray && studentCOScholasticResult2AArray.length()>=0)
                        {
                            com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
                            e.data = studentCOScholasticResult2AArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentResultPart2COScholasticAreas1.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_CO_SCHOLASTIC_RESULTS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&stu_id="+Preferences.getInstance().studentId,e);
                            ArrayList<String> list = new ArrayList<String>();

                            for(int i=0;i<studentCOScholasticResult2AArray.length();i++)
                            {
                                if(studentCOScholasticResult2AArray.getJSONObject(i).getString("grading_area").matches("Life Skills"))
                                {
                                    list.add(studentCOScholasticResult2AArray.get(i).toString());
                                }
                            }



                            String list1= list.toString();
                            String list2 = "{"+'"'+"Result"+'"'+":"+list1+"}";
                            JSONObject jsonObject = new JSONObject(list2);
                            jsArray = jsonObject.getJSONArray("Result");
                            studentResultPart2COScholasticAreas1ListView.invalidateViews();
                            studentCOScholasticResult2AAdapter= new StudentCOScholasticResult2AAdapter(StudentResultPart2COScholasticAreas1.this,jsArray);
                            studentResultPart2COScholasticAreas1ListView.setAdapter(studentCOScholasticResult2AAdapter);
                            studentCOScholasticResult2AAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Preferences.getInstance().loadPreference(StudentResultPart2COScholasticAreas1.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("term",termId);

                params.put("stu_id",Preferences.getInstance().studentId);


                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("cls_id", Preferences.getInstance().studentClassId);
                //params.put("sec_id", Preferences.getInstance().studentSectionId);
                params.put("session",Preferences.getInstance().session1);
                params.put("board",Preferences.getInstance().board);
                //params.put("crr_date",currentDate);
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
}
