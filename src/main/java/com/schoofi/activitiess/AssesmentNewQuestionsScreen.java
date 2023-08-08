package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.schoofi.adapters.AnswerListViewAdapter;
import com.schoofi.adapters.AssesmentQuestionNumberAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.AssesmentQuestionNumberVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.QuizQuestionScreenVO;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AssesmentNewQuestionsScreen extends AppCompatActivity {

    private long timeCountInMilliSeconds = 1 * 60000;
    public ArrayList<AssesmentQuestionNumberVO> temparr1;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;

    private TextView noOfQuestion,questionTextView1,questionTextView2;
    private ImageView questionImageView1,questionImageView2;
    private ListView answerListView;
    private Button next;
    private LinearLayout linearLayoutWithImage,linearLayoutOnlyImage,linearLayoutOnlyText;
    private JSONArray quizQuestionArray;
    private AnswerListViewAdapter answerListViewAdapter;
    public ArrayList<QuizQuestionScreenVO> temparr;
    public ArrayList<String> topic = new ArrayList<String>();
    public ArrayList<String> subTopic = new ArrayList<String>();
    public ArrayList<String> quesDifficultyLevel = new ArrayList<String>();
    public ArrayList<String> correct_option = new ArrayList<String>();
    public ArrayList<String> timeTaken = new ArrayList<String>();
    public ArrayList<String> maxTime = new ArrayList<String>();
    public ArrayList<String> choosenOption = new ArrayList<String>();
    public ArrayList<String> questionId = new ArrayList<String>();
    public ArrayList<String> booleanAnswer = new ArrayList<String>();
    String type,percents,subjectId,topicId1,subTopic1,quesDifficultyLevel1,correct_option1,value1;
    int i=0,j;
    int position=0;
    int currV=0;
    boolean go;
    int time=0;
    String answer,array2;
    int isCorrect=0,totalQuestions=0;
    String test_id,test_time;
    int test_time1=0;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.assesment_question_layout);


        textViewTime = (TextView) findViewById(R.id.textViewTime);

        linearLayoutOnlyImage = (LinearLayout) findViewById(R.id.linear_only_image);
        linearLayoutOnlyText = (LinearLayout) findViewById(R.id.linear_edit_text);
        linearLayoutWithImage = (LinearLayout) findViewById(R.id.linear_edit_text_with_image);
        next = (Button) findViewById(R.id.btn_skip);
        answerListView = (ListView) findViewById(R.id.list_view_answer);
       // noOfQuestion = (TextView) findViewById(R.id.text_no_of_questions);
        questionTextView1 = (TextView) findViewById(R.id.text_question);
        questionTextView2 = (TextView) findViewById(R.id.text_question1);
        questionImageView1 = (ImageView) findViewById(R.id.img_question);
        questionImageView2 = (ImageView) findViewById(R.id.img_question1);

        test_id = getIntent().getStringExtra("test_id");
        test_time1 = Integer.parseInt(getIntent().getStringExtra("test_time"));

        //subjectId = getIntent().getStringExtra("subject_id");
        //topicId = getIntent().getStringExtra("topic_id");

        value1 = getIntent().getStringExtra("value1");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);



        if(value1.matches("1"))
        {
            position=0;

        }

        else
        {
            //position=position+1;
            position=getIntent().getIntExtra("position",position);

        }

        if(value1.matches("1")) {


            test_time1 = Integer.parseInt(String.valueOf(test_time1)) ;
        }
        else
        {

            test_time1 = Integer.parseInt(String.valueOf(test_time1));
            test_time1 = test_time1/1000;

        }

        startStop(Integer.parseInt(String.valueOf(test_time1)));
        timeTaken = new ArrayList<String>();
        maxTime = new ArrayList<String>();
        booleanAnswer = new ArrayList<String>();
        choosenOption = new ArrayList<String>();
        questionId = new ArrayList<String>();

        if(value1.matches("1"))
        {
            initData();
            getQuizQuestionList();
        }
        else
        {
            initData();
            if((position+1)== totalQuestions)
            {
                next.setText("Submit");
            }
            else
            {
                
            }
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (position+1 == totalQuestions) {
                        quizQuestionArray.getJSONObject(position).put("is_correct","0");

                        Cache.Entry e = new Cache.Entry();
                        e.data = quizQuestionArray.toString().getBytes();
                        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);

                        for(int i=0;i<quizQuestionArray.length();i++)
                        {
                            // maxTime.add(quizQuestionArray.getJSONObject(i).getString("question_time"));
                            timeTaken.add(quizQuestionArray.getJSONObject(i).getString("time_taken"));
                            choosenOption.add(quizQuestionArray.getJSONObject(i).getString("choosen_option"));
                            booleanAnswer.add(quizQuestionArray.getJSONObject(i).getString("is_correct"));
                            //Log.d("choosen_for_loop",quizQuestionArray.getJSONObject(i).getString("question_id"));
                            questionId.add(quizQuestionArray.getJSONObject(i).getString("question_id"));
                            if(quizQuestionArray.getJSONObject(i).getString("is_correct").matches("1"))
                            {
                                isCorrect++;
                            }
                            else
                            {

                            }
                        }

                        //Log.d("ll",timeTaken.toString());
                        postQuizSubmit();

                    } else {
                        quizQuestionArray.getJSONObject(position).put("time_taken","0");

                        Cache.Entry e = new Cache.Entry();
                        e.data = quizQuestionArray.toString().getBytes();
                        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);

                        position=position+1;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        answerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Log.d("oiu","poo1");
                try {
                    if((position+1) == totalQuestions)
                    {


                        quizQuestionArray.getJSONObject(position).put("choosen_option",String.valueOf(pos+1));
                        answer = quizQuestionArray.getJSONObject(position).getString("right_option");

                        if(pos==0)
                        {
                            if(answer.matches("1")) {
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                                quizQuestionArray.getJSONObject(position).put("choosen_option","1");

                                Cache.Entry e = new Cache.Entry();
                                e.data = quizQuestionArray.toString().getBytes();
                                VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);

                                Log.d("array",quizQuestionArray.toString());
                            }

                            //Utils.showToast(getApplicationContext(),""+pos);
                            /*Cache.Entry e = new Cache.Entry();
                            e.data = quizQuestionArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.MOCK_TEST_SUBJECT_LIST + "?device_id=" + "" + "&action_for=" + "question_list" + "&Percents=" + percent + "&subject_id=" + subjectId + "&topic=" + topicId + "&total_question=" + totalQusetions, e);
*/
                        }

                        else
                        if(pos==1)
                        {
                            if(answer.matches("2")) {
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                                quizQuestionArray.getJSONObject(position).put("choosen_option","2");

                                Cache.Entry e = new Cache.Entry();
                                e.data = quizQuestionArray.toString().getBytes();
                                VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);

                                Log.d("array",quizQuestionArray.toString());
                            }


                        }

                        else
                        if(pos==2)
                        {
                            if(answer.matches("3")) {

                                Log.d("uyt","ppp");
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                                quizQuestionArray.getJSONObject(position).put("choosen_option","3");

                                Cache.Entry e = new Cache.Entry();
                                e.data = quizQuestionArray.toString().getBytes();
                                VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);

                                Log.d("array",quizQuestionArray.toString());
                            }


                        }

                        else
                        if(pos==3)
                        {
                            if(answer.matches("4")) {
                                Log.d("uyt","ppp");
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                                quizQuestionArray.getJSONObject(position).put("choosen_option","4");

                                Cache.Entry e = new Cache.Entry();
                                e.data = quizQuestionArray.toString().getBytes();
                                VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);

                                Log.d("array",quizQuestionArray.toString());
                            }


                        }

                        else
                        if(pos==4)
                        {
                            if(answer.matches("5")) {
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                                quizQuestionArray.getJSONObject(position).put("choosen_option","5");

                                Cache.Entry e = new Cache.Entry();
                                e.data = quizQuestionArray.toString().getBytes();
                                VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);

                                Log.d("array",quizQuestionArray.toString());
                            }


                        }

                        else
                        {
                            quizQuestionArray.getJSONObject(position).put("is_correct","0");


                            Cache.Entry e = new Cache.Entry();
                            e.data = quizQuestionArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);

                            Log.d("array",quizQuestionArray.toString());
                        }
                        //Utils.showToast(getApplicationContext(),""+pos);

                        // quizQuestionArray.getJSONObject(position).put("time_taken",time);

