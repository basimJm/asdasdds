package com.schoofi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.ParentInnerHomeScreen;
import com.schoofi.activitiess.R;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 10-05-2019.
 */

public class StudentSubjectCollegeAdapter extends BaseAdapter {
    private Context context;
    private JSONArray teacherHomeScreenArray;
    private String value;


    public StudentSubjectCollegeAdapter(Context context,JSONArray teacherHomeScreenArray,String value) {

        this.context = context;
        this.teacherHomeScreenArray = teacherHomeScreenArray;
        this.value = value;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return teacherHomeScreenArray.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        try {
            return teacherHomeScreenArray.getJSONObject(position);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder;
        if(convertView == null)
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.teacher_homescreen_listview_item, null);
            holder.teacherHomeScreenListItemButton = (TextView) convertView.findViewById(R.id.btn_teacher_homescreen_class);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            Preferences.getInstance().loadPreference(context);
            if(value.matches("1"))
            {
               holder.teacherHomeScreenListItemButton.setText((teacherHomeScreenArray.getJSONObject(position).getString("subject_name")+"-"+teacherHomeScreenArray.getJSONObject(position).getString("present_attendance")+"/"+teacherHomeScreenArray.getJSONObject(position).getString("total_attendance")));
                int total_attendance=0,present_attendance=0;
                float attendance_percentage =0;
                total_attendance = (total_attendance+Integer.parseInt(teacherHomeScreenArray.getJSONObject(position).getString("total_attendance")));
                present_attendance = (present_attendance+Integer.parseInt(teacherHomeScreenArray.getJSONObject(position).getString("present_attendance")));
                if(total_attendance==0)
                {
                    attendance_percentage = 0;

                }
                else
                {
                    attendance_percentage = (present_attendance / total_attendance) * 100;
                }

                if(attendance_percentage>Integer.parseInt(teacherHomeScreenArray.getJSONObject(position).getString("threshold")))
                {
                   holder.teacherHomeScreenListItemButton.setTextColor(Color.parseColor("#009f4d"));
                }
                else
                    if(attendance_percentage>Integer.parseInt(teacherHomeScreenArray.getJSONObject(position).getString("threshold")))
                    {
                        holder.teacherHomeScreenListItemButton.setTextColor(Color.parseColor("#ee4749"));
                    }
            }
            else
                if(value.matches("3"))
                {
                    holder.teacherHomeScreenListItemButton.setText(teacherHomeScreenArray.getJSONObject(position).getString("subject"));
                }
            else {
                if(Preferences.getInstance().schoolType.matches("College")) {
                    holder.teacherHomeScreenListItemButton.setText(teacherHomeScreenArray.getJSONObject(position).getString("subject"));
                }
                else
                {
                    holder.teacherHomeScreenListItemButton.setText(teacherHomeScreenArray.getJSONObject(position).getString("subject_name"));
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    static class Holder
    {
        private TextView teacherHomeScreenListItemButton;
    }
}
