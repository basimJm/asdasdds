package com.schoofi.activitiess;


import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParentStudentFeesHistoryDetails extends AppCompatActivity {

    TextView amount,amount1,jointpayment,jointPayment1,delayCharges,delayCharges1,interest,interest1,serviceTax,serviceTax1,handlingCharges,handlingCharges1,paidOn,paidOn1,feeType1,feeType2,feeType3,feeType4,feeType5,feeType6,feeType7,feeType8,feeType9,feeType10,paymentMode,paymentMode1,paymentBank,paymentBank1,feePaymentNo,feePaymentNo1,confirmationNo,confirmationNo1,status,status1,period,transactionId,transactionId1,confirmationStatus,confirmationStatus1,invoiceNo,invoiceNo1,invoiceRefNo,invoiceRefNo1,invoiceDate,invoiceDate1,invoiceStatus,invoiceStatus1;
    String periodStart,periodEnd,periodStart1,periodEnd1,paymentDate,paymentDate1,invoiceDate2,invoiceDate3;
    LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5,linearLayout6,linearLayout7,linearLayout8,linearLayout9,linearLayout10,linearLayout11,linearLayout12,linearLayout13,linearLayout14,linearLayout15,linearLayout16,linearLayout31,linearLayout32,linearLayout33,linearLayout34,linearLayout35,linearLayout36;
    String Rs;

    JSONArray parentStudentFeesHistoryArray;
    private int position;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date1,date2,date3,date4,date5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Parent Student FeesHistoryDetails");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_parent_student_fees_history_details);

        /*amount = (TextView) findViewById(R.id.text_amount_payable1);
        paidOn = (TextView) findViewById(R.id.text_pay_by1);
        feeType = (TextView) findViewById(R.id.text_fee_type1);
        paymentMode = (TextView) findViewById(R.id.text_payment_mode1);
        paymentBank = (TextView) findViewById(R.id.text_payment_bank1);
        feePaymentNo = (TextView) findViewById(R.id.text_fee_payment_no1);
        confirmationNo = (TextView) findViewById(R.id.text_confirmation_no1);
        status = (TextView) findViewById(R.id.text_status1);*/
        paidOn = (TextView) findViewById(R.id.text_pay_by);
        paidOn1 = (TextView) findViewById(R.id.text_pay_by1);
        period = (TextView) findViewById(R.id.text_period);
        paymentMode = (TextView) findViewById(R.id.text_payment_mode);
        paymentMode1 = (TextView) findViewById(R.id.text_payment_mode1);
        paymentBank = (TextView) findViewById(R.id.text_payment_bank);
        paymentBank1 = (TextView) findViewById(R.id.text_payment_bank1);
        feePaymentNo = (TextView) findViewById(R.id.text_fee_payment_no);
        feePaymentNo1 = (TextView) findViewById(R.id.text_fee_payment_no1);
        confirmationNo = (TextView) findViewById(R.id.text_confirmation_no);
        confirmationNo1 = (TextView) findViewById(R.id.text_confirmation_no1);
        status = (TextView) findViewById(R.id.text_status);
        status1 = (TextView) findViewById(R.id.text_status1);
        amount = (TextView) findViewById(R.id.text_amount_payable);
        amount1 = (TextView) findViewById(R.id.text_amount_payable1);
        delayCharges = (TextView) findViewById(R.id.text_delay_charges);
        delayCharges1 = (TextView) findViewById(R.id.text_delay_charges1);
        serviceTax = (TextView) findViewById(R.id.text_service_tax);
        serviceTax1 = (TextView) findViewById(R.id.text_service_tax1);
        handlingCharges = (TextView) findViewById(R.id.text_handling_charges);
        handlingCharges1 = (TextView) findViewById(R.id.text_handling_charges1);
        interest = (TextView) findViewById(R.id.text_interest);
        interest1 = (TextView) findViewById(R.id.text_interest1);
        transactionId = (TextView) findViewById(R.id.text_payment_transaction_id);
        transactionId1 = (TextView) findViewById(R.id.text_payment_transaction_id1);
       // confirmationStatus = (TextView) findViewById(R.id.text_payment_confirmation_status);
        //confirmationStatus1 = (TextView) findViewById(R.id.text_payment_confirmation_status1);
        invoiceNo = (TextView) findViewById(R.id.text_payment_Invoice_No);
        invoiceNo1 = (TextView) findViewById(R.id.text_payment_Invoice_No1);
        invoiceRefNo = (TextView) findViewById(R.id.text_payment_Invoice_RefNo);
        invoiceRefNo1 = (TextView) findViewById(R.id.text_payment_Invoice_RefNo1);
        invoiceDate = (TextView) findViewById(R.id.text_payment_Invoice_Date);
        invoiceDate1 = (TextView) findViewById(R.id.text_payment_Invoice_Date1);
        invoiceStatus = (TextView) findViewById(R.id.text_payment_Invoice_Status);
        invoiceStatus1 = (TextView) findViewById(R.id.text_payment_Invoice_Status1);
        /*feeType1 = (TextView) findViewById(R.id.text_fee_type1);
        feeType2 = (TextView) findViewById(R.id.text_fee_type2);
        feeType3 = (TextView) findViewById(R.id.text_fee_type3);
        feeType4 = (TextView) findViewById(R.id.text_fee_type4);
        feeType5 = (TextView) findViewById(R.id.text_fee_type5);
        feeType6 = (TextView) findViewById(R.id.text_fee_type6);
        feeType7 = (TextView) findViewById(R.id.text_fee_type7);*/
        /*feeType8 = (TextView) findViewById(R.id.text_fee_type8);
        feeType9 = (TextView) findViewById(R.id.text_fee_type9);
        feeType10 = (TextView) findViewById(R.id.text_fee_type10);*/
        linearLayout1 = (LinearLayout) findViewById(R.id.linear_layout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linear_layout2);
        linearLayout3 = (LinearLayout) findViewById(R.id.linear_layout3);
        linearLayout4 = (LinearLayout) findViewById(R.id.linear_layout4);
        linearLayout5 = (LinearLayout) findViewById(R.id.linear_layout5);
        linearLayout6 = (LinearLayout) findViewById(R.id.linear_layout6);
        /*linearLayout7 = (LinearLayout) findViewById(R.id.linear_layout7);
        linearLayout8 = (LinearLayout) findViewById(R.id.linear_layout8);
        linearLayout9 = (LinearLayout) findViewById(R.id.linear_layout9);
        linearLayout10 = (LinearLayout) findViewById(R.id.linear_layout10);
        linearLayout11 = (LinearLayout) findViewById(R.id.linear_layout11);*/
        linearLayout12 = (LinearLayout) findViewById(R.id.linear_layout12);
        linearLayout13 = (LinearLayout) findViewById(R.id.linear_layout13);
        linearLayout14 = (LinearLayout) findViewById(R.id.linear_layout14);
        linearLayout15 = (LinearLayout) findViewById(R.id.linear_layout15);
        linearLayout16 = (LinearLayout) findViewById(R.id.linear_layout16);
        linearLayout31 = (LinearLayout) findViewById(R.id.linear_layout31);
        //linearLayout32 = (LinearLayout) findViewById(R.id.linear_layout32);
        linearLayout33 = (LinearLayout) findViewById(R.id.linear_layout33);
        linearLayout34 = (LinearLayout) findViewById(R.id.linear_layout34);
        linearLayout35 = (LinearLayout) findViewById(R.id.linear_layout35);
        linearLayout36 = (LinearLayout) findViewById(R.id.linear_layout36);
        jointpayment = (TextView) findViewById(R.id.text_jointPayment);
        jointPayment1 = (TextView) findViewById(R.id.text_jointPayment1);
        position = getIntent().getExtras().getInt("position");
        Rs = getApplicationContext().getString(R.string.Rs);

        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEES_HISTORY+"?token="+ Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                parentStudentFeesHistoryArray= null;
            }
            else
            {
                parentStudentFeesHistoryArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(parentStudentFeesHistoryArray!= null)
        {

            try {
                periodStart = parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_start_date");
                periodEnd = parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_end_date");
                paymentDate = parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_date");

                /*switch(Integer.parseInt(periodStart))
                {
                    case 1: periodStart1 = "JAN";
                        break;
                    case 2: periodStart1 = "FEB";
                        break;
                    case 3: periodStart1 = "MAR";
                        break;
                    case 4: periodStart1 = "APR";
                        break;
                    case 5: periodStart1 = "MAY";
                        break;
                    case 6: periodStart1 = "JUN";
                        break;
                    case 7: periodStart1 = "JUL";
                        break;
                    case 8: periodStart1 = "AUG";
                        break;
                    case 9: periodStart1 = "SEP";
                        break;
                    case 10: periodStart1 = "OCT";
                        break;
                    case 11: periodStart1 = "NOV";
                        break;
                    case 12: periodStart1 = "DEC";
                        break;
                    default:
                        Log.d("kkkk","jjjj");
                        break;



                }

                switch(Integer.parseInt(periodEnd))
                {
                    case 1: periodEnd1 = "JAN";
                        break;
                    case 2: periodEnd1 = "FEB";
                        break;
                    case 3: periodEnd1 = "MAR";
                        break;
                    case 4: periodEnd1 = "APR";
                        break;
                    case 5: periodEnd1 = "MAY";
                        break;
                    case 6: periodEnd1 = "JUN";
                        break;
                    case 7: periodEnd1 = "JUL";
                        break;
                    case 8: periodEnd1 = "AUG";
                        break;
                    case 9: periodEnd1 = "SEP";
                        break;
                    case 10: periodEnd1 = "OCT";
                        break;
                    case 11: periodEnd1 = "NOV";
                        break;
                    case 12: periodEnd1 = "DEC";
                        break;
                    default:
                        Log.d("kkkk","jjjj");
                        break;



                }*/

                invoiceDate2 = parentStudentFeesHistoryArray.getJSONObject(position).getString("inv_date");

                try {
                    date1 = formatter.parse(periodStart);
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                    periodStart1 = formatter1.format(date1);

                    date2 = formatter.parse(periodEnd);

                    periodEnd1 = formatter1.format(date2);

                    date3 = formatter.parse(paymentDate);
                    paymentDate1 = formatter1.format(date3);
                    date4 = formatter.parse(invoiceDate2);
                    invoiceDate2 = formatter1.format(date4);

                } catch (ParseException e) {
                    e.printStackTrace();
                }



                period.setText("Period : " + periodStart1 + " to " + periodEnd1);
                paidOn.setText("Paid On");
                paidOn1.setText(paymentDate1);
                amount.setText("Amount Paid");
                amount1.setText(Rs+parentStudentFeesHistoryArray.getJSONObject(position).getString("fees_amount"));
                /*amount.setText("Rs: " + parentStudentFeesHistoryArray.getJSONObject(position).getString("fees_amount") + "/-");
                feeType.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_type_details"));
                feePaymentNo.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_payment_no"));
                paymentMode.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_mode"));
                paymentBank.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_bank"));
                confirmationNo.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("confirmation_no"));
                status.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("confirmation_status"));*/

                paymentMode1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_mode"));
                transactionId1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("transaction_id"));
                //confirmationStatus1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString(""));
                invoiceNo1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("inv_no"));
                invoiceRefNo1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("inv_ref_no"));
                invoiceDate1.setText(invoiceDate2);
                invoiceStatus1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("inv_status"));
                if(parentStudentFeesHistoryArray.getJSONObject(position).getString("fine_amount").matches("") || parentStudentFeesHistoryArray.getJSONObject(position).getString("fine_amount").matches("null") || parentStudentFeesHistoryArray.getJSONObject(position).getString("fine_amount").matches("0"))
                {
                    linearLayout12.setVisibility(View.GONE);
                }

                else
                {
                    delayCharges.setText("Delay Charges");
                    delayCharges1.setText(Rs+parentStudentFeesHistoryArray.getJSONObject(position).getString("fine_amount"));
                }

                if(parentStudentFeesHistoryArray.getJSONObject(position).getString("interest_amount").matches("") || parentStudentFeesHistoryArray.getJSONObject(position).getString("interest_amount").matches("null") || parentStudentFeesHistoryArray.getJSONObject(position).getString("interest_amount").matches("0"))
                {
                    linearLayout13.setVisibility(View.GONE);
                }

                else
                {
                    interest.setText("Interest");
                    interest1.setText(Rs+parentStudentFeesHistoryArray.getJSONObject(position).getString("interest_amount"));
                }

                if(parentStudentFeesHistoryArray.getJSONObject(position).getString("handling_charges").matches("") || parentStudentFeesHistoryArray.getJSONObject(position).getString("handling_charges").matches("null") || parentStudentFeesHistoryArray.getJSONObject(position).getString("handling_charges").matches("0"))
                {
                    linearLayout14.setVisibility(View.GONE);
                }

                else
                {
                    handlingCharges.setText("Handling and Additional Charges");
                    handlingCharges1.setText(Rs+parentStudentFeesHistoryArray.getJSONObject(position).getString("handling_charges"));
                }

                if(parentStudentFeesHistoryArray.getJSONObject(position).getString("service_tax").matches("") || parentStudentFeesHistoryArray.getJSONObject(position).getString("service_tax").matches("null") || parentStudentFeesHistoryArray.getJSONObject(position).getString("service_tax").matches("0"))
                {
                    linearLayout15.setVisibility(View.GONE);
                }

                else
                {
                    serviceTax.setText("Service Tax");
                    serviceTax1.setText(Rs+parentStudentFeesHistoryArray.getJSONObject(position).getString("service_tax"));
                }

                if(parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_payment_no").matches("") || parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_payment_no").matches("null"))
                {
                    linearLayout4.setVisibility(View.GONE);
                }

                else
                {
                    feePaymentNo.setText("Fee Payment No.");
                    feePaymentNo1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_payment_no"));
                }

                if(parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_bank").matches("") || parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_bank").matches("null"))
                {
                    linearLayout3.setVisibility(View.GONE);
                }

                else
                {
                    paymentBank.setText("Payment Bank");
                    paymentBank1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_bank"));
                }

                jointpayment.setText("Joint Payment");
                jointPayment1.setText(Rs+parentStudentFeesHistoryArray.getJSONObject(position).getString("joint_payment"));

                if(parentStudentFeesHistoryArray.getJSONObject(position).getString("confirmation_status").matches("") || parentStudentFeesHistoryArray.getJSONObject(position).getString("confirmation_status").matches("null"))
                {
                    linearLayout6.setVisibility(View.GONE);
                }

                else
                {
                    status.setText("Confirmation Status");
                    status1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("confirmation_status"));
                }

                if(parentStudentFeesHistoryArray.getJSONObject(position).getString("Pay_bank_conf_no").matches("") || parentStudentFeesHistoryArray.getJSONObject(position).getString("Pay_bank_conf_no").matches("null"))
                {
                    linearLayout5.setVisibility(View.GONE);
                }

                else
                {
                    confirmationNo.setText("Bank Confirmation No.");
                    confirmationNo1.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("Pay_bank_conf_no"));
                }


            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
