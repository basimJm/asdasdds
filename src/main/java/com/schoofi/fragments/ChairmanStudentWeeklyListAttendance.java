package com.schoofi.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.ChairmanAttendenceDetails;
import com.schoofi.activitiess.ChairmanSchoolAttendance;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.ChairmanAttendanceListListViewAdapter;
import com.schoofi.adapters.ChairmanDailySectionListAdapter;
import com.schoofi.adapters.ChairmanStudentDailyClassListAdapter;
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
 * Created by Schoofi on 20-07-2016.
 */
public class ChairmanStudentWeeklyListAttendance extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private JSONArray chairmanStudentDailyAttendanceArray;
    private JSONArray chairmanClass1ListArray;
    private TextView selectClass,selectSection,schoolWise,noOfStudents;
    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String fromDate,toDate;
    private int mPage;
    int width;
    PopupWindow popupWindow,popupWindow1;
    private JSONArray chairmanSection1ListArray;
    ChairmanStudentDailyClassListAdapter chairmanStudentDailyClassListAdapter;
    ChairmanDailySectionListAdapter chairmanDailySectionListAdapter;
    private ListView chairmanSectionListView,chairmanClassListView,attendanceListView;
    String sectionId,totalClassStudents;
    String classId,totalSectionStudents;
    ChairmanAttendanceListListViewAdapter chairmanAttendanceListListViewAdapter;
    int value=0;

    public static ChairmanStudentWeeklyListAttendance newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ChairmanStudentWeeklyListAttendance chairmanStudentWeeklyListAttendance = new ChairmanStudentWeeklyListAttendance();
        chairmanStudentWeeklyListAttendance.setArguments(args);
        return chairmanStudentWeeklyListAttendance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chairman_attendance_fragment_daily_weekly_list_layout, container, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((ChairmanSchoolAttendance) getContext()).getWindowManager()
                .getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;

        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();
        cal1.add(Calendar.DATE, -1);
        Date toDate2 = cal1.getTime();
        toDate = new SimpleDateFormat("yyyy-MM-dd").format(toDate2);
        fromDate = new SimpleDateFormat("yyyy-MM-dd").format(todate1);
        selectClass = (TextView) view.findViewById(R.id.text_ch_daily_class_wise);
        selectSection = (TextView) view.findViewById(R.id.text_ch_daily_section_wise);
        schoolWise = (TextView) view.findViewById(R.id.text_ch_daily_school_wise);

        attendanceListView = (ListView) view.findViewById(R.id.chairman_attendance_listView);

        noOfStudents = (TextView) view.findViewById(R.id.text_no_of_students);

        attendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {



                Intent intent = new Intent(getActivity(), ChairmanAttendenceDetails.class);
                intent.putExtra("position",position);
                intent.putExtra("value",value);
                intent.putExtra("from_date",fromDate);
                intent.putExtra("to_date",toDate);
                intent.putExtra("temp","2");
                intent.putExtra("cls_id",classId);
                intent.putExtra("sec_id",sectionId);
                startActivity(intent);

            }
        });

        Preferences.getInstance().loadPreference(getActivity());
        initData();
        getStudentAnnouncementList();

        chairmanClassListView = new ListView(getActivity());


        popupWindow1 = ClassDailyListWindow();

        schoolWise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectClass.setText("Select Class");
                selectSection.setText("Select Section");

                initData();
                getStudentAnnouncementList();

            }
        });

        selectClass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                popupWindow1.showAsDropDown(v, -5, 0);

            }
        });


        chairmanClassListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                fadeInAnimation.setDuration(10);
                v.startAnimation(fadeInAnimation);

                popupWindow1.dismiss();


                try {
                    selectClass.setText(chairmanClass1ListArray.getJSONObject(position).getString("class_name"));
                    classId = chairmanClass1ListArray.getJSONObject(position).getString("class_id");
                    totalClassStudents = chairmanClass1ListArray.getJSONObject(position).getString("total_stu_cls");


                    getStudentAnnouncementList1();


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



            }
        });


        selectSection.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                chairmanSectionListView = new ListView(getActivity());
                popupWindow = SectionDailyListWindow();
                popupWindow.showAsDropDown(v,-5,0);
                chairmanSectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View v, int position,
                                            long arg3) {
                        // TODO Auto-generated method stub

                        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                        fadeInAnimation.setDuration(10);
                        v.startAnimation(fadeInAnimation);

                        popupWindow.dismiss();

                        try {
                            selectSection.setText(chairmanSection1ListArray.getJSONObject(position).getString("section_name"));
                            sectionId = chairmanSection1ListArray.getJSONObject(position).getString("section_id");
                            totalSectionStudents = chairmanSection1ListArray.getJSONObject(position).getString("total_stu_cls_sec");

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        getStudentAnnouncementList2();
                    }
                });

            }
        });





        return view;
    }

    private void initData2()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_CLASS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                chairmanClass1ListArray= null;
            }
            else
            {
                chairmanClass1ListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanClass1ListArray!= null)
        {
            chairmanStudentDailyClassListAdapter= new ChairmanStudentDailyClassListAdapter(getActivity(),chairmanClass1ListArray);
            chairmanClassListView.setAdapter(chairmanStudentDailyClassListAdapter);
            chairmanStudentDailyClassListAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList()
    {

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_CLASS_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        attendanceListView.setVisibility(View.INVISIBLE);
                        Utils.showToast(getActivity(),"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Classes"))
                    {
                        attendanceListView.setVisibility(View.VISIBLE);
                        chairmanClass1ListArray= new JSONObject(response).getJSONArray("Classes");
                        if(null!=chairmanClass1ListArray && chairmanClass1ListArray.length()>=0)
                        {
                            com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
                            e.data = chairmanClass1ListArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_CLASS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId,e);
                            chairmanClassListView.invalidateViews();
                            chairmanStudentDailyClassListAdapter = new ChairmanStudentDailyClassListAdapter(getActivity(), chairmanClass1ListArray);
                            chairmanClassListView.setAdapter(chairmanStudentDailyClassListAdapter);
                            chairmanStudentDailyClassListAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(getActivity(), "Error Fetching Response");


                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

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
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
        }
    }

    private void initData()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                chairmanStudentDailyAttendanceArray= null;
            }
            else
            {
                chairmanStudentDailyAttendanceArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanStudentDailyAttendanceArray!= null)
        {
            attendanceListView.setVisibility(View.VISIBLE);
            chairmanAttendanceListListViewAdapter= new ChairmanAttendanceListListViewAdapter(getActivity(),chairmanStudentDailyAttendanceArray);
            attendanceListView.setAdapter(chairmanAttendanceListListViewAdapter);
            chairmanAttendanceListListViewAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentAnnouncementList()
    {


        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);
        final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);

                try
                {
                    responseObject = new JSONObject(response);

                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        Utils.showToast(getActivity(),"No Records Found");
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("Attendance"))
                    {
                        chairmanStudentDailyAttendanceArray= new JSONObject(response).getJSONArray("Attendance");
                        if(null!=chairmanStudentDailyAttendanceArray && chairmanStudentDailyAttendanceArray.length()>=0)
                        {
                            attendanceListView.setVisibility(View.VISIBLE);
                            com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
                            e.data = chairmanStudentDailyAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId,e);
                            value=1;
                            noOfStudents.setText("No. Of Students: "+Preferences.getInstance().totalStudents);
                            chairmanAttendanceListListViewAdapter= new ChairmanAttendanceListListViewAdapter(getActivity(),chairmanStudentDailyAttendanceArray);
                            attendanceListView.setAdapter(chairmanAttendanceListListViewAdapter);
                            chairmanAttendanceListListViewAdapter.notifyDataSetChanged();




                        }
                    }
                    else
                        Utils.showToast(getActivity(), "Error Fetching Response");
                    loading.dismiss();


                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                    loading.dismiss();

                }






            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                loading.dismiss();

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getActivity());
                Map<String,String> params = new HashMap<String, String>();
                //params.put("student_ID",Preferences.getInstance().studentId);
                //params.put("sec_id=",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                //params.put("cls_id",Preferences.getInstance().studentClassId);
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("frm_date",fromDate);
                params.put("to_date",toDate);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("inst_id",Preferences.getInstance().institutionId);
                //params.put("exam_id","1");
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
            loading.dismiss();
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    public PopupWindow SectionDailyListWindow()
    {
        PopupWindow popupWindow = new PopupWindow(getActivity());


        initData1();
        getSectionList();
        popupWindow.setFocusable(true);
        popupWindow.setWidth(width);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


        popupWindow.setContentView(chairmanSectionListView);

        return popupWindow;
    }

    public PopupWindow ClassDailyListWindow()
    {
        PopupWindow popupWindow = new PopupWindow(getActivity());


        initData2();
        getClassList();
        popupWindow.setFocusable(true);
        popupWindow.setWidth(width);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


        popupWindow.setContentView(chairmanClassListView);

        return popupWindow;
    }

    private void initData1()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_SECTION_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&cls_id="+classId+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                chairmanSection1ListArray= null;
            }
            else
            {
                chairmanSection1ListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanSection1ListArray!= null)
        {
            chairmanDailySectionListAdapter= new ChairmanDailySectionListAdapter(getActivity(),chairmanSection1ListArray);
            chairmanSectionListView.setAdapter(chairmanDailySectionListAdapter);
            chairmanDailySectionListAdapter.notifyDataSetChanged();
        }
    }

    protected void getSectionList()
    {

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_SECTION_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        attendanceListView.setVisibility(View.INVISIBLE);
                        Utils.showToast(getActivity(),"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Sections"))
                    {
                        attendanceListView.setVisibility(View.VISIBLE);
                        chairmanSection1ListArray= new JSONObject(response).getJSONArray("Sections");
                        if(null!=chairmanSection1ListArray && chairmanSection1ListArray.length()>=0)
                        {
                            //totalSectionStudents = chairmanSection1ListArray.getJSONObject(index)
                            com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
                            e.data = chairmanSection1ListArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_SECTION_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&cls_id="+classId+"&device_id="+Preferences.getInstance().deviceId,e);
                            chairmanSectionListView.invalidateViews();
                            chairmanDailySectionListAdapter = new ChairmanDailySectionListAdapter(getActivity(), chairmanSection1ListArray);
                            chairmanSectionListView.setAdapter(chairmanDailySectionListAdapter);
                            chairmanDailySectionListAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                        Utils.showToast(getActivity(), "Error Fetching Response");


                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

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
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("cls_id", classId);
                params.put("device_id", Preferences.getInstance().deviceId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
        }
    }


    protected void getStudentAnnouncementList1()
    {


        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);
        final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_CLASS_WISE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);

                try
                {
                    responseObject = new JSONObject(response);

                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        Utils.showToast(getActivity(),"No Records Found");
                        attendanceListView.setVisibility(View.INVISIBLE);

                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                        attendanceListView.setVisibility(View.INVISIBLE);
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("Attendance"))
                    {
                        chairmanStudentDailyAttendanceArray= new JSONObject(response).getJSONArray("Attendance");
                        if(null!=chairmanStudentDailyAttendanceArray && chairmanStudentDailyAttendanceArray.length()>=0)
                        {
                            com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
                            e.data = chairmanStudentDailyAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_CLASS_WISE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId,e);
                            value=2;
                            noOfStudents.setText("No. Of Students: "+totalClassStudents);
                            chairmanAttendanceListListViewAdapter= new ChairmanAttendanceListListViewAdapter(getActivity(),chairmanStudentDailyAttendanceArray);
                            attendanceListView.setAdapter(chairmanAttendanceListListViewAdapter);
                            chairmanAttendanceListListViewAdapter.notifyDataSetChanged();





                        }
                    }
                    else
                        Utils.showToast(getActivity(), "Error Fetching Response");
                    loading.dismiss();


                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                    loading.dismiss();

                }






            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                loading.dismiss();

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getActivity());
                Map<String,String> params = new HashMap<String, String>();
                //params.put("student_ID",Preferences.getInstance().studentId);
                //params.put("sec_id=",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("cls_id",classId);
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("inst_id",Preferences.getInstance().institutionId);
                params.put("frm_date",fromDate);
                params.put("to_date",toDate);
                params.put("device_id",Preferences.getInstance().deviceId);
                //params.put("exam_id","1");
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
        }
    }


    protected void getStudentAnnouncementList2()
    {


        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);
        final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_SECTION_WISE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);

                try
                {
                    responseObject = new JSONObject(response);

                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        Utils.showToast(getActivity(),"No Records Found");
                        attendanceListView.setVisibility(View.INVISIBLE);
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                        attendanceListView.setVisibility(View.INVISIBLE);
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("Attendance"))
                    {
                        chairmanStudentDailyAttendanceArray= new JSONObject(response).getJSONArray("Attendance");
                        if(null!=chairmanStudentDailyAttendanceArray && chairmanStudentDailyAttendanceArray.length()>=0) {
                            com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
                            e.data = chairmanStudentDailyAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_SECTION_WISE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId+"&sec_id="+sectionId, e);
                            value=3;
                            noOfStudents.setText("No. Of Students: "+totalSectionStudents);
                            chairmanAttendanceListListViewAdapter= new ChairmanAttendanceListListViewAdapter(getActivity(),chairmanStudentDailyAttendanceArray);
                            attendanceListView.setAdapter(chairmanAttendanceListListViewAdapter);
                            chairmanAttendanceListListViewAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                        Utils.showToast(getActivity(), "Error Fetching Response");
                    loading.dismiss();


                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(getActivity(), "");
                    loading.dismiss();

                }






            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                loading.dismiss();

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getActivity());
                Map<String,String> params = new HashMap<String, String>();
                //params.put("student_ID",Preferences.getInstance().studentId);
                params.put("sec_id",sectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("cls_id",classId);
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("frm_date",fromDate);
                params.put("to_date",toDate);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("inst_id",Preferences.getInstance().institutionId);
                //params.put("exam_id","1");
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
            loading.dismiss();
        }
    }
}
