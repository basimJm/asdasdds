package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schoofi.utils.Preferences;

public class AdmissionPaymentCharges extends AppCompatActivity {

    String brochureFees,addmissionFees,courierCharges,value;
    String id;
    TextView basicFees,handlingCharges,courierCharges1,total;
    LinearLayout linearLayout;
    Button payNow;
    Float total1;
    String Rs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admission_payment_charges);

        brochureFees = getIntent().getStringExtra("brochureFees");
        addmissionFees = getIntent().getStringExtra("admissionCharges");
        courierCharges = getIntent().getStringExtra("courierCharges");
        value = getIntent().getStringExtra("value");
        id = getIntent().getStringExtra("id");

        basicFees  = (TextView) findViewById(R.id.text_basic_fees1);
        handlingCharges = (TextView) findViewById(R.id.text_handling_charges1);
        courierCharges1 = (TextView) findViewById(R.id.text_courier_charges1);
        total = (TextView) findViewById(R.id.text_total_Fees);


        Preferences.getInstance().loadPreference(AdmissionPaymentCharges.this);
        Preferences.getInstance().paidAmount = total.getText().toString();
        Preferences.getInstance().savePreference(AdmissionPaymentCharges.this);


        payNow = (Button) findViewById(R.id.btn_pay_now);
        Rs = getApplicationContext().getString(R.string.Rs);

        linearLayout = (LinearLayout) findViewById(R.id.linear_courier);

        if(value.matches("2"))
        {
            linearLayout.setVisibility(View.GONE);
            basicFees.setText(Rs+brochureFees);
            handlingCharges.setText(Rs+addmissionFees);
            total1 = Float.parseFloat(brochureFees)+Float.parseFloat(addmissionFees);
            total.setText(Rs+String.valueOf(total1));
        }

        else
        {
            basicFees.setText(Rs+brochureFees);
            handlingCharges.setText(Rs+addmissionFees);
            courierCharges1.setText(Rs+courierCharges);
            total1 = Float.parseFloat(brochureFees)+Float.parseFloat(addmissionFees)+Float.parseFloat(courierCharges);
            total.setText(Rs+String.valueOf(total1));
        }


        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdmissionPaymentCharges.this,PaymentAdmissionActivityScreen.class);
                intent.putExtra("fees",String.valueOf(total1));
                intent.putExtra("value",value);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });




    }
}
