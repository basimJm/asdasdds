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

public class TeacherStudentProgressUpdateAdapter  extends BaseAdapter {

    private Context context;
    private JSONArray tutorialListArray;
    String date1,date2,date3,date4,value;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public TeacherStudentProgressUpdateAdapter(Context context, JSONArray tutorialListArray) {
        this.context = context;
        this.tutorialListArray = tutorialListArray;
    }

    @Override
    public int getCount() {
        return tutorialListArray.length();
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
            convertView = layoutInflater.inflate(R.layout.tutorial_list_design, null);
            holder.title = convertView.findViewById(R.id.text_student_leave_title);
            holder.uploadedAt = convertView.findViewById(R.id.text_student_leave_starting_date);
            holder.uploadedBy = convertView.findViewById(R.id.text_employee_name);


            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.title.setText(tutorialListArray.getJSONObject(position).getString("stu_name"));

            holder.uploadedAt.setText("Roll No.: "+tutorialListArray.getJSONObject(position).getString("roll_no"));
            holder.uploadedBy.setText("Status: "+tutorialListArray.getJSONObject(position).getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        private TextView title,uploadedBy,uploadedAt;
    }
}
