package com.schoofi.activitiess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.schoofi.adapters.HealthCardStudentListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.AndroidMultiPartEntity;
import com.schoofi.utils.Config;
import com.schoofi.utils.FilePath;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import smtchahal.materialspinner.MaterialSpinner;


public class VisitorEntryFrom extends AppCompatActivity {

    private ImageView back,visitorImage;
    private EditText visitorName,visitorAddress,visitorMobile,visiterEmail,visitorPersonToMeet,visitorPurpose,visitorCardNumberIssued;

    private Button upload,submit;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    int count1 = 0;
    String image;
    int count = 0;
    int count2 = 0;
    private Bitmap bitmap;
    private static final int CAMERA_REQUEST = 1888;
    //String image = getStringImage(bitmap);
    private int PICK_FILE_REQUEST = 1;
    private String selectedFilePath;
    long totalSize = 0;
    private String visitorType,studentId,classId,sectionId,parentType;


    private int STORAGE_PERMISSION_CODE = 23;
    int f;
    private static final String TAG = VisitorEntryFrom.class.getSimpleName();
    private JSONArray adminHealthArray;


    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video
    public String UPLOAD_URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.VISITOR_ENTRY_URL;
    public static final CharSequence[] DAYS_OPTIONS  = {"Two-Wheeler","Four-Wheeler","Other"};

