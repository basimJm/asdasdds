package com.schoofi.activitiess;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmployeeAttendanceScreen extends AppCompatActivity {

    private DateRangeCalendarView calendar;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    int value;
    String dept_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_employee_attendance_screen);

        calendar = (DateRangeCalendarView) findViewById(R.id.calendar);

        value  = getIntent().getIntExtra("value",0);
        if(value == 4 || value ==7)
        {
           dept_id = getIntent().getStringExtra("dept_id");
        }

        Typeface typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "LobsterTwo-Regular.ttf");
        calendar.setFonts(typeface);

        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
                //Toast.makeText(EmployeeAttendanceScreen.this, "Start Date: " + startDate.getTime().toString(), Toast.LENGTH_SHORT).show();
                Date todate1 = startDate.getTime();

                System.out.print("pp"+(new SimpleDateFormat("yyyy-MM-dd").format(todate1)));
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                Date todate1 = startDate.getTime();
                Date todate2 = endDate.getTime();

                //System.out.println("pp"+(new SimpleDateFormat("yyyy-MM-dd").format(todate1)));
                if(value == 1)
                {
                    Intent intent = new Intent(EmployeeAttendanceScreen.this,EmployeeAttendanceMainScreen.class);
                    intent.putExtra("value",2);
                    intent.putExtra("startingDate",new SimpleDateFormat("yyyy-MM-dd").format(todate1));
                    intent.putExtra("endingDate",new SimpleDateFormat("yyyy-MM-dd").format(todate2));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                }

                else
                {
                    if(value ==3) {
                        Intent intent = new Intent(EmployeeAttendanceScreen.this, ChairmanEmployeeDepartmentWiseCount.class);
                        intent.putExtra("value", "2");
                        intent.putExtra("startingDate", new SimpleDateFormat("yyyy-MM-dd").format(todate1));
                        intent.putExtra("endingDate", new SimpleDateFormat("yyyy-MM-dd").format(todate2));

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                    }
                    else
                        if(value ==7)
                        {
                            Intent intent = new Intent(EmployeeAttendanceScreen.this, StudentNewAssignemntVerion.class);
                            intent.putExtra("value", "2");
                            intent.putExtra("startingDate", new SimpleDateFormat("yyyy-MM-dd").format(todate1));
                            intent.putExtra("endingDate", new SimpleDateFormat("yyyy-MM-dd").format(todate2));
                            intent.putExtra("dept_id",dept_id);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                        }
                        else
                            if(value ==12)
                            {
                                Intent intent = new Intent(EmployeeAttendanceScreen.this, StudentNewAssignemntVerion.class);
                                intent.putExtra("value", "2");
                                intent.putExtra("startingDate", new SimpleDateFormat("yyyy-MM-dd").format(todate1));
                                intent.putExtra("endingDate", new SimpleDateFormat("yyyy-MM-dd").format(todate2));


                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                            }

                    else
                    {
                        Intent intent = new Intent(EmployeeAttendanceScreen.this, ChairmanEmployeeWithoutGraphAnalysisScreen.class);
                        intent.putExtra("value", "2");
                        intent.putExtra("startingDate", new SimpleDateFormat("yyyy-MM-dd").format(todate1));
                        intent.putExtra("endingDate", new SimpleDateFormat("yyyy-MM-dd").format(todate2));
                        intent.putExtra("dept_id",dept_id);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                    }
                }

                //Toast.makeText(EmployeeAttendanceScreen.this, "Start Date: " + startDate.getTime().toString() + " End date: " + endDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            }

        });

        findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.resetAllSelectedViews();
            }
        });


//        calendar.setNavLeftImage(ContextCompat.getDrawable(this,R.drawable.ic_left));
//        calendar.setNavRightImage(ContextCompat.getDrawable(this,R.drawable.ic_right));

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -11);
        Calendar later = (Calendar) now.clone();
        later.add(Calendar.MONTH, 12);

        calendar.setVisibleMonthRange(now,later);

        Calendar startSelectionDate = Calendar.getInstance();
        startSelectionDate.add(Calendar.MONTH, 0);
        Calendar endSelectionDate = (Calendar) startSelectionDate.clone();
        endSelectionDate.add(Calendar.DATE, 0);

        calendar.setSelectedDateRange(startSelectionDate, endSelectionDate);

        Calendar current = Calendar.getInstance();
        calendar.setCurrentMonth(current);
    }
}
