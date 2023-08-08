package com.schoofi.activitiess;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.AnnouncementTypeVO;
import com.schoofi.utils.ClassVO;
import com.schoofi.utils.FilePath;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.KnowledgeResourseSubTopicVO;
import com.schoofi.utils.KnowledgeResourseTopicVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SubjectVO;
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

import smtchahal.materialspinner.MaterialSpinner;

public class KnowledgeResourseUploadingScreen extends AppCompatActivity {

    private ImageView back;
    private MaterialSpinner spinnerClass,spinnerSubject,spinnerDocumentType,spinnerKnowledgeTopic,spinnerKnowledgeSubTopic,spinnerResourseType;
    private EditText title,description,editTopic,editSubTopic;
    private Button upload,submit;

    ArrayList<String> typeName;
    ArrayList<AnnouncementTypeVO> announcementTypeVOs;
    ArrayList<String> className;
    ArrayList<ClassVO> classVOS;
    ArrayList<String> subjectName;
    ArrayList<SubjectVO> subjectVOS;
    ArrayList<String> topicName;
    ArrayList<KnowledgeResourseTopicVO> topicVOS;
    ArrayList<String> subTopicName;
    ArrayList<KnowledgeResourseSubTopicVO> subTopicVOS;
    ProgressDialog dialog;


    JSONObject jsonobject;
    JSONArray jsonarray;
    public static final CharSequence[] DAYS_OPTIONS  = {"Sample Paper","Previous Year Paper","Reference Paper","Test Paper","Other"};
    public static final CharSequence[] DAYS_OPTIONS1  = {"Audio","Video","Image","Document","Text"};
    private String SERVER_URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURCE_SUBMIT_LIST;

     String className1,subjectName1,resourseType1,documentType1,subjectId1,topicId1="",subTopicId1="";
     String valueDecider = "0";

