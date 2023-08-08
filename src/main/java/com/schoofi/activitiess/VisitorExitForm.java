package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VisitorExitForm extends AppCompatActivity {

    private ImageView back;
    private TextView visitorName,visitorAddress,visitorExitTime,visitorEntryTime,visitorType,visitorWardName,visitorClassName,visitorAccessories,visitorVehicle,visitorPerson,visitorPurpose;
    private String visitorTypeId;
    private JSONArray chairmanAssigmentClassSectionWiseArray;
    String value,from1,to1;
    int position;
    String visitorId;
    private Button exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_visitor_exit_form);

        position = getIntent().getExtras().getInt("position");
        visitorName = (TextView) findViewById(R.id.text_visitor_name);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        visitorAddress = (TextView) findViewById(R.id.text_visitor_address);
        visitorType = (TextView) findViewById(R.id.text_visitor_type);
        visitorWardName = (TextView) findViewById(R.id.text_visitor_ward_name);
        visitorClassName = (TextView) findViewById(R.id.text_visitor_ward_class);
        visitorAccessories = (TextView) findViewById(R.id.text_visitor_accessories);
        visitorVehicle = (TextView) findViewById(R.id.text_visitor_vehicle);
        visitorEntryTime = (TextView) findViewById(R.id.text_visitor_entry);
        visitorExitTime = (TextView) findViewById(R.id.text_visitor_exit);
        visitorPerson = (TextView) findViewById(R.id.text_visitor_person);
        exit = (Button) findViewById(R.id.btn_exit);
        visitorPurpose = (TextView) findViewById(R.id.text_visitor_purpose);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postMessage();

            }
        });
        initData();
    }

    private void initData() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_MAIN_SERVICE + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&value=" + "4");
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
                visitorExitTime.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("exit_datetime"));
                visitorVehicle.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehical_type")+"-"+chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("vehicle_number"));
                visitorPerson.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_person"));
                visitorPurpose.setText(chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visit_reason"));
                visitorId = chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("visitor_id");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void postMessage()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_EXIT_FORM+"?visitor_id="+visitorId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId;

        StringRequest requestObject = new StringRequest(Request.Method.GET,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    System.out.println(response.toString());
                    System.out.println(url1);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(getApplicationContext(),"Entry Updated Successfully!");
                        //Intent intent = new Intent(PasswordReset.this,LoginScreen.class);

//                        startActivity(intent);
                        finish();

                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(),"Something Went Wrong");
                    }

                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(),"Session expired please login again!!!");
                    }


                    else

                    {

                        Utils.showToast(getApplicationContext(),"Error Occured");
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
			/*@Override
			protected Map<String,String> getParams(){

				Map<String,String> params = new HashMap<String, String>();

				params.put("mob",userMobile.getText().toString());
				params.put("email",userMobile.getText().toString());
				params.put("u_name", userName.getText().toString());
				params.put("dob", "2015-11-10");
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            setSupportProgressBarIndeterminateVisibility(false);
        }
    }
}
