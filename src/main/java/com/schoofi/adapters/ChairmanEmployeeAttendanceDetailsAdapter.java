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

/**
 * Created by Schoofi on 15-04-2019.
 */

public class ChairmanEmployeeAttendanceDetailsAdapter extends BaseAdapter {

    private Context context;
    private JSONArray chairmanEmployeeAttendanceDetailsArray;

    public ChairmanEmployeeAttendanceDetailsAdapter(Context context, JSONArray chairmanEmployeeAttendanceDetailsArray) {
        this.context = context;
        this.chairmanEmployeeAttendanceDetailsArray = chairmanEmployeeAttendanceDetailsArray;
    }

    @Override
    public int getCount() {
        return chairmanEmployeeAttendanceDetailsArray.length();
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
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.employee_attendance_without_graph_listview_layout, null);
            holder.departmentName = (TextView) convertView.findViewById(R.id.text_date);
            holder.present = (TextView) convertView.findViewById((R.id.text_in_time));
            holder.absent = (TextView) convertView.findViewById((R.id.text_out_time));
            holder.leave = (TextView) convertView.findViewById((R.id.text_out_time1));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }


        try {


            holder.departmentName.setText(chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("emp_first_name")+" "+chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("emp_last_name"));
            int total=0,present=0,absent=0,leave=0;
            if(chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("present").matches("null") || chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("present").matches(""))
            {
                present = 0;
            }
            else
            {
                present = Integer.parseInt(chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("present"));
            }

            if(chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("absent").matches("null") || chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("absent").matches(""))
            {
                absent = 0;
            }

            else
            {
                absent = Integer.parseInt(chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("absent"));
            }

            if(chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("leaves").matches("null") || chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("leaves").matches(""))
            {
                leave = 0;
            }

            else
            {
                leave = Integer.parseInt(chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("leaves"));
            }

            if(chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("total_attendance").matches("null") || chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("total_attendance").matches(""))
            {
                total = 0;
            }

            else
            {
                total = Integer.parseInt(chairmanEmployeeAttendanceDetailsArray.getJSONObject(position).getString("total_attendance"));
            }



                holder.present.setText(present+"/"+total);
                holder.absent.setText(absent+"/"+total);
                holder.leave.setText(leave+"/"+total);




        } catch (JSONException e) {
            e.printStackTrace();
        }




        return convertView;
    }

    static class Holder
    {
        private TextView departmentName,present,absent,leave;
    }
}
