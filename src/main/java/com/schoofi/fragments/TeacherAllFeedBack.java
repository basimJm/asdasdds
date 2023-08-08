package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import com.schoofi.activitiess.TeacherAllFeedbackDetails;
import com.schoofi.adapters.TeacherAllFeedBackAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeacherAllFeedBack extends Fragment{
	
	public static final String ARG_PAGE = "ARG_PAGE";
	private ListView teacherFeedBackListView;
	private JSONArray teacherFeedBackAssignedArray1;
	TeacherAllFeedBackAdapter teacherAllFeedBackAdapter;
	SwipyRefreshLayout swipyRefreshLayout;
	Button payNow;

	private int mPage;
	
	public static TeacherAllFeedBack newInstance(int page) {
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, page);
		TeacherAllFeedBack teacherAllFeedBack = new TeacherAllFeedBack();
		teacherAllFeedBack.setArguments(args);
		return teacherAllFeedBack;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPage = getArguments().getInt(ARG_PAGE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teacher_feedback, container, false);
		teacherFeedBackListView = (ListView) view.findViewById(R.id.listView_teacher_feedback);
		swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
		payNow = (Button) view.findViewById(R.id.btn_pay_fees);
		payNow.setVisibility(View.GONE);
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
					Preferences.getInstance().feedbackId = teacherFeedBackAssignedArray1.getJSONObject(position).getString("feedback_id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Preferences.getInstance().savePreference(getActivity());
				Intent intent = new Intent(getActivity(),TeacherAllFeedbackDetails.class);
				intent.putExtra("position", position);
				intent.putExtra("status", "1");
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
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TEACHER_FEEDBACK_ASSIGNED+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				teacherFeedBackAssignedArray1= null;
			}
			else
			{
				teacherFeedBackAssignedArray1= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherFeedBackAssignedArray1!= null)
		{
			teacherAllFeedBackAdapter= new TeacherAllFeedBackAdapter(getActivity(),teacherFeedBackAssignedArray1);
			teacherFeedBackListView.setAdapter(teacherAllFeedBackAdapter);
			teacherAllFeedBackAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentFeedList() 
	{
		
		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TEACHER_FEEDBACK_ASSIGNED+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId+"&ins_id="+Preferences.getInstance().institutionId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
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
						Utils.showToast(getActivity(),"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Tech_FeedList"))
							{
								teacherFeedBackAssignedArray1= new JSONObject(response).getJSONArray("Tech_FeedList");
								if(null!=teacherFeedBackAssignedArray1 && teacherFeedBackAssignedArray1.length()>=0)
								{
									Entry e = new Entry();
									e.data = teacherFeedBackAssignedArray1.toString().getBytes();
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TEACHER_FEEDBACK_ASSIGNED+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId+"&ins_id="+Preferences.getInstance().institutionId,e);
									teacherFeedBackListView.invalidateViews();
									teacherAllFeedBackAdapter= new TeacherAllFeedBackAdapter(getActivity(),teacherFeedBackAssignedArray1);
									teacherFeedBackListView.setAdapter(teacherAllFeedBackAdapter);
									teacherAllFeedBackAdapter.notifyDataSetChanged();
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
