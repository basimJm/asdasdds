package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.schoofi.activitiess.ParentNewFeesHistoryDetails;
import com.schoofi.activitiess.ParentStudentFeesHistoryDetails;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by harsh malhotra on 4/11/2016.
 */
public class ParentStudentFeesHistory extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";


    private int mPage;
    ListView parentStudentFeesHistory;
    JSONArray parentStudentFeesHistoryArray;
    com.schoofi.adapters.ParentStudentFeesHistory parentStudentFeesHistoryAdapter;
    SwipyRefreshLayout swipyRefreshLayout;
    Button pay;

    public static ParentStudentFeesHistory newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ParentStudentFeesHistory parentStudentFeesHistory = new ParentStudentFeesHistory();
        parentStudentFeesHistory.setArguments(args);
        return parentStudentFeesHistory;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_feedback, container, false);
        parentStudentFeesHistory = (ListView) view.findViewById(R.id.listView_teacher_feedback);
        pay = (Button) view.findViewById(R.id.btn_pay_fees);
        pay.setVisibility(View.INVISIBLE);
        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);



        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getStudentFeedList();
            }
        });

        parentStudentFeesHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ParentNewFeesHistoryDetails.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

        initData();
        getStudentFeedList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initData();
        getStudentFeedList();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEES_HISTORY+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                parentStudentFeesHistoryArray= null;
            }
            else
            {
                parentStudentFeesHistoryArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(parentStudentFeesHistoryArray!= null)
        {
            parentStudentFeesHistoryAdapter= new com.schoofi.adapters.ParentStudentFeesHistory(getActivity(),parentStudentFeesHistoryArray);
            parentStudentFeesHistory.setAdapter(parentStudentFeesHistoryAdapter);
            parentStudentFeesHistoryAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEES_HISTORY+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
               System.out.println(response);
               // System.out.println(url);
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
                    if(responseObject.has("fee"))
                    {
                        parentStudentFeesHistoryArray= new JSONObject(response).getJSONArray("fee");
                        if(null!=parentStudentFeesHistoryArray && parentStudentFeesHistoryArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = parentStudentFeesHistoryArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEES_HISTORY+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId,e);
                            parentStudentFeesHistory.invalidateViews();
                            parentStudentFeesHistoryAdapter = new com.schoofi.adapters.ParentStudentFeesHistory(getActivity(), parentStudentFeesHistoryArray);
                            parentStudentFeesHistory.setAdapter(parentStudentFeesHistoryAdapter);
                            parentStudentFeesHistoryAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

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
