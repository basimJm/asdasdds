package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 18-05-2019.
 */

public class StudentSubjectElectiveAdapter extends BaseAdapter {

    private Context context ;
    private JSONArray studentSubjectElectiveArray;

    public StudentSubjectElectiveAdapter(Context context, JSONArray studentSubjectElectiveArray) {
        this.context = context;
        this.studentSubjectElectiveArray = studentSubjectElectiveArray;
    }

    @Override
    public int getCount() {
        return studentSubjectElectiveArray.length();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if(convertView == null)
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.student_elective_subject_choose_listview, null);
            holder.subjectName= (TextView) convertView.findViewById(R.id.txt_studentNameTeacher);
            holder.mark = (ImageView) convertView.findViewById(R.id.imageView_studentAttendance);
            holder.linearLayout = convertView.findViewById(R.id.linear_listView_teacherStudentAttendanceDetails);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            Preferences.getInstance().loadPreference(context);
            holder.subjectName.setText(studentSubjectElectiveArray.getJSONObject(position).getString("subject_name"));
            if(studentSubjectElectiveArray.getJSONObject(position).getString("isAdded").matches("") || studentSubjectElectiveArray.getJSONObject(position).getString("isAdded").matches("0"))
            {
                holder.mark.setImageResource(R.drawable.greycircletick);
                studentSubjectElectiveArray.getJSONObject(position).put("isAdded","0");
            }
            else
            {
                holder.mark.setImageResource(R.drawable.greencircletick);
                studentSubjectElectiveArray.getJSONObject(position).put("isAdded","1");
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if(studentSubjectElectiveArray.getJSONObject(position).getString("isAdded").matches("") || studentSubjectElectiveArray.getJSONObject(position).getString("isAdded").matches("0"))
                        {
                            holder.mark.setImageResource(R.drawable.greencircletick);
                            studentSubjectElectiveArray.getJSONObject(position).put("isAdded","1");

                        }
                        else
                        {
                            holder.mark.setImageResource(R.drawable.greycircletick);
                            studentSubjectElectiveArray.getJSONObject(position).put("isAdded","0");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    static class Holder
    {
        private TextView subjectName;
        private ImageView mark;
        private LinearLayout linearLayout;
    }
}
