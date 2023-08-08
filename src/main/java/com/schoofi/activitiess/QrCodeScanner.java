package com.schoofi.activitiess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.schoofi.utils.Preferences;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by Schoofi on 03-03-2018.
 */

public class QrCodeScanner extends Activity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
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
        Preferences.getInstance().qrCode = rawResult.getContents();
        Preferences.getInstance().savePreference(getApplicationContext());

        Intent intent = new Intent(QrCodeScanner.this,AdminStudentBusBoardingScreen.class);
        intent.putExtra("value","1");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(QrCodeScanner.this,AdminStudentBusBoardingScreen.class);
        intent.putExtra("value","0");
        startActivity(intent);
        finish();
    }
}

