package com.schoofi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.TeacherStudentAttendanceDetails;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class TeacherAttendanceDetailsListViewAdapter extends BaseAdapter implements Filterable{
	
	
	private Context context;
	int count,f;
	private int selectedIndex;
	TeacherStudentAttendanceDetails teacherStudentAttendanceDetails;

	public JSONArray teacherStudentAttendanceArray1;
	private JSONArray filteredArray;
	private TeacherStudentAttendanceFilter teacherStudentAttendanceFilter;
	public boolean c;
	public TeacherAttendanceDetailsListViewAdapter(Context context,JSONArray teacherStudentAttendanceArray1, int f) {
		super();
		this.context = context;
		selectedIndex = -1;
		this.teacherStudentAttendanceArray1 = teacherStudentAttendanceArray1;
		this.filteredArray = teacherStudentAttendanceArray1;
		this.f = f;
		//inflater=(LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setSelectedIndex(int position)
    {
        selectedIndex = position;
        notifyDataSetChanged();
    }


	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filteredArray.length();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		// TODO Auto-generated method stub
		
		final Holder holder;
		
		if(convertView == null)
		{

			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.listview_teacher_student_attendance_details, null);
			holder = new Holder();
			holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_listView_teacherStudentAttendanceDetails);
			holder.studentAttendanceImage = (ImageView) convertView.findViewById(R.id.imageView_studentAttendance);
			holder.studentImage = (ImageView) convertView.findViewById(R.id.imageView_studentImage);
			holder.studentName = (TextView) convertView.findViewById(R.id.txt_studentNameTeacher);
			holder.studentId = (TextView) convertView.findViewById(R.id.txt_studentId);
			
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		if(f == 1)
		{
		
		try {
			holder.studentName.setText(filteredArray.getJSONObject(position).getString("stu_name"));
			//holder.studentId.setText(filteredArray.getJSONObject(position).getString("stu_id"));
			holder.studentId.setText(filteredArray.getJSONObject(position).getString("class_roll_no"));
			/*Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+filteredArray.getJSONObject(position).getString("picture")).placeholder(R.drawable.person).
			error(R.drawable.person).transform(new CircleTransform()).into(holder.studentImage);*/

			Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+filteredArray.getJSONObject(position).getString("picture")).asBitmap().placeholder(R.drawable.person).error(R.drawable.person).into(new BitmapImageViewTarget(holder.studentImage)
			{
				@Override
				protected void setResource(Bitmap resource) {
					RoundedBitmapDrawable circularBitmapDrawable =
							RoundedBitmapDrawableFactory.create(context.getResources(), resource);
					circularBitmapDrawable.setCircular(true);
					holder.studentImage.setImageDrawable(circularBitmapDrawable);
				}
			});
			//teacherStudentAttendanceArray1.getJSONObject(position).put("isPresent", "P");
			
			
			if(filteredArray.getJSONObject(position).getString("status").matches("1"))
			{
				
				teacherStudentAttendanceArray1.getJSONObject(position).put("isPresent", filteredArray.getJSONObject(position).getString("stu_id")+"-L");
				holder.studentAttendanceImage.setImageResource(R.drawable.leaveicon);
				teacherStudentAttendanceArray1.getJSONObject(position).put("isPresent1","L");
				holder.linearLayout.setClickable(false);
			}
			
			else
			{
				holder.linearLayout.setClickable(true);
				if(filteredArray.getJSONObject(position).getString("position").matches("0")) {
					holder.studentAttendanceImage.setImageResource(R.drawable.greencircletick);
				}
				else
				{
					holder.studentAttendanceImage.setImageResource(R.drawable.greycircletick);
				}
				holder.linearLayout.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						if(c == false)
						{
							
							
						
						holder.studentAttendanceImage.setImageResource(R.drawable.greycircletick);
						
						try {
							teacherStudentAttendanceArray1.getJSONObject(position).put("isPresent", filteredArray.getJSONObject(position).getString("stu_id")+"-A");
							teacherStudentAttendanceArray1.getJSONObject(position).put("isPresent1","A");
							filteredArray.getJSONObject(position).put("position","1");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						c=true;
						   
						
						}
						
						else
						{
							
							holder.studentAttendanceImage.setImageResource(R.drawable.greencircletick);
							try {
								teacherStudentAttendanceArray1.getJSONObject(position).put("isPresent", filteredArray.getJSONObject(position).getString("stu_id")+"-P");
								filteredArray.getJSONObject(position).put("position","1");
								teacherStudentAttendanceArray1.getJSONObject(position).put("isPresent1","P");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							c =false;
							count = 0;
						}
						
					}
				
				});
			}
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*if(selectedIndex!= -1 && position == selectedIndex)
		{
			try {
				teacherStudentAttendanceArray1.getJSONObject(position).put("isPresent", "A");
				Utils.showToast(context, "kkkkkk");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else
	{
			//System.out.println("kkk");
			try {
				teacherStudentAttendanceArray1.getJSONObject(position).put("isPresent", "A");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		}
		
		else
		{
			try {
				holder.studentName.setText("Name: "+filteredArray.getJSONObject(position).getString("stu_name"));
				//holder.studentId.setText(filteredArray.getJSONObject(position).getString("stu_id"));
				holder.studentId.setText("Roll Number: "+filteredArray.getJSONObject(position).getString("class_roll_no"));
				/*Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+filteredArray.getJSONObject(position).getString("picture")).placeholder(R.drawable.ic_action_back).
				error(R.drawable.ic_action_back).transform(new CircleTransform()).into(holder.studentImage);*/
				Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+filteredArray.getJSONObject(position).getString("picture")).asBitmap().placeholder(R.drawable.person).error(R.drawable.person).into(new BitmapImageViewTarget(holder.studentImage)
				{
					@Override
					protected void setResource(Bitmap resource) {
						RoundedBitmapDrawable circularBitmapDrawable =
								RoundedBitmapDrawableFactory.create(context.getResources(), resource);
						circularBitmapDrawable.setCircular(true);
						holder.studentImage.setImageDrawable(circularBitmapDrawable);
					}
				});

				if(filteredArray.getJSONObject(position).getString("attendance").toString().matches("P"))
				{
					holder.studentAttendanceImage.setImageResource(R.drawable.greencircletick);
				}

				else
					if(filteredArray.getJSONObject(position).getString("attendance").toString().matches("A"))
					{
						holder.studentAttendanceImage.setImageResource(R.drawable.crossredcircle);
					}
				
					else
						if(filteredArray.getJSONObject(position).getString("attendance").toString().matches("L"))
						{
							holder.studentAttendanceImage.setImageResource(R.drawable.leaveicon);
						}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		
		
		return convertView;
	}
	
	static class Holder
	{
		TextView studentName,studentId;
		ImageView studentImage,studentAttendanceImage;
		LinearLayout linearLayout;
	}

	@Override
	public Filter getFilter() 
	{

		if(teacherStudentAttendanceFilter==null)
			teacherStudentAttendanceFilter=new TeacherStudentAttendanceFilter();
		return teacherStudentAttendanceFilter;
	}
	private class TeacherStudentAttendanceFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) 
		{
			FilterResults filterResults=new FilterResults();
			if(constraint!=null && constraint.length()>0){

				JSONArray array=new JSONArray();
				for(int i=0;i<teacherStudentAttendanceArray1.length();i++){
					try
					{
						if(teacherStudentAttendanceArray1.getJSONObject(i).getString("stu_name").toLowerCase().contains(constraint.toString().toLowerCase()) || teacherStudentAttendanceArray1.getJSONObject(i).getString("class_roll_no").toLowerCase().contains(constraint.toString().toLowerCase()) )
							array.put(teacherStudentAttendanceArray1.getJSONObject(i));
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
				filterResults.count=array.length();
				filterResults.values=array;
			}
			else
			{
				filterResults.count=teacherStudentAttendanceArray1.length();
				filterResults.values=teacherStudentAttendanceArray1;
			}
			return filterResults;
		}

		@Override
		protected void publishResults(CharSequence constraint,FilterResults results) 
		{
			filteredArray = (JSONArray) results.values;
			notifyDataSetChanged();
		}
	}

}
