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

public class TeacherListViewTimeTableAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray teacherTimeTableArray;

	public TeacherListViewTimeTableAdapter(Context context,JSONArray teacherTimeTableArray) {
		super();
		this.context = context;
		this.teacherTimeTableArray = teacherTimeTableArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return teacherTimeTableArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return teacherTimeTableArray.getJSONObject(position);
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
			convertView = layoutInflater.inflate(R.layout.student_time_table_listview, null);
			holder.periodNo = (TextView) convertView.findViewById(R.id.text_student_period);
			holder.subject = (TextView) convertView.findViewById(R.id.text_student_subject);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			holder.periodNo.setText(teacherTimeTableArray.getJSONObject(position).getString("period_no"));

			if(teacherTimeTableArray.getJSONObject(position).getString("subject").matches("") || teacherTimeTableArray.getJSONObject(position).getString("subject").matches("null"))
			{
				holder.subject.setText("Lab/Practical");
			}
			else {
				holder.subject.setText(teacherTimeTableArray.getJSONObject(position).getString("subject"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	static class Holder
	{
		TextView periodNo,subject;
	}
}
