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

public class TeacherStudentHomeWorkStudentSubmittedDetailsAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray studentListArray;
	
	public TeacherStudentHomeWorkStudentSubmittedDetailsAdapter(Context context, JSONArray studentListArray) {
		super();
		this.context = context;
		this.studentListArray = studentListArray;
	}

	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentListArray.length();
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
		// TODO Auto-generated method stub
Holder holder;
		
		if(convertView == null)
		{
			holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.teacher_student_assignment_homework_submitted_not_submitted_student_list, null);
			holder.studentName = (TextView) convertView.findViewById(R.id.text_student_name);
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			holder.studentName.setText(studentListArray.getJSONObject(position).getString("stu_name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class Holder
	{
       TextView studentName;
	}

}
