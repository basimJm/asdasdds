package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.StudentFeedBackImages;
import com.schoofi.adapters.FeedbackDetailListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class StudentFeedbackList extends Fragment{
	
	private ListView studentFeeedBackList;
	
	private FeedbackDetailListAdapter feedbackDetailListAdapter;
	private JSONArray studentFeedbackReply;
	String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	String strtext;
	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String url;
	public String image;
	ArrayList aList= new ArrayList();
	public String feed;
	SwipyRefreshLayout swipyRefreshLayout;
	
	//String str = "...";
	//String image;
	//List<String> elephantList = Arrays.asList(str.split(","));
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.student_feedback_list, container, false);
		strtext = getArguments().getString("feedId"); 
		feed = getArguments().getString("value");
		studentFeeedBackList = (ListView) view.findViewById(R.id.feedbackDetailList);
		swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
		swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {

				if(feed.matches("2"))
				{
					initData();
					getFeedbackReplyList();
				}
				else
				{
					initData1();
					getFeedbackReplyList1();
				}

			}
		});
		
		if(feed.matches("2"))
		{
			initData();
			getFeedbackReplyList();
		}
		else
		{
			initData1();
			getFeedbackReplyList1();

		}
		studentFeeedBackList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		
		studentFeeedBackList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				try {
					image = studentFeedbackReply.getJSONObject(position).getString("image");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(image.matches(""))
				{
					Utils.showToast(getActivity(), "No image");
				}
				
				else
				{
					aList = new ArrayList<String>(Arrays.asList(image.split(",")));
					/*for(int i=0;i<aList.size();i++)
					{
						System.out.println(""+aList.get(i));
					}*/
					
					Intent intent = new Intent(getActivity(),StudentFeedBackImages.class);
					intent.putExtra("array", aList);
					startActivity(intent);
				}
				
				
				
				
				
			}
		});
		
		
		return view;
	}
	
	
	
	private void initData()
	{
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_REPLY_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&feedback_id="+strtext+"&crr_date="+currentDate+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				studentFeedbackReply= null;
			}
			else
			{
				studentFeedbackReply= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentFeedbackReply!= null)
		{
			feedbackDetailListAdapter= new FeedbackDetailListAdapter(getActivity(),studentFeedbackReply);
			studentFeeedBackList.setAdapter(feedbackDetailListAdapter);
			feedbackDetailListAdapter.notifyDataSetChanged();
		}
	}
	
	private void getFeedbackReplyList()
	{
		//setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		
		
			url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_REPLY_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&feedback_id="+strtext+"&crr_date="+currentDate+"&device_id="+Preferences.getInstance().deviceId;
		
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println("8"+response);
				//System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						
						Log.d("kkk", "kkkk");
						
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Feedresp"))
							{
								studentFeedbackReply= new JSONObject(response).getJSONArray("Feedresp");
								if(null!=studentFeedbackReply && studentFeedbackReply.length()>=0)
								{
									Entry e = new Entry();
									e.data = studentFeedbackReply.toString().getBytes();
								
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_REPLY_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&feedback_id="+strtext+"&crr_date="+currentDate+"&device_id="+Preferences.getInstance().deviceId,e);
									studentFeeedBackList.invalidateViews();
									feedbackDetailListAdapter = new FeedbackDetailListAdapter(getActivity(), studentFeedbackReply);
									studentFeeedBackList.setAdapter(feedbackDetailListAdapter);
									feedbackDetailListAdapter.notifyDataSetChanged();
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
	
	private void initData1()
	{
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_FEEDBACK_REPLY_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&feed_id="+strtext+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				studentFeedbackReply= null;
			}
			else
			{
				studentFeedbackReply= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentFeedbackReply!= null)
		{
			feedbackDetailListAdapter= new FeedbackDetailListAdapter(getActivity(),studentFeedbackReply);
			studentFeeedBackList.setAdapter(feedbackDetailListAdapter);
			feedbackDetailListAdapter.notifyDataSetChanged();
		}
	}
	
	private void getFeedbackReplyList1()
	{
		//setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		
		
			url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_FEEDBACK_REPLY_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&feed_id="+strtext+"&device_id="+Preferences.getInstance().deviceId;
		
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				//System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						
						Log.d("kkk", "kkkk");
						
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Feed_prev_resp"))
							{
								studentFeedbackReply= new JSONObject(response).getJSONArray("Feed_prev_resp");
								if(null!=studentFeedbackReply && studentFeedbackReply.length()>=0)
								{
									Entry e = new Entry();
									e.data = studentFeedbackReply.toString().getBytes();
								
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_FEEDBACK_REPLY_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&feed_id="+strtext+"&device_id="+Preferences.getInstance().deviceId,e);
									studentFeeedBackList.invalidateViews();
									feedbackDetailListAdapter = new FeedbackDetailListAdapter(getActivity(), studentFeedbackReply);
									studentFeeedBackList.setAdapter(feedbackDetailListAdapter);
									feedbackDetailListAdapter.notifyDataSetChanged();
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
