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
 * Created by Schoofi on 5/4/2016.
 */
public class TeacherAllFeedBackAdapter extends BaseAdapter {

    private Context context;
    private JSONArray teacherFeedBackAssignedArray1;
    String date1,date2,date4;
    Date date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public TeacherAllFeedBackAdapter(Context context, JSONArray teacherFeedBackAssignedArray1) {
        this.context = context;
        this.teacherFeedBackAssignedArray1= teacherFeedBackAssignedArray1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return teacherFeedBackAssignedArray1.length();
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
        {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.teacher_feedback_list_item, null);
            holder.chairmanStudentFeedbackName = (TextView) convertView.findViewById(R.id.text_user_name);
            //holder.chairmanStudentFeedbckClass = (TextView) convertView.findViewById(R.id.text_class_name);
            holder.chairmanStudentFeedbackType = (TextView) convertView.findViewById(R.id.text_type);
            holder.chairmanStudentFeedbackDate = (TextView) convertView.findViewById(R.id.text_student_feedBack_date);
            holder.chairmanStudentFeedbackDescription = (TextView) convertView.findViewById(R.id.text_student_feedBack_description);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            date1 = teacherFeedBackAssignedArray1.getJSONObject(position).getString("created_date");
            date2 = date1.substring(0, 10);

            try {
                date3 = formatter.parse(date2);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }




            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            date4 = formatter1.format(date3);
            holder.chairmanStudentFeedbackName.setText(teacherFeedBackAssignedArray1.getJSONObject(position).getString("Name"));
            //holder.chairmanStudentFeedbckClass.setText("Class: "+teacherFeedBackAssignedArray.getJSONObject(position).getString("class_name"));
            holder.chairmanStudentFeedbackType.setText(teacherFeedBackAssignedArray1.getJSONObject(position).getString("full_name"));
            holder.chairmanStudentFeedbackDescription.setText(teacherFeedBackAssignedArray1.getJSONObject(position).getString("description"));
            holder.chairmanStudentFeedbackDate.setText(date4);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView chairmanStudentFeedbackName,chairmanStudentFeedbackType,chairmanStudentFeedbackDate,chairmanStudentFeedbackDescription;
        ImageView next;
    }
}
