package com.schoofi.activitiess;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
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
import smtchahal.materialspinner.MaterialSpinner;


public class AddMedicalHistoryDetails extends FragmentsActivity {

    private ImageView back;
    private EditText diseaseName,prescription,diagnosis,surgical;
    private Button fromDate,toDate,add;
    private MaterialSpinner isHospitalised;
    public static final String TAG = FragmentsActivity.class.getSimpleName();
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;

    int count;
    int i;

    private Bitmap bitmap;
    String imageLeave,imageLeave1;
    private int STORAGE_PERMISSION_CODE = 23;
    private ViewGroup mSelectedImagesContainer;
    private ViewGroup mSelectedImagesNone;
    HashSet<Uri> mMedia = new HashSet<Uri>();
    ArrayList<String> images = new ArrayList<String>();
    private Context mContext;
    public static final CharSequence[] DAYS_OPTIONS  = {"Yes","No"};
    String gender1 ="";
    String currentDare = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
    String date5 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

    private DatePicker datePicker;
    private Calendar calendar,calendar1;
    private int year, month, day,year1,month1,day1;
    String Month;
    String date2,date1,date3,date4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_medical_history_details);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        diseaseName = (EditText) findViewById(R.id.edit_disease_name);
        surgical = (EditText) findViewById(R.id.edit_surgical_procedure);
        diagnosis = (EditText) findViewById(R.id.edit_diagnosis);
        prescription = (EditText) findViewById(R.id.edit_prescription);
        fromDate = (Button) findViewById(R.id.btn_from_date);
        toDate = (Button) findViewById(R.id.btn_to_date);
        add = (Button) findViewById(R.id.btn_next);
        isHospitalised = (MaterialSpinner) findViewById(R.id.spinner_is_hospitalised);
        isHospitalised.setBackgroundResource(R.drawable.grey_button);
        mContext = AddMedicalHistoryDetails.this;
        View getImages = findViewById(R.id.get_images);
        //View getNImages = findViewById(R.id.get_n_images);
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);

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

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar1 = Calendar.getInstance();
        year1 = calendar1.get(Calendar.YEAR);
        month1 = calendar1.get(Calendar.MONTH);
        day1 = calendar1.get(Calendar.DAY_OF_MONTH);


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (AddMedicalHistoryDetails.this, R.layout.support_simple_spinner_dropdown_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        isHospitalised.setAdapter(adapter); // Apply the adapter to the spinner







        isHospitalised.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                gender1 = "" + parent.getItemAtPosition(position);

            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
                showDate(year,month,day);
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(998);
                showDate1(year1,month1,day1);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diseaseName.getText().toString().matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please fill the fields!");
                }

                else
                {
                    if(gender1.matches("") || gender1.matches("null"))
                    {
                        postMessage1();
                    }
                    else {


                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                            Date date1 = formatter.parse(currentDare);
                            String fromEditTextDate1 = fromDate.getText().toString();
                            Date date2 = formatter.parse(fromEditTextDate1);
                            String toEditTextDate1 = toDate.getText().toString();
                            Date date3 = formatter.parse(toEditTextDate1);


                            if (date1.compareTo(date2) < 0) {
                                Toast.makeText(getApplicationContext(), "Future Dates are not allowed!", Toast.LENGTH_SHORT).show();

                            } else if (date1.compareTo(date3) < 0) {
                                Toast.makeText(getApplicationContext(), "Future Dates are not allowed!", Toast.LENGTH_SHORT).show();

                            } else if (date3.compareTo(date2) < 0) {
                                Toast.makeText(getApplicationContext(), "To date should be greater than from date", Toast.LENGTH_SHORT).show();

                            } else if (date2.compareTo(date3) == 0) {
                                Toast.makeText(getApplicationContext(), "Dates cannot be equal", Toast.LENGTH_SHORT).show();

                            } else {
                                postMessage1();
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }

        else
        {
            return new DatePickerDialog(this, myDateListener1, year1, month1, day1);
        }

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate1(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {



        switch(month)
        {
            case 1: Month = "Jan";
                break;
            case 2: Month = "Feb";
                break;
            case 3: Month = "Mar";
                break;
            case 4: Month = "Apr";
                break;
            case 5: Month = "May";
                break;
            case 6: Month = "Jun";
                break;
            case 7: Month = "Jul";
                break;
            case 8: Month = "Aug";
                break;
            case 9: Month = "Sep";
                break;
            case 10: Month = "Oct";
                break;
            case 11: Month = "Nov";
                break;
            case 12: Month = "Dec";
                break;
            default : System.out.println("llll");

                break;
        }

        date2 = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

        date1 = String.valueOf(day)+"-"+Month+"-"+String.valueOf(year);
        fromDate.setText(date1);
        fromDate.setTextColor(Color.BLACK);
    }


    private void showDate1(int year, int month, int day) {



        switch(month)
        {
            case 1: Month = "Jan";
                break;
            case 2: Month = "Feb";
                break;
            case 3: Month = "Mar";
                break;
            case 4: Month = "Apr";
                break;
            case 5: Month = "May";
                break;
            case 6: Month = "Jun";
                break;
            case 7: Month = "Jul";
                break;
            case 8: Month = "Aug";
                break;
            case 9: Month = "Sep";
                break;
            case 10: Month = "Oct";
                break;
            case 11: Month = "Nov";
                break;
            case 12: Month = "Dec";
                break;
            default : System.out.println("llll");

                break;
        }

        date3 = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

        date4 = String.valueOf(day)+"-"+Month+"-"+String.valueOf(year);
        toDate.setText(date4);
        toDate.setTextColor(Color.BLACK);
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

    private void postMessage1()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADD_MEDICAL_HISTORY/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"Error Submitting Vaccine Details");
                        loading.dismiss();


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

                        Utils.showToast(getApplicationContext(),"Added Successfully!");
                        loading.dismiss();
                        finish();
                    }

                    else {
                              loading.dismiss();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error submitting! Please try after sometime.");
                    loading.dismiss();
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(getApplicationContext(), "Error submitting! Please try after sometime.");
                loading.dismiss();
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                params.put("token",Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("disease",diseaseName.getText().toString());
                params.put("stu_id",Preferences.getInstance().studentId);

                if(images.toString().matches("") || images.size()==0)
                {
                        //Utils.showToast(getApplicationContext(),"ll");
                    Log.d("jh","k");
                }
                else {
                    params.put("document", images.toString());
                }
                params.put("count",String.valueOf(count));
                if(prescription.getText().toString().matches(""))
                {
                    params.put("prescription","");
                }

                else
                {
                    params.put("prescription",prescription.getText().toString());
                }

                params.put("value","A");

                if(diagnosis.getText().toString().matches(""))
                {
                    params.put("diagnosis","");
                }

                else
                {
                    params.put("diagnosis",diagnosis.getText().toString());
                }

                if(surgical.getText().toString().matches(""))
                {
                    params.put("surgical_procedure","");
                }

                else
                {
                    params.put("surgical_procedure",surgical.getText().toString());
                }




                if(gender1.matches("Select Type") || gender1.matches("null") || gender1.matches(""))
                {
                    params.put("hospitalisation","");
                    params.put("hospitalised_from_date",currentDare);
                    params.put("hospitalised_to_date",currentDare);
                }

                else
                {
                    params.put("hospitalisation",gender1);
                    params.put("hospitalised_from_date",date2);
                    params.put("hospitalised_to_date",date3);
                }

                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(this, "Unable to submit data, kindly enable internet settings!");
            loading.dismiss();
            //setSupportProgressBarIndeterminateVisibility(false);
        }
    }
}
