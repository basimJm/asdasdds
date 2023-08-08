package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.StudentLeaveListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class AdmissionEnquiryFinalScreen extends AppCompatActivity {

    ImageView back,optionImage1,optionImage2,optionImage3;
    Button option1,option2,option3,submit;
    TextView status;
    Boolean c;
    String id;
    String value;
    final Context context = this;
    private Button button;
    private EditText result;
    String brochureFees,admissionCharges,courierCharges;
    String address1;
    String option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admission Enquiry Final Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_dmission_enquiry_final_screen);

        status = (TextView) findViewById(R.id.text_reference_number);
        back = (ImageView) findViewById(R.id.img_back);
        option1 = (Button) findViewById(R.id.btn_pollOption1);
        option2 = (Button) findViewById(R.id.btn_pollOption2);
        option3 = (Button) findViewById(R.id.btn_pollOption3);
        optionImage1 = (ImageView) findViewById(R.id.image_poll1);
        optionImage2 = (ImageView) findViewById(R.id.image_poll2);
        optionImage3 = (ImageView) findViewById(R.id.image_poll3);
        submit = (Button) findViewById(R.id.submit);
        id = getIntent().getStringExtra("id");
        String sourceString = "Your enquiry has been registered with us and your reference id is: "+"<b>" + id + "</b> ";
        status.setText(Html.fromHtml(sourceString));

        brochureFees = getIntent().getStringExtra("brochureFees");
        admissionCharges = getIntent().getStringExtra("admissionCharges");
        courierCharges = getIntent().getStringExtra("courier_charges");
        address1 = getIntent().getStringExtra("address");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdmissionEnquiryFinalScreen.this, AdmissionEnquiry.class);
                startActivity(intent);
                finish();
            }
        });

        option1.setText("I want to recieve prospectus by courier and pay online");
        option2.setText("I want to pay online and download prospectus form");
        option3.setText("I will collect the form from School within 7 days");

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c=true;
                value="1";
                optionImage1.setImageResource(R.drawable.greencircletick);
                optionImage2.setImageResource(R.drawable.greycircletick);
                optionImage3.setImageResource(R.drawable.greycircletick);

            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c=true;
                value="2";
                optionImage1.setImageResource(R.drawable.greycircletick);
                optionImage2.setImageResource(R.drawable.greencircletick);
                optionImage3.setImageResource(R.drawable.greycircletick);

            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c=true;
                value="3";
                optionImage1.setImageResource(R.drawable.greycircletick);
                optionImage2.setImageResource(R.drawable.greycircletick);
                optionImage3.setImageResource(R.drawable.greencircletick);

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c == false)
                {
                    Utils.showToast(getApplicationContext(),"Please select an option");
                }

                else
                {
                      postLeave();
                }

            }
        });






    }

    private void postLeave()
    {
        //Utils.showToast(getApplicationContext(), ""+date2+date1);
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        //Preferences.getInstance().loadPreference(StudentNewLeave.this);
        //System.out.println(Preferences.getInstance().studentId);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.UPDATE_ADDMISSION_ENQUIRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog
                        JSONObject responseObject;
                        try
                        {
                            responseObject = new JSONObject(response);
                            if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                            {

                                Utils.showToast(AdmissionEnquiryFinalScreen.this,"Error Submitting Comment");
                                loading.dismiss();

                            }
                            else
                            if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                            {
                                Utils.showToast(AdmissionEnquiryFinalScreen.this, "Session Expired:Please Login Again");
                                loading.dismiss();
                            }

                            else
                            if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                            {

                                //Utils.showToast(AdmissionEnquiryFinalScreen.this,"Successfuly Submitted Comment");
                                loading.dismiss();

                                if(value.matches("1"))
                                {

                                    Intent intent = new Intent(AdmissionEnquiryFinalScreen.this,AdmissionCourierAddress.class);
                                    //intent.putExtra("email",userInput.getText().toString());
                                    intent.putExtra("brochureFees",brochureFees);
                                    intent.putExtra("admissionCharges",admissionCharges);
                                    intent.putExtra("courierCharges",courierCharges);
                                    intent.putExtra("address",address1);
                                    intent.putExtra("value","1");
                                    intent.putExtra("id",id);
                                    startActivity(intent);


                                }

                                else
                                if(value.matches("2"))
                                {
                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.prompts, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);

                                    // set prompts.xml to alertdialog builder
                                    alertDialogBuilder.setView(promptsView);

                                    final EditText userInput = (EditText) promptsView
                                            .findViewById(R.id.editTextDialogUserInput);

                                    // set dialog message
                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,
                                                                            int id) {
                                                            // get user input and set it to result
                                                            // edit text
                                                            result.setText(userInput.getText());
                                                            Intent intent = new Intent(AdmissionEnquiryFinalScreen.this,AdmissionPaymentCharges.class);
                                                            intent.putExtra("email",userInput.getText().toString());
                                                            intent.putExtra("brochureFees",brochureFees);
                                                            intent.putExtra("admissionCharges",admissionCharges);
                                                            intent.putExtra("courierCharges",courierCharges);
                                                            intent.putExtra("value","2");
                                                            intent.putExtra("id",id);
                                                            startActivity(intent);
                                                        }
                                                    })
                                            .setNegativeButton("Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,
                                                                            int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    // show it
                                    alertDialog.show();

                                }

                                else
                                {
                                    Utils.showToast(getApplicationContext(),"Thanks for submitting enquiry you will be notified the status asap.");
                                    Intent intent = new Intent(AdmissionEnquiryFinalScreen.this,AdmissionEnquiry.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }


                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                            Utils.showToast(AdmissionEnquiryFinalScreen.this, "Error submitting alert! Please try after sometime.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        System.out.println(volleyError);

                        //Showing toast
                        Utils.showToast(getApplicationContext(), "error2");
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                //Converting Bitmap to String

                // Preferences.getInstance().loadPreference(StudentNewLeave.this);
                //studentId = Preferences.getInstance().studentId;


                //http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
                //Getting Image Name
                // String name = editTextName.getText().toString().trim();

                //Creating parameters

                Map<String,String> params = new Hashtable<String, String>();
                //image = getStringImage(bitmap);

                //Adding parameters

                if(value.matches("1"))
                {
                    option = "courier";
                }
                else
                if(value.matches("2"))
                {
                    option = "email";
                }
                else
                {
                    option = "self";
                }

                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("req_ref_id",id);
                params.put("value","1");
                params.put("option",option);
                //params.put("token",Preferences.getInstance().token);
                // params.put("device_id", Preferences.getInstance().deviceId);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
