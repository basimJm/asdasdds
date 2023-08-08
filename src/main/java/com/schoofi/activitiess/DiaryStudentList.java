package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.schoofi.adapters.DiaryStudentListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class DiaryStudentList extends AppCompatActivity {
    private ImageView back;
    private ListView studentListView;
    private DiaryStudentListAdapter employeeListAdapter;
    private String json;
    JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_diary_student_list);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        studentListView = (ListView) findViewById(R.id.listViewInnerAllAssignment);

        json = getIntent().getStringExtra("json");
        try {
             jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        studentListView.invalidateViews();
        employeeListAdapter= new DiaryStudentListAdapter(DiaryStudentList.this,jsonArray);
        studentListView.setAdapter(employeeListAdapter);
        employeeListAdapter.notifyDataSetChanged();


    }
}
