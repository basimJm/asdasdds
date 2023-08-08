package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

public class ChairmanStudentFragementAgeingAnalysisDetails extends AppCompatActivity {

    ImageView back;
    private TextView totalBasicFees,totalAmountRecieved,BalanceDueAmount,InterestAmount,totalBasicFees1,totalAmountRecieved1,BalanceDueAmount1,InterestAmount1;
    LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4;
    String Rs;
    public JSONArray chairmanStudentFragmentAgeingFeesArray;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Student Fragment Ageing Analysis Details");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_chairman_student_fragement_ageing_analysis_details);

        totalBasicFees = (TextView) findViewById(R.id.text_fee_type1);
        totalBasicFees1 = (TextView) findViewById(R.id.text_fee_type11);
        totalAmountRecieved = (TextView) findViewById(R.id.text_fee_type2);
        totalAmountRecieved1 = (TextView) findViewById(R.id.text_fee_type12);
        BalanceDueAmount = (TextView) findViewById(R.id.text_fee_type3);
        BalanceDueAmount1 = (TextView) findViewById(R.id.text_fee_type13);
        InterestAmount = (TextView) findViewById(R.id.text_fee_type4);
        InterestAmount1 = (TextView) findViewById(R.id.text_fee_type14);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
        Rs = getApplicationContext().getString(R.string.Rs);
        value = getIntent().getStringExtra("value");

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanStudentFragementAgeingAnalysisDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES_AGEING_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId +"&ins_id=" + Preferences.getInstance().institutionId);
            chairmanStudentFragmentAgeingFeesArray= new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanStudentFragmentAgeingFeesArray!= null) {
            try {
                if (value.matches("1")) {

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("total_basic_due").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("total_basic_due").matches("null")) {
                        linearLayout1.setVisibility(View.GONE);
                    } else {
                        totalBasicFees.setText("Basic Fees");
                        totalBasicFees1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("total_basic_due"));
                    }

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("received_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("received_amount").matches("null")) {
                        linearLayout2.setVisibility(View.GONE);
                    } else {
                        totalAmountRecieved.setText("Amount Received");
                        totalAmountRecieved1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("received_amount"));
                    }

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("interest_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("interest_amount").matches("null")) {
                        linearLayout3.setVisibility(View.GONE);
                    } else {
                        InterestAmount.setText("Interest Amount");
                        InterestAmount1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("interest_amount"));
                    }

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("balance_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("balance_amount").matches("null")) {
                        linearLayout4.setVisibility(View.GONE);
                    } else {
                        BalanceDueAmount.setText("Balance Due");
                        BalanceDueAmount1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("balance_amount"));
                    }
                } else if (value.matches("2")) {
                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("total_basic_due").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("total_basic_due").matches("null")) {
                        linearLayout1.setVisibility(View.GONE);
                    } else {
                        totalBasicFees.setText("Basic Fees");
                        totalBasicFees1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("total_basic_due"));
                    }

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("received_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("received_amount").matches("null")) {
                        linearLayout2.setVisibility(View.GONE);
                    } else {
                        totalAmountRecieved.setText("Amount Received");
                        totalAmountRecieved1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("received_amount"));
                    }

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("interest_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("interest_amount").matches("null")) {
                        linearLayout3.setVisibility(View.GONE);
                    } else {
                        InterestAmount.setText("Interest Amount");
                        InterestAmount1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("interest_amount"));
                    }

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("balance_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("balance_amount").matches("null")) {
                        linearLayout4.setVisibility(View.GONE);
                    } else {
                        BalanceDueAmount.setText("Balance Due");
                        BalanceDueAmount1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("balance_amount"));
                    }
                } else if (value.matches("3")) {

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("total_basic_due").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("total_basic_due").matches("null")) {
                        linearLayout1.setVisibility(View.GONE);
                    } else {
                        totalBasicFees.setText("Basic Fees");
                        totalBasicFees1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("total_basic_due"));
                    }

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("received_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("received_amount").matches("null")) {
                        linearLayout2.setVisibility(View.GONE);
                    } else {
                        totalAmountRecieved.setText("Amount Received");
                        totalAmountRecieved1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("received_amount"));
                    }

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("interest_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("interest_amount").matches("null")) {
                        linearLayout3.setVisibility(View.GONE);
                    } else {
                        InterestAmount.setText("Interest Amount");
                        InterestAmount1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("interest_amount"));
                    }

                    if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("balance_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("balance_amount").matches("null")) {
                        linearLayout4.setVisibility(View.GONE);
                    } else {
                        BalanceDueAmount.setText("Balance Due");
                        BalanceDueAmount1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("balance_amount"));
                    }


                }

                else
                    if(value.matches("4"))
                    {
                        if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("total_basic_due").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("total_basic_due").matches("null")) {
                            linearLayout1.setVisibility(View.GONE);
                        } else {
                            totalBasicFees.setText("Basic Fees");
                            totalBasicFees1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("total_basic_due"));
                        }

                        if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("received_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("received_amount").matches("null")) {
                            linearLayout2.setVisibility(View.GONE);
                        } else {
                            totalAmountRecieved.setText("Amount Received");
                            totalAmountRecieved1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("received_amount"));
                        }

                        if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("interest_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("interest_amount").matches("null")) {
                            linearLayout3.setVisibility(View.GONE);
                        } else {
                            InterestAmount.setText("Interest Amount");
                            InterestAmount1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("interest_amount"));
                        }

                        if (chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("balance_amount").matches("") || chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("balance_amount").matches("null")) {
                            linearLayout4.setVisibility(View.GONE);
                        } else {
                            BalanceDueAmount.setText("Balance Due");
                            BalanceDueAmount1.setText(Rs + chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("balance_amount"));
                        }
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
