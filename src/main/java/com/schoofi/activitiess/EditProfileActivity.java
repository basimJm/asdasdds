package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class EditProfileActivity extends AppCompatActivity {

    private ImageView back;
    private EditText religion,ews,disability,birthMark,height,weight,bloodGroup,allergies,address,state,city,landmark,pincode,district;
    private JSONArray userProfileArray;
    private Button edit;
    String schoolId = Preferences.getInstance().schoolId;
    String userEmailId = Preferences.getInstance().userEmailId;
    String userId = Preferences.getInstance().userId;
    String token = Preferences.getInstance().token;
    String roleId = Preferences.getInstance().userRoleId;
    private Button markLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_edit_profile);

        back= (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        religion = (EditText) findViewById(R.id.edit_religion);
        ews = (EditText) findViewById(R.id.edit_ews);
        disability = (EditText) findViewById(R.id.edit_disability);
        birthMark = (EditText) findViewById(R.id.edit_birth_mark);
        markLocation = (Button) findViewById(R.id.btn_done);
        height = (EditText) findViewById(R.id.edit_height);
        weight = (EditText) findViewById(R.id.edit_weight);
        bloodGroup = (EditText) findViewById(R.id.edit_blood_group);
        allergies = (EditText) findViewById(R.id.edit_student_allergies);
        address = (EditText) findViewById(R.id.edit_address);
        state = (EditText) findViewById(R.id.edit_state);
        city = (EditText) findViewById(R.id.edit_city);
        landmark = (EditText) findViewById(R.id.edit_landmark);
        pincode = (EditText) findViewById(R.id.edit_pincode);
        district = (EditText) findViewById(R.id.edit_district);
        edit = (Button) findViewById(R.id.btn_edit);
        markLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EditProfileActivity.this,MarkYourLocationFirstScreen.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage1();
            }
        });

        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.USER_PROFILE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id=" +schoolId+"&role_id="+roleId+"&device_id="+ Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(EditProfileActivity.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(EditProfileActivity.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("profile"))
                    {
                        userProfileArray= new JSONObject(response).getJSONArray("profile");
                        if(null!=userProfileArray && userProfileArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = userProfileArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.USER_PROFILE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id=" +schoolId+"&role_id="+roleId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId,e);
									/*username.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name"));
									userSchool.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("school_name"));
									userClass.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("class_name")+" "+responseObject.getJSONArray("profile").getJSONObject(0).getString("section_name"));
									userFathersName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_name"));
									userMotherName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_name"));*/
                            //Picasso.with(UserProfile.this).load(AppConstants.SERVER_URLS.SERVER_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("picture")).placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(userProfileImage);
                            //Glide.with(UserProfile.this).load(AppConstants.SERVER_URLS.SERVER_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("picture")).crossFade().placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(userProfileImage);



                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("religion").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("religion").matches("null"))
                            {
                                religion.setText("");
                            }

                            else
                            {
                                religion.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("religion"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("bpl").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("bpl").matches("null"))
                            {
                                ews.setText("");
                            }

                            else
                            {
                                ews.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("bpl"));
                            }



                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("disability").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("disability").matches("null"))
                            {
                                disability.setText("");
                            }

                            else
                            {
                                disability.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("disability"));
                            }

                            /*if(responseObject.getJSONArray("profile").getJSONObject(0).getString("bus_route_no").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("bus_route_no").matches("null"))
                            {
                                studentBusNumber.setText("Bus Route No: -");
                            }

                            else
                            {
                                studentBusNumber.setText("Bus Route No: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("bus_route_no"));
                            }*/

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("birth_mark").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("birth_mark").matches("null"))
                            {
                                birthMark.setText("");
                            }

                            else
                            {
                                birthMark.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("birth_mark"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("height").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("height").matches("null"))
                            {
                                height.setText("");
                            }

                            else
                            {
                                height.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("height"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("weight").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("weight").matches("null"))
                            {
                                weight.setText("");
                            }

                            else
                            {
                                weight.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("weight"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("blood_group").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("blood_group").matches("null"))
                            {
                                bloodGroup.setText("");
                            }

                            else
                            {
                                bloodGroup.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("blood_group"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("allergies").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("allergies").matches("null"))
                            {
                                allergies.setText("");
                            }

                            else
                            {
                                allergies.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("allergies"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("address1").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("address1").matches("null"))
                            {
                                address.setText("");
                            }

                            else
                            {
                                address.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("address1"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("state").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("state").matches("null"))
                            {
                                state.setText("");
                            }

                            else
                            {
                                state.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("state"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("city").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("city").matches("null"))
                            {
                                city.setText("");
                            }

                            else
                            {
                                city.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("city"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("pincode").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("pincode").matches("null"))
                            {
                                pincode.setText("");
                            }

                            else
                            {
                                pincode.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("pincode"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("landmark").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("landmark").matches("null"))
                            {
                                landmark.setText("");
                            }

                            else
                            {
                                landmark.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("landmark"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("district").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("district").matches("null"))
                            {
                                district.setText("");
                            }

                            else
                            {
                                district.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("district"));
                            }
                            /*

									*//*date1 = responseObject.getJSONArray("profile").getJSONObject(0).getString("user_dob");
									try {
										date7 = formatter.parse(date1);
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

									SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
									date2 = formatter1.format(date7);
									userFathersName.setText(date2);*//*











                           */






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

        //getUserProfile();

    }

    private void postMessage1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.UPDATE_PROFILE/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    if(responseObject.getJSONArray("responseObject").getJSONObject(0).getString("Msg").matches("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(),"Error Submitting details!");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.getJSONArray("responseObject").getJSONObject(0).getString("Msg").matches("1"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(),"Successfully Changed!");
                        finish();

                    }

                    else {
                        loading.dismiss();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error submitting! Please try after sometime.");
                    loading.dismiss();
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(getApplicationContext(), "Error submitting! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                params.put("token",Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("stu_id",Preferences.getInstance().studentId);
                if(religion.getText().toString().matches(""))
                {
                    params.put("religion","");
                }

                else
                {
                    params.put("religion",religion.getText().toString());
                }

                if(ews.getText().toString().matches(""))
                {
                    params.put("ews","");
                }
                else
                {
                    params.put("ews",ews.getText().toString());
                }

                if(disability.getText().toString().matches(""))
                {
                    params.put("disability","");
                }
                else
                {
                    params.put("disability",disability.getText().toString());
                }

                if(birthMark.getText().toString().matches(""))
                {
                    params.put("birth_mark","");

                }

                else
                {
                    params.put("birth_mark",birthMark.getText().toString());
                }

                if(height.getText().toString().matches(""))
                {
                    params.put("height","");
                }

                else
                {
                    params.put("height",height.getText().toString());
                }

                if(weight.getText().toString().matches(""))
                {
                    params.put("weight","");
                }

                else
                {
                    params.put("weight",weight.getText().toString());
                }

                if(bloodGroup.getText().toString().matches(""))
                {
                    params.put("blood_group","");
                }

                else
                {
                    params.put("blood_group",bloodGroup.getText().toString());
                }

                if(allergies.getText().toString().matches(""))
                {
                    params.put("allergies","");
                }

                else
                {
                    params.put("allergies",allergies.getText().toString());
                }

                if(address.getText().toString().matches(""))
                {
                    params.put("address","");
                }

                else
                {
                    params.put("address",address.getText().toString());
                }

                if(state.getText().toString().matches(""))
                {
                    params.put("state","");
                }

                else
                {
                    params.put("state",state.getText().toString());
                }

                if(pincode.getText().toString().matches(""))
                {
                    params.put("pincode","");
                }

                else
                {
                    params.put("pincode",pincode.getText().toString());
                }

                if(city.getText().toString().matches(""))
                {
                    params.put("city","");
                }

                else
                {
                    params.put("city",city.getText().toString());
                }

                if(landmark.getText().toString().matches(""))
                {
                    params.put("landmark","");
                }

                else
                {
                    params.put("landmark",landmark.getText().toString());
                }

                if(district.getText().toString().matches(""))
                {
                    params.put("district","");
                }

                else
                {
                    params.put("district",district.getText().toString());
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
            Utils.showToast(this, "Unable to submit data, kindly enable internet settings!");
            setSupportProgressBarIndeterminateVisibility(false);
            loading.dismiss();
        }
    }
}
