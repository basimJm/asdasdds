package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.TeacherGroupCreationExpandableListAdapter;
import com.schoofi.adapters.TeacherStudentGroupChatListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CategoryStudentAnalysisVO;
import com.schoofi.utils.ChatMainVO;
import com.schoofi.utils.ChatVO;
import com.schoofi.utils.DiaryGroupClassVO;
import com.schoofi.utils.DiaryGroupStudentVO;
import com.schoofi.utils.GroupTypeVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import smtchahal.materialspinner.MaterialSpinner;

public class TeacherGroupCreationPrimaryScreen extends AppCompatActivity {

    ImageView back;
    ExpandableListView expandableListView;
    Button create;
    MaterialSpinner groupType;
    EditText groupName;
    private String groupId1="";
    ArrayList<String> groupName1;
    ArrayList<GroupTypeVO> groupId;
    JSONObject jsonobject,jsonobject1,jsonObject2,jsonObject,jsonObject1;
    JSONArray jsonarray,jsonarray1,employeeListArray,jsonArray,jsonArray1;
    private JSONArray AssignmentArray,diaryMultipleStudentSelectionArray;
    private ArrayList<DiaryGroupClassVO> diaryVOs;
    private ArrayList<DiaryGroupStudentVO> assignmentMultiLevelVOs;
    private TeacherGroupCreationExpandableListAdapter assignmentMultiLevelListAdapter;
    public ArrayList<String> attendance = new ArrayList<String>();
    String array1,array2,array3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_teacher_group_creation_primary_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        expandableListView = (ExpandableListView) findViewById(R.id.listViewInnerAllAssignment);
        create = (Button) findViewById(R.id.btn_create);
        groupName = (EditText) findViewById(R.id.edit_leave_subject);
        groupType = (MaterialSpinner) findViewById(R.id.spinner_group_type);
        groupType.setBackgroundResource(R.drawable.grey_button);


        groupId=new ArrayList<GroupTypeVO>();
        GroupTypeVO tecHomeScreenVO = new GroupTypeVO("Group Of Students", "1");
        GroupTypeVO tecHomeScreenVO1 = new GroupTypeVO("Group Of Teachers", "2");


        groupId.add(tecHomeScreenVO);
        groupId.add(tecHomeScreenVO1);

        final CharSequence[] DAYS_OPTIONS  = {"Group Of Classes","Group Of Students","Group Of Teachers"};

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        groupType.setAdapter(adapter); // Apply the adapter to the spinner

