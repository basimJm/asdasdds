package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.fragments.StudentFeedbackList;
import com.schoofi.utils.FeedBackListItemVO;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedbackDetailListAdapter extends BaseAdapter{

	private Context context;
	private JSONArray studentFeedbackReply;
	String image;
	String date1,date2,date4,date5,date6,date7;
	Date date3,date8,date9;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	StudentFeedbackList studentFeedbackList;
	FeedBackListItemVO feedBackListItemVO;
	String type = "";


	public FeedbackDetailListAdapter(Context context,JSONArray studentFeedbackReply) {

		this.context = context;
		this.studentFeedbackReply = studentFeedbackReply;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentFeedbackReply.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return studentFeedbackReply.getJSONObject(position);
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
	public int getViewTypeCount() 
	{ 
		return 2; 
		} 
	
	/*@Override
	public int getItemViewType(int position)
	{ }*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView == null)
		{
			holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			try {
				if(studentFeedbackReply.getJSONObject(position).getString("assignperson_Name").matches("") ||studentFeedbackReply.getJSONObject(position).getString("assignperson_Name").matches("null"))
						{
				
				convertView = layoutInflater.inflate(R.layout.student_feedback_reply_list, null);
				new FeedBackListItemVO("1");
				type = "1";
						}
				
				else
				{
					convertView = layoutInflater.inflate(R.layout.student_feedback_reply_list1, null);
					new FeedBackListItemVO("2");
					type = "2";
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.date = (TextView) convertView.findViewById(R.id.text_replyStudentFeedbackDate);
			holder.description = (TextView) convertView.findViewById(R.id.text_replyStudentFeedbackDescription);
			holder.title = (TextView) convertView.findViewById(R.id.text_replyStudentFeedbackTitle);
			holder.image = (ImageView) convertView.findViewById(R.id.image_feedback_icon);
			//holder.assignedName = (TextView) convertView.findViewById(R.id.text_replyStudentAssignedName);
			//holder.assignedDate = (TextView) convertView.findViewById(R.id.text_replyStudentTargetDate);
			if(type.matches("2"))
			{
				holder.targetDate = (TextView) convertView.findViewById(R.id.text_replyStudentTargetDate);
				holder.assignedName = (TextView) convertView.findViewById(R.id.text_replyStudentAssignedName);
			}
			convertView.setTag(holder);
		}

		else
		{
			holder = (Holder) convertView.getTag();
		}

		try {

			date1 = studentFeedbackReply.getJSONObject(position).getString("assign_date");
			date4 = studentFeedbackReply.getJSONObject(position).getString("resolve_date");
			date5 = studentFeedbackReply.getJSONObject(position).getString("target_date");
			try {
				date3 = formatter.parse(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
			
				

			

			
				
				


			date2 = formatter1.format(date3);
			/*holder.date.setText("Created date:"+date2);
			//holder.date.setText(studentFeedbackReply.getJSONObject(position).getString("created_date"));
			holder.description.setText("Description:"+studentFeedbackReply.getJSONObject(position).getString("description"));
			holder.title.setText("Replied By:"+studentFeedbackReply.getJSONObject(position).getString("commentor_name"));
			image = studentFeedbackReply.getJSONObject(position).getString("image");
			String assignedname;
			assignedname = studentFeedbackReply.getJSONObject(position).getString("assignperson_Name");*/
			
			if(type.matches("1") && Preferences.getInstance().userRoleId.matches("5") )
			{
				holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("commentor_name"));
				holder.date.setText(date2);
				holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("description"));
				image = studentFeedbackReply.getJSONObject(position).getString("image");
			}
			
			else
				if(type.matches("2") && Preferences.getInstance().userRoleId.matches("5"))
			{
				try {
					date9 = formatter.parse(date5);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				date7 = formatter1.format(date9);
				holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("assigned_by_Name"));
				holder.date.setText("Assigned date: "+date2);
				holder.targetDate.setText("Target Date: "+date7);
				holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("comment_on_assign"));

				image = studentFeedbackReply.getJSONObject(position).getString("image");
			}
			
				else
					if(type.matches("1") && Preferences.getInstance().userRoleId.matches("4"))
					{
						holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("commentor_name"));
						holder.date.setText(date2);
						holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("description"));
						image = studentFeedbackReply.getJSONObject(position).getString("image");
						
					}
			
					else
						if(type.matches("2") && Preferences.getInstance().userRoleId.matches("4"))
					{
						try {
							date9 = formatter.parse(date5);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						date7 = formatter1.format(date9);
						holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("assigned_by_Name"));
						holder.date.setText("Assigned date: "+date2);
						holder.targetDate.setText("Target Date: "+date7);
						holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("comment_on_assign"));
						image = studentFeedbackReply.getJSONObject(position).getString("image");
					}
			
			if(type.matches("1") && Preferences.getInstance().userRoleId.matches("6") )
			{
				holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("commentor_name"));
				holder.date.setText(date2);
				holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("description"));
				image = studentFeedbackReply.getJSONObject(position).getString("image");
			}
			
			else
				if(type.matches("2") && Preferences.getInstance().userRoleId.matches("6"))
			{
				try {
					date9 = formatter.parse(date5);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				date7 = formatter1.format(date9);
				holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("commentor_name"));
				holder.date.setText("Assigned date: "+date2);
				holder.targetDate.setText("Target Date: "+date7);
				holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("description"));
				image = studentFeedbackReply.getJSONObject(position).getString("image");
			}

			if(type.matches("1") && Preferences.getInstance().userRoleId.matches("7") )
			{
				holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("commentor_name"));
				holder.date.setText(date2);
				holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("description"));
				image = studentFeedbackReply.getJSONObject(position).getString("image");
			}
			
			else
				if(type.matches("2") && Preferences.getInstance().userRoleId.matches("7"))
			{
				try {
					date9 = formatter.parse(date5);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				date7 = formatter1.format(date9);
				holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("assigned_by_Name"));
				holder.date.setText("Assigned date: "+date2);
				holder.targetDate.setText("Target Date: "+date7);
				holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("comment_on_assign"));
				holder.assignedName.setText("Assigned To: "+studentFeedbackReply.getJSONObject(position).getString("assignperson_Name"));
				image = studentFeedbackReply.getJSONObject(position).getString("image");
			}
			
			if(type.matches("1") && Preferences.getInstance().userRoleId.matches("8") || Preferences.getInstance().userRoleId.matches("3"))
			{
				holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("commentor_name"));
				holder.date.setText(date2);
				holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("description"));
				image = studentFeedbackReply.getJSONObject(position).getString("image");
			}
			
			else
				if(type.matches("2") && Preferences.getInstance().userRoleId.matches("8") || Preferences.getInstance().userRoleId.matches("3"))
			{
				try {
					date9 = formatter.parse(date5);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				date7 = formatter1.format(date9);
				holder.title.setText(studentFeedbackReply.getJSONObject(position).getString("assigned_by_Name"));
				holder.date.setText("Assigned date: "+date2);
				holder.targetDate.setText("Target Date: "+date7);
				holder.description.setText(studentFeedbackReply.getJSONObject(position).getString("comment_on_assign"));
				holder.assignedName.setText("Assigned To: "+studentFeedbackReply.getJSONObject(position).getString("assignperson_Name"));
				image = studentFeedbackReply.getJSONObject(position).getString("image");
			}
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(image.matches(""))
		{
			holder.image.setImageResource(R.drawable.cameracross);

		}

		else
		{
			holder.image.setImageResource(R.drawable.camera);

		}



		return convertView;
	}

	static class Holder
	{
		TextView title,date,description,assignedDate,assignedName,targetDate;;
		ImageView image;
	}

}
