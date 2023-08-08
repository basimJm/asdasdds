package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import smtchahal.materialspinner.MaterialSpinner;


public class AddVaccinationDate extends AppCompatActivity {

    private Button vaccinationDate,add;
    private EditText remarks,drugName;
    private MaterialSpinner statusSpinner;
    private ImageView back;
    String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    String date5 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    public static final CharSequence[] DAYS_OPTIONS  = {"Done","Pending"};
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String Month;
    String date2,date1,vaccine_id;
    String gender1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_add_vaccination_date);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        vaccinationDate = (Button) findViewById(R.id.btn_vaccination_date);

        vaccine_id = getIntent().getStringExtra("vaccine_id");

        vaccinationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
                showDate(year,month,day);
            }
        });

        statusSpinner = (MaterialSpinner) findViewById(R.id.spinner_status);
        statusSpinner.setBackgroundResource(R.drawable.grey_button);

        remarks = (EditText) findViewById(R.id.edit_remarks);
        drugName = (EditText) findViewById(R.id.edit_drug_name);
        add = (Button) findViewById(R.id.btn_next);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (AddVaccinationDate.this, R.layout.support_simple_spinner_dropdown_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        statusSpinner.setAdapter(adapter); // Apply the adapter to the spinner







        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                gender1 = "" + parent.getItemAtPosition(position);

            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(vaccinationDate.getText().toString().matches("") || gender1.matches("Select Status"))
                {
                    Utils.showToast(getApplicationContext(),"Please fill the details");
                }

                else
                {
                    postMessage1();
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
        vaccinationDate.setText(date1);
        vaccinationDate.setTextColor(Color.BLACK);
    }

    private void postMessage1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADD_VACCINE_CALENDER/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

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
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(getApplicationContext(), "Error submitting! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                params.put("token",Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("vaccine_id",vaccine_id);
                params.put("stu_id",Preferences.getInstance().studentId);
                if(drugName.getText().toString().matches(""))
                {
                    params.put("drug_name","");
                }

                else
                {
                    params.put("drug_name",drugName.getText().toString());
                }

                if(remarks.getText().toString().matches(""))
                {
                    params.put("remark","");
                }

                else
                {
                    params.put("remark",remarks.getText().toString());
                }

                params.put("vaccine_date",date2);
                if(gender1.matches("Select Status"))
                {
                    params.put("status","");
                }

                else
                {
                    params.put("status",gender1);
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
            setSupportProgressBarIndeterminateVisibility(false);
            loading.dismiss();
        }
    }
}
