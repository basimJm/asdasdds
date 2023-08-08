package com.schoofi.activitiess;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.EnquiryTypeVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddmissionFormPrimaryScreen extends AppCompatActivity {

    Spinner selectEnquiryType,selectSchool;
    ImageView back;

    EditText name,email,phone,dob,studentClass,address,gender,stream;
    Button next;
    String name1,email1,phone1,dob1,studentClass1,address1,gender1,schoolId1,enquiryType,stream1,insType;
    JSONObject jsonObject;
    JSONArray enquiryTypeArray;
    ArrayList<String> enuiryNames;
    ArrayList<EnquiryTypeVO> enquiryId;
    EnquiryTypeVO enquiryTypeVO = new EnquiryTypeVO();
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String date,date1;
    String Month;
    Button addmissionRules;
    String brochureFee,admission_rules,admission_add_charges,courier_charges;
    public static final CharSequence[] DAYS_OPTIONS  = {"Select Enquiry-Type","School","College","Hospital","Clubs","Others"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_addmission_form_primary_screen);

        selectEnquiryType = (Spinner) findViewById(R.id.spinner_enquiry_type);
        back = (ImageView) findViewById(R.id.img_back);
        selectSchool = (Spinner) findViewById(R.id.spinner_select_school);
        selectSchool.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        next = (Button) findViewById(R.id.btn_next);
        addmissionRules = (Button) findViewById(R.id.btn_addmissionRules);

        addmissionRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.SERVER_URL+admission_rules));
                startActivity(intent);
            }
        });


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, R.layout.spinner_layout, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        selectEnquiryType.setAdapter(adapter); // Apply the adapter to the spinner



        selectEnquiryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                enquiryType = "" + parent.getItemAtPosition(position);


                if (enquiryType.matches("Select Enquiry-Type")) {
                    Log.d("kkk", "kkk");
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    RequestQueue queue = VolleySingleton.getInstance(AddmissionFormPrimaryScreen.this).getRequestQueue();
                    enuiryNames = new ArrayList<String>();
                    enquiryId = new ArrayList<EnquiryTypeVO>();
                    String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ENQUIRY_TYPE;
                    StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject responseObject;
                            try {
                                responseObject = new JSONObject(response);
                               // System.out.println(response);
                                //toa();
                                if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                                    Utils.showToast(getApplicationContext(), "Invalid type");
                                } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                                    Utils.showToast(AddmissionFormPrimaryScreen.this, "Session Expired:Please Login Again");
                                } else if (responseObject.has("type")) {
                                    selectSchool.setVisibility(View.VISIBLE);
                                    enquiryTypeArray = new JSONObject(response).getJSONArray("type");
                                    if (null != enquiryTypeArray && enquiryTypeArray.length() >= 0) {
                                        Cache.Entry e = new Cache.Entry();
                                        e.data = enquiryTypeArray.toString().getBytes();
                                        VolleySingleton.getInstance(AddmissionFormPrimaryScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ENQUIRY_TYPE + "?device_id=" + Preferences.getInstance().deviceId + "&sch_type=" + enquiryType + "&token=" + Preferences.getInstance().token/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/, e);
                                        for (int i = 0; i < enquiryTypeArray.length(); i++) {
                                            jsonObject = enquiryTypeArray.getJSONObject(i);
                                            enquiryTypeVO.setEnquiryId(jsonObject.optString("school_id"));
                                            enquiryId.add(enquiryTypeVO);
                                            enuiryNames.add(jsonObject.optString("school_name"));
                                        }

                                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String> (AddmissionFormPrimaryScreen.this, R.layout.spinner_layout, enuiryNames);
                                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        selectSchool.setAdapter(adapter1);

                                       /* selectSchool
                                                .setAdapter(new ArrayAdapter<String>(AddmissionFormPrimaryScreen.this,
                                                        R.layout.spinner_layout,
                                                        enuiryNames));*/
                                        //schoolId1 ="Select";

                                        selectSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                            @Override
                                            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                                       int position, long arg3) {
                                                // TODO Auto-generated method stub

                                                insType = ""+arg0.getItemAtPosition(position);
                                                //Utils.showToast(getApplicationContext(),""+insType);
                                                schoolId1 = enquiryId.get(position).getEnquiryId().toString();
                                                try {
                                                    brochureFee = enquiryTypeArray.getJSONObject(position).getString("brochure_fee");
                                                    admission_add_charges = enquiryTypeArray.getJSONObject(position).getString("admission_add_charges");
                                                    admission_rules = enquiryTypeArray.getJSONObject(position).getString("admission_rules");
                                                    courier_charges = enquiryTypeArray.getJSONObject(position).getString("courier_charges");
                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }

                                                if(insType.matches("Select"))
                                                {
                                                    Log.d("jjj","jjjj");
                                                }

                                                else
                                                {
                                                    ((TextView) arg0.getChildAt(0)).setTextColor(Color.BLACK);
                                                }
                                                //System.out.println(examId1);

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> arg0) {
                                                // TODO Auto-generated method stub

                                            }


                                        });


                                    }
                                } else {
                                    Utils.showToast(AddmissionFormPrimaryScreen.this, responseObject.getString("errorMessage"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.showToast(AddmissionFormPrimaryScreen.this, "Error fetching modules! Please try after sometime.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showToast(AddmissionFormPrimaryScreen.this, "Error fetching modules! Please try after sometime.");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            Preferences.getInstance().loadPreference(AddmissionFormPrimaryScreen.this);
                            params.put("device_id", Preferences.getInstance().deviceId);
                            params.put("token", Preferences.getInstance().token);
                            params.put("sch_type", enquiryType);
                            return params;
                        }
                    };

                    requestObject.setRetryPolicy(new DefaultRetryPolicy(
                            25000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    if (Utils.isNetworkAvailable(AddmissionFormPrimaryScreen.this))
                        queue.add(requestObject);
                    else {
                        Utils.showToast(AddmissionFormPrimaryScreen.this, "Unable to fetch data, kindly enable internet settings!");
                    }


                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enquiryType.matches("Select Enquiry-Type"))
                {
                    Utils.showToast(getApplicationContext(),"Plz select enqury Type");
                }
                else
                    if(insType.matches("Select"))
                    {
                        Utils.showToast(getApplicationContext(),"Plz select enqury Type");
                    }

                else
                    {
                        Intent intent = new Intent(AddmissionFormPrimaryScreen.this,AdmissionFormPage1.class);
                        intent.putExtra("type",enquiryType.toString());
                        intent.putExtra("schoolId",schoolId1);
                        intent.putExtra("brochureFees",brochureFee);
                        intent.putExtra("admissionFees",admission_add_charges);
                        intent.putExtra("courier_charges",courier_charges);
                        startActivity(intent);
                    }
            }
        });
    }
}
