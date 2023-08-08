package com.schoofi.activitiess;

import android.app.ProgressDialog;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.payUMoney.sdk.walledSdk.Preferences1;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentAdmissionSuccess1 extends AppCompatActivity {

    TextView payeeName,transactionStatus,transactionId,payment,payFor;
    ImageView back;
    String value,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_payment_admission_success1);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        value = getIntent().getStringExtra("value");
        id = getIntent().getStringExtra("id");


        payeeName = (TextView) findViewById(R.id.text_payeeName1);
        transactionId = (TextView) findViewById(R.id.text_transactionId1);
        transactionStatus = (TextView) findViewById(R.id.text_transactionStatus1);
        payFor = (TextView) findViewById(R.id.text_productInfo1);
        payment = (TextView) findViewById(R.id.text_paidAmount1);

        postAttendance();

    }

    protected void postAttendance()
    {
        //setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);

        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PAYMENT_SUCCESS;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        //Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
                        Toast.makeText(getApplicationContext(), "Payment Gateway error", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "error6", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {


                        loading.dismiss();
                        payeeName.setText(Preferences.getInstance().Name);
                       // transactionStatus.setText(Preferences1.getInstance().paymentStatus);
                        //transactionId.setText(Preferences1.getInstance().transactionNumber);
                        payment.setText(Preferences.getInstance().paidAmount);
                        payFor.setText("Admission Fees");
                        if(value.matches("2"))
                        {
                            Utils.showToast(getApplicationContext(),"Brochure has been sent to your email-Id");
                        }
                        else
                        {
                            Utils.showToast(getApplicationContext(),"Brochure will be  getting delivered asap");
                        }

                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
                    Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_LONG).show();
                    loading.dismiss();
                }
                //setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
                loading.dismiss();
                //setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                //.getInstance().loadPreference(TeacherStudentAttendanceDetails.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
                //params.put("Students", jsonObject1.toString());




                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("user_id",Preferences.getInstance().userId);
                params.put("user_name",Preferences.getInstance().userName);
                params.put("user_phone",Preferences.getInstance().phoneNumber);
                params.put("institute_id",Preferences.getInstance().institutionId);
                params.put("school_id",Preferences.getInstance().schoolId);
                params.put("student_id", Preferences.getInstance().studentId);
                params.put("class_id",Preferences.getInstance().studentClassId);
                params.put("section_id",Preferences.getInstance().studentSectionId);
                params.put("admission_no",Preferences.getInstance().addmissionNumber);
                params.put("fees_amount",Preferences.getInstance().paidAmount);
                params.put("fine_amount","");
                params.put("interest_amount","");
                params.put("service_charges","");
                params.put("service_tax","");
                params.put("payment_bank","");
                params.put("payment_mode","");
                params.put("confirmation_no","");
                params.put("fee_type","");
                params.put("event_id","");
                params.put("value","3");
                params.put("req_ref_id",id);

                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }
}
