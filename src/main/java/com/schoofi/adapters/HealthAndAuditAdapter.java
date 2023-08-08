package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.TeacherHomeScreenVO;

import java.util.ArrayList;

public class HealthAndAuditAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<TeacherHomeScreenVO> temparr;

	public HealthAndAuditAdapter(Context context,
			ArrayList<TeacherHomeScreenVO> temparr) {
		super();
		this.context = context;
		this.temparr = temparr;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return temparr.size();
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
		
		holder.teacherHomeScreenListItemButton.setText(temparr.get(position).getFieldName());
		return convertView;
	}

	static class Holder
	{
		private TextView teacherHomeScreenListItemButton;
	}
}
