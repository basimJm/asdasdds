package com.schoofi.activitiess;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.AssignmentDetailAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class HealthAndAuditUserSubTaskListDetails extends AppCompatActivity {

    private Context mContext;
    ArrayList<String> myList;
    GridView studentFeedBackImagesGrid;
    AssignmentDetailAdapter assignmentDetailAdapter;
    ImageView back;
    JSONArray healthAndAuditUserSubTaskListArray;
    private int position;
    String taskId,uniqueId,array;
    ArrayList aList= new ArrayList();
    TextView status,details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Health And Audit User SubTaskListDetails");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_health_and_audit_user_sub_task_list_details);

        back = (ImageView) findViewById(R.id.img_back);

        position = getIntent().getExtras().getInt("position");
        taskId = getIntent().getStringExtra("task_id");
        uniqueId = getIntent().getStringExtra("unique_id");
        status = (TextView) findViewById(R.id.text_status);
        details = (TextView) findViewById(R.id.text_details);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        status.setVisibility(View.GONE);

        studentFeedBackImagesGrid = (GridView) findViewById(R.id.studentHomeGridView);

        myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(HealthAndAuditUserSubTaskListDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_USER_SUBTASK_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"1"+"&task_id="+taskId+"&unique_id="+uniqueId);
            healthAndAuditUserSubTaskListArray= new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditUserSubTaskListArray!= null)
        {
            try {
                    status.setText(healthAndAuditUserSubTaskListArray.getJSONObject(position).getString("status"));
                    details.setText(healthAndAuditUserSubTaskListArray.getJSONObject(position).getString("audit_report"));
                    array = healthAndAuditUserSubTaskListArray.getJSONObject(position).getString("audit_report_pic");
                    if(array.matches("") || array.matches("null"))
                    {
                        Log.d("harsh","jhhh");
                    }

                    else
                    {
                        aList = new ArrayList<String>(Arrays.asList(array.split(",")));
                        myList = new ArrayList<String>(aList);
                        assignmentDetailAdapter = new AssignmentDetailAdapter(getApplicationContext(), myList);

                        studentFeedBackImagesGrid.setAdapter(assignmentDetailAdapter);
                    }

                studentFeedBackImagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub

                        if(myList.get(position).endsWith("DOC") || myList.get(position).endsWith("doc") || myList.get(position).endsWith("Doc") || myList.get(position).endsWith("docx") || myList.get(position).endsWith("Docx") || myList.get(position).endsWith("DOCX") || myList.get(position).endsWith("pdf") || myList.get(position).endsWith("PDF") || myList.get(position).endsWith("Pdf") || myList.get(position).endsWith("txt") || myList.get(position).endsWith("Txt") || myList.get(position).endsWith("TXT"))
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.SERVER_URL+myList.get(position)));
                            startActivity(intent);
                        }

                        else
                        {
                            Intent intent = new Intent(HealthAndAuditUserSubTaskListDetails.this,TeacherStudentImageDetails.class);
                            intent.putExtra("imageUrl", myList.get(position));
                            startActivity(intent);
                        }



                    }
                });









            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
