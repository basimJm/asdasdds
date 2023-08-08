package com.schoofi.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
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
import com.schoofi.activitiess.ChairmanStudentFragementAgeingAnalysisDetails;
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

/**
 * Created by Schoofi on 23-06-2016.
 */
public class ChairmanStudentFragmentAgeingFees extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    //public ArrayList<StudentDailyAttendanceVO> studentDailyAttendanceList;
    public JSONArray chairmanStudentFragmentAgeingFeesArray;
    private TextView type1, type2, type3, type4,total1,total2,total3,total4;
    private Context context;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    LinearLayout top,linearLayout1,linearLayout2,linearLayout3,linearLayout4;
    String Rs;
    //SwipyRefreshLayout swipyRefreshLayout;
    int value;


    public static ChairmanStudentFragmentAgeingFees newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ChairmanStudentFragmentAgeingFees chairmanStudentFragmentAgeingFees = new ChairmanStudentFragmentAgeingFees();
        chairmanStudentFragmentAgeingFees.setArguments(args);
        return chairmanStudentFragmentAgeingFees;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chairman_student_fragment_ageing_fees, container, false);
        type1 = (TextView) view.findViewById(R.id.text_payeeName);
        type2 = (TextView) view.findViewById(R.id.text_payeeName1);
        type3 = (TextView) view.findViewById(R.id.text_payeeName2);
        type4 = (TextView) view.findViewById(R.id.text_payeeName3);
        total1 = (TextView) view.findViewById(R.id.text_payeeName5);
        total2 = (TextView) view.findViewById(R.id.text_payeeName6);
        total3 = (TextView) view.findViewById(R.id.text_payeeName7);
        total4 = (TextView) view.findViewById(R.id.text_payeeName8);
        top = (LinearLayout) view.findViewById(R.id.linear_top);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linear1);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linear2);
        linearLayout3 = (LinearLayout) view.findViewById(R.id.linear3);
        linearLayout4 = (LinearLayout) view.findViewById(R.id.linear4);
        Rs = getActivity().getString(R.string.Rs);

        initData();
        getStudentFeedList();

        type1.setText("Fees>30 Days");
        type2.setText("Fees<30 Days and <60 Days");
        type3.setText("Fees>60 Days");
        type4.setText("Fees>90 Days");

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=1;
                Intent intent = new Intent(getActivity(), ChairmanStudentFragmentFeesClass.class);
                intent.putExtra("value",value);
                intent.putExtra("temp","1");
                startActivity(intent);
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=2;
                Intent intent = new Intent(getActivity(), ChairmanStudentFragmentFeesClass.class);
                intent.putExtra("value",value);
                intent.putExtra("temp","1");
                startActivity(intent);
            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=3;
                Intent intent = new Intent(getActivity(), ChairmanStudentFragmentFeesClass.class);
                intent.putExtra("value",value);
                intent.putExtra("temp","1");
                startActivity(intent);
            }
        });

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=4;
                Intent intent = new Intent(getActivity(), ChairmanStudentFragmentFeesClass.class);
                intent.putExtra("value",value);
                intent.putExtra("temp","1");
                startActivity(intent);
            }
        });
        /*type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChairmanStudentFragementAgeingAnalysisDetails.class);
                intent.putExtra("value","1");
                startActivity(intent);
            }
        });

        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChairmanStudentFragementAgeingAnalysisDetails.class);
                intent.putExtra("value","2");
                startActivity(intent);
            }
        });

        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChairmanStudentFragementAgeingAnalysisDetails.class);
                intent.putExtra("value","3");
                startActivity(intent);
            }
        });

        type4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChairmanStudentFragementAgeingAnalysisDetails.class);
                intent.putExtra("value","4");
                startActivity(intent);
            }
        });*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        getStudentFeedList();
    }

    private void initData() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES_AGEING_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId +"&ins_id=" + Preferences.getInstance().institutionId);
            if (e == null) {
                chairmanStudentFragmentAgeingFeesArray = null;
            } else {
                chairmanStudentFragmentAgeingFeesArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentFragmentAgeingFeesArray != null) {
            try {
                total1.setText(Rs+chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("total_fee_amount")+"/-");
                total2.setText(Rs+chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("total_fee_amount")+"/-");
                total3.setText(Rs+chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("total_fee_amount")+"/-");
                total4.setText(Rs+chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("total_fee_amount")+"/-");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    protected void getStudentFeedList() {

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES_AGEING_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId +"&ins_id=" + Preferences.getInstance().institutionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
               // System.out.println(response);
               // System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getActivity(), "No Records Found");
                        top.setVisibility(View.GONE);
                    }
                    else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("fee")) {
                        chairmanStudentFragmentAgeingFeesArray = new JSONObject(response).getJSONArray("fee");
                        if (null != chairmanStudentFragmentAgeingFeesArray && chairmanStudentFragmentAgeingFeesArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanStudentFragmentAgeingFeesArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FRAGMENT_STUDENT_FEES_AGEING_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId +"&ins_id=" + Preferences.getInstance().institutionId, e);
                            total1.setText(Rs+chairmanStudentFragmentAgeingFeesArray.getJSONObject(0).getString("total_fee_amount")+"/-");
                            total2.setText(Rs+chairmanStudentFragmentAgeingFeesArray.getJSONObject(1).getString("total_fee_amount")+"/-");
                            total3.setText(Rs+chairmanStudentFragmentAgeingFeesArray.getJSONObject(2).getString("total_fee_amount")+"/-");
                            total4.setText(Rs+chairmanStudentFragmentAgeingFeesArray.getJSONObject(3).getString("total_fee_amount")+"/-");



                        }
                    } else
                        Utils.showToast(getActivity(), "Error Fetching Response");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                    top.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
                top.setVisibility(View.GONE);

            }
        }) {
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
			}*/
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else {
            Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
        }
    }

    private void toa() {
        System.out.println("aaa");
    }
}

