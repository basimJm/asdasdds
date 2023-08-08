package com.schoofi.activitiess;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.FilePath;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TeacherAssignmentUploadVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import polypicker.ImagePickerActivity;
import polypicker.utils.ImageInternalFetcher;

public class DiaryUploadSecondActivity extends AppCompatActivity {

    String ratingId1,typeId1,ratingBar,subjectId1,value,array;
    private EditText title,description;
    private Switch switchBar;
    private Button upload,done;
    private ImageView imageView;
    String file_type;
    int f;
    private static final int CAMERA_REQUEST = 1888;
    //String image = getStringImage(bitmap);
    private int PICK_IMAGE_REQUEST = 1;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    String image;
    private static final String TAG = FragmentsActivity.class.getSimpleName();
    private String SERVER_URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.SCHOOL_DIARY_UPLOAD_URL;

    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;

    private static final int SELECT_FILE1 = 1;
    private static final int SELECT_FILE2 = 2;
    String selectedPath1 = "NONE";
    String selectedPath2 = "NONE";
    int count;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String Month;
    String date2,date1;
    int i;
    ArrayList<String> myList1;
    private Bitmap bitmap;
    String imageLeave,imageLeave1;

    private Context mContext;

    private ViewGroup mSelectedImagesContainer;
    private ViewGroup mSelectedImagesNone;
    HashSet<Uri> mMedia = new HashSet<Uri>();
    private Button submit;
    private String selectedpath;
    String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    String date5 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    ArrayList<String> images = new ArrayList<String>();
    JSONObject jsonobject;
    String moduleType,moduleType1;
    ImageView back;
    private static final int PICK_FILE_REQUEST = 1;
    private static final int READ_REQUEST_CODE = 42;
    String selectedFilePath;


    ImageView ivAttachment;
    Button bUpload;
    TextView tvFileName;
    ProgressDialog dialog;
    private int STORAGE_PERMISSION_CODE = 23;
    String array1;

    JSONArray jsonarray;
    ArrayList<String> UploadClassNames;
    ArrayList<TeacherAssignmentUploadVO> teacherAssignmentUploadVOs;

    int count1=0;
    Button uploadDocument;
    int choice=0,choice1=0,googleDrive=0;
    Uri uri;
    int b=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Diary Upload Second Activity");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_diary_upload_second);


        value = getIntent().getStringExtra("value");
        if(value.matches("1"))
        {
            ratingId1 = getIntent().getStringExtra("ratingId");
            typeId1 = getIntent().getStringExtra("typeId");
            ratingBar = getIntent().getStringExtra("ratingBar");
            subjectId1 = getIntent().getStringExtra("subjectId");
            array1 = getIntent().getStringExtra("array");
           // Utils.showToast(getApplicationContext(),array1.toString());
            //myList1 = (ArrayList<String>) getIntent().getSerializableExtra("array");
        }

        else
        {
            subjectId1 = getIntent().getStringExtra("subjectId");
        }




