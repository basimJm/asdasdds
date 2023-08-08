package com.schoofi.activitiess;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.NotificationIntentAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.AnnouncementTypeVO;
import com.schoofi.utils.EventUploadAudienceVO;
import com.schoofi.utils.FilePath;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewAnnouncement extends AppCompatActivity {

    Spinner audience,type,announceType;
    String announceType1,type1,audience1;
    EditText title,description;
    Button upload,submit;
    Switch holiday;
    ImageView back;
    ArrayList<String> typeName;
    ArrayList<AnnouncementTypeVO> announcementTypeVOs;
    ArrayList<String> audienceName;
    ArrayList<EventUploadAudienceVO> eventUploadAudienceVOs;
    ProgressDialog dialog;
    JSONObject jsonobject;
    JSONArray jsonarray;
    private static final int PICK_FILE_REQUEST = 1;
    private static final int READ_REQUEST_CODE = 42;
    private int STORAGE_PERMISSION_CODE = 23;
    int googleDrive =0,count=0;
    Uri uri;
    String selectedFilePath;
    public static final CharSequence[] DAYS_OPTIONS  = {"Select Announcement Mode","Audio","Video","Image","Document","Text"};
    private String SERVER_URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ADD_ANNOUNCEMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("New Announcement");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_new_announcement);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        audience = (Spinner) findViewById(R.id.spin_audience);
        type = (Spinner) findViewById(R.id.spin_type);
        announceType = (Spinner) findViewById(R.id.spin_announce_type);

        title = (EditText) findViewById(R.id.edit_title);
        description = (EditText) findViewById(R.id.edit_description);
        upload = (Button) findViewById(R.id.btn_upload);
        submit = (Button) findViewById(R.id.btn_submit);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        announceType.setAdapter(adapter); // Apply the adapter to the spinner

        holiday = (Switch) findViewById(R.id.mySwitch1);

        announceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                announceType1 = ""+parent.getItemAtPosition(position);


            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });

        if(Utils.isNetworkAvailable(getApplicationContext()))
        {
           new DownloadJSON().execute();
            new DownloadJSON1().execute();
        }

        else
        {
            Utils.showToast(getApplicationContext(),"Please enable the internet");
        }

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkAndRequestPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.

                    //getImages();
                    if(announceType1.matches("Select Announcement Mode"))
                    {
                        Utils.showToast(getApplicationContext(),"Please select the announcement type");
                    }

                    else
                     if(announceType1.matches("Text"))
                    {
                        Utils.showToast(getApplicationContext(),"You have selected the type Text");
                        announceType1="Text";
                    }

                    else {
                        selectImage();
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(title.getText().toString().matches("") || description.getText().toString().matches("") || announceType1.matches("Select Announcement Mode") || type1.matches("") || audience1.matches(""))
                {
                    Utils.showToast(getApplicationContext(),"All fields are mandatory");
                }

                else
                    if(announceType1.matches("Text"))
                    {
                        getChairmanStudentLeaveList();
                    }

                else
                    if(googleDrive==0)
                    {
                        if (selectedFilePath != null) {
                            dialog = ProgressDialog.show(NewAnnouncement.this, "", "Uploading File...", true);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //creating new thread to handle Http Operations
                                    uploadFile(selectedFilePath);

                                }
                            }).start();
                        } else {
                            Toast.makeText(NewAnnouncement.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
                        }
                    }

                else
                    {
                        try {

                            dialog = ProgressDialog.show(NewAnnouncement.this, "", "Uploading File...", true);
                            final ParcelFileDescriptor mInputPFD = getContentResolver().openFileDescriptor(uri, "r");
                            final FileDescriptor fd = mInputPFD.getFileDescriptor();

                            final Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
                            final int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                            returnCursor.moveToFirst();
                            Log.d("hhhh", returnCursor.getString(nameIndex));


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //creating new thread to handle Http Operations
                                    int serverResponseCode = 0;

                                    HttpURLConnection connection;
                                    DataOutputStream dataOutputStream;
                                    String lineEnd = "\r\n";
                                    String twoHyphens = "--";
                                    String boundary = "*****";


                                    int bytesRead, bytesAvailable, bufferSize;
                                    byte[] buffer;
                                    int maxBufferSize = 1 * 2048 * 2048;

                                    try {


                                        FileInputStream fileInputStream = new FileInputStream(mInputPFD.getFileDescriptor());
                                        URL url = new URL(SERVER_URL);
                                        connection = (HttpURLConnection) url.openConnection();
                                        connection.setDoInput(true);//Allow Inputs
                                        connection.setDoOutput(true);//Allow Outputs
                                        connection.setUseCaches(false);//Don't use a cached Copy
                                        connection.setRequestMethod("POST");
                                        connection.setRequestProperty("Connection", "Keep-Alive");
                                        connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                                        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                                        connection.setRequestProperty("file",String.valueOf(uri));
                                        //connection.setRequestProperty("title","llll");

                                        //creating new dataoutputstream
                                        dataOutputStream = new DataOutputStream(connection.getOutputStream());

                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"ins_id\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(Preferences.getInstance().institutionId);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sch_id\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(Preferences.getInstance().schoolId);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announce_type\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(announceType1);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announcement_to\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(audience1);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announce_cat\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(type1);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user_id\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(Preferences.getInstance().userId);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announcement_title\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(title.getText().toString());
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"token\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(Preferences.getInstance().token);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"device_id\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(Preferences.getInstance().deviceId);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        if(holiday.isChecked())
                                        {
                                            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"notify_employees\""+ lineEnd);
                                            dataOutputStream.writeBytes(lineEnd);
                                            dataOutputStream.writeBytes("yes");
                                            dataOutputStream.writeBytes(lineEnd);
                                            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                                        }

                                        else
                                        {
                                            if(!holiday.isChecked())
                                            {
                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"notify_employees\""+ lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes("no");
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                                            }
                                        }

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user_id\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(Preferences.getInstance().userId);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announcement\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(description.getText().toString());
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);




                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"document\";filename=\""
                                                + returnCursor.getString(nameIndex) + "\"" + lineEnd);

                                        dataOutputStream.writeBytes(lineEnd);

                                        //returns no. of bytes present in fileInputStream
                                        bytesAvailable = fileInputStream.available();
                                        //selecting the buffer size as minimum of available bytes or 1 MB
                                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                        //setting the buffer as byte array of size of bufferSize
                                        buffer = new byte[bufferSize];

                                        //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                                        //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                                        while (bytesRead > 0) {
                                            //write the bytes read from inputstream
                                            dataOutputStream.write(buffer, 0, bufferSize);
                                            bytesAvailable = fileInputStream.available();
                                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                                        }

                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                /*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"abc\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("a");//your parameter value
                dataOutputStream.writeBytes(lineEnd); //to add multiple parameters write Content-Disposition: form-data; name=\"your parameter name\"" + crlf again and keep repeating till here :)
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens);*/



                /*dataOutputStream.writeBytes(String.valueOf((twoHyphens+boundary+lineEnd).getBytes()));
                dataOutputStream.write( "Content-Type: text/plain".getBytes());
                dataOutputStream.write( ("Content-Disposition: form-data; name="+ "abc"+lineEnd).getBytes());;
                dataOutputStream.write( (lineEnd + "a" + lineEnd).getBytes());*/

                                        serverResponseCode = connection.getResponseCode();
                                        String serverResponseMessage = connection.getResponseMessage();

                                        Log.i("jjjj", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
                                        InputStream is = connection.getInputStream();

                                        // retrieve the response from server
                                        int ch;

                                        StringBuffer b = new StringBuffer();
                                        while ((ch = is.read()) != -1) {
                                            b.append((char) ch);
                                        }
                                        String s = b.toString();
                                        Log.i("Response", s);

                                        //response code of 200 indicates the server status OK
                                        if (serverResponseCode == 200) {
                                            finish();
                                        }

                                        fileInputStream.close();
                                        dataOutputStream.flush();
                                        dataOutputStream.close();


                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    dialog.dismiss();
                                }
                            }).start();


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

}



        });



        }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        switch (requestCode) {
            case 1:
                if (resuleCode == Activity.RESULT_OK) {
                    if (requestCode == PICK_FILE_REQUEST) {
                        if (intent == null) {
                            //no data present
                            return;
                        }


                        Uri selectedFileUri = intent.getData();
                        selectedFilePath = FilePath.getPath(this, selectedFileUri);
                        Log.i("lll", "Selected File Path:" + selectedFilePath);

                        count = 1;

                        if (selectedFilePath != null && !selectedFilePath.equals("")) {

                        } else {
                            Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;

            case 42:
                if (requestCode == READ_REQUEST_CODE && resuleCode == Activity.RESULT_OK) {
                    // The document selected by the user won't be returned in the intent.
                    // Instead, a URI to that document will be contained in the return intent
                    // provided to this method as a parameter.
                    // Pull that URI using resultData.getData().

                    if (intent != null) {
                        uri = intent.getData();
                        count = 1;
                    }


                    //showImage(uri);
                }


                break;
        }
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            announcementTypeVOs= new ArrayList<AnnouncementTypeVO>();
            typeName = new ArrayList<String>();
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ANNOUNCEMENT_TYPE+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("type");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                   AnnouncementTypeVO announcementTypeVO = new AnnouncementTypeVO();
                    announcementTypeVO.setTypeId(jsonobject.optString("code"));
                    announcementTypeVOs.add(announcementTypeVO);
                    typeName.add(jsonobject.optString("full_name"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            type
                    .setAdapter(new ArrayAdapter<String>(NewAnnouncement.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            typeName));

            type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub



                    type1 = announcementTypeVOs.get(position).getTypeId().toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }




    }

    private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            eventUploadAudienceVOs= new ArrayList<EventUploadAudienceVO>();
            audienceName = new ArrayList<String>();
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EVENT_UPLOAD_AUDIENCE_URL+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("groups");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    EventUploadAudienceVO eventUploadAudienceVO = new EventUploadAudienceVO();
                    eventUploadAudienceVO.setGroupId(jsonobject.optString("group_id"));
                    eventUploadAudienceVOs.add(eventUploadAudienceVO);
                    audienceName.add(jsonobject.optString("group_name"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            audience
                    .setAdapter(new ArrayAdapter<String>(NewAnnouncement.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            audienceName));

            audience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub



                    audience1 = eventUploadAudienceVOs.get(position).getGroupId().toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }




    }

    private void selectImage() {
        final CharSequence[] items = {"Phone Storage", "Cloud Drives", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(NewAnnouncement.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Phone Storage")) {
                    showFileChooser();
                } else if (items[item].equals("Cloud Drives")) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        showFileChooser1();
                    } else {
                        Utils.showToast(getApplicationContext(), "Your device is not compatible for this feature");
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files

        if(announceType1.matches("Audio"))
        {
            intent.setType("audio/*");
            announceType1 ="audio";
        }

        if(announceType1.matches("Video"))
        {
            intent.setType("video/*");
            announceType1 ="video";
        }

        if(announceType1.matches("Image"))
        {
            intent.setType("image/*");
            announceType1 ="image";
        }


        else
        if(announceType1.matches("Document"))
        {
            intent.setType("*/*");
            announceType1 = "pdf";
        }

        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    private void showFileChooser1() {

        googleDrive =1;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".

        if(announceType1.matches("Audio"))
        {
            intent.setType("audio/*");
            announceType1 ="audio";
        }

        if(announceType1.matches("Video"))
        {
            intent.setType("video/*");
            announceType1 ="video";
        }

        if(announceType1.matches("Image"))
        {
            intent.setType("image/*");
            announceType1 ="image";
        }


        else
        if(announceType1.matches("Document"))
        {
            intent.setType("*/*");
            announceType1 = "pdf";
        }

        startActivityForResult(intent, READ_REQUEST_CODE);
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

    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 2048 * 2048;
        File selectedFile = new File(selectedFilePath);
        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                    Log.d("kkk","lll");
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("file",selectedFilePath);
                //connection.setRequestProperty("title","llll");

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                //addFormField(dataOutputStream,"abc","a");

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"ins_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().institutionId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sch_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().schoolId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announce_type\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(announceType1);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announcement_to\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(audience1);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announce_cat\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(type1);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().userId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announcement_title\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(title.getText().toString());
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"token\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().token);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"device_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().deviceId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().userId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                if(holiday.isChecked())
                {
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"notify_employees\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes("yes");
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                }

                else
                {
                    if(holiday.isChecked())
                    {
                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"notify_employees\""+ lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes("no");
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    }
                }



                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"announcement\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(description.getText().toString());
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);




                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"document\";filename=\""
                            + selectedFilePath + "\"" + lineEnd);

                    dataOutputStream.writeBytes(lineEnd);

                    //returns no. of bytes present in fileInputStream
                    bytesAvailable = fileInputStream.available();
                    //selecting the buffer size as minimum of available bytes or 1 MB
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    //setting the buffer as byte array of size of bufferSize
                    buffer = new byte[bufferSize];

                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                    while (bytesRead > 0) {
                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                /*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"abc\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("a");//your parameter value
                dataOutputStream.writeBytes(lineEnd); //to add multiple parameters write Content-Disposition: form-data; name=\"your parameter name\"" + crlf again and keep repeating till here :)
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens);*/



                /*dataOutputStream.writeBytes(String.valueOf((twoHyphens+boundary+lineEnd).getBytes()));
                dataOutputStream.write( "Content-Type: text/plain".getBytes());
                dataOutputStream.write( ("Content-Disposition: form-data; name="+ "abc"+lineEnd).getBytes());;
                dataOutputStream.write( (lineEnd + "a" + lineEnd).getBytes());*/


                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i("kkk", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
                InputStream is = connection.getInputStream();

                // retrieve the response from server
                int ch;

                StringBuffer b =new StringBuffer();
                while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
                String s=b.toString();
                Log.i("Response",s);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            //tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewAnnouncement.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(NewAnnouncement.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(NewAnnouncement.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(NewAnnouncement.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADD_ANNOUNCEMENT/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                // System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(NewAnnouncement.this, "Error uploading announcement");
                        loading.dismiss();
                       // notificationListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(NewAnnouncement.this, "Session Expired:Please Login Again");
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                            Utils.showToast(getApplicationContext(),"Announcement Uploaded Successfully");
                            finish();
                        loading.dismiss();

                        }

                    else
                        Utils.showToast(NewAnnouncement.this, "Error Uploading");
                    loading.dismiss();
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(NewAnnouncement.this, "Error uploading! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                    loading.dismiss();
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error uploading! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(NewAnnouncement.this);
                Map<String,String> params = new HashMap<String, String>();
                /*;
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);

                params.put("cls_id", Preferences.getInstance().studentClassId);*/
                params.put("user_id",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("announcement_title",title.getText().toString());
                params.put("announcement",description.getText().toString());
                params.put("announcement_to",audience1);
                params.put("announce_type",announceType1);
                params.put("announce_cat",type1);
                if(holiday.isChecked())
                {
                    params.put("notify_employees","yes");
                }

                else
                {
                    params.put("notify_employees","no");
                }
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(NewAnnouncement.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(NewAnnouncement.this, "Unable to upload data, kindly enable internet settings!");
            loading.dismiss();
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
