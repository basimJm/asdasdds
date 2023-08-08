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

public class TeacherFeedbackAssignedAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray teacherFeedBackAssignedArray;
	String date1,date2,date4;
	Date date3;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public TeacherFeedbackAssignedAdapter(Context context,JSONArray teacherFeedBackAssignedArray) {
		super();
		this.context = context;
		this.teacherFeedBackAssignedArray = teacherFeedBackAssignedArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return teacherFeedBackAssignedArray.length();
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
		{
			holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.teacher_feedback_list_item, null);
			holder.chairmanStudentFeedbackName = (TextView) convertView.findViewById(R.id.text_user_name);
			//holder.chairmanStudentFeedbckClass = (TextView) convertView.findViewById(R.id.text_class_name);
			holder.chairmanStudentFeedbackType = (TextView) convertView.findViewById(R.id.text_type);
			holder.chairmanStudentFeedbackDate = (TextView) convertView.findViewById(R.id.text_student_feedBack_date);
			holder.chairmanStudentFeedbackDescription = (TextView) convertView.findViewById(R.id.text_student_feedBack_description);

			convertView.setTag(holder);
		}

		else
		{
			holder = (Holder) convertView.getTag();
		}

		try {
            date1 = teacherFeedBackAssignedArray.getJSONObject(position).getString("created_date");
			date2 = date1.substring(0, 10);
			
			try {
				date3 = formatter.parse(date2);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
			
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			date4 = formatter1.format(date3);
			holder.chairmanStudentFeedbackName.setText(teacherFeedBackAssignedArray.getJSONObject(position).getString("Name"));
			//holder.chairmanStudentFeedbckClass.setText("Class: "+teacherFeedBackAssignedArray.getJSONObject(position).getString("class_name"));
			holder.chairmanStudentFeedbackType.setText(teacherFeedBackAssignedArray.getJSONObject(position).getString("full_name"));
			holder.chairmanStudentFeedbackDescription.setText(teacherFeedBackAssignedArray.getJSONObject(position).getString("description"));
			holder.chairmanStudentFeedbackDate.setText(date4);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class Holder
	{
		TextView chairmanStudentFeedbackName,chairmanStudentFeedbackType,chairmanStudentFeedbackDate,chairmanStudentFeedbackDescription;
		ImageView next;
	}

}
