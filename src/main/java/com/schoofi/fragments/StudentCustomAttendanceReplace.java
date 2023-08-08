package com.schoofi.fragments;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Cache.Entry;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.StudentCustomAttendanceAdapter;
import com.schoofi.adapters.StudentWeeklyAttendanceAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class StudentCustomAttendanceReplace extends Fragment{
	
	ImageView studentCustomAttendanceReplaceImageView;
	ListView studentCustomAttendanceReplaceListView;
	public JSONArray studentCustomAttendanceArray;
	private Context context;
	StudentCustomAttendanceAdapter studentCustomAttendanceAdapter;
	String startingDate,endingDate;
	SwipyRefreshLayout swipyRefreshLayout;
	
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.student_custom_attendance_replace, container, false);
		studentCustomAttendanceReplaceImageView = (ImageView) view.findViewById(R.id.imageView_calender);
		studentCustomAttendanceReplaceListView = (ListView) view.findViewById(R.id.student_custom_attendance_replace_listview);
		swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
		swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				initData();
				getStudentCustomAttendance();
			}
		});
		startingDate = getArguments().getString("StartingDate");
		endingDate = getArguments().getString("EndingDate");
		context = getActivity();
		initData();
		getStudentCustomAttendance();
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getStudentCustomAttendance();
	}
	
	 private void initData() 
		{
			try
			{
				Entry e;
				e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_WEEKLY_ATTENDANCE_URL);
				if(e == null)
				{
					studentCustomAttendanceArray = null;
				}
				else
				{
					studentCustomAttendanceArray = new JSONArray(new String(e.data));
				}
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}

			if(studentCustomAttendanceArray!= null)
			{
				studentCustomAttendanceAdapter= new StudentCustomAttendanceAdapter(getActivity(),studentCustomAttendanceArray);
				studentCustomAttendanceReplaceListView.setAdapter(studentCustomAttendanceAdapter);
				studentCustomAttendanceAdapter.notifyDataSetChanged();
			}
		}
		
		protected void getStudentCustomAttendance() 
		{
			RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
			String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_WEEKLY_ATTENDANCE_URL;
			StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {

					JSONObject responseObject;
					try 
					{
						responseObject = new JSONObject(response);
						toa();
						if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(context,"No Records Found");
						else
							if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
							{
								Utils.showToast(context, "Session Expired:Please Login Again");
							}
							else
								if(responseObject.has("atten-daily"))
								{
									studentCustomAttendanceArray= new JSONObject(response).getJSONArray("atten-daily");
									if(null!=studentCustomAttendanceArray && studentCustomAttendanceArray.length()>=0)
									{
										Entry e = new Entry();
										e.data = studentCustomAttendanceArray.toString().getBytes();
										VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_WEEKLY_ATTENDANCE_URL,e);
										studentCustomAttendanceReplaceListView.invalidateViews();
										studentCustomAttendanceAdapter = new StudentCustomAttendanceAdapter(context, studentCustomAttendanceArray);
										studentCustomAttendanceReplaceListView.setAdapter(studentCustomAttendanceAdapter);
								        studentCustomAttendanceAdapter.notifyDataSetChanged();
										swipyRefreshLayout.setRefreshing(false);
									}
								}
								else
									Utils.showToast(context, "Error Fetching Response");
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
					Map<String,String> params = new HashMap<String, String>();
					params.put("sch_id",Preferences.getInstance().schoolId);
					params.put("sec_id",Preferences.getInstance().studentSectionId);
					params.put("token",Preferences.getInstance().token);
					params.put("u_email_id",Preferences.getInstance().userEmailId);
					params.put("stu_id",Preferences.getInstance().studentId);
					params.put("u_id",Preferences.getInstance().userId);
					params.put("startingDate",startingDate);
					params.put("endingDate", endingDate);
					params.put("cls_id", Preferences.getInstance().studentClassId);
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
