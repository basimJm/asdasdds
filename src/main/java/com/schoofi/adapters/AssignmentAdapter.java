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

public class AssignmentAdapter extends BaseAdapter {
	
	private Context context;
	private JSONArray AssignmentArray;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date1,date2;
	String assignmentType,optionalSubject,assignmentType1;

	public AssignmentAdapter(Context context, JSONArray assignmentArray) {
		super();
		this.context = context;
		AssignmentArray = assignmentArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return AssignmentArray.length();
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
			convertView = layoutInflater.inflate(R.layout.assignment_divider_layout, null);
			holder.title = (TextView) convertView.findViewById(R.id.text_title);
			holder.subject = (TextView) convertView.findViewById(R.id.text_subject);
			holder.teacher = (TextView) convertView.findViewById(R.id.text_teacherName);
			holder.date = (TextView) convertView.findViewById(R.id.text_Submit);
			//holder.file = (TextView) convertView.findViewById(R.id.text_file);
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			holder.title.setText(AssignmentArray.getJSONObject(position).getString("title"));

			optionalSubject = AssignmentArray.getJSONObject(position).getString("opt_subject");
			assignmentType = AssignmentArray.getJSONObject(position).getString("type");
			if(assignmentType.matches("") || assignmentType.matches("null"))
			{
				assignmentType1 = "";
			}

			else
			if(assignmentType.matches("home_work"))
			{
				assignmentType1 = "homework";
			}

			else
			if(assignmentType.matches("assignment") || assignmentType.matches("Assignment"))
			{
				assignmentType1 = "assignment";
			}

			if(optionalSubject.matches("") || optionalSubject.matches("null")) {
				holder.subject.setText("Subject: " + AssignmentArray.getJSONObject(position).getString("subject")+" "+assignmentType1);
			}

			else
			{
				holder.subject.setText("Subject: " + AssignmentArray.getJSONObject(position).getString("subject")+" ("+optionalSubject+") "+assignmentType1);
			}
			holder.teacher.setText("Teacher: "+AssignmentArray.getJSONObject(position).getString("teac_name"));
			//holder.file.setText("File:"+AssignmentArray.getJSONObject(position).getString("assign_path"));
			date1 = AssignmentArray.getJSONObject(position).getString("last_date");
			Date date3 = null;
			try {
				date3 = formatter.parse(date1);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			date2 = formatter1.format(date3);
			holder.date.setText("Submitted By: "+date2);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
	
	static class Holder
	{
		TextView title,subject,date,teacher,file;
	}

}
