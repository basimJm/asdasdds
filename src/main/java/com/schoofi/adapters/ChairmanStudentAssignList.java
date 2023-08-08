package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class ChairmanStudentAssignList extends BaseAdapter {
	
	private Context context;
	private JSONArray chairmanStudentAssignArray;

	public ChairmanStudentAssignList(Context context,JSONArray chairmanStudentAssignArray) {
		super();
		this.context = context;
		this.chairmanStudentAssignArray = chairmanStudentAssignArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chairmanStudentAssignArray.length();
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
			convertView = layoutInflater.inflate(R.layout.assign_listview_layout, null);
			holder.userImage = (ImageView) convertView.findViewById(R.id.imageView_studentImage);
			holder.email = (TextView) convertView.findViewById(R.id.txt_assign_email);
			holder.name = (TextView) convertView.findViewById(R.id.txt_assign_name);
			holder.userclass = (TextView) convertView.findViewById(R.id.txt_assign_class);
			convertView.setTag(holder);
		}
		
		else
		{
		   holder = (Holder) convertView.getTag();
		}
		
		
			try {
				holder.email.setText("Email:"+chairmanStudentAssignArray.getJSONObject(position).getString("user_email_id"));
				
				holder.name.setText("Name:"+chairmanStudentAssignArray.getJSONObject(position).getString("Name"));
				if(chairmanStudentAssignArray.getJSONObject(position).getString("class_name").toString().matches(""))
				{
					holder.userclass.setText("");
				}
				else
				{
				holder.userclass.setText("Class:"+chairmanStudentAssignArray.getJSONObject(position).getString("class_name")+"-"+chairmanStudentAssignArray.getJSONObject(position).getString("section_name"));
				}
				Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+chairmanStudentAssignArray.getJSONObject(position).getString("user_picture")).placeholder(R.drawable.ic_action_back).
				error(R.drawable.ic_action_back).transform(new CircleTransform()).into(holder.userImage);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
		return convertView;
	}
	
	static class Holder
	{
		ImageView userImage;
		TextView name,userclass,email;
	}
	

}
