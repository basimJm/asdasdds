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

public class StudentFeedbackListAdapter extends BaseAdapter {
	
	private Context context;
	private JSONArray studentFeedbackArray;
	

	public StudentFeedbackListAdapter(Context context,JSONArray studentFeedbackArray) {
		this.context = context;
		this.studentFeedbackArray = studentFeedbackArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentFeedbackArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return studentFeedbackArray.getJSONObject(position);
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
			convertView = layoutInflater.inflate(R.layout.student_feedbacklist, null);
			holder.studentFeedbackTypeTextView = (TextView) convertView.findViewById(R.id.text_student_feedBack_type);
			holder.studentFeedbackDateTextView = (TextView) convertView.findViewById(R.id.text_student_feedBack_date);
			holder.studentFeedBackDescriptionTextView = (TextView) convertView.findViewById(R.id.text_student_feedBack_description);
			//holder.studentFeedbackForwardImageView = (ImageView) convertView.findViewById(R.id.imageViewForward);
			
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			String date = studentFeedbackArray.getJSONObject(position).getString("created_date");
			//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
			String date1 = date.substring(0,10);
			//String date2 = simpleDateFormat.format(date);
			
			holder.studentFeedbackTypeTextView.setText(studentFeedbackArray.getJSONObject(position).getString("full_name"));
			holder.studentFeedbackDateTextView.setText(date1);
			holder.studentFeedBackDescriptionTextView.setText(studentFeedbackArray.getJSONObject(position).getString("description"));
			//holder.studentFeedbackForwardImageView.setImageResource(R.drawable.ic_action_next_item);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class Holder
	{
		TextView studentFeedbackTypeTextView,studentFeedbackDateTextView,studentFeedBackDescriptionTextView;
		ImageView studentFeedbackForwardImageView;
	}

}
