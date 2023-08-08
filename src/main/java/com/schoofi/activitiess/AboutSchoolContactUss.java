package com.schoofi.activitiess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Cache.Entry;
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
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CircleTransform;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import android.*;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import utils.CustomMarker;
import utils.LatLngInterpolator;
import utils.MarkerAnimation;

public class AboutSchoolContactUss extends FragmentsActivity implements OnMapReadyCallback {

    private String url;
    private JSONArray imageArray;
    private ImageView schoolLogo,call;
    private TextView title,schoolAddress1,schoolAddress2,email,tel,schoolAddress;
    private String telephone;
    private GoogleMap googleMap;
    private HashMap markersHashMap;
    private Iterator<Map.Entry> iter;
    private CameraUpdate cu;
    Marker newMark;
    private CustomMarker customMarkerOne, customMarkerTwo;
    int j=0;
    int count=0;
    ArrayList<String> myList;

    JSONArray mapsArray;
    String queues;
    int k=0;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("About School ContactUss");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.about_school_contact_uss);

        title = (TextView) findViewById(R.id.text_school_full_name);
        schoolAddress = (TextView) findViewById(R.id.text_school_address);
        schoolLogo = (ImageView) findViewById(R.id.img_school_logo);
        schoolAddress1 = (TextView) findViewById(R.id.text_school_address1);
        schoolAddress2 = (TextView) findViewById(R.id.text_school_address2);
        tel = (TextView) findViewById(R.id.text_school_tel);
        email = (TextView) findViewById(R.id.text_school_email);
        call = (ImageView) findViewById(R.id.img_call_school);
        Preferences.getInstance().loadPreference(getApplicationContext());
        getFeedbackReplyList();



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about_school_contact_us, menu);
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

    private void getFeedbackReplyList()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();


        url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_IMAGES+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId;

        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                final JSONObject responseObject;
               // System.out.println(response);
               // System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Log.d("kkk", "kkkk");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("school"))
                    {
                        imageArray= new JSONObject(response).getJSONArray("school");
                        if(null!=imageArray && imageArray.length()>=0)
                        {
                            Entry e = new Entry();
                            e.data = imageArray.toString().getBytes();

                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_IMAGES+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId,e);
                            //image = responseObject.getJSONArray("school").getJSONObject(0).getString("photo_gallery");
                            //Utils.showToast(getApplicationContext(), ""+image);

                            Picasso.with(getApplicationContext()).load(AppConstants.SERVER_URLS.SERVER_URL+responseObject.getJSONArray("school").getJSONObject(0).getString("school_logo")).placeholder(R.drawable.schoollogo).error(R.drawable.schoollogo).transform(new CircleTransform()).into(schoolLogo);
                            title.setText(responseObject.getJSONArray("school").getJSONObject(0).getString("school_name"));
                            schoolAddress.setText(responseObject.getJSONArray("school").getJSONObject(0).getString("school_name")+",");
                            String addressa = responseObject.getJSONArray("school").getJSONObject(0).getString("state");
                            if(addressa.matches("null") || addressa.matches(""))
                            {
                                schoolAddress1.setText(responseObject.getJSONArray("school").getJSONObject(0).getString("school_address")+","+responseObject.getJSONArray("school").getJSONObject(0).getString("add2"));
                                schoolAddress2.setText(responseObject.getJSONArray("school").getJSONObject(0).getString("city")+"-"+responseObject.getJSONArray("school").getJSONObject(0).getString("zipcode"));
                            }

                            else
                            {
                                schoolAddress1.setText(responseObject.getJSONArray("school").getJSONObject(0).getString("school_address")+","+responseObject.getJSONArray("school").getJSONObject(0).getString("add2")+","+responseObject.getJSONArray("school").getJSONObject(0).getString("city"));
                                schoolAddress2.setText(responseObject.getJSONArray("school").getJSONObject(0).getString("state")+"-"+responseObject.getJSONArray("school").getJSONObject(0).getString("zipcode"));

                            }

                            String tele = responseObject.getJSONArray("school").getJSONObject(0).getString("phone2");
                            if(tele.matches("null") || tele.matches(""))
                            {
                                tel.setText("Tel: "+responseObject.getJSONArray("school").getJSONObject(0).getString("school_phone_no"));
                            }

                            else
                            {
                                tel.setText("Tel: "+responseObject.getJSONArray("school").getJSONObject(0).getString("school_phone_no")+","+responseObject.getJSONArray("school").getJSONObject(0).getString("phone2"));

                            }

                            String emails = responseObject.getJSONArray("school").getJSONObject(0).getString("email_id2");

                            if(emails.matches("null") || emails.matches(""))
                            {
                                email.setText("email: "+responseObject.getJSONArray("school").getJSONObject(0).getString("school_email_id"));
                            }

                            else
                            {
                                email.setText("email: "+responseObject.getJSONArray("school").getJSONObject(0).getString("school_email_id")+","+responseObject.getJSONArray("school").getJSONObject(0).getString("email_id2"));
                            }

                            telephone = responseObject.getJSONArray("school").getJSONObject(0).getString("school_phone_no");

                        }

                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    //setSupportProgressBarIndeterminateVisibility(false);

                    call.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub



                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+telephone));
                            startActivity(intent);



                        }
                    });
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    //setSupportProgressBarIndeterminateVisibility(false);
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
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
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
                new LatLng(28.4744, 77.5040), 11.5f), 4500, null);

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

        CustomMarker[] customMarker = new CustomMarker[2];
        Marker[] newMarker = new Marker[2];










        Preferences.getInstance().loadPreference(getApplicationContext());

        Log.d("hhhhjjj",Preferences.getInstance().schoolLattitude+","+Preferences.getInstance().schoolLongitude);

        customMarker[1] = new CustomMarker("marker", Double.parseDouble(Preferences.getInstance().schoolLattitude), Double.parseDouble(Preferences.getInstance().schoolLongitude));
        //addMarker(customMarker[i]);
        if(newMarker[1]!=null)
        {
            newMarker[1].remove();
        }
        MarkerOptions markerOption = new MarkerOptions().position(
                new LatLng(customMarker[1].getCustomMarkerLatitude(), customMarker[1].getCustomMarkerLongitude())).icon(
                BitmapDescriptorFactory.defaultMarker());

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
        cu = CameraUpdateFactory.newLatLngBounds(bounds, 12);
        //googleMap.moveCamera(cu);
        //googleMap.animateCamera(cu);

    }
}
