package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.AdminVisitorDetailsActivity;
import com.schoofi.activitiess.ChairmanAssignmentAnalysisDetailScreen1;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.AdminVisitorLogPrimaryDetailsAdapter;
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
 * Created by Schoofi on 09-03-2018.
 */

public class AdminVisitorLogsWeeklyDailyPrimaryDetails extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private ListView chairmanAssignmentClassSectionWiseAnalysis;
    private JSONArray chairmanAssigmentClassSectionWiseArray;
    private AdminVisitorLogPrimaryDetailsAdapter adminVisitorLogsPrimaryDetailsAdapter;
    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String fromDate,toDate;

    private int mPage;

    public static AdminVisitorLogsWeeklyDailyPrimaryDetails newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AdminVisitorLogsWeeklyDailyPrimaryDetails adminVisitorLogsWeeklyDailyPrimaryDetails = new AdminVisitorLogsWeeklyDailyPrimaryDetails();
        adminVisitorLogsWeeklyDailyPrimaryDetails.setArguments(args);
        return adminVisitorLogsWeeklyDailyPrimaryDetails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chairman_assignment_analysis_listview, container, false);
        chairmanAssignmentClassSectionWiseAnalysis = (ListView) view.findViewById(R.id.listview_chairman_analysis);
        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();
        cal1.add(Calendar.DATE, -1);
        Date toDate2 = cal1.getTime();
        toDate = new SimpleDateFormat("yyyy-MM-dd").format(toDate2);
        fromDate = new SimpleDateFormat("yyyy-MM-dd").format(todate1);

        initData();
        getChairmanStudentLeaveList();

        chairmanAssignmentClassSectionWiseAnalysis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Preferences.getInstance().loadPreference(getActivity().getApplicationContext());

                    Intent intent = new Intent(getActivity(), AdminVisitorDetailsActivity.class);
                    intent.putExtra("value", "2");
                    intent.putExtra("position",position);

                    startActivity(intent);



            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        getChairmanStudentLeaveList();
    }

    private void initData() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&visitor_type="+Preferences.getInstance().visitorType+ "&value=" + "2");
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
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&visitor_type="+Preferences.getInstance().visitorType+ "&value=" + "2", e);
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
                params.put("value", "2");

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
}
