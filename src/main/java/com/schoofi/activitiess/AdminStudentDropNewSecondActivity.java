package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.AndroidMultiPartEntity;
import com.schoofi.utils.Config;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

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


public class AdminStudentDropNewSecondActivity extends AppCompatActivity {

    private int position;
    private String stu_name,stu_id,session,class_section_name,cls_id,sec_id,pickup_person_id,pickup_person_name;
    private ImageView back,studentImage,pickerImage;
    private Button scanStudentQrCode,scanParentQrCode,uploadPhoto,submit;
    private LinearLayout linearLayout;
    private int STORAGE_PERMISSION_CODE = 23;
    int f=0;
    private static final String TAG = AdminStudentDropForm.class.getSimpleName();
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    long totalSize = 0;
    String image;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Bitmap bitmap;
    public String UPLOAD_URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.BUS_PICKED_URL;

    private Uri fileUri; // file url to store image/video
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_student_drop_new_second);

        Preferences.getInstance().loadPreference(getApplicationContext());

        back= (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        position = getIntent().getExtras().getInt("position");
        stu_name = getIntent().getStringExtra("stu_name");
        stu_id = getIntent().getStringExtra("stu_id");
        session = getIntent().getStringExtra("session");
        class_section_name = getIntent().getStringExtra("class_section_name");
        cls_id = getIntent().getStringExtra("cls_id");
        sec_id = getIntent().getStringExtra("sec_id");
        pickup_person_name = getIntent().getStringExtra("picker_person_name");
        pickup_person_id = getIntent().getStringExtra("picker_person_id");

        Log.d("kkk",stu_name+stu_id+session+class_section_name+cls_id+sec_id+pickup_person_id+pickup_person_name);

        uploadPhoto = (Button) findViewById(R.id.btn_student_leave_upload_file);
        submit = (Button) findViewById(R.id.btn_student_submit);
        pickerImage = (ImageView) findViewById(R.id.imageLeave);

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(f==1)
                    {
                        postLeave();
                    }

                    else
                    {
                        postLeave1();
                    }
                }

        });
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                Preferences.getInstance().loadPreference(AdminStudentDropNewSecondActivity.this);
                //studentId = Preferences.getInstance().studentId;

                try {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                    image = getStringImage(thumbnail);
                    pickerImage.setImageBitmap(thumbnail);
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
                Preferences.getInstance().loadPreference(AdminStudentDropNewSecondActivity.this);
                // studentId = Preferences.getInstance().studentId;
                try {
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    //Setting the Bitmap to ImageView
                    pickerImage.setImageBitmap(bitmap);
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

                pickerImage.setImageBitmap(bitmap);



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
        return Uri.fromFile(getOutputMediaFile(type));
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
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
                        Toast.makeText(AdminStudentDropNewSecondActivity.this, s , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminStudentDropNewSecondActivity.this,ParentQrCodeScanner.class);
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
                        Utils.showToast(getApplicationContext(), "error2");
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


                    params.put("pickup_person_image", image);

                    params.put("token", Preferences.getInstance().token);
                    params.put("device_id", Preferences.getInstance().deviceId);

                    params.put("ins_id",Preferences.getInstance().institutionId);
                    params.put("sch_id",Preferences.getInstance().schoolId);
                    params.put("stu_id",stu_id);
                    params.put("stu_name",stu_name);
                    params.put("cls_id",cls_id);
                    params.put("sec_id",sec_id);
                    params.put("class_section_name",class_section_name);
                    params.put("picker_person_name",pickup_person_name);
                    params.put("pickup_person_id",pickup_person_id);
                    params.put("pickup_or_drop","drop");
                    params.put("session",session);
                   // params.put("stu_card_id",Preferences.getInstance().qrCode);
                    params.put("pickup_person_card_id",Preferences.getInstance().parentQrCode);

                    params.put("value","1");






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
                        Toast.makeText(AdminStudentDropNewSecondActivity.this, s, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminStudentDropNewSecondActivity.this,ParentQrCodeScanner.class);
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
                        Utils.showToast(getApplicationContext(), "error");
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
                    params.put("stu_id",stu_id);
                    params.put("stu_name",stu_name);
                    params.put("cls_id",cls_id);
                    params.put("sec_id",sec_id);
                    params.put("class_section_name",cls_id);
                    params.put("picker_person_name",pickup_person_name);
                    params.put("pickup_person_id",pickup_person_id);
                    params.put("pickup_or_drop","drop");
                    params.put("session",session);
                    // params.put("stu_card_id",Preferences.getInstance().qrCode);
                    params.put("pickup_person_card_id",Preferences.getInstance().parentQrCode);



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
