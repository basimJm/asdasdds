package com.schoofi.adapters;

import android.content.Context;
import android.graphics.Typeface;
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

public class StudentAnnouncementListAdapter extends BaseAdapter{

	private Context context;
	private JSONArray studentAnnouncementArray;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat format = new SimpleDateFormat("EEEE");
	String date,date4,month,year;
	String date1,date2;
	Date date3;
	String fontPath = "OpenSans-Regular.ttf";


	public StudentAnnouncementListAdapter(Context context,JSONArray studentAnnouncementArray)
	{
		this.context = context;
		this.studentAnnouncementArray = studentAnnouncementArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentAnnouncementArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return studentAnnouncementArray.getJSONObject(position);
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
	public View getView(int position, View convertView, ViewGroup viewGroup) {
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
			date = studentAnnouncementArray.getJSONObject(position).getString("date");

			
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			//date2 = formatter1.format(date3);


			date1 = date.substring(8,10);
			month = date.substring(5,7);
			year = date.substring(0,4);
			//System.out.println(date1+month);
			Typeface face = Typeface.createFromAsset(context.getAssets(), fontPath);
			holder.announcementDate.setText(date1);
			holder.announcementDate.setTypeface(face);
			holder.announcementMonth.setTypeface(face);

			switch(Integer.parseInt(month)) {
				case 1:

					holder.announcementMonth.setText("JAN");
					break;

				case 2:
					holder.announcementMonth.setText("FEB");
					break;

				case 3:
					holder.announcementMonth.setText("MAR");
					break;

				case 4:
					holder.announcementMonth.setText("APR");
					break;

				case 5:
					holder.announcementMonth.setText("MAY");
					break;

				case 6:
					holder.announcementMonth.setText("JUN");
					break;

				case 7:
					holder.announcementMonth.setText("JUL");
					break;

				case 8:
					holder.announcementMonth.setText("AUG");
					break;

				case 9:
					holder.announcementMonth.setText("SEP");
					break;

				case 10:
					holder.announcementMonth.setText("OCT");
					break;

				case 11:
					holder.announcementMonth.setText("NOV");
					break;

				case 12:
					holder.announcementMonth.setText("DEC");
					break;

				default:
					holder.announcementMonth.setText("MON");
					break;

			}



			holder.announcementTitle.setText(studentAnnouncementArray.getJSONObject(position).getString("announcement_title"));

			holder.announcementPerson.setText(studentAnnouncementArray.getJSONObject(position).getString("full_name"));

			if(studentAnnouncementArray.getJSONObject(position).getString("group_name").matches("0") || studentAnnouncementArray.getJSONObject(position).getString("group_name").matches("") || studentAnnouncementArray.getJSONObject(position).getString("group_name").matches("null"))
			{
				holder.announcmentGroup.setText("To: "+"All Teachers Group");
			}

			else
			{
				holder.announcmentGroup.setText("To: "+studentAnnouncementArray.getJSONObject(position).getString("group_name"));
			}
			//holder.announcementDate.setText(studentAnnouncementArray.getJSONObject(position).getString("date"));
			/*if(studentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("image") || studentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("txt"))
			{
                 holder.iconAttachment.setImageResource(R.drawable.ic_action_picture);

			}
			else
				if(studentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("video"))
			{
				holder.iconAttachment.setImageResource(R.mipmap.videogrey);
			}
			
				else
					if(studentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("audio"))
				{
					holder.iconAttachment.setImageResource(R.drawable.audioiconupdate);
				}
			
					else
						if(studentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("text"))
					{

					}
					else
						if(studentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("pdf")|| studentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("ppt"))
						{
							holder.iconAttachment.setImageResource(R.drawable.announcementfile);
						}*/

			if(studentAnnouncementArray.getJSONObject(position).getString("image_path").matches(""))
			{

			}
			else {
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
