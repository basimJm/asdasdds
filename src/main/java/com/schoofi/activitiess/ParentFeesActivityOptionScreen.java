package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
//import com.payUMoney.sdk.PayUmoneySdkInitilizer;
//import com.payUMoney.sdk.SdkConstants;
//import com.payUMoney.sdk.walledSdk.Preferences1;
//import com.paytm.pgsdk.PaytmMerchant;
//import com.paytm.pgsdk.PaytmOrder;
//import com.paytm.pgsdk.PaytmPGService;
//import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.schoofi.activities.PaymentSuccess;
import com.schoofi.fragments.ParentStudentFeesUnpaidNewMultilevel;
import com.schoofi.testotpappnew.InitialActivity;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ParentFeesActivityOptionScreen extends AppCompatActivity {


    Button pollOption1,pollOption2,submit;
    ImageView pollOptionImage1,pollOptionImage2;
    String value;
    boolean c;
    HashMap<String, String> params = new HashMap<>();
    String eventId;

    public static final String TAG = "PayUMoneySDK Sample";

    String totalAmount,fee1_type_text,fee_type_text1,fee2_type_text,fee_type_text2,fee3_type_text,fee_type_text3,fee4_type_text,fee_type_text4,fee5_type_text,fee_type_text5,fee_srrvice_tax;

    float feeType11,feeType21,feeType31,feeType41,feeType51,interest,delayCharges,serviceTax1,handlingCharges;

    float handlingCharges11=0;
    int count;
    String array,array1,array2,array3,array4,array5,array6,array7,array8,array9,array10,gross,serviceTax,orderId,customerId,netFeeAmount;
    float total=0,total1=0,total2=0,finalTotalAmount=0;
    //String fee_type_text11,fee_type_text21,fee_type_text31,fee_type_text41,fee_type_text51,interest1,delay_charges1,feeServiceTax11,feeHandlingCharges11,totalAmount1,stu_fee_date_id,count,feeStartDate,feeEndDate,days1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Parent Fees Activity Option Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_parent_fees_activity_option_screen);
        pollOption1 = (Button) findViewById(R.id.btn_pollOption1);
        pollOption2 = (Button) findViewById(R.id.btn_pollOption2);
        pollOptionImage1 = (ImageView) findViewById(R.id.image_poll1);
        pollOptionImage2 = (ImageView) findViewById(R.id.image_poll2);
        submit = (Button) findViewById(R.id.btn_submit);
        /*interest = getIntent().getExtras().getFloat("interest");
        delayCharges = getIntent().getExtras().getFloat("delay_charges");
        fee_srrvice_tax = getIntent().getStringExtra("fees_service_tax");
        serviceTax = getIntent().getExtras().getFloat("feeServiceTax");
        handlingCharges = getIntent().getExtras().getFloat("feeHandlingCharges");
        totalAmount = getIntent().getStringExtra("totalAmount");
        finalTotalAmount = getIntent().getExtras().getFloat("finalTotalAmount");*/
        /*fee_type_text11 = getIntent().getStringExtra("fee_type_text11");
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
        feeEndDate = getIntent().getStringExtra("fee_end_date");
        days1 = getIntent().getStringExtra("days");
*/
        pollOptionImage1.setVisibility(View.INVISIBLE);
        pollOptionImage2.setVisibility(View.INVISIBLE);
        pollOption2.setVisibility(View.VISIBLE);
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
        gross = getIntent().getStringExtra("gross");
        serviceTax = getIntent().getStringExtra("serviceTax");
        netFeeAmount = getIntent().getStringExtra("net_fee_amount");

        Preferences.getInstance().paidAmount = gross;
        Preferences.getInstance().savePreference(getApplicationContext());

        pollOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                value = pollOption1.getText().toString();
                pollOptionImage1.setVisibility(View.VISIBLE);
                pollOptionImage2.setVisibility(View.INVISIBLE);
                c = true;
            }
        });

        pollOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                value = pollOption2.getText().toString();
                pollOptionImage2.setVisibility(View.VISIBLE);
                pollOptionImage1.setVisibility(View.INVISIBLE);
                c = true;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                if(c==false)
                {
                    Utils.showToast(getApplicationContext(),"Please select the payment option");
                }

                else
                {
                    /*initOrderId();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    onStartTransaction(view1);*/

                    if(value.matches("PAYTM")) {

                        Intent intent = new Intent(ParentFeesActivityOptionScreen.this, ParentFeesPaymentSuccessScreen.class);
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
                        intent.putExtra("net_fee_amount", netFeeAmount);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        Intent intent = new Intent(ParentFeesActivityOptionScreen.this, ParentFeesPaymentSuccessScreen.class);
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
                        intent.putExtra("net_fee_amount", netFeeAmount);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();
                    }


                }
            }
        });






    }


    @Override
    protected void onStart(){
        super.onStart();
        //initOrderId();
        //initCustomerId();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //onStartTransaction(view1);
    }


    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
         orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

    }

    private void initCustomerId() {
        Random r = new Random(System.currentTimeMillis());
        customerId = "CUSTOMER" + (8 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void makePayment(View view) {
        Double amount =0.0;
        if(isDouble(String.valueOf(gross))){
            amount = Double.parseDouble(String.valueOf(gross));
        }else{
            Toast.makeText(getApplicationContext(), "Enter correct amount", Toast.LENGTH_LONG).show();
            return ;
        }
        if (amount <= 0.0) {
            Toast.makeText(getApplicationContext(), "Enter Some amount", Toast.LENGTH_LONG).show();
        } else if (amount > 1000000.00) {
            Toast.makeText(getApplicationContext(), "Amount exceeding the limit : 1000000.00 ", Toast.LENGTH_LONG).show();
        } else {

            //PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder(  );

            //builder.setKey(""); //Put your live KEY here
           // builder.setSalt(""); //Put your live SALT here

          //  builder.setMerchantId(""); //Put your live MerchantId here



          //  builder.setIsDebug(true);
            //builder.setDebugKey("4UjvqJCD");// Debug Key
            //builder.setDebugMerchantId("4936099");// Debug Merchant ID
            //builder.setDebugSalt("CLptFu8bhA");// Debug Salt


            if(String.valueOf(gross) != null && String.valueOf(gross).toString().isEmpty()) {
               // builder.setAmount(10);
            } else {
               // builder.setAmount(amount);// debug
            }


           // builder.setTnxId("0nf7");



//            builder.setPhone("9999999999");
//
//
//
//            builder.setProductName("product_name");
//
//
//
//            builder.setFirstName("piyush");
//
//
//            builder.setEmail("piyush.jain@payu.in");
//
//
//
//            builder.setsUrl("https://mobiletest.payumoney.com/mobileapp/payumoney/success.php");
//
//
//
//            builder.setfUrl("https://mobiletest.payumoney.com/mobileapp/payumoney/failure.php");
//
//
//
//            builder.setUdf1("");
//
//
//
//            builder.setUdf2("");
//
//
//
//            builder.setUdf3("");
//
//
//
//            builder.setUdf4("");
//
//
//
//            builder.setUdf5("");
//
//
//            PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();
//
//            PayUmoneySdkInitilizer.startPaymentActivityForResult(this,paymentParam);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        Preferences1.getInstance().loadPreference(getApplicationContext());
//
//
//        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK) {
//                final ProgressDialog dialog;
//                dialog = new ProgressDialog(ParentFeesActivityOptionScreen.this, AlertDialog.THEME_HOLO_DARK);
//                dialog.setCancelable(false);
//                dialog.setMessage("Loading...");
//                dialog.show();
//                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
//                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
//                //showDialogMessage( "Payment Success Id : " + paymentId);
//                Preferences1.getInstance().paymentId = paymentId;
//                Preferences1.getInstance().paymentStatus = "Success";
//                Preferences1.getInstance().savePreference(getApplicationContext());
//                dialog.dismiss();
//                Intent intent = new Intent(ParentFeesActivityOptionScreen.this,ParentFeesPaymentSuccessScreen.class);
//                intent.putExtra("array",array);
//                intent.putExtra("array1",array1);
//                intent.putExtra("array2",array2);
//                intent.putExtra("array3",array3);
//                intent.putExtra("array4",array4);
//                intent.putExtra("array5",array5);
//                intent.putExtra("array6",array6);
//                intent.putExtra("array7",array7);
//                intent.putExtra("array8",array8);
//                intent.putExtra("count",count);
//                intent.putExtra("gross",gross);
//                intent.putExtra("serviceTax",serviceTax);
               /* *//*intent.putExtra("fee_type_text111",fee_type_text11);
                intent.putExtra("fee_type_text211",fee_type_text21);
                intent.putExtra("fee_type_text311",fee_type_text31);
                intent.putExtra("fee_type_text411",fee_type_text41);
                intent.putExtra("fee_type_text511",fee_type_text51);*//*
                intent.putExtra("interest",interest);
                intent.putExtra("delay_charges",delayCharges);
                *//*intent.putExtra("fee1_type_text",fee2_type_text);
                intent.putExtra("fee_type_text1",fee1_type_text);
                intent.putExtra("fee2_type_text",fee4_type_text);
                intent.putExtra("fee_type_text2",fee3_type_text);
                intent.putExtra("fee3_type_text",fee6_type_text);
                intent.putExtra("fee_type_text3",fee5_type_text);
                intent.putExtra("fee4_type_text",fee8_type_text);
                intent.putExtra("fee_type_text4",fee7_type_text);
                intent.putExtra("fee5_type_text",fee10_type_text);
                intent.putExtra("fee_type_text5",fee9_type_text);*//*
                intent.putExtra("fees_service_tax",fee_srrvice_tax);
                intent.putExtra("feeServiceTax",serviceTax);
                intent.putExtra("feeHandlingCharges",handlingCharges);
                intent.putExtra("totalAmount",totalAmount);
                intent.putExtra("finalTotalAmount",finalTotalAmount);
                *//*intent.putExtra("fee_type_text11",fee_type_text11);
                intent.putExtra("fee_type_text21",fee_type_text21);
                intent.putExtra("fee_type_text31",fee_type_text31);
                intent.putExtra("fee_type_text41",fee_type_text41);
                intent.putExtra("fee_type_text51",fee_type_text51);
                intent.putExtra("interest1",interest1);
                intent.putExtra("delay_charges1",delay_charges1);
                intent.putExtra("feeServiceTax1",feeServiceTax11);
                intent.putExtra("feeHandlingCharges1",feeHandlingCharges11);
                intent.putExtra("totalAmount1",totalAmount1);
                intent.putExtra("stu_fee_date_id",stu_fee_date_id);
                intent.putExtra("count",String.valueOf(count));
                intent.putExtra("fee_start_date",feeStartDate);
                intent.putExtra("fee_end_date",feeEndDate);
                intent.putExtra("days",days1);*/
//                startActivity(intent);
//                finish();
//            }
//            else if (resultCode == RESULT_CANCELED) {
//                Log.i(TAG, "failure");
//                showDialogMessage("cancelled");
//            }else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
//                Log.i("app_activity", "failure");
//
//                if (data != null) {
//                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {
//
//                    } else {
//                        showDialogMessage("failure");
//                    }
//                }
//                //Write your code if there's no result
//            }
//
//            else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
//                Log.i(TAG, "User returned without login");
//                showDialogMessage( "User returned without login");
//            }
//        }

    }

    private void showDialogMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    /*public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "Schoof23231232963213");
        paramMap.put("CUST_ID", Preferences.getInstance().studentId);
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "AppURL");
        paramMap.put("TXN_AMOUNT", "789.98");
        paramMap.put("THEME", "merchant");
        paramMap.put("EMAIL", "abc@gmail.com");
        paramMap.put("MOBILE_NO", "123");
        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
                "http://schoofi.com/generateChecksum.php",
                "http://schoofi.com/verifyChecksum.php");

        Service.initialize(Order, Merchant, null);

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
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        String mid = inResponse.getString("MID");
                        String orderId = inResponse.getString("ORDERID");
                        Preferences.getInstance().paidAmount = orderId;
                        Log.d("LOGD",""+Preferences.getInstance().paidAmount);
                        Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();



                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
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
    }*/
}
