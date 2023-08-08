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

public class StudentCustomAttendanceAdapter extends BaseAdapter {
	
	private Context context;
	private JSONArray studentCustomAttendanceArray;
	String date1,date2;
	Date date3;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public StudentCustomAttendanceAdapter(Context context,JSONArray studentCustomAttendanceArray)
	{
		this.context = context;
		this.studentCustomAttendanceArray = studentCustomAttendanceArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentCustomAttendanceArray.length();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView == null)
		{   holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.student_custom_attendance_replace_listview, null);
			holder.studentCustomAttendanceDate = (TextView) convertView.findViewById(R.id.text_student_custom_attendance_date);
			holder.studentCustomAttendanceImage = (ImageView) convertView.findViewById(R.id.img_student_custom_attendance);
            Log.d("hhh", "harsh"+567);
			convertView.setTag(holder);
		}

		else
		{
			holder = (Holder) convertView.getTag();
		}

		try {
			date1 = studentCustomAttendanceArray.getJSONObject(position).getString("date");
			try {
				date3 = formatter.parse(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			date2 = formatter1.format(date3);
			holder.studentCustomAttendanceDate.setText(date2);
			//holder.studentCustomAttendanceDate.setText((studentCustomAttendanceArray.getJSONObject(position).getString("date")));
			if(studentCustomAttendanceArray.getJSONObject(position).getString("attendance").equals("P"))
				holder.studentCustomAttendanceImage.setImageResource(R.drawable.green_tick);
			else
				if(studentCustomAttendanceArray.getJSONObject(position).getString("attendance").equals("A"))
				{
					holder.studentCustomAttendanceImage.setImageResource(R.drawable.red);
				}

				else
				if(studentCustomAttendanceArray.getJSONObject(position).getString("attendance").equals("L"))
				{
					holder.studentCustomAttendanceImage.setImageResource(R.drawable.leaveicon);
				}
				else
					holder.studentCustomAttendanceImage.setImageResource(R.drawable.ic_action_back);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//holder.studentDailyAttendanceImage.setImageResource(studentDailyAttendanceArray.get(position).getIconResId());
		
		return convertView;
	}


	static class Holder
	{
		private TextView studentCustomAttendanceDate;
		private ImageView studentCustomAttendanceImage;
	}
		
	}


