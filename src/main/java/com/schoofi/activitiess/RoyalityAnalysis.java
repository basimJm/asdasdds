package com.schoofi.activitiess;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.adapters.ParentUnpaidFeesNewMultilevelAdapter1;
import com.schoofi.adapters.RoyalityAdapter;
import com.schoofi.adapters.StudentNewAssignmentAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.AssignmentMultiLevelVO;
import com.schoofi.utils.CategoryStudentAnalysisVO;
import com.schoofi.utils.DiaryVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.RoyalityChildVO;
import com.schoofi.utils.RoyalityParentVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.RoyalityChildVO;
import com.schoofi.utils.RoyalityParentVO;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import smtchahal.materialspinner.MaterialSpinner;

public class RoyalityAnalysis extends AppCompatActivity {

    public static final String ARG_PAGE = "ARG_PAGE";
    String value1;
    private StudentNewAssignmentAdapter assignmentMultiLevelListAdapter;
    private JSONArray AssignmentArray;
    private ArrayList<DiaryVO> diaryVOs;
    private ArrayList<AssignmentMultiLevelVO> assignmentMultiLevelVOs;
    ArrayList<RoyalityParentVO> royalityParentVOS;
    ArrayList<RoyalityChildVO> royalityChildVOS;
    private ExpandableListView expandableListView;
    SwipyRefreshLayout swipyRefreshLayout;
    JSONObject jsonObject,jsonObject1;
    JSONArray jsonArray;
    RoyalityAdapter royalityAdapter;
    ArrayList aList= new ArrayList();
    String value="0";
    ArrayList aList1 = new ArrayList();
    public String file,teachId,assignId,description,studentFile,submitDate,subjectName,title,teacherName;
    private MaterialSpinner departmentType;
    private String departmentId1="";
    ArrayList<String> departmentName;
    ArrayList<CategoryStudentAnalysisVO> departmentId;
    JSONObject jsonobject,jsonobject1,jsonObject2;
    JSONArray jsonarray,jsonarray1,employeeListArray,employeeListArray1;
    ImageView back,calendar1;


    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String from1,to1;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_royality_analysis);

        expandableListView = (ExpandableListView) findViewById(R.id.listViewInnerAllAssignment);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        expandableListView.setChildIndicator(null);
        expandableListView.setGroupIndicator(null);
        //expandableListView.setChildDivider(getResources().getDrawable(R.color.transparent));
        expandableListView.setDivider(getResources().getDrawable(R.color.transparent));
        expandableListView.setDividerHeight(2);
        //getStudentFeedList();

        value1 = getIntent().getStringExtra("value");
        calendar1 = (ImageView) findViewById(R.id.img_calender1);

        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RoyalityAnalysis.this,EmployeeAttendanceScreen.class);
                intent.putExtra("value",12);
                intent.putExtra("dept_id",departmentId1);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);




            }


        });

        if(value1.matches("1")) {

            cal.add(Calendar.DATE, -7);
            Date todate1 = cal.getTime();
            from1 = new SimpleDateFormat("yyyy-MM-dd").format(todate1);
            System.out.println(from1);
            cal1.add(Calendar.DATE, -1);
            Date todate2 = cal1.getTime();
            to1 = new SimpleDateFormat("yyyy-MM-dd").format(todate2);
            getStudentFeedList1();

            //initData();
            // getStudentFeedList();

        }
        else
        {
            from1 = getIntent().getStringExtra("startingDate");
            to1 = getIntent().getStringExtra("endingDate");


            getStudentFeedList1();




            //initData();
            // getStudentFeedList();

        }








    }






    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //initData();
        //getStudentFeedList();
    }

	/*private void initData()
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNMENT_URL+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&temp="+"1"+"&status="+"0");
			if(e == null)
			{
				AssignmentArray= null;
			}
			else
			{
				AssignmentArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(AssignmentArray!= null)
		{
			assignmentAdapter= new AssignmentAdapter(getActivity(),AssignmentArray);
			allAssignMentListView.setAdapter(assignmentAdapter);
			assignmentAdapter.notifyDataSetChanged();
		}
	}*/

    protected void getStudentFeedList1()
    {

        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNMENT_NEW_LIST+"?u_email_id="+ Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&from_date="+from1+"&to_date="+to1+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1+"&subject_id="+departmentId1;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");

                    }
                    else
                    if(responseObject.has("fee"))

                    {

                        employeeListArray= new JSONObject(response).getJSONArray("fee");

                        royalityParentVOS = new ArrayList<RoyalityParentVO>();


                        /*for(int i=0;i<parentStudentFeesUnpaidArray.length();i++)
                        {
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("isAdded","1");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text1_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text2_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text3_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text4_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text5_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("interest","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("isDelay","N");
                        }*/
                        if(null!=employeeListArray && employeeListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = employeeListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNMENT_NEW_LIST+"?u_email_id="+ Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&from_date="+from1+"&to_date="+to1+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1+"&subject_id="+departmentId1,e);
                            expandableListView.invalidateViews();
                            /*parentStudentFeesUnpaidAdapter = new ParentStudentFeesUnpaidAdapter(getActivity(), parentStudentFeesUnpaidArray);
                            parentStudentFeesUnpaid.setAdapter(parentStudentFeesUnpaidAdapter);
                            parentStudentFeesUnpaidAdapter.notifyDataSetChanged();*/







                            for(int i=0;i<employeeListArray.length();i++)
                            {
                                RoyalityParentVO parentStudentFessUnpaidMultilevelParentVO = new RoyalityParentVO();
                                parentStudentFessUnpaidMultilevelParentVO.setFeeDate(employeeListArray.getJSONObject(i).getString("date"));
                                royalityChildVOS = new ArrayList<RoyalityChildVO>();
                                jsonObject = employeeListArray.getJSONObject(i);
                                employeeListArray1 = jsonObject.getJSONArray("bifurcation");

                                for (int j=0;j<employeeListArray1.length();j++)
                                {
                                    jsonObject1 = employeeListArray1.getJSONObject(j);
                                    RoyalityChildVO parentStudentFeesUnpaidMultilevelChildVO = new RoyalityChildVO();
                                    parentStudentFeesUnpaidMultilevelChildVO.setAmount(jsonObject1.getString("fee_amount"));
                                    parentStudentFeesUnpaidMultilevelChildVO.setFeesType(jsonObject1.getString("fee_type_text"));
                                    royalityChildVOS.add(parentStudentFeesUnpaidMultilevelChildVO);
                                }

                                parentStudentFessUnpaidMultilevelParentVO.setItems(royalityChildVOS);
                                royalityParentVOS.add(parentStudentFessUnpaidMultilevelParentVO);

                            }










                            //swipyRefreshLayout.setRefreshing(false);

                            royalityAdapter = new RoyalityAdapter(RoyalityAnalysis.this,royalityParentVOS,employeeListArray);
                            expandableListView.setAdapter(royalityAdapter);
                            royalityAdapter.notifyDataSetChanged();

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
}
