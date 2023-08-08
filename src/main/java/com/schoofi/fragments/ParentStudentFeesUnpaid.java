package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.activitiess.ParentStudentUnpaidFeesDetails;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.ParentStudentFeesUnpaidAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by harsh malhotra on 4/11/2016.
 */
public class ParentStudentFeesUnpaid extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    ListView parentStudentFeesUnpaid;
    JSONArray parentStudentFeesUnpaidArray;
    ParentStudentFeesUnpaidAdapter parentStudentFeesUnpaidAdapter;
    SwipyRefreshLayout swipyRefreshLayout;
    ArrayList<String> fee_type_text11,fee_type_text21,fee_type_text31,fee_type_text41,fee_type_text51;
    Button pay;
    ArrayList<String> interest,delay,handlingCharges,serviceTax,totalFeeAmount,stu_fee_date_id,feeStartDate,feeEndDate,days1,singleTotalAmount;
    String crrDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String crrDate1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
    String periodStart,periodEnd,fee_duration,periodStart1,periodEnd1,feeDueDate,feeDueDate1,delayAmount;
    Date date1,date2,date3;
    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
    String fee_type_text1,fee_type_text2,fee_type_text3,fee_type_text4,fee_type_text5;
    int days;
    String totalAmount,payableDuration,rateOfInterest;
    int count,count1;
    String fee1_type_text,fee2_type_text,fee3_type_text,fee4_type_text,fee5_type_text,fee6_type_text,fee7_type_text,fee8_type_text,fee9_type_text,fee10_type_text,fee_srrvice_tax;
    float interest1,totalAmount1,payableDuration1,rateOfInterest1,fee_type_text111=0,fee_type_text211=0,fee_type_text311=0,fee_type_text411=0,fee_type_text511=0,feeInterest=0,feeDelayCharges=0,feeServiceTax=0,feeHandlingCharges=0,finalTotalAmount=0;


    private int mPage;

    public static ParentStudentFeesUnpaid newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ParentStudentFeesUnpaid parentStudentFeesUnpaid = new ParentStudentFeesUnpaid();
        parentStudentFeesUnpaid.setArguments(args);
        return parentStudentFeesUnpaid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_feedback, container, false);
        parentStudentFeesUnpaid = (ListView) view.findViewById(R.id.listView_teacher_feedback);

        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        pay = (Button) view.findViewById(R.id.btn_pay_fees);
        fee_type_text11 = new ArrayList<String>();
        fee_type_text21 = new ArrayList<String>();
        fee_type_text31 = new ArrayList<String>();
        fee_type_text41 = new ArrayList<String>();
        fee_type_text51 = new ArrayList<String>();
        interest = new ArrayList<String>();
        delay = new ArrayList<String>();
        handlingCharges = new ArrayList<String>();
        serviceTax = new ArrayList<String>();
        stu_fee_date_id = new ArrayList<String>();
        totalFeeAmount = new ArrayList<String>();
        feeStartDate = new ArrayList<String>();
        feeEndDate = new ArrayList<String>();
        days1 = new ArrayList<String>();

        initData();
        getStudentFeedList();

       // Utils.showToast(getContext(),interest.toString());

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
                getStudentFeedList();
            }
        });



        parentStudentFeesUnpaid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView checkBox = (ImageView) view.findViewById(R.id.checkbox_credentials);





                try {
                    if (parentStudentFeesUnpaidArray.getJSONObject(position).getString("isAdded").matches("1")) {

                        parentStudentFeesUnpaidArray.getJSONObject(position).put("isAdded", "2");
                        //parentStudentFeesUnpaid.getChildAt(position).setBackgroundColor(android.R.color.darker_gray);
                        //parent.getChildAt(position).setBackgroundColor(R.color.transparent);

                        //Utils.showToast(getActivity(),fee1Amount.toString());

                         checkBox.setImageResource(R.drawable.greencircletick);


                        try {
                            feeDueDate = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_due_date");


                            try {
                                date1 = formatter2.parse(feeDueDate);
                                feeDueDate1 = formatter1.format(date1);
                                DateTime dt = formatter.parseDateTime(crrDate1);
                                DateTime dt1 = formatter.parseDateTime(feeDueDate1);

                                Period period = new Period(dt, dt1);
                                // Utils.showToast(getActivity(),""+period.getDays());

                                fee_type_text1 = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee1_amount");
                                fee_type_text2 = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee2_amount");
                                fee_type_text3 = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee3_amount");
                                fee_type_text4 = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee4_amount");
                                fee_type_text5 = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee5_amount");

                                if (fee_type_text1.matches("0") || fee_type_text1.matches("") || fee_type_text1.matches("null")) {
                                    //fee_type_text11.add("0");
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text1_android", "0");
                                } else {
                                    //fee_type_text11.add(fee_type_text1);
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text1_android", fee_type_text1);
                                }

                                if (fee_type_text2.matches("0") || fee_type_text2.matches("") || fee_type_text2.matches("null")) {
                                    //fee_type_text21.add("0");
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text2_android", "0");
                                } else {
                                    //fee_type_text21.add(fee_type_text2);
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text2_android", fee_type_text2);
                                }

                                if (fee_type_text3.matches("0") || fee_type_text3.matches("") || fee_type_text3.matches("null")) {
                                    //fee_type_text31.add("0");
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text3_android", "0");
                                } else {
                                    //fee_type_text31.add(fee_type_text3);
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text3_android", fee_type_text3);
                                }

                                if (fee_type_text4.matches("0") || fee_type_text4.matches("") || fee_type_text4.matches("null")) {
                                    //fee_type_text41.add("0");
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text14_android", "0");
                                } else {
                                    //fee_type_text41.add(fee_type_text4);
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text4_android", fee_type_text4);
                                }

                                if (fee_type_text5.matches("0") || fee_type_text5.matches("") || fee_type_text5.matches("null")) {
                                    //fee_type_text51.add("0");
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text5_android", "0");
                                } else {
                                    //fee_type_text51.add(fee_type_text5);
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("fee_type_text5_android", fee_type_text5);
                                }

                                //days = String.valueOf(period.getDays());
                                days = Integer.parseInt(parentStudentFeesUnpaidArray.getJSONObject(position).getString("days"));
                                totalAmount = parentStudentFeesUnpaidArray.getJSONObject(position).getString("total_fee_amount");
                                /*rateOfInterest = parentStudentFeesUnpaidArray.getJSONObject(position).getString("interest_rate");
                                payableDuration = parentStudentFeesUnpaidArray.getJSONObject(position).getString("applicable_duration");*/

                                totalAmount1 = Float.parseFloat(totalAmount);
                                //rateOfInterest1 = Float.parseFloat(rateOfInterest);
                                //payableDuration1 = Float.parseFloat(payableDuration);
                               // Utils.showToast(getActivity(), "" + days);

                                if(parentStudentFeesUnpaidArray.getJSONObject(position).getString("interest_amount").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("interest_amount").matches("null"))
                                {
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("interest", "0");
                                }

                                else
                                {
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("interest", parentStudentFeesUnpaidArray.getJSONObject(position).getString("interest_amount"));
                                }




                                /*if (days <= 0) {
                                    //interest.add("0");
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("interest", "0");
                                } else {
                                    interest1 = (totalAmount1 * days * rateOfInterest1) / (payableDuration1 * 100);
                                    //interest.add(String.valueOf(interest1));
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("interest", String.valueOf(interest1));
                                }

                                if (parentStudentFeesUnpaidArray.getJSONObject(position).getString("delay_fine_amount").matches("") || parentStudentFeesUnpaidArray.getJSONObject(position).getString("delay_fine_amount").matches("null")) {
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("isDelay", "N");
                                } else {
                                    parentStudentFeesUnpaidArray.getJSONObject(position).put("isDelay", "Y");
                                }*/

                                //Utils.showToast(getActivity(),fee_type_text11.toString());


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            //String days = Days.daysBetween()





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        parentStudentFeesUnpaidArray.getJSONObject(position).put("isAdded", "1");
                        checkBox.setImageResource(R.drawable.greycircletick);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }





            });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fee_type_text11 = new ArrayList<String>();
                fee_type_text21 = new ArrayList<String>();
                fee_type_text31 = new ArrayList<String>();
                fee_type_text41 = new ArrayList<String>();
                fee_type_text51 = new ArrayList<String>();
                interest = new ArrayList<String>();
                delay = new ArrayList<String>();
                handlingCharges = new ArrayList<String>();
                serviceTax = new ArrayList<String>();
                stu_fee_date_id = new ArrayList<String>();
                totalFeeAmount = new ArrayList<String>();
                feeStartDate = new ArrayList<String>();
                feeEndDate = new ArrayList<String>();
                days1 = new ArrayList<String>();
                //singleTotalAmount = new ArrayList<String>();
                count=0;
                count1=0;





                for(int k=0;k<parentStudentFeesUnpaidArray.length();k++)
                {
                    try {
                        if(parentStudentFeesUnpaidArray.getJSONObject(k).getString("isAdded").matches("2")) {
                          /*fee_type_text11.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text1_android"));
                            fee_type_text21.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text2_android"));
                            fee_type_text31.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text3_android"));
                            fee_type_text41.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text4_android"));
                            fee_type_text51.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text5_android"));
                            interest.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("interest"));*/
                            fee_type_text111 = fee_type_text111 + Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text1_android"));
                            fee_type_text211 = fee_type_text211 + Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text2_android"));
                            fee_type_text311 = fee_type_text311 + Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text3_android"));
                            fee_type_text411 = fee_type_text411 + Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text4_android"));
                            fee_type_text511 = fee_type_text511 + Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text5_android"));
                            feeInterest = feeInterest + Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("interest"));
                            feeDelayCharges = feeDelayCharges+Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("delay_fine_amount"));
                            feeServiceTax = feeServiceTax+Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("service_charges"));
                            feeHandlingCharges = feeHandlingCharges+Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("handling_charges"));
                            finalTotalAmount = finalTotalAmount+Float.parseFloat(parentStudentFeesUnpaidArray.getJSONObject(k).getString("final_fee_amount"));
                            if(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee1_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee1_type_text").matches("null"))
                            {
                                fee1_type_text = "null";
                                fee2_type_text = "null";
                            }

                            else
                            {
                                fee1_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee1_type_text");
                                fee2_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text1");

                            }

                            if(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee2_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee2_type_text").matches("null"))
                            {
                                fee3_type_text = "null";
                                fee4_type_text = "null";
                            }

                            else
                            {
                                fee3_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee2_type_text");
                                fee4_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text2");

                            }

                            if(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee3_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee3_type_text").matches("null"))
                            {
                                fee5_type_text = "null";
                                fee6_type_text = "null";
                            }

                            else
                            {
                                fee5_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee3_type_text");
                                fee6_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text3");

                            }

                            if(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee4_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee4_type_text").matches("null"))
                            {
                                fee7_type_text = "null";
                                fee8_type_text = "null";
                            }

                            else
                            {
                                fee7_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee4_type_text");
                                fee8_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text4");

                            }

                            if(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee5_type_text").matches("") || parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee5_type_text").matches("null"))
                            {
                                fee9_type_text = "null";
                                fee10_type_text = "null";
                            }

                            else
                            {
                                fee9_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee5_type_text");
                                fee10_type_text = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text5");

                            }

                            fee_srrvice_tax = parentStudentFeesUnpaidArray.getJSONObject(k).getString("fees_service_tax");
                            fee_type_text11.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text1_android"));
                            fee_type_text21.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text2_android"));
                            fee_type_text31.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text3_android"));
                            fee_type_text41.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text4_android"));
                            fee_type_text51.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_type_text5_android"));
                            interest.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("interest"));
                            delay.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("delay_fine_amount"));
                            handlingCharges.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("handling_charges"));
                            serviceTax.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("service_charges"));
                            totalFeeAmount.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("final_fee_amount"));
                            stu_fee_date_id.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("stu_fee_date_id"));
                            feeStartDate.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_start_date"));
                            feeEndDate.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("fee_end_date"));
                            days1.add(parentStudentFeesUnpaidArray.getJSONObject(k).getString("days"));
                            count = count+1;


                        }

                        else
                        {
                            count1 = count1+1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Utils.showToast(getActivity(),""+count);

                if(count == 0)
                {
                    Utils.showToast(getActivity(),"Please select atleast one field");
                }

                else {
                    Intent intent = new Intent(getActivity(), ParentStudentUnpaidFeesDetails.class);
                    intent.putExtra("fee_type_text111", fee_type_text111);
                    intent.putExtra("fee_type_text211", fee_type_text211);
                    intent.putExtra("fee_type_text311", fee_type_text311);
                    intent.putExtra("fee_type_text411", fee_type_text411);
                    intent.putExtra("fee_type_text511", fee_type_text511);
                    intent.putExtra("interest", feeInterest);
                    intent.putExtra("delay_charges", feeDelayCharges);
                    intent.putExtra("fee1_type_text", fee2_type_text);
                    intent.putExtra("fee_type_text1", fee1_type_text);
                    intent.putExtra("fee2_type_text", fee4_type_text);
                    intent.putExtra("fee_type_text2", fee3_type_text);
                    intent.putExtra("fee3_type_text", fee6_type_text);
                    intent.putExtra("fee_type_text3", fee5_type_text);
                    intent.putExtra("fee4_type_text", fee8_type_text);
                    intent.putExtra("fee_type_text4", fee7_type_text);
                    intent.putExtra("fee5_type_text", fee10_type_text);
                    intent.putExtra("fee_type_text5", fee9_type_text);
                    intent.putExtra("fees_service_tax", fee_srrvice_tax);
                    intent.putExtra("feeServiceTax", feeServiceTax);
                    intent.putExtra("feeHandlingCharges", feeHandlingCharges);
                    intent.putExtra("totalAmount", totalAmount);
                    intent.putExtra("finalTotalAmount", finalTotalAmount);
                    intent.putExtra("fee_type_text11", fee_type_text11.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("fee_type_text21", fee_type_text21.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("fee_type_text31", fee_type_text31.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("fee_type_text41", fee_type_text41.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("fee_type_text51", fee_type_text51.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("interest1", interest.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("delay_charges1", delay.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("feeServiceTax1", serviceTax.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("feeHandlingCharges1", handlingCharges.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("totalAmount1", totalFeeAmount.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("stu_fee_date_id", stu_fee_date_id.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("fee_start_date", feeStartDate.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("fee_end_date", feeEndDate.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("days", days1.toString().replace("[", "").replace("]", ""));
                    intent.putExtra("count", String.valueOf(count));

                    fee_type_text11.clear();
                    fee_type_text21.clear();
                    fee_type_text31.clear();
                    fee_type_text41.clear();
                    fee_type_text51.clear();
                    interest.clear();
                    delay.clear();
                    serviceTax.clear();
                    handlingCharges.clear();
                    totalFeeAmount.clear();
                    stu_fee_date_id.clear();
                    feeStartDate.clear();
                    feeEndDate.clear();
                    days1.clear();


                    startActivity(intent);
                    getActivity().finish();
                }


            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        fee_type_text11.clear();
        fee_type_text21.clear();
        fee_type_text31.clear();
        fee_type_text41.clear();
        fee_type_text51.clear();
        interest.clear();
        delay.clear();
        serviceTax.clear();
        handlingCharges.clear();
        totalFeeAmount.clear();
        stu_fee_date_id.clear();
        feeStartDate.clear();
        feeEndDate.clear();
        days1.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fee_type_text11.clear();
        fee_type_text21.clear();
        fee_type_text31.clear();
        fee_type_text41.clear();
        fee_type_text51.clear();
        interest.clear();
        delay.clear();
        serviceTax.clear();
        handlingCharges.clear();
        totalFeeAmount.clear();
        stu_fee_date_id.clear();
        feeStartDate.clear();
        feeEndDate.clear();
        days1.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fee_type_text11.clear();
        fee_type_text21.clear();
        fee_type_text31.clear();
        fee_type_text41.clear();
        fee_type_text51.clear();
        interest.clear();
        delay.clear();
        serviceTax.clear();
        handlingCharges.clear();
        totalFeeAmount.clear();
        stu_fee_date_id.clear();
        feeStartDate.clear();
        feeEndDate.clear();
        days1.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

        initData();
        getStudentFeedList();
        /*fee_type_text11 = new ArrayList<String>();
        fee_type_text21 = new ArrayList<String>();
        fee_type_text31 = new ArrayList<String>();
        fee_type_text41 = new ArrayList<String>();
        fee_type_text51 = new ArrayList<String>();
        interest = new ArrayList<String>();
        delay = new ArrayList<String>();
        handlingCharges = new ArrayList<String>();
        serviceTax = new ArrayList<String>();
        stu_fee_date_id = new ArrayList<String>();
        totalFeeAmount = new ArrayList<String>();
        feeStartDate = new ArrayList<String>();
        feeEndDate = new ArrayList<String>();
        days1 = new ArrayList<String>();
        //singleTotalAmount = new ArrayList<String>();
        count=0;
        count1=0;*/
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_UNPAID_FESS+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId);
            if(e == null)
            {
                parentStudentFeesUnpaidArray= null;
            }
            else
            {
                parentStudentFeesUnpaidArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(parentStudentFeesUnpaidArray!= null)
        {
            parentStudentFeesUnpaidAdapter= new ParentStudentFeesUnpaidAdapter(getActivity(),parentStudentFeesUnpaidArray);
            parentStudentFeesUnpaid.setAdapter(parentStudentFeesUnpaidAdapter);
            parentStudentFeesUnpaidAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_UNPAID_FESS+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getActivity(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("fee"))
                    {
                        parentStudentFeesUnpaidArray= new JSONObject(response).getJSONArray("fee");

                        for(int i=0;i<parentStudentFeesUnpaidArray.length();i++)
                        {
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("isAdded","1");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text1_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text2_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text3_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text4_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("fee_type_text5_android","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("interest","0");
                            parentStudentFeesUnpaidArray.getJSONObject(i).put("isDelay","N");
                        }
                        if(null!=parentStudentFeesUnpaidArray && parentStudentFeesUnpaidArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = parentStudentFeesUnpaidArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_UNPAID_FESS+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId,e);
                            parentStudentFeesUnpaid.invalidateViews();
                            parentStudentFeesUnpaidAdapter = new ParentStudentFeesUnpaidAdapter(getActivity(), parentStudentFeesUnpaidArray);
                            parentStudentFeesUnpaid.setAdapter(parentStudentFeesUnpaidAdapter);
                            parentStudentFeesUnpaidAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

                        }
                    }
                    else
                        Utils.showToast(getActivity(), "Error Fetching Response");

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

            }
        })
        {
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