        title = (EditText) findViewById(R.id.edit_title);
        description = (EditText) findViewById(R.id.edit_description);
        switchBar = (Switch) findViewById(R.id.mySwitch);
        upload = (Button) findViewById(R.id.btn_upload);
        uploadDocument = (Button) findViewById(R.id.btn_upload_document);
        done = (Button) findViewById(R.id.btn_done);
        imageView = (ImageView) findViewById(R.id.imageLeave);
        mContext = DiaryUploadSecondActivity.this;

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });






        final View getImages = findViewById(R.id.get_images);
        //View getNImages = findViewById(R.id.get_n_images);
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //if(Build.VERSION.SDK_INT >22) {

                choice = 0;
                uploadDocument.setVisibility(View.GONE);

                if (checkAndRequestPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.

                    //getImages();
                    getImages();
                    //Utils.showToast(getApplicationContext(),String.valueOf(b));
                }
                //}

				/*else
				{
					//getImages();
					showFileChooser();
				}*/

            }
        });

        uploadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                choice=1;

                upload.setVisibility(View.GONE);

                //if(Build.VERSION.SDK_INT >22) {

                if (checkAndRequestPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.

                    //getImages();
                    selectImage();
                }
                //}

				/*else
				{
					//getImages();
					showFileChooser();
				}*/

            }
        });







        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(switchBar.isChecked()){
                    b=1;
                }
                else {
                    b=0;
                }


                if(title.getText().toString().matches("") || description.getText().toString().matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please fill all fields");
                }

                else
                    if (choice == 0) {

                    postAttendance();

                } else {
                        if (googleDrive == 0) {

                            if (selectedFilePath != null) {
                                dialog = ProgressDialog.show(DiaryUploadSecondActivity.this, "", "Uploading File...", true);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //creating new thread to handle Http Operations
                                        uploadFile1(selectedFilePath);

                                    }
                                }).start();
                            } else {
                                Toast.makeText(DiaryUploadSecondActivity.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
                            }
                        } else {


                            try {

                                dialog = ProgressDialog.show(DiaryUploadSecondActivity.this, "", "Uploading File...", true);
                                final ParcelFileDescriptor mInputPFD = getContentResolver().openFileDescriptor(uri, "r");
                                final FileDescriptor fd = mInputPFD.getFileDescriptor();

                                final Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
                                final int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                                returnCursor.moveToFirst();
                                Log.d("hhhh", returnCursor.getString(nameIndex));

                                if (returnCursor.getString(nameIndex).endsWith(".jpg") || returnCursor.getString(nameIndex).endsWith(".JPG") || returnCursor.getString(nameIndex).endsWith(".jpeg") || returnCursor.getString(nameIndex).endsWith(".JPEG") || returnCursor.getString(nameIndex).endsWith(".png") || returnCursor.getString(nameIndex).endsWith(".PNG") || returnCursor.getString(nameIndex).endsWith(".gif") || returnCursor.getString(nameIndex).endsWith(".GIF")) {
                                    file_type = "image";
                                } else if (returnCursor.getString(nameIndex).endsWith(".pdf") || returnCursor.getString(nameIndex).endsWith(".PDF") || returnCursor.getString(nameIndex).endsWith(".doc") || returnCursor.getString(nameIndex).endsWith(".DOC") || returnCursor.getString(nameIndex).endsWith(".docx") || returnCursor.getString(nameIndex).endsWith(".DOCX") || returnCursor.getString(nameIndex).endsWith(".txt") || returnCursor.getString(nameIndex).endsWith(".TXT") || returnCursor.getString(nameIndex).endsWith(".pptx") || returnCursor.getString(nameIndex).endsWith(".PPTX") || returnCursor.getString(nameIndex).endsWith(".xlsx") || returnCursor.getString(nameIndex).endsWith(".XLSX")) {
                                    file_type = "file";
                                } else {
                                    file_type = "null";
                                    Utils.showToast(getApplicationContext(), "The Selected file format cannot be uploaded!!");
                                }

                                if (file_type.matches("file") || file_type.matches("image"))

                                {

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
                                            int maxBufferSize = 1 * 1024 * 1024;

                                            try {


                                                FileInputStream fileInputStream = new FileInputStream(mInputPFD.getFileDescriptor());
                                                URL url = new URL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_UPLOAD_URL_1);
                                                connection = (HttpURLConnection) url.openConnection();
                                                connection.setDoInput(true);//Allow Inputs
                                                connection.setDoOutput(true);//Allow Outputs
                                                connection.setUseCaches(false);//Don't use a cached Copy
                                                connection.setRequestMethod("POST");
                                                connection.setRequestProperty("Connection", "Keep-Alive");
                                                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                                                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                                                connection.setRequestProperty("image", String.valueOf(uri));
                                                Log.d("ll", String.valueOf(uri));
                                                //connection.setRequestProperty("title","llll");

                                                //creating new dataoutputstream
                                                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"teacher_id\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(Preferences.getInstance().teachId);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"ins_id\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(Preferences.getInstance().institutionId);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file_type\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(file_type);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"reply\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(Preferences.getInstance().institutionId);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sch_id\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(Preferences.getInstance().schoolId);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"class_id\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(Preferences.getInstance().studentClassId);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"section_id\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(Preferences.getInstance().studentSectionId);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);


                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"count\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes("1");
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"title\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(title.getText().toString());
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);


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

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sender_id\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(Preferences.getInstance().userId);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(description.getText().toString());
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);


                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"subject_id\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(subjectId1);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                if (value.matches("1")) {

                                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"diary_module_id\"" + lineEnd);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(typeId1);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rating_parameter_id\"" + lineEnd);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(ratingId1);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rating\"" + lineEnd);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(ratingBar);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\"" + lineEnd);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(array1.toString());
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                                                } else {

                                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\"" + lineEnd);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(Preferences.getInstance().studentId);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"temp\"" + lineEnd);
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes("1");
                                                    dataOutputStream.writeBytes(lineEnd);
                                                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                                                }

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"emoticon\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes("");
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                                                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"shareable\"" + lineEnd);
                                                dataOutputStream.writeBytes(lineEnd);
                                                dataOutputStream.writeBytes(String.valueOf(b));
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

                                                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
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
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
                                                            {
                                                                Intent intent = new Intent(DiaryUploadSecondActivity.this, SchoolDiaryMainScreen.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                            else {
                                                                Intent intent = new Intent(DiaryUploadSecondActivity.this, TeacherSubjectStudentList.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                intent.putExtra("value", "2");
                                                                startActivity(intent);
                                                                finish();
                                                            }
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
                                                        Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
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
                                        }
                                    }).start();
                                }

                                else
                                {
                                    dialog.dismiss();
                                }


                                } catch(FileNotFoundException e){
                                    e.printStackTrace();
                                }

                                //addFormField(dataOutputStream,"abc","a");

                        }
                        }






            }
        });





        }

    public String getStringImage(FileInputStream fis){
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
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




    private void getImages() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    private void getNImages() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);

        // limit image pick count to only 5 images.
        intent.putExtra(ImagePickerActivity.EXTRA_SELECTION_LIMIT, 5);
        startActivityForResult(intent, INTENT_REQUEST_GET_N_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        switch (requestCode)
        {
            case 1: if (resuleCode == Activity.RESULT_OK) {
                if (requestCode == PICK_FILE_REQUEST) {
                    if (intent == null) {
                        //no data present
                        return;
                    }


                    Uri selectedFileUri = intent.getData();
                    selectedFilePath = FilePath.getPath(this, selectedFileUri);
                    Log.i(TAG, "Selected File Path:" + selectedFilePath);

                    count=1;

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



            case 13:


                if (resuleCode == Activity.RESULT_OK) {
                    if (requestCode == INTENT_REQUEST_GET_IMAGES || requestCode == INTENT_REQUEST_GET_N_IMAGES && resuleCode == RESULT_OK && intent != null && intent.getData() != null) {

                        //Preferences.getInstance().loadPreference(getApplicationContext());

                        Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);






                        if(parcelableUris ==null) {
                            return;
                        }

                        // Java doesn't allow array casting, this is a little hack


                        Uri[] uris = new Uri[parcelableUris.length];
                        //Uri uris1 = intent.getData();
                        System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                        //Toast.makeText(getApplicationContext(), uris[0].getPath(), Toast.LENGTH_SHORT).show();

                /*for(int i=0;i<=uris.length;i++)
                {

                }
                selectedpath = uris[0].getPath();




                File file = new File(selectedpath);

                FileBody bin1 = new FileBody(file);
                Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();


                MultipartEntity reqEntity = new MultipartEntity();*/






                        if(uris != null) {
                            for (Uri uri : uris) {
                                Log.i(TAG, " uri: " + uri);
                                mMedia.add(uri);
                            }


                /* for(int i=0;i<mMedia.size();i++)
                 {



                 }*/
                            // Toast.makeText(getApplicationContext(), filePath.toString(), Toast.LENGTH_LONG).show();
                            // Toast.makeText(getApplicationContext(), selectedPaths.toString(), Toast.LENGTH_LONG).show();


                            showMedia();


                            for (int i = 1; i <= mMedia.size(); i++) {
                                count = i;

                            }

                            //Utils.showToast(getApplicationContext(), ""+count);

                            if (count == 1) {

                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);
                            } else if (count == 2) {
                                //urt.add(uris[0].getPath().toString());
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt1);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);
                            } else if (count == 2) {
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);
                            } else if (count == 3) {
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);


                                String urt2 = uris[2].getPath().toString();
                                Log.d("tag", "kio" + urt2);
                                File imgFile2 = new File(urt2);
                                FileInputStream fis2 = null;
                                try {
                                    fis2 = new FileInputStream(imgFile2);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave2;
                                imageLeave2 = getStringImage(fis2);
                                images.add(imageLeave2);
                            } else if (count == 4) {
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);

                                String urt2 = uris[2].getPath().toString();
                                Log.d("tag", "kio" + urt2);
                                File imgFile2 = new File(urt2);
                                FileInputStream fis2 = null;
                                try {
                                    fis2 = new FileInputStream(imgFile2);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave2;
                                imageLeave2 = getStringImage(fis2);
                                images.add(imageLeave2);

                                String urt3 = uris[3].getPath().toString();
                                Log.d("tag", "kio" + urt3);
                                File imgFile3 = new File(urt3);
                                FileInputStream fis3 = null;
                                try {
                                    fis3 = new FileInputStream(imgFile3);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave3;
                                imageLeave3 = getStringImage(fis3);
                                images.add(imageLeave3);
                            } else if (count == 5) {
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);

                                String urt2 = uris[2].getPath().toString();
                                Log.d("tag", "kio" + urt2);
                                File imgFile2 = new File(urt2);
                                FileInputStream fis2 = null;
                                try {
                                    fis2 = new FileInputStream(imgFile2);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave2;
                                imageLeave2 = getStringImage(fis2);
                                images.add(imageLeave2);

                                String urt3 = uris[3].getPath().toString();
                                Log.d("tag", "kio" + urt3);
                                File imgFile3 = new File(urt3);
                                FileInputStream fis3 = null;
                                try {
                                    fis3 = new FileInputStream(imgFile3);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave3;
                                imageLeave3 = getStringImage(fis3);
                                images.add(imageLeave3);

                                String urt4 = uris[4].getPath().toString();
                                Log.d("tag", "kio" + urt4);
                                File imgFile4 = new File(urt4);
                                FileInputStream fis4 = null;
                                try {
                                    fis4 = new FileInputStream(imgFile4);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave4;
                                imageLeave4 = getStringImage(fis4);
                                images.add(imageLeave4);
                            } else if (count == 6) {
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);

                                String urt2 = uris[2].getPath().toString();
                                Log.d("tag", "kio" + urt2);
                                File imgFile2 = new File(urt2);
                                FileInputStream fis2 = null;
                                try {
                                    fis2 = new FileInputStream(imgFile2);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave2;
                                imageLeave2 = getStringImage(fis2);
                                images.add(imageLeave2);

                                String urt3 = uris[3].getPath().toString();
                                Log.d("tag", "kio" + urt3);
                                File imgFile3 = new File(urt3);
                                FileInputStream fis3 = null;
                                try {
                                    fis3 = new FileInputStream(imgFile3);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave3;
                                imageLeave3 = getStringImage(fis3);
                                images.add(imageLeave3);

                                String urt4 = uris[4].getPath().toString();
                                Log.d("tag", "kio" + urt4);
                                File imgFile4 = new File(urt4);
                                FileInputStream fis4 = null;
                                try {
                                    fis4 = new FileInputStream(imgFile4);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave4;
                                imageLeave4 = getStringImage(fis4);
                                images.add(imageLeave4);

                                String urt5 = uris[5].getPath().toString();
                                Log.d("tag", "kio" + urt5);
                                File imgFile5 = new File(urt5);
                                FileInputStream fis5 = null;
                                try {
                                    fis5 = new FileInputStream(imgFile5);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave5;
                                imageLeave5 = getStringImage(fis5);
                                images.add(imageLeave5);
                            } else if (count == 7) {
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);

                                String urt2 = uris[2].getPath().toString();
                                Log.d("tag", "kio" + urt2);
                                File imgFile2 = new File(urt2);
                                FileInputStream fis2 = null;
                                try {
                                    fis2 = new FileInputStream(imgFile2);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave2;
                                imageLeave2 = getStringImage(fis2);
                                images.add(imageLeave2);

                                String urt3 = uris[3].getPath().toString();
                                Log.d("tag", "kio" + urt3);
                                File imgFile3 = new File(urt3);
                                FileInputStream fis3 = null;
                                try {
                                    fis3 = new FileInputStream(imgFile3);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave3;
                                imageLeave3 = getStringImage(fis3);
                                images.add(imageLeave3);

                                String urt4 = uris[4].getPath().toString();
                                Log.d("tag", "kio" + urt4);
                                File imgFile4 = new File(urt4);
                                FileInputStream fis4 = null;
                                try {
                                    fis4 = new FileInputStream(imgFile4);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave4;
                                imageLeave4 = getStringImage(fis4);
                                images.add(imageLeave4);

                                String urt5 = uris[5].getPath().toString();
                                Log.d("tag", "kio" + urt5);
                                File imgFile5 = new File(urt5);
                                FileInputStream fis5 = null;
                                try {
                                    fis5 = new FileInputStream(imgFile5);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave5;
                                imageLeave5 = getStringImage(fis5);
                                images.add(imageLeave5);

                                String urt6 = uris[6].getPath().toString();
                                Log.d("tag", "kio" + urt6);
                                File imgFile6 = new File(urt6);
                                FileInputStream fis6 = null;
                                try {
                                    fis6 = new FileInputStream(imgFile6);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave6;
                                imageLeave6 = getStringImage(fis6);
                                images.add(imageLeave6);
                            } else if (count == 8) {
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);

                                String urt2 = uris[2].getPath().toString();
                                Log.d("tag", "kio" + urt2);
                                File imgFile2 = new File(urt2);
                                FileInputStream fis2 = null;
                                try {
                                    fis2 = new FileInputStream(imgFile2);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave2;
                                imageLeave2 = getStringImage(fis2);
                                images.add(imageLeave2);

                                String urt3 = uris[3].getPath().toString();
                                Log.d("tag", "kio" + urt3);
                                File imgFile3 = new File(urt3);
                                FileInputStream fis3 = null;
                                try {
                                    fis3 = new FileInputStream(imgFile3);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave3;
                                imageLeave3 = getStringImage(fis3);
                                images.add(imageLeave3);

                                String urt4 = uris[4].getPath().toString();
                                Log.d("tag", "kio" + urt4);
                                File imgFile4 = new File(urt4);
                                FileInputStream fis4 = null;
                                try {
                                    fis4 = new FileInputStream(imgFile4);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave4;
                                imageLeave4 = getStringImage(fis4);
                                images.add(imageLeave4);

                                String urt5 = uris[5].getPath().toString();
                                Log.d("tag", "kio" + urt5);
                                File imgFile5 = new File(urt5);
                                FileInputStream fis5 = null;
                                try {
                                    fis5 = new FileInputStream(imgFile5);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave5;
                                imageLeave5 = getStringImage(fis5);
                                images.add(imageLeave5);

                                String urt6 = uris[6].getPath().toString();
                                Log.d("tag", "kio" + urt6);
                                File imgFile6 = new File(urt6);
                                FileInputStream fis6 = null;
                                try {
                                    fis6 = new FileInputStream(imgFile6);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave6;
                                imageLeave6 = getStringImage(fis6);
                                images.add(imageLeave6);

                                String urt7 = uris[7].getPath().toString();
                                Log.d("tag", "kio" + urt7);
                                File imgFile7 = new File(urt7);
                                FileInputStream fis7 = null;
                                try {
                                    fis7 = new FileInputStream(imgFile7);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave7;
                                imageLeave7 = getStringImage(fis7);
                                images.add(imageLeave7);
                            } else if (count == 9) {
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);

                                String urt2 = uris[2].getPath().toString();
                                Log.d("tag", "kio" + urt2);
                                File imgFile2 = new File(urt2);
                                FileInputStream fis2 = null;
                                try {
                                    fis2 = new FileInputStream(imgFile2);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave2;
                                imageLeave2 = getStringImage(fis2);
                                images.add(imageLeave2);

                                String urt3 = uris[3].getPath().toString();
                                Log.d("tag", "kio" + urt3);
                                File imgFile3 = new File(urt3);
                                FileInputStream fis3 = null;
                                try {
                                    fis3 = new FileInputStream(imgFile3);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave3;
                                imageLeave3 = getStringImage(fis3);
                                images.add(imageLeave3);

                                String urt4 = uris[4].getPath().toString();
                                Log.d("tag", "kio" + urt4);
                                File imgFile4 = new File(urt4);
                                FileInputStream fis4 = null;
                                try {
                                    fis4 = new FileInputStream(imgFile4);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave4;
                                imageLeave4 = getStringImage(fis4);
                                images.add(imageLeave4);

                                String urt5 = uris[5].getPath().toString();
                                Log.d("tag", "kio" + urt5);
                                File imgFile5 = new File(urt5);
                                FileInputStream fis5 = null;
                                try {
                                    fis5 = new FileInputStream(imgFile5);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave5;
                                imageLeave5 = getStringImage(fis5);
                                images.add(imageLeave5);

                                String urt6 = uris[6].getPath().toString();
                                Log.d("tag", "kio" + urt6);
                                File imgFile6 = new File(urt6);
                                FileInputStream fis6 = null;
                                try {
                                    fis6 = new FileInputStream(imgFile6);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave6;
                                imageLeave6 = getStringImage(fis6);
                                images.add(imageLeave6);

                                String urt7 = uris[7].getPath().toString();
                                Log.d("tag", "kio" + urt7);
                                File imgFile7 = new File(urt7);
                                FileInputStream fis7 = null;
                                try {
                                    fis7 = new FileInputStream(imgFile7);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave7;
                                imageLeave7 = getStringImage(fis7);
                                images.add(imageLeave7);

                                String urt8 = uris[8].getPath().toString();
                                Log.d("tag", "kio" + urt8);
                                File imgFile8 = new File(urt8);
                                FileInputStream fis8 = null;
                                try {
                                    fis8 = new FileInputStream(imgFile8);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave8;
                                imageLeave8 = getStringImage(fis8);
                                images.add(imageLeave8);
                            } else if (count == 10) {
                                String urt = uris[0].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile = new File(urt);
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imgFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                imageLeave = getStringImage(fis);
                                images.add(imageLeave);

                                String urt1 = uris[1].getPath().toString();
                                Log.d("tag", "kio" + urt);
                                File imgFile1 = new File(urt1);
                                FileInputStream fis1 = null;
                                try {
                                    fis1 = new FileInputStream(imgFile1);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave1;
                                imageLeave1 = getStringImage(fis1);
                                images.add(imageLeave1);

                                String urt2 = uris[2].getPath().toString();
                                Log.d("tag", "kio" + urt2);
                                File imgFile2 = new File(urt2);
                                FileInputStream fis2 = null;
                                try {
                                    fis2 = new FileInputStream(imgFile2);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave2;
                                imageLeave2 = getStringImage(fis2);
                                images.add(imageLeave2);

                                String urt3 = uris[3].getPath().toString();
                                Log.d("tag", "kio" + urt3);
                                File imgFile3 = new File(urt3);
                                FileInputStream fis3 = null;
                                try {
                                    fis3 = new FileInputStream(imgFile3);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave3;
                                imageLeave3 = getStringImage(fis3);
                                images.add(imageLeave3);

                                String urt4 = uris[4].getPath().toString();
                                Log.d("tag", "kio" + urt4);
                                File imgFile4 = new File(urt4);
                                FileInputStream fis4 = null;
                                try {
                                    fis4 = new FileInputStream(imgFile4);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave4;
                                imageLeave4 = getStringImage(fis4);
                                images.add(imageLeave4);

                                String urt5 = uris[5].getPath().toString();
                                Log.d("tag", "kio" + urt5);
                                File imgFile5 = new File(urt5);
                                FileInputStream fis5 = null;
                                try {
                                    fis5 = new FileInputStream(imgFile5);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave5;
                                imageLeave5 = getStringImage(fis5);
                                images.add(imageLeave5);

                                String urt6 = uris[6].getPath().toString();
                                Log.d("tag", "kio" + urt6);
                                File imgFile6 = new File(urt6);
                                FileInputStream fis6 = null;
                                try {
                                    fis6 = new FileInputStream(imgFile6);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave6;
                                imageLeave6 = getStringImage(fis6);
                                images.add(imageLeave6);

                                String urt7 = uris[7].getPath().toString();
                                Log.d("tag", "kio" + urt7);
                                File imgFile7 = new File(urt7);
                                FileInputStream fis7 = null;
                                try {
                                    fis7 = new FileInputStream(imgFile7);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave7;
                                imageLeave7 = getStringImage(fis7);
                                images.add(imageLeave7);

                                String urt8 = uris[8].getPath().toString();
                                Log.d("tag", "kio" + urt8);
                                File imgFile8 = new File(urt8);
                                FileInputStream fis8 = null;
                                try {
                                    fis8 = new FileInputStream(imgFile8);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave8;
                                imageLeave8 = getStringImage(fis8);
                                images.add(imageLeave8);

                                String urt9 = uris[9].getPath().toString();
                                Log.d("tag", "kio" + urt9);
                                File imgFile9 = new File(urt9);
                                FileInputStream fis9 = null;
                                try {
                                    fis9 = new FileInputStream(imgFile9);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                                String imageLeave9;
                                imageLeave9 = getStringImage(fis9);
                                images.add(imageLeave9);
                            } else if (count > 10) {
                                Utils.showToast(getApplicationContext(), "Maximum images can be uploded is 10");
                            }
                		/*String urt = uris[0].getPath().toString();
                		Log.d("tag", "kio"+urt);
                		File imgFile = new  File(urt);
                		FileInputStream fis = null;
                		try {
                		    fis = new FileInputStream(imgFile);
                		    } catch (FileNotFoundException e) {
                		    e.printStackTrace();
                		}
                		//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                		//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                		imageLeave = getStringImage(fis);
                		//images.add(imageLeave);

                		String urt1 = uris[1].getPath().toString();
                		Log.d("tag", "kio1"+urt1);
                		File imgFile1 = new  File(urt1);
                		FileInputStream fis1 = null;
                		try {
                		    fis1= new FileInputStream(imgFile1);
                		    } catch (FileNotFoundException e) {
                		    e.printStackTrace();
                		}
                		//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                		//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                		imageLeave1 = getStringImage(fis1);
                		Log.d("harsh", "ll"+imageLeave1);

                		//images.add(imageLeave);

                	}

                	//images.add("jkkkjkjj");
                	//images.add(imageLeave);
                	//images.add(imageLeave);
               // images.add(imageLeave);
                //images.add(imageLeave);
                images.add(imageLeave);
                images.add(imageLeave1);
                //images.add("jjkgfg");

                	count = i;*/


                            //Log.d("array", "kk"+images.toString());
                            //Toast.makeText(getApplicationContext(), images.toString(), Toast.LENGTH_LONG).show();

                	/*try {*/
                            //Getting the Bitmap from Gallery
                		/*String urt = uris[0].getPath().toString();
                		Log.d("tag", ""+urt);
                		File imgFile = new  File(urt);
                		FileInputStream fis = null;
                		try {
                		    fis = new FileInputStream(imgFile);
                		    } catch (FileNotFoundException e) {
                		    e.printStackTrace();
                		}
                		//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                		//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                		imageLeave = getStringImage(fis);*/

                            //bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                            //Toast.makeText(getApplicationContext(), imageLeave.toString(), Toast.LENGTH_LONG).show();
                            Log.d("harsh", "kkkco" + images.toString());
        			/*} catch (IOException e) {
        				e.printStackTrace();
        			}*/

                        }
                    }

                }
                break;

        }





    }

    private void showMedia() {
        // Remove all views before
        // adding the new ones.
        mSelectedImagesContainer.removeAllViews();

        Iterator<Uri> iterator = mMedia.iterator();
        ImageInternalFetcher imageFetcher = new ImageInternalFetcher(this, 500);
        while(iterator.hasNext()) {
            Uri uri = iterator.next();
            //Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();

            // showImage(uri);
            Log.i(TAG, " uri: " + uri);
            if(mMedia.size() >= 1) {
                mSelectedImagesContainer.setVisibility(View.VISIBLE);
            }

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);

            // View removeBtn = imageHolder.findViewById(R.id.remove_media);
            // initRemoveBtn(removeBtn, imageHolder, uri);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            if(!uri.toString().contains("content://")) {
                // probably a relative uri
                uri = Uri.fromFile(new File(uri.toString()));

            }

            imageFetcher.loadImage(uri, thumbnail);

            mSelectedImagesContainer.addView(imageHolder);

            // set the dimension to correctly
            // show the image thumbnail.
            int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
            int htpx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
            //Toolbar toolBar;
        }
    }




    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }





    //gggg= http://www.simplifiedcoding.net/android-volley-tutorial-to-upload-image-to-server/;

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void selectImage() {
        final CharSequence[] items = { "Phone Storage", "Cloud Drives", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryUploadSecondActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Phone Storage")) {
                    showFileChooser();
                } else if (items[item].equals("Cloud Drives")) {
                    if(Build.VERSION.SDK_INT>=19) {
                        showFileChooser1();
                    }

                    else
                    {
                        Utils.showToast(getApplicationContext(),"Your device is not compatible for this feature");
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                //Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
                //getImages();
				/*if(count1 ==0) {
					requestStoragePermission1();
				}

				else
				{
					getImages();
				}*/
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    //Requesting permission
    private void requestStoragePermission1(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA},STORAGE_PERMISSION_CODE);
        count1=1;
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

    /*public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        Utils.showToast(getApplicationContext(),selectedFilePath);
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
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
                //connection.setRequestProperty("title","llll");

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                //addFormField(dataOutputStream,"abc","a");

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"teacher_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().teachId);
                dataOutputStream.writeBytes(lineEnd);
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

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"class_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().studentClassId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"section_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().studentSectionId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().studentId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"count\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("1");
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

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sender_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().userId);
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

                if(value.matches("1")) {

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"diary_module_id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(typeId1);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rating_parameter_id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(ratingId1);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rating\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(ratingBar);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(array1.toString());
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                }

                else
                {

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(Preferences.getInstance().studentId);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"temp\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes("1");
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                }

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"emoticon\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("");
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"shareable\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(String.valueOf(b));
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file_type\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("image");
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);







                //dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
				*//*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(teacherAssignmentUpload1);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);*//*



                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
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

                *//*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"abc\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("a");//your parameter value
                dataOutputStream.writeBytes(lineEnd); //to add multiple parameters write Content-Disposition: form-data; name=\"your parameter name\"" + crlf again and keep repeating till here :)
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens);*//*



                *//*dataOutputStream.writeBytes(String.valueOf((twoHyphens+boundary+lineEnd).getBytes()));
                dataOutputStream.write( "Content-Type: text/plain".getBytes());
                dataOutputStream.write( ("Content-Disposition: form-data; name="+ "abc"+lineEnd).getBytes());;
                dataOutputStream.write( (lineEnd + "a" + lineEnd).getBytes());*//*


                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
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

    }*/


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
                connection.setRequestProperty("image",selectedFilePath);
                //connection.setRequestProperty("title","llll");

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());


                //addFormField(dataOutputStream,"abc","a");

                //writing bytes to data outputstream

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"teacher_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().teachId);
                dataOutputStream.writeBytes(lineEnd);
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

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"reply\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().institutionId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"class_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().studentClassId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"section_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().studentSectionId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().studentId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"count\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("1");
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

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sender_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().userId);
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

                if(value.matches("1")) {

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"diary_module_id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(typeId1);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rating_parameter_id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(ratingId1);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rating\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(ratingBar);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(array1.toString());
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                }

                else
                {

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(Preferences.getInstance().studentId);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"temp\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes("1");
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                }

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"emoticon\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("A");
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"shareable\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(String.valueOf(b));
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file_type\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("image");
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);





                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
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
                            if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
                            {
                                Intent intent = new Intent(DiaryUploadSecondActivity.this, SchoolDiaryMainScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);
                                finish();
                            }

                            else {
                                Intent intent = new Intent(DiaryUploadSecondActivity.this, TeacherSubjectStudentList.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("value", "2");
                                startActivity(intent);
                                finish();
                            }
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

    protected void postAttendance()
    {
        //setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);

        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.SCHOOL_DIARY_UPLOAD_URL;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        //Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "error6", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Toast.makeText(getApplicationContext(), "uploaded", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
                        {
                            Intent intent = new Intent(DiaryUploadSecondActivity.this, SchoolDiaryMainScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                            finish();
                        }

                        else {
                            Intent intent = new Intent(DiaryUploadSecondActivity.this, TeacherSubjectStudentList.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("value", "2");
                            startActivity(intent);
                            finish();
                        }
                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
                    Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_LONG).show();
                    loading.dismiss();
                }
                //setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
                loading.dismiss();
                //setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                //.getInstance().loadPreference(TeacherStudentAttendanceDetails.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
                //params.put("Students", jsonObject1.toString());

                if(count==0)
                {
                    Log.d("kkk","kkk");
                }

                else {
                    params.put("file", images.toString());
                    Log.d("jj",images.toString());
                }


                params.put("sender_id",Preferences.getInstance().userId);

                params.put("teacher_id" , Preferences.getInstance().teachId);
                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("class_id", Preferences.getInstance().studentClassId);
                params.put("section_id", Preferences.getInstance().studentSectionId);
                params.put("description",description.getText().toString());

                params.put("emoticon","");
                if(subjectId1.matches("") || subjectId1.matches("null"))
                {

                }
                else {
                    params.put("subject_id", subjectId1);
                }
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                params.put("file_type","image");
                if(value.matches("1")) {
                    params.put("rating_parameter_id", ratingId1);
                    params.put("diary_module_id", typeId1);
                    params.put("rating", ratingBar);
                    params.put("student_id",array1.toString());
                }
                else
                {
                    params.put("temp","1");
                    params.put("student_id",Preferences.getInstance().studentId);

                }
                params.put("count",String.valueOf(count));
                params.put("value","1");

                params.put("title", title.getText().toString());
                params.put("shareable",String.valueOf(b));




                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }

    public int uploadFile1(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if(selectedFilePath.endsWith(".png") || selectedFilePath.endsWith(".PNG") || selectedFilePath.endsWith(".JPEG") || selectedFilePath.endsWith(".jpeg") || selectedFilePath.endsWith(".JPG") || selectedFilePath.endsWith(".jpg") || selectedFilePath.endsWith(".GIF") || selectedFilePath.endsWith(".gif"))
        {
            file_type="image";
        }
        else if(selectedFilePath.endsWith(".pdf") || selectedFilePath.endsWith(".PDF") ||selectedFilePath.endsWith(".doc") || selectedFilePath.endsWith(".DOC") || selectedFilePath.endsWith(".docx") || selectedFilePath.endsWith(".DOCX") || selectedFilePath.endsWith(".txt") || selectedFilePath.endsWith(".TXT") || selectedFilePath.endsWith(".xlsx") || selectedFilePath.endsWith(".XLSX") || selectedFilePath.endsWith(".pptx") || selectedFilePath.endsWith(".PPTX"))
        {
            file_type="file";
        }

        else
        {
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                    Log.d("kkkk","kkk");
                    Utils.showToast(getApplicationContext(),"File Type Cannot be Uploaded!!");
                }
            });
            return 0;
        }

        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                    Log.d("kkkk","kkk");
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.SCHOOL_DIARY_UPLOAD_URL_1);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("image",selectedFilePath);
                //connection.setRequestProperty("title","llll");

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                //addFormField(dataOutputStream,"abc","a");

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                if(Preferences.getInstance().userRoleId.matches("5") ||Preferences.getInstance().userRoleId.matches("6")) {


                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(Preferences.getInstance().studentId);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"temp\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes("1");
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);


                }

                else
                {
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"student_id\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(array1.toString());
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"diary_module_id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(typeId1);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rating\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(ratingBar);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rating_parameter_id\""+ lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(ratingId1);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                }

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sender_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().userId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);





                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"class_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().studentClassId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"section_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().studentSectionId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(description.getText().toString());
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"emoticon\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("");
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"shareable\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(String.valueOf(b));
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"reply\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("");
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(title.getText().toString());
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"subject_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(subjectId1);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"teacher_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().teachId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);




                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file_type\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(file_type);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"count\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("1");
                dataOutputStream.writeBytes(lineEnd);
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

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"role_id\""+ lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(Preferences.getInstance().userRoleId);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
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

                Log.d("lll",String.valueOf(bytesRead));

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
                            if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
                            {
                                Intent intent = new Intent(DiaryUploadSecondActivity.this, SchoolDiaryMainScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);
                                finish();
                            }

                            else {
                                Intent intent = new Intent(DiaryUploadSecondActivity.this, TeacherSubjectStudentList.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("value", "2");
                                startActivity(intent);
                                finish();
                            }
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
}
