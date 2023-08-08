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

public class HealthAndAuditSubmittedTaskListAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray healthAndAuditSubTasksArray;

	public HealthAndAuditSubmittedTaskListAdapter(Context context,JSONArray healthAndAuditSubTasksArray) {
		super();
		this.context = context;
		this.healthAndAuditSubTasksArray = healthAndAuditSubTasksArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return healthAndAuditSubTasksArray.length();
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
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.health_and_audit_task_listview_layout, null);
			holder.subTaskName = (TextView) convertView.findViewById(R.id.text_taskName);
			convertView.setTag(holder);
			
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			holder.subTaskName.setText(healthAndAuditSubTasksArray.getJSONObject(position).getString("value"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}

	static class Holder
	{
		TextView subTaskName;
	}
}
