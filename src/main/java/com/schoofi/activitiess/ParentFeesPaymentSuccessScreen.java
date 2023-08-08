package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.schoofi.Utility.AvenuesParams;
import com.schoofi.Utility.ServiceUtility;
import com.schoofi.constants.AppConstants;
import com.schoofi.testotpappnew.*;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ParentFeesPaymentSuccessScreen extends AppCompatActivity {

    TextView payeeName,transactionStatus,transactionId,payment,payFor;
    ImageView back;
    String totalAmount,fee1_type_text,fee_type_text1,fee2_type_text,fee_type_text2,fee3_type_text,fee_type_text3,fee4_type_text,fee_type_text4,fee5_type_text,fee_type_text5,fee_srrvice_tax;

    float feeType11,feeType21,feeType31,feeType41,feeType51,interest,delayCharges,serviceTax1,handlingCharges;

    float handlingCharges11=0;
    float total=0,total1=0,total2=0,finalTotalAmount=0;
    int count;
    String array,array1,array2,array3,array4,array5,array6,array7,array8,array9,array10,gross,serviceTax;
    String orderId;
    Button submit;
    RelativeLayout relativeLayout;
    String phone,value;
    String orderId1,mid1,status1,bankName1,txnAmount1,txnDate1,txnId1,response1,paymentMode1,banktxnId1,currency1,gatewayName1,isChecksumValid1,respMsg1,gross1;
   // String fee_type_text11,fee_type_text21,fee_type_text31,fee_type_text41,fee_type_text51,interest1,delay_charges1,feeServiceTax11,feeHandlingCharges11,totalAmount1,stu_fee_date_id,count,feeStartDate,feeEndDate,days1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Parent Fees Payment Success Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_parent_fees_payment_success_screen);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentFeesPaymentSuccessScreen.this,ParentFees.class);
                startActivity(intent);
                finish();
            }
        });



        array = getIntent().getStringExtra("array");
        array1 = getIntent().getStringExtra("array1");
        array2 = getIntent().getStringExtra("array2");
        array3 = getIntent().getStringExtra("array3");
        array4 = getIntent().getStringExtra("array4");
        array5 = getIntent().getStringExtra("array5");
        array6 = getIntent().getStringExtra("array6");
        array7 = getIntent().getStringExtra("array7");
        array8 = getIntent().getStringExtra("array8");
        array9 = getIntent().getStringExtra("array9");
        array10 = getIntent().getStringExtra("array10");
        count = getIntent().getExtras().getInt("count");
        gross1 = getIntent().getStringExtra("gross");
        serviceTax = getIntent().getStringExtra("serviceTax");
        value = getIntent().getStringExtra("value");

        Float a;

        a= Float.parseFloat(gross1);

        gross = String.valueOf(Math.round(a));

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        relativeLayout.setVisibility(View.INVISIBLE);

        /*interest = getIntent().getExtras().getFloat("interest");
        delayCharges = getIntent().getExtras().getFloat("delay_charges");
        fee_srrvice_tax = getIntent().getStringExtra("fees_service_tax");
        serviceTax = getIntent().getExtras().getFloat("feeServiceTax");
        handlingCharges = getIntent().getExtras().getFloat("feeHandlingCharges");
        totalAmount = getIntent().getStringExtra("totalAmount");
        finalTotalAmount = getIntent().getExtras().getFloat("finalTotalAmount");
        fee_type_text11 = getIntent().getStringExtra("fee_type_text11");
        fee_type_text21 = getIntent().getStringExtra("fee_type_text21");
        fee_type_text31 = getIntent().getStringExtra("fee_type_text31");
        fee_type_text41 = getIntent().getStringExtra("fee_type_text41");
        fee_type_text51 = getIntent().getStringExtra("fee_type_text51");
        interest1 = getIntent().getStringExtra("interest1");
        delay_charges1 = getIntent().getStringExtra("delay_charges1");
        feeServiceTax11 = getIntent().getStringExtra("feeServiceTax1");
        feeHandlingCharges11 = getIntent().getStringExtra("feeHandlingCharges1");
        totalAmount1 = getIntent().getStringExtra("totalAmount1");
        stu_fee_date_id = getIntent().getStringExtra("stu_fee_date_id");
        count = getIntent().getStringExtra("count");
        feeStartDate = getIntent().getStringExtra("fee_start_date");
       // Utils.showToast(getApplicationContext(),stu_fee_date_id.toString());
        feeEndDate = getIntent().getStringExtra("fee_end_date");
        days1 = getIntent().getStringExtra("days");*/




        payeeName = (TextView) findViewById(R.id.text_payeeName1);
        transactionId = (TextView) findViewById(R.id.text_transactionId1);
        transactionStatus = (TextView) findViewById(R.id.text_transactionStatus1);
        payFor = (TextView) findViewById(R.id.text_productInfo1);
        payment = (TextView) findViewById(R.id.text_paidAmount1);


        if(value.matches("PAYTM")) {


            initOrderId();
            initPhone();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            onStartTransaction();
        }

        else
        {
            //init();
            //generating order number
            initOrderId();

            String vAccessCode = "AVLO72EG00BL21OLLB";
            String vMerchantId = "140638";
            String vCurrency = "INR";
            String vAmount = gross;
            if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
                Intent intent = new Intent(this, com.schoofi.testotpappnew.WebViewActivity.class);
                intent.putExtra(AvenuesParams.ACCESS_CODE, "AVLO72EG00BL21OLLB");
                intent.putExtra(AvenuesParams.MERCHANT_ID, "140638");
                intent.putExtra(AvenuesParams.ORDER_ID, orderId);
                intent.putExtra(AvenuesParams.CURRENCY, "INR");
                intent.putExtra(AvenuesParams.AMOUNT, gross);

                intent.putExtra(AvenuesParams.REDIRECT_URL, "http://schoofi.com/cctest/ccavresp.php");
                intent.putExtra(AvenuesParams.CANCEL_URL, "http://schoofi.com/cctest/ccavresp.php");
                intent.putExtra(AvenuesParams.RSA_KEY_URL, "http://schoofi.com/cctest/getRSA1.php");

                intent.putExtra("array", array);
                intent.putExtra("array1", array1);
                intent.putExtra("array2", array2);
                intent.putExtra("array3", array3);
                intent.putExtra("array4", array4);
                intent.putExtra("array5", array5);
                intent.putExtra("array6", array6);
                intent.putExtra("array7", array7);
                intent.putExtra("array8", array8);
                intent.putExtra("array9", array9);
                intent.putExtra("array10", array10);
                intent.putExtra("count", count);
                intent.putExtra("gross", gross);
                intent.putExtra("value",value);
                intent.putExtra("serviceTax", serviceTax);
                //intent.putExtra("net_fee_amount", netFeeAmount);

                startActivity(intent);
                finish();
            }else{
                showToast("All parameters are mandatory.");
            }
        }



    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ParentFeesPaymentSuccessScreen.this,ParentFees.class);
        startActivity(intent);
        finish();
    }

    protected void postAttendance()
    {
        //setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);

        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PARENT_FEES_PAYMENT_SUCCESS;

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
                        Toast.makeText(getApplicationContext(), "session expired!!!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {


                        loading.dismiss();
                        payeeName.setText(Preferences.getInstance().Name);
                        transactionStatus.setText(respMsg1);
                        transactionId.setText(txnId1);
                        payment.setText(gross);
                        payFor.setText("School Fees");


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




                params.put("count", String.valueOf(count));
                params.put("stu_fee_date_id", array);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("u_name",Preferences.getInstance().userName);
                params.put("u_phone",Preferences.getInstance().phoneNumber);
                params.put("institute_id",Preferences.getInstance().institutionId);
                params.put("school_id",Preferences.getInstance().schoolId);
                params.put("stu_id", Preferences.getInstance().studentId);
                params.put("class_id",Preferences.getInstance().studentClassId);
                params.put("section_id",Preferences.getInstance().studentSectionId);
                params.put("admission_no",Preferences.getInstance().addmissionNumber);
                params.put("total_fee_amount",array10);
                params.put("delay_fine_amount",array2);
                params.put("interest_amount",array1);
                params.put("service_charges",serviceTax);
                params.put("fees_service_tax",array4);
                params.put("BANKNAME",bankName1);
                params.put("PAYMENTMODE",paymentMode1);
                params.put("BANKTXNID",banktxnId1);
                params.put("STATUS",status1);
                params.put("fee_duration","");
                params.put("fee_payment_no","");
                params.put("TXNID",txnId1);
                params.put("fee_start_date",array5);
                params.put("fee_end_date",array6);
                params.put("final_fee_amount",array7);
                params.put("discount_percent","");
                params.put("handling_charges",array3);
                params.put("days",array9);
                params.put("TXNAMOUNT",String.valueOf(gross));
                params.put("discount_amount",array8);
                params.put("session",Preferences.getInstance().session1);
                params.put("TXNDATE",txnDate1);
                params.put("CURRENCY",currency1);
                params.put("GATEWAYNAME",gatewayName1);
                params.put("IS_CHECKSUM_VALID",isChecksumValid1);
                params.put("RESPCODE",response1);
                params.put("ORDERID",orderId1);
                params.put("RESPMSG",respMsg1);
                params.put("value","1");
                params.put("json_string","");
                params.put("checksum_hash","");

               //System.out.println(array+"1:"+array1+"2:"+array2+"3:"+array3+"4:"+array4+"5:"+array5+"6:"+array6+"7:"+array7+"8:"+array8+"9:"+array9+"10:"+array10+"TXn Amount:"+String.valueOf(gross)+"session:"+Preferences.getInstance().session1+"studentId"+Preferences.getInstance().studentId+"classId"+Preferences.getInstance().studentClassId+"userId:"+Preferences.getInstance().userId);

                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }

    @Override
    protected void onStart(){
        super.onStart();
        initOrderId();
        initPhone();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //onStartTransaction(view1);
    }


    private void initOrderId() {
        Preferences.getInstance().loadPreference(getApplicationContext());
        Random r = new Random(System.currentTimeMillis());
        orderId = Preferences.getInstance().schoolId + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

    }

    private void initPhone() {
        Random r = new Random(System.currentTimeMillis());
        phone = String.valueOf((1 + r.nextInt(2)) * 1000000000
                + r.nextInt(2000000000));

        Log.d("kk",phone);

    }

    public void onStartTransaction() {
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "Schoof97308364337259");
        paramMap.put("CUST_ID", Preferences.getInstance().studentId);
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Education");
        paramMap.put("WEBSITE", "schoofiwap");
        paramMap.put("TXN_AMOUNT", gross);
        paramMap.put("THEME", "merchant");
        paramMap.put("EMAIL", Preferences.getInstance().userEmailId);
        paramMap.put("MOBILE_NO", phone);
        PaytmOrder Order = new PaytmOrder(paramMap);


        //Log.d("debugging ",orderId+"Cus"+Preferences.getInstance().studentId)

        PaytmMerchant Merchant = new PaytmMerchant(
                "https://schoofi.com/generateChecksum.php",
                "https://schoofi.com/verifyChecksum.php");




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


                       // final ProgressDialog loading = ProgressDialog.show(this.getApplicationContext(), "Uploading...", "Please wait...", false, false);

                        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PARENT_FEES_PAYMENT_SUCCESS;

                        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject responseObject;
                                System.out.println(response);
                                //Utils.showToast(getApplicationContext(), ""+response);
                                //System.out.println(url1);
                                try
                                {
                                    responseObject = new JSONObject(response);

                                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                                    {

                                        //Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
                                        Toast.makeText(getApplicationContext(), "Payment Gateway error", Toast.LENGTH_LONG).show();
                                        //loading.dismiss();
                                    }
                                    else
                                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                                    {
                                        Toast.makeText(getApplicationContext(), "session expired!!!", Toast.LENGTH_LONG).show();
                                       // loading.dismiss();
                                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                                    }

                                    else
                                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                                    {


                                      // loading.dismiss();
                                        payeeName.setText(Preferences.getInstance().Name);
                                        transactionStatus.setText(respMsg1);
                                        transactionId.setText(txnId1);
                                        payment.setText(gross);
                                        payFor.setText("School Fees");


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
                                    Toast.makeText(getApplicationContext(), "something went wrong!!", Toast.LENGTH_LONG).show();
                                    //loading.dismiss();
                                }
                                //setProgressBarIndeterminateVisibility(false);

                            }}, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(), "something went wrong!!!", Toast.LENGTH_LONG).show();
                                //loading.dismiss();
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




                                params.put("count", String.valueOf(count));
                               /* *//*Log.d("hjy",""+bankName1+paymentMode1+status1+txnId1+txnDate1+currency1+gatewayName1+isChecksumValid1+respMsg1+orderId1+response1);
                                Log.d("pay1",""+count);
                                Log.d("pay2",""+array.toString());
                                Log.d("pay3",""+array1.toString());
                                Log.d("pay4",""+array2.toString());
                                Log.d("pay5",""+array3.toString());
                                Log.d("pay6",""+array4.toString());
                                Log.d("pay7",""+array5.toString());
                                Log.d("pay8",""+array6.toString());
                                Log.d("pay9",""+array7.toString());
                                Log.d("pay10",""+array8.toString());
                                Log.d("pay11",""+array9.toString());
                                Log.d("pay12",""+Preferences.getInstance().userId);
                                Log.d("pay13",""+Preferences.getInstance().userName);
                                Log.d("pay14",""+Preferences.getInstance().phoneNumber);
                                Log.d("pay15",""+Preferences.getInstance().institutionId);
                                Log.d("pay16",""+Preferences.getInstance().studentClassId);
                                Log.d("pay17",""+Preferences.getInstance().schoolId);
                                Log.d("pay18",""+Preferences.getInstance().studentId);
                                Log.d("pay19",""+Preferences.getInstance().studentSectionId);
                                Log.d("pay20",""+Preferences.getInstance().addmissionNumber);
                                Log.d("pay21",""+gross);
                                Log.d("pay22",""+serviceTax);
                                Log.d("pay21",""+gross);
                                Log.d("pay21",""+gross);
                                Log.d("pay21",""+gross);
                                Log.d("pay21",""+gross);*//**//**/

                                params.put("stu_fee_date_id", array);
                                params.put("u_id",Preferences.getInstance().userId);
                                params.put("u_name",Preferences.getInstance().userName);
                                params.put("u_phone","");
                                params.put("institute_id",Preferences.getInstance().institutionId);
                                params.put("school_id",Preferences.getInstance().schoolId);
                                params.put("stu_id", Preferences.getInstance().studentId);
                                params.put("class_id",Preferences.getInstance().studentClassId);
                                params.put("section_id",Preferences.getInstance().studentSectionId);
                                params.put("admission_no",Preferences.getInstance().addmissionNumber);
                                params.put("total_fee_amount",array10);
                                params.put("delay_fine_amount",array2);
                                params.put("interest_amount",array1);
                                params.put("service_charges",serviceTax);
                                params.put("fees_service_tax",array4);
                                params.put("BANKNAME",bankName1);

                                params.put("PAYMENTMODE",paymentMode1);
                                params.put("BANKTXNID",banktxnId1);
                                params.put("STATUS",status1);
                                params.put("fee_duration","");
                                params.put("fee_payment_no","");
                                params.put("TXNID",txnId1);
                                params.put("fee_start_date",array5);
                                params.put("fee_end_date",array6);
                                params.put("final_amount",array7.toString());
                                params.put("discount_percent","");
                                params.put("handling_charges",array3);
                                params.put("days",array9);
                                params.put("TXNAMOUNT",String.valueOf(gross));
                                params.put("discount_amount",array8);
                                params.put("session",Preferences.getInstance().session1);
                                params.put("TXNDATE",txnDate1);
                                params.put("CURRENCY",currency1);
                                params.put("GATEWAYNAME",gatewayName1);
                                params.put("IS_CHECKSUM_VALID",isChecksumValid1);
                                params.put("RESPCODE",response1);
                                params.put("ORDERID",orderId1);
                                params.put("RESPMSG",respMsg1);
                                params.put("value","1");
                                params.put("json_string",inResponse.toString());
                                params.put("checksum_hash","");

                                // System.out.print(array+"1:"+array1+"2:"+array2+"3:"+array3+"4:"+array4+"5:"+array5+"6:"+array6+"7:"+array7+"8:"+array8);

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


                        // final ProgressDialog loading = ProgressDialog.show(this.getApplicationContext(), "Uploading...", "Please wait...", false, false);

                        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PARENT_FEES_PAYMENT_SUCCESS;

                        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject responseObject;
                                System.out.println(response);
                                //Utils.showToast(getApplicationContext(), ""+response);
                                //System.out.println(url1);
                                try
                                {
                                    responseObject = new JSONObject(response);

                                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                                    {

                                        //Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
                                        Toast.makeText(getApplicationContext(), "Payment Gateway error", Toast.LENGTH_LONG).show();
                                        //loading.dismiss();
                                    }
                                    else
                                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                                    {
                                        Toast.makeText(getApplicationContext(), "session expired!!!", Toast.LENGTH_LONG).show();
                                        // loading.dismiss();
                                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                                    }

                                    else
                                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                                    {


                                        // loading.dismiss();
                                        payeeName.setText(Preferences.getInstance().Name);
                                        transactionStatus.setText(respMsg1);
                                        transactionId.setText(txnId1);
                                        payment.setText("Rs" +gross);
                                        payFor.setText("School Fees");


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
                                    Toast.makeText(getApplicationContext(), "something went wrong!!", Toast.LENGTH_LONG).show();
                                    //loading.dismiss();
                                }
                                //setProgressBarIndeterminateVisibility(false);

                            }}, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(), "something went wrong!!!", Toast.LENGTH_LONG).show();
                                //loading.dismiss();
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




                                params.put("count", String.valueOf(count));
                                /*Log.d("hjy",""+bankName1+paymentMode1+status1+txnId1+txnDate1+currency1+gatewayName1+isChecksumValid1+respMsg1+orderId1+response1);
                                Log.d("pay1",""+count);
                                Log.d("pay2",""+array.toString());
                                Log.d("pay3",""+array1.toString());
                                Log.d("pay4",""+array2.toString());
                                Log.d("pay5",""+array3.toString());
                                Log.d("pay6",""+array4.toString());
                                Log.d("pay7",""+array5.toString());
                                Log.d("pay8",""+array6.toString());
                                Log.d("pay9",""+array7.toString());
                                Log.d("pay10",""+array8.toString());
                                Log.d("pay11",""+array9.toString());
                                Log.d("pay12",""+Preferences.getInstance().userId);
                                Log.d("pay13",""+Preferences.getInstance().userName);
                                Log.d("pay14",""+Preferences.getInstance().phoneNumber);
                                Log.d("pay15",""+Preferences.getInstance().institutionId);
                                Log.d("pay16",""+Preferences.getInstance().studentClassId);
                                Log.d("pay17",""+Preferences.getInstance().schoolId);
                                Log.d("pay18",""+Preferences.getInstance().studentId);
                                Log.d("pay19",""+Preferences.getInstance().studentSectionId);
                                Log.d("pay20",""+Preferences.getInstance().addmissionNumber);
                                Log.d("pay21",""+gross);
                                Log.d("pay22",""+serviceTax);
                                Log.d("pay21",""+gross);
                                Log.d("pay21",""+gross);
                                Log.d("pay21",""+gross);
                                Log.d("pay21",""+gross);*//**//**/

                                params.put("stu_fee_date_id", array);
                                params.put("u_id",Preferences.getInstance().userId);
                                params.put("u_name",Preferences.getInstance().userName);
                                params.put("u_phone","");
                                params.put("institute_id",Preferences.getInstance().institutionId);
                                params.put("school_id",Preferences.getInstance().schoolId);
                                params.put("stu_id", Preferences.getInstance().studentId);
                                params.put("class_id",Preferences.getInstance().studentClassId);
                                params.put("section_id",Preferences.getInstance().studentSectionId);
                                params.put("admission_no",Preferences.getInstance().addmissionNumber);
                                params.put("total_fee_amount",array10);
                                params.put("delay_fine_amount",array2);
                                params.put("interest_amount",array1);
                                params.put("service_charges",serviceTax);
                                params.put("fees_service_tax",array4);
                                params.put("BANKNAME","");

                                params.put("PAYMENTMODE","");
                                params.put("BANKTXNID","");
                                params.put("STATUS",status1);
                                params.put("fee_duration","");
                                params.put("fee_payment_no","");
                                params.put("TXNID",txnId1);
                                params.put("fee_start_date",array5);
                                params.put("fee_end_date",array6);
                                params.put("final_amount",array7.toString());
                                params.put("discount_percent","");
                                params.put("handling_charges",array3);
                                params.put("days",array9);
                                params.put("TXNAMOUNT",String.valueOf(gross));
                                params.put("discount_amount",array8);
                                params.put("session",Preferences.getInstance().session1);
                                params.put("TXNDATE","");
                                params.put("CURRENCY","");
                                params.put("GATEWAYNAME","");
                                params.put("IS_CHECKSUM_VALID",isChecksumValid1);
                                params.put("RESPCODE",response1);
                                params.put("ORDERID",orderId1);
                                params.put("RESPMSG",respMsg1);
                                params.put("value","1");
                                params.put("json_string",inResponse.toString());
                                params.put("checksum_hash","");

                                // System.out.print(array+"1:"+array1+"2:"+array2+"3:"+array3+"4:"+array4+"5:"+array5+"6:"+array6+"7:"+array7+"8:"+array8);

                                return params;
                            }};

                        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                                500000,
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


}
