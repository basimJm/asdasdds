package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.schoofi.adapters.AdminVisitorLogPrimaryDetailsAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

public class AdminVisitorDetailsActivity extends AppCompatActivity {

    private ImageView back;
    private TextView visitorName,visitorAddress,visitorEntryTime,visitorType,visitorWardName,visitorClassName,visitorAccessories,visitorVehicle,visitorPerson,visitorPurpose;
    private String visitorTypeId;
    private JSONArray chairmanAssigmentClassSectionWiseArray;
    String value,from1,to1;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_visitor_details);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //visitorTypeId = getIntent().getStringExtra("visitor_type");
        position = getIntent().getExtras().getInt("position");
        value = getIntent().getStringExtra("value");
        if(value.matches("3")) {
            from1 = getIntent().getStringExtra("from");
            to1 = getIntent().getStringExtra("to");
        }
        else
        {

        }
        visitorName = (TextView) findViewById(R.id.text_visitor_name);
        visitorAddress = (TextView) findViewById(R.id.text_visitor_address);
        visitorType = (TextView) findViewById(R.id.text_visitor_type);
        visitorWardName = (TextView) findViewById(R.id.text_visitor_ward_name);
        visitorClassName = (TextView) findViewById(R.id.text_visitor_ward_class);
        visitorAccessories = (TextView) findViewById(R.id.text_visitor_accessories);
        visitorVehicle = (TextView) findViewById(R.id.text_visitor_vehicle);
        visitorEntryTime = (TextView) findViewById(R.id.text_visitor_entry);
        visitorPerson = (TextView) findViewById(R.id.text_visitor_person);
        visitorPurpose = (TextView) findViewById(R.id.text_visitor_purpose);

        if(value.matches("1"))
        {
            initData();
           // Utils.showToast(getApplicationContext(),"kkk");
        }

        else
            if(value.matches("2"))
            {
                initData2();
            }

            else
                if(value.matches("3"))
                {
                    initData3();
                  // Utils.showToast(getApplicationContext(),"kkk1");
                }

                else
                {
                        initData4();
                }


    }

    private void initData() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&visitor_type="+Preferences.getInstance().visitorType+ "&value=" + "1");
            if (e == null) {
                chairmanAssigmentClassSectionWiseArray = null;
            } else {
                chairmanAssigmentClassSectionWiseArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanAssigmentClassSectionWiseArray != null) {

            try {
                visitorName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_name"));
                String Address1,Accessories1,Vehicle1;
                /*Address1 = "<font color='#000000'>Address: </font>";
                Accessories1 = "<font color='#000000'>Accessories: </font>";
                Vehicle1 = "<font color='#000000'>Vehicle: </font>";*/
                visitorAddress.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_address"));
                visitorType.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_type_name"));
                visitorWardName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("stu_name"));
                visitorClassName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("class_name")+"-"+chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("section_name"));
                visitorAccessories.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_accessories"));
                visitorEntryTime.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("entry_datetime"));
                visitorVehicle.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehical_type")+"-"+chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehicle_number"));
                visitorPerson.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_person"));
                visitorPurpose.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_reason"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData2() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&visitor_type="+Preferences.getInstance().visitorType+ "&value=" + "2");
            if (e == null) {
                chairmanAssigmentClassSectionWiseArray = null;
            } else {
                chairmanAssigmentClassSectionWiseArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanAssigmentClassSectionWiseArray != null) {

            try {
                visitorName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_name"));
                String Address1,Accessories1,Vehicle1;
                /*Address1 = "<font color='#000000'>Address: </font>";
                Accessories1 = "<font color='#000000'>Accessories: </font>";
                Vehicle1 = "<font color='#000000'>Vehicle: </font>";*/
                visitorAddress.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_address"));
                visitorType.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_type_name"));
                visitorWardName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("stu_name"));
                visitorClassName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("class_name")+"-"+chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("section_name"));
                visitorAccessories.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_accessories"));
                visitorEntryTime.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("entry_datetime"));
                visitorVehicle.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehicle_type")+"-"+chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehicle_number"));
                visitorPerson.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_person"));
                visitorPurpose.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_reason"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData3() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&visitor_type="+Preferences.getInstance().visitorType+ "&value=" + "3"+"&from_date="+from1+"&to_date="+to1);
            if (e == null) {
                chairmanAssigmentClassSectionWiseArray = null;
            } else {
                chairmanAssigmentClassSectionWiseArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanAssigmentClassSectionWiseArray != null) {

            try {
                visitorName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_name"));
                String Address1,Accessories1,Vehicle1;
                /*Address1 = "<font color='#000000'>Address: </font>";
                Accessories1 = "<font color='#000000'>Accessories: </font>";
                Vehicle1 = "<font color='#000000'>Vehicle: </font>";*/
                visitorAddress.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_address"));
                visitorType.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_type_name"));
                visitorWardName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("stu_name"));
                visitorClassName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("class_name")+"-"+chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("section_name"));
                visitorAccessories.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_accessories"));
                visitorEntryTime.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("entry_datetime"));
                visitorVehicle.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehicle_type")+"-"+chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehicle_number"));
                visitorPerson.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_person"));
                visitorPurpose.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_reason"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData4() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&visitor_type="+Preferences.getInstance().visitorType+ "&value=" + "4"+"&from="+from1+"&o1="+to1);
            if (e == null) {
                chairmanAssigmentClassSectionWiseArray = null;
            } else {
                chairmanAssigmentClassSectionWiseArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanAssigmentClassSectionWiseArray != null) {

            try {
                visitorName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_name"));
                String Address1,Accessories1,Vehicle1;
               /* Address1 = "<font color='#000000'>Address: </font>";
                Accessories1 = "<font color='#000000'>Accessories: </font>";
                Vehicle1 = "<font color='#000000'>Vehicle: </font>";*/

                visitorAddress.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_address"));
                visitorType.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_type_name"));
                visitorWardName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("stu_name"));
                visitorClassName.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("class_name")+"-"+chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("section_name"));
                visitorAccessories.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_accessories"));
                visitorEntryTime.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("entry_datetime"));
                visitorVehicle.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehicle_type")+"-"+chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehicle_number"));
                visitorPerson.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_person"));
                visitorPurpose.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_reason"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
