package com.schoofi.adapters;

import org.json.JSONArray;
import org.json.JSONException;

import com.schoofi.activitiess.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeacherStudentAttendanceAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray teacherStudentAttendanceArray;
	
	public TeacherStudentAttendanceAdapter(Context context,JSONArray teacherStudentAttendanceArray) {
		super();
		this.context = context;
		this.teacherStudentAttendanceArray = teacherStudentAttendanceArray;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return teacherStudentAttendanceArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return teacherStudentAttendanceArray.getJSONObject(position);
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
			convertView = layoutInflater.inflate(R.layout.listview_teacher_class_list, null);
			holder.teacherClass = (TextView) convertView.findViewById(R.id.text_teaccher_class);
			holder.teacherSection = (TextView) convertView.findViewById(R.id.text_teacher_section);
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			holder.teacherClass.setText(teacherStudentAttendanceArray.getJSONObject(position).getString("class_name"));
			holder.teacherSection.setText(teacherStudentAttendanceArray.getJSONObject(position).getString("section_name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	
	static class Holder
	{
		TextView teacherClass,teacherSection;
	}
}
