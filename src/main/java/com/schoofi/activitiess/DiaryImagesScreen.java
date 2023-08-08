package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.StudentFeedBackGridAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

import java.util.ArrayList;
import java.util.Arrays;

public class DiaryImagesScreen extends AppCompatActivity {

    ArrayList<String> myList;
    GridView studentFeedBackImagesGrid;
    StudentFeedBackGridAdapter studentFeedBackGridAdapter;
    ImageView back,share;
    ArrayList<String> myList1;
    String value,param;
    int position;
    String array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Diary Images Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_grid_image_details);

        studentFeedBackImagesGrid = (GridView) findViewById(R.id.studentHomeGridView);

        array = getIntent().getStringExtra("array");
        String array1 = array.substring(1, array.length()-1);

        myList = new ArrayList<String>(Arrays.asList(array1.split(",")));




        share = (ImageView) findViewById(R.id.img_upload);
        share.setVisibility(View.GONE);

        myList1 = new ArrayList<String>();

        for(int i=0;i<myList.size();i++)
        {
            myList1.add(AppConstants.SERVER_URLS.IMAGE_URL+myList.get(i));
        }

        studentFeedBackGridAdapter = new StudentFeedBackGridAdapter(getApplicationContext(), myList);
        studentFeedBackImagesGrid.setAdapter(studentFeedBackGridAdapter);

        studentFeedBackImagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position1, long id) {
                // TODO Auto-generated method stub



                Intent intent = new Intent(DiaryImagesScreen.this,DiaryImagesDetailsScreen.class);
                intent.putExtra("array", myList);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DiaryImagesScreen.this,SchoolDiaryMainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(DiaryImagesScreen.this,SchoolDiaryMainScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
