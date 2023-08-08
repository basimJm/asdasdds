package com.schoofi.activitiess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import polypicker.ImagePickerActivity;
import polypicker.utils.ImageInternalFetcher;

public class HealthAndAuditUserTaskSubmit extends FragmentActivity {

    private int position;
    private String taskArea,taskId,uniqueId;
    public static final String TAG = FragmentsActivity.class.getSimpleName();
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;

    int count;
    int i;

    private Bitmap bitmap;
    String imageLeave,imageLeave1;
    private int STORAGE_PERMISSION_CODE = 23;


    ImageView back;

    private ViewGroup mSelectedImagesContainer;
    private ViewGroup mSelectedImagesNone;
    HashSet<Uri> mMedia = new HashSet<Uri>();
    private Button submit,upload;
    private String selectedpath;
    private EditText description;
    ArrayList<String> images = new ArrayList<String>();
    JSONObject jsonobject;
    String moduleType,moduleType1;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Health And Audit User TaskSubmit");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_health_and_audit_user_task_submit);
        mContext = HealthAndAuditUserTaskSubmit.this;
        View getImages = findViewById(R.id.get_images);
        //View getNImages = findViewById(R.id.get_n_images);
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        description = (EditText) findViewById(R.id.edit_description);
        upload = (Button) findViewById(R.id.btn_submit_assignment);
        //Utils.showToast(getApplicationContext(), "hhh"+assignId);

        back = (ImageView) findViewById(R.id.img_back);

