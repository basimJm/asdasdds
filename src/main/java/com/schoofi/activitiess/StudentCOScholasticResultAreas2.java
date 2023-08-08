package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.schoofi.adapters.StudentCOScholasticResult2AAdapter;
import com.schoofi.adapters.StudentCOScholasticResult2BAdapter;
import com.schoofi.adapters.StudentCOScholasticResult2CAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentCOScholasticResultAreas2 extends AppCompatActivity {

    ImageView back;
    private ListView studentCOScholasticResult2BListView,studentCOScholasticResult2CListView;
    StudentCOScholasticResult2BAdapter studentCOScholasticResult2BAdapter;
    StudentCOScholasticResult2CAdapter studentCOScholasticResult2CAdapter;
    private JSONArray studentCOScholasticResult2CArray,studentCOScholasticResult2BArray,jsArray,jsArray1;
    String termId;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_coscholastic_result_areas2);

        studentCOScholasticResult2BListView = (ListView) findViewById(R.id.listView_co_scholastic2B);
        studentCOScholasticResult2CListView = (ListView) findViewById(R.id.listView_co_scholastic2C);
        termId = getIntent().getStringExtra("termId");

        next = (Button) findViewById(R.id.btn_next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentCOScholasticResultAreas2.this,StudentCOScholasticResultArea3.class);
                intent.putExtra("termId",termId);
                startActivity(intent);
            }
        });

        initData4();
        initData5();

    }


    private void initData4()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_CO_SCHOLASTIC_RESULTS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&stu_id="+Preferences.getInstance().studentId);
            if(e == null)
            {
                studentCOScholasticResult2BArray= null;
            }
            else
            {
                studentCOScholasticResult2BArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentCOScholasticResult2BArray!= null)
        {
            try {
                ArrayList<String> list = new ArrayList<String>();

                for(int i=0;i<studentCOScholasticResult2BArray.length();i++)
                {

                    if(studentCOScholasticResult2BArray.getJSONObject(i).getString("grading_area").matches("Work Education"))
                    {
                        list.add(studentCOScholasticResult2BArray.get(i).toString());
                    }
                    String list1= list.toString();
                    String list2 = "{"+'"'+"Result"+'"'+":"+list1+"}";
                    JSONObject jsonObject = new JSONObject(list2);
                    jsArray = jsonObject.getJSONArray("Result");
                    studentCOScholasticResult2BListView.invalidateViews();
                    studentCOScholasticResult2BAdapter= new StudentCOScholasticResult2BAdapter(StudentCOScholasticResultAreas2.this,jsArray);
                    studentCOScholasticResult2BListView.setAdapter(studentCOScholasticResult2BAdapter);
                    studentCOScholasticResult2BAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }


    private void initData5()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_CO_SCHOLASTIC_RESULTS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&stu_id="+Preferences.getInstance().studentId);
            if(e == null)
            {
                studentCOScholasticResult2CArray= null;
            }
            else
            {
                studentCOScholasticResult2CArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentCOScholasticResult2CArray!= null)
        {
            try {
                ArrayList<String> list3 = new ArrayList<String>();

                for(int i=0;i<studentCOScholasticResult2CArray.length();i++)
                {

                    if(studentCOScholasticResult2CArray.getJSONObject(i).getString("grading_area").matches("Work Education"))
                    {
                        list3.add(studentCOScholasticResult2CArray.get(i).toString());
                    }
                    String list4= list3.toString();
                    String list5 = "{"+'"'+"Result"+'"'+":"+list4+"}";
                    JSONObject jsonObject = new JSONObject(list5);
                    jsArray1 = jsonObject.getJSONArray("Result");
                    studentCOScholasticResult2CListView.invalidateViews();
                    studentCOScholasticResult2CAdapter= new StudentCOScholasticResult2CAdapter(StudentCOScholasticResultAreas2.this,jsArray1);
                    studentCOScholasticResult2CListView.setAdapter(studentCOScholasticResult2CAdapter);
                    studentCOScholasticResult2CAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }


}
