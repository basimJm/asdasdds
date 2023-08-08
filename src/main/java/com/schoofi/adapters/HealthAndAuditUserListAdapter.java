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

public class HealthAndAuditUserListAdapter extends BaseAdapter {

	private Context context;
	private JSONArray healthAndAuditUserListArray;


	public HealthAndAuditUserListAdapter(Context context,JSONArray healthAndAuditUserListArray) {
		
		this.context = context;
		this.healthAndAuditUserListArray = healthAndAuditUserListArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return healthAndAuditUserListArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return healthAndAuditUserListArray.getJSONObject(position);
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
		{   holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.teacher_homescreen_listview_item, null);
			holder.teacherHomeScreenListItemButton = (TextView) convertView.findViewById(R.id.btn_teacher_homescreen_class);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			holder.teacherHomeScreenListItemButton.setText(healthAndAuditUserListArray.getJSONObject(position).getString("name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}
	
	static class Holder
	{
		private TextView teacherHomeScreenListItemButton;
	}

}