        taskId = getIntent().getStringExtra("task_id");
        uniqueId = getIntent().getStringExtra("unique_id");
        taskArea = getIntent().getStringExtra("task_area");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                postAttendance();

            }
        });


        getImages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //if(Build.VERSION.SDK_INT >22) {

                    if (checkAndRequestPermissions()) {
                        // carry on the normal flow, as the case of  permissions  granted.

                        getImages();
                    }
               // }

                /*else
                {
                    getImages();
                }*/
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


                    for(int i =1;i<=mMedia.size();i++)
                    {
                        count = i;

                    }

                    //Utils.showToast(getApplicationContext(), ""+count);

                    if(count == 1)
                    {

                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);
                    }

                    else
                    if(count == 2)
                    {
                        //urt.add(uris[0].getPath().toString());
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt1);
                        File imgFile1 = new  File(urt1);
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
                    }

                    else
                    if(count == 2)
                    {
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt);
                        File imgFile1 = new  File(urt1);
                        FileInputStream fis1= null;
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
                    }

                    else
                    if(count == 3)
                    {
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt);
                        File imgFile1 = new  File(urt1);
                        FileInputStream fis1= null;
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
                        Log.d("tag", "kio"+urt2);
                        File imgFile2 = new  File(urt2);
                        FileInputStream fis2= null;
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
                    }

                    else
                    if(count == 4)
                    {
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt);
                        File imgFile1 = new  File(urt1);
                        FileInputStream fis1= null;
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
                        Log.d("tag", "kio"+urt2);
                        File imgFile2 = new  File(urt2);
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
                        Log.d("tag", "kio"+urt3);
                        File imgFile3 = new  File(urt3);
                        FileInputStream fis3= null;
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
                    }

                    else
                    if(count == 5)
                    {
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt);
                        File imgFile1 = new  File(urt1);
                        FileInputStream fis1= null;
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
                        Log.d("tag", "kio"+urt2);
                        File imgFile2 = new  File(urt2);
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
                        Log.d("tag", "kio"+urt3);
                        File imgFile3 = new  File(urt3);
                        FileInputStream fis3= null;
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
                        Log.d("tag", "kio"+urt4);
                        File imgFile4 = new  File(urt4);
                        FileInputStream fis4= null;
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
                    }

                    else
                    if(count == 6)
                    {
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt);
                        File imgFile1 = new  File(urt1);
                        FileInputStream fis1= null;
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
                        Log.d("tag", "kio"+urt2);
                        File imgFile2 = new  File(urt2);
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
                        Log.d("tag", "kio"+urt3);
                        File imgFile3 = new  File(urt3);
                        FileInputStream fis3= null;
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
                        Log.d("tag", "kio"+urt4);
                        File imgFile4 = new  File(urt4);
                        FileInputStream fis4= null;
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
                        Log.d("tag", "kio"+urt5);
                        File imgFile5 = new  File(urt5);
                        FileInputStream fis5= null;
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
                    }

                    else
                    if(count == 7)
                    {
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt);
                        File imgFile1 = new  File(urt1);
                        FileInputStream fis1= null;
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
                        Log.d("tag", "kio"+urt2);
                        File imgFile2 = new  File(urt2);
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
                        Log.d("tag", "kio"+urt3);
                        File imgFile3 = new  File(urt3);
                        FileInputStream fis3= null;
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
                        Log.d("tag", "kio"+urt4);
                        File imgFile4 = new  File(urt4);
                        FileInputStream fis4= null;
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
                        Log.d("tag", "kio"+urt5);
                        File imgFile5 = new  File(urt5);
                        FileInputStream fis5= null;
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
                        Log.d("tag", "kio"+urt6);
                        File imgFile6 = new  File(urt6);
                        FileInputStream fis6= null;
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
                    }

                    else
                    if(count == 8)
                    {
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt);
                        File imgFile1 = new  File(urt1);
                        FileInputStream fis1= null;
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
                        Log.d("tag", "kio"+urt2);
                        File imgFile2 = new  File(urt2);
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
                        Log.d("tag", "kio"+urt3);
                        File imgFile3 = new  File(urt3);
                        FileInputStream fis3= null;
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
                        Log.d("tag", "kio"+urt4);
                        File imgFile4 = new  File(urt4);
                        FileInputStream fis4= null;
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
                        Log.d("tag", "kio"+urt5);
                        File imgFile5 = new  File(urt5);
                        FileInputStream fis5= null;
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
                        Log.d("tag", "kio"+urt6);
                        File imgFile6 = new  File(urt6);
                        FileInputStream fis6= null;
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
                        Log.d("tag", "kio"+urt7);
                        File imgFile7 = new  File(urt7);
                        FileInputStream fis7= null;
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
                    }

                    else
                    if(count == 9)
                    {
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt);
                        File imgFile1 = new  File(urt1);
                        FileInputStream fis1= null;
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
                        Log.d("tag", "kio"+urt2);
                        File imgFile2 = new  File(urt2);
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
                        Log.d("tag", "kio"+urt3);
                        File imgFile3 = new  File(urt3);
                        FileInputStream fis3= null;
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
                        Log.d("tag", "kio"+urt4);
                        File imgFile4 = new  File(urt4);
                        FileInputStream fis4= null;
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
                        Log.d("tag", "kio"+urt5);
                        File imgFile5 = new  File(urt5);
                        FileInputStream fis5= null;
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
                        Log.d("tag", "kio"+urt6);
                        File imgFile6 = new  File(urt6);
                        FileInputStream fis6= null;
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
                        Log.d("tag", "kio"+urt7);
                        File imgFile7 = new  File(urt7);
                        FileInputStream fis7= null;
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
                        Log.d("tag", "kio"+urt8);
                        File imgFile8 = new  File(urt8);
                        FileInputStream fis8= null;
                        try {
                            fis8 = new FileInputStream(imgFile8);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                        String imageLeave8;
                        imageLeave8= getStringImage(fis8);
                        images.add(imageLeave8);
                    }

                    else
                    if(count == 10)
                    {
                        String urt = uris[0].getPath().toString();
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
                        images.add(imageLeave);

                        String urt1 = uris[1].getPath().toString();
                        Log.d("tag", "kio"+urt);
                        File imgFile1 = new  File(urt1);
                        FileInputStream fis1= null;
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
                        Log.d("tag", "kio"+urt2);
                        File imgFile2 = new  File(urt2);
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
                        Log.d("tag", "kio"+urt3);
                        File imgFile3 = new  File(urt3);
                        FileInputStream fis3= null;
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
                        Log.d("tag", "kio"+urt4);
                        File imgFile4 = new  File(urt4);
                        FileInputStream fis4= null;
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
                        Log.d("tag", "kio"+urt5);
                        File imgFile5 = new  File(urt5);
                        FileInputStream fis5= null;
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
                        Log.d("tag", "kio"+urt6);
                        File imgFile6 = new  File(urt6);
                        FileInputStream fis6= null;
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
                        Log.d("tag", "kio"+urt7);
                        File imgFile7 = new  File(urt7);
                        FileInputStream fis7= null;
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
                        Log.d("tag", "kio"+urt8);
                        File imgFile8 = new  File(urt8);
                        FileInputStream fis8= null;
                        try {
                            fis8 = new FileInputStream(imgFile8);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                        String imageLeave8;
                        imageLeave8= getStringImage(fis8);
                        images.add(imageLeave8);

                        String urt9 = uris[9].getPath().toString();
                        Log.d("tag", "kio"+urt9);
                        File imgFile9 = new  File(urt9);
                        FileInputStream fis9= null;
                        try {
                            fis9 = new FileInputStream(imgFile9);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                        String imageLeave9;
                        imageLeave9= getStringImage(fis9);
                        images.add(imageLeave9);
                    }

                    else
                    if(count>10)
                    {
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
                    Log.d("harsh", "kkkco"+images.toString());
					/*} catch (IOException e) {
		        				e.printStackTrace();
		        			}*/


                }
            }
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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(
                R.menu.student_submit_assignment_and_homework, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void postAttendance()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        final ProgressDialog loading;
        loading = ProgressDialog.show(HealthAndAuditUserTaskSubmit.this, "Uploading", "Please wait...",true,true);

        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBMIT_RESPONSE;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
               // System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        //Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
                        Toast.makeText(getApplicationContext(), "error uploading", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG).show();
                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");

                        loading.dismiss();
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Toast.makeText(getApplicationContext(), "uploaded", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        finish();
                    }

                    else
                    {
                        System.out.println("kkk");
                        loading.dismiss();
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
                    Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_LONG).show();
                }
                setProgressBarIndeterminateVisibility(false);
                loading.dismiss();

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();

                params.put("task_id" ,taskId);
                params.put("task_area", taskArea);
                params.put("file", images.toString());
                params.put("unique_id", uniqueId);
                params.put("audit_report", description.getText().toString());
                params.put("u_id", Preferences.getInstance().userId);
                params.put("count",""+count);
                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                Preferences.getInstance().loadPreference(getApplicationContext());
                if(Preferences.getInstance().userRoleId.matches("0"))
                {
                    params.put("sch_id","audit");
                }
                else
                {
                    params.put("sch_id",Preferences.getInstance().schoolId);
                }


               // Log.d("file",images.toString());
               // Log.d("io",taskId+" "+taskArea+" "+uniqueId+" "+description.getText().toString()+" "+Preferences.getInstance().userId+" "+count+" "+Preferences.getInstance().token+" "+Preferences.getInstance().deviceId+" "+Preferences.getInstance().institutionId);
                
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                //Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
                getImages();
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
}
