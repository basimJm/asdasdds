package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.adapters.AdminStudentDropStudentList;
import com.schoofi.adapters.ParentHomeScreenAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AdminStudentDropFormNewActivity extends AppCompatActivity {

    private ImageView back;
    private Button scanParentQrCode;
    private ListView parentHomeScreenListView;
    private JSONArray parentHomeScreenArray;
    private AdminStudentDropStudentList parentHomeScreenAdapter;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_student_drop_form_new);
        scanParentQrCode = (Button) findViewById(R.id.btn_scan1);
        parentHomeScreenListView = (ListView) findViewById(R.id.parent_home_screen_listView);
        value = getIntent().getStringExtra("value");

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scanParentQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminStudentDropFormNewActivity.this,ParentQrCodeScanner.class);
                startActivity(intent);
                finish();
            }
        });

        parentHomeScreenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    Intent intent = new Intent(AdminStudentDropFormNewActivity.this,AdminStudentDropNewSecondActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("stu_id",parentHomeScreenArray.getJSONObject(position).getString("stu_id"));
                    intent.putExtra("stu_name",parentHomeScreenArray.getJSONObject(position).getString("stu_name"));
                    intent.putExtra("cls_id",parentHomeScreenArray.getJSONObject(position).getString("class_id"));
                    intent.putExtra("sec_id",parentHomeScreenArray.getJSONObject(position).getString("section_id"));
                    intent.putExtra("class_section_name",parentHomeScreenArray.getJSONObject(position).getString("class_name")+"-"+parentHomeScreenArray.getJSONObject(position).getString("section_name"));
                    intent.putExtra("session",parentHomeScreenArray.getJSONObject(position).getString("session"));
                    intent.putExtra("picker_person_name",parentHomeScreenArray.getJSONObject(position).getString("pickup_person_name"));
                    intent.putExtra("picker_person_id",parentHomeScreenArray.getJSONObject(position).getString("pickup_person_id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        if(value.matches("1"))
        {
            postAttendance();
        }
        else
        {

        }
    }


    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.QR_CODE_STRING/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try {
                    responseObject = new JSONObject(response);

                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "No Records Found!");



                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");

                    } else if (responseObject.has("responseObject")) {
                        loading.dismiss();
                        //Utils.showToast(getApplicationContext(),"Qr Codes Matched Successfully!");
                        parentHomeScreenArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != parentHomeScreenArray && parentHomeScreenArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = parentHomeScreenArray.toString().getBytes();
                            VolleySingleton.getInstance(AdminStudentDropFormNewActivity.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.QR_CODE_STRING + "?ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&qr_code="+Preferences.getInstance().parentQrCode+"&pickup_drop="+"drop", e);

                            for(int u=0;u<parentHomeScreenArray.length();u++)
                            {
                                if(parentHomeScreenArray.getJSONObject(u).getString("stu_check").matches("YES"))
                                {

                                }

                                else {
                                    parentHomeScreenArray.remove(u);
                                }
                            }

                            parentHomeScreenListView.invalidateViews();
                            parentHomeScreenAdapter = new AdminStudentDropStudentList(AdminStudentDropFormNewActivity.this, parentHomeScreenArray);
                            parentHomeScreenListView.setAdapter(parentHomeScreenAdapter);
                            parentHomeScreenAdapter.notifyDataSetChanged();



                        }
                    } else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }


                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error s! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
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
                params.put("device_id",Preferences.getInstance().deviceId);
               // params.put("qr_code_stu",Preferences.getInstance().qrCode);
                params.put("qr_code",Preferences.getInstance().parentQrCode);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("pickup_drop","drop");

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
            //setSupportProgressBarIndeterminateVisibility(false);
        }



    }
}
