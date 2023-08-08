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
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.schoofi.adapters.ChairmanCustomSectionListAdapter;
import com.schoofi.adapters.ChairmanDailySectionListAdapter;
import com.schoofi.adapters.ChairmanStudentCustomClassListAdapter;
import com.schoofi.adapters.ChairmanStudentDailyClassListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;

/**
 * Created by Schoofi on 20-07-2016.
 */
public class ChairmanStudentCustomListAttendance extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    private JSONArray chairmanClass1ListArray;
    float mult = 100;
    String classId;
    private TextView selectClass,selectSection,schoolWise,noOfStudents;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    PopupWindow popupWindow,popupWindow1;
    private PopupWindow calendarPopup,calendarPopup1;
    private JSONArray chairmanSection1ListArray;
    ChairmanStudentDailyClassListAdapter chairmanStudentDailyClassListAdapter;
    ChairmanDailySectionListAdapter chairmanDailySectionListAdapter;
    private JSONArray chairmanStudentDailyAttendanceArray;
    ChairmanAttendanceListListViewAdapter chairmanAttendanceListListViewAdapter;
    int value=0;

    private ListView chairmanSectionListView,chairmanClassListView;
    String sectionId;
    int year_x,month_x,day_x;
    static final int dialog_id=1;
    String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

    public Button from,to;
    String from1,to1,totalClassStudents,totalSectionStudents;
    int width;
    int count1 = 0;
    int count = 0;
    ListView attendanceListView;


    public static ChairmanStudentCustomListAttendance newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ChairmanStudentCustomListAttendance chairmanStudentCustomListAttendance = new ChairmanStudentCustomListAttendance();
        chairmanStudentCustomListAttendance.setArguments(args);
        return chairmanStudentCustomListAttendance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chairman_custom_fragment_list_layout, container, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((ChairmanSchoolAttendance) getContext()).getWindowManager()
                .getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        selectClass = (TextView) view.findViewById(R.id.text_ch_custom_class_wise);
        selectSection = (TextView) view.findViewById(R.id.text_ch_custom_section_wise);
        schoolWise = (TextView) view.findViewById(R.id.text_ch_custom_school_wise);
        noOfStudents = (TextView) view.findViewById(R.id.text_no_of_students);
        from = (Button) view.findViewById(R.id.btn_from);
        to = (Button) view.findViewById(R.id.btn_to);
        attendanceListView = (ListView) view.findViewById(R.id.custom_attendance_listview);

        attendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {



                Intent intent = new Intent(getActivity(), ChairmanAttendenceDetails.class);
                intent.putExtra("position",position);
                intent.putExtra("value",value);
                intent.putExtra("from_date",from1);
                intent.putExtra("to_date",to1);
                intent.putExtra("temp","3");
                intent.putExtra("cls_id",classId);
                intent.putExtra("sec_id",sectionId);
                startActivity(intent);

            }
        });

        from.setOnClickListener(onEditTextClickListener);
        to.setOnClickListener(onEditTextClickListener1);


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

        /*selectClass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                popupWindow1.showAsDropDown(v, -5, 0);

            }
        });*/

        selectClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow1.showAsDropDown(view, -5, 0);
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



    private View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup == null) {
                calendarPopup = new PopupWindow(getActivity());
                CalendarPickerView calendarView = new CalendarPickerView(getActivity());
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
                calendarPopup1 = new PopupWindow(getActivity());
                CalendarPickerView calendarView = new CalendarPickerView(getActivity());
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
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            from.setText(formatter.format(selectedDate.getTime()));
            from1= formatter1.format(selectedDate.getTime());
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            try {
                Date date1 = formatter.parse(date);
                String fromEditTextDate1 = from.getText().toString();
                Date date2 = formatter.parse(fromEditTextDate1);

                if(date1.compareTo(date2)<0)
                {
                    Toast.makeText(getActivity(),"Not Valid",Toast.LENGTH_SHORT).show();
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
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            to.setText(formatter.format(selectedDate.getTime()));
            try {
                Date date1 = formatter.parse(date);

                String toEditTextDate1 = to.getText().toString();

                Date date2 = formatter.parse(toEditTextDate1);
                String fromEditTextDate1 = from.getText().toString();
                to1= formatter1.format(selectedDate.getTime());
                Date date3 = formatter.parse(fromEditTextDate1);
                if(date1.compareTo(date2)<0)
                {
                    Toast.makeText(getActivity(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1 =0;
                }
                else
                if(date2.compareTo(date3)<0)
                {
                    Toast.makeText(getActivity(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1=0;
                }

                else
                if(date2.compareTo(date3)==0)
                {
                    Toast.makeText(getActivity(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1=0;
                }
                else
                {
                    count1 =1;
                    initData();
                    getStudentAnnouncementList();
                }


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private void initData2()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_CLASS_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId);
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
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+from1+"&to_date="+to1+"&device_id="+Preferences.getInstance().deviceId);
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
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+from1+"&to_date="+to1+"&device_id="+Preferences.getInstance().deviceId,e);
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
                params.put("frm_date",from1);
                params.put("to_date",to1);
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
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_CLASS_WISE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+from1+"&to_date="+to1+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId,e);
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
                params.put("frm_date",from1);
                params.put("to_date",to1);
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
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_SECTION_LIST+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+from1+"&to_date="+to1+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId+"&sec_id="+sectionId, e);
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
                params.put("frm_date",from1);
                params.put("to_date", to1);
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
