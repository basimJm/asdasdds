package com.schoofi.activitiess;

import android.content.Intent;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.SpinnerDiaryFirstVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import smtchahal.materialspinner.MaterialSpinner;

public class ParentDiaryUploadScreenFirst extends AppCompatActivity {

    MaterialSpinner materialSpinner3;
    JSONObject jsonObject3;
    JSONArray jsonArray3;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> subjectName;
    private ArrayList<SpinnerDiaryFirstVO> subjectId;
    String subjectId1;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Parent Diary Upoad Screen First");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_parent_diary_upload_screen_first);

        new DownloadJSON().execute();

        submit = (Button) findViewById(R.id.btn_done);
        materialSpinner3 = (MaterialSpinner) findViewById(R.id.spinner_type);
        materialSpinner3.setBackgroundResource(R.drawable.button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      if(subjectId1.matches("") || subjectId1.matches("null"))
                      {
                          Utils.showToast(getApplicationContext(),"Please select subject");
                      }

                else
                      {
                          Intent intent = new Intent(ParentDiaryUploadScreenFirst.this,DiaryUploadSecondActivity.class);
                          intent.putExtra("subjectId",subjectId1);
                          intent.putExtra("value","2");
                          startActivity(intent);
                          finish();
                      }
            }
        });
    }


    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            subjectId = new ArrayList<SpinnerDiaryFirstVO>();
            subjectName = new ArrayList<String>();
            // JSON file URL address
            jsonObject3 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ASSIGNMENT_CLASS_LIST + "?sec_id=" + Preferences.getInstance().studentSectionId + "&sch_id=" + Preferences.getInstance().schoolId + "&cls_id=" + Preferences.getInstance().studentClassId + "&u_id=" + Preferences.getInstance().userId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                jsonArray3 = jsonObject3.getJSONArray("subject_List");
                //System.out.print(jsonarray.toString());
                for (int i = 0; i < jsonArray3.length(); i++) {
                    jsonObject3 = jsonArray3.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    SpinnerDiaryFirstVO spinnerDiaryFirstVO2 = new SpinnerDiaryFirstVO();
                    spinnerDiaryFirstVO2.setTypeId(jsonObject3.optString("subject_id"));
                    subjectId.add(spinnerDiaryFirstVO2);
                    subjectName.add(jsonObject3.optString("subject_name"));

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

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ParentDiaryUploadScreenFirst.this, R.layout.spinner_layout, subjectName);

			/*teacherClass
			.setAdapter(new ArrayAdapter<String>(NewAssignmentTeacher.this,
					android.R.layout.simple_spinner_dropdown_item,
					UploadClassNames));*/
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            materialSpinner3.setAdapter(adapter);

            materialSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    subjectId1 = subjectId.get(position).getTypeId().toString();
                    //Utils.showToast(getApplicationContext(),teacherAssignmentUpload1);


                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub


                }


            });


        }
    }
}
