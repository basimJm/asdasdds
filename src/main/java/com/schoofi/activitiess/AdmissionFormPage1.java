package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.EnquiryTypeVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdmissionFormPage1 extends AppCompatActivity {

    ImageView back;
    Spinner gender;
    EditText name,email,phone,dob,studentClass,address,stream;
    Button next;
    String name1,email1,phone1,dob1,studentClass1,address1,gender1,schoolId1,enquiryType,stream1;
    JSONObject jsonObject;
    JSONArray enquiryTypeArray;
    ArrayList<String> enuiryNames;
    ArrayList<EnquiryTypeVO> enquiryId;
    EnquiryTypeVO enquiryTypeVO = new EnquiryTypeVO();
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String date,date1;
    String Month,brochureFees,addmissionCharges,courierCharges;
    public static final CharSequence[] DAYS_OPTIONS  = {"Select Gender","Male","Female"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admission FormPage1");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admission_form_page1);

        back = (ImageView) findViewById(R.id.img_back);

        name = (EditText) findViewById(R.id.edit_candidateName);
        dob = (EditText) findViewById(R.id.edit_dob);
        gender = (Spinner) findViewById(R.id.spin_gender);
        phone = (EditText) findViewById(R.id.edit_mobile);
        email = (EditText) findViewById(R.id.edit_email_Id);
        address = (EditText) findViewById(R.id.edit_address);
        studentClass = (EditText) findViewById(R.id.edit_class);
        stream = (EditText) findViewById(R.id.edit_stream);
        next = (Button) findViewById(R.id.btn_next);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        enquiryType = getIntent().getStringExtra("type");
        schoolId1 = getIntent().getStringExtra("schoolId");
        addmissionCharges = getIntent().getStringExtra("admissionFees");
        brochureFees = getIntent().getStringExtra("brochureFees");
        courierCharges = getIntent().getStringExtra("courier_charges");



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(999);
                showDate(year, month, day);

            }
        });

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, R.layout.spinner_layout, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        gender.setAdapter(adapter); // Apply the adapter to the spinner



        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                gender1 = "" + parent.getItemAtPosition(position);
                if(gender1.matches("Select Gender"))
                {
                    Log.d("fff","lll");
                }
                else
                {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name1 = name.getText().toString();
                dob1 = dob.getText().toString();
               // gender1 = gender.getText().toString();
                studentClass1 = studentClass.getText().toString();
                address1 = address.getText().toString();
                phone1 = phone.getText().toString();
                email1 = email.getText().toString();
                stream1 = stream.getText().toString();


                if(name1.matches("") || dob1.matches("") || address1.matches("") || studentClass1.matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please fill the fields");
                }

                else
                {
                    Intent intent = new Intent(AdmissionFormPage1.this,AdmissionFormPage2.class);
                    intent.putExtra("name",name1);
                    intent.putExtra("dob",dob1);
                    intent.putExtra("gender",gender1);
                    intent.putExtra("studentClass",studentClass1);
                    intent.putExtra("address",address1);
                    intent.putExtra("phone",phone1);
                    intent.putExtra("email",email1);
                    intent.putExtra("schoolId",schoolId1);
                    intent.putExtra("stream",stream1);
                    //Utils.showToast(getApplicationContext(),enquiryType.toString());
                    intent.putExtra("type",enquiryType);
                    intent.putExtra("brochureFees",brochureFees);
                    intent.putExtra("admissionCharges",addmissionCharges);
                    intent.putExtra("courier_charges",courierCharges);
                    startActivity(intent);
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
        return null;
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

        public void onCancel(DialogInterface dialog){
            // Send a message to confirm cancel button click
            Toast.makeText(getApplicationContext(),"Date Picker Canceled.", Toast.LENGTH_SHORT).show();
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

        date = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

        date1 = String.valueOf(day)+"-"+Month+"-"+String.valueOf(year);
        dob.setText(date1);


    }
}
