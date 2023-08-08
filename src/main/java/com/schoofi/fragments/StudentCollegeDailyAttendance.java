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
import com.schoofi.adapters.StudentCollegeAttendanceAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schoofi on 10-05-2019.
 */

public class StudentCollegeDailyAttendance extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    //public ArrayList<StudentDailyAttendanceVO> studentDailyAttendanceList;
    public JSONArray studentDailyAttendanceArray;
    private ListView studentDailyAttendanceListView;
    private TextView studentDailyAttendanceDateTitle,studentDailyAttendanceAttendanceTitle,newView;
    StudentCollegeAttendanceAdapter studentDailyAttendanceAdapter;
    private Context context;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    SwipyRefreshLayout swipyRefreshLayout;


    public static StudentCollegeDailyAttendance newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        StudentCollegeDailyAttendance studentCollegeDailyAttendance = new StudentCollegeDailyAttendance();
        studentCollegeDailyAttendance.setArguments(args);
        return studentCollegeDailyAttendance;
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
        //studentDailyAttendanceList = new ArrayList<StudentDailyAttendanceVO>();
        studentDailyAttendanceDateTitle = (TextView) view.findViewById(R.id.text_date);
        studentDailyAttendanceAttendanceTitle = (TextView) view.findViewById(R.id.text_attendance);
        studentDailyAttendanceListView = (ListView) view.findViewById(R.id.listView_student_weekly_attendance);
        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getStudentDailyAttendance();
            }
        });
        newView = (TextView) view.findViewById(R.id.newView1);
		/*StudentDailyAttendanceVO vo1 = new StudentDailyAttendanceVO();
		//StudentDailyAttendanceVO vo2  = new StudentDailyAttendanceVO();
		vo1.setDate("2015-12-12");
		vo1.setId(1);
		vo1.setIconResId(R.drawable.ic_action_back);
		studentDailyAttendanceList.add(vo1);*/
        //System.out.println(currentDate);
        context = getActivity();
        initData();
        getStudentDailyAttendance();
        //System.out.println("fff"+currentDate);

        //StudentDailyAttendanceAdapter studentDailyAttendanceAdapter;
        //studentDailyAttendanceListView.setAdapter(new StudentDailyAttendanceAdapter(getActivity(),studentDailyAttendanceArray));
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
        getStudentDailyAttendance();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_DAILY_ATTENDANCE+"?sch_id="+ Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&crr_date="+currentDate+"&device_id="+Preferences.getInstance().deviceId+"&subject_id="+Preferences.getInstance().studentSubjectId);
            if(e == null)
            {
                studentDailyAttendanceArray= null;
            }
            else
            {
                studentDailyAttendanceArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentDailyAttendanceArray!= null)
        {
            studentDailyAttendanceAdapter= new StudentCollegeAttendanceAdapter(getActivity(),studentDailyAttendanceArray);
            studentDailyAttendanceListView.setAdapter(studentDailyAttendanceAdapter);
            studentDailyAttendanceAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentDailyAttendance()
    {
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_DAILY_ATTENDANCE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    //System.out.println(reponseObject);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        newView.setVisibility(View.VISIBLE);
                        studentDailyAttendanceListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(context, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("atten_daily"))
                    {
                        newView.setVisibility(View.INVISIBLE);
                        studentDailyAttendanceListView.setVisibility(View.VISIBLE);
                        studentDailyAttendanceArray= new JSONObject(response).getJSONArray("atten_daily");
                        if(null!=studentDailyAttendanceArray && studentDailyAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentDailyAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_DAILY_ATTENDANCE+"?sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&crr_date="+currentDate+"&device_id="+Preferences.getInstance().deviceId+"&subject_id="+Preferences.getInstance().studentSubjectId,e);
                            studentDailyAttendanceListView.invalidateViews();
                            studentDailyAttendanceAdapter = new StudentCollegeAttendanceAdapter(getActivity(), studentDailyAttendanceArray);
                            studentDailyAttendanceListView.setAdapter(studentDailyAttendanceAdapter);
                            studentDailyAttendanceAdapter.notifyDataSetChanged();
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
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("crr_date",currentDate);
                params.put("cls_id", Preferences.getInstance().studentClassId);
                params.put("device_id", Preferences.getInstance().deviceId);
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
