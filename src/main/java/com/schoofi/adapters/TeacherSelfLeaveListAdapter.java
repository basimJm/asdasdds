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
 * Created by Schoofi on 12-06-2018.
 */

public class TeacherSelfLeaveListAdapter extends BaseAdapter {
    private Context context;
    private JSONArray studentLeaveListArray;
    String date1,date2,date3,date4,value;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    public TeacherSelfLeaveListAdapter(Context context,JSONArray studentLeaveListArray,String value)
    {
        this.context = context;
        this.studentLeaveListArray = studentLeaveListArray;
        this.value = value;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return studentLeaveListArray.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        try {
            return studentLeaveListArray.getJSONObject(position);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder;

        if(convertView == null)
        {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.student_leave_listitem, null);

            holder.studentLeaveEndingDateTextView = (TextView) convertView.findViewById(R.id.text_student_leave_ending_date);
            holder.studentLeaveTitleTextView = (TextView) convertView.findViewById(R.id.text_student_leave_title);
            holder.studentLeaveStartingDateTextView = (TextView) convertView.findViewById(R.id.text_student_leave_starting_date);
            holder.studentLeaveStatusImageView = (ImageView) convertView.findViewById(R.id.imageViewLeaveStatus);
            holder.approvals = (TextView) convertView.findViewById(R.id.text_approvals);
            holder.employeeName = (TextView) convertView.findViewById(R.id.text_employee_name);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {

            date1 = studentLeaveListArray.getJSONObject(position).getString("from_date");
            date2 = studentLeaveListArray.getJSONObject(position).getString("to_date");

            try {
                date5 = formatter.parse(date1);
                date6 = formatter.parse(date2);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            date3 = formatter1.format(date5);
            date4 = formatter1.format(date6);
            holder.studentLeaveTitleTextView.setText(studentLeaveListArray.getJSONObject(position).getString("leave_subject"));
            holder.studentLeaveStartingDateTextView.setText("From: "+date3);

            if(value.matches("1"))
            {
                holder.approvals.setVisibility(View.GONE);
                holder.employeeName.setVisibility(View.GONE);
            }
            else
                if(value.matches("2"))
                {
                    holder.approvals.setText(studentLeaveListArray.getJSONObject(position).getString("level"));
                    holder.employeeName.setVisibility(View.GONE);
                }
                else {

                    holder.approvals.setText(studentLeaveListArray.getJSONObject(position).getString("level"));
                    holder.employeeName.setText(studentLeaveListArray.getJSONObject(position).getString("employee_name"));
                }
            if(studentLeaveListArray.getJSONObject(position).getString("is_half_day").matches("1"))
            {
                holder.studentLeaveEndingDateTextView.setVisibility(View.GONE);
            }
            else {
                holder.studentLeaveEndingDateTextView.setVisibility(View.VISIBLE);
                holder.studentLeaveEndingDateTextView.setText("To: " + date4);
            }
            if(studentLeaveListArray.getJSONObject(position).getString("final_approval").matches("1")) {
                if (studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("0") || studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("null")) {
                    holder.studentLeaveStatusImageView.setImageResource(R.drawable.orangemark);
                } else if (studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("1")) {
                    holder.studentLeaveStatusImageView.setImageResource(R.drawable.green_tick);
                } else if (studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("2")) {
                    holder.studentLeaveStatusImageView.setImageResource(R.drawable.red);
                }
            }
            else
            {
                holder.studentLeaveStatusImageView.setImageResource(R.drawable.orangemark);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView studentLeaveTitleTextView,studentLeaveStartingDateTextView,studentLeaveEndingDateTextView,approvals,employeeName;
        ImageView studentLeaveStatusImageView;
    }
}
