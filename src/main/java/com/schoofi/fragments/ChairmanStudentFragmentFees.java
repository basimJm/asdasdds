package com.schoofi.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.activitiess.ChairmanStudentFragmentFeesClass;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.StudentDailyAttendanceAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schoofi on 23-06-2016.
 */
public class ChairmanStudentFragmentFees extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    //public ArrayList<StudentDailyAttendanceVO> studentDailyAttendanceList;
    public JSONArray chairmanStudentFragmentFeesArray;
    private ListView studentDailyAttendanceListView;
    private TextView totalBasicFees,totalAmountRecieved,BalanceDueAmount,InterestAmount,totalBasicFees1,totalAmountRecieved1,BalanceDueAmount1,InterestAmount1,interestAmountTotal,interestAmountTotal1,totalAmountRecieved2,totalAmountRecieved3;
    StudentDailyAttendanceAdapter studentDailyAttendanceAdapter;
    private Context context;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    SwipyRefreshLayout swipyRefreshLayout;
    String Rs;
    LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLAyout5,linearLayout6,top;
    int value;

    public static ChairmanStudentFragmentFees newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ChairmanStudentFragmentFees chairmanStudentFragmentFees = new ChairmanStudentFragmentFees();
        chairmanStudentFragmentFees.setArguments(args);
        return chairmanStudentFragmentFees;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chairman_student_fragment_fees, container, false);
        context = getActivity();
        totalBasicFees = (TextView) view.findViewById(R.id.text_fee_type1);
        totalBasicFees1 = (TextView) view.findViewById(R.id.text_fee_type11);
        totalAmountRecieved = (TextView) view.findViewById(R.id.text_fee_type2);
        totalAmountRecieved1 = (TextView) view.findViewById(R.id.text_fee_type12);
        BalanceDueAmount = (TextView) view.findViewById(R.id.text_fee_type3);
        BalanceDueAmount1 = (TextView) view.findViewById(R.id.text_fee_type13);
        InterestAmount = (TextView) view.findViewById(R.id.text_fee_type4);
        InterestAmount1 = (TextView) view.findViewById(R.id.text_fee_type14);
        interestAmountTotal = (TextView) view.findViewById(R.id.text_fee_type5);
        interestAmountTotal1 = (TextView) view.findViewById(R.id.text_fee_type15);
        totalAmountRecieved2 = (TextView) view.findViewById(R.id.text_fee_type6);
        totalAmountRecieved3 = (TextView) view.findViewById(R.id.text_fee_type16);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout) view.findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout) view.findViewById(R.id.linearLayout4);
        linearLAyout5 = (LinearLayout) view.findViewById(R.id.linearLayout5);
        linearLayout6 = (LinearLayout) view.findViewById(R.id.linearLayout6);
        top = (LinearLayout) view.findViewById(R.id.linear_top);
        Rs = getActivity().getString(R.string.Rs);
        initData();
        getStudentDailyAttendance();

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=1;
                Intent intent = new Intent(getActivity(), ChairmanStudentFragmentFeesClass.class);
                intent.putExtra("value",value);
                intent.putExtra("temp","0");
                startActivity(intent);

            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=2;
                Intent intent = new Intent(getActivity(), ChairmanStudentFragmentFeesClass.class);
                intent.putExtra("value",value);
                intent.putExtra("temp","0");
                startActivity(intent);

            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=3;
                Intent intent = new Intent(getActivity(), ChairmanStudentFragmentFeesClass.class);
                intent.putExtra("value",value);
                intent.putExtra("temp","0");
                startActivity(intent);

            }
        });

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=4;
                Intent intent = new Intent(getActivity(), ChairmanStudentFragmentFeesClass.class);
                intent.putExtra("value",value);
                intent.putExtra("temp","0");
                startActivity(intent);

            }
        });

        linearLAyout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                chairmanStudentFragmentFeesArray= null;
            }
            else
            {
                chairmanStudentFragmentFeesArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(chairmanStudentFragmentFeesArray!= null)
        {

            try {
                totalBasicFees.setText("Total Fee Amount(for current year)");
                totalBasicFees1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("net_fee_amount")+"/-");
                totalAmountRecieved.setText("Total Due Amount(as on date)");
                totalAmountRecieved1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("total_basic_due")+"/-");
                InterestAmount.setText("Net Amount Received");
                InterestAmount1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("receive_amount_without_interest")+"/-");
                BalanceDueAmount.setText("Pending fees(as on date)");
                BalanceDueAmount1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("pending_fee")+"/-");
                interestAmountTotal.setText("Fine,Interest,Handling,Misc.Charges");
                interestAmountTotal1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("interest_amount")+"/-");
                totalAmountRecieved2.setText("Total Amount Received");
                totalAmountRecieved3.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("receive_amount_with_interest")+"/-");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    protected void getStudentDailyAttendance()
    {
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
               System.out.println(response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
               //    System.out.println(responseObject);
                   // System.out.print(""+url);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        Utils.showToast(getActivity(),"No Records Found");
                        top.setVisibility(View.GONE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(context, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("fee"))
                    {

                        chairmanStudentFragmentFeesArray= new JSONObject(response).getJSONArray("fee");
                        if(null!=chairmanStudentFragmentFeesArray && chairmanStudentFragmentFeesArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanStudentFragmentFeesArray.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId,e);
                            totalBasicFees.setText("Total Fee Amount(for current year)");
                            totalBasicFees1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("net_fee_amount")+"/-");
                            totalAmountRecieved.setText("Total Due Amount(as on date)");
                            totalAmountRecieved1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("total_basic_due")+"/-");
                            InterestAmount.setText("Net Amount Received");
                            InterestAmount1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("receive_amount_without_interest")+"/-");
                            BalanceDueAmount.setText("Pending fees(as on date)");
                            BalanceDueAmount1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("pending_fee")+"/-");
                            interestAmountTotal.setText("Fine,Interest,Handling,Misc.Charges");
                            interestAmountTotal1.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("interest_amount")+"/-");
                            totalAmountRecieved2.setText("Total Amount Received");
                            totalAmountRecieved3.setText(Rs+chairmanStudentFragmentFeesArray.getJSONObject(0).getString("receive_amount_with_interest")+"/-");
                        }
                    }

                    else
                    {
                        totalBasicFees.setVisibility(View.INVISIBLE);
                        totalBasicFees1.setVisibility(View.INVISIBLE);
                        totalAmountRecieved.setVisibility(View.INVISIBLE);
                        totalAmountRecieved1.setVisibility(View.INVISIBLE);
                        InterestAmount.setVisibility(View.INVISIBLE);
                        InterestAmount1.setVisibility(View.INVISIBLE);
                        BalanceDueAmount.setVisibility(View.INVISIBLE);
                        BalanceDueAmount1.setVisibility(View.INVISIBLE);

                    }
                }

                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(context, "Error fetching modules! Please try after sometime.");
                    top.setVisibility(View.GONE);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                top.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getActivity());
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("token",Preferences.getInstance().token);
               /* params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("crr_date",currentDate);
                params.put("cls_id", Preferences.getInstance().studentClassId);*/
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(context, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
