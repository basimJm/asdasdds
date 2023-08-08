package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

public class StudentResultListAdapter extends BaseAdapter{
	
	private Context context;
    private JSONArray studentResultArray;
    
    public int totalObtained=0;
    public int maxMarks=0;
    public StudentResultListAdapter(Context context,JSONArray studentResultArray) {
		super();
		this.context = context;
		this.studentResultArray = studentResultArray;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentResultArray.length();
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
			Preferences.getInstance().loadPreference(context);
			if(Preferences.getInstance().schoolType.matches("College")) {
				convertView = layoutInflater.inflate(R.layout.stude_result_college_result, null);
			}
			else
			{
				convertView = layoutInflater.inflate(R.layout.listview_student_result, null);
			}
			holder.studentMarks = (TextView) convertView.findViewById(R.id.text_student_marks);
			holder.subjectName = (TextView) convertView.findViewById(R.id.text_student_exam);
			holder.weightedMarks = (TextView) convertView.findViewById(R.id.text_student_marks1);
			holder.view1 =  convertView.findViewById(R.id.view1);
			holder.view2 = convertView.findViewById(R.id.view2);
			
			convertView.setTag(holder);
		}
		
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			holder.subjectName.setText(studentResultArray.getJSONObject(position).getString("subject"));

			if(studentResultArray.getJSONObject(0).getString("display_option").matches("G"))
			{
				holder.view2.setVisibility(View.INVISIBLE);
				holder.studentMarks.setVisibility(View.INVISIBLE);
				holder.weightedMarks.setText(studentResultArray.getJSONObject(position).getString("grade"));
			}

			else
				if(studentResultArray.getJSONObject(0).getString("display_option").matches("M"))
				{
					if(studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("actual"))
					{
						holder.weightedMarks.setVisibility(View.INVISIBLE);
						holder.view2.setVisibility(View.INVISIBLE);
						holder.studentMarks.setVisibility(View.VISIBLE);
						holder.studentMarks.setText(studentResultArray.getJSONObject(position).getString("obtained_marks")+"/"+studentResultArray.getJSONObject(position).getString("max_marks"));
					}

					else
						if(studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("weighted"))
						{
							holder.weightedMarks.setVisibility(View.VISIBLE);
							holder.view2.setVisibility(View.INVISIBLE);
							holder.studentMarks.setVisibility(View.INVISIBLE);
							holder.weightedMarks.setText(studentResultArray.getJSONObject(position).getString("obtained_weight_marks")+"/"+studentResultArray.getJSONObject(position).getString("weighted_marks"));
						}

						else
						{
							holder.weightedMarks.setVisibility(View.VISIBLE);
							holder.view2.setVisibility(View.VISIBLE);
							holder.studentMarks.setVisibility(View.VISIBLE);
							holder.studentMarks.setText(studentResultArray.getJSONObject(position).getString("obtained_marks")+"/"+studentResultArray.getJSONObject(position).getString("max_marks"));
							holder.weightedMarks.setText(studentResultArray.getJSONObject(position).getString("obtained_weight_marks")+"/"+studentResultArray.getJSONObject(position).getString("weighted_marks"));
						}
				}

				else
					if(studentResultArray.getJSONObject(0).getString("display_option").matches("GM"))
					{
						if(studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("actual"))
						{
							holder.weightedMarks.setVisibility(View.VISIBLE);
							holder.view2.setVisibility(View.VISIBLE);
							holder.studentMarks.setVisibility(View.VISIBLE);
							holder.weightedMarks.setText(studentResultArray.getJSONObject(position).getString("grade"));
							holder.studentMarks.setText(studentResultArray.getJSONObject(position).getString("obtained_marks")+"/"+studentResultArray.getJSONObject(position).getString("max_marks"));
						}

						else
						if(studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("weighted"))
						{
							holder.weightedMarks.setVisibility(View.VISIBLE);
							holder.view2.setVisibility(View.INVISIBLE);
							holder.studentMarks.setVisibility(View.INVISIBLE);
							holder.weightedMarks.setText(studentResultArray.getJSONObject(position).getString("obtained_weight_marks")+"/"+studentResultArray.getJSONObject(position).getString("weighted_marks")+" ("+studentResultArray.getJSONObject(position).getString("grade")+")");
						}

						else
						{
							holder.weightedMarks.setVisibility(View.VISIBLE);
							holder.view2.setVisibility(View.VISIBLE);
							holder.studentMarks.setVisibility(View.VISIBLE);
							holder.studentMarks.setText(studentResultArray.getJSONObject(position).getString("obtained_marks")+"/"+studentResultArray.getJSONObject(position).getString("max_marks"));
							holder.weightedMarks.setText(studentResultArray.getJSONObject(position).getString("obtained_weight_marks")+"/"+studentResultArray.getJSONObject(position).getString("weighted_marks")+" ("+studentResultArray.getJSONObject(position).getString("grade")+")");
						}
					}

				

			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class Holder
	{
		TextView subjectName,studentMarks,weightedMarks;
		View view1,view2;
	}

}
