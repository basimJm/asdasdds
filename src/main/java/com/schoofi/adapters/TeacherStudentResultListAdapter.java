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

public class TeacherStudentResultListAdapter extends BaseAdapter{
	
	private Context context;
    private JSONArray teacherStudentResultDetailsArray;
    
    public TeacherStudentResultListAdapter(Context context,JSONArray teacherStudentResultDetailsArray) {
		super();
		this.context = context;
		this.teacherStudentResultDetailsArray = teacherStudentResultDetailsArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return teacherStudentResultDetailsArray.length();
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
			convertView = layoutInflater.inflate(R.layout.listview_teacher_student_result_details, null);
			
			holder.studentName = (TextView) convertView.findViewById(R.id.txt_studentNameTeacherStudentResult);
			holder.studentPercentage = (TextView) convertView.findViewById(R.id.txt_studentPercentageTeacherStudentResult);
			holder.studentImage = (ImageView) convertView.findViewById(R.id.imageView_studentImageTeacherStudentResult);
			convertView.setTag(holder);
		}
		
		else
		{
		    holder = (Holder) convertView.getTag();
		}
		
		try {
			float percentage;
			String grade;
			percentage = Float.parseFloat(teacherStudentResultDetailsArray.getJSONObject(position).getString("percent"));
			if(percentage>90)
			{
				grade = "A1";
			}
			
			else
				if(percentage>=80 && percentage<=90)
				{
					grade = "A2";
				}
			
				else
					if(percentage>=70 && percentage<80)
					{
						grade = "B1";
					}
			
					else
						if(percentage>=60 && percentage<70)
						{
							grade = "B2";
						}
			
						else
							if(percentage>=46 && percentage<60)
							{
								grade = "C";
							}
							else
								if(percentage>=33 && percentage<46)
								{
									grade = "D";
								}
								else
								{
									grade = "E";
								}
			holder.studentName.setText(teacherStudentResultDetailsArray.getJSONObject(position).getString("stu_name"));
			holder.studentPercentage.setText(teacherStudentResultDetailsArray.getJSONObject(position).getString("percent")+" ("+grade+")");
			Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+teacherStudentResultDetailsArray.getJSONObject(position).getString("picture")).placeholder(R.drawable.ic_action_person).
			error(R.drawable.ic_action_person).transform(new CircleTransform()).into(holder.studentImage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class Holder
	{
		TextView studentName,studentPercentage;
		ImageView studentImage;
	}

}
