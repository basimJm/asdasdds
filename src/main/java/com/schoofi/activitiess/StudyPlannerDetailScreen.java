package com.schoofi.activitiess;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.schoofi.adapters.EmployeeLeaveLevelUpdateAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

public class StudyPlannerDetailScreen extends AppCompatActivity {

    private TextView eventTitle,eventLocation,eventDescription,eventFromDate,eventToDate,holidayTextView,eventReferences;
    private String plannerId;
    private Button attachment;
    private int position;
    private ImageView back;
    private JSONArray studyPlannerDetailsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_study_planner_detail_screen);

        eventTitle = (TextView) findViewById(R.id.text_title);
        eventLocation = (TextView) findViewById(R.id.text_location);
        eventDescription = (TextView) findViewById(R.id.text_description);
        eventFromDate = (TextView) findViewById(R.id.text_from);
        eventToDate = (TextView) findViewById(R.id.text_to);
        eventReferences = (TextView) findViewById(R.id.text_references);
        attachment = (Button) findViewById(R.id.btn_attachment);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        holidayTextView = (TextView) findViewById(R.id.text_holiday);

       // plannerId = getIntent().getStringExtra("plannerId");
        position = getIntent().getIntExtra("position",0);

        eventTitle.setText("Title: " +getIntent().getStringExtra("title"));
        eventDescription.setText("Description: "+getIntent().getStringExtra("description"));
        eventFromDate.setText("From: "+getIntent().getStringExtra("fromDate"));
        eventToDate.setText("To: "+getIntent().getStringExtra("toDate"));
        holidayTextView.setText("Subject: "+getIntent().getStringExtra("subject"));
        eventLocation.setText("Location: "+getIntent().getStringExtra("location"));
        eventReferences.setText("References: "+getIntent().getStringExtra("references"));
        if(getIntent().getStringExtra("attachment").matches("") || getIntent().getStringExtra("attachment").matches("null"))
        {
           attachment.setVisibility(View.GONE);
        }
        else
        {
            attachment.setVisibility(View.VISIBLE);
        }

       // Log.d("pl",plannerId);
        Log.d("position",String.valueOf(position));
        Log.d("po",getIntent().getStringExtra("title"));
        Log.d("po1",getIntent().getStringExtra("description"));
        Log.d("po2",getIntent().getStringExtra("fromDate"));
        Log.d("po3",getIntent().getStringExtra("toDate"));
        Log.d("po4",getIntent().getStringExtra("subject"));
        Log.d("po5",getIntent().getStringExtra("location"));
        Log.d("po6",getIntent().getStringExtra("references"));

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("attachment").toString().endsWith("DOC") || getIntent().getStringExtra("attachment").toString().endsWith("doc") || getIntent().getStringExtra("attachment").toString().endsWith("Doc") || getIntent().getStringExtra("attachment").toString().endsWith("docx") || getIntent().getStringExtra("attachment").toString().endsWith("Docx") || getIntent().getStringExtra("attachment").toString().endsWith("DOCX") || getIntent().getStringExtra("attachment").toString().endsWith("pdf") || getIntent().getStringExtra("attachment").toString().endsWith("PDF") || getIntent().getStringExtra("attachment").toString().endsWith("Pdf") || getIntent().getStringExtra("attachment").toString().endsWith("txt") || getIntent().getStringExtra("attachment").toString().endsWith("Txt") || getIntent().getStringExtra("attachment").toString().endsWith("TXT"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+getIntent().getStringExtra("attachment").toString()));
                    startActivity(intent);
                }

                else
                {
                    Intent intent = new Intent(StudyPlannerDetailScreen.this,TeacherStudentImageDetails.class);
                    intent.putExtra("imageUrl", getIntent().getStringExtra("attachment").toString());
                    startActivity(intent);
                }

            }
        });


    }



}
