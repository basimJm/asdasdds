package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.schoofi.adapters.StudentCOScholasticResult2BAdapter;
import com.schoofi.adapters.StudentResultCOScholasticSelfAwarenessAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentResultCOScholasticArea5 extends AppCompatActivity {

    ImageView back;
    ListView selfAwarenessListView;
    StudentResultCOScholasticSelfAwarenessAdapter studentResultCOScholasticSelfAwarenessAdapter;
    private JSONArray jsArray,studentResultCOScholasticSelfAwarenessArray;
    String termId = "";
    Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_result_coscholastic_area5);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        next = (Button) findViewById(R.id.btn_next);



        selfAwarenessListView = (ListView) findViewById(R.id.listView_co_scholastic2A);

        termId = getIntent().getStringExtra("termId");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentResultCOScholasticArea5.this,StudentHealthStatusResult.class);
                intent.putExtra("termId",termId);
                startActivity(intent);
            }
        });
        initData4();

    }

    private void initData4()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_CO_SCHOLASTIC_RESULTS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&stu_id="+Preferences.getInstance().studentId);
            if(e == null)
            {
                studentResultCOScholasticSelfAwarenessArray= null;
            }
            else
            {
                studentResultCOScholasticSelfAwarenessArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentResultCOScholasticSelfAwarenessArray!= null)
        {
            try {
                ArrayList<String> list = new ArrayList<String>();

                for(int i=0;i<studentResultCOScholasticSelfAwarenessArray.length();i++)
                {

                    if(studentResultCOScholasticSelfAwarenessArray.getJSONObject(i).getString("grading_area").matches("Self Awareness"))
                    {
                        list.add(studentResultCOScholasticSelfAwarenessArray.get(i).toString());
                    }
                    String list1= list.toString();
                    String list2 = "{"+'"'+"Result"+'"'+":"+list1+"}";
                    JSONObject jsonObject = new JSONObject(list2);
                    jsArray = jsonObject.getJSONArray("Result");
                    selfAwarenessListView.invalidateViews();
                    studentResultCOScholasticSelfAwarenessAdapter= new StudentResultCOScholasticSelfAwarenessAdapter(StudentResultCOScholasticArea5.this,jsArray);
                    selfAwarenessListView.setAdapter(studentResultCOScholasticSelfAwarenessAdapter);
                    studentResultCOScholasticSelfAwarenessAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }
}
