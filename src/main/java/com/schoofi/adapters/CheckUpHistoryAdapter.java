package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Schoofi on 12-02-2018.
 */

public class CheckUpHistoryAdapter extends BaseAdapter {

    private Context context;
    private JSONArray checkUpHistoryArray;
    String date1,date2,date3,date4;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public CheckUpHistoryAdapter(Context context, JSONArray checkUpHistoryArray) {
        this.context = context;
        this.checkUpHistoryArray = checkUpHistoryArray;
    }

    @Override
    public int getCount() {
        return checkUpHistoryArray.length();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder;

        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.check_up_listview, null);

            holder.eventName = (TextView) convertView.findViewById(R.id.text_event_name);
            holder.doctorName = (TextView) convertView.findViewById(R.id.text_doctor_name);
            holder.testName = (TextView) convertView.findViewById(R.id.text_test_name);
            holder.remarks = (TextView) convertView.findViewById(R.id.text_remarks);
            holder.additionalremarks = (TextView) convertView.findViewById(R.id.text_additional_remarks);
            holder.testDate = (TextView) convertView.findViewById(R.id.text_organised_date);
            holder.organiserName = (TextView) convertView.findViewById(R.id.text_organiser_name);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.eventName.setText("Event Name: "+checkUpHistoryArray.getJSONObject(position).getString("program_name"));
            holder.organiserName.setText("Organised By: "+checkUpHistoryArray.getJSONObject(position).getString("organiser_name"));
            holder.doctorName.setText("Doctor: "+checkUpHistoryArray.getJSONObject(position).getString("doctor_name"));
            holder.testName.setText("Test: "+checkUpHistoryArray.getJSONObject(position).getString("test_name"));

            if(checkUpHistoryArray.getJSONObject(position).getString("filled_date").matches("0000-00-00"))
            {
                holder.testDate.setText("Test Date:");
            }

            else
            {
                date1 = checkUpHistoryArray.getJSONObject(position).getString("filled_date");
                try {
                    date5 = formatter.parse(date1);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date3 = formatter1.format(date5);
                holder.testDate.setText("Test Date: "+date3);
            }

            if(checkUpHistoryArray.getJSONObject(position).getString("remarks").matches(""))
            {
                holder.remarks.setVisibility(View.GONE);
            }

            else
            {
                holder.remarks.setVisibility(View.VISIBLE);
                holder.remarks.setText("Remarks: "+checkUpHistoryArray.getJSONObject(position).getString("remarks"));
            }

            if(checkUpHistoryArray.getJSONObject(position).getString("addnl_remarks").matches(""))
            {
                holder.additionalremarks.setVisibility(View.GONE);
            }

            else
            {
                holder.additionalremarks.setVisibility(View.VISIBLE);
                holder.additionalremarks.setText("Additional Remarks: "+checkUpHistoryArray.getJSONObject(position).getString("addnl_remarks"));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    static class Holder
    {
        private TextView eventName,organiserName,doctorName,testName,testDate,remarks,additionalremarks;
    }
}
