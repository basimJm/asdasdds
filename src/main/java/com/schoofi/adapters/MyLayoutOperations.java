package com.schoofi.adapters;


import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyLayoutOperations{



	public static void display(final Activity activity, Button btn, final String taskId)
	{
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout scrollViewlinerLayout = (LinearLayout) activity.findViewById(R.id.linearLayoutForm);

				final ArrayList<String> msg = new ArrayList<String>();

				
				for (int i = 0; i < scrollViewlinerLayout.getChildCount(); i++)
				{
					LinearLayout innerLayout = (LinearLayout) scrollViewlinerLayout.getChildAt(i);
					EditText edit = (EditText) innerLayout.findViewById(R.id.editDescricao);

					msg.add(edit.getText().toString());
					
				}
				
				//Toast t = Toast.makeText(activity.getApplicationContext(), msg.toString(), Toast.LENGTH_SHORT);
				//t.show()
					//final ProgressDialog loading = ProgressDialog.show(activity.getApplicationContext(),"Creating New Task","Please wait...",false,false);
					RequestQueue queue = VolleySingleton.getInstance(activity.getApplicationContext()).getRequestQueue();
					String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_ADD_SUBTASK;
					StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {

							JSONObject responseObject;
							try
							{
								responseObject = new JSONObject(response);
								if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
								{
									//loading.dismiss();
									Utils.showToast(activity.getApplicationContext(), "Please try again later");
									//finish();
								}
								else
								if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
								{
									//loading.dismiss();
									Utils.showToast(activity.getApplicationContext(), "Subtasks Created ");
									activity.finish();
								}

								else
								if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
								{
									//loading.dismiss();
									Utils.showToast(activity.getApplicationContext(), "Session expired");

								}
							}
							catch(JSONException e)
							{
								e.printStackTrace();
								Utils.showToast(activity.getApplicationContext(), "Error submitting alert! Please try after sometime.");
							}
							//loading.dismiss();

						}}, new Response.ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error) {

							Utils.showToast(activity.getApplicationContext(), "Error creating task! Please try after sometime.");
							//loading.dismiss();
						}
					})
					{
						@Override
						protected Map<String,String> getParams(){
							Preferences.getInstance().loadPreference(activity.getApplicationContext());
							Map<String,String> params = new HashMap<String, String>();
							//params.put("u_id",Preferences.getInstance().userId);
							params.put("token", Preferences.getInstance().token);
							params.put("task_id", taskId);
							params.put("device_id", Preferences.getInstance().deviceId);
							//params.put("user_name", healthAndAuditAddUserFullName.getText().toString());
							//params.put("name", healthAndAuditAddUserUserName.getText().toString());
							params.put("ins_id", Preferences.getInstance().institutionId);
							params.put("value",msg.toString().replace("[", "").replace("]", ""));
							//params.put("password" , healthAndAuditAddUserPassword.getText().toString());
							return params;
						}};

					requestObject.setRetryPolicy(new DefaultRetryPolicy(
							25000,
							DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
							DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
					if(Utils.isNetworkAvailable(activity.getApplicationContext()))
						queue.add(requestObject);
					else
					{
						Utils.showToast(activity.getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
						//loading.dismiss();
					}
				}

		});


	}
	
	public static void add(final Activity activity, ImageView btn)
	{
		final LinearLayout linearLayoutForm = (LinearLayout) activity.findViewById(R.id.linearLayoutForm);;
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final LinearLayout newView = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.health_and_audit_sub_task__row_detail, null);

				//newView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

				ImageButton btnRemove = (ImageButton) newView.findViewById(R.id.btnRemove);
				btnRemove.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						linearLayoutForm.removeView(newView);
					}
				});

				linearLayoutForm.addView(newView);
			}
		});
	}
}

