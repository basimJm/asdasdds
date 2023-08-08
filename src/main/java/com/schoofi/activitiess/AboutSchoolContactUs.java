package com.schoofi.activitiess;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.DataItem;
import com.schoofi.adapters.AdminClassListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import utils.CustomMarker;
import utils.LatLngInterpolator;
import utils.MarkerAnimation;


public class AboutSchoolContactUs extends FragmentActivity implements OnMapReadyCallback {

	// Google Map
	private GoogleMap googleMap;
	private HashMap markersHashMap;
	private Iterator<Entry> iter;
	private CameraUpdate cu;
	Marker newMark;
	private CustomMarker customMarkerOne, customMarkerTwo;
	int j=0;
	int count=0;
	ArrayList<String> myList;
	ArrayList<String> myList1;
	String routeNo;

	JSONArray mapsArray,userArray;
	String queues,queues1;
	int k=0;
	JSONObject jsonObject;
	String busNumber;
	int g=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("About School ContactUs");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_about_school_contact_us);

		//googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).getMap();
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
		mapFragment.getMapAsync(this);

	}

	protected void getChairmanStudentLeaveList()
	{
		//setSupportProgressBarIndeterminateVisibility(true);

		RequestQueue queue = VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue();
		final String url = "http://fasttracksoft.us/api/login.php?user=jp_int&password=12345";/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);

				try
				{
					googleMap.clear();
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(AboutSchoolContactUs.this,"No Records Found");
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(AboutSchoolContactUs.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("VehicleList"))
					{
						mapsArray= new JSONObject(response).getJSONArray("VehicleList");
						if(null!=mapsArray && mapsArray.length()>=0)
						{
							Cache.Entry e = new Cache.Entry();
							e.data = mapsArray.toString().getBytes();
							VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue().getCache().put("http://fasttracksoft.us/api/login.php?user=jp_int&password=12345",e);


							for(j=0;j<mapsArray.length();j++)
							{
								Log.d("j",""+j);

							}

							CustomMarker[] customMarker = new CustomMarker[j];
							Marker[] newMarker = new Marker[j];



							/*try {
								// Loading map
								//initilizeMap();
								initializeUiSettings();
								setUpMarkersHashMap();
								initializeMapLocationSettings();
								initializeMapTraffic();
								initializeMapType();
								initializeMapViewSettings();

								//removeMarker(customMarkerOne);
								//zoomAnimateLevelToFitMarkers(120);


							} catch (Exception e1) {
								e1.printStackTrace();
							}*/


                            for(int i=0;i<mapsArray.length();i++)
							{


								    customMarker[i] = new CustomMarker("marker" + String.valueOf(i), Double.parseDouble(mapsArray.getJSONObject(i).getString("Latitude")), Double.parseDouble(mapsArray.getJSONObject(i).getString("Longitude")));
									//addMarker(customMarker[i]);
								if(newMarker[i]!=null)
								{
									newMarker[i].remove();
								}
								MarkerOptions markerOption = new MarkerOptions().position(
										new LatLng(customMarker[i].getCustomMarkerLatitude(), customMarker[i].getCustomMarkerLongitude())).title(mapsArray.getJSONObject(i).getString("VehicleName")).snippet("Ignition: "+mapsArray.getJSONObject(i).getString("Ignition")).icon(
										BitmapDescriptorFactory.defaultMarker());

								newMarker[i] = googleMap.addMarker(markerOption);
								addMarkerToHashMap(customMarker[i], newMarker[i]);

								   // k=1;



							}

							LatLngBounds.Builder b = new LatLngBounds.Builder();
							iter = markersHashMap.entrySet().iterator();

							while (iter.hasNext()) {
								Map.Entry mEntry = (Map.Entry) iter.next();
								CustomMarker key = (CustomMarker) mEntry.getKey();
								LatLng ll = new LatLng(key.getCustomMarkerLatitude(), key.getCustomMarkerLongitude());
								b.include(ll);
							}
							LatLngBounds bounds = b.build();

							// Change the padding as per needed
							cu = CameraUpdateFactory.newLatLngBounds(bounds, 21);
							googleMap.moveCamera(cu);
							googleMap.animateCamera(cu);
						}
					}
					else
						Utils.showToast(AboutSchoolContactUs.this, "Error Fetching Response");
					//setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(AboutSchoolContactUs.this, "Error fetching modules! Please try after sometime.");
					//setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
				//setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(ChairmanStudentLeaves.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("cls_id", classId);
				//params.put("sec_id", sectionId);
				//params.put("crr_date",currentDate);
				params.put("device_id", Preferences.getInstance().deviceId);
				return params;
			}*/};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(AboutSchoolContactUs.this))
			queue.add(requestObject);
		else
		{
			Utils.showToast(AboutSchoolContactUs.this, "Unable to fetch data, kindly enable internet settings!");
		}
	}

	protected void getChairmanStudentLeaveList1()
	{
		//setSupportProgressBarIndeterminateVisibility(true);

		RequestQueue queue = VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue();
		final String url = "http://fasttracksoft.us/api/login.php?user=jp_int&password=12345";//**//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//**//*
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);

				try
				{
					googleMap.clear();
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(AboutSchoolContactUs.this,"No Records Found");
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(AboutSchoolContactUs.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("VehicleList"))
					{
						mapsArray= new JSONObject(response).getJSONArray("VehicleList");
						if(null!=mapsArray && mapsArray.length()>=0)
						{
							Cache.Entry e = new Cache.Entry();
							e.data = mapsArray.toString().getBytes();
							VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue().getCache().put("http://fasttracksoft.us/api/login.php?user=jp_int&password=12345",e);




							CustomMarker[] customMarker = new CustomMarker[2];
							Marker[] newMarker = new Marker[2];



							for(j=0;j<mapsArray.length();j++)
							{
								if(mapsArray.getJSONObject(j).getString("VehicleName").matches(Preferences.getInstance().busNumber))
								{
									count = j;
								}
							}








								customMarker[1] = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(count).getString("Latitude")), Double.parseDouble(mapsArray.getJSONObject(count).getString("Longitude")));
								//addMarker(customMarker[i]);
								if(newMarker[1]!=null)
								{
									newMarker[1].remove();
								}
								MarkerOptions markerOption = new MarkerOptions().position(
										new LatLng(customMarker[1].getCustomMarkerLatitude(), customMarker[1].getCustomMarkerLongitude())).title(mapsArray.getJSONObject(count).getString("VehicleName")).snippet("Ignition: "+mapsArray.getJSONObject(count).getString("Ignition")).icon(
										BitmapDescriptorFactory.fromResource(R.drawable.gpsbus));

								newMarker[1] = googleMap.addMarker(markerOption);
								addMarkerToHashMap(customMarker[1], newMarker[1]);

								//k=1;





							LatLngBounds.Builder b = new LatLngBounds.Builder();
							iter = markersHashMap.entrySet().iterator();

							while (iter.hasNext()) {
								Map.Entry mEntry = (Map.Entry) iter.next();
								CustomMarker key = (CustomMarker) mEntry.getKey();
								LatLng ll = new LatLng(key.getCustomMarkerLatitude(), key.getCustomMarkerLongitude());
								b.include(ll);
							}
							LatLngBounds bounds = b.build();

							// Change the padding as per needed
							cu = CameraUpdateFactory.newLatLngBounds(bounds, 21);
							googleMap.moveCamera(cu);
							googleMap.animateCamera(cu);
						}
					}
					else
						Utils.showToast(AboutSchoolContactUs.this, "Error Fetching Response");
					//setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(AboutSchoolContactUs.this, "Error fetching modules! Please try after sometime.");
					//setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
				//setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(AboutSchoolContactUs.this))
			queue.add(requestObject);
		else
		{
			Utils.showToast(AboutSchoolContactUs.this, "Unable to fetch data, kindly enable internet settings!");
		}
	}

	protected void getChairmanStudentLeaveList2()
	{
		//setSupportProgressBarIndeterminateVisibility(true);

		RequestQueue queue = VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ADMIN_BUS_LIST_MAP+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId;/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				//System.out.println(url);

				try
				{
					googleMap.clear();
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(AboutSchoolContactUs.this,"No Records Found");
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(AboutSchoolContactUs.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("VehicleList"))
					{
						mapsArray= new JSONObject(response).getJSONArray("VehicleList");
						if(null!=mapsArray && mapsArray.length()>=0) {
							Cache.Entry e = new Cache.Entry();
							e.data = mapsArray.toString().getBytes();
							VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST_MAP + "?ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId, e);


							for (j = 0; j < mapsArray.length(); j++) {

							}

							CustomMarker[] customMarker = new CustomMarker[j];
							Marker[] newMarker = new Marker[j];

							String[] trimmedArray = new String[myList.size()];
							String[] trimmedArray2 = new String[myList1.size()];

							trimmedArray = myList.toArray(trimmedArray);
							trimmedArray2 = myList1.toArray(trimmedArray2);
							String[] trimmedArray1 = new String[trimmedArray.length];
							String[] trimmedArray3 = new String[trimmedArray2.length];
							for (int i = 0; i < trimmedArray.length; i++) {
								trimmedArray1[i] = trimmedArray[i].trim();
								trimmedArray3[i] = trimmedArray2[i].trim();
							}


							for (int k = 0; k < trimmedArray1.length; k++) {
								Log.d("k value", "k" + k);
								for (int y = 0; y < mapsArray.length(); y++) {
									Log.d("y value", "y" + y);
									if (trimmedArray1[k].equals(mapsArray.getJSONObject(y).getString("bus_no")))


									{
										//System.out.println("position"+String.valueOf(k));
										g++;
										customMarker[y] = new CustomMarker("marker" + String.valueOf(y), Double.parseDouble(mapsArray.getJSONObject(y).getString("latitude")), Double.parseDouble(mapsArray.getJSONObject(y).getString("longitude")));
										//addMarker(customMarker[i]);
										if (newMarker[y] != null) {
											newMarker[y].remove();
										}
										MarkerOptions markerOption = new MarkerOptions().position(
												new LatLng(customMarker[y].getCustomMarkerLatitude(), customMarker[y].getCustomMarkerLongitude())).title(mapsArray.getJSONObject(y).getString("bus_no") + "(" + trimmedArray3[k] + ")").snippet("Ignition: " + mapsArray.getJSONObject(y).getString("ignition")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gpsbus));

										newMarker[y] = googleMap.addMarker(markerOption);
										addMarkerToHashMap(customMarker[y], newMarker[y]);
										//System.out.println("arrty"+mapsArray.getJSONObject(y).getString("VehicleName"));

										//jsonObject = mapsArray.getJSONObject(y);
										//System.out.println(jsonObject.toString());
										y = mapsArray.length();
									}


								}


							}







							/*try {
								// Loading map
								//initilizeMap();
								initializeUiSettings();
								setUpMarkersHashMap();
								initializeMapLocationSettings();
								initializeMapTraffic();
								initializeMapType();
								initializeMapViewSettings();

								//removeMarker(customMarkerOne);
								//zoomAnimateLevelToFitMarkers(120);


							} catch (Exception e1) {
								e1.printStackTrace();
							}*/


							/*for(int i=0;i<mapsArray.length();i++)
							{


								customMarker[i] = new CustomMarker("marker" + String.valueOf(i), Double.parseDouble(mapsArray.getJSONObject(i).getString("Latitude")), Double.parseDouble(mapsArray.getJSONObject(i).getString("Longitude")));
								//addMarker(customMarker[i]);
								if(newMarker[i]!=null)
								{
									newMarker[i].remove();
								}
								MarkerOptions markerOption = new MarkerOptions().position(
										new LatLng(customMarker[i].getCustomMarkerLatitude(), customMarker[i].getCustomMarkerLongitude())).title(mapsArray.getJSONObject(i).getString("VehicleName")).snippet("Ignition: "+mapsArray.getJSONObject(i).getString("Ignition")).icon(
										BitmapDescriptorFactory.defaultMarker());

								newMarker[i] = googleMap.addMarker(markerOption);
								addMarkerToHashMap(customMarker[i], newMarker[i]);

								k=1;



							}*/

							if (g > 0) {

								LatLngBounds.Builder b = new LatLngBounds.Builder();
								iter = markersHashMap.entrySet().iterator();

								while (iter.hasNext()) {
									Entry mEntry = (Entry) iter.next();
									CustomMarker key = (CustomMarker) mEntry.getKey();
									LatLng ll = new LatLng(key.getCustomMarkerLatitude(), key.getCustomMarkerLongitude());
									b.include(ll);
								}
								LatLngBounds bounds = b.build();

								//if(bounds<0)

								// Change the padding as per needed
								cu = CameraUpdateFactory.newLatLngBounds(bounds, 21);
								googleMap.moveCamera(cu);
								googleMap.animateCamera(cu);

							}
						}
						else
						{

						}
					}
					else
						Utils.showToast(AboutSchoolContactUs.this, "Error Fetching Response");
					//setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(AboutSchoolContactUs.this, "Error fetching modules! Please try after sometime.");
					//setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
				//setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(ChairmanStudentLeaves.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("cls_id", classId);
				//params.put("sec_id", sectionId);
				//params.put("crr_date",currentDate);
				params.put("device_id", Preferences.getInstance().deviceId);
				return params;
			}*/};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(AboutSchoolContactUs.this))
			queue.add(requestObject);
		else
		{
			Utils.showToast(AboutSchoolContactUs.this, "Unable to fetch data, kindly enable internet settings!");
		}
	}


	private void toa()
	{
		System.out.println("aaa");
	}


	@Override
	protected void onResume() {
		super.onResume();
		// initilizeMap();
	}

	private void initilizeMap() {



		// check if map is created successfully or not

	}

	void setCustomMarkerOnePosition() {
		customMarkerOne = new CustomMarker("markerOne", 40.7102747, -73.9945297);
		addMarker(customMarkerOne);
	}

	void setCustomMarkerTwoPosition() {
		customMarkerTwo = new CustomMarker("markerTwo", 43.7297251, -74.0675716);
		addMarker(customMarkerTwo);
	}

	public void startAnimation(View v) {
		animateMarker(customMarkerOne, new LatLng(40.0675716, 40.7297251));
	}

	public void zoomToMarkers(View v) {
		zoomAnimateLevelToFitMarkers(120);
	}

	public void animateBack(View v) {
		animateMarker(customMarkerOne, new LatLng(32.0675716, 27.7297251));
	}

	public void initializeUiSettings() {
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(false);
		googleMap.getUiSettings().setTiltGesturesEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
	}

	public void initializeMapLocationSettings() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		googleMap.setMyLocationEnabled(true);
	}

	public void initializeMapTraffic() {
		googleMap.setTrafficEnabled(true);
	}

	public void initializeMapType() {
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	}


	public void initializeMapViewSettings() {
		googleMap.setIndoorEnabled(true);
		googleMap.setBuildingsEnabled(false);
	}


	//this is method to help us set up a Marker that stores the Markers we want to plot on the map
	public void setUpMarkersHashMap() {
		if (markersHashMap == null) {
			markersHashMap = new HashMap();
		}
	}

	//this is method to help us add a Marker into the hashmap that stores the Markers
	public void addMarkerToHashMap(CustomMarker customMarker, Marker marker) {
		setUpMarkersHashMap();
		markersHashMap.put(customMarker, marker);
	}

	//this is method to help us find a Marker that is stored into the hashmap
	public Marker findMarker(CustomMarker customMarker) {
		iter = markersHashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			CustomMarker key = (CustomMarker) mEntry.getKey();
			if (customMarker.getCustomMarkerId().equals(key.getCustomMarkerId())) {
				Marker value = (Marker) mEntry.getValue();
				return value;
			}
		}
		return null;
	}


	//this is method to help us add a Marker to the map
	public void addMarker(CustomMarker customMarker) {

		if(newMark!=null)
		{
			newMark.remove();
		}
		MarkerOptions markerOption = new MarkerOptions().position(
				new LatLng(customMarker.getCustomMarkerLatitude(), customMarker.getCustomMarkerLongitude())).title("Bus"+j).icon(
				BitmapDescriptorFactory.defaultMarker());

		newMark = googleMap.addMarker(markerOption);
		addMarkerToHashMap(customMarker, newMark);
	}

	//this is method to help us remove a Marker
	public void removeMarker(CustomMarker customMarker) {
		if (markersHashMap != null) {
			if (findMarker(customMarker) != null) {
				findMarker(customMarker).remove();
				markersHashMap.remove(customMarker);
			}
		}
	}

	//this is method to help us fit the Markers into specific bounds for camera position
	public void zoomAnimateLevelToFitMarkers(int padding) {
		LatLngBounds.Builder b = new LatLngBounds.Builder();
		iter = markersHashMap.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			CustomMarker key = (CustomMarker) mEntry.getKey();
			LatLng ll = new LatLng(key.getCustomMarkerLatitude(), key.getCustomMarkerLongitude());
			b.include(ll);
		}
		LatLngBounds bounds = b.build();

		// Change the padding as per needed
		cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		googleMap.animateCamera(cu);
	}

	//this is method to help us move a Marker.
	public void moveMarker(CustomMarker customMarker, LatLng latlng) {
		if (findMarker(customMarker) != null) {
			findMarker(customMarker).setPosition(latlng);
			customMarker.setCustomMarkerLatitude(latlng.latitude);
			customMarker.setCustomMarkerLongitude(latlng.longitude);
		}
	}

	//this is method to animate the Marker. There are flavours for all Android versions
	public void animateMarker(CustomMarker customMarker, LatLng latlng) {
		if (findMarker(customMarker) != null) {

			LatLngInterpolator latlonInter = new LatLngInterpolator.LinearFixed();
			latlonInter.interpolate(20,
					new LatLng(customMarker.getCustomMarkerLatitude(), customMarker.getCustomMarkerLongitude()), latlng);

			customMarker.setCustomMarkerLatitude(latlng.latitude);
			customMarker.setCustomMarkerLongitude(latlng.longitude);

			if (android.os.Build.VERSION.SDK_INT >= 14) {
				MarkerAnimation.animateMarkerToICS(findMarker(customMarker), new LatLng(customMarker.getCustomMarkerLatitude(),
						customMarker.getCustomMarkerLongitude()), latlonInter);
			} else if (android.os.Build.VERSION.SDK_INT >= 11) {
				MarkerAnimation.animateMarkerToHC(findMarker(customMarker), new LatLng(customMarker.getCustomMarkerLatitude(),
						customMarker.getCustomMarkerLongitude()), latlonInter);
			} else {
				MarkerAnimation.animateMarkerToGB(findMarker(customMarker), new LatLng(customMarker.getCustomMarkerLatitude(),
						customMarker.getCustomMarkerLongitude()), latlonInter);
			}
		}
	}


	protected void getUserBusList()
	{
		//setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.USER_BUS_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
						Utils.showToast(AboutSchoolContactUs.this,"No Records Found");
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(AboutSchoolContactUs.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("User_details"))
					{
						userArray= new JSONObject(response).getJSONArray("User_details");


						if(null!=userArray && userArray.length()>=0)
						{
							Cache.Entry e = new Cache.Entry();
							e.data = userArray.toString().getBytes();
							VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_CLASS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId,e);
							Calendar c = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String strDate = sdf.format(c.getTime());
							String hours = strDate.substring(11,13);
							Log.d("ddd",""+hours);

							int hours1 = Integer.parseInt(hours);

							if(hours1>=11)
							{
                                busNumber = userArray.getJSONObject(0).getString("eveningBus");
								routeNo = userArray.getJSONObject(0).getString("eveningRoute");
								getChairmanStudentLeaveList9();
							}

							else
							{
								busNumber = userArray.getJSONObject(0).getString("morningBus");
								routeNo = userArray.getJSONObject(0).getString("morningRoute");
								getChairmanStudentLeaveList9();
							}



						}
					}
					else
						Utils.showToast(AboutSchoolContactUs.this, "Error Fetching Response");


				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(AboutSchoolContactUs.this, "Error fetching modules! Please try after sometime.");

				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");

			}
		})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(AboutSchoolContactUs.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("ins_id",Preferences.getInstance().institutionId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("stu_id",Preferences.getInstance().studentId);
				//params.put("u_id",Preferences.getInstance().userId);
				//params.put("cls_id", classId);
				//params.put("sec_id", sectionId);
				//params.put("crr_date",currentDate);
				params.put("device_id", Preferences.getInstance().deviceId);
				return params;
			}};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(AboutSchoolContactUs.this))
			queue.add(requestObject);
		else
		{
			Utils.showToast(AboutSchoolContactUs.this, "Unable to fetch data, kindly enable internet settings!");
		}
	}


	protected void getChairmanStudentLeaveList9()
	{
		//setSupportProgressBarIndeterminateVisibility(true);

		RequestQueue queue = VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ADMIN_BUS_LIST_MAP+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId;/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);

				try
				{
					googleMap.clear();
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(AboutSchoolContactUs.this,"No Records Found");
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(AboutSchoolContactUs.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("VehicleList"))
					{
						mapsArray= new JSONObject(response).getJSONArray("VehicleList");
						if(null!=mapsArray && mapsArray.length()>=0) {
							Cache.Entry e = new Cache.Entry();
							e.data = mapsArray.toString().getBytes();
							VolleySingleton.getInstance(AboutSchoolContactUs.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST_MAP + "?ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId, e);


							CustomMarker[] customMarker = new CustomMarker[2];
							Marker[] newMarker = new Marker[2];


							for (j = 0; j < mapsArray.length(); j++) {
								if (mapsArray.getJSONObject(j).getString("bus_no").matches(busNumber)) {
									count = j;
								}
							}


							customMarker[1] = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(count).getString("latitude")), Double.parseDouble(mapsArray.getJSONObject(count).getString("longitude")));
							//addMarker(customMarker[i]);
							if (newMarker[1] != null) {
								newMarker[1].remove();
							}
							MarkerOptions markerOption = new MarkerOptions().position(
									new LatLng(customMarker[1].getCustomMarkerLatitude(), customMarker[1].getCustomMarkerLongitude())).title(mapsArray.getJSONObject(count).getString("bus_no")+"("+routeNo+")").snippet("Ignition: " + mapsArray.getJSONObject(count).getString("ignition")).icon(
									BitmapDescriptorFactory.fromResource(R.drawable.gpsbus));

							newMarker[1] = googleMap.addMarker(markerOption);
							addMarkerToHashMap(customMarker[1], newMarker[1]);

							//k=1;


							LatLngBounds.Builder b = new LatLngBounds.Builder();
							iter = markersHashMap.entrySet().iterator();

							while (iter.hasNext()) {
								Map.Entry mEntry = (Map.Entry) iter.next();
								CustomMarker key = (CustomMarker) mEntry.getKey();
								LatLng ll = new LatLng(key.getCustomMarkerLatitude(), key.getCustomMarkerLongitude());
								b.include(ll);
							}
							LatLngBounds bounds = b.build();

							// Change the padding as per needed
							cu = CameraUpdateFactory.newLatLngBounds(bounds, 21);
							googleMap.moveCamera(cu);
							googleMap.animateCamera(cu);
						}
					}
					else
						Utils.showToast(AboutSchoolContactUs.this, "Error Fetching Response");
					//setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(AboutSchoolContactUs.this, "Error fetching modules! Please try after sometime.");
					//setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
				//setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(ChairmanStudentLeaves.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("cls_id", classId);
				//params.put("sec_id", sectionId);
				//params.put("crr_date",currentDate);
				params.put("device_id", Preferences.getInstance().deviceId);
				return params;
			}*/};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(AboutSchoolContactUs.this))
			queue.add(requestObject);
		else
		{
			Utils.showToast(AboutSchoolContactUs.this, "Unable to fetch data, kindly enable internet settings!");
		}
	}


	@Override
	public void onMapReady(GoogleMap googleMap1) {

		googleMap = googleMap1;

		if (googleMap == null) {
			Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		}

		(findViewById(R.id.mapFragment)).getViewTreeObserver().addOnGlobalLayoutListener(
				new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (android.os.Build.VERSION.SDK_INT >= 16) {
							(findViewById(R.id.mapFragment)).getViewTreeObserver().removeOnGlobalLayoutListener(this);
						} else {
							(findViewById(R.id.mapFragment)).getViewTreeObserver().removeGlobalOnLayoutListener(this);
						}
						//setCustomMarkerOnePosition();
						//setCustomMarkerTwoPosition();
					}
				});

		if(Build.VERSION.SDK_INT>=19) {

			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.5937, 78.9629), 11.5f), 4500, null);
		}

		else
		{
			Log.d("kkk","llll");
		}

		try {
			// Loading map
			//initilizeMap();
			initializeUiSettings();
			setUpMarkersHashMap();
			initializeMapLocationSettings();
			initializeMapTraffic();
			initializeMapType();
			initializeMapViewSettings();

			//removeMarker(customMarkerOne);
			//zoomAnimateLevelToFitMarkers(120);


		} catch (Exception e1) {
			e1.printStackTrace();
		}



		int delay = 10000; // delay for 10 sec.
		int period = 60000; // repeat every 10 sec.
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			public void run() {
				//System.out.println("lkjh"); // display the data

				if (Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6")) {

					getUserBusList();

				}

				//googleMap.clear();
				else if (Preferences.getInstance().userRoleId.matches("4")) {
					getChairmanStudentLeaveList1();
				}


				else
				if(Preferences.getInstance().userRoleId.matches("3") || Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8"))
				{
					queues = getIntent().getStringExtra("array");
					myList = new ArrayList<String>(Arrays.asList(queues.split(",")));
					//String my = myList.toString();

					queues1 = getIntent().getStringExtra("array1");
					myList1 = new ArrayList<String>(Arrays.asList(queues1.split(",")));

				 /*System.out.println("array"+my);
				 System.out.print("jhg"+myList.get(1));
				 System.out.print("kjh"+myList.get(0));*/
					//Utils.showToast(getApplicationContext(),my);
					getChairmanStudentLeaveList2();
					//System.out.print("hhhh"+Preferences.getInstance().busNumber.toString());
				}

				else
				{
					getChairmanStudentLeaveList();
				}




			}
		}, delay, period);


	}
}