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

public class StudentListviewSubmittedPollsAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray studentSubmittedPollsArray;
	private String value;

	public StudentListviewSubmittedPollsAdapter(Context context,JSONArray studentSubmittedPollsArray,String value) {
	
		this.context = context;
		this.studentSubmittedPollsArray = studentSubmittedPollsArray;
		this.value = value;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentSubmittedPollsArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return studentSubmittedPollsArray.getJSONObject(position);
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
			convertView = layoutInflater.inflate(R.layout.listview_student_submitted_polls, null);
			holder.question = (TextView) convertView.findViewById(R.id.text_student_submitted_polls_question);
			holder.answer = (TextView) convertView.findViewById(R.id.text_student_submitted_polls_answer);
			if(value.matches("1"))
			{
				holder.answer.setVisibility(View.VISIBLE);

			}
			else {
				holder.answer.setVisibility(View.GONE);
				
			}
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		
		try {
			if(value.matches("1"))
			{
				holder.question.setText(studentSubmittedPollsArray.getJSONObject(position).getString("ques"));
				holder.answer.setText(studentSubmittedPollsArray.getJSONObject(position).getString("action_values"));
			}
			else
				if(value.matches("3"))
				{
					holder.question.setText(studentSubmittedPollsArray.getJSONObject(position).getString("ques"));
				}
			else
			{
				holder.question.setText(studentSubmittedPollsArray.getJSONObject(position).getString("poll_name"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
  static class Holder
  {
	  TextView question,answer;
  }
}
