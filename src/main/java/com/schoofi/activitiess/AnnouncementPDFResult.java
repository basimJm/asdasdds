package com.schoofi.activitiess;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.constants.AppConstants;

public class AnnouncementPDFResult extends AppCompatActivity {

    private Button viewAnnouncement;
    private TextView title,gropp,description,date;
    ImageView back;
    String url,title1,group,description1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_announcement_pdfresult);

        viewAnnouncement = (Button) findViewById(R.id.btn_view);
        back = (ImageView) findViewById(R.id.img_back);
        title = (TextView) findViewById(R.id.text_studentImageViewScreenTitle);
        gropp = (TextView) findViewById(R.id.text_studentImageViewScreenSenderName);
        description = (TextView) findViewById(R.id.text_studentImageViewScreenDescription);
        date = (TextView) findViewById(R.id.text_studentImageViewScreenDate);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        url = getIntent().getStringExtra("url");
        Log.d("p",url);
        if(url.matches(""))
        {
            viewAnnouncement.setVisibility(View.GONE);
        }
        else
        {
            viewAnnouncement.setVisibility(View.VISIBLE);
        }
        title1 = getIntent().getStringExtra("title");
       // group = getIntent().getStringExtra("group");
        description1 = getIntent().getStringExtra("description");

        title.setText(title1);
       // gropp.setText(group);
        description.setText(description1);

        date.setVisibility(View.INVISIBLE);
        gropp.setVisibility(View.GONE);

        viewAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+url));
                startActivity(intent);
            }
        });


    }
}
