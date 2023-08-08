package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
//import com.payUMoney.sdk.PayUmoneySdkInitilizer;
//import com.payUMoney.sdk.SdkConstants;
//import com.payUMoney.sdk.walledSdk.Preferences1;
//import com.schoofi.activities.PaymentActivity;
import com.schoofi.activities.PaymentSuccess;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

import java.util.HashMap;

public class PaymentActivitySecondScreen extends AppCompatActivity {

    String fees;
    Button pollOption1,pollOption2,submit;
    ImageView pollOptionImage1,pollOptionImage2;
    String value;
    boolean c;
    HashMap<String, String> params = new HashMap<>();
    String eventId;

    public static final String TAG = "PayUMoneySDK Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Payment Activity Second Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_payment_activity_second_screen);

        fees = getIntent().getStringExtra("fees");
        eventId = getIntent().getStringExtra("eventId");
        Preferences.getInstance().paidAmount = fees;
        Preferences.getInstance().savePreference(getApplicationContext());

        pollOption1 = (Button) findViewById(R.id.btn_pollOption1);
        pollOption2 = (Button) findViewById(R.id.btn_pollOption2);
        pollOptionImage1 = (ImageView) findViewById(R.id.image_poll1);
        pollOptionImage2 = (ImageView) findViewById(R.id.image_poll2);
        submit = (Button) findViewById(R.id.btn_submit);

        pollOptionImage1.setVisibility(View.INVISIBLE);
        pollOptionImage2.setVisibility(View.INVISIBLE);
        pollOption2.setVisibility(View.INVISIBLE);

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
            public void onClick(View view) {

                if(c==false)
                {
                    Utils.showToast(getApplicationContext(),"Please select the payment option");
                }

                else
                {
                    Intent intent = new Intent(PaymentActivitySecondScreen.this,PaymentSuccess.class);
                    intent.putExtra("value","1");
                    intent.putExtra("eventId",eventId);
                    intent.putExtra("fees",fees);
                    startActivity(intent);
                    finish();

                }
            }
        });






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
        if(isDouble(fees)){
            amount = Double.parseDouble(fees);
        }else{
            Toast.makeText(getApplicationContext(), "Enter correct amount", Toast.LENGTH_LONG).show();
            return ;
        }
        if (amount <= 0.0) {
            Toast.makeText(getApplicationContext(), "Enter Some amount", Toast.LENGTH_LONG).show();
        } else if (amount > 1000000.00) {
            Toast.makeText(getApplicationContext(), "Amount exceeding the limit : 1000000.00 ", Toast.LENGTH_LONG).show();
        } else {

//            PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder(  );
//
//            builder.setKey(""); //Put your live KEY here
//            builder.setSalt(""); //Put your live SALT here
//
//                builder.setMerchantId(""); //Put your live MerchantId here
//
//
//
//            builder.setIsDebug(true);
//            builder.setDebugKey("4UjvqJCD");// Debug Key
//            builder.setDebugMerchantId("4936099");// Debug Merchant ID
//            builder.setDebugSalt("CLptFu8bhA");// Debug Salt
//
//
//            if(fees != null && fees.toString().isEmpty()) {
//                builder.setAmount(10);
//            } else {
//                builder.setAmount(amount);// debug
//            }
//
//
//                builder.setTnxId("0nf7");
//
//
//
//                builder.setPhone("9999999999");
//
//
//
//                builder.setProductName("product_name");
//
//
//
//                builder.setFirstName("piyush");
//
//
//                builder.setEmail("piyush.jain@payu.in");
//
//
//
//                builder.setsUrl("https://mobiletest.payumoney.com/mobileapp/payumoney/success.php");
//
//
//
//                builder.setfUrl("https://mobiletest.payumoney.com/mobileapp/payumoney/failure.php");
//
//
//
//                builder.setUdf1("");
//
//
//
//                builder.setUdf2("");
//
//
//
//                builder.setUdf3("");
//
//
//
//                builder.setUdf4("");
//
//
//
//                builder.setUdf5("");
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
//                dialog = new ProgressDialog(PaymentActivitySecondScreen.this, AlertDialog.THEME_HOLO_DARK);
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
//                Intent intent = new Intent(PaymentActivitySecondScreen.this,PaymentSuccess.class);
//                intent.putExtra("value","1");
//                intent.putExtra("eventId",eventId);
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
}
