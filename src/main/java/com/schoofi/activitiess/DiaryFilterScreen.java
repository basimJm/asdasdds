package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.schoofi.adapters.DiaryFilterScreenLeftAdapter;
import com.schoofi.adapters.DiaryMultiLevelListAdapter;
import com.schoofi.utils.DiaryFilterScreenLeftVO;

import java.util.ArrayList;

public class DiaryFilterScreen extends AppCompatActivity {

    private ListView leftListView,rightListView;
    private TextView toDate,fromDate;
    public ArrayList<DiaryFilterScreenLeftVO> temparr;
    DiaryFilterScreenLeftAdapter diaryFilterScreenLeftAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_diary_filter_screen);

        leftListView = (ListView) findViewById(R.id.list_first);
        rightListView = (ListView) findViewById(R.id.list_second);
        toDate = (TextView) findViewById(R.id.text_to_date);
        fromDate = (TextView) findViewById(R.id.text_from_date);

        temparr=new ArrayList<DiaryFilterScreenLeftVO>();

        DiaryFilterScreenLeftVO diaryFilterScreenLeftVO = new DiaryFilterScreenLeftVO("From","0");
        DiaryFilterScreenLeftVO diaryFilterScreenLeftVO1 = new DiaryFilterScreenLeftVO("To","1");
        DiaryFilterScreenLeftVO diaryFilterScreenLeftVO2 = new DiaryFilterScreenLeftVO("Subject","2");
        DiaryFilterScreenLeftVO diaryFilterScreenLeftVO3 = new DiaryFilterScreenLeftVO("Teacher","3");
        DiaryFilterScreenLeftVO diaryFilterScreenLeftVO4 = new DiaryFilterScreenLeftVO("Categories","4");

        temparr.add(diaryFilterScreenLeftVO);
        temparr.add(diaryFilterScreenLeftVO1);
        temparr.add(diaryFilterScreenLeftVO2);
        temparr.add(diaryFilterScreenLeftVO3);
        temparr.add(diaryFilterScreenLeftVO4);

        diaryFilterScreenLeftAdapter = new DiaryFilterScreenLeftAdapter(getApplicationContext(),temparr);
        leftListView.setAdapter(diaryFilterScreenLeftAdapter);
    }
}
