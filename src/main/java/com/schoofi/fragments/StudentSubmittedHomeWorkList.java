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
import com.schoofi.activitiess.StudentFeedBackImages;
import com.schoofi.activitiess.TeacherStudentViewAssignment;
import com.schoofi.adapters.TeacherStudentHomeWorkStudentSubmittedDetailsAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class StudentSubmittedHomeWorkList extends Fragment{
	
	public static final String ARG_PAGE = "ARG_PAGE";
	ListView studentNotSubmittedListView;
	TeacherStudentHomeWorkStudentSubmittedDetailsAdapter teacherStudentHomeWorkStudentSubmittedDetailsAdapter;
	private JSONArray studentListArray;
	String asnId;
	public String studentFile;
	ArrayList aList= new ArrayList();
	//ArrayList aList1 = new ArrayList();
	public String file,teachId,assignId;
	Button pay;
	SwipyRefreshLayout swipyRefreshLayout;
	

	private int mPage;

	public static StudentSubmittedHomeWorkList newInstance(int page) {
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, page);
		StudentSubmittedHomeWorkList studentSubmittedHomeWorkList = new StudentSubmittedHomeWorkList();
		studentSubmittedHomeWorkList.setArguments(args);
		return studentSubmittedHomeWorkList;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPage = getArguments().getInt(ARG_PAGE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teacher_feedback, container, false);
		studentNotSubmittedListView = (ListView) view.findViewById(R.id.listView_teacher_feedback);
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
		asnId = getActivity().getIntent().getStringExtra("asn_id");
		initData();
		getStudentFeedList();
		
		studentNotSubmittedListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				try {
					studentFile	 = studentListArray.getJSONObject(position).getString("assignment");
					
				//
					//Utils.showToast(getActivity(), ""+assignId);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(studentFile.matches("") || studentFile.matches("null"))
					{
						Intent intent = new Intent(getActivity(),TeacherStudentViewAssignment.class);
						intent.putExtra("array", "");
						try {
							intent.putExtra("teacherId", studentListArray.getJSONObject(position).getString("teacher_id"));
							intent.putExtra("asn_id", studentListArray.getJSONObject(position).getString("assign_id"));
							intent.putExtra("desc", studentListArray.getJSONObject(position).getString("description"));
							intent.putExtra("title", studentListArray.getJSONObject(position).getString("title"));
							intent.putExtra("last_date", studentListArray.getJSONObject(position).getString("submit_date"));
							intent.putExtra("asn_date", "88888");
							if(studentListArray.getJSONObject(position).getString("opt_subject").matches("") || studentListArray.getJSONObject(position).getString("opt_subject").matches("null")) {
								intent.putExtra("subject", studentListArray.getJSONObject(position).getString("subject"));
							}

							else
							{
								intent.putExtra("subject", studentListArray.getJSONObject(position).getString("subject")+"("+studentListArray.getJSONObject(position).getString("opt_subject")+")");
							}

							intent.putExtra("array2","1");
							intent.putExtra("array1", "n");
							startActivity(intent);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					else
						
					{
							aList = new ArrayList<String>(Arrays.asList(studentFile.split(",")));
							
							for(int i=0;i<aList.size();i++)
							{
								System.out.println(""+aList.get(i));
							}
							
							//for(int j)
						Intent intent = new Intent(getActivity(),TeacherStudentViewAssignment.class);
						intent.putExtra("array", aList);
						try {
							intent.putExtra("teacherId", studentListArray.getJSONObject(position).getString("teacher_id"));
							intent.putExtra("asn_id", studentListArray.getJSONObject(position).getString("assign_id"));
							intent.putExtra("desc", studentListArray.getJSONObject(position).getString("description"));
							intent.putExtra("title", studentListArray.getJSONObject(position).getString("title"));
							intent.putExtra("last_date", studentListArray.getJSONObject(position).getString("submit_date"));
							intent.putExtra("asn_date", "88888");
							if(studentListArray.getJSONObject(position).getString("opt_subject").matches("") || studentListArray.getJSONObject(position).getString("opt_subject").matches("null")) {
								intent.putExtra("subject", studentListArray.getJSONObject(position).getString("subject"));
							}

							else
							{
								intent.putExtra("subject", studentListArray.getJSONObject(position).getString("subject")+"("+studentListArray.getJSONObject(position).getString("opt_subject")+")");
							}

							intent.putExtra("array2","2");
							intent.putExtra("array1", "n");
							startActivity(intent);
						} catch (JSONException e) {
							e.printStackTrace();
						}

						

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
			Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_ASSIGNMENT_SUBMITTED_LIST+"?teach_id="+Preferences.getInstance().teachId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&asn_id="+asnId);
			if(e == null)
			{
				studentListArray= null;
			}
			else
			{
				studentListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentListArray!= null)
		{
		teacherStudentHomeWorkStudentSubmittedDetailsAdapter= new TeacherStudentHomeWorkStudentSubmittedDetailsAdapter(getActivity(),studentListArray);
			studentNotSubmittedListView.setAdapter(teacherStudentHomeWorkStudentSubmittedDetailsAdapter);
			teacherStudentHomeWorkStudentSubmittedDetailsAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentFeedList() 
	{
		
		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_ASSIGNMENT_SUBMITTED_LIST+"?teach_id="+Preferences.getInstance().teachId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&asn_id="+asnId;
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
							if(responseObject.has("asn_Submitted"))
							{
								studentListArray= new JSONObject(response).getJSONArray("asn_Submitted");
								if(null!=studentListArray && studentListArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = studentListArray.toString().getBytes();
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_ASSIGNMENT_SUBMITTED_LIST+"?teach_id="+Preferences.getInstance().teachId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&asn_id="+asnId,e);
								    studentNotSubmittedListView.invalidateViews();
									teacherStudentHomeWorkStudentSubmittedDetailsAdapter= new TeacherStudentHomeWorkStudentSubmittedDetailsAdapter(getActivity(),studentListArray);
									studentNotSubmittedListView.setAdapter(teacherStudentHomeWorkStudentSubmittedDetailsAdapter);
									teacherStudentHomeWorkStudentSubmittedDetailsAdapter.notifyDataSetChanged();
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
