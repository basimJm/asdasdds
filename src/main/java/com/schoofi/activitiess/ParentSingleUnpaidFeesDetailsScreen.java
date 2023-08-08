package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParentSingleUnpaidFeesDetailsScreen extends AppCompatActivity {

    TextView period,feeType1,feeType2,feeType3,feeType4,feeType5,feeType6,feeType7,feeType8,feeType9,feeType10,feeInterest,feeInterest1,feeDelayCharges,feeDelayCharges1,feeHandlingCharges,feeHandlingCharges1,feeTotal,feeTotal1,feeServiceTax,feeServiceTax1,feeTotal2,feeTotal3,feesDueDate,feesDueDate1;
    Button payNow;
    LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5,linearLayout6,linearLayout7;
    JSONArray parentStudentFeesUnpaidArray;
    int position;
    int SELECT_PDF=0;
    String periodStart,periodEnd, fee1_type_text,fee_type_text1,fee2_type_text,fee_type_text2,fee3_type_text,fee_type_text3,fee4_type_text,fee_type_text4,fee5_type_text,fee_type_text5,fee_srrvice_tax,fee6_type_text,fee7_type_text,fee8_type_text,fee9_type_text,fee10_type_text;
    Date date1,date2,date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    float feeType11,feeType21,feeType31,feeType41,feeType51,interest,delayCharges;
    String Rs;
    float handlingCharges11=0;
    float total=0,total1=0,serviceTax,total2=0;
    String totalAmount,payableDuration,rateOfInterest,feeDueDate,feeDueDate1;
    String crrDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String crrDate1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Parent Single Unpaid Fees Details Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_parent_single_unpaid_fees_details_screen);

        period = (TextView) findViewById(R.id.text_period);

        feeType1 = (TextView) findViewById(R.id.text_fee_type1);
        feeType2 = (TextView) findViewById(R.id.text_fee_type11);
        feeType3 = (TextView) findViewById(R.id.text_fee_type2);
        feeType4 = (TextView) findViewById(R.id.text_fee_type12);
        feeType5 = (TextView) findViewById(R.id.text_fee_type3);
        feeType6 = (TextView) findViewById(R.id.text_fee_type13);
        feeType7 = (TextView) findViewById(R.id.text_fee_type4);
        feeType8 = (TextView) findViewById(R.id.text_fee_type14);
        feeType9 = (TextView) findViewById(R.id.text_fee_type5);
        feeType10 = (TextView) findViewById(R.id.text_fee_type15);
        feeInterest = (TextView) findViewById(R.id.text_interest);
        feeInterest1 = (TextView) findViewById(R.id.text_interest1);
        feeDelayCharges = (TextView) findViewById(R.id.text_delay_charges);
        feeDelayCharges1 = (TextView) findViewById(R.id.text_delay_charges1);
        feeTotal = (TextView) findViewById(R.id.text_total_charges);
        feeTotal1 = (TextView) findViewById(R.id.text_total_charges1);
        feeHandlingCharges = (TextView) findViewById(R.id.text_handling_charges);
        feeHandlingCharges1 = (TextView) findViewById(R.id.text_handling_charges1);
        feeServiceTax = (TextView) findViewById(R.id.text_fee_service_tax);
        feeServiceTax1 = (TextView) findViewById(R.id.text_fee_service_tax1);
        feeTotal2 = (TextView) findViewById(R.id.text_total_charges2);
        feeTotal3 = (TextView) findViewById(R.id.text_total_charges3);
        feesDueDate = (TextView) findViewById(R.id.text_fee_due_date);
        feesDueDate1 = (TextView) findViewById(R.id.text_fee_due_date1);
        payNow = (Button) findViewById(R.id.btn_payNow);
        Rs = getApplicationContext().getString(R.string.Rs);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
        linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
        linearLayout6 = (LinearLayout) findViewById(R.id.linearLayout6);
        linearLayout7 = (LinearLayout) findViewById(R.id.linearLayout7);

        position = getIntent().getExtras().getInt("position");
        periodStart = getIntent().getStringExtra("periodStart");
        periodEnd = getIntent().getStringExtra("periodEnd");

        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_UNPAID_FESS+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                parentStudentFeesUnpaidArray= null;
            }
            else
            {
                parentStudentFeesUnpaidArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(parentStudentFeesUnpaidArray!= null) {

            try {
                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee1_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee1_type_text").matches("null"))
                {
                    fee1_type_text = "null";
                    fee2_type_text = "null";
                }

                else
                {
                    fee1_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee1_type_text");
                    fee2_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_type_text1");

                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee2_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee2_type_text").matches("null"))
                {
                    fee3_type_text = "null";
                    fee4_type_text = "null";
                }

                else
                {
                    fee3_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee2_type_text");
                    fee4_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_type_text2");

                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee3_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee3_type_text").matches("null"))
                {
                    fee5_type_text = "null";
                    fee6_type_text = "null";
                }

                else
                {
                    fee5_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee3_type_text");
                    fee6_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_type_text3");

                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee4_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee4_type_text").matches("null"))
                {
                    fee7_type_text = "null";
                    fee8_type_text = "null";
                }

                else
                {
                    fee7_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee4_type_text");
                    fee8_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_type_text4");

                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee5_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee5_type_text").matches("null"))
                {
                    fee9_type_text = "null";
                    fee10_type_text = "null";
                }

                else
                {
                    fee9_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee5_type_text");
                    fee10_type_text = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_type_text5");

                }

                fee_srrvice_tax = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fees_service_tax");
                feeDueDate = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_due_date");

                period.setText("Period: "+periodStart+" to "+periodEnd);

                    try {
                        date1 = formatter2.parse(feeDueDate);
                        feeDueDate1 = formatter1.format(date1);
                        feesDueDate.setText("Pay By");
                        feesDueDate1.setText(feeDueDate1);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee1_amount").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee1_amount").matches("null") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee1_amount").matches("0"))
                {
                    linearLayout1.setVisibility(View.GONE);
                }

                else
                {
                    feeType1.setText(fee2_type_text);
                    feeType2.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee1_amount"));
                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee2_amount").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee2_amount").matches("null") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee2_amount").matches("0"))
                {
                    linearLayout2.setVisibility(View.GONE);
                }

                else
                {
                    feeType3.setText(fee4_type_text);
                    feeType4.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee2_amount"));
                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee3_amount").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee3_amount").matches("null") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee3_amount").matches("0"))
                {
                    linearLayout3.setVisibility(View.GONE);
                }

                else
                {
                    feeType5.setText(fee6_type_text);
                    feeType6.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee3_amount"));
                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee4_amount").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee4_amount").matches("null") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee4_amount").matches("0"))
                {
                    linearLayout4.setVisibility(View.GONE);
                }

                else
                {
                    feeType7.setText(fee8_type_text);
                    feeType8.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee4_amount"));
                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee5_amount").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee5_amount").matches("null") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee5_amount").matches("0"))
                {
                    linearLayout5.setVisibility(View.GONE);
                }

                else
                {
                    feeType9.setText(fee10_type_text);
                    feeType10.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee5_amount"));
                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("interest_amount").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("interest_amount").matches("null") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("interest_amount").matches("0"))
                {
                    linearLayout7.setVisibility(View.GONE);
                }

                else
                {
                    feeInterest.setText("Interest");
                    feeInterest1.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("interest_amount"));
                }

                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("delay_fine_amount").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("delay_fine_amount").matches("null") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("delay_fine_amount").matches("0"))
                {
                    linearLayout6.setVisibility(View.GONE);
                }

                else
                {
                    feeDelayCharges.setText("Delay Charges");
                    feeDelayCharges1.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("delay_fine_amount"));
                }

                feeTotal.setText("Total");
                feeTotal1.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("total_fee_amount"));

                feeServiceTax.setText("Service Tax");
                feeServiceTax1.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("service_charges"));

                feeHandlingCharges.setText("Handling and Additional Charges");
                feeHandlingCharges1.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("handling_charges"));

                feeTotal2.setText("Total Fees with Service Tax");
                feeTotal3.setText(Rs+parentStudentFeesUnpaidArray.getJSONObject(position).getString("final_fee_amount"));

                /*feeType1.setText(fee2_type_text);
                feeType1.setText(fee2_type_text);
                feeType1.setText(fee2_type_text);
                feeType1.setText(fee2_type_text);
                feeType1.setText(fee2_type_text);*/

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent = new Intent(ParentSingleUnpaidFeesDetailsScreen.this,ParentFeesActivityOptionScreen.class);
                    intent.putExtra("fee_type_text111","");
                    intent.putExtra("fee_type_text211","");
                    intent.putExtra("fee_type_text311","");
                    intent.putExtra("fee_type_text411","");
                    intent.putExtra("fee_type_text511","");
                    intent.putExtra("interest","");
                    intent.putExtra("delay_charges","");
                /*intent.putExtra("fee1_type_text",fee2_type_text);
                intent.putExtra("fee_type_text1",fee1_type_text);
                intent.putExtra("fee2_type_text",fee4_type_text);
                intent.putExtra("fee_type_text2",fee3_type_text);
                intent.putExtra("fee3_type_text",fee6_type_text);
                intent.putExtra("fee_type_text3",fee5_type_text);
                intent.putExtra("fee4_type_text",fee8_type_text);
                intent.putExtra("fee_type_text4",fee7_type_text);
                intent.putExtra("fee5_type_text",fee10_type_text);
                intent.putExtra("fee_type_text5",fee9_type_text);*/
                    intent.putExtra("fees_service_tax",fee_srrvice_tax);
                    intent.putExtra("feeServiceTax",serviceTax);
                    intent.putExtra("feeHandlingCharges","");
                    intent.putExtra("totalAmount","");
                    intent.putExtra("finalTotalAmount",parentStudentFeesUnpaidArray.getJSONObject(position).getString("final_fee_amount"));
                    intent.putExtra("fee_type_text11",parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee1_amount"));
                    intent.putExtra("fee_type_text21",parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee2_amount"));
                    intent.putExtra("fee_type_text31",parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee3_amount"));
                    intent.putExtra("fee_type_text41",parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee4_amount"));
                    intent.putExtra("fee_type_text51",parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee5_amount"));
                    intent.putExtra("interest1",parentStudentFeesUnpaidArray.getJSONObject(position).getString("interest_amount"));
                    intent.putExtra("delay_charges1",parentStudentFeesUnpaidArray.getJSONObject(position).getString("delay_fine_amount"));
                    intent.putExtra("feeServiceTax1",parentStudentFeesUnpaidArray.getJSONObject(position).getString("service_charges"));
                    intent.putExtra("feeHandlingCharges1",parentStudentFeesUnpaidArray.getJSONObject(position).getString("handling_charges"));
                    intent.putExtra("totalAmount1",parentStudentFeesUnpaidArray.getJSONObject(position).getString("final_fee_amount"));
                    intent.putExtra("stu_fee_date_id",parentStudentFeesUnpaidArray.getJSONObject(position).getString("stu_fee_date_id"));
                    intent.putExtra("count","1");
                    intent.putExtra("fee_start_date",parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_start_date"));
                    intent.putExtra("fee_end_date",parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_end_date"));
                    intent.putExtra("days",parentStudentFeesUnpaidArray.getJSONObject(position).getString("days"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
