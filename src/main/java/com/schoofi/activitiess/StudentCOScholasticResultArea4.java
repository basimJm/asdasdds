package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.schoofi.adapters.StudentCOScholasticResult2BAdapter;
import com.schoofi.adapters.StudentCOScholasticResult2CAdapter;
import com.schoofi.adapters.StudentResultCOScholastic3AAdapter;
import com.schoofi.adapters.StudentResultCOScholastic3BAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentCOScholasticResultArea4 extends AppCompatActivity {

    ImageView back;
    private ListView studentCOScholasticResult3AListView,studentCOScholasticResult3BListView;
    StudentResultCOScholastic3AAdapter studentResultCOScholastic3AAdapter;
    StudentResultCOScholastic3BAdapter studentResultCOScholastic3BAdapter;
    private JSONArray studentCOScholasticResult3AArray,studentCOScholasticResult3BArray,jsArray,jsArray1;
    String termId;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_coscholastic_result_area4);

        studentCOScholasticResult3AListView = (ListView) findViewById(R.id.listView_co_scholastic3A);
        studentCOScholasticResult3BListView = (ListView) findViewById(R.id.listView_co_scholastic3B);
        termId = getIntent().getStringExtra("termId");

        next = (Button) findViewById(R.id.btn_next);

        if(termId.matches("OT"))
        {
            next.setVisibility(View.VISIBLE);
        }

        else
        {
            next.setVisibility(View.GONE);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(termId.matches("OT")) {
                    Intent intent = new Intent(StudentCOScholasticResultArea4.this, StudentResultCOScholasticArea5.class);
                    intent.putExtra("termId", termId);
                    startActivity(intent);
                }

                else
                {
                    Log.d("hhh","kkk");
                }
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
                studentCOScholasticResult3AArray= null;
            }
            else
            {
                studentCOScholasticResult3AArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentCOScholasticResult3AArray!= null)
        {
            try {
                ArrayList<String> list = new ArrayList<String>();

                for(int i=0;i<studentCOScholasticResult3AArray.length();i++)
                {

                    if(studentCOScholasticResult3AArray.getJSONObject(i).getString("grading_area").matches("Co-Curriculur Activities"))
                    {
                        list.add(studentCOScholasticResult3AArray.get(i).toString());
                    }
                    String list1= list.toString();
                    String list2 = "{"+'"'+"Result"+'"'+":"+list1+"}";
                    JSONObject jsonObject = new JSONObject(list2);
                    jsArray = jsonObject.getJSONArray("Result");
                    studentCOScholasticResult3AListView.invalidateViews();
                    studentResultCOScholastic3AAdapter= new StudentResultCOScholastic3AAdapter(StudentCOScholasticResultArea4.this,jsArray);
                    studentCOScholasticResult3AListView.setAdapter(studentResultCOScholastic3AAdapter);
                    studentResultCOScholastic3AAdapter.notifyDataSetChanged();

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
                studentCOScholasticResult3BArray= null;
            }
            else
            {
                studentCOScholasticResult3BArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentCOScholasticResult3BArray!= null)
        {
            try {
                ArrayList<String> list3 = new ArrayList<String>();

                for(int i=0;i<studentCOScholasticResult3BArray.length();i++)
                {

                    if(studentCOScholasticResult3BArray.getJSONObject(i).getString("grading_area").matches("Health & Physical Education"))
                    {
                        list3.add(studentCOScholasticResult3BArray.get(i).toString());
                    }
                    String list4= list3.toString();
                    String list5 = "{"+'"'+"Result"+'"'+":"+list4+"}";
                    JSONObject jsonObject = new JSONObject(list5);
                    jsArray1 = jsonObject.getJSONArray("Result");
                    studentCOScholasticResult3BListView.invalidateViews();
                    studentResultCOScholastic3BAdapter= new StudentResultCOScholastic3BAdapter(StudentCOScholasticResultArea4.this,jsArray1);
                    studentCOScholasticResult3BListView.setAdapter(studentResultCOScholastic3BAdapter);
                    studentResultCOScholastic3BAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }
}
