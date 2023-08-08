package com.schoofi.activitiess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import utils.CustomMarker;
import utils.LatLngInterpolator;
import utils.MarkerAnimation;

public class MarkYourLocation extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener {

    private ImageView back;
    private Button done, useLocation;
    private GoogleMap mMap;

    //To store longitude and latitude from map
    private double longitude;
    private double latitude;
    private EditText address;
    private TextView or;
    int count = 0;
    String lat = "", lon = "";
    private int STORAGE_PERMISSION_CODE = 23;
    String latt = "", longi = "";
    private GoogleMap googleMap;
    private HashMap markersHashMap;
    private Iterator<Map.Entry> iter;
    private CameraUpdate cu;
    private GoogleApiClient googleApiClient;
    Marker newMark;
    private CustomMarker customMarkerOne, customMarkerTwo;
    String address1 = "";
    private TextView lattitudeTextView,longitudeTextView,mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Mark Your Location");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_mark_your_location);

        back = (ImageView) findViewById(R.id.img_back);

        lattitudeTextView = (TextView) findViewById(R.id.txt_lattitude);
        longitudeTextView = (TextView) findViewById(R.id.txt_longitude);
        mark = (TextView) findViewById(R.id.text_mark);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        done = (Button) findViewById(R.id.btn_done);
        useLocation = (Button) findViewById(R.id.btn_mark);
        address = (EditText) findViewById(R.id.edit_address);
        address.setText("Address");

        if (checkAndRequestPermissions()) {


            // Register the listener with the Location Manager to receive location updates

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            LocationManager locationManager =
                    (LocationManager) MarkYourLocation.this.getSystemService(Context.LOCATION_SERVICE);
            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    lat = Double.toString(location.getLatitude());
                    lon = Double.toString(location.getLongitude());
                    //TextView tv = (TextView) findViewById(R.id.txtLoc);
                    //tv.setText("Your Location is:" + lat + "--" + lon);

                    Log.d("kkkf", lat + " " + lon);
                    // Utils.showToast(getApplicationContext(), lat + " " + lon);
                   // GetAddress(lat, lon);
                    getCompleteAddressString(location.getLatitude(),location.getLongitude());
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            if (ActivityCompat.checkSelfPermission(MarkYourLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MarkYourLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // return;

            mark.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getLocationFromAddress1(MarkYourLocation.this, address.getText().toString());
                    mark.setVisibility(View.GONE);
                    hideSoftKeyboard(MarkYourLocation.this);
                }
            });

