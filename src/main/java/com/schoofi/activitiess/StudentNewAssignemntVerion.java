package com.schoofi.activitiess;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.adapters.AssignmentMultiLevelListAdapter;
import com.schoofi.adapters.StudentNewAssignmentAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.fragments.AssignMentAll;
import com.schoofi.utils.AssignmentMultiLevelVO;
import com.schoofi.utils.CategoryStudentAnalysisVO;
import com.schoofi.utils.DiaryVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;
import smtchahal.materialspinner.MaterialSpinner;

public class StudentNewAssignemntVerion extends AppCompatActivity {


    public static final String ARG_PAGE = "ARG_PAGE";
    String value1;
    private StudentNewAssignmentAdapter assignmentMultiLevelListAdapter;
    private JSONArray AssignmentArray;
    private ArrayList<DiaryVO> diaryVOs;
    private ArrayList<AssignmentMultiLevelVO> assignmentMultiLevelVOs;
    private ExpandableListView expandableListView;
    SwipyRefreshLayout swipyRefreshLayout;
    JSONObject jsonObject,jsonObject1;
    JSONArray jsonArray;
    ArrayList aList= new ArrayList();
    String value="0";
    ArrayList aList1 = new ArrayList();
    public String file,teachId,assignId,description,studentFile,submitDate,subjectName,title,teacherName;
    private MaterialSpinner departmentType;
    private String departmentId1="";
    ArrayList<String> departmentName;
    ArrayList<CategoryStudentAnalysisVO> departmentId;
    JSONObject jsonobject,jsonobject1,jsonObject2;
    JSONArray jsonarray,jsonarray1,employeeListArray;
    ImageView back,calendar1;
    int count1 = 0;
    int count = 0;

    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String from1,to1;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    //Date date = new Date();
    String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
    int selectedPosition = 0;