        groupType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).toString().matches("Group Of Classes")) {
                    getStudentFeedList();
                    groupId1 = "1";
                    expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            return true;
                        }
                    });

                   // expandableListView.setGroupIndicator(@nul);
                }
                else if(parent.getItemAtPosition(position).toString().matches("Group Of Students"))
                {
                    getStudentFeedList();
                    groupId1 = "2";
                    expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            return false;
                        }
                    });
                }
                else
                {
                    groupId1 = "3";
                    Intent intent = new Intent(TeacherGroupCreationPrimaryScreen.this,TeacherGroupCreateActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (groupName.getText().toString().matches("")) {
                    Utils.showToast(getApplicationContext(), "Please Enter the Group Name!");
                } else {

                    if (groupId1.matches("1")) {
                        attendance.clear();
                        for (int i = 0; i < diaryVOs.size(); i++) {
                            if (diaryVOs.get(i).getIsAdded().matches("1")) {
                                attendance.add(diaryVOs.get(i).getSectionId());
                            }
                        }

                        array1 = attendance.toString();
                        array2 = array1.substring(1, array1.length() - 1);

                        String new_str = array2.replaceAll(" ",","); //replace , with space

                        String temp = new_str.trim();             //remove trailing space

                        array3=temp.replaceAll(",,",","); // now replacing space with ,

                        Log.d("isAdded", array2.toString());
                        if(array3.matches("") || array3.matches("null"))
                        {
                            Utils.showToast(getApplicationContext(),"Please mark at least one class!");
                        }
                        else
                        {
                            postAttendance();
                        }
                    }
                    else

                        if(groupId1.matches("2"))
                        {


                            attendance.clear();

                            for(int i=0;i<diaryVOs.size();i++) {

                                try {
                                    Log.d("pp",assignmentMultiLevelListAdapter.AssignmentArray.getJSONObject(i).getJSONArray("bifurcation").toString());
                                    jsonObject = AssignmentArray.getJSONObject(i);
                                    jsonArray = jsonObject.getJSONArray("bifurcation");

                                    for(int j=0;j<jsonArray.length();j++)
                                    {
                                        if(jsonArray.getJSONObject(j).getString("isAdded").matches("1"))
                                        {
                                            attendance.add(jsonArray.getJSONObject(j).getString("user_id"));
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                            array1 = attendance.toString();
                            array2 = array1.substring(1, array1.length() - 1);
                            String new_str = array2.replaceAll(" ",","); //replace , with space

                            String temp = new_str.trim();             //remove trailing space

                            array3=temp.replaceAll(",,",","); // now replacing space with ,
                            //Log.d("pi",array3);
                            if(array3.matches("") || array3.matches("null"))
                            {
                                Utils.showToast(getApplicationContext(),"Please mark the students!");
                            }
                            else
                            {
                                postAttendance();
                            }

                    }
                    else
                    {
                        try
                        {
                            Cache.Entry e;
                            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.DIARY_TEACHER_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id");
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
                          for (int i=0;i<diaryMultipleStudentSelectionArray.length();i++)
                          {
                              try {
                                  if(diaryMultipleStudentSelectionArray.getJSONObject(i).getString("isAdded").matches("1"))
                                  {
                                      attendance.add(diaryMultipleStudentSelectionArray.getJSONObject(i).getString("user_id"));
                                  }
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }

                            array1 = attendance.toString();
                            array2 = array1.substring(1, array1.length() - 1);
                            String new_str = array2.replaceAll(" ",","); //replace , with space

                            String temp = new_str.trim();             //remove trailing space

                            array3=temp.replaceAll(",,",","); // now replacing space with ,
                            //Log.d("pi",array3);
                            if(array3.matches("") || array3.matches("null"))
                            {
                                Utils.showToast(getApplicationContext(),"Please mark the students!");
                            }
                            else
                            {
                                postAttendance();
                            }

                        }
                    }

                }
            }
        });





    }


    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(TeacherGroupCreationPrimaryScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TECAHER_CLASS_LSIT+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&u_id="+Preferences.getInstance().userId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId;
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
                    if(responseObject.has("class_List"))
                    {
                        expandableListView.setVisibility(View.VISIBLE);
                        AssignmentArray= new JSONObject(response).getJSONArray("class_List");
                        if(null!=AssignmentArray && AssignmentArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = AssignmentArray.toString().getBytes();
                            diaryVOs = new ArrayList<DiaryGroupClassVO>();
                            VolleySingleton.getInstance(TeacherGroupCreationPrimaryScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TECAHER_CLASS_LSIT+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&u_id="+Preferences.getInstance().userId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId,e);


                            for(int i=0;i<AssignmentArray.length();i++)
                            {
                                AssignmentArray.getJSONObject(i).put("isAdded","0");
                                DiaryGroupClassVO diaryVO = new DiaryGroupClassVO();
                                diaryVO.setClassId(AssignmentArray.getJSONObject(i).getString("class_id"));
                                diaryVO.setSectionId(AssignmentArray.getJSONObject(i).getString("section_id"));
                                diaryVO.setClassName(AssignmentArray.getJSONObject(i).getString("class_name"));
                                diaryVO.setSectionName(AssignmentArray.getJSONObject(i).getString("section_name"));
                                diaryVO.setIsAdded(AssignmentArray.getJSONObject(i).getString("isAdded"));
                                jsonObject = AssignmentArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");

                                assignmentMultiLevelVOs = new ArrayList<DiaryGroupStudentVO>();

                                for(int j=0;j<jsonArray.length();j++)
                                {
                                    AssignmentArray.getJSONObject(i).getJSONArray("bifurcation").getJSONObject(j).put("isAdded","0");
                                    jsonArray.getJSONObject(j).put("isAdded","0");
                                    jsonObject1 = jsonArray.getJSONObject(j);

                                    DiaryGroupStudentVO assignmentMultiLevelVO = new DiaryGroupStudentVO();
                                    assignmentMultiLevelVO.setIsAdded(jsonObject1.getString("isAdded"));
                                    assignmentMultiLevelVO.setStudentId(jsonObject1.getString("stu_id"));
                                    assignmentMultiLevelVO.setStudentName(jsonObject1.getString("stu_name"));
                                    assignmentMultiLevelVO.setStudentPicture(jsonObject1.getString("picture"));
                                    assignmentMultiLevelVO.setStudentRoll(jsonObject1.getString("class_roll_no"));


                                    assignmentMultiLevelVOs.add(assignmentMultiLevelVO);
                                }
                                diaryVO.setItems(assignmentMultiLevelVOs);
                                diaryVOs.add(diaryVO);
                            }
                            expandableListView.invalidateViews();
                            assignmentMultiLevelListAdapter = new TeacherGroupCreationExpandableListAdapter(TeacherGroupCreationPrimaryScreen.this,diaryVOs,AssignmentArray);
                            expandableListView.setAdapter(assignmentMultiLevelListAdapter);
                            assignmentMultiLevelListAdapter.notifyDataSetChanged();
                            //expandableListView.setSelection(AssignmentArray.length());
                            //expandableListView.smoothScrollToPosition(AssignmentArray.length());

                            // swipyRefreshLayout.setRefreshing(false);

                            // scrollToEnd();

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


    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_GROUP_CREATION_ACTIVITY/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(),"Error Creating Event");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(),"Successfully Created ");

                        finish();

                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                params.put("created_by",Preferences.getInstance().userId);
                params.put("group_name",groupName.getText().toString());

                if(groupId1.matches("1"))
                {
                    params.put("group_type","groupofclass");
                    params.put("section_ids",array3);

                }
                else
                    if(groupId1.matches("2"))
                    {
                        params.put("group_type","groupofstudent");
                        params.put("member_stu_user_ids",array3);
                    }
                    else
                    {
                        params.put("group_type","groupofteacher");
                        params.put("member_teachers",array3);
                    }


                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(this))
            queue.add(requestObject);
        else
        {
            loading.dismiss();
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            //setSupportProgressBarIndeterminateVisibility(false);
        }



    }
}
