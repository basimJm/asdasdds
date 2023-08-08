package com.schoofi.adapters;

import android.content.Context;
import android.graphics.Color;
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
import java.util.Locale;

/**
 * Created by Schoofi on 18-06-2018.
 */

public class TurnstileDetailsLogAdapter extends BaseAdapter {

    private Context context;
    private JSONArray turnstileDetailsLogArray;
    private int value;
    SimpleDateFormat myDateFormat1 = new SimpleDateFormat("d MMMM yyyy HH:mm:ss", java.util.Locale.getDefault());
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yyyy HH:mm:ss");
    final String date1="";
    String date2="";
    String date56="";

    public TurnstileDetailsLogAdapter(Context context, JSONArray turnstileDetailsLogArray,int value) {
        this.context = context;
        this.turnstileDetailsLogArray = turnstileDetailsLogArray;
        this.value = value;

    }

    @Override
    public int getCount() {
        return turnstileDetailsLogArray.length();
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
        Holder holder;

        if(convertView == null)
        {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.turnstile_details_listview_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.text_student_daily_name);
            holder.zoneName = (TextView) convertView.findViewById(R.id.text_student_daily_zone_name);
            holder.inTime = (TextView) convertView.findViewById(R.id.text_student_daily_in_time);
            holder.outTime = (TextView) convertView.findViewById(R.id.text_student_out_time);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.name.setText(turnstileDetailsLogArray.getJSONObject(position).getString("name_on_card"));
            holder.zoneName.setText(turnstileDetailsLogArray.getJSONObject(position).getString("zone_name"));
            holder.inTime.setText(turnstileDetailsLogArray.getJSONObject(position).getString("in_out"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");

            Date date3=null ;
            Date date4=null;
            Date date5 = null;
            Date date6 = null;
            String demoDate;
            try {
                date3 = formatter.parse(turnstileDetailsLogArray.getJSONObject(position).getString("date_time"));
                date4 = formatter1.parse(turnstileDetailsLogArray.getJSONObject(position).getString("start_time"));
                date5 = formatter1.parse(turnstileDetailsLogArray.getJSONObject(position).getString("end_time"));
                demoDate = formatter1.format(date3);
                date6 = formatter1.parse(demoDate);

            } catch (ParseException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm ");
            String date2="";
            date2 = sdf.format(date3);

            long milliseconds = date4.getTime();
            long milliseconds1 = date5.getTime();
            long milliseconds2 = date6.getTime();

            if(value == 1) {

                if (turnstileDetailsLogArray.getJSONObject(position).getString("in_out").matches("IN")) {
                    if (milliseconds < milliseconds2) {
                        holder.outTime.setTextColor(Color.parseColor("#EE4749"));
                    } else {
                        holder.outTime.setTextColor(Color.parseColor("#009F4D"));
                    }
                } else {
                    if (milliseconds1 > milliseconds2) {
                        holder.outTime.setTextColor(Color.parseColor("#EE4749"));
                    } else {
                        holder.outTime.setTextColor(Color.parseColor("#009F4D"));
                    }
                }
            }
            else
            {

            }

            holder.outTime.setText(date2);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        private TextView name,zoneName,inTime,outTime;
    }
}
