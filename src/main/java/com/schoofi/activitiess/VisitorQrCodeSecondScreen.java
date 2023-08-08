package com.schoofi.activitiess;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.schoofi.utils.Preferences;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class VisitorQrCodeSecondScreen extends Activity implements ZBarScannerView.ResultHandler{

    private ZBarScannerView mScannerView;
    private String visitorName,visitorAddress,visitorMobile,visitorEmail,vehicleNumber,vehicleId,personToMeet,purposeOfMeet,visitorAccessories;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        visitorName = getIntent().getStringExtra("visitor_name");
        visitorAddress = getIntent().getStringExtra("visitor_address");
        visitorEmail = getIntent().getStringExtra("visitor_email");
        visitorMobile = getIntent().getStringExtra("visitor_mobile");
        vehicleNumber = getIntent().getStringExtra("visitor_vehicle_number");
        personToMeet = getIntent().getStringExtra("visitor_person_to_meet");
        purposeOfMeet = getIntent().getStringExtra("visitor_purpose");
        vehicleId = getIntent().getStringExtra("vehicle_id");
        visitorAccessories = getIntent().getStringExtra("visitor_accessories");
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("kk", rawResult.getContents()); // Prints scan results
        Log.v("kk", rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        Preferences.getInstance().loadPreference(getApplicationContext());
        Preferences.getInstance().visitorQrCode = rawResult.getContents();
        Preferences.getInstance().savePreference(getApplicationContext());

        Intent intent = new Intent(VisitorQrCodeSecondScreen.this,VisitorEntryFrom.class);
        intent.putExtra("value","1");
        intent.putExtra("visitor_name",visitorName);
        intent.putExtra("visitor_address",visitorAddress);
        intent.putExtra("visitor_email",visitorEmail);
        intent.putExtra("visitor_mobile",visitorMobile);
        intent.putExtra("visitor_vehicle_number",vehicleNumber);
        intent.putExtra("visitor_person_to_meet",personToMeet);
        intent.putExtra("visitor_purpose",purposeOfMeet);
        intent.putExtra("vehicle_id",vehicleId);
        intent.putExtra("visitor_accessories",visitorAccessories);
        intent.putExtra("visitor_type","9");

        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(VisitorQrCodeSecondScreen.this,VisitorEntryFrom.class);
        intent.putExtra("value","1");
        intent.putExtra("visitor_name",visitorName);
        intent.putExtra("visitor_address",visitorAddress);
        intent.putExtra("visitor_email",visitorEmail);
        intent.putExtra("visitor_mobile",visitorMobile);
        intent.putExtra("visitor_vehicle_number",vehicleNumber);
        intent.putExtra("visitor_person_to_meet",personToMeet);
        intent.putExtra("visitor_purpose",purposeOfMeet);
        intent.putExtra("vehicle_id",vehicleId);
        intent.putExtra("visitor_accessories",visitorAccessories);
        intent.putExtra("visitor_type","9");
        startActivity(intent);
        finish();
    }
}
