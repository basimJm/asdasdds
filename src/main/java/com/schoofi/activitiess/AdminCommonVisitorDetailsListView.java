package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.bumptech.glide.Glide;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AdminCommonVisitorDetailsListView extends AppCompatActivity {

    private ImageView back,visitorImage;
    private String value;
    private JSONArray adminCommonVisitorListView;
    private int position;
    private TextView visitorName,visitorAddress,visitorEmail,visitorPhone,visitorType,visitorPersonToMeet,visitorPurpose,visitorStudent,visitorCardIssued;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.0){
            // 6.5inch device or bigger
            setContentView(R.layout.activity_admin_common_visitor_details_tablet_list_view);
        }else{
            // smaller device

            setContentView(R.layout.activity_admin_common_visitor_details_list_view);
        }


        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        value = getIntent().getStringExtra("value");
        position =getIntent().getExtras().getInt("position");

        visitorImage = (ImageView) findViewById(R.id.image_view_visitor);
        visitorName = (TextView) findViewById(R.id.text_visitor_name);
        visitorAddress = (TextView) findViewById(R.id.text_visitor_address);
        visitorCardIssued = (TextView) findViewById(R.id.text_card_id_issued);
        visitorEmail = (TextView) findViewById(R.id.text_email);
        visitorPhone = (TextView) findViewById(R.id.text_phone);
        visitorType = (TextView) findViewById(R.id.text_visitor_type);
        visitorStudent = (TextView) findViewById(R.id.text_studentName);
        visitorPersonToMeet = (TextView) findViewById(R.id.text_person_to_meet);
        visitorPurpose = (TextView) findViewById(R.id.text_purpose);

        if(value.matches("1"))
        {
          initData();
        }

        else
            if(value.matches("2"))
            {
                initData1();
            }

    }

    protected void initData()
    {
        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(AdminCommonVisitorDetailsListView.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT+"?device_id="+ Preferences.getInstance().deviceId);
            adminCommonVisitorListView= new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(adminCommonVisitorListView!= null)
        {
            try {





                Glide.with(AdminCommonVisitorDetailsListView.this).load(AppConstants.SERVER_URLS.IMAGE_URL+adminCommonVisitorListView.getJSONObject(position).getString("image_path")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(visitorImage);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    protected void initData1()
    {
        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(AdminCommonVisitorDetailsListView.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT+"?device_id="+ Preferences.getInstance().deviceId);
            adminCommonVisitorListView= new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(adminCommonVisitorListView!= null)
        {
            try {





                Glide.with(AdminCommonVisitorDetailsListView.this).load(AppConstants.SERVER_URLS.IMAGE_URL+adminCommonVisitorListView.getJSONObject(position).getString("image_path")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(visitorImage);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
