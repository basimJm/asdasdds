package com.schoofi.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.AdmissionEnquiryFinalScreen;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by harsh malhotra on 4/26/2016.
 */
public class AdmissionEnquirySubjectAdapter {



    public static void display(final Activity activity, Button btn,final String name,final String type,final String dob,final String gender,final String email,final String phone,final String address,final String schoolId,final String studentClass,final String stream,final String fatherName,final String fatherEmail,final String fatherPhone,final String motherName,final String motherEmail,final String motherPhone,final String brochureFees,final String admissionFees,final String courierCharges)
    {



        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LinearLayout scrollViewlinerLayout = (LinearLayout) activity.findViewById(R.id.linearLayoutForm);
                JSONArray imageArray;

                EditText previousSchoolName,previousSchoolAddress,lastClassAttended,lastStream,totalPercentage,grade;
                final String previousSchoolName1,previousSchoolAddress1,lastClassAttended1,previousStream1,totalPercentage1,grade1;

                previousSchoolName = (EditText) activity.findViewById(R.id.edit_school_name);
                previousSchoolAddress = (EditText) activity.findViewById(R.id.edit_school_address);
                lastClassAttended = (EditText) activity.findViewById(R.id.edit_last_class_attended);

                lastStream = (EditText) activity.findViewById(R.id.edit_last_stream);
                totalPercentage = (EditText) activity.findViewById(R.id.edit_percentage);
                grade = (EditText) activity.findViewById(R.id.edit_grade);

                previousSchoolName1 = previousSchoolName.getText().toString();
                previousSchoolAddress1 = previousSchoolAddress.getText().toString();
                lastClassAttended1 = lastClassAttended.getText().toString();
                previousStream1 = lastStream.getText().toString();
                totalPercentage1 = totalPercentage.getText().toString();
                grade1 = grade.getText().toString();
                //Log.d("hhhh","hhh"+previousSchoolAddress1);

                final String count;
                int i;

                final ArrayList<String> subject = new ArrayList<String>();
                final ArrayList<String> totalMarks = new ArrayList<String>();
                final ArrayList<String> obtained = new ArrayList<String>();


                for (i = 0; i < scrollViewlinerLayout.getChildCount(); i++) {
                    LinearLayout innerLayout = (LinearLayout) scrollViewlinerLayout.getChildAt(i);
                    EditText edit = (EditText) innerLayout.findViewById(R.id.edit_subject);
                    EditText edit1 = (EditText) innerLayout.findViewById(R.id.edit_total_marks);
                    EditText edit2 = (EditText) innerLayout.findViewById(R.id.edit_marks_obtained);

                    if(edit.getText().toString().matches(""))
                    {
                        subject.add("NA");
                    }

                    else {
                        subject.add(edit.getText().toString());
                    }

                    if(edit1.getText().toString().matches(""))
                    {
                        totalMarks.add("NA");
                    }

                    else {
                        totalMarks.add(edit1.getText().toString());
                    }

                    if(edit2.getText().toString().matches(""))
                    {
                        obtained.add("NA");
                    }

                    else
                    {
                        obtained.add(edit2.getText().toString());
                    }




                }

                count = String.valueOf(i);
                Log.d("hhhh","hhh"+type);

                RequestQueue queue = VolleySingleton.getInstance(activity.getApplicationContext()).getRequestQueue();
                String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMISSION_ENQUIRY_FORM_SUBMIT;
                StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseObject;
                        Log.d("msg",response);
                        try
                        {
                            responseObject = new JSONObject(response);
                            if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                            {
                                //loading.dismiss();
                                Utils.showToast(activity.getApplicationContext(), "Please try again later");
                                //finish();
                            }
                            else
                            if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                            {
                                //loading.dismiss();
                                Utils.showToast(activity.getApplicationContext(), "Subtasks Created ");
                                activity.finish();
                            }

                            else
                            if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                            {
                                //loading.dismiss();
                                Utils.showToast(activity.getApplicationContext(), "Session expired");

                            }

                            else
                            if(responseObject.has("enq_list"))
                            {
                                //loading.dismiss();
                                Utils.showToast(activity.getApplicationContext(), "Enquiry Submitted Successfully");
                                Intent intent = new Intent(activity, AdmissionEnquiryFinalScreen.class);
                                intent.putExtra("id",responseObject.getJSONObject("enq_list").getString("req_ref_id").toString());
                                intent.putExtra("brochureFees",brochureFees);
                                intent.putExtra("admissionCharges",admissionFees);
                                intent.putExtra("courier_charges",courierCharges);
                                intent.putExtra("address",address);
                                activity.startActivity(intent);

                            }
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                            Utils.showToast(activity.getApplicationContext(), "Error submitting alert! Please try after sometime.");
                        }
                        //loading.dismiss();

                    }}, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Utils.showToast(activity.getApplicationContext(), "Error creating task! Please try after sometime.");
                        //loading.dismiss();
                    }
                })
                {
                    @Override
                    protected Map<String,String> getParams(){
                        Preferences.getInstance().loadPreference(activity.getApplicationContext());
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("u_id",Preferences.getInstance().userId);
                        params.put("token", Preferences.getInstance().token);
                        params.put("phone", phone);
                        params.put("device_id", Preferences.getInstance().deviceId);
                        params.put("subject",subject.toString().replace("[", "").replace("]", ""));
                        params.put("total_marks",totalMarks.toString().replace("[", "").replace("]", ""));
                        params.put("obtained_marks",obtained.toString().replace("[", "").replace("]", ""));
                        params.put("candidate_name",name);
                        params.put("dob",dob);
                        params.put("email",email);
                        params.put("mobile",phone);
                        params.put("f_name",fatherName);
                        params.put("f_email",fatherEmail);
                        params.put("f_phone",fatherPhone);
                        params.put("m_name",motherName);
                        params.put("m_email",motherEmail);
                        params.put("m_phone",motherPhone);
                        params.put("address",address);
                        params.put("required_class",studentClass);
                        params.put("gender",gender);
                        params.put("required_school",schoolId);
                        params.put("ins_type",type);
                        params.put("required_stream",stream);
                        params.put("previous_school_name",previousSchoolName1);
                        params.put("previous_school_address",previousSchoolAddress1);
                        params.put("previous_class",lastClassAttended1);
                        params.put("stream",previousStream1);
                        params.put("total_percentage",totalPercentage1);
                        params.put("CGPA",grade1);
                        params.put("count",count);

                        return params;
                    }};

                requestObject.setRetryPolicy(new DefaultRetryPolicy(
                        25000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                if(Utils.isNetworkAvailable(activity.getApplicationContext()))
                    queue.add(requestObject);
                else
                {
                    Utils.showToast(activity.getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
                    //loading.dismiss();
                }
            }

        });

            }

            public static void add(final Activity activity, Button btn) {
                final LinearLayout linearLayoutForm = (LinearLayout) activity.findViewById(R.id.linearLayoutForm);


                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final LinearLayout newView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.subject_listview_row, null);

                        //newView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        ImageButton btnRemove = (ImageButton) newView.findViewById(R.id.btnRemove);
                        btnRemove.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                linearLayoutForm.removeView(newView);
                            }
                        });

                        linearLayoutForm.addView(newView);
                    }
                });
            }
        }

