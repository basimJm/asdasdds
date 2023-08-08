package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import smtchahal.materialspinner.MaterialSpinner;


public class AddInsuranceDetails extends AppCompatActivity {

    ImageView back;
    EditText insuranceName,policyNumber,insuranceCompanyAddress,insuranceAmount,policyHolderName,insuranceCoverage,insuranceCompanyNumber,groupNumber;
    Button fromDate,toDate,add;
    MaterialSpinner insuranceType;
    public static final CharSequence[] DAYS_OPTIONS  = {"Individual","Family"};
    String gender1;

    String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_add_insurance_details);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fromDate = (Button) findViewById(R.id.btn_from_date);
        toDate = (Button) findViewById(R.id.btn_to_date);
        insuranceName = (EditText) findViewById(R.id.edit_insurance_company);
        insuranceCompanyAddress = (EditText) findViewById(R.id.edit_insurance_company_address);
        policyNumber = (EditText) findViewById(R.id.edit_policy_number);
        insuranceAmount = (EditText) findViewById(R.id.edit_amount);
        insuranceCoverage = (EditText) findViewById(R.id.edit_coverage);
        insuranceType = (MaterialSpinner) findViewById(R.id.spinner_type);
        insuranceType.setBackgroundResource(R.drawable.grey_button);
        add = (Button) findViewById(R.id.btn_next);
        groupNumber = (EditText) findViewById(R.id.edit_group);
        insuranceCompanyNumber = (EditText) findViewById(R.id.edit_insurance_company_number);
        policyHolderName = (EditText) findViewById(R.id.edit_policy_holder);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar1 = Calendar.getInstance();
        year1 = calendar1.get(Calendar.YEAR);
        month1 = calendar1.get(Calendar.MONTH);
        day1 = calendar1.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (AddInsuranceDetails.this, R.layout.support_simple_spinner_dropdown_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        insuranceType.setAdapter(adapter); // Apply the adapter to the spinner







        insuranceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                if(policyHolderName.getText().toString().matches("") || policyNumber.getText().toString().matches("") || insuranceName.getText().toString().matches("") || insuranceCoverage.getText().toString().matches("") || gender1.matches("Select Type"))
                {
                    Utils.showToast(getApplicationContext(),"Please fill the fields!");
                }

                else {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

                    try {
                       // Date date1 = formatter.parse(date);
                        String toEditTextDate1 = toDate.getText().toString();
                        Date date2 = formatter.parse(toEditTextDate1);
                        String fromEditTextDate1 = fromDate.getText().toString();
                        Date date3 = formatter.parse(fromEditTextDate1);


                        if (date2.compareTo(date3) < 0) {
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

    private void postMessage1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADD_INSURANCE_DETAILS/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

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
                params.put("insurance_company",insuranceName.getText().toString());
                params.put("stu_id",Preferences.getInstance().studentId);
                if(insuranceCompanyAddress.getText().toString().matches(""))
                {
                    params.put("insurance_company_address","");
                }

                else
                {
                    params.put("insurance_company_address",insuranceCompanyAddress.getText().toString());
                }

                if(insuranceCompanyNumber.getText().toString().matches(""))
                {
                    params.put("insurance_company_phone_number","");
                }

                else
                {
                    params.put("insurance_company_phone_number",insuranceCompanyNumber.getText().toString());
                }

                if(policyNumber.getText().toString().matches(""))
                {
                    params.put("policy_number","");
                }

                else
                {
                    params.put("policy_number",policyNumber.getText().toString());
                }

                if(policyHolderName.getText().toString().matches(""))
                {
                    params.put("policy_holder","");
                }

                else
                {
                    params.put("policy_holder",policyHolderName.getText().toString());
                }


                if(groupNumber.getText().toString().matches(""))
                {
                    params.put("group_number","");
                }

                else
                {
                    params.put("group_number",groupNumber.getText().toString());
                }

                params.put("insurance_amount",insuranceAmount.getText().toString());
                params.put("insurance_coverage",insuranceCoverage.getText().toString());

                params.put("from_date",date2);
                params.put("to_date",date3);
                if(gender1.matches("Select Type"))
                {
                    params.put("insurance_type","");
                }

                else
                {
                    params.put("insurance_type",gender1);
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
