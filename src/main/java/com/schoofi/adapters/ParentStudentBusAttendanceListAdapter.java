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
 * Created by Schoofi on 06-03-2017.
 */

public class ParentStudentBusAttendanceListAdapter extends BaseAdapter {

    private Context context;
    //private ArrayList<StudentDailyAttendanceVO> studentDailyAttendanceList;
    private JSONArray studentDailyAttendanceArray;
    String date1,date2;
    Date date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ParentStudentBusAttendanceListAdapter(Context context, JSONArray studentDailyAttendanceArray) {
        this.context = context;
        this.studentDailyAttendanceArray = studentDailyAttendanceArray;
    }

    @Override
    public int getCount() {
        return studentDailyAttendanceArray.length();
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
            convertView = layoutInflater.inflate(R.layout.parent_student_bus_attendance_listview_adapter, null);
            holder.studentDailyAttendanceDate = (TextView) convertView.findViewById(R.id.text_student_daily_attendance_date);
            holder.studentDailyAttendanceImage = (ImageView) convertView.findViewById(R.id.img_student_daily_attendance);
           // holder.studentDailyAttendanceImage1 = (ImageView) convertView.findViewById(R.id.img_student_daily_attendance1);
            //Log.d("hhh", "harsh"+567);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            date1 = studentDailyAttendanceArray.getJSONObject(position).getString("attendance_date");

            try {
                date3 = formatter.parse(date1);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            date2 = formatter1.format(date3);

            holder.studentDailyAttendanceDate.setText(date2+"("+studentDailyAttendanceArray.getJSONObject(position).getString("bus_route_no")+")");
            //holder.studentDailyAttendanceDate.setText((studentDailyAttendanceArray.getJSONObject(position).getString("date")));
            if (studentDailyAttendanceArray.getJSONObject(position).getString("attendance").equals("P")) {
                holder.studentDailyAttendanceImage.setImageResource(R.drawable.green_tick);
            } else if (studentDailyAttendanceArray.getJSONObject(position).getString("attendance").equals("A")) {
                holder.studentDailyAttendanceImage.setImageResource(R.drawable.red);
            }
            else if (studentDailyAttendanceArray.getJSONObject(position).getString("attendance").equals("L")) {
                holder.studentDailyAttendanceImage.setImageResource(R.drawable.leaveicon);
            }else if (studentDailyAttendanceArray.getJSONObject(position).getString("attendance").equals("") || studentDailyAttendanceArray.getJSONObject(position).getString("attendance").equals("null"))
            {
                holder.studentDailyAttendanceImage.setImageResource(R.drawable.leaveicon);
            }

           /* if (studentDailyAttendanceArray.getJSONObject(position).getString("morning_shift").equals("P")) {
                holder.studentDailyAttendanceImage.setImageResource(R.drawable.green_tick);
            } else if (studentDailyAttendanceArray.getJSONObject(position).getString("morning_shift").equals("A")) {
                holder.studentDailyAttendanceImage.setImageResource(R.drawable.red);
            }
            else if (studentDailyAttendanceArray.getJSONObject(position).getString("morning_shift").equals("L")) {
                holder.studentDailyAttendanceImage.setImageResource(R.drawable.leaveicon);
            }else if (studentDailyAttendanceArray.getJSONObject(position).getString("morning_shift").equals("") || studentDailyAttendanceArray.getJSONObject(position).getString("morning_shift").equals("null"))
            {
                holder.studentDailyAttendanceImage.setImageResource(R.drawable.leaveicon);
            }*/
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //holder.studentDailyAttendanceImage.setImageResource(studentDailyAttendanceArray.get(position).getIconResId());
        return convertView;
    }

    static class Holder
    {
        TextView studentDailyAttendanceDate;
        ImageView studentDailyAttendanceImage,studentDailyAttendanceImage1;
    }
}
