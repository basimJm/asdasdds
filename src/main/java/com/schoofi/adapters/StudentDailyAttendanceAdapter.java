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

public class StudentDailyAttendanceAdapter extends BaseAdapter {

	private Context context;
	//private ArrayList<StudentDailyAttendanceVO> studentDailyAttendanceList;
	private JSONArray studentDailyAttendanceArray;
	String date1,date2;
	Date date3;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	

	public StudentDailyAttendanceAdapter(Context context,JSONArray studentDailyAttendanceArray)
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
			convertView = layoutInflater.inflate(R.layout.student_daily_attendance_listitem, null);
			holder.studentDailyAttendanceDate = (TextView) convertView.findViewById(R.id.text_student_daily_attendance_date);
			holder.studentDailyAttendanceImage = (ImageView) convertView.findViewById(R.id.img_student_daily_attendance);
            //Log.d("hhh", "harsh"+567);
			convertView.setTag(holder);
		}

		else
		{
			holder = (Holder) convertView.getTag();
		}

		try {
			date1 = studentDailyAttendanceArray.getJSONObject(position).getString("date");

			try {
				date3 = formatter.parse(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			date2 = formatter1.format(date3);

			holder.studentDailyAttendanceDate.setText(date2);
			//holder.studentDailyAttendanceDate.setText((studentDailyAttendanceArray.getJSONObject(position).getString("date")));
			if (studentDailyAttendanceArray.getJSONObject(position).getString("attendance").equals("P")) {
				holder.studentDailyAttendanceImage.setImageResource(R.drawable.green_tick);
			} else if (studentDailyAttendanceArray.getJSONObject(position).getString("attendance").equals("A")) {
				holder.studentDailyAttendanceImage.setImageResource(R.drawable.red);
			} else if (studentDailyAttendanceArray.getJSONObject(position).getString("attendance").equals("L"))
			{
				holder.studentDailyAttendanceImage.setImageResource(R.drawable.leaveicon);
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//holder.studentDailyAttendanceImage.setImageResource(studentDailyAttendanceArray.get(position).getIconResId());
		
		return convertView;
	}

	static class Holder
	{
		TextView studentDailyAttendanceDate;
		ImageView studentDailyAttendanceImage;
	}

}
