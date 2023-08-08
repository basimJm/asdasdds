package com.schoofi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.CheckUpFormActivity;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.StudentFeedBackImages;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CheckUpFormVO;
import com.schoofi.utils.CheckUpFormVO1;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VaccinationVO;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schoofi on 15-02-2018.
 */

public class CheckUpFormAdapter extends BaseAdapter {

    private Context context;
    private JSONArray checkUpFormArray;
    ArrayList<String> checkUpName;
    ArrayList<CheckUpFormVO> checkUpId = new ArrayList<CheckUpFormVO>();

    ArrayList<String> checkUpName1;
    ArrayList<CheckUpFormVO1> checkUpId1 = new ArrayList<CheckUpFormVO1>();

    JSONObject jsonobject;
    JSONArray jsonarray;
    public ArrayList myItems = new ArrayList();
    public ArrayList myItems1 = new ArrayList();
    String value2,eventId,additionalRemarks;
    int pos,rema;


    public CheckUpFormAdapter(Context context, JSONArray checkUpFormArray,String eventId) {
        this.context = context;
        this.checkUpFormArray = checkUpFormArray;
        this.eventId = eventId;

        for(int i=0;i<checkUpFormArray.length();i++)
        {
            try {
                myItems.add(checkUpFormArray.getJSONObject(i).getString("remarks"));
                myItems1.add(checkUpFormArray.getJSONObject(i).getString("addnl_remarks"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    @Override
    public int getCount() {
        return checkUpFormArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder;

        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.check_up_form_listview, null);
            holder.testName = (TextView) convertView.findViewById(R.id.text_name);
            holder.remarks = (EditText) convertView.findViewById(R.id.edit_remarks);
            holder.additionalRemarks = (EditText) convertView.findViewById(R.id.edit_additional_remarks);
            holder.save = (Button) convertView.findViewById(R.id.btn_save);
            holder.edit = (Button) convertView.findViewById(R.id.btn_edit);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {


            holder.testName.setText(checkUpFormArray.getJSONObject(position).getString("test_name"));
            if(checkUpFormArray.getJSONObject(position).getString("remarks").matches("0"))
            {
                holder.save.setEnabled(true);

                holder.edit.setEnabled(false);
                holder.edit.setBackgroundResource(R.drawable.button_red_50_percent);
            }

            else
            {
                holder.remarks.setText(checkUpFormArray.getJSONObject(position).getString("remarks"));
                holder.save.setEnabled(false);
                holder.save.setBackgroundResource(R.drawable.button_green_50_percent);
                holder.edit.setEnabled(true);
                holder.edit.setBackgroundResource(R.drawable.buttons);
            }

            if(checkUpFormArray.getJSONObject(position).getString("addnl_remarks").matches("0"))
            {

            }

            else
            {
                holder.additionalRemarks.setText(checkUpFormArray.getJSONObject(position).getString("addnl_remarks"));
            }

            holder.save.setTag(new Integer(position));

            holder.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*try {
                        image = studentEventListArray.getJSONObject((Integer) v.getTag()).getString("images");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/



                    if(holder.remarks.getText().toString().matches(""))
                    {
                        Utils.showToast(context.getApplicationContext(),"Please fill remarks");
                    }

                    else {
                        value2 = "ins";
                        pos = (Integer)v.getTag();
                        postMessage1();
                    }



                }
            });

            holder.ref = position;


            holder.edit.setTag(new Integer(position));

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*try {
                        image = studentEventListArray.getJSONObject((Integer) v.getTag()).getString("images");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                    if(holder.remarks.getText().toString().matches(""))
                    {
                        Utils.showToast(context.getApplicationContext(),"Please fill remarks");
                    }





                    else {





                              value2 = "up";
                              pos = (Integer) v.getTag();
                              //additionalRemarks = holder.additionalRemarks.getText().toString();
                              postMessage1();





                    }

                }
            });

            holder.remarks.setTag(new Integer(position));
            holder.additionalRemarks.setTag(new Integer(position));

           /* holder.remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final int position = (Integer) v.getTag();
                    final EditText Caption = (EditText) v;

                    *//*CheckUpFormVO checkFormVO = checkUpId.get(position);


                    checkUpId.get(position).setCheckUpValue(Caption.getText().toString());*//*
                    myItems.set(position,Caption.getText().toString());
                }
            });*/

            holder.remarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {


                     myItems.set(holder.ref,s.toString());
                }
            });

            holder.additionalRemarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    myItems1.set(holder.ref,s.toString());
                }
            });


            /*holder.additionalRemarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        final int position = (Integer) v.getTag();
                        final EditText Caption1 = (EditText) v;

                        *//*CheckUpFormVO1 checkFormVO1 = checkUpId1.get(position);


                        checkUpId1.get(position).setAdditionalRemarks(Caption1.getText().toString());*//*
                        myItems1.set(position,Caption1.getText().toString());


                    }
                }
            });*/

           /* holder.remarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_NEXT) {

                        rema =1;

                        final int position = (Integer) v.getTag();
                        final EditText Caption = (EditText) v;

                    *//*CheckUpFormVO checkFormVO = checkUpId.get(position);


                    checkUpId.get(position).setCheckUpValue(Caption.getText().toString());*//*
                        myItems.set(position,Caption.getText().toString());

                        return true;
                    }
                    return false;

                }
            });*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        private TextView testName;
        private EditText remarks,additionalRemarks;
        private Button save,edit;
        int ref;
    }


    private void postMessage1()
    {

        RequestQueue queue = VolleySingleton.getInstance(context.getApplicationContext()).getRequestQueue();


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADD_CHECK_UP_DETAILS/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(context.getApplicationContext(),"Error Submitting Vaccine Details");


                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(context.getApplicationContext(), "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("testList"))
                    {

                        Utils.showToast(context.getApplicationContext(),"Successfully Saved!");
                        Intent intent = new Intent(context, CheckUpFormActivity.class);
                        intent.putExtra("event_id",eventId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);

                        ((Activity)context).finish();

                    }

                    else {

                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(context.getApplicationContext(), "Error submitting! Please try after sometime.");
                }
               // setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(context.getApplicationContext(), "Error submitting! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Preferences.getInstance().loadPreference(context.getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                try {


                    params.put("token",Preferences.getInstance().token);
                    params.put("device_id", Preferences.getInstance().deviceId);
                    params.put("event_id",eventId);
                    params.put("user_id",Preferences.getInstance().userId);
                    params.put("remarks",myItems.get(pos).toString());
                    params.put("addnl_remarks",myItems1.get(pos).toString());
                    params.put("test_id",checkUpFormArray.getJSONObject(pos).getString("test_id"));
                    params.put("update_or_add",value2);
                    params.put("stu_id",Preferences.getInstance().studentId);
                    params.put("ins_id",Preferences.getInstance().institutionId);
                    params.put("sch_id",Preferences.getInstance().schoolId);

                    Log.d("params",Preferences.getInstance().token+"d"+Preferences.getInstance().deviceId+"e"+eventId+"u"+Preferences.getInstance().userId+"r"+myItems.get(pos).toString()+"ad"+myItems1.get(pos).toString()+"t"+checkUpFormArray.getJSONObject(pos).getString("test_id")+"u"+value2+"st"+Preferences.getInstance().studentId+"in"+Preferences.getInstance().institutionId+"sch"+Preferences.getInstance().schoolId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }




                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(context.getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(context.getApplicationContext(), "Unable to submit data, kindly enable internet settings!");
            //setSupportProgressBarIndeterminateVisibility(false);
        }
    }



}


