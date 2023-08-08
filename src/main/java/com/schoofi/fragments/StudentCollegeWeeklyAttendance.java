package com.schoofi.fragments;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.StudentCollegeAttendanceAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schoofi on 10-05-2019.
 */

public class StudentCollegeWeeklyAttendance extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    public JSONArray studentWeeklyAttendanceArray;
    private ListView studentWeeklyAttendanceListView;
    private TextView studentWeeklyAttendanceDateTitle,studentWeeklyAttendanceAttendanceTitle,newView;
    StudentCollegeAttendanceAdapter studentWeeklyAttendanceAdapter;
    private Context context;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String fromDate,toDate;
    SwipyRefreshLayout swipyRefreshLayout;
    //cal.add(Calendar.DATE, -7);




    public static StudentCollegeWeeklyAttendance newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        StudentCollegeWeeklyAttendance studentCollegeWeeklyAttendance = new StudentCollegeWeeklyAttendance();
        studentCollegeWeeklyAttendance.setArguments(args);
        return studentCollegeWeeklyAttendance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_college_daily_weekly_attendance_layout, container, false);
        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();
        fromDate = new SimpleDateFormat("yyyy-MM-dd").format(todate1);
        //System.out.println(fromDate);
        cal1.add(Calendar.DATE, -1);
        Date todate2 = cal1.getTime();
        toDate = new SimpleDateFormat("yyyy-MM-dd").format(todate2);
        studentWeeklyAttendanceDateTitle = (TextView) view.findViewById(R.id.text_date);
        newView = (TextView) view.findViewById(R.id.newView1);
        studentWeeklyAttendanceAttendanceTitle = (TextView) view.findViewById(R.id.text_attendance);
        studentWeeklyAttendanceListView = (ListView) view.findViewById(R.id.listView_student_weekly_attendance);
        swipyRefreshLayout= (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getStudentWeeklyAttendance();
            }
        });
        context = getActivity();
        //System.out.println(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_WEEKLY_ATTENDANCE_URL+"?sch_id="+Preferences.getInstance().schoolId+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sec_id="+Preferences.getInstance().studentSectionId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&startingDate="+fromDate+"&endingDate="+toDate);
        initData();
        getStudentWeeklyAttendance();
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initData();
        getStudentWeeklyAttendance();
    }

    private void initData()
    {
        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_CUSTOM_ATTENDANCE+"?sch_id="+ Preferences.getInstance().schoolId+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sec_id="+Preferences.getInstance().studentSectionId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&startingDate="+fromDate+"&endingDate="+toDate+"&device_id="+Preferences.getInstance().deviceId+"&subject_id="+Preferences.getInstance().studentSubjectId);
            if(e == null)
            {
                studentWeeklyAttendanceArray = null;
            }
            else
            {
                studentWeeklyAttendanceArray = new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentWeeklyAttendanceArray!= null)
        {
            studentWeeklyAttendanceAdapter= new StudentCollegeAttendanceAdapter(getActivity(),studentWeeklyAttendanceArray);
            studentWeeklyAttendanceListView.setAdapter(studentWeeklyAttendanceAdapter);
            studentWeeklyAttendanceAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentWeeklyAttendance()
    {
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_CUSTOM_ATTENDANCE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //System.out.println(response);

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    toa();

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        newView.setVisibility(View.VISIBLE);
                        studentWeeklyAttendanceListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(context, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Test_Variable"))
                    {
                        newView.setVisibility(View.INVISIBLE);
                        studentWeeklyAttendanceListView.setVisibility(View.VISIBLE);
                        studentWeeklyAttendanceArray= new JSONObject(response).getJSONArray("Test_Variable");
                        if(null!=studentWeeklyAttendanceArray && studentWeeklyAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentWeeklyAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_CUSTOM_ATTENDANCE+"?sch_id="+Preferences.getInstance().schoolId+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sec_id="+Preferences.getInstance().studentSectionId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&startingDate="+fromDate+"&endingDate="+toDate+"&device_id="+Preferences.getInstance().deviceId+"&subject_id="+Preferences.getInstance().studentSubjectId,e);
                            studentWeeklyAttendanceListView.invalidateViews();

                            studentWeeklyAttendanceAdapter = new StudentCollegeAttendanceAdapter(context, studentWeeklyAttendanceArray);
                            studentWeeklyAttendanceListView.setAdapter(studentWeeklyAttendanceAdapter);
                            studentWeeklyAttendanceAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else
                        Utils.showToast(context, "Error Fetching Response");
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(context, "Error fetching modules! Please try after sometime.");
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getActivity());
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("startingDate",fromDate);
                params.put("endingDate", toDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("cls_id", Preferences.getInstance().studentClassId);
                params.put("subject_id",Preferences.getInstance().studentSubjectId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(context))
            queue.add(requestObject);
        else
        {
            Utils.showToast(context, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
