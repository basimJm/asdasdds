package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ClassVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SectionVO;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VaccinationVO;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import smtchahal.materialspinner.MaterialSpinner;

public class AddVacinationDetails extends AppCompatActivity {

    private ImageView back;
    private MaterialSpinner materialSpinnerVaccinationName;
    private EditText vaccineName;
    private Button add;
    ArrayList<String> vaccinationName;
    ArrayList<VaccinationVO> vaccineId;

    JSONObject jsonobject;
    JSONArray jsonarray;
    String vaccineId1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_add_vacination_details);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        materialSpinnerVaccinationName = (MaterialSpinner) findViewById(R.id.spinner_vaccine);
        vaccineName = (EditText) findViewById(R.id.edit_vaccine_name);
        vaccineName.setVisibility(View.INVISIBLE);
        add = (Button) findViewById(R.id.btn_next);
        materialSpinnerVaccinationName.setBackgroundResource(R.drawable.grey_button);
        new DownloadJSON().execute();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vaccineId1.matches("0"))
                {
                    if(vaccineName.getText().toString().matches(""))
                    {
                        Utils.showToast(getApplicationContext(),"Please fill the name of vaccine!");
                    }

                    else
                    {
                        postMessage1();
                    }
                }

                else
                {
                    Intent intent = new Intent(AddVacinationDetails.this,AddVaccinationDate.class);
                    intent.putExtra("vaccine_id",vaccineId1);
                    startActivity(intent);
                }
            }
        });


    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            vaccineId = new ArrayList<VaccinationVO>();
            vaccinationName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VACCINE_DROPDOWNS+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("vaccines");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    VaccinationVO vaccinationVO = new VaccinationVO();

                    vaccinationVO.setVaccineId(jsonobject.optString("vaccine_id"));
                    vaccineId.add(vaccinationVO);

                    vaccinationName.add(jsonobject.optString("vaccine_name"));

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

            materialSpinnerVaccinationName
                    .setAdapter(new ArrayAdapter<String>(AddVacinationDetails.this,
                            android.R.layout.simple_spinner_dropdown_item,vaccinationName
                    ));

            materialSpinnerVaccinationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    vaccineId1 = vaccineId.get(position).getVaccineId().toString();
                    //System.out.println(groupId1);
                    if(vaccineId1.matches("0"))
                    {
                        vaccineName.setVisibility(View.VISIBLE);
                    }

                    else
                    {
                        vaccineName.setVisibility(View.INVISIBLE);
                    }





                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }


    private void postMessage1()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADD_VACCINE/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(AddVacinationDetails.this,"Error Submitting Vaccine");
                        loading.dismiss();


                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(AddVacinationDetails.this, "Session Expired:Please Login Again");
                        loading.dismiss();
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("2"))
                    {

                        Utils.showToast(AddVacinationDetails.this,"Vaccine Already Exists!");
                        loading.dismiss();
                        finish();
                    }

                    else {
                        loading.dismiss();
                        vaccineId1 = responseObject.getJSONObject("Msg").getString("vaccine_id").toString();
                        Intent intent = new Intent(AddVacinationDetails.this,AddVaccinationDate.class);
                        intent.putExtra("vaccine_id",vaccineId1);
                        startActivity(intent);
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AddVacinationDetails.this, "Error submitting alert! Please try after sometime.");
                    loading.dismiss();
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(AddVacinationDetails.this, "Error submitting alert! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(AddVacinationDetails.this);
                Map<String,String> params = new HashMap<String, String>();

                params.put("token",Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("vaccine",vaccineName.getText().toString());

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
            loading.dismiss();
        }
    }
}
