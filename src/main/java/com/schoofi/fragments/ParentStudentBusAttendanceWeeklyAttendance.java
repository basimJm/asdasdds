package com.schoofi.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import com.schoofi.adapters.ParentStudentBusAttendanceListAdapter;
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
 * Created by Schoofi on 06-03-2017.
 */

public class ParentStudentBusAttendanceWeeklyAttendance extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    public JSONArray studentWeeklyAttendanceArray;
    private ListView studentWeeklyAttendanceListView;
    private TextView studentWeeklyAttendanceDateTitle,studentWeeklyAttendanceAttendanceTitle,newView;
    ParentStudentBusAttendanceListAdapter parentStudentBusAttendanceListAdapter;
    private Context context;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String fromDate,toDate;
    SwipyRefreshLayout swipyRefreshLayout;

    public static ParentStudentBusAttendanceWeeklyAttendance newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ParentStudentBusAttendanceWeeklyAttendance parentStudentBusAttendanceWeeklyAttendance = new ParentStudentBusAttendanceWeeklyAttendance();
        parentStudentBusAttendanceWeeklyAttendance.setArguments(args);
        return parentStudentBusAttendanceWeeklyAttendance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_weekly_attendance, container, false);
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
                getStudentDailyAttendance();
            }
        });
        context = getActivity();
        //System.out.println(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_WEEKLY_ATTENDANCE_URL+"?sch_id="+Preferences.getInstance().schoolId+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sec_id="+Preferences.getInstance().studentSectionId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&startingDate="+fromDate+"&endingDate="+toDate);
        initData();
        getStudentDailyAttendance();
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initData();
        getStudentDailyAttendance();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_STUDENT_BUS_ATTENDANCE+"?sch_id="+ Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&from_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId);
            if(e == null)
            {
                studentWeeklyAttendanceArray= null;
            }
            else
            {
                studentWeeklyAttendanceArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentWeeklyAttendanceArray!= null)
        {
            parentStudentBusAttendanceListAdapter= new ParentStudentBusAttendanceListAdapter(getActivity(),studentWeeklyAttendanceArray);
            studentWeeklyAttendanceListView.setAdapter(parentStudentBusAttendanceListAdapter);
            parentStudentBusAttendanceListAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentDailyAttendance()
    {
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_STUDENT_BUS_ATTENDANCE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    //System.out.println(reponseObject);
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
                    if(responseObject.has("responseObject"))
                    {
                        newView.setVisibility(View.INVISIBLE);
                        studentWeeklyAttendanceListView.setVisibility(View.VISIBLE);
                        studentWeeklyAttendanceArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=studentWeeklyAttendanceArray && studentWeeklyAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentWeeklyAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_STUDENT_BUS_ATTENDANCE+"?sch_id="+ Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&from_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId,e);
                            studentWeeklyAttendanceListView.invalidateViews();
                            parentStudentBusAttendanceListAdapter = new ParentStudentBusAttendanceListAdapter(getActivity(), studentWeeklyAttendanceArray);
                            studentWeeklyAttendanceListView.setAdapter(parentStudentBusAttendanceListAdapter);
                            parentStudentBusAttendanceListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }

                    else
                    {
                        System.out.println("harsh");
                    }
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
                params.put("from_date",fromDate);
                params.put("token",Preferences.getInstance().token);
                params.put("to_date",toDate);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("stu_id",Preferences.getInstance().studentId);
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
