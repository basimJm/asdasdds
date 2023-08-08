package com.schoofi.activities;



import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
//import com.payUMoney.sdk.walledSdk.Preferences1;
//import com.paytm.pgsdk.PaytmMerchant;
//import com.paytm.pgsdk.PaytmOrder;
//import com.paytm.pgsdk.PaytmPGService;
//import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentSuccess extends AppCompatActivity {

    TextView payeeName,transactionStatus,transactionId,payment,payFor;
    ImageView back;
    String value,eventId,fees;
    RelativeLayout relativeLayout;
    String orderId,orderId1,mid1,status1,bankName1,txnAmount1,txnDate1,txnId1,response1,paymentMode1,banktxnId1,currency1,gatewayName1,isChecksumValid1,respMsg1,gross1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Payment Success");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_payment_success);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        value = getIntent().getStringExtra("value");
        eventId = getIntent().getStringExtra("eventId");
        fees = getIntent().getStringExtra("fees");

        payeeName = (TextView) findViewById(R.id.text_payeeName1);
        transactionId = (TextView) findViewById(R.id.text_transactionId1);
        transactionStatus = (TextView) findViewById(R.id.text_transactionStatus1);
        payFor = (TextView) findViewById(R.id.text_productInfo1);
        payment = (TextView) findViewById(R.id.text_paidAmount1);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        //initOrderId();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //onStartTransaction();








    }

    @Override
    protected void onStart(){
        super.onStart();
        //initOrderId();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //onStartTransaction(view1);
    }


    /*private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

    }

    public void onStartTransaction() {
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "Schoof97308364337259");
        paramMap.put("CUST_ID", "1234567890");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Education");
        paramMap.put("WEBSITE", "schoofiwap");
        paramMap.put("TXN_AMOUNT", fees);
        paramMap.put("THEME", "merchant");
        paramMap.put("EMAIL", Preferences.getInstance().userEmailId);
        paramMap.put("MOBILE_NO", "123");
        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
                "http://schoofi.com/generateChecksum.php",
                "http://schoofi.com/verifyChecksum.php");




        Service.initialize(Order, Merchant, null);

        Log.d("kkkjuy","");

        //Service.enableLog(getApplicationContext());

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionSuccess(final Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        Log.d("LOG", "Payment Transaction is successful " + inResponse.toString());
                        mid1 = inResponse.getString("MID");
                        orderId1 = inResponse.getString("ORDERID");
                        status1 = inResponse.getString("STATUS");
                        bankName1 = inResponse.getString("BANKNAME");
                        txnAmount1 = inResponse.getString("TXNAMOUNT");
                        txnDate1 = inResponse.getString("TXNDATE");
                        txnId1 = inResponse.getString("TXNID");
                        response1 = inResponse.getString("RESPCODE");
                        paymentMode1 = inResponse.getString("PAYMENTMODE");
                        banktxnId1 = inResponse.getString("BANKTXNID");
                        currency1 = inResponse.getString("CURRENCY");
                        gatewayName1 = inResponse.getString("GATEWAYNAME");
                        isChecksumValid1 = inResponse.getString("IS_CHECKSUM_VALID");
                        respMsg1 = inResponse.getString("RESPMSG");

                        Preferences.getInstance().paidAmount = orderId1;
                        Log.d("LOGD",""+Preferences.getInstance().paidAmount);
                        //Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                        relativeLayout.setVisibility(View.VISIBLE);

                        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();


                       // final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);

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
                                       // loading.dismiss();
                                    }
                                    else
                                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                                    {
                                        Toast.makeText(getApplicationContext(), "error6", Toast.LENGTH_LONG).show();
                                       // loading.dismiss();
                                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                                    }

                                    else
                                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                                    {


                                        //loading.dismiss();
                                        payeeName.setText(Preferences.getInstance().Name);
                                        transactionStatus.setText(status1);
                                        transactionId.setText(txnId1);
                                        payment.setText("Rs "+fees);
                                        payFor.setText(Preferences.getInstance().productInfo);

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
                                    //loading.dismiss();
                                }
                                //setProgressBarIndeterminateVisibility(false);

                            }}, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
                              //  loading.dismiss();
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
                                params.put("fees_amount",fees);
                                params.put("fine_amount","");
                                params.put("interest_amount","");
                                params.put("service_charges","");
                                params.put("service_tax","");
                                params.put("payment_bank",bankName1);
                                params.put("payment_mode",paymentMode1);
                                params.put("confirmation_no","");
                                params.put("fee_type","");
                                params.put("event_id",eventId);
                                params.put("value","1");
                                params.put("req_ref_id","");

                                return params;
                            }};

                        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                                25000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        queue.add(requestObject);
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     final Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        response1 = inResponse.getString("RESPCODE");
                        status1 = inResponse.getString("STATUS");
                        respMsg1 = inResponse.getString("RESPMSG");
                        orderId1 = inResponse.getString("ORDERID");
                        isChecksumValid1 = inResponse.getString("IS_CHECKSUM_VALID");
                        txnId1 = inResponse.getString("TXNID");
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                        Preferences.getInstance().paidAmount = orderId1;
                        Log.d("LOGD",""+Preferences.getInstance().paidAmount);
                        //Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                        relativeLayout.setVisibility(View.VISIBLE);
                        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
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
                                        // loading.dismiss();
                                    }
                                    else
                                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                                    {
                                        Toast.makeText(getApplicationContext(), "error6", Toast.LENGTH_LONG).show();
                                        // loading.dismiss();
                                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                                    }

                                    else
                                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                                    {


                                        //loading.dismiss();
                                        payeeName.setText(Preferences.getInstance().Name);
                                        transactionStatus.setText(status1);
                                        transactionId.setText(txnId1);
                                        payment.setText("Rs "+fees);
                                        payFor.setText(Preferences.getInstance().productInfo);

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
                                    //loading.dismiss();
                                }
                                //setProgressBarIndeterminateVisibility(false);

                            }}, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
                                //  loading.dismiss();
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
                                params.put("fees_amount",fees);
                                params.put("fine_amount","");
                                params.put("interest_amount","");
                                params.put("service_charges","");
                                params.put("service_tax","");
                                params.put("payment_bank","");
                                params.put("payment_mode","");
                                params.put("confirmation_no","");
                                params.put("fee_type","");
                                params.put("event_id",eventId);
                                params.put("value","1");
                                params.put("req_ref_id","");

                                return params;
                            }};

                        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                                25000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        queue.add(requestObject);
                    }
                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                });


        //finish();
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
                        transactionStatus.setText(Preferences1.getInstance().paymentStatus);
                        transactionId.setText(Preferences1.getInstance().transactionNumber);
                        payment.setText(Preferences.getInstance().paidAmount);
                        payFor.setText(Preferences.getInstance().productInfo);

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
                params.put("event_id",eventId);
                params.put("value","1");
                params.put("req_ref_id","");

                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }*/


}
