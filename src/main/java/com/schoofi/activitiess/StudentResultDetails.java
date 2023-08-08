package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.schoofi.adapters.StudentResultDetailsAdapter;
import com.schoofi.adapters.StudentResultListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;

public class StudentResultDetails extends AppCompatActivity {

    String examId,subId;
    private TextView screenTitle,newView,subjectName,subjectMarks,totalMarks,total,percentageText,percentageText1;
    private ListView studentResultDetailsListView;
    StudentResultDetailsAdapter studentResultDetailsAdapter;
    private JSONArray studentResultDetailsArray;
    float total1=0,totalObtained=0;
    float percentage;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_result_details);

        examId = getIntent().getStringExtra("examId");
        subId = getIntent().getStringExtra("subId");

        studentResultDetailsListView = (ListView) findViewById(R.id.listViewResulDetails);
        totalMarks = (TextView) findViewById(R.id.text_totalMark);
        total = (TextView) findViewById(R.id.text_totalMarks1);
        percentageText = (TextView) findViewById(R.id.text_percentage);
        percentageText1 = (TextView) findViewById(R.id.text_percentage1);
        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
        getStudentResultList();




    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_RESULT_DETAILS+"?sec_id="+ Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&stu_id="+Preferences.getInstance().studentId+"&sub_id="+subId+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&session="+Preferences.getInstance().session1+"&board="+Preferences.getInstance().board+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                studentResultDetailsArray= null;
            }
            else
            {
                studentResultDetailsArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentResultDetailsArray!= null)
        {
            studentResultDetailsAdapter = new StudentResultDetailsAdapter(StudentResultDetails.this, studentResultDetailsArray);
            studentResultDetailsListView.setAdapter(studentResultDetailsAdapter);
            studentResultDetailsAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentResultList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentResultDetails.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_RESULT_DETAILS+"?sec_id="+ Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&stu_id="+Preferences.getInstance().studentId+"&sub_id="+subId+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&session="+Preferences.getInstance().session1+"&board="+Preferences.getInstance().board+"&ins_id="+Preferences.getInstance().institutionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                total1=0;
                totalObtained=0;
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Status")&&responseObject.getString("Status").equals("0"))
                    {
                       // newView.setVisibility(View.VISIBLE);
                        studentResultDetailsListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        //newView.setVisibility(View.INVISIBLE);
                        Utils.showToast(StudentResultDetails.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Result"))
                    {
                        //newView.setVisibility(View.INVISIBLE);
                        studentResultDetailsListView.setVisibility(View.VISIBLE);
                        studentResultDetailsArray= new JSONObject(response).getJSONArray("Result");


                        if(null!=studentResultDetailsArray && studentResultDetailsArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentResultDetailsArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentResultDetails.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_RESULT_DETAILS+"?sec_id="+ Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&stu_id="+Preferences.getInstance().studentId+"&sub_id="+subId+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&session="+Preferences.getInstance().session1+"&board="+Preferences.getInstance().board+"&ins_id="+Preferences.getInstance().institutionId,e);
                            studentResultDetailsListView.invalidateViews();
                            studentResultDetailsAdapter = new StudentResultDetailsAdapter(StudentResultDetails.this, studentResultDetailsArray);
                            studentResultDetailsListView.setAdapter(studentResultDetailsAdapter);
                            studentResultDetailsAdapter.notifyDataSetChanged();


                            for(int i =0;i<studentResultDetailsArray.length();i++)
                            {
                                total1 = total1+Float.parseFloat(studentResultDetailsArray.getJSONObject(i).getString("max_marks"));
                                totalObtained = totalObtained+Float.parseFloat(studentResultDetailsArray.getJSONObject(i).getString("obtained_marks"));
                            }



                            percentage = (totalObtained/total1)*100;
                            //System.out.println(percentage);

                            DecimalFormat df=new DecimalFormat("0.00");
                            String formate = df.format(percentage);
                            double finalValue = 0;
                            try {
                                finalValue = (Double)df.parse(formate) ;
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                            String grade;
                            if(percentage>90)
                            {
                                grade = "A1";
                            }

                            else
                            if(percentage>=80 && percentage<=90)
                            {
                                grade = "A2";
                            }

                            else
                            if(percentage>=70 && percentage<80)
                            {
                                grade = "B1";
                            }

                            else
                            if(percentage>=60 && percentage<70)
                            {
                                grade = "B2";
                            }

                            else
                            if(percentage>=46 && percentage<60)
                            {
                                grade = "C";
                            }
                            else
                            if(percentage>=33 && percentage<46)
                            {
                                grade = "D";
                            }
                            else
                            {
                                grade = "E";
                            }

                            if(totalObtained % 1==0)
                            {
                                int totalObtainedInt = Math.round(totalObtained);
                                int total2 = Math.round(total1);
                                total.setText(Integer.toString(totalObtainedInt)+"/"+Integer.toString(total2));
                            }

                            else
                            {
                                total.setText(Float.toString(totalObtained)+"/"+Float.toString(total1));
                            }



                            percentageText1.setText(Double.toString(finalValue)+"% "+" ("+grade+")");



                        }
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                    }

                    else
                        Utils.showToast(StudentResultDetails.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentResultDetails.this, "Error fetching modules! Please try after sometime.");
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

				Preferences.getInstance().loadPreference(StudentResult.this);

				Map<String,String> params = new HashMap<String, String>();
				params.put("stu_id",Preferences.getInstance().studentId);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("ex_id",examId1);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("sch_id", Preferences.getInstance().schoolId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudentResultDetails.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentResultDetails.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initData();
        getStudentResultList();
    }
}
