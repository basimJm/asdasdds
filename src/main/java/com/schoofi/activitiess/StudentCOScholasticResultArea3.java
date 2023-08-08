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
import com.schoofi.adapters.StudentCOScholasticResult2DAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentCOScholasticResultArea3 extends AppCompatActivity {

    ListView studentCOScholasticResultArea3ListView;
    StudentCOScholasticResult2DAdapter studentCOScholasticResult2DAdapter;
    ImageView back;
    String termId = "";
    JSONArray studentCOScholasticResultArea2DArray,jsArray;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_coscholastic_result_area3);

        back = (ImageView) findViewById(R.id.img_back);

        termId = getIntent().getStringExtra("termId");
        next = (Button) findViewById(R.id.btn_next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentCOScholasticResultArea3.this,StudentCOScholasticResultArea4.class);
                intent.putExtra("termId",termId);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        studentCOScholasticResultArea3ListView = (ListView) findViewById(R.id.listView_co_scholastic2D);

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
                studentCOScholasticResultArea2DArray= null;
            }
            else
            {
                studentCOScholasticResultArea2DArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentCOScholasticResultArea2DArray!= null)
        {
            try {
                ArrayList<String> list = new ArrayList<String>();

                for(int i=0;i<studentCOScholasticResultArea2DArray.length();i++)
                {

                    if(studentCOScholasticResultArea2DArray.getJSONObject(i).getString("grading_area").matches("Attitude and Values"))
                    {
                        list.add(studentCOScholasticResultArea2DArray.get(i).toString());
                    }
                    String list1= list.toString();
                    String list2 = "{"+'"'+"Result"+'"'+":"+list1+"}";
                    JSONObject jsonObject = new JSONObject(list2);
                    jsArray = jsonObject.getJSONArray("Result");
                    studentCOScholasticResultArea3ListView.invalidateViews();
                    studentCOScholasticResult2DAdapter= new StudentCOScholasticResult2DAdapter(StudentCOScholasticResultArea3.this,jsArray);
                    studentCOScholasticResultArea3ListView.setAdapter(studentCOScholasticResult2DAdapter);
                    studentCOScholasticResult2DAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }
}
