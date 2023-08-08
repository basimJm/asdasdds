package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherAnnouncementAdapter extends BaseAdapter {
	
	private Context context;
	private JSONArray teacherAnnouncementArray;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat format = new SimpleDateFormat("EEEE");
	String date,date4,month,year;
	String date1,date2;
	Date date3;
	


	public TeacherAnnouncementAdapter(Context context,JSONArray teacherAnnouncementArray) {
		super();
		this.context = context;
		this.teacherAnnouncementArray = teacherAnnouncementArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return teacherAnnouncementArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		
		try {
			return teacherAnnouncementArray.getJSONObject(position);
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
			convertView = layoutInflater.inflate(R.layout.listview_new_student_announcement, null);
			holder.announcementDate = (TextView) convertView.findViewById(R.id.text_date);
			holder.announcementMonth = (TextView) convertView.findViewById(R.id.text_month);

			holder.announcementPerson = (TextView) convertView.findViewById(R.id.textView_student_announcement_person);
			holder.announcementTitle = (TextView) convertView.findViewById(R.id.textView_student_announcement_title);

			holder.announcmentGroup = (TextView) convertView.findViewById(R.id.textView_student_announcement_person1);
			holder.iconAttachment = (ImageView) convertView.findViewById(R.id.image_view_icon);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}

		try {
			date = teacherAnnouncementArray.getJSONObject(position).getString("date");


			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			//date2 = formatter1.format(date3);


			date1 = date.substring(8,10);
			month = date.substring(5,7);
			year = date.substring(0,4);
			//System.out.println(date1+month);
			holder.announcementDate.setText(date1);

			switch(Integer.parseInt(month)) {
				case 1:

					holder.announcementMonth.setText("Jan");
					break;

				case 2:
					holder.announcementMonth.setText("Feb");
					break;

				case 3:
					holder.announcementMonth.setText("Mar");
					break;

				case 4:
					holder.announcementMonth.setText("Apr");
					break;

				case 5:
					holder.announcementMonth.setText("May");
					break;

				case 6:
					holder.announcementMonth.setText("Jun");
					break;

				case 7:
					holder.announcementMonth.setText("Jul");
					break;

				case 8:
					holder.announcementMonth.setText("Aug");
					break;

				case 9:
					holder.announcementMonth.setText("Sep");
					break;

				case 10:
					holder.announcementMonth.setText("Oct");
					break;

				case 11:
					holder.announcementMonth.setText("Nov");
					break;

				case 12:
					holder.announcementMonth.setText("Dec");
					break;

				default:
					holder.announcementMonth.setText("Month");
					break;

			}



			holder.announcementTitle.setText(teacherAnnouncementArray.getJSONObject(position).getString("announcement_title"));

			holder.announcementPerson.setText(teacherAnnouncementArray.getJSONObject(position).getString("full_name"));

			if(teacherAnnouncementArray.getJSONObject(position).getString("group_name").matches("0") || teacherAnnouncementArray.getJSONObject(position).getString("group_name").matches("") || teacherAnnouncementArray.getJSONObject(position).getString("group_name").matches("null"))
			{
				holder.announcmentGroup.setText("To: "+"All Teachers Group");
			}

			else
			{
				holder.announcmentGroup.setText("To: "+teacherAnnouncementArray.getJSONObject(position).getString("group_name"));
			}
			//holder.announcementDate.setText(studentAnnouncementArray.getJSONObject(position).getString("date"));
			/*if(teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("image") || teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("txt"))
			{
				holder.iconAttachment.setImageResource(R.drawable.ic_action_picture);

			}
			else
			if(teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("video"))
			{
				holder.iconAttachment.setImageResource(R.mipmap.videogrey);
			}

			else
			if(teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("audio"))
			{
				holder.iconAttachment.setImageResource(R.drawable.audioiconupdate);
			}

			else
			if(teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("text"))
			{

			}*/
			if(teacherAnnouncementArray.getJSONObject(position).getString("image_path").matches(""))
			{

			}
			else
			{
				holder.iconAttachment.setImageResource(R.drawable.ic_action_attachment);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class Holder
	{
		ImageView iconAttachment;
		TextView announcementTitle,announcementPerson,announcementDate,announcementMonth,announcmentGroup;
	}

}
