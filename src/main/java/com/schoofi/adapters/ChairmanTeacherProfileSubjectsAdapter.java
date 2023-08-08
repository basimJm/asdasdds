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
 * Created by Schoofi on 13-07-2018.
 */

public class ChairmanTeacherProfileSubjectsAdapter extends BaseAdapter {
    private Context context;
    //private ArrayList<StudentDailyAttendanceVO> studentDailyAttendanceList;
    private JSONArray studentDailyAttendanceArray;
    String date1,date2;
    Date date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    public ChairmanTeacherProfileSubjectsAdapter(Context context,JSONArray studentDailyAttendanceArray)
    {
        this.context = context;
        this.studentDailyAttendanceArray = studentDailyAttendanceArray;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return studentDailyAttendanceArray.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if(convertView == null)
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chairman_class_subjects_list, null);
            holder.studentDailyAttendanceDate = (TextView) convertView.findViewById(R.id.text_student_daily_attendance_date);
            holder.studentDailyAttendanceImage = (TextView) convertView.findViewById(R.id.text_student_daily_attendance);
            //Log.d("hhh", "harsh"+567);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {




            holder.studentDailyAttendanceDate.setText(studentDailyAttendanceArray.getJSONObject(position).getString("class_name")+"-"+studentDailyAttendanceArray.getJSONObject(position).getString("section_name"));
            holder.studentDailyAttendanceImage.setText(studentDailyAttendanceArray.getJSONObject(position).getString("subject"));
            //holder.studentDailyAttendanceDate.setText((studentDailyAttendanceArray.getJSONObject(position).getString("date")));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //holder.studentDailyAttendanceImage.setImageResource(studentDailyAttendanceArray.get(position).getIconResId());

        return convertView;
    }

    static class Holder
    {
        TextView studentDailyAttendanceDate,studentDailyAttendanceImage;
    }
}