    private static final int PICK_FILE_REQUEST = 1;
    private static final int READ_REQUEST_CODE = 42;
    private int STORAGE_PERMISSION_CODE = 23;
    int googleDrive =0,count=0;
    Uri uri;
    String selectedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_knowledge_resourse_uploading_screen);

        back = (ImageView) findViewById(R.id.img_back);
        spinnerClass = (MaterialSpinner) findViewById(R.id.spinner_class);
        spinnerSubject = (MaterialSpinner) findViewById(R.id.spinner_subject);
        spinnerDocumentType = (MaterialSpinner) findViewById(R.id.document_type);
        spinnerKnowledgeTopic = (MaterialSpinner) findViewById(R.id.spinner_topic);
        spinnerKnowledgeSubTopic = (MaterialSpinner) findViewById(R.id.spinner_subTopic);
        spinnerResourseType = (MaterialSpinner) findViewById(R.id.resourse_type);
        title = (EditText) findViewById(R.id.edit_diagnosis);
        description = (EditText) findViewById(R.id.edit_description);
        upload = (Button) findViewById(R.id.btn_upload);
        submit = (Button) findViewById(R.id.btn_next);

        spinnerClass.setBackgroundResource(R.drawable.grey_button);
        spinnerSubject.setBackgroundResource(R.drawable.grey_button);
        spinnerDocumentType.setBackgroundResource(R.drawable.grey_button);
        spinnerKnowledgeTopic.setBackgroundResource(R.drawable.grey_button);
        spinnerKnowledgeSubTopic.setBackgroundResource(R.drawable.grey_button);
        spinnerResourseType.setBackgroundResource(R.drawable.grey_button);

        editTopic = (EditText) findViewById(R.id.edit_topic);
        editSubTopic = (EditText) findViewById(R.id.edit_sub_topic);

        editTopic.setVisibility(View.GONE);
        editSubTopic.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item, DAYS_OPTIONS1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinnerDocumentType.setAdapter(adapter); // Apply the adapter to the spinner



        spinnerDocumentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                documentType1 = ""+parent.getItemAtPosition(position);


            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });


        ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item, DAYS_OPTIONS);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinnerResourseType.setAdapter(adapter1); // Apply the adapter to the spinner



        spinnerResourseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                resourseType1 = ""+parent.getItemAtPosition(position);


            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });

        if(Utils.isNetworkAvailable(getApplicationContext()))
        {
            new DownloadJSON().execute();
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
                    if(documentType1.matches("Select Document Type"))
                    {
                        Utils.showToast(getApplicationContext(),"Please select the announcement type");
                    }

                    else
                    if(documentType1.matches("Text"))
                    {
                        Utils.showToast(getApplicationContext(),"You have selected the type Text");
                        documentType1="Text";
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

                if(valueDecider.matches("1"))
                {

                    topicId1 = editTopic.getText().toString();
                    subTopicId1 = editSubTopic.getText().toString();
                    Log.d("po","pooo");
                }

                else
                    if(valueDecider.matches("2"))
                {

                    subTopicId1 = editSubTopic.getText().toString();
                }


                   if(title.getText().toString().matches("") || description.getText().toString().matches("") || resourseType1.matches("Select Resourse Type") || topicId1.matches("") || subTopicId1.matches(""))
                {
                    Utils.showToast(getApplicationContext(),"All fields are mandatory");
                }

                else
                if(documentType1.matches("Text"))
                {
                    getChairmanStudentLeaveList();
                }

                else
                if(googleDrive==0)
                {
                    if (selectedFilePath != null) {
                        dialog = ProgressDialog.show(KnowledgeResourseUploadingScreen.this, "", "Uploading File...", true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //creating new thread to handle Http Operations
                                uploadFile(selectedFilePath);

                            }
                        }).start();
                    } else {
                        Toast.makeText(KnowledgeResourseUploadingScreen.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    try {

                        dialog = ProgressDialog.show(KnowledgeResourseUploadingScreen.this, "", "Uploading File...", true);
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

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"document_type\""+ lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(resourseType1);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_by\""+ lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(Preferences.getInstance().userId);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
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





                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(description.getText().toString());
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"subject_id\""+ lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(subjectId1);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                    if(valueDecider.matches("1"))
                                    {
                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"topic\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(editTopic.getText().toString());
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_topic\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(editSubTopic.getText().toString());
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"value\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes("1");
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                                    }

                                    else
                                    if(valueDecider.matches("2"))
                                    {
                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"topic_id\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(topicId1);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_topic\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(editSubTopic.getText().toString());
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"value\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes("2");
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                                    }

                                    else
                                    {
                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"topic_id\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(topicId1);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_topic_id\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(subTopicId1);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"value\""+ lineEnd);
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes("0");
                                        dataOutputStream.writeBytes(lineEnd);
                                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                                    }


                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"class_number\""+ lineEnd);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(className1);
                                    dataOutputStream.writeBytes(lineEnd);
                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);




                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
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
            classVOS= new ArrayList<ClassVO>();
            className = new ArrayList<String>();

            Log.d("url1",AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_CLASS_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token);
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_CLASS_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("class_list");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    ClassVO classVO = new ClassVO();
                    classVO.setClassName(jsonobject.optString("class_number"));
                    classVOS.add(classVO);
                    className.add(jsonobject.optString("class_number"));

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

            spinnerClass
                    .setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            className));

            spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub



                    className1 = classVOS.get(position).getClassName().toString();
                    new DownloadJSON1().execute();

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
            subjectVOS= new ArrayList<SubjectVO>();
            subjectName = new ArrayList<String>();

            Log.d("url1",AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_SUBJECT_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&class_number="+className1);
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_SUBJECT_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&class_number="+className1);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("subject_lists");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    SubjectVO subjectVO = new SubjectVO();
                    subjectVO.setSubjectName(jsonobject.optString("subject_name"));
                    subjectVO.setSubjectID(jsonobject.optString("subject_id"));
                    subjectVOS.add(subjectVO);
                    subjectName.add(jsonobject.optString("subject_name"));

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

            spinnerSubject
                    .setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            subjectName));

            spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub



                    subjectName1 = subjectVOS.get(position).getSubjectName().toString();
                    subjectId1 = subjectVOS.get(position).getSubjectID().toString();
                    new DownloadJSON2().execute();


                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }




    }




    private class DownloadJSON2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            topicVOS= new ArrayList<KnowledgeResourseTopicVO>();
            topicName = new ArrayList<String>();

            Log.d("url1",AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_TOPIC_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&class_id="+className1+"&subject_id="+subjectId1);
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_TOPIC_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&class_id="+className1+"&subject_id="+subjectId1);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("topic_details");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    KnowledgeResourseTopicVO knowledgeResourseTopicVO = new KnowledgeResourseTopicVO();
                    knowledgeResourseTopicVO.setTopicId(jsonobject.optString("topic_id"));
                    topicVOS.add(knowledgeResourseTopicVO);

                    topicName.add(jsonobject.optString("topic"));

                }

                KnowledgeResourseTopicVO knowledgeResourseTopicVO1 = new KnowledgeResourseTopicVO();
                knowledgeResourseTopicVO1.setTopicId("Other");
                topicVOS.add(knowledgeResourseTopicVO1);

                topicName.add("Other");

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            spinnerKnowledgeTopic
                    .setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            topicName));

            spinnerKnowledgeTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub



                    topicId1 = topicVOS.get(position).getTopicId().toString();
                    if(topicId1.matches("Other"))
                    {
                        editTopic.setVisibility(View.VISIBLE);
                        spinnerKnowledgeSubTopic.setVisibility(View.GONE);
                        editSubTopic.setVisibility(View.VISIBLE);
                        subTopicId1="Other";
                        valueDecider = "1";
                    }
                    else
                    {
                        new DownloadJSON3().execute();
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }




    }


    private class DownloadJSON3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            subTopicVOS= new ArrayList<KnowledgeResourseSubTopicVO>();
            subTopicName = new ArrayList<String>();

            Log.d("url1",AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_SUB_TOPIC_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&class_id="+className1+"&subject_id="+subjectId1+"&topic_id="+topicId1);
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_SUB_TOPIC_LIST+"?ins_id="+ Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&class_id="+className1+"&subject_id="+subjectId1+"&topic_id="+topicId1);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("subtopic_details");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    KnowledgeResourseSubTopicVO knowledgeResourseSubTopicVO = new KnowledgeResourseSubTopicVO();
                    knowledgeResourseSubTopicVO.setSubTopicId(jsonobject.optString("sub_topic_id"));
                    subTopicVOS.add(knowledgeResourseSubTopicVO);
                    subTopicName.add(jsonobject.optString("sub_topic"));

                }

                KnowledgeResourseSubTopicVO knowledgeResourseSubTopicVO1 = new KnowledgeResourseSubTopicVO();
                knowledgeResourseSubTopicVO1.setSubTopicId("Other");
                subTopicVOS.add(knowledgeResourseSubTopicVO1);

                subTopicName.add("Other");
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            spinnerKnowledgeSubTopic
                    .setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            subTopicName));

            spinnerKnowledgeSubTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub



                    subTopicId1 = subTopicVOS.get(position).getSubTopicId().toString();

                    if(subTopicId1.matches("Other"))
                    {
                        editSubTopic.setVisibility(View.VISIBLE);
                        valueDecider = "2";
                    }
                    else
                    {

                    }
                    //new DownloadJSON3().execute();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(KnowledgeResourseUploadingScreen.this);
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

        if(documentType1.matches("Audio"))
        {
            intent.setType("audio/*");
            documentType1 ="audio";
        }

        if(documentType1.matches("Video"))
        {
            intent.setType("video/*");
            documentType1 ="video";
        }

        if(documentType1.matches("Image"))
        {
            intent.setType("image/*");
            documentType1 ="image";
        }


        else
        if(documentType1.matches("Document"))
        {
            intent.setType("*/*");
            documentType1 = "pdf";
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

        if(documentType1.matches("Audio"))
        {
            intent.setType("audio/*");
            documentType1 ="audio";
        }

        if(documentType1.matches("Video"))
        {
            intent.setType("video/*");
            documentType1 ="video";
        }

        if(documentType1.matches("Image"))
        {
            intent.setType("image/*");
            documentType1 ="image";
        }


        else
        if(documentType1.matches("Document"))
        {
            intent.setType("*/*");
            documentType1 = "pdf";
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

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"document_type\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(resourseType1);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_by\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().userId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
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





                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(description.getText().toString());
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"subject_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(subjectId1);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                if(valueDecider.matches("1"))
                {
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"topic\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(editTopic.getText().toString());
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_topic\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(editSubTopic.getText().toString());
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"value\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes("1");
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                }

                else
                    if(valueDecider.matches("2"))
                {
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"topic_id\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(topicId1);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_topic\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(editSubTopic.getText().toString());
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"value\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes("2");
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                }

                else
                    {
                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"topic_id\""+ lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(topicId1);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_topic_id\""+ lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(subTopicId1);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"value\""+ lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes("0");
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    }


                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"class_number\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(className1);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);




                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
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
                        Toast.makeText(KnowledgeResourseUploadingScreen.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(KnowledgeResourseUploadingScreen.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(KnowledgeResourseUploadingScreen.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURCE_SUBMIT_LIST/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                 System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Error uploading announcement");
                        loading.dismiss();
                        // notificationListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(getApplicationContext(),"Uploaded Successfully");
                        finish();
                        loading.dismiss();

                    }

                    else
                        Utils.showToast(getApplicationContext(), "Error Uploading");
                    loading.dismiss();
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error uploading! Please try after sometime.");
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
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                /*;
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                // params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);

                params.put("cls_id", Preferences.getInstance().studentClassId);*/
                params.put("uploaded_by",Preferences.getInstance().userId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                params.put("title",title.getText().toString());
                params.put("description",description.getText().toString());
                params.put("class_number",className1);
                params.put("subject_id",subjectId1);
                params.put("document_type",resourseType1);
                if(valueDecider.matches("1"))
                {
                    params.put("topic",editTopic.getText().toString());
                    params.put("sub_topic",editSubTopic.getText().toString());
                    params.put("value","1");

                }
                else
                    if(valueDecider.matches("2"))
                    {
                        params.put("topic_id",topicId1);
                        params.put("sub_topic",editSubTopic.getText().toString());
                        params.put("value","2");
                    }
                    else
                    {
                        params.put("topic_id",topicId1);
                        params.put("sub_topic_id",subTopicId1);
                        params.put("value","0");
                    }


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
            loading.dismiss();
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
