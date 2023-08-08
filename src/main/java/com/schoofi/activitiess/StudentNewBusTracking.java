package com.schoofi.activitiess;

import android.content.pm.PackageManager;
import android.graphics.Color;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import utils.CustomMarker;
import utils.LatLngInterpolator;
import utils.MarkerAnimation;

public class StudentNewBusTracking extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private HashMap markersHashMap;
    private Iterator<Map.Entry> iter;
    private CameraUpdate cu;
    Marker newMark;
    private CustomMarker customMarkerOne, customMarkerTwo;
    JSONArray mapsArray;
    String busNumber, routeNumber;
    // String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String date;

    HorizontalCalendar horizontalCalendar;
    String selectedDate1, selectedDate2;
    String dat;
    private Polyline pl;
    private TextView totalTimeTaken, totalDistanceTravelled;
    int count = 0;
    double distanceTravelled=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tracker t = ((SchoofiApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Student New Bus Tracking");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_student_new_bus_tracking);

        Preferences.getInstance().loadPreference(getApplicationContext());
        if (Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6")) {
            routeNumber = getIntent().getStringExtra("bus_route");
            date = getIntent().getStringExtra("date");
        } else {

            busNumber = getIntent().getStringExtra("bus_no");
            routeNumber = getIntent().getStringExtra("bus_route");
            date = getIntent().getStringExtra("date");
        }

        totalDistanceTravelled = (TextView) findViewById(R.id.text_total_questions);
        totalTimeTaken = (TextView) findViewById(R.id.text_total_time_taken);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        getChairmanStudentLeaveList3();


    }

    @Override
    protected void onResume() {
        super.onResume();

        getChairmanStudentLeaveList3();

        Preferences.getInstance().loadPreference(getApplicationContext());
        if (Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6")) {
            getChairmanStudentLeaveList1();
        } else {

            getChairmanStudentLeaveList2();
        }
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        if (newMark != null) {
            newMark.remove();
        }
        MarkerOptions markerOption = new MarkerOptions().position(
                new LatLng(customMarker.getCustomMarkerLatitude(), customMarker.getCustomMarkerLongitude())).title("Bus").icon(
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

    protected void getChairmanStudentLeaveList2() {
        //setSupportProgressBarIndeterminateVisibility(true);

        RequestQueue queue = VolleySingleton.getInstance(StudentNewBusTracking.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST_MAP; /*+ "?ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId+"&bus_number="+busNumber+"&route_number="+routeNumber+"&date="+date+"&role_id="+Preferences.getInstance().userRoleId*/
        ;/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                // System.out.println(url);

                try {
                    googleMap.clear();
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        // Utils.showToast(getApplicationContext(), "No Bus Tracking Available");
                        googleMap.clear();
                        CustomMarker customMarker;
                        Marker marker = null;

                        Preferences.getInstance().loadPreference(getApplicationContext());

                        customMarker = new CustomMarker("marker", Double.parseDouble(Preferences.getInstance().schoolLattitude), Double.parseDouble(Preferences.getInstance().schoolLongitude));
                        //addMarker(customMarker[i]);
                        if (marker != null) {
                            marker.remove();
                        }

                        //String markerOneName = mapsArray.getJSONObject(k).getString("bus_number")+"("+mapsArray.getJSONObject(k).getString("route_number")+")";
                        MarkerOptions markerOption = new MarkerOptions().position(
                                new LatLng(customMarker.getCustomMarkerLatitude(), customMarker.getCustomMarkerLongitude())).title(Preferences.getInstance().schoolName).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        marker = googleMap.addMarker(markerOption);
                        addMarkerToHashMap(customMarker, marker);
                        LatLngBounds.Builder b = new LatLngBounds.Builder();
                        iter = markersHashMap.entrySet().iterator();

                        while (iter.hasNext()) {
                            Map.Entry mEntry = (Map.Entry) iter.next();
                            CustomMarker key = (CustomMarker) mEntry.getKey();
                            LatLng ll = new LatLng(key.getCustomMarkerLatitude(), key.getCustomMarkerLongitude());
                            b.include(ll);
                        }
                        LatLngBounds bounds = b.build();

                        //Change the padding as per needed
                        //cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
                        //googleMap.moveCamera(cu);
                        //googleMap.animateCamera(cu);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(Preferences.getInstance().schoolLattitude), Double.parseDouble(Preferences.getInstance().schoolLongitude)), 14.0f));
                        count = 1;

                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        googleMap.clear();
                        count = 1;
                    } else if (responseObject.has("responseObject")) {
                        mapsArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != mapsArray && mapsArray.length() >= 0) {
                            /*Cache.Entry e = new Cache.Entry();
                            e.data = mapsArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentNewBusTracking.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST_MAP + "?ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId+"&bus_number="+busNumber+"&route_number="+routeNumber+"&date="+date+"&role_id="+Preferences.getInstance().userRoleId, e);

*/

                            int l = mapsArray.length();

                            float distance = 0, time = 0;
                            int k = 0;


                            try {

                                PolylineOptions options = new PolylineOptions().width(15).color(Color.BLUE).geodesic(true);

                                for (int z = 0; z < l; z++) {
                                    LatLng point = new LatLng(
                                            Double.parseDouble(mapsArray.getJSONObject(z).getString("latitude")),
                                            Double.parseDouble(mapsArray.getJSONObject(z).getString("longitude")));

                                    // Utils.showToast(getApplicationContext(),"kk");
                                    options.add(point);

                                    try {

                                        distance = distance + Float.parseFloat(mapsArray.getJSONObject(z).getString("distance_travelled"));
                                        time = time + Float.parseFloat(mapsArray.getJSONObject(z).getString("total_time"));

                                    } catch (Exception e3) {
                                        Log.d("Exception", e3.toString());
                                    }

                                    k = z;


                                }

                                pl = googleMap.addPolyline(options);

                                totalDistanceTravelled.setText(mapsArray.getJSONObject(k).getString("distance_travelled") + " Km");
                                int hours = Integer.parseInt(mapsArray.getJSONObject(k).getString("total_time")) / 60; //since both are ints, you get an int
                                int minutes = Integer.parseInt(mapsArray.getJSONObject(k).getString("total_time")) % 60;
                                // System.out.printf("%d:%02d", hours, minutes);
                                totalTimeTaken.setText(hours + " hours and " + minutes + " minutes");

                                /*if(mapsArray.getJSONObject(k).getString("distance_travelled").matches("0") || mapsArray.getJSONObject(k).getString("distance_travelled").matches("") || mapsArray.getJSONObject(k).getString("distance_travelled").matches("null"))
                                {
                                    distance(Double.parseDouble(mapsArray.getJSONObject(k).getString("latitude")),Double.parseDouble(mapsArray.getJSONObject(k).getString("longitude")),Double.parseDouble(mapsArray.getJSONObject(0).getString("latitude")),Double.parseDouble(mapsArray.getJSONObject(0).getString("longitude")));
                                    totalDistanceTravelled.setText(String.valueOf(distanceTravelled));
                                }*/

                                CustomMarker customMarker1, customMarker2, customMarker3;
                                Marker newMarker1 = null, newMarker2 = null, newMarker3 = null;


                                String currentTime1 = "", currentTime2 = "";

                                try {
                                    String _24HourTime = mapsArray.getJSONObject(k).getString("crr_time");
                                    String _24HourTime1 = mapsArray.getJSONObject(0).getString("crr_time");
                                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("hh:mm");
                                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                    Date _24HourDt = _24HourSDF.parse(_24HourTime);
                                    Date _24HourDt1 = _24HourSDF.parse(_24HourTime1);
                                    System.out.println(_24HourDt);
                                    System.out.println(_12HourSDF.format(_24HourDt));
                                    currentTime1 = _12HourSDF.format(_24HourDt);
                                    currentTime2 = _12HourSDF.format(_24HourDt1);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                                customMarker1 = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(k).getString("latitude")), Double.parseDouble(mapsArray.getJSONObject(k).getString("longitude")));
                                //addMarker(customMarker[i]);
                                if (newMarker1 != null) {
                                    newMarker1.remove();
                                }

                                String markerOneName = mapsArray.getJSONObject(k).getString("bus_number") + "(" + mapsArray.getJSONObject(k).getString("route_number") + ")";
                                MarkerOptions markerOption = new MarkerOptions().position(
                                        new LatLng(customMarker1.getCustomMarkerLatitude(), customMarker1.getCustomMarkerLongitude())).title(markerOneName).snippet(currentTime1).icon(
                                        BitmapDescriptorFactory.fromResource(R.drawable.gpsbus));

                                newMarker1 = googleMap.addMarker(markerOption);
                                addMarkerToHashMap(customMarker1, newMarker1);


                                customMarker2 = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(0).getString("latitude")), Double.parseDouble(mapsArray.getJSONObject(0).getString("longitude")));
                                //addMarker(customMarker[i]);
                                if (newMarker2 != null) {
                                    newMarker2.remove();
                                }
                                MarkerOptions markerOption1 = new MarkerOptions().position(
                                        new LatLng(customMarker2.getCustomMarkerLatitude(), customMarker2.getCustomMarkerLongitude())).title(mapsArray.getJSONObject(0).getString("bus_number") + "(" + mapsArray.getJSONObject(0).getString("route_number") + ")").snippet(currentTime2).icon(
                                        BitmapDescriptorFactory.fromResource(R.drawable.gpsbus));

                                newMarker2 = googleMap.addMarker(markerOption1);
                                addMarkerToHashMap(customMarker2, newMarker2);

                                if (mapsArray.getJSONObject(0).getString("student_lat").matches("") || mapsArray.getJSONObject(0).getString("student_lat").matches("null") || mapsArray.getJSONObject(0).getString("student_long").matches("") || mapsArray.getJSONObject(0).getString("student_long").matches("null")) {

                                } else {
                                    customMarker3 = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(0).getString("student_lat")), Double.parseDouble(mapsArray.getJSONObject(0).getString("student_long")));
                                    //addMarker(customMarker[i]);
                                    if (newMarker3 != null) {
                                        newMarker3.remove();
                                    }
                                    MarkerOptions markerOption2 = new MarkerOptions().position(
                                            new LatLng(customMarker3.getCustomMarkerLatitude(), customMarker3.getCustomMarkerLongitude())).title("Your Marked Location!").snippet(mapsArray.getJSONObject(0).getString("student_address")).icon(
                                            BitmapDescriptorFactory.defaultMarker());

                                    newMarker3 = googleMap.addMarker(markerOption2);
                                    addMarkerToHashMap(customMarker3, newMarker3);
                                }

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
                                /* cu = CameraUpdateFactory.newLatLngBounds(bounds, 12);
                                googleMap.moveCamera(cu);
                                googleMap.animateCamera(cu);*/


                                List<LatLng> points = options.getPoints(); // route is instance of PolylineOptions

                                LatLngBounds.Builder bc = new LatLngBounds.Builder();

                                for (LatLng item : points) {
                                    bc.include(item);
                                }

                                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));


                            } catch (JSONException e3) {
                                Log.d("kk", String.valueOf(e3));
                            }

                        } else {

                        }
                    } else {
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                        googleMap.clear();
                        count = 1;
                    }
                    //setSuppo
                    // rtProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    googleMap.clear();
                    count = 1;
                    //setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                googleMap.clear();
                count = 1;
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("token", Preferences.getInstance().token);

                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("route_number", routeNumber);
                params.put("bus_number", busNumber);
                params.put("date", date);
                params.put("role_id", Preferences.getInstance().userRoleId);
                return params;
            }
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
            googleMap.clear();
            count = 1;
        }
    }


    protected void getChairmanStudentLeaveList1() {
        //setSupportProgressBarIndeterminateVisibility(true);

        RequestQueue queue = VolleySingleton.getInstance(StudentNewBusTracking.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST_MAP;/* + "?ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId+"&route_number="+routeNumber+"&date="+date+"&role_id="+Preferences.getInstance().userRoleId+"&stu_id="+Preferences.getInstance().studentId;*//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/
        final StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                // System.out.println(url);

                try {
                    googleMap.clear();
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        //Utils.showToast(getApplicationContext(), "No Bus Tracking Available");
                        Log.d("kkk", "jjj");
                        googleMap.clear();
                        CustomMarker customMarker;
                        Marker marker = null;

                        Preferences.getInstance().loadPreference(getApplicationContext());

                        customMarker = new CustomMarker("marker", Double.parseDouble(Preferences.getInstance().schoolLattitude), Double.parseDouble(Preferences.getInstance().schoolLongitude));
                        //addMarker(customMarker[i]);
                        if (marker != null) {
                            marker.remove();
                        }

                        //String markerOneName = mapsArray.getJSONObject(k).getString("bus_number")+"("+mapsArray.getJSONObject(k).getString("route_number")+")";
                        MarkerOptions markerOption = new MarkerOptions().position(
                                new LatLng(customMarker.getCustomMarkerLatitude(), customMarker.getCustomMarkerLongitude())).title(Preferences.getInstance().schoolName).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        marker = googleMap.addMarker(markerOption);
                        addMarkerToHashMap(customMarker, marker);
                        LatLngBounds.Builder b = new LatLngBounds.Builder();
                        iter = markersHashMap.entrySet().iterator();

                        while (iter.hasNext()) {
                            Map.Entry mEntry = (Map.Entry) iter.next();
                            CustomMarker key = (CustomMarker) mEntry.getKey();
                            LatLng ll = new LatLng(key.getCustomMarkerLatitude(), key.getCustomMarkerLongitude());
                            b.include(ll);
                        }
                        LatLngBounds bounds = b.build();

                        //Change the padding as per needed
                        //cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
                        //googleMap.moveCamera(cu);
                        //googleMap.animateCamera(cu);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(Preferences.getInstance().schoolLattitude), Double.parseDouble(Preferences.getInstance().schoolLongitude)), 14.0f));
                        count = 1;
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        googleMap.clear();
                        count = 1;
                    } else if (responseObject.has("responseObject")) {
                        mapsArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != mapsArray && mapsArray.length() >= 0) {
                            /*Cache.Entry e = new Cache.Entry();
                            e.data = mapsArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentNewBusTracking.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST_MAP + "?ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId+"&route_number="+routeNumber+"&date="+date+"&role_id="+Preferences.getInstance().userRoleId+"&stu_id="+Preferences.getInstance().studentId, e);

*/

                            int l = mapsArray.length();

                            float distance = 0, time = 0;
                            int k = 0;


                            try {

                                PolylineOptions options = new PolylineOptions().width(15).color(Color.BLUE).geodesic(true);
                                for (int z = 0; z < l; z++) {
                                    LatLng point = new LatLng(
                                            Double.parseDouble(mapsArray.getJSONObject(z).getString("latitude")),
                                            Double.parseDouble(mapsArray.getJSONObject(z).getString("longitude")));

                                    // Utils.showToast(getApplicationContext(),"kk");
                                    options.add(point);

                                    try {

                                        distance = distance + Float.parseFloat(mapsArray.getJSONObject(z).getString("distance_travelled"));
                                        time = time + Float.parseFloat(mapsArray.getJSONObject(z).getString("total_time"));

                                    } catch (Exception e3) {
                                        Log.d("Exception", e3.toString());
                                    }

                                    k = z;


                                }

                                pl = googleMap.addPolyline(options);
                                totalDistanceTravelled.setText(mapsArray.getJSONObject(k).getString("distance_travelled") + " Km");
                                int hours = Integer.parseInt(mapsArray.getJSONObject(k).getString("total_time")) / 60; //since both are ints, you get an int
                                int minutes = Integer.parseInt(mapsArray.getJSONObject(k).getString("total_time")) % 60;
                                // System.out.printf("%d:%02d", hours, minutes);
                                totalTimeTaken.setText(hours + " hours and " + minutes + " minutes");
                               /* if(mapsArray.getJSONObject(k).getString("distance_travelled").matches("0") || mapsArray.getJSONObject(k).getString("distance_travelled").matches("") || mapsArray.getJSONObject(k).getString("distance_travelled").matches("null"))
                                {
                                    distance(Double.parseDouble(mapsArray.getJSONObject(k).getString("latitude")),Double.parseDouble(mapsArray.getJSONObject(k).getString("longitude")),Double.parseDouble(mapsArray.getJSONObject(0).getString("latitude")),Double.parseDouble(mapsArray.getJSONObject(0).getString("longitude")));
                                    totalDistanceTravelled.setText(String.valueOf(distanceTravelled));
                                }*/


                                CustomMarker customMarker1, customMarker2, customMarker3;
                                Marker newMarker1 = null, newMarker2 = null, newMarker3 = null;


                                String currentTime1 = "", currentTime2 = "";

                                try {
                                    String _24HourTime = mapsArray.getJSONObject(k).getString("crr_time");
                                    String _24HourTime1 = mapsArray.getJSONObject(0).getString("crr_time");
                                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("hh:mm");
                                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                    Date _24HourDt = _24HourSDF.parse(_24HourTime);
                                    Date _24HourDt1 = _24HourSDF.parse(_24HourTime1);
                                    System.out.println(_24HourDt);
                                    System.out.println(_12HourSDF.format(_24HourDt));
                                    currentTime1 = _12HourSDF.format(_24HourDt);
                                    currentTime2 = _12HourSDF.format(_24HourDt1);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                                customMarker1 = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(k).getString("latitude")), Double.parseDouble(mapsArray.getJSONObject(k).getString("longitude")));
                                //addMarker(customMarker[i]);
                                if (newMarker1 != null) {
                                    newMarker1.remove();
                                }

                                String markerOneName = mapsArray.getJSONObject(k).getString("bus_number") + "(" + mapsArray.getJSONObject(k).getString("route_number") + ")";
                                MarkerOptions markerOption = new MarkerOptions().position(
                                        new LatLng(customMarker1.getCustomMarkerLatitude(), customMarker1.getCustomMarkerLongitude())).title(markerOneName).snippet(currentTime1).icon(
                                        BitmapDescriptorFactory.fromResource(R.drawable.gpsbus));

                                newMarker1 = googleMap.addMarker(markerOption);
                                addMarkerToHashMap(customMarker1, newMarker1);


                                customMarker2 = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(0).getString("latitude")), Double.parseDouble(mapsArray.getJSONObject(0).getString("longitude")));
                                //addMarker(customMarker[i]);
                                if (newMarker2 != null) {
                                    newMarker2.remove();
                                }
                                MarkerOptions markerOption1 = new MarkerOptions().position(
                                        new LatLng(customMarker2.getCustomMarkerLatitude(), customMarker2.getCustomMarkerLongitude())).title(mapsArray.getJSONObject(0).getString("bus_number") + "(" + mapsArray.getJSONObject(0).getString("route_number") + ")").snippet(currentTime2).icon(
                                        BitmapDescriptorFactory.fromResource(R.drawable.gpsbus));

                                newMarker2 = googleMap.addMarker(markerOption1);
                                addMarkerToHashMap(customMarker2, newMarker2);


                                if (mapsArray.getJSONObject(0).getString("student_lat").matches("") || mapsArray.getJSONObject(0).getString("student_lat").matches("null") || mapsArray.getJSONObject(0).getString("student_long").matches("") || mapsArray.getJSONObject(0).getString("student_long").matches("null")) {

                                } else {
                                    customMarker3 = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(0).getString("student_lat")), Double.parseDouble(mapsArray.getJSONObject(0).getString("student_long")));
                                    //addMarker(customMarker[i]);
                                    if (newMarker3 != null) {
                                        newMarker3.remove();
                                    }
                                    MarkerOptions markerOption2 = new MarkerOptions().position(
                                            new LatLng(customMarker3.getCustomMarkerLatitude(), customMarker3.getCustomMarkerLongitude())).title("Your Marked Location!").snippet(mapsArray.getJSONObject(0).getString("student_address")).icon(
                                            BitmapDescriptorFactory.defaultMarker());

                                    newMarker3 = googleMap.addMarker(markerOption2);
                                    addMarkerToHashMap(customMarker3, newMarker3);
                                }

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
                                /* cu = CameraUpdateFactory.newLatLngBounds(bounds, 12);
                                googleMap.moveCamera(cu);
                                googleMap.animateCamera(cu);*/


                                List<LatLng> points = options.getPoints(); // route is instance of PolylineOptions

                                LatLngBounds.Builder bc = new LatLngBounds.Builder();

                                for (LatLng item : points) {
                                    bc.include(item);
                                }

                                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));


                            } catch (JSONException e3) {
                                Log.d("kk", String.valueOf(e3));
                            }

                        } else {

                        }
                    } else {
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                        //setSupportProgressBarIndeterminateVisibility(false);
                        googleMap.clear();
                        count = 1;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    googleMap.clear();
                    count = 1;
                    //setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                googleMap.clear();
                count = 1;
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                Preferences.getInstance().loadPreference(StudentNewBusTracking.this);
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("token", Preferences.getInstance().token);

                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("route_number", routeNumber);

                System.out.print(routeNumber + date);
                params.put("stu_id", Preferences.getInstance().studentId);
                params.put("date", date);
                params.put("role_id", Preferences.getInstance().userRoleId);
                return params;
            }
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
            googleMap.clear();
            count = 1;
        }
    }


    private void toa() {
        System.out.println("aaa");
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {

        googleMap = googleMap1;


        //googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).getMap();
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

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(28.4744, 77.5040), 16.0f), 4500, null);

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
        int period = 30000; // repeat every 10 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                if (Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6")) {
                    if (count == 0) {
                        getChairmanStudentLeaveList1();
                    } else {

                    }
                } else {

                    if (count == 0) {
                        getChairmanStudentLeaveList2();
                    } else {

                    }
                }

            }
        }, delay, period);


    }


    protected void getChairmanStudentLeaveList3() {
        //setSupportProgressBarIndeterminateVisibility(true);

        RequestQueue queue = VolleySingleton.getInstance(StudentNewBusTracking.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BUS_STOP_LIST;/* + "?ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId+"&route_number="+routeNumber+"&date="+date+"&role_id="+Preferences.getInstance().userRoleId+"&stu_id="+Preferences.getInstance().studentId;*//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/
        final StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                 System.out.println(response);
                // System.out.println(url);

                try {
                    googleMap.clear();
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("responseObject") && responseObject.getString("responseObject").equals("0")) {
                        //Utils.showToast(getApplicationContext(), "No Bus Tracking Available");
                        /*Log.d("kkk", "jjj");
                        googleMap.clear();
                        CustomMarker customMarker;
                        Marker marker = null;

                        Preferences.getInstance().loadPreference(getApplicationContext());

                        customMarker = new CustomMarker("marker", Double.parseDouble(Preferences.getInstance().schoolLattitude), Double.parseDouble(Preferences.getInstance().schoolLongitude));
                        //addMarker(customMarker[i]);
                        if (marker != null) {
                            marker.remove();
                        }

                        //String markerOneName = mapsArray.getJSONObject(k).getString("bus_number")+"("+mapsArray.getJSONObject(k).getString("route_number")+")";
                        MarkerOptions markerOption = new MarkerOptions().position(
                                new LatLng(customMarker.getCustomMarkerLatitude(), customMarker.getCustomMarkerLongitude())).title(Preferences.getInstance().schoolName).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        marker = googleMap.addMarker(markerOption);
                        addMarkerToHashMap(customMarker, marker);
                        LatLngBounds.Builder b = new LatLngBounds.Builder();
                        iter = markersHashMap.entrySet().iterator();

                        while (iter.hasNext()) {
                            Map.Entry mEntry = (Map.Entry) iter.next();
                            CustomMarker key = (CustomMarker) mEntry.getKey();
                            LatLng ll = new LatLng(key.getCustomMarkerLatitude(), key.getCustomMarkerLongitude());
                            b.include(ll);
                        }
                        LatLngBounds bounds = b.build();

                        //Change the padding as per needed
                        //cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
                        //googleMap.moveCamera(cu);
                        //googleMap.animateCamera(cu);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(Preferences.getInstance().schoolLattitude), Double.parseDouble(Preferences.getInstance().schoolLongitude)), 14.0f));
                        count = 1;*/
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        googleMap.clear();
                        count = 1;
                    } else if (responseObject.has("responseObject")) {
                        mapsArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != mapsArray && mapsArray.length() >= 0) {
                            /*Cache.Entry e = new Cache.Entry();
                            e.data = mapsArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentNewBusTracking.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_LIST_MAP + "?ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId+"&route_number="+routeNumber+"&date="+date+"&role_id="+Preferences.getInstance().userRoleId+"&stu_id="+Preferences.getInstance().studentId, e);

*/
                            int z=0;
                            for(z=0;z<mapsArray.length();z++)
                            {
                                Log.d("kkk","lll");
                            }

                            int k;
                            k=z;
                            CustomMarker[] customMarker = new CustomMarker[k];
                            Marker[] newMarker = new Marker[k];

                            for(int j=0;j<k;j++) {

                                customMarker[j] = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(j).getString("stop_latitude")), Double.parseDouble(mapsArray.getJSONObject(j).getString("stop_longitude")));
                                //addMarker(customMarker[i]);
                                if (newMarker[j] != null) {
                                    newMarker[j].remove();
                                }
                                MarkerOptions markerOption = new MarkerOptions().position(
                                        new LatLng(customMarker[j].getCustomMarkerLatitude(), customMarker[j].getCustomMarkerLongitude())).title(mapsArray.getJSONObject(j).getString("stop_name")).icon(
                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                                newMarker[j] = googleMap.addMarker(markerOption);
                                addMarkerToHashMap(customMarker[j], newMarker[j]);

                                //k=1;
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



                        } else {

                        }
                    } else {
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                        //setSupportProgressBarIndeterminateVisibility(false);
                        googleMap.clear();
                        count = 1;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    googleMap.clear();
                    count = 1;
                    //setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                googleMap.clear();
                count = 1;
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                Preferences.getInstance().loadPreference(StudentNewBusTracking.this);
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("token", Preferences.getInstance().token);

                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("route_name", routeNumber);

               // System.out.print(routeNumber + date);
                //params.put("stu_id", Preferences.getInstance().studentId);
              //  params.put("date", date);
              //  params.put("role_id", Preferences.getInstance().userRoleId);
                return params;
            }
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
            googleMap.clear();
            count = 1;
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        distanceTravelled = dist;
        Utils.showToast(getApplicationContext(),String.valueOf(distanceTravelled));
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
