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

public class StudentPolllListAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray studentPollArray;
	String date1,date2;
	Date date3;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	

	public StudentPolllListAdapter(Context context, JSONArray studentPollArray) {
		
		this.context = context;
		this.studentPollArray = studentPollArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentPollArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return studentPollArray.getJSONObject(position);
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
			convertView = layoutInflater.inflate(R.layout.listview_student_poll, null);
			holder.date = (TextView) convertView.findViewById(R.id.text_poll_date);
			holder.title = (TextView) convertView.findViewById(R.id.text_poll_title);
			holder.next = (ImageView) convertView.findViewById(R.id.img_poll);
			holder.subject = (TextView) convertView.findViewById(R.id.text_subject);
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			
			date1 = studentPollArray.getJSONObject(position).getString("publish_date");
			try {
				date3 = formatter.parse(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			date2 = formatter1.format(date3);
			//holder.date.setText(studentPollArray.getJSONObject(position).getString("pub_date"));
			holder.date.setText(date2);
			holder.title.setText(studentPollArray.getJSONObject(position).getString("poll_name"));
			holder.next.setImageResource(R.drawable.ic_action_next_item);
			if(studentPollArray.getJSONObject(position).getString("subject").matches("") || studentPollArray.getJSONObject(position).getString("subject").matches("null"))
			{
				holder.subject.setVisibility(View.GONE);
			}
			else
			{
				holder.subject.setVisibility(View.VISIBLE);
				holder.subject.setText(studentPollArray.getJSONObject(position).getString("subject"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
   static class Holder
   {
	   TextView title,date,subject;
	   ImageView next;
   }
}
