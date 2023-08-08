package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.palette.graphics.Palette;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.DiaryMultiLevelListAdapter;
import com.schoofi.adapters.ParentUnpaidFeesNewMultilevelAdapter1;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DiarySubVO;
import com.schoofi.utils.DiaryVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SchoolDiaryMainScreen extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private JSONArray schoolDiaryArray;
    private ArrayList<DiaryVO> diaryVOs;
    private ArrayList<DiarySubVO> diarySubVOs;

    String profileUrl;
    Target target;
    JSONArray jsonArray;
    JSONObject jsonObject,jsonObject1;
    String diaryModuleId,teacherId,toDate,fromDate,subjectId;
    DiaryMultiLevelListAdapter diaryMultiLevelListAdapter;
    ImageView plus,back;
    final Context context = this;
    String diary_sub_Id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("School Diary Main Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_school_diary_main_screen);


        Preferences.getInstance().loadPreference(getApplicationContext());

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listView_diary);
        back = (ImageView) findViewById(R.id.img_back);
        plus = (ImageView) findViewById(R.id.img_plus);
       // context = getApplicationContext();

        postLocalRegistration();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.getInstance().loadPreference(getApplicationContext());
                if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6")) {
                    Intent intent = new Intent(SchoolDiaryMainScreen.this, ParentDiaryUploadScreenFirst.class);

                    startActivity(intent);
                }

                else
                {
                    Intent intent = new Intent(SchoolDiaryMainScreen.this, DiaryUploadActivity.class);

                    startActivity(intent);
                }
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //Utils.showToast(getApplicationContext(),diaryVOs.get(groupPosition).getItems().get(childPosition).getDescription());
                Intent intent = new Intent(SchoolDiaryMainScreen.this,SchoolDiaryDetailScreen.class);
                intent.putExtra("childPosition",childPosition);
                intent.putExtra("groupPosition",groupPosition);
                intent.putExtra("diaryModuleId",diaryVOs.get(groupPosition).getItems().get(childPosition).getDiaryModuleId());
                intent.putExtra("diarySubModuleId",diaryVOs.get(groupPosition).getItems().get(childPosition).getDiarySubId());
                intent.putExtra("type",diaryVOs.get(groupPosition).getItems().get(childPosition).getType());
                intent.putExtra("rating",diaryVOs.get(groupPosition).getItems().get(childPosition).getRating());
                intent.putExtra("file",diaryVOs.get(groupPosition).getItems().get(childPosition).getFileUrl());
                intent.putExtra("time",diaryVOs.get(groupPosition).getItems().get(childPosition).getTime());
                intent.putExtra("date1",diaryVOs.get(groupPosition).getDate());
                intent.putExtra("ratingParameter",diaryVOs.get(groupPosition).getItems().get(childPosition).getRatingParameter());
                intent.putExtra("attachment",diaryVOs.get(groupPosition).getItems().get(childPosition).getAttachment());

                startActivity(intent);
                return false;
            }
        });



        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    final int childPosition = ExpandableListView.getPackedPositionChild(id);

                    // You now have everything that you would as if this was an OnChildClickListener()
                    // Add your logic here.

                    // Return true as we are handling the event.
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.teacher_attendance_submit_dialog_box, null);



                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            // get user input and set it to result


                                            // edit text

                                          diary_sub_Id = diaryVOs.get(groupPosition).getItems().get(childPosition).getDiarySubId();
                                            postMessage();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                    return true;
                }

                return false;
            }
        });








    }

    @Override
    protected void onResume() {
        super.onResume();

        postLocalRegistration();
    }

    protected void postLocalRegistration()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.SCHOOL_DIARY_URL;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                // System.out.println(url1);


                try {
                    responseObject = new JSONObject(response);
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        loading.dismiss();
                    }
                    else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        loading.dismiss();
                    } else if (responseObject.has("responseObject")) {
                        loading.dismiss();

                        schoolDiaryArray = new JSONObject(response).getJSONArray("responseObject");
                       // profileUrl = schoolDiaryArray.getJSONObject(0).getString("school_banner");



                        if(null!=schoolDiaryArray && schoolDiaryArray.length()>=0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = schoolDiaryArray.toString().getBytes();
                            diaryVOs = new ArrayList<DiaryVO>();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_URL + "?token=" + Preferences.getInstance().token + "&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId, e);
                            expandableListView.invalidateViews();


                            for(int i=0;i<schoolDiaryArray.length();i++)
                            {
                                DiaryVO diaryVO = new DiaryVO();
                                diaryVO.setDate(schoolDiaryArray.getJSONObject(i).getString("crr_date"));
                                jsonObject = schoolDiaryArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");

                                diarySubVOs= new ArrayList<DiarySubVO>();
                                //Utils.showToast(getApplicationContext(),jsonArray.toString());
                                //Log.d("fff",jsonArray.toString());

                                for(int j=0;j<jsonArray.length();j++)
                                {
                                   jsonObject1 = jsonArray.getJSONObject(j);
                                   DiarySubVO diarySubVO = new DiarySubVO();
                                    diarySubVO.setDescription(jsonObject1.getString("description"));
                                    diarySubVO.setEmoticon(jsonObject1.getString("emoticon"));
                                    diarySubVO.setFileUrl(jsonObject1.getString("image"));
                                    diarySubVO.setInitials(jsonObject1.getString("initials"));
                                    diarySubVO.setProfileUrl(jsonObject1.getString("sender_picture"));
                                    diarySubVO.setRating(jsonObject1.getString("rating"));
                                    diarySubVO.setShareable(jsonObject1.getString("shareable"));
                                    diarySubVO.setSubject(jsonObject1.getString("subject"));
                                    diarySubVO.setTime(jsonObject1.getString("crr_time"));
                                    diarySubVO.setTitle(jsonObject1.getString("title"));
                                    diarySubVO.setDiaryModuleId(jsonObject1.getString("diary_module_id"));
                                    diarySubVO.setDiarySubId(jsonObject1.getString("diary_sub_id"));
                                    diarySubVO.setAttachment(jsonObject1.getString("file_type"));
                                    diarySubVO.setRoleId(jsonObject1.getString("role_id"));
                                    diarySubVO.setRatingParameter(jsonObject1.getString("rating_parameter"));
                                    diarySubVO.setType(jsonObject1.getString("type"));
                                    diarySubVOs.add(diarySubVO);
                                   // Utils.showToast(getApplicationContext(),"kkk");

                                }

                                diaryVO.setItems(diarySubVOs);
                                diaryVOs.add(diaryVO);


                            }

                            diaryMultiLevelListAdapter = new DiaryMultiLevelListAdapter(getApplicationContext(),diaryVOs,schoolDiaryArray);
                            expandableListView.setAdapter(diaryMultiLevelListAdapter);
                            diaryMultiLevelListAdapter.notifyDataSetChanged();
                        }
                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                    setProgressBarIndeterminateVisibility(false);
                    loading.dismiss();
                }


                // mParties = new String[]
                // dynamicToolbarColor();


                setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();

                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                /*if(diaryModuleId.matches("") || diaryModuleId.matches("null"))
                {
                    params.put("diary_module_id","A");
                }
                else {
                    params.put("diary_module_id", diaryModuleId);
                }

                if(subjectId.matches("") || subjectId.matches("null"))
                {
                    params.put("subject_id","A");
                }
                else {
                    params.put("subject_id", subjectId);
                }

                if(fromDate.matches("") || fromDate.matches("null"))
                {
                    params.put("from_date","");
                }

                else {
                    params.put("from_date", fromDate);
                }

                if(toDate.matches("") || toDate.matches("null"))
                {
                    params.put("to_date","");
                }

                else {
                    params.put("to_date", toDate);
                }

                if(teacherId.matches("") || teacherId.matches("null"))
                {
                    params.put("teacher_id",teacherId);
                }

                else {
                    params.put("teacher_id", teacherId);
                }*/
                //System.out.print(Preferences.getInstance().userId);




                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }

    private void postMessage()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_DELETE_URL+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token+"&diary_sub_id="+diary_sub_Id;

        StringRequest requestObject = new StringRequest(Request.Method.GET,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(getApplicationContext(),"Deleted Successfully!!!");
                        postLocalRegistration();
                    }


                    else

                    {

                        Utils.showToast(getApplicationContext(),"Error Occured");
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
			/*@Override
			protected Map<String,String> getParams(){

				Map<String,String> params = new HashMap<String, String>();

				params.put("mob",userMobile.getText().toString());
				params.put("email",userMobile.getText().toString());
				params.put("u_name", userName.getText().toString());
				params.put("dob", "2015-11-10");
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            setSupportProgressBarIndeterminateVisibility(false);
        }
    }
}
