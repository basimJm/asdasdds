package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.ParentUnpaidFeesNewMultilevelAdapter1;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ParentStudentFeesUnpaidMultilevelChildVO;
import com.schoofi.utils.ParentStudentFessUnpaidMultilevelParentVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParentStudentFeesUnpaidNewMultilevelDetails extends AppCompatActivity {

    private TextView feeType1,feeType2,feeType3,feeType4,feeType5,feeType6,feeType7,feeType8,feeType9,amount1,amount2,amount3,amount4,amount5,amount6,amount7,amount8,amount9;
    private Button payNow;
    int count;
    String array,array1,array2,array3,array4,array5,array6,array7,array8,array9,array10,gross,serviceTax;
    private JSONArray parentStudentFeesUnpaidParentArray;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Parent Student Fees Unpaid New Multilevel Details");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_parent_student_fees_unpaid_new_multilevel_details);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentStudentFeesUnpaidNewMultilevelDetails.this,ParentFees.class);
                startActivity(intent);
                finish();
            }
        });

        feeType1 = (TextView) findViewById(R.id.text_fee_type1);
        feeType2 = (TextView) findViewById(R.id.text_fee_type2);
        feeType3 = (TextView) findViewById(R.id.text_fee_type3);
        feeType4 = (TextView) findViewById(R.id.text_fee_type4);
        feeType5 = (TextView) findViewById(R.id.text_fee_type5);
        feeType6 = (TextView) findViewById(R.id.text_fee_type6);
        feeType7 = (TextView) findViewById(R.id.text_fee_type7);
        feeType8 = (TextView) findViewById(R.id.text_fee_type8);
        feeType9 = (TextView) findViewById(R.id.text_fee_type9);


        amount1 = (TextView) findViewById(R.id.text_fee_type11);
        amount2 = (TextView) findViewById(R.id.text_fee_type12);
        amount3 = (TextView) findViewById(R.id.text_fee_type13);
        amount4 = (TextView) findViewById(R.id.text_fee_type14);
        amount5 = (TextView) findViewById(R.id.text_fee_type15);
        amount6 = (TextView) findViewById(R.id.text_fee_type16);
        amount7 = (TextView) findViewById(R.id.text_fee_type17);
        amount8 = (TextView) findViewById(R.id.text_fee_type18);
        amount9 = (TextView) findViewById(R.id.text_fee_type19);

        payNow = (Button) findViewById(R.id.btn_payNow);

        array = getIntent().getStringExtra("array");
        array1 = getIntent().getStringExtra("array1");
        array2 = getIntent().getStringExtra("array2");
        array3 = getIntent().getStringExtra("array3");
        array4 = getIntent().getStringExtra("array4");
        array5 = getIntent().getStringExtra("array5");
        array6 = getIntent().getStringExtra("array6");
        array7 = getIntent().getStringExtra("array7");
        array8 = getIntent().getStringExtra("array8");
        array9 = getIntent().getStringExtra("array9");
        array10 = getIntent().getStringExtra("array10");
        count = getIntent().getExtras().getInt("count");
        serviceTax = getIntent().getStringExtra("serviceTax");


        getStudentFeedList();




        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if(parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("gross_total").matches("0.00"))
                    {
                        Utils.showToast(getApplicationContext(),"Payable amount is incorrect!");
                    }

                    else
                    {
                        Intent intent = new Intent(ParentStudentFeesUnpaidNewMultilevelDetails.this, ParentFeesActivityOptionScreen.class);
                        intent.putExtra("array", array);
                        intent.putExtra("array1", array1);
                        intent.putExtra("array2", array2);
                        intent.putExtra("array3", array3);
                        intent.putExtra("array4", array4);
                        intent.putExtra("array5", array5);
                        intent.putExtra("array6", array6);
                        intent.putExtra("array7", array7);
                        intent.putExtra("array8", array8);
                        intent.putExtra("array9", array9);
                        intent.putExtra("array10",array10);
                        intent.putExtra("count", count);
                        intent.putExtra("gross", parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("gross_total"));
                        intent.putExtra("net_fee_amount",parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("net_fee_amount"));
                        intent.putExtra("serviceTax", serviceTax);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ParentStudentFeesUnpaidNewMultilevelDetails.this,ParentFees.class);
        startActivity(intent);
        finish();
    }

    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_STUDENT_UNPAID_FEES_DETAILS;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("fee"))
                    {

                        parentStudentFeesUnpaidParentArray= new JSONObject(response).getJSONArray("fee");




                        /*for(int i=0;i<parentStudentFeesUnpaidArray.length();i++)
                        {
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("isAdded","1");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text1_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text2_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text3_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text4_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text5_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("interest","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("isDelay","N");
                        }*/
                        if(null!=parentStudentFeesUnpaidParentArray && parentStudentFeesUnpaidParentArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = parentStudentFeesUnpaidParentArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_STUDENT_UNPAID_FEES_DETAILS+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&fee_due_date_id="+array.toString(),e);

                            amount1.setText("Rs "+parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("total_fee_amount"));
                            amount2.setText("Rs "+parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("discount_amount"));
                            amount3.setText("Rs "+parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("delay_fine_amount"));
                            amount4.setText("Rs "+parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("handling_charges"));
                            amount5.setText("Rs "+parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("service_tax"));
                            amount6.setText("Rs "+parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("gross_total"));
                            amount7.setText("Rs "+parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("interest_amount"));
                            amount9.setText("Rs "+parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("net_fee_amount"));
                            amount8.setText("Rs "+parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("previous_paid"));

                            feeType1.setText("Total Fees:");
                            feeType2.setText("Discount Amount:");
                            feeType3.setText("Delay Fine Amount:");
                            feeType4.setText("Handling Charges");
                            feeType5.setText("GST");
                            feeType6.setText("Payable Amount:");
                            feeType7.setText("Interest Amount");
                            feeType9.setText("Net Fee Amount Due");
                            feeType8.setText("Amount Paid");

                            loading.dismiss();





                        }
                    }
                    else {
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                        loading.dismiss();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loading.dismiss();
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");

            }
        })
        {
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("cls_id",Preferences.getInstance().studentClassId);
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("fee_due_date_id",array.toString());
				return params;
			}};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            loading.dismiss();
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }


}
