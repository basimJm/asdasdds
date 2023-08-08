package com.schoofi.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

import com.schoofi.activitiess.R;
import com.schoofi.adapters.TeacherAttendanceDetailsListViewAdapter.Holder;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SchoolNotificationListAdapter extends BaseAdapter{
	
	private JSONArray schoolNotificationListArray;
	public SchoolNotificationListAdapter( Context context,JSONArray schoolNotificationListArray) {
		super();
		
		this.context = context;
		this.schoolNotificationListArray = schoolNotificationListArray;
	}

	private Context context;
	String date1,date2;
	Date date5;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return schoolNotificationListArray.length();
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
		
		final Holder holder;
		if(convertView == null)
		{
			holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.listview_school_gallery, null);
			holder.schoolNotificationListImage = (ImageView) convertView.findViewById(R.id.imageView_studentImage);
			holder.title = (TextView) convertView.findViewById(R.id.txt_studentNameTeacher);
			holder.date = (TextView) convertView.findViewById(R.id.txt_studentId);
			holder.attendance = (ImageView) convertView.findViewById(R.id.imageView_studentAttendance);
			holder.attendance.setVisibility(convertView.INVISIBLE);
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			date1 = schoolNotificationListArray.getJSONObject(position).getString("date");
			try {
				date5 = formatter.parse(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			date2 = formatter1.format(date5);
			holder.title.setText(schoolNotificationListArray.getJSONObject(position).getString("title"));
			holder.date.setText(date2);
			Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+schoolNotificationListArray.getJSONObject(position).getString("image")).placeholder(R.drawable.profilebig).
			error(R.drawable.profilebig).into(holder.schoolNotificationListImage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class Holder
	{
		ImageView schoolNotificationListImage,attendance;
		TextView title,date;
	}

}
