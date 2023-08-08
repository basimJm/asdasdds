package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import android.util.Log;
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
import com.schoofi.activitiess.ChairmanAssignmentAnalysisDetailScreen1;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.ChairmanAssignmentAnalysisAdapter;
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
 * Created by Schoofi on 11-08-2017.
 */

public class DailyChairmanAssignmentAttendanceAnalysis extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String VALUE = "VALUE";
    private ListView chairmanAssignmentClassSectionWiseAnalysis;
    private JSONArray chairmanAssigmentClassSectionWiseArray;
    private ChairmanAssignmentAnalysisAdapter chairmanAssignmentAnalysisAdapter;

    private String value;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    private int mPage;

    public static DailyChairmanAssignmentAttendanceAnalysis newInstance(int page,String value1) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(VALUE,value1);
        DailyChairmanAssignmentAttendanceAnalysis dailyChairmanAssignmentAttendanceAnalysis = new DailyChairmanAssignmentAttendanceAnalysis();
        dailyChairmanAssignmentAttendanceAnalysis.setArguments(args);
        return dailyChairmanAssignmentAttendanceAnalysis;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        value = getArguments().getString(VALUE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chairman_assignment_analysis_listview, container, false);
        chairmanAssignmentClassSectionWiseAnalysis = (ListView) view.findViewById(R.id.listview_chairman_analysis);


        if(value.matches("1")) {

            initData();
            getChairmanStudentLeaveList();
        }

        else
            if(value.matches("2"))
            {
                initData1();
                getChairmanStudentLeaveList1();
            }

            else
            if(value.matches("3"))
            {
                initData2();
                getChairmanStudentLeaveList2();
            }

       /* chairmanAssignmentClassSectionWiseAnalysis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Preferences.getInstance().loadPreference(getActivity().getApplicationContext());
                try {
                    Intent intent = new Intent(getActivity(), ChairmanAssignmentAnalysisDetailScreen1.class);
                    intent.putExtra("value","1");
                    Preferences.getInstance().chairmanAssignmentClassId = chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("class_id");
                    Preferences.getInstance().chairmanAssignmentClassId = chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("class_name");
                    Preferences.getInstance().savePreference(getActivity().getApplicationContext());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(value.matches("1")) {

            initData();
            getChairmanStudentLeaveList();
        }

        else
        if(value.matches("2"))
        {
            initData1();
            getChairmanStudentLeaveList1();
        }

        else
        if(value.matches("3"))
        {
            initData2();
            getChairmanStudentLeaveList2();
        }


    }

    private void initData() {

        Preferences.getInstance().loadPreference(getActivity().getApplicationContext());


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&crr_date="+currentDate+"&class_id="+Preferences.getInstance().chairmanAssignmentClassId+"&class_name="+Preferences.getInstance().chairmanAssignmentClassName+"&section_id="+Preferences.getInstance().chairmanAssignmentSectionId+"&section_name="+Preferences.getInstance().chairmanAssignmentSectionName);
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

    protected void getChairmanStudentLeaveList() {
        //setSupportProgressBarIndeterminateVisibility(true);
        Preferences.getInstance().loadPreference(getActivity().getApplicationContext());
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
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
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&crr_date="+currentDate+"&class_id="+Preferences.getInstance().chairmanAssignmentClassId+"&class_name="+Preferences.getInstance().chairmanAssignmentClassName+"&section_id="+Preferences.getInstance().chairmanAssignmentSectionId+"&section_name="+Preferences.getInstance().chairmanAssignmentSectionName, e);
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

                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("class_id",Preferences.getInstance().chairmanAssignmentClassId);
                params.put("crr_date",currentDate);
                params.put("class_name",Preferences.getInstance().chairmanAssignmentClassName);
                params.put("section_name",Preferences.getInstance().chairmanAssignmentSectionName);
                params.put("section_id",Preferences.getInstance().chairmanAssignmentSectionId);

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


    private void toa() {
        System.out.println("aaa");
    }


    private void initData1() {

        Preferences.getInstance().loadPreference(getActivity().getApplicationContext());


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&crr_date="+currentDate+"&teac_id="+Preferences.getInstance().chairmanAssignmentTeacherId+"&teac_name="+Preferences.getInstance().chairmanAssignmentTeacherName);
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
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&crr_date="+currentDate+"&teac_id="+Preferences.getInstance().chairmanAssignmentTeacherId+"&teac_name="+Preferences.getInstance().chairmanAssignmentTeacherName, e);
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
                params.put("crr_date",currentDate);

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
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&crr_date="+currentDate+"&subject_id="+Preferences.getInstance().chairmanAssgnmentSubjectId+"&subject_name="+Preferences.getInstance().chairmanAssignmentSubjectName);
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
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS + "?school_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&session="+Preferences.getInstance().session1+ "&value=" + value+"&crr_date="+currentDate+"&subject_id="+Preferences.getInstance().chairmanAssgnmentSubjectId+"&subject_name="+Preferences.getInstance().chairmanAssignmentSubjectName, e);
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
                params.put("crr_date",currentDate);

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
