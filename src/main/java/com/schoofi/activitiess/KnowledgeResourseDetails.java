package com.schoofi.activitiess;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.schoofi.adapters.KnowledgeResourseSubjectScreenAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

public class KnowledgeResourseDetails extends AppCompatActivity {

    private ImageView back,fileIcon;
    private TextView title,description;
    private int position;
    private JSONArray healthAndAuditTaskListArray;
    String subjectId = "",classId="";
    int oi=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_knowledge_resourse_details);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        position = getIntent().getExtras().getInt("position");
        subjectId = getIntent().getStringExtra("subject_id");
        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
        {

        }
        else
        {
            classId = getIntent().getStringExtra("class_id");
        }

        title = (TextView) findViewById(R.id.text_leave_detail_leave_starting_date1);
        description = (TextView) findViewById(R.id.text_leave_detail_leave_description1);

        fileIcon = (ImageView) findViewById(R.id.image_icon);


        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
        {
            initData();
           // getClassList();
            Log.d("oiu",Preferences.getInstance().userRoleId);
        }

        else
        {
            initData1();
            //getClassList1();
        }

        fileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+healthAndAuditTaskListArray.getJSONObject(position).getString("file_path")));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
        {
            initData();
            // getClassList();
            Log.d("oiu",Preferences.getInstance().userRoleId);
        }

        else
        {
            initData1();
            //getClassList1();
        }
    }




    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_LIST+"?stu_id="+ Preferences.getInstance().studentId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&class_id="+Preferences.getInstance().studentClassId+"&subject_id="+subjectId);
            if(e == null)
            {
                Log.d("ppp","oooo");
                healthAndAuditTaskListArray= null;
            }
            else
            {
                healthAndAuditTaskListArray= new JSONArray(new String(e.data));
                Log.d("ppp","oooo1");
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(healthAndAuditTaskListArray!= null)
        {
            try {
                title.setText(healthAndAuditTaskListArray.getJSONObject(position).getString("title"));
                description.setText(healthAndAuditTaskListArray.getJSONObject(position).getString("description"));
                if(healthAndAuditTaskListArray.getJSONObject(position).getString("file_path").matches(""))
                {
                    fileIcon.setImageResource(R.drawable.cameracross);
                    oi=0;
                }
                else
                {
                    fileIcon.setImageResource(R.drawable.camera);
                    oi=1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void initData1() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ASSESMENT_KNOWLEDGE_RESOURSE_LIST + "?token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&sch_id=" + Preferences.getInstance().schoolId + "&class_number=" + classId + "&subject_id=" + subjectId);
            if (e == null) {
                healthAndAuditTaskListArray = null;
            } else {
                healthAndAuditTaskListArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (healthAndAuditTaskListArray != null) {
            try {
                title.setText(healthAndAuditTaskListArray.getJSONObject(position).getString("title"));
                description.setText(healthAndAuditTaskListArray.getJSONObject(position).getString("description"));
                if (healthAndAuditTaskListArray.getJSONObject(position).getString("file_path").matches("")) {
                    fileIcon.setImageResource(R.drawable.cameracross);
                    oi = 0;
                } else {
                    fileIcon.setImageResource(R.drawable.camera);
                    oi = 1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
