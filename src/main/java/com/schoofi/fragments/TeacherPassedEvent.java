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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.StudentEventListDetails;
import com.schoofi.activitiess.TeacherEventList;
import com.schoofi.activitiess.TeacherEventListDetails;
import com.schoofi.adapters.StudentEventListAdapter;
import com.schoofi.adapters.TeacherEventListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schoofi on 26-12-2016.
 */

public class TeacherPassedEvent extends Fragment {

    private ListView studentEventListListView;
    private SwipyRefreshLayout swipyRefreshLayout;
    TeacherEventListAdapter teacherEventListAdapter;
    private JSONArray teacherEventListArray;

    private int mPage;
    public static final String ARG_PAGE = "ARG_PAGE";

    public static TeacherPassedEvent newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TeacherPassedEvent teacherPassedEvent = new TeacherPassedEvent();
        teacherPassedEvent.setArguments(args);
        return teacherPassedEvent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_event_list, container, false);
        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        studentEventListListView = (ListView) view.findViewById(R.id.listview_eventList);


        initData();
        getChairmanStudentLeaveList();


        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                swipyRefreshLayout.setRefreshing(true);
                initData();
                getChairmanStudentLeaveList();
            }
        });


        studentEventListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {Intent intent = new Intent(getActivity(),TeacherEventListDetails.class);
                    intent.putExtra("position",position);
                    intent.putExtra("value","2");
                    intent.putExtra("value4","2");
                    intent.putExtra("parent_event",teacherEventListArray.getJSONObject(position).getString("event_id"));
                    intent.putExtra("program_name",teacherEventListArray.getJSONObject(position).getString("program_name"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_EVENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&value="+"2");
            if(e == null)
            {
                teacherEventListArray= null;
            }
            else
            {
                teacherEventListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherEventListArray!= null)
        {
            teacherEventListAdapter= new TeacherEventListAdapter(getActivity(),teacherEventListArray,"2");
            studentEventListListView.setAdapter(teacherEventListAdapter);
            teacherEventListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_EVENT_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        Utils.showToast(getActivity(),"No Events Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("events"))
                    {
                        teacherEventListArray= new JSONObject(response).getJSONArray("events");
                        if(null!=teacherEventListArray && teacherEventListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherEventListArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_EVENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&value="+"2",e);
                            studentEventListListView.invalidateViews();
                            teacherEventListAdapter= new TeacherEventListAdapter(getActivity(),teacherEventListArray,"2");
                            studentEventListListView.setAdapter(teacherEventListAdapter);
                            teacherEventListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else
                        Utils.showToast(getActivity(), "Error Fetching Response");
                    //setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                    //setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
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
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                // params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("value","2");
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
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


    private void toa()
    {
        System.out.println("aaa");
    }
}
