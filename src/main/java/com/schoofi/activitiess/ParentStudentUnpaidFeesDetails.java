package com.schoofi.activitiess;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParentStudentUnpaidFeesDetails extends AppCompatActivity {

    TextView period,feeType1,feeType2,feeType3,feeType4,feeType5,feeType6,feeType7,feeType8,feeType9,feeType10,feeInterest,feeInterest1,feeDelayCharges,feeDelayCharges1,feeHandlingCharges,feeHandlingCharges1,feeTotal,feeTotal1,feeServiceTax,feeServiceTax1,feeTotal2,feeTotal3;
    Button payNow;
    LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5,linearLayout6,linearLayout7;
    //JSONArray parentStudentFeesUnpaidArray;
    int position;
    int SELECT_PDF=0;
    String totalAmount,fee1_type_text,fee_type_text1,fee2_type_text,fee_type_text2,fee3_type_text,fee_type_text3,fee4_type_text,fee_type_text4,fee5_type_text,fee_type_text5,fee_srrvice_tax;
    Date date1,date2,date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    float feeType11,feeType21,feeType31,feeType41,feeType51,interest,delayCharges,serviceTax1,handlingCharges;
    LinearLayout linearLayout;
    String Rs;
    float handlingCharges11=0;
    float total=0,total1=0,serviceTax,total2=0,finalTotalAmount=0;
    ImageView back;
    String fee_type_text11,fee_type_text21,fee_type_text31,fee_type_text41,fee_type_text51,interest1,delay_charges1,feeServiceTax11,feeHandlingCharges11,totalAmount1,stu_fee_date_id,count,feeStartDate,feeEndDate,days1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Parent Student UnpaidFeesDetails");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_parent_student_unpaid_fees_details);

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
        payNow = (Button) findViewById(R.id.btn_payNow);
        Rs = getApplicationContext().getString(R.string.Rs);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
        linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
        linearLayout6 = (LinearLayout) findViewById(R.id.linearLayout6);
        linearLayout7 = (LinearLayout) findViewById(R.id.linearLayout7);
        linearLayout = (LinearLayout) findViewById(R.id.linaer_layout21);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentStudentUnpaidFeesDetails.this,ParentFees.class);
                startActivity(intent);
                finish();
            }
        });

       // position = getIntent().getExtras().getInt("position");

        feeType11 = getIntent().getExtras().getFloat("fee_type_text111");
        feeType21 = getIntent().getExtras().getFloat("fee_type_text211");
        feeType31 = getIntent().getExtras().getFloat("fee_type_text311");
        feeType41 = getIntent().getExtras().getFloat("fee_type_text411");
        feeType51 = getIntent().getExtras().getFloat("fee_type_text511");
        fee1_type_text = getIntent().getStringExtra("fee1_type_text");
        fee_type_text1 = getIntent().getStringExtra("fee_type_text1");
        fee2_type_text = getIntent().getStringExtra("fee2_type_text");
        fee_type_text2 = getIntent().getStringExtra("fee_type_text2");
        fee3_type_text = getIntent().getStringExtra("fee3_type_text");
        fee_type_text3 = getIntent().getStringExtra("fee_type_text3");
        fee4_type_text = getIntent().getStringExtra("fee4_type_text");
        fee_type_text4 = getIntent().getStringExtra("fee_type_text4");
        fee5_type_text = getIntent().getStringExtra("fee5_type_text");
        fee_type_text5 = getIntent().getStringExtra("fee_type_text5");
        interest = getIntent().getExtras().getFloat("interest");
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
        feeEndDate = getIntent().getStringExtra("fee_end_date");
        days1 = getIntent().getStringExtra("days");
        //Utils.showToast(getApplicationContext(),fee_type_text11);

        linearLayout.setVisibility(View.GONE);

        if(feeType11 == 0)
        {
            linearLayout1.setVisibility(View.GONE);
        }

        else
        {
              feeType1.setText(fee1_type_text);
              feeType2.setText(Rs+feeType11);
        }

        if(feeType21 == 0)
        {
            linearLayout2.setVisibility(View.GONE);
        }

        else
        {
            feeType3.setText(fee2_type_text);
            feeType4.setText(Rs+feeType21);
        }

        if(feeType31 == 0)
        {
            linearLayout3.setVisibility(View.GONE);
        }

        else
        {
            feeType5.setText(fee3_type_text);
            feeType6.setText(Rs+feeType31);
        }

        if(feeType41 == 0)
        {
            linearLayout4.setVisibility(View.GONE);
        }

        else
        {
            feeType7.setText(fee4_type_text);
            feeType8.setText(Rs+feeType41);
        }

        if(feeType51 == 0)
        {
            linearLayout5.setVisibility(View.GONE);
        }

        else
        {
            feeType9.setText(fee5_type_text);
            feeType10.setText(Rs+feeType51);
        }

        if(delayCharges == 0)
        {
            linearLayout6.setVisibility(View.GONE);
        }

        else
        {
            feeDelayCharges.setText("Delay Charges");
            feeDelayCharges1.setText(Rs+delayCharges);
        }

        if(interest == 0)
        {
            linearLayout7.setVisibility(View.GONE);
        }

        else
        {
            feeInterest.setText("Interest");
            feeInterest1.setText(Rs+interest);
        }

        //handlingCharges11 = handlingCharges11+((feeType11+feeType21+feeType31+feeType41+feeType51+delayCharges+interest)*Float.parseFloat(Preferences.getInstance().feesAddCharges))/100;





        feeHandlingCharges.setText("Handling And Additional Charges");
        feeHandlingCharges1.setText(Rs+String.valueOf(handlingCharges));
        //total2 = total1+handlingCharges;
        //feeTotal1.setText(Rs+totalAmount);
        feeTotal3.setText(Rs+String.valueOf(finalTotalAmount));
       // feeTotal.setText("Total");
        feeTotal2.setText("Total with service tax");
        feeServiceTax.setText("Service Tax");
        feeServiceTax1.setText(Rs+String.valueOf(serviceTax));

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentStudentUnpaidFeesDetails.this,ParentFeesActivityOptionScreen.class);
                intent.putExtra("fee_type_text111",fee_type_text11);
                intent.putExtra("fee_type_text211",fee_type_text21);
                intent.putExtra("fee_type_text311",fee_type_text31);
                intent.putExtra("fee_type_text411",fee_type_text41);
                intent.putExtra("fee_type_text511",fee_type_text51);
                intent.putExtra("interest",interest);
                intent.putExtra("delay_charges",delayCharges);
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
                intent.putExtra("feeHandlingCharges",handlingCharges);
                intent.putExtra("totalAmount",totalAmount);
                intent.putExtra("finalTotalAmount",finalTotalAmount);
                intent.putExtra("fee_type_text11",fee_type_text11);
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
                intent.putExtra("days",days1);
                startActivity(intent);
            }
        });




        //openPDF();




    }

    public void openPDF(){

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF "), SELECT_PDF);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PDF)
            {
                System.out.println("SELECT_PDF");
                Uri selectedImageUri = data.getData();
                String  selectedPath = selectedImageUri.getPath();
                File myFile = new File(selectedImageUri.toString());
                String path = myFile.getAbsolutePath();
                //System.out.println("SELECT_AUDIO Path : " + selectedPath);
                Utils.showToast(getApplicationContext(),path);
                //doFileUpload();
            }

        }
    }
}
