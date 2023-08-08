package com.schoofi.activitiess;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.StockSubTypeListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.StockTypeVO;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StockManagementHome extends AppCompatActivity {
	
	Spinner stockType,classType,sectionType;
	ListView stockSubTypeList;
	private JSONArray stockTypeArray,stockSubTypeListArray;
	StockSubTypeListAdapter stockSubTypeListAdapter;
	StockTypeVO stockTypeVO = new StockTypeVO();
	private JSONObject jsonObject;
	ArrayList<String> stockTypeName;
	ArrayList<StockTypeVO> itemId;
	//userId-requestpersonId,userId-requestpersonId,userId-requestpersonId,userId-requestpersonId
	String itemId1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionbar = getSupportActionBar();
		actionbar.hide();
		setContentView(R.layout.activity_stock_management_home);
		
		stockType = (Spinner) findViewById(R.id.spinnerStockType);
		classType = (Spinner) findViewById(R.id.spinnerClass);
		sectionType = (Spinner) findViewById(R.id.spinnerSection);
		stockSubTypeList = (ListView) findViewById(R.id.listViewStockItemList);
		classType.setVisibility(View.GONE);
		sectionType.setVisibility(View.GONE);
		new DownloadJSON().execute();
	}
	
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {
		 
		@Override
		protected Void doInBackground(Void... params) {
				
			
			// Create an array to populate the spinner 
			itemId = new ArrayList<StockTypeVO>();
			stockTypeName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/
			
			// JSON file URL address
			jsonObject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.STOCK_TYPE_LIST+"?ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId);
			try {
				// Locate the NodeList name
				stockTypeArray = jsonObject.getJSONArray("stock");
				//System.out.println(stockTypeArray.toString());
				for (int i = 0; i < stockTypeArray.length(); i++) {
					jsonObject = stockTypeArray.getJSONObject(i);
					StockTypeVO stockTypeVO = new StockTypeVO();
					
					stockTypeVO.setStockId(jsonObject.optString("code"));
					itemId.add(stockTypeVO);
					
					stockTypeName.add(jsonObject.optString("full_name"));
					
				}
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			// Locate the spinner in activity_main.xml
			
			stockType
			.setAdapter(new ArrayAdapter<String>(StockManagementHome.this,
					android.R.layout.simple_spinner_dropdown_item,
					stockTypeName));
			
			stockType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					
					
				    itemId1 = itemId.get(position).getStockId().toString();
					System.out.println(itemId1);
					if(itemId1.matches("0"))
					{
						Log.d("jj", "lll");
					}
					else
					{
					
					
					
					try
					{
						Entry e;
						e = VolleySingleton.getInstance(StockManagementHome.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STOCK_TYPE_SUB_LIST+"?code="+itemId1+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId);
						if(e == null)
						{
							stockSubTypeListArray= null;
						}
						else
						{
							stockSubTypeListArray = new JSONArray(new String(e.data));
						}
					}
					catch(JSONException e)
					{
						e.printStackTrace();
					}

					if(stockSubTypeListArray!= null)
					{
						stockSubTypeListAdapter= new StockSubTypeListAdapter(StockManagementHome.this,stockSubTypeListArray);
						stockSubTypeList.setAdapter(stockSubTypeListAdapter);
						stockSubTypeListAdapter.notifyDataSetChanged();
					}
					
					RequestQueue queue = VolleySingleton.getInstance(StockManagementHome.this).getRequestQueue();
					String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STOCK_TYPE_SUB_LIST+"?code="+itemId1+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId;
					//System.out.println(url);
					StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {

							JSONObject responseObject;
							try 
							{
								responseObject = new JSONObject(response);
								//System.out.println(response);
								//toa();
								if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
								{
									//linearLayout.setVisibility(View.VISIBLE);
									//studentExamScheduleListView.setVisibility(View.INVISIBLE);
									Utils.showToast(getApplicationContext(), "No Records Found");
								}
								else
									if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
									{
										Utils.showToast(StockManagementHome.this, "Session Expired:Please Login Again");
									}
									else
										if(responseObject.has("Stock_sub_list"))
										{
											//noRecords.setVisibility(View.INVISIBLE);
											//studentExamScheduleListView.setVisibility(View.VISIBLE);
											stockSubTypeListArray= new JSONObject(response).getJSONArray("Stock_sub_list");
											if(null!=stockSubTypeListArray && stockSubTypeListArray.length()>=0)
											{
												Entry e = new Entry();
												e.data = stockSubTypeListArray.toString().getBytes();
												VolleySingleton.getInstance(StockManagementHome.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STOCK_TYPE_SUB_LIST+"?code="+itemId1+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId,e);
												stockSubTypeList.invalidateViews();
												stockSubTypeListAdapter= new StockSubTypeListAdapter(StockManagementHome.this,stockSubTypeListArray);
												stockSubTypeList.setAdapter(stockSubTypeListAdapter);
												stockSubTypeListAdapter.notifyDataSetChanged();	
											}
										}
										else
										{
											Utils.showToast(StockManagementHome.this, responseObject.getString("errorMessage"));
										}
							}
							catch(JSONException e)
							{
								e.printStackTrace();
								Utils.showToast(StockManagementHome.this, "Error fetching modules! Please try after sometime.");
							}

						}}, new Response.ErrorListener() 
						{
							@Override
							public void onErrorResponse(VolleyError error) 
							{
								Utils.showToast(StockManagementHome.this, "Error fetching modules! Please try after sometime.");
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
						if(Utils.isNetworkAvailable(StockManagementHome.this))
							queue.add(requestObject);
						else
						{
							Utils.showToast(StockManagementHome.this, "Unable to fetch data, kindly enable internet settings!");
						}
					}

					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
				
			});
			
			
			
	
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_management_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
