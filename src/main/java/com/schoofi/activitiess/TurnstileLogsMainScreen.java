package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.schoofi.constants.AppConstants;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchooolZoneVO;
import com.schoofi.utils.StudentExamVO;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VaccinationVO;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import smtchahal.materialspinner.MaterialSpinner;


public class TurnstileLogsMainScreen extends AppCompatActivity {

    private ImageView back;
    private MaterialSpinner zoneSpinner,categorySpinner;
    private Button fromDate,toDate,next;
    public static final CharSequence[] DAYS_OPTIONS  = {"All","Parent","Student","Employee","Visitor"};
    String gender1;

    String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
    String date5 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");

    private DatePicker datePicker;
    private Calendar calendar,calendar1;
    private int year, month, day,year1,month1,day1;
    String Month;
    String date2,date1,date4;
    String date3 ="";
    ArrayList<String> examName;

    ArrayList<SchooolZoneVO> examId;
    String examId1;
    private TextView title,noRecords,selectTerm,selectExam;
    private Spinner examNames;
    SchooolZoneVO schooolZoneVO = new SchooolZoneVO();
    JSONObject jsonobject;
    JSONArray jsonarray;
    int count1 = 0;
    int count = 0;
    int count5=0;
    int count6=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_turntile_logs_main_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        zoneSpinner = (MaterialSpinner) findViewById(R.id.spinner_zone);
        categorySpinner = (MaterialSpinner) findViewById(R.id.spinner_category);
        fromDate = (Button) findViewById(R.id.btn_from);
        toDate = (Button) findViewById(R.id.btn_to);
        next = (Button) findViewById(R.id.btn_next);

        zoneSpinner.setBackgroundResource(R.drawable.grey_button);
        categorySpinner.setBackgroundResource(R.drawable.grey_button);

        //Alerter.create(TurnstileLogsMainScreen.this).setTitle("Alert Title").setText("Alert text...").show();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar1 = Calendar.getInstance();
        year1 = calendar1.get(Calendar.YEAR);
        month1 = calendar1.get(Calendar.MONTH);
        day1 = calendar1.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (TurnstileLogsMainScreen.this, R.layout.support_simple_spinner_dropdown_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        categorySpinner.setAdapter(adapter); // Apply the adapter to the spinner







        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        new DownloadJSON().execute();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gender1.matches("") || examId1.matches("") || date2.matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please select the fields");
                }





                else {
                    if(count5 ==1 && count6==0) {
                        Intent intent = new Intent(TurnstileLogsMainScreen.this, TurnstileLogsSecondScreen.class);
                        intent.putExtra("category", gender1);
                        intent.putExtra("zone_id", examId1);
                        intent.putExtra("start_date", date2);
                        intent.putExtra("end_date", date3);
                        startActivity(intent);
                    }
                    else
                        if(count5==1 &&count6==1) {

                            if (count == 0 && count1 == 0)
                                Toast.makeText(getApplicationContext(), "not valid date", Toast.LENGTH_SHORT).show();
                            else if (count == 0 && count1 == 1) {
                                Toast.makeText(getApplicationContext(), "not valid date", Toast.LENGTH_SHORT).show();
                            } else if (count == 1 && count1 == 0) {
                                Toast.makeText(getApplicationContext(), "not valid date", Toast.LENGTH_SHORT).show();
                            } else
                            //view = inflater.inflate(R.layout.student_daily_attendance, container, false);
                            {
                                Intent intent = new Intent(TurnstileLogsMainScreen.this, TurnstileLogsSecondScreen.class);
                                intent.putExtra("category", gender1);
                                intent.putExtra("zone_id", examId1);
                                intent.putExtra("start_date", date2);
                                intent.putExtra("end_date", date3);
                                startActivity(intent);
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

        count5=1;

        String month1,day1;
        if(month<10)
        {
            month1 = "0"+Integer.toString(month);
        }

        else
        {
            month1 = Integer.toString(month);
        }

        if(day<10)
        {
            day1 = "0"+Integer.toString(day);
        }

        else
        {
            day1 = Integer.toString(day);
        }

        date2 = Integer.toString(year)+"-"+month1+"-"+day1;

        date1 = String.valueOf(day)+"-"+Month+"-"+String.valueOf(year);
        fromDate.setText(date1);
        fromDate.setTextColor(Color.BLACK);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date date1 = formatter.parse(date);

            Date date2 = formatter.parse(fromDate.getText().toString());

            if(date1.compareTo(date2)<0)
            {
                Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                count=0;
            }
            else
            {
                count =1;
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

        count6=1;

        String month1,day1;
        if(month<10)
        {
            month1 = "0"+Integer.toString(month);
        }

        else
        {
            month1 = Integer.toString(month);
        }

        if(day<10)
        {
            day1 = "0"+Integer.toString(day);
        }

        else
        {
            day1 = Integer.toString(day);
        }

        date3 = Integer.toString(year)+"-"+month1+"-"+day1;

        date4 = String.valueOf(day)+"-"+Month+"-"+String.valueOf(year);
        toDate.setText(date4);
        toDate.setTextColor(Color.BLACK);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            Date date1 = formatter.parse(date);


            Date date2 = formatter.parse(toDate.getText().toString());

            Date date3 = formatter.parse(fromDate.getText().toString());
            if(date1.compareTo(date2)<0)
            {
                Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                count1 =0;
            }
            else
            if(date2.compareTo(date3)<0)
            {
                Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                count1=0;
            }

            else
            if(date2.compareTo(date3)==0)
            {
                Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                count1=0;
            }
            else
            {
                count1 =1;
            }


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            examId = new ArrayList<SchooolZoneVO>();
            examName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            Preferences.getInstance().loadPreference(TurnstileLogsMainScreen.this);
            System.out.println(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ZONE_SPINNER+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId);
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ZONE_SPINNER+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("responseObject");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    SchooolZoneVO schooolZoneVO = new SchooolZoneVO();

                    schooolZoneVO.setZoneId(jsonobject.optString("zone_id"));
                    examId.add(schooolZoneVO);

                    examName.add(jsonobject.optString("zone_name"));

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

            zoneSpinner
                    .setAdapter(new ArrayAdapter<String>(TurnstileLogsMainScreen.this,
                            android.R.layout.simple_spinner_dropdown_item,examName
                    ));

            zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    examId1 = examId.get(position).getZoneId().toString();
                    //System.out.println(groupId1);






                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                    examId1="";

                }


            });




        }
    }
}
