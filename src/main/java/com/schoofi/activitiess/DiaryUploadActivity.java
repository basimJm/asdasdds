package com.schoofi.activitiess;

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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.SpinnerDiaryFirstVO;
import com.schoofi.utils.SpinnerDiarySecondVO;
import com.schoofi.utils.TeacherAssignmentUploadVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import smtchahal.materialspinner.MaterialSpinner;

public class DiaryUploadActivity extends AppCompatActivity {

    MaterialSpinner materialSpinner1, materialSpinner2,materialSpinner3;
    RatingBar ratingBar;
    Button done;
    JSONObject jsonObject1, jsonObject2, jsonObject3;
    JSONArray jsonArray1, jsonArray2, jsonArray3;
    ArrayList<String> typeName;
    ArrayList<SpinnerDiaryFirstVO> typeId;
    ArrayList<String> ratingName;
    ArrayList<SpinnerDiarySecondVO> ratingId;
    String typeId1, ratingId1, subjectId1="0";
    private ArrayAdapter<String> adapter;
    private ArrayList<String> subjectName;
    private ArrayList<SpinnerDiaryFirstVO> subjectId;
    int f=0,h=0;
    String ratinh;
    private Button markStudents;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Diary Upload Activity");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_diary_upload);

        materialSpinner1 = (MaterialSpinner) findViewById(R.id.spinner_type);
        materialSpinner2 = (MaterialSpinner) findViewById(R.id.spinner_rating);
        materialSpinner3 = (MaterialSpinner) findViewById(R.id.spinner_subject);

        markStudents = (Button) findViewById(R.id.btn_mark_students);


        materialSpinner1.setBackgroundResource(R.drawable.button);
        materialSpinner2.setBackgroundResource(R.drawable.button);
        materialSpinner3.setBackgroundResource(R.drawable.button);
        ratingBar = (RatingBar) findViewById(R.id.star_rating);
        done = (Button) findViewById(R.id.btn_done);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new DownloadJSON1().execute();
        new DownloadJSON2().execute();
        new DownloadJSON().execute();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratinh = String.valueOf(rating);
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f==1)
                {
                    if(ratingId1.matches("")|| ratingId1.matches("null"))
                    {
                        Utils.showToast(getApplicationContext(),"Please select all fields");
                    }
                }







               else
                {

                    if(f==3)
                    {
                        ratinh="0";
                        ratingId1="0";
                    }

                    else
                    {
                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                                ratinh = String.valueOf(rating);
                            }
                        });
                    }
                    Intent intent = new Intent(DiaryUploadActivity.this,DiaryUploadSecondActivity.class);
                    intent.putExtra("typeId", typeId1);
                    intent.putExtra("ratingId", ratingId1);
                    intent.putExtra("ratingBar", ratinh);

                        intent.putExtra("subjectId", subjectId1);

                    intent.putExtra("array",Preferences.getInstance().studentId);
                    intent.putExtra("value","1");
                    startActivity(intent);
                }
            }
        });

        markStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f==3)
                {
                    ratinh="0";
                    ratingId1="0";
                }

                else
                {
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                            ratinh = String.valueOf(rating);
                        }
                    });
                }

                Intent intent = new Intent(DiaryUploadActivity.this, DiaryMultipleStudentSelectionScreen.class);
                intent.putExtra("typeId", typeId1);
                intent.putExtra("ratingId", ratingId1);
                intent.putExtra("ratingBar", ratinh);

                intent.putExtra("subjectId", subjectId1);

                startActivity(intent);

            }
        });
    }

    private class DownloadJSON2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            ratingId = new ArrayList<SpinnerDiarySecondVO>();
            ratingName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            jsonObject2 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_SPINNER_DIARY_URL + "?value=" + "1" + "&ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&module_type=" + "diary_hygiene");
            try {
                // Locate the NodeList name
                jsonArray2 = jsonObject2.getJSONArray("responseObject");
                for (int i = 0; i < jsonArray2.length(); i++) {
                    jsonObject2 = jsonArray2.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    //EventUploadAudienceVO eventUploadAudienceVO1 = new EventUploadAudienceVO();
                    //SpinnerExamVO spinnerExamVO1 = new SpinnerExamVO();
                    SpinnerDiarySecondVO spinnerDiarySecondVO = new SpinnerDiarySecondVO();

                    spinnerDiarySecondVO.setRatingId(jsonObject2.optString("code"));
                    ratingId.add(spinnerDiarySecondVO);

                    ratingName.add(jsonObject2.optString("full_name"));

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


            adapter = new ArrayAdapter<>(DiaryUploadActivity.this, android.R.layout.simple_spinner_item, ratingName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            materialSpinner2.setAdapter(adapter);


            materialSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    ratingId1 = ratingId.get(position).getRatingId().toString();


                    // System.out.println(boardId1);


                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                    ratingId1 = "";

                }


            });


        }
    }

    private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            typeId = new ArrayList<SpinnerDiaryFirstVO>();
            typeName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            jsonObject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_SPINNER_DIARY_URL + "?value=" + "1" + "&ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&module_type=" + "diary_module");
            try {
                // Locate the NodeList name
                jsonArray1 = jsonObject1.getJSONArray("responseObject");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    jsonObject1 = jsonArray1.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    //EventUploadAudienceVO eventUploadAudienceVO1 = new EventUploadAudienceVO();
                    //SpinnerYearVO spinnerYearVO1 = new SpinnerYearVO();
                    SpinnerDiaryFirstVO spinnerDiaryFirstVO = new SpinnerDiaryFirstVO();

                    spinnerDiaryFirstVO.setTypeId(jsonObject1.optString("code"));
                    typeId.add(spinnerDiaryFirstVO);

                    typeName.add(jsonObject1.optString("full_name"));

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


            adapter = new ArrayAdapter<>(DiaryUploadActivity.this, android.R.layout.simple_spinner_item, typeName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            materialSpinner1.setAdapter(adapter);


            materialSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    typeId1 = typeId.get(position).getTypeId().toString();

                    if(typeId1.matches("diary_n") || typeId1.matches("diary_p"))
                    {

                        ratingBar.setVisibility(View.INVISIBLE);
                        materialSpinner2.setVisibility(View.INVISIBLE);

                        f=3;
                    }

                    else
                    {
                        ratingBar.setVisibility(View.VISIBLE);
                        materialSpinner2.setVisibility(View.VISIBLE);
                        f=4;

                    }


                    // System.out.println(boardId1);


                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                    typeId1 = "";

                }


            });


        }
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

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(DiaryUploadActivity.this, R.layout.spinner_layout, subjectName);

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