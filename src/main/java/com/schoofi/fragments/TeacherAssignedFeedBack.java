package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
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
import com.schoofi.activitiess.TeacherAssignedFeedBackDetails;
import com.schoofi.adapters.TeacherFeedbackAssignedAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeacherAssignedFeedBack extends Fragment{
	
	public static final String ARG_PAGE = "ARG_PAGE";
	private ListView teacherFeedBackListView;
	private JSONArray teacherFeedBackAssignedArray;
	TeacherFeedbackAssignedAdapter teacherFeedbackAssignedAdapter;
	SwipyRefreshLayout swipyRefreshLayout;

	private int mPage;
	
	public static TeacherAssignedFeedBack newInstance(int page) {
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, page);
		TeacherAssignedFeedBack teacherAssignedFeedBack = new TeacherAssignedFeedBack();
		teacherAssignedFeedBack.setArguments(args);
		return teacherAssignedFeedBack;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPage = getArguments().getInt(ARG_PAGE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teacher_all_feedback, container, false);
		teacherFeedBackListView = (ListView) view.findViewById(R.id.listView_teacher_feedback);
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
		
		teacherFeedBackListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Preferences.getInstance().loadPreference(getActivity());
				try {
					Preferences.getInstance().feedbackId = teacherFeedBackAssignedArray.getJSONObject(position).getString("feedback_id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Preferences.getInstance().savePreference(getActivity());
				
				Intent intent = new Intent(getActivity(),TeacherAssignedFeedBackDetails.class);
				intent.putExtra("position", position);
				intent.putExtra("status", "2");
				startActivity(intent);
				
			}
		});
		
				
		return view;
		
	}
	
	private void initData() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TEACHER_FEEDBACK+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId);
			if(e == null)
			{
				teacherFeedBackAssignedArray= null;
			}
			else
			{
				teacherFeedBackAssignedArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherFeedBackAssignedArray!= null)
		{
			teacherFeedbackAssignedAdapter= new TeacherFeedbackAssignedAdapter(getActivity(),teacherFeedBackAssignedArray);
			teacherFeedBackListView.setAdapter(teacherFeedbackAssignedAdapter);
			teacherFeedbackAssignedAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentFeedList() 
	{
		
		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TEACHER_FEEDBACK+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
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
						Utils.showToast(getActivity(),"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Tech_FeedList"))
							{
								teacherFeedBackAssignedArray= new JSONObject(response).getJSONArray("Tech_FeedList");
								if(null!=teacherFeedBackAssignedArray && teacherFeedBackAssignedArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = teacherFeedBackAssignedArray.toString().getBytes();
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TEACHER_FEEDBACK+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId,e);
									teacherFeedBackListView.invalidateViews();
									teacherFeedbackAssignedAdapter = new TeacherFeedbackAssignedAdapter(getActivity(), teacherFeedBackAssignedArray);
									teacherFeedBackListView.setAdapter(teacherFeedbackAssignedAdapter);
									teacherFeedbackAssignedAdapter.notifyDataSetChanged();
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
