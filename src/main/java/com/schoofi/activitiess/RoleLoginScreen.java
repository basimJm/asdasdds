package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activities.LoginScreen;
import com.schoofi.activities.StudentHomeScreen;
import com.schoofi.activities.TeacherHomeScreen;
import com.schoofi.adapters.HealthAndAuditAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.TeacherHomeScreenVO;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RoleLoginScreen extends AppCompatActivity {

    public ArrayList<TeacherHomeScreenVO> temparr;
    private TextView logout;
    //TeacherHomeScreenAdapter teacherHomeScreenAdapter;
    private ListView healthAndAuditListView;
    ImageView back;
    private String userEmail,userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_role_login_screen);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        logout = (TextView) findViewById(R.id.txt_teacherLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoleLoginScreen.this,LoginScreen.class);
                Preferences.getInstance().isLoggedIn = false;
                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                startActivity(intent);
            }
        });

        //userEmail = getIntent().getStringExtra("user_name");
        //userPassword = getIntent().getStringExtra("user_pass");

        Preferences.getInstance().loadPreference(getApplicationContext());
        userEmail = Preferences.getInstance().userName1;
        userPassword = Preferences.getInstance().userPassword;

        temparr=new ArrayList<TeacherHomeScreenVO>();
        Preferences.getInstance().loadPreference(getApplicationContext());
        final ArrayList aList = new ArrayList(Arrays.asList(Preferences.getInstance().fullRoleId.split(",")));
        for(int i=0;i<aList.size();i++)
        {
            if(aList.get(i).toString().matches("1"))
            {
                TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Super Admin", String.valueOf(i));
                temparr.add(tecHomeScreenVO);
            }
            else
                if(aList.get(i).toString().matches("2"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Institute Admin", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("3"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Admin", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("4"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Teacher", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("5"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Student", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("6"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Parent", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("7"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Chairman", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("8"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Principal", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("9"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Enquiry User", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("10"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Bus Admin", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("11"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Fee Admin", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("12"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Enquiry Admin", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("13"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Institute Fee Admin", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("14"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Admission Co-ordinator", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("15"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Medical Co-ordinator", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("16"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Bus Co-ordinator", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("17"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Security Co-ordinator", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("18"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Bus Tracking Co-ordinator", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("19"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("H/M Co-ordinator", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("20"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Exam Admin", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("21"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Director", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("22"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Visitor", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("23"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Receptionist", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("24"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Hostel Admin", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("26"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Non Teaching", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("27"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Human Resource", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }

                else
                if(aList.get(i).toString().matches("0"))
                {
                    TeacherHomeScreenVO tecHomeScreenVO = new TeacherHomeScreenVO("Audit User", String.valueOf(i));
                    temparr.add(tecHomeScreenVO);
                }
        }

        healthAndAuditListView = (ListView) findViewById(R.id.teacher_homescreen_listview);
        HealthAndAuditAdapter healthAndAuditAdapter = new HealthAndAuditAdapter(getApplicationContext(),temparr);
        healthAndAuditListView.setAdapter(healthAndAuditAdapter);

        healthAndAuditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Preferences.getInstance().loadPreference(getApplicationContext());
                Preferences.getInstance().userRoleId = String.valueOf(aList.get(position));
               //
                Preferences.getInstance().savePreference(getApplicationContext());
                if(Preferences.getInstance().userRoleId.matches("null") || Preferences.getInstance().userRoleId.matches(""))
                {

                }
                else
                {
                    //Utils.showToast(getApplicationContext(),Preferences.getInstance().userRoleId);
                    login();
                }

            }
        });


    }

    private void login()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        final ProgressDialog dialog;
        dialog = new ProgressDialog(RoleLoginScreen.this, AlertDialog.THEME_HOLO_DARK);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        dialog.show();

        //System.out.println("username: " + userName);
        //System.out.println("userPass: "+ userPass);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        String urlString = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.LOGIN_DETAILS/*+"?user_Name="+userName+"&user_Pass="+userPass*/;
        //System.out.println(urlString);
        StringRequest requestObject = new StringRequest(Request.Method.POST,urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                dialog.dismiss();
                Log.d("op",response);
                try
                {
                    toa();
                    responseObject = new JSONObject(response);
                    System.out.println(responseObject.toString());
                    if(responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
                        Toast.makeText(RoleLoginScreen.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    else if(responseObject.has("User_details")) {
                       // Utils.showToast(getApplicationContext(),"90");
                       //



                            Intent intent;
                            Preferences.getInstance().loadPreference(getApplicationContext());
                            if (Preferences.getInstance().userRoleId.matches("5")) {

                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().studentSectionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("section_id");
                                Preferences.getInstance().studentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("stu_id");
                                Preferences.getInstance().studentClassId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("class_id");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                               // Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                //Preferences.getInstance().busRouteNo = responseObject.getJSONArray("User_details").getJSONObject(0).getString("route_no");
                                //Preferences.getInstance().busNumber =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("bus_number");
                                //Preferences.getInstance().lattitude =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("latitude");
                                //Preferences.getInstance().longitude =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("longitude");
                                //Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("mobile");
                                Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
                                Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
                                Preferences.getInstance().addmissionNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("admn_No");
                                Preferences.getInstance().feesAddCharges = responseObject.getJSONArray("User_details").getJSONObject(0).getString("fee_add_charges");
                                Preferences.getInstance().eventAddCharges = responseObject.getJSONArray("User_details").getJSONObject(0).getString("event_add_charges");
                                Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                                Preferences.getInstance().session1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("session");
                                Preferences.getInstance().board = responseObject.getJSONArray("User_details").getJSONObject(0).getString("board");
                                Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
                                Preferences.getInstance().schoolType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_type");
                                Preferences.getInstance().studentGender = responseObject.getJSONArray("User_details").getJSONObject(0).getString("gender");
                                Preferences.getInstance().studentSemester = responseObject.getJSONArray("User_details").getJSONObject(0).getString("semester");
                                Preferences.getInstance().studentCourse = responseObject.getJSONArray("User_details").getJSONObject(0).getString("course_id");
                                Preferences.getInstance().isHostelStudent = responseObject.getJSONArray("User_details").getJSONObject(0).getString("is_hostel");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, StudentHomeScreen.class);

                                startActivity(intent);
                                //finish();
                            } else if (Preferences.getInstance().userRoleId.matches("7")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							/*Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");*/
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, ChairmanHomeScreen.class);
                                startActivity(intent);
                                finish();
                            } else if (Preferences.getInstance().userRoleId.matches("6")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, ParentHomeScreen.class);
                                startActivity(intent);
                               // finish();
                            } else if (Preferences.getInstance().userRoleId.matches("8")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
                                //Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("mobile");
                                Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
                                Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().session1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("session");
                                Preferences.getInstance().board = responseObject.getJSONArray("User_details").getJSONObject(0).getString("board");
                                Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
                                Preferences.getInstance().totalStudents = responseObject.getJSONArray("User_details").getJSONObject(0).getString("total_stu");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
                                Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
                                Preferences.getInstance().schoolType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_type");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, ChairmanDashboard.class);
                                startActivity(intent);
                               // finish();

                            } else if (Preferences.getInstance().userRoleId.matches("4")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                              //  Preferences.getInstance().teachId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                //Preferences.getInstance().busRouteNo = responseObject.getJSONArray("User_details").getJSONObject(0).getString("route_no");
                                //Preferences.getInstance().busNumber =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("bus_number");
                                //Preferences.getInstance().lattitude =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("latitude");
                                //Preferences.getInstance().longitude =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("longitude");
                                Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("mobile");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
                                Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
                                Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                             //   Preferences.getInstance().teacherId1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().session1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("session");
                                Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
                                Preferences.getInstance().board = responseObject.getJSONArray("User_details").getJSONObject(0).getString("board");
                                Preferences.getInstance().chairmanAssignmentTeacherId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
                                Preferences.getInstance().schoolType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_type");
                                Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");

                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, TeacherHomeScreen.class);
                                startActivity(intent);
                              //  finish();
                            } else if (Preferences.getInstance().userRoleId.matches("0")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                //Preferences.getInstance().teachId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("teac_id");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");

                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, AuditUserHomeScreen.class);
                                startActivity(intent);
                               // finish();
                            } else if (Preferences.getInstance().userRoleId.matches("3")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                //Preferences.getInstance().teachId = "Harsh jkkl";
                                Preferences.getInstance().session1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("session");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
                                Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
                                Preferences.getInstance().healthAndSafety = responseObject.getJSONArray("User_details").getJSONObject(0).getString("health_safety");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
                                Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, AdminHomeScreen.class);
                                startActivity(intent);
                               // finish();
                            } else if (Preferences.getInstance().userRoleId.matches("9")) {

                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                               // Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");

                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, AdmissionEnquiry.class);
                                startActivity(intent);
                               // finish();
                            } else if (Preferences.getInstance().userRoleId.matches("10")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                               // Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");

                                Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
                                Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
                                Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
                                Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, BusAdminHomeScreen.class);
                                startActivity(intent);
                              //  finish();
                            } else if (Preferences.getInstance().userRoleId.matches("11")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                              //  Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");

                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, FeeAdminScreen.class);
                                startActivity(intent);
                              //  finish();
                            } else if (Preferences.getInstance().userRoleId.matches("2")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                //Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                               // Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                //Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                //Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, ChairmanHomeScreen.class);
                                startActivity(intent);
                              //  finish();
                            } else if (Preferences.getInstance().userRoleId.matches("15")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                //Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                //Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                //Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, MedicalCoordinatorHomeScreen.class);
                                startActivity(intent);
                              //  finish();
                            } else if (Preferences.getInstance().userRoleId.matches("16")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                //Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                              //  Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                //Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                //Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");

                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
                                Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, BusCoordinatorAdminScreen.class);
                                startActivity(intent);
                              //  finish();
                            } else if (Preferences.getInstance().userRoleId.matches("17")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                //Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                //Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                //Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
                                Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, SecurityAdminScreen.class);
                                startActivity(intent);
                              //  finish();
                            }

                            else if (Preferences.getInstance().userRoleId.matches("26")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                //Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                //Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                //Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
                                Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, NonTeachingHomeScreen.class);
                                startActivity(intent);
                                //  finish();
                            }

                            else if (Preferences.getInstance().userRoleId.matches("27")) {
                                Preferences.getInstance().loadPreference(RoleLoginScreen.this);
                                Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
                                Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
                                Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
                                Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
                                //Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
                                Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
                                //Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
                                Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
                                Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                //Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
                                //Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
                                Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
                                Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
                                Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
                                Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
                                Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
                                Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
                                Preferences.getInstance().isLoggedIn = true;
                                Preferences.getInstance().savePreference(RoleLoginScreen.this);
                                intent = new Intent(RoleLoginScreen.this, HRHomeScreen.class);
                                startActivity(intent);
                                //  finish();
                            }
                        }



                }

                catch(JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(RoleLoginScreen.this, "Error fetching modules! Please try after sometime", Toast.LENGTH_SHORT).show();
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {


            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Toast.makeText(RoleLoginScreen.this, "Try after sometime", Toast.LENGTH_SHORT).show();
                setSupportProgressBarIndeterminateVisibility(false);
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();


                params.put("device_id", Preferences.getInstance().deviceId);
                Log.d("device_id", Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                Log.d("token",Preferences.getInstance().token);
                params.put("role_id",Preferences.getInstance().userRoleId);
                Log.d("role_id",Preferences.getInstance().userRoleId);
                params.put("user_Name",userEmail);
                Log.d("user_Name",userEmail);
                params.put("user_Pass",userPassword);
                Log.d("user_Pass",userPassword);

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
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            dialog.dismiss();
        }
    }

    public void toa(){
        System.out.println("sdkbksdbksd");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
