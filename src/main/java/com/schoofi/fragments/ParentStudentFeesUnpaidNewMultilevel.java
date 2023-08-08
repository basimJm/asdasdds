package com.schoofi.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.ParentStudentFeesUnpaidNewMultilevelDetails;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.ParentStudentFeesUnpaidAdapter;
import com.schoofi.adapters.ParentUnpaidFeesNewMultilevelAdapter;
import com.schoofi.adapters.ParentUnpaidFeesNewMultilevelAdapter1;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ParentStudentFeesUnpaidMultilevelChildVO;
import com.schoofi.utils.ParentStudentFessUnpaidMultilevelParentVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.openrnd.multilevellistview.MultiLevelListView;

/**
 * Created by Schoofi on 01-09-2016.
 */
public class ParentStudentFeesUnpaidNewMultilevel extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private ExpandableListView expandableListView;
    private JSONArray parentStudentFeesUnpaidParentArray,parentStudentFeesUnpaidChildArray,parentStudentFeesUnpaidChildArray1;
    JSONObject jsonObject,jsonObject1;
    private ParentUnpaidFeesNewMultilevelAdapter1 parentUnpaidFeesNewMultilevelAdapter;
    ArrayList<String> list;
    ArrayList<ParentStudentFessUnpaidMultilevelParentVO> parentStudentFessUnpaidMultilevelParentVOs;
    ArrayList<ParentStudentFeesUnpaidMultilevelChildVO> parentStudentFeesUnpaidMultilevelChildVOs;
    public ArrayList<String> feesUnpaid = new ArrayList<String>();
    private Button payNow;

    ArrayList<String> fee_type_text11,fee_type_text21,fee_type_text31,fee_type_text41,fee_type_text51;
    Button pay;
    ArrayList<String> totalFeesAmount,interest,delay,discount,handlingCharges,serviceTax,totalFeeAmount,stu_fee_date_id,feeStartDate,feeEndDate,days1,singleTotalAmount;
    String crrDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String crrDate1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

    String periodStart,periodEnd,fee_duration,periodStart1,periodEnd1,feeDueDate,feeDueDate1,delayAmount;
    Date date1,date2,date3;
    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
    String fee_type_text1,fee_type_text2,fee_type_text3,fee_type_text4,fee_type_text5;
    int days;
    String totalAmount,payableDuration,rateOfInterest;
    int count,count1;
    String totalFeesAmount1,fee1_type_text,fee2_type_text,fee3_type_text,fee4_type_text,fee5_type_text,fee6_type_text,fee7_type_text,fee8_type_text,fee9_type_text,fee10_type_text,fee_srrvice_tax;
    float interest1,totalAmount1,payableDuration1,rateOfInterest1,fee_type_text111=0,fee_type_text211=0,fee_type_text311=0,fee_type_text411=0,fee_type_text511=0,feeInterest=0,feeDelayCharges=0,feeServiceTax=0,feeHandlingCharges=0,finalTotalAmount=0;




    private int mPage;

    public static ParentStudentFeesUnpaidNewMultilevel newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ParentStudentFeesUnpaidNewMultilevel parentStudentFeesUnpaidNewMultilevel = new ParentStudentFeesUnpaidNewMultilevel();
        parentStudentFeesUnpaidNewMultilevel.setArguments(args);
        return parentStudentFeesUnpaidNewMultilevel;
    }

    @Override
    public void onResume() {
        super.onResume();
        getStudentFeedList();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_unpaid_fees_new_multilevel, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_listView);
        payNow = (Button) view.findViewById(R.id.btn_pay_fees);

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
        discount = new ArrayList<String>();
        totalFeesAmount = new ArrayList<String>();

        payNow.setOnClickListener(new View.OnClickListener() {
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
                discount = new ArrayList<String>();
                totalFeesAmount = new ArrayList<String>();
                //singleTotalAmount = new ArrayList<String>();
                count=0;
                count1=0;

                for(int u=0;u<parentStudentFeesUnpaidParentArray.length();u++)
                {
                    try {
                        if(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("isAdded").matches("1"))
                        {
                            feesUnpaid.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("fee_due_date"));
                            interest.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("interest_amount"));
                            delay.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("delay_fine_amount"));
                            handlingCharges.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("handling_charges"));
                            serviceTax.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("service_tax"));
                            feeStartDate.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("fee_start_date"));
                            feeEndDate.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("fee_end_date"));
                            totalFeeAmount.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("gross_total"));
                            days1.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("days"));
                            discount.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("discount_amount"));
                            totalFeesAmount.add(parentStudentFeesUnpaidParentArray.getJSONObject(u).getString("total_fee_amount"));

                            count = count+1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent(getActivity(),ParentStudentFeesUnpaidNewMultilevelDetails.class);

                intent.putExtra("array",feesUnpaid.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array1",interest.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array2",delay.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array3",handlingCharges.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array4",serviceTax.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array5",feeStartDate.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array6",feeEndDate.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array7",totalFeeAmount.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array8",discount.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array9",days1.toString().replace("[", "").replace("]", ""));
                intent.putExtra("array10",totalFeesAmount.toString().replace("[", "").replace("]", ""));
                intent.putExtra("count",count);
                try {
                    intent.putExtra("serviceTax",parentStudentFeesUnpaidParentArray.getJSONObject(0).getString("service_tax_rate"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startActivity(intent);
                getActivity().finish();



            }
        });

        getStudentFeedList();



        return view;
    }



    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_UNPAID_FESS+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getActivity(), "No Records Found");
                        payNow.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                        payNow.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("fee"))

                    {
                        payNow.setVisibility(View.VISIBLE);
                        parentStudentFeesUnpaidParentArray= new JSONObject(response).getJSONArray("fee");

                        parentStudentFessUnpaidMultilevelParentVOs = new ArrayList<ParentStudentFessUnpaidMultilevelParentVO>();


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
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_UNPAID_FESS+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId,e);
                            expandableListView.invalidateViews();
                            /*parentStudentFeesUnpaidAdapter = new ParentStudentFeesUnpaidAdapter(getActivity(), parentStudentFeesUnpaidArray);
                            parentStudentFeesUnpaid.setAdapter(parentStudentFeesUnpaidAdapter);
                            parentStudentFeesUnpaidAdapter.notifyDataSetChanged();*/

                            for(int k =0;k<parentStudentFeesUnpaidParentArray.length();k++)
                            {
                                parentStudentFeesUnpaidParentArray.getJSONObject(k).put("isAdded","0");
                            }





                            for(int i=0;i<parentStudentFeesUnpaidParentArray.length();i++)
                            {
                                ParentStudentFessUnpaidMultilevelParentVO parentStudentFessUnpaidMultilevelParentVO = new ParentStudentFessUnpaidMultilevelParentVO();
                                parentStudentFessUnpaidMultilevelParentVO.setFeesStartDate(parentStudentFeesUnpaidParentArray.getJSONObject(i).getString("fee_start_date"));
                                parentStudentFessUnpaidMultilevelParentVO.setFeesEndDate(parentStudentFeesUnpaidParentArray.getJSONObject(i).getString("fee_end_date"));
                                parentStudentFessUnpaidMultilevelParentVO.setPayBy(parentStudentFeesUnpaidParentArray.getJSONObject(i).getString("fee_due_date"));
                                parentStudentFessUnpaidMultilevelParentVO.setTotalAmount(parentStudentFeesUnpaidParentArray.getJSONObject(i).getString("total_fee_amount"));
                                parentStudentFeesUnpaidMultilevelChildVOs = new ArrayList<ParentStudentFeesUnpaidMultilevelChildVO>();
                                jsonObject = parentStudentFeesUnpaidParentArray.getJSONObject(i);
                                parentStudentFeesUnpaidChildArray1 = jsonObject.getJSONArray("bifurcation");

                                for (int j=0;j<parentStudentFeesUnpaidChildArray1.length();j++)
                                {
                                    jsonObject1 = parentStudentFeesUnpaidChildArray1.getJSONObject(j);
                                    ParentStudentFeesUnpaidMultilevelChildVO parentStudentFeesUnpaidMultilevelChildVO = new ParentStudentFeesUnpaidMultilevelChildVO();
                                    parentStudentFeesUnpaidMultilevelChildVO.setAmount(jsonObject1.getString("fee_amount"));
                                    parentStudentFeesUnpaidMultilevelChildVO.setFeesType(jsonObject1.getString("fee_type_text"));
                                    parentStudentFeesUnpaidMultilevelChildVOs.add(parentStudentFeesUnpaidMultilevelChildVO);
                                }

                                parentStudentFessUnpaidMultilevelParentVO.setItems(parentStudentFeesUnpaidMultilevelChildVOs);
                                parentStudentFessUnpaidMultilevelParentVOs.add(parentStudentFessUnpaidMultilevelParentVO);

                            }










                            //swipyRefreshLayout.setRefreshing(false);

                            parentUnpaidFeesNewMultilevelAdapter = new ParentUnpaidFeesNewMultilevelAdapter1(getActivity(),parentStudentFessUnpaidMultilevelParentVOs,parentStudentFeesUnpaidParentArray);
                            expandableListView.setAdapter(parentUnpaidFeesNewMultilevelAdapter);
                            parentUnpaidFeesNewMultilevelAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        Utils.showToast(getActivity(), "Error Fetching Response");

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                    payNow.setVisibility(View.INVISIBLE);

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                payNow.setVisibility(View.INVISIBLE);

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
            payNow.setVisibility(View.INVISIBLE);
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }


}
