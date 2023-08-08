//package com.schoofi.activities;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
//import com.payUMoney.sdk.PayUmoneySdkInitilizer;
//import com.payUMoney.sdk.SdkConstants;
//import com.payUMoney.sdk.utils.SdkHelper;
//import com.payUMoney.sdk.walledSdk.Preferences1;
//import com.schoofi.activitiess.R;
//import com.schoofi.utils.Preferences;
//import com.schoofi.utils.SchoofiApplication;
//import com.schoofi.utils.TrackerName;
//
//import java.util.HashMap;
//
//
//public class PaymentActivity extends Activity {
//
//
//    EditText amt = null, txnid = null, phone = null, pinfo = null, fname = null, email = null, surl = null, furl = null, mid = null, udf1 = null, udf2 = null, udf3 = null, udf4 = null, udf5 = null;
//    Button pay = null;
//
//    HashMap<String, String> params = new HashMap<>();
//
//    public static final String TAG = "PayUMoneySDK Sample";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);
//
//        // Set screen name.
//        t.setScreenName("Payment Activity");
//
//        // Send a screen view.
//        t.send(new HitBuilders.ScreenViewBuilder().build());
//        setContentView(R.layout.activity_payment);
//        //setContentView(R.layout.activity_my);
//
//        amt = (EditText) findViewById(R.id.amount);
//        txnid = (EditText) findViewById(R.id.txnid);
//        mid = (EditText) findViewById(R.id.merchant_id);
//        phone = (EditText) findViewById(R.id.phone);
//        pinfo = (EditText) findViewById(R.id.pinfo);
//        fname = (EditText) findViewById(R.id.fname);
//        email = (EditText) findViewById(R.id.email);
//        surl = (EditText) findViewById(R.id.surl);
//        furl = (EditText) findViewById(R.id.furl);
//        udf1 = (EditText) findViewById(R.id.udf1);
//        udf2 = (EditText) findViewById(R.id.udf2);
//        udf3 = (EditText) findViewById(R.id.udf3);
//        udf4 = (EditText) findViewById(R.id.udf4);
//        udf5 = (EditText) findViewById(R.id.udf5);
//        furl = (EditText) findViewById(R.id.furl);
//        pay = (Button) findViewById(R.id.pay);
//
//        txnid.setVisibility(View.INVISIBLE);
//        mid.setVisibility(View.INVISIBLE);
//        phone.setVisibility(View.INVISIBLE);
//        pinfo.setVisibility(View.INVISIBLE);
//        fname.setVisibility(View.INVISIBLE);
//        email.setVisibility(View.INVISIBLE);
//        surl.setVisibility(View.INVISIBLE);
//        furl.setVisibility(View.INVISIBLE);
//        udf1.setVisibility(View.INVISIBLE);
//        udf2.setVisibility(View.INVISIBLE);
//        udf3.setVisibility(View.INVISIBLE);
//        udf4.setVisibility(View.INVISIBLE);
//        udf5.setVisibility(View.INVISIBLE);
//    }
//
//    private boolean isDouble(String str) {
//        try {
//            Double.parseDouble(str);
//            return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//
//    public void makePayment(View view) {
//        Double amount =0.0;
//        if(isDouble(amt.getText().toString())){
//            amount = Double.parseDouble(amt.getText().toString());
//        }else{
//            Toast.makeText(getApplicationContext(), "Enter correct amount", Toast.LENGTH_LONG).show();
//            return ;
//        }
//        if (amount <= 0.0) {
//            Toast.makeText(getApplicationContext(), "Enter Some amount", Toast.LENGTH_LONG).show();
//        } else if (amount > 1000000.00) {
//            Toast.makeText(getApplicationContext(), "Amount exceeding the limit : 1000000.00 ", Toast.LENGTH_LONG).show();
//        } else {
//
//            PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder(  );
//
//            builder.setKey(""); //Put your live KEY here
//            builder.setSalt(""); //Put your live SALT here
//            if(mid != null && mid.getText().toString().isEmpty()) {
//                builder.setMerchantId(""); //Put your live MerchantId here
//            } else {
//                builder.setMerchantId(mid.getText().toString());
//            }
//
//
//            builder.setIsDebug(true);
//            builder.setDebugKey("4UjvqJCD");// Debug Key
//            builder.setDebugMerchantId("4936099");// Debug Merchant ID
//            builder.setDebugSalt("CLptFu8bhA");// Debug Salt
//
//
//            if(amt != null && amt.getText().toString().isEmpty()) {
//                builder.setAmount(10);
//            } else {
//                builder.setAmount(amount);// debug
//            }
//
//            if(txnid != null && txnid.getText().toString().isEmpty()) {
//                builder.setTnxId("0nf7");
//            } else {
//                builder.setTnxId(txnid.getText().toString());
//            }
//
//            if(phone != null && phone.getText().toString().isEmpty()) {
//                builder.setPhone("8882434664");
//            } else {
//                builder.setPhone(phone.getText().toString());// debug
//            }
//
//            if(pinfo != null && pinfo.getText().toString().isEmpty()) {
//                builder.setProductName("product_name");
//            } else {
//                builder.setProductName(pinfo.getText().toString());// debug
//            }
//
//            if(fname != null && fname.getText().toString().isEmpty()) {
//                builder.setFirstName("piyush");
//            } else {
//                builder.setFirstName(fname.getText().toString());// debug
//            }
//
//            if(email != null && email.getText().toString().isEmpty()) {
//                builder.setEmail("piyush.jain@payu.in");
//            } else {
//                builder.setEmail(email.getText().toString());// debug
//            }
//
//            if(surl != null && surl.getText().toString().isEmpty()) {
//                builder.setsUrl("https://mobiletest.payumoney.com/mobileapp/payumoney/success.php");
//            } else {
//                builder.setsUrl(surl.getText().toString());// debug
//            }
//
//            if(furl != null && furl.getText().toString().isEmpty()) {
//                builder.setfUrl("https://mobiletest.payumoney.com/mobileapp/payumoney/failure.php");
//            } else {
//                builder.setfUrl(furl.getText().toString());// debug
//            }
//
//            if(udf1 != null && udf1.getText().toString().isEmpty()) {
//                builder.setUdf1("");
//            } else {
//                builder.setUdf1(udf1.getText().toString());// debug
//            }
//
//            if(udf2 != null && udf2.getText().toString().isEmpty()) {
//                builder.setUdf2("");
//            } else {
//                builder.setUdf2(udf2.getText().toString());// debug
//            }
//
//            if(udf3 != null && udf3.getText().toString().isEmpty()) {
//                builder.setUdf3("");
//            } else {
//                builder.setUdf3(udf3.getText().toString());// debug
//            }
//
//            if(udf4 != null && udf4.getText().toString().isEmpty()) {
//                builder.setUdf4("");
//            } else {
//                builder.setUdf4(udf4.getText().toString());// debug
//            }
//
//            if(udf5 != null && udf5.getText().toString().isEmpty()) {
//                builder.setUdf5("");
//            } else {
//                builder.setUdf5(udf5.getText().toString());// debug
//            }
//
//            PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();
//
//            PayUmoneySdkInitilizer.startPaymentActivityForResult(this,paymentParam);
//
//        }
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        Preferences1.getInstance().loadPreference(getApplicationContext());
//
//
//        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK) {
//                final ProgressDialog dialog;
//                dialog = new ProgressDialog(PaymentActivity.this, AlertDialog.THEME_HOLO_DARK);
//                dialog.setCancelable(false);
//                dialog.setMessage("Loading...");
//                dialog.show();
//                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
//                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
//                //showDialogMessage( "Payment Success Id : " + paymentId);
//                Preferences1.getInstance().paymentId = paymentId;
//                Preferences1.getInstance().paymentStatus = "Success";
//                Preferences.getInstance().savePreference(getApplicationContext());
//                dialog.dismiss();
//                Intent intent = new Intent(PaymentActivity.this,PaymentSuccess.class);
//                intent.putExtra("value","1");
//                startActivity(intent);
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
//
//    }
//
//    private void showDialogMessage(String message){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(TAG);
//        builder.setMessage(message);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
//
//    }
//
//}
