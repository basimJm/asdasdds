package com.schoofi.activitiess;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.schoofi.utils.Utils;

public class AdmissionCourierAddress extends AppCompatActivity {

    private EditText address,pincode;
    private Button done;
    String address1,brochureFees,courierCharges,admissionCharges,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admission_courier_address);

        address = (EditText) findViewById(R.id.edit_address);
        pincode = (EditText) findViewById(R.id.edit_pincode);

        done = (Button) findViewById(R.id.btn_done);

        address1 = getIntent().getStringExtra("address");
        brochureFees = getIntent().getStringExtra("brochureFees");
        courierCharges = getIntent().getStringExtra("courierCharges");
        admissionCharges = getIntent().getStringExtra("admissionCharges");
        id = getIntent().getStringExtra("id");


        address.setText(address1);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(address.getText().toString().matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please fill the address");
                }

                else
                    if(pincode.getText().toString().matches(""))
                    {
                        Utils.showToast(getApplicationContext(),"Please fill the pincode");
                    }
                else
                    {
                        Intent intent = new Intent(AdmissionCourierAddress.this,AdmissionPaymentCharges.class);
                        //intent.putExtra("email",userInput.getText().toString());
                        intent.putExtra("brochureFees",brochureFees);
                        intent.putExtra("admissionCharges",admissionCharges);
                        intent.putExtra("courierCharges",courierCharges);
                        intent.putExtra("address",address.getText().toString());
                        intent.putExtra("value","1");
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
            }
        });




    }
}