//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //Do something after 100ms
//
//                                Cache.Entry e = new Cache.Entry();
//                                e.data = quizQuestionArray.toString().getBytes();
//                                VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);
//
//                                Log.d("array",quizQuestionArray.toString());
//                            }
//                        }, 60000);







                        //Log.d("ll",timeTaken.toString());
                        postQuizSubmit();




                    }

                    else
                    {

                        answer = quizQuestionArray.getJSONObject(position).getString("right_option");

                        if(pos==0)
                        {
                            if(answer.matches("1")) {
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                            }
                            quizQuestionArray.getJSONObject(position).put("choosen_option","1");
                            //Utils.showToast(getApplicationContext(),""+pos);
                            /*Cache.Entry e = new Cache.Entry();
                            e.data = quizQuestionArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.MOCK_TEST_SUBJECT_LIST + "?device_id=" + "" + "&action_for=" + "question_list" + "&Percents=" + percent + "&subject_id=" + subjectId + "&topic=" + topicId + "&total_question=" + totalQusetions, e);
*/
                        }

                        else
                        if(pos==1)
                        {
                            if(answer.matches("2")) {
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                            }

                            quizQuestionArray.getJSONObject(position).put("choosen_option","2");
                        }

                        else
                        if(pos==2)
                        {
                            if(answer.matches("3")) {
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                            }

                            quizQuestionArray.getJSONObject(position).put("choosen_option","3");
                        }

                        else
                        if(pos==3)
                        {
                            if(answer.matches("4")) {
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                            }

                            quizQuestionArray.getJSONObject(position).put("choosen_option","4");
                        }
                        else
                        if(pos==4)
                        {
                            if(answer.matches("5")) {
                                quizQuestionArray.getJSONObject(position).put("is_correct", "1");
                            }

                            quizQuestionArray.getJSONObject(position).put("choosen_option","5");
                        }
                        else
                        {
                            quizQuestionArray.getJSONObject(position).put("is_correct","0");
                        }
                        //Utils.showToast(getApplicationContext(),""+pos);

                        quizQuestionArray.getJSONObject(position).put("time_taken",time);

                        Cache.Entry e = new Cache.Entry();
                        e.data = quizQuestionArray.toString().getBytes();
                        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id, e);

                        position=position+1;
                        //position=position+1;
                        Intent intent = new Intent(getApplicationContext(), AssesmentNewQuestionsScreen.class);

                        intent.putExtra("value1", "2");
                        // intent.putExtra("subject_id", subjectId);
                        //intent.putExtra("topic_id", topicId);
                        //intent.putExtra("percent", percents);
                        intent.putExtra("position", position);
                        intent.putExtra("total_questions",totalQuestions);
                        // intent.putExtra("categories",array2);
                        intent.putExtra("test_id",test_id);
                        intent.putExtra("test_time",String.valueOf(test_time1));
                        Log.d("test_time", String.valueOf(test_time1));
                        Log.d("time", String.valueOf(time));
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }










            }
        });


    }

    private void startStop(int time) {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            setTimerValues(time);
            // call to initialize the progress bar values

            // showing the reset icon
          //  imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            //imageViewStartStop.setImageResource(R.drawable.icon_stop);
            // making edit text not editable
           // editTextMinute.setEnabled(false);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else {

            // hiding the reset icon
          //  imageViewReset.setVisibility(View.GONE);
            // changing stop icon to start icon
            //imageViewStartStop.setImageResource(R.drawable.icon_start);
            // making edit text editable
          //  editTextMinute.setEnabled(true);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }

    /**
     * method to initialize the values for count down timer
     */
    private void setTimerValues(int time) {


            // fetching value from edit text and type cast to integer

        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time  * 1000;
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

              //  progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

                test_time1 = (int) millisUntilFinished;

                Log.d("op",(String.valueOf(millisUntilFinished)));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                //setProgressBarValues();
                // hiding the reset icon
              //  imageViewReset.setVisibility(View.GONE);
                // changing stop icon to start icon
               // imageViewStartStop.setImageResource(R.drawable.icon_start);
                // making edit text editable
                //editTextMinute.setEnabled(true);
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
//    private void setProgressBarValues() {
//
//        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
//        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
//    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }

    protected void getQuizQuestionList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();


                    if(responseObject.has("responseObject"))
                    {
                        quizQuestionArray= new JSONObject(response).getJSONArray("responseObject");


                        if(null!=quizQuestionArray && quizQuestionArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = quizQuestionArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id,e);
                            if(quizQuestionArray.getJSONObject(position).getString("status").matches("0"))
                            {
                                Utils.showToast(getApplicationContext(),AppConstants.MESSAGES.NO_RECORD_FOUND_MESSAGE);
                            }

                            else
                            if(quizQuestionArray.getJSONObject(position).getString("status").matches("2"))
                            {
                                Utils.showToast(getApplicationContext(),AppConstants.MESSAGES.NETWORK_ERROR_MESSAGE);
                            }

                            else
                            if(quizQuestionArray.getJSONObject(position).getString("status").matches("1"))
                            {
                                temparr1=new ArrayList<AssesmentQuestionNumberVO>();

                                for(i=0;i<quizQuestionArray.length();i++)
                                {
                                    Log.d("kk",""+i);
                                    quizQuestionArray.getJSONObject(i).put("is_correct", "0");
                                    quizQuestionArray.getJSONObject(i).put("time_taken","0");
                                    quizQuestionArray.getJSONObject(i).put("choosen_option","NA");


                                    AssesmentQuestionNumberVO tecHomeScreenVO = new AssesmentQuestionNumberVO(String.valueOf(i+1), String.valueOf(i+1));


                                    temparr1.add(tecHomeScreenVO);
                                }
                                j=i;
                                totalQuestions = j;

                                mAdapter = new AssesmentQuestionNumberAdapter(AssesmentNewQuestionsScreen.this, temparr1,0);
                                mRecyclerView.setAdapter(mAdapter);

                               // noOfQuestion.setText(position+1+"/"+j);
                                if(quizQuestionArray.getJSONObject(position).getString("question_image").matches("") || quizQuestionArray.getJSONObject(position).getString("question_image").matches("null"))
                                {
                                    linearLayoutWithImage.setVisibility(View.GONE);
                                    linearLayoutOnlyImage.setVisibility(View.GONE);
                                    linearLayoutOnlyText.setVisibility(View.VISIBLE);
                                    questionTextView1.setVisibility(View.VISIBLE);
                                    questionTextView1.setText(quizQuestionArray.getJSONObject(position).getString("question_text"));
                                }
                                else
                                if((!quizQuestionArray.getJSONObject(position).getString("question_image").matches("") || !quizQuestionArray.getJSONObject(position).getString("question_image").matches("null")) && (!quizQuestionArray.getJSONObject(position).getString("question_text").matches("null") || !quizQuestionArray.getJSONObject(position).getString("question_text").matches("")))
                                {
                                    linearLayoutWithImage.setVisibility(View.VISIBLE);
                                    linearLayoutOnlyImage.setVisibility(View.GONE);
                                    linearLayoutOnlyText.setVisibility(View.GONE);
                                    questionTextView2.setText(quizQuestionArray.getJSONObject(position).getString("question_text"));
                                    Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL+quizQuestionArray.getJSONObject(position).getString("question_image")).placeholder(R.drawable.icon).error(R.drawable.icon).crossFade().into(questionImageView2);
                                }

                                else
                                {
                                    linearLayoutWithImage.setVisibility(View.GONE);
                                    linearLayoutOnlyImage.setVisibility(View.VISIBLE);
                                    linearLayoutOnlyText.setVisibility(View.GONE);
                                    Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL+quizQuestionArray.getJSONObject(position).getString("question_image")).placeholder(R.drawable.icon).crossFade().into(questionImageView1);
                                }




                                temparr=new ArrayList<QuizQuestionScreenVO>();
                                QuizQuestionScreenVO quizQuestionScreenVO = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option1"),quizQuestionArray.getJSONObject(position).getString("option1_image"),"A");
                                QuizQuestionScreenVO quizQuestionScreenVO1 = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option2"),quizQuestionArray.getJSONObject(position).getString("option2_image"),"B");
                                QuizQuestionScreenVO quizQuestionScreenVO2 = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option3"),quizQuestionArray.getJSONObject(position).getString("option3_image"),"C");
                                QuizQuestionScreenVO quizQuestionScreenVO3 = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option4"),quizQuestionArray.getJSONObject(position).getString("option4_image"),"D");
                                QuizQuestionScreenVO quizQuestionScreenVO4 = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option5"),quizQuestionArray.getJSONObject(position).getString("option5_image"),"E");
                                temparr.add(quizQuestionScreenVO);
                                temparr.add(quizQuestionScreenVO1);
                                temparr.add(quizQuestionScreenVO2);
                                temparr.add(quizQuestionScreenVO3);
                                temparr.add(quizQuestionScreenVO4);
                                //Utils.showToast(getApplicationContext(),temparr.toString());
                                answerListView.invalidateViews();
                                answerListViewAdapter = new AnswerListViewAdapter(AssesmentNewQuestionsScreen.this, temparr,"1");
                                answerListView.setAdapter(answerListViewAdapter);
                                answerListViewAdapter.notifyDataSetChanged();
                                // swipyRefreshLayout.setRefreshing(false);


                            }

                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), AppConstants.MESSAGES.UNABLE_TO_FETCH_DATA);
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), AppConstants.MESSAGES.UNABLE_TO_FETCH_DATA);
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), AppConstants.MESSAGES.UNABLE_TO_FETCH_DATA);
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            /*@Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                params.put("action_for","level_list");
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token","");
                //params.put("subject_id",Preferences.getInstance().subjectId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("device_id", "");
                return params;
            }*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), AppConstants.MESSAGES.NETWORK_ERROR_MESSAGE);
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ALLOTED_TEST_QUESTION_LIST+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&test_id="+test_id);
            if(e == null)
            {
                quizQuestionArray= null;
            }
            else
            {
                quizQuestionArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(quizQuestionArray!= null)
        {
            try {

                Log.d("response",quizQuestionArray.toString());
                for(int i=0;i<quizQuestionArray.length();i++)
                {
                    totalQuestions = totalQuestions+1;

                }

                temparr1=new ArrayList<AssesmentQuestionNumberVO>();

                for(i=0;i<quizQuestionArray.length();i++)
                {



                    AssesmentQuestionNumberVO tecHomeScreenVO = new AssesmentQuestionNumberVO(String.valueOf(i+1), String.valueOf(i+1));


                    temparr1.add(tecHomeScreenVO);
                }
                j=i;
                totalQuestions = j;

                mAdapter = new AssesmentQuestionNumberAdapter(AssesmentNewQuestionsScreen.this, temparr1,position);
                mRecyclerView.setAdapter(mAdapter);

//                noOfQuestion.setText(position+1+"/"+totalQuestions);
                if(quizQuestionArray.getJSONObject(position).getString("question_image").matches("") || quizQuestionArray.getJSONObject(position).getString("question_image").matches("null"))
                {
                    linearLayoutWithImage.setVisibility(View.GONE);
                    linearLayoutOnlyImage.setVisibility(View.GONE);
                    linearLayoutOnlyText.setVisibility(View.VISIBLE);
                    questionTextView1.setVisibility(View.VISIBLE);
                    questionTextView1.setText(quizQuestionArray.getJSONObject(position).getString("question_text"));
                }
                else
                if((!quizQuestionArray.getJSONObject(position).getString("question_image").matches("") || !quizQuestionArray.getJSONObject(position).getString("question_image").matches("null")) && (!quizQuestionArray.getJSONObject(position).getString("question_text").matches("null") || !quizQuestionArray.getJSONObject(position).getString("question_text").matches("")))
                {
                    linearLayoutWithImage.setVisibility(View.VISIBLE);
                    linearLayoutOnlyImage.setVisibility(View.GONE);
                    linearLayoutOnlyText.setVisibility(View.GONE);
                    questionTextView2.setText(quizQuestionArray.getJSONObject(position).getString("question_text"));
                    Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL+quizQuestionArray.getJSONObject(position).getString("question_image")).placeholder(R.drawable.icon).error(R.drawable.icon).crossFade().into(questionImageView2);
                }

                else
                {
                    linearLayoutWithImage.setVisibility(View.GONE);
                    linearLayoutOnlyImage.setVisibility(View.VISIBLE);
                    linearLayoutOnlyText.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL+quizQuestionArray.getJSONObject(position).getString("question_image")).placeholder(R.drawable.icon).crossFade().into(questionImageView1);
                }




                temparr=new ArrayList<QuizQuestionScreenVO>();
                QuizQuestionScreenVO quizQuestionScreenVO = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option1"),quizQuestionArray.getJSONObject(position).getString("option1_image"),"A");
                QuizQuestionScreenVO quizQuestionScreenVO1 = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option2"),quizQuestionArray.getJSONObject(position).getString("option2_image"),"B");
                QuizQuestionScreenVO quizQuestionScreenVO2 = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option3"),quizQuestionArray.getJSONObject(position).getString("option3_image"),"C");
                QuizQuestionScreenVO quizQuestionScreenVO3 = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option4"),quizQuestionArray.getJSONObject(position).getString("option4_image"),"D");
                QuizQuestionScreenVO quizQuestionScreenVO4 = new QuizQuestionScreenVO(quizQuestionArray.getJSONObject(position).getString("option5"),quizQuestionArray.getJSONObject(position).getString("option5_image"),"E");
                temparr.add(quizQuestionScreenVO);
                temparr.add(quizQuestionScreenVO1);
                temparr.add(quizQuestionScreenVO2);
                temparr.add(quizQuestionScreenVO3);
                temparr.add(quizQuestionScreenVO4);
                //Utils.showToast(getApplicationContext(),temparr.toString());
                answerListView.invalidateViews();
                answerListViewAdapter = new AnswerListViewAdapter(AssesmentNewQuestionsScreen.this, temparr,"1");
                answerListView.setAdapter(answerListViewAdapter);
                answerListViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {

    }

    protected void postQuizSubmit()
    {
        setProgressBarIndeterminateVisibility(true);


        // maxTime.add(quizQuestionArray.getJSONObject(i).getString("question_time"));
//                            timeTaken.add(quizQuestionArray.getJSONObject(i).getString("time_taken"));
        try {
            for(int i=0;i<quizQuestionArray.length();i++)
            {
                choosenOption.add(quizQuestionArray.getJSONObject(i).getString("choosen_option"));
                topic.add(quizQuestionArray.getJSONObject(i).getString("topic_id"));
                subTopic.add(quizQuestionArray.getJSONObject(i).getString("sub_topic_id"));
                quesDifficultyLevel.add(quizQuestionArray.getJSONObject(i).getString("difficulty_level"));
                correct_option.add(quizQuestionArray.getJSONObject(i).getString("right_option"));
                Log.d("choosen_for_loop",quizQuestionArray.getJSONObject(i).getString("question_id"));
                // Log.d("choosen_for_loop",quizQuestionArray.getJSONObject(i).getString("choosen_option"));
                booleanAnswer.add(quizQuestionArray.getJSONObject(i).getString("is_correct"));
                questionId.add(quizQuestionArray.getJSONObject(i).getString("question_id")+"-"+quizQuestionArray.getJSONObject(i).getString("choosen_option"));
                Log.d("ko",quizQuestionArray.getJSONObject(i).getString("question_id")+"-"+quizQuestionArray.getJSONObject(i).getString("choosen_option"));
                if(quizQuestionArray.getJSONObject(i).getString("is_correct").matches("1"))
                {
                    isCorrect++;
                }
                else
                {

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();



        final ProgressDialog loading = ProgressDialog.show(this, "Submitting Quiz", "Please wait...", false, false);
        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.ALLOTED_TEST_SUBMITTED_SERVICE;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                // Utils.showToast(getApplicationContext(), ""+response);
                // System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);



                    if(responseObject.has("responseObject")&&responseObject.getString("responseObject").equals("1"))
                    {
                        loading.dismiss();

                        Utils.showToast(getApplicationContext(), AppConstants.MESSAGES.REGISTERED_SUCCESSFULLY);

                        Preferences.getInstance().loadPreference(getApplicationContext());
                        // Preferences.getInstance().historyId = responseObject.getJSONArray("responseObject").getJSONObject(0).getString("history_id");
                        Preferences.getInstance().savePreference(getApplicationContext());
                        int position=0;

                        finish();









                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), AppConstants.MESSAGES.SOMETHING_WENT_WRONG, Toast.LENGTH_LONG).show();
                }
                setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();

                Toast.makeText(getApplicationContext(), AppConstants.MESSAGES.SOMETHING_WENT_WRONG, Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                String timeTaken1,maxTime1,booleanAnswer1,choosenOption1,questionId1;
                timeTaken1 = timeTaken.toString();
                maxTime1= maxTime.toString();
                booleanAnswer1 = booleanAnswer.toString();
                choosenOption1 = choosenOption.toString();
                questionId1 = questionId.toString();
                topicId1 = topic.toString();
                subTopic1 = subTopic.toString();
                correct_option1 = correct_option.toString();
                quesDifficultyLevel1 = quesDifficultyLevel.toString();

                /*Log.d("q_id",questionId1.substring(1,questionId1.length()-1));
                Log.d("c_id",choosenOption1.substring(1,choosenOption1.length()-1));
                Log.d("b_id",booleanAnswer1.substring(1,booleanAnswer1.length()-1));*/


                Map<String,String> params = new HashMap<String, String>();

                //  params.put("Percents",percents);
                // params.put("timeTaken",timeTaken1.substring(1,timeTaken1.length()-1));
                //params.put("maxTime",maxTime1.substring(1,maxTime1.length()-1));
                //params.put("action_for","ins_answer");
                params.put("test_type","Quiz");
                params.put("total_question",String.valueOf(totalQuestions));
                params.put("total_right_answer",String.valueOf(isCorrect));
                //params.put("topic",topicId);
                //params.put("subject_id",subjectId);
                params.put("user_email",Preferences.getInstance().userEmailId);
                params.put("device_type","A");
                params.put("question_id",questionId1.substring(1,questionId1.length()-1));
                // params.put("categories",array2);
                params.put("choosen_option",choosenOption1.substring(1,choosenOption1.length()-1));
                params.put("is_correct",booleanAnswer1.substring(1,booleanAnswer1.length()-1));
                params.put("user_id", Preferences.getInstance().userId);

                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);

                Log.d("test_type","Quiz");
                Log.d("total_question",String.valueOf(totalQuestions));
                Log.d("total_right_answer",String.valueOf(isCorrect));
                //params.put("topic",topicId);
                //params.put("subject_id",subjectId);
                Log.d("user_email",Preferences.getInstance().userEmailId);
                Log.d("device_type","A");
                Log.d("question_id",questionId1.substring(1,questionId1.length()-1));
                // params.put("categories",array2);
                Log.d("choosen_option",choosenOption1.substring(1,choosenOption1.length()-1));
                Log.d("is_correct",booleanAnswer1.substring(1,booleanAnswer1.length()-1));
                Log.d("user_id", Preferences.getInstance().userId);

                Log.d("device_id",Preferences.getInstance().deviceId);
                Log.d("ins_id",Preferences.getInstance().institutionId);
                Log.d("sch_id",Preferences.getInstance().schoolId);

                try {
                    params.put("cls_id",quizQuestionArray.getJSONObject(0).getString("class_id"));
                    params.put("subject_id",quizQuestionArray.getJSONObject(0).getString("subject_id"));
                    params.put("test_id",test_id);
                    params.put("student_id",Preferences.getInstance().studentId);
                    params.put("topic",topicId1.substring(1,topicId1.length()-1));
                    params.put("sub_topic",subTopic1.substring(1,subTopic1.length()-1));
                    params.put("response_time","1, 3");
                    params.put("stu_response",questionId1.substring(1,questionId1.length()-1));
                    params.put("test_difficulty_level","1, 4");
                    params.put("ques_difficulty_level",quesDifficultyLevel1.substring(1,quesDifficultyLevel1.length()-1));
                    params.put("correct_option",correct_option1.substring(1,correct_option1.length()-1));

                    Log.d("cls_id",quizQuestionArray.getJSONObject(0).getString("class_id"));
                    Log.d("subject_id",quizQuestionArray.getJSONObject(0).getString("subject_id"));
                    Log.d("test_id",test_id);
                    Log.d("student_id",Preferences.getInstance().studentId);
                    Log.d("topic",topicId1.substring(1,topicId1.length()-1));
                    Log.d("sub_topic",subTopic1.substring(1,subTopic1.length()-1));
                    Log.d("response_time","1, 3");
                    Log.d("stu_response",questionId1.substring(1,questionId1.length()-1));
                    Log.d("test_difficulty_level","1, 4");
                    Log.d("ques_difficulty_level",quesDifficultyLevel1.substring(1,quesDifficultyLevel1.length()-1));
                    Log.d("correct_option",correct_option1.substring(1,correct_option1.length()-1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }
}
