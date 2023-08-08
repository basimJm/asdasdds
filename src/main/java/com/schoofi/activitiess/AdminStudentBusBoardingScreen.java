package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activities.StudentHomeScreen;
import com.schoofi.adapters.AdminStudentBusBoardingRouteAdapter;
import com.schoofi.adapters.TeacherStudentListSubjectAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.core.BarcodeScannerView;


public class AdminStudentBusBoardingScreen extends AppCompatActivity {

    private ImageView back,studentImage;
    private Button scan,submit;
    private TextView studentName,studentAdmissionNumber,studentBusRouteNumber,studentClass;
    private int STORAGE_PERMISSION_CODE = 23;
    String value;
    private JSONArray busBoardingArray,busRouteListArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private ListView busRouteListView;
    private AdminStudentBusBoardingRouteAdapter adminStudentBusBoardingRouteAdapter;
    final Context context = this;
    private String busRouteId;
    private LinearLayout linearLayout;
    private TextView busRouteText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_student_bus_boarding_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        value = getIntent().getStringExtra("value");
        if(value.matches("1"))
        {
            postAttendance();
        }

        else {

        }


       scan = (Button) findViewById(R.id.btn_scan_qrcode);
        studentImage = (ImageView) findViewById(R.id.imageView_studentImage);
        studentName = (TextView) findViewById(R.id.txt_studentName);
        studentAdmissionNumber = (TextView) findViewById(R.id.txt_studentId);
        studentClass = (TextView) findViewById(R.id.txt_studentClassSection);
        busRouteText = (TextView) findViewById(R.id.text_bus_route);
        linearLayout = (LinearLayout) findViewById(R.id.linear_listView_teacherStudentAttendanceDetails);
        linearLayout.setVisibility(View.GONE);
        busRouteText.setVisibility(View.GONE);
       // studentBusRouteNumber = (TextView) findViewById(R.id.txt_student_bus_route);
        //submit = (Button) findViewById(R.id.btn_submit);

        //submit.setVisibility(View.GONE);
        busRouteListView = (ListView) findViewById(R.id.list_bus_route);

        busRouteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.bus_boarding_popup_view, null);



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);




                final TextView studentName1 = (TextView) promptsView.findViewById(R.id.text_student_Name);
                final TextView studentClass1 =  (TextView) promptsView.findViewById(R.id.text_class_section);
                final TextView busRoute1 =   (TextView) promptsView.findViewById(R.id.text_bus_route);

                try {
                    studentName1.setText(busBoardingArray.getJSONObject(0).getString("stu_name"));
                    studentClass1.setText(busBoardingArray.getJSONObject(0).getString("class_name")+"-"+busBoardingArray.getJSONObject(0).getString("section_name"));
                    busRoute1.setText(busRouteListArray.getJSONObject(position).getString("bus_route_no"));
                    busRouteId = busRouteListArray.getJSONObject(0).getString("bus_route_no");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // get user input and set it to result


                                        // edit text


                                        postMessage1();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                //System.out.println(array2);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkAndRequestPermissions())
                {
                    Intent intent = new Intent(AdminStudentBusBoardingScreen.this, QrCodeScanner.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


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

    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_DETAILS/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try {
                    responseObject = new JSONObject(response);

                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        //submit.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                        busRouteText.setVisibility(View.GONE);


                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        linearLayout.setVisibility(View.GONE);
                        busRouteText.setVisibility(View.GONE);
                        //submit.setVisibility(View.GONE);
                    } else if (responseObject.has("responseObject")) {
                        loading.dismiss();
                        linearLayout.setVisibility(View.VISIBLE);
                        busRouteText.setVisibility(View.VISIBLE);
                        busBoardingArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != busBoardingArray && busBoardingArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = busBoardingArray.toString().getBytes();
                            VolleySingleton.getInstance(AdminStudentBusBoardingScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_DETAILS + "?ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&qr_code="+Preferences.getInstance().qrCode, e);
                            Preferences.getInstance().loadPreference(getApplicationContext());
                            Preferences.getInstance().studentId = busBoardingArray.getJSONObject(0).getString("stu_id");
                            Preferences.getInstance().savePreference(getApplicationContext());

                            studentName.setText(busBoardingArray.getJSONObject(0).getString("stu_name"));
                            studentAdmissionNumber.setText(busBoardingArray.getJSONObject(0).getString("admn_no"));
                            studentClass.setText(busBoardingArray.getJSONObject(0).getString("class_name")+"-"+busBoardingArray.getJSONObject(0).getString("section_name"));
                            //studentBusRouteNumber.setText(busBoardingArray.getJSONObject(0).getString(""));
                            mDrawableBuilder = TextDrawable.builder().round();
                            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(busBoardingArray.getJSONObject(0).getString("stu_name").charAt(0)), R.color.blue);
                            String imagePath = busBoardingArray.getJSONObject(0).getString("picture");
			/*Picasso.with(context).load(imagePath).placeholder(R.drawable.profilebig).
			error(R.drawable.profilebig).transform(new CircleTransform()).into(holder.studentImage);*/
                            Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL+imagePath).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(studentImage)
                            {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    studentImage.setImageDrawable(circularBitmapDrawable);
                                }
                            });

                            initData();
                            getTeacherClassList();

                            //submit.setVisibility(View.VISIBLE);


                        }
                    } else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }


                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    linearLayout.setVisibility(View.GONE);
                    busRouteText.setVisibility(View.GONE);
                    //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error s! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                linearLayout.setVisibility(View.GONE);
                busRouteText.setVisibility(View.GONE);
                //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                //params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
                //params.put("Students", jsonObject1.toString());

                params.put("user_id",Preferences.getInstance().userId);
                //params.put("tea_id", Preferences.getInstance().userId);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("qr_code",Preferences.getInstance().qrCode);
                //params.put("Students", "harsh");
                //params.put("u_email_id", Preferences.getInstance().userEmailId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(this))
            queue.add(requestObject);
        else
        {
            loading.dismiss();
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            linearLayout.setVisibility(View.GONE);
            busRouteText.setVisibility(View.GONE);
            //setSupportProgressBarIndeterminateVisibility(false);
        }



    }

    private void postMessage1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BUS_BOARDING_NOTIFICATION_URL/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"Error in boarding!");


                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(getApplicationContext(),"Successfuly Boarded");
                        finish();
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                try {
                    params.put("user_id",Preferences.getInstance().userId);

                    params.put("token",Preferences.getInstance().token);

                    params.put("device_id", Preferences.getInstance().deviceId);
                    params.put("stu_id",busBoardingArray.getJSONObject(0).getString("stu_id"));
                    params.put("stu_name",busBoardingArray.getJSONObject(0).getString("stu_name"));
                    params.put("class_section_name",busBoardingArray.getJSONObject(0).getString("class_name")+"-"+busBoardingArray.getJSONObject(0).getString("section_name"));
                    params.put("bus_route_no",busRouteId);
                    //params.put("cls_id",busBoardingArray.getJSONObject(0).getString("class_id"));
                    //params.put("sec_id",busBoardingArray.getJSONObject(0).getString("section_id"));
                    params.put("ins_id",Preferences.getInstance().institutionId);
                    params.put("sch_id",Preferences.getInstance().schoolId);
                    params.put("pickup_or_drop","pickup");




                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            setSupportProgressBarIndeterminateVisibility(false);
        }
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BUS_ROUTE_INFO+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&stu_id="+Preferences.getInstance().studentId);
            if(e == null)
            {
                busRouteListArray= null;
            }
            else
            {
                busRouteListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(busRouteListArray!= null)
        {
            adminStudentBusBoardingRouteAdapter= new AdminStudentBusBoardingRouteAdapter(AdminStudentBusBoardingScreen.this,busRouteListArray);
            busRouteListView.setAdapter(adminStudentBusBoardingRouteAdapter);
            adminStudentBusBoardingRouteAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AdminStudentBusBoardingScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BUS_ROUTE_INFO;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                // System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))

                    {

                        busRouteListArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=busRouteListArray && busRouteListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = busRouteListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.BUS_ROUTE_INFO+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&stu_id="+Preferences.getInstance().studentId,e);
                            busRouteListView.invalidateViews();
                            adminStudentBusBoardingRouteAdapter= new AdminStudentBusBoardingRouteAdapter(AdminStudentBusBoardingScreen.this,busRouteListArray);
                            busRouteListView.setAdapter(adminStudentBusBoardingRouteAdapter);
                            adminStudentBusBoardingRouteAdapter.notifyDataSetChanged();
                            // swipyRefreshLayout.setRefreshing(false);
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);

                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                //params.put("crr_date",currentDate);
                return params;
            }};

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
}
