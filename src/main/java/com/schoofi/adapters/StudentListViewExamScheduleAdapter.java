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

public class StudentListViewExamScheduleAdapter extends BaseAdapter{

	private Context context;
	private JSONArray studentExamArray;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date1,date2;
	Date date3;
	public StudentListViewExamScheduleAdapter(Context context,JSONArray studentExamArray) 
	{
		this.context = context;
		this.studentExamArray = studentExamArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentExamArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return studentExamArray.getJSONObject(position);
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
			convertView = layoutInflater.inflate(R.layout.student_exam_schedule_listview, null);
			holder.examDate = (TextView) convertView.findViewById(R.id.text_student_exam_date);
			holder.examSubject = (TextView) convertView.findViewById(R.id.text_student_exam_subject);
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			date1 = studentExamArray.getJSONObject(position).getString("exam_date");

			if(date1.matches("") || date1.matches("null")|| date1.matches("0000-00-00"))
			{
				holder.examDate.setText("NA");
			}

			else {
				try {
					date3 = formatter.parse(date1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				date2 = formatter1.format(date3);
				holder.examDate.setText(date2);
			}
			//holder.examDate.setText(studentExamArray.getJSONObject(position).getString("exam_date"));
			holder.examSubject.setText(studentExamArray.getJSONObject(position).getString("subject"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class Holder
	{
		TextView examDate,examSubject;
	}

}
