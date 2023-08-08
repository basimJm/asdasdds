package com.schoofi.activitiess;

import android.app.ProgressDialog;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.adapters.PollOptionsListViewAdapter;
import com.schoofi.adapters.PollOptionsListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewPollQuestionList extends AppCompatActivity {






    private TextView noOfQuestion;


    private Button next;

    private JSONArray pollOptionArray;

    String studentId = Preferences.getInstance().studentId;
    String userId = Preferences.getInstance().userId;
    String userEmailId = Preferences.getInstance().userEmailId;
    String token = Preferences.getInstance().token;
    String schoolId = Preferences.getInstance().schoolId;
    String userType = Preferences.getInstance().userType;
    int index,count1=0,count2=0;
    String value,value1="",poll_id,value2="";
    private Boolean status;
    String array1,array2,array3,array4;
    public ArrayList<String> attendance = new ArrayList<String>();
    public ArrayList<String> attendanceName = new ArrayList<String>();


    private Button answer1,answer2,answer3,answer4,answer5,answer6,answer7,answer8,answer9,answer10;
    private ImageView imageAnswer1,imageAnswer2,imageAnswer3,imageAnswer4,imageAnswer5,imageAnswer6,imageAnswer7,imageAnswer8,imageAnswer9,imageAnswer10,back;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_new_poll_question_list);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        next = (Button) findViewById(R.id.btn_skip);

        noOfQuestion = (TextView) findViewById(R.id.text_question);
        imageAnswer1 = (ImageView) findViewById(R.id.image_poll1);
        imageAnswer2 = (ImageView) findViewById(R.id.image_poll2);
        imageAnswer3 = (ImageView) findViewById(R.id.image_poll3);
        imageAnswer4 = (ImageView) findViewById(R.id.image_poll4);
        answer1 = (Button) findViewById(R.id.btn_pollOption1);
        answer2 = (Button) findViewById(R.id.btn_pollOption2);
        answer3 = (Button) findViewById(R.id.btn_pollOption3);
        answer4 = (Button) findViewById(R.id.btn_pollOption4);
        imageAnswer5 = (ImageView) findViewById(R.id.image_poll5);
        imageAnswer6 = (ImageView) findViewById(R.id.image_poll6);
        imageAnswer7 = (ImageView) findViewById(R.id.image_poll7);
        imageAnswer8 = (ImageView) findViewById(R.id.image_poll8);
        answer5 = (Button) findViewById(R.id.btn_pollOption5);
        answer6 = (Button) findViewById(R.id.btn_pollOption6);
        answer7 = (Button) findViewById(R.id.btn_pollOption7);
        answer8 = (Button) findViewById(R.id.btn_pollOption8);
        imageAnswer9 = (ImageView) findViewById(R.id.image_poll9);
        imageAnswer10 = (ImageView) findViewById(R.id.image_poll10);
        answer9 = (Button) findViewById(R.id.btn_pollOption9);
        answer10 = (Button) findViewById(R.id.btn_pollOption10);

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.VISIBLE);
                imageAnswer2.setVisibility(View.GONE);
                imageAnswer3.setVisibility(View.GONE);
                imageAnswer4.setVisibility(View.GONE);
                imageAnswer5.setVisibility(View.GONE);
                imageAnswer6.setVisibility(View.GONE);
                imageAnswer7.setVisibility(View.GONE);
                imageAnswer8.setVisibility(View.GONE);
                imageAnswer9.setVisibility(View.GONE);
                imageAnswer10.setVisibility(View.GONE);
                count1 = 0;
                status = true;
                value1 = answer1.getText().toString();
                value2 = "0";
                count2=0;
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.GONE);
                imageAnswer2.setVisibility(View.VISIBLE);
                imageAnswer3.setVisibility(View.GONE);
                imageAnswer4.setVisibility(View.GONE);
                imageAnswer5.setVisibility(View.GONE);
                imageAnswer6.setVisibility(View.GONE);
                imageAnswer7.setVisibility(View.GONE);
                imageAnswer8.setVisibility(View.GONE);
                imageAnswer9.setVisibility(View.GONE);
                imageAnswer10.setVisibility(View.GONE);
                count1 = 0;
                status = true;
                value1 = answer2.getText().toString();
                value2 = "1";
                count2=0;
            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.GONE);
                imageAnswer2.setVisibility(View.GONE);
                imageAnswer3.setVisibility(View.VISIBLE);
                imageAnswer4.setVisibility(View.GONE);
                imageAnswer5.setVisibility(View.GONE);
                imageAnswer6.setVisibility(View.GONE);
                imageAnswer7.setVisibility(View.GONE);
                imageAnswer8.setVisibility(View.GONE);
                imageAnswer9.setVisibility(View.GONE);
                imageAnswer10.setVisibility(View.GONE);
                count1 = 0;
                status = true;
                value1 = answer3.getText().toString();
                value2 = "2";
                count2=0;
            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.GONE);
                imageAnswer2.setVisibility(View.GONE);
                imageAnswer3.setVisibility(View.GONE);
                imageAnswer4.setVisibility(View.VISIBLE);
                imageAnswer5.setVisibility(View.GONE);
                imageAnswer6.setVisibility(View.GONE);
                imageAnswer7.setVisibility(View.GONE);
                imageAnswer8.setVisibility(View.GONE);
                imageAnswer9.setVisibility(View.GONE);
                imageAnswer10.setVisibility(View.GONE);
                count1 = 0;
                status = true;
                value1 = answer4.getText().toString();
                value2 = "3";
                count2=0;
            }
        });

        answer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.GONE);
                imageAnswer2.setVisibility(View.GONE);
                imageAnswer3.setVisibility(View.GONE);
                imageAnswer4.setVisibility(View.GONE);
                imageAnswer5.setVisibility(View.VISIBLE);
                imageAnswer6.setVisibility(View.GONE);
                imageAnswer7.setVisibility(View.GONE);
                imageAnswer8.setVisibility(View.GONE);
                imageAnswer9.setVisibility(View.GONE);
                imageAnswer10.setVisibility(View.GONE);
                count1 = 0;
                status = true;
                value1 = answer5.getText().toString();
                value2 = "4";
                count2=0;
            }
        });

        answer6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.GONE);
                imageAnswer2.setVisibility(View.GONE);
                imageAnswer3.setVisibility(View.GONE);
                imageAnswer4.setVisibility(View.GONE);
                imageAnswer5.setVisibility(View.GONE);
                imageAnswer6.setVisibility(View.VISIBLE);
                imageAnswer7.setVisibility(View.GONE);
                imageAnswer8.setVisibility(View.GONE);
                imageAnswer9.setVisibility(View.GONE);
                imageAnswer10.setVisibility(View.GONE);
                count1 = 0;
                status = true;
                value1 = answer6.getText().toString();
                value2 = "5";
                count2=0;
            }
        });

        answer7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.GONE);
                imageAnswer2.setVisibility(View.GONE);
                imageAnswer3.setVisibility(View.GONE);
                imageAnswer4.setVisibility(View.GONE);
                imageAnswer5.setVisibility(View.GONE);
                imageAnswer6.setVisibility(View.GONE);
                imageAnswer7.setVisibility(View.VISIBLE);
                imageAnswer8.setVisibility(View.GONE);
                imageAnswer9.setVisibility(View.GONE);
                imageAnswer10.setVisibility(View.GONE);
                count1 = 0;
                status = true;
                value1 = answer7.getText().toString();
                value2 = "6";
                count2=0;
            }
        });

        answer8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.GONE);
                imageAnswer2.setVisibility(View.GONE);
                imageAnswer3.setVisibility(View.GONE);
                imageAnswer4.setVisibility(View.GONE);
                imageAnswer5.setVisibility(View.GONE);
                imageAnswer6.setVisibility(View.GONE);
                imageAnswer7.setVisibility(View.GONE);
                imageAnswer8.setVisibility(View.VISIBLE);
                imageAnswer9.setVisibility(View.GONE);
                imageAnswer10.setVisibility(View.GONE);
                count1 = 0;
                status = true;
                value1 = answer8.getText().toString();
                value2 = "7";
                count2=0;
            }
        });

        answer9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.GONE);
                imageAnswer2.setVisibility(View.GONE);
                imageAnswer3.setVisibility(View.GONE);
                imageAnswer4.setVisibility(View.GONE);
                imageAnswer5.setVisibility(View.GONE);
                imageAnswer6.setVisibility(View.GONE);
                imageAnswer7.setVisibility(View.GONE);
                imageAnswer8.setVisibility(View.GONE);
                imageAnswer9.setVisibility(View.VISIBLE);
                imageAnswer10.setVisibility(View.GONE);
                count1 = 0;
                status = true;
                value1 = answer9.getText().toString();
                value2 = "8";
                count2=0;
            }
        });

        answer10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAnswer1.setVisibility(View.GONE);
                imageAnswer2.setVisibility(View.GONE);
                imageAnswer3.setVisibility(View.GONE);
                imageAnswer4.setVisibility(View.GONE);
                imageAnswer5.setVisibility(View.GONE);
                imageAnswer6.setVisibility(View.GONE);
                imageAnswer7.setVisibility(View.GONE);
                imageAnswer8.setVisibility(View.GONE);
                imageAnswer9.setVisibility(View.GONE);
                imageAnswer10.setVisibility(View.VISIBLE);
                count1 = 0;
                status = true;
                value1 = answer10.getText().toString();
                value2 = "9";
                count2=0;
            }
        });

        index = getIntent().getIntExtra("index",0);
        poll_id = getIntent().getStringExtra("poll_id");

        Log.d("index", String.valueOf(index));
        Log.d("poll_id",poll_id);

        Preferences.getInstance().loadPreference(getApplicationContext());
        if(Preferences.getInstance().userRoleId.matches("5"))
        {
            value = "1";
            initData();
            getStudentPollList();
        }
        else
            if(Preferences.getInstance().userRoleId.matches("6"))
            {
                value = "2";
                initData();
                getStudentPollList();
            }

            else
                if(Preferences.getInstance().userRoleId.matches("4"))
                {
                    value = "3";
                    initData();
                    getStudentPollList();
                }

                else
                {
                    value = "4";
                    initData();
                    getStudentPollList();
                }



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (value1.matches("") || value1.matches("null")) {
                    Utils.showToast(getApplicationContext(), "Please select one option");
                } else {


                    if (index == pollOptionArray.length() - 1) {

                        try {
                            attendance.add(pollOptionArray.getJSONObject(index).getString("q_id") + "-" + value2);

                            array1 = attendance.toString();
                            array2 = array1.substring(1, array1.length()-1);
                            attendanceName.add(value1);
                            array3 = attendanceName.toString();
                            array4 = array3.substring(1,array3.length()-1);
                            Log.d("array", array2.toString());
                            Log.d("array1", array4.toString());
                            if(array2.matches("") || array2.matches("null") || array4.matches("") || array4.matches("null"))
                            {
                                Utils.showToast(getApplicationContext(),"Not able to submit");
                            }
                            else
                            {
                                postAttendance();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (index == pollOptionArray.length() - 2) {
                            next.setText("Submit");
                        }
                        try {
                            attendance.add(pollOptionArray.getJSONObject(index).getString("q_id") + "-" + value2);
                            attendanceName.add(value1);
                            value1 = "";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        index = index + 1;

                        Cache.Entry e = new Cache.Entry();
                        e.data = pollOptionArray.toString().getBytes();
                        VolleySingleton.getInstance(NewPollQuestionList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_QUESTION_LIST + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&u_type=" + userType + "&sch_id=" + schoolId + "&device_id=" + Preferences.getInstance().deviceId + "&value=" + value + "&poll_id=" + poll_id, e);
                        try {
                            noOfQuestion.setText(index + 1 + "." + " " + pollOptionArray.getJSONObject(index).getString("ques"));

                            int position = Integer.parseInt(pollOptionArray.getJSONObject(index).getString("number_of_choices"));

                            switch (position) {
                                case 1:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setVisibility(View.GONE);
                                    answer3.setVisibility(View.GONE);
                                    answer4.setVisibility(View.GONE);
                                    answer5.setVisibility(View.GONE);
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 2:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setVisibility(View.GONE);
                                    answer4.setVisibility(View.GONE);
                                    answer5.setVisibility(View.GONE);
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 3:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setVisibility(View.GONE);
                                    answer5.setVisibility(View.GONE);
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 4:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setVisibility(View.GONE);
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 5:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 6:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 7:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 8:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                                    answer8.setText(pollOptionArray.getJSONObject(index).getString("choice_h"));
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 9:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                                    answer8.setText(pollOptionArray.getJSONObject(index).getString("choice_h"));
                                    answer9.setText(pollOptionArray.getJSONObject(index).getString("choice_i"));
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 10:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                                    answer8.setText(pollOptionArray.getJSONObject(index).getString("choice_h"));
                                    answer9.setText(pollOptionArray.getJSONObject(index).getString("choice_i"));
                                    answer10.setText(pollOptionArray.getJSONObject(index).getString("choice_j"));
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;


                                default:
                                    Utils.showToast(getApplicationContext(), "no records");
                                    break;


                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                    }
                }
            }
        });


    }


    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_QUESTION_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+ Preferences.getInstance().deviceId+"&value="+value+"&poll_id="+poll_id);
            if(e == null)
            {
                pollOptionArray= null;
            }
            else
            {
                pollOptionArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(pollOptionArray!= null)
        {
            try {
                noOfQuestion.setText(index+1 +"."+" "+pollOptionArray.getJSONObject(index).getString("ques"));
                int position = Integer.parseInt(pollOptionArray.getJSONObject(index).getString("number_of_choices"));

                switch (position)
                {
                    case 1:
                            answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                            answer2.setVisibility(View.GONE);
                            answer3.setVisibility(View.GONE);
                            answer4.setVisibility(View.GONE);
                            answer5.setVisibility(View.GONE);
                            answer6.setVisibility(View.GONE);
                            answer7.setVisibility(View.GONE);
                            answer8.setVisibility(View.GONE);
                            answer9.setVisibility(View.GONE);
                            answer10.setVisibility(View.GONE);
                            imageAnswer1.setVisibility(View.GONE);
                            imageAnswer2.setVisibility(View.GONE);
                            imageAnswer3.setVisibility(View.GONE);
                            imageAnswer4.setVisibility(View.GONE);
                            imageAnswer5.setVisibility(View.GONE);
                            imageAnswer6.setVisibility(View.GONE);
                            imageAnswer7.setVisibility(View.GONE);
                            imageAnswer8.setVisibility(View.GONE);
                            imageAnswer9.setVisibility(View.GONE);
                            imageAnswer10.setVisibility(View.GONE);

                            break;

                    case 2:
                        answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                        answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                        answer3.setVisibility(View.GONE);
                        answer4.setVisibility(View.GONE);
                        answer5.setVisibility(View.GONE);
                        answer6.setVisibility(View.GONE);
                        answer7.setVisibility(View.GONE);
                        answer8.setVisibility(View.GONE);
                        answer9.setVisibility(View.GONE);
                        answer10.setVisibility(View.GONE);
                        imageAnswer1.setVisibility(View.GONE);
                        imageAnswer2.setVisibility(View.GONE);
                        imageAnswer3.setVisibility(View.GONE);
                        imageAnswer4.setVisibility(View.GONE);
                        imageAnswer5.setVisibility(View.GONE);
                        imageAnswer6.setVisibility(View.GONE);
                        imageAnswer7.setVisibility(View.GONE);
                        imageAnswer8.setVisibility(View.GONE);
                        imageAnswer9.setVisibility(View.GONE);
                        imageAnswer10.setVisibility(View.GONE);

                        break;

                    case 3:
                        answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                        answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                        answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                        answer4.setVisibility(View.GONE);
                        answer5.setVisibility(View.GONE);
                        answer6.setVisibility(View.GONE);
                        answer7.setVisibility(View.GONE);
                        answer8.setVisibility(View.GONE);
                        answer9.setVisibility(View.GONE);
                        answer10.setVisibility(View.GONE);
                        imageAnswer1.setVisibility(View.GONE);
                        imageAnswer2.setVisibility(View.GONE);
                        imageAnswer3.setVisibility(View.GONE);
                        imageAnswer4.setVisibility(View.GONE);
                        imageAnswer5.setVisibility(View.GONE);
                        imageAnswer6.setVisibility(View.GONE);
                        imageAnswer7.setVisibility(View.GONE);
                        imageAnswer8.setVisibility(View.GONE);
                        imageAnswer9.setVisibility(View.GONE);
                        imageAnswer10.setVisibility(View.GONE);

                        break;

                    case 4:
                        answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                        answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                        answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                        answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                        answer5.setVisibility(View.GONE);
                        answer6.setVisibility(View.GONE);
                        answer7.setVisibility(View.GONE);
                        answer8.setVisibility(View.GONE);
                        answer9.setVisibility(View.GONE);
                        answer10.setVisibility(View.GONE);
                        imageAnswer1.setVisibility(View.GONE);
                        imageAnswer2.setVisibility(View.GONE);
                        imageAnswer3.setVisibility(View.GONE);
                        imageAnswer4.setVisibility(View.GONE);
                        imageAnswer5.setVisibility(View.GONE);
                        imageAnswer6.setVisibility(View.GONE);
                        imageAnswer7.setVisibility(View.GONE);
                        imageAnswer8.setVisibility(View.GONE);
                        imageAnswer9.setVisibility(View.GONE);
                        imageAnswer10.setVisibility(View.GONE);

                        break;

                    case 5:
                        answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                        answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                        answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                        answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                        answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                        answer6.setVisibility(View.GONE);
                        answer7.setVisibility(View.GONE);
                        answer8.setVisibility(View.GONE);
                        answer9.setVisibility(View.GONE);
                        answer10.setVisibility(View.GONE);
                        imageAnswer1.setVisibility(View.GONE);
                        imageAnswer2.setVisibility(View.GONE);
                        imageAnswer3.setVisibility(View.GONE);
                        imageAnswer4.setVisibility(View.GONE);
                        imageAnswer5.setVisibility(View.GONE);
                        imageAnswer6.setVisibility(View.GONE);
                        imageAnswer7.setVisibility(View.GONE);
                        imageAnswer8.setVisibility(View.GONE);
                        imageAnswer9.setVisibility(View.GONE);
                        imageAnswer10.setVisibility(View.GONE);

                        break;

                    case 6:
                        answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                        answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                        answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                        answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                        answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                        answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                        answer7.setVisibility(View.GONE);
                        answer8.setVisibility(View.GONE);
                        answer9.setVisibility(View.GONE);
                        answer10.setVisibility(View.GONE);
                        imageAnswer1.setVisibility(View.GONE);
                        imageAnswer2.setVisibility(View.GONE);
                        imageAnswer3.setVisibility(View.GONE);
                        imageAnswer4.setVisibility(View.GONE);
                        imageAnswer5.setVisibility(View.GONE);
                        imageAnswer6.setVisibility(View.GONE);
                        imageAnswer7.setVisibility(View.GONE);
                        imageAnswer8.setVisibility(View.GONE);
                        imageAnswer9.setVisibility(View.GONE);
                        imageAnswer10.setVisibility(View.GONE);

                        break;

                    case 7:
                        answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                        answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                        answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                        answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                        answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                        answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                        answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                        answer8.setVisibility(View.GONE);
                        answer9.setVisibility(View.GONE);
                        answer10.setVisibility(View.GONE);
                        imageAnswer1.setVisibility(View.GONE);
                        imageAnswer2.setVisibility(View.GONE);
                        imageAnswer3.setVisibility(View.GONE);
                        imageAnswer4.setVisibility(View.GONE);
                        imageAnswer5.setVisibility(View.GONE);
                        imageAnswer6.setVisibility(View.GONE);
                        imageAnswer7.setVisibility(View.GONE);
                        imageAnswer8.setVisibility(View.GONE);
                        imageAnswer9.setVisibility(View.GONE);
                        imageAnswer10.setVisibility(View.GONE);

                        break;

                    case 8:
                        answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                        answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                        answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                        answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                        answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                        answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                        answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                        answer8.setText(pollOptionArray.getJSONObject(index).getString("choice_h"));
                        answer9.setVisibility(View.GONE);
                        answer10.setVisibility(View.GONE);
                        imageAnswer1.setVisibility(View.GONE);
                        imageAnswer2.setVisibility(View.GONE);
                        imageAnswer3.setVisibility(View.GONE);
                        imageAnswer4.setVisibility(View.GONE);
                        imageAnswer5.setVisibility(View.GONE);
                        imageAnswer6.setVisibility(View.GONE);
                        imageAnswer7.setVisibility(View.GONE);
                        imageAnswer8.setVisibility(View.GONE);
                        imageAnswer9.setVisibility(View.GONE);
                        imageAnswer10.setVisibility(View.GONE);

                        break;

                    case 9:
                        answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                        answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                        answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                        answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                        answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                        answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                        answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                        answer8.setText(pollOptionArray.getJSONObject(index).getString("choice_h"));
                        answer9.setText(pollOptionArray.getJSONObject(index).getString("choice_i"));
                        answer10.setVisibility(View.GONE);
                        imageAnswer1.setVisibility(View.GONE);
                        imageAnswer2.setVisibility(View.GONE);
                        imageAnswer3.setVisibility(View.GONE);
                        imageAnswer4.setVisibility(View.GONE);
                        imageAnswer5.setVisibility(View.GONE);
                        imageAnswer6.setVisibility(View.GONE);
                        imageAnswer7.setVisibility(View.GONE);
                        imageAnswer8.setVisibility(View.GONE);
                        imageAnswer9.setVisibility(View.GONE);
                        imageAnswer10.setVisibility(View.GONE);

                        break;

                    case 10:
                        answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                        answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                        answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                        answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                        answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                        answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                        answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                        answer8.setText(pollOptionArray.getJSONObject(index).getString("choice_h"));
                        answer9.setText(pollOptionArray.getJSONObject(index).getString("choice_i"));
                        answer10.setText(pollOptionArray.getJSONObject(index).getString("choice_j"));
                        imageAnswer1.setVisibility(View.GONE);
                        imageAnswer2.setVisibility(View.GONE);
                        imageAnswer3.setVisibility(View.GONE);
                        imageAnswer4.setVisibility(View.GONE);
                        imageAnswer5.setVisibility(View.GONE);
                        imageAnswer6.setVisibility(View.GONE);
                        imageAnswer7.setVisibility(View.GONE);
                        imageAnswer8.setVisibility(View.GONE);
                        imageAnswer9.setVisibility(View.GONE);
                        imageAnswer10.setVisibility(View.GONE);

                        break;


                     default: Utils.showToast(getApplicationContext(),"no records");
                              break;





                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    protected void getStudentPollList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(NewPollQuestionList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_QUESTION_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&value="+value+"&poll_id="+poll_id;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(NewPollQuestionList.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(NewPollQuestionList.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("poll_List"))
                    {
                        pollOptionArray= new JSONObject(response).getJSONArray("poll_List");
                        if(null!=pollOptionArray && pollOptionArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = pollOptionArray.toString().getBytes();
                            VolleySingleton.getInstance(NewPollQuestionList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_QUESTION_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&value="+value+"&poll_id="+poll_id,e);
                            noOfQuestion.setText(index+1 +"."+" "+pollOptionArray.getJSONObject(index).getString("ques"));
                            int position = Integer.parseInt(pollOptionArray.getJSONObject(index).getString("number_of_choices"));

                            switch (position)
                            {
                                case 1:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setVisibility(View.GONE);
                                    answer3.setVisibility(View.GONE);
                                    answer4.setVisibility(View.GONE);
                                    answer5.setVisibility(View.GONE);
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 2:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setVisibility(View.GONE);
                                    answer4.setVisibility(View.GONE);
                                    answer5.setVisibility(View.GONE);
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 3:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setVisibility(View.GONE);
                                    answer5.setVisibility(View.GONE);
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 4:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setVisibility(View.GONE);
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 5:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setVisibility(View.GONE);
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 6:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setVisibility(View.GONE);
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 7:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                                    answer8.setVisibility(View.GONE);
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 8:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                                    answer8.setText(pollOptionArray.getJSONObject(index).getString("choice_h"));
                                    answer9.setVisibility(View.GONE);
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 9:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                                    answer8.setText(pollOptionArray.getJSONObject(index).getString("choice_h"));
                                    answer9.setText(pollOptionArray.getJSONObject(index).getString("choice_i"));
                                    answer10.setVisibility(View.GONE);
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;

                                case 10:
                                    answer1.setText(pollOptionArray.getJSONObject(index).getString("choice_a"));
                                    answer2.setText(pollOptionArray.getJSONObject(index).getString("choice_b"));
                                    answer3.setText(pollOptionArray.getJSONObject(index).getString("choice_c"));
                                    answer4.setText(pollOptionArray.getJSONObject(index).getString("choice_d"));
                                    answer5.setText(pollOptionArray.getJSONObject(index).getString("choice_e"));
                                    answer6.setText(pollOptionArray.getJSONObject(index).getString("choice_f"));
                                    answer7.setText(pollOptionArray.getJSONObject(index).getString("choice_g"));
                                    answer8.setText(pollOptionArray.getJSONObject(index).getString("choice_h"));
                                    answer9.setText(pollOptionArray.getJSONObject(index).getString("choice_i"));
                                    answer10.setText(pollOptionArray.getJSONObject(index).getString("choice_j"));
                                    imageAnswer1.setVisibility(View.GONE);
                                    imageAnswer2.setVisibility(View.GONE);
                                    imageAnswer3.setVisibility(View.GONE);
                                    imageAnswer4.setVisibility(View.GONE);
                                    imageAnswer5.setVisibility(View.GONE);
                                    imageAnswer6.setVisibility(View.GONE);
                                    imageAnswer7.setVisibility(View.GONE);
                                    imageAnswer8.setVisibility(View.GONE);
                                    imageAnswer9.setVisibility(View.GONE);
                                    imageAnswer10.setVisibility(View.GONE);

                                    break;


                                default: Utils.showToast(getApplicationContext(),"no records");
                                    break;





                            }

									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(NewPollQuestionList.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(NewPollQuestionList.this, "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(NewPollQuestionList.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(NewPollQuestionList.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.NEW_POLL_SUBMIT_LIST/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(NewPollQuestionList.this,"Error Submitting response");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(NewPollQuestionList.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Utils.showToast(NewPollQuestionList.this,"Successfuly Submitted ");
                        finish();

                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    Utils.showToast(NewPollQuestionList.this, "Error submitting alert! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Utils.showToast(NewPollQuestionList.this, "Error submitting alert! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(NewPollQuestionList.this);
                Map<String,String> params = new HashMap<String, String>();

                 params.put("user_id",Preferences.getInstance().userId);
                 params.put("token",Preferences.getInstance().token);
                 params.put("device_id",Preferences.getInstance().deviceId);
                 params.put("question_action",array2.toString());
                 params.put("action_value",array4.toString());
                Log.d("user_id",Preferences.getInstance().userId);
                Log.d("token",Preferences.getInstance().token);
                Log.d("device_id",Preferences.getInstance().deviceId);

                Log.d("question_action",array2.toString());
                Log.d("action_value",array4.toString());

                 params.put("count",(String.valueOf(pollOptionArray.length())));
                 params.put("poll_id",poll_id);
                Log.d("count",(String.valueOf(pollOptionArray.length())));
                Log.d("poll_id",poll_id);

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
            loading.dismiss();
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            //setSupportProgressBarIndeterminateVisibility(false);
        }



    }







        // getQuizQuestionList();



}
