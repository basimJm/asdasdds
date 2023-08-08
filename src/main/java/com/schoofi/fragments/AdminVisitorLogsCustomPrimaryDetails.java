package com.schoofi.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.AdminVisitorDetailsActivity;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.AdminVisitorLogPrimaryDetailsAdapter;
import com.schoofi.adapters.AdminVisitorLogsTypeWiseAdapter;
import com.schoofi.adapters.ChairmanAssignmentAnalysisAdapter;
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
 * Created by Schoofi on 09-03-2018.
 */

public class AdminVisitorLogsCustomPrimaryDetails extends Fragment {

    private EditText fromEditTextDate,toEditTextDate;
    private Button button_student_custom_date_picker;
    private PopupWindow calendarPopup,calendarPopup1;
    int count1 = 0;
    int count = 0;
    int year_x,month_x,day_x;
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    static final int dialog_id=1;
    String from1,to1;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    private TextView newView;
    //public static final String ARG_PAGE = "ARG_PAGE";
    public static final String VALUE = "VALUE";
   // private ListView chairmanAssignmentClassSectionWiseAnalysis;
    //private JSONArray chairmanAssigmentClassSectionWiseArray;
    private ChairmanAssignmentAnalysisAdapter chairmanAssignmentAnalysisAdapter;
    private AdminVisitorLogPrimaryDetailsAdapter adminVisitorLogsPrimaryDetailsAdapter;

