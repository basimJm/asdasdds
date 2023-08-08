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
import com.schoofi.activitiess.TeacherAssignmentHomeWorkDetails;
import com.schoofi.activitiess.TeacherStudentViewAssignment;
import com.schoofi.adapters.AssignmentAdapter;
import com.schoofi.adapters.TeacherCircularViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Schoofi on 09-08-2016.
 */
public class TeacherCircularView extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    ListView circularListView;
    TeacherCircularViewAdapter teacherCircularViewAdapter;
    private JSONArray circularArray;
    String assignId,description;
    ArrayList aList= new ArrayList();

    public String file,title;
    SwipyRefreshLayout swipyRefreshLayout;

    public static TeacherCircularView newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TeacherCircularView teacherCircularView = new TeacherCircularView();
        teacherCircularView.setArguments(args);
        return teacherCircularView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assignment_layout, container, false);
        circularListView = (ListView) view.findViewById(R.id.listViewInnerAllAssignment);
        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getStudentFeedList();
            }
        });

        initData();
        getStudentFeedList();

        circularListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                try {
                    file	 = circularArray.getJSONObject(position).getString("assign_path");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    assignId = circularArray.getJSONObject(position).getString("id");
                    description = circularArray.getJSONObject(position).getString("description");
                    title = circularArray.getJSONObject(position).getString("title");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if(file.matches(""))
                {
                    Intent intent = new Intent(getActivity(),TeacherStudentViewAssignment.class);
                    intent.putExtra("array", "null");
                    intent.putExtra("array2","1");
                    intent.putExtra("array1", "n");
                    intent.putExtra("asn_id", assignId);
                    intent.putExtra("title",title);
                    intent.putExtra("subject","null");
                    intent.putExtra("last_date","null");
                    intent.putExtra("desc",description);
                    startActivity(intent);
                }

                else
                {
                    aList = new ArrayList<String>(Arrays.asList(file.split(",")));
                    for(int i=0;i<aList.size();i++)
                    {
                        System.out.println(""+aList.get(i));
                    }

                    Intent intent = new Intent(getActivity(),TeacherStudentViewAssignment.class);
                    intent.putExtra("array", aList);
                    intent.putExtra("array2","2");
                    intent.putExtra("array1", "n");
                    intent.putExtra("asn_id", assignId);
                    intent.putExtra("desc",description);
                    intent.putExtra("title",title);
                    intent.putExtra("subject","null");
                    intent.putExtra("last_date","null");
                    startActivity(intent);
                }





            }
        });


        return view;

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initData();
        getStudentFeedList();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ASSIGNMENT_URL+"?u_email_id="+ Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&temp="+"1"+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                circularArray= null;
            }
            else
            {
                circularArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(circularArray!= null)
        {
            teacherCircularViewAdapter= new TeacherCircularViewAdapter(getActivity(),circularArray);
            circularListView.setAdapter(teacherCircularViewAdapter);
            teacherCircularViewAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ASSIGNMENT_URL+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&temp="+"1"+"&ins_id="+Preferences.getInstance().institutionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getActivity(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Tech_asn"))
                    {
                        circularArray= new JSONObject(response).getJSONArray("Tech_asn");
                        if(null!=circularArray && circularArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = circularArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ASSIGNMENT_URL+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&temp="+"1"+"&ins_id="+Preferences.getInstance().institutionId,e);
                            circularListView.invalidateViews();
                            teacherCircularViewAdapter= new TeacherCircularViewAdapter(getActivity(),circularArray);
                            circularListView.setAdapter(teacherCircularViewAdapter);
                            teacherCircularViewAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                            //assignId = AssignmentArray.getJSONObject(index)

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
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

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
