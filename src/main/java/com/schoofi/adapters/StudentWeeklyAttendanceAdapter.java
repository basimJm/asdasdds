package com.schoofi.adapters;

import android.content.Context;
import android.util.Log;
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

public class StudentWeeklyAttendanceAdapter extends BaseAdapter {
	
	private Context context;
	private JSONArray studentWeeklyAttendanceArray;
	String date1,date2;
	Date date3;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public StudentWeeklyAttendanceAdapter(Context context,JSONArray studentWeeklyAttendanceArray)
	{
		this.context = context;
		this.studentWeeklyAttendanceArray = studentWeeklyAttendanceArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentWeeklyAttendanceArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView == null)
		{   holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.student_weekly_attendance_listitem, null);
			holder.studentWeeklyAttendanceDate = (TextView) convertView.findViewById(R.id.text_student_weekly_attendance_date);
			holder.studentWeeklyAttendanceImage = (ImageView) convertView.findViewById(R.id.img_student_weekly_attendance);
            Log.d("hhh", "harsh"+567);
			convertView.setTag(holder);
		}

		else
		{
			holder = (Holder) convertView.getTag();
		}

		try {
			
			date1 = studentWeeklyAttendanceArray.getJSONObject(position).getString("date");
			//holder.studentWeeklyAttendanceDate.setText((studentWeeklyAttendanceArray.getJSONObject(position).getString("date")));
			try {
				date3 = formatter.parse(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			date2 = formatter1.format(date3);
			holder.studentWeeklyAttendanceDate.setText(date2);
			if(studentWeeklyAttendanceArray.getJSONObject(position).getString("attendance").equals("P"))
				holder.studentWeeklyAttendanceImage.setImageResource(R.drawable.green_tick);
			else
				if(studentWeeklyAttendanceArray.getJSONObject(position).getString("attendance").equals("A"))
				{
					holder.studentWeeklyAttendanceImage.setImageResource(R.drawable.red);
				}

				else
				if(studentWeeklyAttendanceArray.getJSONObject(position).getString("attendance").equals("L"))
				{
					holder.studentWeeklyAttendanceImage.setImageResource(R.drawable.leaveicon);
				}
				else
					holder.studentWeeklyAttendanceImage.setImageResource(R.drawable.ic_action_back);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//holder.studentDailyAttendanceImage.setImageResource(studentDailyAttendanceArray.get(position).getIconResId());
		
		return convertView;
	}

	static class Holder
	{
		private TextView studentWeeklyAttendanceDate;
		private ImageView studentWeeklyAttendanceImage;
	}


}
