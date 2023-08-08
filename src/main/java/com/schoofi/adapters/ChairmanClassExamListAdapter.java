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

public class ChairmanClassExamListAdapter extends BaseAdapter{

	private Context context;
	private JSONArray chairmanClassExamListArray;

	public ChairmanClassExamListAdapter(Context context, JSONArray chairmanClassExamListArray) {
		super();
		this.context = context;
		this.chairmanClassExamListArray = chairmanClassExamListArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chairmanClassExamListArray.length();
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
			convertView = layoutInflater.inflate(R.layout.custom_dialog_list_view, null);
			holder.exmaTextList = (TextView) convertView.findViewById(R.id.text_dialogListView);
			
			convertView.setTag(holder);
			}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			holder.exmaTextList.setText(chairmanClassExamListArray.getJSONObject(position).getString("exam_name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
		
		
	}
	
	static class Holder
	
	{
		TextView exmaTextList;
	}
}
