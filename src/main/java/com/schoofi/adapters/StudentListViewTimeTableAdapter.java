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

public class StudentListViewTimeTableAdapter extends BaseAdapter{
	private Context context;
	private JSONArray studentTimeTableArray;
	
	public StudentListViewTimeTableAdapter(Context context,JSONArray studentTimeTableArray)
	{
		this.context = context;
		this.studentTimeTableArray = studentTimeTableArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentTimeTableArray.length();
	}

	@Override
	public Object getItem(int position) {
		try {
			return studentTimeTableArray.getJSONObject(position);
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
			holder.periodNo.setText(studentTimeTableArray.getJSONObject(position).getString("period_no"));
			if(studentTimeTableArray.getJSONObject(position).getString("subject_name").matches("") || studentTimeTableArray.getJSONObject(position).getString("subject_name").matches("null"))
			{
				holder.subject.setText("Lab/Practical");
			}
			else
			{
				holder.subject.setText(studentTimeTableArray.getJSONObject(position).getString("subject_name"));
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
