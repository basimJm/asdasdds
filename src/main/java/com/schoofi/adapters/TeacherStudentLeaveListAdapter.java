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

public class TeacherStudentLeaveListAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray teacherStudentLeaveListArray;
	String date1,date2,date3,date4;
	Date date5,date6;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public TeacherStudentLeaveListAdapter(Context context,JSONArray teacherStudentLeaveListArray) {
		super();
		this.context = context;
		this.teacherStudentLeaveListArray = teacherStudentLeaveListArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return teacherStudentLeaveListArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return teacherStudentLeaveListArray.getJSONObject(position);
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
			convertView = layoutInflater.inflate(R.layout.teacher_student_leave_listview, null);
			holder.studentName = (TextView) convertView.findViewById(R.id.text_student_name);
			holder.fromDate = (TextView) convertView.findViewById(R.id.text_teacher_leave_starting_date);
			holder.toDate = (TextView) convertView.findViewById(R.id.text_teacher_leave_ending_date);
			holder.leaveSubject = (TextView) convertView.findViewById(R.id.text_teacher_leave_subject);
			holder.statusImage = (ImageView) convertView.findViewById(R.id.imageViewTeacherLeaveStatus);
			convertView.setTag(holder);
			
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			date1 = teacherStudentLeaveListArray.getJSONObject(position).getString("from_date");
			date2 = teacherStudentLeaveListArray.getJSONObject(position).getString("to_date");
			
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
			
			holder.studentName.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("stu_name"));
			//holder.fromDate.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("from_date"));
			//holder.toDate.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("to_date"));
			holder.fromDate.setText(date3);
			holder.toDate.setText(date4);
			holder.leaveSubject.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("subject"));
			
			
			if(teacherStudentLeaveListArray.getJSONObject(position).getString("status").matches("0"))
			{
				holder.statusImage.setImageResource(R.drawable.neworange);
			}
			
			else
				if(teacherStudentLeaveListArray.getJSONObject(position).getString("status").matches("1"))
				{
					holder.statusImage.setImageResource(R.drawable.green_tick);
				}
			
				else
					if(teacherStudentLeaveListArray.getJSONObject(position).getString("status").matches("2"))
					{
						holder.statusImage.setImageResource(R.drawable.red);
					}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
static class Holder
{
	TextView studentName,leaveSubject,fromDate,toDate;
	ImageView statusImage;
}
}
