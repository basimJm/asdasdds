package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.FeedbackVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminAdmissionUpdateStatus extends AppCompatActivity {

    private EditText message;
    private ImageView back;
    private Spinner status;
    private Button submit;
    JSONObject jsonobject;
    JSONArray jsonarray;
    String message1;
    ProgressDialog mProgressDialog;
    ArrayList<String> types;
    ArrayList<FeedbackVO> code;
    String codeId;
    String req_ref_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_admission_update_status);

        message = (EditText) findViewById(R.id.edit_message);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        status = (Spinner) findViewById(R.id.spin_status_type);
        submit = (Button) findViewById(R.id.btn_submit);
        req_ref_id = getIntent().getStringExtra("req_ref_id");

        new DownloadJSON().execute();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             postMessage();
            }
        });


    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            code = new ArrayList<FeedbackVO>();
            types = new ArrayList<String>();
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.STUDENT_FEEDBACK_TYPE+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId+"&value="+"1"+"&sch_id="+Preferences.getInstance().schoolId);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("Typelist");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    FeedbackVO feedbackVO = new FeedbackVO();
                    feedbackVO.setCode(jsonobject.optString("code"));
                    code.add(feedbackVO);
                    types.add(jsonobject.optString("full_name"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            status
                    .setAdapter(new ArrayAdapter<String>(AdminAdmissionUpdateStatus.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            types));

            status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub

                    codeId = code.get(position).getCode().toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }
    }

    private void postMessage()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.UPDATE_ADDMISSION_ENQUIRY;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(AdminAdmissionUpdateStatus.this,"Error Submitting Comment");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(AdminAdmissionUpdateStatus.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(AdminAdmissionUpdateStatus.this,"Admission status submitted successfully");
                        finish();

                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AdminAdmissionUpdateStatus.this, "Error submitting alert! Please try after sometime.");
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(AdminAdmissionUpdateStatus.this, "Error submitting alert! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(AdminAdmissionUpdateStatus.this);
                Map<String,String> params = new HashMap<String, String>();

                params.put("value","4");
                params.put("req_ref_id",req_ref_id);
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("conf_detail",message.getText().toString());
                params.put("conf_status",codeId);

                return params;
            }};

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
