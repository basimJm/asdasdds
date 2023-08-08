package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParentFeesSuccessScreenCCAvenue extends AppCompatActivity {

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
    String json_string,order_id,tracking_id,bank_ref_no,order_status,failure_message,payment_mode,card_name,status_code,status_message,currency,amount,billing_name,billing_address,billing_city,billing_state,billing_zip,billing_country,billing_tel,billing_email,delivery_name,delivery_address,delivery_city,delivery_state,delivery_zip,delivery_country,delivery_tel,merchant_param1,merchant_param2,merchant_param3,merchant_param4,merchant_param5,vault,offer_type,offer_code,discount_value,mer_amount,eci_value,retry,response_code,billing_notes,trans_date,bin_country,gross1;
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
        value = getIntent().getStringExtra("value");

        order_id = getIntent().getStringExtra("order_id");
        tracking_id = getIntent().getStringExtra("tracking_id");
        bank_ref_no = getIntent().getStringExtra("bank_ref_no");
        order_status = getIntent().getStringExtra("transStatus");
        failure_message = getIntent().getStringExtra("failure_message");
        payment_mode = getIntent().getStringExtra("payment_mode");
        card_name = getIntent().getStringExtra("card_name");
        status_code = getIntent().getStringExtra("status_code");
        status_message = getIntent().getStringExtra("status_message");
        currency = getIntent().getStringExtra("currency");
        amount = getIntent().getStringExtra("amount");
        billing_name = getIntent().getStringExtra("billing_name");
        billing_address = getIntent().getStringExtra("billing_address");
        billing_city = getIntent().getStringExtra("billing_city");
        billing_state = getIntent().getStringExtra("billing_state");
        billing_zip = getIntent().getStringExtra("billing_zip");
        billing_country = getIntent().getStringExtra("billing_country");
        billing_tel = getIntent().getStringExtra("billing_tel");
        billing_email = getIntent().getStringExtra("billing_email");
        merchant_param1 = getIntent().getStringExtra("merchant_param1");
        merchant_param2 = getIntent().getStringExtra("merchant_param2");
        merchant_param3 = getIntent().getStringExtra("merchant_param3");
        merchant_param4 = getIntent().getStringExtra("merchant_param4");
        merchant_param5 = getIntent().getStringExtra("merchant_param5");
        vault = getIntent().getStringExtra("vault");
        offer_type = getIntent().getStringExtra("offer_type");
        offer_code = getIntent().getStringExtra("offer_code");
        discount_value = getIntent().getStringExtra("discount_value");
        mer_amount = getIntent().getStringExtra("mer_amount");
        eci_value = getIntent().getStringExtra("eci_value");
        retry = getIntent().getStringExtra("retry");
        response_code = getIntent().getStringExtra("response_code");
        billing_notes = getIntent().getStringExtra("billing_notes");
        trans_date = getIntent().getStringExtra("trans_date");
        bin_country = getIntent().getStringExtra("bin_country");
        json_string = getIntent().getStringExtra("json");

        Log.d("array",array);
        Log.d("array1",array1);
        Log.d("array2",array2);
        Log.d("array3",array3);
        Log.d("array4",array4);
        Log.d("array5",array5);
        Log.d("array6",array6);
        Log.d("array7",array7);
        Log.d("array8",array8);
        Log.d("array9",array9);
        Log.d("array10",array10);
        Log.d("count",String.valueOf(count));
        Log.d("gross","kkk"+gross1);
        Log.d("service_tax","kkkp"+serviceTax);
        Log.d("order_id","kkkk"+order_id);
        Log.d("tracking_id","jhh"+tracking_id+bank_ref_no+order_status+failure_message+payment_mode+card_name+status_code+status_message+currency+amount+billing_name+billing_email+billing_tel+billing_state+billing_address+billing_country+billing_zip+billing_notes);
        Log.d("merchant_param","kkkkl"+merchant_param1+merchant_param2+merchant_param3+merchant_param4+merchant_param5+vault+offer_type+offer_code+discount_value+mer_amount+eci_value+retry+response_code+trans_date+bin_country);
        Log.d("json","bbb"+json_string);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        relativeLayout.setVisibility(View.INVISIBLE);
        payeeName = (TextView) findViewById(R.id.text_payeeName1);
        transactionId = (TextView) findViewById(R.id.text_transactionId1);
        transactionStatus = (TextView) findViewById(R.id.text_transactionStatus1);
        payFor = (TextView) findViewById(R.id.text_productInfo1);
        payment = (TextView) findViewById(R.id.text_paidAmount1);
        postToServer();



    }

    private void postToServer()
    {
        Log.d("kjhg",Preferences.getInstance().userId+Preferences.getInstance().userEmailId+Preferences.getInstance().phoneNumber);

        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
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
                        relativeLayout.setVisibility(View.VISIBLE);
                        payeeName.setText(Preferences.getInstance().Name);
                        transactionStatus.setText(order_status);
                        transactionId.setText(tracking_id);
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


                System.out.print(count+array+"1:"+array1+"2:"+array2+"3:"+array3+"4:"+array4+"5:"+array5+"6:"+array6+"7:"+array7+"8:"+array8);



                params.put("count", String.valueOf(count));

                Preferences.getInstance().loadPreference(ParentFeesSuccessScreenCCAvenue.this);


                if(array.matches("null"))
                {
                    params.put("stu_fee_date_id", "");
                }

                else
                {
                    params.put("stu_fee_date_id", array);
                }



                params.put("u_id",Preferences.getInstance().userId);
                params.put("u_name",Preferences.getInstance().userName);
                params.put("u_phone",Preferences.getInstance().phoneNumber);
                params.put("institute_id",Preferences.getInstance().institutionId);
                params.put("school_id",Preferences.getInstance().schoolId);
                params.put("stu_id", Preferences.getInstance().studentId);
                params.put("class_id",Preferences.getInstance().studentClassId);
                params.put("section_id",Preferences.getInstance().studentSectionId);
                params.put("admission_no",Preferences.getInstance().addmissionNumber);



                if(array10.matches("null")) {
                    params.put("total_fee_amount", "");
                }
                else
                {
                    params.put("total_fee_amount", array10);
                }

                if(array2.matches("null"))
                {
                    params.put("delay_fine_amount","");
                }
                else
                {
                    params.put("delay_fine_amount",array2);
                }

                if(array1.matches("null"))
                {
                    params.put("interest_amount","");
                }

                else {
                    params.put("interest_amount",array1);
                }

               if(serviceTax.matches("null"))
               {
                   params.put("service_charges","");
               }
               else {
                   params.put("service_charges", serviceTax);
               }

               if(array4.matches(""))
               {
                   params.put("fees_service_tax", array4);
               }

               else {
                   params.put("fees_service_tax", array4);
               }
                //params.put("BANKNAME",bankName1);

                if(payment_mode.matches("null"))
                {
                    params.put("PAYMENTMODE","");
                }
                else {
                    params.put("PAYMENTMODE", payment_mode);
                }

                if(bank_ref_no.matches("null"))
                {
                    params.put("BANKTXNID","");
                }

                else {
                    params.put("BANKTXNID", bank_ref_no);
                }

                if(order_status.matches("null"))
                {
                    params.put("STATUS","");
                }
                else {
                    params.put("STATUS", order_status);
                }
                params.put("fee_duration","");
                params.put("fee_payment_no","");

                if(tracking_id.matches("null"))
                {
                    params.put("TXNID","");
                }
                else
                {
                    params.put("TXNID",tracking_id);
                }
                if(array5.matches("null"))
                {
                    params.put("fee_start_date","");
                }
                else {
                    params.put("fee_start_date", array5);
                }

                if(array6.matches("null"))
                {
                    params.put("fee_end_date","");
                }

                else {
                    params.put("fee_end_date", array6);
                }

                if(array7.matches("null"))
                {
                    params.put("final_amount","");
                }
                else {
                    params.put("final_amount", array7.toString());
                }


                params.put("discount_percent","");

                if(array3.matches("null"))
                {
                    params.put("handling_charges","");
                }

                else {
                    params.put("handling_charges", array3);
                }

                if(array9.matches(""))
                {
                    params.put("days","");
                }

                else {
                    params.put("days", array9);
                }
                //params.put("TXNAMOUNT",String.valueOf(gross));
                if(amount.matches("null"))
                {
                    params.put("TXNAMOUNT","");
                }

                else {
                    params.put("TXNAMOUNT", amount);
                }

                if(array8.matches("null"))
                {
                    params.put("discount_amount","");
                }

                else
                {
                    params.put("discount_amount",array8);
                }

                params.put("session",Preferences.getInstance().session1);

                if(trans_date.matches("null"))
                {
                    params.put("TXNDATE","");
                }

                else
                {
                    params.put("TXNDATE",trans_date);
                }

                if(currency.matches("null"))
                {
                    params.put("CURRENCY","");
                }

                else
                {
                    params.put("CURRENCY",currency);
                }

                params.put("GATEWAYNAME","CC-Avenue");
                params.put("IS_CHECKSUM_VALID","");

                if(response_code.matches("null"))
                {
                    params.put("RESPCODE","");
                }

                else
                {
                    params.put("RESPCODE",response_code);
                }
                if(order_id.matches("null"))
                {
                    params.put("ORDERID","");
                }
                else {
                    params.put("ORDERID", order_id);
                }

                if(failure_message.matches("null"))
                {
                    params.put("RESPMSG","");
                }
                else {
                    params.put("RESPMSG", failure_message);
                }
                params.put("value","2");
                if(json_string.matches("null"))
                {
                    params.put("json_string","");
                }
                else {
                    params.put("json_string", json_string);
                }
                params.put("checksum_hash","");
                if(card_name.matches("null"))
                {
                    params.put("card_name","");
                }
                else {
                    params.put("card_name", card_name);
                }
                if(status_code.matches("null"))
                {
                    params.put("status_code","");
                }
                else {
                    params.put("status_code", status_code);
                }

                if(status_message.matches("null"))
                {
                    params.put("status_message","");
                }

                else {
                    params.put("status_message", status_message);
                }

                if(billing_name.matches("null"))
                {
                    params.put("billing_name","");
                }
                else {
                    params.put("billing_name", billing_name);
                }
                if(billing_address.matches("null"))
                {
                    params.put("billing_address","");
                }

                else
                {
                    params.put("billing_address",billing_address);
                }

                if(billing_city.matches("null"))
                {
                    params.put("billing_city","");
                }

                else
                {
                    params.put("billing_city",billing_city);
                }

               if(billing_state.matches("null"))
               {
                   params.put("billing_state","");
               }

               else
               {
                   params.put("billing_state",billing_state);
               }
               if(billing_zip.matches("null"))
               {
                   params.put("billing_zip","");
               }

               else
               {
                   params.put("billing_zip",billing_zip);
               }

               if(billing_country.matches("null"))
               {
                   params.put("billing_country","");

               }

               else
               {
                   params.put("billing_country",billing_country);
               }


               if(billing_tel.matches("null"))
               {
                   params.put("billing_tel","");
               }

               else
               {
                   params.put("billing_tel",billing_tel);
               }

                if(billing_email.matches("null"))
                {
                    params.put("billing_email","");
                }

                else
                {
                    params.put("billing_email",billing_email);
                }

                if(merchant_param1.matches("null"))
                {
                    params.put("merchant_param1","");
                }
                else
                {
                    params.put("merchant_param1",merchant_param1);
                }

                if(merchant_param2.matches("null"))
                {
                    params.put("merchant_param2","");
                }
                else
                {
                    params.put("merchant_param2",merchant_param2);
                }

                if(merchant_param3.matches("null"))
                {
                    params.put("merchant_param3","");
                }
                else
                {
                    params.put("merchant_param3",merchant_param3);
                }

                if(merchant_param4.matches("null"))
                {
                    params.put("merchant_param4","");
                }
                else
                {
                    params.put("merchant_param4",merchant_param4);
                }

                if(merchant_param5.matches("null"))
                {
                    params.put("merchant_param5","");
                }
                else
                {
                    params.put("merchant_param5",merchant_param5);
                }

                if(vault.matches("null"))
                {
                    params.put("vault","");
                }

                else
                {
                    params.put("vault",vault);
                }

                if(offer_type.matches("null"))
                {
                    params.put("offer_type","");
                }

                else
                {
                    params.put("offer_type",offer_type);
                }

                if(offer_code.matches("null"))
                {
                    params.put("offer_code","");
                }

                else
                {
                    params.put("offer_code",offer_code);
                }

                if(discount_value.matches("null"))
                {
                    params.put("discount_value","");
                }

                else
                {
                    params.put("discount_value",discount_value);
                }

               if(mer_amount.matches("null"))
               {
                   params.put("mer_amount","");
               }

               else
               {
                   params.put("mer_amount",mer_amount);
               }

               if(eci_value.matches("null"))
               {
                   params.put("eci_value","");
               }

               else
               {
                   params.put("eci_value",eci_value);
               }

               if(retry.matches("null"))
               {
                   params.put("retry","");
               }

               else
               {
                   params.put("retry",retry);
               }


               if(billing_notes.matches("null"))
               {
                   params.put("billing_notes","");
               }

               else
               {
                   params.put("billing_notes",billing_notes);
               }





                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ParentFeesSuccessScreenCCAvenue.this,ParentFees.class);
        startActivity(intent);
        finish();
    }
}
