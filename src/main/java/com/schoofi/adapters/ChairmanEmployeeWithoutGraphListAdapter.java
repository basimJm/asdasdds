package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Schoofi on 10-04-2019.
 */

public class ChairmanEmployeeWithoutGraphListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray employeeAttendanceArray;

    String date1,date2;
    Date date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ChairmanEmployeeWithoutGraphListAdapter(Context context,JSONArray employeeAttendanceArray) {

        this.context = context;
        this.employeeAttendanceArray = employeeAttendanceArray;
    }

    @Override
    public int getCount() {
        return employeeAttendanceArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return employeeAttendanceArray.getJSONObject(position);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null)
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.employee_attendance_without_graph_listview_layout, null);
            holder.date = (TextView) convertView.findViewById(R.id.text_date);
            holder.inTime = (TextView) convertView.findViewById((R.id.text_in_time));
            holder.outTime = (TextView) convertView.findViewById((R.id.text_out_time));
            holder.leave = (TextView) convertView.findViewById((R.id.text_out_time1));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }


        try {
            date1 = employeeAttendanceArray.getJSONObject(position).getString("crr_date");
            try {
                date3 = formatter.parse(date1);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            date2 = formatter1.format(date3);

            holder.date.setText(date2);
            String total,present,absent,leave;
            total = employeeAttendanceArray.getJSONObject(position).getString("total_attendance");
            present = employeeAttendanceArray.getJSONObject(position).getString("present");
            absent = employeeAttendanceArray.getJSONObject(position).getString("absent");
            leave = employeeAttendanceArray.getJSONObject(position).getString("leave");
            if(total.matches("null"))
            {
                total = "0";
            }
            else
            {

            }

            if(present.matches("null"))
            {
                present = "0";
            }
            else
            {
              
            }

            if(absent.matches("null"))
            {
                absent = "0";
            }
            else
            {

            }

            if(leave.matches("null"))
            {
                leave = "0";
            }
            else
            {

            }
            holder.inTime.setText((present+"/"+total));
            holder.outTime.setText((absent+"/"+total));
            holder.leave.setText((leave+"/"+total));
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return convertView;
    }

    static  class  Holder
    {
        private TextView date,inTime,outTime,leave;

    }
}
