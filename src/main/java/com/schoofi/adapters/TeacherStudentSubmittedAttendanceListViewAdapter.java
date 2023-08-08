package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class TeacherStudentSubmittedAttendanceListViewAdapter extends BaseAdapter implements Filterable{

	private JSONArray teacherStudentAttendanceArray1;
	private JSONArray filteredArray;
	private TeacherStudentAttendanceFilter teacherStudentAttendanceFilter;
	public TeacherStudentSubmittedAttendanceListViewAdapter(Context context,JSONArray teacherStudentAttendanceArray1) {
		super();
		this.teacherStudentAttendanceArray1 = teacherStudentAttendanceArray1;
		this.context = context;
		this.filteredArray = teacherStudentAttendanceArray1;
	}

	private Context context;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filteredArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		try {
			return filteredArray.getJSONObject(position);
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
		final Holder holder;
		if(convertView == null)
		{
			holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.listview_teacher_student_attendance_details, null);
			holder.userimage = (ImageView) convertView.findViewById(R.id.imageView_studentImage);
			holder.attendanceStatus = (ImageView) convertView.findViewById(R.id.imageView_studentAttendance);
			holder.studentName = (TextView) convertView.findViewById(R.id.txt_studentNameTeacher);
			holder.studentId = (TextView) convertView.findViewById(R.id.txt_studentId);
			convertView.setTag(holder);
		}

		else
		{
			holder = (Holder) convertView.getTag();
		}

		try {
			holder.studentName.setText(filteredArray.getJSONObject(position).getString("stu_name"));
			//holder.studentId.setText(filteredArray.getJSONObject(position).getString("stu_id"));
			holder.studentId.setText("");
			Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+filteredArray.getJSONObject(position).getString("picture")).placeholder(R.drawable.ic_action_back).
			error(R.drawable.ic_action_back).transform(new CircleTransform()).into(holder.userimage);

			if(filteredArray.getJSONObject(position).getString("attendance").toString().matches("P"))
			{
				holder.attendanceStatus.setImageResource(R.drawable.greencircletick);
			}

			else
				if(filteredArray.getJSONObject(position).getString("attendance").toString().matches("A"))
				{
					holder.attendanceStatus.setImageResource(R.drawable.crossredcircle);
				}
			
				else
					if(filteredArray.getJSONObject(position).getString("attendance").toString().matches("L"))
					{
						holder.attendanceStatus.setImageResource(R.drawable.leaveicon);
					}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}

	static class Holder
	{
		ImageView userimage,attendanceStatus;
		TextView studentName,studentId;
	}

	@Override
	public Filter getFilter() {
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
						if(teacherStudentAttendanceArray1.getJSONObject(i).getString("stu_name").toLowerCase().contains(constraint.toString().toLowerCase()))
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
