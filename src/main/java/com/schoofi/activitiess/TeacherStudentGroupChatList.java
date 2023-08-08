package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.ChatMainAdapter;
import com.schoofi.adapters.StudentChatAdapter;
import com.schoofi.adapters.TeacherStudentGroupChatListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ChatMainVO;
import com.schoofi.utils.ChatVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.stfalcon.chatkit.messages.MessageInput;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TeacherStudentGroupChatList extends AppCompatActivity implements MessageInput.InputListener{

    RecyclerView expandableListView;
    private ChatMainAdapter assignmentMultiLevelListAdapter;
    private JSONArray AssignmentArray;
    private ArrayList<ChatMainVO> diaryVOs;
    private ArrayList<ChatVO> assignmentMultiLevelVOs;
    JSONObject jsonObject,jsonObject1;
    JSONArray jsonArray;
    String recipient_id,type;
    private ImageView plus,back;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.chat_main_layout);

        recipient_id= getIntent().getStringExtra("recipient_id");
        type = getIntent().getStringExtra("type");



        expandableListView = (RecyclerView) findViewById(R.id.listViewInnerAllAssignment);





        getStudentFeedList();

        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });















    }


    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(TeacherStudentGroupChatList.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_DIARY_CHAT_SERVICE+"?token="+ Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&from_user_id="+Preferences.getInstance().userId+"&recipient="+recipient_id+"&type="+type;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //  System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Chat Found");
                        expandableListView.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        expandableListView.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        expandableListView.setVisibility(View.VISIBLE);
                        AssignmentArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=AssignmentArray && AssignmentArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = AssignmentArray.toString().getBytes();
                            diaryVOs = new ArrayList<ChatMainVO>();
                            VolleySingleton.getInstance(TeacherStudentGroupChatList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_DIARY_CHAT_SERVICE+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&from_user_id="+Preferences.getInstance().userId+"&recipient="+recipient_id+"&type="+type,e);


                            for(int i=0;i<AssignmentArray.length();i++)
                            {
                                ChatMainVO diaryVO = new ChatMainVO();
                                diaryVO.setDate(AssignmentArray.getJSONObject(i).getString("date"));
                                jsonObject = AssignmentArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");

                                assignmentMultiLevelVOs = new ArrayList<ChatVO>();

                                for(int j=0;j<jsonArray.length();j++)
                                {
                                    jsonObject1 = jsonArray.getJSONObject(j);
                                    ChatVO assignmentMultiLevelVO = new ChatVO();

                                    assignmentMultiLevelVO.setMessage(jsonObject1.getString("msg"));
                                    assignmentMultiLevelVO.setTime(jsonObject1.getString("date_time"));
                                    assignmentMultiLevelVO.setUser_id(jsonObject1.getString("from_user_id"));
                                    assignmentMultiLevelVO.setUserName(jsonObject1.getString("from_user_name"));
                                    assignmentMultiLevelVO.setIsvisible(jsonObject1.getString("visible_to_all"));
                                    assignmentMultiLevelVO.setIsvisible(jsonObject1.getString("id"));
                                    if(jsonObject1.getString("chat_type").matches("null") || jsonObject1.getString("chat_type").matches(""))
                                    {
                                        assignmentMultiLevelVO.setChatType("Group");
                                    }
                                    else
                                    {
                                        assignmentMultiLevelVO.setChatType("Personal");
                                    }
                                    assignmentMultiLevelVOs.add(assignmentMultiLevelVO);
                                }
                                diaryVO.setItems(assignmentMultiLevelVOs);
                                diaryVOs.add(diaryVO);
                            }

                            assignmentMultiLevelListAdapter = new ChatMainAdapter(jsonArray,TeacherStudentGroupChatList.this,diaryVOs);
                            expandableListView.setLayoutManager(new LinearLayoutManager(TeacherStudentGroupChatList.this, RecyclerView.VERTICAL, false));
                            expandableListView.setAdapter(assignmentMultiLevelListAdapter);

                           // assignmentMultiLevelListAdapter.notifyDataSetChanged();
                            expandableListView.scrollToPosition(diaryVOs.size()-1);



                        }
                    }
                    else {
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                        expandableListView.setVisibility(View.GONE);
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    expandableListView.setVisibility(View.GONE);

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                expandableListView.setVisibility(View.GONE);

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
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
            expandableListView.setVisibility(View.GONE);
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    private void scrollToEnd() {
        expandableListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                expandableListView.smoothScrollToPosition(AssignmentArray.length() - 1);
                Log.d("ppppp","oooooo");
            }
        });
    }

    @Override
    public boolean onSubmit(CharSequence input) {

        Log.d("ppp",input.toString());
        postAttendance(input.toString());
        return true;
    }


    protected void postAttendance(final String input)
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.STUDENT_DIARY_SUBMIT_SERVICE;
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);

                try
                {

                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        //Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Intent mIntent = new Intent(TeacherStudentGroupChatList.this,TeacherStudentGroupChatList.class);
                        mIntent.putExtra("recipient_id",recipient_id);
                        mIntent.putExtra("type",type);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mIntent);
                        overridePendingTransition(0,0); //0 for no animation
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
                    Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_LONG).show();
                }
                setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                //.getInstance().loadPreference(TeacherStudentAttendanceDetails.this);
                Map<String,String> params = new HashMap<String, String>();


                params.put("from_user_id",Preferences.getInstance().userId);
                params.put("recipient",recipient_id);


                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                params.put("date_time",currentDate);
                params.put("type",type);
                params.put("msg",input);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }


}
