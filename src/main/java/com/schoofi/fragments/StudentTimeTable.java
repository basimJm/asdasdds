package com.schoofi.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.StudentLeaveRequest;
import com.schoofi.activitiess.StudentSchedule;
import com.schoofi.activitiess.StudentTimeTableDetails;
import com.schoofi.adapters.StudentLeaveListAdapter;
import com.schoofi.adapters.StudentListViewTimeTableAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class StudentTimeTable extends Fragment {
	
	private TextView period,subject;
	private ListView studentListViewTimeTable;
	private StudentListViewTimeTableAdapter studentListViewTimeTableAdapter;
	private JSONArray studentTimeTableArray;
	private Context context;
	//String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	String userEmailId = Preferences.getInstance().userEmailId;
	String userId = Preferences.getInstance().userId;
	String token = Preferences.getInstance().token;
	String studentId = Preferences.getInstance().studentId;
	String sectionId = Preferences.getInstance().studentSectionId;
	String classId = Preferences.getInstance().studentClassId;
	String schoolId = Preferences.getInstance().schoolId;
	String date;
	//String date = getArguments().getString("date");
	//String dates;
	
	@Deprecated
	public void onAttach(StudentSchedule studentSchedule) {
		// TODO Auto-generated method stub
		date = getArguments().getString("date");
		
		super.onAttach(studentSchedule);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view =inflater.inflate(R.layout.student_time_table, container, false);
		period = (TextView) view.findViewById(R.id.text_period);
		subject = (TextView) view.findViewById(R.id.text_subject);
		studentListViewTimeTable = (ListView) view.findViewById(R.id.student_time_table_listView);
		context = getActivity();
		studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
		/*System.out.println(date);
		System.out.println(userEmailId);
		System.out.println(userId);
		System.out.println(studentId);
		System.out.println(sectionId);
		System.out.println(token);
		System.out.println(classId);
		System.out.println(schoolId);*/
		
		initData();
		getTimeTable();
		studentListViewTimeTable.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent  = new Intent(context,StudentTimeTableDetails.class);
				intent.putExtra("position", position);
				startActivity(intent);
				
			}
		});
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getTimeTable();
	}
	
	private void initData() 
	{
		/*if(date1 == null)
		{
		  dates = date;	
		}
		
		else
		{
			dates = date1;
		}*/
		
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&stu_id="+studentId+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&date="+date);
			if(e == null)
			{
				studentTimeTableArray= null;
			}
			else
			{
				studentTimeTableArray = new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentTimeTableArray!= null)
		{
			studentListViewTimeTableAdapter= new StudentListViewTimeTableAdapter(context,studentTimeTableArray);
			studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
			studentListViewTimeTableAdapter.notifyDataSetChanged();
		}
	}
	
	protected void getTimeTable() 
	{
		/*if(date1 == null)
		{
		  dates = date;	
		}
		
		else
		{
			dates = date1;
		}*/
		
		RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
		String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&stu_id="+studentId+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&date="+date;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					System.out.println(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(context,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(context, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Schedule"))
							{
								studentTimeTableArray= new JSONObject(response).getJSONArray("Schedule");
								if(null!=studentTimeTableArray && studentTimeTableArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = studentTimeTableArray.toString().getBytes();
									VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&stu_id="+studentId+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&date="+date/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/,e);
									studentListViewTimeTable.invalidateViews();
									studentListViewTimeTableAdapter= new StudentListViewTimeTableAdapter(context, studentTimeTableArray);
									studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
									studentListViewTimeTableAdapter.notifyDataSetChanged();	
								}
							}
					else
					{
						Utils.showToast(context, responseObject.getString("errorMessage"));
					}
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
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				Preferences.getInstance().loadPreference(context);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("stu_id", Preferences.getInstance().studentId);
				params.put("sch_id", Preferences.getInstance().schoolId);
				params.put("token", Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("u_id", Preferences.getInstance().userId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("date", date);
				return params;
			}*/};		

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
