package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.schoofi.utils.Preferences;

public class PaymentCharges extends AppCompatActivity {

    TextView basicFees,total,handlingCharges;
    Button payNow;
    Float total1;
    String total2;
    String fees,eventId;
    String Rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_payment_charges);

        basicFees = (TextView) findViewById(R.id.text_basic_fees1);
        total = (TextView) findViewById(R.id.text_total_Fees);
        handlingCharges = (TextView) findViewById(R.id.text_handling_charges1);

        payNow = (Button) findViewById(R.id.btn_pay_now);

        fees = getIntent().getStringExtra("fees");
        eventId = getIntent().getStringExtra("eventId");
        Rs = getApplicationContext().getString(R.string.Rs);

        basicFees.setText(Rs+fees);
        handlingCharges.setText(Rs+ Preferences.getInstance().eventAddCharges);

        total1 = Float.parseFloat(fees)+Float.parseFloat(Preferences.getInstance().eventAddCharges);

        total.setText(Rs+String.valueOf(total1));

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                total2 = String.valueOf(total1);

                Intent intent = new Intent(PaymentCharges.this,PaymentActivitySecondScreen.class);
                intent.putExtra("fees",total2);
                intent.putExtra("eventId",eventId);
                startActivity(intent);
                finish();

            }
        });


    }
}
