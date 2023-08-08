package com.schoofi.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
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
 * Created by Schoofi on 27-03-2019.
 */

public class ChairmanEmployeeAnalysisAdapter extends BaseAdapter {

    private Context context;
    private JSONArray employeeAttendanceArray;

    String date1,date2;
    Date date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ChairmanEmployeeAnalysisAdapter(Context context,JSONArray employeeAttendanceArray) {

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
            convertView = layoutInflater.inflate(R.layout.employee_attendance_listview, null);
            holder.date = (TextView) convertView.findViewById(R.id.text_date);
            holder.inTime = (TextView) convertView.findViewById((R.id.text_in_time));
            holder.outTime = (TextView) convertView.findViewById((R.id.text_out_time));
            holder.attendance = (ImageView) convertView.findViewById((R.id.img__daily_attendance));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }


        try {
            date1 = employeeAttendanceArray.getJSONObject(position).getString("attendance_date");
            try {
                date3 = formatter.parse(date1);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            date2 = formatter1.format(date3);

            holder.date.setText(date2);
            holder.inTime.setText((employeeAttendanceArray.getJSONObject(position).getString("in_time")));
            holder.outTime.setText((employeeAttendanceArray.getJSONObject(position).getString("out_time")));
            if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("P")) {
                holder.attendance.setImageResource(R.drawable.green_tick);
            } else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("A")) {
                holder.attendance.setImageResource(R.drawable.red);
            } else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("CL"))
            {
                holder.attendance.setImageResource(R.drawable.cl);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("EL"))
            {
                holder.attendance.setImageResource(R.drawable.el);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("LWP"))
            {
                holder.attendance.setImageResource(R.drawable.lwp);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("H"))
            {
                holder.attendance.setImageResource(R.drawable.hd);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("DL"))
            {
                holder.attendance.setImageResource(R.drawable.dl);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("SP"))
            {
                holder.attendance.setImageResource(R.drawable.sp);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("CPL"))
            {
                holder.attendance.setImageResource(R.drawable.cpl);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }
            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("OT"))
            {
                holder.attendance.setImageResource(R.drawable.ot);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("OD"))
            {
                holder.attendance.setImageResource(R.drawable.od);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("RH"))
            {
                holder.attendance.setImageResource(R.drawable.rh);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("VL"))
            {
                holder.attendance.setImageResource(R.drawable.vl);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("SL"))
            {
                holder.attendance.setImageResource(R.drawable.sl);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("WO"))
            {
                holder.attendance.setImageResource(R.drawable.wo);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("HA"))
            {
                holder.attendance.setImageResource(R.drawable.ha);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("HCL"))
            {
                holder.attendance.setImageResource(R.drawable.hcl);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("HLWP"))
            {
                holder.attendance.setImageResource(R.drawable.hlwp);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }
            else if (employeeAttendanceArray.getJSONObject(position).getString("attendance").equals("SPL"))
            {
                holder.attendance.setImageResource(R.drawable.spl);

                holder.attendance.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }
            else
            {
                holder.attendance.setImageResource((R.drawable.leaveicon));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return convertView;
    }

    static  class  Holder
    {
        private TextView date,inTime,outTime;
        private ImageView attendance;
    }
}