    private MaterialSpinner vehicleType;
    private EditText vistorVehicleNumber,visitorAccessories;
    private String vehicleId1="";
    private Button scan;
    private String value;
    private int postion1,position2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.0){
            // 6.5inch device or bigger
            setContentView(R.layout.activity_visitor_entry_form_tablet);
        }else{
            // smaller device

            setContentView(R.layout.activity_visitor_entry_from);
        }


        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //scan = (Button) findViewById(R.id.btn_visitor_scan);

        value = getIntent().getStringExtra("value");

        visitorName = (EditText) findViewById(  R.id.edit_visitor_name);
        visitorAddress = (EditText) findViewById(R.id.edit_visitor_address);
        visitorMobile = (EditText) findViewById(R.id.edit_phone_number);
        visiterEmail = (EditText) findViewById(R.id.edit_visitor_email);
        visitorPersonToMeet = (EditText) findViewById(R.id.edit_visitor_person);
        visitorPurpose = (EditText) findViewById(R.id.edit_visitor_purpose);
       // visitorCardNumberIssued = (EditText) findViewById(R.id.edit_visitor_card_issued);
        vehicleType = (MaterialSpinner) findViewById(R.id.spinner_vehicle_type);
        vehicleType.setBackgroundResource(R.drawable.grey_button);
        vistorVehicleNumber = (EditText) findViewById(R.id.edit_visitor_vehicle_number);
        visitorAccessories = (EditText) findViewById(R.id.edit_visitor_accessories);
        scan = (Button) findViewById(R.id.btn_visitor_scan);
        scan.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (VisitorEntryFrom.this, R.layout.support_simple_spinner_dropdown_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        vehicleType.setAdapter(adapter); // Apply the adapter to the spinner







        vehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                vehicleId1 = "" + parent.getItemAtPosition(position);
                postion1 = position;


            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });

        if(value.matches("1"))
        {

            visitorName.setText(getIntent().getStringExtra("visitor_name"));
            visitorAddress.setText(getIntent().getStringExtra("visitor_address"));
            visiterEmail.setText(getIntent().getStringExtra("visitor_email"));
            visitorMobile.setText(getIntent().getStringExtra("visitor_mobile"));
            vistorVehicleNumber.setText(getIntent().getStringExtra("visitor_vehicle_number"));
            visitorPurpose.setText(getIntent().getStringExtra("visitor_purpose"));
            visitorPersonToMeet.setText(getIntent().getStringExtra("visitor_person_to_meet"));
            visitorAccessories.setText(getIntent().getStringExtra("visitor_accessories"));
            position2 = getIntent().getExtras().getInt("position");
            vehicleType.setSelection(position2);




        }

        Preferences.getInstance().loadPreference(getApplicationContext());
        if(Preferences.getInstance().visitorQrCode == null)
        {
            scan.setVisibility(View.VISIBLE);
        }

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkAndRequestPermissions()) {
                    Intent intent = new Intent(VisitorEntryFrom.this, VisitorQrCodeSecondScreen.class);
                    if(visitorName.getText().toString().matches(""))
                    {
                        intent.putExtra("visitor_name","");
                    }
                    else
                    {
                        intent.putExtra("visitor_name",visitorName.getText().toString());
                    }
                    if(visitorAddress.getText().toString().matches(""))
                    {
                        intent.putExtra("visitor_address","");
                    }
                    else
                    {
                        intent.putExtra("visitor_address",visitorAddress.getText().toString());
                    }

                    if(visitorMobile.getText().toString().matches(""))
                    {
                        intent.putExtra("visitor_mobile","");
                    }
                    else
                    {
                        intent.putExtra("visitor_mobile",visitorMobile.getText().toString());
                    }

                    if(visiterEmail.getText().toString().matches(""))
                    {
                        intent.putExtra("visitor_email","");
                    }

                    else
                    {
                        intent.putExtra("visitor_email",visiterEmail.getText().toString());
                    }

                    if(vistorVehicleNumber.getText().toString().matches(""))
                    {
                        intent.putExtra("visitor_vehicle_number","");
                    }
                    else
                    {
                        intent.putExtra("visitor_vehicle_number",vistorVehicleNumber.getText().toString());
                    }

                    if(vehicleId1.matches(""))
                    {
                        intent.putExtra("vehicle_id","");
                    }
                    else
                    {
                        intent.putExtra("vehicle_id",vehicleId1);
                    }

                    if(visitorPersonToMeet.getText().toString().matches(""))
                    {
                        intent.putExtra("visitor_person_to_meet","");
                    }
                    else
                    {
                        intent.putExtra("visitor_person_to_meet",visitorPersonToMeet.getText().toString());
                    }

                    if(visitorPurpose.getText().toString().matches(""))
                    {
                        intent.putExtra("visitor_purpose","");
                    }
                    else
                    {
                        intent.putExtra("visitor_purpose",visitorPurpose.getText().toString());
                    }

                    if(visitorAccessories.getText().toString().matches(""))
                    {
                        intent.putExtra("visitor_accessories","");
                    }
                    else
                    {
                        intent.putExtra("visitor_accessories",visitorAccessories.getText().toString());
                    }

                    startActivity(intent);
                    finish();
                }

            }
        });




        visitorImage = (ImageView) findViewById(R.id.visitorImage);
        upload = (Button) findViewById(R.id.btn_visitor_image);
        submit = (Button) findViewById(R.id.btn_visitor_submit);

        visitorType = getIntent().getStringExtra("visitor_type");
        if(visitorType.matches("1"))
        {
            //studentId = getIntent().getStringExtra("stu_id");
            classId = getIntent().getStringExtra("class_id");
            sectionId = getIntent().getStringExtra("section_id");
            parentType = getIntent().getStringExtra("parent_type");
          // Utils.showToast(getApplicationContext(),studentId+"-"+sectionId+"-"+classId+"-"+parentType);
            getStudentPollList();
        }

        /*scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitorEntryFrom.this,VisitorQrCodeScanner.class);
                startActivity(intent);
            }
        });

       if(value.matches("1"))
       {
           Preferences.getInstance().loadPreference(getApplicationContext());
           visitorCardNumberIssued.setText(Preferences.getInstance().visitorQrCode);

       }

       else

       {

       }*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(visitorName.getText().toString().matches("") || visitorAddress.getText().toString().matches("") || visitorMobile.getText().toString().matches("") || visitorPersonToMeet.getText().toString().matches("") || visitorPurpose.getText().toString().matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please fill the fields!");
                }
else
                {

                    if(f==1)
                    {
                        postLeave();
                    }

                    else {

                        postLeave1();
                    }

                }
            }
        });



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions())
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                    f=1;
                }
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                Preferences.getInstance().loadPreference(VisitorEntryFrom.this);
                studentId = Preferences.getInstance().studentId;

                try {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                    image = getStringImage(thumbnail);
                    visitorImage.setImageBitmap(thumbnail);
                }
                catch (OutOfMemoryError e)
                {
                    Utils.showToast(getApplicationContext(),"Image size too high Please select different image");
                }
                //Utils.showToast(getApplicationContext(),image);
				/*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
				File destination = new File(Environment.getExternalStorageDirectory(),
						System.currentTimeMillis() + ".jpg");
				FileOutputStream fo;
				try {
					destination.createNewFile();
					fo = new FileOutputStream(destination);
					fo.write(bytes.toByteArray());
					fo.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
                //ivImage.setImageBitmap(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                //String[] projection = {MediaStore.MediaColumns.DATA};
                Log.d("harsh", "kk" + selectedImageUri.toString());
                Preferences.getInstance().loadPreference(VisitorEntryFrom.this);
                studentId = Preferences.getInstance().studentId;
                try {
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    //Setting the Bitmap to ImageView
                    visitorImage.setImageBitmap(bitmap);
                    image = getStringImage(bitmap);


                    //Utils.showToast(getApplicationContext(),image);
                    //image = getStringImage(bitmap);
                    //System.out.println(image);
                    //Utils.showToast(getApplicationContext(), image);
                }
                catch (OutOfMemoryError e)
                {
                    Utils.showToast(getApplicationContext(),"Image size too high Please select different image");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

				/*CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
						null);
				Cursor cursor = cursorLoader.loadInBackground();
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
				cursor.moveToFirst();
				String selectedImagePath = cursor.getString(column_index);
				Bitmap bm;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(selectedImagePath, options);
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeFile(selectedImagePath, options);*/
                //ivImage.setImageBitmap(bm);
            }
        }
    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage()  {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


    }

   /* @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (resuleCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (intent == null) {
                    //no data present
                    return;
                }


                Uri selectedFileUri = intent.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);
                Log.i("kkk", "Selected File Path:" + selectedFilePath);


                if (selectedFilePath != null && !selectedFilePath.equals("")) {

                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }*/

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),STORAGE_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }



    /**
     * Receiving activity result method will be called after closing the camera
     * */
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                //launchUploadActivity(true);
                BitmapFactory.Options options = new BitmapFactory.Options();

                // down sizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;

                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

                visitorImage.setImageBitmap(bitmap);



            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                //launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }*/

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return FileProvider.getUriForFile(VisitorEntryFrom.this,
                BuildConfig.APPLICATION_ID + ".provider",getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            //progressBar.setProgress(progress[0]);

            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(fileUri.getPath());

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void getStudentPollList()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(VisitorEntryFrom.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_DETAILS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId+"&sec_id="+sectionId+"&stu_id="+Preferences.getInstance().studentId+"&parent_type="+parentType;
        Log.d("kk",url);
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        adminHealthArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=adminHealthArray && adminHealthArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminHealthArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_DETAILS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+classId+"&sec_id="+sectionId+"&stu_id="+Preferences.getInstance().studentId+"&parent_type="+parentType,e);
                            visitorName.setText(adminHealthArray.getJSONObject(0).getString("parent_name"));
                            visitorAddress.setText(adminHealthArray.getJSONObject(0).getString("parent_address"));
                            visiterEmail.setText(adminHealthArray.getJSONObject(0).getString("parent_email"));
                            visitorMobile.setText(adminHealthArray.getJSONObject(0).getString("parent_mobile"));

                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    private void postLeave()
    {
        //Utils.showToast(getApplicationContext(), ""+date2+date1);
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        //Preferences.getInstance().loadPreference(StudentNewLeave.this);
        //System.out.println(Preferences.getInstance().studentId);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //System.out.println(s);
                        //Showing toast message of the response
                        Toast.makeText(VisitorEntryFrom.this, s , Toast.LENGTH_SHORT).show();
                        Preferences.getInstance().loadPreference(getApplicationContext());
                        Preferences.getInstance().visitorQrCode = null;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        Intent intent = new Intent(VisitorEntryFrom.this,VisitorMainScreen.class);
                        intent.putExtra("value","5");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        System.out.println(volleyError);

                        //Showing toast
                        Utils.showToast(getApplicationContext(), "error in creating");
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                //Converting Bitmap to String

                // Preferences.getInstance().loadPreference(StudentNewLeave.this);
                //studentId = Preferences.getInstance().studentId;


                //http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
                //Getting Image Name
                // String name = editTextName.getText().toString().trim();

                //Creating parameters

                Map<String,String> params = new Hashtable<String, String>();
                //image = getStringImage(bitmap);

                //Adding parameters
                params.put("visitor_picture", image);

                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);

                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("visitor_name",visitorName.getText().toString());
                params.put("visitor_type",visitorType);
                Preferences.getInstance().loadPreference(getApplicationContext());
                if(visitorType.matches("1"))
                {
                    Log.d("ll",studentId+classId+sectionId+parentType);
                    params.put("stu_id",Preferences.getInstance().studentId);
                    params.put("cls_id",classId);
                    params.put("sec_id",sectionId);
                    params.put("parent_type",parentType);
                }
                else
                {

                }



                if(visiterEmail.getText().toString().matches(""))
                {

                }
                else
                {
                    params.put("visitor_email",visiterEmail.getText().toString());
                }

                if(vehicleId1.matches("") || vehicleId1.matches("Select Vehicle Type"))
                {

                }

                else
                {
                    params.put("vehicle_type",vehicleId1);
                }

                if(vistorVehicleNumber.getText().toString().matches(""))
                {
                    params.put("vehicle_number","");
                }

                else
                {
                    params.put("vehicle_number",vistorVehicleNumber.getText().toString());
                }



                params.put("visit_reason",visitorPurpose.getText().toString());
                params.put("visitor_address",visitorAddress.getText().toString());
                params.put("visit_person",visitorPersonToMeet.getText().toString());
                params.put("visitor_phone_no",visitorMobile.getText().toString());
                params.put("value","1");
                if(Preferences.getInstance().visitorQrCode==null)
                {

                }
                else {
                    params.put("card_issued", Preferences.getInstance().visitorQrCode);
                }

                if(visitorAccessories.getText().toString().matches(""))
                {

                }

                else
                {
                    params.put("visitor_accessories",visitorAccessories.getText().toString());
                }



                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



    private void postLeave1() {
        //Utils.showToast(getApplicationContext(), ""+date2+date1);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        //Preferences.getInstance().loadPreference(StudentNewLeave.this);
        //System.out.println(Preferences.getInstance().studentId);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //System.out.println(s);
                        //Showing toast message of the response
                        Toast.makeText(VisitorEntryFrom.this, s, Toast.LENGTH_SHORT).show();
                        Preferences.getInstance().loadPreference(getApplicationContext());
                        Preferences.getInstance().visitorQrCode = null;
                        Preferences.getInstance().savePreference(getApplicationContext());
                        Intent intent = new Intent(VisitorEntryFrom.this,VisitorMainScreen.class);
                        intent.putExtra("value","9");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        System.out.println(volleyError);

                        //Showing toast
                        Utils.showToast(getApplicationContext(), "error in creating");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //Converting Bitmap to String

                // Preferences.getInstance().loadPreference(StudentNewLeave.this);
                //studentId = Preferences.getInstance().studentId;


                //http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
                //Getting Image Name
                // String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);

                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("visitor_name",visitorName.getText().toString());
                params.put("visitor_phone_no",visitorMobile.getText().toString());
                params.put("visitor_type",visitorType);
                if(visitorType.matches("1"))
                {
                    params.put("stu_id",Preferences.getInstance().studentId);
                    params.put("cls_id",classId);
                    params.put("sec_id",sectionId);
                    params.put("parent_type",parentType);
                }
                else
                {

                }


                if(visiterEmail.getText().toString().matches(""))
                {

                }
                else
                {
                    params.put("visitor_email",visiterEmail.getText().toString());
                }

                if(vehicleId1.matches("") || vehicleId1.matches("Select Vehicle Type"))
                {

                }

                else
                {
                    params.put("vehicle_type",vehicleId1);
                }

                if(vistorVehicleNumber.getText().toString().matches(""))
                {
                    params.put("vehicle_number","");
                }

                else
                {
                    params.put("vehicle_number",vistorVehicleNumber.getText().toString());
                }

                params.put("visit_reason",visitorPurpose.getText().toString());
                params.put("visitor_address",visitorAddress.getText().toString());
                params.put("visit_person",visitorPersonToMeet.getText().toString());
                if(Preferences.getInstance().visitorQrCode==null)
                {

                }
                else {
                    params.put("card_issued", Preferences.getInstance().visitorQrCode);
                }

                if(visitorAccessories.getText().toString().matches(""))
                {

                }

                else
                {
                    params.put("visitor_accessories",visitorAccessories.getText().toString());
                }





                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
