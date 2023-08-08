package com.schoofi.activitiess;

import android.content.pm.PackageManager;
import android.graphics.Color;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import utils.CustomMarker;
import utils.LatLngInterpolator;
import utils.MarkerAnimation;


public class AllBussesMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private HashMap markersHashMap;
    private Iterator<Map.Entry> iter;
    private CameraUpdate cu;
    Marker newMark;
    private CustomMarker customMarkerOne, customMarkerTwo;
    JSONArray mapsArray;
    String busNumber,routeNumber;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    HorizontalCalendar horizontalCalendar;
    String selectedDate1,selectedDate2;
    String dat;
    private Polyline pl;
    private TextView totalTimeTaken,totalDistanceTravelled;
    int count=0;
    private Button refresh;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bus_tracking);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -12);

        totalDistanceTravelled = (TextView) findViewById(R.id.text_total_questions);
        totalTimeTaken = (TextView) findViewById(R.id.text_total_time_taken);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)
                .dayFormat("EEE")
                .dayNumberFormat("dd")
                .textColor(Color.LTGRAY, Color.WHITE)
                .selectedDateBackground(Color.TRANSPARENT)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date0, int position) {

                dat = new SimpleDateFormat("yyyy-MM-dd").format(date0);
                selectedDate1 = (dat);
                selectedDate2 = new SimpleDateFormat("dd-MMM-yyyy").format(date0);
                date = dat;


            }

        });



        //googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).getMap();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        //getChairmanStudentLeaveList3();

        linearLayout = (LinearLayout) findViewById(R.id.linear);
        refresh = (Button) findViewById(R.id.btn_refresh);

        Preferences.getInstance().loadPreference(getApplicationContext());

        if(Preferences.getInstance().userRoleId.matches("3")|| Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8"))
        {
            linearLayout.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChairmanStudentLeaveList3();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getChairmanStudentLeaveList3();
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

        if(newMark!=null)
        {
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

    private void toa()
    {
        System.out.println("aaa");
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


       /* int delay = 10000; // delay for 10 sec.
        int period = 30000; // repeat every 10 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run() {



                getChairmanStudentLeaveList3();


            }
        }, delay, period);*/

    }


    protected void getChairmanStudentLeaveList3() {
        //setSupportProgressBarIndeterminateVisibility(true);

        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALL_BUS_MAP_LIST;/* + "?ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId+"&route_number="+routeNumber+"&date="+date+"&role_id="+Preferences.getInstance().userRoleId+"&stu_id="+Preferences.getInstance().studentId;*//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/
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
                            MarkerOptions[] markerOption = new MarkerOptions[k];

                            for(int j=0;j<k;j++) {

                                customMarker[j] = new CustomMarker("marker", Double.parseDouble(mapsArray.getJSONObject(j).getString("latitude")), Double.parseDouble(mapsArray.getJSONObject(j).getString("longitude")));
                                //addMarker(customMarker[i]);
                                if (newMarker[j] != null) {
                                    newMarker[j].remove();
                                }
                                 markerOption[j] = new MarkerOptions().position(
                                        new LatLng(customMarker[j].getCustomMarkerLatitude(), customMarker[j].getCustomMarkerLongitude())).title(mapsArray.getJSONObject(j).getString("bus_number")+"("+mapsArray.getJSONObject(j).getString("route_number")+")").snippet(mapsArray.getJSONObject(j).getString("ignition")+"-"+mapsArray.getJSONObject(j).getString("crr_time")).icon(
                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                                newMarker[j] = googleMap.addMarker(markerOption[j]);
                                newMarker[j].showInfoWindow();

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
                Preferences.getInstance().loadPreference(getApplicationContext());
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("token", Preferences.getInstance().token);

                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("date",date);


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
}
