package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudentFeedBackReply extends Fragment{
	
	private Button submit,cancel;
	private EditText comment;
	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	String url1,strtext,msg;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.student_feedback_reply, container, false);
		submit = (Button) view.findViewById(R.id.btn_submit);
		cancel = (Button) view.findViewById(R.id.btn_cancel);
		comment = (EditText) view.findViewById(R.id.edit_msg);
		strtext = getArguments().getString("feedId"); 
		getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 Intent intent = getActivity().getIntent();
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                         | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 getActivity().overridePendingTransition(0, 0);
                 getActivity().finish();

                 getActivity().overridePendingTransition(0, 0);
                 startActivity(intent);
				
			}
		});
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(comment.getText().toString().matches(""))
				{
					Utils.showToast(getActivity(), "Plz fill the message");
				}
				
				else
				{
					postMessage();
				}
				
			}
		});
		return view;
	}
	
	private void postMessage()
	{
		//setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		
		
			msg = comment.getText().toString();
			url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_POST_COMMENT;
		
		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						
						Utils.showToast(getActivity(),"Error Submitting Comment");
						
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
						}
					
						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{
								
								Utils.showToast(getActivity(),"Successfuly Submitted Comment");
								Intent intent = getActivity().getIntent();
				                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
				                         | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				                 getActivity().overridePendingTransition(0, 0);
				                 getActivity().finish();

				                 getActivity().overridePendingTransition(0, 0);
				                 startActivity(intent);
							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getActivity(), "Error submitting alert! Please try after sometime.");
				}
				//setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(getActivity(), "Error submitting alert! Please try after sometime.");
					//setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(getActivity());
				Map<String,String> params = new HashMap<String, String>();
				params.put("token",Preferences.getInstance().token);
				params.put("device_id",Preferences.getInstance().deviceId);
				params.put("u_email_id", Preferences.getInstance().userEmailId);
				params.put("user_id",Preferences.getInstance().userId);
				params.put("feedback_id", strtext);
				params.put("crr_date", currentDate);
				params.put("reply", msg);
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
				//setSupportProgressBarIndeterminateVisibility(false);
			}
	}

}
