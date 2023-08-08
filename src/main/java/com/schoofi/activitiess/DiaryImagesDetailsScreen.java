package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mzelzoghbi.zgallery.ZGallery;
import com.mzelzoghbi.zgallery.entities.ZColor;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

import java.util.ArrayList;
import java.util.Arrays;

public class DiaryImagesDetailsScreen extends AppCompatActivity {

    ArrayList<String> myList;
    ArrayList<String> myList1;
    String array;
    ArrayList aList= new ArrayList();
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Diary Images Details Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_diary_images_details_screen);
        //myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
        position = getIntent().getExtras().getInt("position");
        array = getIntent().getStringExtra("array");

        aList = new ArrayList<String>(Arrays.asList(array.split(",")));

        myList1 = new ArrayList<String>();

        for(int i = 0;i<aList.size();i++)
        {
            myList1.add(AppConstants.SERVER_URLS.IMAGE_URL+aList.get(i));
            //System.out.println(myList1.get(i));
        }



        ZGallery.with(this,myList1)
                .setToolbarTitleColor(ZColor.WHITE) // toolbar title color
                .setGalleryBackgroundColor(ZColor.WHITE).setSelectedImgPosition(position) // activity background color
                .setToolbarColorResId(R.color.colorPrimary) // toolbar color
                .setTitle("Zak Gallery") // toolbar title
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DiaryImagesDetailsScreen.this,SchoolDiaryMainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(DiaryImagesDetailsScreen.this,SchoolDiaryMainScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