            /*address.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (event != null) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {
                            if (!event.isShiftPressed()) {
                                // the user is done typing.

                                getLocationFromAddress(MarkYourLocation.this, v.getText().toString());
                                //Utils.showToast(getApplicationContext(),v.getText().toString());


                                return true; // consume.
                            }
                        }
                    }

                    return false; // pass on to other listeners.

                }

            });

            address.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    //getLocationFromAddress(MarkYourLocation.this, s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });*/
            or = (TextView) findViewById(R.id.text_or);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);


            //Initializing googleapi client
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            done.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (address.getText().toString().matches("") || address.getText().toString().matches("null")) {

                        Utils.showToast(getApplicationContext(),"Please fill the location" );
                    }

                    else {
                        getChairmanStudentLeaveList();
                    }


                }
            });

        /*if(checkAndRequestPermissions()) {

            LocationManager locationManager =
                    (LocationManager) MarkYourLocation.this.getSystemService(Context.LOCATION_SERVICE);
            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    lat = Double.toString(location.getLatitude());
                    lon = Double.toString(location.getLongitude());
                    //TextView tv = (TextView) findViewById(R.id.txtLoc);
                    //tv.setText("Your Location is:" + lat + "--" + lon);

                    Log.d("kkkf", lat + " " + lon);

                    // Utils.showToast(getApplicationContext(), lat + " " + lon);
                    GetAddress(lat, lon);

                    googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).getMap();





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

                   // googleMap.getMap


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

                    Log.d("hhhhjjj", Preferences.getInstance().schoolLattitude + "," + Preferences.getInstance().schoolLongitude);
                    customMarker[1] = new CustomMarker("marker", Double.parseDouble(Preferences.getInstance().schoolLattitude), Double.parseDouble(Preferences.getInstance().schoolLongitude));

                    //addMarker(customMarker[i]);
                    if (newMarker[1] != null) {
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

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            if (ActivityCompat.checkSelfPermission(MarkYourLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MarkYourLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
*/

        /*address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mark.setVisibility(View.INVISIBLE);
            }
        });
*/
           /* useLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkAndRequestPermissions()) {

                        count = 1;
                        address.setVisibility(View.INVISIBLE);
                        or.setVisibility(View.INVISIBLE);


                        // Register the listener with the Location Manager to receive location updates

                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        LocationManager locationManager =
                                (LocationManager) MarkYourLocation.this.getSystemService(Context.LOCATION_SERVICE);
                        // Define a listener that responds to location updates
                        LocationListener locationListener = new LocationListener() {
                            public void onLocationChanged(Location location) {
                                // Called when a new location is found by the network location provider.
                                lat = Double.toString(location.getLatitude());
                                lon = Double.toString(location.getLongitude());
                                //TextView tv = (TextView) findViewById(R.id.txtLoc);
                                //tv.setText("Your Location is:" + lat + "--" + lon);

                                Log.d("kkkf", lat + " " + lon);
                                // Utils.showToast(getApplicationContext(), lat + " " + lon);
                                GetAddress(lat, lon);
                            }

                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }

                            public void onProviderEnabled(String provider) {
                            }

                            public void onProviderDisabled(String provider) {
                            }
                        };
                        if (ActivityCompat.checkSelfPermission(MarkYourLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MarkYourLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        return;


                    }
                }
            });*/


        }




    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
                address1 = strReturnedAddress.toString();
                address.setText(strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public String GetAddress(String lat, String lon) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        String ret = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                ret = strReturnedAddress.toString();
                address1 = ret.toString();
                Log.d("loi",ret);
                address.setText(ret);
            } else {
                ret = "No Address returned!";
                Log.d("loi",ret);
                //Utils.showToast(getApplicationContext(), ret + " " + "Please try again!!! ");

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ret = "Can't get Address!";
            Log.d("loi",ret);
        }
        return ret;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

            //Utils.showToast(getApplicationContext(),String .valueOf(p1.latitude)+String.valueOf(p1.longitude));
            latt = String.valueOf(p1.latitude);
            longi = String.valueOf(p1.longitude);
            mMap.clear();
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
            GetAddress(String.valueOf(latitude),String.valueOf(longitude));
            lattitudeTextView.setText("Latitude: "+String.valueOf(latitude));
            longitudeTextView.setText("Longitude: "+String.valueOf(longitude));

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    public LatLng getLocationFromAddress1(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

            //Utils.showToast(getApplicationContext(),String .valueOf(p1.latitude)+String.valueOf(p1.longitude));
            latt = String.valueOf(p1.latitude);
            longi = String.valueOf(p1.longitude);
            mMap.clear();
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
            //GetAddress(String.valueOf(latitude),String.valueOf(longitude));
            lattitudeTextView.setText("Latitude: "+String.valueOf(latitude));
            longitudeTextView.setText("Longitude: "+String.valueOf(longitude));

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    protected void getChairmanStudentLeaveList() {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(MarkYourLocation.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.MARK_LOCATION_URL/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                // System.out.println(response);
                //System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Error in Submitting");
                        // notificationListView.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("1")) {

                        Utils.showToast(getApplicationContext(), "Submitted Successfully");
                        finish();

                    }
                    else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("2")) {

                        Utils.showToast(getApplicationContext(), "No Bus data found! Please contact your school to get your bus data updated!");
                        finish();

                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error in Submitting");
                    setSupportProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                /*;
                params.put("sec_id",Preferences.getInstance().studentSectionId);

                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);

                params.put("cls_id", Preferences.getInstance().studentClassId);*/
                params.put("stu_id", Preferences.getInstance().studentId);
                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);


                    params.put("latitude", lat);
                    params.put("longitude", lon);
                    params.put("address", address1);
                    Log.d("kk",address1);


                    //params.put("address", address.getText().toString());



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
            Utils.showToast(getApplicationContext(), "Unable to upload data, kindly enable internet settings!");
        }
    }


    private void toa() {
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


    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                Log.i("centerLat", String.valueOf(cameraPosition.target.latitude));

                Log.i("centerLong", String.valueOf(cameraPosition.target.longitude));
            }
        });
    }*/

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        //Creating a location object
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
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
            GetAddress(String.valueOf(latitude),String.valueOf(longitude));
            lattitudeTextView.setText("Latitude: "+String.valueOf(latitude));
            longitudeTextView.setText("Longitude: "+String.valueOf(longitude));
        }
    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //Displaying current coordinates in toast
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                String newLat="",newLong="";

                newLat = String.valueOf(cameraPosition.target.latitude);
                newLong = String.valueOf(cameraPosition.target.longitude);

                if(newLat.matches(String.valueOf(latitude)) && newLong.matches(String.valueOf(longitude)))
                {
                    Log.d("lll","lll");
                }

                else {

                    mMap.clear();

                    Log.i("centerLat", String.valueOf(cameraPosition.target.latitude));

                    Log.i("centerLong", String.valueOf(cameraPosition.target.longitude));


                    LatLng latLng = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);

                    mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title("Current Location"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.setOnMarkerDragListener(MarkYourLocation.this);
                    mMap.setOnMapLongClickListener(MarkYourLocation.this);
                    GetAddress(String.valueOf(newLat),String.valueOf(newLong));

                    lattitudeTextView.setText("Latitude: "+String.valueOf(newLat));
                    longitudeTextView.setText("Longitude: "+String.valueOf(newLong));
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();

        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }
}
