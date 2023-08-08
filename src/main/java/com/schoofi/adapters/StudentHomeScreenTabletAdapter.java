package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.CircularTextView;
import com.schoofi.utils.Preferences;

import java.util.ArrayList;

public class StudentHomeScreenTabletAdapter extends BaseAdapter {

	Context context;
	ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
	int []colorId;
	ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
	private static LayoutInflater inflater = null;
	String fontPath = "fonts/Asap-Regular.otf";
	
	
	public StudentHomeScreenTabletAdapter(Context context, ArrayList<Integer> studentHomeScreenIconFinal1, ArrayList<String> studentHomeScreenIconNamefinal2, int []Colors)
	{
		this.context = context;
		this.studentHomeScreenIconFinal = studentHomeScreenIconFinal1;
		this.studentHomeScreenIconNamefinal = studentHomeScreenIconNamefinal2;
		colorId = Colors;
		inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentHomeScreenIconFinal.size();
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
		Holder holder = new Holder();
		
		View rowView;
		
		
		rowView = inflater.inflate(R.layout.student_home_screen_tablet, null);
		holder.imageView = (ImageView) rowView.findViewById(R.id.studentHomeScreenImageView);
		holder.imageText = (TextView) rowView.findViewById(R.id.text_home);
		holder.linearLayout = (LinearLayout) rowView.findViewById(R.id.linear_student_home_grid_layout);
		holder.circularTextView = (CircularTextView) rowView.findViewById(R.id.circularTextView);
		//holder.imageText.setText("Category");
		holder.imageView.setImageResource(studentHomeScreenIconFinal.get(position));
		holder.imageText.setText(studentHomeScreenIconNamefinal.get(position));
		if(studentHomeScreenIconNamefinal.get(position).matches("LEAVE REQUEST"))
		{
			Preferences.getInstance().loadPreference(context);
			if(Preferences.getInstance().userRoleId.matches("4"))
			{
				holder.circularTextView.setText(Preferences.getInstance().teacherBadgeCount);
				holder.circularTextView.setVisibility(View.VISIBLE);
			}

			else
			{
				holder.circularTextView.setVisibility(View.GONE);
			}
		}

		else
		{
			holder.circularTextView.setVisibility(View.GONE);
		}
		holder.linearLayout.setBackgroundColor(colorId[position]);
		
		return rowView;
	}
	
	static class Holder
	{
	 ImageView imageView;
	 TextView imageText;
		CircularTextView circularTextView;
	 LinearLayout linearLayout;
	 
	}
	
	

}




