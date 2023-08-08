package com.schoofi.activitiess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.LoginScreen;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.FilePath;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

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

public class DiaryReplyScreen extends AppCompatActivity {

    private ImageView back;
    private EditText message;
    private Button submit,uploadImage,uploadDocument;
    private static final int PICK_FILE_REQUEST = 1;
    private static final int READ_REQUEST_CODE = 42;
    private int STORAGE_PERMISSION_CODE = 23;
    int googleDrive =0,count=0;
    Uri uri;
    String selectedFilePath;
    ProgressDialog dialog;
    private String SERVER_URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.SCHOLL_DIARY_REPLY_SUBMIT;
    String diarySubId,diaryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Diary Reply Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_diary_reply_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        message = (EditText) findViewById(R.id.text_reply);
        submit = (Button) findViewById(R.id.btn_submit);
        uploadImage = (Button) findViewById(R.id.btn_upload_image);

        diarySubId = getIntent().getStringExtra("diarySubId");
        diaryId = getIntent().getStringExtra("diaryId");

        Preferences.getInstance().loadPreference(getApplicationContext());

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkAndRequestPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.

                    //getImages();

                        selectImage();

                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (message.getText().toString().matches("") || message.getText().toString().matches("null")) {

                    Utils.showToast(getApplicationContext(), "Please fill the message");
                } else if (diaryId.matches("") || diaryId.matches("null") || diarySubId.matches("") || diarySubId.matches("null")) {
                    Utils.showToast(getApplicationContext(), "Please try after-sometime");
                }
                else
                    if(count==0)
                    {
                        getChairmanStudentLeaveList();
                    }
                else if (googleDrive == 0) {
                    if (selectedFilePath != null) {
                        dialog = ProgressDialog.show(DiaryReplyScreen.this, "", "Uploading File...", true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //creating new thread to handle Http Operations
                                uploadFile(selectedFilePath);

                            }
                        }).start();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please choose a File First", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {

                        dialog = ProgressDialog.show(DiaryReplyScreen.this, "", "Uploading File...", true);
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
                                    connection.setRequestProperty("image", String.valueOf(uri));
                                    //connection.setRequestProperty("title","llll");

                                    //creating new dataoutputstream
                                    dataOutputStream = new DataOutputStream(connection.getOutputStream());


                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"token\"" + lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(Preferences.getInstance().token);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"device_id\"" + lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(Preferences.getInstance().deviceId);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"ins_id\"" + lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(Preferences.getInstance().institutionId);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sch_id\"" + lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(Preferences.getInstance().schoolId);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"diary_sub_id\"" + lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(diarySubId);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"diary_id\"" + lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(Preferences.getInstance().studentId);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"reply_message\"" + lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(message.getText().toString());
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"replied_by\"" + lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(Preferences.getInstance().userId);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"count\"" + lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(String.valueOf(count));
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
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


    private void selectImage() {
        final CharSequence[] items = {"Phone Storage", "Cloud Drives", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryReplyScreen.this);
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


            intent.setType("*/*");



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

             intent.setType("*/*");



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

    public int uploadFile(String selectedFilePath){

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

        Log.d("111","kkk");

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
                connection.setRequestProperty("image",selectedFilePath);
                Log.d("111","kkk1");
                //connection.setRequestProperty("title","llll");

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());


                //addFormField(dataOutputStream,"abc","a");

                //writing bytes to data outputstream
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"token\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().token);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                Log.d("111","kkk2");

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"device_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().deviceId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                Log.d("111","kkk3");

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

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"diary_sub_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(diarySubId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"diary_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().studentId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"reply_message\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(message.getText().toString());
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"replied_by\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().userId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"count\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(String.valueOf(count));
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);





                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                Log.d("111","kkk4");

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
                        Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog dialog;
        dialog = new ProgressDialog(DiaryReplyScreen.this, AlertDialog.THEME_HOLO_DARK);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        dialog.show();
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOLL_DIARY_REPLY_SUBMIT/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        Utils.showToast(getApplicationContext(), "Error submitting reply");
                        dialog.dismiss();
                        // notificationListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        dialog.dismiss();
                    }
                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(getApplicationContext(),"Reply Submitted Successfully");
                        dialog.dismiss();
                        finish();

                    }

                    else
                        Utils.showToast(getApplicationContext(), "Error Uploading");
                    dialog.dismiss();
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error uploading! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                    dialog.dismiss();
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error uploading! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                /*;
                params.put("sec_id",Preferences.getInstance().studentSectionId);

                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);

                params.put("cls_id", Preferences.getInstance().studentClassId);*/
                params.put("diary_sub_id",diarySubId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("diary_id", Preferences.getInstance().studentId);
                params.put("reply_message",message.getText().toString());
                params.put("replied_by",Preferences.getInstance().userId);
                params.put("count","0");
                params.put("token",Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);

                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to upload data, kindly enable internet settings!");
            dialog.dismiss();
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