    private EditText fromEditTextDate,toEditTextDate;
    private PopupWindow calendarPopup,calendarPopup1;




    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_new_assignemnt_verion);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        expandableListView = (ExpandableListView) findViewById(R.id.listViewInnerAllAssignment);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        expandableListView.setChildIndicator(null);
        expandableListView.setGroupIndicator(null);
        //expandableListView.setChildDivider(getResources().getDrawable(R.color.transparent));
        expandableListView.setDivider(getResources().getDrawable(R.color.transparent));
        expandableListView.setDividerHeight(2);
        //getStudentFeedList();

        value1 = getIntent().getStringExtra("value");

        departmentType = (MaterialSpinner) findViewById(R.id.spinner_category);
        departmentType.setBackgroundResource(R.drawable.grey_button);

        getStudentFeedList1();


        fromEditTextDate = (EditText) findViewById(R.id.edit_fromDatePicker);
        toEditTextDate = (EditText) findViewById(R.id.edit_toDatePicker);


        fromEditTextDate.setOnClickListener(onEditTextClickListener);
        toEditTextDate.setOnClickListener(onEditTextClickListener1);

        fromEditTextDate.setInputType(0);
        toEditTextDate.setInputType(0);





        calendar1 = (ImageView) findViewById(R.id.img_calender1);
        calendar1.setVisibility(View.INVISIBLE);

        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(StudentNewAssignemntVerion.this,EmployeeAttendanceScreen.class);
                    intent.putExtra("value",7);
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
            cal1.add(Calendar.DATE,0);
            Date todate2 = cal1.getTime();
            to1 = new SimpleDateFormat("yyyy-MM-dd").format(todate2);

                //initData();
               // getStudentFeedList();

        }
        else
        {
            from1 = getIntent().getStringExtra("startingDate");
            to1 = getIntent().getStringExtra("endingDate");
            departmentId1 = getIntent().getStringExtra("dept_id");

            Log.d("dept_id",departmentId1);

            if(departmentId1.matches("") || departmentId1.matches("null"))
            {

               getStudentFeedList1();
            }

            else
            {

                getStudentFeedList();
            }






                //initData();
               // getStudentFeedList();

        }

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {



                file = diaryVOs.get(groupPosition).getItems1().get(childPosition).getTeacherfile();
                studentFile = diaryVOs.get(groupPosition).getItems1().get(childPosition).getStudentFile();
                Log.d("student_file",studentFile);
                Log.d("file",file);
                assignId = diaryVOs.get(groupPosition).getItems1().get(childPosition).getAssignId();
                submitDate = diaryVOs.get(groupPosition).getItems1().get(childPosition).getSubmitdate();
                description = diaryVOs.get(groupPosition).getItems1().get(childPosition).getDescription();
                if(diaryVOs.get(groupPosition).getItems1().get(childPosition).getOptionalSubject().matches("") || diaryVOs.get(groupPosition).getItems1().get(childPosition).getOptionalSubject().matches("null")) {
                    subjectName = diaryVOs.get(groupPosition).getItems1().get(childPosition).getSubjectName();
                }

                else
                {
                    subjectName = diaryVOs.get(groupPosition).getItems1().get(childPosition).getSubjectName()+"("+diaryVOs.get(groupPosition).getItems1().get(childPosition).getOptionalSubject()+")";
                }
                title = diaryVOs.get(groupPosition).getItems1().get(childPosition).getTitle();
                teachId  = diaryVOs.get(groupPosition).getItems1().get(childPosition).getTeacherId();
                teacherName = diaryVOs.get(groupPosition).getItems1().get(childPosition).getTeacherName();


                if(studentFile.matches("") || studentFile.matches("null"))
                {
                    if(file.matches("") || file.matches("null"))
                    {
                        value="1";
                        //aList = new ArrayList<String>(Arrays.asList(file.split(",")));
                        Intent intent = new Intent(StudentNewAssignemntVerion.this,StudentSubmitAssignmentAndHomework.class);
                        //intent.putExtra("array", aList);
                        intent.putExtra("teacherId", teachId);
                        intent.putExtra("assign_id", assignId);
                        intent.putExtra("desc",description);
                        intent.putExtra("title",title);
                        intent.putExtra("last_date",submitDate);
                        intent.putExtra("asn_date",diaryVOs.get(groupPosition).getDate());
                        intent.putExtra("subject",subjectName);
                        intent.putExtra("teach_name",teacherName);
                        intent.putExtra("value",value);
                        startActivity(intent);

                    }
                    else
                    {
                        value = "0";
                        aList = new ArrayList<String>(Arrays.asList(file.split(",")));
                        Intent intent = new Intent(StudentNewAssignemntVerion.this,StudentSubmitAssignmentAndHomework.class);
                        intent.putExtra("array", aList);
                        intent.putExtra("teacherId", teachId);
                        intent.putExtra("assign_id", assignId);
                        intent.putExtra("desc",description);
                        intent.putExtra("title",title);
                        intent.putExtra("last_date",submitDate);
                        intent.putExtra("asn_date",diaryVOs.get(groupPosition).getDate());
                        intent.putExtra("subject",subjectName);
                        intent.putExtra("teach_name",teacherName);
                        intent.putExtra("value",value);
                        startActivity(intent);

                    }

                }


                else {

                    aList = new ArrayList<String>(Arrays.asList(file.split(",")));
                    aList1 = new ArrayList<String>(Arrays.asList(studentFile.split(",")));
                    for (int i = 0; i < aList.size(); i++) {
                        System.out.println("" + aList.get(i));
                    }

                    for (int j = 0; j < aList1.size(); j++) {
                        System.out.println("" + aList1.get(j));
                    }

                    Intent intent = new Intent(StudentNewAssignemntVerion.this, AssignmentDetails.class);
                    intent.putExtra("array", aList);
                    intent.putExtra("array1", aList1);
                    intent.putExtra("teacherId", teachId);
                    intent.putExtra("assign_id", assignId);
                    intent.putExtra("desc", description);
                    intent.putExtra("title", title);
                    intent.putExtra("last_date", submitDate);
                    intent.putExtra("asn_date", diaryVOs.get(groupPosition).getDate());
                    intent.putExtra("subject", subjectName);
                    intent.putExtra("teach_name", teacherName);



                    startActivity(intent);
                }



                return false;
            }
        });

        new DownloadJSON1().execute();



    }

    private View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup == null) {
                calendarPopup = new PopupWindow(getApplicationContext());
                CalendarPickerView calendarView = new CalendarPickerView(getApplicationContext());
                calendarView.setListener(dateSelectionListener);
                calendarPopup.setContentView(calendarView);
                calendarPopup.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup.setHeight(1);
                calendarPopup.setWidth(view.getWidth());
                calendarPopup.setOutsideTouchable(true);
            }
            calendarPopup.showAsDropDown(view);
        }
    };

    private View.OnClickListener onEditTextClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup1 == null) {
                calendarPopup1 = new PopupWindow(StudentNewAssignemntVerion.this);
                CalendarPickerView calendarView = new CalendarPickerView(StudentNewAssignemntVerion.this);
                calendarView.setListener(dateSelectionListener1);
                calendarPopup1.setContentView(calendarView);
                calendarPopup1.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup1.setHeight(1);
                calendarPopup1.setWidth(view.getWidth());
                calendarPopup1.setOutsideTouchable(true);
            }
            calendarPopup1.showAsDropDown(view);
        }
    };

    private CalendarNumbersView.DateSelectionListener dateSelectionListener = new CalendarNumbersView.DateSelectionListener() {
        @Override
        public void onDateSelected(Calendar selectedDate) {
            if (calendarPopup.isShowing()) {
                calendarPopup.getContentView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        calendarPopup.dismiss();
                    }
                }, 500);//For clarity, we close the popup not immediately.
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            from1= formatter1.format(selectedDate.getTime());

            fromEditTextDate.setText(formatter.format(selectedDate.getTime()));
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            try {
                Date date1 = formatter.parse(date);
                String fromEditTextDate1 = fromEditTextDate.getText().toString();
                Date date2 = formatter.parse(fromEditTextDate1);

                if(date1.compareTo(date2)<0)
                {
                    Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count=0;
                }
                else
                {
                    count =1;
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private CalendarNumbersView.DateSelectionListener dateSelectionListener1 = new CalendarNumbersView.DateSelectionListener() {
        @Override
        public void onDateSelected(Calendar selectedDate) {
            if (calendarPopup1.isShowing()) {
                calendarPopup1.getContentView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        calendarPopup1.dismiss();
                    }
                }, 500);//For clarity, we close the popup not immediately.
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            to1= formatter1.format(selectedDate.getTime());
            toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            try {
                Date date1 = formatter.parse(date);

                String toEditTextDate1 = toEditTextDate.getText().toString();
                Date date2 = formatter.parse(toEditTextDate1);
                String fromEditTextDate1 = fromEditTextDate.getText().toString();
                Date date3 = formatter.parse(fromEditTextDate1);
                if(date1.compareTo(date2)<0)
                {
                    Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1 =0;
                }
                else
                if(date2.compareTo(date3)<0)
                {
                    Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1=0;
                }

                else
                if(date2.compareTo(date3)==0)
                {
                    Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1=0;
                }
                else
                {
                    count1 =1;

                    getStudentFeedList();

                }


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Preferences.getInstance().loadPreference(getApplicationContext());


            // Create an array to populate the spinner
            departmentId = new ArrayList<CategoryStudentAnalysisVO>();
            departmentName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            Log.d("URL", AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNEMENT_SUBJECT_LIST+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id"+Preferences.getInstance().studentSectionId+"&stu_id="+Preferences.getInstance().studentId+"&session="+Preferences.getInstance().session1);
            jsonobject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNEMENT_SUBJECT_LIST+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id"+Preferences.getInstance().studentSectionId+"&stu_id="+Preferences.getInstance().studentId+"&session="+Preferences.getInstance().session1);
            try {
                // Locate the NodeList name
                jsonarray1 = jsonobject1.getJSONArray("subject_list");
                for (int i = 0; i < jsonarray1.length(); i++) {
                    jsonobject1 = jsonarray1.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    //ClassVO classVO = new ClassVO();
                    CategoryStudentAnalysisVO categoryStudentAnalysisVO = new CategoryStudentAnalysisVO();

                    categoryStudentAnalysisVO.setCategoryId(jsonobject1.optString("subject_id"));
                    departmentId.add(categoryStudentAnalysisVO);

                    departmentName.add(jsonobject1.optString("subject"));

                    if(departmentId1.matches("") || departmentId1.matches("null"))
                    {

                    }
                    else
                    {
                        for(int j = 0;j<jsonarray1.length();j++)
                        {

                            if(jsonarray1.getJSONObject(j).getString("subject_id").matches(departmentId1))
                            {
                                selectedPosition = j;
                            }
                            else
                            {

                            }

                        }
                    }


                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            departmentType
                    .setAdapter(new ArrayAdapter<String>(StudentNewAssignemntVerion.this,
                            android.R.layout.simple_spinner_dropdown_item,departmentName
                    ));

            if(departmentId1.matches("") || departmentId1.matches("null"))
            {

            }
            else {
                departmentType.setSelection(selectedPosition + 1);
            }



            departmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub

                    Log.d("io",String.valueOf(position));


                    departmentId1 = departmentId.get(position).getCategoryId().toString();

                    System.out.println(departmentId1);
                    //initData();
                    getStudentFeedList();












                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                   departmentId1 = "";
                   getStudentFeedList1();

                }


            });




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

    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(StudentNewAssignemntVerion.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNMENT_NEW_LIST+"?u_email_id="+ Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&from_date="+from1+"&to_date="+to1+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1+"&subject_id="+departmentId1;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
              //  System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Assignments/Home-Works Found");
                        expandableListView.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        expandableListView.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("Assignment"))
                    {
                        expandableListView.setVisibility(View.VISIBLE);
                        AssignmentArray= new JSONObject(response).getJSONArray("Assignment");
                        if(null!=AssignmentArray && AssignmentArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = AssignmentArray.toString().getBytes();
                            diaryVOs = new ArrayList<DiaryVO>();
                            VolleySingleton.getInstance(StudentNewAssignemntVerion.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNMENT_NEW_LIST+"?u_email_id="+ Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&from_date="+from1+"&to_date="+to1+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1+"&subject_id="+departmentId1,e);


                            for(int i=0;i<AssignmentArray.length();i++)
                            {
                                DiaryVO diaryVO = new DiaryVO();
                                diaryVO.setDate(AssignmentArray.getJSONObject(i).getString("created_date"));
                                jsonObject = AssignmentArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");

                                assignmentMultiLevelVOs = new ArrayList<AssignmentMultiLevelVO>();

                                for(int j=0;j<jsonArray.length();j++)
                                {
                                    jsonObject1 = jsonArray.getJSONObject(j);
                                    AssignmentMultiLevelVO assignmentMultiLevelVO = new AssignmentMultiLevelVO();
                                    assignmentMultiLevelVO.setAssignId(jsonObject1.getString("id"));
                                    assignmentMultiLevelVO.setDescription(jsonObject1.getString("description"));
                                    assignmentMultiLevelVO.setOptionalSubject(jsonObject1.getString("opt_subject"));
                                    assignmentMultiLevelVO.setSubjectName(jsonObject1.getString("subject"));
                                    assignmentMultiLevelVO.setStudentFile(jsonObject1.getString("assignment"));
                                    assignmentMultiLevelVO.setTeacherfile(jsonObject1.getString("assign_path"));
                                    assignmentMultiLevelVO.setTitle(jsonObject1.getString("title"));
                                    assignmentMultiLevelVO.setTeacherName(jsonObject1.getString("teac_name"));
                                    assignmentMultiLevelVO.setSubmitdate(jsonObject1.getString("last_date"));
                                    assignmentMultiLevelVO.setTeacherId(jsonObject1.getString("student_description"));
                                    assignmentMultiLevelVO.setType(jsonObject1.getString("type"));
                                    assignmentMultiLevelVOs.add(assignmentMultiLevelVO);
                                }
                                diaryVO.setItems1(assignmentMultiLevelVOs);
                                diaryVOs.add(diaryVO);
                            }
                            expandableListView.invalidateViews();
                            assignmentMultiLevelListAdapter = new StudentNewAssignmentAdapter(StudentNewAssignemntVerion.this,diaryVOs);
                            expandableListView.setAdapter(assignmentMultiLevelListAdapter);
                            assignmentMultiLevelListAdapter.notifyDataSetChanged();

                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else {
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                        expandableListView.setVisibility(View.GONE);
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    expandableListView.setVisibility(View.GONE);

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                expandableListView.setVisibility(View.GONE);

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
            expandableListView.setVisibility(View.GONE);
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    protected void getStudentFeedList1()
    {

        RequestQueue queue = VolleySingleton.getInstance(StudentNewAssignemntVerion.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNMENT_NEW_LIST+"?u_email_id="+ Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&from_date="+from1+"&to_date="+to1+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Assignments/Home-Works Found");
                        expandableListView.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        expandableListView.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("Assignment"))
                    {
                        expandableListView.setVisibility(View.VISIBLE);
                        AssignmentArray= new JSONObject(response).getJSONArray("Assignment");
                        if(null!=AssignmentArray && AssignmentArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = AssignmentArray.toString().getBytes();
                            diaryVOs = new ArrayList<DiaryVO>();
                            VolleySingleton.getInstance(StudentNewAssignemntVerion.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ASSIGNMENT_NEW_LIST+"?u_email_id="+ Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&from_date="+from1+"&to_date="+to1+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1,e);


                            for(int i=0;i<AssignmentArray.length();i++)
                            {
                                DiaryVO diaryVO = new DiaryVO();
                                diaryVO.setDate(AssignmentArray.getJSONObject(i).getString("created_date"));
                                jsonObject = AssignmentArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");

                                assignmentMultiLevelVOs = new ArrayList<AssignmentMultiLevelVO>();

                                for(int j=0;j<jsonArray.length();j++)
                                {
                                    jsonObject1 = jsonArray.getJSONObject(j);
                                    AssignmentMultiLevelVO assignmentMultiLevelVO = new AssignmentMultiLevelVO();
                                    assignmentMultiLevelVO.setAssignId(jsonObject1.getString("id"));
                                    assignmentMultiLevelVO.setDescription(jsonObject1.getString("description"));
                                    assignmentMultiLevelVO.setOptionalSubject(jsonObject1.getString("opt_subject"));
                                    assignmentMultiLevelVO.setSubjectName(jsonObject1.getString("subject"));
                                    assignmentMultiLevelVO.setStudentFile(jsonObject1.getString("assignment"));
                                    assignmentMultiLevelVO.setTeacherfile(jsonObject1.getString("assign_path"));
                                    assignmentMultiLevelVO.setTitle(jsonObject1.getString("title"));
                                    assignmentMultiLevelVO.setTeacherName(jsonObject1.getString("teac_name"));
                                    assignmentMultiLevelVO.setSubmitdate(jsonObject1.getString("last_date"));
                                    assignmentMultiLevelVO.setTeacherId(jsonObject1.getString("student_description"));
                                    assignmentMultiLevelVO.setType(jsonObject1.getString("type"));
                                    assignmentMultiLevelVOs.add(assignmentMultiLevelVO);
                                }
                                diaryVO.setItems1(assignmentMultiLevelVOs);
                                diaryVOs.add(diaryVO);
                            }
                            expandableListView.invalidateViews();
                            assignmentMultiLevelListAdapter = new StudentNewAssignmentAdapter(StudentNewAssignemntVerion.this,diaryVOs);
                            expandableListView.setAdapter(assignmentMultiLevelListAdapter);
                            assignmentMultiLevelListAdapter.notifyDataSetChanged();

                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else {
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                        expandableListView.setVisibility(View.GONE);
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    expandableListView.setVisibility(View.GONE);

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                expandableListView.setVisibility(View.GONE);

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
            expandableListView.setVisibility(View.GONE);
        }
    }

}
