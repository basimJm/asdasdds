package com.schoofi.activitiess;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.GoogleMap;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventImageSharingScreen extends AppCompatActivity {

    LinearLayout linearLayoutHeader;
    ImageView schoolLogo,schoofiLogo,googlePlayLogo,iosAppStoreLogo,schoofiBelowLogo,uploadLogo,back;
    TextView schoolName,eventTitle;
    String position1;
    int position;
    private JSONArray schoolNotificationListArray,studentEventListArray,teacherEventListArray;
    private int STORAGE_PERMISSION_CODE = 23;
    Bitmap mbitmap;
    View rootView;
    File imagePath;
    String title,value,param;
    String fontPath = "fonts/RobotoCondensed-Bold.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Event Image Sharing Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_event_image_sharing_screen);
        position1 = getIntent().getStringExtra("position1");
        position = getIntent().getExtras().getInt("position");

        schoolLogo = (ImageView) findViewById(R.id.imageView_schoolLogo);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        schoofiLogo = (ImageView) findViewById(R.id.imageView_schoolLogo1);
        googlePlayLogo = (ImageView) findViewById(R.id.google_play_icon);
        iosAppStoreLogo = (ImageView) findViewById(R.id.app_store_icon);
        schoofiBelowLogo = (ImageView) findViewById(R.id.schoofi_below_layout);
        linearLayoutHeader = (LinearLayout) findViewById(R.id.headerLayout);
        uploadLogo = (ImageView) findViewById(R.id.img_share);
        schoolName = (TextView) findViewById(R.id.text_school_name);
        //title = getIntent().getStringExtra("title");
        eventTitle = (TextView) findViewById(R.id.text_school_title);
        value = getIntent().getStringExtra("value");
        param = getIntent().getStringExtra("param");

       // Utils.showToast(getApplicationContext(),value+param+position1+position);

        Typeface face = Typeface.createFromAsset(getAssets(), fontPath);

        eventTitle.setTypeface(face);
        schoolName.setTypeface(face);
        //eventTitle.setText(title);

        if(param.matches("2"))
        {
            initData1();
        }

        else {


            initData();
        }

        uploadLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkAndRequestPermissions()) {

                    back.setVisibility(View.INVISIBLE);
                    uploadLogo.setVisibility(View.INVISIBLE);
                    linearLayoutHeader.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Bitmap bitmap = takeScreenshot();
                    saveBitmap(bitmap);
                    shareIt();
                    //linearLayoutHeader.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    uploadLogo.setVisibility(View.VISIBLE);
                    linearLayoutHeader.setBackgroundColor(Color.parseColor("#EE4749"));

                }

                else
                {

                }

            }
        });






    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void initData1() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_EVENT_LIST + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&value=" + value);
            if (e == null) {
                teacherEventListArray = null;
            } else {
                teacherEventListArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (teacherEventListArray != null)
            try {
                eventTitle.setText(teacherEventListArray.getJSONObject(position).getString("program_name"));


                Glide.with(EventImageSharingScreen.this).load(AppConstants.SERVER_URLS.IMAGE_URL + position1).crossFade().placeholder(R.drawable.eventimage).error(R.drawable.eventimage).into(schoofiLogo);


                getStudentFeedList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EVENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&value="+value);
            if(e == null)
            {
                studentEventListArray= null;
            }
            else
            {
                studentEventListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentEventListArray!= null)
            try {
                eventTitle.setText(studentEventListArray.getJSONObject(position).getString("program_name"));


                    Glide.with(EventImageSharingScreen.this).load(AppConstants.SERVER_URLS.IMAGE_URL+position1).crossFade().placeholder(R.drawable.eventimage).error(R.drawable.eventimage).into(schoofiLogo);



                getStudentFeedList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }



    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        //String shareBody = "In Schoofi-Learn, My score with screen shot";
        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Schoofi-Learn score");
        //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),STORAGE_PERMISSION_CODE);
            return false;
        }
        return true;
    }



    protected void getStudentFeedList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_WEBSITE_URL + "?ins_id=" + Preferences.getInstance().institutionId+"&sch_id=" + Preferences.getInstance().schoolId;
        StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        /*Intent intent = new Intent(StudentSettings.this,AboutSchool.class);
                        startActivity(intent);*/

                        Utils.showToast(getApplicationContext(), "Not Able to Load School Logo Please Try After Sometime");
                    }
                    //Utils.showToast(A,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("school"))
                    {
                        schoolNotificationListArray= new JSONObject(response).getJSONArray("school");
                        if(null!=schoolNotificationListArray && schoolNotificationListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = schoolNotificationListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId,e);
                            Glide.with(EventImageSharingScreen.this).load(AppConstants.SERVER_URLS.IMAGE_URL+schoolNotificationListArray.getJSONObject(0).getString("school_logo")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(schoolLogo);
                            schoolName.setText(schoolNotificationListArray.getJSONObject(0).getString("school_website_url"));

                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
                setSupportProgressBarIndeterminateVisibility(false);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        back.setVisibility(View.VISIBLE);
        uploadLogo.setVisibility(View.VISIBLE);
    }
}
