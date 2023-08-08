package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
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
import com.schoofi.activitiess.AssignmentDetails;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.TeacherAssignmentHomeWorkDetails;
import com.schoofi.adapters.AssignmentMultiLevelListAdapter;
import com.schoofi.adapters.HomeWorkAdapter;
import com.schoofi.adapters.TeacherNewMultilevelAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.AssignmentMultiLevelVO;
import com.schoofi.utils.DiaryVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class TeacherHomework extends Fragment{
	
	public static final String ARG_PAGE = "ARG_PAGE";

	private int mPage;
	private TeacherNewMultilevelAdapter teacherNewMultilevelAdapter;
	private JSONArray AssignmentArray;
	private ArrayList<DiaryVO> diaryVOs;
	private ArrayList<AssignmentMultiLevelVO> assignmentMultiLevelVOs;
	private ExpandableListView expandableListView;
	SwipyRefreshLayout swipyRefreshLayout;
	JSONObject jsonObject,jsonObject1;
	JSONArray jsonArray;
	ArrayList aList= new ArrayList();
	String value="0";
	ArrayList aList1 = new ArrayList();
	public String file,teachId,assignId,description,studentFile,submitDate,subjectName,title,teacherName;

	public static TeacherHomework newInstance(int page) {
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, page);
		TeacherHomework teacherHomework = new TeacherHomework();
		teacherHomework.setArguments(args);
		return teacherHomework;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPage = getArguments().getInt(ARG_PAGE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.assignment_new_multilevel_main_layout, container, false);
		expandableListView = (ExpandableListView) view.findViewById(R.id.listViewInnerAllAssignment);
		swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);

		expandableListView.setChildIndicator(null);
		expandableListView.setGroupIndicator(null);
		//expandableListView.setChildDivider(getResources().getDrawable(R.color.transparent));
		expandableListView.setDivider(getResources().getDrawable(R.color.transparent));
		expandableListView.setDividerHeight(2);

		expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {



				file = diaryVOs.get(groupPosition).getItems1().get(childPosition).getTeacherfile();
				studentFile = diaryVOs.get(groupPosition).getItems1().get(childPosition).getStudentFile();
				assignId = diaryVOs.get(groupPosition).getItems1().get(childPosition).getAssignId();
				submitDate = diaryVOs.get(groupPosition).getItems1().get(childPosition).getSubmitdate();
				description = diaryVOs.get(groupPosition).getItems1().get(childPosition).getDescription();
				if(diaryVOs.get(groupPosition).getItems1().get(childPosition).getOptionalSubject().matches("") || diaryVOs.get(groupPosition).getItems1().get(childPosition).getOptionalSubject().matches("null")) {
					subjectName = diaryVOs.get(groupPosition).getItems1().get(childPosition).getSubjectName();
				}

				else
				{
					subjectName = diaryVOs.get(groupPosition).getItems1().get(childPosition).getSubjectName()+"("+diaryVOs.get(groupPosition).getItems1().get(childPosition).getOptionalSubject()+")";
				}
				title = diaryVOs.get(groupPosition).getItems1().get(childPosition).getTitle();
				teachId  = diaryVOs.get(groupPosition).getItems1().get(childPosition).getTeacherId();
				teacherName = diaryVOs.get(groupPosition).getItems1().get(childPosition).getTeacherName();



					if(file.matches("") || file.matches("null")) {
						value = "1";

						aList = new ArrayList<String>(Arrays.asList(file.split(",")));
						Intent intent = new Intent(getActivity(), TeacherAssignmentHomeWorkDetails.class);
						intent.putExtra("array", "");
						intent.putExtra("teacherId", teachId);
						intent.putExtra("asn_id", assignId);
						intent.putExtra("desc", description);
						intent.putExtra("title", title);
						intent.putExtra("last_date", submitDate);
						intent.putExtra("asn_date", diaryVOs.get(groupPosition).getDate());
						intent.putExtra("subject", subjectName);
						intent.putExtra("teach_name", teacherName);
						intent.putExtra("value", value);

						intent.putExtra("array2","1");
						intent.putExtra("array1", "n");

						startActivity(intent);
					}




				else {

					aList = new ArrayList<String>(Arrays.asList(file.split(",")));
					//aList1 = new ArrayList<String>(Arrays.asList(studentFile.split(",")));
					for (int i = 0; i < aList.size(); i++) {
						System.out.println("" + aList.get(i));
					}

					for (int j = 0; j < aList1.size(); j++) {
						System.out.println("" + aList1.get(j));
					}

					Intent intent = new Intent(getActivity(), TeacherAssignmentHomeWorkDetails.class);
					intent.putExtra("array", aList);
					//intent.putExtra("array1", aList1);
					intent.putExtra("teacherId", teachId);
					intent.putExtra("asn_id", assignId);
					intent.putExtra("desc", description);
					intent.putExtra("title", title);
					intent.putExtra("last_date", submitDate);
					intent.putExtra("asn_date", diaryVOs.get(groupPosition).getDate());
					intent.putExtra("subject", subjectName);
					intent.putExtra("teach_name", teacherName);
						intent.putExtra("array1", "n");
						intent.putExtra("array2","2");




					startActivity(intent);
				}



				return false;
			}
		});
		
		/*allAssignMentListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				try {
				file	 = AssignmentArray.getJSONObject(position).getString("assign_path");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(file.matches(""))
				{
					try {
						asnId = AssignmentArray.getJSONObject(position).getString("id");
						description = AssignmentArray.getJSONObject(position).getString("description");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Intent intent = new Intent(getActivity(),TeacherAssignmentHomeWorkDetails.class);
					intent.putExtra("array", "");
					intent.putExtra("array2","1");
					intent.putExtra("array1", "n");
					intent.putExtra("asn_id", asnId);
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
					
					try {
						asnId = AssignmentArray.getJSONObject(position).getString("id");
						description = AssignmentArray.getJSONObject(position).getString("description");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Intent intent = new Intent(getActivity(),TeacherAssignmentHomeWorkDetails.class);
					intent.putExtra("array", aList);
					intent.putExtra("array1", "n");
					intent.putExtra("array2","2");
					intent.putExtra("asn_id", asnId);
					intent.putExtra("desc",description);
					startActivity(intent);
				}
				
				
				
				
				
			}
		});*/
		
		
		return view;
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//initData();
		getStudentFeedList();
	}
	
	/*private void initData()
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ASSIGNMENT_URL+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&temp="+"2"+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				AssignmentArray= null;
			}
			else
			{
				AssignmentArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(AssignmentArray!= null)
		{
			homeWorkAdapter= new HomeWorkAdapter(getActivity(),AssignmentArray);
			allAssignMentListView.setAdapter(homeWorkAdapter);
			homeWorkAdapter.notifyDataSetChanged();
		}
	}*/

	protected void getStudentFeedList() 
	{
		
		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ASSIGNMENT_URL+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&temp="+"2"+"&ins_id="+Preferences.getInstance().institutionId;
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
						Utils.showToast(getActivity(),"No Assignments/Home-works Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Tech_asn"))
							{
								AssignmentArray= new JSONObject(response).getJSONArray("Tech_asn");
								if(null!=AssignmentArray && AssignmentArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = AssignmentArray.toString().getBytes();
									diaryVOs = new ArrayList<DiaryVO>();
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ASSIGNMENT_URL+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&temp="+"2"+"&ins_id="+Preferences.getInstance().institutionId,e);
									expandableListView.invalidateViews();

									for(int i=0;i<AssignmentArray.length();i++)
									{
										DiaryVO diaryVO = new DiaryVO();
										diaryVO.setDate(AssignmentArray.getJSONObject(i).getString("created_date"));
										jsonObject = AssignmentArray.getJSONObject(i);
										jsonArray = jsonObject.getJSONArray("bifurcation");

										assignmentMultiLevelVOs = new ArrayList<AssignmentMultiLevelVO>();

										for(int j=0;j<jsonArray.length();j++)
										{
											jsonObject1 = jsonArray.getJSONObject(j);
											AssignmentMultiLevelVO assignmentMultiLevelVO = new AssignmentMultiLevelVO();
											assignmentMultiLevelVO.setAssignId(jsonObject1.getString("id"));
											assignmentMultiLevelVO.setDescription(jsonObject1.getString("description"));
											assignmentMultiLevelVO.setOptionalSubject(jsonObject1.getString("opt_subject"));
											assignmentMultiLevelVO.setSubjectName(jsonObject1.getString("subject"));
											//assignmentMultiLevelVO.setStudentFile(jsonObject1.getString("assign_path"));
											assignmentMultiLevelVO.setTeacherfile(jsonObject1.getString("assign_path"));
											assignmentMultiLevelVO.setTitle(jsonObject1.getString("title"));
											assignmentMultiLevelVO.setTeacherName(jsonObject1.getString("teac_name"));
											assignmentMultiLevelVO.setSubmitdate(jsonObject1.getString("last_date"));
											assignmentMultiLevelVO.setTeacherId(jsonObject1.getString("teacher_id"));
											assignmentMultiLevelVO.setType(jsonObject1.getString("type"));
											assignmentMultiLevelVOs.add(assignmentMultiLevelVO);
										}
										diaryVO.setItems1(assignmentMultiLevelVOs);
										diaryVOs.add(diaryVO);
									}

									teacherNewMultilevelAdapter = new TeacherNewMultilevelAdapter(getActivity(),diaryVOs);
									expandableListView.setAdapter(teacherNewMultilevelAdapter);
									teacherNewMultilevelAdapter.notifyDataSetChanged();

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