    private String value;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    // Date date = new Date();
    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String fromDate,toDate;
    String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());

    public static final String ARG_PAGE = "ARG_PAGE";
    private ListView chairmanAssignmentClassSectionWiseAnalysis;
    private JSONArray chairmanAssigmentClassSectionWiseArray;
    private AdminVisitorLogsTypeWiseAdapter adminVisitorLogsTypeWiseAdapter;
    private Context context;

    private int mPage;

    public static AdminVisitorFirstScreenCustom newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AdminVisitorFirstScreenCustom adminVisitorFirstScreenCustom = new AdminVisitorFirstScreenCustom();
        adminVisitorFirstScreenCustom.setArguments(args);
        return adminVisitorFirstScreenCustom;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        value = getArguments().getString(VALUE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chairman_assignment_custom_analysis, container, false);
        chairmanAssignmentClassSectionWiseAnalysis = (ListView) view.findViewById(R.id.listview_chairman_analysis);
        fromEditTextDate = (EditText) view.findViewById(R.id.edit_fromDatePicker);
        toEditTextDate = (EditText) view.findViewById(R.id.edit_toDatePicker);
        context = getActivity();
        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();
        fromDate = new SimpleDateFormat("yyyy-MM-dd").format(todate1);
        cal1.add(Calendar.DATE, -1);
        Date todate2 = cal1.getTime();
        toDate = new SimpleDateFormat("yyyy-MM-dd").format(todate2);

        fromEditTextDate.setOnClickListener(onEditTextClickListener);
        toEditTextDate.setOnClickListener(onEditTextClickListener1);
        //}

        //fromEditTextDate.setOnTouchListener(otl);
        fromEditTextDate.setInputType(0);
        toEditTextDate.setInputType(0);
        button_student_custom_date_picker = (Button) view.findViewById(R.id.btn_student_custom_date_picker);
        button_student_custom_date_picker.setText("Done");
        button_student_custom_date_picker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(count==0 && count1==0)
                    Toast.makeText(getActivity(), "not valid date", Toast.LENGTH_SHORT).show();
                else
                if(count==0 && count1==1)
                {
                    Toast.makeText(getActivity(), "not valid date", Toast.LENGTH_SHORT).show();
                }
                else
                if(count==1 && count1==0)
                {
                    Toast.makeText(getActivity(), "not valid date", Toast.LENGTH_SHORT).show();
                }
                else
                //view = inflater.inflate(R.layout.student_daily_attendance, container, false);
                {


                        initData();
                        getChairmanStudentLeaveList();

                }
            }
        });



        //setDateTimeField();
        System.out.println(date);




        chairmanAssignmentClassSectionWiseAnalysis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Preferences.getInstance().loadPreference(getActivity().getApplicationContext());

                Intent intent = new Intent(getActivity(), AdminVisitorDetailsActivity.class);
                intent.putExtra("value", "3");
                intent.putExtra("position",position);

                startActivity(intent);



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
                }


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };






    private void initData() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&visitor_type="+Preferences.getInstance().visitorType+ "&value=" + "3"+"&from_date="+from1+"&to_date="+to1);
            if (e == null) {
                chairmanAssigmentClassSectionWiseArray = null;
            } else {
                chairmanAssigmentClassSectionWiseArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanAssigmentClassSectionWiseArray != null) {
            adminVisitorLogsPrimaryDetailsAdapter = new AdminVisitorLogPrimaryDetailsAdapter(getActivity(), chairmanAssigmentClassSectionWiseArray);
            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(adminVisitorLogsPrimaryDetailsAdapter);
            adminVisitorLogsPrimaryDetailsAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList() {
        //setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getActivity(), "No Analysis Found");
                        chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {
                        chairmanAssigmentClassSectionWiseArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != chairmanAssigmentClassSectionWiseArray && chairmanAssigmentClassSectionWiseArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanAssigmentClassSectionWiseArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&visitor_type="+Preferences.getInstance().visitorType+ "&value=" + "3"+"&from_date="+from1+"&to_date="+to1, e);
                            chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.VISIBLE);
                            chairmanAssignmentClassSectionWiseAnalysis.invalidateViews();
                            adminVisitorLogsPrimaryDetailsAdapter = new AdminVisitorLogPrimaryDetailsAdapter(getActivity(), chairmanAssigmentClassSectionWiseArray);
                            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(adminVisitorLogsPrimaryDetailsAdapter);
                            adminVisitorLogsPrimaryDetailsAdapter.notifyDataSetChanged();


                        }
                    } else
                        Utils.showToast(getActivity().getApplicationContext(), "Error Fetching Response");
                    // setSupportProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getActivity().getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    //setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getActivity().getApplicationContext(), "Error fetching modules! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(getActivity());
                Map<String, String> params = new HashMap<String, String>();
                params.put("sch_id", Preferences.getInstance().schoolId);

                params.put("token", Preferences.getInstance().token);
                params.put("visitor_type",Preferences.getInstance().visitorType);

                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("session", Preferences.getInstance().session1);
                params.put("from_date",from1);
                params.put("to_date",to1);
                params.put("value", "3");

                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else {
            Utils.showToast(getActivity().getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa() {
        System.out.println("aaa");
    }


    


    private void initData1() {

        Preferences.getInstance().loadPreference(getActivity().getApplicationContext());


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&from_date="+from1+"&to_date="+to1+"&teac_id="+Preferences.getInstance().chairmanAssignmentTeacherId+"&teac_name="+Preferences.getInstance().chairmanAssignmentTeacherName);
            if (e == null) {
                chairmanAssigmentClassSectionWiseArray = null;
            } else {
                chairmanAssigmentClassSectionWiseArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanAssigmentClassSectionWiseArray != null) {
            chairmanAssignmentAnalysisAdapter = new ChairmanAssignmentAnalysisAdapter(getActivity(), chairmanAssigmentClassSectionWiseArray,value);
            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(chairmanAssignmentAnalysisAdapter);
            chairmanAssignmentAnalysisAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList1() {
        //setSupportProgressBarIndeterminateVisibility(true);
        Preferences.getInstance().loadPreference(getActivity().getApplicationContext());
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getActivity(), "No Analysis Found");
                        chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {
                        chairmanAssigmentClassSectionWiseArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != chairmanAssigmentClassSectionWiseArray && chairmanAssigmentClassSectionWiseArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanAssigmentClassSectionWiseArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&from_date="+from1+"&to_date="+to1+"&teac_id="+Preferences.getInstance().chairmanAssignmentTeacherId+"&teac_name="+Preferences.getInstance().chairmanAssignmentTeacherName, e);
                            chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.VISIBLE);
                            chairmanAssignmentClassSectionWiseAnalysis.invalidateViews();
                            chairmanAssignmentAnalysisAdapter = new ChairmanAssignmentAnalysisAdapter(getActivity(), chairmanAssigmentClassSectionWiseArray,value);
                            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(chairmanAssignmentAnalysisAdapter);
                            chairmanAssignmentAnalysisAdapter.notifyDataSetChanged();


                        }
                    } else
                        Utils.showToast(getActivity().getApplicationContext(), "Error Fetching Response");
                    // setSupportProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getActivity().getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    //setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getActivity().getApplicationContext(), "Error fetching modules! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(getActivity());
                Map<String, String> params = new HashMap<String, String>();
                params.put("school_id", Preferences.getInstance().schoolId);

                params.put("token", Preferences.getInstance().token);

                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("session", Preferences.getInstance().session1);
                params.put("value", value);
                params.put("from_date",from1);
                params.put("to_date",to1);

                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("teac_id",Preferences.getInstance().chairmanAssignmentTeacherId);
                params.put("teac_name",Preferences.getInstance().chairmanAssignmentTeacherName);

                //Log.d("jj",value+Preferences.getInstance().chairmanAssignmentClassId+Preferences.getInstance().chairmanAssignmentClassName+Preferences.getInstance().chairmanAssignmentSectionName+Preferences.getInstance().chairmanAssignmentSectionId);
                return params;
            }
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else {
            Utils.showToast(getActivity().getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void initData2() {

        Preferences.getInstance().loadPreference(getActivity().getApplicationContext());


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&from_date="+from1+"&to_date="+to1+"&subject_id="+Preferences.getInstance().chairmanAssgnmentSubjectId+"&subject_name="+Preferences.getInstance().chairmanAssignmentSubjectName);
            if (e == null) {
                chairmanAssigmentClassSectionWiseArray = null;
            } else {
                chairmanAssigmentClassSectionWiseArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanAssigmentClassSectionWiseArray != null) {
            chairmanAssignmentAnalysisAdapter = new ChairmanAssignmentAnalysisAdapter(getActivity(), chairmanAssigmentClassSectionWiseArray,value);
            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(chairmanAssignmentAnalysisAdapter);
            chairmanAssignmentAnalysisAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList2() {
        //setSupportProgressBarIndeterminateVisibility(true);
        Preferences.getInstance().loadPreference(getActivity().getApplicationContext());
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getActivity(), "No Analysis Found");
                        chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {
                        chairmanAssigmentClassSectionWiseArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != chairmanAssigmentClassSectionWiseArray && chairmanAssigmentClassSectionWiseArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanAssigmentClassSectionWiseArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&from_date="+from1+"&to_date="+to1+"&subject_id="+Preferences.getInstance().chairmanAssgnmentSubjectId+"&subject_name="+Preferences.getInstance().chairmanAssignmentSubjectName, e);
                            chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.VISIBLE);
                            chairmanAssignmentClassSectionWiseAnalysis.invalidateViews();
                            chairmanAssignmentAnalysisAdapter = new ChairmanAssignmentAnalysisAdapter(getActivity(), chairmanAssigmentClassSectionWiseArray,value);
                            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(chairmanAssignmentAnalysisAdapter);
                            chairmanAssignmentAnalysisAdapter.notifyDataSetChanged();


                        }
                    } else
                        Utils.showToast(getActivity().getApplicationContext(), "Error Fetching Response");
                    // setSupportProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getActivity().getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    //setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getActivity().getApplicationContext(), "Error fetching modules! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(getActivity());
                Map<String, String> params = new HashMap<String, String>();
                params.put("school_id", Preferences.getInstance().schoolId);

                params.put("token", Preferences.getInstance().token);

                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("session", Preferences.getInstance().session1);
                params.put("value", value);
                params.put("from_date",from1);
                params.put("to_date",to1);

                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("subject_id",Preferences.getInstance().chairmanAssgnmentSubjectId);
                params.put("subject_name",Preferences.getInstance().chairmanAssignmentSubjectName);

                //Log.d("jj",value+Preferences.getInstance().chairmanAssignmentClassId+Preferences.getInstance().chairmanAssignmentClassName+Preferences.getInstance().chairmanAssignmentSectionName+Preferences.getInstance().chairmanAssignmentSectionId);
                return params;
            }
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else {
            Utils.showToast(getActivity().getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }
}
