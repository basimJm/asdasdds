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

public class DiaryStudentListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray diaryStudentArray;

    public DiaryStudentListAdapter(Context context, JSONArray diaryStudentArray) {
        this.context = context;
        this.diaryStudentArray = diaryStudentArray;
    }

    @Override
    public int getCount() {
        return diaryStudentArray.length();
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
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.diary_student_listview, null);

            holder.studentName = convertView.findViewById(R.id.text_student_leave_title);
            holder.studentRoll = convertView.findViewById(R.id.text_employee_name);
            convertView.setTag(holder);


        }
        else
        {
          holder = (Holder) convertView.getTag();
        }

        try {
            holder.studentName.setText("Name: "+diaryStudentArray.getJSONObject(position).getString("stu_name"));
            holder.studentRoll.setText("Roll No.: "+diaryStudentArray.getJSONObject(position).getString("class_roll_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

        static class Holder
        {
            private TextView studentName,studentRoll;
        }
}
