package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.ChairmanAttendanceDetailsListViewAdapter;
import com.schoofi.adapters.TeacherStudentBusAttendanceBusListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;

public class ChairmanFeesAnalysis extends AppCompatActivity {

    private ImageView back;

    private TextView fromDate,toDate,cashCollection1,swipeCollection1,ddCollection1,chequeCollection1,ccCollection1,onLineCollection1,neftCollection1,cashCollection,swipeCollection,ddCollection,chequeCollection,ccCollection,onLineCollection,totalCollection,totalCollection1,neftCollection;
    private Button done;
    private PopupWindow calendarPopup,calendarPopup1;
    String from1,to1;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    int c=0;
    String value;
    private JSONArray chairmanFeesAnalysisArray;
    String fromDate1,fromDate2,toDate1,toDate2;
    Date fromDate3,toDate3;
    String Rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Fees Analysis");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_chairman_fees_analysis);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        done = (Button) findViewById(R.id.btn_done);

        value = getIntent().getStringExtra("value");

        fromDate = (TextView) findViewById(R.id.text_from_date);
        toDate = (TextView) findViewById(R.id.text_to_date);

        cashCollection = (TextView) findViewById(R.id.text_fee_type11);
        ddCollection = (TextView) findViewById(R.id.text_fee_type12);
        chequeCollection = (TextView) findViewById(R.id.text_fee_type13);
        swipeCollection = (TextView) findViewById(R.id.text_fee_type14);
        ccCollection = (TextView) findViewById(R.id.text_fee_type15);
        neftCollection = (TextView) findViewById(R.id.text_fee_type18);
        onLineCollection = (TextView) findViewById(R.id.text_fee_type16);

        cashCollection1 = (TextView) findViewById(R.id.text_fee_type1);
        ddCollection1 = (TextView) findViewById(R.id.text_fee_type2);
        chequeCollection1 = (TextView) findViewById(R.id.text_fee_type3);
        swipeCollection1 = (TextView) findViewById(R.id.text_fee_type4);
        ccCollection1 = (TextView) findViewById(R.id.text_fee_type5);
        onLineCollection1 = (TextView) findViewById(R.id.text_fee_type6);
        neftCollection1 = (TextView) findViewById(R.id.text_fee_type8);
        totalCollection = (TextView) findViewById(R.id.text_fee_type17);
        totalCollection1 = (TextView) findViewById(R.id.text_fee_type7);

        Rs = getApplicationContext().getString(R.string.Rs);

        cashCollection1.setText("Cash");
        ddCollection1.setText("Demand Draft");
        chequeCollection1.setText("Cheque");
        swipeCollection1.setText("Swipe Machine");
        ccCollection1.setText("Credit/Debit Card");
        neftCollection1.setText("NEFT/Online");
        onLineCollection1.setText("Mobile");
        totalCollection1.setText("Total Collection");

        if(value.matches("2"))
        {
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date= dateFormat.format(yesterday());
            fromDate.setText(date);
        }

        else {


            fromDate.setText(date);
        }

        fromDate.setOnClickListener(onEditTextClickListener);
        toDate.setOnClickListener(onEditTextClickListener1);



         from1 = date;

        initData();
        getChairmanStudentLeaveList();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(c==1)
                {

                    initData();
                    getChairmanStudentLeaveList();
                }

                else
                    if(c==2)
                    {
                        initData1();
                        getChairmanStudentLeaveList1();
                    }

            }
        });



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

            fromDate.setText(formatter.format(selectedDate.getTime()));
            c=1;




        }
    };


    private View.OnClickListener onEditTextClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup1 == null) {
                calendarPopup1 = new PopupWindow(getApplicationContext());
                CalendarPickerView calendarView1 = new CalendarPickerView(getApplicationContext());
                calendarView1.setListener(dateSelectionListener1);
                calendarPopup1.setContentView(calendarView1);
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
            to1 = formatter1.format(selectedDate.getTime());

            toDate.setText(formatter.format(selectedDate.getTime()));
            c=2;
        }

    } ;


    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FEES_ANALYSIS+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&from_date="+from1+"&to_date="+"");
            if(e == null)
            {
                chairmanFeesAnalysisArray= null;
            }
            else
            {
                chairmanFeesAnalysisArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanFeesAnalysisArray!= null)
        {
            try {
                cashCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_cash"));
                ddCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_dd"));
                chequeCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_Cheque"));
                swipeCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_sm"));
                ccCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_cc"));
                onLineCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_online"));
                totalCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("total_collection"));
                neftCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_neft"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(ChairmanFeesAnalysis.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FEES_ANALYSIS;//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        chairmanFeesAnalysisArray= new JSONObject(response).getJSONArray("responseObject");


                        if(null!=chairmanFeesAnalysisArray && chairmanFeesAnalysisArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanFeesAnalysisArray.toString().getBytes();
                            VolleySingleton.getInstance(ChairmanFeesAnalysis.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FEES_ANALYSIS+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&from_date="+from1+"&to_date="+"",e);
                            cashCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_cash"));
                            ddCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_dd"));
                            chequeCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_Cheque"));
                            swipeCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_sm"));
                            ccCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_cc"));
                            onLineCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_online"));
                            totalCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("total_collection"));
                            neftCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_neft"));

                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("from_date",from1);
                params.put("to_date","");
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

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


    private void initData1()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FEES_ANALYSIS+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&from_date="+from1+"&to_date="+to1);
            if(e == null)
            {
                chairmanFeesAnalysisArray= null;
            }
            else
            {
                chairmanFeesAnalysisArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanFeesAnalysisArray!= null)
        {
            try {
                cashCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_cash"));
                ddCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_dd"));
                chequeCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_Cheque"));
                swipeCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_sm"));
                ccCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_cc"));
                onLineCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_online"));
                totalCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("total_collection"));
                neftCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_neft"));
        } catch (JSONException e) {
        e.printStackTrace();
    }


        }
    }

    protected void getChairmanStudentLeaveList1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(ChairmanFeesAnalysis.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FEES_ANALYSIS;//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        chairmanFeesAnalysisArray= new JSONObject(response).getJSONArray("responseObject");


                        if(null!=chairmanFeesAnalysisArray && chairmanFeesAnalysisArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanFeesAnalysisArray.toString().getBytes();
                            VolleySingleton.getInstance(ChairmanFeesAnalysis.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FEES_ANALYSIS+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&from_date="+from1+"&to_date="+to1,e);
                            cashCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_cash"));
                            ddCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_dd"));
                            chequeCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_Cheque"));
                            swipeCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_sm"));
                            ccCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_cc"));
                            totalCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("total_collection"));
                            onLineCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_online"));
                            neftCollection.setText(Rs+chairmanFeesAnalysisArray.getJSONObject(0).getString("collection_neft")
                            );
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("from_date",from1);
                params.put("to_date",to1);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

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

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

}
